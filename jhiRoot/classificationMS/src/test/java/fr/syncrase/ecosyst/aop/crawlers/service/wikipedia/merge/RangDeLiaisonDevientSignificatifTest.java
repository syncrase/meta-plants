package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.merge;

import fr.syncrase.ecosyst.ClassificationMsApp;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.TestUtils;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.AtomicClassificationNom;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.AtomicCronquistRank;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.CronquistClassificationBranch;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.crawler.WikipediaCrawler;
import fr.syncrase.ecosyst.domain.CronquistRank;
import fr.syncrase.ecosyst.domain.enumeration.CronquistTaxonomikRanks;
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
public class RangDeLiaisonDevientSignificatifTest {

    WikipediaCrawler wikipediaCrawler;

    TestUtils utils = new TestUtils();

    @Autowired
    private CronquistService cronquistService;

    public RangDeLiaisonDevientSignificatifTest() {
        this.wikipediaCrawler = new WikipediaCrawler(cronquistService);
    }

    @Test
    public void mergeDesBranchesSimples() {
        try {
            CronquistClassificationBranch classification;
            String wiki = "https://fr.wikipedia.org/wiki/Arjona";
            classification = wikipediaCrawler.scrapWiki(wiki);
            Collection<CronquistRank> arjonaRanks = cronquistService.saveCronquist(classification, wiki);
            LinkedMap<CronquistTaxonomikRanks, CronquistRank> arjonaClassification = utils.transformToMapOfRanksByName(arjonaRanks);

            // Les plantes suivantes appartiennent à la sous-classe des Rosidae, mais on ne le sait pas pour atalaya. On le découvre quand on enregistre Cossinia
            wiki = "https://fr.wikipedia.org/wiki/Atalaya_(genre)";
            classification = wikipediaCrawler.scrapWiki(wiki);
            Collection<CronquistRank> atalayaRanks = cronquistService.saveCronquist(classification, wiki);
            LinkedMap<CronquistTaxonomikRanks, CronquistRank> atalayaClassification = utils.transformToMapOfRanksByName(atalayaRanks);
            // Lors de cet ajout : le rang de liaison sous-règne prend le nom tracheobionta
            assertEquals("Le sous-règne tracheobionta doit avoir été ajoutée dans la classification de atalaya",
                         atalayaClassification.get(CronquistTaxonomikRanks.SOUSREGNE).getId(),
                         arjonaClassification.get(CronquistTaxonomikRanks.SOUSREGNE).getId()
                        );
            for (CronquistTaxonomikRanks rank : CronquistTaxonomikRanks.values()) {
                assertEquals("De la classe au règne les deux classifications doivent être égales",
                             atalayaClassification.get(rank).getId(),
                             arjonaClassification.get(rank).getId()
                            );
                if (rank.ordinal() >= CronquistTaxonomikRanks.CLASSE.ordinal()) {
                    break;
                }
            }

            wiki = "https://fr.wikipedia.org/wiki/Cossinia";
            classification = wikipediaCrawler.scrapWiki(wiki);
            cronquistService.saveCronquist(classification, wiki);// TODO le test lancé tout seul plante en erreur transient

            Long atalayaId = atalayaClassification.get(atalayaClassification.lastKey()).getId();
            CronquistClassificationBranch newAtalayaClassification = cronquistService.getClassificationBranchOfThisRank(atalayaId);
            Long rosidaeId = arjonaClassification.get(CronquistTaxonomikRanks.SOUSCLASSE).getId();
            Long sousClasseId = newAtalayaClassification.getRang(CronquistTaxonomikRanks.SOUSCLASSE).getId();
            assertEquals("La sous-classe rosidae doit avoir été ajoutée dans la classification d'atalaya",
                         sousClasseId,
                         rosidaeId
                        );

            Set<AtomicCronquistRank> taxonsOfRosidae = cronquistService.getTaxonsOf(rosidaeId);
            assertEquals(
                "Rosidae doit posséder deux taxons de liaison (vers Santatales et vers Sapindales)",
                2,
                taxonsOfRosidae.size()
                        );
//            CronquistClassificationBranch sousClasseBeforeMerge = cronquistService.getClassificationBranchOfThisRank(arjonaClassification.get(CronquistTaxonomikRanks.SOUSCLASSE).getId());
//            assertNull("La sous-classe d'arjona doit avoir été supprimée car mergée avec le rang de liaison d'Atalaya", sousClasseBeforeMerge);

        } catch (IOException e) {
            fail("unable to scrap wiki : " + e.getMessage());
        }

    }

