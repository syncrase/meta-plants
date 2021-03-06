package fr.syncrase.ecosyst.domain.crawler.wikipedia;

import fr.syncrase.ecosyst.domain.classification.CronquistClassificationBranch;
import fr.syncrase.ecosyst.domain.classification.consistency.ClassificationReconstructionException;
import fr.syncrase.ecosyst.domain.classification.entities.ICronquistRank;
import fr.syncrase.ecosyst.domain.classification.entities.IUrl;
import fr.syncrase.ecosyst.domain.classification.entities.atomic.AtomicClassificationNom;
import fr.syncrase.ecosyst.domain.classification.enumeration.RankName;
import fr.syncrase.ecosyst.service.classification.CronquistService;
import org.apache.commons.collections4.map.LinkedMap;
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
import java.util.Map;
import java.util.Set;

public class WikipediaCrawler {

    public static final String LIST_INDICATOR_IN_URL = "Cat%C3%A9gorie";
    private final Logger log = LoggerFactory.getLogger(WikipediaCrawler.class);

    private final CronquistService cronquistService;
    WikipediaHtmlExtractor wikipediaHtmlExtractor;

    public WikipediaCrawler(CronquistService cronquistService) {
        this.cronquistService = cronquistService;
        wikipediaHtmlExtractor = new WikipediaHtmlExtractor();
    }

