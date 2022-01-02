package fr.syncrase.ecosyst.aop.insertdata;

import fr.syncrase.ecosyst.domain.Allelopathie;
import fr.syncrase.ecosyst.domain.Plante;
import fr.syncrase.ecosyst.repository.AllelopathieRepository;
import fr.syncrase.ecosyst.service.AllelopathieQueryService;
import fr.syncrase.ecosyst.service.criteria.AllelopathieCriteria;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.jhipster.service.filter.LongFilter;

import java.util.List;
import java.util.Map;

public class InsertInteractions {

    private final Logger log = LoggerFactory.getLogger(InsertInteractions.class);
    private final Map<String, Plante> insertedPlants;
    private final AllelopathieQueryService allelopathieQueryService;
    private AllelopathieRepository allelopathieRepository;

    public InsertInteractions(Map<String, Plante> insertedPlants, AllelopathieQueryService allelopathieQueryService, AllelopathieRepository allelopathieRepository) {
        this.allelopathieQueryService = allelopathieQueryService;
        this.insertedPlants = insertedPlants;
        this.allelopathieRepository = allelopathieRepository;
    }

    private Allelopathie getOrInsertAllelopathie(@NotNull Allelopathie allelopathie) {
        AllelopathieCriteria allelopathieCriteria = new AllelopathieCriteria();

        LongFilter cibleIdFilter = new LongFilter();
        cibleIdFilter.setEquals(allelopathie.getCible().getId());
        allelopathieCriteria.setCibleId(cibleIdFilter);

        LongFilter origineIdFilter = new LongFilter();
        origineIdFilter.setEquals(allelopathie.getOrigine().getId());
        allelopathieCriteria.setOrigineId(origineIdFilter);

        List<Allelopathie> allelopathies = allelopathieQueryService.findByCriteria(allelopathieCriteria);

        if (allelopathies.size() == 0) {
            allelopathieRepository.save(allelopathie);
        } else {
            if (allelopathies.size() == 1) {
                allelopathie = allelopathies.get(0);
                log.info("Existing allélopathie : " + allelopathie);
            } else {
                log.error("Unable to get instance of : " + allelopathie);
            }
        }
        return allelopathie;
    }

    protected void insertAllInteractions() {
        log.info("Insert plant interaction!!!");
        log.info("Plant quantity: " + insertedPlants.size());
        interactionPourCarotte(insertedPlants);
        interactionPourHaricot(insertedPlants);
        interactionPourTomate(insertedPlants);
        interactionPourBetterave(insertedPlants);
        interactionPourFraisier(insertedPlants);
        interactionPourLaitue(insertedPlants);
        interactionContreHaricot(insertedPlants);
        interactionContreTomate(insertedPlants);
    }

