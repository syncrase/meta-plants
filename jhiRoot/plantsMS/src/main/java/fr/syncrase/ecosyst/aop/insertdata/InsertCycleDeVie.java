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
        insertCycleDeVie("laitue pommée grosse blonde paresseuse", MoisEnum.MAI, MoisEnum.OCTOBRE, null, null);
        insertCycleDeVie("laitue batavia iceberg reine des glaces", null, null, MoisEnum.FEVRIER, MoisEnum.MARS);
        insertCycleDeVie("laitue pommée reine de mai", MoisEnum.MARS, MoisEnum.MAI, MoisEnum.FEVRIER, MoisEnum.MARS);
        insertCycleDeVie("laitue cressonnette marocaine", MoisEnum.MARS, MoisEnum.AOUT, MoisEnum.MARS, MoisEnum.AOUT);
        insertCycleDeVie("laitue rouge grenobloise", MoisEnum.AVRIL, MoisEnum.SEPTEMBRE, MoisEnum.FEVRIER, MoisEnum.MARS);
        insertCycleDeVie("laitue reine de mai de pleine terre", MoisEnum.MARS, MoisEnum.AVRIL, MoisEnum.DECEMBRE, MoisEnum.FEVRIER);
        insertCycleDeVie("laitue batavia lollo rossa", MoisEnum.MARS, MoisEnum.SEPTEMBRE, MoisEnum.JANVIER, MoisEnum.FEVRIER);
        insertCycleDeVie("laitue à couper", MoisEnum.MARS, MoisEnum.AOUT, MoisEnum.SEPTEMBRE, MoisEnum.FEVRIER);
        insertCycleDeVie("tomate noire russe", MoisEnum.MARS, MoisEnum.AVRIL, MoisEnum.FEVRIER, MoisEnum.MARS);
        insertCycleDeVie("tomate beefsteak", MoisEnum.MARS, MoisEnum.MAI, MoisEnum.FEVRIER, MoisEnum.AVRIL);
        insertCycleDeVie("carotte jaune du doubs", MoisEnum.MARS, MoisEnum.NOVEMBRE, MoisEnum.MARS, MoisEnum.NOVEMBRE);
        insertCycleDeVie("betterave de détroit améliorée 3", MoisEnum.AVRIL, MoisEnum.JUILLET, MoisEnum.MARS, MoisEnum.JUILLET);
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
