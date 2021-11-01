package fr.syncrase.perma.web.rest;

import fr.syncrase.perma.MicroserviceApp;
import fr.syncrase.perma.config.TestSecurityConfiguration;
import fr.syncrase.perma.domain.Plante;
import fr.syncrase.perma.domain.CycleDeVie;
import fr.syncrase.perma.domain.Classification;
import fr.syncrase.perma.domain.Ressemblance;
import fr.syncrase.perma.domain.Allelopathie;
import fr.syncrase.perma.domain.NomVernaculaire;
import fr.syncrase.perma.repository.PlanteRepository;
import fr.syncrase.perma.service.PlanteService;
import fr.syncrase.perma.service.dto.PlanteDTO;
import fr.syncrase.perma.service.mapper.PlanteMapper;
import fr.syncrase.perma.service.dto.PlanteCriteria;
import fr.syncrase.perma.service.PlanteQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PlanteResource} REST controller.
 */
@SpringBootTest(classes = { MicroserviceApp.class, TestSecurityConfiguration.class })
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class PlanteResourceIT {

    private static final String DEFAULT_NOM_LATIN = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LATIN = "BBBBBBBBBB";

    private static final String DEFAULT_ENTRETIEN = "AAAAAAAAAA";
    private static final String UPDATED_ENTRETIEN = "BBBBBBBBBB";

    private static final String DEFAULT_HISTOIRE = "AAAAAAAAAA";
    private static final String UPDATED_HISTOIRE = "BBBBBBBBBB";

    private static final String DEFAULT_EXPOSITION = "AAAAAAAAAA";
    private static final String UPDATED_EXPOSITION = "BBBBBBBBBB";

    private static final String DEFAULT_RUSTICITE = "AAAAAAAAAA";
    private static final String UPDATED_RUSTICITE = "BBBBBBBBBB";

    @Autowired
    private PlanteRepository planteRepository;

    @Mock
    private PlanteRepository planteRepositoryMock;

    @Autowired
    private PlanteMapper planteMapper;

    @Mock
    private PlanteService planteServiceMock;

    @Autowired
    private PlanteService planteService;

    @Autowired
    private PlanteQueryService planteQueryService;

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
            .exposition(DEFAULT_EXPOSITION)
            .rusticite(DEFAULT_RUSTICITE);
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
            .exposition(UPDATED_EXPOSITION)
            .rusticite(UPDATED_RUSTICITE);
        return plante;
    }

    @BeforeEach
    public void initTest() {
        plante = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlante() throws Exception {
        int databaseSizeBeforeCreate = planteRepository.findAll().size();
        // Create the Plante
        PlanteDTO planteDTO = planteMapper.toDto(plante);
        restPlanteMockMvc.perform(post("/api/plantes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(planteDTO)))
            .andExpect(status().isCreated());

        // Validate the Plante in the database
        List<Plante> planteList = planteRepository.findAll();
        assertThat(planteList).hasSize(databaseSizeBeforeCreate + 1);
        Plante testPlante = planteList.get(planteList.size() - 1);
        assertThat(testPlante.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
        assertThat(testPlante.getEntretien()).isEqualTo(DEFAULT_ENTRETIEN);
        assertThat(testPlante.getHistoire()).isEqualTo(DEFAULT_HISTOIRE);
        assertThat(testPlante.getExposition()).isEqualTo(DEFAULT_EXPOSITION);
        assertThat(testPlante.getRusticite()).isEqualTo(DEFAULT_RUSTICITE);
    }

    @Test
    @Transactional
    public void createPlanteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = planteRepository.findAll().size();

        // Create the Plante with an existing ID
        plante.setId(1L);
        PlanteDTO planteDTO = planteMapper.toDto(plante);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlanteMockMvc.perform(post("/api/plantes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(planteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Plante in the database
        List<Plante> planteList = planteRepository.findAll();
        assertThat(planteList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNomLatinIsRequired() throws Exception {
        int databaseSizeBeforeTest = planteRepository.findAll().size();
        // set the field null
        plante.setNomLatin(null);

        // Create the Plante, which fails.
        PlanteDTO planteDTO = planteMapper.toDto(plante);


        restPlanteMockMvc.perform(post("/api/plantes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(planteDTO)))
            .andExpect(status().isBadRequest());

        List<Plante> planteList = planteRepository.findAll();
        assertThat(planteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPlantes() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList
        restPlanteMockMvc.perform(get("/api/plantes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(plante.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)))
            .andExpect(jsonPath("$.[*].entretien").value(hasItem(DEFAULT_ENTRETIEN)))
            .andExpect(jsonPath("$.[*].histoire").value(hasItem(DEFAULT_HISTOIRE)))
            .andExpect(jsonPath("$.[*].exposition").value(hasItem(DEFAULT_EXPOSITION)))
            .andExpect(jsonPath("$.[*].rusticite").value(hasItem(DEFAULT_RUSTICITE)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllPlantesWithEagerRelationshipsIsEnabled() throws Exception {
        when(planteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPlanteMockMvc.perform(get("/api/plantes?eagerload=true"))
            .andExpect(status().isOk());

        verify(planteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllPlantesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(planteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPlanteMockMvc.perform(get("/api/plantes?eagerload=true"))
            .andExpect(status().isOk());

        verify(planteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getPlante() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get the plante
        restPlanteMockMvc.perform(get("/api/plantes/{id}", plante.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(plante.getId().intValue()))
            .andExpect(jsonPath("$.nomLatin").value(DEFAULT_NOM_LATIN))
            .andExpect(jsonPath("$.entretien").value(DEFAULT_ENTRETIEN))
            .andExpect(jsonPath("$.histoire").value(DEFAULT_HISTOIRE))
            .andExpect(jsonPath("$.exposition").value(DEFAULT_EXPOSITION))
            .andExpect(jsonPath("$.rusticite").value(DEFAULT_RUSTICITE));
    }


    @Test
    @Transactional
    public void getPlantesByIdFiltering() throws Exception {
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
    public void getAllPlantesByNomLatinIsEqualToSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where nomLatin equals to DEFAULT_NOM_LATIN
        defaultPlanteShouldBeFound("nomLatin.equals=" + DEFAULT_NOM_LATIN);

        // Get all the planteList where nomLatin equals to UPDATED_NOM_LATIN
        defaultPlanteShouldNotBeFound("nomLatin.equals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    public void getAllPlantesByNomLatinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where nomLatin not equals to DEFAULT_NOM_LATIN
        defaultPlanteShouldNotBeFound("nomLatin.notEquals=" + DEFAULT_NOM_LATIN);

        // Get all the planteList where nomLatin not equals to UPDATED_NOM_LATIN
        defaultPlanteShouldBeFound("nomLatin.notEquals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    public void getAllPlantesByNomLatinIsInShouldWork() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where nomLatin in DEFAULT_NOM_LATIN or UPDATED_NOM_LATIN
        defaultPlanteShouldBeFound("nomLatin.in=" + DEFAULT_NOM_LATIN + "," + UPDATED_NOM_LATIN);

        // Get all the planteList where nomLatin equals to UPDATED_NOM_LATIN
        defaultPlanteShouldNotBeFound("nomLatin.in=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    public void getAllPlantesByNomLatinIsNullOrNotNull() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where nomLatin is not null
        defaultPlanteShouldBeFound("nomLatin.specified=true");

        // Get all the planteList where nomLatin is null
        defaultPlanteShouldNotBeFound("nomLatin.specified=false");
    }
                @Test
    @Transactional
    public void getAllPlantesByNomLatinContainsSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where nomLatin contains DEFAULT_NOM_LATIN
        defaultPlanteShouldBeFound("nomLatin.contains=" + DEFAULT_NOM_LATIN);

        // Get all the planteList where nomLatin contains UPDATED_NOM_LATIN
        defaultPlanteShouldNotBeFound("nomLatin.contains=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    public void getAllPlantesByNomLatinNotContainsSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where nomLatin does not contain DEFAULT_NOM_LATIN
        defaultPlanteShouldNotBeFound("nomLatin.doesNotContain=" + DEFAULT_NOM_LATIN);

        // Get all the planteList where nomLatin does not contain UPDATED_NOM_LATIN
        defaultPlanteShouldBeFound("nomLatin.doesNotContain=" + UPDATED_NOM_LATIN);
    }


    @Test
    @Transactional
    public void getAllPlantesByEntretienIsEqualToSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where entretien equals to DEFAULT_ENTRETIEN
        defaultPlanteShouldBeFound("entretien.equals=" + DEFAULT_ENTRETIEN);

        // Get all the planteList where entretien equals to UPDATED_ENTRETIEN
        defaultPlanteShouldNotBeFound("entretien.equals=" + UPDATED_ENTRETIEN);
    }

    @Test
    @Transactional
    public void getAllPlantesByEntretienIsNotEqualToSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where entretien not equals to DEFAULT_ENTRETIEN
        defaultPlanteShouldNotBeFound("entretien.notEquals=" + DEFAULT_ENTRETIEN);

        // Get all the planteList where entretien not equals to UPDATED_ENTRETIEN
        defaultPlanteShouldBeFound("entretien.notEquals=" + UPDATED_ENTRETIEN);
    }

    @Test
    @Transactional
    public void getAllPlantesByEntretienIsInShouldWork() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where entretien in DEFAULT_ENTRETIEN or UPDATED_ENTRETIEN
        defaultPlanteShouldBeFound("entretien.in=" + DEFAULT_ENTRETIEN + "," + UPDATED_ENTRETIEN);

        // Get all the planteList where entretien equals to UPDATED_ENTRETIEN
        defaultPlanteShouldNotBeFound("entretien.in=" + UPDATED_ENTRETIEN);
    }

    @Test
    @Transactional
    public void getAllPlantesByEntretienIsNullOrNotNull() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where entretien is not null
        defaultPlanteShouldBeFound("entretien.specified=true");

        // Get all the planteList where entretien is null
        defaultPlanteShouldNotBeFound("entretien.specified=false");
    }
                @Test
    @Transactional
    public void getAllPlantesByEntretienContainsSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where entretien contains DEFAULT_ENTRETIEN
        defaultPlanteShouldBeFound("entretien.contains=" + DEFAULT_ENTRETIEN);

        // Get all the planteList where entretien contains UPDATED_ENTRETIEN
        defaultPlanteShouldNotBeFound("entretien.contains=" + UPDATED_ENTRETIEN);
    }

    @Test
    @Transactional
    public void getAllPlantesByEntretienNotContainsSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where entretien does not contain DEFAULT_ENTRETIEN
        defaultPlanteShouldNotBeFound("entretien.doesNotContain=" + DEFAULT_ENTRETIEN);

        // Get all the planteList where entretien does not contain UPDATED_ENTRETIEN
        defaultPlanteShouldBeFound("entretien.doesNotContain=" + UPDATED_ENTRETIEN);
    }


    @Test
    @Transactional
    public void getAllPlantesByHistoireIsEqualToSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where histoire equals to DEFAULT_HISTOIRE
        defaultPlanteShouldBeFound("histoire.equals=" + DEFAULT_HISTOIRE);

        // Get all the planteList where histoire equals to UPDATED_HISTOIRE
        defaultPlanteShouldNotBeFound("histoire.equals=" + UPDATED_HISTOIRE);
    }

    @Test
    @Transactional
    public void getAllPlantesByHistoireIsNotEqualToSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where histoire not equals to DEFAULT_HISTOIRE
        defaultPlanteShouldNotBeFound("histoire.notEquals=" + DEFAULT_HISTOIRE);

        // Get all the planteList where histoire not equals to UPDATED_HISTOIRE
        defaultPlanteShouldBeFound("histoire.notEquals=" + UPDATED_HISTOIRE);
    }

    @Test
    @Transactional
    public void getAllPlantesByHistoireIsInShouldWork() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where histoire in DEFAULT_HISTOIRE or UPDATED_HISTOIRE
        defaultPlanteShouldBeFound("histoire.in=" + DEFAULT_HISTOIRE + "," + UPDATED_HISTOIRE);

        // Get all the planteList where histoire equals to UPDATED_HISTOIRE
        defaultPlanteShouldNotBeFound("histoire.in=" + UPDATED_HISTOIRE);
    }

    @Test
    @Transactional
    public void getAllPlantesByHistoireIsNullOrNotNull() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where histoire is not null
        defaultPlanteShouldBeFound("histoire.specified=true");

        // Get all the planteList where histoire is null
        defaultPlanteShouldNotBeFound("histoire.specified=false");
    }
                @Test
    @Transactional
    public void getAllPlantesByHistoireContainsSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where histoire contains DEFAULT_HISTOIRE
        defaultPlanteShouldBeFound("histoire.contains=" + DEFAULT_HISTOIRE);

        // Get all the planteList where histoire contains UPDATED_HISTOIRE
        defaultPlanteShouldNotBeFound("histoire.contains=" + UPDATED_HISTOIRE);
    }

    @Test
    @Transactional
    public void getAllPlantesByHistoireNotContainsSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where histoire does not contain DEFAULT_HISTOIRE
        defaultPlanteShouldNotBeFound("histoire.doesNotContain=" + DEFAULT_HISTOIRE);

        // Get all the planteList where histoire does not contain UPDATED_HISTOIRE
        defaultPlanteShouldBeFound("histoire.doesNotContain=" + UPDATED_HISTOIRE);
    }


    @Test
    @Transactional
    public void getAllPlantesByExpositionIsEqualToSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where exposition equals to DEFAULT_EXPOSITION
        defaultPlanteShouldBeFound("exposition.equals=" + DEFAULT_EXPOSITION);

        // Get all the planteList where exposition equals to UPDATED_EXPOSITION
        defaultPlanteShouldNotBeFound("exposition.equals=" + UPDATED_EXPOSITION);
    }

    @Test
    @Transactional
    public void getAllPlantesByExpositionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where exposition not equals to DEFAULT_EXPOSITION
        defaultPlanteShouldNotBeFound("exposition.notEquals=" + DEFAULT_EXPOSITION);

        // Get all the planteList where exposition not equals to UPDATED_EXPOSITION
        defaultPlanteShouldBeFound("exposition.notEquals=" + UPDATED_EXPOSITION);
    }

    @Test
    @Transactional
    public void getAllPlantesByExpositionIsInShouldWork() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where exposition in DEFAULT_EXPOSITION or UPDATED_EXPOSITION
        defaultPlanteShouldBeFound("exposition.in=" + DEFAULT_EXPOSITION + "," + UPDATED_EXPOSITION);

        // Get all the planteList where exposition equals to UPDATED_EXPOSITION
        defaultPlanteShouldNotBeFound("exposition.in=" + UPDATED_EXPOSITION);
    }

    @Test
    @Transactional
    public void getAllPlantesByExpositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where exposition is not null
        defaultPlanteShouldBeFound("exposition.specified=true");

        // Get all the planteList where exposition is null
        defaultPlanteShouldNotBeFound("exposition.specified=false");
    }
                @Test
    @Transactional
    public void getAllPlantesByExpositionContainsSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where exposition contains DEFAULT_EXPOSITION
        defaultPlanteShouldBeFound("exposition.contains=" + DEFAULT_EXPOSITION);

        // Get all the planteList where exposition contains UPDATED_EXPOSITION
        defaultPlanteShouldNotBeFound("exposition.contains=" + UPDATED_EXPOSITION);
    }

    @Test
    @Transactional
    public void getAllPlantesByExpositionNotContainsSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where exposition does not contain DEFAULT_EXPOSITION
        defaultPlanteShouldNotBeFound("exposition.doesNotContain=" + DEFAULT_EXPOSITION);

        // Get all the planteList where exposition does not contain UPDATED_EXPOSITION
        defaultPlanteShouldBeFound("exposition.doesNotContain=" + UPDATED_EXPOSITION);
    }


    @Test
    @Transactional
    public void getAllPlantesByRusticiteIsEqualToSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where rusticite equals to DEFAULT_RUSTICITE
        defaultPlanteShouldBeFound("rusticite.equals=" + DEFAULT_RUSTICITE);

        // Get all the planteList where rusticite equals to UPDATED_RUSTICITE
        defaultPlanteShouldNotBeFound("rusticite.equals=" + UPDATED_RUSTICITE);
    }

    @Test
    @Transactional
    public void getAllPlantesByRusticiteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where rusticite not equals to DEFAULT_RUSTICITE
        defaultPlanteShouldNotBeFound("rusticite.notEquals=" + DEFAULT_RUSTICITE);

        // Get all the planteList where rusticite not equals to UPDATED_RUSTICITE
        defaultPlanteShouldBeFound("rusticite.notEquals=" + UPDATED_RUSTICITE);
    }

    @Test
    @Transactional
    public void getAllPlantesByRusticiteIsInShouldWork() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where rusticite in DEFAULT_RUSTICITE or UPDATED_RUSTICITE
        defaultPlanteShouldBeFound("rusticite.in=" + DEFAULT_RUSTICITE + "," + UPDATED_RUSTICITE);

        // Get all the planteList where rusticite equals to UPDATED_RUSTICITE
        defaultPlanteShouldNotBeFound("rusticite.in=" + UPDATED_RUSTICITE);
    }

    @Test
    @Transactional
    public void getAllPlantesByRusticiteIsNullOrNotNull() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where rusticite is not null
        defaultPlanteShouldBeFound("rusticite.specified=true");

        // Get all the planteList where rusticite is null
        defaultPlanteShouldNotBeFound("rusticite.specified=false");
    }
                @Test
    @Transactional
    public void getAllPlantesByRusticiteContainsSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where rusticite contains DEFAULT_RUSTICITE
        defaultPlanteShouldBeFound("rusticite.contains=" + DEFAULT_RUSTICITE);

        // Get all the planteList where rusticite contains UPDATED_RUSTICITE
        defaultPlanteShouldNotBeFound("rusticite.contains=" + UPDATED_RUSTICITE);
    }

    @Test
    @Transactional
    public void getAllPlantesByRusticiteNotContainsSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        // Get all the planteList where rusticite does not contain DEFAULT_RUSTICITE
        defaultPlanteShouldNotBeFound("rusticite.doesNotContain=" + DEFAULT_RUSTICITE);

        // Get all the planteList where rusticite does not contain UPDATED_RUSTICITE
        defaultPlanteShouldBeFound("rusticite.doesNotContain=" + UPDATED_RUSTICITE);
    }


    @Test
    @Transactional
    public void getAllPlantesByCycleDeVieIsEqualToSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);
        CycleDeVie cycleDeVie = CycleDeVieResourceIT.createEntity(em);
        em.persist(cycleDeVie);
        em.flush();
        plante.setCycleDeVie(cycleDeVie);
        planteRepository.saveAndFlush(plante);
        Long cycleDeVieId = cycleDeVie.getId();

        // Get all the planteList where cycleDeVie equals to cycleDeVieId
        defaultPlanteShouldBeFound("cycleDeVieId.equals=" + cycleDeVieId);

        // Get all the planteList where cycleDeVie equals to cycleDeVieId + 1
        defaultPlanteShouldNotBeFound("cycleDeVieId.equals=" + (cycleDeVieId + 1));
    }


    @Test
    @Transactional
    public void getAllPlantesByClassificationIsEqualToSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);
        Classification classification = ClassificationResourceIT.createEntity(em);
        em.persist(classification);
        em.flush();
        plante.setClassification(classification);
        planteRepository.saveAndFlush(plante);
        Long classificationId = classification.getId();

        // Get all the planteList where classification equals to classificationId
        defaultPlanteShouldBeFound("classificationId.equals=" + classificationId);

        // Get all the planteList where classification equals to classificationId + 1
        defaultPlanteShouldNotBeFound("classificationId.equals=" + (classificationId + 1));
    }


    @Test
    @Transactional
    public void getAllPlantesByConfusionsIsEqualToSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);
        Ressemblance confusions = RessemblanceResourceIT.createEntity(em);
        em.persist(confusions);
        em.flush();
        plante.addConfusions(confusions);
        planteRepository.saveAndFlush(plante);
        Long confusionsId = confusions.getId();

        // Get all the planteList where confusions equals to confusionsId
        defaultPlanteShouldBeFound("confusionsId.equals=" + confusionsId);

        // Get all the planteList where confusions equals to confusionsId + 1
        defaultPlanteShouldNotBeFound("confusionsId.equals=" + (confusionsId + 1));
    }


    @Test
    @Transactional
    public void getAllPlantesByInteractionsIsEqualToSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);
        Allelopathie interactions = AllelopathieResourceIT.createEntity(em);
        em.persist(interactions);
        em.flush();
        plante.addInteractions(interactions);
        planteRepository.saveAndFlush(plante);
        Long interactionsId = interactions.getId();

        // Get all the planteList where interactions equals to interactionsId
        defaultPlanteShouldBeFound("interactionsId.equals=" + interactionsId);

        // Get all the planteList where interactions equals to interactionsId + 1
        defaultPlanteShouldNotBeFound("interactionsId.equals=" + (interactionsId + 1));
    }


    @Test
    @Transactional
    public void getAllPlantesByNomsVernaculairesIsEqualToSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);
        NomVernaculaire nomsVernaculaires = NomVernaculaireResourceIT.createEntity(em);
        em.persist(nomsVernaculaires);
        em.flush();
        plante.addNomsVernaculaires(nomsVernaculaires);
        planteRepository.saveAndFlush(plante);
        Long nomsVernaculairesId = nomsVernaculaires.getId();

        // Get all the planteList where nomsVernaculaires equals to nomsVernaculairesId
        defaultPlanteShouldBeFound("nomsVernaculairesId.equals=" + nomsVernaculairesId);

        // Get all the planteList where nomsVernaculaires equals to nomsVernaculairesId + 1
        defaultPlanteShouldNotBeFound("nomsVernaculairesId.equals=" + (nomsVernaculairesId + 1));
    }


    @Test
    @Transactional
    public void getAllPlantesByAllelopathieRecueIsEqualToSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);
        Allelopathie allelopathieRecue = AllelopathieResourceIT.createEntity(em);
        em.persist(allelopathieRecue);
        em.flush();
        plante.setAllelopathieRecue(allelopathieRecue);
        allelopathieRecue.setCible(plante);
        planteRepository.saveAndFlush(plante);
        Long allelopathieRecueId = allelopathieRecue.getId();

        // Get all the planteList where allelopathieRecue equals to allelopathieRecueId
        defaultPlanteShouldBeFound("allelopathieRecueId.equals=" + allelopathieRecueId);

        // Get all the planteList where allelopathieRecue equals to allelopathieRecueId + 1
        defaultPlanteShouldNotBeFound("allelopathieRecueId.equals=" + (allelopathieRecueId + 1));
    }


    @Test
    @Transactional
    public void getAllPlantesByAllelopathieProduiteIsEqualToSomething() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);
        Allelopathie allelopathieProduite = AllelopathieResourceIT.createEntity(em);
        em.persist(allelopathieProduite);
        em.flush();
        plante.setAllelopathieProduite(allelopathieProduite);
        allelopathieProduite.setOrigine(plante);
        planteRepository.saveAndFlush(plante);
        Long allelopathieProduiteId = allelopathieProduite.getId();

        // Get all the planteList where allelopathieProduite equals to allelopathieProduiteId
        defaultPlanteShouldBeFound("allelopathieProduiteId.equals=" + allelopathieProduiteId);

        // Get all the planteList where allelopathieProduite equals to allelopathieProduiteId + 1
        defaultPlanteShouldNotBeFound("allelopathieProduiteId.equals=" + (allelopathieProduiteId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPlanteShouldBeFound(String filter) throws Exception {
        restPlanteMockMvc.perform(get("/api/plantes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(plante.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)))
            .andExpect(jsonPath("$.[*].entretien").value(hasItem(DEFAULT_ENTRETIEN)))
            .andExpect(jsonPath("$.[*].histoire").value(hasItem(DEFAULT_HISTOIRE)))
            .andExpect(jsonPath("$.[*].exposition").value(hasItem(DEFAULT_EXPOSITION)))
            .andExpect(jsonPath("$.[*].rusticite").value(hasItem(DEFAULT_RUSTICITE)));

        // Check, that the count call also returns 1
        restPlanteMockMvc.perform(get("/api/plantes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPlanteShouldNotBeFound(String filter) throws Exception {
        restPlanteMockMvc.perform(get("/api/plantes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPlanteMockMvc.perform(get("/api/plantes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingPlante() throws Exception {
        // Get the plante
        restPlanteMockMvc.perform(get("/api/plantes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlante() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        int databaseSizeBeforeUpdate = planteRepository.findAll().size();

        // Update the plante
        Plante updatedPlante = planteRepository.findById(plante.getId()).get();
        // Disconnect from session so that the updates on updatedPlante are not directly saved in db
        em.detach(updatedPlante);
        updatedPlante
            .nomLatin(UPDATED_NOM_LATIN)
            .entretien(UPDATED_ENTRETIEN)
            .histoire(UPDATED_HISTOIRE)
            .exposition(UPDATED_EXPOSITION)
            .rusticite(UPDATED_RUSTICITE);
        PlanteDTO planteDTO = planteMapper.toDto(updatedPlante);

        restPlanteMockMvc.perform(put("/api/plantes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(planteDTO)))
            .andExpect(status().isOk());

        // Validate the Plante in the database
        List<Plante> planteList = planteRepository.findAll();
        assertThat(planteList).hasSize(databaseSizeBeforeUpdate);
        Plante testPlante = planteList.get(planteList.size() - 1);
        assertThat(testPlante.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
        assertThat(testPlante.getEntretien()).isEqualTo(UPDATED_ENTRETIEN);
        assertThat(testPlante.getHistoire()).isEqualTo(UPDATED_HISTOIRE);
        assertThat(testPlante.getExposition()).isEqualTo(UPDATED_EXPOSITION);
        assertThat(testPlante.getRusticite()).isEqualTo(UPDATED_RUSTICITE);
    }

    @Test
    @Transactional
    public void updateNonExistingPlante() throws Exception {
        int databaseSizeBeforeUpdate = planteRepository.findAll().size();

        // Create the Plante
        PlanteDTO planteDTO = planteMapper.toDto(plante);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanteMockMvc.perform(put("/api/plantes").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(planteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Plante in the database
        List<Plante> planteList = planteRepository.findAll();
        assertThat(planteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePlante() throws Exception {
        // Initialize the database
        planteRepository.saveAndFlush(plante);

        int databaseSizeBeforeDelete = planteRepository.findAll().size();

        // Delete the plante
        restPlanteMockMvc.perform(delete("/api/plantes/{id}", plante.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Plante> planteList = planteRepository.findAll();
        assertThat(planteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
