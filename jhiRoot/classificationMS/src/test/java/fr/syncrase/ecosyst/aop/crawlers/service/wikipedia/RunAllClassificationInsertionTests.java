package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.crawler.CrawlerTest;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.insertion.BasicInsertionTest;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.merge.BasicSynonymAdditionTest;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.merge.DoubleWaySynonymTest;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.merge.AjoutDeSynonymesEtSuppressionDeRangDeLiaisonsTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    BasicInsertionTest.class,
    BasicSynonymAdditionTest.class,
    CrawlerTest.class,
    DoubleWaySynonymTest.class,
    AjoutDeSynonymesEtSuppressionDeRangDeLiaisonsTest.class,
//    ScrapAndInsertClassificationIntegrationTest.class,
})
public class RunAllClassificationInsertionTests {
}

