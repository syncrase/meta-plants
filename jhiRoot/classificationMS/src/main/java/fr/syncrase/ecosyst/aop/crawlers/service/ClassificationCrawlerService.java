package fr.syncrase.ecosyst.aop.crawlers.service;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.crawler.WikipediaCrawler;
import org.springframework.stereotype.Service;

@Service
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
