package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.crawler;

import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.*;
import fr.syncrase.ecosyst.domain.enumeration.CronquistTaxonomikRanks;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class CronquistClassificationExtractor {

    private final Logger log = LoggerFactory.getLogger(CronquistClassificationExtractor.class);
    CronquistClassificationBranch cronquistClassification;

    /**
     * Extract classification from the wikipedia classification table
     *
     * @param mainTable classification table
     * @return The generated classification object
     */
    public CronquistClassificationBranch getClassification(@NotNull Element mainTable) throws ClassificationReconstructionException {
        Elements elementsDeClassification = mainTable.select("tbody tr");

        cronquistClassification = new CronquistClassificationBranch();
        for (Element classificationItem : elementsDeClassification) {
            setCronquistTaxonomyItemFromElement(classificationItem);
        }

        Map<String, RangTaxonomique> rangTaxonMap = extractionRangsTaxonomiquesInferieurs(mainTable);
        if (rangTaxonMap.keySet().size() > 0) {
            rangTaxonMap.forEach(this::setCronquistTaxonomyItem);
        }
        cronquistClassification.clearTail();
        cronquistClassification.inferAllRank();
        return cronquistClassification;
    }

    /**
     * Extract rank values from JSOUP Element
     *
     * @param classificationItem row of the taxonomy table which contains taxonomy rank and name
     */
    private void setCronquistTaxonomyItemFromElement(Element classificationItem) {
        String classificationItemKey = selectText(classificationItem, "th a");
        RangTaxonomique rangTaxonomique = new RangTaxonomique(selectText(classificationItem, "td span a", "td a"), classificationItem.select("td a").attr("href"));
        classificationItemKey = getItemKey(rangTaxonomique.getTaxonName(), classificationItemKey);
        if (classificationItemKey != null) {
            setCronquistTaxonomyItem(classificationItemKey, rangTaxonomique);
        }
    }

    /**
     * Vérification basique de la concordance entre le rang et le nom du taxon
     *
     * @param taxon                 Nom du taxon dont le suffixe doit correspondre au rang
     * @param classificationItemKey rang du taxon
     * @return S'il y a une incohérence, retourne null
     */
    @Contract(pure = true)
    private @Nullable String getItemKey(@NotNull String taxon, String classificationItemKey) {
        if (taxon.endsWith("les") && !Arrays.asList(new String[]{"Ordre"}).contains(classificationItemKey)) {
            log.error("Le taxon {} est signalé comme étant de rang {}, seul un ordre possède ce suffixe. Erreur dans le wiki", taxon, classificationItemKey);
            return null;
        }
        if (taxon.endsWith("eae") && !Arrays.asList(new String[]{"Famille"}).contains(classificationItemKey)) {
            log.error("Le taxon {} est signalé comme étant de rang {}, seul une famille possède ce suffixe. Erreur dans le wiki", taxon, classificationItemKey);
            return null;
        }
        return classificationItemKey;
    }

    private void updateRank(@NotNull AtomicCronquistRank rank, String rankNomFr, String url) {
        rank.addNameToCronquistRank(new AtomicClassificationNom().nomFr(rankNomFr));
        if ((url != null) && (!url.equals(""))) {
            rank.addUrls(new AtomicUrl().url(Wikipedia.getValidUrl(url)));
        }
    }

    /**
     * @param classificationItemKey rang taxonomique
     * @param rangValues            taxon
     */
    private void setCronquistTaxonomyItem(@NotNull String classificationItemKey, RangTaxonomique rangValues) {
        switch (classificationItemKey) {
            case "Super-Règne":
                updateRank(cronquistClassification.getRang(CronquistTaxonomikRanks.SUPERREGNE), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Règne":
                updateRank(cronquistClassification.getRang(CronquistTaxonomikRanks.REGNE), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Sous-règne":
                updateRank(cronquistClassification.getRang(CronquistTaxonomikRanks.SOUSREGNE), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Rameau":
                updateRank(cronquistClassification.getRang(CronquistTaxonomikRanks.RAMEAU), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Infra-règne":
                updateRank(cronquistClassification.getRang(CronquistTaxonomikRanks.INFRAREGNE), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Super-division":
            case "Super-embranchement":
                updateRank(cronquistClassification.getRang(CronquistTaxonomikRanks.SUPEREMBRANCHEMENT), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Division":
            case "Embranchement":
                updateRank(cronquistClassification.getRang(CronquistTaxonomikRanks.EMBRANCHEMENT), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Sous-division":
            case "Sous-embranchement":
                updateRank(cronquistClassification.getRang(CronquistTaxonomikRanks.SOUSEMBRANCHEMENT), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Infra-embranchement":
                updateRank(cronquistClassification.getRang(CronquistTaxonomikRanks.INFRAEMBRANCHEMENT), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Micro-embranchement":
                updateRank(cronquistClassification.getRang(CronquistTaxonomikRanks.MICROEMBRANCHEMENT), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Super-classe":
                updateRank(cronquistClassification.getRang(CronquistTaxonomikRanks.SUPERCLASSE), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Classe":
                updateRank(cronquistClassification.getRang(CronquistTaxonomikRanks.CLASSE), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Sous-classe":
                updateRank(cronquistClassification.getRang(CronquistTaxonomikRanks.SOUSCLASSE), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Infra-classe":
                updateRank(cronquistClassification.getRang(CronquistTaxonomikRanks.INFRACLASSE), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Super-ordre":
                updateRank(cronquistClassification.getRang(CronquistTaxonomikRanks.SUPERORDRE), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Ordre":
                updateRank(cronquistClassification.getRang(CronquistTaxonomikRanks.ORDRE), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Sous-ordre":
                updateRank(cronquistClassification.getRang(CronquistTaxonomikRanks.SOUSORDRE), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Infra-ordre":
                updateRank(cronquistClassification.getRang(CronquistTaxonomikRanks.INFRAORDRE), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Micro-ordre":
                updateRank(cronquistClassification.getRang(CronquistTaxonomikRanks.MICROORDRE), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Super-famille":
                updateRank(cronquistClassification.getRang(CronquistTaxonomikRanks.SUPERFAMILLE), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Famille":
                updateRank(cronquistClassification.getRang(CronquistTaxonomikRanks.FAMILLE), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Sous-famille":
                updateRank(cronquistClassification.getRang(CronquistTaxonomikRanks.SOUSFAMILLE), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Tribu":
                updateRank(cronquistClassification.getRang(CronquistTaxonomikRanks.TRIBU), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Sous-tribu":
                updateRank(cronquistClassification.getRang(CronquistTaxonomikRanks.SOUSTRIBU), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Genre":
                updateRank(cronquistClassification.getRang(CronquistTaxonomikRanks.GENRE), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Sous-genre":
                updateRank(cronquistClassification.getRang(CronquistTaxonomikRanks.SOUSGENRE), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Section":
                updateRank(cronquistClassification.getRang(CronquistTaxonomikRanks.SECTION), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Sous-section":
                updateRank(cronquistClassification.getRang(CronquistTaxonomikRanks.SOUSSECTION), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Espèce":
                updateRank(cronquistClassification.getRang(CronquistTaxonomikRanks.ESPECE), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Sous-espèce":
                updateRank(cronquistClassification.getRang(CronquistTaxonomikRanks.SOUSESPECE), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Variété":
                updateRank(cronquistClassification.getRang(CronquistTaxonomikRanks.VARIETE), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Sous-variété":
                updateRank(cronquistClassification.getRang(CronquistTaxonomikRanks.SOUSVARIETE), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Forme":
                updateRank(cronquistClassification.getRang(CronquistTaxonomikRanks.FORME), rangValues.getTaxonName(), rangValues.getUrl());
                break;
            case "Sous-forme":
                updateRank(cronquistClassification.getRang(CronquistTaxonomikRanks.SOUSFORME), rangValues.getTaxonName(), rangValues.getUrl());
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
     * @param item       Element from which the contained string is extract
     * @param cssQueries Ensemble de cssQueries qui sont testées. La première qui retourne un résultat est utilisée. La query DOIT retourner un unique élément
     * @return the text value of the node or an empty string
     */
    private @NotNull
    String selectText(@NotNull Element item, String @NotNull ... cssQueries) {
        for (String cssQuery : cssQueries) {
            Optional<Element> el = item.select(cssQuery).stream().findFirst();
            if (el.isPresent()) {
                return el.map(Element::text).get();//.orElse("")
            }
        }
        return "";
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
