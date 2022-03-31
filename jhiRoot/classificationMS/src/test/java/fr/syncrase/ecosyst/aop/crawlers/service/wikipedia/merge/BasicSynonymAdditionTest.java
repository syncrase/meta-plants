package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.merge;

import fr.syncrase.ecosyst.ClassificationMsApp;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.TestUtils;
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
import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClassificationMsApp.class)
public class BasicSynonymAdditionTest {

    WikipediaCrawler wikipediaCrawler;

    TestUtils utils = new TestUtils();

    @Autowired
    private CronquistService cronquistService;

    public BasicSynonymAdditionTest() {
        this.wikipediaCrawler = new WikipediaCrawler(cronquistService);
    }

    @Test
    public void mergeDesBranchesAvecSynonymes() {
        CronquistClassificationBranch classification;
        try {
            // Règne 	Plantae
            //Sous-règne 	Tracheobionta
            //Division 	Magnoliophyta
            //Classe 	Magnoliopsida
            //Sous-classe 	Hamamelidae
            //Ordre 	Saxifragales
            //Famille 	Hamamelidaceae
            //Genre Corylopsis
            String wiki = "https://fr.wikipedia.org/wiki/Corylopsis";// Synonymes ORDRE Saxifragales
            classification = wikipediaCrawler.scrapWiki(wiki);
            Collection<CronquistRank> corylopsisRanks = cronquistService.saveCronquist(classification, wiki);
            LinkedMap<CronquistTaxonomikRanks, CronquistRank> corylopsisClassification = utils.transformToMapOfRanksByName(corylopsisRanks);
            Set<String> corylopsisOrdreNames = utils.getNamesFromMapRank(corylopsisClassification, CronquistTaxonomikRanks.ORDRE);
            assertEquals("Corylopsis ne doit contenir qu'un seul ordre", 1, corylopsisOrdreNames.size());
            assertTrue("L'ordre de corylopsis doit être Saxifragales", corylopsisOrdreNames.contains("Saxifragales"));

            // Règne 	Plantae
            //Sous-règne 	Tracheobionta
            //Division 	Magnoliophyta
            //Classe 	Magnoliopsida
            //Sous-classe 	Hamamelidae
            //Ordre 	Hamamelidales
            //Famille 	Hamamelidaceae
            //Genre Distylium
            wiki = "https://fr.wikipedia.org/wiki/Distylium";// Synonymes ORDRE Hamamelidales
            classification = wikipediaCrawler.scrapWiki(wiki);
            Collection<CronquistRank> distyliumRanks = cronquistService.saveCronquist(classification, wiki);
            LinkedMap<CronquistTaxonomikRanks, CronquistRank> distyliumClassification = utils.transformToMapOfRanksByName(distyliumRanks);
            Set<String> distyliumOrdres = utils.getNamesFromMapRank(distyliumClassification, CronquistTaxonomikRanks.ORDRE);
            assertEquals("Distylium ne doit contenir que deux ordres", 2, distyliumOrdres.size());
            assertTrue("L'ordre de distylium doit être Hamamelidales et Saxifragales", distyliumOrdres.containsAll(Set.of("Hamamelidales", "Saxifragales")));

            // Règne 	Plantae
            //Sous-règne 	Tracheobionta
            //Division 	Magnoliophyta
            //Classe 	Magnoliopsida
            //Sous-classe 	Hamamelidae
            //Ordre 	Hamamelidales
            //Famille 	Hamamelidaceae
            //Genre Loropetalum
            wiki = "https://fr.wikipedia.org/wiki/Loropetalum";// Synonymes ORDRE Hamamelidales TODO vraiment nécessaire ? Mes synonymes sont déjà pris en compte
            classification = wikipediaCrawler.scrapWiki(wiki);
            Collection<CronquistRank> loropetalumRanks = cronquistService.saveCronquist(classification, wiki);
            LinkedMap<CronquistTaxonomikRanks, CronquistRank> loropetalumClassification = utils.transformToMapOfRanksByName(loropetalumRanks);
            Set<String> loropetalumOrdres = utils.getNamesFromMapRank(loropetalumClassification, CronquistTaxonomikRanks.ORDRE);
            assertEquals("Loropetalum ne doit contenir que deux ordres", 2, loropetalumOrdres.size());
            assertTrue("L'ordre de loropetalum doit être Hamamelidales et Saxifragales", loropetalumOrdres.containsAll(Set.of("Hamamelidales", "Saxifragales")));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void enregistrementDUnRangSynonymeAvecMerge() {
        try {
            CronquistClassificationBranch classification;
            // Ordre Lamiales & Classe Magnoliopsida
            // Règne 	Plantae
            //Sous-règne 	Tracheobionta
            //Division 	Magnoliophyta
            //Classe 	Magnoliopsida
            //Ordre 	Lamiales
            //Famille 	Verbenaceae
            //Genre 	Oxera
            //Espèce Oxera neriifolia
            String wiki = "https://fr.wikipedia.org/wiki/Oxera_neriifolia";
            classification = wikipediaCrawler.scrapWiki(wiki);
            //            Collection<CronquistRank> neriifoliaRanks =
            cronquistService.saveCronquist(classification, wiki);
            //            LinkedMap<CronquistTaxonomikRanks, CronquistRank> neriifoliaClassification = transformToMapOfRanksByName(neriifoliaRanks);

            // Ordre Asparagales
            //            wiki = "https://fr.wikipedia.org/wiki/Hostaceae";
            //            classification = wikipediaCrawler.scrapWiki(wiki);
            //            Collection<CronquistRank> hostaceaeRanks = cronquistService.saveCronquist(classification, wiki);
            //            LinkedMap<CronquistTaxonomikRanks, CronquistRank> hostaceaeClassification = transformToMapOfRanksByName(hostaceaeRanks);

            // Ordre Lamiales & Classe Equisetopsida
            // Règne 	Plantae
            //Sous-règne 	Tracheobionta
            //Division 	Magnoliophyta
            //Classe 	Equisetopsida
            //Ordre 	Lamiales
            //Famille Selaginaceae
            wiki = "https://fr.wikipedia.org/wiki/Selaginaceae";
            classification = wikipediaCrawler.scrapWiki(wiki);
            Collection<CronquistRank> selaginaceaeRanks = cronquistService.saveCronquist(classification, wiki);
            LinkedMap<CronquistTaxonomikRanks, CronquistRank> selaginaceaeClassification = utils.transformToMapOfRanksByName(selaginaceaeRanks);

            // Les ordres Asparagales & Lamiales sont définis comme synonymes
            Set<String> selaginaceaeClasses = utils.getNamesFromMapRank(selaginaceaeClassification, CronquistTaxonomikRanks.CLASSE);
            assertEquals("Selaginaceae doit contenir deux classes", 2, selaginaceaeClasses.size());
            assertTrue("Les classes de Selaginaceae doivent être Equisetopsida et Magnoliopsida", selaginaceaeClasses.containsAll(Set.of("Equisetopsida", "Magnoliopsida")));
            // Tester aussi hostaceae et oxera ?

            //            CronquistClassificationBranch classificationBranchOfOxeraNeriifolia = cronquistService.getClassificationById(neriifoliaClassification.get(neriifoliaClassification.lastKey()).getId());
            //            getNamesFromMapRank(classificationBranchOfOxeraNeriifolia, CronquistTaxonomikRanks.SOUSCLASSE);
            //            assertTrue(
            //                "Oxera neriifolia doit posséder la Sous-classe Magnoliidae",
            //                classificationBranchOfOxeraNeriifolia.getRang(CronquistTaxonomikRanks.SOUSCLASSE).getNoms().stream().map(AtomicClassificationNom::getNomFr).collect(Collectors.toSet()).contains("Magnoliidae")
            //                      );
            //            assertTrue(
            //                "Oxera neriifolia doit posséder le Super-ordre Lilianae",
            //                classificationBranchOfOxeraNeriifolia.getRang(CronquistTaxonomikRanks.SUPERORDRE).getNoms().stream().map(AtomicClassificationNom::getNomFr).collect(Collectors.toSet()).contains("Lilianae")
            //                      );

            //            CronquistClassificationBranch classificationBranchOfSelaginaceae = cronquistService.getClassificationById(selaginaceaeClassification.get(selaginaceaeClassification.lastKey()).getId());
            //            assertTrue(
            //                "Selaginaceae doit posséder la Sous-classe Magnoliidae",
            //                classificationBranchOfSelaginaceae.getRang(CronquistTaxonomikRanks.SOUSCLASSE).getNoms().stream().map(AtomicClassificationNom::getNomFr).collect(Collectors.toSet()).contains("Magnoliidae")
            //                      );
            //            assertTrue(
            //                "Selaginaceae doit posséder la Sous-classe Magnoliidae et le Super-ordre Lilianae",
            //                classificationBranchOfSelaginaceae.getRang(CronquistTaxonomikRanks.SUPERORDRE).getNoms().stream().map(AtomicClassificationNom::getNomFr).collect(Collectors.toSet()).contains("Lilianae")
            //                      );

            //         => vérifier la synonymie + les enfants ont bien été mergés
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
