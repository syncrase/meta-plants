package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.ClassificationCronquist;
import fr.syncrase.ecosyst.domain.Plante;
import fr.syncrase.ecosyst.repository.ClassificationCronquistRepository;
import fr.syncrase.ecosyst.service.criteria.ClassificationCronquistCriteria;
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
 * Integration tests for the {@link ClassificationCronquistResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClassificationCronquistResourceIT {

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

    private static final String DEFAULT_SUPER_EMBRANCHEMENT = "AAAAAAAAAA";
    private static final String UPDATED_SUPER_EMBRANCHEMENT = "BBBBBBBBBB";

    private static final String DEFAULT_DIVISION = "AAAAAAAAAA";
    private static final String UPDATED_DIVISION = "BBBBBBBBBB";

    private static final String DEFAULT_SOUS_EMBRANCHEMENT = "AAAAAAAAAA";
    private static final String UPDATED_SOUS_EMBRANCHEMENT = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/classification-cronquists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClassificationCronquistRepository classificationCronquistRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClassificationCronquistMockMvc;

    private ClassificationCronquist classificationCronquist;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassificationCronquist createEntity(EntityManager em) {
        ClassificationCronquist classificationCronquist = new ClassificationCronquist()
            .superRegne(DEFAULT_SUPER_REGNE)
            .regne(DEFAULT_REGNE)
            .sousRegne(DEFAULT_SOUS_REGNE)
            .rameau(DEFAULT_RAMEAU)
            .infraRegne(DEFAULT_INFRA_REGNE)
            .superEmbranchement(DEFAULT_SUPER_EMBRANCHEMENT)
            .division(DEFAULT_DIVISION)
            .sousEmbranchement(DEFAULT_SOUS_EMBRANCHEMENT)
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
        return classificationCronquist;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassificationCronquist createUpdatedEntity(EntityManager em) {
        ClassificationCronquist classificationCronquist = new ClassificationCronquist()
            .superRegne(UPDATED_SUPER_REGNE)
            .regne(UPDATED_REGNE)
            .sousRegne(UPDATED_SOUS_REGNE)
            .rameau(UPDATED_RAMEAU)
            .infraRegne(UPDATED_INFRA_REGNE)
            .superEmbranchement(UPDATED_SUPER_EMBRANCHEMENT)
            .division(UPDATED_DIVISION)
            .sousEmbranchement(UPDATED_SOUS_EMBRANCHEMENT)
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
        return classificationCronquist;
    }

    @BeforeEach
    public void initTest() {
        classificationCronquist = createEntity(em);
    }

    @Test
    @Transactional
    void createClassificationCronquist() throws Exception {
        int databaseSizeBeforeCreate = classificationCronquistRepository.findAll().size();
        // Create the ClassificationCronquist
        restClassificationCronquistMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classificationCronquist))
            )
            .andExpect(status().isCreated());

        // Validate the ClassificationCronquist in the database
        List<ClassificationCronquist> classificationCronquistList = classificationCronquistRepository.findAll();
        assertThat(classificationCronquistList).hasSize(databaseSizeBeforeCreate + 1);
        ClassificationCronquist testClassificationCronquist = classificationCronquistList.get(classificationCronquistList.size() - 1);
        assertThat(testClassificationCronquist.getSuperRegne()).isEqualTo(DEFAULT_SUPER_REGNE);
        assertThat(testClassificationCronquist.getRegne()).isEqualTo(DEFAULT_REGNE);
        assertThat(testClassificationCronquist.getSousRegne()).isEqualTo(DEFAULT_SOUS_REGNE);
        assertThat(testClassificationCronquist.getRameau()).isEqualTo(DEFAULT_RAMEAU);
        assertThat(testClassificationCronquist.getInfraRegne()).isEqualTo(DEFAULT_INFRA_REGNE);
        assertThat(testClassificationCronquist.getSuperEmbranchement()).isEqualTo(DEFAULT_SUPER_EMBRANCHEMENT);
        assertThat(testClassificationCronquist.getDivision()).isEqualTo(DEFAULT_DIVISION);
        assertThat(testClassificationCronquist.getSousEmbranchement()).isEqualTo(DEFAULT_SOUS_EMBRANCHEMENT);
        assertThat(testClassificationCronquist.getInfraEmbranchement()).isEqualTo(DEFAULT_INFRA_EMBRANCHEMENT);
        assertThat(testClassificationCronquist.getMicroEmbranchement()).isEqualTo(DEFAULT_MICRO_EMBRANCHEMENT);
        assertThat(testClassificationCronquist.getSuperClasse()).isEqualTo(DEFAULT_SUPER_CLASSE);
        assertThat(testClassificationCronquist.getClasse()).isEqualTo(DEFAULT_CLASSE);
        assertThat(testClassificationCronquist.getSousClasse()).isEqualTo(DEFAULT_SOUS_CLASSE);
        assertThat(testClassificationCronquist.getInfraClasse()).isEqualTo(DEFAULT_INFRA_CLASSE);
        assertThat(testClassificationCronquist.getSuperOrdre()).isEqualTo(DEFAULT_SUPER_ORDRE);
        assertThat(testClassificationCronquist.getOrdre()).isEqualTo(DEFAULT_ORDRE);
        assertThat(testClassificationCronquist.getSousOrdre()).isEqualTo(DEFAULT_SOUS_ORDRE);
        assertThat(testClassificationCronquist.getInfraOrdre()).isEqualTo(DEFAULT_INFRA_ORDRE);
        assertThat(testClassificationCronquist.getMicroOrdre()).isEqualTo(DEFAULT_MICRO_ORDRE);
        assertThat(testClassificationCronquist.getSuperFamille()).isEqualTo(DEFAULT_SUPER_FAMILLE);
        assertThat(testClassificationCronquist.getFamille()).isEqualTo(DEFAULT_FAMILLE);
        assertThat(testClassificationCronquist.getSousFamille()).isEqualTo(DEFAULT_SOUS_FAMILLE);
        assertThat(testClassificationCronquist.getTribu()).isEqualTo(DEFAULT_TRIBU);
        assertThat(testClassificationCronquist.getSousTribu()).isEqualTo(DEFAULT_SOUS_TRIBU);
        assertThat(testClassificationCronquist.getGenre()).isEqualTo(DEFAULT_GENRE);
        assertThat(testClassificationCronquist.getSousGenre()).isEqualTo(DEFAULT_SOUS_GENRE);
        assertThat(testClassificationCronquist.getSection()).isEqualTo(DEFAULT_SECTION);
        assertThat(testClassificationCronquist.getSousSection()).isEqualTo(DEFAULT_SOUS_SECTION);
        assertThat(testClassificationCronquist.getEspece()).isEqualTo(DEFAULT_ESPECE);
        assertThat(testClassificationCronquist.getSousEspece()).isEqualTo(DEFAULT_SOUS_ESPECE);
        assertThat(testClassificationCronquist.getVariete()).isEqualTo(DEFAULT_VARIETE);
        assertThat(testClassificationCronquist.getSousVariete()).isEqualTo(DEFAULT_SOUS_VARIETE);
        assertThat(testClassificationCronquist.getForme()).isEqualTo(DEFAULT_FORME);
    }

    @Test
    @Transactional
    void createClassificationCronquistWithExistingId() throws Exception {
        // Create the ClassificationCronquist with an existing ID
        classificationCronquist.setId(1L);

        int databaseSizeBeforeCreate = classificationCronquistRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClassificationCronquistMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classificationCronquist))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassificationCronquist in the database
        List<ClassificationCronquist> classificationCronquistList = classificationCronquistRepository.findAll();
        assertThat(classificationCronquistList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllClassificationCronquists() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList
        restClassificationCronquistMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classificationCronquist.getId().intValue())))
            .andExpect(jsonPath("$.[*].superRegne").value(hasItem(DEFAULT_SUPER_REGNE)))
            .andExpect(jsonPath("$.[*].regne").value(hasItem(DEFAULT_REGNE)))
            .andExpect(jsonPath("$.[*].sousRegne").value(hasItem(DEFAULT_SOUS_REGNE)))
            .andExpect(jsonPath("$.[*].rameau").value(hasItem(DEFAULT_RAMEAU)))
            .andExpect(jsonPath("$.[*].infraRegne").value(hasItem(DEFAULT_INFRA_REGNE)))
            .andExpect(jsonPath("$.[*].superEmbranchement").value(hasItem(DEFAULT_SUPER_EMBRANCHEMENT)))
            .andExpect(jsonPath("$.[*].division").value(hasItem(DEFAULT_DIVISION)))
            .andExpect(jsonPath("$.[*].sousEmbranchement").value(hasItem(DEFAULT_SOUS_EMBRANCHEMENT)))
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
    void getClassificationCronquist() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get the classificationCronquist
        restClassificationCronquistMockMvc
            .perform(get(ENTITY_API_URL_ID, classificationCronquist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(classificationCronquist.getId().intValue()))
            .andExpect(jsonPath("$.superRegne").value(DEFAULT_SUPER_REGNE))
            .andExpect(jsonPath("$.regne").value(DEFAULT_REGNE))
            .andExpect(jsonPath("$.sousRegne").value(DEFAULT_SOUS_REGNE))
            .andExpect(jsonPath("$.rameau").value(DEFAULT_RAMEAU))
            .andExpect(jsonPath("$.infraRegne").value(DEFAULT_INFRA_REGNE))
            .andExpect(jsonPath("$.superEmbranchement").value(DEFAULT_SUPER_EMBRANCHEMENT))
            .andExpect(jsonPath("$.division").value(DEFAULT_DIVISION))
            .andExpect(jsonPath("$.sousEmbranchement").value(DEFAULT_SOUS_EMBRANCHEMENT))
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
    void getClassificationCronquistsByIdFiltering() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        Long id = classificationCronquist.getId();

        defaultClassificationCronquistShouldBeFound("id.equals=" + id);
        defaultClassificationCronquistShouldNotBeFound("id.notEquals=" + id);

        defaultClassificationCronquistShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultClassificationCronquistShouldNotBeFound("id.greaterThan=" + id);

        defaultClassificationCronquistShouldBeFound("id.lessThanOrEqual=" + id);
        defaultClassificationCronquistShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySuperRegneIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where superRegne equals to DEFAULT_SUPER_REGNE
        defaultClassificationCronquistShouldBeFound("superRegne.equals=" + DEFAULT_SUPER_REGNE);

        // Get all the classificationCronquistList where superRegne equals to UPDATED_SUPER_REGNE
        defaultClassificationCronquistShouldNotBeFound("superRegne.equals=" + UPDATED_SUPER_REGNE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySuperRegneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where superRegne not equals to DEFAULT_SUPER_REGNE
        defaultClassificationCronquistShouldNotBeFound("superRegne.notEquals=" + DEFAULT_SUPER_REGNE);

        // Get all the classificationCronquistList where superRegne not equals to UPDATED_SUPER_REGNE
        defaultClassificationCronquistShouldBeFound("superRegne.notEquals=" + UPDATED_SUPER_REGNE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySuperRegneIsInShouldWork() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where superRegne in DEFAULT_SUPER_REGNE or UPDATED_SUPER_REGNE
        defaultClassificationCronquistShouldBeFound("superRegne.in=" + DEFAULT_SUPER_REGNE + "," + UPDATED_SUPER_REGNE);

        // Get all the classificationCronquistList where superRegne equals to UPDATED_SUPER_REGNE
        defaultClassificationCronquistShouldNotBeFound("superRegne.in=" + UPDATED_SUPER_REGNE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySuperRegneIsNullOrNotNull() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where superRegne is not null
        defaultClassificationCronquistShouldBeFound("superRegne.specified=true");

        // Get all the classificationCronquistList where superRegne is null
        defaultClassificationCronquistShouldNotBeFound("superRegne.specified=false");
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySuperRegneContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where superRegne contains DEFAULT_SUPER_REGNE
        defaultClassificationCronquistShouldBeFound("superRegne.contains=" + DEFAULT_SUPER_REGNE);

        // Get all the classificationCronquistList where superRegne contains UPDATED_SUPER_REGNE
        defaultClassificationCronquistShouldNotBeFound("superRegne.contains=" + UPDATED_SUPER_REGNE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySuperRegneNotContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where superRegne does not contain DEFAULT_SUPER_REGNE
        defaultClassificationCronquistShouldNotBeFound("superRegne.doesNotContain=" + DEFAULT_SUPER_REGNE);

        // Get all the classificationCronquistList where superRegne does not contain UPDATED_SUPER_REGNE
        defaultClassificationCronquistShouldBeFound("superRegne.doesNotContain=" + UPDATED_SUPER_REGNE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByRegneIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where regne equals to DEFAULT_REGNE
        defaultClassificationCronquistShouldBeFound("regne.equals=" + DEFAULT_REGNE);

        // Get all the classificationCronquistList where regne equals to UPDATED_REGNE
        defaultClassificationCronquistShouldNotBeFound("regne.equals=" + UPDATED_REGNE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByRegneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where regne not equals to DEFAULT_REGNE
        defaultClassificationCronquistShouldNotBeFound("regne.notEquals=" + DEFAULT_REGNE);

        // Get all the classificationCronquistList where regne not equals to UPDATED_REGNE
        defaultClassificationCronquistShouldBeFound("regne.notEquals=" + UPDATED_REGNE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByRegneIsInShouldWork() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where regne in DEFAULT_REGNE or UPDATED_REGNE
        defaultClassificationCronquistShouldBeFound("regne.in=" + DEFAULT_REGNE + "," + UPDATED_REGNE);

        // Get all the classificationCronquistList where regne equals to UPDATED_REGNE
        defaultClassificationCronquistShouldNotBeFound("regne.in=" + UPDATED_REGNE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByRegneIsNullOrNotNull() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where regne is not null
        defaultClassificationCronquistShouldBeFound("regne.specified=true");

        // Get all the classificationCronquistList where regne is null
        defaultClassificationCronquistShouldNotBeFound("regne.specified=false");
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByRegneContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where regne contains DEFAULT_REGNE
        defaultClassificationCronquistShouldBeFound("regne.contains=" + DEFAULT_REGNE);

        // Get all the classificationCronquistList where regne contains UPDATED_REGNE
        defaultClassificationCronquistShouldNotBeFound("regne.contains=" + UPDATED_REGNE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByRegneNotContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where regne does not contain DEFAULT_REGNE
        defaultClassificationCronquistShouldNotBeFound("regne.doesNotContain=" + DEFAULT_REGNE);

        // Get all the classificationCronquistList where regne does not contain UPDATED_REGNE
        defaultClassificationCronquistShouldBeFound("regne.doesNotContain=" + UPDATED_REGNE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousRegneIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousRegne equals to DEFAULT_SOUS_REGNE
        defaultClassificationCronquistShouldBeFound("sousRegne.equals=" + DEFAULT_SOUS_REGNE);

        // Get all the classificationCronquistList where sousRegne equals to UPDATED_SOUS_REGNE
        defaultClassificationCronquistShouldNotBeFound("sousRegne.equals=" + UPDATED_SOUS_REGNE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousRegneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousRegne not equals to DEFAULT_SOUS_REGNE
        defaultClassificationCronquistShouldNotBeFound("sousRegne.notEquals=" + DEFAULT_SOUS_REGNE);

        // Get all the classificationCronquistList where sousRegne not equals to UPDATED_SOUS_REGNE
        defaultClassificationCronquistShouldBeFound("sousRegne.notEquals=" + UPDATED_SOUS_REGNE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousRegneIsInShouldWork() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousRegne in DEFAULT_SOUS_REGNE or UPDATED_SOUS_REGNE
        defaultClassificationCronquistShouldBeFound("sousRegne.in=" + DEFAULT_SOUS_REGNE + "," + UPDATED_SOUS_REGNE);

        // Get all the classificationCronquistList where sousRegne equals to UPDATED_SOUS_REGNE
        defaultClassificationCronquistShouldNotBeFound("sousRegne.in=" + UPDATED_SOUS_REGNE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousRegneIsNullOrNotNull() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousRegne is not null
        defaultClassificationCronquistShouldBeFound("sousRegne.specified=true");

        // Get all the classificationCronquistList where sousRegne is null
        defaultClassificationCronquistShouldNotBeFound("sousRegne.specified=false");
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousRegneContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousRegne contains DEFAULT_SOUS_REGNE
        defaultClassificationCronquistShouldBeFound("sousRegne.contains=" + DEFAULT_SOUS_REGNE);

        // Get all the classificationCronquistList where sousRegne contains UPDATED_SOUS_REGNE
        defaultClassificationCronquistShouldNotBeFound("sousRegne.contains=" + UPDATED_SOUS_REGNE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousRegneNotContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousRegne does not contain DEFAULT_SOUS_REGNE
        defaultClassificationCronquistShouldNotBeFound("sousRegne.doesNotContain=" + DEFAULT_SOUS_REGNE);

        // Get all the classificationCronquistList where sousRegne does not contain UPDATED_SOUS_REGNE
        defaultClassificationCronquistShouldBeFound("sousRegne.doesNotContain=" + UPDATED_SOUS_REGNE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByRameauIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where rameau equals to DEFAULT_RAMEAU
        defaultClassificationCronquistShouldBeFound("rameau.equals=" + DEFAULT_RAMEAU);

        // Get all the classificationCronquistList where rameau equals to UPDATED_RAMEAU
        defaultClassificationCronquistShouldNotBeFound("rameau.equals=" + UPDATED_RAMEAU);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByRameauIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where rameau not equals to DEFAULT_RAMEAU
        defaultClassificationCronquistShouldNotBeFound("rameau.notEquals=" + DEFAULT_RAMEAU);

        // Get all the classificationCronquistList where rameau not equals to UPDATED_RAMEAU
        defaultClassificationCronquistShouldBeFound("rameau.notEquals=" + UPDATED_RAMEAU);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByRameauIsInShouldWork() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where rameau in DEFAULT_RAMEAU or UPDATED_RAMEAU
        defaultClassificationCronquistShouldBeFound("rameau.in=" + DEFAULT_RAMEAU + "," + UPDATED_RAMEAU);

        // Get all the classificationCronquistList where rameau equals to UPDATED_RAMEAU
        defaultClassificationCronquistShouldNotBeFound("rameau.in=" + UPDATED_RAMEAU);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByRameauIsNullOrNotNull() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where rameau is not null
        defaultClassificationCronquistShouldBeFound("rameau.specified=true");

        // Get all the classificationCronquistList where rameau is null
        defaultClassificationCronquistShouldNotBeFound("rameau.specified=false");
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByRameauContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where rameau contains DEFAULT_RAMEAU
        defaultClassificationCronquistShouldBeFound("rameau.contains=" + DEFAULT_RAMEAU);

        // Get all the classificationCronquistList where rameau contains UPDATED_RAMEAU
        defaultClassificationCronquistShouldNotBeFound("rameau.contains=" + UPDATED_RAMEAU);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByRameauNotContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where rameau does not contain DEFAULT_RAMEAU
        defaultClassificationCronquistShouldNotBeFound("rameau.doesNotContain=" + DEFAULT_RAMEAU);

        // Get all the classificationCronquistList where rameau does not contain UPDATED_RAMEAU
        defaultClassificationCronquistShouldBeFound("rameau.doesNotContain=" + UPDATED_RAMEAU);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByInfraRegneIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where infraRegne equals to DEFAULT_INFRA_REGNE
        defaultClassificationCronquistShouldBeFound("infraRegne.equals=" + DEFAULT_INFRA_REGNE);

        // Get all the classificationCronquistList where infraRegne equals to UPDATED_INFRA_REGNE
        defaultClassificationCronquistShouldNotBeFound("infraRegne.equals=" + UPDATED_INFRA_REGNE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByInfraRegneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where infraRegne not equals to DEFAULT_INFRA_REGNE
        defaultClassificationCronquistShouldNotBeFound("infraRegne.notEquals=" + DEFAULT_INFRA_REGNE);

        // Get all the classificationCronquistList where infraRegne not equals to UPDATED_INFRA_REGNE
        defaultClassificationCronquistShouldBeFound("infraRegne.notEquals=" + UPDATED_INFRA_REGNE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByInfraRegneIsInShouldWork() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where infraRegne in DEFAULT_INFRA_REGNE or UPDATED_INFRA_REGNE
        defaultClassificationCronquistShouldBeFound("infraRegne.in=" + DEFAULT_INFRA_REGNE + "," + UPDATED_INFRA_REGNE);

        // Get all the classificationCronquistList where infraRegne equals to UPDATED_INFRA_REGNE
        defaultClassificationCronquistShouldNotBeFound("infraRegne.in=" + UPDATED_INFRA_REGNE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByInfraRegneIsNullOrNotNull() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where infraRegne is not null
        defaultClassificationCronquistShouldBeFound("infraRegne.specified=true");

        // Get all the classificationCronquistList where infraRegne is null
        defaultClassificationCronquistShouldNotBeFound("infraRegne.specified=false");
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByInfraRegneContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where infraRegne contains DEFAULT_INFRA_REGNE
        defaultClassificationCronquistShouldBeFound("infraRegne.contains=" + DEFAULT_INFRA_REGNE);

        // Get all the classificationCronquistList where infraRegne contains UPDATED_INFRA_REGNE
        defaultClassificationCronquistShouldNotBeFound("infraRegne.contains=" + UPDATED_INFRA_REGNE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByInfraRegneNotContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where infraRegne does not contain DEFAULT_INFRA_REGNE
        defaultClassificationCronquistShouldNotBeFound("infraRegne.doesNotContain=" + DEFAULT_INFRA_REGNE);

        // Get all the classificationCronquistList where infraRegne does not contain UPDATED_INFRA_REGNE
        defaultClassificationCronquistShouldBeFound("infraRegne.doesNotContain=" + UPDATED_INFRA_REGNE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySuperEmbranchementIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where superEmbranchement equals to DEFAULT_SUPER_EMBRANCHEMENT
        defaultClassificationCronquistShouldBeFound("superEmbranchement.equals=" + DEFAULT_SUPER_EMBRANCHEMENT);

        // Get all the classificationCronquistList where superEmbranchement equals to UPDATED_SUPER_EMBRANCHEMENT
        defaultClassificationCronquistShouldNotBeFound("superEmbranchement.equals=" + UPDATED_SUPER_EMBRANCHEMENT);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySuperEmbranchementIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where superEmbranchement not equals to DEFAULT_SUPER_EMBRANCHEMENT
        defaultClassificationCronquistShouldNotBeFound("superEmbranchement.notEquals=" + DEFAULT_SUPER_EMBRANCHEMENT);

        // Get all the classificationCronquistList where superEmbranchement not equals to UPDATED_SUPER_EMBRANCHEMENT
        defaultClassificationCronquistShouldBeFound("superEmbranchement.notEquals=" + UPDATED_SUPER_EMBRANCHEMENT);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySuperEmbranchementIsInShouldWork() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where superEmbranchement in DEFAULT_SUPER_EMBRANCHEMENT or UPDATED_SUPER_EMBRANCHEMENT
        defaultClassificationCronquistShouldBeFound(
            "superEmbranchement.in=" + DEFAULT_SUPER_EMBRANCHEMENT + "," + UPDATED_SUPER_EMBRANCHEMENT
        );

        // Get all the classificationCronquistList where superEmbranchement equals to UPDATED_SUPER_EMBRANCHEMENT
        defaultClassificationCronquistShouldNotBeFound("superEmbranchement.in=" + UPDATED_SUPER_EMBRANCHEMENT);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySuperEmbranchementIsNullOrNotNull() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where superEmbranchement is not null
        defaultClassificationCronquistShouldBeFound("superEmbranchement.specified=true");

        // Get all the classificationCronquistList where superEmbranchement is null
        defaultClassificationCronquistShouldNotBeFound("superEmbranchement.specified=false");
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySuperEmbranchementContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where superEmbranchement contains DEFAULT_SUPER_EMBRANCHEMENT
        defaultClassificationCronquistShouldBeFound("superEmbranchement.contains=" + DEFAULT_SUPER_EMBRANCHEMENT);

        // Get all the classificationCronquistList where superEmbranchement contains UPDATED_SUPER_EMBRANCHEMENT
        defaultClassificationCronquistShouldNotBeFound("superEmbranchement.contains=" + UPDATED_SUPER_EMBRANCHEMENT);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySuperEmbranchementNotContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where superEmbranchement does not contain DEFAULT_SUPER_EMBRANCHEMENT
        defaultClassificationCronquistShouldNotBeFound("superEmbranchement.doesNotContain=" + DEFAULT_SUPER_EMBRANCHEMENT);

        // Get all the classificationCronquistList where superEmbranchement does not contain UPDATED_SUPER_EMBRANCHEMENT
        defaultClassificationCronquistShouldBeFound("superEmbranchement.doesNotContain=" + UPDATED_SUPER_EMBRANCHEMENT);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByDivisionIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where division equals to DEFAULT_DIVISION
        defaultClassificationCronquistShouldBeFound("division.equals=" + DEFAULT_DIVISION);

        // Get all the classificationCronquistList where division equals to UPDATED_DIVISION
        defaultClassificationCronquistShouldNotBeFound("division.equals=" + UPDATED_DIVISION);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByDivisionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where division not equals to DEFAULT_DIVISION
        defaultClassificationCronquistShouldNotBeFound("division.notEquals=" + DEFAULT_DIVISION);

        // Get all the classificationCronquistList where division not equals to UPDATED_DIVISION
        defaultClassificationCronquistShouldBeFound("division.notEquals=" + UPDATED_DIVISION);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByDivisionIsInShouldWork() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where division in DEFAULT_DIVISION or UPDATED_DIVISION
        defaultClassificationCronquistShouldBeFound("division.in=" + DEFAULT_DIVISION + "," + UPDATED_DIVISION);

        // Get all the classificationCronquistList where division equals to UPDATED_DIVISION
        defaultClassificationCronquistShouldNotBeFound("division.in=" + UPDATED_DIVISION);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByDivisionIsNullOrNotNull() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where division is not null
        defaultClassificationCronquistShouldBeFound("division.specified=true");

        // Get all the classificationCronquistList where division is null
        defaultClassificationCronquistShouldNotBeFound("division.specified=false");
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByDivisionContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where division contains DEFAULT_DIVISION
        defaultClassificationCronquistShouldBeFound("division.contains=" + DEFAULT_DIVISION);

        // Get all the classificationCronquistList where division contains UPDATED_DIVISION
        defaultClassificationCronquistShouldNotBeFound("division.contains=" + UPDATED_DIVISION);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByDivisionNotContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where division does not contain DEFAULT_DIVISION
        defaultClassificationCronquistShouldNotBeFound("division.doesNotContain=" + DEFAULT_DIVISION);

        // Get all the classificationCronquistList where division does not contain UPDATED_DIVISION
        defaultClassificationCronquistShouldBeFound("division.doesNotContain=" + UPDATED_DIVISION);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousEmbranchementIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousEmbranchement equals to DEFAULT_SOUS_EMBRANCHEMENT
        defaultClassificationCronquistShouldBeFound("sousEmbranchement.equals=" + DEFAULT_SOUS_EMBRANCHEMENT);

        // Get all the classificationCronquistList where sousEmbranchement equals to UPDATED_SOUS_EMBRANCHEMENT
        defaultClassificationCronquistShouldNotBeFound("sousEmbranchement.equals=" + UPDATED_SOUS_EMBRANCHEMENT);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousEmbranchementIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousEmbranchement not equals to DEFAULT_SOUS_EMBRANCHEMENT
        defaultClassificationCronquistShouldNotBeFound("sousEmbranchement.notEquals=" + DEFAULT_SOUS_EMBRANCHEMENT);

        // Get all the classificationCronquistList where sousEmbranchement not equals to UPDATED_SOUS_EMBRANCHEMENT
        defaultClassificationCronquistShouldBeFound("sousEmbranchement.notEquals=" + UPDATED_SOUS_EMBRANCHEMENT);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousEmbranchementIsInShouldWork() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousEmbranchement in DEFAULT_SOUS_EMBRANCHEMENT or UPDATED_SOUS_EMBRANCHEMENT
        defaultClassificationCronquistShouldBeFound(
            "sousEmbranchement.in=" + DEFAULT_SOUS_EMBRANCHEMENT + "," + UPDATED_SOUS_EMBRANCHEMENT
        );

        // Get all the classificationCronquistList where sousEmbranchement equals to UPDATED_SOUS_EMBRANCHEMENT
        defaultClassificationCronquistShouldNotBeFound("sousEmbranchement.in=" + UPDATED_SOUS_EMBRANCHEMENT);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousEmbranchementIsNullOrNotNull() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousEmbranchement is not null
        defaultClassificationCronquistShouldBeFound("sousEmbranchement.specified=true");

        // Get all the classificationCronquistList where sousEmbranchement is null
        defaultClassificationCronquistShouldNotBeFound("sousEmbranchement.specified=false");
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousEmbranchementContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousEmbranchement contains DEFAULT_SOUS_EMBRANCHEMENT
        defaultClassificationCronquistShouldBeFound("sousEmbranchement.contains=" + DEFAULT_SOUS_EMBRANCHEMENT);

        // Get all the classificationCronquistList where sousEmbranchement contains UPDATED_SOUS_EMBRANCHEMENT
        defaultClassificationCronquistShouldNotBeFound("sousEmbranchement.contains=" + UPDATED_SOUS_EMBRANCHEMENT);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousEmbranchementNotContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousEmbranchement does not contain DEFAULT_SOUS_EMBRANCHEMENT
        defaultClassificationCronquistShouldNotBeFound("sousEmbranchement.doesNotContain=" + DEFAULT_SOUS_EMBRANCHEMENT);

        // Get all the classificationCronquistList where sousEmbranchement does not contain UPDATED_SOUS_EMBRANCHEMENT
        defaultClassificationCronquistShouldBeFound("sousEmbranchement.doesNotContain=" + UPDATED_SOUS_EMBRANCHEMENT);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByInfraEmbranchementIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where infraEmbranchement equals to DEFAULT_INFRA_EMBRANCHEMENT
        defaultClassificationCronquistShouldBeFound("infraEmbranchement.equals=" + DEFAULT_INFRA_EMBRANCHEMENT);

        // Get all the classificationCronquistList where infraEmbranchement equals to UPDATED_INFRA_EMBRANCHEMENT
        defaultClassificationCronquistShouldNotBeFound("infraEmbranchement.equals=" + UPDATED_INFRA_EMBRANCHEMENT);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByInfraEmbranchementIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where infraEmbranchement not equals to DEFAULT_INFRA_EMBRANCHEMENT
        defaultClassificationCronquistShouldNotBeFound("infraEmbranchement.notEquals=" + DEFAULT_INFRA_EMBRANCHEMENT);

        // Get all the classificationCronquistList where infraEmbranchement not equals to UPDATED_INFRA_EMBRANCHEMENT
        defaultClassificationCronquistShouldBeFound("infraEmbranchement.notEquals=" + UPDATED_INFRA_EMBRANCHEMENT);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByInfraEmbranchementIsInShouldWork() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where infraEmbranchement in DEFAULT_INFRA_EMBRANCHEMENT or UPDATED_INFRA_EMBRANCHEMENT
        defaultClassificationCronquistShouldBeFound(
            "infraEmbranchement.in=" + DEFAULT_INFRA_EMBRANCHEMENT + "," + UPDATED_INFRA_EMBRANCHEMENT
        );

        // Get all the classificationCronquistList where infraEmbranchement equals to UPDATED_INFRA_EMBRANCHEMENT
        defaultClassificationCronquistShouldNotBeFound("infraEmbranchement.in=" + UPDATED_INFRA_EMBRANCHEMENT);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByInfraEmbranchementIsNullOrNotNull() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where infraEmbranchement is not null
        defaultClassificationCronquistShouldBeFound("infraEmbranchement.specified=true");

        // Get all the classificationCronquistList where infraEmbranchement is null
        defaultClassificationCronquistShouldNotBeFound("infraEmbranchement.specified=false");
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByInfraEmbranchementContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where infraEmbranchement contains DEFAULT_INFRA_EMBRANCHEMENT
        defaultClassificationCronquistShouldBeFound("infraEmbranchement.contains=" + DEFAULT_INFRA_EMBRANCHEMENT);

        // Get all the classificationCronquistList where infraEmbranchement contains UPDATED_INFRA_EMBRANCHEMENT
        defaultClassificationCronquistShouldNotBeFound("infraEmbranchement.contains=" + UPDATED_INFRA_EMBRANCHEMENT);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByInfraEmbranchementNotContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where infraEmbranchement does not contain DEFAULT_INFRA_EMBRANCHEMENT
        defaultClassificationCronquistShouldNotBeFound("infraEmbranchement.doesNotContain=" + DEFAULT_INFRA_EMBRANCHEMENT);

        // Get all the classificationCronquistList where infraEmbranchement does not contain UPDATED_INFRA_EMBRANCHEMENT
        defaultClassificationCronquistShouldBeFound("infraEmbranchement.doesNotContain=" + UPDATED_INFRA_EMBRANCHEMENT);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByMicroEmbranchementIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where microEmbranchement equals to DEFAULT_MICRO_EMBRANCHEMENT
        defaultClassificationCronquistShouldBeFound("microEmbranchement.equals=" + DEFAULT_MICRO_EMBRANCHEMENT);

        // Get all the classificationCronquistList where microEmbranchement equals to UPDATED_MICRO_EMBRANCHEMENT
        defaultClassificationCronquistShouldNotBeFound("microEmbranchement.equals=" + UPDATED_MICRO_EMBRANCHEMENT);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByMicroEmbranchementIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where microEmbranchement not equals to DEFAULT_MICRO_EMBRANCHEMENT
        defaultClassificationCronquistShouldNotBeFound("microEmbranchement.notEquals=" + DEFAULT_MICRO_EMBRANCHEMENT);

        // Get all the classificationCronquistList where microEmbranchement not equals to UPDATED_MICRO_EMBRANCHEMENT
        defaultClassificationCronquistShouldBeFound("microEmbranchement.notEquals=" + UPDATED_MICRO_EMBRANCHEMENT);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByMicroEmbranchementIsInShouldWork() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where microEmbranchement in DEFAULT_MICRO_EMBRANCHEMENT or UPDATED_MICRO_EMBRANCHEMENT
        defaultClassificationCronquistShouldBeFound(
            "microEmbranchement.in=" + DEFAULT_MICRO_EMBRANCHEMENT + "," + UPDATED_MICRO_EMBRANCHEMENT
        );

        // Get all the classificationCronquistList where microEmbranchement equals to UPDATED_MICRO_EMBRANCHEMENT
        defaultClassificationCronquistShouldNotBeFound("microEmbranchement.in=" + UPDATED_MICRO_EMBRANCHEMENT);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByMicroEmbranchementIsNullOrNotNull() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where microEmbranchement is not null
        defaultClassificationCronquistShouldBeFound("microEmbranchement.specified=true");

        // Get all the classificationCronquistList where microEmbranchement is null
        defaultClassificationCronquistShouldNotBeFound("microEmbranchement.specified=false");
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByMicroEmbranchementContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where microEmbranchement contains DEFAULT_MICRO_EMBRANCHEMENT
        defaultClassificationCronquistShouldBeFound("microEmbranchement.contains=" + DEFAULT_MICRO_EMBRANCHEMENT);

        // Get all the classificationCronquistList where microEmbranchement contains UPDATED_MICRO_EMBRANCHEMENT
        defaultClassificationCronquistShouldNotBeFound("microEmbranchement.contains=" + UPDATED_MICRO_EMBRANCHEMENT);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByMicroEmbranchementNotContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where microEmbranchement does not contain DEFAULT_MICRO_EMBRANCHEMENT
        defaultClassificationCronquistShouldNotBeFound("microEmbranchement.doesNotContain=" + DEFAULT_MICRO_EMBRANCHEMENT);

        // Get all the classificationCronquistList where microEmbranchement does not contain UPDATED_MICRO_EMBRANCHEMENT
        defaultClassificationCronquistShouldBeFound("microEmbranchement.doesNotContain=" + UPDATED_MICRO_EMBRANCHEMENT);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySuperClasseIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where superClasse equals to DEFAULT_SUPER_CLASSE
        defaultClassificationCronquistShouldBeFound("superClasse.equals=" + DEFAULT_SUPER_CLASSE);

        // Get all the classificationCronquistList where superClasse equals to UPDATED_SUPER_CLASSE
        defaultClassificationCronquistShouldNotBeFound("superClasse.equals=" + UPDATED_SUPER_CLASSE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySuperClasseIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where superClasse not equals to DEFAULT_SUPER_CLASSE
        defaultClassificationCronquistShouldNotBeFound("superClasse.notEquals=" + DEFAULT_SUPER_CLASSE);

        // Get all the classificationCronquistList where superClasse not equals to UPDATED_SUPER_CLASSE
        defaultClassificationCronquistShouldBeFound("superClasse.notEquals=" + UPDATED_SUPER_CLASSE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySuperClasseIsInShouldWork() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where superClasse in DEFAULT_SUPER_CLASSE or UPDATED_SUPER_CLASSE
        defaultClassificationCronquistShouldBeFound("superClasse.in=" + DEFAULT_SUPER_CLASSE + "," + UPDATED_SUPER_CLASSE);

        // Get all the classificationCronquistList where superClasse equals to UPDATED_SUPER_CLASSE
        defaultClassificationCronquistShouldNotBeFound("superClasse.in=" + UPDATED_SUPER_CLASSE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySuperClasseIsNullOrNotNull() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where superClasse is not null
        defaultClassificationCronquistShouldBeFound("superClasse.specified=true");

        // Get all the classificationCronquistList where superClasse is null
        defaultClassificationCronquistShouldNotBeFound("superClasse.specified=false");
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySuperClasseContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where superClasse contains DEFAULT_SUPER_CLASSE
        defaultClassificationCronquistShouldBeFound("superClasse.contains=" + DEFAULT_SUPER_CLASSE);

        // Get all the classificationCronquistList where superClasse contains UPDATED_SUPER_CLASSE
        defaultClassificationCronquistShouldNotBeFound("superClasse.contains=" + UPDATED_SUPER_CLASSE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySuperClasseNotContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where superClasse does not contain DEFAULT_SUPER_CLASSE
        defaultClassificationCronquistShouldNotBeFound("superClasse.doesNotContain=" + DEFAULT_SUPER_CLASSE);

        // Get all the classificationCronquistList where superClasse does not contain UPDATED_SUPER_CLASSE
        defaultClassificationCronquistShouldBeFound("superClasse.doesNotContain=" + UPDATED_SUPER_CLASSE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByClasseIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where classe equals to DEFAULT_CLASSE
        defaultClassificationCronquistShouldBeFound("classe.equals=" + DEFAULT_CLASSE);

        // Get all the classificationCronquistList where classe equals to UPDATED_CLASSE
        defaultClassificationCronquistShouldNotBeFound("classe.equals=" + UPDATED_CLASSE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByClasseIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where classe not equals to DEFAULT_CLASSE
        defaultClassificationCronquistShouldNotBeFound("classe.notEquals=" + DEFAULT_CLASSE);

        // Get all the classificationCronquistList where classe not equals to UPDATED_CLASSE
        defaultClassificationCronquistShouldBeFound("classe.notEquals=" + UPDATED_CLASSE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByClasseIsInShouldWork() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where classe in DEFAULT_CLASSE or UPDATED_CLASSE
        defaultClassificationCronquistShouldBeFound("classe.in=" + DEFAULT_CLASSE + "," + UPDATED_CLASSE);

        // Get all the classificationCronquistList where classe equals to UPDATED_CLASSE
        defaultClassificationCronquistShouldNotBeFound("classe.in=" + UPDATED_CLASSE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByClasseIsNullOrNotNull() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where classe is not null
        defaultClassificationCronquistShouldBeFound("classe.specified=true");

        // Get all the classificationCronquistList where classe is null
        defaultClassificationCronquistShouldNotBeFound("classe.specified=false");
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByClasseContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where classe contains DEFAULT_CLASSE
        defaultClassificationCronquistShouldBeFound("classe.contains=" + DEFAULT_CLASSE);

        // Get all the classificationCronquistList where classe contains UPDATED_CLASSE
        defaultClassificationCronquistShouldNotBeFound("classe.contains=" + UPDATED_CLASSE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByClasseNotContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where classe does not contain DEFAULT_CLASSE
        defaultClassificationCronquistShouldNotBeFound("classe.doesNotContain=" + DEFAULT_CLASSE);

        // Get all the classificationCronquistList where classe does not contain UPDATED_CLASSE
        defaultClassificationCronquistShouldBeFound("classe.doesNotContain=" + UPDATED_CLASSE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousClasseIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousClasse equals to DEFAULT_SOUS_CLASSE
        defaultClassificationCronquistShouldBeFound("sousClasse.equals=" + DEFAULT_SOUS_CLASSE);

        // Get all the classificationCronquistList where sousClasse equals to UPDATED_SOUS_CLASSE
        defaultClassificationCronquistShouldNotBeFound("sousClasse.equals=" + UPDATED_SOUS_CLASSE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousClasseIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousClasse not equals to DEFAULT_SOUS_CLASSE
        defaultClassificationCronquistShouldNotBeFound("sousClasse.notEquals=" + DEFAULT_SOUS_CLASSE);

        // Get all the classificationCronquistList where sousClasse not equals to UPDATED_SOUS_CLASSE
        defaultClassificationCronquistShouldBeFound("sousClasse.notEquals=" + UPDATED_SOUS_CLASSE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousClasseIsInShouldWork() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousClasse in DEFAULT_SOUS_CLASSE or UPDATED_SOUS_CLASSE
        defaultClassificationCronquistShouldBeFound("sousClasse.in=" + DEFAULT_SOUS_CLASSE + "," + UPDATED_SOUS_CLASSE);

        // Get all the classificationCronquistList where sousClasse equals to UPDATED_SOUS_CLASSE
        defaultClassificationCronquistShouldNotBeFound("sousClasse.in=" + UPDATED_SOUS_CLASSE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousClasseIsNullOrNotNull() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousClasse is not null
        defaultClassificationCronquistShouldBeFound("sousClasse.specified=true");

        // Get all the classificationCronquistList where sousClasse is null
        defaultClassificationCronquistShouldNotBeFound("sousClasse.specified=false");
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousClasseContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousClasse contains DEFAULT_SOUS_CLASSE
        defaultClassificationCronquistShouldBeFound("sousClasse.contains=" + DEFAULT_SOUS_CLASSE);

        // Get all the classificationCronquistList where sousClasse contains UPDATED_SOUS_CLASSE
        defaultClassificationCronquistShouldNotBeFound("sousClasse.contains=" + UPDATED_SOUS_CLASSE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousClasseNotContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousClasse does not contain DEFAULT_SOUS_CLASSE
        defaultClassificationCronquistShouldNotBeFound("sousClasse.doesNotContain=" + DEFAULT_SOUS_CLASSE);

        // Get all the classificationCronquistList where sousClasse does not contain UPDATED_SOUS_CLASSE
        defaultClassificationCronquistShouldBeFound("sousClasse.doesNotContain=" + UPDATED_SOUS_CLASSE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByInfraClasseIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where infraClasse equals to DEFAULT_INFRA_CLASSE
        defaultClassificationCronquistShouldBeFound("infraClasse.equals=" + DEFAULT_INFRA_CLASSE);

        // Get all the classificationCronquistList where infraClasse equals to UPDATED_INFRA_CLASSE
        defaultClassificationCronquistShouldNotBeFound("infraClasse.equals=" + UPDATED_INFRA_CLASSE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByInfraClasseIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where infraClasse not equals to DEFAULT_INFRA_CLASSE
        defaultClassificationCronquistShouldNotBeFound("infraClasse.notEquals=" + DEFAULT_INFRA_CLASSE);

        // Get all the classificationCronquistList where infraClasse not equals to UPDATED_INFRA_CLASSE
        defaultClassificationCronquistShouldBeFound("infraClasse.notEquals=" + UPDATED_INFRA_CLASSE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByInfraClasseIsInShouldWork() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where infraClasse in DEFAULT_INFRA_CLASSE or UPDATED_INFRA_CLASSE
        defaultClassificationCronquistShouldBeFound("infraClasse.in=" + DEFAULT_INFRA_CLASSE + "," + UPDATED_INFRA_CLASSE);

        // Get all the classificationCronquistList where infraClasse equals to UPDATED_INFRA_CLASSE
        defaultClassificationCronquistShouldNotBeFound("infraClasse.in=" + UPDATED_INFRA_CLASSE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByInfraClasseIsNullOrNotNull() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where infraClasse is not null
        defaultClassificationCronquistShouldBeFound("infraClasse.specified=true");

        // Get all the classificationCronquistList where infraClasse is null
        defaultClassificationCronquistShouldNotBeFound("infraClasse.specified=false");
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByInfraClasseContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where infraClasse contains DEFAULT_INFRA_CLASSE
        defaultClassificationCronquistShouldBeFound("infraClasse.contains=" + DEFAULT_INFRA_CLASSE);

        // Get all the classificationCronquistList where infraClasse contains UPDATED_INFRA_CLASSE
        defaultClassificationCronquistShouldNotBeFound("infraClasse.contains=" + UPDATED_INFRA_CLASSE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByInfraClasseNotContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where infraClasse does not contain DEFAULT_INFRA_CLASSE
        defaultClassificationCronquistShouldNotBeFound("infraClasse.doesNotContain=" + DEFAULT_INFRA_CLASSE);

        // Get all the classificationCronquistList where infraClasse does not contain UPDATED_INFRA_CLASSE
        defaultClassificationCronquistShouldBeFound("infraClasse.doesNotContain=" + UPDATED_INFRA_CLASSE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySuperOrdreIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where superOrdre equals to DEFAULT_SUPER_ORDRE
        defaultClassificationCronquistShouldBeFound("superOrdre.equals=" + DEFAULT_SUPER_ORDRE);

        // Get all the classificationCronquistList where superOrdre equals to UPDATED_SUPER_ORDRE
        defaultClassificationCronquistShouldNotBeFound("superOrdre.equals=" + UPDATED_SUPER_ORDRE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySuperOrdreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where superOrdre not equals to DEFAULT_SUPER_ORDRE
        defaultClassificationCronquistShouldNotBeFound("superOrdre.notEquals=" + DEFAULT_SUPER_ORDRE);

        // Get all the classificationCronquistList where superOrdre not equals to UPDATED_SUPER_ORDRE
        defaultClassificationCronquistShouldBeFound("superOrdre.notEquals=" + UPDATED_SUPER_ORDRE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySuperOrdreIsInShouldWork() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where superOrdre in DEFAULT_SUPER_ORDRE or UPDATED_SUPER_ORDRE
        defaultClassificationCronquistShouldBeFound("superOrdre.in=" + DEFAULT_SUPER_ORDRE + "," + UPDATED_SUPER_ORDRE);

        // Get all the classificationCronquistList where superOrdre equals to UPDATED_SUPER_ORDRE
        defaultClassificationCronquistShouldNotBeFound("superOrdre.in=" + UPDATED_SUPER_ORDRE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySuperOrdreIsNullOrNotNull() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where superOrdre is not null
        defaultClassificationCronquistShouldBeFound("superOrdre.specified=true");

        // Get all the classificationCronquistList where superOrdre is null
        defaultClassificationCronquistShouldNotBeFound("superOrdre.specified=false");
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySuperOrdreContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where superOrdre contains DEFAULT_SUPER_ORDRE
        defaultClassificationCronquistShouldBeFound("superOrdre.contains=" + DEFAULT_SUPER_ORDRE);

        // Get all the classificationCronquistList where superOrdre contains UPDATED_SUPER_ORDRE
        defaultClassificationCronquistShouldNotBeFound("superOrdre.contains=" + UPDATED_SUPER_ORDRE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySuperOrdreNotContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where superOrdre does not contain DEFAULT_SUPER_ORDRE
        defaultClassificationCronquistShouldNotBeFound("superOrdre.doesNotContain=" + DEFAULT_SUPER_ORDRE);

        // Get all the classificationCronquistList where superOrdre does not contain UPDATED_SUPER_ORDRE
        defaultClassificationCronquistShouldBeFound("superOrdre.doesNotContain=" + UPDATED_SUPER_ORDRE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByOrdreIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where ordre equals to DEFAULT_ORDRE
        defaultClassificationCronquistShouldBeFound("ordre.equals=" + DEFAULT_ORDRE);

        // Get all the classificationCronquistList where ordre equals to UPDATED_ORDRE
        defaultClassificationCronquistShouldNotBeFound("ordre.equals=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByOrdreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where ordre not equals to DEFAULT_ORDRE
        defaultClassificationCronquistShouldNotBeFound("ordre.notEquals=" + DEFAULT_ORDRE);

        // Get all the classificationCronquistList where ordre not equals to UPDATED_ORDRE
        defaultClassificationCronquistShouldBeFound("ordre.notEquals=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByOrdreIsInShouldWork() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where ordre in DEFAULT_ORDRE or UPDATED_ORDRE
        defaultClassificationCronquistShouldBeFound("ordre.in=" + DEFAULT_ORDRE + "," + UPDATED_ORDRE);

        // Get all the classificationCronquistList where ordre equals to UPDATED_ORDRE
        defaultClassificationCronquistShouldNotBeFound("ordre.in=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByOrdreIsNullOrNotNull() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where ordre is not null
        defaultClassificationCronquistShouldBeFound("ordre.specified=true");

        // Get all the classificationCronquistList where ordre is null
        defaultClassificationCronquistShouldNotBeFound("ordre.specified=false");
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByOrdreContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where ordre contains DEFAULT_ORDRE
        defaultClassificationCronquistShouldBeFound("ordre.contains=" + DEFAULT_ORDRE);

        // Get all the classificationCronquistList where ordre contains UPDATED_ORDRE
        defaultClassificationCronquistShouldNotBeFound("ordre.contains=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByOrdreNotContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where ordre does not contain DEFAULT_ORDRE
        defaultClassificationCronquistShouldNotBeFound("ordre.doesNotContain=" + DEFAULT_ORDRE);

        // Get all the classificationCronquistList where ordre does not contain UPDATED_ORDRE
        defaultClassificationCronquistShouldBeFound("ordre.doesNotContain=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousOrdreIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousOrdre equals to DEFAULT_SOUS_ORDRE
        defaultClassificationCronquistShouldBeFound("sousOrdre.equals=" + DEFAULT_SOUS_ORDRE);

        // Get all the classificationCronquistList where sousOrdre equals to UPDATED_SOUS_ORDRE
        defaultClassificationCronquistShouldNotBeFound("sousOrdre.equals=" + UPDATED_SOUS_ORDRE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousOrdreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousOrdre not equals to DEFAULT_SOUS_ORDRE
        defaultClassificationCronquistShouldNotBeFound("sousOrdre.notEquals=" + DEFAULT_SOUS_ORDRE);

        // Get all the classificationCronquistList where sousOrdre not equals to UPDATED_SOUS_ORDRE
        defaultClassificationCronquistShouldBeFound("sousOrdre.notEquals=" + UPDATED_SOUS_ORDRE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousOrdreIsInShouldWork() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousOrdre in DEFAULT_SOUS_ORDRE or UPDATED_SOUS_ORDRE
        defaultClassificationCronquistShouldBeFound("sousOrdre.in=" + DEFAULT_SOUS_ORDRE + "," + UPDATED_SOUS_ORDRE);

        // Get all the classificationCronquistList where sousOrdre equals to UPDATED_SOUS_ORDRE
        defaultClassificationCronquistShouldNotBeFound("sousOrdre.in=" + UPDATED_SOUS_ORDRE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousOrdreIsNullOrNotNull() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousOrdre is not null
        defaultClassificationCronquistShouldBeFound("sousOrdre.specified=true");

        // Get all the classificationCronquistList where sousOrdre is null
        defaultClassificationCronquistShouldNotBeFound("sousOrdre.specified=false");
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousOrdreContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousOrdre contains DEFAULT_SOUS_ORDRE
        defaultClassificationCronquistShouldBeFound("sousOrdre.contains=" + DEFAULT_SOUS_ORDRE);

        // Get all the classificationCronquistList where sousOrdre contains UPDATED_SOUS_ORDRE
        defaultClassificationCronquistShouldNotBeFound("sousOrdre.contains=" + UPDATED_SOUS_ORDRE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousOrdreNotContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousOrdre does not contain DEFAULT_SOUS_ORDRE
        defaultClassificationCronquistShouldNotBeFound("sousOrdre.doesNotContain=" + DEFAULT_SOUS_ORDRE);

        // Get all the classificationCronquistList where sousOrdre does not contain UPDATED_SOUS_ORDRE
        defaultClassificationCronquistShouldBeFound("sousOrdre.doesNotContain=" + UPDATED_SOUS_ORDRE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByInfraOrdreIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where infraOrdre equals to DEFAULT_INFRA_ORDRE
        defaultClassificationCronquistShouldBeFound("infraOrdre.equals=" + DEFAULT_INFRA_ORDRE);

        // Get all the classificationCronquistList where infraOrdre equals to UPDATED_INFRA_ORDRE
        defaultClassificationCronquistShouldNotBeFound("infraOrdre.equals=" + UPDATED_INFRA_ORDRE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByInfraOrdreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where infraOrdre not equals to DEFAULT_INFRA_ORDRE
        defaultClassificationCronquistShouldNotBeFound("infraOrdre.notEquals=" + DEFAULT_INFRA_ORDRE);

        // Get all the classificationCronquistList where infraOrdre not equals to UPDATED_INFRA_ORDRE
        defaultClassificationCronquistShouldBeFound("infraOrdre.notEquals=" + UPDATED_INFRA_ORDRE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByInfraOrdreIsInShouldWork() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where infraOrdre in DEFAULT_INFRA_ORDRE or UPDATED_INFRA_ORDRE
        defaultClassificationCronquistShouldBeFound("infraOrdre.in=" + DEFAULT_INFRA_ORDRE + "," + UPDATED_INFRA_ORDRE);

        // Get all the classificationCronquistList where infraOrdre equals to UPDATED_INFRA_ORDRE
        defaultClassificationCronquistShouldNotBeFound("infraOrdre.in=" + UPDATED_INFRA_ORDRE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByInfraOrdreIsNullOrNotNull() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where infraOrdre is not null
        defaultClassificationCronquistShouldBeFound("infraOrdre.specified=true");

        // Get all the classificationCronquistList where infraOrdre is null
        defaultClassificationCronquistShouldNotBeFound("infraOrdre.specified=false");
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByInfraOrdreContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where infraOrdre contains DEFAULT_INFRA_ORDRE
        defaultClassificationCronquistShouldBeFound("infraOrdre.contains=" + DEFAULT_INFRA_ORDRE);

        // Get all the classificationCronquistList where infraOrdre contains UPDATED_INFRA_ORDRE
        defaultClassificationCronquistShouldNotBeFound("infraOrdre.contains=" + UPDATED_INFRA_ORDRE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByInfraOrdreNotContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where infraOrdre does not contain DEFAULT_INFRA_ORDRE
        defaultClassificationCronquistShouldNotBeFound("infraOrdre.doesNotContain=" + DEFAULT_INFRA_ORDRE);

        // Get all the classificationCronquistList where infraOrdre does not contain UPDATED_INFRA_ORDRE
        defaultClassificationCronquistShouldBeFound("infraOrdre.doesNotContain=" + UPDATED_INFRA_ORDRE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByMicroOrdreIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where microOrdre equals to DEFAULT_MICRO_ORDRE
        defaultClassificationCronquistShouldBeFound("microOrdre.equals=" + DEFAULT_MICRO_ORDRE);

        // Get all the classificationCronquistList where microOrdre equals to UPDATED_MICRO_ORDRE
        defaultClassificationCronquistShouldNotBeFound("microOrdre.equals=" + UPDATED_MICRO_ORDRE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByMicroOrdreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where microOrdre not equals to DEFAULT_MICRO_ORDRE
        defaultClassificationCronquistShouldNotBeFound("microOrdre.notEquals=" + DEFAULT_MICRO_ORDRE);

        // Get all the classificationCronquistList where microOrdre not equals to UPDATED_MICRO_ORDRE
        defaultClassificationCronquistShouldBeFound("microOrdre.notEquals=" + UPDATED_MICRO_ORDRE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByMicroOrdreIsInShouldWork() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where microOrdre in DEFAULT_MICRO_ORDRE or UPDATED_MICRO_ORDRE
        defaultClassificationCronquistShouldBeFound("microOrdre.in=" + DEFAULT_MICRO_ORDRE + "," + UPDATED_MICRO_ORDRE);

        // Get all the classificationCronquistList where microOrdre equals to UPDATED_MICRO_ORDRE
        defaultClassificationCronquistShouldNotBeFound("microOrdre.in=" + UPDATED_MICRO_ORDRE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByMicroOrdreIsNullOrNotNull() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where microOrdre is not null
        defaultClassificationCronquistShouldBeFound("microOrdre.specified=true");

        // Get all the classificationCronquistList where microOrdre is null
        defaultClassificationCronquistShouldNotBeFound("microOrdre.specified=false");
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByMicroOrdreContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where microOrdre contains DEFAULT_MICRO_ORDRE
        defaultClassificationCronquistShouldBeFound("microOrdre.contains=" + DEFAULT_MICRO_ORDRE);

        // Get all the classificationCronquistList where microOrdre contains UPDATED_MICRO_ORDRE
        defaultClassificationCronquistShouldNotBeFound("microOrdre.contains=" + UPDATED_MICRO_ORDRE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByMicroOrdreNotContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where microOrdre does not contain DEFAULT_MICRO_ORDRE
        defaultClassificationCronquistShouldNotBeFound("microOrdre.doesNotContain=" + DEFAULT_MICRO_ORDRE);

        // Get all the classificationCronquistList where microOrdre does not contain UPDATED_MICRO_ORDRE
        defaultClassificationCronquistShouldBeFound("microOrdre.doesNotContain=" + UPDATED_MICRO_ORDRE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySuperFamilleIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where superFamille equals to DEFAULT_SUPER_FAMILLE
        defaultClassificationCronquistShouldBeFound("superFamille.equals=" + DEFAULT_SUPER_FAMILLE);

        // Get all the classificationCronquistList where superFamille equals to UPDATED_SUPER_FAMILLE
        defaultClassificationCronquistShouldNotBeFound("superFamille.equals=" + UPDATED_SUPER_FAMILLE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySuperFamilleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where superFamille not equals to DEFAULT_SUPER_FAMILLE
        defaultClassificationCronquistShouldNotBeFound("superFamille.notEquals=" + DEFAULT_SUPER_FAMILLE);

        // Get all the classificationCronquistList where superFamille not equals to UPDATED_SUPER_FAMILLE
        defaultClassificationCronquistShouldBeFound("superFamille.notEquals=" + UPDATED_SUPER_FAMILLE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySuperFamilleIsInShouldWork() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where superFamille in DEFAULT_SUPER_FAMILLE or UPDATED_SUPER_FAMILLE
        defaultClassificationCronquistShouldBeFound("superFamille.in=" + DEFAULT_SUPER_FAMILLE + "," + UPDATED_SUPER_FAMILLE);

        // Get all the classificationCronquistList where superFamille equals to UPDATED_SUPER_FAMILLE
        defaultClassificationCronquistShouldNotBeFound("superFamille.in=" + UPDATED_SUPER_FAMILLE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySuperFamilleIsNullOrNotNull() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where superFamille is not null
        defaultClassificationCronquistShouldBeFound("superFamille.specified=true");

        // Get all the classificationCronquistList where superFamille is null
        defaultClassificationCronquistShouldNotBeFound("superFamille.specified=false");
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySuperFamilleContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where superFamille contains DEFAULT_SUPER_FAMILLE
        defaultClassificationCronquistShouldBeFound("superFamille.contains=" + DEFAULT_SUPER_FAMILLE);

        // Get all the classificationCronquistList where superFamille contains UPDATED_SUPER_FAMILLE
        defaultClassificationCronquistShouldNotBeFound("superFamille.contains=" + UPDATED_SUPER_FAMILLE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySuperFamilleNotContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where superFamille does not contain DEFAULT_SUPER_FAMILLE
        defaultClassificationCronquistShouldNotBeFound("superFamille.doesNotContain=" + DEFAULT_SUPER_FAMILLE);

        // Get all the classificationCronquistList where superFamille does not contain UPDATED_SUPER_FAMILLE
        defaultClassificationCronquistShouldBeFound("superFamille.doesNotContain=" + UPDATED_SUPER_FAMILLE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByFamilleIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where famille equals to DEFAULT_FAMILLE
        defaultClassificationCronquistShouldBeFound("famille.equals=" + DEFAULT_FAMILLE);

        // Get all the classificationCronquistList where famille equals to UPDATED_FAMILLE
        defaultClassificationCronquistShouldNotBeFound("famille.equals=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByFamilleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where famille not equals to DEFAULT_FAMILLE
        defaultClassificationCronquistShouldNotBeFound("famille.notEquals=" + DEFAULT_FAMILLE);

        // Get all the classificationCronquistList where famille not equals to UPDATED_FAMILLE
        defaultClassificationCronquistShouldBeFound("famille.notEquals=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByFamilleIsInShouldWork() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where famille in DEFAULT_FAMILLE or UPDATED_FAMILLE
        defaultClassificationCronquistShouldBeFound("famille.in=" + DEFAULT_FAMILLE + "," + UPDATED_FAMILLE);

        // Get all the classificationCronquistList where famille equals to UPDATED_FAMILLE
        defaultClassificationCronquistShouldNotBeFound("famille.in=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByFamilleIsNullOrNotNull() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where famille is not null
        defaultClassificationCronquistShouldBeFound("famille.specified=true");

        // Get all the classificationCronquistList where famille is null
        defaultClassificationCronquistShouldNotBeFound("famille.specified=false");
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByFamilleContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where famille contains DEFAULT_FAMILLE
        defaultClassificationCronquistShouldBeFound("famille.contains=" + DEFAULT_FAMILLE);

        // Get all the classificationCronquistList where famille contains UPDATED_FAMILLE
        defaultClassificationCronquistShouldNotBeFound("famille.contains=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByFamilleNotContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where famille does not contain DEFAULT_FAMILLE
        defaultClassificationCronquistShouldNotBeFound("famille.doesNotContain=" + DEFAULT_FAMILLE);

        // Get all the classificationCronquistList where famille does not contain UPDATED_FAMILLE
        defaultClassificationCronquistShouldBeFound("famille.doesNotContain=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousFamilleIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousFamille equals to DEFAULT_SOUS_FAMILLE
        defaultClassificationCronquistShouldBeFound("sousFamille.equals=" + DEFAULT_SOUS_FAMILLE);

        // Get all the classificationCronquistList where sousFamille equals to UPDATED_SOUS_FAMILLE
        defaultClassificationCronquistShouldNotBeFound("sousFamille.equals=" + UPDATED_SOUS_FAMILLE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousFamilleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousFamille not equals to DEFAULT_SOUS_FAMILLE
        defaultClassificationCronquistShouldNotBeFound("sousFamille.notEquals=" + DEFAULT_SOUS_FAMILLE);

        // Get all the classificationCronquistList where sousFamille not equals to UPDATED_SOUS_FAMILLE
        defaultClassificationCronquistShouldBeFound("sousFamille.notEquals=" + UPDATED_SOUS_FAMILLE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousFamilleIsInShouldWork() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousFamille in DEFAULT_SOUS_FAMILLE or UPDATED_SOUS_FAMILLE
        defaultClassificationCronquistShouldBeFound("sousFamille.in=" + DEFAULT_SOUS_FAMILLE + "," + UPDATED_SOUS_FAMILLE);

        // Get all the classificationCronquistList where sousFamille equals to UPDATED_SOUS_FAMILLE
        defaultClassificationCronquistShouldNotBeFound("sousFamille.in=" + UPDATED_SOUS_FAMILLE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousFamilleIsNullOrNotNull() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousFamille is not null
        defaultClassificationCronquistShouldBeFound("sousFamille.specified=true");

        // Get all the classificationCronquistList where sousFamille is null
        defaultClassificationCronquistShouldNotBeFound("sousFamille.specified=false");
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousFamilleContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousFamille contains DEFAULT_SOUS_FAMILLE
        defaultClassificationCronquistShouldBeFound("sousFamille.contains=" + DEFAULT_SOUS_FAMILLE);

        // Get all the classificationCronquistList where sousFamille contains UPDATED_SOUS_FAMILLE
        defaultClassificationCronquistShouldNotBeFound("sousFamille.contains=" + UPDATED_SOUS_FAMILLE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousFamilleNotContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousFamille does not contain DEFAULT_SOUS_FAMILLE
        defaultClassificationCronquistShouldNotBeFound("sousFamille.doesNotContain=" + DEFAULT_SOUS_FAMILLE);

        // Get all the classificationCronquistList where sousFamille does not contain UPDATED_SOUS_FAMILLE
        defaultClassificationCronquistShouldBeFound("sousFamille.doesNotContain=" + UPDATED_SOUS_FAMILLE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByTribuIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where tribu equals to DEFAULT_TRIBU
        defaultClassificationCronquistShouldBeFound("tribu.equals=" + DEFAULT_TRIBU);

        // Get all the classificationCronquistList where tribu equals to UPDATED_TRIBU
        defaultClassificationCronquistShouldNotBeFound("tribu.equals=" + UPDATED_TRIBU);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByTribuIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where tribu not equals to DEFAULT_TRIBU
        defaultClassificationCronquistShouldNotBeFound("tribu.notEquals=" + DEFAULT_TRIBU);

        // Get all the classificationCronquistList where tribu not equals to UPDATED_TRIBU
        defaultClassificationCronquistShouldBeFound("tribu.notEquals=" + UPDATED_TRIBU);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByTribuIsInShouldWork() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where tribu in DEFAULT_TRIBU or UPDATED_TRIBU
        defaultClassificationCronquistShouldBeFound("tribu.in=" + DEFAULT_TRIBU + "," + UPDATED_TRIBU);

        // Get all the classificationCronquistList where tribu equals to UPDATED_TRIBU
        defaultClassificationCronquistShouldNotBeFound("tribu.in=" + UPDATED_TRIBU);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByTribuIsNullOrNotNull() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where tribu is not null
        defaultClassificationCronquistShouldBeFound("tribu.specified=true");

        // Get all the classificationCronquistList where tribu is null
        defaultClassificationCronquistShouldNotBeFound("tribu.specified=false");
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByTribuContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where tribu contains DEFAULT_TRIBU
        defaultClassificationCronquistShouldBeFound("tribu.contains=" + DEFAULT_TRIBU);

        // Get all the classificationCronquistList where tribu contains UPDATED_TRIBU
        defaultClassificationCronquistShouldNotBeFound("tribu.contains=" + UPDATED_TRIBU);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByTribuNotContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where tribu does not contain DEFAULT_TRIBU
        defaultClassificationCronquistShouldNotBeFound("tribu.doesNotContain=" + DEFAULT_TRIBU);

        // Get all the classificationCronquistList where tribu does not contain UPDATED_TRIBU
        defaultClassificationCronquistShouldBeFound("tribu.doesNotContain=" + UPDATED_TRIBU);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousTribuIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousTribu equals to DEFAULT_SOUS_TRIBU
        defaultClassificationCronquistShouldBeFound("sousTribu.equals=" + DEFAULT_SOUS_TRIBU);

        // Get all the classificationCronquistList where sousTribu equals to UPDATED_SOUS_TRIBU
        defaultClassificationCronquistShouldNotBeFound("sousTribu.equals=" + UPDATED_SOUS_TRIBU);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousTribuIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousTribu not equals to DEFAULT_SOUS_TRIBU
        defaultClassificationCronquistShouldNotBeFound("sousTribu.notEquals=" + DEFAULT_SOUS_TRIBU);

        // Get all the classificationCronquistList where sousTribu not equals to UPDATED_SOUS_TRIBU
        defaultClassificationCronquistShouldBeFound("sousTribu.notEquals=" + UPDATED_SOUS_TRIBU);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousTribuIsInShouldWork() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousTribu in DEFAULT_SOUS_TRIBU or UPDATED_SOUS_TRIBU
        defaultClassificationCronquistShouldBeFound("sousTribu.in=" + DEFAULT_SOUS_TRIBU + "," + UPDATED_SOUS_TRIBU);

        // Get all the classificationCronquistList where sousTribu equals to UPDATED_SOUS_TRIBU
        defaultClassificationCronquistShouldNotBeFound("sousTribu.in=" + UPDATED_SOUS_TRIBU);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousTribuIsNullOrNotNull() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousTribu is not null
        defaultClassificationCronquistShouldBeFound("sousTribu.specified=true");

        // Get all the classificationCronquistList where sousTribu is null
        defaultClassificationCronquistShouldNotBeFound("sousTribu.specified=false");
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousTribuContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousTribu contains DEFAULT_SOUS_TRIBU
        defaultClassificationCronquistShouldBeFound("sousTribu.contains=" + DEFAULT_SOUS_TRIBU);

        // Get all the classificationCronquistList where sousTribu contains UPDATED_SOUS_TRIBU
        defaultClassificationCronquistShouldNotBeFound("sousTribu.contains=" + UPDATED_SOUS_TRIBU);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousTribuNotContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousTribu does not contain DEFAULT_SOUS_TRIBU
        defaultClassificationCronquistShouldNotBeFound("sousTribu.doesNotContain=" + DEFAULT_SOUS_TRIBU);

        // Get all the classificationCronquistList where sousTribu does not contain UPDATED_SOUS_TRIBU
        defaultClassificationCronquistShouldBeFound("sousTribu.doesNotContain=" + UPDATED_SOUS_TRIBU);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByGenreIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where genre equals to DEFAULT_GENRE
        defaultClassificationCronquistShouldBeFound("genre.equals=" + DEFAULT_GENRE);

        // Get all the classificationCronquistList where genre equals to UPDATED_GENRE
        defaultClassificationCronquistShouldNotBeFound("genre.equals=" + UPDATED_GENRE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByGenreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where genre not equals to DEFAULT_GENRE
        defaultClassificationCronquistShouldNotBeFound("genre.notEquals=" + DEFAULT_GENRE);

        // Get all the classificationCronquistList where genre not equals to UPDATED_GENRE
        defaultClassificationCronquistShouldBeFound("genre.notEquals=" + UPDATED_GENRE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByGenreIsInShouldWork() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where genre in DEFAULT_GENRE or UPDATED_GENRE
        defaultClassificationCronquistShouldBeFound("genre.in=" + DEFAULT_GENRE + "," + UPDATED_GENRE);

        // Get all the classificationCronquistList where genre equals to UPDATED_GENRE
        defaultClassificationCronquistShouldNotBeFound("genre.in=" + UPDATED_GENRE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByGenreIsNullOrNotNull() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where genre is not null
        defaultClassificationCronquistShouldBeFound("genre.specified=true");

        // Get all the classificationCronquistList where genre is null
        defaultClassificationCronquistShouldNotBeFound("genre.specified=false");
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByGenreContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where genre contains DEFAULT_GENRE
        defaultClassificationCronquistShouldBeFound("genre.contains=" + DEFAULT_GENRE);

        // Get all the classificationCronquistList where genre contains UPDATED_GENRE
        defaultClassificationCronquistShouldNotBeFound("genre.contains=" + UPDATED_GENRE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByGenreNotContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where genre does not contain DEFAULT_GENRE
        defaultClassificationCronquistShouldNotBeFound("genre.doesNotContain=" + DEFAULT_GENRE);

        // Get all the classificationCronquistList where genre does not contain UPDATED_GENRE
        defaultClassificationCronquistShouldBeFound("genre.doesNotContain=" + UPDATED_GENRE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousGenreIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousGenre equals to DEFAULT_SOUS_GENRE
        defaultClassificationCronquistShouldBeFound("sousGenre.equals=" + DEFAULT_SOUS_GENRE);

        // Get all the classificationCronquistList where sousGenre equals to UPDATED_SOUS_GENRE
        defaultClassificationCronquistShouldNotBeFound("sousGenre.equals=" + UPDATED_SOUS_GENRE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousGenreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousGenre not equals to DEFAULT_SOUS_GENRE
        defaultClassificationCronquistShouldNotBeFound("sousGenre.notEquals=" + DEFAULT_SOUS_GENRE);

        // Get all the classificationCronquistList where sousGenre not equals to UPDATED_SOUS_GENRE
        defaultClassificationCronquistShouldBeFound("sousGenre.notEquals=" + UPDATED_SOUS_GENRE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousGenreIsInShouldWork() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousGenre in DEFAULT_SOUS_GENRE or UPDATED_SOUS_GENRE
        defaultClassificationCronquistShouldBeFound("sousGenre.in=" + DEFAULT_SOUS_GENRE + "," + UPDATED_SOUS_GENRE);

        // Get all the classificationCronquistList where sousGenre equals to UPDATED_SOUS_GENRE
        defaultClassificationCronquistShouldNotBeFound("sousGenre.in=" + UPDATED_SOUS_GENRE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousGenreIsNullOrNotNull() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousGenre is not null
        defaultClassificationCronquistShouldBeFound("sousGenre.specified=true");

        // Get all the classificationCronquistList where sousGenre is null
        defaultClassificationCronquistShouldNotBeFound("sousGenre.specified=false");
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousGenreContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousGenre contains DEFAULT_SOUS_GENRE
        defaultClassificationCronquistShouldBeFound("sousGenre.contains=" + DEFAULT_SOUS_GENRE);

        // Get all the classificationCronquistList where sousGenre contains UPDATED_SOUS_GENRE
        defaultClassificationCronquistShouldNotBeFound("sousGenre.contains=" + UPDATED_SOUS_GENRE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousGenreNotContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousGenre does not contain DEFAULT_SOUS_GENRE
        defaultClassificationCronquistShouldNotBeFound("sousGenre.doesNotContain=" + DEFAULT_SOUS_GENRE);

        // Get all the classificationCronquistList where sousGenre does not contain UPDATED_SOUS_GENRE
        defaultClassificationCronquistShouldBeFound("sousGenre.doesNotContain=" + UPDATED_SOUS_GENRE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySectionIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where section equals to DEFAULT_SECTION
        defaultClassificationCronquistShouldBeFound("section.equals=" + DEFAULT_SECTION);

        // Get all the classificationCronquistList where section equals to UPDATED_SECTION
        defaultClassificationCronquistShouldNotBeFound("section.equals=" + UPDATED_SECTION);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySectionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where section not equals to DEFAULT_SECTION
        defaultClassificationCronquistShouldNotBeFound("section.notEquals=" + DEFAULT_SECTION);

        // Get all the classificationCronquistList where section not equals to UPDATED_SECTION
        defaultClassificationCronquistShouldBeFound("section.notEquals=" + UPDATED_SECTION);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySectionIsInShouldWork() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where section in DEFAULT_SECTION or UPDATED_SECTION
        defaultClassificationCronquistShouldBeFound("section.in=" + DEFAULT_SECTION + "," + UPDATED_SECTION);

        // Get all the classificationCronquistList where section equals to UPDATED_SECTION
        defaultClassificationCronquistShouldNotBeFound("section.in=" + UPDATED_SECTION);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySectionIsNullOrNotNull() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where section is not null
        defaultClassificationCronquistShouldBeFound("section.specified=true");

        // Get all the classificationCronquistList where section is null
        defaultClassificationCronquistShouldNotBeFound("section.specified=false");
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySectionContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where section contains DEFAULT_SECTION
        defaultClassificationCronquistShouldBeFound("section.contains=" + DEFAULT_SECTION);

        // Get all the classificationCronquistList where section contains UPDATED_SECTION
        defaultClassificationCronquistShouldNotBeFound("section.contains=" + UPDATED_SECTION);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySectionNotContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where section does not contain DEFAULT_SECTION
        defaultClassificationCronquistShouldNotBeFound("section.doesNotContain=" + DEFAULT_SECTION);

        // Get all the classificationCronquistList where section does not contain UPDATED_SECTION
        defaultClassificationCronquistShouldBeFound("section.doesNotContain=" + UPDATED_SECTION);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousSectionIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousSection equals to DEFAULT_SOUS_SECTION
        defaultClassificationCronquistShouldBeFound("sousSection.equals=" + DEFAULT_SOUS_SECTION);

        // Get all the classificationCronquistList where sousSection equals to UPDATED_SOUS_SECTION
        defaultClassificationCronquistShouldNotBeFound("sousSection.equals=" + UPDATED_SOUS_SECTION);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousSectionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousSection not equals to DEFAULT_SOUS_SECTION
        defaultClassificationCronquistShouldNotBeFound("sousSection.notEquals=" + DEFAULT_SOUS_SECTION);

        // Get all the classificationCronquistList where sousSection not equals to UPDATED_SOUS_SECTION
        defaultClassificationCronquistShouldBeFound("sousSection.notEquals=" + UPDATED_SOUS_SECTION);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousSectionIsInShouldWork() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousSection in DEFAULT_SOUS_SECTION or UPDATED_SOUS_SECTION
        defaultClassificationCronquistShouldBeFound("sousSection.in=" + DEFAULT_SOUS_SECTION + "," + UPDATED_SOUS_SECTION);

        // Get all the classificationCronquistList where sousSection equals to UPDATED_SOUS_SECTION
        defaultClassificationCronquistShouldNotBeFound("sousSection.in=" + UPDATED_SOUS_SECTION);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousSectionIsNullOrNotNull() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousSection is not null
        defaultClassificationCronquistShouldBeFound("sousSection.specified=true");

        // Get all the classificationCronquistList where sousSection is null
        defaultClassificationCronquistShouldNotBeFound("sousSection.specified=false");
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousSectionContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousSection contains DEFAULT_SOUS_SECTION
        defaultClassificationCronquistShouldBeFound("sousSection.contains=" + DEFAULT_SOUS_SECTION);

        // Get all the classificationCronquistList where sousSection contains UPDATED_SOUS_SECTION
        defaultClassificationCronquistShouldNotBeFound("sousSection.contains=" + UPDATED_SOUS_SECTION);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousSectionNotContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousSection does not contain DEFAULT_SOUS_SECTION
        defaultClassificationCronquistShouldNotBeFound("sousSection.doesNotContain=" + DEFAULT_SOUS_SECTION);

        // Get all the classificationCronquistList where sousSection does not contain UPDATED_SOUS_SECTION
        defaultClassificationCronquistShouldBeFound("sousSection.doesNotContain=" + UPDATED_SOUS_SECTION);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByEspeceIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where espece equals to DEFAULT_ESPECE
        defaultClassificationCronquistShouldBeFound("espece.equals=" + DEFAULT_ESPECE);

        // Get all the classificationCronquistList where espece equals to UPDATED_ESPECE
        defaultClassificationCronquistShouldNotBeFound("espece.equals=" + UPDATED_ESPECE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByEspeceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where espece not equals to DEFAULT_ESPECE
        defaultClassificationCronquistShouldNotBeFound("espece.notEquals=" + DEFAULT_ESPECE);

        // Get all the classificationCronquistList where espece not equals to UPDATED_ESPECE
        defaultClassificationCronquistShouldBeFound("espece.notEquals=" + UPDATED_ESPECE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByEspeceIsInShouldWork() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where espece in DEFAULT_ESPECE or UPDATED_ESPECE
        defaultClassificationCronquistShouldBeFound("espece.in=" + DEFAULT_ESPECE + "," + UPDATED_ESPECE);

        // Get all the classificationCronquistList where espece equals to UPDATED_ESPECE
        defaultClassificationCronquistShouldNotBeFound("espece.in=" + UPDATED_ESPECE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByEspeceIsNullOrNotNull() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where espece is not null
        defaultClassificationCronquistShouldBeFound("espece.specified=true");

        // Get all the classificationCronquistList where espece is null
        defaultClassificationCronquistShouldNotBeFound("espece.specified=false");
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByEspeceContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where espece contains DEFAULT_ESPECE
        defaultClassificationCronquistShouldBeFound("espece.contains=" + DEFAULT_ESPECE);

        // Get all the classificationCronquistList where espece contains UPDATED_ESPECE
        defaultClassificationCronquistShouldNotBeFound("espece.contains=" + UPDATED_ESPECE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByEspeceNotContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where espece does not contain DEFAULT_ESPECE
        defaultClassificationCronquistShouldNotBeFound("espece.doesNotContain=" + DEFAULT_ESPECE);

        // Get all the classificationCronquistList where espece does not contain UPDATED_ESPECE
        defaultClassificationCronquistShouldBeFound("espece.doesNotContain=" + UPDATED_ESPECE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousEspeceIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousEspece equals to DEFAULT_SOUS_ESPECE
        defaultClassificationCronquistShouldBeFound("sousEspece.equals=" + DEFAULT_SOUS_ESPECE);

        // Get all the classificationCronquistList where sousEspece equals to UPDATED_SOUS_ESPECE
        defaultClassificationCronquistShouldNotBeFound("sousEspece.equals=" + UPDATED_SOUS_ESPECE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousEspeceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousEspece not equals to DEFAULT_SOUS_ESPECE
        defaultClassificationCronquistShouldNotBeFound("sousEspece.notEquals=" + DEFAULT_SOUS_ESPECE);

        // Get all the classificationCronquistList where sousEspece not equals to UPDATED_SOUS_ESPECE
        defaultClassificationCronquistShouldBeFound("sousEspece.notEquals=" + UPDATED_SOUS_ESPECE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousEspeceIsInShouldWork() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousEspece in DEFAULT_SOUS_ESPECE or UPDATED_SOUS_ESPECE
        defaultClassificationCronquistShouldBeFound("sousEspece.in=" + DEFAULT_SOUS_ESPECE + "," + UPDATED_SOUS_ESPECE);

        // Get all the classificationCronquistList where sousEspece equals to UPDATED_SOUS_ESPECE
        defaultClassificationCronquistShouldNotBeFound("sousEspece.in=" + UPDATED_SOUS_ESPECE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousEspeceIsNullOrNotNull() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousEspece is not null
        defaultClassificationCronquistShouldBeFound("sousEspece.specified=true");

        // Get all the classificationCronquistList where sousEspece is null
        defaultClassificationCronquistShouldNotBeFound("sousEspece.specified=false");
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousEspeceContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousEspece contains DEFAULT_SOUS_ESPECE
        defaultClassificationCronquistShouldBeFound("sousEspece.contains=" + DEFAULT_SOUS_ESPECE);

        // Get all the classificationCronquistList where sousEspece contains UPDATED_SOUS_ESPECE
        defaultClassificationCronquistShouldNotBeFound("sousEspece.contains=" + UPDATED_SOUS_ESPECE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousEspeceNotContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousEspece does not contain DEFAULT_SOUS_ESPECE
        defaultClassificationCronquistShouldNotBeFound("sousEspece.doesNotContain=" + DEFAULT_SOUS_ESPECE);

        // Get all the classificationCronquistList where sousEspece does not contain UPDATED_SOUS_ESPECE
        defaultClassificationCronquistShouldBeFound("sousEspece.doesNotContain=" + UPDATED_SOUS_ESPECE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByVarieteIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where variete equals to DEFAULT_VARIETE
        defaultClassificationCronquistShouldBeFound("variete.equals=" + DEFAULT_VARIETE);

        // Get all the classificationCronquistList where variete equals to UPDATED_VARIETE
        defaultClassificationCronquistShouldNotBeFound("variete.equals=" + UPDATED_VARIETE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByVarieteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where variete not equals to DEFAULT_VARIETE
        defaultClassificationCronquistShouldNotBeFound("variete.notEquals=" + DEFAULT_VARIETE);

        // Get all the classificationCronquistList where variete not equals to UPDATED_VARIETE
        defaultClassificationCronquistShouldBeFound("variete.notEquals=" + UPDATED_VARIETE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByVarieteIsInShouldWork() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where variete in DEFAULT_VARIETE or UPDATED_VARIETE
        defaultClassificationCronquistShouldBeFound("variete.in=" + DEFAULT_VARIETE + "," + UPDATED_VARIETE);

        // Get all the classificationCronquistList where variete equals to UPDATED_VARIETE
        defaultClassificationCronquistShouldNotBeFound("variete.in=" + UPDATED_VARIETE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByVarieteIsNullOrNotNull() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where variete is not null
        defaultClassificationCronquistShouldBeFound("variete.specified=true");

        // Get all the classificationCronquistList where variete is null
        defaultClassificationCronquistShouldNotBeFound("variete.specified=false");
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByVarieteContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where variete contains DEFAULT_VARIETE
        defaultClassificationCronquistShouldBeFound("variete.contains=" + DEFAULT_VARIETE);

        // Get all the classificationCronquistList where variete contains UPDATED_VARIETE
        defaultClassificationCronquistShouldNotBeFound("variete.contains=" + UPDATED_VARIETE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByVarieteNotContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where variete does not contain DEFAULT_VARIETE
        defaultClassificationCronquistShouldNotBeFound("variete.doesNotContain=" + DEFAULT_VARIETE);

        // Get all the classificationCronquistList where variete does not contain UPDATED_VARIETE
        defaultClassificationCronquistShouldBeFound("variete.doesNotContain=" + UPDATED_VARIETE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousVarieteIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousVariete equals to DEFAULT_SOUS_VARIETE
        defaultClassificationCronquistShouldBeFound("sousVariete.equals=" + DEFAULT_SOUS_VARIETE);

        // Get all the classificationCronquistList where sousVariete equals to UPDATED_SOUS_VARIETE
        defaultClassificationCronquistShouldNotBeFound("sousVariete.equals=" + UPDATED_SOUS_VARIETE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousVarieteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousVariete not equals to DEFAULT_SOUS_VARIETE
        defaultClassificationCronquistShouldNotBeFound("sousVariete.notEquals=" + DEFAULT_SOUS_VARIETE);

        // Get all the classificationCronquistList where sousVariete not equals to UPDATED_SOUS_VARIETE
        defaultClassificationCronquistShouldBeFound("sousVariete.notEquals=" + UPDATED_SOUS_VARIETE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousVarieteIsInShouldWork() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousVariete in DEFAULT_SOUS_VARIETE or UPDATED_SOUS_VARIETE
        defaultClassificationCronquistShouldBeFound("sousVariete.in=" + DEFAULT_SOUS_VARIETE + "," + UPDATED_SOUS_VARIETE);

        // Get all the classificationCronquistList where sousVariete equals to UPDATED_SOUS_VARIETE
        defaultClassificationCronquistShouldNotBeFound("sousVariete.in=" + UPDATED_SOUS_VARIETE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousVarieteIsNullOrNotNull() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousVariete is not null
        defaultClassificationCronquistShouldBeFound("sousVariete.specified=true");

        // Get all the classificationCronquistList where sousVariete is null
        defaultClassificationCronquistShouldNotBeFound("sousVariete.specified=false");
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousVarieteContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousVariete contains DEFAULT_SOUS_VARIETE
        defaultClassificationCronquistShouldBeFound("sousVariete.contains=" + DEFAULT_SOUS_VARIETE);

        // Get all the classificationCronquistList where sousVariete contains UPDATED_SOUS_VARIETE
        defaultClassificationCronquistShouldNotBeFound("sousVariete.contains=" + UPDATED_SOUS_VARIETE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsBySousVarieteNotContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where sousVariete does not contain DEFAULT_SOUS_VARIETE
        defaultClassificationCronquistShouldNotBeFound("sousVariete.doesNotContain=" + DEFAULT_SOUS_VARIETE);

        // Get all the classificationCronquistList where sousVariete does not contain UPDATED_SOUS_VARIETE
        defaultClassificationCronquistShouldBeFound("sousVariete.doesNotContain=" + UPDATED_SOUS_VARIETE);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByFormeIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where forme equals to DEFAULT_FORME
        defaultClassificationCronquistShouldBeFound("forme.equals=" + DEFAULT_FORME);

        // Get all the classificationCronquistList where forme equals to UPDATED_FORME
        defaultClassificationCronquistShouldNotBeFound("forme.equals=" + UPDATED_FORME);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByFormeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where forme not equals to DEFAULT_FORME
        defaultClassificationCronquistShouldNotBeFound("forme.notEquals=" + DEFAULT_FORME);

        // Get all the classificationCronquistList where forme not equals to UPDATED_FORME
        defaultClassificationCronquistShouldBeFound("forme.notEquals=" + UPDATED_FORME);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByFormeIsInShouldWork() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where forme in DEFAULT_FORME or UPDATED_FORME
        defaultClassificationCronquistShouldBeFound("forme.in=" + DEFAULT_FORME + "," + UPDATED_FORME);

        // Get all the classificationCronquistList where forme equals to UPDATED_FORME
        defaultClassificationCronquistShouldNotBeFound("forme.in=" + UPDATED_FORME);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByFormeIsNullOrNotNull() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where forme is not null
        defaultClassificationCronquistShouldBeFound("forme.specified=true");

        // Get all the classificationCronquistList where forme is null
        defaultClassificationCronquistShouldNotBeFound("forme.specified=false");
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByFormeContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where forme contains DEFAULT_FORME
        defaultClassificationCronquistShouldBeFound("forme.contains=" + DEFAULT_FORME);

        // Get all the classificationCronquistList where forme contains UPDATED_FORME
        defaultClassificationCronquistShouldNotBeFound("forme.contains=" + UPDATED_FORME);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByFormeNotContainsSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        // Get all the classificationCronquistList where forme does not contain DEFAULT_FORME
        defaultClassificationCronquistShouldNotBeFound("forme.doesNotContain=" + DEFAULT_FORME);

        // Get all the classificationCronquistList where forme does not contain UPDATED_FORME
        defaultClassificationCronquistShouldBeFound("forme.doesNotContain=" + UPDATED_FORME);
    }

    @Test
    @Transactional
    void getAllClassificationCronquistsByPlanteIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);
        Plante plante;
        if (TestUtil.findAll(em, Plante.class).isEmpty()) {
            plante = PlanteResourceIT.createEntity(em);
            em.persist(plante);
            em.flush();
        } else {
            plante = TestUtil.findAll(em, Plante.class).get(0);
        }
        em.persist(plante);
        em.flush();
        classificationCronquist.setPlante(plante);
        classificationCronquistRepository.saveAndFlush(classificationCronquist);
        Long planteId = plante.getId();

        // Get all the classificationCronquistList where plante equals to planteId
        defaultClassificationCronquistShouldBeFound("planteId.equals=" + planteId);

        // Get all the classificationCronquistList where plante equals to (planteId + 1)
        defaultClassificationCronquistShouldNotBeFound("planteId.equals=" + (planteId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClassificationCronquistShouldBeFound(String filter) throws Exception {
        restClassificationCronquistMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classificationCronquist.getId().intValue())))
            .andExpect(jsonPath("$.[*].superRegne").value(hasItem(DEFAULT_SUPER_REGNE)))
            .andExpect(jsonPath("$.[*].regne").value(hasItem(DEFAULT_REGNE)))
            .andExpect(jsonPath("$.[*].sousRegne").value(hasItem(DEFAULT_SOUS_REGNE)))
            .andExpect(jsonPath("$.[*].rameau").value(hasItem(DEFAULT_RAMEAU)))
            .andExpect(jsonPath("$.[*].infraRegne").value(hasItem(DEFAULT_INFRA_REGNE)))
            .andExpect(jsonPath("$.[*].superEmbranchement").value(hasItem(DEFAULT_SUPER_EMBRANCHEMENT)))
            .andExpect(jsonPath("$.[*].division").value(hasItem(DEFAULT_DIVISION)))
            .andExpect(jsonPath("$.[*].sousEmbranchement").value(hasItem(DEFAULT_SOUS_EMBRANCHEMENT)))
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
        restClassificationCronquistMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClassificationCronquistShouldNotBeFound(String filter) throws Exception {
        restClassificationCronquistMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClassificationCronquistMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingClassificationCronquist() throws Exception {
        // Get the classificationCronquist
        restClassificationCronquistMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewClassificationCronquist() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        int databaseSizeBeforeUpdate = classificationCronquistRepository.findAll().size();

        // Update the classificationCronquist
        ClassificationCronquist updatedClassificationCronquist = classificationCronquistRepository
            .findById(classificationCronquist.getId())
            .get();
        // Disconnect from session so that the updates on updatedClassificationCronquist are not directly saved in db
        em.detach(updatedClassificationCronquist);
        updatedClassificationCronquist
            .superRegne(UPDATED_SUPER_REGNE)
            .regne(UPDATED_REGNE)
            .sousRegne(UPDATED_SOUS_REGNE)
            .rameau(UPDATED_RAMEAU)
            .infraRegne(UPDATED_INFRA_REGNE)
            .superEmbranchement(UPDATED_SUPER_EMBRANCHEMENT)
            .division(UPDATED_DIVISION)
            .sousEmbranchement(UPDATED_SOUS_EMBRANCHEMENT)
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

        restClassificationCronquistMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedClassificationCronquist.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedClassificationCronquist))
            )
            .andExpect(status().isOk());

        // Validate the ClassificationCronquist in the database
        List<ClassificationCronquist> classificationCronquistList = classificationCronquistRepository.findAll();
        assertThat(classificationCronquistList).hasSize(databaseSizeBeforeUpdate);
        ClassificationCronquist testClassificationCronquist = classificationCronquistList.get(classificationCronquistList.size() - 1);
        assertThat(testClassificationCronquist.getSuperRegne()).isEqualTo(UPDATED_SUPER_REGNE);
        assertThat(testClassificationCronquist.getRegne()).isEqualTo(UPDATED_REGNE);
        assertThat(testClassificationCronquist.getSousRegne()).isEqualTo(UPDATED_SOUS_REGNE);
        assertThat(testClassificationCronquist.getRameau()).isEqualTo(UPDATED_RAMEAU);
        assertThat(testClassificationCronquist.getInfraRegne()).isEqualTo(UPDATED_INFRA_REGNE);
        assertThat(testClassificationCronquist.getSuperEmbranchement()).isEqualTo(UPDATED_SUPER_EMBRANCHEMENT);
        assertThat(testClassificationCronquist.getDivision()).isEqualTo(UPDATED_DIVISION);
        assertThat(testClassificationCronquist.getSousEmbranchement()).isEqualTo(UPDATED_SOUS_EMBRANCHEMENT);
        assertThat(testClassificationCronquist.getInfraEmbranchement()).isEqualTo(UPDATED_INFRA_EMBRANCHEMENT);
        assertThat(testClassificationCronquist.getMicroEmbranchement()).isEqualTo(UPDATED_MICRO_EMBRANCHEMENT);
        assertThat(testClassificationCronquist.getSuperClasse()).isEqualTo(UPDATED_SUPER_CLASSE);
        assertThat(testClassificationCronquist.getClasse()).isEqualTo(UPDATED_CLASSE);
        assertThat(testClassificationCronquist.getSousClasse()).isEqualTo(UPDATED_SOUS_CLASSE);
        assertThat(testClassificationCronquist.getInfraClasse()).isEqualTo(UPDATED_INFRA_CLASSE);
        assertThat(testClassificationCronquist.getSuperOrdre()).isEqualTo(UPDATED_SUPER_ORDRE);
        assertThat(testClassificationCronquist.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testClassificationCronquist.getSousOrdre()).isEqualTo(UPDATED_SOUS_ORDRE);
        assertThat(testClassificationCronquist.getInfraOrdre()).isEqualTo(UPDATED_INFRA_ORDRE);
        assertThat(testClassificationCronquist.getMicroOrdre()).isEqualTo(UPDATED_MICRO_ORDRE);
        assertThat(testClassificationCronquist.getSuperFamille()).isEqualTo(UPDATED_SUPER_FAMILLE);
        assertThat(testClassificationCronquist.getFamille()).isEqualTo(UPDATED_FAMILLE);
        assertThat(testClassificationCronquist.getSousFamille()).isEqualTo(UPDATED_SOUS_FAMILLE);
        assertThat(testClassificationCronquist.getTribu()).isEqualTo(UPDATED_TRIBU);
        assertThat(testClassificationCronquist.getSousTribu()).isEqualTo(UPDATED_SOUS_TRIBU);
        assertThat(testClassificationCronquist.getGenre()).isEqualTo(UPDATED_GENRE);
        assertThat(testClassificationCronquist.getSousGenre()).isEqualTo(UPDATED_SOUS_GENRE);
        assertThat(testClassificationCronquist.getSection()).isEqualTo(UPDATED_SECTION);
        assertThat(testClassificationCronquist.getSousSection()).isEqualTo(UPDATED_SOUS_SECTION);
        assertThat(testClassificationCronquist.getEspece()).isEqualTo(UPDATED_ESPECE);
        assertThat(testClassificationCronquist.getSousEspece()).isEqualTo(UPDATED_SOUS_ESPECE);
        assertThat(testClassificationCronquist.getVariete()).isEqualTo(UPDATED_VARIETE);
        assertThat(testClassificationCronquist.getSousVariete()).isEqualTo(UPDATED_SOUS_VARIETE);
        assertThat(testClassificationCronquist.getForme()).isEqualTo(UPDATED_FORME);
    }

    @Test
    @Transactional
    void putNonExistingClassificationCronquist() throws Exception {
        int databaseSizeBeforeUpdate = classificationCronquistRepository.findAll().size();
        classificationCronquist.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassificationCronquistMockMvc
            .perform(
                put(ENTITY_API_URL_ID, classificationCronquist.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classificationCronquist))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassificationCronquist in the database
        List<ClassificationCronquist> classificationCronquistList = classificationCronquistRepository.findAll();
        assertThat(classificationCronquistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClassificationCronquist() throws Exception {
        int databaseSizeBeforeUpdate = classificationCronquistRepository.findAll().size();
        classificationCronquist.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassificationCronquistMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classificationCronquist))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassificationCronquist in the database
        List<ClassificationCronquist> classificationCronquistList = classificationCronquistRepository.findAll();
        assertThat(classificationCronquistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClassificationCronquist() throws Exception {
        int databaseSizeBeforeUpdate = classificationCronquistRepository.findAll().size();
        classificationCronquist.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassificationCronquistMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classificationCronquist))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClassificationCronquist in the database
        List<ClassificationCronquist> classificationCronquistList = classificationCronquistRepository.findAll();
        assertThat(classificationCronquistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClassificationCronquistWithPatch() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        int databaseSizeBeforeUpdate = classificationCronquistRepository.findAll().size();

        // Update the classificationCronquist using partial update
        ClassificationCronquist partialUpdatedClassificationCronquist = new ClassificationCronquist();
        partialUpdatedClassificationCronquist.setId(classificationCronquist.getId());

        partialUpdatedClassificationCronquist
            .rameau(UPDATED_RAMEAU)
            .superEmbranchement(UPDATED_SUPER_EMBRANCHEMENT)
            .division(UPDATED_DIVISION)
            .infraEmbranchement(UPDATED_INFRA_EMBRANCHEMENT)
            .microEmbranchement(UPDATED_MICRO_EMBRANCHEMENT)
            .classe(UPDATED_CLASSE)
            .superOrdre(UPDATED_SUPER_ORDRE)
            .ordre(UPDATED_ORDRE)
            .infraOrdre(UPDATED_INFRA_ORDRE)
            .superFamille(UPDATED_SUPER_FAMILLE)
            .famille(UPDATED_FAMILLE)
            .sousFamille(UPDATED_SOUS_FAMILLE)
            .sousTribu(UPDATED_SOUS_TRIBU)
            .sousGenre(UPDATED_SOUS_GENRE)
            .forme(UPDATED_FORME);

        restClassificationCronquistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClassificationCronquist.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClassificationCronquist))
            )
            .andExpect(status().isOk());

        // Validate the ClassificationCronquist in the database
        List<ClassificationCronquist> classificationCronquistList = classificationCronquistRepository.findAll();
        assertThat(classificationCronquistList).hasSize(databaseSizeBeforeUpdate);
        ClassificationCronquist testClassificationCronquist = classificationCronquistList.get(classificationCronquistList.size() - 1);
        assertThat(testClassificationCronquist.getSuperRegne()).isEqualTo(DEFAULT_SUPER_REGNE);
        assertThat(testClassificationCronquist.getRegne()).isEqualTo(DEFAULT_REGNE);
        assertThat(testClassificationCronquist.getSousRegne()).isEqualTo(DEFAULT_SOUS_REGNE);
        assertThat(testClassificationCronquist.getRameau()).isEqualTo(UPDATED_RAMEAU);
        assertThat(testClassificationCronquist.getInfraRegne()).isEqualTo(DEFAULT_INFRA_REGNE);
        assertThat(testClassificationCronquist.getSuperEmbranchement()).isEqualTo(UPDATED_SUPER_EMBRANCHEMENT);
        assertThat(testClassificationCronquist.getDivision()).isEqualTo(UPDATED_DIVISION);
        assertThat(testClassificationCronquist.getSousEmbranchement()).isEqualTo(DEFAULT_SOUS_EMBRANCHEMENT);
        assertThat(testClassificationCronquist.getInfraEmbranchement()).isEqualTo(UPDATED_INFRA_EMBRANCHEMENT);
        assertThat(testClassificationCronquist.getMicroEmbranchement()).isEqualTo(UPDATED_MICRO_EMBRANCHEMENT);
        assertThat(testClassificationCronquist.getSuperClasse()).isEqualTo(DEFAULT_SUPER_CLASSE);
        assertThat(testClassificationCronquist.getClasse()).isEqualTo(UPDATED_CLASSE);
        assertThat(testClassificationCronquist.getSousClasse()).isEqualTo(DEFAULT_SOUS_CLASSE);
        assertThat(testClassificationCronquist.getInfraClasse()).isEqualTo(DEFAULT_INFRA_CLASSE);
        assertThat(testClassificationCronquist.getSuperOrdre()).isEqualTo(UPDATED_SUPER_ORDRE);
        assertThat(testClassificationCronquist.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testClassificationCronquist.getSousOrdre()).isEqualTo(DEFAULT_SOUS_ORDRE);
        assertThat(testClassificationCronquist.getInfraOrdre()).isEqualTo(UPDATED_INFRA_ORDRE);
        assertThat(testClassificationCronquist.getMicroOrdre()).isEqualTo(DEFAULT_MICRO_ORDRE);
        assertThat(testClassificationCronquist.getSuperFamille()).isEqualTo(UPDATED_SUPER_FAMILLE);
        assertThat(testClassificationCronquist.getFamille()).isEqualTo(UPDATED_FAMILLE);
        assertThat(testClassificationCronquist.getSousFamille()).isEqualTo(UPDATED_SOUS_FAMILLE);
        assertThat(testClassificationCronquist.getTribu()).isEqualTo(DEFAULT_TRIBU);
        assertThat(testClassificationCronquist.getSousTribu()).isEqualTo(UPDATED_SOUS_TRIBU);
        assertThat(testClassificationCronquist.getGenre()).isEqualTo(DEFAULT_GENRE);
        assertThat(testClassificationCronquist.getSousGenre()).isEqualTo(UPDATED_SOUS_GENRE);
        assertThat(testClassificationCronquist.getSection()).isEqualTo(DEFAULT_SECTION);
        assertThat(testClassificationCronquist.getSousSection()).isEqualTo(DEFAULT_SOUS_SECTION);
        assertThat(testClassificationCronquist.getEspece()).isEqualTo(DEFAULT_ESPECE);
        assertThat(testClassificationCronquist.getSousEspece()).isEqualTo(DEFAULT_SOUS_ESPECE);
        assertThat(testClassificationCronquist.getVariete()).isEqualTo(DEFAULT_VARIETE);
        assertThat(testClassificationCronquist.getSousVariete()).isEqualTo(DEFAULT_SOUS_VARIETE);
        assertThat(testClassificationCronquist.getForme()).isEqualTo(UPDATED_FORME);
    }

    @Test
    @Transactional
    void fullUpdateClassificationCronquistWithPatch() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        int databaseSizeBeforeUpdate = classificationCronquistRepository.findAll().size();

        // Update the classificationCronquist using partial update
        ClassificationCronquist partialUpdatedClassificationCronquist = new ClassificationCronquist();
        partialUpdatedClassificationCronquist.setId(classificationCronquist.getId());

        partialUpdatedClassificationCronquist
            .superRegne(UPDATED_SUPER_REGNE)
            .regne(UPDATED_REGNE)
            .sousRegne(UPDATED_SOUS_REGNE)
            .rameau(UPDATED_RAMEAU)
            .infraRegne(UPDATED_INFRA_REGNE)
            .superEmbranchement(UPDATED_SUPER_EMBRANCHEMENT)
            .division(UPDATED_DIVISION)
            .sousEmbranchement(UPDATED_SOUS_EMBRANCHEMENT)
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

        restClassificationCronquistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClassificationCronquist.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClassificationCronquist))
            )
            .andExpect(status().isOk());

        // Validate the ClassificationCronquist in the database
        List<ClassificationCronquist> classificationCronquistList = classificationCronquistRepository.findAll();
        assertThat(classificationCronquistList).hasSize(databaseSizeBeforeUpdate);
        ClassificationCronquist testClassificationCronquist = classificationCronquistList.get(classificationCronquistList.size() - 1);
        assertThat(testClassificationCronquist.getSuperRegne()).isEqualTo(UPDATED_SUPER_REGNE);
        assertThat(testClassificationCronquist.getRegne()).isEqualTo(UPDATED_REGNE);
        assertThat(testClassificationCronquist.getSousRegne()).isEqualTo(UPDATED_SOUS_REGNE);
        assertThat(testClassificationCronquist.getRameau()).isEqualTo(UPDATED_RAMEAU);
        assertThat(testClassificationCronquist.getInfraRegne()).isEqualTo(UPDATED_INFRA_REGNE);
        assertThat(testClassificationCronquist.getSuperEmbranchement()).isEqualTo(UPDATED_SUPER_EMBRANCHEMENT);
        assertThat(testClassificationCronquist.getDivision()).isEqualTo(UPDATED_DIVISION);
        assertThat(testClassificationCronquist.getSousEmbranchement()).isEqualTo(UPDATED_SOUS_EMBRANCHEMENT);
        assertThat(testClassificationCronquist.getInfraEmbranchement()).isEqualTo(UPDATED_INFRA_EMBRANCHEMENT);
        assertThat(testClassificationCronquist.getMicroEmbranchement()).isEqualTo(UPDATED_MICRO_EMBRANCHEMENT);
        assertThat(testClassificationCronquist.getSuperClasse()).isEqualTo(UPDATED_SUPER_CLASSE);
        assertThat(testClassificationCronquist.getClasse()).isEqualTo(UPDATED_CLASSE);
        assertThat(testClassificationCronquist.getSousClasse()).isEqualTo(UPDATED_SOUS_CLASSE);
        assertThat(testClassificationCronquist.getInfraClasse()).isEqualTo(UPDATED_INFRA_CLASSE);
        assertThat(testClassificationCronquist.getSuperOrdre()).isEqualTo(UPDATED_SUPER_ORDRE);
        assertThat(testClassificationCronquist.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testClassificationCronquist.getSousOrdre()).isEqualTo(UPDATED_SOUS_ORDRE);
        assertThat(testClassificationCronquist.getInfraOrdre()).isEqualTo(UPDATED_INFRA_ORDRE);
        assertThat(testClassificationCronquist.getMicroOrdre()).isEqualTo(UPDATED_MICRO_ORDRE);
        assertThat(testClassificationCronquist.getSuperFamille()).isEqualTo(UPDATED_SUPER_FAMILLE);
        assertThat(testClassificationCronquist.getFamille()).isEqualTo(UPDATED_FAMILLE);
        assertThat(testClassificationCronquist.getSousFamille()).isEqualTo(UPDATED_SOUS_FAMILLE);
        assertThat(testClassificationCronquist.getTribu()).isEqualTo(UPDATED_TRIBU);
        assertThat(testClassificationCronquist.getSousTribu()).isEqualTo(UPDATED_SOUS_TRIBU);
        assertThat(testClassificationCronquist.getGenre()).isEqualTo(UPDATED_GENRE);
        assertThat(testClassificationCronquist.getSousGenre()).isEqualTo(UPDATED_SOUS_GENRE);
        assertThat(testClassificationCronquist.getSection()).isEqualTo(UPDATED_SECTION);
        assertThat(testClassificationCronquist.getSousSection()).isEqualTo(UPDATED_SOUS_SECTION);
        assertThat(testClassificationCronquist.getEspece()).isEqualTo(UPDATED_ESPECE);
        assertThat(testClassificationCronquist.getSousEspece()).isEqualTo(UPDATED_SOUS_ESPECE);
        assertThat(testClassificationCronquist.getVariete()).isEqualTo(UPDATED_VARIETE);
        assertThat(testClassificationCronquist.getSousVariete()).isEqualTo(UPDATED_SOUS_VARIETE);
        assertThat(testClassificationCronquist.getForme()).isEqualTo(UPDATED_FORME);
    }

    @Test
    @Transactional
    void patchNonExistingClassificationCronquist() throws Exception {
        int databaseSizeBeforeUpdate = classificationCronquistRepository.findAll().size();
        classificationCronquist.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassificationCronquistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, classificationCronquist.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(classificationCronquist))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassificationCronquist in the database
        List<ClassificationCronquist> classificationCronquistList = classificationCronquistRepository.findAll();
        assertThat(classificationCronquistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClassificationCronquist() throws Exception {
        int databaseSizeBeforeUpdate = classificationCronquistRepository.findAll().size();
        classificationCronquist.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassificationCronquistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(classificationCronquist))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassificationCronquist in the database
        List<ClassificationCronquist> classificationCronquistList = classificationCronquistRepository.findAll();
        assertThat(classificationCronquistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClassificationCronquist() throws Exception {
        int databaseSizeBeforeUpdate = classificationCronquistRepository.findAll().size();
        classificationCronquist.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassificationCronquistMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(classificationCronquist))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClassificationCronquist in the database
        List<ClassificationCronquist> classificationCronquistList = classificationCronquistRepository.findAll();
        assertThat(classificationCronquistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClassificationCronquist() throws Exception {
        // Initialize the database
        classificationCronquistRepository.saveAndFlush(classificationCronquist);

        int databaseSizeBeforeDelete = classificationCronquistRepository.findAll().size();

        // Delete the classificationCronquist
        restClassificationCronquistMockMvc
            .perform(delete(ENTITY_API_URL_ID, classificationCronquist.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ClassificationCronquist> classificationCronquistList = classificationCronquistRepository.findAll();
        assertThat(classificationCronquistList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
