package fr.syncrase.ecosyst.aop.insertdata;

import fr.syncrase.ecosyst.domain.CycleDeVie;
import fr.syncrase.ecosyst.domain.PeriodeAnnee;
import fr.syncrase.ecosyst.domain.Plante;
import fr.syncrase.ecosyst.domain.Semis;
import fr.syncrase.ecosyst.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;

import java.util.Map;
import java.util.Optional;

public class InsertCycleDeVie {

    private final Logger log = LoggerFactory.getLogger(InsertCycleDeVie.class);
    private final Map<String, Plante> insertedPlants;
    private CycleDeVieRepository cycleDeVieRepository;
    private PeriodeAnneeRepository periodeAnneeRepository;
    private SemisRepository semisRepository;
    private PlanteRepository planteRepository;
    private MoisRepository moisRepository;
    private InsertMois insertMois;

    public InsertCycleDeVie(Map<String, Plante> insertedPlants, CycleDeVieRepository cycleDeVieRepository, PeriodeAnneeRepository periodeAnneeRepository, SemisRepository semisRepository, PlanteRepository planteRepository, MoisRepository moisRepository, InsertMois insertMois) {
        this.cycleDeVieRepository = cycleDeVieRepository;
        this.periodeAnneeRepository = periodeAnneeRepository;
        this.semisRepository = semisRepository;
        this.planteRepository = planteRepository;
        this.moisRepository = moisRepository;
        this.insertedPlants = insertedPlants;
        this.insertMois = insertMois;
    }

    public void insertAllCycleDeVie() {
        insertCycleDeVieChouCabus();
    }

    private void insertCycleDeVieChouCabus() {
        CycleDeVie cycle = getOrInsertCycleDeVie(new CycleDeVie()
            .semis(getOrInsertSemis(new Semis()
                .semisPleineTerre(getOrInsertPeriodeAnnee(new PeriodeAnnee()
                    .debut(insertMois.getOrInsertMois(MoisEnum.MARS.getMois()))
                    .fin(insertMois.getOrInsertMois(MoisEnum.JUIN.getMois()))))
                .semisSousAbris(getOrInsertPeriodeAnnee(new PeriodeAnnee()
                    .debut(insertMois.getOrInsertMois(MoisEnum.JANVIER.getMois()))
                    .fin(insertMois.getOrInsertMois(MoisEnum.MARS.getMois())))))
            ));
        planteRepository.save(insertedPlants.get("chou cabus").cycleDeVie(cycle));
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
            Optional<Semis> returned = semisRepository.findOne(Example.of(semis));
            if (returned.isPresent()) {
                semis = returned.get();
                log.info("Existing semis : " + semis);
            } else {
                log.error("Unable to get instance of : " + semis);
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
