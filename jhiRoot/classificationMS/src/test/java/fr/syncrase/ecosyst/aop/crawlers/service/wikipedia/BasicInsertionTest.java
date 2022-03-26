package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia;

import fr.syncrase.ecosyst.ClassificationMsApp;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.AtomicClassificationNom;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.AtomicCronquistRank;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.CronquistClassificationBranch;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.crawler.WikipediaCrawler;
import fr.syncrase.ecosyst.domain.ClassificationNom;
import fr.syncrase.ecosyst.domain.CronquistRank;
import fr.syncrase.ecosyst.repository.ClassificationNomRepository;
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
public class BasicInsertionTest {

    WikipediaCrawler wikipediaCrawler;

    @Autowired
    private ClassificationNomRepository classificationNomRepository;

    @Autowired
    private CronquistService cronquistService;

    public BasicInsertionTest() {
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

}