    private void interactionContreTomate(Map<String, Plante> insertedPlants) {
        log.info("interactionContreTomate"); // Contre tomate
        Allelopathie interactionContreTomate1 = new Allelopathie();
        interactionContreTomate1.setOrigine(insertedPlants.get("haricot"));
        interactionContreTomate1.setCible(insertedPlants.get("tomate"));
        interactionContreTomate1.setImpact(-5);
        interactionContreTomate1.setDescription("");
        interactionContreTomate1.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreTomate1);
        Allelopathie interactionContreTomate6 = new Allelopathie();
        interactionContreTomate6.setOrigine(insertedPlants.get("betterave"));
        interactionContreTomate6.setCible(insertedPlants.get("tomate"));
        interactionContreTomate6.setImpact(-5);
        interactionContreTomate6.setDescription("");
        interactionContreTomate6.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreTomate6);
    }

    private void interactionContreHaricot(Map<String, Plante> insertedPlants) {
        log.info("interactionContreHaricot"); // Contre haricot
        Allelopathie interactionContreHaricot4 = new Allelopathie();
        interactionContreHaricot4.setOrigine(insertedPlants.get("tomate"));
        interactionContreHaricot4.setCible(insertedPlants.get("haricot"));
        interactionContreHaricot4.setImpact(-5);
        interactionContreHaricot4.setDescription("");
        interactionContreHaricot4.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreHaricot4);
    }

    private void interactionPourLaitue(Map<String, Plante> insertedPlants) {
        log.info("interactionPourLaitue"); // Pour laitue
        Allelopathie interactionPourLaitue1 = new Allelopathie();
        interactionPourLaitue1.setOrigine(insertedPlants.get("fraisier des bois"));
        interactionPourLaitue1.setCible(insertedPlants.get("laitue"));
        interactionPourLaitue1.setImpact(5);
        interactionPourLaitue1.setDescription("");
        interactionPourLaitue1.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourLaitue1);
        Allelopathie interactionPourLaitue2 = new Allelopathie();
        interactionPourLaitue2.setOrigine(insertedPlants.get("carotte"));
        interactionPourLaitue2.setCible(insertedPlants.get("laitue"));
        interactionPourLaitue2.setImpact(5);
        interactionPourLaitue2.setDescription("");
        interactionPourLaitue2.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourLaitue2);
    }

    private void interactionPourFraisier(Map<String, Plante> insertedPlants) {
        log.info("interactionPourFraisier"); // Pour fraisier
        Allelopathie interactionPourFraisier4 = new Allelopathie();
        interactionPourFraisier4.setOrigine(insertedPlants.get("laitue"));
        interactionPourFraisier4.setCible(insertedPlants.get("fraisier des bois"));
        interactionPourFraisier4.setImpact(5);
        interactionPourFraisier4.setDescription("");
        interactionPourFraisier4.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourFraisier4);
    }

    private void interactionPourBetterave(Map<String, Plante> insertedPlants) {
        log.info("interactionPourBetterave"); // Pour betterave
        Allelopathie interactionPourBetterave5 = new Allelopathie();
        interactionPourBetterave5.setOrigine(insertedPlants.get("laitue"));
        interactionPourBetterave5.setCible(insertedPlants.get("betterave"));
        interactionPourBetterave5.setImpact(5);
        interactionPourBetterave5.setDescription("");
        interactionPourBetterave5.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourBetterave5);
        Allelopathie interactionPourBetterave6 = new Allelopathie();
        interactionPourBetterave6.setOrigine(insertedPlants.get("fraisier des bois"));
        interactionPourBetterave6.setCible(insertedPlants.get("betterave"));
        interactionPourBetterave6.setImpact(5);
        interactionPourBetterave6.setDescription("");
        interactionPourBetterave6.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourBetterave6);
    }

    private void interactionPourTomate(Map<String, Plante> insertedPlants) {
        log.info("interactionPourTomate"); // Pour tomate
        Allelopathie interactionPourTomate5 = new Allelopathie();
        interactionPourTomate5.setOrigine(insertedPlants.get("carotte"));
        interactionPourTomate5.setCible(insertedPlants.get("tomate"));
        interactionPourTomate5.setImpact(5);
        interactionPourTomate5.setDescription("");
        interactionPourTomate5.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourTomate5);
    }

    private void interactionPourHaricot(Map<String, Plante> insertedPlants) {
        log.info("interactionPourHaricot"); // Pour haricot
        Allelopathie interactionPourHaricot2 = new Allelopathie();
        interactionPourHaricot2.setOrigine(insertedPlants.get("laitue"));
        interactionPourHaricot2.setCible(insertedPlants.get("haricot"));
        interactionPourHaricot2.setImpact(5);
        interactionPourHaricot2.setDescription("");
        interactionPourHaricot2.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourHaricot2);
        Allelopathie interactionPourHaricot3 = new Allelopathie();
        interactionPourHaricot3.setOrigine(insertedPlants.get("tomate"));
        interactionPourHaricot3.setCible(insertedPlants.get("haricot"));
        interactionPourHaricot3.setImpact(5);
        interactionPourHaricot3.setDescription("");
        interactionPourHaricot3.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourHaricot3);
        Allelopathie interactionPourHaricot4 = new Allelopathie();
        interactionPourHaricot4.setOrigine(insertedPlants.get("carotte"));
        interactionPourHaricot4.setCible(insertedPlants.get("haricot"));
        interactionPourHaricot4.setImpact(5);
        interactionPourHaricot4.setDescription("");
        interactionPourHaricot4.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourHaricot4);
        Allelopathie interactionPourHaricot8 = new Allelopathie();
        interactionPourHaricot8.setOrigine(insertedPlants.get("betterave"));
        interactionPourHaricot8.setCible(insertedPlants.get("haricot"));
        interactionPourHaricot8.setImpact(5);
        interactionPourHaricot8.setDescription("");
        interactionPourHaricot8.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourHaricot8);
    }

    private void interactionPourCarotte(Map<String, Plante> insertedPlants) {
        log.info("interactionPourCarotte"); // Pour carotte
        Allelopathie interactionPourCarotte5 = new Allelopathie();
        interactionPourCarotte5.setOrigine(insertedPlants.get("tomate"));
        interactionPourCarotte5.setCible(insertedPlants.get("carotte"));
        interactionPourCarotte5.setImpact(5);
        interactionPourCarotte5.setDescription("");
        interactionPourCarotte5.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourCarotte5);
        Allelopathie interactionPourCarotte6 = new Allelopathie();
        interactionPourCarotte6.setOrigine(insertedPlants.get("laitue"));
        interactionPourCarotte6.setCible(insertedPlants.get("carotte"));
        interactionPourCarotte6.setImpact(5);
        interactionPourCarotte6.setDescription("");
        interactionPourCarotte6.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourCarotte6);
        Allelopathie interactionPourCarotte12 = new Allelopathie();
        interactionPourCarotte12.setOrigine(insertedPlants.get("carotte"));
        interactionPourCarotte12.setCible(insertedPlants.get("carotte"));
        interactionPourCarotte12.setImpact(5);
        interactionPourCarotte12.setDescription("");
        interactionPourCarotte12.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourCarotte12);
    }

}
