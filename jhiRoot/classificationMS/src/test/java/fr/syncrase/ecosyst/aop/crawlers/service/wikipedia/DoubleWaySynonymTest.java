package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia;

import fr.syncrase.ecosyst.ClassificationMsApp;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.AtomicClassificationNom;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.CronquistClassificationBranch;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.crawler.WikipediaCrawler;
import fr.syncrase.ecosyst.domain.CronquistRank;
import fr.syncrase.ecosyst.domain.enumeration.CronquistTaxonomikRanks;
import fr.syncrase.ecosyst.repository.ClassificationNomRepository;
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
public class DoubleWaySynonymTest {

    WikipediaCrawler wikipediaCrawler;

    TestUtils utils = new TestUtils();

    @Autowired
    private ClassificationNomRepository classificationNomRepository;

    @Autowired
    private CronquistService cronquistService;

    public DoubleWaySynonymTest() {
        this.wikipediaCrawler = new WikipediaCrawler(cronquistService);
    }

    @Test
    public void enregistrementDUnRangSynonymeAvecMerge2() {
        try {
            CronquistClassificationBranch classification;
            // Ordre Sapindales & Famille Sapindaceae & Genre Lepisanthes
            String wiki = "https://fr.wikipedia.org/wiki/Lepisanthes_senegalensis";
            classification = wikipediaCrawler.scrapWiki(wiki);
            Collection<CronquistRank> sepisanthesSenegalensisRanks = cronquistService.saveCronquist(classification, wiki);
            LinkedMap<CronquistTaxonomikRanks, CronquistRank> sepisanthesSenegalensisClassification = utils.transformToMapOfRanksByName(sepisanthesSenegalensisRanks);

            // Ordre Sapindales & Famille Aceraceae & Genre Acer
            wiki = "https://fr.wikipedia.org/wiki/%C3%89rable_de_Miyabe";
            classification = wikipediaCrawler.scrapWiki(wiki);
            Collection<CronquistRank> erableMiyabeRanks = cronquistService.saveCronquist(classification, wiki);
            LinkedMap<CronquistTaxonomikRanks, CronquistRank> erableMiyabeClassification = utils.transformToMapOfRanksByName(erableMiyabeRanks);

            // Ordre Sapindales & Famille Sapindaceae & Genre Acer
            wiki = "https://fr.wikipedia.org/wiki/%C3%89rable_de_Montpellier";
            classification = wikipediaCrawler.scrapWiki(wiki);
            Collection<CronquistRank> erableMontpellierRanks = cronquistService.saveCronquist(classification, wiki);
            LinkedMap<CronquistTaxonomikRanks, CronquistRank> erableMontpellierClassification = utils.transformToMapOfRanksByName(erableMontpellierRanks);

            // puisque Aceraceae = Sapindaceae
            // ⇒ Lepisanthes_senegalensis doit posséder la famille synonyme Aceraceae
            CronquistClassificationBranch classificationBranchOfChironia = cronquistService.getClassificationBranchOfThisRank(sepisanthesSenegalensisClassification.get(sepisanthesSenegalensisClassification.lastKey()).getId());
            Set<String> nomsDeFamilleDeSepisanthesSenegalensis = classificationBranchOfChironia.getRang(CronquistTaxonomikRanks.FAMILLE).getNoms().stream().map(AtomicClassificationNom::getNomFr).collect(Collectors.toSet());
            assertEquals("Lepisanthes_senegalensis doit contenir deux familles", 2, nomsDeFamilleDeSepisanthesSenegalensis.size());
            assertTrue("Lepisanthes_senegalensis doit posséder la famille synonyme Aceraceae", nomsDeFamilleDeSepisanthesSenegalensis.containsAll(Set.of("Aceraceae", "Sapindaceae")));

            // ⇒ L'érable de Miyabe doit posséder la famille synonyme Sapindaceae
            CronquistClassificationBranch classificationBranchOfErableMiyabe = cronquistService.getClassificationBranchOfThisRank(erableMiyabeClassification.get(erableMiyabeClassification.lastKey()).getId());
            Set<String> nomsDeFamilleDeErableMiyabe = classificationBranchOfErableMiyabe.getRang(CronquistTaxonomikRanks.FAMILLE).getNoms().stream().map(AtomicClassificationNom::getNomFr).collect(Collectors.toSet());
            assertEquals("L'érable de Miyabe doit contenir deux familles", 2, nomsDeFamilleDeErableMiyabe.size());
            assertTrue("L'érable de Miyabe doit posséder la famille synonyme Aceraceae", nomsDeFamilleDeErableMiyabe.containsAll(Set.of("Aceraceae", "Sapindaceae")));

            // ⇒ L'érable de montpellier possède la famille synonyme Aceraceae
            Set<String> nomsDeFamilleDeErableMontpellier = utils.getNamesFromMapRank(erableMontpellierClassification, CronquistTaxonomikRanks.FAMILLE);
            assertEquals("L'érable de Montpellier doit contenir deux familles", 2, nomsDeFamilleDeErableMontpellier.size());
            assertTrue("L'érable de Montpellier doit posséder la famille synonyme Aceraceae", nomsDeFamilleDeErableMontpellier.containsAll(Set.of("Aceraceae", "Sapindaceae")));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
