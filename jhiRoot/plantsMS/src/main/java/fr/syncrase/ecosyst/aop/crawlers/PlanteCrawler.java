package fr.syncrase.ecosyst.aop.crawlers;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

//@Component
public class PlanteCrawler implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

    }
}
