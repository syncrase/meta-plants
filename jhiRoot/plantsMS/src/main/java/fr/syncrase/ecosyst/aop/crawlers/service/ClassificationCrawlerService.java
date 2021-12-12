package fr.syncrase.ecosyst.aop.crawlers.service;

import fr.syncrase.ecosyst.aop.crawlers.service.aujardin.AuJardinCrawler;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.WikipediaCrawler;
import fr.syncrase.ecosyst.domain.Classification;
import fr.syncrase.ecosyst.domain.Plante;
import fr.syncrase.ecosyst.repository.APGIIIRepository;
import fr.syncrase.ecosyst.repository.ClassificationRepository;
import fr.syncrase.ecosyst.repository.CronquistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClassificationCrawlerService {
    private final ClassificationRepository classificationRepository;
    private final CronquistRepository cronquistRepository;
    private final APGIIIRepository apgiiiRepository;

    public ClassificationCrawlerService(ClassificationRepository classificationRepository, CronquistRepository cronquistRepository, APGIIIRepository apgiiiRepository) {

        this.classificationRepository = classificationRepository;
        this.cronquistRepository = cronquistRepository;
        this.apgiiiRepository = apgiiiRepository;
    }

    public List<Plante> crawl() {
        List<Plante> plantesCrawlees = new AuJardinCrawler().getPlantesCrawlees();
        WikipediaCrawler wikipediaCrawler = new WikipediaCrawler(classificationRepository, cronquistRepository, apgiiiRepository);

        return plantesCrawlees;
    }

    public List<Classification> crawlClassification() {
        return null;
    }
}
