package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia;

import fr.syncrase.ecosyst.ClassificationMsApp;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.AtomicClassificationNom;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.AtomicCronquistRank;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.CronquistClassificationBranch;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.crawler.WikipediaCrawler;
import fr.syncrase.ecosyst.domain.ClassificationNom;
import fr.syncrase.ecosyst.domain.CronquistRank;
import fr.syncrase.ecosyst.domain.enumeration.CronquistTaxonomikRanks;
import fr.syncrase.ecosyst.repository.ClassificationNomRepository;
import org.apache.commons.collections4.map.LinkedMap;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClassificationMsApp.class)
public class RangDeLiaisonDevientSignificatifTest {

    WikipediaCrawler wikipediaCrawler;

    TestUtils utils = new TestUtils();

    @Autowired
    private ClassificationNomRepository classificationNomRepository;

    @Autowired
    private CronquistService cronquistService;

    public RangDeLiaisonDevientSignificatifTest() {
        this.wikipediaCrawler = new WikipediaCrawler(cronquistService);
    }

    @Test
    public void mergeDesBranchesSimples() {
        try {
            CronquistClassificationBranch classification;
            // Les trois plantes suivantes appartiennent à la sous-classe des Rosidae, mais on ne le sait pas pour atalaya. On le découvre quand on enregistre Cossinia
            String wiki = "https://fr.wikipedia.org/wiki/Arjona";
            classification = wikipediaCrawler.scrapWiki(wiki);
            Collection<CronquistRank> arjonaRanks = cronquistService.saveCronquist(classification, wiki);
            LinkedMap<CronquistTaxonomikRanks, CronquistRank> arjonaClassification = utils.transformToMapOfRanksByName(arjonaRanks);

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

            wiki = "https://fr.wikipedia.org/wiki/Cossinia";
            classification = wikipediaCrawler.scrapWiki(wiki);
            // Collection<CronquistRank> cossiniaRanks =
            cronquistService.saveCronquist(classification, wiki);

            Long atalayaId = atalayaClassification.get(atalayaClassification.lastKey()).getId();
            CronquistClassificationBranch newAtalayaClassification = cronquistService.getClassificationBranchOfThisRank(atalayaId);
            Long rosidaeId = arjonaClassification.get(CronquistTaxonomikRanks.SOUSCLASSE).getId();
            Long sousClasseId = newAtalayaClassification.getRang(CronquistTaxonomikRanks.SOUSCLASSE).getId();
            assertEquals("La sous-classe rosidae doit avoir été ajoutée dans la classification d'atalaya",
                         sousClasseId,
                         rosidaeId
                        );

            Set<AtomicCronquistRank> taxonsOfRosidae = cronquistService.getTaxonsOf(rosidaeId);
            assertEquals(
                "Rosidae doit posséder deux taxons de liaison (vers Santatales et vers Sapindales)",
                2,
                taxonsOfRosidae.size()
                        );
            CronquistClassificationBranch sousClasseLiaisonBeforeMerge = cronquistService.getClassificationBranchOfThisRank(atalayaClassification.get(CronquistTaxonomikRanks.SOUSCLASSE).getId());
            assertNull("La sous-classe de liaison de la classification d'atalaya doit avoir été supprimée car remplacée par un rang significatif", sousClasseLiaisonBeforeMerge);

        } catch (IOException e) {
            fail("unable to scrap wiki : " + e.getMessage());
        }

    }

    @Test
    public void transformationDUnRangDeLiaisonEnRangSignificatif() {
        CronquistClassificationBranch classification;
        try {
            String wiki = "https://fr.wikipedia.org/wiki/Chironia";
            classification = wikipediaCrawler.scrapWiki(wiki);
            Collection<CronquistRank> chironiaRanks = cronquistService.saveCronquist(classification, wiki);
            LinkedMap<CronquistTaxonomikRanks, CronquistRank> chironiaClassification = utils.transformToMapOfRanksByName(chironiaRanks);

            wiki = "https://fr.wikipedia.org/wiki/Monodiella";
            classification = wikipediaCrawler.scrapWiki(wiki);
            Collection<CronquistRank> monodiellaRanks = cronquistService.saveCronquist(classification, wiki);
            LinkedMap<CronquistTaxonomikRanks, CronquistRank> monodiellaClassification = utils.transformToMapOfRanksByName(monodiellaRanks);

            CronquistClassificationBranch classificationBranchOfChironia = cronquistService.getClassificationBranchOfThisRank(chironiaClassification.get(chironiaClassification.lastKey()).getId());
            assertEquals("Le sous règne Tracheobionta doit avoir été ajouté a chironia",
                         monodiellaClassification.get(CronquistTaxonomikRanks.SOUSREGNE).getId(),
                         classificationBranchOfChironia.getRang(CronquistTaxonomikRanks.SOUSREGNE).getId()
                        );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
