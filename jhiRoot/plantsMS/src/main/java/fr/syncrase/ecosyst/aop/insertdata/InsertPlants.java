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
import org.jetbrains.annotations.Unmodifiable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import tech.jhipster.service.filter.LongFilter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InsertPlants {

    private final Logger log = LoggerFactory.getLogger(InsertPlants.class);

    private final ClassificationCronquistRepository classificationCronquistRepository;
    private final NomVernaculaireRepository nomVernaculaireRepository;
    private final PlanteRepository planteRepository;
    private final PlanteQueryService planteQueryService;

    Map<String, Plante> insertedPlants;

    public InsertPlants(ClassificationCronquistRepository classificationCronquistRepository, NomVernaculaireRepository nomVernaculaireRepository, PlanteRepository planteRepository, PlanteQueryService planteQueryService) {
        this.classificationCronquistRepository = classificationCronquistRepository;
        this.nomVernaculaireRepository = nomVernaculaireRepository;
        this.planteRepository = planteRepository;
        this.planteQueryService = planteQueryService;
    }

    /**
     * insert all plants in database and
     */
    protected Map<String, Plante> insertAllPlants() {
        insertedPlants = new HashMap<>();

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
        insertPlante(
            new ClassificationCronquist().ordre("apiales").famille("apiaceae").genre("daucus").espece("daucus carota"),
            new String[]{"carotte"},
            Stream.of(new Object[][]{
                {"carotte jaune du doubs", new String[]{"carotte jaune du doubs"}},
            }).collect(Collectors.toMap(plantePotagere -> (String) plantePotagere[0], nomsVernaculaires -> (String[]) nomsVernaculaires[1]))
        );

        insertPlante(
            new ClassificationCronquist().ordre("caryophyllales").famille("chenopodiaceae").genre("beta").espece("beta vulgaris"),
            new String[]{"betterave"},
            Stream.of(new Object[][]{
                {"bette", new String[]{"bette"}},
                {"betterave de détroit améliorée 3", new String[]{"betterave de détroit améliorée 3"}},
            }).collect(Collectors.toMap(plantePotagere -> (String) plantePotagere[0], nomsVernaculaires -> (String[]) nomsVernaculaires[1]))
        );

        insertPlante(
            new ClassificationCronquist().ordre("solanales").famille("solanaceae").genre("solanum").espece("solanum lycopersicum"),
            new String[]{"tomate"},
            Stream.of(new Object[][]{
                {"tomate noire russe", new String[]{"tomate noire russe"}},
                {"tomate beefsteak", new String[]{"tomate beefsteak"}},
            }).collect(Collectors.toMap(plantePotagere -> (String) plantePotagere[0], nomsVernaculaires -> (String[]) nomsVernaculaires[1]))
        );

        insertPlante(
            new ClassificationCronquist().ordre("asterales").famille("asteraceae").genre("lactuca").espece("lactuca sativa"),
            new String[]{"laitue"},
            Stream.of(new Object[][]{
                {"laitue rouge grenobloise", new String[]{"laitue rouge grenobloise"}},
                {"laitue reine de mai de pleine terre", new String[]{"laitue reine de mai de pleine terre"}},
                {"laitue batavia lollo rossa", new String[]{"laitue batavia lollo rossa"}},
                {"laitue pommée grosse blonde paresseuse", new String[]{"laitue pommée grosse blonde paresseuse"}},
                {"laitue pommée reine de mai", new String[]{"laitue pommée reine de mai"}},
                {"laitue batavia iceberg reine des glaces", new String[]{"laitue batavia iceberg reine des glaces"}},
                {"laitue cressonnette marocaine", new String[]{"laitue cressonnette marocaine"}},
                {"laitue à couper", new String[]{"laitue à couper"}},
            }).collect(Collectors.toMap(plantePotagere -> (String) plantePotagere[0], nomsVernaculaires -> (String[]) nomsVernaculaires[1]))
        );

        insertPlante(
            new ClassificationCronquist().ordre("fabales").famille("fabaceae").genre("phaseolus").espece("phaseolus vulgaris"),
            new String[]{"haricot"},
            Stream.of(new Object[][]{}).collect(Collectors.toMap(plantePotagere -> (String) plantePotagere[0], nomsVernaculaires -> (String[]) nomsVernaculaires[1]))
        );

        insertPlante(
            new ClassificationCronquist().ordre("rosales").famille("rosaceae").genre("fragaria").espece("fragaria vesca"),
            new String[]{"fraisier des bois"},
            Stream.of(new Object[][]{}).collect(Collectors.toMap(plantePotagere -> (String) plantePotagere[0], nomsVernaculaires -> (String[]) nomsVernaculaires[1]))
        );

        insertPlante(
            new ClassificationCronquist().ordre("liliales").famille("liliaceae").genre("allium").espece("allium schoenoprasum"),
            new String[]{"ciboulette", "ciboulette commune", "civette"},
            Stream.of(new Object[][]{}).collect(Collectors.toMap(plantePotagere -> (String) plantePotagere[0], nomsVernaculaires -> (String[]) nomsVernaculaires[1]))
        );

        return insertedPlants;
    }

    private void insertPlante(ClassificationCronquist cronquist, String[] nomsVernaculairesPlanteBotanique, Map<String, String[]> plantesPotageres) {
        cronquist = getOrInsertCronquist(cronquist);
        Set<NomVernaculaire> nomVernaculaireSet = getSynchronizedNomsVernaculaires(nomsVernaculairesPlanteBotanique);
        Plante planteBotanique = getSynchronizedPlante(nomVernaculaireSet, cronquist);

        for (String nv : nomsVernaculairesPlanteBotanique) {
            insertedPlants.put(nv, planteBotanique);
        }
        for (String pp : plantesPotageres.keySet()) {
            insertedPlants.put(pp, insertPlantePotagere(planteBotanique, plantesPotageres.get(pp)));
        }
    }

    @NotNull
    private Set<NomVernaculaire> getSynchronizedNomsVernaculaires(String @NotNull ... noms) {
        Set<NomVernaculaire> nomVernaculaireHashSet = new HashSet<>();
        if (noms.length == 0) {
            return nomVernaculaireHashSet;
        }
        for (String nom : noms) {
            nomVernaculaireHashSet.add(new NomVernaculaire().nom(nom));
        }
        return persistAllNomsVernaculaires(nomVernaculaireHashSet);
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

    @Unmodifiable
    @NotNull
    private List<Plante> removeDoublons(@NotNull List<Plante> foundPlants) {
        TreeSet<Plante> planteSet = new TreeSet<>(Comparator.comparing(Plante::getId));
        planteSet.addAll(foundPlants);
        return List.copyOf(planteSet);
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

}
