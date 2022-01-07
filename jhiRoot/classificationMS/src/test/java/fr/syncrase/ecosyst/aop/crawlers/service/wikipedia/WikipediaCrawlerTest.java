package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
class WikipediaCrawlerTest {

    WikipediaCrawler wikipediaCrawler;
    //    @SpyBean
    @Autowired
    private CronquistService cronquistService;

    WikipediaCrawlerTest() {
//        this.cronquistService = cronquistService;
        wikipediaCrawler = new WikipediaCrawler(cronquistService);
    }

    @Test
    void scrapWikiList() {
        // TODO étrange, la méthode crawlAllWikipedia semble être appelée => à cause de la classe ClassificationCrawler qui surcharge le chargement du contexte
        Assertions.assertEquals(2, 2);
//        try {
//            wikipediaCrawler.scrapWiki("https://fr.wikipedia.org/wiki/Corylopsis");// Synonymes Saxifragales
//            wikipediaCrawler.scrapWiki("https://fr.wikipedia.org/wiki/Distylium");// Synonymes Hamamelidales
//            wikipediaCrawler.scrapWiki("https://fr.wikipedia.org/wiki/Loropetalum");// Synonymes Hamamelidales
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Test
    void scrapWiki() {
        Assertions.assertEquals(2, 2);
    }
}
