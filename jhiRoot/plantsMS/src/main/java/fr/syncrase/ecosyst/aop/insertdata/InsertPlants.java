package fr.syncrase.ecosyst.aop.insertdata;

import fr.syncrase.ecosyst.domain.ClassificationCronquist;
import fr.syncrase.ecosyst.domain.NomVernaculaire;
import fr.syncrase.ecosyst.domain.Plante;
import fr.syncrase.ecosyst.repository.ClassificationCronquistRepository;
import fr.syncrase.ecosyst.repository.NomVernaculaireRepository;
import fr.syncrase.ecosyst.repository.PlanteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InsertPlants {

    private final Logger log = LoggerFactory.getLogger(InsertPlants.class);

//    private final ClassificationRepository classificationRepository;
    private final ClassificationCronquistRepository classificationCronquistRepository;
    private final NomVernaculaireRepository nomVernaculaireRepository;
    private final PlanteRepository planteRepository;

    public InsertPlants(ClassificationCronquistRepository classificationCronquistRepository, NomVernaculaireRepository nomVernaculaireRepository, PlanteRepository planteRepository) {
//        this.classificationRepository = classificationRepository;
        this.classificationCronquistRepository = classificationCronquistRepository;
        this.nomVernaculaireRepository = nomVernaculaireRepository;
        this.planteRepository = planteRepository;
    }

    /**
     * insert all plants in database and
     */
    protected Map<String, Plante> insertAllPlants() {
        Map<String, Plante> insertedPlants = new HashMap<>();

        RestTemplate restTemplate = new RestTemplate();
//        {
//            "plantesId.equals": plant.id
//        }

        Map<String, String> variables = new HashMap<String, String>(3);
        variables.put("cronquistRankId", "48601");
//        variables.put("id", photo.getAttribute("id"));
//        variables.put("secret", photo.getAttribute("secret"));
        Object forObject = restTemplate.getForObject("http://localhost:8082/cronquist-ranks", Object.class, variables);
        // TODO call classificationMS
        ClassificationCronquist daucusCarotaCron = getOrInsertCronquist(new ClassificationCronquist().ordre("apiales").famille("apiaceae").genre("daucus").espece("daucus carota"));
//        Classification daucusCarotaClassif = getOrInsertClassification(new Classification().cronquist(daucusCarotaCron));
        Plante carotte = getOrInsertPlante(new Plante().classificationCronquist(daucusCarotaCron).addNomsVernaculaires(new NomVernaculaire().nom("carotte")));
        insertedPlants.put("carotte", carotte);
        insertedPlants.put("carotte jaune du doubs", insertPlantePotagere(carotte, new Plante().addNomsVernaculaires(new NomVernaculaire().nom("carotte jaune du doubs"))));


//        ClassificationCronquist betaVulgarisCron = getOrInsertCronquist(new ClassificationCronquist().ordre("caryophyllales").famille("chenopodiaceae").genre("beta").espece("beta vulgaris"));
//        Classification betaVulgarisClassif = getOrInsertClassification(new Classification().cronquist(betaVulgarisCron));
//        Plante betterave = getOrInsertPlante(new Plante().classification(betaVulgarisClassif).addNomsVernaculaires(new NomVernaculaire().nom("betterave")));
//        insertedPlants.put("betterave", betterave);
//        insertedPlants.put("bette", insertPlantePotagere(betterave, new Plante().addNomsVernaculaires(new NomVernaculaire().nom("bette"))));
//        insertedPlants.put("betterave de détroit améliorée 3", insertPlantePotagere(betterave, new Plante().addNomsVernaculaires(new NomVernaculaire().nom("betterave de détroit améliorée 3"))));
//
//
//        ClassificationCronquist solanumLycopersicumCron = getOrInsertCronquist(new ClassificationCronquist().ordre("solanales").famille("solanaceae").genre("solanum").espece("solanum lycopersicum"));
//        Classification solanumLycopersicumClassif = getOrInsertClassification(new Classification().cronquist(solanumLycopersicumCron));
//        Plante tomate = getOrInsertPlante(new Plante().classification(solanumLycopersicumClassif).addNomsVernaculaires(new NomVernaculaire().nom("tomate")));
//        insertedPlants.put("tomate", tomate);
//        insertedPlants.put("tomate noire russe", insertPlantePotagere(tomate, new Plante().addNomsVernaculaires(new NomVernaculaire().nom("tomate noire russe"))));
//        insertedPlants.put("tomate beefsteak", insertPlantePotagere(tomate, new Plante().addNomsVernaculaires(new NomVernaculaire().nom("tomate beefsteak"))));
//
//
//        ClassificationCronquist lactucaSativaCron = getOrInsertCronquist(new ClassificationCronquist().ordre("asterales").famille("asteraceae").genre("lactuca").espece("lactuca sativa"));
//        Classification lactucaSativaClassif = getOrInsertClassification(new Classification().cronquist(lactucaSativaCron));
//        Plante laitue = getOrInsertPlante(new Plante().classification(lactucaSativaClassif).addNomsVernaculaires(new NomVernaculaire().nom("laitue")));
//        insertedPlants.put("laitue", laitue);
//        insertedPlants.put("laitue rouge grenobloise", insertPlantePotagere(laitue, new Plante().addNomsVernaculaires(new NomVernaculaire().nom("laitue rouge grenobloise"))));
//        insertedPlants.put("laitue reine de mai de pleine terre", insertPlantePotagere(laitue, new Plante().addNomsVernaculaires(new NomVernaculaire().nom("laitue reine de mai de pleine terre"))));
//        insertedPlants.put("laitue batavia lollo rossa", insertPlantePotagere(laitue, new Plante().addNomsVernaculaires(new NomVernaculaire().nom("laitue batavia lollo rossa"))));
//        insertedPlants.put("laitue pommée grosse blonde paresseuse", insertPlantePotagere(laitue, new Plante().addNomsVernaculaires(new NomVernaculaire().nom("laitue pommée grosse blonde paresseuse"))));
//        insertedPlants.put("laitue pommée reine de mai", insertPlantePotagere(laitue, new Plante().addNomsVernaculaires(new NomVernaculaire().nom("laitue pommée reine de mai"))));
//        insertedPlants.put("laitue batavia iceberg reine des glaces", insertPlantePotagere(laitue, new Plante().addNomsVernaculaires(new NomVernaculaire().nom("laitue batavia iceberg reine des glaces"))));
//        insertedPlants.put("laitue cressonnette marocaine", insertPlantePotagere(laitue, new Plante().addNomsVernaculaires(new NomVernaculaire().nom("laitue cressonnette marocaine"))));
//        insertedPlants.put("laitue à couper", insertPlantePotagere(laitue, new Plante().addNomsVernaculaires(new NomVernaculaire().nom("laitue à couper"))));
//
//
//        ClassificationCronquist phaseolusVulgarisCron = getOrInsertCronquist(new ClassificationCronquist().ordre("fabales").famille("fabaceae").genre("phaseolus").espece("phaseolus vulgaris"));
//        Classification phaseolusVulgarisClassif = getOrInsertClassification(new Classification().cronquist(phaseolusVulgarisCron));
//        Plante haricot = getOrInsertPlante(new Plante().classification(phaseolusVulgarisClassif).addNomsVernaculaires(new NomVernaculaire().nom("haricot")));
//        insertedPlants.put("haricot", haricot);
//
//
//        ClassificationCronquist fragariaVescaCron = getOrInsertCronquist(new ClassificationCronquist().ordre("rosales").famille("rosaceae").genre("fragaria").espece("fragaria vesca"));
//        Classification fragariaVescaClassif = getOrInsertClassification(new Classification().cronquist(fragariaVescaCron));
//        Plante fraisierDesBois = getOrInsertPlante(new Plante().classification(fragariaVescaClassif).addNomsVernaculaires(new NomVernaculaire().nom("fraisier des bois")));
//        insertedPlants.put("fraisier des bois", fraisierDesBois);
//
//
//        ClassificationCronquist alliumSchoenoprasumCron = getOrInsertCronquist(new ClassificationCronquist().ordre("liliales").famille("liliaceae").genre("allium").espece("allium schoenoprasum"));
//        Classification alliumSchoenoprasumClassif = getOrInsertClassification(new Classification().cronquist(alliumSchoenoprasumCron));
//        Plante ciboulette = getOrInsertPlante(new Plante().classification(alliumSchoenoprasumClassif).addNomsVernaculaires(new NomVernaculaire().nom("ciboulette")));
//        insertedPlants.put("ciboulette", ciboulette);
//        insertedPlants.put("ciboulette commune", insertPlantePotagere(ciboulette, new Plante()
//                .addNomsVernaculaires(new NomVernaculaire().nom("ciboulette commune"))
//                .addNomsVernaculaires(new NomVernaculaire().nom("civette"))
//            )
//        );


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
            for (NomVernaculaire nomVernaculaire : plantePotagere.getNomsVernaculaires()) {
                getOrInsertNomVernaculaire(nomVernaculaire);
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

    private void getOrInsertNomVernaculaire(NomVernaculaire nom) {

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
    }

    private ClassificationCronquist getOrInsertCronquist(ClassificationCronquist cronquist) {
        ClassificationCronquist simplifiedCronquist = new ClassificationCronquist();
        simplifiedCronquist.setEspece(cronquist.getEspece());
        if (!classificationCronquistRepository.exists(Example.of(simplifiedCronquist))) {
            classificationCronquistRepository.save(cronquist);
        } else {
            Optional<ClassificationCronquist> returned = classificationCronquistRepository.findOne(Example.of(cronquist));
            if (returned.isPresent()) {
                cronquist = returned.get();
                log.info("Existing cronquist : " + cronquist);
            } else {
                log.error("Unable to get instance of : " + cronquist);
            }
        }
        return cronquist;
    }

    private ClassificationCronquist getOrInsertClassificationCronquist(ClassificationCronquist classification) {
        if (!classificationCronquistRepository.exists(Example.of(classification))) {
            classificationCronquistRepository.save(classification);
        } else {
            Optional<ClassificationCronquist> returned = classificationCronquistRepository.findOne(Example.of(classification));
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
