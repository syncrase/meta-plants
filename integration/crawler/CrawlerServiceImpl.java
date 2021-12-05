package fr.syncrase.perma.service.crawler;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.syncrase.perma.domain.Classification;
import fr.syncrase.perma.domain.Plante;
import fr.syncrase.perma.service.crawler.aujardin.AuJardinCrawler;
import fr.syncrase.perma.service.mapper.PlanteMapper;

@Service
@Transactional
public class CrawlerServiceImpl implements CrawlerService {

	private final PlanteMapper planteMapper;

	public CrawlerServiceImpl(PlanteMapper planteMapper) {
		this.planteMapper = planteMapper;
	}

	@Override
	public List<Plante> crawl() {
		List<Plante> plantesCrawlees = new AuJardinCrawler().getPlantesCrawlees();

//		return planteMapper.toDto(plantesCrawlees);
		return plantesCrawlees;
	}

	@Override
	public List<Classification> crawlClassification() {
//		List<Classification> classificationsCrawlees = new ClassificationCrawler().getClassificationsCrawlees();

//		return planteMapper.toDto(plantesCrawlees);
//		return classificationsCrawlees;
		return null;
	}

}
