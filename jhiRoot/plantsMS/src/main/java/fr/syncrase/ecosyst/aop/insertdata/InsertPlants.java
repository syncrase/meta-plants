package fr.syncrase.ecosyst.aop.insertdata;

import fr.syncrase.ecosyst.domain.Classification;
import fr.syncrase.ecosyst.domain.Cronquist;
import fr.syncrase.ecosyst.domain.NomVernaculaire;
import fr.syncrase.ecosyst.domain.Plante;
import fr.syncrase.ecosyst.repository.ClassificationRepository;
import fr.syncrase.ecosyst.repository.CronquistRepository;
import fr.syncrase.ecosyst.repository.NomVernaculaireRepository;
import fr.syncrase.ecosyst.repository.PlanteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

public class InsertPlants {

    private final Logger log = LoggerFactory.getLogger(InsertPlants.class);

    private final ClassificationRepository classificationRepository;
    private final CronquistRepository cronquistRepository;
    private final NomVernaculaireRepository nomVernaculaireRepository;
    private final PlanteRepository planteRepository;

    public InsertPlants(ClassificationRepository classificationRepository, CronquistRepository cronquistRepository, NomVernaculaireRepository nomVernaculaireRepository, PlanteRepository planteRepository) {
        this.classificationRepository = classificationRepository;
        this.cronquistRepository = cronquistRepository;
        this.nomVernaculaireRepository = nomVernaculaireRepository;
        this.planteRepository = planteRepository;
    }

