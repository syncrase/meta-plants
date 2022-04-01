package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia;

import fr.syncrase.ecosyst.ClassificationMsApp;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.CronquistClassificationBranch;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.crawler.WikipediaCrawler;
import fr.syncrase.ecosyst.domain.IClassificationNom;
import fr.syncrase.ecosyst.domain.ICronquistRank;
import fr.syncrase.ecosyst.domain.enumeration.RankName;
import org.apache.commons.collections4.map.LinkedMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClassificationMsApp.class)
public class ScrapAndInsertClassificationIntegrationTest {

    WikipediaCrawler wikipediaCrawler;

    TestUtils utils = new TestUtils();

    @Autowired
    private CronquistService cronquistService;

    public ScrapAndInsertClassificationIntegrationTest() {
        this.wikipediaCrawler = new WikipediaCrawler(cronquistService);
    }

    @Test
    public void mergeDesBranchesSimples() {
        try {
            CronquistClassificationBranch classification;
            String wiki = "https://fr.wikipedia.org/wiki/Arjona";
            classification = wikipediaCrawler.scrapWiki(wiki);
            Collection<ICronquistRank> arjonaRanks = cronquistService.saveCronquist(classification, wiki);
            LinkedMap<RankName, ICronquistRank> arjonaClassification = utils.transformToMapOfRanksByName(arjonaRanks);

            // Les plantes suivantes appartiennent à la sous-classe des Rosidae, mais on ne le sait pas pour atalaya. On le découvre quand on enregistre Cossinia
            wiki = "https://fr.wikipedia.org/wiki/Atalaya_(genre)";
            classification = wikipediaCrawler.scrapWiki(wiki);
            Collection<ICronquistRank> atalayaRanks = cronquistService.saveCronquist(classification, wiki);
            LinkedMap<RankName, ICronquistRank> atalayaClassification = utils.transformToMapOfRanksByName(atalayaRanks);
            // Lors de cet ajout : le rang de liaison sous-règne prend le nom tracheobionta
            assertEquals("Le sous-règne tracheobionta doit avoir été ajoutée dans la classification de atalaya",
                         atalayaClassification.get(RankName.SOUSREGNE).getId(),
                         arjonaClassification.get(RankName.SOUSREGNE).getId()
                        );
            for (RankName rank : RankName.values()) {
                assertEquals("De la classe au règne les deux classifications doivent être égales",
                             atalayaClassification.get(rank).getId(),
                             arjonaClassification.get(rank).getId()
                            );
                if (rank.ordinal() >= RankName.CLASSE.ordinal()) {
                    break;
                }
            }

            wiki = "https://fr.wikipedia.org/wiki/Cossinia";
            classification = wikipediaCrawler.scrapWiki(wiki);
            cronquistService.saveCronquist(classification, wiki);

            Long atalayaId = atalayaClassification.get(atalayaClassification.lastKey()).getId();
            CronquistClassificationBranch newAtalayaClassification = cronquistService.getClassificationById(atalayaId);
            Long rosidaeId = arjonaClassification.get(RankName.SOUSCLASSE).getId();
            Long sousClasseId = newAtalayaClassification.getRang(RankName.SOUSCLASSE).getId();
            assertEquals("La sous-classe rosidae doit avoir été ajoutée dans la classification d'atalaya",
                         sousClasseId,
                         rosidaeId
                        );

            Set<ICronquistRank> taxonsOfRosidae = cronquistService.getTaxonsOf(rosidaeId);
            assertEquals(
                "Rosidae doit posséder deux taxons de liaison (vers Santatales et vers Sapindales)",
                2,
                taxonsOfRosidae.size()
                        );
            //            CronquistClassificationBranch sousClasseBeforeMerge = cronquistService.getClassificationById(arjonaClassification.get(RankName.SOUSCLASSE).getId());
            //            assertNull("La sous-classe d'arjona doit avoir été supprimée car mergée avec le rang de liaison d'Atalaya", sousClasseBeforeMerge);

        } catch (IOException e) {
            fail("unable to scrap wiki : " + e.getMessage());
        }

    }

