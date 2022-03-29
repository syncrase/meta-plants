package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    BasicInsertionTest.class,
    BasicSynonymAdditionTest.class,
    CrawlerTest.class,
    DoubleWaySynonymTest.class,
    RangDeLiaisonDevientSignificatifTest.class,
//    ScrapAndInsertClassificationIntegrationTest.class,
})
public class RunAllClassificationInsertionTests {
}