    /**
     * insert all plants in database and
     */
    protected Map<String, Plante> insertAllPlants() {
        Map<String, Plante> insertedPlants = new HashMap<>();
        Map<String, String> plantAttributes = new HashMap<>();


        Cronquist daucusCarotaCron = getOrInsertCronquist(new Cronquist().ordre("apiales").famille("apiaceae").genre("daucus").espece("daucus carota"));
        Classification daucusCarotaClassif = getOrInsertClassification(new Classification().cronquist(daucusCarotaCron));
        Plante carotte = getOrInsertPlante(new Plante().classification(daucusCarotaClassif).addNomsVernaculaires(new NomVernaculaire().nom("carotte")));
        insertedPlants.put("carotte", carotte);
        insertedPlants.put("carotte jaune du doubs", insertPlantePotagere(carotte, new Plante().addNomsVernaculaires(new NomVernaculaire().nom("carotte jaune du doubs"))));


        Cronquist betaVulgarisCron = getOrInsertCronquist(new Cronquist().ordre("caryophyllales").famille("chenopodiaceae").genre("beta").espece("beta vulgaris"));
        Classification betaVulgarisClassif = getOrInsertClassification(new Classification().cronquist(betaVulgarisCron));
        Plante betterave = getOrInsertPlante(new Plante().classification(betaVulgarisClassif).addNomsVernaculaires(new NomVernaculaire().nom("betterave")));
        insertedPlants.put("betterave", betterave);
        insertedPlants.put("bette", insertPlantePotagere(betterave, new Plante().addNomsVernaculaires(new NomVernaculaire().nom("bette"))));
        insertedPlants.put("betterave de détroit améliorée 3", insertPlantePotagere(betterave, new Plante().addNomsVernaculaires(new NomVernaculaire().nom("betterave de détroit améliorée 3"))));


        Cronquist solanumLycopersicumCron = getOrInsertCronquist(new Cronquist().ordre("solanales").famille("solanaceae").genre("solanum").espece("solanum lycopersicum"));
        Classification solanumLycopersicumClassif = getOrInsertClassification(new Classification().cronquist(solanumLycopersicumCron));
        Plante tomate = getOrInsertPlante(new Plante().classification(solanumLycopersicumClassif).addNomsVernaculaires(new NomVernaculaire().nom("tomate")));
        insertedPlants.put("tomate", tomate);
        insertedPlants.put("tomate noire russe", insertPlantePotagere(tomate, new Plante().addNomsVernaculaires(new NomVernaculaire().nom("tomate noire russe"))));
        insertedPlants.put("tomate beefsteak", insertPlantePotagere(tomate, new Plante().addNomsVernaculaires(new NomVernaculaire().nom("tomate beefsteak"))));


        Cronquist lactucaSativaCron = getOrInsertCronquist(new Cronquist().ordre("asterales").famille("asteraceae").genre("lactuca").espece("lactuca sativa"));
        Classification lactucaSativaClassif = getOrInsertClassification(new Classification().cronquist(lactucaSativaCron));
        Plante laitue = getOrInsertPlante(new Plante().classification(lactucaSativaClassif).addNomsVernaculaires(new NomVernaculaire().nom("laitue")));
        insertedPlants.put("laitue", laitue);
        insertedPlants.put("laitue rouge grenobloise", insertPlantePotagere(laitue, new Plante().addNomsVernaculaires(new NomVernaculaire().nom("laitue rouge grenobloise"))));
        insertedPlants.put("laitue reine de mai de pleine terre", insertPlantePotagere(laitue, new Plante().addNomsVernaculaires(new NomVernaculaire().nom("laitue reine de mai de pleine terre"))));
        insertedPlants.put("laitue batavia lollo rossa", insertPlantePotagere(laitue, new Plante().addNomsVernaculaires(new NomVernaculaire().nom("laitue batavia lollo rossa"))));
        insertedPlants.put("laitue pommée grosse blonde paresseuse", insertPlantePotagere(laitue, new Plante().addNomsVernaculaires(new NomVernaculaire().nom("laitue pommée grosse blonde paresseuse"))));
        insertedPlants.put("laitue pommée reine de mai", insertPlantePotagere(laitue, new Plante().addNomsVernaculaires(new NomVernaculaire().nom("laitue pommée reine de mai"))));
        insertedPlants.put("laitue batavia iceberg reine des glaces", insertPlantePotagere(laitue, new Plante().addNomsVernaculaires(new NomVernaculaire().nom("laitue batavia iceberg reine des glaces"))));
        insertedPlants.put("laitue cressonnette marocaine", insertPlantePotagere(laitue, new Plante().addNomsVernaculaires(new NomVernaculaire().nom("laitue cressonnette marocaine"))));
        insertedPlants.put("laitue à couper", insertPlantePotagere(laitue, new Plante().addNomsVernaculaires(new NomVernaculaire().nom("laitue à couper"))));


        Cronquist phaseolusVulgarisCron = getOrInsertCronquist(new Cronquist().ordre("fabales").famille("fabaceae").genre("phaseolus").espece("phaseolus vulgaris"));
        Classification phaseolusVulgarisClassif = getOrInsertClassification(new Classification().cronquist(phaseolusVulgarisCron));
        Plante haricot = getOrInsertPlante(new Plante().classification(phaseolusVulgarisClassif).addNomsVernaculaires(new NomVernaculaire().nom("haricot")));
        insertedPlants.put("haricot", haricot);


        Cronquist fragariaVescaCron = getOrInsertCronquist(new Cronquist().ordre("rosales").famille("rosaceae").genre("fragaria").espece("fragaria vesca"));
        Classification fragariaVescaClassif = getOrInsertClassification(new Classification().cronquist(fragariaVescaCron));
        Plante fraisierDesBois = getOrInsertPlante(new Plante().classification(fragariaVescaClassif).addNomsVernaculaires(new NomVernaculaire().nom("fraisier des bois")));
        insertedPlants.put("fraisier des bois", fraisierDesBois);


        Cronquist alliumSchoenoprasumCron = getOrInsertCronquist(new Cronquist().ordre("liliales").famille("liliaceae").genre("allium").espece("allium schoenoprasum"));
        Classification alliumSchoenoprasumClassif = getOrInsertClassification(new Classification().cronquist(alliumSchoenoprasumCron));
        Plante ciboulette = getOrInsertPlante(new Plante().classification(alliumSchoenoprasumClassif).addNomsVernaculaires(new NomVernaculaire().nom("ciboulette")));
        insertedPlants.put("ciboulette", ciboulette);
        insertedPlants.put("ciboulette commune", insertPlantePotagere(ciboulette, new Plante()
                .addNomsVernaculaires(new NomVernaculaire().nom("ciboulette commune"))
                .addNomsVernaculaires(new NomVernaculaire().nom("civette"))
            )
        );


        return insertedPlants;
    }

