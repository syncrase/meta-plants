package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.crawler;

import fr.syncrase.ecosyst.ClassificationMsApp;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.CronquistClassificationBranch;
import fr.syncrase.ecosyst.domain.IClassificationNom;
import fr.syncrase.ecosyst.domain.enumeration.RankName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClassificationMsApp.class)
public class CrawlerTest {

    WikipediaCrawler wikipediaCrawler;

    public CrawlerTest() {
        this.wikipediaCrawler = new WikipediaCrawler(null);
    }

    @Test
    public void uneErreurEvidenteDansLaClassificationNEstPasPriseEnCompte() {
        // Le genre contient Gentianaceae mais c'est une famille, le genre est ignoré (à voir avec le scraper)
        CronquistClassificationBranch classification;
        try {
            String wiki = "https://fr.wikipedia.org/wiki/Monodiella";
            classification = wikipediaCrawler.scrapWiki(wiki);

            Set<String> nomsDuGenreMonodiella = classification.getRang(RankName.GENRE).getNomsWrappers().stream().map(IClassificationNom::getNomFr).collect(Collectors.toSet());
            assertEquals("Le genre monodiella ne doit contenir qu'un seul nom", 1, nomsDuGenreMonodiella.size());
            assertTrue("Le genre monodiella doit être 'Monodiella' pas 'Gentianaceae'", nomsDuGenreMonodiella.contains("Monodiella"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
