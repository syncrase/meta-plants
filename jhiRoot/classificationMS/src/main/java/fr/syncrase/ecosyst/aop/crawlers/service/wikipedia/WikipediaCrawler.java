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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class WikipediaCrawler {

    private final Logger log = LoggerFactory.getLogger(WikipediaCrawler.class);

    private final CronquistService cronquistService;

    public WikipediaCrawler(CronquistService cronquistService) {
        this.cronquistService = cronquistService;

        try {
//            scrapWikiList("https://fr.wikipedia.org/wiki/Cat%C3%A9gorie:Classification_de_Cronquist");
            /*
             Enregistrement d'une sous-tribu
             */
            scrapWiki("https://fr.wikipedia.org/wiki/Ptychospermatinae");
            /*
            Enregistrement d'une espèce partageant la même famille avec la plante précédente
             */
            scrapWiki("https://fr.wikipedia.org/wiki/Palmier_%C3%A0_huile");

            /*
            AssertThat
            - TODO un enregistrement  n'enregistre qu'un seul élément de chaque rang
            - le rang est bien enregistré en base
            - TODO les classes sont synonymes
            - les sous classes sont égales
            - les super-ordres sont égaux
            - ainsi que les ordres et les familles
            -  TODO enregistrer une deuxième fois une espece ne duplique pas les sous rangs
             */
//            scrapWiki("https://fr.wikipedia.org/wiki/Anisoptera_(v%C3%A9g%C3%A9tal)");
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

        extractPremiereClassification(encadreTaxonomique);
//            insertSuperRegne(superRegne);
    }

    private void extractPremiereClassification(@NotNull Elements encadreTaxonomique) {
        // TODO Contient plusieurs classifications, en général Cronquist et APGN
        // TODO dans l'état actuel des choses, je ne garde que la PREMIERE section !

        switch (getTypeOfMainClassification(encadreTaxonomique)) {
            case "APG III":
                log.info("APG III to be implemented");
//                superRegne.setApg3(extractionApg3(encadreTaxonomique));
//                return superRegne;
                break;
            case "Cronquist":
                extractionCronquist(encadreTaxonomique);
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

    private void extractionCronquist(@NotNull Elements encadreTaxonomique) {
        Elements tables = encadreTaxonomique.select("table.taxobox_classification");
        Element mainTable = tables.get(0);
        Elements elementsDeClassification = mainTable.select("tbody tr");
        SuperRegneWrapper superRegneWrapper = new SuperRegneWrapper();
        // TODO je crée un nouveau super regne et je l'ajoute à la fin. (pour pouvoir rajouter un rang inférieur quand les rangs supérieur n'existe pas, je ne fais que renseigner les valeurs vides présentes)
        for (Element classificationItem : elementsDeClassification) {
            setCronquistTaxonomyItemFromElement(superRegneWrapper, classificationItem);
        }
        // TODO Je merge le superRegne obtenu avec le superRegne initial

        Map<String, String> rangTaxonMap = extractionRangsTaxonomiquesInferieurs(mainTable);
        if (rangTaxonMap.keySet().size() > 0) {
            rangTaxonMap.forEach((rang, taxon) -> setCronquistTaxonomyItem(superRegneWrapper, rang, taxon));
        }

        cronquistService.saveCronquist(superRegneWrapper);

        log.info("Created Cronquist classification : " + superRegneWrapper);
    }

//    private void saveSuperRegneWrapper(SuperRegneWrapper superRegneWrapper) {
////        SuperRegneWrapper superRegneWrapper = new SuperRegneWrapper().setCronquist(cronquist);
//        sousFormeRepository.save(superRegneWrapper.getSousForme());
//        formeRepository.save(superRegneWrapper.getForme());
//        sousVarieteRepository.save(superRegneWrapper.getSousVariete());
//        varieteRepository.save(superRegneWrapper.getVariete());
//        sousEspeceRepository.save(superRegneWrapper.getSousEspece());
//        especeRepository.save(superRegneWrapper.getEspece());
//        sousSectionRepository.save(superRegneWrapper.getSousSection());
//        sectionRepository.save(superRegneWrapper.getSection());
//        sousGenreRepository.save(superRegneWrapper.getSousGenre());
//        genreRepository.save(superRegneWrapper.getGenre());
//        sousTribuRepository.save(superRegneWrapper.getSousTribu());
//        tribuRepository.save(superRegneWrapper.getTribu());
//        sousFamilleRepository.save(superRegneWrapper.getSousFamille());
//        familleRepository.save(superRegneWrapper.getFamille());
//        superFamilleRepository.save(superRegneWrapper.getSuperFamille());
//        microOrdreRepository.save(superRegneWrapper.getMicroOrdre());
//        infraOrdreRepository.save(superRegneWrapper.getInfraOrdre());
//        sousOrdreRepository.save(superRegneWrapper.getSousOrdre());
//        ordreRepository.save(superRegneWrapper.getOrdre());
//        superOrdreRepository.save(superRegneWrapper.getSuperOrdre());
//        infraClasseRepository.save(superRegneWrapper.getInfraClasse());
//        sousClasseRepository.save(superRegneWrapper.getSousClasse());
//        classeRepository.save(superRegneWrapper.getClasse());
//        superClasseRepository.save(superRegneWrapper.getSuperClasse());
//        microEmbranchementRepository.save(superRegneWrapper.getMicroEmbranchement());
//        infraEmbranchementRepository.save(superRegneWrapper.getInfraEmbranchement());
//        sousDivisionRepository.save(superRegneWrapper.getSousDivision());
//        divisionRepository.save(superRegneWrapper.getDivision());
//        superDivisionRepository.save(superRegneWrapper.getSuperDivision());
//        infraRegneRepository.save(superRegneWrapper.getInfraRegne());
//        rameauRepository.save(superRegneWrapper.getRameau());
//        sousRegneRepository.save(superRegneWrapper.getSousRegne());
//        regneRepository.save(superRegneWrapper.getRegne());
//        superRegneRepository.save(superRegneWrapper.getSuperRegne());
//    }

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
//                el.select("p").text().equals("Espèces de rang inférieur") ||
//                el.select("p").text().equals("Genres de rang inférieur") ||
                    el.select("p").text().contains("de rang inférieur") ||
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

    /**
     * @param superRegneWrapper  side effect
     * @param classificationItem row of the taxonomy table which contains taxonomy rank and name
     */
    private void setCronquistTaxonomyItemFromElement(SuperRegneWrapper superRegneWrapper, Element classificationItem) {
        String classificationItemKey = selectText(classificationItem, "th a");
        switch (classificationItemKey) {
            case "Super-Règne":
                superRegneWrapper.getSuperRegne().setNomFr(selectText(classificationItem, "td span a"));
                break;
            case "Règne":
                superRegneWrapper.getRegne().setNomFr(selectText(classificationItem, "td span a"));
                break;
            case "Sous-règne":
                superRegneWrapper.getSousRegne().setNomFr(selectText(classificationItem, "td span a"));
                break;
            case "Rameau":
                superRegneWrapper.getRameau().setNomFr(selectText(classificationItem, "td span a"));
                break;
            case "Infra-règne":
                superRegneWrapper.getInfraRegne().setNomFr(selectText(classificationItem, "td span a"));
                break;
            case "Super-division":
                superRegneWrapper.getSuperDivision().setNomFr(selectText(classificationItem, "td span a"));
                break;
            case "Division":
                superRegneWrapper.getDivision().setNomFr(selectText(classificationItem, "td span a"));
                break;
            case "Sous-division":
                superRegneWrapper.getSousDivision().setNomFr(selectText(classificationItem, "td span a"));
                break;
            case "Infra-embranchement":
                superRegneWrapper.getInfraEmbranchement().setNomFr(selectText(classificationItem, "td span a"));
                break;
            case "Micro-embranchement":
                superRegneWrapper.getMicroEmbranchement().setNomFr(selectText(classificationItem, "td span a"));
                break;
            case "Super-classe":
                superRegneWrapper.getSuperClasse().setNomFr(selectText(classificationItem, "td span a"));
                break;
            case "Classe":
                superRegneWrapper.getClasse().setNomFr(selectText(classificationItem, "td span a"));
                break;
            case "Sous-classe":
                superRegneWrapper.getSousClasse().setNomFr(selectText(classificationItem, "td span a"));
                break;
            case "Infra-classe":
                superRegneWrapper.getInfraClasse().setNomFr(selectText(classificationItem, "td span a"));
                break;
            case "Super-ordre":
                superRegneWrapper.getSuperOrdre().setNomFr(selectText(classificationItem, "td span a"));
                break;
            case "Ordre":
                superRegneWrapper.getOrdre().setNomFr(selectText(classificationItem, "td span a"));
                break;
            case "Sous-ordre":
                superRegneWrapper.getSousOrdre().setNomFr(selectText(classificationItem, "td span a"));
                break;
            case "Infra-ordre":
                superRegneWrapper.getInfraOrdre().setNomFr(selectText(classificationItem, "td span a"));
                break;
            case "Micro-ordre":
                superRegneWrapper.getMicroOrdre().setNomFr(selectText(classificationItem, "td span a"));
                break;
            case "Super-famille":
                superRegneWrapper.getSuperFamille().setNomFr(selectText(classificationItem, "td span a"));
                break;
            case "Famille":
                superRegneWrapper.getFamille().setNomFr(selectText(classificationItem, "td span a"));
                break;
            case "Sous-famille":
                superRegneWrapper.getSousFamille().setNomFr(selectText(classificationItem, "td span a"));
                break;
            case "Tribu":
                superRegneWrapper.getTribu().setNomFr(selectText(classificationItem, "td a"));
                break;
            case "Sous-tribu":
                superRegneWrapper.getSousTribu().setNomFr(selectText(classificationItem, "td a"));
                break;
            case "Genre":
                superRegneWrapper.getGenre().setNomFr(selectText(classificationItem, "td a"));
                break;
            case "Sous-genre":
                superRegneWrapper.getSousGenre().setNomFr(selectText(classificationItem, "td a"));
                break;
            case "Section":
                superRegneWrapper.getSection().setNomFr(selectText(classificationItem, "td a"));
                break;
            case "Sous-section":
                superRegneWrapper.getSousSection().setNomFr(selectText(classificationItem, "td a"));
                break;
            case "Espèce":
                superRegneWrapper.getEspece().setNomFr(selectText(classificationItem, "td a"));
                break;
            case "Sous-espèce":
                superRegneWrapper.getSousEspece().setNomFr(selectText(classificationItem, "td a"));
                break;
            case "Variété":
                superRegneWrapper.getVariete().setNomFr(selectText(classificationItem, "td a"));
                break;
            case "Sous-variété":
                superRegneWrapper.getSousVariete().setNomFr(selectText(classificationItem, "td a"));
                break;
            case "Forme":
                superRegneWrapper.getForme().setNomFr(selectText(classificationItem, "td a"));
                break;
            case "Sous-forme":
                superRegneWrapper.getSousForme().setNomFr(selectText(classificationItem, "td a"));
                break;
            case "":
                log.warn("Rang taxonomique inexistant dans le wiki");
                break;
            default:
                log.warn(classificationItemKey + " ne correspond pas à du Cronquist ou NOT YET IMPLEMENTED");

        }
    }

    /**
     * @param superRegneWrapper       side effect
     * @param classificationItemKey   rang taxonomique
     * @param classificationItemValue taxon
     */
    private void setCronquistTaxonomyItem(SuperRegneWrapper superRegneWrapper, @NotNull String classificationItemKey, String classificationItemValue) {
        switch (classificationItemKey) {
            case "Super-Règne":
                superRegneWrapper.getSuperRegne().setNomFr(classificationItemValue);
                break;
            case "Règne":
                superRegneWrapper.getRegne().setNomFr(classificationItemValue);
                break;
            case "Sous-règne":
                superRegneWrapper.getSousRegne().setNomFr(classificationItemValue);
                break;
            case "Rameau":
                superRegneWrapper.getRameau().setNomFr(classificationItemValue);
                break;
            case "Infra-règne":
                superRegneWrapper.getInfraRegne().setNomFr(classificationItemValue);
                break;
            case "Super-division":
                superRegneWrapper.getSuperDivision().setNomFr(classificationItemValue);
                break;
            case "Division":
                superRegneWrapper.getDivision().setNomFr(classificationItemValue);
                break;
            case "Sous-division":
                superRegneWrapper.getSousDivision().setNomFr(classificationItemValue);
                break;
            case "Infra-embranchement":
                superRegneWrapper.getInfraEmbranchement().setNomFr(classificationItemValue);
                break;
            case "Micro-embranchement":
                superRegneWrapper.getMicroEmbranchement().setNomFr(classificationItemValue);
                break;
            case "Super-classe":
                superRegneWrapper.getSuperClasse().setNomFr(classificationItemValue);
                break;
            case "Classe":
                superRegneWrapper.getClasse().setNomFr(classificationItemValue);
                break;
            case "Sous-classe":
                superRegneWrapper.getSousClasse().setNomFr(classificationItemValue);
                break;
            case "Infra-classe":
                superRegneWrapper.getInfraClasse().setNomFr(classificationItemValue);
                break;
            case "Super-ordre":
                superRegneWrapper.getSuperOrdre().setNomFr(classificationItemValue);
                break;
            case "Ordre":
                superRegneWrapper.getOrdre().setNomFr(classificationItemValue);
                break;
            case "Sous-ordre":
                superRegneWrapper.getSousOrdre().setNomFr(classificationItemValue);
                break;
            case "Infra-ordre":
                superRegneWrapper.getInfraOrdre().setNomFr(classificationItemValue);
                break;
            case "Micro-ordre":
                superRegneWrapper.getMicroOrdre().setNomFr(classificationItemValue);
                break;
            case "Super-famille":
                superRegneWrapper.getSuperFamille().setNomFr(classificationItemValue);
                break;
            case "Famille":
                superRegneWrapper.getFamille().setNomFr(classificationItemValue);
                break;
            case "Sous-famille":
                superRegneWrapper.getSousFamille().setNomFr(classificationItemValue);
                break;
            case "Tribu":
                superRegneWrapper.getTribu().setNomFr(classificationItemValue);
                break;
            case "Sous-tribu":
                superRegneWrapper.getSousTribu().setNomFr(classificationItemValue);
                break;
            case "Genre":
                superRegneWrapper.getGenre().setNomFr(classificationItemValue);
                break;
            case "Sous-genre":
                superRegneWrapper.getSousGenre().setNomFr(classificationItemValue);
                break;
            case "Section":
                superRegneWrapper.getSection().setNomFr(classificationItemValue);
                break;
            case "Sous-section":
                superRegneWrapper.getSousSection().setNomFr(classificationItemValue);
                break;
            case "Espèce":
                superRegneWrapper.getEspece().setNomFr(classificationItemValue);
                break;
            case "Sous-espèce":
                superRegneWrapper.getSousEspece().setNomFr(classificationItemValue);
                break;
            case "Variété":
                superRegneWrapper.getVariete().setNomFr(classificationItemValue);
                break;
            case "Sous-variété":
                superRegneWrapper.getSousVariete().setNomFr(classificationItemValue);
                break;
            case "Forme":
                superRegneWrapper.getForme().setNomFr(classificationItemValue);
                break;
            case "Sous-forme":
                superRegneWrapper.getSousForme().setNomFr(classificationItemValue);
                break;
            case "":
                log.warn("Rang taxonomique inexistant dans le wiki");
                break;
            default:
                log.warn(classificationItemKey + " ne correspond pas à du Cronquist");

        }
    }

    // TODO add apgiii model
//    private APGIII extractionApg3(@NotNull Elements encadreTaxonomique) {
//        log.info("Extract APGIII classification");
//        Elements elementsDeClassification = encadreTaxonomique.select("tbody tr");
//        APGIII apgiii = new APGIII();
//        for (Element classificationItem : elementsDeClassification) {
//            // Get th = rang taxonomique et td = taxon
//            // TODO setClades, addClades, setSousFamille, setGenre
////            Elements rangTaxonomiqueElement = classificationItem.select("th a");
////            String classificationItemKey = rangTaxonomiqueElement
////                .get(0)// ne contient qu'un seul titre
////                .childNode(0)// TextNode value
////                .toString();
//            String classificationItemKey = selectText(classificationItem, "th a");
//            // TODO dans le cas des clades la cssQuery devient "td span a span"
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

//    private void insertSuperRegne(SuperRegneWrapper superRegne) {
//        if (superRegne.getCronquist() != null) {
//            cronquistRepository.save(superRegne.getCronquist());
//        }
//        if (superRegne.getApg3() != null) {
//            apgiiiRepository.save(superRegne.getApg3());
//        }
//        superRegneRepository.save(superRegne.getSuperRegne());
//    }


}
