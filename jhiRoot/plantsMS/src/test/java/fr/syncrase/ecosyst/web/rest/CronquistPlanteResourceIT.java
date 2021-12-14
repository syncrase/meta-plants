package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.CronquistPlante;
import fr.syncrase.ecosyst.repository.CronquistPlanteRepository;
import fr.syncrase.ecosyst.service.criteria.CronquistPlanteCriteria;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CronquistPlanteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CronquistPlanteResourceIT {

    private static final String DEFAULT_SUPER_REGNE = "AAAAAAAAAA";
    private static final String UPDATED_SUPER_REGNE = "BBBBBBBBBB";

    private static final String DEFAULT_REGNE = "AAAAAAAAAA";
    private static final String UPDATED_REGNE = "BBBBBBBBBB";

    private static final String DEFAULT_SOUS_REGNE = "AAAAAAAAAA";
    private static final String UPDATED_SOUS_REGNE = "BBBBBBBBBB";

    private static final String DEFAULT_RAMEAU = "AAAAAAAAAA";
    private static final String UPDATED_RAMEAU = "BBBBBBBBBB";

    private static final String DEFAULT_INFRA_REGNE = "AAAAAAAAAA";
    private static final String UPDATED_INFRA_REGNE = "BBBBBBBBBB";

    private static final String DEFAULT_SUPER_DIVISION = "AAAAAAAAAA";
    private static final String UPDATED_SUPER_DIVISION = "BBBBBBBBBB";

    private static final String DEFAULT_DIVISION = "AAAAAAAAAA";
    private static final String UPDATED_DIVISION = "BBBBBBBBBB";

    private static final String DEFAULT_SOUS_DIVISION = "AAAAAAAAAA";
    private static final String UPDATED_SOUS_DIVISION = "BBBBBBBBBB";

    private static final String DEFAULT_INFRA_EMBRANCHEMENT = "AAAAAAAAAA";
    private static final String UPDATED_INFRA_EMBRANCHEMENT = "BBBBBBBBBB";

    private static final String DEFAULT_MICRO_EMBRANCHEMENT = "AAAAAAAAAA";
    private static final String UPDATED_MICRO_EMBRANCHEMENT = "BBBBBBBBBB";

    private static final String DEFAULT_SUPER_CLASSE = "AAAAAAAAAA";
    private static final String UPDATED_SUPER_CLASSE = "BBBBBBBBBB";

    private static final String DEFAULT_CLASSE = "AAAAAAAAAA";
    private static final String UPDATED_CLASSE = "BBBBBBBBBB";

    private static final String DEFAULT_SOUS_CLASSE = "AAAAAAAAAA";
    private static final String UPDATED_SOUS_CLASSE = "BBBBBBBBBB";

    private static final String DEFAULT_INFRA_CLASSE = "AAAAAAAAAA";
    private static final String UPDATED_INFRA_CLASSE = "BBBBBBBBBB";

    private static final String DEFAULT_SUPER_ORDRE = "AAAAAAAAAA";
    private static final String UPDATED_SUPER_ORDRE = "BBBBBBBBBB";

    private static final String DEFAULT_ORDRE = "AAAAAAAAAA";
    private static final String UPDATED_ORDRE = "BBBBBBBBBB";

    private static final String DEFAULT_SOUS_ORDRE = "AAAAAAAAAA";
    private static final String UPDATED_SOUS_ORDRE = "BBBBBBBBBB";

    private static final String DEFAULT_INFRA_ORDRE = "AAAAAAAAAA";
    private static final String UPDATED_INFRA_ORDRE = "BBBBBBBBBB";

    private static final String DEFAULT_MICRO_ORDRE = "AAAAAAAAAA";
    private static final String UPDATED_MICRO_ORDRE = "BBBBBBBBBB";

    private static final String DEFAULT_SUPER_FAMILLE = "AAAAAAAAAA";
    private static final String UPDATED_SUPER_FAMILLE = "BBBBBBBBBB";

    private static final String DEFAULT_FAMILLE = "AAAAAAAAAA";
    private static final String UPDATED_FAMILLE = "BBBBBBBBBB";

    private static final String DEFAULT_SOUS_FAMILLE = "AAAAAAAAAA";
    private static final String UPDATED_SOUS_FAMILLE = "BBBBBBBBBB";

    private static final String DEFAULT_TRIBU = "AAAAAAAAAA";
    private static final String UPDATED_TRIBU = "BBBBBBBBBB";

    private static final String DEFAULT_SOUS_TRIBU = "AAAAAAAAAA";
    private static final String UPDATED_SOUS_TRIBU = "BBBBBBBBBB";

    private static final String DEFAULT_GENRE = "AAAAAAAAAA";
    private static final String UPDATED_GENRE = "BBBBBBBBBB";

    private static final String DEFAULT_SOUS_GENRE = "AAAAAAAAAA";
    private static final String UPDATED_SOUS_GENRE = "BBBBBBBBBB";

    private static final String DEFAULT_SECTION = "AAAAAAAAAA";
    private static final String UPDATED_SECTION = "BBBBBBBBBB";

    private static final String DEFAULT_SOUS_SECTION = "AAAAAAAAAA";
    private static final String UPDATED_SOUS_SECTION = "BBBBBBBBBB";

    private static final String DEFAULT_ESPECE = "AAAAAAAAAA";
    private static final String UPDATED_ESPECE = "BBBBBBBBBB";

    private static final String DEFAULT_SOUS_ESPECE = "AAAAAAAAAA";
    private static final String UPDATED_SOUS_ESPECE = "BBBBBBBBBB";

    private static final String DEFAULT_VARIETE = "AAAAAAAAAA";
    private static final String UPDATED_VARIETE = "BBBBBBBBBB";

    private static final String DEFAULT_SOUS_VARIETE = "AAAAAAAAAA";
    private static final String UPDATED_SOUS_VARIETE = "BBBBBBBBBB";

    private static final String DEFAULT_FORME = "AAAAAAAAAA";
    private static final String UPDATED_FORME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cronquist-plantes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CronquistPlanteRepository cronquistPlanteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCronquistPlanteMockMvc;

    private CronquistPlante cronquistPlante;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CronquistPlante createEntity(EntityManager em) {
        CronquistPlante cronquistPlante = new CronquistPlante()
            .superRegne(DEFAULT_SUPER_REGNE)
            .regne(DEFAULT_REGNE)
            .sousRegne(DEFAULT_SOUS_REGNE)
            .rameau(DEFAULT_RAMEAU)
            .infraRegne(DEFAULT_INFRA_REGNE)
            .superDivision(DEFAULT_SUPER_DIVISION)
            .division(DEFAULT_DIVISION)
            .sousDivision(DEFAULT_SOUS_DIVISION)
            .infraEmbranchement(DEFAULT_INFRA_EMBRANCHEMENT)
            .microEmbranchement(DEFAULT_MICRO_EMBRANCHEMENT)
            .superClasse(DEFAULT_SUPER_CLASSE)
            .classe(DEFAULT_CLASSE)
            .sousClasse(DEFAULT_SOUS_CLASSE)
            .infraClasse(DEFAULT_INFRA_CLASSE)
            .superOrdre(DEFAULT_SUPER_ORDRE)
            .ordre(DEFAULT_ORDRE)
            .sousOrdre(DEFAULT_SOUS_ORDRE)
            .infraOrdre(DEFAULT_INFRA_ORDRE)
            .microOrdre(DEFAULT_MICRO_ORDRE)
            .superFamille(DEFAULT_SUPER_FAMILLE)
            .famille(DEFAULT_FAMILLE)
            .sousFamille(DEFAULT_SOUS_FAMILLE)
            .tribu(DEFAULT_TRIBU)
            .sousTribu(DEFAULT_SOUS_TRIBU)
            .genre(DEFAULT_GENRE)
            .sousGenre(DEFAULT_SOUS_GENRE)
            .section(DEFAULT_SECTION)
            .sousSection(DEFAULT_SOUS_SECTION)
            .espece(DEFAULT_ESPECE)
            .sousEspece(DEFAULT_SOUS_ESPECE)
            .variete(DEFAULT_VARIETE)
            .sousVariete(DEFAULT_SOUS_VARIETE)
            .forme(DEFAULT_FORME);
        return cronquistPlante;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CronquistPlante createUpdatedEntity(EntityManager em) {
        CronquistPlante cronquistPlante = new CronquistPlante()
            .superRegne(UPDATED_SUPER_REGNE)
            .regne(UPDATED_REGNE)
            .sousRegne(UPDATED_SOUS_REGNE)
            .rameau(UPDATED_RAMEAU)
            .infraRegne(UPDATED_INFRA_REGNE)
            .superDivision(UPDATED_SUPER_DIVISION)
            .division(UPDATED_DIVISION)
            .sousDivision(UPDATED_SOUS_DIVISION)
            .infraEmbranchement(UPDATED_INFRA_EMBRANCHEMENT)
            .microEmbranchement(UPDATED_MICRO_EMBRANCHEMENT)
            .superClasse(UPDATED_SUPER_CLASSE)
            .classe(UPDATED_CLASSE)
            .sousClasse(UPDATED_SOUS_CLASSE)
            .infraClasse(UPDATED_INFRA_CLASSE)
            .superOrdre(UPDATED_SUPER_ORDRE)
            .ordre(UPDATED_ORDRE)
            .sousOrdre(UPDATED_SOUS_ORDRE)
            .infraOrdre(UPDATED_INFRA_ORDRE)
            .microOrdre(UPDATED_MICRO_ORDRE)
            .superFamille(UPDATED_SUPER_FAMILLE)
            .famille(UPDATED_FAMILLE)
            .sousFamille(UPDATED_SOUS_FAMILLE)
            .tribu(UPDATED_TRIBU)
            .sousTribu(UPDATED_SOUS_TRIBU)
            .genre(UPDATED_GENRE)
            .sousGenre(UPDATED_SOUS_GENRE)
            .section(UPDATED_SECTION)
            .sousSection(UPDATED_SOUS_SECTION)
            .espece(UPDATED_ESPECE)
            .sousEspece(UPDATED_SOUS_ESPECE)
            .variete(UPDATED_VARIETE)
            .sousVariete(UPDATED_SOUS_VARIETE)
            .forme(UPDATED_FORME);
        return cronquistPlante;
    }

    @BeforeEach
    public void initTest() {
        cronquistPlante = createEntity(em);
    }

    @Test
    @Transactional
    void createCronquistPlante() throws Exception {
        int databaseSizeBeforeCreate = cronquistPlanteRepository.findAll().size();
        // Create the CronquistPlante
        restCronquistPlanteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cronquistPlante))
            )
            .andExpect(status().isCreated());

        // Validate the CronquistPlante in the database
        List<CronquistPlante> cronquistPlanteList = cronquistPlanteRepository.findAll();
        assertThat(cronquistPlanteList).hasSize(databaseSizeBeforeCreate + 1);
        CronquistPlante testCronquistPlante = cronquistPlanteList.get(cronquistPlanteList.size() - 1);
        assertThat(testCronquistPlante.getSuperRegne()).isEqualTo(DEFAULT_SUPER_REGNE);
        assertThat(testCronquistPlante.getRegne()).isEqualTo(DEFAULT_REGNE);
        assertThat(testCronquistPlante.getSousRegne()).isEqualTo(DEFAULT_SOUS_REGNE);
        assertThat(testCronquistPlante.getRameau()).isEqualTo(DEFAULT_RAMEAU);
        assertThat(testCronquistPlante.getInfraRegne()).isEqualTo(DEFAULT_INFRA_REGNE);
        assertThat(testCronquistPlante.getSuperDivision()).isEqualTo(DEFAULT_SUPER_DIVISION);
        assertThat(testCronquistPlante.getDivision()).isEqualTo(DEFAULT_DIVISION);
        assertThat(testCronquistPlante.getSousDivision()).isEqualTo(DEFAULT_SOUS_DIVISION);
        assertThat(testCronquistPlante.getInfraEmbranchement()).isEqualTo(DEFAULT_INFRA_EMBRANCHEMENT);
        assertThat(testCronquistPlante.getMicroEmbranchement()).isEqualTo(DEFAULT_MICRO_EMBRANCHEMENT);
        assertThat(testCronquistPlante.getSuperClasse()).isEqualTo(DEFAULT_SUPER_CLASSE);
        assertThat(testCronquistPlante.getClasse()).isEqualTo(DEFAULT_CLASSE);
        assertThat(testCronquistPlante.getSousClasse()).isEqualTo(DEFAULT_SOUS_CLASSE);
        assertThat(testCronquistPlante.getInfraClasse()).isEqualTo(DEFAULT_INFRA_CLASSE);
        assertThat(testCronquistPlante.getSuperOrdre()).isEqualTo(DEFAULT_SUPER_ORDRE);
        assertThat(testCronquistPlante.getOrdre()).isEqualTo(DEFAULT_ORDRE);
        assertThat(testCronquistPlante.getSousOrdre()).isEqualTo(DEFAULT_SOUS_ORDRE);
        assertThat(testCronquistPlante.getInfraOrdre()).isEqualTo(DEFAULT_INFRA_ORDRE);
        assertThat(testCronquistPlante.getMicroOrdre()).isEqualTo(DEFAULT_MICRO_ORDRE);
        assertThat(testCronquistPlante.getSuperFamille()).isEqualTo(DEFAULT_SUPER_FAMILLE);
        assertThat(testCronquistPlante.getFamille()).isEqualTo(DEFAULT_FAMILLE);
        assertThat(testCronquistPlante.getSousFamille()).isEqualTo(DEFAULT_SOUS_FAMILLE);
        assertThat(testCronquistPlante.getTribu()).isEqualTo(DEFAULT_TRIBU);
        assertThat(testCronquistPlante.getSousTribu()).isEqualTo(DEFAULT_SOUS_TRIBU);
        assertThat(testCronquistPlante.getGenre()).isEqualTo(DEFAULT_GENRE);
        assertThat(testCronquistPlante.getSousGenre()).isEqualTo(DEFAULT_SOUS_GENRE);
        assertThat(testCronquistPlante.getSection()).isEqualTo(DEFAULT_SECTION);
        assertThat(testCronquistPlante.getSousSection()).isEqualTo(DEFAULT_SOUS_SECTION);
        assertThat(testCronquistPlante.getEspece()).isEqualTo(DEFAULT_ESPECE);
        assertThat(testCronquistPlante.getSousEspece()).isEqualTo(DEFAULT_SOUS_ESPECE);
        assertThat(testCronquistPlante.getVariete()).isEqualTo(DEFAULT_VARIETE);
        assertThat(testCronquistPlante.getSousVariete()).isEqualTo(DEFAULT_SOUS_VARIETE);
        assertThat(testCronquistPlante.getForme()).isEqualTo(DEFAULT_FORME);
    }

    @Test
    @Transactional
    void createCronquistPlanteWithExistingId() throws Exception {
        // Create the CronquistPlante with an existing ID
        cronquistPlante.setId(1L);

        int databaseSizeBeforeCreate = cronquistPlanteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCronquistPlanteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cronquistPlante))
            )
            .andExpect(status().isBadRequest());

        // Validate the CronquistPlante in the database
        List<CronquistPlante> cronquistPlanteList = cronquistPlanteRepository.findAll();
        assertThat(cronquistPlanteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCronquistPlantes() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList
        restCronquistPlanteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cronquistPlante.getId().intValue())))
            .andExpect(jsonPath("$.[*].superRegne").value(hasItem(DEFAULT_SUPER_REGNE)))
            .andExpect(jsonPath("$.[*].regne").value(hasItem(DEFAULT_REGNE)))
            .andExpect(jsonPath("$.[*].sousRegne").value(hasItem(DEFAULT_SOUS_REGNE)))
            .andExpect(jsonPath("$.[*].rameau").value(hasItem(DEFAULT_RAMEAU)))
            .andExpect(jsonPath("$.[*].infraRegne").value(hasItem(DEFAULT_INFRA_REGNE)))
            .andExpect(jsonPath("$.[*].superDivision").value(hasItem(DEFAULT_SUPER_DIVISION)))
            .andExpect(jsonPath("$.[*].division").value(hasItem(DEFAULT_DIVISION)))
            .andExpect(jsonPath("$.[*].sousDivision").value(hasItem(DEFAULT_SOUS_DIVISION)))
            .andExpect(jsonPath("$.[*].infraEmbranchement").value(hasItem(DEFAULT_INFRA_EMBRANCHEMENT)))
            .andExpect(jsonPath("$.[*].microEmbranchement").value(hasItem(DEFAULT_MICRO_EMBRANCHEMENT)))
            .andExpect(jsonPath("$.[*].superClasse").value(hasItem(DEFAULT_SUPER_CLASSE)))
            .andExpect(jsonPath("$.[*].classe").value(hasItem(DEFAULT_CLASSE)))
            .andExpect(jsonPath("$.[*].sousClasse").value(hasItem(DEFAULT_SOUS_CLASSE)))
            .andExpect(jsonPath("$.[*].infraClasse").value(hasItem(DEFAULT_INFRA_CLASSE)))
            .andExpect(jsonPath("$.[*].superOrdre").value(hasItem(DEFAULT_SUPER_ORDRE)))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)))
            .andExpect(jsonPath("$.[*].sousOrdre").value(hasItem(DEFAULT_SOUS_ORDRE)))
            .andExpect(jsonPath("$.[*].infraOrdre").value(hasItem(DEFAULT_INFRA_ORDRE)))
            .andExpect(jsonPath("$.[*].microOrdre").value(hasItem(DEFAULT_MICRO_ORDRE)))
            .andExpect(jsonPath("$.[*].superFamille").value(hasItem(DEFAULT_SUPER_FAMILLE)))
            .andExpect(jsonPath("$.[*].famille").value(hasItem(DEFAULT_FAMILLE)))
            .andExpect(jsonPath("$.[*].sousFamille").value(hasItem(DEFAULT_SOUS_FAMILLE)))
            .andExpect(jsonPath("$.[*].tribu").value(hasItem(DEFAULT_TRIBU)))
            .andExpect(jsonPath("$.[*].sousTribu").value(hasItem(DEFAULT_SOUS_TRIBU)))
            .andExpect(jsonPath("$.[*].genre").value(hasItem(DEFAULT_GENRE)))
            .andExpect(jsonPath("$.[*].sousGenre").value(hasItem(DEFAULT_SOUS_GENRE)))
            .andExpect(jsonPath("$.[*].section").value(hasItem(DEFAULT_SECTION)))
            .andExpect(jsonPath("$.[*].sousSection").value(hasItem(DEFAULT_SOUS_SECTION)))
            .andExpect(jsonPath("$.[*].espece").value(hasItem(DEFAULT_ESPECE)))
            .andExpect(jsonPath("$.[*].sousEspece").value(hasItem(DEFAULT_SOUS_ESPECE)))
            .andExpect(jsonPath("$.[*].variete").value(hasItem(DEFAULT_VARIETE)))
            .andExpect(jsonPath("$.[*].sousVariete").value(hasItem(DEFAULT_SOUS_VARIETE)))
            .andExpect(jsonPath("$.[*].forme").value(hasItem(DEFAULT_FORME)));
    }

    @Test
    @Transactional
    void getCronquistPlante() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get the cronquistPlante
        restCronquistPlanteMockMvc
            .perform(get(ENTITY_API_URL_ID, cronquistPlante.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cronquistPlante.getId().intValue()))
            .andExpect(jsonPath("$.superRegne").value(DEFAULT_SUPER_REGNE))
            .andExpect(jsonPath("$.regne").value(DEFAULT_REGNE))
            .andExpect(jsonPath("$.sousRegne").value(DEFAULT_SOUS_REGNE))
            .andExpect(jsonPath("$.rameau").value(DEFAULT_RAMEAU))
            .andExpect(jsonPath("$.infraRegne").value(DEFAULT_INFRA_REGNE))
            .andExpect(jsonPath("$.superDivision").value(DEFAULT_SUPER_DIVISION))
            .andExpect(jsonPath("$.division").value(DEFAULT_DIVISION))
            .andExpect(jsonPath("$.sousDivision").value(DEFAULT_SOUS_DIVISION))
            .andExpect(jsonPath("$.infraEmbranchement").value(DEFAULT_INFRA_EMBRANCHEMENT))
            .andExpect(jsonPath("$.microEmbranchement").value(DEFAULT_MICRO_EMBRANCHEMENT))
            .andExpect(jsonPath("$.superClasse").value(DEFAULT_SUPER_CLASSE))
            .andExpect(jsonPath("$.classe").value(DEFAULT_CLASSE))
            .andExpect(jsonPath("$.sousClasse").value(DEFAULT_SOUS_CLASSE))
            .andExpect(jsonPath("$.infraClasse").value(DEFAULT_INFRA_CLASSE))
            .andExpect(jsonPath("$.superOrdre").value(DEFAULT_SUPER_ORDRE))
            .andExpect(jsonPath("$.ordre").value(DEFAULT_ORDRE))
            .andExpect(jsonPath("$.sousOrdre").value(DEFAULT_SOUS_ORDRE))
            .andExpect(jsonPath("$.infraOrdre").value(DEFAULT_INFRA_ORDRE))
            .andExpect(jsonPath("$.microOrdre").value(DEFAULT_MICRO_ORDRE))
            .andExpect(jsonPath("$.superFamille").value(DEFAULT_SUPER_FAMILLE))
            .andExpect(jsonPath("$.famille").value(DEFAULT_FAMILLE))
            .andExpect(jsonPath("$.sousFamille").value(DEFAULT_SOUS_FAMILLE))
            .andExpect(jsonPath("$.tribu").value(DEFAULT_TRIBU))
            .andExpect(jsonPath("$.sousTribu").value(DEFAULT_SOUS_TRIBU))
            .andExpect(jsonPath("$.genre").value(DEFAULT_GENRE))
            .andExpect(jsonPath("$.sousGenre").value(DEFAULT_SOUS_GENRE))
            .andExpect(jsonPath("$.section").value(DEFAULT_SECTION))
            .andExpect(jsonPath("$.sousSection").value(DEFAULT_SOUS_SECTION))
            .andExpect(jsonPath("$.espece").value(DEFAULT_ESPECE))
            .andExpect(jsonPath("$.sousEspece").value(DEFAULT_SOUS_ESPECE))
            .andExpect(jsonPath("$.variete").value(DEFAULT_VARIETE))
            .andExpect(jsonPath("$.sousVariete").value(DEFAULT_SOUS_VARIETE))
            .andExpect(jsonPath("$.forme").value(DEFAULT_FORME));
    }

    @Test
    @Transactional
    void getCronquistPlantesByIdFiltering() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        Long id = cronquistPlante.getId();

        defaultCronquistPlanteShouldBeFound("id.equals=" + id);
        defaultCronquistPlanteShouldNotBeFound("id.notEquals=" + id);

        defaultCronquistPlanteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCronquistPlanteShouldNotBeFound("id.greaterThan=" + id);

        defaultCronquistPlanteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCronquistPlanteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySuperRegneIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where superRegne equals to DEFAULT_SUPER_REGNE
        defaultCronquistPlanteShouldBeFound("superRegne.equals=" + DEFAULT_SUPER_REGNE);

        // Get all the cronquistPlanteList where superRegne equals to UPDATED_SUPER_REGNE
        defaultCronquistPlanteShouldNotBeFound("superRegne.equals=" + UPDATED_SUPER_REGNE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySuperRegneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where superRegne not equals to DEFAULT_SUPER_REGNE
        defaultCronquistPlanteShouldNotBeFound("superRegne.notEquals=" + DEFAULT_SUPER_REGNE);

        // Get all the cronquistPlanteList where superRegne not equals to UPDATED_SUPER_REGNE
        defaultCronquistPlanteShouldBeFound("superRegne.notEquals=" + UPDATED_SUPER_REGNE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySuperRegneIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where superRegne in DEFAULT_SUPER_REGNE or UPDATED_SUPER_REGNE
        defaultCronquistPlanteShouldBeFound("superRegne.in=" + DEFAULT_SUPER_REGNE + "," + UPDATED_SUPER_REGNE);

        // Get all the cronquistPlanteList where superRegne equals to UPDATED_SUPER_REGNE
        defaultCronquistPlanteShouldNotBeFound("superRegne.in=" + UPDATED_SUPER_REGNE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySuperRegneIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where superRegne is not null
        defaultCronquistPlanteShouldBeFound("superRegne.specified=true");

        // Get all the cronquistPlanteList where superRegne is null
        defaultCronquistPlanteShouldNotBeFound("superRegne.specified=false");
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySuperRegneContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where superRegne contains DEFAULT_SUPER_REGNE
        defaultCronquistPlanteShouldBeFound("superRegne.contains=" + DEFAULT_SUPER_REGNE);

        // Get all the cronquistPlanteList where superRegne contains UPDATED_SUPER_REGNE
        defaultCronquistPlanteShouldNotBeFound("superRegne.contains=" + UPDATED_SUPER_REGNE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySuperRegneNotContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where superRegne does not contain DEFAULT_SUPER_REGNE
        defaultCronquistPlanteShouldNotBeFound("superRegne.doesNotContain=" + DEFAULT_SUPER_REGNE);

        // Get all the cronquistPlanteList where superRegne does not contain UPDATED_SUPER_REGNE
        defaultCronquistPlanteShouldBeFound("superRegne.doesNotContain=" + UPDATED_SUPER_REGNE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByRegneIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where regne equals to DEFAULT_REGNE
        defaultCronquistPlanteShouldBeFound("regne.equals=" + DEFAULT_REGNE);

        // Get all the cronquistPlanteList where regne equals to UPDATED_REGNE
        defaultCronquistPlanteShouldNotBeFound("regne.equals=" + UPDATED_REGNE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByRegneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where regne not equals to DEFAULT_REGNE
        defaultCronquistPlanteShouldNotBeFound("regne.notEquals=" + DEFAULT_REGNE);

        // Get all the cronquistPlanteList where regne not equals to UPDATED_REGNE
        defaultCronquistPlanteShouldBeFound("regne.notEquals=" + UPDATED_REGNE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByRegneIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where regne in DEFAULT_REGNE or UPDATED_REGNE
        defaultCronquistPlanteShouldBeFound("regne.in=" + DEFAULT_REGNE + "," + UPDATED_REGNE);

        // Get all the cronquistPlanteList where regne equals to UPDATED_REGNE
        defaultCronquistPlanteShouldNotBeFound("regne.in=" + UPDATED_REGNE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByRegneIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where regne is not null
        defaultCronquistPlanteShouldBeFound("regne.specified=true");

        // Get all the cronquistPlanteList where regne is null
        defaultCronquistPlanteShouldNotBeFound("regne.specified=false");
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByRegneContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where regne contains DEFAULT_REGNE
        defaultCronquistPlanteShouldBeFound("regne.contains=" + DEFAULT_REGNE);

        // Get all the cronquistPlanteList where regne contains UPDATED_REGNE
        defaultCronquistPlanteShouldNotBeFound("regne.contains=" + UPDATED_REGNE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByRegneNotContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where regne does not contain DEFAULT_REGNE
        defaultCronquistPlanteShouldNotBeFound("regne.doesNotContain=" + DEFAULT_REGNE);

        // Get all the cronquistPlanteList where regne does not contain UPDATED_REGNE
        defaultCronquistPlanteShouldBeFound("regne.doesNotContain=" + UPDATED_REGNE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousRegneIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousRegne equals to DEFAULT_SOUS_REGNE
        defaultCronquistPlanteShouldBeFound("sousRegne.equals=" + DEFAULT_SOUS_REGNE);

        // Get all the cronquistPlanteList where sousRegne equals to UPDATED_SOUS_REGNE
        defaultCronquistPlanteShouldNotBeFound("sousRegne.equals=" + UPDATED_SOUS_REGNE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousRegneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousRegne not equals to DEFAULT_SOUS_REGNE
        defaultCronquistPlanteShouldNotBeFound("sousRegne.notEquals=" + DEFAULT_SOUS_REGNE);

        // Get all the cronquistPlanteList where sousRegne not equals to UPDATED_SOUS_REGNE
        defaultCronquistPlanteShouldBeFound("sousRegne.notEquals=" + UPDATED_SOUS_REGNE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousRegneIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousRegne in DEFAULT_SOUS_REGNE or UPDATED_SOUS_REGNE
        defaultCronquistPlanteShouldBeFound("sousRegne.in=" + DEFAULT_SOUS_REGNE + "," + UPDATED_SOUS_REGNE);

        // Get all the cronquistPlanteList where sousRegne equals to UPDATED_SOUS_REGNE
        defaultCronquistPlanteShouldNotBeFound("sousRegne.in=" + UPDATED_SOUS_REGNE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousRegneIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousRegne is not null
        defaultCronquistPlanteShouldBeFound("sousRegne.specified=true");

        // Get all the cronquistPlanteList where sousRegne is null
        defaultCronquistPlanteShouldNotBeFound("sousRegne.specified=false");
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousRegneContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousRegne contains DEFAULT_SOUS_REGNE
        defaultCronquistPlanteShouldBeFound("sousRegne.contains=" + DEFAULT_SOUS_REGNE);

        // Get all the cronquistPlanteList where sousRegne contains UPDATED_SOUS_REGNE
        defaultCronquistPlanteShouldNotBeFound("sousRegne.contains=" + UPDATED_SOUS_REGNE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousRegneNotContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousRegne does not contain DEFAULT_SOUS_REGNE
        defaultCronquistPlanteShouldNotBeFound("sousRegne.doesNotContain=" + DEFAULT_SOUS_REGNE);

        // Get all the cronquistPlanteList where sousRegne does not contain UPDATED_SOUS_REGNE
        defaultCronquistPlanteShouldBeFound("sousRegne.doesNotContain=" + UPDATED_SOUS_REGNE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByRameauIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where rameau equals to DEFAULT_RAMEAU
        defaultCronquistPlanteShouldBeFound("rameau.equals=" + DEFAULT_RAMEAU);

        // Get all the cronquistPlanteList where rameau equals to UPDATED_RAMEAU
        defaultCronquistPlanteShouldNotBeFound("rameau.equals=" + UPDATED_RAMEAU);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByRameauIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where rameau not equals to DEFAULT_RAMEAU
        defaultCronquistPlanteShouldNotBeFound("rameau.notEquals=" + DEFAULT_RAMEAU);

        // Get all the cronquistPlanteList where rameau not equals to UPDATED_RAMEAU
        defaultCronquistPlanteShouldBeFound("rameau.notEquals=" + UPDATED_RAMEAU);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByRameauIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where rameau in DEFAULT_RAMEAU or UPDATED_RAMEAU
        defaultCronquistPlanteShouldBeFound("rameau.in=" + DEFAULT_RAMEAU + "," + UPDATED_RAMEAU);

        // Get all the cronquistPlanteList where rameau equals to UPDATED_RAMEAU
        defaultCronquistPlanteShouldNotBeFound("rameau.in=" + UPDATED_RAMEAU);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByRameauIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where rameau is not null
        defaultCronquistPlanteShouldBeFound("rameau.specified=true");

        // Get all the cronquistPlanteList where rameau is null
        defaultCronquistPlanteShouldNotBeFound("rameau.specified=false");
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByRameauContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where rameau contains DEFAULT_RAMEAU
        defaultCronquistPlanteShouldBeFound("rameau.contains=" + DEFAULT_RAMEAU);

        // Get all the cronquistPlanteList where rameau contains UPDATED_RAMEAU
        defaultCronquistPlanteShouldNotBeFound("rameau.contains=" + UPDATED_RAMEAU);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByRameauNotContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where rameau does not contain DEFAULT_RAMEAU
        defaultCronquistPlanteShouldNotBeFound("rameau.doesNotContain=" + DEFAULT_RAMEAU);

        // Get all the cronquistPlanteList where rameau does not contain UPDATED_RAMEAU
        defaultCronquistPlanteShouldBeFound("rameau.doesNotContain=" + UPDATED_RAMEAU);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByInfraRegneIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where infraRegne equals to DEFAULT_INFRA_REGNE
        defaultCronquistPlanteShouldBeFound("infraRegne.equals=" + DEFAULT_INFRA_REGNE);

        // Get all the cronquistPlanteList where infraRegne equals to UPDATED_INFRA_REGNE
        defaultCronquistPlanteShouldNotBeFound("infraRegne.equals=" + UPDATED_INFRA_REGNE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByInfraRegneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where infraRegne not equals to DEFAULT_INFRA_REGNE
        defaultCronquistPlanteShouldNotBeFound("infraRegne.notEquals=" + DEFAULT_INFRA_REGNE);

        // Get all the cronquistPlanteList where infraRegne not equals to UPDATED_INFRA_REGNE
        defaultCronquistPlanteShouldBeFound("infraRegne.notEquals=" + UPDATED_INFRA_REGNE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByInfraRegneIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where infraRegne in DEFAULT_INFRA_REGNE or UPDATED_INFRA_REGNE
        defaultCronquistPlanteShouldBeFound("infraRegne.in=" + DEFAULT_INFRA_REGNE + "," + UPDATED_INFRA_REGNE);

        // Get all the cronquistPlanteList where infraRegne equals to UPDATED_INFRA_REGNE
        defaultCronquistPlanteShouldNotBeFound("infraRegne.in=" + UPDATED_INFRA_REGNE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByInfraRegneIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where infraRegne is not null
        defaultCronquistPlanteShouldBeFound("infraRegne.specified=true");

        // Get all the cronquistPlanteList where infraRegne is null
        defaultCronquistPlanteShouldNotBeFound("infraRegne.specified=false");
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByInfraRegneContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where infraRegne contains DEFAULT_INFRA_REGNE
        defaultCronquistPlanteShouldBeFound("infraRegne.contains=" + DEFAULT_INFRA_REGNE);

        // Get all the cronquistPlanteList where infraRegne contains UPDATED_INFRA_REGNE
        defaultCronquistPlanteShouldNotBeFound("infraRegne.contains=" + UPDATED_INFRA_REGNE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByInfraRegneNotContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where infraRegne does not contain DEFAULT_INFRA_REGNE
        defaultCronquistPlanteShouldNotBeFound("infraRegne.doesNotContain=" + DEFAULT_INFRA_REGNE);

        // Get all the cronquistPlanteList where infraRegne does not contain UPDATED_INFRA_REGNE
        defaultCronquistPlanteShouldBeFound("infraRegne.doesNotContain=" + UPDATED_INFRA_REGNE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySuperDivisionIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where superDivision equals to DEFAULT_SUPER_DIVISION
        defaultCronquistPlanteShouldBeFound("superDivision.equals=" + DEFAULT_SUPER_DIVISION);

        // Get all the cronquistPlanteList where superDivision equals to UPDATED_SUPER_DIVISION
        defaultCronquistPlanteShouldNotBeFound("superDivision.equals=" + UPDATED_SUPER_DIVISION);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySuperDivisionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where superDivision not equals to DEFAULT_SUPER_DIVISION
        defaultCronquistPlanteShouldNotBeFound("superDivision.notEquals=" + DEFAULT_SUPER_DIVISION);

        // Get all the cronquistPlanteList where superDivision not equals to UPDATED_SUPER_DIVISION
        defaultCronquistPlanteShouldBeFound("superDivision.notEquals=" + UPDATED_SUPER_DIVISION);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySuperDivisionIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where superDivision in DEFAULT_SUPER_DIVISION or UPDATED_SUPER_DIVISION
        defaultCronquistPlanteShouldBeFound("superDivision.in=" + DEFAULT_SUPER_DIVISION + "," + UPDATED_SUPER_DIVISION);

        // Get all the cronquistPlanteList where superDivision equals to UPDATED_SUPER_DIVISION
        defaultCronquistPlanteShouldNotBeFound("superDivision.in=" + UPDATED_SUPER_DIVISION);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySuperDivisionIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where superDivision is not null
        defaultCronquistPlanteShouldBeFound("superDivision.specified=true");

        // Get all the cronquistPlanteList where superDivision is null
        defaultCronquistPlanteShouldNotBeFound("superDivision.specified=false");
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySuperDivisionContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where superDivision contains DEFAULT_SUPER_DIVISION
        defaultCronquistPlanteShouldBeFound("superDivision.contains=" + DEFAULT_SUPER_DIVISION);

        // Get all the cronquistPlanteList where superDivision contains UPDATED_SUPER_DIVISION
        defaultCronquistPlanteShouldNotBeFound("superDivision.contains=" + UPDATED_SUPER_DIVISION);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySuperDivisionNotContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where superDivision does not contain DEFAULT_SUPER_DIVISION
        defaultCronquistPlanteShouldNotBeFound("superDivision.doesNotContain=" + DEFAULT_SUPER_DIVISION);

        // Get all the cronquistPlanteList where superDivision does not contain UPDATED_SUPER_DIVISION
        defaultCronquistPlanteShouldBeFound("superDivision.doesNotContain=" + UPDATED_SUPER_DIVISION);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByDivisionIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where division equals to DEFAULT_DIVISION
        defaultCronquistPlanteShouldBeFound("division.equals=" + DEFAULT_DIVISION);

        // Get all the cronquistPlanteList where division equals to UPDATED_DIVISION
        defaultCronquistPlanteShouldNotBeFound("division.equals=" + UPDATED_DIVISION);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByDivisionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where division not equals to DEFAULT_DIVISION
        defaultCronquistPlanteShouldNotBeFound("division.notEquals=" + DEFAULT_DIVISION);

        // Get all the cronquistPlanteList where division not equals to UPDATED_DIVISION
        defaultCronquistPlanteShouldBeFound("division.notEquals=" + UPDATED_DIVISION);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByDivisionIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where division in DEFAULT_DIVISION or UPDATED_DIVISION
        defaultCronquistPlanteShouldBeFound("division.in=" + DEFAULT_DIVISION + "," + UPDATED_DIVISION);

        // Get all the cronquistPlanteList where division equals to UPDATED_DIVISION
        defaultCronquistPlanteShouldNotBeFound("division.in=" + UPDATED_DIVISION);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByDivisionIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where division is not null
        defaultCronquistPlanteShouldBeFound("division.specified=true");

        // Get all the cronquistPlanteList where division is null
        defaultCronquistPlanteShouldNotBeFound("division.specified=false");
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByDivisionContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where division contains DEFAULT_DIVISION
        defaultCronquistPlanteShouldBeFound("division.contains=" + DEFAULT_DIVISION);

        // Get all the cronquistPlanteList where division contains UPDATED_DIVISION
        defaultCronquistPlanteShouldNotBeFound("division.contains=" + UPDATED_DIVISION);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByDivisionNotContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where division does not contain DEFAULT_DIVISION
        defaultCronquistPlanteShouldNotBeFound("division.doesNotContain=" + DEFAULT_DIVISION);

        // Get all the cronquistPlanteList where division does not contain UPDATED_DIVISION
        defaultCronquistPlanteShouldBeFound("division.doesNotContain=" + UPDATED_DIVISION);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousDivisionIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousDivision equals to DEFAULT_SOUS_DIVISION
        defaultCronquistPlanteShouldBeFound("sousDivision.equals=" + DEFAULT_SOUS_DIVISION);

        // Get all the cronquistPlanteList where sousDivision equals to UPDATED_SOUS_DIVISION
        defaultCronquistPlanteShouldNotBeFound("sousDivision.equals=" + UPDATED_SOUS_DIVISION);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousDivisionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousDivision not equals to DEFAULT_SOUS_DIVISION
        defaultCronquistPlanteShouldNotBeFound("sousDivision.notEquals=" + DEFAULT_SOUS_DIVISION);

        // Get all the cronquistPlanteList where sousDivision not equals to UPDATED_SOUS_DIVISION
        defaultCronquistPlanteShouldBeFound("sousDivision.notEquals=" + UPDATED_SOUS_DIVISION);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousDivisionIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousDivision in DEFAULT_SOUS_DIVISION or UPDATED_SOUS_DIVISION
        defaultCronquistPlanteShouldBeFound("sousDivision.in=" + DEFAULT_SOUS_DIVISION + "," + UPDATED_SOUS_DIVISION);

        // Get all the cronquistPlanteList where sousDivision equals to UPDATED_SOUS_DIVISION
        defaultCronquistPlanteShouldNotBeFound("sousDivision.in=" + UPDATED_SOUS_DIVISION);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousDivisionIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousDivision is not null
        defaultCronquistPlanteShouldBeFound("sousDivision.specified=true");

        // Get all the cronquistPlanteList where sousDivision is null
        defaultCronquistPlanteShouldNotBeFound("sousDivision.specified=false");
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousDivisionContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousDivision contains DEFAULT_SOUS_DIVISION
        defaultCronquistPlanteShouldBeFound("sousDivision.contains=" + DEFAULT_SOUS_DIVISION);

        // Get all the cronquistPlanteList where sousDivision contains UPDATED_SOUS_DIVISION
        defaultCronquistPlanteShouldNotBeFound("sousDivision.contains=" + UPDATED_SOUS_DIVISION);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousDivisionNotContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousDivision does not contain DEFAULT_SOUS_DIVISION
        defaultCronquistPlanteShouldNotBeFound("sousDivision.doesNotContain=" + DEFAULT_SOUS_DIVISION);

        // Get all the cronquistPlanteList where sousDivision does not contain UPDATED_SOUS_DIVISION
        defaultCronquistPlanteShouldBeFound("sousDivision.doesNotContain=" + UPDATED_SOUS_DIVISION);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByInfraEmbranchementIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where infraEmbranchement equals to DEFAULT_INFRA_EMBRANCHEMENT
        defaultCronquistPlanteShouldBeFound("infraEmbranchement.equals=" + DEFAULT_INFRA_EMBRANCHEMENT);

        // Get all the cronquistPlanteList where infraEmbranchement equals to UPDATED_INFRA_EMBRANCHEMENT
        defaultCronquistPlanteShouldNotBeFound("infraEmbranchement.equals=" + UPDATED_INFRA_EMBRANCHEMENT);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByInfraEmbranchementIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where infraEmbranchement not equals to DEFAULT_INFRA_EMBRANCHEMENT
        defaultCronquistPlanteShouldNotBeFound("infraEmbranchement.notEquals=" + DEFAULT_INFRA_EMBRANCHEMENT);

        // Get all the cronquistPlanteList where infraEmbranchement not equals to UPDATED_INFRA_EMBRANCHEMENT
        defaultCronquistPlanteShouldBeFound("infraEmbranchement.notEquals=" + UPDATED_INFRA_EMBRANCHEMENT);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByInfraEmbranchementIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where infraEmbranchement in DEFAULT_INFRA_EMBRANCHEMENT or UPDATED_INFRA_EMBRANCHEMENT
        defaultCronquistPlanteShouldBeFound("infraEmbranchement.in=" + DEFAULT_INFRA_EMBRANCHEMENT + "," + UPDATED_INFRA_EMBRANCHEMENT);

        // Get all the cronquistPlanteList where infraEmbranchement equals to UPDATED_INFRA_EMBRANCHEMENT
        defaultCronquistPlanteShouldNotBeFound("infraEmbranchement.in=" + UPDATED_INFRA_EMBRANCHEMENT);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByInfraEmbranchementIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where infraEmbranchement is not null
        defaultCronquistPlanteShouldBeFound("infraEmbranchement.specified=true");

        // Get all the cronquistPlanteList where infraEmbranchement is null
        defaultCronquistPlanteShouldNotBeFound("infraEmbranchement.specified=false");
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByInfraEmbranchementContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where infraEmbranchement contains DEFAULT_INFRA_EMBRANCHEMENT
        defaultCronquistPlanteShouldBeFound("infraEmbranchement.contains=" + DEFAULT_INFRA_EMBRANCHEMENT);

        // Get all the cronquistPlanteList where infraEmbranchement contains UPDATED_INFRA_EMBRANCHEMENT
        defaultCronquistPlanteShouldNotBeFound("infraEmbranchement.contains=" + UPDATED_INFRA_EMBRANCHEMENT);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByInfraEmbranchementNotContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where infraEmbranchement does not contain DEFAULT_INFRA_EMBRANCHEMENT
        defaultCronquistPlanteShouldNotBeFound("infraEmbranchement.doesNotContain=" + DEFAULT_INFRA_EMBRANCHEMENT);

        // Get all the cronquistPlanteList where infraEmbranchement does not contain UPDATED_INFRA_EMBRANCHEMENT
        defaultCronquistPlanteShouldBeFound("infraEmbranchement.doesNotContain=" + UPDATED_INFRA_EMBRANCHEMENT);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByMicroEmbranchementIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where microEmbranchement equals to DEFAULT_MICRO_EMBRANCHEMENT
        defaultCronquistPlanteShouldBeFound("microEmbranchement.equals=" + DEFAULT_MICRO_EMBRANCHEMENT);

        // Get all the cronquistPlanteList where microEmbranchement equals to UPDATED_MICRO_EMBRANCHEMENT
        defaultCronquistPlanteShouldNotBeFound("microEmbranchement.equals=" + UPDATED_MICRO_EMBRANCHEMENT);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByMicroEmbranchementIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where microEmbranchement not equals to DEFAULT_MICRO_EMBRANCHEMENT
        defaultCronquistPlanteShouldNotBeFound("microEmbranchement.notEquals=" + DEFAULT_MICRO_EMBRANCHEMENT);

        // Get all the cronquistPlanteList where microEmbranchement not equals to UPDATED_MICRO_EMBRANCHEMENT
        defaultCronquistPlanteShouldBeFound("microEmbranchement.notEquals=" + UPDATED_MICRO_EMBRANCHEMENT);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByMicroEmbranchementIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where microEmbranchement in DEFAULT_MICRO_EMBRANCHEMENT or UPDATED_MICRO_EMBRANCHEMENT
        defaultCronquistPlanteShouldBeFound("microEmbranchement.in=" + DEFAULT_MICRO_EMBRANCHEMENT + "," + UPDATED_MICRO_EMBRANCHEMENT);

        // Get all the cronquistPlanteList where microEmbranchement equals to UPDATED_MICRO_EMBRANCHEMENT
        defaultCronquistPlanteShouldNotBeFound("microEmbranchement.in=" + UPDATED_MICRO_EMBRANCHEMENT);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByMicroEmbranchementIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where microEmbranchement is not null
        defaultCronquistPlanteShouldBeFound("microEmbranchement.specified=true");

        // Get all the cronquistPlanteList where microEmbranchement is null
        defaultCronquistPlanteShouldNotBeFound("microEmbranchement.specified=false");
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByMicroEmbranchementContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where microEmbranchement contains DEFAULT_MICRO_EMBRANCHEMENT
        defaultCronquistPlanteShouldBeFound("microEmbranchement.contains=" + DEFAULT_MICRO_EMBRANCHEMENT);

        // Get all the cronquistPlanteList where microEmbranchement contains UPDATED_MICRO_EMBRANCHEMENT
        defaultCronquistPlanteShouldNotBeFound("microEmbranchement.contains=" + UPDATED_MICRO_EMBRANCHEMENT);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByMicroEmbranchementNotContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where microEmbranchement does not contain DEFAULT_MICRO_EMBRANCHEMENT
        defaultCronquistPlanteShouldNotBeFound("microEmbranchement.doesNotContain=" + DEFAULT_MICRO_EMBRANCHEMENT);

        // Get all the cronquistPlanteList where microEmbranchement does not contain UPDATED_MICRO_EMBRANCHEMENT
        defaultCronquistPlanteShouldBeFound("microEmbranchement.doesNotContain=" + UPDATED_MICRO_EMBRANCHEMENT);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySuperClasseIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where superClasse equals to DEFAULT_SUPER_CLASSE
        defaultCronquistPlanteShouldBeFound("superClasse.equals=" + DEFAULT_SUPER_CLASSE);

        // Get all the cronquistPlanteList where superClasse equals to UPDATED_SUPER_CLASSE
        defaultCronquistPlanteShouldNotBeFound("superClasse.equals=" + UPDATED_SUPER_CLASSE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySuperClasseIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where superClasse not equals to DEFAULT_SUPER_CLASSE
        defaultCronquistPlanteShouldNotBeFound("superClasse.notEquals=" + DEFAULT_SUPER_CLASSE);

        // Get all the cronquistPlanteList where superClasse not equals to UPDATED_SUPER_CLASSE
        defaultCronquistPlanteShouldBeFound("superClasse.notEquals=" + UPDATED_SUPER_CLASSE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySuperClasseIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where superClasse in DEFAULT_SUPER_CLASSE or UPDATED_SUPER_CLASSE
        defaultCronquistPlanteShouldBeFound("superClasse.in=" + DEFAULT_SUPER_CLASSE + "," + UPDATED_SUPER_CLASSE);

        // Get all the cronquistPlanteList where superClasse equals to UPDATED_SUPER_CLASSE
        defaultCronquistPlanteShouldNotBeFound("superClasse.in=" + UPDATED_SUPER_CLASSE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySuperClasseIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where superClasse is not null
        defaultCronquistPlanteShouldBeFound("superClasse.specified=true");

        // Get all the cronquistPlanteList where superClasse is null
        defaultCronquistPlanteShouldNotBeFound("superClasse.specified=false");
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySuperClasseContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where superClasse contains DEFAULT_SUPER_CLASSE
        defaultCronquistPlanteShouldBeFound("superClasse.contains=" + DEFAULT_SUPER_CLASSE);

        // Get all the cronquistPlanteList where superClasse contains UPDATED_SUPER_CLASSE
        defaultCronquistPlanteShouldNotBeFound("superClasse.contains=" + UPDATED_SUPER_CLASSE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySuperClasseNotContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where superClasse does not contain DEFAULT_SUPER_CLASSE
        defaultCronquistPlanteShouldNotBeFound("superClasse.doesNotContain=" + DEFAULT_SUPER_CLASSE);

        // Get all the cronquistPlanteList where superClasse does not contain UPDATED_SUPER_CLASSE
        defaultCronquistPlanteShouldBeFound("superClasse.doesNotContain=" + UPDATED_SUPER_CLASSE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByClasseIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where classe equals to DEFAULT_CLASSE
        defaultCronquistPlanteShouldBeFound("classe.equals=" + DEFAULT_CLASSE);

        // Get all the cronquistPlanteList where classe equals to UPDATED_CLASSE
        defaultCronquistPlanteShouldNotBeFound("classe.equals=" + UPDATED_CLASSE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByClasseIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where classe not equals to DEFAULT_CLASSE
        defaultCronquistPlanteShouldNotBeFound("classe.notEquals=" + DEFAULT_CLASSE);

        // Get all the cronquistPlanteList where classe not equals to UPDATED_CLASSE
        defaultCronquistPlanteShouldBeFound("classe.notEquals=" + UPDATED_CLASSE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByClasseIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where classe in DEFAULT_CLASSE or UPDATED_CLASSE
        defaultCronquistPlanteShouldBeFound("classe.in=" + DEFAULT_CLASSE + "," + UPDATED_CLASSE);

        // Get all the cronquistPlanteList where classe equals to UPDATED_CLASSE
        defaultCronquistPlanteShouldNotBeFound("classe.in=" + UPDATED_CLASSE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByClasseIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where classe is not null
        defaultCronquistPlanteShouldBeFound("classe.specified=true");

        // Get all the cronquistPlanteList where classe is null
        defaultCronquistPlanteShouldNotBeFound("classe.specified=false");
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByClasseContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where classe contains DEFAULT_CLASSE
        defaultCronquistPlanteShouldBeFound("classe.contains=" + DEFAULT_CLASSE);

        // Get all the cronquistPlanteList where classe contains UPDATED_CLASSE
        defaultCronquistPlanteShouldNotBeFound("classe.contains=" + UPDATED_CLASSE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByClasseNotContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where classe does not contain DEFAULT_CLASSE
        defaultCronquistPlanteShouldNotBeFound("classe.doesNotContain=" + DEFAULT_CLASSE);

        // Get all the cronquistPlanteList where classe does not contain UPDATED_CLASSE
        defaultCronquistPlanteShouldBeFound("classe.doesNotContain=" + UPDATED_CLASSE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousClasseIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousClasse equals to DEFAULT_SOUS_CLASSE
        defaultCronquistPlanteShouldBeFound("sousClasse.equals=" + DEFAULT_SOUS_CLASSE);

        // Get all the cronquistPlanteList where sousClasse equals to UPDATED_SOUS_CLASSE
        defaultCronquistPlanteShouldNotBeFound("sousClasse.equals=" + UPDATED_SOUS_CLASSE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousClasseIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousClasse not equals to DEFAULT_SOUS_CLASSE
        defaultCronquistPlanteShouldNotBeFound("sousClasse.notEquals=" + DEFAULT_SOUS_CLASSE);

        // Get all the cronquistPlanteList where sousClasse not equals to UPDATED_SOUS_CLASSE
        defaultCronquistPlanteShouldBeFound("sousClasse.notEquals=" + UPDATED_SOUS_CLASSE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousClasseIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousClasse in DEFAULT_SOUS_CLASSE or UPDATED_SOUS_CLASSE
        defaultCronquistPlanteShouldBeFound("sousClasse.in=" + DEFAULT_SOUS_CLASSE + "," + UPDATED_SOUS_CLASSE);

        // Get all the cronquistPlanteList where sousClasse equals to UPDATED_SOUS_CLASSE
        defaultCronquistPlanteShouldNotBeFound("sousClasse.in=" + UPDATED_SOUS_CLASSE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousClasseIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousClasse is not null
        defaultCronquistPlanteShouldBeFound("sousClasse.specified=true");

        // Get all the cronquistPlanteList where sousClasse is null
        defaultCronquistPlanteShouldNotBeFound("sousClasse.specified=false");
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousClasseContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousClasse contains DEFAULT_SOUS_CLASSE
        defaultCronquistPlanteShouldBeFound("sousClasse.contains=" + DEFAULT_SOUS_CLASSE);

        // Get all the cronquistPlanteList where sousClasse contains UPDATED_SOUS_CLASSE
        defaultCronquistPlanteShouldNotBeFound("sousClasse.contains=" + UPDATED_SOUS_CLASSE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousClasseNotContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousClasse does not contain DEFAULT_SOUS_CLASSE
        defaultCronquistPlanteShouldNotBeFound("sousClasse.doesNotContain=" + DEFAULT_SOUS_CLASSE);

        // Get all the cronquistPlanteList where sousClasse does not contain UPDATED_SOUS_CLASSE
        defaultCronquistPlanteShouldBeFound("sousClasse.doesNotContain=" + UPDATED_SOUS_CLASSE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByInfraClasseIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where infraClasse equals to DEFAULT_INFRA_CLASSE
        defaultCronquistPlanteShouldBeFound("infraClasse.equals=" + DEFAULT_INFRA_CLASSE);

        // Get all the cronquistPlanteList where infraClasse equals to UPDATED_INFRA_CLASSE
        defaultCronquistPlanteShouldNotBeFound("infraClasse.equals=" + UPDATED_INFRA_CLASSE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByInfraClasseIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where infraClasse not equals to DEFAULT_INFRA_CLASSE
        defaultCronquistPlanteShouldNotBeFound("infraClasse.notEquals=" + DEFAULT_INFRA_CLASSE);

        // Get all the cronquistPlanteList where infraClasse not equals to UPDATED_INFRA_CLASSE
        defaultCronquistPlanteShouldBeFound("infraClasse.notEquals=" + UPDATED_INFRA_CLASSE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByInfraClasseIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where infraClasse in DEFAULT_INFRA_CLASSE or UPDATED_INFRA_CLASSE
        defaultCronquistPlanteShouldBeFound("infraClasse.in=" + DEFAULT_INFRA_CLASSE + "," + UPDATED_INFRA_CLASSE);

        // Get all the cronquistPlanteList where infraClasse equals to UPDATED_INFRA_CLASSE
        defaultCronquistPlanteShouldNotBeFound("infraClasse.in=" + UPDATED_INFRA_CLASSE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByInfraClasseIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where infraClasse is not null
        defaultCronquistPlanteShouldBeFound("infraClasse.specified=true");

        // Get all the cronquistPlanteList where infraClasse is null
        defaultCronquistPlanteShouldNotBeFound("infraClasse.specified=false");
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByInfraClasseContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where infraClasse contains DEFAULT_INFRA_CLASSE
        defaultCronquistPlanteShouldBeFound("infraClasse.contains=" + DEFAULT_INFRA_CLASSE);

        // Get all the cronquistPlanteList where infraClasse contains UPDATED_INFRA_CLASSE
        defaultCronquistPlanteShouldNotBeFound("infraClasse.contains=" + UPDATED_INFRA_CLASSE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByInfraClasseNotContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where infraClasse does not contain DEFAULT_INFRA_CLASSE
        defaultCronquistPlanteShouldNotBeFound("infraClasse.doesNotContain=" + DEFAULT_INFRA_CLASSE);

        // Get all the cronquistPlanteList where infraClasse does not contain UPDATED_INFRA_CLASSE
        defaultCronquistPlanteShouldBeFound("infraClasse.doesNotContain=" + UPDATED_INFRA_CLASSE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySuperOrdreIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where superOrdre equals to DEFAULT_SUPER_ORDRE
        defaultCronquistPlanteShouldBeFound("superOrdre.equals=" + DEFAULT_SUPER_ORDRE);

        // Get all the cronquistPlanteList where superOrdre equals to UPDATED_SUPER_ORDRE
        defaultCronquistPlanteShouldNotBeFound("superOrdre.equals=" + UPDATED_SUPER_ORDRE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySuperOrdreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where superOrdre not equals to DEFAULT_SUPER_ORDRE
        defaultCronquistPlanteShouldNotBeFound("superOrdre.notEquals=" + DEFAULT_SUPER_ORDRE);

        // Get all the cronquistPlanteList where superOrdre not equals to UPDATED_SUPER_ORDRE
        defaultCronquistPlanteShouldBeFound("superOrdre.notEquals=" + UPDATED_SUPER_ORDRE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySuperOrdreIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where superOrdre in DEFAULT_SUPER_ORDRE or UPDATED_SUPER_ORDRE
        defaultCronquistPlanteShouldBeFound("superOrdre.in=" + DEFAULT_SUPER_ORDRE + "," + UPDATED_SUPER_ORDRE);

        // Get all the cronquistPlanteList where superOrdre equals to UPDATED_SUPER_ORDRE
        defaultCronquistPlanteShouldNotBeFound("superOrdre.in=" + UPDATED_SUPER_ORDRE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySuperOrdreIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where superOrdre is not null
        defaultCronquistPlanteShouldBeFound("superOrdre.specified=true");

        // Get all the cronquistPlanteList where superOrdre is null
        defaultCronquistPlanteShouldNotBeFound("superOrdre.specified=false");
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySuperOrdreContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where superOrdre contains DEFAULT_SUPER_ORDRE
        defaultCronquistPlanteShouldBeFound("superOrdre.contains=" + DEFAULT_SUPER_ORDRE);

        // Get all the cronquistPlanteList where superOrdre contains UPDATED_SUPER_ORDRE
        defaultCronquistPlanteShouldNotBeFound("superOrdre.contains=" + UPDATED_SUPER_ORDRE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySuperOrdreNotContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where superOrdre does not contain DEFAULT_SUPER_ORDRE
        defaultCronquistPlanteShouldNotBeFound("superOrdre.doesNotContain=" + DEFAULT_SUPER_ORDRE);

        // Get all the cronquistPlanteList where superOrdre does not contain UPDATED_SUPER_ORDRE
        defaultCronquistPlanteShouldBeFound("superOrdre.doesNotContain=" + UPDATED_SUPER_ORDRE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByOrdreIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where ordre equals to DEFAULT_ORDRE
        defaultCronquistPlanteShouldBeFound("ordre.equals=" + DEFAULT_ORDRE);

        // Get all the cronquistPlanteList where ordre equals to UPDATED_ORDRE
        defaultCronquistPlanteShouldNotBeFound("ordre.equals=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByOrdreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where ordre not equals to DEFAULT_ORDRE
        defaultCronquistPlanteShouldNotBeFound("ordre.notEquals=" + DEFAULT_ORDRE);

        // Get all the cronquistPlanteList where ordre not equals to UPDATED_ORDRE
        defaultCronquistPlanteShouldBeFound("ordre.notEquals=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByOrdreIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where ordre in DEFAULT_ORDRE or UPDATED_ORDRE
        defaultCronquistPlanteShouldBeFound("ordre.in=" + DEFAULT_ORDRE + "," + UPDATED_ORDRE);

        // Get all the cronquistPlanteList where ordre equals to UPDATED_ORDRE
        defaultCronquistPlanteShouldNotBeFound("ordre.in=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByOrdreIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where ordre is not null
        defaultCronquistPlanteShouldBeFound("ordre.specified=true");

        // Get all the cronquistPlanteList where ordre is null
        defaultCronquistPlanteShouldNotBeFound("ordre.specified=false");
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByOrdreContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where ordre contains DEFAULT_ORDRE
        defaultCronquistPlanteShouldBeFound("ordre.contains=" + DEFAULT_ORDRE);

        // Get all the cronquistPlanteList where ordre contains UPDATED_ORDRE
        defaultCronquistPlanteShouldNotBeFound("ordre.contains=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByOrdreNotContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where ordre does not contain DEFAULT_ORDRE
        defaultCronquistPlanteShouldNotBeFound("ordre.doesNotContain=" + DEFAULT_ORDRE);

        // Get all the cronquistPlanteList where ordre does not contain UPDATED_ORDRE
        defaultCronquistPlanteShouldBeFound("ordre.doesNotContain=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousOrdreIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousOrdre equals to DEFAULT_SOUS_ORDRE
        defaultCronquistPlanteShouldBeFound("sousOrdre.equals=" + DEFAULT_SOUS_ORDRE);

        // Get all the cronquistPlanteList where sousOrdre equals to UPDATED_SOUS_ORDRE
        defaultCronquistPlanteShouldNotBeFound("sousOrdre.equals=" + UPDATED_SOUS_ORDRE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousOrdreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousOrdre not equals to DEFAULT_SOUS_ORDRE
        defaultCronquistPlanteShouldNotBeFound("sousOrdre.notEquals=" + DEFAULT_SOUS_ORDRE);

        // Get all the cronquistPlanteList where sousOrdre not equals to UPDATED_SOUS_ORDRE
        defaultCronquistPlanteShouldBeFound("sousOrdre.notEquals=" + UPDATED_SOUS_ORDRE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousOrdreIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousOrdre in DEFAULT_SOUS_ORDRE or UPDATED_SOUS_ORDRE
        defaultCronquistPlanteShouldBeFound("sousOrdre.in=" + DEFAULT_SOUS_ORDRE + "," + UPDATED_SOUS_ORDRE);

        // Get all the cronquistPlanteList where sousOrdre equals to UPDATED_SOUS_ORDRE
        defaultCronquistPlanteShouldNotBeFound("sousOrdre.in=" + UPDATED_SOUS_ORDRE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousOrdreIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousOrdre is not null
        defaultCronquistPlanteShouldBeFound("sousOrdre.specified=true");

        // Get all the cronquistPlanteList where sousOrdre is null
        defaultCronquistPlanteShouldNotBeFound("sousOrdre.specified=false");
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousOrdreContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousOrdre contains DEFAULT_SOUS_ORDRE
        defaultCronquistPlanteShouldBeFound("sousOrdre.contains=" + DEFAULT_SOUS_ORDRE);

        // Get all the cronquistPlanteList where sousOrdre contains UPDATED_SOUS_ORDRE
        defaultCronquistPlanteShouldNotBeFound("sousOrdre.contains=" + UPDATED_SOUS_ORDRE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousOrdreNotContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousOrdre does not contain DEFAULT_SOUS_ORDRE
        defaultCronquistPlanteShouldNotBeFound("sousOrdre.doesNotContain=" + DEFAULT_SOUS_ORDRE);

        // Get all the cronquistPlanteList where sousOrdre does not contain UPDATED_SOUS_ORDRE
        defaultCronquistPlanteShouldBeFound("sousOrdre.doesNotContain=" + UPDATED_SOUS_ORDRE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByInfraOrdreIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where infraOrdre equals to DEFAULT_INFRA_ORDRE
        defaultCronquistPlanteShouldBeFound("infraOrdre.equals=" + DEFAULT_INFRA_ORDRE);

        // Get all the cronquistPlanteList where infraOrdre equals to UPDATED_INFRA_ORDRE
        defaultCronquistPlanteShouldNotBeFound("infraOrdre.equals=" + UPDATED_INFRA_ORDRE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByInfraOrdreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where infraOrdre not equals to DEFAULT_INFRA_ORDRE
        defaultCronquistPlanteShouldNotBeFound("infraOrdre.notEquals=" + DEFAULT_INFRA_ORDRE);

        // Get all the cronquistPlanteList where infraOrdre not equals to UPDATED_INFRA_ORDRE
        defaultCronquistPlanteShouldBeFound("infraOrdre.notEquals=" + UPDATED_INFRA_ORDRE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByInfraOrdreIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where infraOrdre in DEFAULT_INFRA_ORDRE or UPDATED_INFRA_ORDRE
        defaultCronquistPlanteShouldBeFound("infraOrdre.in=" + DEFAULT_INFRA_ORDRE + "," + UPDATED_INFRA_ORDRE);

        // Get all the cronquistPlanteList where infraOrdre equals to UPDATED_INFRA_ORDRE
        defaultCronquistPlanteShouldNotBeFound("infraOrdre.in=" + UPDATED_INFRA_ORDRE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByInfraOrdreIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where infraOrdre is not null
        defaultCronquistPlanteShouldBeFound("infraOrdre.specified=true");

        // Get all the cronquistPlanteList where infraOrdre is null
        defaultCronquistPlanteShouldNotBeFound("infraOrdre.specified=false");
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByInfraOrdreContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where infraOrdre contains DEFAULT_INFRA_ORDRE
        defaultCronquistPlanteShouldBeFound("infraOrdre.contains=" + DEFAULT_INFRA_ORDRE);

        // Get all the cronquistPlanteList where infraOrdre contains UPDATED_INFRA_ORDRE
        defaultCronquistPlanteShouldNotBeFound("infraOrdre.contains=" + UPDATED_INFRA_ORDRE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByInfraOrdreNotContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where infraOrdre does not contain DEFAULT_INFRA_ORDRE
        defaultCronquistPlanteShouldNotBeFound("infraOrdre.doesNotContain=" + DEFAULT_INFRA_ORDRE);

        // Get all the cronquistPlanteList where infraOrdre does not contain UPDATED_INFRA_ORDRE
        defaultCronquistPlanteShouldBeFound("infraOrdre.doesNotContain=" + UPDATED_INFRA_ORDRE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByMicroOrdreIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where microOrdre equals to DEFAULT_MICRO_ORDRE
        defaultCronquistPlanteShouldBeFound("microOrdre.equals=" + DEFAULT_MICRO_ORDRE);

        // Get all the cronquistPlanteList where microOrdre equals to UPDATED_MICRO_ORDRE
        defaultCronquistPlanteShouldNotBeFound("microOrdre.equals=" + UPDATED_MICRO_ORDRE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByMicroOrdreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where microOrdre not equals to DEFAULT_MICRO_ORDRE
        defaultCronquistPlanteShouldNotBeFound("microOrdre.notEquals=" + DEFAULT_MICRO_ORDRE);

        // Get all the cronquistPlanteList where microOrdre not equals to UPDATED_MICRO_ORDRE
        defaultCronquistPlanteShouldBeFound("microOrdre.notEquals=" + UPDATED_MICRO_ORDRE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByMicroOrdreIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where microOrdre in DEFAULT_MICRO_ORDRE or UPDATED_MICRO_ORDRE
        defaultCronquistPlanteShouldBeFound("microOrdre.in=" + DEFAULT_MICRO_ORDRE + "," + UPDATED_MICRO_ORDRE);

        // Get all the cronquistPlanteList where microOrdre equals to UPDATED_MICRO_ORDRE
        defaultCronquistPlanteShouldNotBeFound("microOrdre.in=" + UPDATED_MICRO_ORDRE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByMicroOrdreIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where microOrdre is not null
        defaultCronquistPlanteShouldBeFound("microOrdre.specified=true");

        // Get all the cronquistPlanteList where microOrdre is null
        defaultCronquistPlanteShouldNotBeFound("microOrdre.specified=false");
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByMicroOrdreContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where microOrdre contains DEFAULT_MICRO_ORDRE
        defaultCronquistPlanteShouldBeFound("microOrdre.contains=" + DEFAULT_MICRO_ORDRE);

        // Get all the cronquistPlanteList where microOrdre contains UPDATED_MICRO_ORDRE
        defaultCronquistPlanteShouldNotBeFound("microOrdre.contains=" + UPDATED_MICRO_ORDRE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByMicroOrdreNotContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where microOrdre does not contain DEFAULT_MICRO_ORDRE
        defaultCronquistPlanteShouldNotBeFound("microOrdre.doesNotContain=" + DEFAULT_MICRO_ORDRE);

        // Get all the cronquistPlanteList where microOrdre does not contain UPDATED_MICRO_ORDRE
        defaultCronquistPlanteShouldBeFound("microOrdre.doesNotContain=" + UPDATED_MICRO_ORDRE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySuperFamilleIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where superFamille equals to DEFAULT_SUPER_FAMILLE
        defaultCronquistPlanteShouldBeFound("superFamille.equals=" + DEFAULT_SUPER_FAMILLE);

        // Get all the cronquistPlanteList where superFamille equals to UPDATED_SUPER_FAMILLE
        defaultCronquistPlanteShouldNotBeFound("superFamille.equals=" + UPDATED_SUPER_FAMILLE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySuperFamilleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where superFamille not equals to DEFAULT_SUPER_FAMILLE
        defaultCronquistPlanteShouldNotBeFound("superFamille.notEquals=" + DEFAULT_SUPER_FAMILLE);

        // Get all the cronquistPlanteList where superFamille not equals to UPDATED_SUPER_FAMILLE
        defaultCronquistPlanteShouldBeFound("superFamille.notEquals=" + UPDATED_SUPER_FAMILLE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySuperFamilleIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where superFamille in DEFAULT_SUPER_FAMILLE or UPDATED_SUPER_FAMILLE
        defaultCronquistPlanteShouldBeFound("superFamille.in=" + DEFAULT_SUPER_FAMILLE + "," + UPDATED_SUPER_FAMILLE);

        // Get all the cronquistPlanteList where superFamille equals to UPDATED_SUPER_FAMILLE
        defaultCronquistPlanteShouldNotBeFound("superFamille.in=" + UPDATED_SUPER_FAMILLE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySuperFamilleIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where superFamille is not null
        defaultCronquistPlanteShouldBeFound("superFamille.specified=true");

        // Get all the cronquistPlanteList where superFamille is null
        defaultCronquistPlanteShouldNotBeFound("superFamille.specified=false");
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySuperFamilleContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where superFamille contains DEFAULT_SUPER_FAMILLE
        defaultCronquistPlanteShouldBeFound("superFamille.contains=" + DEFAULT_SUPER_FAMILLE);

        // Get all the cronquistPlanteList where superFamille contains UPDATED_SUPER_FAMILLE
        defaultCronquistPlanteShouldNotBeFound("superFamille.contains=" + UPDATED_SUPER_FAMILLE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySuperFamilleNotContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where superFamille does not contain DEFAULT_SUPER_FAMILLE
        defaultCronquistPlanteShouldNotBeFound("superFamille.doesNotContain=" + DEFAULT_SUPER_FAMILLE);

        // Get all the cronquistPlanteList where superFamille does not contain UPDATED_SUPER_FAMILLE
        defaultCronquistPlanteShouldBeFound("superFamille.doesNotContain=" + UPDATED_SUPER_FAMILLE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByFamilleIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where famille equals to DEFAULT_FAMILLE
        defaultCronquistPlanteShouldBeFound("famille.equals=" + DEFAULT_FAMILLE);

        // Get all the cronquistPlanteList where famille equals to UPDATED_FAMILLE
        defaultCronquistPlanteShouldNotBeFound("famille.equals=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByFamilleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where famille not equals to DEFAULT_FAMILLE
        defaultCronquistPlanteShouldNotBeFound("famille.notEquals=" + DEFAULT_FAMILLE);

        // Get all the cronquistPlanteList where famille not equals to UPDATED_FAMILLE
        defaultCronquistPlanteShouldBeFound("famille.notEquals=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByFamilleIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where famille in DEFAULT_FAMILLE or UPDATED_FAMILLE
        defaultCronquistPlanteShouldBeFound("famille.in=" + DEFAULT_FAMILLE + "," + UPDATED_FAMILLE);

        // Get all the cronquistPlanteList where famille equals to UPDATED_FAMILLE
        defaultCronquistPlanteShouldNotBeFound("famille.in=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByFamilleIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where famille is not null
        defaultCronquistPlanteShouldBeFound("famille.specified=true");

        // Get all the cronquistPlanteList where famille is null
        defaultCronquistPlanteShouldNotBeFound("famille.specified=false");
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByFamilleContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where famille contains DEFAULT_FAMILLE
        defaultCronquistPlanteShouldBeFound("famille.contains=" + DEFAULT_FAMILLE);

        // Get all the cronquistPlanteList where famille contains UPDATED_FAMILLE
        defaultCronquistPlanteShouldNotBeFound("famille.contains=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByFamilleNotContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where famille does not contain DEFAULT_FAMILLE
        defaultCronquistPlanteShouldNotBeFound("famille.doesNotContain=" + DEFAULT_FAMILLE);

        // Get all the cronquistPlanteList where famille does not contain UPDATED_FAMILLE
        defaultCronquistPlanteShouldBeFound("famille.doesNotContain=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousFamilleIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousFamille equals to DEFAULT_SOUS_FAMILLE
        defaultCronquistPlanteShouldBeFound("sousFamille.equals=" + DEFAULT_SOUS_FAMILLE);

        // Get all the cronquistPlanteList where sousFamille equals to UPDATED_SOUS_FAMILLE
        defaultCronquistPlanteShouldNotBeFound("sousFamille.equals=" + UPDATED_SOUS_FAMILLE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousFamilleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousFamille not equals to DEFAULT_SOUS_FAMILLE
        defaultCronquistPlanteShouldNotBeFound("sousFamille.notEquals=" + DEFAULT_SOUS_FAMILLE);

        // Get all the cronquistPlanteList where sousFamille not equals to UPDATED_SOUS_FAMILLE
        defaultCronquistPlanteShouldBeFound("sousFamille.notEquals=" + UPDATED_SOUS_FAMILLE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousFamilleIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousFamille in DEFAULT_SOUS_FAMILLE or UPDATED_SOUS_FAMILLE
        defaultCronquistPlanteShouldBeFound("sousFamille.in=" + DEFAULT_SOUS_FAMILLE + "," + UPDATED_SOUS_FAMILLE);

        // Get all the cronquistPlanteList where sousFamille equals to UPDATED_SOUS_FAMILLE
        defaultCronquistPlanteShouldNotBeFound("sousFamille.in=" + UPDATED_SOUS_FAMILLE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousFamilleIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousFamille is not null
        defaultCronquistPlanteShouldBeFound("sousFamille.specified=true");

        // Get all the cronquistPlanteList where sousFamille is null
        defaultCronquistPlanteShouldNotBeFound("sousFamille.specified=false");
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousFamilleContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousFamille contains DEFAULT_SOUS_FAMILLE
        defaultCronquistPlanteShouldBeFound("sousFamille.contains=" + DEFAULT_SOUS_FAMILLE);

        // Get all the cronquistPlanteList where sousFamille contains UPDATED_SOUS_FAMILLE
        defaultCronquistPlanteShouldNotBeFound("sousFamille.contains=" + UPDATED_SOUS_FAMILLE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousFamilleNotContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousFamille does not contain DEFAULT_SOUS_FAMILLE
        defaultCronquistPlanteShouldNotBeFound("sousFamille.doesNotContain=" + DEFAULT_SOUS_FAMILLE);

        // Get all the cronquistPlanteList where sousFamille does not contain UPDATED_SOUS_FAMILLE
        defaultCronquistPlanteShouldBeFound("sousFamille.doesNotContain=" + UPDATED_SOUS_FAMILLE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByTribuIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where tribu equals to DEFAULT_TRIBU
        defaultCronquistPlanteShouldBeFound("tribu.equals=" + DEFAULT_TRIBU);

        // Get all the cronquistPlanteList where tribu equals to UPDATED_TRIBU
        defaultCronquistPlanteShouldNotBeFound("tribu.equals=" + UPDATED_TRIBU);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByTribuIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where tribu not equals to DEFAULT_TRIBU
        defaultCronquistPlanteShouldNotBeFound("tribu.notEquals=" + DEFAULT_TRIBU);

        // Get all the cronquistPlanteList where tribu not equals to UPDATED_TRIBU
        defaultCronquistPlanteShouldBeFound("tribu.notEquals=" + UPDATED_TRIBU);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByTribuIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where tribu in DEFAULT_TRIBU or UPDATED_TRIBU
        defaultCronquistPlanteShouldBeFound("tribu.in=" + DEFAULT_TRIBU + "," + UPDATED_TRIBU);

        // Get all the cronquistPlanteList where tribu equals to UPDATED_TRIBU
        defaultCronquistPlanteShouldNotBeFound("tribu.in=" + UPDATED_TRIBU);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByTribuIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where tribu is not null
        defaultCronquistPlanteShouldBeFound("tribu.specified=true");

        // Get all the cronquistPlanteList where tribu is null
        defaultCronquistPlanteShouldNotBeFound("tribu.specified=false");
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByTribuContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where tribu contains DEFAULT_TRIBU
        defaultCronquistPlanteShouldBeFound("tribu.contains=" + DEFAULT_TRIBU);

        // Get all the cronquistPlanteList where tribu contains UPDATED_TRIBU
        defaultCronquistPlanteShouldNotBeFound("tribu.contains=" + UPDATED_TRIBU);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByTribuNotContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where tribu does not contain DEFAULT_TRIBU
        defaultCronquistPlanteShouldNotBeFound("tribu.doesNotContain=" + DEFAULT_TRIBU);

        // Get all the cronquistPlanteList where tribu does not contain UPDATED_TRIBU
        defaultCronquistPlanteShouldBeFound("tribu.doesNotContain=" + UPDATED_TRIBU);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousTribuIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousTribu equals to DEFAULT_SOUS_TRIBU
        defaultCronquistPlanteShouldBeFound("sousTribu.equals=" + DEFAULT_SOUS_TRIBU);

        // Get all the cronquistPlanteList where sousTribu equals to UPDATED_SOUS_TRIBU
        defaultCronquistPlanteShouldNotBeFound("sousTribu.equals=" + UPDATED_SOUS_TRIBU);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousTribuIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousTribu not equals to DEFAULT_SOUS_TRIBU
        defaultCronquistPlanteShouldNotBeFound("sousTribu.notEquals=" + DEFAULT_SOUS_TRIBU);

        // Get all the cronquistPlanteList where sousTribu not equals to UPDATED_SOUS_TRIBU
        defaultCronquistPlanteShouldBeFound("sousTribu.notEquals=" + UPDATED_SOUS_TRIBU);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousTribuIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousTribu in DEFAULT_SOUS_TRIBU or UPDATED_SOUS_TRIBU
        defaultCronquistPlanteShouldBeFound("sousTribu.in=" + DEFAULT_SOUS_TRIBU + "," + UPDATED_SOUS_TRIBU);

        // Get all the cronquistPlanteList where sousTribu equals to UPDATED_SOUS_TRIBU
        defaultCronquistPlanteShouldNotBeFound("sousTribu.in=" + UPDATED_SOUS_TRIBU);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousTribuIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousTribu is not null
        defaultCronquistPlanteShouldBeFound("sousTribu.specified=true");

        // Get all the cronquistPlanteList where sousTribu is null
        defaultCronquistPlanteShouldNotBeFound("sousTribu.specified=false");
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousTribuContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousTribu contains DEFAULT_SOUS_TRIBU
        defaultCronquistPlanteShouldBeFound("sousTribu.contains=" + DEFAULT_SOUS_TRIBU);

        // Get all the cronquistPlanteList where sousTribu contains UPDATED_SOUS_TRIBU
        defaultCronquistPlanteShouldNotBeFound("sousTribu.contains=" + UPDATED_SOUS_TRIBU);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousTribuNotContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousTribu does not contain DEFAULT_SOUS_TRIBU
        defaultCronquistPlanteShouldNotBeFound("sousTribu.doesNotContain=" + DEFAULT_SOUS_TRIBU);

        // Get all the cronquistPlanteList where sousTribu does not contain UPDATED_SOUS_TRIBU
        defaultCronquistPlanteShouldBeFound("sousTribu.doesNotContain=" + UPDATED_SOUS_TRIBU);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByGenreIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where genre equals to DEFAULT_GENRE
        defaultCronquistPlanteShouldBeFound("genre.equals=" + DEFAULT_GENRE);

        // Get all the cronquistPlanteList where genre equals to UPDATED_GENRE
        defaultCronquistPlanteShouldNotBeFound("genre.equals=" + UPDATED_GENRE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByGenreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where genre not equals to DEFAULT_GENRE
        defaultCronquistPlanteShouldNotBeFound("genre.notEquals=" + DEFAULT_GENRE);

        // Get all the cronquistPlanteList where genre not equals to UPDATED_GENRE
        defaultCronquistPlanteShouldBeFound("genre.notEquals=" + UPDATED_GENRE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByGenreIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where genre in DEFAULT_GENRE or UPDATED_GENRE
        defaultCronquistPlanteShouldBeFound("genre.in=" + DEFAULT_GENRE + "," + UPDATED_GENRE);

        // Get all the cronquistPlanteList where genre equals to UPDATED_GENRE
        defaultCronquistPlanteShouldNotBeFound("genre.in=" + UPDATED_GENRE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByGenreIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where genre is not null
        defaultCronquistPlanteShouldBeFound("genre.specified=true");

        // Get all the cronquistPlanteList where genre is null
        defaultCronquistPlanteShouldNotBeFound("genre.specified=false");
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByGenreContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where genre contains DEFAULT_GENRE
        defaultCronquistPlanteShouldBeFound("genre.contains=" + DEFAULT_GENRE);

        // Get all the cronquistPlanteList where genre contains UPDATED_GENRE
        defaultCronquistPlanteShouldNotBeFound("genre.contains=" + UPDATED_GENRE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByGenreNotContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where genre does not contain DEFAULT_GENRE
        defaultCronquistPlanteShouldNotBeFound("genre.doesNotContain=" + DEFAULT_GENRE);

        // Get all the cronquistPlanteList where genre does not contain UPDATED_GENRE
        defaultCronquistPlanteShouldBeFound("genre.doesNotContain=" + UPDATED_GENRE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousGenreIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousGenre equals to DEFAULT_SOUS_GENRE
        defaultCronquistPlanteShouldBeFound("sousGenre.equals=" + DEFAULT_SOUS_GENRE);

        // Get all the cronquistPlanteList where sousGenre equals to UPDATED_SOUS_GENRE
        defaultCronquistPlanteShouldNotBeFound("sousGenre.equals=" + UPDATED_SOUS_GENRE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousGenreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousGenre not equals to DEFAULT_SOUS_GENRE
        defaultCronquistPlanteShouldNotBeFound("sousGenre.notEquals=" + DEFAULT_SOUS_GENRE);

        // Get all the cronquistPlanteList where sousGenre not equals to UPDATED_SOUS_GENRE
        defaultCronquistPlanteShouldBeFound("sousGenre.notEquals=" + UPDATED_SOUS_GENRE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousGenreIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousGenre in DEFAULT_SOUS_GENRE or UPDATED_SOUS_GENRE
        defaultCronquistPlanteShouldBeFound("sousGenre.in=" + DEFAULT_SOUS_GENRE + "," + UPDATED_SOUS_GENRE);

        // Get all the cronquistPlanteList where sousGenre equals to UPDATED_SOUS_GENRE
        defaultCronquistPlanteShouldNotBeFound("sousGenre.in=" + UPDATED_SOUS_GENRE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousGenreIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousGenre is not null
        defaultCronquistPlanteShouldBeFound("sousGenre.specified=true");

        // Get all the cronquistPlanteList where sousGenre is null
        defaultCronquistPlanteShouldNotBeFound("sousGenre.specified=false");
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousGenreContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousGenre contains DEFAULT_SOUS_GENRE
        defaultCronquistPlanteShouldBeFound("sousGenre.contains=" + DEFAULT_SOUS_GENRE);

        // Get all the cronquistPlanteList where sousGenre contains UPDATED_SOUS_GENRE
        defaultCronquistPlanteShouldNotBeFound("sousGenre.contains=" + UPDATED_SOUS_GENRE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousGenreNotContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousGenre does not contain DEFAULT_SOUS_GENRE
        defaultCronquistPlanteShouldNotBeFound("sousGenre.doesNotContain=" + DEFAULT_SOUS_GENRE);

        // Get all the cronquistPlanteList where sousGenre does not contain UPDATED_SOUS_GENRE
        defaultCronquistPlanteShouldBeFound("sousGenre.doesNotContain=" + UPDATED_SOUS_GENRE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySectionIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where section equals to DEFAULT_SECTION
        defaultCronquistPlanteShouldBeFound("section.equals=" + DEFAULT_SECTION);

        // Get all the cronquistPlanteList where section equals to UPDATED_SECTION
        defaultCronquistPlanteShouldNotBeFound("section.equals=" + UPDATED_SECTION);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySectionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where section not equals to DEFAULT_SECTION
        defaultCronquistPlanteShouldNotBeFound("section.notEquals=" + DEFAULT_SECTION);

        // Get all the cronquistPlanteList where section not equals to UPDATED_SECTION
        defaultCronquistPlanteShouldBeFound("section.notEquals=" + UPDATED_SECTION);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySectionIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where section in DEFAULT_SECTION or UPDATED_SECTION
        defaultCronquistPlanteShouldBeFound("section.in=" + DEFAULT_SECTION + "," + UPDATED_SECTION);

        // Get all the cronquistPlanteList where section equals to UPDATED_SECTION
        defaultCronquistPlanteShouldNotBeFound("section.in=" + UPDATED_SECTION);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySectionIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where section is not null
        defaultCronquistPlanteShouldBeFound("section.specified=true");

        // Get all the cronquistPlanteList where section is null
        defaultCronquistPlanteShouldNotBeFound("section.specified=false");
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySectionContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where section contains DEFAULT_SECTION
        defaultCronquistPlanteShouldBeFound("section.contains=" + DEFAULT_SECTION);

        // Get all the cronquistPlanteList where section contains UPDATED_SECTION
        defaultCronquistPlanteShouldNotBeFound("section.contains=" + UPDATED_SECTION);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySectionNotContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where section does not contain DEFAULT_SECTION
        defaultCronquistPlanteShouldNotBeFound("section.doesNotContain=" + DEFAULT_SECTION);

        // Get all the cronquistPlanteList where section does not contain UPDATED_SECTION
        defaultCronquistPlanteShouldBeFound("section.doesNotContain=" + UPDATED_SECTION);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousSectionIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousSection equals to DEFAULT_SOUS_SECTION
        defaultCronquistPlanteShouldBeFound("sousSection.equals=" + DEFAULT_SOUS_SECTION);

        // Get all the cronquistPlanteList where sousSection equals to UPDATED_SOUS_SECTION
        defaultCronquistPlanteShouldNotBeFound("sousSection.equals=" + UPDATED_SOUS_SECTION);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousSectionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousSection not equals to DEFAULT_SOUS_SECTION
        defaultCronquistPlanteShouldNotBeFound("sousSection.notEquals=" + DEFAULT_SOUS_SECTION);

        // Get all the cronquistPlanteList where sousSection not equals to UPDATED_SOUS_SECTION
        defaultCronquistPlanteShouldBeFound("sousSection.notEquals=" + UPDATED_SOUS_SECTION);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousSectionIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousSection in DEFAULT_SOUS_SECTION or UPDATED_SOUS_SECTION
        defaultCronquistPlanteShouldBeFound("sousSection.in=" + DEFAULT_SOUS_SECTION + "," + UPDATED_SOUS_SECTION);

        // Get all the cronquistPlanteList where sousSection equals to UPDATED_SOUS_SECTION
        defaultCronquistPlanteShouldNotBeFound("sousSection.in=" + UPDATED_SOUS_SECTION);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousSectionIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousSection is not null
        defaultCronquistPlanteShouldBeFound("sousSection.specified=true");

        // Get all the cronquistPlanteList where sousSection is null
        defaultCronquistPlanteShouldNotBeFound("sousSection.specified=false");
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousSectionContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousSection contains DEFAULT_SOUS_SECTION
        defaultCronquistPlanteShouldBeFound("sousSection.contains=" + DEFAULT_SOUS_SECTION);

        // Get all the cronquistPlanteList where sousSection contains UPDATED_SOUS_SECTION
        defaultCronquistPlanteShouldNotBeFound("sousSection.contains=" + UPDATED_SOUS_SECTION);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousSectionNotContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousSection does not contain DEFAULT_SOUS_SECTION
        defaultCronquistPlanteShouldNotBeFound("sousSection.doesNotContain=" + DEFAULT_SOUS_SECTION);

        // Get all the cronquistPlanteList where sousSection does not contain UPDATED_SOUS_SECTION
        defaultCronquistPlanteShouldBeFound("sousSection.doesNotContain=" + UPDATED_SOUS_SECTION);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByEspeceIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where espece equals to DEFAULT_ESPECE
        defaultCronquistPlanteShouldBeFound("espece.equals=" + DEFAULT_ESPECE);

        // Get all the cronquistPlanteList where espece equals to UPDATED_ESPECE
        defaultCronquistPlanteShouldNotBeFound("espece.equals=" + UPDATED_ESPECE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByEspeceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where espece not equals to DEFAULT_ESPECE
        defaultCronquistPlanteShouldNotBeFound("espece.notEquals=" + DEFAULT_ESPECE);

        // Get all the cronquistPlanteList where espece not equals to UPDATED_ESPECE
        defaultCronquistPlanteShouldBeFound("espece.notEquals=" + UPDATED_ESPECE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByEspeceIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where espece in DEFAULT_ESPECE or UPDATED_ESPECE
        defaultCronquistPlanteShouldBeFound("espece.in=" + DEFAULT_ESPECE + "," + UPDATED_ESPECE);

        // Get all the cronquistPlanteList where espece equals to UPDATED_ESPECE
        defaultCronquistPlanteShouldNotBeFound("espece.in=" + UPDATED_ESPECE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByEspeceIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where espece is not null
        defaultCronquistPlanteShouldBeFound("espece.specified=true");

        // Get all the cronquistPlanteList where espece is null
        defaultCronquistPlanteShouldNotBeFound("espece.specified=false");
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByEspeceContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where espece contains DEFAULT_ESPECE
        defaultCronquistPlanteShouldBeFound("espece.contains=" + DEFAULT_ESPECE);

        // Get all the cronquistPlanteList where espece contains UPDATED_ESPECE
        defaultCronquistPlanteShouldNotBeFound("espece.contains=" + UPDATED_ESPECE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByEspeceNotContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where espece does not contain DEFAULT_ESPECE
        defaultCronquistPlanteShouldNotBeFound("espece.doesNotContain=" + DEFAULT_ESPECE);

        // Get all the cronquistPlanteList where espece does not contain UPDATED_ESPECE
        defaultCronquistPlanteShouldBeFound("espece.doesNotContain=" + UPDATED_ESPECE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousEspeceIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousEspece equals to DEFAULT_SOUS_ESPECE
        defaultCronquistPlanteShouldBeFound("sousEspece.equals=" + DEFAULT_SOUS_ESPECE);

        // Get all the cronquistPlanteList where sousEspece equals to UPDATED_SOUS_ESPECE
        defaultCronquistPlanteShouldNotBeFound("sousEspece.equals=" + UPDATED_SOUS_ESPECE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousEspeceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousEspece not equals to DEFAULT_SOUS_ESPECE
        defaultCronquistPlanteShouldNotBeFound("sousEspece.notEquals=" + DEFAULT_SOUS_ESPECE);

        // Get all the cronquistPlanteList where sousEspece not equals to UPDATED_SOUS_ESPECE
        defaultCronquistPlanteShouldBeFound("sousEspece.notEquals=" + UPDATED_SOUS_ESPECE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousEspeceIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousEspece in DEFAULT_SOUS_ESPECE or UPDATED_SOUS_ESPECE
        defaultCronquistPlanteShouldBeFound("sousEspece.in=" + DEFAULT_SOUS_ESPECE + "," + UPDATED_SOUS_ESPECE);

        // Get all the cronquistPlanteList where sousEspece equals to UPDATED_SOUS_ESPECE
        defaultCronquistPlanteShouldNotBeFound("sousEspece.in=" + UPDATED_SOUS_ESPECE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousEspeceIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousEspece is not null
        defaultCronquistPlanteShouldBeFound("sousEspece.specified=true");

        // Get all the cronquistPlanteList where sousEspece is null
        defaultCronquistPlanteShouldNotBeFound("sousEspece.specified=false");
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousEspeceContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousEspece contains DEFAULT_SOUS_ESPECE
        defaultCronquistPlanteShouldBeFound("sousEspece.contains=" + DEFAULT_SOUS_ESPECE);

        // Get all the cronquistPlanteList where sousEspece contains UPDATED_SOUS_ESPECE
        defaultCronquistPlanteShouldNotBeFound("sousEspece.contains=" + UPDATED_SOUS_ESPECE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousEspeceNotContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousEspece does not contain DEFAULT_SOUS_ESPECE
        defaultCronquistPlanteShouldNotBeFound("sousEspece.doesNotContain=" + DEFAULT_SOUS_ESPECE);

        // Get all the cronquistPlanteList where sousEspece does not contain UPDATED_SOUS_ESPECE
        defaultCronquistPlanteShouldBeFound("sousEspece.doesNotContain=" + UPDATED_SOUS_ESPECE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByVarieteIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where variete equals to DEFAULT_VARIETE
        defaultCronquistPlanteShouldBeFound("variete.equals=" + DEFAULT_VARIETE);

        // Get all the cronquistPlanteList where variete equals to UPDATED_VARIETE
        defaultCronquistPlanteShouldNotBeFound("variete.equals=" + UPDATED_VARIETE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByVarieteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where variete not equals to DEFAULT_VARIETE
        defaultCronquistPlanteShouldNotBeFound("variete.notEquals=" + DEFAULT_VARIETE);

        // Get all the cronquistPlanteList where variete not equals to UPDATED_VARIETE
        defaultCronquistPlanteShouldBeFound("variete.notEquals=" + UPDATED_VARIETE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByVarieteIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where variete in DEFAULT_VARIETE or UPDATED_VARIETE
        defaultCronquistPlanteShouldBeFound("variete.in=" + DEFAULT_VARIETE + "," + UPDATED_VARIETE);

        // Get all the cronquistPlanteList where variete equals to UPDATED_VARIETE
        defaultCronquistPlanteShouldNotBeFound("variete.in=" + UPDATED_VARIETE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByVarieteIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where variete is not null
        defaultCronquistPlanteShouldBeFound("variete.specified=true");

        // Get all the cronquistPlanteList where variete is null
        defaultCronquistPlanteShouldNotBeFound("variete.specified=false");
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByVarieteContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where variete contains DEFAULT_VARIETE
        defaultCronquistPlanteShouldBeFound("variete.contains=" + DEFAULT_VARIETE);

        // Get all the cronquistPlanteList where variete contains UPDATED_VARIETE
        defaultCronquistPlanteShouldNotBeFound("variete.contains=" + UPDATED_VARIETE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByVarieteNotContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where variete does not contain DEFAULT_VARIETE
        defaultCronquistPlanteShouldNotBeFound("variete.doesNotContain=" + DEFAULT_VARIETE);

        // Get all the cronquistPlanteList where variete does not contain UPDATED_VARIETE
        defaultCronquistPlanteShouldBeFound("variete.doesNotContain=" + UPDATED_VARIETE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousVarieteIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousVariete equals to DEFAULT_SOUS_VARIETE
        defaultCronquistPlanteShouldBeFound("sousVariete.equals=" + DEFAULT_SOUS_VARIETE);

        // Get all the cronquistPlanteList where sousVariete equals to UPDATED_SOUS_VARIETE
        defaultCronquistPlanteShouldNotBeFound("sousVariete.equals=" + UPDATED_SOUS_VARIETE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousVarieteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousVariete not equals to DEFAULT_SOUS_VARIETE
        defaultCronquistPlanteShouldNotBeFound("sousVariete.notEquals=" + DEFAULT_SOUS_VARIETE);

        // Get all the cronquistPlanteList where sousVariete not equals to UPDATED_SOUS_VARIETE
        defaultCronquistPlanteShouldBeFound("sousVariete.notEquals=" + UPDATED_SOUS_VARIETE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousVarieteIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousVariete in DEFAULT_SOUS_VARIETE or UPDATED_SOUS_VARIETE
        defaultCronquistPlanteShouldBeFound("sousVariete.in=" + DEFAULT_SOUS_VARIETE + "," + UPDATED_SOUS_VARIETE);

        // Get all the cronquistPlanteList where sousVariete equals to UPDATED_SOUS_VARIETE
        defaultCronquistPlanteShouldNotBeFound("sousVariete.in=" + UPDATED_SOUS_VARIETE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousVarieteIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousVariete is not null
        defaultCronquistPlanteShouldBeFound("sousVariete.specified=true");

        // Get all the cronquistPlanteList where sousVariete is null
        defaultCronquistPlanteShouldNotBeFound("sousVariete.specified=false");
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousVarieteContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousVariete contains DEFAULT_SOUS_VARIETE
        defaultCronquistPlanteShouldBeFound("sousVariete.contains=" + DEFAULT_SOUS_VARIETE);

        // Get all the cronquistPlanteList where sousVariete contains UPDATED_SOUS_VARIETE
        defaultCronquistPlanteShouldNotBeFound("sousVariete.contains=" + UPDATED_SOUS_VARIETE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesBySousVarieteNotContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where sousVariete does not contain DEFAULT_SOUS_VARIETE
        defaultCronquistPlanteShouldNotBeFound("sousVariete.doesNotContain=" + DEFAULT_SOUS_VARIETE);

        // Get all the cronquistPlanteList where sousVariete does not contain UPDATED_SOUS_VARIETE
        defaultCronquistPlanteShouldBeFound("sousVariete.doesNotContain=" + UPDATED_SOUS_VARIETE);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByFormeIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where forme equals to DEFAULT_FORME
        defaultCronquistPlanteShouldBeFound("forme.equals=" + DEFAULT_FORME);

        // Get all the cronquistPlanteList where forme equals to UPDATED_FORME
        defaultCronquistPlanteShouldNotBeFound("forme.equals=" + UPDATED_FORME);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByFormeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where forme not equals to DEFAULT_FORME
        defaultCronquistPlanteShouldNotBeFound("forme.notEquals=" + DEFAULT_FORME);

        // Get all the cronquistPlanteList where forme not equals to UPDATED_FORME
        defaultCronquistPlanteShouldBeFound("forme.notEquals=" + UPDATED_FORME);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByFormeIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where forme in DEFAULT_FORME or UPDATED_FORME
        defaultCronquistPlanteShouldBeFound("forme.in=" + DEFAULT_FORME + "," + UPDATED_FORME);

        // Get all the cronquistPlanteList where forme equals to UPDATED_FORME
        defaultCronquistPlanteShouldNotBeFound("forme.in=" + UPDATED_FORME);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByFormeIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where forme is not null
        defaultCronquistPlanteShouldBeFound("forme.specified=true");

        // Get all the cronquistPlanteList where forme is null
        defaultCronquistPlanteShouldNotBeFound("forme.specified=false");
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByFormeContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where forme contains DEFAULT_FORME
        defaultCronquistPlanteShouldBeFound("forme.contains=" + DEFAULT_FORME);

        // Get all the cronquistPlanteList where forme contains UPDATED_FORME
        defaultCronquistPlanteShouldNotBeFound("forme.contains=" + UPDATED_FORME);
    }

    @Test
    @Transactional
    void getAllCronquistPlantesByFormeNotContainsSomething() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        // Get all the cronquistPlanteList where forme does not contain DEFAULT_FORME
        defaultCronquistPlanteShouldNotBeFound("forme.doesNotContain=" + DEFAULT_FORME);

        // Get all the cronquistPlanteList where forme does not contain UPDATED_FORME
        defaultCronquistPlanteShouldBeFound("forme.doesNotContain=" + UPDATED_FORME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCronquistPlanteShouldBeFound(String filter) throws Exception {
        restCronquistPlanteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cronquistPlante.getId().intValue())))
            .andExpect(jsonPath("$.[*].superRegne").value(hasItem(DEFAULT_SUPER_REGNE)))
            .andExpect(jsonPath("$.[*].regne").value(hasItem(DEFAULT_REGNE)))
            .andExpect(jsonPath("$.[*].sousRegne").value(hasItem(DEFAULT_SOUS_REGNE)))
            .andExpect(jsonPath("$.[*].rameau").value(hasItem(DEFAULT_RAMEAU)))
            .andExpect(jsonPath("$.[*].infraRegne").value(hasItem(DEFAULT_INFRA_REGNE)))
            .andExpect(jsonPath("$.[*].superDivision").value(hasItem(DEFAULT_SUPER_DIVISION)))
            .andExpect(jsonPath("$.[*].division").value(hasItem(DEFAULT_DIVISION)))
            .andExpect(jsonPath("$.[*].sousDivision").value(hasItem(DEFAULT_SOUS_DIVISION)))
            .andExpect(jsonPath("$.[*].infraEmbranchement").value(hasItem(DEFAULT_INFRA_EMBRANCHEMENT)))
            .andExpect(jsonPath("$.[*].microEmbranchement").value(hasItem(DEFAULT_MICRO_EMBRANCHEMENT)))
            .andExpect(jsonPath("$.[*].superClasse").value(hasItem(DEFAULT_SUPER_CLASSE)))
            .andExpect(jsonPath("$.[*].classe").value(hasItem(DEFAULT_CLASSE)))
            .andExpect(jsonPath("$.[*].sousClasse").value(hasItem(DEFAULT_SOUS_CLASSE)))
            .andExpect(jsonPath("$.[*].infraClasse").value(hasItem(DEFAULT_INFRA_CLASSE)))
            .andExpect(jsonPath("$.[*].superOrdre").value(hasItem(DEFAULT_SUPER_ORDRE)))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)))
            .andExpect(jsonPath("$.[*].sousOrdre").value(hasItem(DEFAULT_SOUS_ORDRE)))
            .andExpect(jsonPath("$.[*].infraOrdre").value(hasItem(DEFAULT_INFRA_ORDRE)))
            .andExpect(jsonPath("$.[*].microOrdre").value(hasItem(DEFAULT_MICRO_ORDRE)))
            .andExpect(jsonPath("$.[*].superFamille").value(hasItem(DEFAULT_SUPER_FAMILLE)))
            .andExpect(jsonPath("$.[*].famille").value(hasItem(DEFAULT_FAMILLE)))
            .andExpect(jsonPath("$.[*].sousFamille").value(hasItem(DEFAULT_SOUS_FAMILLE)))
            .andExpect(jsonPath("$.[*].tribu").value(hasItem(DEFAULT_TRIBU)))
            .andExpect(jsonPath("$.[*].sousTribu").value(hasItem(DEFAULT_SOUS_TRIBU)))
            .andExpect(jsonPath("$.[*].genre").value(hasItem(DEFAULT_GENRE)))
            .andExpect(jsonPath("$.[*].sousGenre").value(hasItem(DEFAULT_SOUS_GENRE)))
            .andExpect(jsonPath("$.[*].section").value(hasItem(DEFAULT_SECTION)))
            .andExpect(jsonPath("$.[*].sousSection").value(hasItem(DEFAULT_SOUS_SECTION)))
            .andExpect(jsonPath("$.[*].espece").value(hasItem(DEFAULT_ESPECE)))
            .andExpect(jsonPath("$.[*].sousEspece").value(hasItem(DEFAULT_SOUS_ESPECE)))
            .andExpect(jsonPath("$.[*].variete").value(hasItem(DEFAULT_VARIETE)))
            .andExpect(jsonPath("$.[*].sousVariete").value(hasItem(DEFAULT_SOUS_VARIETE)))
            .andExpect(jsonPath("$.[*].forme").value(hasItem(DEFAULT_FORME)));

        // Check, that the count call also returns 1
        restCronquistPlanteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCronquistPlanteShouldNotBeFound(String filter) throws Exception {
        restCronquistPlanteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCronquistPlanteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCronquistPlante() throws Exception {
        // Get the cronquistPlante
        restCronquistPlanteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCronquistPlante() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        int databaseSizeBeforeUpdate = cronquistPlanteRepository.findAll().size();

        // Update the cronquistPlante
        CronquistPlante updatedCronquistPlante = cronquistPlanteRepository.findById(cronquistPlante.getId()).get();
        // Disconnect from session so that the updates on updatedCronquistPlante are not directly saved in db
        em.detach(updatedCronquistPlante);
        updatedCronquistPlante
            .superRegne(UPDATED_SUPER_REGNE)
            .regne(UPDATED_REGNE)
            .sousRegne(UPDATED_SOUS_REGNE)
            .rameau(UPDATED_RAMEAU)
            .infraRegne(UPDATED_INFRA_REGNE)
            .superDivision(UPDATED_SUPER_DIVISION)
            .division(UPDATED_DIVISION)
            .sousDivision(UPDATED_SOUS_DIVISION)
            .infraEmbranchement(UPDATED_INFRA_EMBRANCHEMENT)
            .microEmbranchement(UPDATED_MICRO_EMBRANCHEMENT)
            .superClasse(UPDATED_SUPER_CLASSE)
            .classe(UPDATED_CLASSE)
            .sousClasse(UPDATED_SOUS_CLASSE)
            .infraClasse(UPDATED_INFRA_CLASSE)
            .superOrdre(UPDATED_SUPER_ORDRE)
            .ordre(UPDATED_ORDRE)
            .sousOrdre(UPDATED_SOUS_ORDRE)
            .infraOrdre(UPDATED_INFRA_ORDRE)
            .microOrdre(UPDATED_MICRO_ORDRE)
            .superFamille(UPDATED_SUPER_FAMILLE)
            .famille(UPDATED_FAMILLE)
            .sousFamille(UPDATED_SOUS_FAMILLE)
            .tribu(UPDATED_TRIBU)
            .sousTribu(UPDATED_SOUS_TRIBU)
            .genre(UPDATED_GENRE)
            .sousGenre(UPDATED_SOUS_GENRE)
            .section(UPDATED_SECTION)
            .sousSection(UPDATED_SOUS_SECTION)
            .espece(UPDATED_ESPECE)
            .sousEspece(UPDATED_SOUS_ESPECE)
            .variete(UPDATED_VARIETE)
            .sousVariete(UPDATED_SOUS_VARIETE)
            .forme(UPDATED_FORME);

        restCronquistPlanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCronquistPlante.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCronquistPlante))
            )
            .andExpect(status().isOk());

        // Validate the CronquistPlante in the database
        List<CronquistPlante> cronquistPlanteList = cronquistPlanteRepository.findAll();
        assertThat(cronquistPlanteList).hasSize(databaseSizeBeforeUpdate);
        CronquistPlante testCronquistPlante = cronquistPlanteList.get(cronquistPlanteList.size() - 1);
        assertThat(testCronquistPlante.getSuperRegne()).isEqualTo(UPDATED_SUPER_REGNE);
        assertThat(testCronquistPlante.getRegne()).isEqualTo(UPDATED_REGNE);
        assertThat(testCronquistPlante.getSousRegne()).isEqualTo(UPDATED_SOUS_REGNE);
        assertThat(testCronquistPlante.getRameau()).isEqualTo(UPDATED_RAMEAU);
        assertThat(testCronquistPlante.getInfraRegne()).isEqualTo(UPDATED_INFRA_REGNE);
        assertThat(testCronquistPlante.getSuperDivision()).isEqualTo(UPDATED_SUPER_DIVISION);
        assertThat(testCronquistPlante.getDivision()).isEqualTo(UPDATED_DIVISION);
        assertThat(testCronquistPlante.getSousDivision()).isEqualTo(UPDATED_SOUS_DIVISION);
        assertThat(testCronquistPlante.getInfraEmbranchement()).isEqualTo(UPDATED_INFRA_EMBRANCHEMENT);
        assertThat(testCronquistPlante.getMicroEmbranchement()).isEqualTo(UPDATED_MICRO_EMBRANCHEMENT);
        assertThat(testCronquistPlante.getSuperClasse()).isEqualTo(UPDATED_SUPER_CLASSE);
        assertThat(testCronquistPlante.getClasse()).isEqualTo(UPDATED_CLASSE);
        assertThat(testCronquistPlante.getSousClasse()).isEqualTo(UPDATED_SOUS_CLASSE);
        assertThat(testCronquistPlante.getInfraClasse()).isEqualTo(UPDATED_INFRA_CLASSE);
        assertThat(testCronquistPlante.getSuperOrdre()).isEqualTo(UPDATED_SUPER_ORDRE);
        assertThat(testCronquistPlante.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testCronquistPlante.getSousOrdre()).isEqualTo(UPDATED_SOUS_ORDRE);
        assertThat(testCronquistPlante.getInfraOrdre()).isEqualTo(UPDATED_INFRA_ORDRE);
        assertThat(testCronquistPlante.getMicroOrdre()).isEqualTo(UPDATED_MICRO_ORDRE);
        assertThat(testCronquistPlante.getSuperFamille()).isEqualTo(UPDATED_SUPER_FAMILLE);
        assertThat(testCronquistPlante.getFamille()).isEqualTo(UPDATED_FAMILLE);
        assertThat(testCronquistPlante.getSousFamille()).isEqualTo(UPDATED_SOUS_FAMILLE);
        assertThat(testCronquistPlante.getTribu()).isEqualTo(UPDATED_TRIBU);
        assertThat(testCronquistPlante.getSousTribu()).isEqualTo(UPDATED_SOUS_TRIBU);
        assertThat(testCronquistPlante.getGenre()).isEqualTo(UPDATED_GENRE);
        assertThat(testCronquistPlante.getSousGenre()).isEqualTo(UPDATED_SOUS_GENRE);
        assertThat(testCronquistPlante.getSection()).isEqualTo(UPDATED_SECTION);
        assertThat(testCronquistPlante.getSousSection()).isEqualTo(UPDATED_SOUS_SECTION);
        assertThat(testCronquistPlante.getEspece()).isEqualTo(UPDATED_ESPECE);
        assertThat(testCronquistPlante.getSousEspece()).isEqualTo(UPDATED_SOUS_ESPECE);
        assertThat(testCronquistPlante.getVariete()).isEqualTo(UPDATED_VARIETE);
        assertThat(testCronquistPlante.getSousVariete()).isEqualTo(UPDATED_SOUS_VARIETE);
        assertThat(testCronquistPlante.getForme()).isEqualTo(UPDATED_FORME);
    }

    @Test
    @Transactional
    void putNonExistingCronquistPlante() throws Exception {
        int databaseSizeBeforeUpdate = cronquistPlanteRepository.findAll().size();
        cronquistPlante.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCronquistPlanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cronquistPlante.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cronquistPlante))
            )
            .andExpect(status().isBadRequest());

        // Validate the CronquistPlante in the database
        List<CronquistPlante> cronquistPlanteList = cronquistPlanteRepository.findAll();
        assertThat(cronquistPlanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCronquistPlante() throws Exception {
        int databaseSizeBeforeUpdate = cronquistPlanteRepository.findAll().size();
        cronquistPlante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCronquistPlanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cronquistPlante))
            )
            .andExpect(status().isBadRequest());

        // Validate the CronquistPlante in the database
        List<CronquistPlante> cronquistPlanteList = cronquistPlanteRepository.findAll();
        assertThat(cronquistPlanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCronquistPlante() throws Exception {
        int databaseSizeBeforeUpdate = cronquistPlanteRepository.findAll().size();
        cronquistPlante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCronquistPlanteMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cronquistPlante))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CronquistPlante in the database
        List<CronquistPlante> cronquistPlanteList = cronquistPlanteRepository.findAll();
        assertThat(cronquistPlanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCronquistPlanteWithPatch() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        int databaseSizeBeforeUpdate = cronquistPlanteRepository.findAll().size();

        // Update the cronquistPlante using partial update
        CronquistPlante partialUpdatedCronquistPlante = new CronquistPlante();
        partialUpdatedCronquistPlante.setId(cronquistPlante.getId());

        partialUpdatedCronquistPlante
            .rameau(UPDATED_RAMEAU)
            .superDivision(UPDATED_SUPER_DIVISION)
            .sousClasse(UPDATED_SOUS_CLASSE)
            .superOrdre(UPDATED_SUPER_ORDRE)
            .infraOrdre(UPDATED_INFRA_ORDRE)
            .microOrdre(UPDATED_MICRO_ORDRE)
            .famille(UPDATED_FAMILLE)
            .genre(UPDATED_GENRE)
            .sousGenre(UPDATED_SOUS_GENRE)
            .sousVariete(UPDATED_SOUS_VARIETE);

        restCronquistPlanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCronquistPlante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCronquistPlante))
            )
            .andExpect(status().isOk());

        // Validate the CronquistPlante in the database
        List<CronquistPlante> cronquistPlanteList = cronquistPlanteRepository.findAll();
        assertThat(cronquistPlanteList).hasSize(databaseSizeBeforeUpdate);
        CronquistPlante testCronquistPlante = cronquistPlanteList.get(cronquistPlanteList.size() - 1);
        assertThat(testCronquistPlante.getSuperRegne()).isEqualTo(DEFAULT_SUPER_REGNE);
        assertThat(testCronquistPlante.getRegne()).isEqualTo(DEFAULT_REGNE);
        assertThat(testCronquistPlante.getSousRegne()).isEqualTo(DEFAULT_SOUS_REGNE);
        assertThat(testCronquistPlante.getRameau()).isEqualTo(UPDATED_RAMEAU);
        assertThat(testCronquistPlante.getInfraRegne()).isEqualTo(DEFAULT_INFRA_REGNE);
        assertThat(testCronquistPlante.getSuperDivision()).isEqualTo(UPDATED_SUPER_DIVISION);
        assertThat(testCronquistPlante.getDivision()).isEqualTo(DEFAULT_DIVISION);
        assertThat(testCronquistPlante.getSousDivision()).isEqualTo(DEFAULT_SOUS_DIVISION);
        assertThat(testCronquistPlante.getInfraEmbranchement()).isEqualTo(DEFAULT_INFRA_EMBRANCHEMENT);
        assertThat(testCronquistPlante.getMicroEmbranchement()).isEqualTo(DEFAULT_MICRO_EMBRANCHEMENT);
        assertThat(testCronquistPlante.getSuperClasse()).isEqualTo(DEFAULT_SUPER_CLASSE);
        assertThat(testCronquistPlante.getClasse()).isEqualTo(DEFAULT_CLASSE);
        assertThat(testCronquistPlante.getSousClasse()).isEqualTo(UPDATED_SOUS_CLASSE);
        assertThat(testCronquistPlante.getInfraClasse()).isEqualTo(DEFAULT_INFRA_CLASSE);
        assertThat(testCronquistPlante.getSuperOrdre()).isEqualTo(UPDATED_SUPER_ORDRE);
        assertThat(testCronquistPlante.getOrdre()).isEqualTo(DEFAULT_ORDRE);
        assertThat(testCronquistPlante.getSousOrdre()).isEqualTo(DEFAULT_SOUS_ORDRE);
        assertThat(testCronquistPlante.getInfraOrdre()).isEqualTo(UPDATED_INFRA_ORDRE);
        assertThat(testCronquistPlante.getMicroOrdre()).isEqualTo(UPDATED_MICRO_ORDRE);
        assertThat(testCronquistPlante.getSuperFamille()).isEqualTo(DEFAULT_SUPER_FAMILLE);
        assertThat(testCronquistPlante.getFamille()).isEqualTo(UPDATED_FAMILLE);
        assertThat(testCronquistPlante.getSousFamille()).isEqualTo(DEFAULT_SOUS_FAMILLE);
        assertThat(testCronquistPlante.getTribu()).isEqualTo(DEFAULT_TRIBU);
        assertThat(testCronquistPlante.getSousTribu()).isEqualTo(DEFAULT_SOUS_TRIBU);
        assertThat(testCronquistPlante.getGenre()).isEqualTo(UPDATED_GENRE);
        assertThat(testCronquistPlante.getSousGenre()).isEqualTo(UPDATED_SOUS_GENRE);
        assertThat(testCronquistPlante.getSection()).isEqualTo(DEFAULT_SECTION);
        assertThat(testCronquistPlante.getSousSection()).isEqualTo(DEFAULT_SOUS_SECTION);
        assertThat(testCronquistPlante.getEspece()).isEqualTo(DEFAULT_ESPECE);
        assertThat(testCronquistPlante.getSousEspece()).isEqualTo(DEFAULT_SOUS_ESPECE);
        assertThat(testCronquistPlante.getVariete()).isEqualTo(DEFAULT_VARIETE);
        assertThat(testCronquistPlante.getSousVariete()).isEqualTo(UPDATED_SOUS_VARIETE);
        assertThat(testCronquistPlante.getForme()).isEqualTo(DEFAULT_FORME);
    }

    @Test
    @Transactional
    void fullUpdateCronquistPlanteWithPatch() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        int databaseSizeBeforeUpdate = cronquistPlanteRepository.findAll().size();

        // Update the cronquistPlante using partial update
        CronquistPlante partialUpdatedCronquistPlante = new CronquistPlante();
        partialUpdatedCronquistPlante.setId(cronquistPlante.getId());

        partialUpdatedCronquistPlante
            .superRegne(UPDATED_SUPER_REGNE)
            .regne(UPDATED_REGNE)
            .sousRegne(UPDATED_SOUS_REGNE)
            .rameau(UPDATED_RAMEAU)
            .infraRegne(UPDATED_INFRA_REGNE)
            .superDivision(UPDATED_SUPER_DIVISION)
            .division(UPDATED_DIVISION)
            .sousDivision(UPDATED_SOUS_DIVISION)
            .infraEmbranchement(UPDATED_INFRA_EMBRANCHEMENT)
            .microEmbranchement(UPDATED_MICRO_EMBRANCHEMENT)
            .superClasse(UPDATED_SUPER_CLASSE)
            .classe(UPDATED_CLASSE)
            .sousClasse(UPDATED_SOUS_CLASSE)
            .infraClasse(UPDATED_INFRA_CLASSE)
            .superOrdre(UPDATED_SUPER_ORDRE)
            .ordre(UPDATED_ORDRE)
            .sousOrdre(UPDATED_SOUS_ORDRE)
            .infraOrdre(UPDATED_INFRA_ORDRE)
            .microOrdre(UPDATED_MICRO_ORDRE)
            .superFamille(UPDATED_SUPER_FAMILLE)
            .famille(UPDATED_FAMILLE)
            .sousFamille(UPDATED_SOUS_FAMILLE)
            .tribu(UPDATED_TRIBU)
            .sousTribu(UPDATED_SOUS_TRIBU)
            .genre(UPDATED_GENRE)
            .sousGenre(UPDATED_SOUS_GENRE)
            .section(UPDATED_SECTION)
            .sousSection(UPDATED_SOUS_SECTION)
            .espece(UPDATED_ESPECE)
            .sousEspece(UPDATED_SOUS_ESPECE)
            .variete(UPDATED_VARIETE)
            .sousVariete(UPDATED_SOUS_VARIETE)
            .forme(UPDATED_FORME);

        restCronquistPlanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCronquistPlante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCronquistPlante))
            )
            .andExpect(status().isOk());

        // Validate the CronquistPlante in the database
        List<CronquistPlante> cronquistPlanteList = cronquistPlanteRepository.findAll();
        assertThat(cronquistPlanteList).hasSize(databaseSizeBeforeUpdate);
        CronquistPlante testCronquistPlante = cronquistPlanteList.get(cronquistPlanteList.size() - 1);
        assertThat(testCronquistPlante.getSuperRegne()).isEqualTo(UPDATED_SUPER_REGNE);
        assertThat(testCronquistPlante.getRegne()).isEqualTo(UPDATED_REGNE);
        assertThat(testCronquistPlante.getSousRegne()).isEqualTo(UPDATED_SOUS_REGNE);
        assertThat(testCronquistPlante.getRameau()).isEqualTo(UPDATED_RAMEAU);
        assertThat(testCronquistPlante.getInfraRegne()).isEqualTo(UPDATED_INFRA_REGNE);
        assertThat(testCronquistPlante.getSuperDivision()).isEqualTo(UPDATED_SUPER_DIVISION);
        assertThat(testCronquistPlante.getDivision()).isEqualTo(UPDATED_DIVISION);
        assertThat(testCronquistPlante.getSousDivision()).isEqualTo(UPDATED_SOUS_DIVISION);
        assertThat(testCronquistPlante.getInfraEmbranchement()).isEqualTo(UPDATED_INFRA_EMBRANCHEMENT);
        assertThat(testCronquistPlante.getMicroEmbranchement()).isEqualTo(UPDATED_MICRO_EMBRANCHEMENT);
        assertThat(testCronquistPlante.getSuperClasse()).isEqualTo(UPDATED_SUPER_CLASSE);
        assertThat(testCronquistPlante.getClasse()).isEqualTo(UPDATED_CLASSE);
        assertThat(testCronquistPlante.getSousClasse()).isEqualTo(UPDATED_SOUS_CLASSE);
        assertThat(testCronquistPlante.getInfraClasse()).isEqualTo(UPDATED_INFRA_CLASSE);
        assertThat(testCronquistPlante.getSuperOrdre()).isEqualTo(UPDATED_SUPER_ORDRE);
        assertThat(testCronquistPlante.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testCronquistPlante.getSousOrdre()).isEqualTo(UPDATED_SOUS_ORDRE);
        assertThat(testCronquistPlante.getInfraOrdre()).isEqualTo(UPDATED_INFRA_ORDRE);
        assertThat(testCronquistPlante.getMicroOrdre()).isEqualTo(UPDATED_MICRO_ORDRE);
        assertThat(testCronquistPlante.getSuperFamille()).isEqualTo(UPDATED_SUPER_FAMILLE);
        assertThat(testCronquistPlante.getFamille()).isEqualTo(UPDATED_FAMILLE);
        assertThat(testCronquistPlante.getSousFamille()).isEqualTo(UPDATED_SOUS_FAMILLE);
        assertThat(testCronquistPlante.getTribu()).isEqualTo(UPDATED_TRIBU);
        assertThat(testCronquistPlante.getSousTribu()).isEqualTo(UPDATED_SOUS_TRIBU);
        assertThat(testCronquistPlante.getGenre()).isEqualTo(UPDATED_GENRE);
        assertThat(testCronquistPlante.getSousGenre()).isEqualTo(UPDATED_SOUS_GENRE);
        assertThat(testCronquistPlante.getSection()).isEqualTo(UPDATED_SECTION);
        assertThat(testCronquistPlante.getSousSection()).isEqualTo(UPDATED_SOUS_SECTION);
        assertThat(testCronquistPlante.getEspece()).isEqualTo(UPDATED_ESPECE);
        assertThat(testCronquistPlante.getSousEspece()).isEqualTo(UPDATED_SOUS_ESPECE);
        assertThat(testCronquistPlante.getVariete()).isEqualTo(UPDATED_VARIETE);
        assertThat(testCronquistPlante.getSousVariete()).isEqualTo(UPDATED_SOUS_VARIETE);
        assertThat(testCronquistPlante.getForme()).isEqualTo(UPDATED_FORME);
    }

    @Test
    @Transactional
    void patchNonExistingCronquistPlante() throws Exception {
        int databaseSizeBeforeUpdate = cronquistPlanteRepository.findAll().size();
        cronquistPlante.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCronquistPlanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cronquistPlante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cronquistPlante))
            )
            .andExpect(status().isBadRequest());

        // Validate the CronquistPlante in the database
        List<CronquistPlante> cronquistPlanteList = cronquistPlanteRepository.findAll();
        assertThat(cronquistPlanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCronquistPlante() throws Exception {
        int databaseSizeBeforeUpdate = cronquistPlanteRepository.findAll().size();
        cronquistPlante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCronquistPlanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cronquistPlante))
            )
            .andExpect(status().isBadRequest());

        // Validate the CronquistPlante in the database
        List<CronquistPlante> cronquistPlanteList = cronquistPlanteRepository.findAll();
        assertThat(cronquistPlanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCronquistPlante() throws Exception {
        int databaseSizeBeforeUpdate = cronquistPlanteRepository.findAll().size();
        cronquistPlante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCronquistPlanteMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cronquistPlante))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CronquistPlante in the database
        List<CronquistPlante> cronquistPlanteList = cronquistPlanteRepository.findAll();
        assertThat(cronquistPlanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCronquistPlante() throws Exception {
        // Initialize the database
        cronquistPlanteRepository.saveAndFlush(cronquistPlante);

        int databaseSizeBeforeDelete = cronquistPlanteRepository.findAll().size();

        // Delete the cronquistPlante
        restCronquistPlanteMockMvc
            .perform(delete(ENTITY_API_URL_ID, cronquistPlante.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CronquistPlante> cronquistPlanteList = cronquistPlanteRepository.findAll();
        assertThat(cronquistPlanteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
