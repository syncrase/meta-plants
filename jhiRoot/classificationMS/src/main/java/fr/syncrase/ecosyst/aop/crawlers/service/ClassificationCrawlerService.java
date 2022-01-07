package fr.syncrase.ecosyst.aop.crawlers.service;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.WikipediaCrawler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClassificationCrawlerService {

    private final CronquistService cronquistService;

    public ClassificationCrawlerService(CronquistService cronquistService) {
        this.cronquistService = cronquistService;
    }

    public void crawl() {
        WikipediaCrawler wikipediaCrawler = new WikipediaCrawler(cronquistService);
        wikipediaCrawler.crawlAllWikipedia();

    }

}
