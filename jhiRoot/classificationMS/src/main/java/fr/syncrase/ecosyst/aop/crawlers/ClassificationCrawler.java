package fr.syncrase.ecosyst.aop.crawlers;

import fr.syncrase.ecosyst.aop.crawlers.service.ClassificationCrawlerService;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class ClassificationCrawler implements ApplicationListener<ContextRefreshedEvent> {

    final ClassificationCrawlerService crawler;

    public ClassificationCrawler(ClassificationCrawlerService crawler) {
        this.crawler = crawler;
    }

    @Override
    public void onApplicationEvent(@NotNull ContextRefreshedEvent event) {
//        crawler.crawl();
    }
}
