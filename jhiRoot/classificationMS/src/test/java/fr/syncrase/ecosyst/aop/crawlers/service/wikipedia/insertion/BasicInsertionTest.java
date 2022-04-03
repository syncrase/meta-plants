package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.insertion;

import fr.syncrase.ecosyst.ClassificationMsApp;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.TestUtils;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.CronquistClassificationBranch;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.entities.classification.AtomicClassificationNom;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.crawler.WikipediaCrawler;
import fr.syncrase.ecosyst.domain.ClassificationNom;
import fr.syncrase.ecosyst.domain.IClassificationNom;
import fr.syncrase.ecosyst.domain.ICronquistRank;
import fr.syncrase.ecosyst.domain.enumeration.RankName;
import fr.syncrase.ecosyst.repository.ClassificationNomRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.entities.classification.AtomicClassificationNom.getAtomicClassificationNomTreeSet;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClassificationMsApp.class)
public class BasicInsertionTest {

    WikipediaCrawler wikipediaCrawler;

    TestUtils utils = new TestUtils();


    @Autowired
    private ClassificationNomRepository classificationNomRepository;

    @Autowired
    private CronquistService cronquistService;

    public BasicInsertionTest() {
        this.wikipediaCrawler = new WikipediaCrawler(cronquistService);
    }

    @Test
    public void enregistrementDUneClassificationDontTousLesRangsSontInconnus() {
        // Règne 	Plantae
        //Sous-règne 	Tracheobionta
        //Division 	Magnoliophyta
        //Classe 	Magnoliopsida
        //Sous-classe 	Dilleniidae
        //Ordre 	Nepenthales
        //Famille 	Droseraceae
        //Genre Aldrovanda
        String wiki = "https://fr.wikipedia.org/wiki/Aldrovanda";
        CronquistClassificationBranch classification;
        try {
            classification = wikipediaCrawler.scrapWiki(wiki);
            CronquistClassificationBranch cronquistRanks = cronquistService.saveCronquist(classification, wiki);
            cronquistRanks.getClassificationBranch().forEach((rankName, cronquistRank) -> assertNotNull(cronquistRank.getId()));
        } catch (IOException e) {
            fail("unable to scrap wiki : " + e.getMessage());
        }
    }

    @Test
    public void enregistrementDUneClassificationEntierementConnu() {
        // Règne 	Plantae
        //Sous-règne 	Tracheobionta
        //Division 	Magnoliophyta
        //Classe 	Magnoliopsida
        //Sous-classe 	Dilleniidae
        //Ordre 	Nepenthales
        //Famille 	Droseraceae
        //Genre Aldrovanda
        String wiki = "https://fr.wikipedia.org/wiki/Aldrovanda";
        CronquistClassificationBranch classification;
        try {
            classification = wikipediaCrawler.scrapWiki(wiki);
            CronquistClassificationBranch cronquistRanks = cronquistService.saveCronquist(classification, wiki);
            // Tous les rangs sont enregistrés
            cronquistRanks.getClassificationBranch().forEach((rankName, cronquistRank) -> assertNotNull(cronquistRank.getId()));

            // Tous les noms de rang sont uniques
            assertThatEachSignificantNameIsUnique(cronquistRanks);

            assertTrue("Le rang le plus bas doit être un rang significatif", cronquistRanks.getRangDeBase().isRangSignificatif());

            utils.assertThatEachLinkNameHasOnlyOneName(cronquistRanks);
        } catch (IOException e) {
            fail("unable to scrap wiki : " + e.getMessage());
        }
    }

    private void assertThatEachSignificantNameIsUnique(@NotNull CronquistClassificationBranch cronquistRanks) {
        List<AtomicClassificationNom> classificationNoms = new ArrayList<>();
        for (Map.Entry<RankName, ICronquistRank> rank : cronquistRanks.getClassificationBranch().entrySet()) {
            classificationNoms.addAll(
                rank.getValue().getNoms().stream()
                    .map(IClassificationNom::getNomFr)
                    .filter(Objects::nonNull)
                    .flatMap(nom -> classificationNomRepository.findAll(Example.of(new ClassificationNom().nomFr(nom))).stream())
                    .map(AtomicClassificationNom::new)
                    .collect(Collectors.toList())
                                     );
        }
        TreeSet<IClassificationNom> nomTreeSet = getAtomicClassificationNomTreeSet();
        nomTreeSet.addAll(classificationNoms);
        assertEquals(nomTreeSet.size(), classificationNoms.size());
    }

}
