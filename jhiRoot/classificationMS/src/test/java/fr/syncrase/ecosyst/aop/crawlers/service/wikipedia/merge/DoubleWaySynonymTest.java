package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.merge;

import fr.syncrase.ecosyst.ClassificationMsApp;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.TestUtils;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.CronquistClassificationBranch;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.crawler.WikipediaCrawler;
import fr.syncrase.ecosyst.domain.IClassificationNom;
import fr.syncrase.ecosyst.domain.enumeration.RankName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
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
    private CronquistService cronquistService;

    public DoubleWaySynonymTest() {
        this.wikipediaCrawler = new WikipediaCrawler(cronquistService);
    }

    @Test
    public void enregistrementDUnRangSynonymeAvecMerge2() {
        try {
            CronquistClassificationBranch classification;
            // Règne 	Plantae
            //Sous-règne 	Tracheobionta
            //Division 	Magnoliophyta
            //Classe 	Magnoliopsida
            //Sous-classe 	Rosidae
            //Ordre 	Sapindales
            //Famille 	Sapindaceae (+Aceraceae)
            //Genre 	Lepisanthes
            //Espèce Lepisanthes senegalensis
            String wiki = "https://fr.wikipedia.org/wiki/Lepisanthes_senegalensis";
            classification = wikipediaCrawler.scrapWiki(wiki);
            CronquistClassificationBranch sepisanthesSenegalensisClassification = cronquistService.saveCronquist(classification, wiki);
            //            LinkedMap<RankName, ICronquistRank> sepisanthesSenegalensisClassification = utils.transformToMapOfRanksByName(sepisanthesSenegalensisRanks);

            // Règne 	Plantae
            //Sous-règne 	Tracheobionta
            //Division 	Magnoliophyta
            //Classe 	Magnoliopsida
            //Sous-classe 	Rosidae
            //Ordre 	Sapindales
            //Famille 	Aceraceae (+Sapindaceae)
            //Genre 	Acer
            //Espèce Acer Miyabei
            wiki = "https://fr.wikipedia.org/wiki/%C3%89rable_de_Miyabe";
            classification = wikipediaCrawler.scrapWiki(wiki);
            CronquistClassificationBranch erableMiyabeClassification = cronquistService.saveCronquist(classification, wiki);
            //            LinkedMap<RankName, ICronquistRank> erableMiyabeClassification = utils.transformToMapOfRanksByName(erableMiyabeRanks);

            // Règne 	Plantae
            //Sous-règne 	Tracheobionta
            //Division 	Magnoliophyta
            //Classe 	Magnoliopsida
            //Sous-classe 	Rosidae
            //Ordre 	Sapindales
            //Famille 	Sapindaceae (+Aceraceae)
            //Genre 	Acer
            //Espèce Acer monspessulanum
            wiki = "https://fr.wikipedia.org/wiki/%C3%89rable_de_Montpellier";
            classification = wikipediaCrawler.scrapWiki(wiki);
            CronquistClassificationBranch erableMontpellierClassification = cronquistService.saveCronquist(classification, wiki);
            //            LinkedMap<RankName, ICronquistRank> erableMontpellierClassification = utils.transformToMapOfRanksByName(erableMontpellierRanks);

            // puisque Aceraceae = Sapindaceae
            // ⇒ Lepisanthes_senegalensis doit posséder la famille synonyme Aceraceae
            CronquistClassificationBranch classificationBranchOfChironia = cronquistService.getClassificationById(sepisanthesSenegalensisClassification.getRangDeBase().getId());
            Set<String> nomsDeFamilleDeSepisanthesSenegalensis = classificationBranchOfChironia.getRang(RankName.FAMILLE).getNoms().stream().map(IClassificationNom::getNomFr).collect(Collectors.toSet());
            assertEquals("Lepisanthes_senegalensis doit contenir deux familles", 2, nomsDeFamilleDeSepisanthesSenegalensis.size());
            assertTrue("Lepisanthes_senegalensis doit posséder la famille synonyme Aceraceae", nomsDeFamilleDeSepisanthesSenegalensis.containsAll(Set.of("Aceraceae", "Sapindaceae")));

            // ⇒ L'érable de Miyabe doit posséder la famille synonyme Sapindaceae
            CronquistClassificationBranch classificationBranchOfErableMiyabe = cronquistService.getClassificationById(erableMiyabeClassification.getRangDeBase().getId());
            Set<String> nomsDeFamilleDeErableMiyabe = classificationBranchOfErableMiyabe.getRang(RankName.FAMILLE).getNoms().stream().map(IClassificationNom::getNomFr).collect(Collectors.toSet());
            assertEquals("L'érable de Miyabe doit contenir deux familles", 2, nomsDeFamilleDeErableMiyabe.size());
            assertTrue("L'érable de Miyabe doit posséder la famille synonyme Aceraceae", nomsDeFamilleDeErableMiyabe.containsAll(Set.of("Aceraceae", "Sapindaceae")));

            // ⇒ L'érable de montpellier possède la famille synonyme Aceraceae
            Set<String> nomsDeFamilleDeErableMontpellier = utils.getRankNames(erableMontpellierClassification, RankName.FAMILLE);
            assertEquals("L'érable de Montpellier doit contenir deux familles", 2, nomsDeFamilleDeErableMontpellier.size());
            assertTrue("L'érable de Montpellier doit posséder la famille synonyme Aceraceae", nomsDeFamilleDeErableMontpellier.containsAll(Set.of("Aceraceae", "Sapindaceae")));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
