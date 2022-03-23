package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia;

import fr.syncrase.ecosyst.ClassificationMsApp;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.AtomicClassificationNom;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.AtomicCronquistRank;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.CronquistClassificationBranch;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.scraper.WikipediaCrawler;
import fr.syncrase.ecosyst.domain.ClassificationNom;
import fr.syncrase.ecosyst.domain.CronquistRank;
import fr.syncrase.ecosyst.domain.enumeration.CronquistTaxonomikRanks;
import fr.syncrase.ecosyst.repository.ClassificationNomRepository;
import fr.syncrase.ecosyst.repository.CronquistRankRepository;
import org.apache.commons.collections4.map.LinkedMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.AtomicClassificationNom.getAtomicClassificationNomTreeSet;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClassificationMsApp.class)
public class ScrapAndInsertClassificationIntegrationTest {

    WikipediaCrawler wikipediaCrawler;
    @Autowired
    private CronquistRankRepository cronquistRankRepository;
    @Autowired
    private ClassificationNomRepository classificationNomRepository;
    @Autowired
    private CronquistService cronquistService;

    public ScrapAndInsertClassificationIntegrationTest() {
        this.wikipediaCrawler = new WikipediaCrawler(cronquistService);
    }

    @Test
    public void enregistrementDUneClassificationDontTousLesRangsSontInconnus() {
        List<String> wikis = new ArrayList<>();
        wikis.add("https://fr.wikipedia.org/wiki/Aldrovanda");
        wikis.forEach(wiki -> {
            CronquistClassificationBranch classification;
            try {
                classification = wikipediaCrawler.scrapWiki(wiki);
                Collection<CronquistRank> cronquistRanks = cronquistService.saveCronquist(classification, wiki);
                cronquistRanks.forEach(cronquistRank -> assertNotNull(cronquistRank.getId()));
            } catch (IOException e) {
                fail("unable to scrap wiki : " + e.getMessage());
            }
        });
    }

    @Test
    public void enregistrementDUneClassificationEntierementConnu() {
        List<String> wikis = new ArrayList<>();
        wikis.add("https://fr.wikipedia.org/wiki/Aldrovanda");
        wikis.forEach(wiki -> {
            CronquistClassificationBranch classification;
            try {
                classification = wikipediaCrawler.scrapWiki(wiki);
                Collection<CronquistRank> cronquistRanks = cronquistService.saveCronquist(classification, wiki);
                // Tous les rangs sont enregistrés
                cronquistRanks.forEach(cronquistRank -> assertNotNull(cronquistRank.getId()));

                // Tous les noms de rang sont uniques
                List<AtomicClassificationNom> classificationNoms = new ArrayList<>();
                for (CronquistRank rank : cronquistRanks) {
                    classificationNoms.addAll(
                        rank.getNoms().stream()
                            .map(ClassificationNom::getNomFr)
                            .filter(Objects::nonNull)
                            .flatMap(nom -> classificationNomRepository.findAll(Example.of(new ClassificationNom().nomFr(nom))).stream())
                            .map(AtomicClassificationNom::new)
                            .collect(Collectors.toList())
                                             );
                }
                TreeSet<AtomicClassificationNom> nomTreeSet = getAtomicClassificationNomTreeSet();
                nomTreeSet.addAll(classificationNoms);
                assertEquals(nomTreeSet.size(), classificationNoms.size());

                // Il n'existe aucun rang de liaison sans enfant
                // TODO ne pas parcourir la liste mais récupérer directement le dernier pour vérifier que c'est un rang significatif
                for (CronquistRank cronquistRank : cronquistRanks) {
                    AtomicCronquistRank atomicCronquistRank = AtomicCronquistRank.newRank(cronquistRank);
                    if (atomicCronquistRank.isRangDeLiaison() && cronquistRank.getChildren().size() == 0) {
                        fail("Ce rang de liaison ne possède pas de rang inférieur " + cronquistRank);
                    }
                }
            } catch (IOException e) {
                fail("unable to scrap wiki : " + e.getMessage());
            }
        });
    }


