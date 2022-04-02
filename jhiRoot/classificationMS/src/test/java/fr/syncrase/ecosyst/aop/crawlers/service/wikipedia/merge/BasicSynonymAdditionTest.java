package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.merge;

import fr.syncrase.ecosyst.ClassificationMsApp;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.TestUtils;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.CronquistClassificationBranch;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.crawler.WikipediaCrawler;
import fr.syncrase.ecosyst.domain.enumeration.RankName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
            CronquistClassificationBranch corylopsisClassification = cronquistService.saveCronquist(classification, wiki);
            Set<String> corylopsisOrdreNames = utils.getRankNames(corylopsisClassification, RankName.ORDRE);
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
            CronquistClassificationBranch distyliumClassification = cronquistService.saveCronquist(classification, wiki);
            Set<String> names = Set.of("Hamamelidales",
                                       "Saxifragales");
            utils.checkThatRankContainsOnlyTheseNames(
                distyliumClassification,
                RankName.ORDRE,
                names
                                                     );

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
            CronquistClassificationBranch loropetalumClassification = cronquistService.saveCronquist(classification, wiki);
            names = Set.of("Hamamelidales",
                           "Saxifragales");
            utils.checkThatRankContainsOnlyTheseNames(
                loropetalumClassification,
                RankName.ORDRE,
                names);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void enregistrementDUnRangSynonymeAvecMerge() {
        try {
            CronquistClassificationBranch classification;
            // Règne 	Plantae
            //Sous-règne 	Tracheobionta
            //Division 	Magnoliophyta
            //Classe 	Magnoliopsida (+Equisetopsida)
            //Ordre 	Lamiales
            //Famille 	Verbenaceae
            //Genre 	Oxera
            //Espèce Oxera neriifolia
            String wiki = "https://fr.wikipedia.org/wiki/Oxera_neriifolia";
            classification = wikipediaCrawler.scrapWiki(wiki);
            CronquistClassificationBranch neriifoliaClassification = cronquistService.saveCronquist(classification, wiki);
            //            LinkedMap<RankName, ICronquistRank> neriifoliaClassification = utils.transformToMapOfRanksByName(neriifoliaRanks);

            // Ordre Asparagales
            //            wiki = "https://fr.wikipedia.org/wiki/Hostaceae";
            //            classification = wikipediaCrawler.scrapWiki(wiki);
            //            Collection<CronquistRank> hostaceaeRanks = cronquistService.saveCronquist(classification, wiki);
            //            LinkedMap<RankName, CronquistRank> hostaceaeClassification = transformToMapOfRanksByName(hostaceaeRanks);

            // Règne 	Plantae
            //Sous-règne 	Tracheobionta
            //Division 	Magnoliophyta
            //Classe 	Equisetopsida (+Magnoliopsida)
            //Ordre 	Lamiales
            //Famille Selaginaceae
            wiki = "https://fr.wikipedia.org/wiki/Selaginaceae";
            classification = wikipediaCrawler.scrapWiki(wiki);
            CronquistClassificationBranch selaginaceaeClassification = cronquistService.saveCronquist(classification, wiki);
            //            LinkedMap<RankName, ICronquistRank> selaginaceaeClassification = utils.transformToMapOfRanksByName(selaginaceaeRanks);

            Set<String> names = Set.of("Magnoliopsida",
                                       "Equisetopsida");
            utils.checkThatRankContainsOnlyTheseNames(
                selaginaceaeClassification,
                RankName.CLASSE,
                names
                                                     );

            //            CronquistClassificationBranch neriifoliaNewClassification = cronquistService.getClassificationById(neriifoliaClassification.get(RankName.ORDRE).getId());
            //            utils.checkThatRankContainsOnlyTheseNames(
            //                neriifoliaNewClassification,// TODO Mettre des interface autour des objets
            //                RankName.CLASSE,
            //                names
            //                                                     );

            CronquistClassificationBranch neriifoliaPartialClassification = cronquistService.getClassificationById(neriifoliaClassification.getRang(RankName.ORDRE).getId());
            CronquistClassificationBranch selaginaceaePartialClassification = cronquistService.getClassificationById(selaginaceaeClassification.getRang(RankName.ORDRE).getId());

            utils.assertThatClassificationsAreTheSame(neriifoliaPartialClassification, selaginaceaePartialClassification);

            //            CronquistClassificationBranch classificationBranchOfOxeraNeriifolia = cronquistService.getClassificationById(neriifoliaClassification.get(neriifoliaClassification.lastKey()).getId());
            //            getRankNames(classificationBranchOfOxeraNeriifolia, RankName.SOUSCLASSE);
            //            assertTrue(
            //                "Oxera neriifolia doit posséder la Sous-classe Magnoliidae",
            //                classificationBranchOfOxeraNeriifolia.getRang(RankName.SOUSCLASSE).getNoms().stream().map(AtomicClassificationNom::getNomFr).collect(Collectors.toSet()).contains("Magnoliidae")
            //                      );
            //            assertTrue(
            //                "Oxera neriifolia doit posséder le Super-ordre Lilianae",
            //                classificationBranchOfOxeraNeriifolia.getRang(RankName.SUPERORDRE).getNoms().stream().map(AtomicClassificationNom::getNomFr).collect(Collectors.toSet()).contains("Lilianae")
            //                      );

            //            CronquistClassificationBranch classificationBranchOfSelaginaceae = cronquistService.getClassificationById(selaginaceaeClassification.get(selaginaceaeClassification.lastKey()).getId());
            //            assertTrue(
            //                "Selaginaceae doit posséder la Sous-classe Magnoliidae",
            //                classificationBranchOfSelaginaceae.getRang(RankName.SOUSCLASSE).getNoms().stream().map(AtomicClassificationNom::getNomFr).collect(Collectors.toSet()).contains("Magnoliidae")
            //                      );
            //            assertTrue(
            //                "Selaginaceae doit posséder la Sous-classe Magnoliidae et le Super-ordre Lilianae",
            //                classificationBranchOfSelaginaceae.getRang(RankName.SUPERORDRE).getNoms().stream().map(AtomicClassificationNom::getNomFr).collect(Collectors.toSet()).contains("Lilianae")
            //                      );

            //         => vérifier la synonymie + les enfants ont bien été mergés
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
