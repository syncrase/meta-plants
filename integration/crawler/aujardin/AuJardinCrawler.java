package fr.syncrase.perma.service.crawler.aujardin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import fr.syncrase.perma.domain.Plante;

public class AuJardinCrawler {

	List<Plante> plantesCrawlees;

	public AuJardinCrawler() {

		plantesCrawlees = new ArrayList<>();
		try {
			Elements sectionsPrincipales = getAllMainSections();
			for (Element section : sectionsPrincipales) {
				Elements listePlantes = getAllPlants(section);
				for (Element lienPlante : listePlantes) {
					Plante plante = extractPlante(lienPlante);
					plantesCrawlees.add(plante);
				}
				break;// TODO supprimer après que l'extraction d'une plante soit terminée
			}

		} catch (IOException e) {
			System.out.println("Erreur");
		}
	}

	private Elements getAllPlants(Element section) throws IOException {
		Document page;
		page = Jsoup.connect(getValidUrl(section.attr("href"))).get();
		Elements listePlantes = page.select("ul.rubrique li a[href]");
		return listePlantes;
	}

	private Elements getAllMainSections() throws IOException {
		Document page = Jsoup.connect("https://www.aujardin.info/plantes/").get();
		Elements sectionsPrincipales = page.select("div.items a[href]");
		return sectionsPrincipales;
	}

	private Plante extractPlante(Element lienPlante) throws IOException {


		FichePlante fp = new FichePlante(Jsoup.connect(getValidUrl(lienPlante.attr("href"))).get());
		
//		Document page = ;
		return fp.getPlante();
	}

	private static String getValidUrl(String scrappedUrl) {
		return "https://www" + scrappedUrl.split("www")[1];
	}

	public List<Plante> getPlantesCrawlees() {
		return plantesCrawlees;
	}

}
