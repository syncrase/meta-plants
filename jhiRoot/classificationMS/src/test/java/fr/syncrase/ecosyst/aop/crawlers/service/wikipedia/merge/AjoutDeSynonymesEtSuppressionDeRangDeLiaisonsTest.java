package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.merge;

import fr.syncrase.ecosyst.ClassificationMsApp;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.TestUtils;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClassificationMsApp.class)
public class AjoutDeSynonymesEtSuppressionDeRangDeLiaisonsTest {

    WikipediaCrawler wikipediaCrawler;

    TestUtils utils = new TestUtils();

    @Autowired
    private CronquistService cronquistService;

    public AjoutDeSynonymesEtSuppressionDeRangDeLiaisonsTest() {
        this.wikipediaCrawler = new WikipediaCrawler(cronquistService);
    }

    @Test
    public void enregistrementDUneClassificationAvecDeuxRangsSynonymesSuccessifs() {
        try {
            CronquistClassificationBranch classification;
            // Règne 	Plantae
            //Sous-règne 	doit être ajouté > Tracheobionta// TODO rang de liaison supprimé ?
            //Division 	doit être ajouté > Magnoliophyta// TODO rang de liaison supprimé ?
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
            //Super-ordre 	doit être ajouté > Rosanae// TODO rang de liaison supprimé ?
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
}