    @Test
    public void mergeDesBranchesSimples() {
        try {
            CronquistClassificationBranch classification;
            // Les trois plantes suivantes appartiennent à la sous-classe des Rosidae, mais on ne le sais pas pour atalaya. On le découvre quand on enregistre Cossinia
            String wiki = "https://fr.wikipedia.org/wiki/Arjona";
            classification = wikipediaCrawler.scrapWiki(wiki);
            Collection<CronquistRank> arjonaRanks = cronquistService.saveCronquist(classification, wiki);
            LinkedMap<CronquistTaxonomikRanks, CronquistRank> arjonaClassification = new LinkedMap<>();
            arjonaRanks.forEach(cronquistRank -> arjonaClassification.put(cronquistRank.getRank(), cronquistRank));

            wiki = "https://fr.wikipedia.org/wiki/Atalaya_(genre)";
            classification = wikipediaCrawler.scrapWiki(wiki);
            Collection<CronquistRank> atalayaRanks = cronquistService.saveCronquist(classification, wiki);
            LinkedMap<CronquistTaxonomikRanks, CronquistRank> atalayaClassification = new LinkedMap<>();
            atalayaRanks.forEach(cronquistRank -> atalayaClassification.put(cronquistRank.getRank(), cronquistRank));
            // Lors de cet ajout: le rang de liaison sous-règne prend le nom tracheobionta
            // TODO De la classe au règne les deux classifications sont égales
            assertEquals("Le sous-règne tracheobionta doit exister dans la classification de atalaya",
                         atalayaClassification.get(CronquistTaxonomikRanks.SOUSREGNE).getId(),
                         arjonaClassification.get(CronquistTaxonomikRanks.SOUSREGNE).getId()
                        );

            wiki = "https://fr.wikipedia.org/wiki/Cossinia";
            classification = wikipediaCrawler.scrapWiki(wiki);
            Collection<CronquistRank> cossiniaRanks = cronquistService.saveCronquist(classification, wiki);
            // La sous-classe rosidae doit être présente dans la classification de atalaya
            CronquistClassificationBranch newAtalayaClassification = cronquistService.getClassificationBranchOfThisRank(atalayaClassification.get(atalayaClassification.lastKey()).getId());
            Long rosidaeId = arjonaClassification.get(CronquistTaxonomikRanks.SOUSCLASSE).getId();
            Long sousClasseId = newAtalayaClassification.getRang(CronquistTaxonomikRanks.SOUSCLASSE).getId();
            assertEquals("La sous-classe rosidae doit être présente dans la classification de atalaya",
                         sousClasseId,
                         rosidaeId
                        );
            // Rosidae doit posséder deux taxons de liaison vers Santatales et vers Sapindales
            Set<AtomicCronquistRank> ranks = cronquistService.getTaxons(rosidaeId);
            // L'ancien rang de liaison a été supprimé

        } catch (IOException e) {
            fail("unable to scrap wiki : " + e.getMessage());
        }

    }

    @Test
    public void mergeDesBranchesAvecSynonymes() {
        List<String> wikis = new ArrayList<>();
        wikis.add("https://fr.wikipedia.org/wiki/Corylopsis");// Synonymes Saxifragales
        wikis.add("https://fr.wikipedia.org/wiki/Distylium");// Synonymes Hamamelidales
        wikis.add("https://fr.wikipedia.org/wiki/Loropetalum");// Synonymes Hamamelidales
    }

    @Test
    public void lesNomsDeRangsIncoherentsNeSontPasPrisEnCompte() {
        List<String> wikis = new ArrayList<>();
        wikis.add("https://fr.wikipedia.org/wiki/Chironia");
        wikis.add("https://fr.wikipedia.org/wiki/Monodiella");
        wikis.add("https://fr.wikipedia.org/wiki/Aldrovanda");
        wikis.add("https://fr.wikipedia.org/wiki/Amphorogyne");// Rosidae
    }

    @Test
    public void uneErreurEvidenteDansLaClassificationNEstPasPriseEnCompte() {
        List<String> wikis = new ArrayList<>();
        wikis.add("https://fr.wikipedia.org/wiki/Monodiella");
        //         => Le genre monodiella ne possède qu'un seul nom et qu'une seule url
    }

