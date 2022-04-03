package fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.merge;

import fr.syncrase.ecosyst.ClassificationMsApp;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.CronquistService;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.aggregates.classification.CronquistClassificationBranch;
import fr.syncrase.ecosyst.aop.crawlers.service.wikipedia.crawler.WikipediaCrawler;
import fr.syncrase.ecosyst.domain.enumeration.RankName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClassificationMsApp.class)
public class LeRangScrappeFaitEvoluerLaClassificationExistanteTest {

    WikipediaCrawler wikipediaCrawler;

    @Autowired
    private CronquistService cronquistService;

    public LeRangScrappeFaitEvoluerLaClassificationExistanteTest() {
        this.wikipediaCrawler = new WikipediaCrawler(cronquistService);
    }

    @Test
    public void transformationDUnRangDeLiaisonEnRangSignificatif() {
        CronquistClassificationBranch classification;
        try {
            // Règne 	Plantae
            //Sous-règne 	(+Tracheobionta déduis du suivant)
            //Division 	Magnoliophyta
            //Classe 	Magnoliopsida
            //Ordre 	Gentianales
            //Famille 	Gentianaceae
            //Genre Chironia
            String wiki = "https://fr.wikipedia.org/wiki/Chironia";
            classification = wikipediaCrawler.scrapWiki(wiki);
            CronquistClassificationBranch chironiaClassification = cronquistService.saveCronquist(classification, wiki);

            //Règne 	Plantae
            //Sous-règne 	Tracheobionta
            //Division 	Magnoliophyta
            //Classe 	Magnoliopsida
            //Ordre 	Gentianales
            //Genre 	Gentianaceae
            //Genre Monodiella
            wiki = "https://fr.wikipedia.org/wiki/Monodiella";
            classification = wikipediaCrawler.scrapWiki(wiki);
            CronquistClassificationBranch monodiellaClassification = cronquistService.saveCronquist(classification, wiki);

            CronquistClassificationBranch classificationBranchOfChironia = cronquistService.getClassificationById(chironiaClassification.getRangDeBase().getId());
            assertEquals("Le sous règne Tracheobionta doit avoir été ajouté a chironia",
                         monodiellaClassification.getRang(RankName.SOUSREGNE).getId(),
                         classificationBranchOfChironia.getRang(RankName.SOUSREGNE).getId()
                        );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
