package fr.syncrase.ecosyst.aop.insertdata;

import fr.syncrase.ecosyst.domain.Allelopathie;
import fr.syncrase.ecosyst.domain.Plante;
import fr.syncrase.ecosyst.repository.AllelopathieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InsertInteractions {

    private final Logger log = LoggerFactory.getLogger(InsertInteractions.class);
    private final Map<String, Plante> insertedPlants;
    private final AllelopathieRepository allelopathieRepository;

    public InsertInteractions(Map<String, Plante> insertedPlants, AllelopathieRepository allelopathieRepository) {
        this.allelopathieRepository = allelopathieRepository;

        this.insertedPlants = insertedPlants;
//        insertAllInteractions(this.insertedPlants);
    }

    private Allelopathie getOrInsertAllelopathie(Allelopathie allelopathie) {

        List<Allelopathie> all = allelopathieRepository.findAll(Example.of(allelopathie));
        log.info("Is there multiple allelopathie " + all.size());


        if (!allelopathieRepository.exists(Example.of(allelopathie))) {
            allelopathieRepository.save(allelopathie);
        } else {
            Optional<Allelopathie> returned = allelopathieRepository.findOne(Example.of(allelopathie));
            if (returned.isPresent()) {
                allelopathie = returned.get();
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
        interactionPourChouCommun(insertedPlants);
        interactionPourHaricot(insertedPlants);
        interactionPourConcombre(insertedPlants);
        interactionPourPommeDeTerre(insertedPlants);
        interactionPourTomate(insertedPlants);
        interactionPourAsperge(insertedPlants);
        interactionPourBetterave(insertedPlants);
        interactionPourFramboisier(insertedPlants);
        interactionPourAubergine(insertedPlants);
        interactionPourCourge(insertedPlants);
        interactionPourPotiron(insertedPlants);
        interactionPourFraisier(insertedPlants);
        interactionPourLaitue(insertedPlants);
        interactionPourMais(insertedPlants);
        interactionPourMelon(insertedPlants);
        interactionPourOignon(insertedPlants);
        interactionPourPoireau(insertedPlants);
        interactionPourPecher(insertedPlants);
        interactionPourPoirier(insertedPlants);
        interactionPourPommier(insertedPlants);
        interactionPourPois(insertedPlants);
        interactionPourRadis(insertedPlants);
        interactionPourRosier(insertedPlants);
        interactionPourPiment(insertedPlants);
        interactionPourPoivron(insertedPlants);
        interactionPourArtichaut(insertedPlants);
        interactionPourBette(insertedPlants);
        interactionPourCeleri(insertedPlants);
        interactionContreChouFleur(insertedPlants);
        interactionContreAil(insertedPlants);
        interactionContreCarotte(insertedPlants);
        interactionContreAbsinthe(insertedPlants);
        interactionContreArtichaut(insertedPlants);
        interactionContreBasilic(insertedPlants);
        interactionContreBetterave(insertedPlants);
        interactionContreRadis(insertedPlants);
        interactionContreChou(insertedPlants);
        interactionContreConcombre(insertedPlants);
        interactionContreCourge(insertedPlants);
        interactionContreEchalotte(insertedPlants);
        interactionContreEpinard(insertedPlants);
        interactionContreFenouil(insertedPlants);
        interactionContreHaricot(insertedPlants);
        interactionContreLaitue(insertedPlants);
        interactionContreMelon(insertedPlants);
        interactionContreOignon(insertedPlants);
        interactionContrePoireau(insertedPlants);
        interactionContrePois(insertedPlants);
        interactionContrePommeDeTerre(insertedPlants);
        interactionContreTomate(insertedPlants);
        interactionContreSauge(insertedPlants);
        interactionContrePersil(insertedPlants);
        interactionContreFraisier(insertedPlants);
        interactionContreAsperge(insertedPlants);
        interactionContreAubergine(insertedPlants);
    }

    private void interactionContreAubergine(Map<String, Plante> insertedPlants) {
        log.info("interactionContreAubergine"); // Contre aubergine
        Allelopathie interactionContreAubergine1 = new Allelopathie();
        interactionContreAubergine1.setOrigine(insertedPlants.get("haricot"));
        interactionContreAubergine1.setCible(insertedPlants.get("aubergine"));
        interactionContreAubergine1.setImpact(-5);
        interactionContreAubergine1.setDescription("");
        interactionContreAubergine1.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreAubergine1);
    }

    private void interactionContreAsperge(Map<String, Plante> insertedPlants) {
        log.info("interactionContreAsperge"); // Contre asperge
        Allelopathie interactionContreAsperge1 = new Allelopathie();
        interactionContreAsperge1.setOrigine(insertedPlants.get("tomate"));
        interactionContreAsperge1.setCible(insertedPlants.get("asperge"));
        interactionContreAsperge1.setImpact(-5);
        interactionContreAsperge1.setDescription("");
        interactionContreAsperge1.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreAsperge1);
        Allelopathie interactionContreAsperge2 = new Allelopathie();
        interactionContreAsperge2.setOrigine(insertedPlants.get("haricot"));
        interactionContreAsperge2.setCible(insertedPlants.get("asperge"));
        interactionContreAsperge2.setImpact(-5);
        interactionContreAsperge2.setDescription("");
        interactionContreAsperge2.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreAsperge2);
        Allelopathie interactionContreAsperge3 = new Allelopathie();
        interactionContreAsperge3.setOrigine(insertedPlants.get("persil"));
        interactionContreAsperge3.setCible(insertedPlants.get("asperge"));
        interactionContreAsperge3.setImpact(-5);
        interactionContreAsperge3.setDescription("");
        interactionContreAsperge3.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreAsperge3);
    }

    private void interactionContreFraisier(Map<String, Plante> insertedPlants) {
        log.info("interactionContreFraisier"); // Contre fraisier
        Allelopathie interactionContreFraisier1 = new Allelopathie();
        interactionContreFraisier1.setOrigine(insertedPlants.get("chou commun"));
        interactionContreFraisier1.setCible(insertedPlants.get("fraisier des bois"));
        interactionContreFraisier1.setImpact(-5);
        interactionContreFraisier1.setDescription("");
        interactionContreFraisier1.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreFraisier1);
    }

    private void interactionContrePersil(Map<String, Plante> insertedPlants) {
        log.info("interactionContrePersil"); // Contre persil
        Allelopathie interactionContrePersil1 = new Allelopathie();
        interactionContrePersil1.setOrigine(insertedPlants.get("laitue"));
        interactionContrePersil1.setCible(insertedPlants.get("persil"));
        interactionContrePersil1.setImpact(-5);
        interactionContrePersil1.setDescription("");
        interactionContrePersil1.setType("Inconnu");
        getOrInsertAllelopathie(interactionContrePersil1);
    }

    private void interactionContreSauge(Map<String, Plante> insertedPlants) {
        log.info("interactionContreSauge"); // Contre sauge
        Allelopathie interactionContreSauge1 = new Allelopathie();
        interactionContreSauge1.setOrigine(insertedPlants.get("oignon"));
        interactionContreSauge1.setCible(insertedPlants.get("sauge"));
        interactionContreSauge1.setImpact(-5);
        interactionContreSauge1.setDescription("");
        interactionContreSauge1.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreSauge1);
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
        Allelopathie interactionContreTomate2 = new Allelopathie();
        interactionContreTomate2.setOrigine(insertedPlants.get("concombre"));
        interactionContreTomate2.setCible(insertedPlants.get("tomate"));
        interactionContreTomate2.setImpact(-5);
        interactionContreTomate2.setDescription("");
        interactionContreTomate2.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreTomate2);
        Allelopathie interactionContreTomate3 = new Allelopathie();
        interactionContreTomate3.setOrigine(insertedPlants.get("chou rave"));
        interactionContreTomate3.setCible(insertedPlants.get("tomate"));
        interactionContreTomate3.setImpact(-5);
        interactionContreTomate3.setDescription("");
        interactionContreTomate3.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreTomate3);
        Allelopathie interactionContreTomate4 = new Allelopathie();
        interactionContreTomate4.setOrigine(insertedPlants.get("chou commun"));
        interactionContreTomate4.setCible(insertedPlants.get("tomate"));
        interactionContreTomate4.setImpact(-5);
        interactionContreTomate4.setDescription("");
        interactionContreTomate4.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreTomate4);
        Allelopathie interactionContreTomate5 = new Allelopathie();
        interactionContreTomate5.setOrigine(insertedPlants.get("pomme de terre"));
        interactionContreTomate5.setCible(insertedPlants.get("tomate"));
        interactionContreTomate5.setImpact(-5);
        interactionContreTomate5.setDescription("");
        interactionContreTomate5.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreTomate5);
        Allelopathie interactionContreTomate6 = new Allelopathie();
        interactionContreTomate6.setOrigine(insertedPlants.get("betterave"));
        interactionContreTomate6.setCible(insertedPlants.get("tomate"));
        interactionContreTomate6.setImpact(-5);
        interactionContreTomate6.setDescription("");
        interactionContreTomate6.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreTomate6);
        Allelopathie interactionContreTomate7 = new Allelopathie();
        interactionContreTomate7.setOrigine(insertedPlants.get("pois"));
        interactionContreTomate7.setCible(insertedPlants.get("tomate"));
        interactionContreTomate7.setImpact(-5);
        interactionContreTomate7.setDescription("");
        interactionContreTomate7.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreTomate7);
    }

    private void interactionContrePommeDeTerre(Map<String, Plante> insertedPlants) {
        log.info("interactionContrePommeDeTerre"); // Contre pommeDeTerre
        Allelopathie interactionContrePommeDeTerre1 = new Allelopathie();
        interactionContrePommeDeTerre1.setOrigine(insertedPlants.get("tomate"));
        interactionContrePommeDeTerre1.setCible(insertedPlants.get("pomme de terre"));
        interactionContrePommeDeTerre1.setImpact(-5);
        interactionContrePommeDeTerre1.setDescription("");
        interactionContrePommeDeTerre1.setType("Inconnu");
        getOrInsertAllelopathie(interactionContrePommeDeTerre1);
        Allelopathie interactionContrePommeDeTerre2 = new Allelopathie();
        interactionContrePommeDeTerre2.setOrigine(insertedPlants.get("courge"));
        interactionContrePommeDeTerre2.setCible(insertedPlants.get("pomme de terre"));
        interactionContrePommeDeTerre2.setImpact(-5);
        interactionContrePommeDeTerre2.setDescription("");
        interactionContrePommeDeTerre2.setType("Inconnu");
        getOrInsertAllelopathie(interactionContrePommeDeTerre2);
        Allelopathie interactionContrePommeDeTerre3 = new Allelopathie();
        interactionContrePommeDeTerre3.setOrigine(insertedPlants.get("carotte"));
        interactionContrePommeDeTerre3.setCible(insertedPlants.get("pomme de terre"));
        interactionContrePommeDeTerre3.setImpact(-5);
        interactionContrePommeDeTerre3.setDescription("");
        interactionContrePommeDeTerre3.setType("Inconnu");
        getOrInsertAllelopathie(interactionContrePommeDeTerre3);
        Allelopathie interactionContrePommeDeTerre4 = new Allelopathie();
        interactionContrePommeDeTerre4.setOrigine(insertedPlants.get("oignon"));
        interactionContrePommeDeTerre4.setCible(insertedPlants.get("pomme de terre"));
        interactionContrePommeDeTerre4.setImpact(-5);
        interactionContrePommeDeTerre4.setDescription("");
        interactionContrePommeDeTerre4.setType("Inconnu");
        getOrInsertAllelopathie(interactionContrePommeDeTerre4);
        Allelopathie interactionContrePommeDeTerre5 = new Allelopathie();
        interactionContrePommeDeTerre5.setOrigine(insertedPlants.get("framboisier"));
        interactionContrePommeDeTerre5.setCible(insertedPlants.get("pomme de terre"));
        interactionContrePommeDeTerre5.setImpact(-5);
        interactionContrePommeDeTerre5.setDescription("");
        interactionContrePommeDeTerre5.setType("Inconnu");
        getOrInsertAllelopathie(interactionContrePommeDeTerre5);
        Allelopathie interactionContrePommeDeTerre6 = new Allelopathie();
        interactionContrePommeDeTerre6.setOrigine(insertedPlants.get("arroche des jardins"));
        interactionContrePommeDeTerre6.setCible(insertedPlants.get("pomme de terre"));
        interactionContrePommeDeTerre6.setImpact(-5);
        interactionContrePommeDeTerre6.setDescription("");
        interactionContrePommeDeTerre6.setType("Inconnu");
        getOrInsertAllelopathie(interactionContrePommeDeTerre6);
        Allelopathie interactionContrePommeDeTerre7 = new Allelopathie();
        interactionContrePommeDeTerre7.setOrigine(insertedPlants.get("tournesol"));
        interactionContrePommeDeTerre7.setCible(insertedPlants.get("pomme de terre"));
        interactionContrePommeDeTerre7.setImpact(-5);
        interactionContrePommeDeTerre7.setDescription("");
        interactionContrePommeDeTerre7.setType("Inconnu");
        getOrInsertAllelopathie(interactionContrePommeDeTerre7);
        Allelopathie interactionContrePommeDeTerre8 = new Allelopathie();
        interactionContrePommeDeTerre8.setOrigine(insertedPlants.get("melon"));
        interactionContrePommeDeTerre8.setCible(insertedPlants.get("pomme de terre"));
        interactionContrePommeDeTerre8.setImpact(-5);
        interactionContrePommeDeTerre8.setDescription("");
        interactionContrePommeDeTerre8.setType("Inconnu");
        getOrInsertAllelopathie(interactionContrePommeDeTerre8);
    }

    private void interactionContrePois(Map<String, Plante> insertedPlants) {
        log.info("interactionContrePois"); // Contre pois
        Allelopathie interactionContrePois1 = new Allelopathie();
        interactionContrePois1.setOrigine(insertedPlants.get("ail"));
        interactionContrePois1.setCible(insertedPlants.get("pois"));
        interactionContrePois1.setImpact(-5);
        interactionContrePois1.setDescription("");
        interactionContrePois1.setType("Inconnu");
        getOrInsertAllelopathie(interactionContrePois1);
        Allelopathie interactionContrePois2 = new Allelopathie();
        interactionContrePois2.setOrigine(insertedPlants.get("échalote"));
        interactionContrePois2.setCible(insertedPlants.get("pois"));
        interactionContrePois2.setImpact(-5);
        interactionContrePois2.setDescription("");
        interactionContrePois2.setType("Inconnu");
        getOrInsertAllelopathie(interactionContrePois2);
        Allelopathie interactionContrePois3 = new Allelopathie();
        interactionContrePois3.setOrigine(insertedPlants.get("oignon"));
        interactionContrePois3.setCible(insertedPlants.get("pois"));
        interactionContrePois3.setImpact(-5);
        interactionContrePois3.setDescription("");
        interactionContrePois3.setType("Inconnu");
        getOrInsertAllelopathie(interactionContrePois3);
        Allelopathie interactionContrePois4 = new Allelopathie();
        interactionContrePois4.setOrigine(insertedPlants.get("poireau"));
        interactionContrePois4.setCible(insertedPlants.get("pois"));
        interactionContrePois4.setImpact(-5);
        interactionContrePois4.setDescription("");
        interactionContrePois4.setType("Inconnu");
        getOrInsertAllelopathie(interactionContrePois4);
        Allelopathie interactionContrePois5 = new Allelopathie();
        interactionContrePois5.setOrigine(insertedPlants.get("tomate"));
        interactionContrePois5.setCible(insertedPlants.get("pois"));
        interactionContrePois5.setImpact(-5);
        interactionContrePois5.setDescription("");
        interactionContrePois5.setType("Inconnu");
        getOrInsertAllelopathie(interactionContrePois5);
    }

    private void interactionContrePoireau(Map<String, Plante> insertedPlants) {
        log.info("interactionContrePoireau"); // Contre poireau
        Allelopathie interactionContrePoireau1 = new Allelopathie();
        interactionContrePoireau1.setOrigine(insertedPlants.get("brocoli"));
        interactionContrePoireau1.setCible(insertedPlants.get("poireau"));
        interactionContrePoireau1.setImpact(-5);
        interactionContrePoireau1.setDescription("");
        interactionContrePoireau1.setType("Inconnu");
        getOrInsertAllelopathie(interactionContrePoireau1);
        Allelopathie interactionContrePoireau2 = new Allelopathie();
        interactionContrePoireau2.setOrigine(insertedPlants.get("haricot"));
        interactionContrePoireau2.setCible(insertedPlants.get("poireau"));
        interactionContrePoireau2.setImpact(-5);
        interactionContrePoireau2.setDescription("");
        interactionContrePoireau2.setType("Inconnu");
        getOrInsertAllelopathie(interactionContrePoireau2);
        Allelopathie interactionContrePoireau3 = new Allelopathie();
        interactionContrePoireau3.setOrigine(insertedPlants.get("fève"));
        interactionContrePoireau3.setCible(insertedPlants.get("poireau"));
        interactionContrePoireau3.setImpact(-5);
        interactionContrePoireau3.setDescription("");
        interactionContrePoireau3.setType("Inconnu");
        getOrInsertAllelopathie(interactionContrePoireau3);
    }

    private void interactionContreOignon(Map<String, Plante> insertedPlants) {
        log.info("interactionContreOignon"); // Contre oignon
        Allelopathie interactionContreOignon1 = new Allelopathie();
        interactionContreOignon1.setOrigine(insertedPlants.get("pois"));
        interactionContreOignon1.setCible(insertedPlants.get("oignon"));
        interactionContreOignon1.setImpact(-5);
        interactionContreOignon1.setDescription("");
        interactionContreOignon1.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreOignon1);
        Allelopathie interactionContreOignon2 = new Allelopathie();
        interactionContreOignon2.setOrigine(insertedPlants.get("haricot"));
        interactionContreOignon2.setCible(insertedPlants.get("oignon"));
        interactionContreOignon2.setImpact(-5);
        interactionContreOignon2.setDescription("");
        interactionContreOignon2.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreOignon2);
        Allelopathie interactionContreOignon3 = new Allelopathie();
        interactionContreOignon3.setOrigine(insertedPlants.get("fève"));
        interactionContreOignon3.setCible(insertedPlants.get("oignon"));
        interactionContreOignon3.setImpact(-5);
        interactionContreOignon3.setDescription("");
        interactionContreOignon3.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreOignon3);
        Allelopathie interactionContreOignon4 = new Allelopathie();
        interactionContreOignon4.setOrigine(insertedPlants.get("lentille"));
        interactionContreOignon4.setCible(insertedPlants.get("oignon"));
        interactionContreOignon4.setImpact(-5);
        interactionContreOignon4.setDescription("");
        interactionContreOignon4.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreOignon4);
    }

    private void interactionContreMelon(Map<String, Plante> insertedPlants) {
        log.info("interactionContreMelon"); // Contre melon
        Allelopathie interactionContreMelon1 = new Allelopathie();
        interactionContreMelon1.setOrigine(insertedPlants.get("concombre"));
        interactionContreMelon1.setCible(insertedPlants.get("melon"));
        interactionContreMelon1.setImpact(-5);
        interactionContreMelon1.setDescription("");
        interactionContreMelon1.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreMelon1);
        Allelopathie interactionContreMelon2 = new Allelopathie();
        interactionContreMelon2.setOrigine(insertedPlants.get("courge"));
        interactionContreMelon2.setCible(insertedPlants.get("melon"));
        interactionContreMelon2.setImpact(-5);
        interactionContreMelon2.setDescription("");
        interactionContreMelon2.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreMelon2);
    }

    private void interactionContreLaitue(Map<String, Plante> insertedPlants) {
        log.info("interactionContreLaitue"); // Contre laitue
        Allelopathie interactionContreLaitue1 = new Allelopathie();
        interactionContreLaitue1.setOrigine(insertedPlants.get("tournesol"));
        interactionContreLaitue1.setCible(insertedPlants.get("laitue"));
        interactionContreLaitue1.setImpact(-5);
        interactionContreLaitue1.setDescription("");
        interactionContreLaitue1.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreLaitue1);
        Allelopathie interactionContreLaitue2 = new Allelopathie();
        interactionContreLaitue2.setOrigine(insertedPlants.get("persil"));
        interactionContreLaitue2.setCible(insertedPlants.get("laitue"));
        interactionContreLaitue2.setImpact(-5);
        interactionContreLaitue2.setDescription("");
        interactionContreLaitue2.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreLaitue2);
    }

    private void interactionContreHaricot(Map<String, Plante> insertedPlants) {
        log.info("interactionContreHaricot"); // Contre haricot
        Allelopathie interactionContreHaricot1 = new Allelopathie();
        interactionContreHaricot1.setOrigine(insertedPlants.get("oignon"));
        interactionContreHaricot1.setCible(insertedPlants.get("haricot"));
        interactionContreHaricot1.setImpact(-5);
        interactionContreHaricot1.setDescription("");
        interactionContreHaricot1.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreHaricot1);
        Allelopathie interactionContreHaricot2 = new Allelopathie();
        interactionContreHaricot2.setOrigine(insertedPlants.get("ail"));
        interactionContreHaricot2.setCible(insertedPlants.get("haricot"));
        interactionContreHaricot2.setImpact(-5);
        interactionContreHaricot2.setDescription("");
        interactionContreHaricot2.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreHaricot2);
        Allelopathie interactionContreHaricot3 = new Allelopathie();
        interactionContreHaricot3.setOrigine(insertedPlants.get("échalote"));
        interactionContreHaricot3.setCible(insertedPlants.get("haricot"));
        interactionContreHaricot3.setImpact(-5);
        interactionContreHaricot3.setDescription("");
        interactionContreHaricot3.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreHaricot3);
        Allelopathie interactionContreHaricot4 = new Allelopathie();
        interactionContreHaricot4.setOrigine(insertedPlants.get("tomate"));
        interactionContreHaricot4.setCible(insertedPlants.get("haricot"));
        interactionContreHaricot4.setImpact(-5);
        interactionContreHaricot4.setDescription("");
        interactionContreHaricot4.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreHaricot4);
        Allelopathie interactionContreHaricot5 = new Allelopathie();
        interactionContreHaricot5.setOrigine(insertedPlants.get("fenouil"));
        interactionContreHaricot5.setCible(insertedPlants.get("haricot"));
        interactionContreHaricot5.setImpact(-5);
        interactionContreHaricot5.setDescription("");
        interactionContreHaricot5.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreHaricot5);
        Allelopathie interactionContreHaricot6 = new Allelopathie();
        interactionContreHaricot6.setOrigine(insertedPlants.get("pois"));
        interactionContreHaricot6.setCible(insertedPlants.get("haricot"));
        interactionContreHaricot6.setImpact(-5);
        interactionContreHaricot6.setDescription("");
        interactionContreHaricot6.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreHaricot6);
    }

    private void interactionContreFenouil(Map<String, Plante> insertedPlants) {
        log.info("interactionContreFenouil"); // Contre fenouil
        Allelopathie interactionContreFenouil1 = new Allelopathie();
        interactionContreFenouil1.setOrigine(insertedPlants.get("tomate"));
        interactionContreFenouil1.setCible(insertedPlants.get("fenouil"));
        interactionContreFenouil1.setImpact(-5);
        interactionContreFenouil1.setDescription("");
        interactionContreFenouil1.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreFenouil1);
        Allelopathie interactionContreFenouil2 = new Allelopathie();
        interactionContreFenouil2.setOrigine(insertedPlants.get("chou rave"));
        interactionContreFenouil2.setCible(insertedPlants.get("fenouil"));
        interactionContreFenouil2.setImpact(-5);
        interactionContreFenouil2.setDescription("");
        interactionContreFenouil2.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreFenouil2);
        Allelopathie interactionContreFenouil4 = new Allelopathie();
        interactionContreFenouil4.setOrigine(insertedPlants.get("carvi"));
        interactionContreFenouil4.setCible(insertedPlants.get("fenouil"));
        interactionContreFenouil4.setImpact(-5);
        interactionContreFenouil4.setDescription("");
        interactionContreFenouil4.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreFenouil4);
        Allelopathie interactionContreFenouil5 = new Allelopathie();
        interactionContreFenouil5.setOrigine(insertedPlants.get("haricot"));
        interactionContreFenouil5.setCible(insertedPlants.get("fenouil"));
        interactionContreFenouil5.setImpact(-5);
        interactionContreFenouil5.setDescription("");
        interactionContreFenouil5.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreFenouil5);
        Allelopathie interactionContreFenouil6 = new Allelopathie();
        interactionContreFenouil6.setOrigine(insertedPlants.get("pois"));
        interactionContreFenouil6.setCible(insertedPlants.get("fenouil"));
        interactionContreFenouil6.setImpact(-5);
        interactionContreFenouil6.setDescription("");
        interactionContreFenouil6.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreFenouil6);
        Allelopathie interactionContreFenouil7 = new Allelopathie();
        interactionContreFenouil7.setOrigine(insertedPlants.get("échalote"));
        interactionContreFenouil7.setCible(insertedPlants.get("fenouil"));
        interactionContreFenouil7.setImpact(-5);
        interactionContreFenouil7.setDescription("");
        interactionContreFenouil7.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreFenouil7);
        Allelopathie interactionContreFenouil8 = new Allelopathie();
        interactionContreFenouil8.setOrigine(insertedPlants.get("absinthe"));
        interactionContreFenouil8.setCible(insertedPlants.get("fenouil"));
        interactionContreFenouil8.setImpact(-5);
        interactionContreFenouil8.setDescription("L'absinthe semble gêner la formation des graines");
        interactionContreFenouil8.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreFenouil8);
        Allelopathie interactionContreFenouil9 = new Allelopathie();
        interactionContreFenouil9.setOrigine(insertedPlants.get("coriandre"));
        interactionContreFenouil9.setCible(insertedPlants.get("fenouil"));
        interactionContreFenouil9.setImpact(-5);
        interactionContreFenouil9.setDescription("La coriandre semble gêner la formation des graines");
        interactionContreFenouil9.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreFenouil9);
    }

    private void interactionContreEpinard(Map<String, Plante> insertedPlants) {
        log.info("interactionContreEpinard"); // Contre epinard
        Allelopathie interactionContreEpinard1 = new Allelopathie();
        interactionContreEpinard1.setOrigine(insertedPlants.get("pomme de terre"));
        interactionContreEpinard1.setCible(insertedPlants.get("épinard"));
        interactionContreEpinard1.setImpact(-5);
        interactionContreEpinard1.setDescription("");
        interactionContreEpinard1.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreEpinard1);
        Allelopathie interactionContreEpinard2 = new Allelopathie();
        interactionContreEpinard2.setOrigine(insertedPlants.get("betterave"));
        interactionContreEpinard2.setCible(insertedPlants.get("épinard"));
        interactionContreEpinard2.setImpact(-5);
        interactionContreEpinard2.setDescription("");
        interactionContreEpinard2.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreEpinard2);
    }

    private void interactionContreEchalotte(Map<String, Plante> insertedPlants) {
        log.info("interactionContreEchalotte"); // Contre echalotte
        Allelopathie interactionContreEchalotte1 = new Allelopathie();
        interactionContreEchalotte1.setOrigine(insertedPlants.get("pois"));
        interactionContreEchalotte1.setCible(insertedPlants.get("échalote"));
        interactionContreEchalotte1.setImpact(-5);
        interactionContreEchalotte1.setDescription("");
        interactionContreEchalotte1.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreEchalotte1);
        Allelopathie interactionContreEchalotte2 = new Allelopathie();
        interactionContreEchalotte2.setOrigine(insertedPlants.get("haricot"));
        interactionContreEchalotte2.setCible(insertedPlants.get("échalote"));
        interactionContreEchalotte2.setImpact(-5);
        interactionContreEchalotte2.setDescription("");
        interactionContreEchalotte2.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreEchalotte2);
        Allelopathie interactionContreEchalotte3 = new Allelopathie();
        interactionContreEchalotte3.setOrigine(insertedPlants.get("fève"));
        interactionContreEchalotte3.setCible(insertedPlants.get("échalote"));
        interactionContreEchalotte3.setImpact(-5);
        interactionContreEchalotte3.setDescription("");
        interactionContreEchalotte3.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreEchalotte3);
        Allelopathie interactionContreEchalotte4 = new Allelopathie();
        interactionContreEchalotte4.setOrigine(insertedPlants.get("lentille"));
        interactionContreEchalotte4.setCible(insertedPlants.get("échalote"));
        interactionContreEchalotte4.setImpact(-5);
        interactionContreEchalotte4.setDescription("");
        interactionContreEchalotte4.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreEchalotte4);
    }

    private void interactionContreCourge(Map<String, Plante> insertedPlants) {
        log.info("interactionContreCourge"); // Contre courge
        Allelopathie interactionContreCourge1 = new Allelopathie();
        interactionContreCourge1.setOrigine(insertedPlants.get("pomme de terre"));
        interactionContreCourge1.setCible(insertedPlants.get("courge"));
        interactionContreCourge1.setImpact(-5);
        interactionContreCourge1.setDescription("");
        interactionContreCourge1.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreCourge1);
    }

    private void interactionContreConcombre(Map<String, Plante> insertedPlants) {
        log.info("interactionContreConcombre"); // Contre concombre
        Allelopathie interactionContreConcombre1 = new Allelopathie();
        interactionContreConcombre1.setOrigine(insertedPlants.get("pomme de terre"));
        interactionContreConcombre1.setCible(insertedPlants.get("concombre"));
        interactionContreConcombre1.setImpact(-5);
        interactionContreConcombre1.setDescription("");
        interactionContreConcombre1.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreConcombre1);
        Allelopathie interactionContreConcombre2 = new Allelopathie();
        interactionContreConcombre2.setOrigine(insertedPlants.get("tomate"));
        interactionContreConcombre2.setCible(insertedPlants.get("concombre"));
        interactionContreConcombre2.setImpact(-5);
        interactionContreConcombre2.setDescription("");
        interactionContreConcombre2.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreConcombre2);
        Allelopathie interactionContreConcombre3 = new Allelopathie();
        interactionContreConcombre3.setOrigine(insertedPlants.get("courgette"));
        interactionContreConcombre3.setCible(insertedPlants.get("concombre"));
        interactionContreConcombre3.setImpact(-5);
        interactionContreConcombre3.setDescription("");
        interactionContreConcombre3.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreConcombre3);
    }

    private void interactionContreChou(Map<String, Plante> insertedPlants) {
        log.info("interactionContreChou"); // Contre chou
        Allelopathie interactionContreChou1 = new Allelopathie();
        interactionContreChou1.setOrigine(insertedPlants.get("fraisier des bois"));
        interactionContreChou1.setCible(insertedPlants.get("chou commun"));
        interactionContreChou1.setImpact(-5);
        interactionContreChou1.setDescription("");
        interactionContreChou1.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreChou1);
        Allelopathie interactionContreChou2 = new Allelopathie();
        interactionContreChou2.setOrigine(insertedPlants.get("oignon"));
        interactionContreChou2.setCible(insertedPlants.get("chou commun"));
        interactionContreChou2.setImpact(-5);
        interactionContreChou2.setDescription("");
        interactionContreChou2.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreChou2);
        Allelopathie interactionContreChou3 = new Allelopathie();
        interactionContreChou3.setOrigine(insertedPlants.get("tomate"));
        interactionContreChou3.setCible(insertedPlants.get("chou commun"));
        interactionContreChou3.setImpact(-5);
        interactionContreChou3.setDescription("");
        interactionContreChou3.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreChou3);
    }

    private void interactionContreRadis(Map<String, Plante> insertedPlants) {
        log.info("interactionContreRadis"); // Contre radis
        Allelopathie interactionContreRadis1 = new Allelopathie();
        interactionContreRadis1.setOrigine(insertedPlants.get("cerfeuil"));
        interactionContreRadis1.setCible(insertedPlants.get("radis"));
        interactionContreRadis1.setImpact(-5);
        interactionContreRadis1.setDescription("");
        interactionContreRadis1.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreRadis1);
    }

    private void interactionContreBetterave(Map<String, Plante> insertedPlants) {
        log.info("interactionContreBetterave"); // Contre betterave
        Allelopathie interactionContreBetterave1 = new Allelopathie();
        interactionContreBetterave1.setOrigine(insertedPlants.get("haricot"));
        interactionContreBetterave1.setCible(insertedPlants.get("betterave"));
        interactionContreBetterave1.setImpact(-5);
        interactionContreBetterave1.setDescription("");
        interactionContreBetterave1.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreBetterave1);
        Allelopathie interactionContreBetterave2 = new Allelopathie();
        interactionContreBetterave2.setOrigine(insertedPlants.get("épinard"));
        interactionContreBetterave2.setCible(insertedPlants.get("betterave"));
        interactionContreBetterave2.setImpact(-5);
        interactionContreBetterave2.setDescription("");
        interactionContreBetterave2.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreBetterave2);
    }

    private void interactionContreBasilic(Map<String, Plante> insertedPlants) {
        log.info("interactionContreBasilic"); // Contre basilic
        Allelopathie interactionContreBasilic1 = new Allelopathie();
        interactionContreBasilic1.setOrigine(insertedPlants.get("rue officinale"));
        interactionContreBasilic1.setCible(insertedPlants.get("basilic"));
        interactionContreBasilic1.setImpact(-5);
        interactionContreBasilic1.setDescription("");
        interactionContreBasilic1.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreBasilic1);
    }

    private void interactionContreArtichaut(Map<String, Plante> insertedPlants) {
        log.info("interactionContreArtichaut"); // Contre artichaut
        Allelopathie interactionContreArtichaut1 = new Allelopathie();
        interactionContreArtichaut1.setOrigine(insertedPlants.get("fève"));
        interactionContreArtichaut1.setCible(insertedPlants.get("artichaut"));
        interactionContreArtichaut1.setImpact(-5);
        interactionContreArtichaut1.setDescription("");
        interactionContreArtichaut1.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreArtichaut1);
    }

    private void interactionContreAbsinthe(Map<String, Plante> insertedPlants) {
        log.info("interactionContreAbsinthe"); // Contre absinthe
        Allelopathie interactionContreAbsinthe1 = new Allelopathie();
        interactionContreAbsinthe1.setOrigine(insertedPlants.get("carvi"));
        interactionContreAbsinthe1.setCible(insertedPlants.get("absinthe"));
        interactionContreAbsinthe1.setImpact(-5);
        interactionContreAbsinthe1.setDescription("");
        interactionContreAbsinthe1.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreAbsinthe1);
        Allelopathie interactionContreAbsinthe2 = new Allelopathie();
        interactionContreAbsinthe2.setOrigine(insertedPlants.get("sauge"));
        interactionContreAbsinthe2.setCible(insertedPlants.get("absinthe"));
        interactionContreAbsinthe2.setImpact(-5);
        interactionContreAbsinthe2.setDescription("");
        interactionContreAbsinthe2.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreAbsinthe2);
        Allelopathie interactionContreAbsinthe3 = new Allelopathie();
        interactionContreAbsinthe3.setOrigine(insertedPlants.get("anis vert"));
        interactionContreAbsinthe3.setCible(insertedPlants.get("absinthe"));
        interactionContreAbsinthe3.setImpact(-5);
        interactionContreAbsinthe3.setDescription("");
        interactionContreAbsinthe3.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreAbsinthe3);
        Allelopathie interactionContreAbsinthe4 = new Allelopathie();
        interactionContreAbsinthe4.setOrigine(insertedPlants.get("fenouil"));
        interactionContreAbsinthe4.setCible(insertedPlants.get("absinthe"));
        interactionContreAbsinthe4.setImpact(-5);
        interactionContreAbsinthe4.setDescription("");
        interactionContreAbsinthe4.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreAbsinthe4);
    }

    private void interactionContreCarotte(Map<String, Plante> insertedPlants) {
        log.info("interactionContreCarotte"); // Contre carotte
        Allelopathie interactionContreCarotte1 = new Allelopathie();
        interactionContreCarotte1.setOrigine(insertedPlants.get("aneth"));
        interactionContreCarotte1.setCible(insertedPlants.get("carotte"));
        interactionContreCarotte1.setImpact(-5);
        interactionContreCarotte1.setDescription(
            "En compagnie d'aneth la carotte est sensible aux maladies et aux ravageurs et au mieux végètera. " +
                "Elle ne poussent pas là où auparavant il y en avait déjà"
        );
        interactionContreCarotte1.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreCarotte1);
    }

    private void interactionContreAil(Map<String, Plante> insertedPlants) {
        log.info("interactionContreAil"); // Contre insertedPlants.get("ail")
        Allelopathie interactionContreAil1 = new Allelopathie();
        interactionContreAil1.setOrigine(insertedPlants.get("fève"));
        interactionContreAil1.setCible(insertedPlants.get("ail"));
        interactionContreAil1.setImpact(-5);
        interactionContreAil1.setDescription("");
        interactionContreAil1.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreAil1);
        Allelopathie interactionContreAil2 = new Allelopathie();
        interactionContreAil2.setOrigine(insertedPlants.get("lentille"));
        interactionContreAil2.setCible(insertedPlants.get("ail"));
        interactionContreAil2.setImpact(-5);
        interactionContreAil2.setDescription("");
        interactionContreAil2.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreAil2);
        Allelopathie interactionContreAil3 = new Allelopathie();
        interactionContreAil3.setOrigine(insertedPlants.get("haricot"));
        interactionContreAil3.setCible(insertedPlants.get("ail"));
        interactionContreAil3.setImpact(-5);
        interactionContreAil3.setDescription("");
        interactionContreAil3.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreAil3);
        Allelopathie interactionContreAil4 = new Allelopathie();
        interactionContreAil4.setOrigine(insertedPlants.get("pois"));
        interactionContreAil4.setCible(insertedPlants.get("ail"));
        interactionContreAil4.setImpact(-5);
        interactionContreAil4.setDescription("");
        interactionContreAil4.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreAil4);
    }

    private void interactionContreChouFleur(Map<String, Plante> insertedPlants) {
        log.info("interactionContreChouFleur"); // Contre chouFleur
        Allelopathie interactionContreChouFleur1 = new Allelopathie();
        interactionContreChouFleur1.setOrigine(insertedPlants.get("céleri"));
        interactionContreChouFleur1.setCible(insertedPlants.get("chou fleur"));
        interactionContreChouFleur1.setImpact(-5);
        interactionContreChouFleur1.setDescription(
            "Le chou fleur utilise mieux les nutriments présents dans le sols lorsqu'il accompagné du céleri, de même pour ce dernier."
        );
        interactionContreChouFleur1.setType("Inconnu");
        getOrInsertAllelopathie(interactionContreChouFleur1);
    }

    private void interactionPourCeleri(Map<String, Plante> insertedPlants) {
        log.info("interactionPourCeleri"); // Pour celeri
        Allelopathie interactionPourCeleri1 = new Allelopathie();
        interactionPourCeleri1.setOrigine(insertedPlants.get("poireau"));
        interactionPourCeleri1.setCible(insertedPlants.get("céleri"));
        interactionPourCeleri1.setImpact(5);
        interactionPourCeleri1.setDescription("");
        interactionPourCeleri1.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourCeleri1);
        Allelopathie interactionPourCeleri2 = new Allelopathie();
        interactionPourCeleri2.setOrigine(insertedPlants.get("tomate"));
        interactionPourCeleri2.setCible(insertedPlants.get("céleri"));
        interactionPourCeleri2.setImpact(5);
        interactionPourCeleri2.setDescription("");
        interactionPourCeleri2.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourCeleri2);
        Allelopathie interactionPourCeleri3 = new Allelopathie();
        interactionPourCeleri3.setOrigine(insertedPlants.get("chou fleur"));
        interactionPourCeleri3.setCible(insertedPlants.get("céleri"));
        interactionPourCeleri3.setImpact(5);
        interactionPourCeleri3.setDescription(
            "Le céleri cultivé seul n'utilise qu'une partie des substances nutritives trouvées dans le sol. Quand on le plante avec du chou fleur, " +
                "il les utilise mieux, de même que ce dernier. La récolte est alors meilleure pour les deux plantes."
        );
        interactionPourCeleri3.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourCeleri3);
        Allelopathie interactionPourCeleri4 = new Allelopathie();
        interactionPourCeleri4.setOrigine(insertedPlants.get("épinard"));
        interactionPourCeleri4.setCible(insertedPlants.get("céleri"));
        interactionPourCeleri4.setImpact(5);
        interactionPourCeleri4.setDescription("");
        interactionPourCeleri4.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourCeleri4);
        Allelopathie interactionPourCeleri5 = new Allelopathie();
        interactionPourCeleri5.setOrigine(insertedPlants.get("concombre"));
        interactionPourCeleri5.setCible(insertedPlants.get("céleri"));
        interactionPourCeleri5.setImpact(5);
        interactionPourCeleri5.setDescription("");
        interactionPourCeleri5.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourCeleri5);
        Allelopathie interactionPourCeleri6 = new Allelopathie();
        interactionPourCeleri6.setOrigine(insertedPlants.get("haricot"));
        interactionPourCeleri6.setCible(insertedPlants.get("céleri"));
        interactionPourCeleri6.setImpact(5);
        interactionPourCeleri6.setDescription("");
        interactionPourCeleri6.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourCeleri6);
    }

    private void interactionPourBette(Map<String, Plante> insertedPlants) {
        log.info("interactionPourBette"); // Pour bette
        Allelopathie interactionPourBette1 = new Allelopathie();
        interactionPourBette1.setOrigine(insertedPlants.get("radis"));
        interactionPourBette1.setCible(insertedPlants.get("bette"));
        interactionPourBette1.setImpact(5);
        interactionPourBette1.setDescription("");
        interactionPourBette1.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourBette1);
        Allelopathie interactionPourBette2 = new Allelopathie();
        interactionPourBette2.setOrigine(insertedPlants.get("carotte"));
        interactionPourBette2.setCible(insertedPlants.get("bette"));
        interactionPourBette2.setImpact(5);
        interactionPourBette2.setDescription("");
        interactionPourBette2.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourBette2);
        Allelopathie interactionPourBette3 = new Allelopathie();
        interactionPourBette3.setOrigine(insertedPlants.get("haricot"));
        interactionPourBette3.setCible(insertedPlants.get("bette"));
        interactionPourBette3.setImpact(5);
        interactionPourBette3.setDescription("");
        interactionPourBette3.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourBette3);
        Allelopathie interactionPourBette4 = new Allelopathie();
        interactionPourBette4.setOrigine(insertedPlants.get("raifort"));
        interactionPourBette4.setCible(insertedPlants.get("bette"));
        interactionPourBette4.setImpact(5);
        interactionPourBette4.setDescription("");
        interactionPourBette4.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourBette4);
    }

    private void interactionPourArtichaut(Map<String, Plante> insertedPlants) {
        log.info("interactionPourArtichaut"); // Pour artichaut
        Allelopathie interactionPourArtichaut1 = new Allelopathie();
        interactionPourArtichaut1.setOrigine(insertedPlants.get("fève"));
        interactionPourArtichaut1.setCible(insertedPlants.get("artichaut"));
        interactionPourArtichaut1.setImpact(5);
        interactionPourArtichaut1.setDescription("");
        interactionPourArtichaut1.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourArtichaut1);
    }

    private void interactionPourPoivron(Map<String, Plante> insertedPlants) {
        log.info("interactionPourPoivron"); // Pour poivron
        Allelopathie interactionPourPoivron1 = new Allelopathie();
        interactionPourPoivron1.setOrigine(insertedPlants.get("basilic"));
        interactionPourPoivron1.setCible(insertedPlants.get("poivron"));
        interactionPourPoivron1.setImpact(5);
        interactionPourPoivron1.setDescription("Le basilic s'associe parfaitement avec le genre capsicum");
        interactionPourPoivron1.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourPoivron1);
    }

    private void interactionPourPiment(Map<String, Plante> insertedPlants) {
        log.info("interactionPourPiment"); // Pour piment
        Allelopathie interactionPourPiment1 = new Allelopathie();
        interactionPourPiment1.setOrigine(insertedPlants.get("basilic"));
        interactionPourPiment1.setCible(insertedPlants.get("piment"));
        interactionPourPiment1.setImpact(5);
        interactionPourPiment1.setDescription("Le basilic s'associe parfaitement avec le genre capsicum");
        interactionPourPiment1.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourPiment1);
    }

    private void interactionPourRosier(Map<String, Plante> insertedPlants) {
        log.info("interactionPourRosier"); // Pour rosiers
        Allelopathie interactionPourRosier1 = new Allelopathie();
        interactionPourRosier1.setOrigine(insertedPlants.get("géranium des prés"));
        interactionPourRosier1.setCible(insertedPlants.get("rosier de france"));
        interactionPourRosier1.setImpact(5);
        interactionPourRosier1.setDescription("Les géraniums protègent les rosiers");
        interactionPourRosier1.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourRosier1);
        Allelopathie interactionPourRosier2 = new Allelopathie();
        interactionPourRosier2.setOrigine(insertedPlants.get("ail"));
        interactionPourRosier2.setCible(insertedPlants.get("rosier de france"));
        interactionPourRosier2.setImpact(5);
        interactionPourRosier2.setDescription("L'ail planté au pied des rosiers les rends plus beaux et résistants");
        interactionPourRosier2.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourRosier2);
        Allelopathie interactionPourRosier3 = new Allelopathie();
        interactionPourRosier3.setOrigine(insertedPlants.get("menthe poivrée"));
        interactionPourRosier3.setCible(insertedPlants.get("rosier de france"));
        interactionPourRosier3.setImpact(5);
        interactionPourRosier3.setDescription("Les pucerons noirs des rosiers sont repoussés par la menthe verte ou poivrée");
        interactionPourRosier3.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourRosier3);
    }

    private void interactionPourRadis(Map<String, Plante> insertedPlants) {
        log.info("interactionPourRadis"); // Pour radis
        Allelopathie interactionPourRadis1 = new Allelopathie();
        interactionPourRadis1.setOrigine(insertedPlants.get("épinard"));
        interactionPourRadis1.setCible(insertedPlants.get("radis"));
        interactionPourRadis1.setImpact(5);
        interactionPourRadis1.setDescription("");
        interactionPourRadis1.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourRadis1);
        Allelopathie interactionPourRadis2 = new Allelopathie();
        interactionPourRadis2.setOrigine(insertedPlants.get("menthe poivrée"));
        interactionPourRadis2.setCible(insertedPlants.get("radis"));
        interactionPourRadis2.setImpact(5);
        interactionPourRadis2.setDescription("");
        interactionPourRadis2.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourRadis2);
        Allelopathie interactionPourRadis3 = new Allelopathie();
        interactionPourRadis3.setOrigine(insertedPlants.get("concombre"));
        interactionPourRadis3.setCible(insertedPlants.get("radis"));
        interactionPourRadis3.setImpact(5);
        interactionPourRadis3.setDescription("");
        interactionPourRadis3.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourRadis3);
        Allelopathie interactionPourRadis4 = new Allelopathie();
        interactionPourRadis4.setOrigine(insertedPlants.get("grande capucine"));
        interactionPourRadis4.setCible(insertedPlants.get("radis"));
        interactionPourRadis4.setImpact(5);
        interactionPourRadis4.setDescription("");
        interactionPourRadis4.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourRadis4);
        Allelopathie interactionPourRadis5 = new Allelopathie();
        interactionPourRadis5.setOrigine(insertedPlants.get("carotte"));
        interactionPourRadis5.setCible(insertedPlants.get("radis"));
        interactionPourRadis5.setImpact(5);
        interactionPourRadis5.setDescription("");
        interactionPourRadis5.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourRadis5);
        Allelopathie interactionPourRadis6 = new Allelopathie();
        interactionPourRadis6.setOrigine(insertedPlants.get("laitue"));
        interactionPourRadis6.setCible(insertedPlants.get("radis"));
        interactionPourRadis6.setImpact(5);
        interactionPourRadis6.setDescription("");
        interactionPourRadis6.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourRadis6);
        Allelopathie interactionPourRadis7 = new Allelopathie();
        interactionPourRadis7.setOrigine(insertedPlants.get("pois"));
        interactionPourRadis7.setCible(insertedPlants.get("radis"));
        interactionPourRadis7.setImpact(5);
        interactionPourRadis7.setDescription("");
        interactionPourRadis7.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourRadis7);
    }

    private void interactionPourPois(Map<String, Plante> insertedPlants) {
        log.info("interactionPourPois"); // Pour pois
        Allelopathie interactionPourPois1 = new Allelopathie();
        interactionPourPois1.setOrigine(insertedPlants.get("radis"));
        interactionPourPois1.setCible(insertedPlants.get("pois"));
        interactionPourPois1.setImpact(5);
        interactionPourPois1.setDescription("");
        interactionPourPois1.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourPois1);
        Allelopathie interactionPourPois2 = new Allelopathie();
        interactionPourPois2.setOrigine(insertedPlants.get("carotte"));
        interactionPourPois2.setCible(insertedPlants.get("pois"));
        interactionPourPois2.setImpact(5);
        interactionPourPois2.setDescription("");
        interactionPourPois2.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourPois2);
        Allelopathie interactionPourPois3 = new Allelopathie();
        interactionPourPois3.setOrigine(insertedPlants.get("concombre"));
        interactionPourPois3.setCible(insertedPlants.get("pois"));
        interactionPourPois3.setImpact(5);
        interactionPourPois3.setDescription("");
        interactionPourPois3.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourPois3);
        Allelopathie interactionPourPois4 = new Allelopathie();
        interactionPourPois4.setOrigine(insertedPlants.get("maïs"));
        interactionPourPois4.setCible(insertedPlants.get("pois"));
        interactionPourPois4.setImpact(5);
        interactionPourPois4.setDescription("");
        interactionPourPois4.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourPois4);
        Allelopathie interactionPourPois5 = new Allelopathie();
        interactionPourPois5.setOrigine(insertedPlants.get("pomme de terre"));
        interactionPourPois5.setCible(insertedPlants.get("pois"));
        interactionPourPois5.setImpact(5);
        interactionPourPois5.setDescription("");
        interactionPourPois5.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourPois5);
    }

    private void interactionPourPommier(Map<String, Plante> insertedPlants) {
        log.info("interactionPourPommier"); // Pour pommier
        Allelopathie interactionPourPommier1 = new Allelopathie();
        interactionPourPommier1.setOrigine(insertedPlants.get("ciboulette"));
        interactionPourPommier1.setCible(insertedPlants.get("pommier"));
        interactionPourPommier1.setImpact(5);
        interactionPourPommier1.setDescription(
            "La ciboulette se plante près des pommiers pour prévenir de la tavelure, de la gale et des chancres"
        );
        interactionPourPommier1.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourPommier1);
        Allelopathie interactionPourPommier2 = new Allelopathie();
        interactionPourPommier2.setOrigine(insertedPlants.get("grande capucine"));
        interactionPourPommier2.setCible(insertedPlants.get("pommier"));
        interactionPourPommier2.setImpact(5);
        interactionPourPommier2.setDescription("Plantée au pied des pommiers, elle prévient contre le puceron lanigère");
        interactionPourPommier2.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourPommier2);
    }

    private void interactionPourPoirier(Map<String, Plante> insertedPlants) {
        log.info("interactionPourPoirier"); // Pour poirier
        Allelopathie interactionPourPoirier1 = new Allelopathie();
        interactionPourPoirier1.setOrigine(insertedPlants.get("sauge"));
        interactionPourPoirier1.setCible(insertedPlants.get("poirier"));
        interactionPourPoirier1.setImpact(5);
        interactionPourPoirier1.setDescription("");
        interactionPourPoirier1.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourPoirier1);
    }

    private void interactionPourPecher(Map<String, Plante> insertedPlants) {
        log.info("interactionPourPecher"); // Pour pêcher
        Allelopathie interactionPourPecher1 = new Allelopathie();
        interactionPourPecher1.setOrigine(insertedPlants.get("tanaisie"));
        interactionPourPecher1.setCible(insertedPlants.get("pêcher"));
        interactionPourPecher1.setImpact(5);
        interactionPourPecher1.setDescription("");
        interactionPourPecher1.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourPecher1);
        Allelopathie interactionPourPecher2 = new Allelopathie();
        interactionPourPecher2.setOrigine(insertedPlants.get("ail"));
        interactionPourPecher2.setCible(insertedPlants.get("pêcher"));
        interactionPourPecher2.setImpact(5);
        interactionPourPecher2.setDescription("L'ail planté aux pieds des pêchers pour protéger de la cloque");
        interactionPourPecher2.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourPecher2);
        Allelopathie interactionPourPecher3 = new Allelopathie();
        interactionPourPecher3.setOrigine(insertedPlants.get("oignon"));
        interactionPourPecher3.setCible(insertedPlants.get("pêcher"));
        interactionPourPecher3.setImpact(5);
        interactionPourPecher3.setDescription("L'oignon planté aux pieds des pêchers pour protéger de la cloque");
        interactionPourPecher3.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourPecher3);
    }

    private void interactionPourPoireau(Map<String, Plante> insertedPlants) {
        log.info("interactionPourPoireau"); // Pour poireau
        Allelopathie interactionPourPoireau1 = new Allelopathie();
        interactionPourPoireau1.setOrigine(insertedPlants.get("carotte"));
        interactionPourPoireau1.setCible(insertedPlants.get("poireau"));
        interactionPourPoireau1.setImpact(5);
        interactionPourPoireau1.setDescription("Les carottes contribues à la lutte contre la teigne du poireau");
        interactionPourPoireau1.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourPoireau1);
    }

    private void interactionPourOignon(Map<String, Plante> insertedPlants) {
        log.info("interactionPourOignon"); // Pour oignon
        Allelopathie interactionPourOignon1 = new Allelopathie();
        interactionPourOignon1.setOrigine(insertedPlants.get("carotte"));
        interactionPourOignon1.setCible(insertedPlants.get("oignon"));
        interactionPourOignon1.setImpact(5);
        interactionPourOignon1.setDescription("La mouche de l’oignon est repoussée par les carottes. Leur odeur repousse les mouches.");
        interactionPourOignon1.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourOignon1);
        Allelopathie interactionPourOignon2 = new Allelopathie();
        interactionPourOignon2.setOrigine(insertedPlants.get("fraisier des bois"));
        interactionPourOignon2.setCible(insertedPlants.get("oignon"));
        interactionPourOignon2.setImpact(5);
        interactionPourOignon2.setDescription("");
        interactionPourOignon2.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourOignon2);
    }

    private void interactionPourMelon(Map<String, Plante> insertedPlants) {
        log.info("interactionPourMelon"); // Pour melon
        Allelopathie interactionPourMelon1 = new Allelopathie();
        interactionPourMelon1.setOrigine(insertedPlants.get("origan"));
        interactionPourMelon1.setCible(insertedPlants.get("melon"));
        interactionPourMelon1.setImpact(5);
        interactionPourMelon1.setDescription("");
        interactionPourMelon1.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourMelon1);
    }

    private void interactionPourMais(Map<String, Plante> insertedPlants) {
        log.info("interactionPourMais"); // Pour mais
        Allelopathie interactionPourMais1 = new Allelopathie();
        interactionPourMais1.setOrigine(insertedPlants.get("haricot"));
        interactionPourMais1.setCible(insertedPlants.get("maïs"));
        interactionPourMais1.setImpact(5);
        interactionPourMais1.setDescription("");
        interactionPourMais1.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourMais1);
        Allelopathie interactionPourMais2 = new Allelopathie();
        interactionPourMais2.setOrigine(insertedPlants.get("pois"));
        interactionPourMais2.setCible(insertedPlants.get("maïs"));
        interactionPourMais2.setImpact(5);
        interactionPourMais2.setDescription("");
        interactionPourMais2.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourMais2);
        Allelopathie interactionPourMais3 = new Allelopathie();
        interactionPourMais3.setOrigine(insertedPlants.get("concombre"));
        interactionPourMais3.setCible(insertedPlants.get("maïs"));
        interactionPourMais3.setImpact(5);
        interactionPourMais3.setDescription("");
        interactionPourMais3.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourMais3);
        Allelopathie interactionPourMais4 = new Allelopathie();
        interactionPourMais4.setOrigine(insertedPlants.get("courge"));
        interactionPourMais4.setCible(insertedPlants.get("maïs"));
        interactionPourMais4.setImpact(5);
        interactionPourMais4.setDescription("");
        interactionPourMais4.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourMais4);
        Allelopathie interactionPourMais5 = new Allelopathie();
        interactionPourMais5.setOrigine(insertedPlants.get("potiron"));
        interactionPourMais5.setCible(insertedPlants.get("maïs"));
        interactionPourMais5.setImpact(5);
        interactionPourMais5.setDescription("");
        interactionPourMais5.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourMais5);
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
        Allelopathie interactionPourLaitue3 = new Allelopathie();
        interactionPourLaitue3.setOrigine(insertedPlants.get("radis"));
        interactionPourLaitue3.setCible(insertedPlants.get("laitue"));
        interactionPourLaitue3.setImpact(5);
        interactionPourLaitue3.setDescription("");
        interactionPourLaitue3.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourLaitue3);
    }

    private void interactionPourFraisier(Map<String, Plante> insertedPlants) {
        log.info("interactionPourFraisier"); // Pour fraisier
        Allelopathie interactionPourFraisier1 = new Allelopathie();
        interactionPourFraisier1.setOrigine(insertedPlants.get("oignon"));
        interactionPourFraisier1.setCible(insertedPlants.get("fraisier des bois"));
        interactionPourFraisier1.setImpact(5);
        interactionPourFraisier1.setDescription("");
        interactionPourFraisier1.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourFraisier1);
        Allelopathie interactionPourFraisier2 = new Allelopathie();
        interactionPourFraisier2.setOrigine(insertedPlants.get("bourrache"));
        interactionPourFraisier2.setCible(insertedPlants.get("fraisier des bois"));
        interactionPourFraisier2.setImpact(5);
        interactionPourFraisier2.setDescription("");
        interactionPourFraisier2.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourFraisier2);
        Allelopathie interactionPourFraisier3 = new Allelopathie();
        interactionPourFraisier3.setOrigine(insertedPlants.get("épinard"));
        interactionPourFraisier3.setCible(insertedPlants.get("fraisier des bois"));
        interactionPourFraisier3.setImpact(5);
        interactionPourFraisier3.setDescription("");
        interactionPourFraisier3.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourFraisier3);
        Allelopathie interactionPourFraisier4 = new Allelopathie();
        interactionPourFraisier4.setOrigine(insertedPlants.get("laitue"));
        interactionPourFraisier4.setCible(insertedPlants.get("fraisier des bois"));
        interactionPourFraisier4.setImpact(5);
        interactionPourFraisier4.setDescription("");
        interactionPourFraisier4.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourFraisier4);
    }

    private void interactionPourPotiron(Map<String, Plante> insertedPlants) {
        log.info("interactionPourPotiron"); // Pour potiron
        Allelopathie interactionPourPotiron1 = new Allelopathie();
        interactionPourPotiron1.setOrigine(insertedPlants.get("grande capucine"));
        interactionPourPotiron1.setCible(insertedPlants.get("potiron"));
        interactionPourPotiron1.setImpact(5);
        interactionPourPotiron1.setDescription("");
        interactionPourPotiron1.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourPotiron1);
        Allelopathie interactionPourPotiron2 = new Allelopathie();
        interactionPourPotiron2.setOrigine(insertedPlants.get("pomme de terre"));
        interactionPourPotiron2.setCible(insertedPlants.get("potiron"));
        interactionPourPotiron2.setImpact(5);
        interactionPourPotiron2.setDescription("");
        interactionPourPotiron2.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourPotiron2);
        Allelopathie interactionPourPotiron3 = new Allelopathie();
        interactionPourPotiron3.setOrigine(insertedPlants.get("épinard"));
        interactionPourPotiron3.setCible(insertedPlants.get("potiron"));
        interactionPourPotiron3.setImpact(5);
        interactionPourPotiron3.setDescription("");
        interactionPourPotiron3.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourPotiron3);
        Allelopathie interactionPourPotiron4 = new Allelopathie();
        interactionPourPotiron4.setOrigine(insertedPlants.get("fraisier des bois"));
        interactionPourPotiron4.setCible(insertedPlants.get("potiron"));
        interactionPourPotiron4.setImpact(5);
        interactionPourPotiron4.setDescription("");
        interactionPourPotiron4.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourPotiron4);
        Allelopathie interactionPourPotiron5 = new Allelopathie();
        interactionPourPotiron5.setOrigine(insertedPlants.get("maïs"));
        interactionPourPotiron5.setCible(insertedPlants.get("potiron"));
        interactionPourPotiron5.setImpact(5);
        interactionPourPotiron5.setDescription("");
        interactionPourPotiron5.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourPotiron5);
        Allelopathie interactionPourPotiron6 = new Allelopathie();
        interactionPourPotiron6.setOrigine(insertedPlants.get("radis"));
        interactionPourPotiron6.setCible(insertedPlants.get("potiron"));
        interactionPourPotiron6.setImpact(5);
        interactionPourPotiron6.setDescription("");
        interactionPourPotiron6.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourPotiron6);
    }

    private void interactionPourCourge(Map<String, Plante> insertedPlants) {
        log.info("interactionPourCourge"); // Pour courge
        Allelopathie interactionPourCourge1 = new Allelopathie();
        interactionPourCourge1.setOrigine(insertedPlants.get("grande capucine"));
        interactionPourCourge1.setCible(insertedPlants.get("courge"));
        interactionPourCourge1.setImpact(5);
        interactionPourCourge1.setDescription("");
        interactionPourCourge1.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourCourge1);
        Allelopathie interactionPourCourge2 = new Allelopathie();
        interactionPourCourge2.setOrigine(insertedPlants.get("pomme de terre"));
        interactionPourCourge2.setCible(insertedPlants.get("courge"));
        interactionPourCourge2.setImpact(5);
        interactionPourCourge2.setDescription("");
        interactionPourCourge2.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourCourge2);
        Allelopathie interactionPourCourge3 = new Allelopathie();
        interactionPourCourge3.setOrigine(insertedPlants.get("épinard"));
        interactionPourCourge3.setCible(insertedPlants.get("courge"));
        interactionPourCourge3.setImpact(5);
        interactionPourCourge3.setDescription("");
        interactionPourCourge3.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourCourge3);
        Allelopathie interactionPourCourge4 = new Allelopathie();
        interactionPourCourge4.setOrigine(insertedPlants.get("fraisier des bois"));
        interactionPourCourge4.setCible(insertedPlants.get("courge"));
        interactionPourCourge4.setImpact(5);
        interactionPourCourge4.setDescription("");
        interactionPourCourge4.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourCourge4);
        Allelopathie interactionPourCourge5 = new Allelopathie();
        interactionPourCourge5.setOrigine(insertedPlants.get("maïs"));
        interactionPourCourge5.setCible(insertedPlants.get("courge"));
        interactionPourCourge5.setImpact(5);
        interactionPourCourge5.setDescription("");
        interactionPourCourge5.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourCourge5);
        Allelopathie interactionPourCourge6 = new Allelopathie();
        interactionPourCourge6.setOrigine(insertedPlants.get("radis"));
        interactionPourCourge6.setCible(insertedPlants.get("courge"));
        interactionPourCourge6.setImpact(5);
        interactionPourCourge6.setDescription("");
        interactionPourCourge6.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourCourge6);
        Allelopathie interactionPourCourge7 = new Allelopathie();
        interactionPourCourge7.setOrigine(insertedPlants.get("bourrache"));
        interactionPourCourge7.setCible(insertedPlants.get("courge"));
        interactionPourCourge7.setImpact(5);
        interactionPourCourge7.setDescription("");
        interactionPourCourge7.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourCourge7);
        Allelopathie interactionPourCourge8 = new Allelopathie();
        interactionPourCourge8.setOrigine(insertedPlants.get("grande capucine"));
        interactionPourCourge8.setCible(insertedPlants.get("courge"));
        interactionPourCourge8.setImpact(5);
        interactionPourCourge8.setDescription("La capucine éloigne les punaises des courgettes et citrouilles");
        interactionPourCourge8.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourCourge8);
    }

    private void interactionPourAubergine(Map<String, Plante> insertedPlants) {
        log.info("interactionPourAubergine"); // Pour aubergine
        Allelopathie interactionPourAubergine1 = new Allelopathie();
        interactionPourAubergine1.setOrigine(insertedPlants.get("haricot"));
        interactionPourAubergine1.setCible(insertedPlants.get("aubergine"));
        interactionPourAubergine1.setImpact(5);
        interactionPourAubergine1.setDescription("");
        interactionPourAubergine1.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourAubergine1);
        Allelopathie interactionPourAubergine2 = new Allelopathie();
        interactionPourAubergine2.setOrigine(insertedPlants.get("basilic"));
        interactionPourAubergine2.setCible(insertedPlants.get("aubergine"));
        interactionPourAubergine2.setImpact(5);
        interactionPourAubergine2.setDescription("");
        interactionPourAubergine2.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourAubergine2);
    }

    private void interactionPourFramboisier(Map<String, Plante> insertedPlants) {
        log.info("interactionPourFramboisier");
        Allelopathie interactionPourFramboisier1 = new Allelopathie();
        interactionPourFramboisier1.setOrigine(insertedPlants.get("myosotis des champs"));
        interactionPourFramboisier1.setCible(insertedPlants.get("framboisier"));
        interactionPourFramboisier1.setImpact(5);
        interactionPourFramboisier1.setDescription("Le vers de framboisier est repoussé par la myosotis. Il empêche le vers de proliférer");
        interactionPourFramboisier1.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourFramboisier1);
    }

    private void interactionPourBetterave(Map<String, Plante> insertedPlants) {
        log.info("interactionPourBetterave"); // Pour betterave
        Allelopathie interactionPourBetterave1 = new Allelopathie();
        interactionPourBetterave1.setOrigine(insertedPlants.get("coriandre"));
        interactionPourBetterave1.setCible(insertedPlants.get("betterave"));
        interactionPourBetterave1.setImpact(5);
        interactionPourBetterave1.setDescription("");
        interactionPourBetterave1.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourBetterave1);
        Allelopathie interactionPourBetterave2 = new Allelopathie();
        interactionPourBetterave2.setOrigine(insertedPlants.get("chou commun"));
        interactionPourBetterave2.setCible(insertedPlants.get("betterave"));
        interactionPourBetterave2.setImpact(5);
        interactionPourBetterave2.setDescription("");
        interactionPourBetterave2.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourBetterave2);
        Allelopathie interactionPourBetterave3 = new Allelopathie();
        interactionPourBetterave3.setOrigine(insertedPlants.get("haricot"));
        interactionPourBetterave3.setCible(insertedPlants.get("betterave"));
        interactionPourBetterave3.setImpact(5);
        interactionPourBetterave3.setDescription("");
        interactionPourBetterave3.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourBetterave3);
        Allelopathie interactionPourBetterave4 = new Allelopathie();
        interactionPourBetterave4.setOrigine(insertedPlants.get("oignon"));
        interactionPourBetterave4.setCible(insertedPlants.get("betterave"));
        interactionPourBetterave4.setImpact(5);
        interactionPourBetterave4.setDescription("");
        interactionPourBetterave4.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourBetterave4);
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
        Allelopathie interactionPourBetterave7 = new Allelopathie();
        interactionPourBetterave7.setOrigine(insertedPlants.get("chou rave"));
        interactionPourBetterave7.setCible(insertedPlants.get("betterave"));
        interactionPourBetterave7.setImpact(5);
        interactionPourBetterave7.setDescription("");
        interactionPourBetterave7.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourBetterave7);
        Allelopathie interactionPourBetterave8 = new Allelopathie();
        interactionPourBetterave8.setOrigine(insertedPlants.get("concombre"));
        interactionPourBetterave8.setCible(insertedPlants.get("betterave"));
        interactionPourBetterave8.setImpact(5);
        interactionPourBetterave8.setDescription("");
        interactionPourBetterave8.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourBetterave8);
    }

    private void interactionPourAsperge(Map<String, Plante> insertedPlants) {
        log.info("interactionPourAsperge"); // Pour asperge
        Allelopathie interactionPourAsperge1 = new Allelopathie();
        interactionPourAsperge1.setOrigine(insertedPlants.get("tomate"));
        interactionPourAsperge1.setCible(insertedPlants.get("asperge"));
        interactionPourAsperge1.setImpact(5);
        interactionPourAsperge1.setDescription("");
        interactionPourAsperge1.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourAsperge1);
        Allelopathie interactionPourAsperge2 = new Allelopathie();
        interactionPourAsperge2.setOrigine(insertedPlants.get("persil"));
        interactionPourAsperge2.setCible(insertedPlants.get("asperge"));
        interactionPourAsperge2.setImpact(5);
        interactionPourAsperge2.setDescription("");
        interactionPourAsperge2.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourAsperge2);
        Allelopathie interactionPourAsperge3 = new Allelopathie();
        interactionPourAsperge3.setOrigine(insertedPlants.get("basilic"));
        interactionPourAsperge3.setCible(insertedPlants.get("asperge"));
        interactionPourAsperge3.setImpact(5);
        interactionPourAsperge3.setDescription("");
        interactionPourAsperge3.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourAsperge3);
        Allelopathie interactionPourAsperge4 = new Allelopathie();
        interactionPourAsperge4.setOrigine(insertedPlants.get("haricot"));
        interactionPourAsperge4.setCible(insertedPlants.get("asperge"));
        interactionPourAsperge4.setImpact(5);
        interactionPourAsperge4.setDescription("");
        interactionPourAsperge4.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourAsperge4);
    }

    private void interactionPourTomate(Map<String, Plante> insertedPlants) {
        log.info("interactionPourTomate"); // Pour tomate
        Allelopathie interactionPourTomate1 = new Allelopathie();
        interactionPourTomate1.setOrigine(insertedPlants.get("persil"));
        interactionPourTomate1.setCible(insertedPlants.get("tomate"));
        interactionPourTomate1.setImpact(5);
        interactionPourTomate1.setDescription("Le persil rend la tomate plus résistante aux maladies");
        interactionPourTomate1.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourTomate1);
        Allelopathie interactionPourTomate2 = new Allelopathie();
        interactionPourTomate2.setOrigine(insertedPlants.get("oignon"));
        interactionPourTomate2.setCible(insertedPlants.get("tomate"));
        interactionPourTomate2.setImpact(5);
        interactionPourTomate2.setDescription("");
        interactionPourTomate2.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourTomate2);
        Allelopathie interactionPourTomate3 = new Allelopathie();
        interactionPourTomate3.setOrigine(insertedPlants.get("poireau"));
        interactionPourTomate3.setCible(insertedPlants.get("tomate"));
        interactionPourTomate3.setImpact(5);
        interactionPourTomate3.setDescription("");
        interactionPourTomate3.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourTomate3);
        Allelopathie interactionPourTomate4 = new Allelopathie();
        interactionPourTomate4.setOrigine(insertedPlants.get("oeillet d'inde"));
        interactionPourTomate4.setCible(insertedPlants.get("tomate"));
        interactionPourTomate4.setImpact(5);
        interactionPourTomate4.setDescription("");
        interactionPourTomate4.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourTomate4);
        Allelopathie interactionPourTomate5 = new Allelopathie();
        interactionPourTomate5.setOrigine(insertedPlants.get("carotte"));
        interactionPourTomate5.setCible(insertedPlants.get("tomate"));
        interactionPourTomate5.setImpact(5);
        interactionPourTomate5.setDescription("");
        interactionPourTomate5.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourTomate5);
        Allelopathie interactionPourTomate6 = new Allelopathie();
        interactionPourTomate6.setOrigine(insertedPlants.get("basilic"));
        interactionPourTomate6.setCible(insertedPlants.get("tomate"));
        interactionPourTomate6.setImpact(5);
        interactionPourTomate6.setDescription("Le basilic rend la tomate plus résistante aux maladies");
        interactionPourTomate6.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourTomate6);
        Allelopathie interactionPourTomate7 = new Allelopathie();
        interactionPourTomate7.setOrigine(insertedPlants.get("asperge"));
        interactionPourTomate7.setCible(insertedPlants.get("tomate"));
        interactionPourTomate7.setImpact(5);
        interactionPourTomate7.setDescription("");
        interactionPourTomate7.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourTomate7);
        Allelopathie interactionPourTomate8 = new Allelopathie();
        interactionPourTomate8.setOrigine(insertedPlants.get("souci officinal"));
        interactionPourTomate8.setCible(insertedPlants.get("tomate"));
        interactionPourTomate8.setImpact(5);
        interactionPourTomate8.setDescription("");
        interactionPourTomate8.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourTomate8);
        Allelopathie interactionPourTomate9 = new Allelopathie();
        interactionPourTomate9.setOrigine(insertedPlants.get("bourrache"));
        interactionPourTomate9.setCible(insertedPlants.get("tomate"));
        interactionPourTomate9.setImpact(5);
        interactionPourTomate9.setDescription("");
        interactionPourTomate9.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourTomate9);
        Allelopathie interactionPourTomate10 = new Allelopathie();
        interactionPourTomate10.setOrigine(insertedPlants.get("grande capucine"));
        interactionPourTomate10.setCible(insertedPlants.get("tomate"));
        interactionPourTomate10.setImpact(5);
        interactionPourTomate10.setDescription("");
        interactionPourTomate10.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourTomate10);
    }

    private void interactionPourPommeDeTerre(Map<String, Plante> insertedPlants) {
        log.info("interactionPourPommeDeTerre"); // Pour pommeDeTerre
        Allelopathie interactionPourPommeDeTerre1 = new Allelopathie();
        interactionPourPommeDeTerre1.setOrigine(insertedPlants.get("chou commun"));
        interactionPourPommeDeTerre1.setCible(insertedPlants.get("pomme de terre"));
        interactionPourPommeDeTerre1.setImpact(5);
        interactionPourPommeDeTerre1.setDescription("");
        interactionPourPommeDeTerre1.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourPommeDeTerre1);
        Allelopathie interactionPourPommeDeTerre2 = new Allelopathie();
        interactionPourPommeDeTerre2.setOrigine(insertedPlants.get("fève"));
        interactionPourPommeDeTerre2.setCible(insertedPlants.get("pomme de terre"));
        interactionPourPommeDeTerre2.setImpact(5);
        interactionPourPommeDeTerre2.setDescription("");
        interactionPourPommeDeTerre2.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourPommeDeTerre2);
        Allelopathie interactionPourPommeDeTerre3 = new Allelopathie();
        interactionPourPommeDeTerre3.setOrigine(insertedPlants.get("pois"));
        interactionPourPommeDeTerre3.setCible(insertedPlants.get("pomme de terre"));
        interactionPourPommeDeTerre3.setImpact(5);
        interactionPourPommeDeTerre3.setDescription("Le doryphore de la pomme de terre est repoussé par le pois");
        interactionPourPommeDeTerre3.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourPommeDeTerre3);
        Allelopathie interactionPourPommeDeTerre4 = new Allelopathie();
        interactionPourPommeDeTerre4.setOrigine(insertedPlants.get("courge"));
        interactionPourPommeDeTerre4.setCible(insertedPlants.get("pomme de terre"));
        interactionPourPommeDeTerre4.setImpact(5);
        interactionPourPommeDeTerre4.setDescription("");
        interactionPourPommeDeTerre4.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourPommeDeTerre4);
        Allelopathie interactionPourPommeDeTerre5 = new Allelopathie();
        interactionPourPommeDeTerre5.setOrigine(insertedPlants.get("potiron"));
        interactionPourPommeDeTerre5.setCible(insertedPlants.get("pomme de terre"));
        interactionPourPommeDeTerre5.setImpact(5);
        interactionPourPommeDeTerre5.setDescription("");
        interactionPourPommeDeTerre5.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourPommeDeTerre5);
        Allelopathie interactionPourPommeDeTerre6 = new Allelopathie();
        interactionPourPommeDeTerre6.setOrigine(insertedPlants.get("haricot"));
        interactionPourPommeDeTerre6.setCible(insertedPlants.get("pomme de terre"));
        interactionPourPommeDeTerre6.setImpact(5);
        interactionPourPommeDeTerre6.setDescription("");
        interactionPourPommeDeTerre6.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourPommeDeTerre6);
        Allelopathie interactionPourPommeDeTerre7 = new Allelopathie();
        interactionPourPommeDeTerre7.setOrigine(insertedPlants.get("oeillet d'inde"));
        interactionPourPommeDeTerre7.setCible(insertedPlants.get("pomme de terre"));
        interactionPourPommeDeTerre7.setImpact(5);
        interactionPourPommeDeTerre7.setDescription("");
        interactionPourPommeDeTerre7.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourPommeDeTerre7);
        Allelopathie interactionPourPommeDeTerre8 = new Allelopathie();
        interactionPourPommeDeTerre8.setOrigine(insertedPlants.get("souci officinal"));
        interactionPourPommeDeTerre8.setCible(insertedPlants.get("pomme de terre"));
        interactionPourPommeDeTerre8.setImpact(5);
        interactionPourPommeDeTerre8.setDescription("");
        interactionPourPommeDeTerre8.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourPommeDeTerre8);
        Allelopathie interactionPourPommeDeTerre9 = new Allelopathie();
        interactionPourPommeDeTerre9.setOrigine(insertedPlants.get("concombre"));
        interactionPourPommeDeTerre9.setCible(insertedPlants.get("pomme de terre"));
        interactionPourPommeDeTerre9.setImpact(5);
        interactionPourPommeDeTerre9.setDescription("");
        interactionPourPommeDeTerre9.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourPommeDeTerre9);
        Allelopathie interactionPourPommeDeTerre10 = new Allelopathie();
        interactionPourPommeDeTerre10.setOrigine(insertedPlants.get("bourrache"));
        interactionPourPommeDeTerre10.setCible(insertedPlants.get("pomme de terre"));
        interactionPourPommeDeTerre10.setImpact(5);
        interactionPourPommeDeTerre10.setDescription("");
        interactionPourPommeDeTerre10.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourPommeDeTerre10);
    }

    private void interactionPourConcombre(Map<String, Plante> insertedPlants) {
        log.info("interactionPourConcombre"); // Pour concombre
        Allelopathie interactionPourConcombre1 = new Allelopathie();
        interactionPourConcombre1.setOrigine(insertedPlants.get("basilic"));
        interactionPourConcombre1.setCible(insertedPlants.get("concombre"));
        interactionPourConcombre1.setImpact(5);
        interactionPourConcombre1.setDescription("");
        interactionPourConcombre1.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourConcombre1);
        Allelopathie interactionPourConcombre2 = new Allelopathie();
        interactionPourConcombre2.setOrigine(insertedPlants.get("chou commun"));
        interactionPourConcombre2.setCible(insertedPlants.get("concombre"));
        interactionPourConcombre2.setImpact(5);
        interactionPourConcombre2.setDescription("");
        interactionPourConcombre2.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourConcombre2);
        Allelopathie interactionPourConcombre3 = new Allelopathie();
        interactionPourConcombre3.setOrigine(insertedPlants.get("haricot"));
        interactionPourConcombre3.setCible(insertedPlants.get("concombre"));
        interactionPourConcombre3.setImpact(5);
        interactionPourConcombre3.setDescription("");
        interactionPourConcombre3.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourConcombre3);
        Allelopathie interactionPourConcombre4 = new Allelopathie();
        interactionPourConcombre4.setOrigine(insertedPlants.get("laitue"));
        interactionPourConcombre4.setCible(insertedPlants.get("concombre"));
        interactionPourConcombre4.setImpact(5);
        interactionPourConcombre4.setDescription("");
        interactionPourConcombre4.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourConcombre4);
        Allelopathie interactionPourConcombre5 = new Allelopathie();
        interactionPourConcombre5.setOrigine(insertedPlants.get("maïs"));
        interactionPourConcombre5.setCible(insertedPlants.get("concombre"));
        interactionPourConcombre5.setImpact(5);
        interactionPourConcombre5.setDescription("");
        interactionPourConcombre5.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourConcombre5);
        Allelopathie interactionPourConcombre6 = new Allelopathie();
        interactionPourConcombre6.setOrigine(insertedPlants.get("tournesol"));
        interactionPourConcombre6.setCible(insertedPlants.get("concombre"));
        interactionPourConcombre6.setImpact(5);
        interactionPourConcombre6.setDescription("");
        interactionPourConcombre6.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourConcombre6);
        Allelopathie interactionPourConcombre7 = new Allelopathie();
        interactionPourConcombre7.setOrigine(insertedPlants.get("pomme de terre"));
        interactionPourConcombre7.setCible(insertedPlants.get("concombre"));
        interactionPourConcombre7.setImpact(5);
        interactionPourConcombre7.setDescription("");
        interactionPourConcombre7.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourConcombre7);
        Allelopathie interactionPourConcombre8 = new Allelopathie();
        interactionPourConcombre8.setOrigine(insertedPlants.get("radis"));
        interactionPourConcombre8.setCible(insertedPlants.get("concombre"));
        interactionPourConcombre8.setImpact(5);
        interactionPourConcombre8.setDescription("");
        interactionPourConcombre8.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourConcombre8);
        Allelopathie interactionPourConcombre9 = new Allelopathie();
        interactionPourConcombre9.setOrigine(insertedPlants.get("origan"));
        interactionPourConcombre9.setCible(insertedPlants.get("concombre"));
        interactionPourConcombre9.setImpact(5);
        interactionPourConcombre9.setDescription("");
        interactionPourConcombre9.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourConcombre9);
        Allelopathie interactionPourConcombre10 = new Allelopathie();
        interactionPourConcombre10.setOrigine(insertedPlants.get("pois"));
        interactionPourConcombre10.setCible(insertedPlants.get("concombre"));
        interactionPourConcombre10.setImpact(5);
        interactionPourConcombre10.setDescription("");
        interactionPourConcombre10.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourConcombre10);
        Allelopathie interactionPourConcombre11 = new Allelopathie();
        interactionPourConcombre11.setOrigine(insertedPlants.get("aneth"));
        interactionPourConcombre11.setCible(insertedPlants.get("concombre"));
        interactionPourConcombre11.setImpact(5);
        interactionPourConcombre11.setDescription("L’aneth protège les concombres");
        interactionPourConcombre11.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourConcombre11);
    }

    private void interactionPourHaricot(Map<String, Plante> insertedPlants) {
        log.info("interactionPourHaricot"); // Pour haricot
        Allelopathie interactionPourHaricot1 = new Allelopathie();
        interactionPourHaricot1.setOrigine(insertedPlants.get("concombre"));
        interactionPourHaricot1.setCible(insertedPlants.get("haricot"));
        interactionPourHaricot1.setImpact(5);
        interactionPourHaricot1.setDescription("");
        interactionPourHaricot1.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourHaricot1);
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
        Allelopathie interactionPourHaricot5 = new Allelopathie();
        interactionPourHaricot5.setOrigine(insertedPlants.get("aubergine"));
        interactionPourHaricot5.setCible(insertedPlants.get("haricot"));
        interactionPourHaricot5.setImpact(5);
        interactionPourHaricot5.setDescription("");
        interactionPourHaricot5.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourHaricot5);
        Allelopathie interactionPourHaricot6 = new Allelopathie();
        interactionPourHaricot6.setOrigine(insertedPlants.get("oeillet d'inde"));
        interactionPourHaricot6.setCible(insertedPlants.get("haricot"));
        interactionPourHaricot6.setImpact(5);
        interactionPourHaricot6.setDescription("");
        interactionPourHaricot6.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourHaricot6);
        Allelopathie interactionPourHaricot7 = new Allelopathie();
        interactionPourHaricot7.setOrigine(insertedPlants.get("souci officinal"));
        interactionPourHaricot7.setCible(insertedPlants.get("haricot"));
        interactionPourHaricot7.setImpact(5);
        interactionPourHaricot7.setDescription("");
        interactionPourHaricot7.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourHaricot7);
        Allelopathie interactionPourHaricot8 = new Allelopathie();
        interactionPourHaricot8.setOrigine(insertedPlants.get("betterave"));
        interactionPourHaricot8.setCible(insertedPlants.get("haricot"));
        interactionPourHaricot8.setImpact(5);
        interactionPourHaricot8.setDescription("");
        interactionPourHaricot8.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourHaricot8);
        Allelopathie interactionPourHaricot9 = new Allelopathie();
        interactionPourHaricot9.setOrigine(insertedPlants.get("maïs"));
        interactionPourHaricot9.setCible(insertedPlants.get("haricot"));
        interactionPourHaricot9.setImpact(5);
        interactionPourHaricot9.setDescription("");
        interactionPourHaricot9.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourHaricot9);
        Allelopathie interactionPourHaricot10 = new Allelopathie();
        interactionPourHaricot10.setOrigine(insertedPlants.get("pomme de terre"));
        interactionPourHaricot10.setCible(insertedPlants.get("haricot"));
        interactionPourHaricot10.setImpact(5);
        interactionPourHaricot10.setDescription("");
        interactionPourHaricot10.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourHaricot10);
    }

    private void interactionPourChouCommun(Map<String, Plante> insertedPlants) {
        log.info("interactionPourChouCommun"); // Pour chouCommun
        Allelopathie interactionPourChouCommun1 = new Allelopathie();
        interactionPourChouCommun1.setOrigine(insertedPlants.get("sarriette"));
        interactionPourChouCommun1.setCible(insertedPlants.get("chou commun"));
        interactionPourChouCommun1.setImpact(5);
        interactionPourChouCommun1.setDescription("");
        interactionPourChouCommun1.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourChouCommun1);
        Allelopathie interactionPourChouCommun2 = new Allelopathie();
        interactionPourChouCommun2.setOrigine(insertedPlants.get("betterave"));
        interactionPourChouCommun2.setCible(insertedPlants.get("chou commun"));
        interactionPourChouCommun2.setImpact(5);
        interactionPourChouCommun2.setDescription("");
        interactionPourChouCommun2.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourChouCommun2);
        Allelopathie interactionPourChouCommun3 = new Allelopathie();
        interactionPourChouCommun3.setOrigine(insertedPlants.get("tomate"));
        interactionPourChouCommun3.setCible(insertedPlants.get("chou commun"));
        interactionPourChouCommun3.setImpact(5);
        interactionPourChouCommun3.setDescription("La piéride du chou est repoussée par la tomate");
        interactionPourChouCommun3.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourChouCommun3);
        Allelopathie interactionPourChouCommun4 = new Allelopathie();
        interactionPourChouCommun4.setOrigine(insertedPlants.get("romarin"));
        interactionPourChouCommun4.setCible(insertedPlants.get("chou commun"));
        interactionPourChouCommun4.setImpact(5);
        interactionPourChouCommun4.setDescription("");
        interactionPourChouCommun4.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourChouCommun4);
        Allelopathie interactionPourChouCommun5 = new Allelopathie();
        interactionPourChouCommun5.setOrigine(insertedPlants.get("menthe poivrée"));
        interactionPourChouCommun5.setCible(insertedPlants.get("chou commun"));
        interactionPourChouCommun5.setImpact(5);
        interactionPourChouCommun5.setDescription("La menthe protège les choux des papillons");
        interactionPourChouCommun5.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourChouCommun5);
        Allelopathie interactionPourChouCommun6 = new Allelopathie();
        interactionPourChouCommun6.setOrigine(insertedPlants.get("sauge"));
        interactionPourChouCommun6.setCible(insertedPlants.get("chou commun"));
        interactionPourChouCommun6.setImpact(5);
        interactionPourChouCommun6.setDescription("La sauge protège les choux des papillons");
        interactionPourChouCommun6.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourChouCommun6);
        Allelopathie interactionPourChouCommun7 = new Allelopathie();
        interactionPourChouCommun7.setOrigine(insertedPlants.get("thym commun"));
        interactionPourChouCommun7.setCible(insertedPlants.get("chou commun"));
        interactionPourChouCommun7.setImpact(5);
        interactionPourChouCommun7.setDescription("");
        interactionPourChouCommun7.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourChouCommun7);
        Allelopathie interactionPourChouCommun8 = new Allelopathie();
        interactionPourChouCommun8.setOrigine(insertedPlants.get("grande capucine"));
        interactionPourChouCommun8.setCible(insertedPlants.get("chou commun"));
        interactionPourChouCommun8.setImpact(5);
        interactionPourChouCommun8.setDescription("");
        interactionPourChouCommun8.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourChouCommun8);
        Allelopathie interactionPourChouCommun9 = new Allelopathie();
        interactionPourChouCommun9.setOrigine(insertedPlants.get("pomme de terre"));
        interactionPourChouCommun9.setCible(insertedPlants.get("chou commun"));
        interactionPourChouCommun9.setImpact(5);
        interactionPourChouCommun9.setDescription("");
        interactionPourChouCommun9.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourChouCommun9);
        Allelopathie interactionPourChouCommun10 = new Allelopathie();
        interactionPourChouCommun10.setOrigine(insertedPlants.get("hysope"));
        interactionPourChouCommun10.setCible(insertedPlants.get("chou commun"));
        interactionPourChouCommun10.setImpact(5);
        interactionPourChouCommun10.setDescription("L'hysope empêche les mouches blanche de pondre dans les choux");
        interactionPourChouCommun10.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourChouCommun10);
        Allelopathie interactionPourChouCommun11 = new Allelopathie();
        interactionPourChouCommun11.setOrigine(insertedPlants.get("céleri"));
        interactionPourChouCommun11.setCible(insertedPlants.get("chou commun"));
        interactionPourChouCommun11.setImpact(5);
        interactionPourChouCommun11.setDescription("La piéride du chou est repoussée par le céleri");
        interactionPourChouCommun11.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourChouCommun11);
        Allelopathie interactionPourChouCommun12 = new Allelopathie();
        interactionPourChouCommun12.setOrigine(insertedPlants.get("bourrache"));
        interactionPourChouCommun12.setCible(insertedPlants.get("chou commun"));
        interactionPourChouCommun12.setImpact(5);
        interactionPourChouCommun12.setDescription("");
        interactionPourChouCommun12.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourChouCommun12);
        Allelopathie interactionPourChouCommun13 = new Allelopathie();
        interactionPourChouCommun13.setOrigine(insertedPlants.get("oeillet d'inde"));
        interactionPourChouCommun13.setCible(insertedPlants.get("chou commun"));
        interactionPourChouCommun13.setImpact(5);
        interactionPourChouCommun13.setDescription("");
        interactionPourChouCommun13.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourChouCommun13);
        Allelopathie interactionPourChouCommun14 = new Allelopathie();
        interactionPourChouCommun14.setOrigine(insertedPlants.get("tomate"));
        interactionPourChouCommun14.setCible(insertedPlants.get("chou commun"));
        interactionPourChouCommun14.setImpact(5);
        interactionPourChouCommun14.setDescription(
            "La piéride du chou n'aime pas l'odeur de la tomate. " +
                "L'effet protecteur est renforcé lorsqu'on met entre les plantes menacées, les gourmands des tomates."
        );
        interactionPourChouCommun14.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourChouCommun14);
        Allelopathie interactionPourChouCommun15 = new Allelopathie();
        interactionPourChouCommun15.setOrigine(insertedPlants.get("céleri"));
        interactionPourChouCommun15.setCible(insertedPlants.get("chou commun"));
        interactionPourChouCommun15.setImpact(5);
        interactionPourChouCommun15.setDescription("La piéride du chou n'aime pas l'odeur du céleri");
        interactionPourChouCommun15.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourChouCommun15);
    }

    private void interactionPourCarotte(Map<String, Plante> insertedPlants) {
        log.info("interactionPourCarotte"); // Pour carotte
        Allelopathie interactionPourCarotte1 = new Allelopathie();
        interactionPourCarotte1.setOrigine(insertedPlants.get("ail"));
        interactionPourCarotte1.setCible(insertedPlants.get("carotte"));
        interactionPourCarotte1.setImpact(5);
        interactionPourCarotte1.setDescription("");
        interactionPourCarotte1.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourCarotte1);
        Allelopathie interactionPourCarotte2 = new Allelopathie();
        interactionPourCarotte2.setOrigine(insertedPlants.get("aneth"));
        interactionPourCarotte2.setCible(insertedPlants.get("carotte"));
        interactionPourCarotte2.setImpact(5);
        interactionPourCarotte2.setDescription("L’aneth protège les carottes et aide à la levée");
        interactionPourCarotte2.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourCarotte2);
        Allelopathie interactionPourCarotte3 = new Allelopathie();
        interactionPourCarotte3.setOrigine(insertedPlants.get("échalote"));
        interactionPourCarotte3.setCible(insertedPlants.get("carotte"));
        interactionPourCarotte3.setImpact(5);
        interactionPourCarotte3.setDescription("");
        interactionPourCarotte3.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourCarotte3);
        Allelopathie interactionPourCarotte4 = new Allelopathie();
        interactionPourCarotte4.setOrigine(insertedPlants.get("poireau"));
        interactionPourCarotte4.setCible(insertedPlants.get("carotte"));
        interactionPourCarotte4.setImpact(5);
        interactionPourCarotte4.setDescription("");
        interactionPourCarotte4.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourCarotte4);
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
        Allelopathie interactionPourCarotte7 = new Allelopathie();
        interactionPourCarotte7.setOrigine(insertedPlants.get("ciboulette"));
        interactionPourCarotte7.setCible(insertedPlants.get("carotte"));
        interactionPourCarotte7.setImpact(5);
        interactionPourCarotte7.setDescription("");
        interactionPourCarotte7.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourCarotte7);
        Allelopathie interactionPourCarotte8 = new Allelopathie();
        interactionPourCarotte8.setOrigine(insertedPlants.get("radis"));
        interactionPourCarotte8.setCible(insertedPlants.get("carotte"));
        interactionPourCarotte8.setImpact(5);
        interactionPourCarotte8.setDescription("");
        interactionPourCarotte8.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourCarotte8);
        Allelopathie interactionPourCarotte9 = new Allelopathie();
        interactionPourCarotte9.setOrigine(insertedPlants.get("oignon"));
        interactionPourCarotte9.setCible(insertedPlants.get("carotte"));
        interactionPourCarotte9.setImpact(5);
        interactionPourCarotte9.setDescription("L'oignon repousse la mouche de la carotte");
        interactionPourCarotte9.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourCarotte9);
        Allelopathie interactionPourCarotte10 = new Allelopathie();
        interactionPourCarotte10.setOrigine(insertedPlants.get("pois"));
        interactionPourCarotte10.setCible(insertedPlants.get("carotte"));
        interactionPourCarotte10.setImpact(5);
        interactionPourCarotte10.setDescription("");
        interactionPourCarotte10.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourCarotte10);
        Allelopathie interactionPourCarotte11 = new Allelopathie();
        interactionPourCarotte11.setOrigine(insertedPlants.get("poireau"));
        interactionPourCarotte11.setCible(insertedPlants.get("carotte"));
        interactionPourCarotte11.setImpact(5);
        interactionPourCarotte11.setDescription("Le poireau repousse la mouche de la carotte");
        interactionPourCarotte11.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourCarotte11);
        Allelopathie interactionPourCarotte12 = new Allelopathie();
        interactionPourCarotte12.setOrigine(insertedPlants.get("carotte"));
        interactionPourCarotte12.setCible(insertedPlants.get("carotte"));
        interactionPourCarotte12.setImpact(5);
        interactionPourCarotte12.setDescription("");
        interactionPourCarotte12.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourCarotte12);
        Allelopathie interactionPourCarotte13 = new Allelopathie();
        interactionPourCarotte13.setOrigine(insertedPlants.get("carotte"));
        interactionPourCarotte13.setCible(insertedPlants.get("carotte"));
        interactionPourCarotte13.setImpact(5);
        interactionPourCarotte13.setDescription("");
        interactionPourCarotte13.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourCarotte13);
        Allelopathie interactionPourCarotte14 = new Allelopathie();
        interactionPourCarotte14.setOrigine(insertedPlants.get("romarin"));
        interactionPourCarotte14.setCible(insertedPlants.get("carotte"));
        interactionPourCarotte14.setImpact(5);
        interactionPourCarotte14.setDescription("");
        interactionPourCarotte14.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourCarotte14);
        Allelopathie interactionPourCarotte15 = new Allelopathie();
        interactionPourCarotte15.setOrigine(insertedPlants.get("scorsonère des prés"));
        interactionPourCarotte15.setCible(insertedPlants.get("carotte"));
        interactionPourCarotte15.setImpact(5);
        interactionPourCarotte15.setDescription("");
        interactionPourCarotte15.setType("Inconnu");
        getOrInsertAllelopathie(interactionPourCarotte15);
    }

}
