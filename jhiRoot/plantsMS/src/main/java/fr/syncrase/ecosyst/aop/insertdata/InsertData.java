package fr.syncrase.ecosyst.aop.insertdata;

import fr.syncrase.ecosyst.domain.Plante;
import fr.syncrase.ecosyst.repository.*;
import fr.syncrase.ecosyst.service.AllelopathieQueryService;
import fr.syncrase.ecosyst.service.PlanteQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Component
public class InsertData implements ApplicationListener<ContextRefreshedEvent> {
    //implements ApplicationListener<ContextRefreshedEvent>
    private final Logger log = LoggerFactory.getLogger(InsertData.class);

    private RacineRepository racineRepository;
    private MoisRepository moisRepository;
    private StrateRepository strateRepository;
    private SolRepository solRepository;
    private FeuillageRepository feuillageRepository;
    private PlanteRepository planteRepository;
    private PlanteQueryService planteQueryService;
    private AllelopathieRepository allelopathieRepository;
    private AllelopathieQueryService allelopathieQueryService;
//    private ClassificationRepository classificationRepository;
//    private CronquistPlanteRepository cronquistRepository;
    private NomVernaculaireRepository nomVernaculaireRepository;
    private CycleDeVieRepository cycleDeVieRepository;
    private PeriodeAnneeRepository periodeAnneeRepository;
    private SemisRepository semisRepository;
    private ClassificationCronquistRepository classificationCronquistRepository;

    @Autowired
    public void setAllelopathieQueryService(AllelopathieQueryService allelopathieQueryService) {
        this.allelopathieQueryService = allelopathieQueryService;
    }

    @Autowired
    public void setPlanteQueryService(PlanteQueryService planteQueryService) {
        this.planteQueryService = planteQueryService;
    }

    @Autowired
    public void setClassificationCronquistRepository(ClassificationCronquistRepository classificationCronquistRepository) {
        this.classificationCronquistRepository = classificationCronquistRepository;
    }

    @Autowired
    public void setSemisRepository(SemisRepository semisRepository) {
        this.semisRepository = semisRepository;
    }

    @Autowired
    public void setPeriodeAnneeRepository(PeriodeAnneeRepository periodeAnneeRepository) {
        this.periodeAnneeRepository = periodeAnneeRepository;
    }

    @Autowired
    public void setCycleDeVieRepository(CycleDeVieRepository cycleDeVieRepository) {
        this.cycleDeVieRepository = cycleDeVieRepository;
    }

    @Autowired
    public void setNomVernaculaireRepository(NomVernaculaireRepository nomVernaculaireRepository) {
        this.nomVernaculaireRepository = nomVernaculaireRepository;
    }

//    @Autowired
//    public void setCronquistRepository(CronquistPlanteRepository cronquistRepository) {
//        this.cronquistRepository = cronquistRepository;
//    }

    @Autowired
    public void setRacineRepository(RacineRepository racineRepository) {
        this.racineRepository = racineRepository;
    }

    @Autowired
    public void setMoisRepository(MoisRepository moisRepository) {
        this.moisRepository = moisRepository;
    }

    @Autowired
    public void setStrateRepository(StrateRepository strateRepository) {
        this.strateRepository = strateRepository;
    }

    @Autowired
    public void setSolRepository(SolRepository solRepository) {
        this.solRepository = solRepository;
    }

    @Autowired
    public void setFeuillageRepository(FeuillageRepository feuillageRepository) {
        this.feuillageRepository = feuillageRepository;
    }

    @Autowired
    public void setPlanteRepository(PlanteRepository planteRepository) {
        this.planteRepository = planteRepository;
    }

    @Autowired
    public void setAllelopathieRepository(AllelopathieRepository allelopathieRepository) {
        this.allelopathieRepository = allelopathieRepository;
    }

//    @Autowired
//    public void setClassificationRepository(ClassificationRepository classificationRepository) {
//        this.classificationRepository = classificationRepository;
//    }

    @Transactional
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        InsertMois moisSetter = new InsertMois(moisRepository);
        moisSetter.insertAllMois();
        InsertRacines racineSetter = new InsertRacines(racineRepository);
        racineSetter.insertAllRacines();
        InsertStrates strateSetter = new InsertStrates(strateRepository);
        strateSetter.insertAllStrates();
        InsertFeuillages feuillageSetter = new InsertFeuillages(feuillageRepository);
        feuillageSetter.insertAllFeuillages();
        InsertSols solSetter = new InsertSols(solRepository);
        solSetter.insertAllSols();

        InsertPlants plantsSetter = new InsertPlants(classificationCronquistRepository, nomVernaculaireRepository, planteRepository, planteQueryService);
        Map<String, Plante> insertedPlants = plantsSetter.insertAllPlants();

        InsertInteractions interactionSetter = new InsertInteractions(insertedPlants, allelopathieQueryService, allelopathieRepository);
        interactionSetter.insertAllInteractions();

        InsertCycleDeVie cycleDeVieSetter = new InsertCycleDeVie(insertedPlants, cycleDeVieRepository, periodeAnneeRepository, semisRepository, planteRepository, moisSetter);
        cycleDeVieSetter.insertAllCycleDeVie();

    }


}