    /**
     * Une plante potagère déjà existante ne verra pas ses noms vernaculaires enregistrés
     *
     * @param planteBotanique plante associée à la classification qui contient les plantes potagères
     * @param plantePotagere  plante non associée à aucune classification, mais associée à une plante botanique
     * @return la plante potagère avec son id
     */
    private Plante insertPlantePotagere(Plante planteBotanique, Plante plantePotagere) {
        // Enregistre la plante potagère
        persistNomsVernaculaires(plantePotagere);
        planteRepository.save(plantePotagere);

        // Ajoute la plante potagère à la plante botanique
        planteBotanique.addPlantesPotageres(plantePotagere);
        // Enregistre la plante botanique
        planteRepository.save(planteBotanique);

        return plantePotagere;
    }

    /**
     * @param plantePotagere side effect : complete nomsvernaculaires with ids
     */
    private void persistNomsVernaculaires(Plante plantePotagere) {
        if (plantePotagere.getNomsVernaculaires().size() > 0) {
            Iterator<NomVernaculaire> iterator = plantePotagere.getNomsVernaculaires().iterator();
            while (iterator.hasNext()) {
                getOrInsertNomVernaculaire(iterator.next());
            }
        }
    }

    private Plante getOrInsertPlante(Plante plante) {
        persistNomsVernaculaires(plante);
        if (!planteRepository.exists(Example.of(plante))) {
            planteRepository.save(plante);
        } else {
            Optional<Plante> returned = planteRepository.findOne(Example.of(plante));
            if (returned.isPresent()) {
                plante = returned.get();
//                nom.addPlantes(plante);
//                nomVernaculaireRepository.save(nom);
            } else {
                log.error("Unable to get instance of : " + plante);
            }
        }
        return plante;
    }

    private NomVernaculaire getOrInsertNomVernaculaire(NomVernaculaire nom) {

        if (!nomVernaculaireRepository.exists(Example.of(nom))) {
            nomVernaculaireRepository.save(nom);
        } else {
            Optional<NomVernaculaire> returned = nomVernaculaireRepository.findOne(Example.of(nom));
            if (returned.isPresent()) {
                nom = returned.get();
            } else {
                log.error("Unable to get instance of : " + nom);
            }
        }
        return nom;
    }

    private Cronquist getOrInsertCronquist(Cronquist cronquist) {
        Cronquist simplifiedCronquist = new Cronquist();
        simplifiedCronquist.setEspece(cronquist.getEspece());
        if (!cronquistRepository.exists(Example.of(simplifiedCronquist))) {
            cronquistRepository.save(cronquist);
        } else {
            Optional<Cronquist> returned = cronquistRepository.findOne(Example.of(cronquist));
            if (returned.isPresent()) {
                cronquist = returned.get();
                log.info("Existing cronquist : " + cronquist);
            } else {
                log.error("Unable to get instance of : " + cronquist);
            }
        }
        return cronquist;
    }

    private Classification getOrInsertClassification(Classification classification) {
        if (!classificationRepository.exists(Example.of(classification))) {
            classificationRepository.save(classification);
        } else {
            Optional<Classification> returned = classificationRepository.findOne(Example.of(classification));
            if (returned.isPresent()) {
                classification = returned.get();
                log.info("Existing classification : " + classification);
            } else {
                log.error("Unable to get instance of : " + classification);
            }
        }
        return classification;
    }


}
