package fr.syncrase.ecosyst.aop.insertdata;

import fr.syncrase.ecosyst.domain.ClassificationCronquist;
import fr.syncrase.ecosyst.domain.NomVernaculaire;
import fr.syncrase.ecosyst.domain.Plante;
import fr.syncrase.ecosyst.repository.ClassificationCronquistRepository;
import fr.syncrase.ecosyst.repository.NomVernaculaireRepository;
import fr.syncrase.ecosyst.repository.PlanteRepository;
import fr.syncrase.ecosyst.security.SecurityUtils;
import fr.syncrase.ecosyst.service.PlanteQueryService;
import fr.syncrase.ecosyst.service.criteria.PlanteCriteria;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import tech.jhipster.service.filter.LongFilter;

import java.util.*;
import java.util.stream.Collectors;

public class InsertPlants {

    private final Logger log = LoggerFactory.getLogger(InsertPlants.class);

    //    private final ClassificationRepository classificationRepository;
    private final ClassificationCronquistRepository classificationCronquistRepository;
    private final NomVernaculaireRepository nomVernaculaireRepository;
    private final PlanteRepository planteRepository;
    private final PlanteQueryService planteQueryService;

    public InsertPlants(ClassificationCronquistRepository classificationCronquistRepository, NomVernaculaireRepository nomVernaculaireRepository, PlanteRepository planteRepository, PlanteQueryService planteQueryService) {
//        this.classificationRepository = classificationRepository;
        this.classificationCronquistRepository = classificationCronquistRepository;
        this.nomVernaculaireRepository = nomVernaculaireRepository;
        this.planteRepository = planteRepository;
        this.planteQueryService = planteQueryService;
    }