    @Test
    public void transformationDUnRangDeLiaisonEnRangSignificatif() {
        CronquistClassificationBranch classification;
        try {
            String wiki = "https://fr.wikipedia.org/wiki/Chironia";
            classification = wikipediaCrawler.scrapWiki(wiki);
            Collection<CronquistRank> chironiaRanks = cronquistService.saveCronquist(classification, wiki);
            LinkedMap<CronquistTaxonomikRanks, CronquistRank> chironiaClassification = utils.transformToMapOfRanksByName(chironiaRanks);

            wiki = "https://fr.wikipedia.org/wiki/Monodiella";
            classification = wikipediaCrawler.scrapWiki(wiki);
            Collection<CronquistRank> monodiellaRanks = cronquistService.saveCronquist(classification, wiki);
            LinkedMap<CronquistTaxonomikRanks, CronquistRank> monodiellaClassification = utils.transformToMapOfRanksByName(monodiellaRanks);

            CronquistClassificationBranch classificationBranchOfChironia = cronquistService.getClassificationBranchOfThisRank(chironiaClassification.get(chironiaClassification.lastKey()).getId());
            assertEquals("Le sous règne Tracheobionta doit avoir été ajouté a chironia",
                         monodiellaClassification.get(CronquistTaxonomikRanks.SOUSREGNE).getId(),
                         classificationBranchOfChironia.getRang(CronquistTaxonomikRanks.SOUSREGNE).getId()
                        );
        } catch (IOException e) {
            e.printStackTrace();
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
            Collection<CronquistRank> erableCreteRanks = cronquistService.saveCronquist(classification, wiki);
            LinkedMap<CronquistTaxonomikRanks, CronquistRank> erableCreteClassification = utils.transformToMapOfRanksByName(erableCreteRanks);

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
            Collection<CronquistRank> erableMiyabeRanks = cronquistService.saveCronquist(classification, wiki);
            LinkedMap<CronquistTaxonomikRanks, CronquistRank> erableMiyabeClassification = utils.transformToMapOfRanksByName(erableMiyabeRanks);

            // L'érable de miyabe
            // - doit posséder le superordre Rosanae
            // - doit posséder la sous-classe Magnoliidae en plus de Rosidae
            // - doit posséder la classe Equisetopsida en plus de Magnoliopsida
            Set<String> ordresDeErableMiyabe = utils.getNamesFromMapRank(erableMiyabeClassification, CronquistTaxonomikRanks.SUPERORDRE);
            assertTrue("L'érable de Miyabe doit posséder le superordre Rosanae", ordresDeErableMiyabe.contains("Rosanae"));

            Set<String> nomsDeSousClasseDeErableMiyabe = utils.getNamesFromMapRank(erableMiyabeClassification, CronquistTaxonomikRanks.SOUSCLASSE);
            assertEquals("L'érable de Miyabe doit contenir deux sous-classes", 2, nomsDeSousClasseDeErableMiyabe.size());
            assertTrue("L'érable de Miyabe doit posséder la sous-classe synonyme Magnoliidae", nomsDeSousClasseDeErableMiyabe.containsAll(Set.of("Magnoliidae", "Rosidae")));

            Set<String> nomsDeClasseDeErableMiyabe = utils.getNamesFromMapRank(erableMiyabeClassification, CronquistTaxonomikRanks.CLASSE);
            assertEquals("L'érable de Miyabe doit contenir deux classes", 2, nomsDeClasseDeErableMiyabe.size());
            assertTrue("L'érable de Miyabe doit posséder la classe synonyme Equisetopsida", nomsDeClasseDeErableMiyabe.containsAll(Set.of("Equisetopsida", "Magnoliopsida")));

            // L'érable de Crête
            // - doit posséder la sous-classe Rosidae en plus de Magnoliidae
            // - doit posséder la classe Magnoliopsida en plus de Equisetopsida
            // - doit posséder la division Magnoliophyta
            // - doit posséder le sous-règne Tracheobionta
            CronquistClassificationBranch classificationBranchOfErableCrete = cronquistService.getClassificationBranchOfThisRank(erableCreteClassification.get(erableCreteClassification.lastKey()).getId());

            Set<String> nomsDeSousClasseDeErableCrete = classificationBranchOfErableCrete.getRang(CronquistTaxonomikRanks.SOUSCLASSE).getNoms().stream().map(AtomicClassificationNom::getNomFr).collect(Collectors.toSet());
            assertEquals("L'érable de Crete doit contenir deux sous-classes", 2, nomsDeSousClasseDeErableCrete.size());
            assertTrue("L'érable de Crete doit posséder la sous-classes synonyme Rosidae", nomsDeSousClasseDeErableCrete.containsAll(Set.of("Rosidae", "Magnoliidae")));

            Set<String> nomsDeClasseDeErableCrete = classificationBranchOfErableCrete.getRang(CronquistTaxonomikRanks.CLASSE).getNoms().stream().map(AtomicClassificationNom::getNomFr).collect(Collectors.toSet());
            assertEquals("L'érable de Crete doit contenir deux classes", 2, nomsDeClasseDeErableCrete.size());
            assertTrue("L'érable de Crete doit posséder la classes synonyme Magnoliopsida", nomsDeClasseDeErableCrete.containsAll(Set.of("Magnoliopsida", "Equisetopsida")));

            Set<String> nomsDEmbranchementDeErableCrete = classificationBranchOfErableCrete.getRang(CronquistTaxonomikRanks.EMBRANCHEMENT).getNoms().stream().map(AtomicClassificationNom::getNomFr).collect(Collectors.toSet());
            assertTrue("L'érable de Crete doit posséder l'embranchement Magnoliophyta", nomsDEmbranchementDeErableCrete.contains("Magnoliophyta"));

            Set<String> nomsDeSousRegneDeErableCrete = classificationBranchOfErableCrete.getRang(CronquistTaxonomikRanks.SOUSREGNE).getNoms().stream().map(AtomicClassificationNom::getNomFr).collect(Collectors.toSet());
            assertTrue("L'érable de Crete doit posséder le sous-règne Tracheobionta", nomsDeSousRegneDeErableCrete.contains("Tracheobionta"));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
