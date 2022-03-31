package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.merge;

import fr.syncrase.ecosyst.ClassificationMsApp;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.TestUtils;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.entities.AtomicCronquistRank;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.CronquistClassificationBranch;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.crawler.WikipediaCrawler;
import fr.syncrase.ecosyst.domain.CronquistRank;
import fr.syncrase.ecosyst.domain.enumeration.CronquistTaxonomikRanks;
import org.apache.commons.collections4.map.LinkedMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClassificationMsApp.class)
public class DeuxClassificationsExistantesSontMergeeParLAjoutDUnRangTest {

    WikipediaCrawler wikipediaCrawler;

    TestUtils utils = new TestUtils();

    @Autowired
    private CronquistService cronquistService;

    public DeuxClassificationsExistantesSontMergeeParLAjoutDUnRangTest() {
        this.wikipediaCrawler = new WikipediaCrawler(cronquistService);
    }

    @Test
    public void mergeDesBranchesSimples() {
        try {
            CronquistClassificationBranch classification;
            // Règne 	Plantae
            //Sous-règne 	Tracheobionta
            //Division 	Magnoliophyta
            //Classe 	Magnoliopsida
            //Sous-classe 	Rosidae
            //Ordre 	Santalales
            //Famille 	Santalaceae
            //Genre Arjona
            String wiki = "https://fr.wikipedia.org/wiki/Arjona";
            classification = wikipediaCrawler.scrapWiki(wiki);
            Collection<CronquistRank> arjonaRanks = cronquistService.saveCronquist(classification, wiki);
            LinkedMap<CronquistTaxonomikRanks, CronquistRank> arjonaClassification = utils.transformToMapOfRanksByName(arjonaRanks);

            // Les plantes suivantes appartiennent à la sous-classe des Rosidae, mais on ne le sait pas pour atalaya. On le découvre quand on enregistre Cossinia
            // Règne 	Plantae
            //Division 	Magnoliophyta
            //Classe 	Magnoliopsida
            //Ordre 	Sapindales
            //Famille 	Sapindaceae
            //Genre Atalaya
            wiki = "https://fr.wikipedia.org/wiki/Atalaya_(genre)";
            classification = wikipediaCrawler.scrapWiki(wiki);
            Collection<CronquistRank> atalayaRanks = cronquistService.saveCronquist(classification, wiki);
            LinkedMap<CronquistTaxonomikRanks, CronquistRank> atalayaClassification = utils.transformToMapOfRanksByName(atalayaRanks);
            // Lors de cet ajout : le rang de liaison sous-règne prend le nom tracheobionta
            assertEquals("Le sous-règne tracheobionta doit avoir été ajoutée dans la classification de atalaya",
                         atalayaClassification.get(CronquistTaxonomikRanks.SOUSREGNE).getId(),
                         arjonaClassification.get(CronquistTaxonomikRanks.SOUSREGNE).getId()
                        );
            for (CronquistTaxonomikRanks rank : CronquistTaxonomikRanks.values()) {
                assertEquals("De la classe au règne les deux classifications doivent être égales",
                             atalayaClassification.get(rank).getId(),
                             arjonaClassification.get(rank).getId()
                            );
                if (rank.ordinal() >= CronquistTaxonomikRanks.CLASSE.ordinal()) {
                    break;
                }
            }

            // Règne 	Plantae
            //Sous-règne 	Tracheobionta
            //Division 	Magnoliophyta
            //Classe 	Magnoliopsida
            //Sous-classe 	Rosidae
            //Ordre 	Sapindales
            //Famille 	Sapindaceae
            //Genre Cossinia
            wiki = "https://fr.wikipedia.org/wiki/Cossinia";
            classification = wikipediaCrawler.scrapWiki(wiki);
            cronquistService.saveCronquist(classification, wiki);
            // TODO test que les rang de liaison supprimés le sont bien
            Long atalayaId = atalayaClassification.get(atalayaClassification.lastKey()).getId();
            CronquistClassificationBranch newAtalayaClassification = cronquistService.getClassificationById(atalayaId);
            Long arjonaId = arjonaClassification.get(arjonaClassification.lastKey()).getId();
            CronquistClassificationBranch newArjonaClassification = cronquistService.getClassificationById(arjonaId);
            Long arjonaSousClasse = newArjonaClassification.getRang(CronquistTaxonomikRanks.SOUSCLASSE).getId();
            Long atalayaSousClasse = newAtalayaClassification.getRang(CronquistTaxonomikRanks.SOUSCLASSE).getId();
            assertEquals("La sous-classe rosidae doit avoir été ajoutée dans la classification d'atalaya",
                         atalayaSousClasse,
                         arjonaSousClasse
                        );
            assertEquals("La sous-classe rosidae ne possède qu'un seul nom (pas de nom de liaison superflue)", 1, newAtalayaClassification.getRang(CronquistTaxonomikRanks.SOUSCLASSE).getNoms().size());

            Set<AtomicCronquistRank> taxonsOfRosidae = cronquistService.getTaxonsOf(arjonaSousClasse);
            assertEquals(
                "Rosidae doit posséder deux taxons de liaison (vers Santatales et vers Sapindales)",
                2,
                taxonsOfRosidae.size()
                        );
            //            CronquistClassificationBranch sousClasseBeforeMerge = cronquistService.getClassificationById(arjonaClassification.get(CronquistTaxonomikRanks.SOUSCLASSE).getId());
            //            assertNull("La sous-classe d'arjona doit avoir été supprimée car mergée avec le rang de liaison d'Atalaya", sousClasseBeforeMerge);


        } catch (IOException e) {
            fail("unable to scrap wiki : " + e.getMessage());
        }

    }
}