    /**
     * insert all plants in database and
     */
    protected Map<String, Plante> insertAllPlants() {
        Map<String, Plante> insertedPlants = new HashMap<>();

//        {
//            "plantesId.equals": plant.id
//        }
        String url = "http://localhost:8082/api/cronquist-ranks";
        Map<String, String> variables = new HashMap<String, String>(3);
        variables.put("cronquistRankId", "48601");
//        variables.put("id", photo.getAttribute("id"));
//        variables.put("secret", photo.getAttribute("secret"));
//        Object forObject = restTemplate.getForObject(url, Object.class, variables);
        // TODO aucun utilisateur n'est connecté au chargement de l'application => pas de JWT
        Optional<String> tokenOpt = SecurityUtils.getCurrentUserJWT();
        if (tokenOpt.isPresent()) {
            String token = tokenOpt.get();
//            Object forObject = new RestTemplate().getForObject("http://localhost:9000/services/classificationms/api/cronquist-ranks", Object.class, variables);
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.setBearerAuth(token);
            HttpEntity<String> entity = new HttpEntity<>("body", headers);

            ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String body = exchange.getBody();
        }

        // TODO call classificationMS
        ClassificationCronquist daucusCarotaCron = getOrInsertCronquist(new ClassificationCronquist().ordre("apiales").famille("apiaceae").genre("daucus").espece("daucus carota"));
        Set<NomVernaculaire> daucusCarotaNoms = getSynchronizedNomsVernaculaires("carotte");
        Plante carotte = getSynchronizedPlante(daucusCarotaNoms, daucusCarotaCron);
        insertedPlants.put("carotte", carotte);
        insertedPlants.put("carotte jaune du doubs", insertPlantePotagere(carotte, "carotte jaune du doubs"));

        ClassificationCronquist betaVulgarisCron = getOrInsertCronquist(new ClassificationCronquist().ordre("caryophyllales").famille("chenopodiaceae").genre("beta").espece("beta vulgaris"));
        Set<NomVernaculaire> betaVulgarisNoms = getSynchronizedNomsVernaculaires("carotte");
        Plante betterave = getSynchronizedPlante(betaVulgarisNoms, betaVulgarisCron);
        insertedPlants.put("betterave", betterave);
        insertedPlants.put("bette", insertPlantePotagere(betterave, "bette"));
        insertedPlants.put("betterave de détroit améliorée 3", insertPlantePotagere(betterave, "betterave de détroit améliorée 3"));

        ClassificationCronquist solanumLycopersicumCron = getOrInsertCronquist(new ClassificationCronquist().ordre("solanales").famille("solanaceae").genre("solanum").espece("solanum lycopersicum"));
        Set<NomVernaculaire> solanumLycopersicumNoms = getSynchronizedNomsVernaculaires("tomate");
        Plante tomate = getSynchronizedPlante(solanumLycopersicumNoms, solanumLycopersicumCron);
        insertedPlants.put("tomate", tomate);
        insertedPlants.put("tomate noire russe", insertPlantePotagere(tomate, "tomate noire russe"));
        insertedPlants.put("tomate beefsteak", insertPlantePotagere(tomate, "tomate beefsteak"));

        ClassificationCronquist lactucaSativaCron = getOrInsertCronquist(new ClassificationCronquist().ordre("asterales").famille("asteraceae").genre("lactuca").espece("lactuca sativa"));
        Set<NomVernaculaire> lactucaSativaNoms = getSynchronizedNomsVernaculaires("laitue");
        Plante laitue = getSynchronizedPlante(lactucaSativaNoms, lactucaSativaCron);
        insertedPlants.put("laitue", laitue);
        insertedPlants.put("laitue rouge grenobloise", insertPlantePotagere(laitue, "laitue rouge grenobloise"));
        insertedPlants.put("laitue reine de mai de pleine terre", insertPlantePotagere(laitue, "laitue reine de mai de pleine terre"));
        insertedPlants.put("laitue batavia lollo rossa", insertPlantePotagere(laitue, "laitue batavia lollo rossa"));
        insertedPlants.put("laitue pommée grosse blonde paresseuse", insertPlantePotagere(laitue, "laitue pommée grosse blonde paresseuse"));
        insertedPlants.put("laitue pommée reine de mai", insertPlantePotagere(laitue, "laitue pommée reine de mai"));
        insertedPlants.put("laitue batavia iceberg reine des glaces", insertPlantePotagere(laitue, "laitue batavia iceberg reine des glaces"));
        insertedPlants.put("laitue cressonnette marocaine", insertPlantePotagere(laitue, "laitue cressonnette marocaine"));
        insertedPlants.put("laitue à couper", insertPlantePotagere(laitue, "laitue à couper"));

        ClassificationCronquist phaseolusVulgarisCron = getOrInsertCronquist(new ClassificationCronquist().ordre("fabales").famille("fabaceae").genre("phaseolus").espece("phaseolus vulgaris"));
        Set<NomVernaculaire> phaseolusVulgarisNoms = getSynchronizedNomsVernaculaires("haricot");
        Plante haricot = getSynchronizedPlante(phaseolusVulgarisNoms, phaseolusVulgarisCron);
        insertedPlants.put("haricot", haricot);

        ClassificationCronquist fragariaVescaCron = getOrInsertCronquist(new ClassificationCronquist().ordre("rosales").famille("rosaceae").genre("fragaria").espece("fragaria vesca"));
        Set<NomVernaculaire> fragariaVescaNoms = getSynchronizedNomsVernaculaires("fraisier des bois");
        Plante fraisierDesBois = getSynchronizedPlante(fragariaVescaNoms, fragariaVescaCron);
        insertedPlants.put("fraisier des bois", fraisierDesBois);

        ClassificationCronquist alliumSchoenoprasumCron = getOrInsertCronquist(new ClassificationCronquist().ordre("liliales").famille("liliaceae").genre("allium").espece("allium schoenoprasum"));
        Set<NomVernaculaire> alliumSchoenoprasumNoms = getSynchronizedNomsVernaculaires("ciboulette", "ciboulette commune", "civette");
        Plante ciboulette = getSynchronizedPlante(alliumSchoenoprasumNoms, alliumSchoenoprasumCron);
        insertedPlants.put("ciboulette", ciboulette);
        return insertedPlants;
    }

    @NotNull
    private Set<NomVernaculaire> getSynchronizedNomsVernaculaires(String @NotNull ... noms) {
        Set<NomVernaculaire> daucusCarotaNoms = new HashSet<>();
        if (noms.length == 0) {
            return daucusCarotaNoms;
        }
        for (String nom : noms) {
            daucusCarotaNoms.add(new NomVernaculaire().nom(nom));
        }
        ;
        return persistAllNomsVernaculaires(daucusCarotaNoms);
    }

