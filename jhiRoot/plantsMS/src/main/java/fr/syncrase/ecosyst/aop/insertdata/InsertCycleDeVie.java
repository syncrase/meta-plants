package fr.syncrase.ecosyst.aop.insertdata;

import fr.syncrase.ecosyst.domain.CycleDeVie;
import fr.syncrase.ecosyst.domain.PeriodeAnnee;
import fr.syncrase.ecosyst.domain.Plante;
import fr.syncrase.ecosyst.domain.Semis;
import fr.syncrase.ecosyst.repository.CycleDeVieRepository;
import fr.syncrase.ecosyst.repository.PeriodeAnneeRepository;
import fr.syncrase.ecosyst.repository.PlanteRepository;
import fr.syncrase.ecosyst.repository.SemisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class InsertCycleDeVie {

    private final Logger log = LoggerFactory.getLogger(InsertCycleDeVie.class);
    private final Map<String, Plante> insertedPlants;
    private final CycleDeVieRepository cycleDeVieRepository;
    private final PeriodeAnneeRepository periodeAnneeRepository;
    private final SemisRepository semisRepository;
    private final PlanteRepository planteRepository;
    private final InsertMois insertMois;

    public InsertCycleDeVie(Map<String, Plante> insertedPlants, CycleDeVieRepository cycleDeVieRepository, PeriodeAnneeRepository periodeAnneeRepository, SemisRepository semisRepository, PlanteRepository planteRepository, InsertMois insertMois) {
        this.cycleDeVieRepository = cycleDeVieRepository;
        this.periodeAnneeRepository = periodeAnneeRepository;
        this.semisRepository = semisRepository;
        this.planteRepository = planteRepository;
        this.insertedPlants = insertedPlants;
        this.insertMois = insertMois;
    }

    public void insertAllCycleDeVie() {
        insertCycleDeVie("chou cabus", MoisEnum.MARS, MoisEnum.JUIN, MoisEnum.JANVIER, MoisEnum.MARS);
        insertCycleDeVie("poireau", MoisEnum.MARS, MoisEnum.AVRIL, null, null);
        insertCycleDeVie("radis 18 jours", MoisEnum.SEPTEMBRE, MoisEnum.MARS, MoisEnum.JANVIER, MoisEnum.MARS);
        insertCycleDeVie("pourpier", MoisEnum.MAI, MoisEnum.AOUT, MoisEnum.JANVIER, MoisEnum.AVRIL);
        insertCycleDeVie("chou cabus coeur de boeuf des vertus", MoisEnum.OCTOBRE, MoisEnum.MARS, MoisEnum.JANVIER, MoisEnum.MARS);
        insertCycleDeVie("chou fleur merveille de toutes saisons", MoisEnum.AVRIL, MoisEnum.JUIN, MoisEnum.JANVIER, MoisEnum.FEVRIER);
        insertCycleDeVie("laitue rouge grenobloise", MoisEnum.AVRIL, MoisEnum.SEPTEMBRE, MoisEnum.FEVRIER, MoisEnum.MARS);
        insertCycleDeVie("laitue reine de mai de pleine terre", MoisEnum.MARS, MoisEnum.AVRIL, MoisEnum.DECEMBRE, MoisEnum.FEVRIER);
        insertCycleDeVie("laitue batavia lollo rossa", MoisEnum.MARS, MoisEnum.SEPTEMBRE, MoisEnum.JANVIER, MoisEnum.FEVRIER);
        insertCycleDeVie("radis kocto hf1", MoisEnum.AVRIL, MoisEnum.SEPTEMBRE, MoisEnum.FEVRIER, MoisEnum.AVRIL);
        insertCycleDeVie("piment fort de cayenne", MoisEnum.AVRIL, MoisEnum.JUIN, MoisEnum.FEVRIER, MoisEnum.AVRIL);
        insertCycleDeVie("aubergine violetta di firenze", MoisEnum.AVRIL, MoisEnum.JUIN, MoisEnum.FEVRIER, MoisEnum.AVRIL);
        insertCycleDeVie("poireau monstrueux de carentan", MoisEnum.FEVRIER, MoisEnum.MAI, MoisEnum.JANVIER, MoisEnum.MARS);
        insertCycleDeVie("persil frisé vert foncé ou double", MoisEnum.FEVRIER, MoisEnum.SEPTEMBRE, null, null);
        insertCycleDeVie("ciboulette commune", MoisEnum.FEVRIER, MoisEnum.JUIN, null, null);
        insertCycleDeVie("artichaut gros vert de laon", MoisEnum.MARS, MoisEnum.MAI, MoisEnum.FEVRIER, MoisEnum.MARS);
        insertCycleDeVie("épinard d'été matador", MoisEnum.FEVRIER, MoisEnum.AVRIL, null, null);
        insertCycleDeVie("navet rouge plat hâtif à feuille entière", MoisEnum.MARS, MoisEnum.MAI, MoisEnum.FEVRIER, MoisEnum.MARS);
        insertCycleDeVie("laitue pommée grosse blonde paresseuse", MoisEnum.MAI, MoisEnum.OCTOBRE, MoisEnum.FEVRIER, MoisEnum.AVRIL);
        insertCycleDeVie("laitue batavia iceberg reine des glaces", MoisEnum.AVRIL, MoisEnum.JUIN, MoisEnum.FEVRIER, MoisEnum.MARS);
        insertCycleDeVie("laitue pommée reine de mai", MoisEnum.MARS, MoisEnum.MAI, MoisEnum.FEVRIER, MoisEnum.MARS);
        insertCycleDeVie("tomate noire russe", MoisEnum.MARS, MoisEnum.AVRIL, MoisEnum.FEVRIER, MoisEnum.MARS);
        insertCycleDeVie("tomate beefsteak", MoisEnum.MARS, MoisEnum.MAI, MoisEnum.FEVRIER, MoisEnum.AVRIL);
        insertCycleDeVie("pois nain norli grain rond", MoisEnum.FEVRIER, MoisEnum.JUIN, MoisEnum.FEVRIER, MoisEnum.JUIN);
        insertCycleDeVie("fève aguadulce à très longue cosse", MoisEnum.FEVRIER, MoisEnum.DECEMBRE, MoisEnum.FEVRIER, MoisEnum.DECEMBRE);
        insertCycleDeVie("bourrache", MoisEnum.MARS, MoisEnum.JUIN, MoisEnum.MARS, MoisEnum.JUIN);
        insertCycleDeVie("thym ordinaire", MoisEnum.MARS, MoisEnum.JUILLET, MoisEnum.MARS, MoisEnum.JUILLET);
        insertCycleDeVie("ciboule de chine", MoisEnum.MARS, MoisEnum.NOVEMBRE, MoisEnum.MARS, MoisEnum.NOVEMBRE);
        insertCycleDeVie("carotte jaune du doubs", MoisEnum.MARS, MoisEnum.NOVEMBRE, MoisEnum.MARS, MoisEnum.NOVEMBRE);
        insertCycleDeVie("sauge officinale", MoisEnum.MARS, MoisEnum.SEPTEMBRE, MoisEnum.MARS, MoisEnum.SEPTEMBRE);
        insertCycleDeVie("concombre vert long maraîcher", MoisEnum.MAI, MoisEnum.JUIN, MoisEnum.MARS, MoisEnum.MAI);
        insertCycleDeVie("melon ancien vieille france", MoisEnum.AVRIL, MoisEnum.JUIN, MoisEnum.MARS, MoisEnum.MAI);
        insertCycleDeVie("laitue cressonnette marocaine", MoisEnum.MARS, MoisEnum.AOUT, MoisEnum.MARS, MoisEnum.AOUT);
        insertCycleDeVie("roquette cultivée", MoisEnum.MARS, MoisEnum.SEPTEMBRE, MoisEnum.MARS, MoisEnum.SEPTEMBRE);
        insertCycleDeVie("roquette sauvage", MoisEnum.MARS, MoisEnum.SEPTEMBRE, MoisEnum.MARS, MoisEnum.SEPTEMBRE);
        insertCycleDeVie("maïs doux bantam", MoisEnum.AVRIL, MoisEnum.JUIN, null, null);
        insertCycleDeVie("menthe poivrée", MoisEnum.MARS, MoisEnum.NOVEMBRE, MoisEnum.MARS, MoisEnum.NOVEMBRE);
        insertCycleDeVie("sarriette commune", MoisEnum.AVRIL, MoisEnum.JUIN, MoisEnum.AVRIL, MoisEnum.JUIN);
        insertCycleDeVie("origan marjolaine", MoisEnum.AVRIL, MoisEnum.JUIN, MoisEnum.AVRIL, MoisEnum.JUIN);
        insertCycleDeVie("coriandre", MoisEnum.MARS, MoisEnum.SEPTEMBRE, MoisEnum.MARS, MoisEnum.SEPTEMBRE);
        insertCycleDeVie("betterave de détroit améliorée 3", MoisEnum.AVRIL, MoisEnum.JUILLET, MoisEnum.MARS, MoisEnum.JUILLET);
        insertCycleDeVie("potiron petit bonnet turc", MoisEnum.AVRIL, MoisEnum.JUIN, null, null);
        insertCycleDeVie("courge pâtisson blanc", MoisEnum.AVRIL, MoisEnum.JUIN, null, null);
        insertCycleDeVie("chou de chine", MoisEnum.MAI, MoisEnum.SEPTEMBRE, null, null);
        insertCycleDeVie("mâche à grosse graine", MoisEnum.JUILLET, MoisEnum.SEPTEMBRE, null, null);
        insertCycleDeVie("laitue à couper", MoisEnum.MARS, MoisEnum.AOUT, MoisEnum.SEPTEMBRE, MoisEnum.FEVRIER);
        insertCycleDeVie("épinard géant d'hiver", MoisEnum.AOUT, MoisEnum.OCTOBRE, null, null);
    }

    private void insertCycleDeVie(String nomVernaculaire, MoisEnum pleineTerreDebut, MoisEnum pleineTerreFin, MoisEnum sousAbrisDebut, MoisEnum sousAbrisFin) {

        PeriodeAnnee periodePleineTerre = null;
        if (pleineTerreDebut != null && pleineTerreFin != null) {
            periodePleineTerre = getOrInsertPeriodeAnnee(
                new PeriodeAnnee()
                    .debut(insertMois.getOrInsertMois(pleineTerreDebut.getMois()))
                    .fin(insertMois.getOrInsertMois(pleineTerreFin.getMois()))
            );
        }

        PeriodeAnnee periodeSousAbris = null;
        if (sousAbrisDebut != null && sousAbrisFin != null) {
            periodeSousAbris = getOrInsertPeriodeAnnee(
                new PeriodeAnnee()
                    .debut(insertMois.getOrInsertMois(sousAbrisDebut.getMois()))
                    .fin(insertMois.getOrInsertMois(sousAbrisFin.getMois()))
            );
        }

        Semis semis = new Semis()
            .semisPleineTerre(periodePleineTerre)
            .semisSousAbris(periodeSousAbris);

        CycleDeVie cycle = getOrInsertCycleDeVie(new CycleDeVie()
            .semis(
                getOrInsertSemis(semis)
            ));
        try {
            planteRepository.save(insertedPlants.get(nomVernaculaire).cycleDeVie(cycle));
        } catch (Exception e) {
            log.error("Échec de l'insertion de la plante : " + nomVernaculaire);
            e.printStackTrace();
        }
    }

    private PeriodeAnnee getOrInsertPeriodeAnnee(PeriodeAnnee periodeAnnee) {
        if (!periodeAnneeRepository.exists(Example.of(periodeAnnee))) {
            periodeAnneeRepository.save(periodeAnnee);
        } else {
            Optional<PeriodeAnnee> returned = periodeAnneeRepository.findOne(Example.of(periodeAnnee));
            if (returned.isPresent()) {
                periodeAnnee = returned.get();
                log.info("Existing periodeAnnee : " + periodeAnnee);
            } else {
                log.error("Unable to get instance of : " + periodeAnnee);
            }
        }
        return periodeAnnee;
    }

    private Semis getOrInsertSemis(Semis semis) {
        if (!semisRepository.exists(Example.of(semis))) {
            semisRepository.save(semis);
        } else {
            if (semis.getSemisSousAbris() == null || semis.getSemisPleineTerre() == null) {
                List<Semis> returned = semisRepository.findAll(Example.of(semis));
                if (returned.size() == 1) {
                    semis = returned.get(0);
                    log.info("Existing semis : " + semis);
                } else {
                    // Many results, let's get the one which validate the null fields
                    Semis finalSemis = semis;
                    List<Semis> semisList = returned.stream()
                        .filter(s ->
                            (finalSemis.getSemisSousAbris() == null && s.getSemisSousAbris() == null)
                                ||
                                (finalSemis.getSemisPleineTerre() == null && s.getSemisPleineTerre() == null)
                        )
                        .collect(Collectors.toList());
                    if (semisList.size() == 1) {
                        semis = semisList.get(0);
                    } else {
                        semisRepository.save(semis);
                    }
                }
            } else {
                Optional<Semis> returned = semisRepository.findOne(Example.of(semis));
                if (returned.isPresent()) {
                    semis = returned.get();
                    log.info("Existing semis : " + semis);
                } else {
                    log.error("Unable to get instance of : " + semis);
                }
            }
        }
        return semis;
    }

    private CycleDeVie getOrInsertCycleDeVie(CycleDeVie cycleDeVie) {
        if (!cycleDeVieRepository.exists(Example.of(cycleDeVie))) {
            cycleDeVieRepository.save(cycleDeVie);
        } else {
            Optional<CycleDeVie> returned = cycleDeVieRepository.findOne(Example.of(cycleDeVie));
            if (returned.isPresent()) {
                cycleDeVie = returned.get();
                log.info("Existing cycleDeVie : " + cycleDeVie);
            } else {
                log.error("Unable to get instance of : " + cycleDeVie);
            }
        }
        return cycleDeVie;
    }


}
