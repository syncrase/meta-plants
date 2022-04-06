package fr.syncrase.ecosyst.service.crawler;

import fr.syncrase.ecosyst.service.classification.CronquistService;
import fr.syncrase.ecosyst.domain.crawler.wikipedia.WikipediaCrawler;
import org.springframework.stereotype.Service;

@Service
public class ClassificationCrawlerService {

    private final CronquistService cronquistService;

    public ClassificationCrawlerService(CronquistService cronquistService) {
        this.cronquistService = cronquistService;
    }

    public void crawlAllWikipedia() {
        WikipediaCrawler wikipediaCrawler = new WikipediaCrawler(cronquistService);
        wikipediaCrawler.crawlAllWikipedia();

    }

}
