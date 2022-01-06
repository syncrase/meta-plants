package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia;

import fr.syncrase.ecosyst.domain.ClassificationNom;
import fr.syncrase.ecosyst.domain.CronquistRank;
import fr.syncrase.ecosyst.domain.Url;
import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistUtils.addNameToCronquistRank;

public class CronquistClassificationExtractor {

    private final Logger log = LoggerFactory.getLogger(CronquistClassificationExtractor.class);
    CronquistClassification cronquistClassification;

    public CronquistClassification getClassification(@NotNull Element mainTable) {
        Elements elementsDeClassification = mainTable.select("tbody tr");

        cronquistClassification = new CronquistClassification();
        for (Element classificationItem : elementsDeClassification) {
            setCronquistTaxonomyItemFromElement(classificationItem);
        }

        Map<String, RangTaxonomique> rangTaxonMap = extractionRangsTaxonomiquesInferieurs(mainTable);
        if (rangTaxonMap.keySet().size() > 0) {
            rangTaxonMap.forEach(this::setCronquistTaxonomyItem);
        }
        cronquistClassification.clearTail();
        return cronquistClassification;
    }

    /**
     * @param classificationItem row of the taxonomy table which contains taxonomy rank and name
     */
    private void setCronquistTaxonomyItemFromElement(Element classificationItem) {
        String classificationItemKey = selectText(classificationItem, "th a");
        switch (classificationItemKey) {
            case "Super-Règne":
                updateRank(cronquistClassification.getSuperRegne(), selectText(classificationItem, "td span a"), classificationItem.select("td a").attr("href"));
                break;
            case "Règne":
                updateRank(cronquistClassification.getRegne(), selectText(classificationItem, "td span a"), classificationItem.select("td a").attr("href"));
                break;
            case "Sous-règne":
                updateRank(cronquistClassification.getSousRegne(), selectText(classificationItem, "td span a"), classificationItem.select("td a").attr("href"));
                break;
            case "Rameau":
                updateRank(cronquistClassification.getRameau(), selectText(classificationItem, "td span a"), classificationItem.select("td a").attr("href"));
                break;
            case "Infra-règne":
                updateRank(cronquistClassification.getInfraRegne(), selectText(classificationItem, "td span a"), classificationItem.select("td a").attr("href"));
                break;
            case "Super-division":
            case "Super-embranchement":
                updateRank(cronquistClassification.getSuperDivision(), selectText(classificationItem, "td span a"), classificationItem.select("td a").attr("href"));
                break;
            case "Division":
            case "Embranchement":
                updateRank(cronquistClassification.getEmbranchement(), selectText(classificationItem, "td span a"), classificationItem.select("td a").attr("href"));
                break;
            case "Sous-division":
            case "Sous-embranchement":
                updateRank(cronquistClassification.getSousEmbranchement(), selectText(classificationItem, "td span a"), classificationItem.select("td a").attr("href"));
                break;
            case "Infra-embranchement":
                updateRank(cronquistClassification.getInfraEmbranchement(), selectText(classificationItem, "td span a"), classificationItem.select("td a").attr("href"));
                break;
            case "Micro-embranchement":
                updateRank(cronquistClassification.getMicroEmbranchement(), selectText(classificationItem, "td span a"), classificationItem.select("td a").attr("href"));
                break;
            case "Super-classe":
                updateRank(cronquistClassification.getSuperClasse(), selectText(classificationItem, "td span a"), classificationItem.select("td a").attr("href"));
                break;
            case "Classe":
                updateRank(cronquistClassification.getClasse(), selectText(classificationItem, "td span a"), classificationItem.select("td a").attr("href"));
                break;
            case "Sous-classe":
                updateRank(cronquistClassification.getSousClasse(), selectText(classificationItem, "td span a"), classificationItem.select("td a").attr("href"));
                break;
            case "Infra-classe":
                updateRank(cronquistClassification.getInfraClasse(), selectText(classificationItem, "td span a"), classificationItem.select("td a").attr("href"));
                break;
            case "Super-ordre":
                updateRank(cronquistClassification.getSuperOrdre(), selectText(classificationItem, "td span a"), classificationItem.select("td a").attr("href"));
                break;
            case "Ordre":
                updateRank(cronquistClassification.getOrdre(), selectText(classificationItem, "td span a"), classificationItem.select("td a").attr("href"));
                break;
            case "Sous-ordre":
                updateRank(cronquistClassification.getSousOrdre(), selectText(classificationItem, "td span a"), classificationItem.select("td a").attr("href"));
                break;
            case "Infra-ordre":
                updateRank(cronquistClassification.getInfraOrdre(), selectText(classificationItem, "td span a"), classificationItem.select("td a").attr("href"));
                break;
            case "Micro-ordre":
                updateRank(cronquistClassification.getMicroOrdre(), selectText(classificationItem, "td span a"), classificationItem.select("td a").attr("href"));
                break;
            case "Super-famille":
                updateRank(cronquistClassification.getSuperFamille(), selectText(classificationItem, "td span a"), classificationItem.select("td a").attr("href"));
                break;
            case "Famille":
                updateRank(cronquistClassification.getFamille(), selectText(classificationItem, "td span a"), classificationItem.select("td a").attr("href"));
                break;
            case "Sous-famille":
                updateRank(cronquistClassification.getSousFamille(), selectText(classificationItem, "td span a"), classificationItem.select("td a").attr("href"));
                break;
            case "Tribu":
                updateRank(cronquistClassification.getTribu(), selectText(classificationItem, "td a"), classificationItem.select("td a").attr("href"));
                break;
            case "Sous-tribu":
                updateRank(cronquistClassification.getSousTribu(), selectText(classificationItem, "td a"), classificationItem.select("td a").attr("href"));
                break;
            case "Genre":
                updateRank(cronquistClassification.getGenre(), selectText(classificationItem, "td a"), classificationItem.select("td a").attr("href"));
                break;
            case "Sous-genre":
                updateRank(cronquistClassification.getSousGenre(), selectText(classificationItem, "td a"), classificationItem.select("td a").attr("href"));
                break;
            case "Section":
                updateRank(cronquistClassification.getSection(), selectText(classificationItem, "td a"), classificationItem.select("td a").attr("href"));
                break;
            case "Sous-section":
                updateRank(cronquistClassification.getSousSection(), selectText(classificationItem, "td a"), classificationItem.select("td a").attr("href"));
                break;
            case "Espèce":
                updateRank(cronquistClassification.getEspece(), selectText(classificationItem, "td a"), classificationItem.select("td a").attr("href"));
                break;
            case "Sous-espèce":
                updateRank(cronquistClassification.getSousEspece(), selectText(classificationItem, "td a"), classificationItem.select("td a").attr("href"));
                break;
            case "Variété":
                updateRank(cronquistClassification.getVariete(), selectText(classificationItem, "td a"), classificationItem.select("td a").attr("href"));
                break;
            case "Sous-variété":
                updateRank(cronquistClassification.getSousVariete(), selectText(classificationItem, "td a"), classificationItem.select("td a").attr("href"));
                break;
            case "Forme":
                updateRank(cronquistClassification.getForme(), selectText(classificationItem, "td a"), classificationItem.select("td a").attr("href"));
                break;
            case "Sous-forme":
                updateRank(cronquistClassification.getSousForme(), selectText(classificationItem, "td a"), classificationItem.select("td a").attr("href"));
                break;
            case "":
                log.warn("Rang taxonomique inexistant dans le wiki");
                break;
            default:
                log.warn(classificationItemKey + " ne correspond pas à du Cronquist ou NOT YET IMPLEMENTED");

        }
    }

