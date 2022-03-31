package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.merge;

import fr.syncrase.ecosyst.ClassificationMsApp;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.TestUtils;
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

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClassificationMsApp.class)
public class LeRangScrappeFaitEvoluerLaClassificationExistanteTest {

    WikipediaCrawler wikipediaCrawler;

    TestUtils utils = new TestUtils();

    @Autowired
    private CronquistService cronquistService;

    public LeRangScrappeFaitEvoluerLaClassificationExistanteTest() {
        this.wikipediaCrawler = new WikipediaCrawler(cronquistService);
    }

    @Test
    public void transformationDUnRangDeLiaisonEnRangSignificatif() {
        CronquistClassificationBranch classification;
        try {
            // Règne 	Plantae
            //Division 	Magnoliophyta
            //Classe 	Magnoliopsida
            //Ordre 	Gentianales
            //Famille 	Gentianaceae
            //Genre Chironia
            String wiki = "https://fr.wikipedia.org/wiki/Chironia";
            classification = wikipediaCrawler.scrapWiki(wiki);
            Collection<CronquistRank> chironiaRanks = cronquistService.saveCronquist(classification, wiki);
            LinkedMap<CronquistTaxonomikRanks, CronquistRank> chironiaClassification = utils.transformToMapOfRanksByName(chironiaRanks);

            //Règne 	Plantae
            //Sous-règne 	Tracheobionta
            //Division 	Magnoliophyta
            //Classe 	Magnoliopsida
            //Ordre 	Gentianales
            //Genre 	Gentianaceae
            //Genre Monodiella
            wiki = "https://fr.wikipedia.org/wiki/Monodiella";
            classification = wikipediaCrawler.scrapWiki(wiki);
            Collection<CronquistRank> monodiellaRanks = cronquistService.saveCronquist(classification, wiki);
            LinkedMap<CronquistTaxonomikRanks, CronquistRank> monodiellaClassification = utils.transformToMapOfRanksByName(monodiellaRanks);

            CronquistClassificationBranch classificationBranchOfChironia = cronquistService.getClassificationById(chironiaClassification.get(chironiaClassification.lastKey()).getId());
            assertEquals("Le sous règne Tracheobionta doit avoir été ajouté a chironia",
                         monodiellaClassification.get(CronquistTaxonomikRanks.SOUSREGNE).getId(),
                         classificationBranchOfChironia.getRang(CronquistTaxonomikRanks.SOUSREGNE).getId()
                        );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
