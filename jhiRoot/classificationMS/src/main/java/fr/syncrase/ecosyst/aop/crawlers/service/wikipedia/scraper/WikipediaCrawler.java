package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.scraper;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.ClassificationReconstructionException;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.CronquistClassificationBranch;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class WikipediaCrawler {

    public static final String LIST_INDICATOR_IN_URL = "Cat%C3%A9gorie";
    public static final String HREF = "href";
    public static final String LIST_SELECTOR_2 = "div.mw-category-group ul li a[href]";
    public static final String LIST_SELECTOR_1 = "div.CategoryTreeItem a[href]";
    public static final String CLASSIFICATION_SELECTOR = "div.infobox_v3.large.taxobox_v3.plante.bordered";
    public static final String MAIN_CLASSIFICATION_SELECTOR = "table.taxobox_classification caption a";
    private final Logger log = LoggerFactory.getLogger(WikipediaCrawler.class);

    private final CronquistService cronquistService;

    public WikipediaCrawler(CronquistService cronquistService) {
        this.cronquistService = cronquistService;
    }

    public void crawlAllWikipedia() {
        //            scrapWikiList(Wikipedia.scrappingBaseUrl);
        List<String> wikis = new ArrayList<>();
        wikis.add("https://fr.wikipedia.org/wiki/Arjona");// Rosidae// : merge branch
        //        wikis.add("https://fr.wikipedia.org/wiki/Atalaya_(genre)");// : merge branch
        //        wikis.add("https://fr.wikipedia.org/wiki/Cossinia");// : merge branch

        wikis.forEach(wiki -> {
            CronquistClassificationBranch classification;
            try {
                classification = scrapWiki(wiki);
                cronquistService.saveCronquist(classification, wiki);
            } catch (IOException e) {
                log.error("unable to scrap wiki : " + e.getMessage());
            }
        });




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
        log.info("All classification from Wikipedia had been scrapped");
    }

    public void scrapWikiList(String urlWikiListe) throws IOException {
        log.info("Get list from : " + urlWikiListe);
        Document doc = Jsoup.connect(urlWikiListe).get();
        Elements wikiList = doc.select(LIST_SELECTOR_1);
        if (wikiList.size() > 0) {
            log.info(wikiList.size() + " éléments de liste récupérées");
        }
        // Si 0 alors c'est peut-être un autre html/css qui est utilisé
        if (wikiList.size() == 0) {
            wikiList = doc.select(LIST_SELECTOR_2);
            log.info(wikiList.size() + " éléments de liste récupérées avec l'autre CSS");
        }
        if (wikiList.size() == 0) {
            log.error("Échec de récupération des éléments de liste de la page " + urlWikiListe);
        }

        for (Element listItem : wikiList) {
            urlWikiListe = listItem.attr(HREF);
            // Si l'url contient "Catégorie" alors la page contient une liste
            String urlWiki = Wikipedia.getValidUrl(urlWikiListe);
            if (urlWikiListe.contains(LIST_INDICATOR_IN_URL)) {
                scrapWikiList(urlWiki);
            } else {
                CronquistClassificationBranch classification = scrapWiki(urlWiki);
                // TODO ajouter dans une queue (reçue de l'extérieur)
                //                scrappedClassificationStore.add(classification);
                cronquistService.saveCronquist(classification, urlWiki);
            }
        }
    }

    public CronquistClassificationBranch scrapWiki(String urlWiki) throws IOException {
        try {
            log.info("Get classification from : " + urlWiki);
            Elements encadreTaxonomique = Jsoup.connect(urlWiki).get().select(CLASSIFICATION_SELECTOR);

            return extractPremiereClassification(encadreTaxonomique);
        } catch (SocketTimeoutException e) {
            log.error("La page Wikipedia ne répond pas {}\n. Vérifier la connexion internet!", urlWiki);
        } catch (IOException e) {
            log.error("Problème d'accès lors de l'extraction des données de la page Wikipedia {}", urlWiki);
        }
        return null;
    }

    private @Nullable CronquistClassificationBranch extractPremiereClassification(@NotNull Elements encadreTaxonomique) {
        // TODO Contient plusieurs classifications, en général Cronquist et APGN
        // TODO dans l'état actuel des choses, je ne garde que la PREMIERE section !

        switch (getTypeOfMainClassification(encadreTaxonomique)) {
            case "APG III":
                log.info("APG III to be implemented");
                break;
            case "Cronquist":
                return extractionCronquist(encadreTaxonomique);
            //                cronquistService.saveCronquist(extractionCronquist(encadreTaxonomique), urlWiki);
            //                break;
            case "No classification":
                log.info("No classification table found in this page");
                break;
            case "No Cronquist":
                log.info("Taxon inexistant en Cronquist");
                break;
            default:
                log.warn("Classification's type cannot be determined");
        }
        return null;
    }

    private @NotNull String getTypeOfMainClassification(@NotNull Elements encadreTaxonomique) {

        Elements taxoTitles = encadreTaxonomique.select(MAIN_CLASSIFICATION_SELECTOR);
        if (taxoTitles.size() == 0) {
            return "No classification";
        }
        String taxoTitle = taxoTitles.get(0)// ne contient qu'un seul titre
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

/*
    private APGIII extractionApg3(@NotNull Elements encadreTaxonomique) {
        log.info("Extract APGIII classification");
        Elements elementsDeClassification = encadreTaxonomique.select("tbody tr");
        APGIII apgiii = new APGIII();
        for (Element classificationItem : elementsDeClassification) {
            // Get th = rang taxonomique et td = taxon
//            Elements rangTaxonomiqueElement = classificationItem.select("th a");
//            String classificationItemKey = rangTaxonomiqueElement
//                .get(0)// ne contient qu'un seul titre
//                .childNode(0)// TextNode value
//                .toString();
            String classificationItemKey = selectText(classificationItem, "th a");
            // dans le cas des clades la cssQuery devient "td span a span"
            switch (classificationItemKey) {
                case "Règne":
                    log.debug("\tRègne NOT YET IMPLEMENTED");
                    break;
                case "Ordre":
                    apgiii.setOrdre(selectText(classificationItem, "td span a"));
                    break;
                case "Clade":
                    log.debug("\tClade NOT YET IMPLEMENTED");
                    break;
                case "Famille":
                    apgiii.setFamille(selectText(classificationItem, "td span a"));
                    break;
                case "Sous-famille":
                    log.debug("\tSous-famille NOT YET IMPLEMENTED");
                    break;
                case "Tribu":
                    log.debug("\tTribu NOT YET IMPLEMENTED");
                    break;
                case "Sous-tribu":
                    log.debug("\tSous-tribu NOT YET IMPLEMENTED");
                    break;
                case "":
                    log.warn("Rang taxonomique inexistant");
                    break;
                default:
                    log.warn("\t" + classificationItemKey + " ne correspond pas à du APGIII");
            }
        }
        log.info("(TO BE COMPLETED) Created APG III classification APGIII : " + apgiii);
        return apgiii;
    }*/

    private CronquistClassificationBranch extractionCronquist(@NotNull Elements encadreTaxonomique) {
        Elements tables = encadreTaxonomique.select("table.taxobox_classification");
        Element mainTable = tables.get(0);

        CronquistClassificationExtractor cronquistClassificationExtractor = new CronquistClassificationExtractor();
        CronquistClassificationBranch cronquistClassification = null;
        try {
            cronquistClassification = cronquistClassificationExtractor.getClassification(mainTable);
        } catch (ClassificationReconstructionException e) {
            log.error("Impossible d'extraire la classification de la page");
            e.printStackTrace();
        }


        log.info("Created Cronquist classification : " + cronquistClassification);
        return cronquistClassification;
    }

}
