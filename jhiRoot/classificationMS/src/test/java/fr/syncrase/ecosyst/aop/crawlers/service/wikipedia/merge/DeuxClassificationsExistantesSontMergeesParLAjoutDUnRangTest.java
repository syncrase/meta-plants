package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.merge;

import fr.syncrase.ecosyst.ClassificationMsApp;
import fr.syncrase.ecosyst.service.classification.CronquistService;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.TestUtils;
import fr.syncrase.ecosyst.domain.classification.CronquistClassificationBranch;
import fr.syncrase.ecosyst.domain.crawler.wikipedia.WikipediaCrawler;
import fr.syncrase.ecosyst.domain.classification.entities.ICronquistRank;
import fr.syncrase.ecosyst.domain.classification.enumeration.RankName;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClassificationMsApp.class)
public class DeuxClassificationsExistantesSontMergeesParLAjoutDUnRangTest {

    WikipediaCrawler wikipediaCrawler;

    TestUtils utils = new TestUtils();

    @Autowired
    private CronquistService cronquistService;

    public DeuxClassificationsExistantesSontMergeesParLAjoutDUnRangTest() {
        this.wikipediaCrawler = new WikipediaCrawler(cronquistService);
    }

    @BeforeAll
    static void init() {

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
            CronquistClassificationBranch arjonaClassification = cronquistService.saveCronquist(classification, wiki);

            // La plante suivante appartient à la sous-classe des Rosidae, mais on ne le sait pas pour atalaya. On le découvre quand on enregistre Cossinia
            // Règne 	Plantae
            //Sous-règne 	(+Tracheobionta déduit du précédent)
            //Division 	Magnoliophyta
            //Classe 	Magnoliopsida
            //Sous-classe 	(+Rosidae déduis du suivant)
            //Ordre 	Sapindales
            //Famille 	Sapindaceae
            //Genre Atalaya
            wiki = "https://fr.wikipedia.org/wiki/Atalaya_(genre)";
            classification = wikipediaCrawler.scrapWiki(wiki);
            CronquistClassificationBranch atalayaClassification = cronquistService.saveCronquist(classification, wiki);
            // Lors de cet ajout : le rang de liaison sous-règne prend le nom tracheobionta
            assertEquals("Le sous-règne tracheobionta doit avoir été ajoutée dans la classification de atalaya",
                         atalayaClassification.getRang(RankName.SOUSREGNE).getId(),
                         arjonaClassification.getRang(RankName.SOUSREGNE).getId()
                        );

            CronquistClassificationBranch neriifoliaPartialClassification = cronquistService.getClassificationById(atalayaClassification.getRang(RankName.CLASSE).getId());
            CronquistClassificationBranch selaginaceaePartialClassification = cronquistService.getClassificationById(arjonaClassification.getRang(RankName.CLASSE).getId());
            utils.assertThatClassificationsAreTheSame(neriifoliaPartialClassification, selaginaceaePartialClassification);

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
            CronquistClassificationBranch cossiniaClassification = cronquistService.saveCronquist(classification, wiki);

            utils.checkThatEachRankOwnsAsManyUrlAsNames(cossiniaClassification);

            Long atalayaId = atalayaClassification.getRangDeBase().getId();
            CronquistClassificationBranch newAtalayaClassification = cronquistService.getClassificationById(atalayaId);
            Long arjonaId = arjonaClassification.getRangDeBase().getId();
            CronquistClassificationBranch newArjonaClassification = cronquistService.getClassificationById(arjonaId);

            Long arjonaSousClasse = newArjonaClassification.getRang(RankName.SOUSCLASSE).getId();
            Long atalayaSousClasse = newAtalayaClassification.getRang(RankName.SOUSCLASSE).getId();
            assertEquals("La sous-classe rosidae doit avoir été ajoutée dans la classification d'atalaya",
                         atalayaSousClasse,
                         arjonaSousClasse
                        );
            assertEquals("La sous-classe rosidae ne doit posséder qu'un seul nom (pas de nom de liaison superflue)", 1, newAtalayaClassification.getRang(RankName.SOUSCLASSE).getNomsWrappers().size());

            Set<ICronquistRank> taxonsOfRosidae = cronquistService.getTaxonsOf(arjonaSousClasse);
            assertEquals(
                "Rosidae doit posséder deux taxons de liaison (vers Santatales et vers Sapindales)",
                2,
                taxonsOfRosidae.size()
                        );

            CronquistClassificationBranch arjonaClassificationAfterInserts = cronquistService.getClassificationById(arjonaClassification.getRang(RankName.SOUSCLASSE).getId());
            assertNull("La sous-classe d'arjona doit avoir été supprimée car mergée avec le rang de liaison d'Atalaya", arjonaClassificationAfterInserts);

        } catch (IOException e) {
            fail("unable to scrap wiki : " + e.getMessage());
        }

    }
}
