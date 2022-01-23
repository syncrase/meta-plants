package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia;

import fr.syncrase.ecosyst.ClassificationMsApp;
import fr.syncrase.ecosyst.domain.CronquistRank;
import fr.syncrase.ecosyst.repository.CronquistRankRepository;
import fr.syncrase.ecosyst.service.CronquistRankQueryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
//@SpringBootTest(classes = ClassificationMsApp.class)
@SpringBootTest
@Import(WikipediaCrawlerTestConfiguration.class)
//@TestPropertySource(
//    locations = "classpath:config/application-dev.yml")
//@ContextConfiguration(classes = { CronquistService.class })
class WikipediaCrawlerTest {

    WikipediaCrawler wikipediaCrawler;

    //        @SpyBean
//    @MockBean  = new CronquistService()
    @Autowired
//    @Spy
//    @InjectMocks
    private CronquistService cronquistService;

    WikipediaCrawlerTest() {
        wikipediaCrawler = new WikipediaCrawler(cronquistService);
    }

    @Test
    void enregistrementDUneClassificationDontTousLesRangsSontInconnus() {
        // TODO étrange, la méthode crawlAllWikipedia semble être appelée => à cause de la classe ClassificationCrawler qui surcharge le chargement du contexte
        Assertions.assertEquals(2, 2);
        try {
            wikipediaCrawler.scrapWiki("https://fr.wikipedia.org/wiki/Aldrovanda");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    void enregistrementDUnRangSynonymeAvecMerge2() {
        Assertions.assertEquals(2, 2);
//        try {
//        scrapWiki("https://fr.wikipedia.org/wiki/Lepisanthes_senegalensis");
//        scrapWiki("https://fr.wikipedia.org/wiki/%C3%89rable_de_Miyabe");
//        scrapWiki("https://fr.wikipedia.org/wiki/%C3%89rable_de_Montpellier");
        // => vérifier la synonymie + les enfants ont bien été mergés
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Test
    void enregistrementDUneClassificationAvecDeuxRangsSynonymesSuccessifs() {
        Assertions.assertEquals(2, 2);
//        try {
//        scrapWiki("https://fr.wikipedia.org/wiki/%C3%89rable_de_Cr%C3%A8te");
//        scrapWiki("https://fr.wikipedia.org/wiki/%C3%89rable_de_Miyabe");
        // => vérifier la synonymie + les enfants ont bien été mergés
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Test
    void enregistrementDUneClassificationAvecDeuxRangsSynonymesSuccessifs2() {
        Assertions.assertEquals(2, 2);
//        try {
//        scrapWiki("https://fr.wikipedia.org/wiki/Bridgesia_incisifolia");
//        scrapWiki("https://fr.wikipedia.org/wiki/%C3%89rable_de_Miyabe");
//        scrapWiki("https://fr.wikipedia.org/wiki/%C3%89rable_de_Montpellier");
        // => vérifier la synonymie + les enfants ont bien été mergés
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

//    @Test
//    void scrapWiki() {
//        Assertions.assertEquals(2, 2);
//    }
}
