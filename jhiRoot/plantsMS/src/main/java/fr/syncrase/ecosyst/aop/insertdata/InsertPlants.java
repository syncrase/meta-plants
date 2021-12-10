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

        plantAttributes.put("Ordre", "apiales");
        plantAttributes.put("Famille", "apiaceae");
        plantAttributes.put("Genre", "daucus");
        plantAttributes.put("Espece", "daucus carota");
        plantAttributes.put("Plante", "carotte");
        insertedPlants.put("carotte", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "carotte jaune du doubs");
        insertedPlants.put("carotte jaune du doubs", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "caryophyllales");
        plantAttributes.put("Famille", "chenopodiaceae");
        plantAttributes.put("Genre", "beta");
        plantAttributes.put("Espece", "beta vulgaris");
        plantAttributes.put("Plante", "betterave");
        insertedPlants.put("betterave", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "bette");
        insertedPlants.put("bette", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "betterave de détroit améliorée 3");
        insertedPlants.put("betterave de détroit améliorée 3", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "solanales");
        plantAttributes.put("Famille", "solanaceae");
        plantAttributes.put("Genre", "solanum");
        plantAttributes.put("Espece", "solanum lycopersicum");
        plantAttributes.put("Plante", "tomate");
        insertedPlants.put("tomate", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "tomate noire russe");
        insertedPlants.put("tomate noire russe", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "tomate beefsteak");
        insertedPlants.put("tomate beefsteak", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "asterales");
        plantAttributes.put("Famille", "asteraceae");
        plantAttributes.put("Genre", "lactuca");
        plantAttributes.put("Espece", "lactuca sativa");
        plantAttributes.put("Plante", "laitue");
        insertedPlants.put("laitue", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "laitue rouge grenobloise");
        insertedPlants.put("laitue rouge grenobloise", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "laitue reine de mai de pleine terre");
        insertedPlants.put("laitue reine de mai de pleine terre", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "laitue batavia lollo rossa");
        insertedPlants.put("laitue batavia lollo rossa", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "laitue pommée grosse blonde paresseuse");
        insertedPlants.put("laitue pommée grosse blonde paresseuse", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "laitue pommée reine de mai");
        insertedPlants.put("laitue pommée reine de mai", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "laitue batavia iceberg reine des glaces");
        insertedPlants.put("laitue batavia iceberg reine des glaces", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "laitue cressonnette marocaine");
        insertedPlants.put("laitue cressonnette marocaine", insertPlant(plantAttributes));
        plantAttributes.put("Plante", "laitue à couper");
        insertedPlants.put("laitue à couper", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "fabales");
        plantAttributes.put("Famille", "fabaceae");
        plantAttributes.put("Genre", "phaseolus");
        plantAttributes.put("Espece", "phaseolus vulgaris");
        plantAttributes.put("Plante", "haricot");
        insertedPlants.put("haricot", insertPlant(plantAttributes));

        plantAttributes.put("Ordre", "rosales");
        plantAttributes.put("Famille", "rosaceae");
        plantAttributes.put("Genre", "fragaria");
        plantAttributes.put("Espece", "fragaria vesca");
        plantAttributes.put("Plante", "fraisier des bois");
        insertedPlants.put("fraisier des bois", insertPlant(plantAttributes));
        return insertedPlants;
    }

    /**
     * @param data null if failed to insert something
     * @return The newly created plant
     */
    private Plante insertPlant(Map<String, String> data) {
        try {
            Cronquist cronquist = getOrInsertCronquist(data);
            Classification classification = getOrInsertClassification(cronquist);

            NomVernaculaire nom = getOrInsertNomVernaculaire(data);
            Plante plante = getOrInsertPlante(nom, classification);

            log.info("Plant saved : " + plante);
            return plante;
        } catch (Exception e) {
            log.error("Unable to insert {" + data.get("Plante") + "}" + e.getLocalizedMessage());
        }
        return null;
    }

    private NomVernaculaire getOrInsertNomVernaculaire(Map<String, String> data) {
        NomVernaculaire nom = new NomVernaculaire();
        nom.setNom(data.get("Plante"));

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

    private Plante getOrInsertPlante(NomVernaculaire nom, Classification classification) {
        Plante plante;
        plante = new Plante();
        plante.addNomsVernaculaires(nom);
        plante.setClassification(classification);
        if (!planteRepository.exists(Example.of(plante))) {
            planteRepository.save(plante);
        } else {
            Optional<Plante> returned = planteRepository.findOne(Example.of(plante));
            if (returned.isPresent()) {
                plante = returned.get();
                nom.addPlantes(plante);
                nomVernaculaireRepository.save(nom);
            } else {
                log.error("Unable to get instance of : " + plante);
            }
        }
        return plante;
    }

    private Cronquist getOrInsertCronquist(Map<String, String> data) {
        Cronquist cronquist = new Cronquist();
        cronquist.setOrdre(data.get("Ordre"));
        cronquist.setFamille(data.get("Famille"));
        cronquist.setGenre(data.get("Genre"));
        cronquist.setEspece(data.get("Espece"));

        if (!cronquistRepository.exists(Example.of(cronquist))) {
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

    private Classification getOrInsertClassification(Cronquist cronquist) {
        Classification classification = new Classification();
        classification.setCronquist(cronquist);
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
