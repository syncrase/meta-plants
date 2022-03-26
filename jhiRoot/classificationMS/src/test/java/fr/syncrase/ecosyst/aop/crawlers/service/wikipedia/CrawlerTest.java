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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.AtomicClassificationNom.getAtomicClassificationNomTreeSet;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClassificationMsApp.class)
public class CrawlerTest {

    WikipediaCrawler wikipediaCrawler;

    @Autowired
    private ClassificationNomRepository classificationNomRepository;

    @Autowired
    private CronquistService cronquistService;

    public CrawlerTest() {
        this.wikipediaCrawler = new WikipediaCrawler(cronquistService);
    }

    @Test
    public void uneErreurEvidenteDansLaClassificationNEstPasPriseEnCompte() {
        // Le genre contient Gentianaceae mais c'est une famille, le genre est ignoré (à voir avec le scraper)
        CronquistClassificationBranch classification;
        try {
            String wiki = "https://fr.wikipedia.org/wiki/Monodiella";
            //         => Le genre monodiella ne possède pas de genre
            classification = wikipediaCrawler.scrapWiki(wiki);
            // TODO le save est inutile, je ne veux tester que le crawler
//            Collection<CronquistRank> monodiellaRanks = cronquistService.saveCronquist(classification, wiki);
//            LinkedMap<CronquistTaxonomikRanks, CronquistRank> monodiellaClassification = utils.transformToMapOfRanksByName(monodiellaRanks);
//
//            Set<String> nomsDuGenreMonodiella = utils.getNamesFromMapRank(monodiellaClassification, CronquistTaxonomikRanks.GENRE);
//            assertEquals("Le genre monodiella ne doit contenir qu'un seul nom", 1, nomsDuGenreMonodiella.size());
//            assertTrue("Le genre monodiella doit être 'Monodiella' pas 'Gentianaceae'", nomsDuGenreMonodiella.contains("Monodiella"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
