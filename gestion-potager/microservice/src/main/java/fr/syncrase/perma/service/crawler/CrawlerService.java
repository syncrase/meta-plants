package fr.syncrase.perma.service.crawler;

import java.util.List;

import fr.syncrase.perma.domain.Classification;
import fr.syncrase.perma.domain.Plante;

public interface CrawlerService {

	public List<Plante> crawl();

	public List<Classification> crawlClassification();

}
