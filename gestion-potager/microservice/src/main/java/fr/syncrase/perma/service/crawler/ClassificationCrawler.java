package fr.syncrase.perma.service.crawler;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import fr.syncrase.perma.domain.Classification;

public class ClassificationCrawler {

	List<Classification> classificationsCrawlees;

	public ClassificationCrawler() throws IOException {

		Document page = Jsoup.connect("https://www.aujardin.info/plantes/").get();
		Elements sectionsPrincipales = page.select("div.items a[href]");

	}

	public List<Classification> getClassificationsCrawlees() {
		return classificationsCrawlees;
	}

}
