package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class WikipediaCrawler {

    private final Logger log = LoggerFactory.getLogger(WikipediaCrawler.class);

    private final CronquistService cronquistService;

    public WikipediaCrawler(CronquistService cronquistService) {
        this.cronquistService = cronquistService;
    }

    @Contract(pure = true)
    static @NotNull String getValidUrl(@NotNull String scrappedUrl) {
        if (scrappedUrl.contains(("http"))) {
            return scrappedUrl;
        }
        return "https://fr.wikipedia.org" + scrappedUrl;
    }

    public void crawlAllWikipedia() {
        try {
//            scrapWikiList("https://fr.wikipedia.org/wiki/Cat%C3%A9gorie:Classification_de_Cronquist");

            scrapWiki("https://fr.wikipedia.org/wiki/Acanthaceae");
            scrapWiki("https://fr.wikipedia.org/wiki/Amphorogyne");
            scrapWiki("https://fr.wikipedia.org/wiki/Amphorogyne");// Rosidae
            scrapWiki("https://fr.wikipedia.org/wiki/Anthobolus");
            scrapWiki("https://fr.wikipedia.org/wiki/Anthobolus");// Rosidae
            scrapWiki("https://fr.wikipedia.org/wiki/Arjona");
            scrapWiki("https://fr.wikipedia.org/wiki/Arjona");// Rosidae// : merge branch
            scrapWiki("https://fr.wikipedia.org/wiki/Atalaya_(genre)");
            scrapWiki("https://fr.wikipedia.org/wiki/Atalaya_(genre)");// : merge branch
            scrapWiki("https://fr.wikipedia.org/wiki/Bois_de_Judas");
            scrapWiki("https://fr.wikipedia.org/wiki/Bois_de_Judas");// Rosidae// : merge branch
            scrapWiki("https://fr.wikipedia.org/wiki/Corylopsis");
            scrapWiki("https://fr.wikipedia.org/wiki/Corylopsis");// Synonymes
            scrapWiki("https://fr.wikipedia.org/wiki/Cossinia");
            scrapWiki("https://fr.wikipedia.org/wiki/Cossinia");// : merge branch
            scrapWiki("https://fr.wikipedia.org/wiki/Distylium");
            scrapWiki("https://fr.wikipedia.org/wiki/Distylium");// Synonymes
            scrapWiki("https://fr.wikipedia.org/wiki/Euclea");
            scrapWiki("https://fr.wikipedia.org/wiki/Exocarpos_cupressiformis");
            scrapWiki("https://fr.wikipedia.org/wiki/Hostaceae");
            scrapWiki("https://fr.wikipedia.org/wiki/Montrouziera_cauliflora");
            scrapWiki("https://fr.wikipedia.org/wiki/Oxera_neriifolia");
            scrapWiki("https://fr.wikipedia.org/wiki/Pachystachys");
            scrapWiki("https://fr.wikipedia.org/wiki/Pachystachys_coccinea");
            scrapWiki("https://fr.wikipedia.org/wiki/Pachystachys_spicata");
            scrapWiki("https://fr.wikipedia.org/wiki/Parrotia");
            scrapWiki("https://fr.wikipedia.org/wiki/Parrotia_persica");
            scrapWiki("https://fr.wikipedia.org/wiki/Parrotia_subaequalis");
            scrapWiki("https://fr.wikipedia.org/wiki/Peristrophe");
            scrapWiki("https://fr.wikipedia.org/wiki/Peristrophe_speciosa");
            scrapWiki("https://fr.wikipedia.org/wiki/Ptychospermatinae");
            scrapWiki("https://fr.wikipedia.org/wiki/Scleropyrum");
            scrapWiki("https://fr.wikipedia.org/wiki/Selaginaceae");
            scrapWiki("https://fr.wikipedia.org/wiki/Spirogardnera");
            scrapWiki("https://fr.wikipedia.org/wiki/Tetrameristaceae");

            
            /*
            TEST
            - Deux enregistrements d'un même n'enregistre qu'un seul rang
            - Enregistrement d'une espèce partageant un même rang avec une autre classification => la classification se rattache à l'existant, le rang en commun possède un enfant supplémentaire
             */
            // https://fr.wikipedia.org/wiki/Ptychospermatinae rang inférieur
            // https://fr.wikipedia.org/wiki/Forsythia_%C3%97intermedia rang inférieur
            // https://fr.wikipedia.org/wiki/Ch%C3%A8vrefeuille rang inférieur
            // TODO ajouter la classification des insectes
            // https://fr.wikipedia.org/wiki/Altise_des_tubercules Autre format pour la table de taxonomie des insectes

            // TODO ajouter le scrapping des formats des pages suivantes
            // https://fr.wikipedia.org/wiki/Cat%C3%A9gorie:Section_Cristasemen autre format de liste
            // https://fr.wikipedia.org/wiki/Cat%C3%A9gorie:Section_Donaldia
            // https://fr.wikipedia.org/wiki/Cat%C3%A9gorie:Section_Bracteibegonia
            // https://fr.wikipedia.org/wiki/Cat%C3%A9gorie:Section_Baryandra
            // https://fr.wikipedia.org/wiki/Cat%C3%A9gorie:Section_Barya
            // https://fr.wikipedia.org/wiki/Cat%C3%A9gorie:Section_Baccabegonia
            // https://fr.wikipedia.org/wiki/Cat%C3%A9gorie:Section_Apterobegonia
            // https://fr.wikipedia.org/wiki/Cat%C3%A9gorie:Section_Alicida
            // https://fr.wikipedia.org/wiki/Cat%C3%A9gorie:Section_Pilderia

            // https://fr.wikipedia.org/wiki/Cat%C3%A9gorie:Gyrostemonaceae
            // https://fr.wikipedia.org/wiki/Cat%C3%A9gorie:Rapateaceae
            // https://fr.wikipedia.org/wiki/Cat%C3%A9gorie:Columelliaceae
            // https://fr.wikipedia.org/wiki/Cat%C3%A9gorie:Clethraceae
            // https://fr.wikipedia.org/wiki/Cat%C3%A9gorie:Circaeasteraceae
            // https://fr.wikipedia.org/wiki/Cat%C3%A9gorie:Chloranthaceae
            // https://fr.wikipedia.org/wiki/Cat%C3%A9gorie:Cercidiphyllaceae
            //
            //
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("All classification from Wikipedia had been scrapped");
    }

    public void scrapWikiList(String urlWikiListe) throws IOException {
        log.info("Get list from : " + urlWikiListe);
        Document doc = Jsoup.connect(urlWikiListe).get();
        Elements wikiList = doc.select("div.CategoryTreeItem a[href]");
        if (wikiList.size() > 0) {
            log.info(wikiList.size() + " éléments de liste récupérées");
        }
        // Si 0 alors c'est peut-être un autre html/css qui est utilisé
        if (wikiList.size() == 0) {
            wikiList = doc.select("div.mw-category-group ul li a[href]");
            log.info(wikiList.size() + " éléments de liste récupérées avec l'autre CSS");
        }
        if (wikiList.size() == 0) {
            log.error("Échec de récupération des éléments de liste de la page " + urlWikiListe);
        }

        for (Element listItem : wikiList) {
            urlWikiListe = listItem.attr("href");
            // Si l'url contient "Catégorie" alors la page contient une liste
            if (urlWikiListe.contains("Cat%C3%A9gorie")) {
                scrapWikiList(getValidUrl(urlWikiListe));
            } else {
                scrapWiki(getValidUrl(urlWikiListe));
            }
        }
    }

    public void scrapWiki(String urlWiki) throws IOException {
        log.info("Get classification from : " + urlWiki);
        Elements encadreTaxonomique = Jsoup
            .connect(urlWiki)
            .get()
            .select("div.infobox_v3.large.taxobox_v3.plante.bordered");

        extractPremiereClassification(encadreTaxonomique, urlWiki);
    }

    private void extractPremiereClassification(@NotNull Elements encadreTaxonomique, String urlWiki) {
        // TODO Contient plusieurs classifications, en général Cronquist et APGN
        // TODO dans l'état actuel des choses, je ne garde que la PREMIERE section !

        switch (getTypeOfMainClassification(encadreTaxonomique)) {
            case "APG III":
                log.info("APG III to be implemented");
                break;
            case "Cronquist":
                cronquistService.saveCronquist(extractionCronquist(encadreTaxonomique), urlWiki);
                break;
            case "No classification":
                log.info("No classification table found in this page");
                break;
            case "No Cronquist":
                log.info("Taxon inexistant en Cronquist");
                break;
            default:
                log.warn("Classification's type cannot be determined");
        }
    }

    private @NotNull String getTypeOfMainClassification(@NotNull Elements encadreTaxonomique) {

        Elements taxoTitles = encadreTaxonomique
            .select("table.taxobox_classification caption a");
        if (taxoTitles.size() == 0) {
            return "No classification";
        }
        String taxoTitle = taxoTitles
            .get(0)// ne contient qu'un seul titre
            .childNode(0)// TextNode
            .toString();

        // Récupère le nom de l'encadré : ['Classification', 'Classification APG III (2009)']
        if (taxoTitle.contains("APG III")) {
            return "APG III";
        }
        if (taxoTitle.contains("Cronquist")) {
            Elements small = encadreTaxonomique.select("small");
            if (small.stream().anyMatch(el -> el.toString().contains("Taxon inexistant"))) {
                return "No Cronquist";
            }
            return "Cronquist";
        }

        // Récupère les clés de classification de la première table
        Elements tables = encadreTaxonomique.select("table.taxobox_classification");
        Element mainTable = tables.get(0);
        Elements taxoKeys = mainTable.select("tbody tr th a");
        for (Element classificationKeys : taxoKeys) {
            if (classificationKeys.text().contains("Clade")) {
                return "APG III";
            }
        }
        return "Cronquist";
    }


//    private APGIII extractionApg3(@NotNull Elements encadreTaxonomique) {
//        log.info("Extract APGIII classification");
//        Elements elementsDeClassification = encadreTaxonomique.select("tbody tr");
//        APGIII apgiii = new APGIII();
//        for (Element classificationItem : elementsDeClassification) {
//            // Get th = rang taxonomique et td = taxon
////            Elements rangTaxonomiqueElement = classificationItem.select("th a");
////            String classificationItemKey = rangTaxonomiqueElement
////                .get(0)// ne contient qu'un seul titre
////                .childNode(0)// TextNode value
////                .toString();
//            String classificationItemKey = selectText(classificationItem, "th a");
//            // dans le cas des clades la cssQuery devient "td span a span"
//            switch (classificationItemKey) {
//                case "Règne":
//                    log.debug("\tRègne NOT YET IMPLEMENTED");
//                    break;
//                case "Ordre":
//                    apgiii.setOrdre(selectText(classificationItem, "td span a"));
//                    break;
//                case "Clade":
//                    log.debug("\tClade NOT YET IMPLEMENTED");
//                    break;
//                case "Famille":
//                    apgiii.setFamille(selectText(classificationItem, "td span a"));
//                    break;
//                case "Sous-famille":
//                    log.debug("\tSous-famille NOT YET IMPLEMENTED");
//                    break;
//                case "Tribu":
//                    log.debug("\tTribu NOT YET IMPLEMENTED");
//                    break;
//                case "Sous-tribu":
//                    log.debug("\tSous-tribu NOT YET IMPLEMENTED");
//                    break;
//                case "":
//                    log.warn("Rang taxonomique inexistant");
//                    break;
//                default:
//                    log.warn("\t" + classificationItemKey + " ne correspond pas à du APGIII");
//            }
//        }
//        log.info("(TO BE COMPLETED) Created APG III classification APGIII : " + apgiii);
//        return apgiii;
//    }

    private CronquistClassification extractionCronquist(@NotNull Elements encadreTaxonomique) {
        Elements tables = encadreTaxonomique.select("table.taxobox_classification");
        Element mainTable = tables.get(0);

        CronquistClassificationExtractor cronquistClassificationExtractor = new CronquistClassificationExtractor();
        CronquistClassification cronquistClassification = cronquistClassificationExtractor.getClassification(mainTable);


        log.info("Created Cronquist classification : " + cronquistClassification);
        return cronquistClassification;
    }

}