    @Test
    public void enregistrementDUneClassificationAvecDeuxRangsSynonymesSuccessifs() {
        try {
            CronquistClassificationBranch classification;
            // Règne 	Plantae
            //Sous-règne 	doit être ajouté > Tracheobionta
            //Division 	doit être ajouté > Magnoliophyta
            //Classe 	Equisetopsida (+Magnoliopsida)
            //Sous-classe 	Magnoliidae (+Rosidae)
            //Super-ordre 	Rosanae
            //Ordre 	Sapindales
            //Famille 	Aceraceae
            //Genre 	Acer
            String wiki = "https://fr.wikipedia.org/wiki/%C3%89rable_de_Cr%C3%A8te";
            classification = wikipediaCrawler.scrapWiki(wiki);
            Collection<ICronquistRank> erableCreteRanks = cronquistService.saveCronquist(classification, wiki);
            LinkedMap<RankName, ICronquistRank> erableCreteClassification = utils.transformToMapOfRanksByName(erableCreteRanks);

            // Règne 	Plantae
            //Sous-règne 	Tracheobionta
            //Division 	Magnoliophyta
            //Classe 	Magnoliopsida (+Equisetopsida)
            //Sous-classe 	Rosidae (+Magnoliidae)
            //Super-ordre 	doit être ajouté > Rosanae
            //Ordre 	Sapindales
            //Famille 	Aceraceae
            //Genre 	Acer
            wiki = "https://fr.wikipedia.org/wiki/%C3%89rable_de_Miyabe";
            classification = wikipediaCrawler.scrapWiki(wiki);
            Collection<ICronquistRank> erableMiyabeRanks = cronquistService.saveCronquist(classification, wiki);
            LinkedMap<RankName, ICronquistRank> erableMiyabeClassification = utils.transformToMapOfRanksByName(erableMiyabeRanks);

            // L'érable de miyabe
            // - doit posséder le superordre Rosanae
            // - doit posséder la sous-classe Magnoliidae en plus de Rosidae
            // - doit posséder la classe Equisetopsida en plus de Magnoliopsida
            Set<String> ordresDeErableMiyabe = utils.getRankNames(erableMiyabeClassification, RankName.SUPERORDRE);
            assertTrue("L'érable de Miyabe doit posséder le superordre Rosanae", ordresDeErableMiyabe.contains("Rosanae"));

            Set<String> nomsDeSousClasseDeErableMiyabe = utils.getRankNames(erableMiyabeClassification, RankName.SOUSCLASSE);
            assertEquals("L'érable de Miyabe doit contenir deux sous-classes", 2, nomsDeSousClasseDeErableMiyabe.size());
            assertTrue("L'érable de Miyabe doit posséder la sous-classe synonyme Magnoliidae", nomsDeSousClasseDeErableMiyabe.containsAll(Set.of("Magnoliidae", "Rosidae")));

            Set<String> nomsDeClasseDeErableMiyabe = utils.getRankNames(erableMiyabeClassification, RankName.CLASSE);
            assertEquals("L'érable de Miyabe doit contenir deux classes", 2, nomsDeClasseDeErableMiyabe.size());
            assertTrue("L'érable de Miyabe doit posséder la classe synonyme Equisetopsida", nomsDeClasseDeErableMiyabe.containsAll(Set.of("Equisetopsida", "Magnoliopsida")));

            // L'érable de Crête
            // - doit posséder la sous-classe Rosidae en plus de Magnoliidae
            // - doit posséder la classe Magnoliopsida en plus de Equisetopsida
            // - doit posséder la division Magnoliophyta
            // - doit posséder le sous-règne Tracheobionta
            CronquistClassificationBranch classificationBranchOfErableCrete = cronquistService.getClassificationById(erableCreteClassification.get(erableCreteClassification.lastKey()).getId());

            Set<String> nomsDeSousClasseDeErableCrete = classificationBranchOfErableCrete.getRang(RankName.SOUSCLASSE).getNoms().stream().map(IClassificationNom::getNomFr).collect(Collectors.toSet());
            assertEquals("L'érable de Crete doit contenir deux sous-classes", 2, nomsDeSousClasseDeErableCrete.size());
            assertTrue("L'érable de Crete doit posséder la sous-classes synonyme Rosidae", nomsDeSousClasseDeErableCrete.containsAll(Set.of("Rosidae", "Magnoliidae")));

            Set<String> nomsDeClasseDeErableCrete = classificationBranchOfErableCrete.getRang(RankName.CLASSE).getNoms().stream().map(IClassificationNom::getNomFr).collect(Collectors.toSet());
            assertEquals("L'érable de Crete doit contenir deux classes", 2, nomsDeClasseDeErableCrete.size());
            assertTrue("L'érable de Crete doit posséder la classes synonyme Magnoliopsida", nomsDeClasseDeErableCrete.containsAll(Set.of("Magnoliopsida", "Equisetopsida")));

            Set<String> nomsDEmbranchementDeErableCrete = classificationBranchOfErableCrete.getRang(RankName.EMBRANCHEMENT).getNoms().stream().map(IClassificationNom::getNomFr).collect(Collectors.toSet());
            assertTrue("L'érable de Crete doit posséder l'embranchement Magnoliophyta", nomsDEmbranchementDeErableCrete.contains("Magnoliophyta"));

            Set<String> nomsDeSousRegneDeErableCrete = classificationBranchOfErableCrete.getRang(RankName.SOUSREGNE).getNoms().stream().map(IClassificationNom::getNomFr).collect(Collectors.toSet());
            assertTrue("L'érable de Crete doit posséder le sous-règne Tracheobionta", nomsDeSousRegneDeErableCrete.contains("Tracheobionta"));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //    @Test
    //    public void lesNomsDeRangsIncoherentsNeSontPasPrisEnCompte() {
    //
    //        // TODO en quoi les noms de rang sont incohérents ?
    //        CronquistClassificationBranch classification;
    //        try {
    //            String wiki = "https://fr.wikipedia.org/wiki/Chironia";
    //            wiki = "https://fr.wikipedia.org/wiki/Chironia";
    //            classification = wikipediaCrawler.scrapWiki(wiki);
    //            Collection<CronquistRank> chironiaRanks = cronquistService.saveCronquist(classification, wiki);
    //
    //            wiki = "https://fr.wikipedia.org/wiki/Monodiella";
    //            classification = wikipediaCrawler.scrapWiki(wiki);
    //            Collection<CronquistRank> monodiellaRanks = cronquistService.saveCronquist(classification, wiki);
    //
    //            wiki = "https://fr.wikipedia.org/wiki/Aldrovanda";
    //            classification = wikipediaCrawler.scrapWiki(wiki);
    //            Collection<CronquistRank> aldrovandaRanks = cronquistService.saveCronquist(classification, wiki);
    //
    //            wiki = "https://fr.wikipedia.org/wiki/Amphorogyne";// Rosidae
    //            classification = wikipediaCrawler.scrapWiki(wiki);
    //            Collection<CronquistRank> amphorogyneRanks = cronquistService.saveCronquist(classification, wiki);
    //
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //        }
    //    }


    //    @Test
    //    public void enregistrementDUneClassificationAvecDeuxRangsSynonymesSuccessifs2() {
    //        List<String> wikis = new ArrayList<>();
    //        wikis.add("https://fr.wikipedia.org/wiki/Bridgesia_incisifolia");
    //        wikis.add("https://fr.wikipedia.org/wiki/%C3%89rable_de_Miyabe");
    //        wikis.add("https://fr.wikipedia.org/wiki/%C3%89rable_de_Montpellier");
    //        //         => vérifier la synonymie + les enfants ont bien été mergés
    //    }
    //
    //    @Test
    //    public void testAvecToutConfonduDansTousLesSens() {
    //        List<String> wikis = new ArrayList<>();
    //        wikis.add("https://fr.wikipedia.org/wiki/Acanthe");
    //        wikis.add("https://fr.wikipedia.org/wiki/Acanthe_%C3%A0_feuilles_molles");
    //        wikis.add("https://fr.wikipedia.org/wiki/Acanthus_spinosus");
    //        wikis.add("https://fr.wikipedia.org/wiki/Acer_campestre");
    //        wikis.add("https://fr.wikipedia.org/wiki/Acer_cappadocicum");
    //        wikis.add("https://fr.wikipedia.org/wiki/Acer_carpinifolium");
    //        wikis.add("https://fr.wikipedia.org/wiki/Acer_chaneyi");
    //        wikis.add("https://fr.wikipedia.org/wiki/Acer_circinatum");
    //        wikis.add("https://fr.wikipedia.org/wiki/Acer_davidii");
    //        wikis.add("https://fr.wikipedia.org/wiki/Acer_douglasense");
    //        wikis.add("https://fr.wikipedia.org/wiki/Acer_velutinum");
    //        wikis.add("https://fr.wikipedia.org/wiki/Aldrovanda");
    //        wikis.add("https://fr.wikipedia.org/wiki/Amphorogyne");// Rosidae
    //        wikis.add("https://fr.wikipedia.org/wiki/Andrographis");
    //        wikis.add("https://fr.wikipedia.org/wiki/Andrographis_paniculata");
    //        wikis.add("https://fr.wikipedia.org/wiki/Anisacanthus");
    //        wikis.add("https://fr.wikipedia.org/wiki/Anisoptera_(v%C3%A9g%C3%A9tal)");
    //        wikis.add("https://fr.wikipedia.org/wiki/Anthobolus");// Rosidae
    //        wikis.add("https://fr.wikipedia.org/wiki/Aphelandra");
    //        wikis.add("https://fr.wikipedia.org/wiki/Aphelandra_sinclairiana");
    //        wikis.add("https://fr.wikipedia.org/wiki/Arjona");// Rosidae// : merge branch
    //        wikis.add("https://fr.wikipedia.org/wiki/Asystasia");
    //        wikis.add("https://fr.wikipedia.org/wiki/Asystasia_gangetica");
    //        wikis.add("https://fr.wikipedia.org/wiki/Atalaya_(genre)");// : merge branch
    //        wikis.add("https://fr.wikipedia.org/wiki/Avicennia_germinans");
    //        wikis.add("https://fr.wikipedia.org/wiki/Barleria");
    //        wikis.add("https://fr.wikipedia.org/wiki/Barleria_cristata");
    //        wikis.add("https://fr.wikipedia.org/wiki/Barleria_obtusa");
    //        wikis.add("https://fr.wikipedia.org/wiki/Barleriola");
    //        wikis.add("https://fr.wikipedia.org/wiki/Blackstonia");
    //        wikis.add("https://fr.wikipedia.org/wiki/Blechum");
    //        wikis.add("https://fr.wikipedia.org/wiki/Bois_de_Judas");// Rosidae// : merge branch
    //        wikis.add("https://fr.wikipedia.org/wiki/Bridgesia_incisifolia");
    //        wikis.add("https://fr.wikipedia.org/wiki/Buckleya");
    //        wikis.add("https://fr.wikipedia.org/wiki/%C3%89rable_argent%C3%A9");
    //        wikis.add("https://fr.wikipedia.org/wiki/%C3%89rable_%C3%A0_%C3%A9corce_de_papier");
    //        wikis.add("https://fr.wikipedia.org/wiki/%C3%89rable_%C3%A0_%C3%A9pis");
    //        wikis.add("https://fr.wikipedia.org/wiki/%C3%89rable_%C3%A0_cinq_folioles");
    //        wikis.add("https://fr.wikipedia.org/wiki/%C3%89rable_%C3%A0_feuille_de_vigne");
    //        wikis.add("https://fr.wikipedia.org/wiki/%C3%89rable_%C3%A0_feuilles_d%27obier");
    //        wikis.add("https://fr.wikipedia.org/wiki/%C3%89rable_%C3%A0_grandes_feuilles");
    //        wikis.add("https://fr.wikipedia.org/wiki/%C3%89rable_champ%C3%AAtre");
    //        wikis.add("https://fr.wikipedia.org/wiki/%C3%89rable_de_Cappadoce");
    //        wikis.add("https://fr.wikipedia.org/wiki/%C3%89rable_de_Cr%C3%A8te");
    //        wikis.add("https://fr.wikipedia.org/wiki/%C3%89rable_de_Miyabe");
    //        wikis.add("https://fr.wikipedia.org/wiki/%C3%89rable_de_Montpellier");
    //        wikis.add("https://fr.wikipedia.org/wiki/%C3%89rable_de_Pennsylvanie");
    //        wikis.add("https://fr.wikipedia.org/wiki/Carlowrightia");
    //        wikis.add("https://fr.wikipedia.org/wiki/Centaurium");
    //        wikis.add("https://fr.wikipedia.org/wiki/Cervantesia");
    //        wikis.add("https://fr.wikipedia.org/wiki/Chironia");
    //        wikis.add("https://fr.wikipedia.org/wiki/Corylopsis");// Synonymes
    //        wikis.add("https://fr.wikipedia.org/wiki/Cossinia");// : merge branch
    //        wikis.add("https://fr.wikipedia.org/wiki/Deinanthe");
    //        wikis.add("https://fr.wikipedia.org/wiki/Diatenopteryx_sorbifolia");
    //        wikis.add("https://fr.wikipedia.org/wiki/Dicliptera");
    //        wikis.add("https://fr.wikipedia.org/wiki/Dicliptera_suberecta");
    //        wikis.add("https://fr.wikipedia.org/wiki/Dipterocarpus");
    //        wikis.add("https://fr.wikipedia.org/wiki/Distylium");// Synonymes
    //        wikis.add("https://fr.wikipedia.org/wiki/Eremophila_latrobei");
    //        wikis.add("https://fr.wikipedia.org/wiki/Eremophila_mitchellii");
    //        wikis.add("https://fr.wikipedia.org/wiki/Eremophila_nivea");
    //        wikis.add("https://fr.wikipedia.org/wiki/Eremophila_(plante)");
    //        wikis.add("https://fr.wikipedia.org/wiki/Graptophyllum");
    //        wikis.add("https://fr.wikipedia.org/wiki/Hemigraphis");
    //        wikis.add("https://fr.wikipedia.org/wiki/Huaceae");
    //        wikis.add("https://fr.wikipedia.org/wiki/Hygrophila_(plante)");
    //        wikis.add("https://fr.wikipedia.org/wiki/Hygrophila_polysperma");
    //        wikis.add("https://fr.wikipedia.org/wiki/Justicia_adhatoda");
    //        wikis.add("https://fr.wikipedia.org/wiki/Justicia_aurea");
    //        wikis.add("https://fr.wikipedia.org/wiki/Justicia_betonica");
    //        wikis.add("https://fr.wikipedia.org/wiki/Justicia_californica");
    //        wikis.add("https://fr.wikipedia.org/wiki/Justicia_carnea");
    //        wikis.add("https://fr.wikipedia.org/wiki/Justicia_gendarussa");
    //        wikis.add("https://fr.wikipedia.org/wiki/Justicia_spicigera");
    //        wikis.add("https://fr.wikipedia.org/wiki/Kielmeyera");
    //        wikis.add("https://fr.wikipedia.org/wiki/Lepisanthes_senegalensis");
    //        wikis.add("https://fr.wikipedia.org/wiki/Loropetalum");
    //        wikis.add("https://fr.wikipedia.org/wiki/Lyallia_kerguelensis");
    //        wikis.add("https://fr.wikipedia.org/wiki/Molinadendron");
    //        wikis.add("https://fr.wikipedia.org/wiki/Monodiella");
    //        wikis.add("https://fr.wikipedia.org/wiki/Odontonema");
    //        wikis.add("https://fr.wikipedia.org/wiki/Oxera_pulchella");
    //        wikis.add("https://fr.wikipedia.org/wiki/Phlogacanthus");
    //        wikis.add("https://fr.wikipedia.org/wiki/Phlogacanthus_turgidus");
    //        wikis.add("https://fr.wikipedia.org/wiki/Picanier_jaune");
    //        wikis.add("https://fr.wikipedia.org/wiki/Pseuderanthemum");
    //        wikis.add("https://fr.wikipedia.org/wiki/Ptychospermatinae");
    //        wikis.add("https://fr.wikipedia.org/wiki/Ruellia");
    //        wikis.add("https://fr.wikipedia.org/wiki/Ruellia_brevifolia");
    //        wikis.add("https://fr.wikipedia.org/wiki/Ruellia_chartacea");
    //        wikis.add("https://fr.wikipedia.org/wiki/Ruellia_devosiana");
    //        wikis.add("https://fr.wikipedia.org/wiki/Ruellia_geminiflora");
    //        wikis.add("https://fr.wikipedia.org/wiki/Ruellia_schnellii");
    //        wikis.add("https://fr.wikipedia.org/wiki/Ruellia_simplex");
    //        wikis.add("https://fr.wikipedia.org/wiki/Ruellia_tuberosa");
    //        wikis.add("https://fr.wikipedia.org/wiki/Sanchezia");
    //        wikis.add("https://fr.wikipedia.org/wiki/Strobilanthes_kunthiana");
    //        wikis.add("https://fr.wikipedia.org/wiki/Thunbergia");
    //        wikis.add("https://fr.wikipedia.org/wiki/Thunbergia_alata");
    //        wikis.add("https://fr.wikipedia.org/wiki/Thunbergia_erecta");
    //        wikis.add("https://fr.wikipedia.org/wiki/Thunbergia_fragrans");
    //        wikis.add("https://fr.wikipedia.org/wiki/Thunbergia_grandiflora");
    //        wikis.add("https://fr.wikipedia.org/wiki/Thunbergia_mysorensis");
    //        wikis.add("https://fr.wikipedia.org/wiki/Whitfieldia");
    //        wikis.add("https://fr.wikipedia.org/wiki/Whitfieldia_elongata");
    //        //         => vérifier la synonymie + les enfants ont bien été mergés
    //    }

}