    /**
     * Une plante potagère déjà existante ne verra pas ses noms vernaculaires enregistrés
     *
     * @param planteBotanique Plante associée à la classification qui contient les plantes potagères. Cette plante doit déjà être synchronisée avec la base de données
     * @param noms            Plantes potagères qui seront associées à la plante botanique
     * @return la plante potagère avec son id
     */
    private @NotNull Plante insertPlantePotagere(@NotNull Plante planteBotanique, @NotNull String... noms) {
        if (planteBotanique.getId() == null) {
            throw new IllegalArgumentException("La plante botanique doit posséder un id");
        }
        // Enregistre la plante potagère
        Set<NomVernaculaire> nomVernaculaires = Arrays.stream(noms).map(nom -> new NomVernaculaire().nom(nom)).collect(Collectors.toSet());
        Set<NomVernaculaire> synchronizedNomsVernaculaires = persistAllNomsVernaculaires(nomVernaculaires);
        Plante plantePotagere = getSynchronizedPlante(synchronizedNomsVernaculaires, null);
        // Ajoute la plante potagère à la plante botanique
        planteBotanique.addPlantesPotageres(plantePotagere);
        // Enregistre la plante botanique
        planteRepository.save(planteBotanique);

        return plantePotagere;
    }

    /**
     * @param nomsVernaculaires liste des noms vernaculaires à synchroniser avec la base de données
     * @return Le set des noms synchronisés en base
     */
    private @NotNull Set<NomVernaculaire> persistAllNomsVernaculaires(@NotNull Set<NomVernaculaire> nomsVernaculaires) {
        Set<NomVernaculaire> synchronizedNomsVernaculaires = new HashSet<>();
        if (nomsVernaculaires.size() > 0) {
            for (NomVernaculaire nomVernaculaire : nomsVernaculaires) {
                synchronizedNomsVernaculaires.add(getOrInsertNomVernaculaire(nomVernaculaire));
            }
        }
        return synchronizedNomsVernaculaires;
    }

    /**
     * @param nomVernaculaireSet      Noms vernaculaires de la plante à enregistrer. Ils doivent déjà être synchronisés
     * @param classificationCronquist Peut-être null dans le cas d'une plante potagère
     * @return La plante synchronisée avec la base de données
     */
    private Plante getSynchronizedPlante(@NotNull Set<NomVernaculaire> nomVernaculaireSet, ClassificationCronquist classificationCronquist) {
        PlanteCriteria planteCriteria = new PlanteCriteria();
        // Filtre sur la classification
        if (classificationCronquist != null) {
            LongFilter idClassificationFilter = new LongFilter();
            idClassificationFilter.setEquals(classificationCronquist.getId());
            planteCriteria.setClassificationCronquistId(idClassificationFilter);
        }
        // Filtre sur les noms vernaculaires
        LongFilter nomVernaculaireFilter = new LongFilter();
        nomVernaculaireFilter.setIn(nomVernaculaireSet.stream().map(NomVernaculaire::getId).collect(Collectors.toList()));
        planteCriteria.setNomsVernaculairesId(nomVernaculaireFilter);

        Plante plante = null;
        List<Plante> foundPlants = removeDoublons(planteQueryService.findByCriteria(planteCriteria));
        if (foundPlants.size() == 0) {
            plante = new Plante();
            plante.nomsVernaculaires(nomVernaculaireSet).classificationCronquist(classificationCronquist);
            planteRepository.save(plante);
        } else {
            if (foundPlants.size() == 1) {
                plante = foundPlants.get(0);
            } else {
                log.error("Found multiple instances of : " + nomVernaculaireSet);
            }
        }
        return plante;
    }

    @NotNull
    private List<Plante> removeDoublons(@NotNull List<Plante> foundPlants) {
        if (foundPlants.size() == 0) {
            return foundPlants;
        }
        Map<Long, List<Plante>> plantesMap = foundPlants.stream().collect(Collectors.groupingBy(Plante::getId));
        List<Plante> foundPlants2 = new ArrayList<>();
        plantesMap.keySet().forEach(id -> foundPlants2.add(plantesMap.get(id).get(0)));
        foundPlants = foundPlants2;
        return foundPlants;
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

    private ClassificationCronquist getOrInsertCronquist(@NotNull ClassificationCronquist cronquist) {
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