    public void crawlAllWikipedia() {
        try {
            scrapAndSaveWikiList(Wikipedia.scrappingBaseUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void findWiki() {
        CronquistClassificationBranch lilianae = cronquistService.getClassificationByName("Lilianae");
        Set<ICronquistRank> taxonsOfLiliane = cronquistService.getTaxonsOf(lilianae.getRangDeBase().getId());
        findRangDeBase(taxonsOfLiliane);
    }

    /**
     * M??thode de test pour looger tous les rangs de base poss??dant le superordre Lilianae
     *
     * @param taxonsOfLiliane
     */
    private void findRangDeBase(@NotNull Set<ICronquistRank> taxonsOfLiliane) {
        for (ICronquistRank iCronquistRank : taxonsOfLiliane) {
            Set<ICronquistRank> taxons = cronquistService.getTaxonsOf(iCronquistRank.getId());
            if (taxons.size() == 0) {
                // Ce rang n'a plus de taxons, je scrappe le wiki pour regarder le superordre s'il vaut Lilianae
                for (IUrl iUrl : iCronquistRank.getIUrls()) {
                    try {
                        CronquistClassificationBranch classification = extractClassificationFromWiki(iUrl.getUrl());
                        if (classification.getRang(RankName.SUPERORDRE).doTheRankHasOneOfTheseNames(Set.of(new AtomicClassificationNom().nomFr("Lilianae")))) {
                            //                            return extractClassification;
                            log.info("Le wiki qui fourni le super-ordre Lilianae est : " + iUrl.getUrl());
                        }
                    } catch (IOException e) {
                        log.error("Impossible de scrapper la page " + iUrl.getUrl());
                    }
                }
            } else {
                findRangDeBase(taxons);
            }
        }
    }

    public void testCrawlAllWikipedia() {
        //            scrapAndSaveWikiList(Wikipedia.scrappingBaseUrl);
        List<String> wikis = new ArrayList<>();
        //        wikis.add("https://fr.wikipedia.org/wiki/Arjona");
        //        wikis.add("https://fr.wikipedia.org/wiki/Atalaya_(genre)");
        //        wikis.add("https://fr.wikipedia.org/wiki/Cossinia");
        wikis.add("https://fr.wikipedia.org/wiki/Helanthium_bolivianum");

        wikis.forEach(wiki -> {
            CronquistClassificationBranch classification;
            try {
                classification = extractClassificationFromWiki(wiki);
                cronquistService.saveCronquist(classification, wiki);
            } catch (IOException e) {
                log.error("unable to scrap wiki : " + e.getMessage());
            }
        });

            /*
            TEST
            - Deux enregistrements d'un m??me n'enregistre qu'un seul rang
            - Enregistrement d'une esp??ce partageant un m??me rang avec une autre extractClassification => la extractClassification se rattache ?? l'existant, le rang en commun poss??de un enfant suppl??mentaire
             */
        // https://fr.wikipedia.org/wiki/Ptychospermatinae rang inf??rieur
        // https://fr.wikipedia.org/wiki/Forsythia_%C3%97intermedia rang inf??rieur
        // https://fr.wikipedia.org/wiki/Ch%C3%A8vrefeuille rang inf??rieur
        // TODO ajouter la extractClassification des insectes
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
        log.info("All extractClassification from Wikipedia had been scrapped");
    }

    public void scrapAndSaveWikiList(String urlWikiListe) throws IOException {
        log.info("Get list from : " + urlWikiListe);
        Document doc = Jsoup.connect(urlWikiListe).get();

        Elements wikiList = wikipediaHtmlExtractor.extractList(doc);
        if (wikiList.size() == 0) {
            log.error("??chec de r??cup??ration des ??l??ments de liste de la page " + urlWikiListe);
        }

        for (Element listItem : wikiList) {
            urlWikiListe = wikipediaHtmlExtractor.extractUrlAttr(listItem);
            // Si l'url contient "Cat??gorie" alors la page contient une liste
            String urlWiki = Wikipedia.getValidUrl(urlWikiListe);
            if (urlWikiListe.contains(LIST_INDICATOR_IN_URL)) {
                scrapAndSaveWikiList(urlWiki);
            } else {
                CronquistClassificationBranch classification = extractClassification(urlWiki);
                // TODO ne pas enregistrer ici, comment faire ?
                cronquistService.saveCronquist(classification, urlWiki);
            }
        }
    }

    public CronquistClassificationBranch extractClassification(String urlWiki) throws IOException {
        CronquistClassificationBranch extractedClassification = extractClassificationFromWiki(urlWiki);
        extractedClassification = checkAllRanksLevel(extractedClassification);
        return extractedClassification;
    }

    private @NotNull CronquistClassificationBranch checkAllRanksLevel(CronquistClassificationBranch extractedClassification) throws IOException {
        CronquistClassificationBranch classificationWithDoubleCheckedRanks = new CronquistClassificationBranch();
        if (extractedClassification != null) {
            LinkedMap<RankName, ICronquistRank> classificationBranch = doubleCheckEachRank(extractedClassification, classificationWithDoubleCheckedRanks);
            // TODO A voir si je garde l'assignation ici ou si je la passe dans la m??thode. WARN si j'??crase un rang scrapp?? non v??rifi?? avec un rang valid??
            for (Map.Entry<RankName, ICronquistRank> validatedCronquistRankEntry : classificationWithDoubleCheckedRanks.getClassificationBranch().entrySet()) {
                ICronquistRank rankEntryValue = validatedCronquistRankEntry.getValue();
                if (rankEntryValue.isRangSignificatif()) {
                    classificationBranch.put(validatedCronquistRankEntry.getKey(), rankEntryValue);
                }
            }

        }
        return classificationWithDoubleCheckedRanks;
    }

    @NotNull
    private LinkedMap<RankName, ICronquistRank> doubleCheckEachRank(
        @NotNull CronquistClassificationBranch extractedClassification,
        CronquistClassificationBranch classificationWithDoubleCheckedRanks
    ) throws IOException {
        // TODO ici v??rifier que les rangs correspondent bien aux bons niveaux de extractClassification
            /*
            Algo parcours tous les rangs et garde dans une liste tous les rangs qui ont ??t?? v??rifi??s ainsi que ceux qui ne sont pas cronquist (ils seront noms synonymes)
            Reconstruit une branche ?? partir de tous ces rangs valid??s (nouveau constructeur de CronquistClassificationBranch)
             */
        // Pour chacun des rangs : scrap la page wiki r??f??renc??e pour v??rifier le niveau du rang
        LinkedMap<RankName, ICronquistRank> classificationBranch = extractedClassification.getClassificationBranch();
        for (Map.Entry<RankName, ICronquistRank> scrappedRankEntry : classificationBranch.entrySet()) {
            // TODO si je l'ai en base de donn??es => la v??rification est d??j?? faite => query DB?
            ICronquistRank scrappedRank = scrappedRankEntry.getValue();
            Set<IUrl> urls = scrappedRank.getIUrls();
            // S'il poss??de une url
            if (urls.size() > 0) {
                for (IUrl url : urls) {
                    // erreur possible : 2 urls pour un m??me qui donnent un rang diff??rent ?
                    // scrap la page pour v??rifier le rang de base
                    CronquistClassificationBranch classificationJustForCheckLevels = extractClassificationFromWiki(url.getUrl());
                    // Si le rang ne correspond pas ??? le mettre ?? jour dans la classification
                    if (classificationJustForCheckLevels == null) {
                        /*
                        null si
                        - ce n'est pas un rang de la classification de cronquist
                        - la page wiki n'existe pas
                         */
                        // classificationBranch.put(scrappedRankEntry.getKey(), AtomicCronquistRank.getDefaultRank(scrappedRankEntry.getKey()));
                        continue;
                    }
                    RankName trueRankLevelOfTheScrapedRank = classificationJustForCheckLevels.getRangDeBase().getRankName();
                    boolean leRangScrappeSAvereNePasEtreDuBonNiveau = !trueRankLevelOfTheScrapedRank.equals(scrappedRank.getRankName());
                    if (leRangScrappeSAvereNePasEtreDuBonNiveau) {
                        classificationWithDoubleCheckedRanks.put(trueRankLevelOfTheScrapedRank, scrappedRank);
                        continue;
                    }
                    boolean leRangDeVerificationNePossedePasLeNomScrappeInitiale = !classificationJustForCheckLevels.getRangDeBase().doTheRankHasOneOfTheseNames(classificationBranch.get(trueRankLevelOfTheScrapedRank).getNomsWrappers());
                    if (leRangDeVerificationNePossedePasLeNomScrappeInitiale) {
                        // TODO Pour un m??me niveau : Le nom scrapp?? est diff??rent du nom quand on le v??rifie => merger l'ensemble des noms ?
                        // Exemple Magnoliophyta et angiosperme
                        // TODO n'ajouter que les noms inexistants
                        classificationBranch.get(trueRankLevelOfTheScrapedRank).addAllNamesToCronquistRank(classificationJustForCheckLevels.getRangDeBase().getNomsWrappers());
                    }
                }
            }
        }
        classificationWithDoubleCheckedRanks.clearTail();
        return classificationBranch;
    }

    public @Nullable CronquistClassificationBranch extractClassificationFromWiki(String urlWiki) throws IOException {
        try {
            log.info("Get extractClassification from : " + urlWiki);
            Elements encadreTaxonomique = wikipediaHtmlExtractor.extractEncadreDeClassification(urlWiki);

            CronquistClassificationBranch cronquistClassificationBranch = extractPremiereClassification(encadreTaxonomique);
            return cronquistClassificationBranch;
        } catch (SocketTimeoutException e) {
            log.error("La page Wikipedia ne r??pond pas {}\n. V??rifier la connexion internet!", urlWiki);
        } catch (IOException e) {
            log.error("Probl??me d'acc??s lors de l'extraction des donn??es de la page Wikipedia {}", urlWiki);
        }
        return null;
    }

    private @Nullable CronquistClassificationBranch extractPremiereClassification(@NotNull Elements encadreTaxonomique) {
        // TODO Contient plusieurs classifications, en g??n??ral Cronquist et APGN
        // TODO dans l'??tat actuel des choses, je ne garde que la PREMIERE section !

        switch (wikipediaHtmlExtractor.extractTypeOfMainClassification(encadreTaxonomique)) {
            case "APG III":
                log.info("APG III to be implemented");
                break;
            case "Cronquist":
                CronquistClassificationBranch cronquistClassificationBranch = extractionCronquist(encadreTaxonomique);
                return cronquistClassificationBranch;
            case "No extractClassification":
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

/*
    private APGIII extractionApg3(@NotNull Elements encadreTaxonomique) {
        log.info("Extract APGIII extractClassification");
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
                case "R??gne":
                    log.debug("\tR??gne NOT YET IMPLEMENTED");
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
                    log.warn("\t" + classificationItemKey + " ne correspond pas ?? du APGIII");
            }
        }
        log.info("(TO BE COMPLETED) Created APG III extractClassification APGIII : " + apgiii);
        return apgiii;
    }*/

    /**
     * R??cup??re la extractClassification contenue dans l'encadr?? de extractClassification.
     * V??rifications pr??liminaires :
     * <ul>
     *     <li>Les noms dont le suffixe ne correspond pas au niveau de extractClassification ne sont pas int??gr??s ?? la extractClassification.</li>
     *     <li>Chacun des rangs subi une v??rification pour s'assurer que le niveau de extractClassification est le bon</li>
     * </ul>
     *
     * @param encadreTaxonomique Elements HTML qui contiennent l'encadr?? de classification
     * @return La classification extraite ayant subi les v??rifications pr??liminaires
     */
    private CronquistClassificationBranch extractionCronquist(@NotNull Elements encadreTaxonomique) {
        Element mainTable = wikipediaHtmlExtractor.extractMainTableOfClassificationFrame(encadreTaxonomique);

        CronquistClassificationBuilder cronquistClassificationBuilder = new CronquistClassificationBuilder();
        CronquistClassificationBranch cronquistClassification = null;
        try {
            cronquistClassification = cronquistClassificationBuilder.getClassification(mainTable);

        } catch (ClassificationReconstructionException e) {
            log.error("Impossible d'extraire la extractClassification de la page");
            e.printStackTrace();
        } catch (UnableToScrapClassification e) {
            log.error(e.getMessage());
        }

        log.info("Created Cronquist extractClassification : " + cronquistClassification);
        return cronquistClassification;
    }

}
