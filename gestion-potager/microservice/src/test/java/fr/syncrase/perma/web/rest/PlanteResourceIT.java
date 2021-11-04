package fr.syncrase.perma.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.perma.IntegrationTest;
import fr.syncrase.perma.domain.Allelopathie;
import fr.syncrase.perma.domain.Classification;
import fr.syncrase.perma.domain.CycleDeVie;
import fr.syncrase.perma.domain.Exposition;
import fr.syncrase.perma.domain.Feuillage;
import fr.syncrase.perma.domain.NomVernaculaire;
import fr.syncrase.perma.domain.Plante;
import fr.syncrase.perma.domain.Racine;
import fr.syncrase.perma.domain.Ressemblance;
import fr.syncrase.perma.domain.Sol;
import fr.syncrase.perma.domain.Strate;
import fr.syncrase.perma.domain.Temperature;
import fr.syncrase.perma.repository.PlanteRepository;
import fr.syncrase.perma.service.PlanteService;
import fr.syncrase.perma.service.criteria.PlanteCriteria;
import fr.syncrase.perma.service.dto.PlanteDTO;
import fr.syncrase.perma.service.mapper.PlanteMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PlanteResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PlanteResourceIT {

    private static final String DEFAULT_NOM_LATIN = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LATIN = "BBBBBBBBBB";

    private static final String DEFAULT_ENTRETIEN = "AAAAAAAAAA";
    private static final String UPDATED_ENTRETIEN = "BBBBBBBBBB";

    private static final String DEFAULT_HISTOIRE = "AAAAAAAAAA";
    private static final String UPDATED_HISTOIRE = "BBBBBBBBBB";

    private static final String DEFAULT_VITESSE = "AAAAAAAAAA";
    private static final String UPDATED_VITESSE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/plantes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PlanteRepository planteRepository;

    @Mock
    private PlanteRepository planteRepositoryMock;

    @Autowired
    private PlanteMapper planteMapper;

    @Mock
    private PlanteService planteServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlanteMockMvc;

    private Plante plante;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plante createEntity(EntityManager em) {
        Plante plante = new Plante()
            .nomLatin(DEFAULT_NOM_LATIN)
            .entretien(DEFAULT_ENTRETIEN)
            .histoire(DEFAULT_HISTOIRE)
            .vitesse(DEFAULT_VITESSE);
        return plante;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plante createUpdatedEntity(EntityManager em) {
        Plante plante = new Plante()
            .nomLatin(UPDATED_NOM_LATIN)
            .entretien(UPDATED_ENTRETIEN)
            .histoire(UPDATED_HISTOIRE)
            .vitesse(UPDATED_VITESSE);
        return plante;
    }

    @BeforeEach
    public void initTest() {
        plante = createEntity(em);
    }

    @Test
    @Transactional
    void createPlante() throws Exception {
        int databaseSizeBeforeCreate = planteRepository.findAll().size();
        // Create the Plante
        PlanteDTO planteDTO = planteMapper.toDto(plante);
        restPlanteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(planteDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Plante in the database
        List<Plante> planteList = planteRepository.findAll();
        assertThat(planteList).hasSize(databaseSizeBeforeCreate + 1);
        Plante testPlante = planteList.get(planteList.size() - 1);
        assertThat(testPlante.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
        assertThat(testPlante.getEntretien()).isEqualTo(DEFAULT_ENTRETIEN);
        assertThat(testPlante.getHistoire()).isEqualTo(DEFAULT_HISTOIRE);
        assertThat(testPlante.getVitesse()).isEqualTo(DEFAULT_VITESSE);
    }

    @Test
    @Transactional
    void createPlanteWithExistingId() throws Exception {
        // Create the Plante with an existing ID
        plante.setId(1L);
        PlanteDTO planteDTO = planteMapper.toDto(plante);

        int databaseSizeBeforeCreate = planteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlanteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(planteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plante in the database
        List<Plante> planteList = planteRepository.findAll();
        assertThat(planteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomLatinIsRequired() throws Exception {
        int databaseSizeBeforeTest = planteRepository.findAll().size();
        // set the field null
        plante.setNomLatin(null);

        // Create the Plante, which fails.
        PlanteDTO planteDTO = planteMapper.toDto(plante);

        restPlanteMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(planteDTO))
            )
            .andExpect(status().isBadRequest());

        List<Plante> planteList = planteRepository.findAll();
        assertThat(planteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPlantes() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList
        restPlanteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(plante.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)))
            .andExpect(jsonPath("$.[*].entretien").value(hasItem(DEFAULT_ENTRETIEN)))
            .andExpect(jsonPath("$.[*].histoire").value(hasItem(DEFAULT_HISTOIRE)))
            .andExpect(jsonPath("$.[*].vitesse").value(hasItem(DEFAULT_VITESSE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPlantesWithEagerRelationshipsIsEnabled() throws Exception {
        when(planteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPlanteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(planteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPlantesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(planteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPlanteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(planteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getPlante() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get the plante
        restPlanteMockMvc
            .perform(get(ENTITY_API_URL_ID, plante.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(plante.getId().intValue()))
            .andExpect(jsonPath("$.nomLatin").value(DEFAULT_NOM_LATIN))
            .andExpect(jsonPath("$.entretien").value(DEFAULT_ENTRETIEN))
            .andExpect(jsonPath("$.histoire").value(DEFAULT_HISTOIRE))
            .andExpect(jsonPath("$.vitesse").value(DEFAULT_VITESSE));
    }

    @Test
    @Transactional
    void getPlantesByIdFiltering() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        Long id = plante.getId();

        defaultPlanteShouldBeFound("id.equals=" + id);
        defaultPlanteShouldNotBeFound("id.notEquals=" + id);

        defaultPlanteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPlanteShouldNotBeFound("id.greaterThan=" + id);

        defaultPlanteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPlanteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPlantesByNomLatinIsEqualToSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where nomLatin equals to DEFAULT_NOM_LATIN
        defaultPlanteShouldBeFound("nomLatin.equals=" + DEFAULT_NOM_LATIN);

        // Get all the planteList where nomLatin equals to UPDATED_NOM_LATIN
        defaultPlanteShouldNotBeFound("nomLatin.equals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllPlantesByNomLatinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where nomLatin not equals to DEFAULT_NOM_LATIN
        defaultPlanteShouldNotBeFound("nomLatin.notEquals=" + DEFAULT_NOM_LATIN);

        // Get all the planteList where nomLatin not equals to UPDATED_NOM_LATIN
        defaultPlanteShouldBeFound("nomLatin.notEquals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllPlantesByNomLatinIsInShouldWork() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where nomLatin in DEFAULT_NOM_LATIN or UPDATED_NOM_LATIN
        defaultPlanteShouldBeFound("nomLatin.in=" + DEFAULT_NOM_LATIN + "," + UPDATED_NOM_LATIN);

        // Get all the planteList where nomLatin equals to UPDATED_NOM_LATIN
        defaultPlanteShouldNotBeFound("nomLatin.in=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllPlantesByNomLatinIsNullOrNotNull() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where nomLatin is not null
        defaultPlanteShouldBeFound("nomLatin.specified=true");

        // Get all the planteList where nomLatin is null
        defaultPlanteShouldNotBeFound("nomLatin.specified=false");
    }

    @Test
    @Transactional
    void getAllPlantesByNomLatinContainsSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where nomLatin contains DEFAULT_NOM_LATIN
        defaultPlanteShouldBeFound("nomLatin.contains=" + DEFAULT_NOM_LATIN);

        // Get all the planteList where nomLatin contains UPDATED_NOM_LATIN
        defaultPlanteShouldNotBeFound("nomLatin.contains=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllPlantesByNomLatinNotContainsSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where nomLatin does not contain DEFAULT_NOM_LATIN
        defaultPlanteShouldNotBeFound("nomLatin.doesNotContain=" + DEFAULT_NOM_LATIN);

        // Get all the planteList where nomLatin does not contain UPDATED_NOM_LATIN
        defaultPlanteShouldBeFound("nomLatin.doesNotContain=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllPlantesByEntretienIsEqualToSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where entretien equals to DEFAULT_ENTRETIEN
        defaultPlanteShouldBeFound("entretien.equals=" + DEFAULT_ENTRETIEN);

        // Get all the planteList where entretien equals to UPDATED_ENTRETIEN
        defaultPlanteShouldNotBeFound("entretien.equals=" + UPDATED_ENTRETIEN);
    }

    @Test
    @Transactional
    void getAllPlantesByEntretienIsNotEqualToSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where entretien not equals to DEFAULT_ENTRETIEN
        defaultPlanteShouldNotBeFound("entretien.notEquals=" + DEFAULT_ENTRETIEN);

        // Get all the planteList where entretien not equals to UPDATED_ENTRETIEN
        defaultPlanteShouldBeFound("entretien.notEquals=" + UPDATED_ENTRETIEN);
    }

    @Test
    @Transactional
    void getAllPlantesByEntretienIsInShouldWork() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where entretien in DEFAULT_ENTRETIEN or UPDATED_ENTRETIEN
        defaultPlanteShouldBeFound("entretien.in=" + DEFAULT_ENTRETIEN + "," + UPDATED_ENTRETIEN);

        // Get all the planteList where entretien equals to UPDATED_ENTRETIEN
        defaultPlanteShouldNotBeFound("entretien.in=" + UPDATED_ENTRETIEN);
    }

    @Test
    @Transactional
    void getAllPlantesByEntretienIsNullOrNotNull() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where entretien is not null
        defaultPlanteShouldBeFound("entretien.specified=true");

        // Get all the planteList where entretien is null
        defaultPlanteShouldNotBeFound("entretien.specified=false");
    }

    @Test
    @Transactional
    void getAllPlantesByEntretienContainsSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where entretien contains DEFAULT_ENTRETIEN
        defaultPlanteShouldBeFound("entretien.contains=" + DEFAULT_ENTRETIEN);

        // Get all the planteList where entretien contains UPDATED_ENTRETIEN
        defaultPlanteShouldNotBeFound("entretien.contains=" + UPDATED_ENTRETIEN);
    }

    @Test
    @Transactional
    void getAllPlantesByEntretienNotContainsSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where entretien does not contain DEFAULT_ENTRETIEN
        defaultPlanteShouldNotBeFound("entretien.doesNotContain=" + DEFAULT_ENTRETIEN);

        // Get all the planteList where entretien does not contain UPDATED_ENTRETIEN
        defaultPlanteShouldBeFound("entretien.doesNotContain=" + UPDATED_ENTRETIEN);
    }

    @Test
    @Transactional
    void getAllPlantesByHistoireIsEqualToSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where histoire equals to DEFAULT_HISTOIRE
        defaultPlanteShouldBeFound("histoire.equals=" + DEFAULT_HISTOIRE);

        // Get all the planteList where histoire equals to UPDATED_HISTOIRE
        defaultPlanteShouldNotBeFound("histoire.equals=" + UPDATED_HISTOIRE);
    }

    @Test
    @Transactional
    void getAllPlantesByHistoireIsNotEqualToSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where histoire not equals to DEFAULT_HISTOIRE
        defaultPlanteShouldNotBeFound("histoire.notEquals=" + DEFAULT_HISTOIRE);

        // Get all the planteList where histoire not equals to UPDATED_HISTOIRE
        defaultPlanteShouldBeFound("histoire.notEquals=" + UPDATED_HISTOIRE);
    }

    @Test
    @Transactional
    void getAllPlantesByHistoireIsInShouldWork() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where histoire in DEFAULT_HISTOIRE or UPDATED_HISTOIRE
        defaultPlanteShouldBeFound("histoire.in=" + DEFAULT_HISTOIRE + "," + UPDATED_HISTOIRE);

        // Get all the planteList where histoire equals to UPDATED_HISTOIRE
        defaultPlanteShouldNotBeFound("histoire.in=" + UPDATED_HISTOIRE);
    }

    @Test
    @Transactional
    void getAllPlantesByHistoireIsNullOrNotNull() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where histoire is not null
        defaultPlanteShouldBeFound("histoire.specified=true");

        // Get all the planteList where histoire is null
        defaultPlanteShouldNotBeFound("histoire.specified=false");
    }

    @Test
    @Transactional
    void getAllPlantesByHistoireContainsSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where histoire contains DEFAULT_HISTOIRE
        defaultPlanteShouldBeFound("histoire.contains=" + DEFAULT_HISTOIRE);

        // Get all the planteList where histoire contains UPDATED_HISTOIRE
        defaultPlanteShouldNotBeFound("histoire.contains=" + UPDATED_HISTOIRE);
    }

    @Test
    @Transactional
    void getAllPlantesByHistoireNotContainsSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where histoire does not contain DEFAULT_HISTOIRE
        defaultPlanteShouldNotBeFound("histoire.doesNotContain=" + DEFAULT_HISTOIRE);

        // Get all the planteList where histoire does not contain UPDATED_HISTOIRE
        defaultPlanteShouldBeFound("histoire.doesNotContain=" + UPDATED_HISTOIRE);
    }

    @Test
    @Transactional
    void getAllPlantesByVitesseIsEqualToSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where vitesse equals to DEFAULT_VITESSE
        defaultPlanteShouldBeFound("vitesse.equals=" + DEFAULT_VITESSE);

        // Get all the planteList where vitesse equals to UPDATED_VITESSE
        defaultPlanteShouldNotBeFound("vitesse.equals=" + UPDATED_VITESSE);
    }

    @Test
    @Transactional
    void getAllPlantesByVitesseIsNotEqualToSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where vitesse not equals to DEFAULT_VITESSE
        defaultPlanteShouldNotBeFound("vitesse.notEquals=" + DEFAULT_VITESSE);

        // Get all the planteList where vitesse not equals to UPDATED_VITESSE
        defaultPlanteShouldBeFound("vitesse.notEquals=" + UPDATED_VITESSE);
    }

    @Test
    @Transactional
    void getAllPlantesByVitesseIsInShouldWork() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where vitesse in DEFAULT_VITESSE or UPDATED_VITESSE
        defaultPlanteShouldBeFound("vitesse.in=" + DEFAULT_VITESSE + "," + UPDATED_VITESSE);

        // Get all the planteList where vitesse equals to UPDATED_VITESSE
        defaultPlanteShouldNotBeFound("vitesse.in=" + UPDATED_VITESSE);
    }

    @Test
    @Transactional
    void getAllPlantesByVitesseIsNullOrNotNull() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where vitesse is not null
        defaultPlanteShouldBeFound("vitesse.specified=true");

        // Get all the planteList where vitesse is null
        defaultPlanteShouldNotBeFound("vitesse.specified=false");
    }

    @Test
    @Transactional
    void getAllPlantesByVitesseContainsSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where vitesse contains DEFAULT_VITESSE
        defaultPlanteShouldBeFound("vitesse.contains=" + DEFAULT_VITESSE);

        // Get all the planteList where vitesse contains UPDATED_VITESSE
        defaultPlanteShouldNotBeFound("vitesse.contains=" + UPDATED_VITESSE);
    }

    @Test
    @Transactional
    void getAllPlantesByVitesseNotContainsSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where vitesse does not contain DEFAULT_VITESSE
        defaultPlanteShouldNotBeFound("vitesse.doesNotContain=" + DEFAULT_VITESSE);

        // Get all the planteList where vitesse does not contain UPDATED_VITESSE
        defaultPlanteShouldBeFound("vitesse.doesNotContain=" + UPDATED_VITESSE);
    }

    @Test
    @Transactional
    void getAllPlantesByCycleDeVieIsEqualToSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);
        CycleDeVie cycleDeVie;
        if (TestUtil.findAll(em, CycleDeVie.class).isEmpty()) {
            cycleDeVie = CycleDeVieResourceIT.createEntity(em);
            em.persist(cycleDeVie);
            em.flush();
        } else {
            cycleDeVie = TestUtil.findAll(em, CycleDeVie.class).get(0);
        }
        em.persist(cycleDeVie);
        em.flush();
        plante.setCycleDeVie(cycleDeVie);
        planteRepository.saveAndFlush(plante);
        Long cycleDeVieId = cycleDeVie.getId();

        // Get all the planteList where cycleDeVie equals to cycleDeVieId
        defaultPlanteShouldBeFound("cycleDeVieId.equals=" + cycleDeVieId);

        // Get all the planteList where cycleDeVie equals to (cycleDeVieId + 1)
        defaultPlanteShouldNotBeFound("cycleDeVieId.equals=" + (cycleDeVieId + 1));
    }

    @Test
    @Transactional
    void getAllPlantesByClassificationIsEqualToSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);
        Classification classification;
        if (TestUtil.findAll(em, Classification.class).isEmpty()) {
            classification = ClassificationResourceIT.createEntity(em);
            em.persist(classification);
            em.flush();
        } else {
            classification = TestUtil.findAll(em, Classification.class).get(0);
        }
        em.persist(classification);
        em.flush();
        plante.setClassification(classification);
        planteRepository.saveAndFlush(plante);
        Long classificationId = classification.getId();

        // Get all the planteList where classification equals to classificationId
        defaultPlanteShouldBeFound("classificationId.equals=" + classificationId);

        // Get all the planteList where classification equals to (classificationId + 1)
        defaultPlanteShouldNotBeFound("classificationId.equals=" + (classificationId + 1));
    }

    @Test
    @Transactional
    void getAllPlantesByConfusionsIsEqualToSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);
        Ressemblance confusions;
        if (TestUtil.findAll(em, Ressemblance.class).isEmpty()) {
            confusions = RessemblanceResourceIT.createEntity(em);
            em.persist(confusions);
            em.flush();
        } else {
            confusions = TestUtil.findAll(em, Ressemblance.class).get(0);
        }
        em.persist(confusions);
        em.flush();
        plante.addConfusions(confusions);
        planteRepository.saveAndFlush(plante);
        Long confusionsId = confusions.getId();

        // Get all the planteList where confusions equals to confusionsId
        defaultPlanteShouldBeFound("confusionsId.equals=" + confusionsId);

        // Get all the planteList where confusions equals to (confusionsId + 1)
        defaultPlanteShouldNotBeFound("confusionsId.equals=" + (confusionsId + 1));
    }

    @Test
    @Transactional
    void getAllPlantesByInteractionsIsEqualToSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);
        Allelopathie interactions;
        if (TestUtil.findAll(em, Allelopathie.class).isEmpty()) {
            interactions = AllelopathieResourceIT.createEntity(em);
            em.persist(interactions);
            em.flush();
        } else {
            interactions = TestUtil.findAll(em, Allelopathie.class).get(0);
        }
        em.persist(interactions);
        em.flush();
        plante.addInteractions(interactions);
        planteRepository.saveAndFlush(plante);
        Long interactionsId = interactions.getId();

        // Get all the planteList where interactions equals to interactionsId
        defaultPlanteShouldBeFound("interactionsId.equals=" + interactionsId);

        // Get all the planteList where interactions equals to (interactionsId + 1)
        defaultPlanteShouldNotBeFound("interactionsId.equals=" + (interactionsId + 1));
    }

    @Test
    @Transactional
    void getAllPlantesByExpositionsIsEqualToSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);
        Exposition expositions;
        if (TestUtil.findAll(em, Exposition.class).isEmpty()) {
            expositions = ExpositionResourceIT.createEntity(em);
            em.persist(expositions);
            em.flush();
        } else {
            expositions = TestUtil.findAll(em, Exposition.class).get(0);
        }
        em.persist(expositions);
        em.flush();
        plante.addExpositions(expositions);
        planteRepository.saveAndFlush(plante);
        Long expositionsId = expositions.getId();

        // Get all the planteList where expositions equals to expositionsId
        defaultPlanteShouldBeFound("expositionsId.equals=" + expositionsId);

        // Get all the planteList where expositions equals to (expositionsId + 1)
        defaultPlanteShouldNotBeFound("expositionsId.equals=" + (expositionsId + 1));
    }

    @Test
    @Transactional
    void getAllPlantesBySolsIsEqualToSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);
        Sol sols;
        if (TestUtil.findAll(em, Sol.class).isEmpty()) {
            sols = SolResourceIT.createEntity(em);
            em.persist(sols);
            em.flush();
        } else {
            sols = TestUtil.findAll(em, Sol.class).get(0);
        }
        em.persist(sols);
        em.flush();
        plante.addSols(sols);
        planteRepository.saveAndFlush(plante);
        Long solsId = sols.getId();

        // Get all the planteList where sols equals to solsId
        defaultPlanteShouldBeFound("solsId.equals=" + solsId);

        // Get all the planteList where sols equals to (solsId + 1)
        defaultPlanteShouldNotBeFound("solsId.equals=" + (solsId + 1));
    }

    @Test
    @Transactional
    void getAllPlantesByNomsVernaculairesIsEqualToSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);
        NomVernaculaire nomsVernaculaires;
        if (TestUtil.findAll(em, NomVernaculaire.class).isEmpty()) {
            nomsVernaculaires = NomVernaculaireResourceIT.createEntity(em);
            em.persist(nomsVernaculaires);
            em.flush();
        } else {
            nomsVernaculaires = TestUtil.findAll(em, NomVernaculaire.class).get(0);
        }
        em.persist(nomsVernaculaires);
        em.flush();
        plante.addNomsVernaculaires(nomsVernaculaires);
        planteRepository.saveAndFlush(plante);
        Long nomsVernaculairesId = nomsVernaculaires.getId();

        // Get all the planteList where nomsVernaculaires equals to nomsVernaculairesId
        defaultPlanteShouldBeFound("nomsVernaculairesId.equals=" + nomsVernaculairesId);

        // Get all the planteList where nomsVernaculaires equals to (nomsVernaculairesId + 1)
        defaultPlanteShouldNotBeFound("nomsVernaculairesId.equals=" + (nomsVernaculairesId + 1));
    }

    @Test
    @Transactional
    void getAllPlantesByTemperatureIsEqualToSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);
        Temperature temperature;
        if (TestUtil.findAll(em, Temperature.class).isEmpty()) {
            temperature = TemperatureResourceIT.createEntity(em);
            em.persist(temperature);
            em.flush();
        } else {
            temperature = TestUtil.findAll(em, Temperature.class).get(0);
        }
        em.persist(temperature);
        em.flush();
        plante.setTemperature(temperature);
        planteRepository.saveAndFlush(plante);
        Long temperatureId = temperature.getId();

        // Get all the planteList where temperature equals to temperatureId
        defaultPlanteShouldBeFound("temperatureId.equals=" + temperatureId);

        // Get all the planteList where temperature equals to (temperatureId + 1)
        defaultPlanteShouldNotBeFound("temperatureId.equals=" + (temperatureId + 1));
    }

    @Test
    @Transactional
    void getAllPlantesByRacineIsEqualToSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);
        Racine racine;
        if (TestUtil.findAll(em, Racine.class).isEmpty()) {
            racine = RacineResourceIT.createEntity(em);
            em.persist(racine);
            em.flush();
        } else {
            racine = TestUtil.findAll(em, Racine.class).get(0);
        }
        em.persist(racine);
        em.flush();
        plante.setRacine(racine);
        planteRepository.saveAndFlush(plante);
        Long racineId = racine.getId();

        // Get all the planteList where racine equals to racineId
        defaultPlanteShouldBeFound("racineId.equals=" + racineId);

        // Get all the planteList where racine equals to (racineId + 1)
        defaultPlanteShouldNotBeFound("racineId.equals=" + (racineId + 1));
    }

    @Test
    @Transactional
    void getAllPlantesByStrateIsEqualToSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);
        Strate strate;
        if (TestUtil.findAll(em, Strate.class).isEmpty()) {
            strate = StrateResourceIT.createEntity(em);
            em.persist(strate);
            em.flush();
        } else {
            strate = TestUtil.findAll(em, Strate.class).get(0);
        }
        em.persist(strate);
        em.flush();
        plante.setStrate(strate);
        planteRepository.saveAndFlush(plante);
        Long strateId = strate.getId();

        // Get all the planteList where strate equals to strateId
        defaultPlanteShouldBeFound("strateId.equals=" + strateId);

        // Get all the planteList where strate equals to (strateId + 1)
        defaultPlanteShouldNotBeFound("strateId.equals=" + (strateId + 1));
    }

    @Test
    @Transactional
    void getAllPlantesByFeuillageIsEqualToSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);
        Feuillage feuillage;
        if (TestUtil.findAll(em, Feuillage.class).isEmpty()) {
            feuillage = FeuillageResourceIT.createEntity(em);
            em.persist(feuillage);
            em.flush();
        } else {
            feuillage = TestUtil.findAll(em, Feuillage.class).get(0);
        }
        em.persist(feuillage);
        em.flush();
        plante.setFeuillage(feuillage);
        planteRepository.saveAndFlush(plante);
        Long feuillageId = feuillage.getId();

        // Get all the planteList where feuillage equals to feuillageId
        defaultPlanteShouldBeFound("feuillageId.equals=" + feuillageId);

        // Get all the planteList where feuillage equals to (feuillageId + 1)
        defaultPlanteShouldNotBeFound("feuillageId.equals=" + (feuillageId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPlanteShouldBeFound(String filter) throws Exception {
        restPlanteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(plante.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)))
            .andExpect(jsonPath("$.[*].entretien").value(hasItem(DEFAULT_ENTRETIEN)))
            .andExpect(jsonPath("$.[*].histoire").value(hasItem(DEFAULT_HISTOIRE)))
            .andExpect(jsonPath("$.[*].vitesse").value(hasItem(DEFAULT_VITESSE)));

        // Check, that the count call also returns 1
        restPlanteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPlanteShouldNotBeFound(String filter) throws Exception {
        restPlanteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPlanteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPlante() throws Exception {
        // Get the plante
        restPlanteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPlante() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        int databaseSizeBeforeUpdate = planteRepository.findAll().size();

        // Update the plante
        Plante updatedPlante = planteRepository.findById(plante.getId()).get();
        // Disconnect from session so that the updates on updatedPlante are not directly saved in db
        em.detach(updatedPlante);
        updatedPlante.nomLatin(UPDATED_NOM_LATIN).entretien(UPDATED_ENTRETIEN).histoire(UPDATED_HISTOIRE).vitesse(UPDATED_VITESSE);
        PlanteDTO planteDTO = planteMapper.toDto(updatedPlante);

        restPlanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, planteDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(planteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Plante in the database
        List<Plante> planteList = planteRepository.findAll();
        assertThat(planteList).hasSize(databaseSizeBeforeUpdate);
        Plante testPlante = planteList.get(planteList.size() - 1);
        assertThat(testPlante.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
        assertThat(testPlante.getEntretien()).isEqualTo(UPDATED_ENTRETIEN);
        assertThat(testPlante.getHistoire()).isEqualTo(UPDATED_HISTOIRE);
        assertThat(testPlante.getVitesse()).isEqualTo(UPDATED_VITESSE);
    }

    @Test
    @Transactional
    void putNonExistingPlante() throws Exception {
        int databaseSizeBeforeUpdate = planteRepository.findAll().size();
        plante.setId(count.incrementAndGet());

        // Create the Plante
        PlanteDTO planteDTO = planteMapper.toDto(plante);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, planteDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(planteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plante in the database
        List<Plante> planteList = planteRepository.findAll();
        assertThat(planteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlante() throws Exception {
        int databaseSizeBeforeUpdate = planteRepository.findAll().size();
        plante.setId(count.incrementAndGet());

        // Create the Plante
        PlanteDTO planteDTO = planteMapper.toDto(plante);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(planteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plante in the database
        List<Plante> planteList = planteRepository.findAll();
        assertThat(planteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlante() throws Exception {
        int databaseSizeBeforeUpdate = planteRepository.findAll().size();
        plante.setId(count.incrementAndGet());

        // Create the Plante
        PlanteDTO planteDTO = planteMapper.toDto(plante);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanteMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(planteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Plante in the database
        List<Plante> planteList = planteRepository.findAll();
        assertThat(planteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePlanteWithPatch() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        int databaseSizeBeforeUpdate = planteRepository.findAll().size();

        // Update the plante using partial update
        Plante partialUpdatedPlante = new Plante();
        partialUpdatedPlante.setId(plante.getId());

        partialUpdatedPlante.histoire(UPDATED_HISTOIRE);

        restPlanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlante.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlante))
            )
            .andExpect(status().isOk());

        // Validate the Plante in the database
        List<Plante> planteList = planteRepository.findAll();
        assertThat(planteList).hasSize(databaseSizeBeforeUpdate);
        Plante testPlante = planteList.get(planteList.size() - 1);
        assertThat(testPlante.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
        assertThat(testPlante.getEntretien()).isEqualTo(DEFAULT_ENTRETIEN);
        assertThat(testPlante.getHistoire()).isEqualTo(UPDATED_HISTOIRE);
        assertThat(testPlante.getVitesse()).isEqualTo(DEFAULT_VITESSE);
    }

    @Test
    @Transactional
    void fullUpdatePlanteWithPatch() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        int databaseSizeBeforeUpdate = planteRepository.findAll().size();

        // Update the plante using partial update
        Plante partialUpdatedPlante = new Plante();
        partialUpdatedPlante.setId(plante.getId());

        partialUpdatedPlante.nomLatin(UPDATED_NOM_LATIN).entretien(UPDATED_ENTRETIEN).histoire(UPDATED_HISTOIRE).vitesse(UPDATED_VITESSE);

        restPlanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlante.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlante))
            )
            .andExpect(status().isOk());

        // Validate the Plante in the database
        List<Plante> planteList = planteRepository.findAll();
        assertThat(planteList).hasSize(databaseSizeBeforeUpdate);
        Plante testPlante = planteList.get(planteList.size() - 1);
        assertThat(testPlante.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
        assertThat(testPlante.getEntretien()).isEqualTo(UPDATED_ENTRETIEN);
        assertThat(testPlante.getHistoire()).isEqualTo(UPDATED_HISTOIRE);
        assertThat(testPlante.getVitesse()).isEqualTo(UPDATED_VITESSE);
    }

    @Test
    @Transactional
    void patchNonExistingPlante() throws Exception {
        int databaseSizeBeforeUpdate = planteRepository.findAll().size();
        plante.setId(count.incrementAndGet());

        // Create the Plante
        PlanteDTO planteDTO = planteMapper.toDto(plante);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, planteDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(planteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plante in the database
        List<Plante> planteList = planteRepository.findAll();
        assertThat(planteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlante() throws Exception {
        int databaseSizeBeforeUpdate = planteRepository.findAll().size();
        plante.setId(count.incrementAndGet());

        // Create the Plante
        PlanteDTO planteDTO = planteMapper.toDto(plante);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(planteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plante in the database
        List<Plante> planteList = planteRepository.findAll();
        assertThat(planteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlante() throws Exception {
        int databaseSizeBeforeUpdate = planteRepository.findAll().size();
        plante.setId(count.incrementAndGet());

        // Create the Plante
        PlanteDTO planteDTO = planteMapper.toDto(plante);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanteMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(planteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Plante in the database
        List<Plante> planteList = planteRepository.findAll();
        assertThat(planteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePlante() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        int databaseSizeBeforeDelete = planteRepository.findAll().size();

        // Delete the plante
        restPlanteMockMvc
            .perform(delete(ENTITY_API_URL_ID, plante.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Plante> planteList = planteRepository.findAll();
        assertThat(planteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