    private void updateRank(@NotNull CronquistRank rank, String rankNomFr, String url) {
        addNameToCronquistRank(rank, new ClassificationNom().nomFr(rankNomFr));
        if ((url != null) && (!url.equals(""))) {
            rank.addUrls(new Url().url(WikipediaCrawler.getValidUrl(url)));
        }
    }

    /**
     * @param classificationItemKey rang taxonomique
     * @param rangValues            taxon
     */
    private void setCronquistTaxonomyItem(@NotNull String classificationItemKey, RangTaxonomique rangValues) {
        switch (classificationItemKey) {
            case "Super-Règne":
                updateRank(cronquistClassification.getSuperRegne(), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Règne":
                updateRank(cronquistClassification.getRegne(), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Sous-règne":
                updateRank(cronquistClassification.getSousRegne(), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Rameau":
                updateRank(cronquistClassification.getRameau(), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Infra-règne":
                updateRank(cronquistClassification.getInfraRegne(), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Super-division":
            case "Super-embranchement":
                updateRank(cronquistClassification.getSuperDivision(), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Division":
            case "Embranchement":
                updateRank(cronquistClassification.getEmbranchement(), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Sous-division":
            case "Sous-embranchement":
                updateRank(cronquistClassification.getSousEmbranchement(), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Infra-embranchement":
                updateRank(cronquistClassification.getInfraEmbranchement(), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Micro-embranchement":
                updateRank(cronquistClassification.getMicroEmbranchement(), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Super-classe":
                updateRank(cronquistClassification.getSuperClasse(), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Classe":
                updateRank(cronquistClassification.getClasse(), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Sous-classe":
                updateRank(cronquistClassification.getSousClasse(), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Infra-classe":
                updateRank(cronquistClassification.getInfraClasse(), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Super-ordre":
                updateRank(cronquistClassification.getSuperOrdre(), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Ordre":
                updateRank(cronquistClassification.getOrdre(), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Sous-ordre":
                updateRank(cronquistClassification.getSousOrdre(), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Infra-ordre":
                updateRank(cronquistClassification.getInfraOrdre(), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Micro-ordre":
                updateRank(cronquistClassification.getMicroOrdre(), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Super-famille":
                updateRank(cronquistClassification.getSuperFamille(), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Famille":
                updateRank(cronquistClassification.getFamille(), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Sous-famille":
                updateRank(cronquistClassification.getSousFamille(), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Tribu":
                updateRank(cronquistClassification.getTribu(), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Sous-tribu":
                updateRank(cronquistClassification.getSousTribu(), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Genre":
                updateRank(cronquistClassification.getGenre(), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Sous-genre":
                updateRank(cronquistClassification.getSousGenre(), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Section":
                updateRank(cronquistClassification.getSection(), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Sous-section":
                updateRank(cronquistClassification.getSousSection(), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Espèce":
                updateRank(cronquistClassification.getEspece(), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Sous-espèce":
                updateRank(cronquistClassification.getSousEspece(), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Variété":
                updateRank(cronquistClassification.getVariete(), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Sous-variété":
                updateRank(cronquistClassification.getSousVariete(), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Forme":
                updateRank(cronquistClassification.getForme(), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Sous-forme":
                updateRank(cronquistClassification.getSousForme(), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "":
                log.warn("Rang taxonomique inexistant dans le wiki");
                break;
            default:
                log.warn(classificationItemKey + " ne correspond pas à du Cronquist");

        }
    }


    private @NotNull Map<String, RangTaxonomique> extractionRangsTaxonomiquesInferieurs(@NotNull Element mainTable) {
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
                el.select("p").text().contains("de rang inférieur") ||
                el.select("p br").size() != 0 ||
                el.select("div.left").size() != 0 && el.select("div.left").get(0).childrenSize() == 0 || //https://fr.wikipedia.org/wiki/Cryosophila_williamsii
                el.select("hr").size() != 0 ||
                el.select("dl").size() != 0


        );

        Map<String, RangTaxonomique> rangTaxonMap = new HashMap<>();
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
                    rangTaxonMap.put(rangs.get(i).text(), new RangTaxonomique(taxons.get(i).text(), taxons.get(i).attr("href")));
                }
            }
        }
        return rangTaxonMap;
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

    private static class RangTaxonomique {
        private final String taxonName;
        private final String url;

        public RangTaxonomique(String taxonName, String url) {
            this.taxonName = taxonName;
            this.url = url;
        }

        public String getTaxonName() {
            return taxonName;
        }

        public String getUrl() {
            return url;
        }
    }
}
