package fr.syncrase.perma.web.rest;

import fr.syncrase.perma.MicroserviceApp;
import fr.syncrase.perma.config.TestSecurityConfiguration;
import fr.syncrase.perma.domain.Cronquist;
import fr.syncrase.perma.repository.CronquistRepository;
import fr.syncrase.perma.service.CronquistService;
import fr.syncrase.perma.service.dto.CronquistDTO;
import fr.syncrase.perma.service.mapper.CronquistMapper;
import fr.syncrase.perma.service.dto.CronquistCriteria;
import fr.syncrase.perma.service.CronquistQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CronquistResource} REST controller.
 */
@SpringBootTest(classes = { MicroserviceApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class CronquistResourceIT {

    private static final String DEFAULT_REGNE = "AAAAAAAAAA";
    private static final String UPDATED_REGNE = "BBBBBBBBBB";

    private static final String DEFAULT_SOUS_REGNE = "AAAAAAAAAA";
    private static final String UPDATED_SOUS_REGNE = "BBBBBBBBBB";

    private static final String DEFAULT_DIVISION = "AAAAAAAAAA";
    private static final String UPDATED_DIVISION = "BBBBBBBBBB";

    private static final String DEFAULT_CLASSE = "AAAAAAAAAA";
    private static final String UPDATED_CLASSE = "BBBBBBBBBB";

    private static final String DEFAULT_SOUS_CLASSE = "AAAAAAAAAA";
    private static final String UPDATED_SOUS_CLASSE = "BBBBBBBBBB";

    private static final String DEFAULT_ORDRE = "AAAAAAAAAA";
    private static final String UPDATED_ORDRE = "BBBBBBBBBB";

    private static final String DEFAULT_FAMILLE = "AAAAAAAAAA";
    private static final String UPDATED_FAMILLE = "BBBBBBBBBB";

    private static final String DEFAULT_GENRE = "AAAAAAAAAA";
    private static final String UPDATED_GENRE = "BBBBBBBBBB";

    @Autowired
    private CronquistRepository cronquistRepository;

    @Autowired
    private CronquistMapper cronquistMapper;

    @Autowired
    private CronquistService cronquistService;

    @Autowired
    private CronquistQueryService cronquistQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCronquistMockMvc;

    private Cronquist cronquist;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cronquist createEntity(EntityManager em) {
        Cronquist cronquist = new Cronquist()
            .regne(DEFAULT_REGNE)
            .sousRegne(DEFAULT_SOUS_REGNE)
            .division(DEFAULT_DIVISION)
            .classe(DEFAULT_CLASSE)
            .sousClasse(DEFAULT_SOUS_CLASSE)
            .ordre(DEFAULT_ORDRE)
            .famille(DEFAULT_FAMILLE)
            .genre(DEFAULT_GENRE);
        return cronquist;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cronquist createUpdatedEntity(EntityManager em) {
        Cronquist cronquist = new Cronquist()
            .regne(UPDATED_REGNE)
            .sousRegne(UPDATED_SOUS_REGNE)
            .division(UPDATED_DIVISION)
            .classe(UPDATED_CLASSE)
            .sousClasse(UPDATED_SOUS_CLASSE)
            .ordre(UPDATED_ORDRE)
            .famille(UPDATED_FAMILLE)
            .genre(UPDATED_GENRE);
        return cronquist;
    }

    @BeforeEach
    public void initTest() {
        cronquist = createEntity(em);
    }

    @Test
    @Transactional
    public void createCronquist() throws Exception {
        int databaseSizeBeforeCreate = cronquistRepository.findAll().size();
        // Create the Cronquist
        CronquistDTO cronquistDTO = cronquistMapper.toDto(cronquist);
        restCronquistMockMvc.perform(post("/api/cronquists").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cronquistDTO)))
            .andExpect(status().isCreated());

        // Validate the Cronquist in the database
        List<Cronquist> cronquistList = cronquistRepository.findAll();
        assertThat(cronquistList).hasSize(databaseSizeBeforeCreate + 1);
        Cronquist testCronquist = cronquistList.get(cronquistList.size() - 1);
        assertThat(testCronquist.getRegne()).isEqualTo(DEFAULT_REGNE);
        assertThat(testCronquist.getSousRegne()).isEqualTo(DEFAULT_SOUS_REGNE);
        assertThat(testCronquist.getDivision()).isEqualTo(DEFAULT_DIVISION);
        assertThat(testCronquist.getClasse()).isEqualTo(DEFAULT_CLASSE);
        assertThat(testCronquist.getSousClasse()).isEqualTo(DEFAULT_SOUS_CLASSE);
        assertThat(testCronquist.getOrdre()).isEqualTo(DEFAULT_ORDRE);
        assertThat(testCronquist.getFamille()).isEqualTo(DEFAULT_FAMILLE);
        assertThat(testCronquist.getGenre()).isEqualTo(DEFAULT_GENRE);
    }

    @Test
    @Transactional
    public void createCronquistWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cronquistRepository.findAll().size();

        // Create the Cronquist with an existing ID
        cronquist.setId(1L);
        CronquistDTO cronquistDTO = cronquistMapper.toDto(cronquist);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCronquistMockMvc.perform(post("/api/cronquists").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cronquistDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cronquist in the database
        List<Cronquist> cronquistList = cronquistRepository.findAll();
        assertThat(cronquistList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkRegneIsRequired() throws Exception {
        int databaseSizeBeforeTest = cronquistRepository.findAll().size();
        // set the field null
        cronquist.setRegne(null);

        // Create the Cronquist, which fails.
        CronquistDTO cronquistDTO = cronquistMapper.toDto(cronquist);


        restCronquistMockMvc.perform(post("/api/cronquists").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cronquistDTO)))
            .andExpect(status().isBadRequest());

        List<Cronquist> cronquistList = cronquistRepository.findAll();
        assertThat(cronquistList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSousRegneIsRequired() throws Exception {
        int databaseSizeBeforeTest = cronquistRepository.findAll().size();
        // set the field null
        cronquist.setSousRegne(null);

        // Create the Cronquist, which fails.
        CronquistDTO cronquistDTO = cronquistMapper.toDto(cronquist);


        restCronquistMockMvc.perform(post("/api/cronquists").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cronquistDTO)))
            .andExpect(status().isBadRequest());

        List<Cronquist> cronquistList = cronquistRepository.findAll();
        assertThat(cronquistList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDivisionIsRequired() throws Exception {
        int databaseSizeBeforeTest = cronquistRepository.findAll().size();
        // set the field null
        cronquist.setDivision(null);

        // Create the Cronquist, which fails.
        CronquistDTO cronquistDTO = cronquistMapper.toDto(cronquist);


        restCronquistMockMvc.perform(post("/api/cronquists").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cronquistDTO)))
            .andExpect(status().isBadRequest());

        List<Cronquist> cronquistList = cronquistRepository.findAll();
        assertThat(cronquistList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkClasseIsRequired() throws Exception {
        int databaseSizeBeforeTest = cronquistRepository.findAll().size();
        // set the field null
        cronquist.setClasse(null);

        // Create the Cronquist, which fails.
        CronquistDTO cronquistDTO = cronquistMapper.toDto(cronquist);


        restCronquistMockMvc.perform(post("/api/cronquists").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cronquistDTO)))
            .andExpect(status().isBadRequest());

        List<Cronquist> cronquistList = cronquistRepository.findAll();
        assertThat(cronquistList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSousClasseIsRequired() throws Exception {
        int databaseSizeBeforeTest = cronquistRepository.findAll().size();
        // set the field null
        cronquist.setSousClasse(null);

        // Create the Cronquist, which fails.
        CronquistDTO cronquistDTO = cronquistMapper.toDto(cronquist);


        restCronquistMockMvc.perform(post("/api/cronquists").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cronquistDTO)))
            .andExpect(status().isBadRequest());

        List<Cronquist> cronquistList = cronquistRepository.findAll();
        assertThat(cronquistList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOrdreIsRequired() throws Exception {
        int databaseSizeBeforeTest = cronquistRepository.findAll().size();
        // set the field null
        cronquist.setOrdre(null);

        // Create the Cronquist, which fails.
        CronquistDTO cronquistDTO = cronquistMapper.toDto(cronquist);


        restCronquistMockMvc.perform(post("/api/cronquists").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cronquistDTO)))
            .andExpect(status().isBadRequest());

        List<Cronquist> cronquistList = cronquistRepository.findAll();
        assertThat(cronquistList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFamilleIsRequired() throws Exception {
        int databaseSizeBeforeTest = cronquistRepository.findAll().size();
        // set the field null
        cronquist.setFamille(null);

        // Create the Cronquist, which fails.
        CronquistDTO cronquistDTO = cronquistMapper.toDto(cronquist);


        restCronquistMockMvc.perform(post("/api/cronquists").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cronquistDTO)))
            .andExpect(status().isBadRequest());

        List<Cronquist> cronquistList = cronquistRepository.findAll();
        assertThat(cronquistList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGenreIsRequired() throws Exception {
        int databaseSizeBeforeTest = cronquistRepository.findAll().size();
        // set the field null
        cronquist.setGenre(null);

        // Create the Cronquist, which fails.
        CronquistDTO cronquistDTO = cronquistMapper.toDto(cronquist);


        restCronquistMockMvc.perform(post("/api/cronquists").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cronquistDTO)))
            .andExpect(status().isBadRequest());

        List<Cronquist> cronquistList = cronquistRepository.findAll();
        assertThat(cronquistList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCronquists() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList
        restCronquistMockMvc.perform(get("/api/cronquists?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cronquist.getId().intValue())))
            .andExpect(jsonPath("$.[*].regne").value(hasItem(DEFAULT_REGNE)))
            .andExpect(jsonPath("$.[*].sousRegne").value(hasItem(DEFAULT_SOUS_REGNE)))
            .andExpect(jsonPath("$.[*].division").value(hasItem(DEFAULT_DIVISION)))
            .andExpect(jsonPath("$.[*].classe").value(hasItem(DEFAULT_CLASSE)))
            .andExpect(jsonPath("$.[*].sousClasse").value(hasItem(DEFAULT_SOUS_CLASSE)))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)))
            .andExpect(jsonPath("$.[*].famille").value(hasItem(DEFAULT_FAMILLE)))
            .andExpect(jsonPath("$.[*].genre").value(hasItem(DEFAULT_GENRE)));
    }
    
    @Test
    @Transactional
    public void getCronquist() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get the cronquist
        restCronquistMockMvc.perform(get("/api/cronquists/{id}", cronquist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cronquist.getId().intValue()))
            .andExpect(jsonPath("$.regne").value(DEFAULT_REGNE))
            .andExpect(jsonPath("$.sousRegne").value(DEFAULT_SOUS_REGNE))
            .andExpect(jsonPath("$.division").value(DEFAULT_DIVISION))
            .andExpect(jsonPath("$.classe").value(DEFAULT_CLASSE))
            .andExpect(jsonPath("$.sousClasse").value(DEFAULT_SOUS_CLASSE))
            .andExpect(jsonPath("$.ordre").value(DEFAULT_ORDRE))
            .andExpect(jsonPath("$.famille").value(DEFAULT_FAMILLE))
            .andExpect(jsonPath("$.genre").value(DEFAULT_GENRE));
    }


    @Test
    @Transactional
    public void getCronquistsByIdFiltering() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        Long id = cronquist.getId();

        defaultCronquistShouldBeFound("id.equals=" + id);
        defaultCronquistShouldNotBeFound("id.notEquals=" + id);

        defaultCronquistShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCronquistShouldNotBeFound("id.greaterThan=" + id);

        defaultCronquistShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCronquistShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCronquistsByRegneIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where regne equals to DEFAULT_REGNE
        defaultCronquistShouldBeFound("regne.equals=" + DEFAULT_REGNE);

        // Get all the cronquistList where regne equals to UPDATED_REGNE
        defaultCronquistShouldNotBeFound("regne.equals=" + UPDATED_REGNE);
    }

    @Test
    @Transactional
    public void getAllCronquistsByRegneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where regne not equals to DEFAULT_REGNE
        defaultCronquistShouldNotBeFound("regne.notEquals=" + DEFAULT_REGNE);

        // Get all the cronquistList where regne not equals to UPDATED_REGNE
        defaultCronquistShouldBeFound("regne.notEquals=" + UPDATED_REGNE);
    }

    @Test
    @Transactional
    public void getAllCronquistsByRegneIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where regne in DEFAULT_REGNE or UPDATED_REGNE
        defaultCronquistShouldBeFound("regne.in=" + DEFAULT_REGNE + "," + UPDATED_REGNE);

        // Get all the cronquistList where regne equals to UPDATED_REGNE
        defaultCronquistShouldNotBeFound("regne.in=" + UPDATED_REGNE);
    }

    @Test
    @Transactional
    public void getAllCronquistsByRegneIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where regne is not null
        defaultCronquistShouldBeFound("regne.specified=true");

        // Get all the cronquistList where regne is null
        defaultCronquistShouldNotBeFound("regne.specified=false");
    }
                @Test
    @Transactional
    public void getAllCronquistsByRegneContainsSomething() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where regne contains DEFAULT_REGNE
        defaultCronquistShouldBeFound("regne.contains=" + DEFAULT_REGNE);

        // Get all the cronquistList where regne contains UPDATED_REGNE
        defaultCronquistShouldNotBeFound("regne.contains=" + UPDATED_REGNE);
    }

    @Test
    @Transactional
    public void getAllCronquistsByRegneNotContainsSomething() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where regne does not contain DEFAULT_REGNE
        defaultCronquistShouldNotBeFound("regne.doesNotContain=" + DEFAULT_REGNE);

        // Get all the cronquistList where regne does not contain UPDATED_REGNE
        defaultCronquistShouldBeFound("regne.doesNotContain=" + UPDATED_REGNE);
    }


    @Test
    @Transactional
    public void getAllCronquistsBySousRegneIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where sousRegne equals to DEFAULT_SOUS_REGNE
        defaultCronquistShouldBeFound("sousRegne.equals=" + DEFAULT_SOUS_REGNE);

        // Get all the cronquistList where sousRegne equals to UPDATED_SOUS_REGNE
        defaultCronquistShouldNotBeFound("sousRegne.equals=" + UPDATED_SOUS_REGNE);
    }

    @Test
    @Transactional
    public void getAllCronquistsBySousRegneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where sousRegne not equals to DEFAULT_SOUS_REGNE
        defaultCronquistShouldNotBeFound("sousRegne.notEquals=" + DEFAULT_SOUS_REGNE);

        // Get all the cronquistList where sousRegne not equals to UPDATED_SOUS_REGNE
        defaultCronquistShouldBeFound("sousRegne.notEquals=" + UPDATED_SOUS_REGNE);
    }

    @Test
    @Transactional
    public void getAllCronquistsBySousRegneIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where sousRegne in DEFAULT_SOUS_REGNE or UPDATED_SOUS_REGNE
        defaultCronquistShouldBeFound("sousRegne.in=" + DEFAULT_SOUS_REGNE + "," + UPDATED_SOUS_REGNE);

        // Get all the cronquistList where sousRegne equals to UPDATED_SOUS_REGNE
        defaultCronquistShouldNotBeFound("sousRegne.in=" + UPDATED_SOUS_REGNE);
    }

    @Test
    @Transactional
    public void getAllCronquistsBySousRegneIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where sousRegne is not null
        defaultCronquistShouldBeFound("sousRegne.specified=true");

        // Get all the cronquistList where sousRegne is null
        defaultCronquistShouldNotBeFound("sousRegne.specified=false");
    }
                @Test
    @Transactional
    public void getAllCronquistsBySousRegneContainsSomething() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where sousRegne contains DEFAULT_SOUS_REGNE
        defaultCronquistShouldBeFound("sousRegne.contains=" + DEFAULT_SOUS_REGNE);

        // Get all the cronquistList where sousRegne contains UPDATED_SOUS_REGNE
        defaultCronquistShouldNotBeFound("sousRegne.contains=" + UPDATED_SOUS_REGNE);
    }

    @Test
    @Transactional
    public void getAllCronquistsBySousRegneNotContainsSomething() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where sousRegne does not contain DEFAULT_SOUS_REGNE
        defaultCronquistShouldNotBeFound("sousRegne.doesNotContain=" + DEFAULT_SOUS_REGNE);

        // Get all the cronquistList where sousRegne does not contain UPDATED_SOUS_REGNE
        defaultCronquistShouldBeFound("sousRegne.doesNotContain=" + UPDATED_SOUS_REGNE);
    }


    @Test
    @Transactional
    public void getAllCronquistsByDivisionIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where division equals to DEFAULT_DIVISION
        defaultCronquistShouldBeFound("division.equals=" + DEFAULT_DIVISION);

        // Get all the cronquistList where division equals to UPDATED_DIVISION
        defaultCronquistShouldNotBeFound("division.equals=" + UPDATED_DIVISION);
    }

    @Test
    @Transactional
    public void getAllCronquistsByDivisionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where division not equals to DEFAULT_DIVISION
        defaultCronquistShouldNotBeFound("division.notEquals=" + DEFAULT_DIVISION);

        // Get all the cronquistList where division not equals to UPDATED_DIVISION
        defaultCronquistShouldBeFound("division.notEquals=" + UPDATED_DIVISION);
    }

    @Test
    @Transactional
    public void getAllCronquistsByDivisionIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where division in DEFAULT_DIVISION or UPDATED_DIVISION
        defaultCronquistShouldBeFound("division.in=" + DEFAULT_DIVISION + "," + UPDATED_DIVISION);

        // Get all the cronquistList where division equals to UPDATED_DIVISION
        defaultCronquistShouldNotBeFound("division.in=" + UPDATED_DIVISION);
    }

    @Test
    @Transactional
    public void getAllCronquistsByDivisionIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where division is not null
        defaultCronquistShouldBeFound("division.specified=true");

        // Get all the cronquistList where division is null
        defaultCronquistShouldNotBeFound("division.specified=false");
    }
                @Test
    @Transactional
    public void getAllCronquistsByDivisionContainsSomething() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where division contains DEFAULT_DIVISION
        defaultCronquistShouldBeFound("division.contains=" + DEFAULT_DIVISION);

        // Get all the cronquistList where division contains UPDATED_DIVISION
        defaultCronquistShouldNotBeFound("division.contains=" + UPDATED_DIVISION);
    }

    @Test
    @Transactional
    public void getAllCronquistsByDivisionNotContainsSomething() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where division does not contain DEFAULT_DIVISION
        defaultCronquistShouldNotBeFound("division.doesNotContain=" + DEFAULT_DIVISION);

        // Get all the cronquistList where division does not contain UPDATED_DIVISION
        defaultCronquistShouldBeFound("division.doesNotContain=" + UPDATED_DIVISION);
    }


    @Test
    @Transactional
    public void getAllCronquistsByClasseIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where classe equals to DEFAULT_CLASSE
        defaultCronquistShouldBeFound("classe.equals=" + DEFAULT_CLASSE);

        // Get all the cronquistList where classe equals to UPDATED_CLASSE
        defaultCronquistShouldNotBeFound("classe.equals=" + UPDATED_CLASSE);
    }

    @Test
    @Transactional
    public void getAllCronquistsByClasseIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where classe not equals to DEFAULT_CLASSE
        defaultCronquistShouldNotBeFound("classe.notEquals=" + DEFAULT_CLASSE);

        // Get all the cronquistList where classe not equals to UPDATED_CLASSE
        defaultCronquistShouldBeFound("classe.notEquals=" + UPDATED_CLASSE);
    }

    @Test
    @Transactional
    public void getAllCronquistsByClasseIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where classe in DEFAULT_CLASSE or UPDATED_CLASSE
        defaultCronquistShouldBeFound("classe.in=" + DEFAULT_CLASSE + "," + UPDATED_CLASSE);

        // Get all the cronquistList where classe equals to UPDATED_CLASSE
        defaultCronquistShouldNotBeFound("classe.in=" + UPDATED_CLASSE);
    }

    @Test
    @Transactional
    public void getAllCronquistsByClasseIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where classe is not null
        defaultCronquistShouldBeFound("classe.specified=true");

        // Get all the cronquistList where classe is null
        defaultCronquistShouldNotBeFound("classe.specified=false");
    }
                @Test
    @Transactional
    public void getAllCronquistsByClasseContainsSomething() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where classe contains DEFAULT_CLASSE
        defaultCronquistShouldBeFound("classe.contains=" + DEFAULT_CLASSE);

        // Get all the cronquistList where classe contains UPDATED_CLASSE
        defaultCronquistShouldNotBeFound("classe.contains=" + UPDATED_CLASSE);
    }

    @Test
    @Transactional
    public void getAllCronquistsByClasseNotContainsSomething() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where classe does not contain DEFAULT_CLASSE
        defaultCronquistShouldNotBeFound("classe.doesNotContain=" + DEFAULT_CLASSE);

        // Get all the cronquistList where classe does not contain UPDATED_CLASSE
        defaultCronquistShouldBeFound("classe.doesNotContain=" + UPDATED_CLASSE);
    }


    @Test
    @Transactional
    public void getAllCronquistsBySousClasseIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where sousClasse equals to DEFAULT_SOUS_CLASSE
        defaultCronquistShouldBeFound("sousClasse.equals=" + DEFAULT_SOUS_CLASSE);

        // Get all the cronquistList where sousClasse equals to UPDATED_SOUS_CLASSE
        defaultCronquistShouldNotBeFound("sousClasse.equals=" + UPDATED_SOUS_CLASSE);
    }

    @Test
    @Transactional
    public void getAllCronquistsBySousClasseIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where sousClasse not equals to DEFAULT_SOUS_CLASSE
        defaultCronquistShouldNotBeFound("sousClasse.notEquals=" + DEFAULT_SOUS_CLASSE);

        // Get all the cronquistList where sousClasse not equals to UPDATED_SOUS_CLASSE
        defaultCronquistShouldBeFound("sousClasse.notEquals=" + UPDATED_SOUS_CLASSE);
    }

    @Test
    @Transactional
    public void getAllCronquistsBySousClasseIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where sousClasse in DEFAULT_SOUS_CLASSE or UPDATED_SOUS_CLASSE
        defaultCronquistShouldBeFound("sousClasse.in=" + DEFAULT_SOUS_CLASSE + "," + UPDATED_SOUS_CLASSE);

        // Get all the cronquistList where sousClasse equals to UPDATED_SOUS_CLASSE
        defaultCronquistShouldNotBeFound("sousClasse.in=" + UPDATED_SOUS_CLASSE);
    }

    @Test
    @Transactional
    public void getAllCronquistsBySousClasseIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where sousClasse is not null
        defaultCronquistShouldBeFound("sousClasse.specified=true");

        // Get all the cronquistList where sousClasse is null
        defaultCronquistShouldNotBeFound("sousClasse.specified=false");
    }
                @Test
    @Transactional
    public void getAllCronquistsBySousClasseContainsSomething() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where sousClasse contains DEFAULT_SOUS_CLASSE
        defaultCronquistShouldBeFound("sousClasse.contains=" + DEFAULT_SOUS_CLASSE);

        // Get all the cronquistList where sousClasse contains UPDATED_SOUS_CLASSE
        defaultCronquistShouldNotBeFound("sousClasse.contains=" + UPDATED_SOUS_CLASSE);
    }

    @Test
    @Transactional
    public void getAllCronquistsBySousClasseNotContainsSomething() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where sousClasse does not contain DEFAULT_SOUS_CLASSE
        defaultCronquistShouldNotBeFound("sousClasse.doesNotContain=" + DEFAULT_SOUS_CLASSE);

        // Get all the cronquistList where sousClasse does not contain UPDATED_SOUS_CLASSE
        defaultCronquistShouldBeFound("sousClasse.doesNotContain=" + UPDATED_SOUS_CLASSE);
    }


    @Test
    @Transactional
    public void getAllCronquistsByOrdreIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where ordre equals to DEFAULT_ORDRE
        defaultCronquistShouldBeFound("ordre.equals=" + DEFAULT_ORDRE);

        // Get all the cronquistList where ordre equals to UPDATED_ORDRE
        defaultCronquistShouldNotBeFound("ordre.equals=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    public void getAllCronquistsByOrdreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where ordre not equals to DEFAULT_ORDRE
        defaultCronquistShouldNotBeFound("ordre.notEquals=" + DEFAULT_ORDRE);

        // Get all the cronquistList where ordre not equals to UPDATED_ORDRE
        defaultCronquistShouldBeFound("ordre.notEquals=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    public void getAllCronquistsByOrdreIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where ordre in DEFAULT_ORDRE or UPDATED_ORDRE
        defaultCronquistShouldBeFound("ordre.in=" + DEFAULT_ORDRE + "," + UPDATED_ORDRE);

        // Get all the cronquistList where ordre equals to UPDATED_ORDRE
        defaultCronquistShouldNotBeFound("ordre.in=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    public void getAllCronquistsByOrdreIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where ordre is not null
        defaultCronquistShouldBeFound("ordre.specified=true");

        // Get all the cronquistList where ordre is null
        defaultCronquistShouldNotBeFound("ordre.specified=false");
    }
                @Test
    @Transactional
    public void getAllCronquistsByOrdreContainsSomething() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where ordre contains DEFAULT_ORDRE
        defaultCronquistShouldBeFound("ordre.contains=" + DEFAULT_ORDRE);

        // Get all the cronquistList where ordre contains UPDATED_ORDRE
        defaultCronquistShouldNotBeFound("ordre.contains=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    public void getAllCronquistsByOrdreNotContainsSomething() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where ordre does not contain DEFAULT_ORDRE
        defaultCronquistShouldNotBeFound("ordre.doesNotContain=" + DEFAULT_ORDRE);

        // Get all the cronquistList where ordre does not contain UPDATED_ORDRE
        defaultCronquistShouldBeFound("ordre.doesNotContain=" + UPDATED_ORDRE);
    }


    @Test
    @Transactional
    public void getAllCronquistsByFamilleIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where famille equals to DEFAULT_FAMILLE
        defaultCronquistShouldBeFound("famille.equals=" + DEFAULT_FAMILLE);

        // Get all the cronquistList where famille equals to UPDATED_FAMILLE
        defaultCronquistShouldNotBeFound("famille.equals=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    public void getAllCronquistsByFamilleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where famille not equals to DEFAULT_FAMILLE
        defaultCronquistShouldNotBeFound("famille.notEquals=" + DEFAULT_FAMILLE);

        // Get all the cronquistList where famille not equals to UPDATED_FAMILLE
        defaultCronquistShouldBeFound("famille.notEquals=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    public void getAllCronquistsByFamilleIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where famille in DEFAULT_FAMILLE or UPDATED_FAMILLE
        defaultCronquistShouldBeFound("famille.in=" + DEFAULT_FAMILLE + "," + UPDATED_FAMILLE);

        // Get all the cronquistList where famille equals to UPDATED_FAMILLE
        defaultCronquistShouldNotBeFound("famille.in=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    public void getAllCronquistsByFamilleIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where famille is not null
        defaultCronquistShouldBeFound("famille.specified=true");

        // Get all the cronquistList where famille is null
        defaultCronquistShouldNotBeFound("famille.specified=false");
    }
                @Test
    @Transactional
    public void getAllCronquistsByFamilleContainsSomething() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where famille contains DEFAULT_FAMILLE
        defaultCronquistShouldBeFound("famille.contains=" + DEFAULT_FAMILLE);

        // Get all the cronquistList where famille contains UPDATED_FAMILLE
        defaultCronquistShouldNotBeFound("famille.contains=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    public void getAllCronquistsByFamilleNotContainsSomething() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where famille does not contain DEFAULT_FAMILLE
        defaultCronquistShouldNotBeFound("famille.doesNotContain=" + DEFAULT_FAMILLE);

        // Get all the cronquistList where famille does not contain UPDATED_FAMILLE
        defaultCronquistShouldBeFound("famille.doesNotContain=" + UPDATED_FAMILLE);
    }


    @Test
    @Transactional
    public void getAllCronquistsByGenreIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where genre equals to DEFAULT_GENRE
        defaultCronquistShouldBeFound("genre.equals=" + DEFAULT_GENRE);

        // Get all the cronquistList where genre equals to UPDATED_GENRE
        defaultCronquistShouldNotBeFound("genre.equals=" + UPDATED_GENRE);
    }

    @Test
    @Transactional
    public void getAllCronquistsByGenreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where genre not equals to DEFAULT_GENRE
        defaultCronquistShouldNotBeFound("genre.notEquals=" + DEFAULT_GENRE);

        // Get all the cronquistList where genre not equals to UPDATED_GENRE
        defaultCronquistShouldBeFound("genre.notEquals=" + UPDATED_GENRE);
    }

    @Test
    @Transactional
    public void getAllCronquistsByGenreIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where genre in DEFAULT_GENRE or UPDATED_GENRE
        defaultCronquistShouldBeFound("genre.in=" + DEFAULT_GENRE + "," + UPDATED_GENRE);

        // Get all the cronquistList where genre equals to UPDATED_GENRE
        defaultCronquistShouldNotBeFound("genre.in=" + UPDATED_GENRE);
    }

    @Test
    @Transactional
    public void getAllCronquistsByGenreIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where genre is not null
        defaultCronquistShouldBeFound("genre.specified=true");

        // Get all the cronquistList where genre is null
        defaultCronquistShouldNotBeFound("genre.specified=false");
    }
                @Test
    @Transactional
    public void getAllCronquistsByGenreContainsSomething() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where genre contains DEFAULT_GENRE
        defaultCronquistShouldBeFound("genre.contains=" + DEFAULT_GENRE);

        // Get all the cronquistList where genre contains UPDATED_GENRE
        defaultCronquistShouldNotBeFound("genre.contains=" + UPDATED_GENRE);
    }

    @Test
    @Transactional
    public void getAllCronquistsByGenreNotContainsSomething() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        // Get all the cronquistList where genre does not contain DEFAULT_GENRE
        defaultCronquistShouldNotBeFound("genre.doesNotContain=" + DEFAULT_GENRE);

        // Get all the cronquistList where genre does not contain UPDATED_GENRE
        defaultCronquistShouldBeFound("genre.doesNotContain=" + UPDATED_GENRE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCronquistShouldBeFound(String filter) throws Exception {
        restCronquistMockMvc.perform(get("/api/cronquists?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cronquist.getId().intValue())))
            .andExpect(jsonPath("$.[*].regne").value(hasItem(DEFAULT_REGNE)))
            .andExpect(jsonPath("$.[*].sousRegne").value(hasItem(DEFAULT_SOUS_REGNE)))
            .andExpect(jsonPath("$.[*].division").value(hasItem(DEFAULT_DIVISION)))
            .andExpect(jsonPath("$.[*].classe").value(hasItem(DEFAULT_CLASSE)))
            .andExpect(jsonPath("$.[*].sousClasse").value(hasItem(DEFAULT_SOUS_CLASSE)))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)))
            .andExpect(jsonPath("$.[*].famille").value(hasItem(DEFAULT_FAMILLE)))
            .andExpect(jsonPath("$.[*].genre").value(hasItem(DEFAULT_GENRE)));

        // Check, that the count call also returns 1
        restCronquistMockMvc.perform(get("/api/cronquists/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCronquistShouldNotBeFound(String filter) throws Exception {
        restCronquistMockMvc.perform(get("/api/cronquists?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCronquistMockMvc.perform(get("/api/cronquists/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingCronquist() throws Exception {
        // Get the cronquist
        restCronquistMockMvc.perform(get("/api/cronquists/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCronquist() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        int databaseSizeBeforeUpdate = cronquistRepository.findAll().size();

        // Update the cronquist
        Cronquist updatedCronquist = cronquistRepository.findById(cronquist.getId()).get();
        // Disconnect from session so that the updates on updatedCronquist are not directly saved in db
        em.detach(updatedCronquist);
        updatedCronquist
            .regne(UPDATED_REGNE)
            .sousRegne(UPDATED_SOUS_REGNE)
            .division(UPDATED_DIVISION)
            .classe(UPDATED_CLASSE)
            .sousClasse(UPDATED_SOUS_CLASSE)
            .ordre(UPDATED_ORDRE)
            .famille(UPDATED_FAMILLE)
            .genre(UPDATED_GENRE);
        CronquistDTO cronquistDTO = cronquistMapper.toDto(updatedCronquist);

        restCronquistMockMvc.perform(put("/api/cronquists").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cronquistDTO)))
            .andExpect(status().isOk());

        // Validate the Cronquist in the database
        List<Cronquist> cronquistList = cronquistRepository.findAll();
        assertThat(cronquistList).hasSize(databaseSizeBeforeUpdate);
        Cronquist testCronquist = cronquistList.get(cronquistList.size() - 1);
        assertThat(testCronquist.getRegne()).isEqualTo(UPDATED_REGNE);
        assertThat(testCronquist.getSousRegne()).isEqualTo(UPDATED_SOUS_REGNE);
        assertThat(testCronquist.getDivision()).isEqualTo(UPDATED_DIVISION);
        assertThat(testCronquist.getClasse()).isEqualTo(UPDATED_CLASSE);
        assertThat(testCronquist.getSousClasse()).isEqualTo(UPDATED_SOUS_CLASSE);
        assertThat(testCronquist.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testCronquist.getFamille()).isEqualTo(UPDATED_FAMILLE);
        assertThat(testCronquist.getGenre()).isEqualTo(UPDATED_GENRE);
    }

    @Test
    @Transactional
    public void updateNonExistingCronquist() throws Exception {
        int databaseSizeBeforeUpdate = cronquistRepository.findAll().size();

        // Create the Cronquist
        CronquistDTO cronquistDTO = cronquistMapper.toDto(cronquist);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCronquistMockMvc.perform(put("/api/cronquists").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cronquistDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cronquist in the database
        List<Cronquist> cronquistList = cronquistRepository.findAll();
        assertThat(cronquistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCronquist() throws Exception {
        // Initialize the database
        cronquistRepository.saveAndFlush(cronquist);

        int databaseSizeBeforeDelete = cronquistRepository.findAll().size();

        // Delete the cronquist
        restCronquistMockMvc.perform(delete("/api/cronquists/{id}", cronquist.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cronquist> cronquistList = cronquistRepository.findAll();
        assertThat(cronquistList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
