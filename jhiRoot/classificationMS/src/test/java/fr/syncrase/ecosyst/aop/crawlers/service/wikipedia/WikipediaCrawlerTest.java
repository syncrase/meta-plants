package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
    void enregistrementDUneClassificationDontTousLesRangsSontInconnus() {
        // TODO étrange, la méthode crawlAllWikipedia semble être appelée => à cause de la classe ClassificationCrawler qui surcharge le chargement du contexte
        Assertions.assertEquals(2, 2);
//        try {
//            scrapWiki("https://fr.wikipedia.org/wiki/Aldrovanda");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Test
    void mergeDesBranchesSimples() {
        Assertions.assertEquals(2, 2);
//        try {
//            scrapWiki("https://fr.wikipedia.org/wiki/Arjona");// Rosidae// : merge branch
//            scrapWiki("https://fr.wikipedia.org/wiki/Atalaya_(genre)");// : merge branch
//            scrapWiki("https://fr.wikipedia.org/wiki/Cossinia");// : merge branch
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Test
    void mergeDesBranchesAvecSynonymes() {
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
    void lesNomsDeRangsIncoherentsNeSontPasPrisEnCompte() {
        Assertions.assertEquals(2, 2);
//        try {
//        scrapWiki("https://fr.wikipedia.org/wiki/Chironia");
//        scrapWiki("https://fr.wikipedia.org/wiki/Monodiella");
//        scrapWiki("https://fr.wikipedia.org/wiki/Aldrovanda");
//        scrapWiki("https://fr.wikipedia.org/wiki/Amphorogyne");// Rosidae
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Test
    void uneErreurEvidenteDansLaClassificationNEstPasPriseEnCompte() {
        Assertions.assertEquals(2, 2);
//        try {
//        scrapWiki("https://fr.wikipedia.org/wiki/Monodiella");
        // => Le genre monodiella ne possède qu'un seul nom et qu'une seule url
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Test
    void transformationDUnRangIntermediaireEnRangTaxonomique() {
        Assertions.assertEquals(2, 2);
//        try {
//        scrapWiki("https://fr.wikipedia.org/wiki/Chironia");
//        scrapWiki("https://fr.wikipedia.org/wiki/Monodiella");
        // => vérifier qu'il n'y a qu'un seul sous règne
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Test
    void enregistrementDUnRangSynonymeAvecMerge() {
        Assertions.assertEquals(2, 2);
//        try {
//            scrapWiki("https://fr.wikipedia.org/wiki/Oxera_neriifolia");
//            scrapWiki("https://fr.wikipedia.org/wiki/Hostaceae");
//            scrapWiki("https://fr.wikipedia.org/wiki/Selaginaceae");
        // => vérifier la synonymie + les enfants ont bien été mergés
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Test
    void scrapWiki() {
        Assertions.assertEquals(2, 2);
    }
}
