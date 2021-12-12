package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia;

import fr.syncrase.ecosyst.domain.APGIII;
import fr.syncrase.ecosyst.domain.Classification;
import fr.syncrase.ecosyst.domain.Cronquist;
import fr.syncrase.ecosyst.repository.APGIIIRepository;
import fr.syncrase.ecosyst.repository.ClassificationRepository;
import fr.syncrase.ecosyst.repository.CronquistRepository;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class WikipediaCrawler {

    private final Logger log = LoggerFactory.getLogger(WikipediaCrawler.class);
    private final ClassificationRepository classificationRepository;
    private final CronquistRepository cronquistRepository;
    private final APGIIIRepository apgiiiRepository;

    Set<Classification> classifications = new HashSet<>();

    public WikipediaCrawler(ClassificationRepository classificationRepository, CronquistRepository cronquistRepository, APGIIIRepository apgiiiRepository) {
        this.classificationRepository = classificationRepository;
        this.cronquistRepository = cronquistRepository;
        this.apgiiiRepository = apgiiiRepository;

        try {
            scrapWikiList("https://fr.wikipedia.org/wiki/Cat%C3%A9gorie:Classification_de_Cronquist");
            // https://fr.wikipedia.org/wiki/Ptychospermatinae rang inférieur
            // https://fr.wikipedia.org/wiki/Forsythia_%C3%97intermedia rang inférieur
            // https://fr.wikipedia.org/wiki/Ch%C3%A8vrefeuille rang inférieur
            // https://fr.wikipedia.org/wiki/Altise_des_tubercules Autre format pour la table de taxonomie des insectes
            // https://fr.wikipedia.org/wiki/Cat%C3%A9gorie:Section_Cristasemen autre format de liste
            // https://fr.wikipedia.org/wiki/Cat%C3%A9gorie:Section_Donaldia
            // https://fr.wikipedia.org/wiki/Cat%C3%A9gorie:Section_Bracteibegonia
            // https://fr.wikipedia.org/wiki/Cat%C3%A9gorie:Section_Baryandra
            // https://fr.wikipedia.org/wiki/Cat%C3%A9gorie:Section_Barya
            // https://fr.wikipedia.org/wiki/Cat%C3%A9gorie:Section_Baccabegonia
            // https://fr.wikipedia.org/wiki/Cat%C3%A9gorie:Section_Apterobegonia
            // https://fr.wikipedia.org/wiki/Cat%C3%A9gorie:Section_Alicida
            // https://fr.wikipedia.org/wiki/Cat%C3%A9gorie:Gyrostemonaceae
            // https://fr.wikipedia.org/wiki/Cat%C3%A9gorie:Section_Pilderia
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

    private void scrapWikiList(String urlWikiListe) throws IOException {
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

    private void scrapWiki(String urlWiki) throws IOException {
        log.info("Get classification from : " + urlWiki);
        Elements encadreTaxonomique = Jsoup
            .connect(urlWiki)
            .get()
            .select("div.infobox_v3.large.taxobox_v3.plante.bordered");

        Classification classification = extractPremiereClassification(encadreTaxonomique);
        if (classification != null) {
            classifications.add(classification);
            insertClassification(classification);
        }
    }

    private Classification extractPremiereClassification(@NotNull Elements encadreTaxonomique) {
        // TODO Contient plusieurs classifications, en général Cronquist et APGN
        // TODO dans l'état actuel des choses, je ne garde que la PREMIERE section !

        Classification classification = new Classification();
        switch (getTypeOfMainClassification(encadreTaxonomique)) {
            case "APG III":
                classification.setApg3(extractionApg3(encadreTaxonomique));
                return classification;
            case "Cronquist":
                classification.setCronquist(extractionCronquist(encadreTaxonomique));
                return classification;
            case "No classification":
                log.info("No classification table found in this page");
                break;
            case "No Cronquist":
                log.info("Taxon inexistant en Cronquist");
                break;
            default:
                log.warn("Classification cannot be determined");
        }


        return null;
    }

    private String getTypeOfMainClassification(@NotNull Elements encadreTaxonomique) {

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

    private @NotNull Cronquist extractionCronquist(@NotNull Elements encadreTaxonomique) {
        Elements tables = encadreTaxonomique.select("table.taxobox_classification");
        Element mainTable = tables.get(0);
        Elements elementsDeClassification = mainTable.select("tbody tr");
        Cronquist cronquist = new Cronquist();
        for (Element classificationItem : elementsDeClassification) {
            setCronquistTaxonomyItemFromElement(cronquist, classificationItem);
        }

        Map<String, String> rangTaxonMap = extractionRangsTaxonomiquesInferieurs(mainTable);
        if (rangTaxonMap.keySet().size() > 0) {
            rangTaxonMap.forEach((rang, taxon) -> setCronquistTaxonomyItem(cronquist, rang, taxon));
        }

        log.info("Created Cronquist classification : " + cronquist);
        return cronquist;
    }

    private @NotNull Map<String, String> extractionRangsTaxonomiquesInferieurs(@NotNull Element mainTable) {
        Map<String, String> rangTaxonMap = new HashMap<>();
        // Ajout du rang taxonomique le plus inférieur
        Elements siblings = mainTable.siblingElements();
        // Retire les éléments qui correspondent à autre chose que le rang ou le nom du taxon
        siblings.removeIf(el ->
            el.className().equals("entete") ||
                el.className().equals("images") ||
                el.className().equals("legend") ||
                el.tagName().equals("table") ||
                el.select("p a").attr("title").contains("Classification") ||
                el.select("p a").attr("title").contains("Synonyme") ||
                el.tagName().equals("ul") ||
                el.select("p a").attr("title").contains("Statut de conservation") ||
                el.select("p sup").attr("class").equals("reference") ||
                el.select("p img").size() != 0 ||
                el.select("p").text().equals("Taxons de rang inférieur") ||
                el.select("p").text().equals("Répartition géographique") ||
                el.select("p").text().equals("(voir texte) ") ||
                el.select("div ul").size() != 0 ||
                el.select("center").size() != 0 ||
                el.select("div div img").attr("alt").equals("Attention !") ||
                el.select("p b").text().equals("DD Données insuffisantes") || //https://fr.wikipedia.org/wiki/Alisma_gramineum
                el.select("p.mw-empty-elt").size() != 0 ||
                el.select("p i").size() != 0 || //https://fr.wikipedia.org/wiki/Alternanthera_reineckii Le synonyme n'est pas dans le div synonyme
                el.select("p").text().equals("Espèces de rang inférieur") ||
                el.select("p").text().equals("Genres de rang inférieur") ||
                el.select("p br").size() != 0 ||
                el.select("div.left").size() != 0 && el.select("div.left").get(0).childrenSize() == 0 || //https://fr.wikipedia.org/wiki/Cryosophila_williamsii
                el.select("hr").size() != 0 ||
                el.select("dl").size() != 0


        );
        // Je ne souhaite que des couples (rang ; nom)
        if (siblings.size() % 2 != 0) {
            log.error("Impossible de récupérer le rang taxonomique inférieur dans cet HTML :\n" + siblings);
        } else {
            Elements rangs = siblings.select("p.bloc a");
            rangs.removeIf(rang -> rang.text().equals("CITES"));
            Elements taxons = siblings.select("div.taxobox_classification b span");
            taxons.removeIf(taxon ->
                taxon.select("span").hasClass("cite_crochet") ||
                    taxon.select("span.indicateur-langue").size() != 0
            );

            if (rangs.size() != taxons.size()) {
                log.error("Pas la même quantité de rangs et de noms de taxons récupérés. Traitement impossible");
                log.info(rangs.toString());
                log.info(taxons.toString());
            } else {
                for (int i = 0; i < rangs.size(); i++) {
                    rangTaxonMap.put(rangs.get(i).text(), taxons.get(i).text());
                }
            }
        }
        return rangTaxonMap;
    }

    private void setCronquistTaxonomyItemFromElement(Cronquist cronquist, Element classificationItem) {
        String classificationItemKey = selectText(classificationItem, "th a");
        switch (classificationItemKey) {
            case "Règne":
                cronquist.setRegne(selectText(classificationItem, "td span a"));
                break;
            case "Sous-règne":
                cronquist.setSousRegne(selectText(classificationItem, "td span a"));
                break;
            case "Division":
                cronquist.setDivision(selectText(classificationItem, "td span a"));
                break;
            case "Classe":
                cronquist.setClasse(selectText(classificationItem, "td span a"));
                break;
            case "Sous-classe":
                cronquist.setSousClasse(selectText(classificationItem, "td span a"));
                break;
            case "Ordre":
                cronquist.setOrdre(selectText(classificationItem, "td span a"));
                break;
            case "Famille":
                cronquist.setFamille(selectText(classificationItem, "td span a"));
                break;
            case "Genre":
                cronquist.setGenre(selectText(classificationItem, "td a"));
                break;
            case "Espèce":
                cronquist.setEspece(selectText(classificationItem, "td a"));
                break;
            case "":
                log.warn("Rang taxonomique inexistant");
                break;
            default:
                log.warn(classificationItemKey + " ne correspond pas à du Cronquist");

        }
    }

    /**
     * @param cronquist               side effect
     * @param classificationItemKey   rang taxonomique
     * @param classificationItemValue taxon
     */
    private void setCronquistTaxonomyItem(Cronquist cronquist, @NotNull String classificationItemKey, String classificationItemValue) {
        switch (classificationItemKey) {
            case "Règne":
                cronquist.setRegne(classificationItemValue);
                break;
            case "Sous-règne":
                cronquist.setSousRegne(classificationItemValue);
                break;
            case "Division":
                cronquist.setDivision(classificationItemValue);
                break;
            case "Classe":
                cronquist.setClasse(classificationItemValue);
                break;
            case "Sous-classe":
                cronquist.setSousClasse(classificationItemValue);
                break;
            case "Ordre":
                cronquist.setOrdre(classificationItemValue);
                break;
            case "Famille":
                cronquist.setFamille(classificationItemValue);
                break;
            case "Genre":
                cronquist.setGenre(classificationItemValue);
                break;
            case "Espèce":
                cronquist.setEspece(classificationItemValue);
                break;
            default:
                log.warn(classificationItemKey + " ne correspond pas à du Cronquist");

        }
    }

    private @NotNull APGIII extractionApg3(@NotNull Elements encadreTaxonomique) {
        log.info("Extract APGIII classification");
        Elements elementsDeClassification = encadreTaxonomique.select("tbody tr");
        APGIII apgiii = new APGIII();
        for (Element classificationItem : elementsDeClassification) {
            // Get th = rang taxonomique et td = taxon
            // TODO setClades, addClades, setSousFamille, setGenre
//            Elements rangTaxonomiqueElement = classificationItem.select("th a");
//            String classificationItemKey = rangTaxonomiqueElement
//                .get(0)// ne contient qu'un seul titre
//                .childNode(0)// TextNode value
//                .toString();
            String classificationItemKey = selectText(classificationItem, "th a");
            // TODO dans le cas des clades la cssQuery devient "td span a span"
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
    }

    /**
     * @param item     Element from which the contained string is extract
     * @param cssQuery La query DOIT retourner un unique élément
     * @return the text value of the node or an empty string
     */
    private @NotNull
    String selectText(@NotNull Element item, String cssQuery) {
        Optional<Element> el = item.select(cssQuery).stream().findFirst();
        return el.map(Element::text).orElse("");
    }

    @Contract(pure = true)
    private @NotNull String getValidUrl(String scrappedUrl) {
        return "https://fr.wikipedia.org" + scrappedUrl;
    }

    private void insertClassification(@NotNull Classification classification) {
        if (classification.getCronquist() != null) {
            cronquistRepository.save(classification.getCronquist());
        }
        if (classification.getApg3() != null) {
            apgiiiRepository.save(classification.getApg3());
        }
        classificationRepository.save(classification);
    }


}