    @Test
    public void transformationDUnRangIntermediaireEnRangTaxonomique() {
        List<String> wikis = new ArrayList<>();
        wikis.add("https://fr.wikipedia.org/wiki/Chironia");
        wikis.add("https://fr.wikipedia.org/wiki/Monodiella");
        //         => vérifier qu'il n'y a qu'un seul sous règne
    }

    @Test
    public void enregistrementDUnRangSynonymeAvecMerge() {
        List<String> wikis = new ArrayList<>();
        wikis.add("https://fr.wikipedia.org/wiki/Oxera_neriifolia");
        wikis.add("https://fr.wikipedia.org/wiki/Hostaceae");
        wikis.add("https://fr.wikipedia.org/wiki/Selaginaceae");
        //         => vérifier la synonymie + les enfants ont bien été mergés
    }

    @Test
    public void enregistrementDUnRangSynonymeAvecMerge2() {
        List<String> wikis = new ArrayList<>();
        wikis.add("https://fr.wikipedia.org/wiki/Lepisanthes_senegalensis");
        wikis.add("https://fr.wikipedia.org/wiki/%C3%89rable_de_Miyabe");
        wikis.add("https://fr.wikipedia.org/wiki/%C3%89rable_de_Montpellier");
        //         => vérifier la synonymie + les enfants ont bien été mergés
    }

    @Test
    public void enregistrementDUneClassificationAvecDeuxRangsSynonymesSuccessifs() {
        List<String> wikis = new ArrayList<>();
        wikis.add("https://fr.wikipedia.org/wiki/%C3%89rable_de_Cr%C3%A8te");
        wikis.add("https://fr.wikipedia.org/wiki/%C3%89rable_de_Miyabe");
        //         => vérifier la synonymie + les enfants ont bien été mergés
    }

    @Test
    public void enregistrementDUneClassificationAvecDeuxRangsSynonymesSuccessifs2() {
        List<String> wikis = new ArrayList<>();
        wikis.add("https://fr.wikipedia.org/wiki/Bridgesia_incisifolia");
        wikis.add("https://fr.wikipedia.org/wiki/%C3%89rable_de_Miyabe");
        wikis.add("https://fr.wikipedia.org/wiki/%C3%89rable_de_Montpellier");
        //         => vérifier la synonymie + les enfants ont bien été mergés
    }

