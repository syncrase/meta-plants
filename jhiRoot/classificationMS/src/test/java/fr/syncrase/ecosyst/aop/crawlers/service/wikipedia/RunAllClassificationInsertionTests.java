package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.crawler.CrawlerTest;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.insertion.BasicInsertionTest;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.merge.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    BasicInsertionTest.class,
    BasicSynonymAdditionTest.class,
    CrawlerTest.class,
    DoubleWaySynonymTest.class,
    LeRangScrappeFaitEvoluerLaClassificationExistanteTest.class,
    DeuxClassificationsExistantesSontMergeesParLAjoutDUnRangTest.class,
    AjoutDeSynonymesEtSuppressionDeRangDeLiaisonsTest.class,// DOIT suivre le précédent, car modifie les données et fait planter un test TODO removeAllDataAfterEachTest
    //    ScrapAndInsertClassificationIntegrationTest.class,
})
public class RunAllClassificationInsertionTests {
}

