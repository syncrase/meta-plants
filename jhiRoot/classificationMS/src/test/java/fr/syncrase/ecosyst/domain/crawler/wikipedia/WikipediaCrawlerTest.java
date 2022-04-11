package fr.syncrase.ecosyst.domain.crawler.wikipedia;

import fr.syncrase.ecosyst.domain.classification.CronquistClassificationBranch;
import fr.syncrase.ecosyst.domain.classification.entities.atomic.AtomicClassificationNom;
import fr.syncrase.ecosyst.domain.classification.enumeration.RankName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class WikipediaCrawlerTest {


    private final WikipediaCrawler wikipediaCrawler;

    public WikipediaCrawlerTest() {
        this.wikipediaCrawler = new WikipediaCrawler(null);
    }

    @Test
    void crawlAllWikipedia() {
        assertTrue(true, "true == true");
    }

    @Test
    void findWiki() {
        assertTrue(true, "true == true");
    }

    @Test
    void testCrawlAllWikipedia() {
        assertTrue(true, "true == true");
    }

    @Test
    void scrapAndSaveWikiList() {
        assertTrue(true, "true == true");
    }

    @Test
    void extractClassification() {
        assertTrue(true, "true == true");
    }

    @Test
    void extractClassificationFromWiki() {
        try {
            CronquistClassificationBranch cronquistClassificationBranch = wikipediaCrawler.extractClassificationFromWiki("https://fr.wikipedia.org/wiki/Angiosperme");
            assertNotNull(cronquistClassificationBranch, "La classification ne doit pas être nulle");
            assertTrue(cronquistClassificationBranch.getRang(RankName.REGNE).doTheRankHasOneOfTheseNames(Set.of(new AtomicClassificationNom().nomFr("Plantae"))));
            assertTrue(cronquistClassificationBranch.getRang(RankName.SOUSREGNE).doTheRankHasOneOfTheseNames(Set.of(new AtomicClassificationNom().nomFr("Tracheophyta"))));
            assertTrue(cronquistClassificationBranch.getRang(RankName.EMBRANCHEMENT).doTheRankHasOneOfTheseNames(Set.of(new AtomicClassificationNom().nomFr("Magnoliophyta"))));
            assertEquals(cronquistClassificationBranch.getRangDeBase().getRankName(), RankName.EMBRANCHEMENT, "Le rang de base du rang angiosperme doit être embranchement");
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(true, "true == true");
    }
}