    @Test
    public void testAvecToutConfonduDansTousLesSens() {
        List<String> wikis = new ArrayList<>();
        wikis.add("https://fr.wikipedia.org/wiki/Acanthe");
        wikis.add("https://fr.wikipedia.org/wiki/Acanthe_%C3%A0_feuilles_molles");
        wikis.add("https://fr.wikipedia.org/wiki/Acanthus_spinosus");
        wikis.add("https://fr.wikipedia.org/wiki/Acer_campestre");
        wikis.add("https://fr.wikipedia.org/wiki/Acer_cappadocicum");
        wikis.add("https://fr.wikipedia.org/wiki/Acer_carpinifolium");
        wikis.add("https://fr.wikipedia.org/wiki/Acer_chaneyi");
        wikis.add("https://fr.wikipedia.org/wiki/Acer_circinatum");
        wikis.add("https://fr.wikipedia.org/wiki/Acer_davidii");
        wikis.add("https://fr.wikipedia.org/wiki/Acer_douglasense");
        wikis.add("https://fr.wikipedia.org/wiki/Acer_velutinum");
        wikis.add("https://fr.wikipedia.org/wiki/Aldrovanda");
        wikis.add("https://fr.wikipedia.org/wiki/Amphorogyne");// Rosidae
        wikis.add("https://fr.wikipedia.org/wiki/Andrographis");
        wikis.add("https://fr.wikipedia.org/wiki/Andrographis_paniculata");
        wikis.add("https://fr.wikipedia.org/wiki/Anisacanthus");
        wikis.add("https://fr.wikipedia.org/wiki/Anisoptera_(v%C3%A9g%C3%A9tal)");
        wikis.add("https://fr.wikipedia.org/wiki/Anthobolus");// Rosidae
        wikis.add("https://fr.wikipedia.org/wiki/Aphelandra");
        wikis.add("https://fr.wikipedia.org/wiki/Aphelandra_sinclairiana");
        wikis.add("https://fr.wikipedia.org/wiki/Arjona");// Rosidae// : merge branch
        wikis.add("https://fr.wikipedia.org/wiki/Asystasia");
        wikis.add("https://fr.wikipedia.org/wiki/Asystasia_gangetica");
        wikis.add("https://fr.wikipedia.org/wiki/Atalaya_(genre)");// : merge branch
        wikis.add("https://fr.wikipedia.org/wiki/Avicennia_germinans");
        wikis.add("https://fr.wikipedia.org/wiki/Barleria");
        wikis.add("https://fr.wikipedia.org/wiki/Barleria_cristata");
        wikis.add("https://fr.wikipedia.org/wiki/Barleria_obtusa");
        wikis.add("https://fr.wikipedia.org/wiki/Barleriola");
        wikis.add("https://fr.wikipedia.org/wiki/Blackstonia");
        wikis.add("https://fr.wikipedia.org/wiki/Blechum");
        wikis.add("https://fr.wikipedia.org/wiki/Bois_de_Judas");// Rosidae// : merge branch
        wikis.add("https://fr.wikipedia.org/wiki/Bridgesia_incisifolia");
        wikis.add("https://fr.wikipedia.org/wiki/Buckleya");
        wikis.add("https://fr.wikipedia.org/wiki/%C3%89rable_argent%C3%A9");
        wikis.add("https://fr.wikipedia.org/wiki/%C3%89rable_%C3%A0_%C3%A9corce_de_papier");
        wikis.add("https://fr.wikipedia.org/wiki/%C3%89rable_%C3%A0_%C3%A9pis");
        wikis.add("https://fr.wikipedia.org/wiki/%C3%89rable_%C3%A0_cinq_folioles");
        wikis.add("https://fr.wikipedia.org/wiki/%C3%89rable_%C3%A0_feuille_de_vigne");
        wikis.add("https://fr.wikipedia.org/wiki/%C3%89rable_%C3%A0_feuilles_d%27obier");
        wikis.add("https://fr.wikipedia.org/wiki/%C3%89rable_%C3%A0_grandes_feuilles");
        wikis.add("https://fr.wikipedia.org/wiki/%C3%89rable_champ%C3%AAtre");
        wikis.add("https://fr.wikipedia.org/wiki/%C3%89rable_de_Cappadoce");
        wikis.add("https://fr.wikipedia.org/wiki/%C3%89rable_de_Cr%C3%A8te");
        wikis.add("https://fr.wikipedia.org/wiki/%C3%89rable_de_Miyabe");
        wikis.add("https://fr.wikipedia.org/wiki/%C3%89rable_de_Montpellier");
        wikis.add("https://fr.wikipedia.org/wiki/%C3%89rable_de_Pennsylvanie");
        wikis.add("https://fr.wikipedia.org/wiki/Carlowrightia");
        wikis.add("https://fr.wikipedia.org/wiki/Centaurium");
        wikis.add("https://fr.wikipedia.org/wiki/Cervantesia");
        wikis.add("https://fr.wikipedia.org/wiki/Chironia");
        wikis.add("https://fr.wikipedia.org/wiki/Corylopsis");// Synonymes
        wikis.add("https://fr.wikipedia.org/wiki/Cossinia");// : merge branch
        wikis.add("https://fr.wikipedia.org/wiki/Deinanthe");
        wikis.add("https://fr.wikipedia.org/wiki/Diatenopteryx_sorbifolia");
        wikis.add("https://fr.wikipedia.org/wiki/Dicliptera");
        wikis.add("https://fr.wikipedia.org/wiki/Dicliptera_suberecta");
        wikis.add("https://fr.wikipedia.org/wiki/Dipterocarpus");
        wikis.add("https://fr.wikipedia.org/wiki/Distylium");// Synonymes
        wikis.add("https://fr.wikipedia.org/wiki/Eremophila_latrobei");
        wikis.add("https://fr.wikipedia.org/wiki/Eremophila_mitchellii");
        wikis.add("https://fr.wikipedia.org/wiki/Eremophila_nivea");
        wikis.add("https://fr.wikipedia.org/wiki/Eremophila_(plante)");
        wikis.add("https://fr.wikipedia.org/wiki/Graptophyllum");
        wikis.add("https://fr.wikipedia.org/wiki/Hemigraphis");
        wikis.add("https://fr.wikipedia.org/wiki/Huaceae");
        wikis.add("https://fr.wikipedia.org/wiki/Hygrophila_(plante)");
        wikis.add("https://fr.wikipedia.org/wiki/Hygrophila_polysperma");
        wikis.add("https://fr.wikipedia.org/wiki/Justicia_adhatoda");
        wikis.add("https://fr.wikipedia.org/wiki/Justicia_aurea");
        wikis.add("https://fr.wikipedia.org/wiki/Justicia_betonica");
        wikis.add("https://fr.wikipedia.org/wiki/Justicia_californica");
        wikis.add("https://fr.wikipedia.org/wiki/Justicia_carnea");
        wikis.add("https://fr.wikipedia.org/wiki/Justicia_gendarussa");
        wikis.add("https://fr.wikipedia.org/wiki/Justicia_spicigera");
        wikis.add("https://fr.wikipedia.org/wiki/Kielmeyera");
        wikis.add("https://fr.wikipedia.org/wiki/Lepisanthes_senegalensis");
        wikis.add("https://fr.wikipedia.org/wiki/Loropetalum");
        wikis.add("https://fr.wikipedia.org/wiki/Lyallia_kerguelensis");
        wikis.add("https://fr.wikipedia.org/wiki/Molinadendron");
        wikis.add("https://fr.wikipedia.org/wiki/Monodiella");
        wikis.add("https://fr.wikipedia.org/wiki/Odontonema");
        wikis.add("https://fr.wikipedia.org/wiki/Oxera_pulchella");
        wikis.add("https://fr.wikipedia.org/wiki/Phlogacanthus");
        wikis.add("https://fr.wikipedia.org/wiki/Phlogacanthus_turgidus");
        wikis.add("https://fr.wikipedia.org/wiki/Picanier_jaune");
        wikis.add("https://fr.wikipedia.org/wiki/Pseuderanthemum");
        wikis.add("https://fr.wikipedia.org/wiki/Ptychospermatinae");
        wikis.add("https://fr.wikipedia.org/wiki/Ruellia");
        wikis.add("https://fr.wikipedia.org/wiki/Ruellia_brevifolia");
        wikis.add("https://fr.wikipedia.org/wiki/Ruellia_chartacea");
        wikis.add("https://fr.wikipedia.org/wiki/Ruellia_devosiana");
        wikis.add("https://fr.wikipedia.org/wiki/Ruellia_geminiflora");
        wikis.add("https://fr.wikipedia.org/wiki/Ruellia_schnellii");
        wikis.add("https://fr.wikipedia.org/wiki/Ruellia_simplex");
        wikis.add("https://fr.wikipedia.org/wiki/Ruellia_tuberosa");
        wikis.add("https://fr.wikipedia.org/wiki/Sanchezia");
        wikis.add("https://fr.wikipedia.org/wiki/Strobilanthes_kunthiana");
        wikis.add("https://fr.wikipedia.org/wiki/Thunbergia");
        wikis.add("https://fr.wikipedia.org/wiki/Thunbergia_alata");
        wikis.add("https://fr.wikipedia.org/wiki/Thunbergia_erecta");
        wikis.add("https://fr.wikipedia.org/wiki/Thunbergia_fragrans");
        wikis.add("https://fr.wikipedia.org/wiki/Thunbergia_grandiflora");
        wikis.add("https://fr.wikipedia.org/wiki/Thunbergia_mysorensis");
        wikis.add("https://fr.wikipedia.org/wiki/Whitfieldia");
        wikis.add("https://fr.wikipedia.org/wiki/Whitfieldia_elongata");
        //         => vérifier la synonymie + les enfants ont bien été mergés
    }

}
