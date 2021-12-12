package fr.syncrase.ecosyst.aop.crawlers.service;

import fr.syncrase.ecosyst.aop.crawlers.service.aujardin.AuJardinCrawler;
import fr.syncrase.ecosyst.domain.Classification;
import fr.syncrase.ecosyst.domain.Plante;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PlanteCrawlerService {

    public PlanteCrawlerService() {

    }

    public List<Plante> crawl() {
        List<Plante> plantesCrawlees = new AuJardinCrawler().getPlantesCrawlees();

//		return planteMapper.toDto(plantesCrawlees);
        return plantesCrawlees;
    }

    public List<Classification> crawlClassification() {
//		List<Classification> classificationsCrawlees = new ClassificationCrawler().getClassificationsCrawlees();

//		return planteMapper.toDto(plantesCrawlees);
//		return classificationsCrawlees;
        return null;
    }

}
