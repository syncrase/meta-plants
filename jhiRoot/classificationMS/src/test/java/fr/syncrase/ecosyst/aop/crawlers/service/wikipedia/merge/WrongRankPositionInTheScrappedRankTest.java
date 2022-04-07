package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.merge;

import fr.syncrase.ecosyst.ClassificationMsApp;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.TestUtils;
import fr.syncrase.ecosyst.domain.classification.CronquistClassificationBranch;
import fr.syncrase.ecosyst.domain.classification.enumeration.RankName;
import fr.syncrase.ecosyst.domain.crawler.wikipedia.WikipediaCrawler;
import fr.syncrase.ecosyst.service.classification.CronquistService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClassificationMsApp.class)
public class WrongRankPositionInTheScrappedRankTest {

    WikipediaCrawler wikipediaCrawler;

    TestUtils utils = new TestUtils();

    @Autowired
    private CronquistService cronquistService;

    public WrongRankPositionInTheScrappedRankTest() {
        this.wikipediaCrawler = new WikipediaCrawler(cronquistService);
    }

    @Test
    public void mergeDesBranchesAvecSynonymes() {
        CronquistClassificationBranch classification;
        try {
            // TODO tester le scraping de https://fr.wikipedia.org/wiki/Asparagaceae
            // Règne 	Plantae
            //Sous-règne 	Tracheobionta
            //Division 	Magnoliophyta
            //Classe 	Magnoliopsida
            //Sous-classe 	Hamamelidae
            //Ordre 	Saxifragales
            //Famille 	Hamamelidaceae
            //Genre Corylopsis
            String wiki = "https://fr.wikipedia.org/wiki/Agave_lechuguilla";// Synonymes ORDRE Saxifragales
            classification = wikipediaCrawler.scrapWiki(wiki);
            CronquistClassificationBranch agaveLechuguillaClassification = cronquistService.saveCronquist(classification, wiki);
//            Set<String> corylopsisOrdreNames = utils.getRankNames(corylopsisClassification, RankName.ORDRE);
//            assertEquals("Corylopsis ne doit contenir qu'un seul ordre", 1, corylopsisOrdreNames.size());
//            assertTrue("L'ordre de corylopsis doit être Saxifragales", corylopsisOrdreNames.contains("Saxifragales"));

            // TODO trouver une plante dont la page wiki contient un super-ordre Lilianae
            // Règne 	Plantae
            //Division 	Angiospermae
            //Classe 	Lilianae
            //Ordre 	Alismatales
            //Famille 	Alismataceae
            //Genre 	Helanthium
            //Espèce Helanthium bolivianum
            wiki = "https://fr.wikipedia.org/wiki/Helanthium_bolivianum";
            classification = wikipediaCrawler.scrapWiki(wiki);
            CronquistClassificationBranch helanthiumBolivianumClassification = cronquistService.saveCronquist(classification, wiki);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
