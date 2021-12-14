package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.APGIIIPlante;
import fr.syncrase.ecosyst.domain.Clade;
import fr.syncrase.ecosyst.repository.APGIIIPlanteRepository;
import fr.syncrase.ecosyst.service.APGIIIPlanteService;
import fr.syncrase.ecosyst.service.criteria.APGIIIPlanteCriteria;
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
 * Integration tests for the {@link APGIIIPlanteResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class APGIIIPlanteResourceIT {

    private static final String DEFAULT_ORDRE = "AAAAAAAAAA";
    private static final String UPDATED_ORDRE = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/apgiii-plantes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private APGIIIPlanteRepository aPGIIIPlanteRepository;

    @Mock
    private APGIIIPlanteRepository aPGIIIPlanteRepositoryMock;

    @Mock
    private APGIIIPlanteService aPGIIIPlanteServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAPGIIIPlanteMockMvc;

    private APGIIIPlante aPGIIIPlante;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static APGIIIPlante createEntity(EntityManager em) {
        APGIIIPlante aPGIIIPlante = new APGIIIPlante()
            .ordre(DEFAULT_ORDRE)
            .famille(DEFAULT_FAMILLE)
            .sousFamille(DEFAULT_SOUS_FAMILLE)
            .tribu(DEFAULT_TRIBU)
            .sousTribu(DEFAULT_SOUS_TRIBU)
            .genre(DEFAULT_GENRE);
        return aPGIIIPlante;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static APGIIIPlante createUpdatedEntity(EntityManager em) {
        APGIIIPlante aPGIIIPlante = new APGIIIPlante()
            .ordre(UPDATED_ORDRE)
            .famille(UPDATED_FAMILLE)
            .sousFamille(UPDATED_SOUS_FAMILLE)
            .tribu(UPDATED_TRIBU)
            .sousTribu(UPDATED_SOUS_TRIBU)
            .genre(UPDATED_GENRE);
        return aPGIIIPlante;
    }

    @BeforeEach
    public void initTest() {
        aPGIIIPlante = createEntity(em);
    }

    @Test
    @Transactional
    void createAPGIIIPlante() throws Exception {
        int databaseSizeBeforeCreate = aPGIIIPlanteRepository.findAll().size();
        // Create the APGIIIPlante
        restAPGIIIPlanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aPGIIIPlante)))
            .andExpect(status().isCreated());

        // Validate the APGIIIPlante in the database
        List<APGIIIPlante> aPGIIIPlanteList = aPGIIIPlanteRepository.findAll();
        assertThat(aPGIIIPlanteList).hasSize(databaseSizeBeforeCreate + 1);
        APGIIIPlante testAPGIIIPlante = aPGIIIPlanteList.get(aPGIIIPlanteList.size() - 1);
        assertThat(testAPGIIIPlante.getOrdre()).isEqualTo(DEFAULT_ORDRE);
        assertThat(testAPGIIIPlante.getFamille()).isEqualTo(DEFAULT_FAMILLE);
        assertThat(testAPGIIIPlante.getSousFamille()).isEqualTo(DEFAULT_SOUS_FAMILLE);
        assertThat(testAPGIIIPlante.getTribu()).isEqualTo(DEFAULT_TRIBU);
        assertThat(testAPGIIIPlante.getSousTribu()).isEqualTo(DEFAULT_SOUS_TRIBU);
        assertThat(testAPGIIIPlante.getGenre()).isEqualTo(DEFAULT_GENRE);
    }

    @Test
    @Transactional
    void createAPGIIIPlanteWithExistingId() throws Exception {
        // Create the APGIIIPlante with an existing ID
        aPGIIIPlante.setId(1L);

        int databaseSizeBeforeCreate = aPGIIIPlanteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAPGIIIPlanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aPGIIIPlante)))
            .andExpect(status().isBadRequest());

        // Validate the APGIIIPlante in the database
        List<APGIIIPlante> aPGIIIPlanteList = aPGIIIPlanteRepository.findAll();
        assertThat(aPGIIIPlanteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrdreIsRequired() throws Exception {
        int databaseSizeBeforeTest = aPGIIIPlanteRepository.findAll().size();
        // set the field null
        aPGIIIPlante.setOrdre(null);

        // Create the APGIIIPlante, which fails.

        restAPGIIIPlanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aPGIIIPlante)))
            .andExpect(status().isBadRequest());

        List<APGIIIPlante> aPGIIIPlanteList = aPGIIIPlanteRepository.findAll();
        assertThat(aPGIIIPlanteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFamilleIsRequired() throws Exception {
        int databaseSizeBeforeTest = aPGIIIPlanteRepository.findAll().size();
        // set the field null
        aPGIIIPlante.setFamille(null);

        // Create the APGIIIPlante, which fails.

        restAPGIIIPlanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aPGIIIPlante)))
            .andExpect(status().isBadRequest());

        List<APGIIIPlante> aPGIIIPlanteList = aPGIIIPlanteRepository.findAll();
        assertThat(aPGIIIPlanteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSousFamilleIsRequired() throws Exception {
        int databaseSizeBeforeTest = aPGIIIPlanteRepository.findAll().size();
        // set the field null
        aPGIIIPlante.setSousFamille(null);

        // Create the APGIIIPlante, which fails.

        restAPGIIIPlanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aPGIIIPlante)))
            .andExpect(status().isBadRequest());

        List<APGIIIPlante> aPGIIIPlanteList = aPGIIIPlanteRepository.findAll();
        assertThat(aPGIIIPlanteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGenreIsRequired() throws Exception {
        int databaseSizeBeforeTest = aPGIIIPlanteRepository.findAll().size();
        // set the field null
        aPGIIIPlante.setGenre(null);

        // Create the APGIIIPlante, which fails.

        restAPGIIIPlanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aPGIIIPlante)))
            .andExpect(status().isBadRequest());

        List<APGIIIPlante> aPGIIIPlanteList = aPGIIIPlanteRepository.findAll();
        assertThat(aPGIIIPlanteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAPGIIIPlantes() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);

        // Get all the aPGIIIPlanteList
        restAPGIIIPlanteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aPGIIIPlante.getId().intValue())))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)))
            .andExpect(jsonPath("$.[*].famille").value(hasItem(DEFAULT_FAMILLE)))
            .andExpect(jsonPath("$.[*].sousFamille").value(hasItem(DEFAULT_SOUS_FAMILLE)))
            .andExpect(jsonPath("$.[*].tribu").value(hasItem(DEFAULT_TRIBU)))
            .andExpect(jsonPath("$.[*].sousTribu").value(hasItem(DEFAULT_SOUS_TRIBU)))
            .andExpect(jsonPath("$.[*].genre").value(hasItem(DEFAULT_GENRE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAPGIIIPlantesWithEagerRelationshipsIsEnabled() throws Exception {
        when(aPGIIIPlanteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAPGIIIPlanteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(aPGIIIPlanteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAPGIIIPlantesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(aPGIIIPlanteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAPGIIIPlanteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(aPGIIIPlanteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getAPGIIIPlante() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);

        // Get the aPGIIIPlante
        restAPGIIIPlanteMockMvc
            .perform(get(ENTITY_API_URL_ID, aPGIIIPlante.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aPGIIIPlante.getId().intValue()))
            .andExpect(jsonPath("$.ordre").value(DEFAULT_ORDRE))
            .andExpect(jsonPath("$.famille").value(DEFAULT_FAMILLE))
            .andExpect(jsonPath("$.sousFamille").value(DEFAULT_SOUS_FAMILLE))
            .andExpect(jsonPath("$.tribu").value(DEFAULT_TRIBU))
            .andExpect(jsonPath("$.sousTribu").value(DEFAULT_SOUS_TRIBU))
            .andExpect(jsonPath("$.genre").value(DEFAULT_GENRE));
    }

    @Test
    @Transactional
    void getAPGIIIPlantesByIdFiltering() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);

        Long id = aPGIIIPlante.getId();

        defaultAPGIIIPlanteShouldBeFound("id.equals=" + id);
        defaultAPGIIIPlanteShouldNotBeFound("id.notEquals=" + id);

        defaultAPGIIIPlanteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAPGIIIPlanteShouldNotBeFound("id.greaterThan=" + id);

        defaultAPGIIIPlanteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAPGIIIPlanteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAPGIIIPlantesByOrdreIsEqualToSomething() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);

        // Get all the aPGIIIPlanteList where ordre equals to DEFAULT_ORDRE
        defaultAPGIIIPlanteShouldBeFound("ordre.equals=" + DEFAULT_ORDRE);

        // Get all the aPGIIIPlanteList where ordre equals to UPDATED_ORDRE
        defaultAPGIIIPlanteShouldNotBeFound("ordre.equals=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllAPGIIIPlantesByOrdreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);

        // Get all the aPGIIIPlanteList where ordre not equals to DEFAULT_ORDRE
        defaultAPGIIIPlanteShouldNotBeFound("ordre.notEquals=" + DEFAULT_ORDRE);

        // Get all the aPGIIIPlanteList where ordre not equals to UPDATED_ORDRE
        defaultAPGIIIPlanteShouldBeFound("ordre.notEquals=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllAPGIIIPlantesByOrdreIsInShouldWork() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);

        // Get all the aPGIIIPlanteList where ordre in DEFAULT_ORDRE or UPDATED_ORDRE
        defaultAPGIIIPlanteShouldBeFound("ordre.in=" + DEFAULT_ORDRE + "," + UPDATED_ORDRE);

        // Get all the aPGIIIPlanteList where ordre equals to UPDATED_ORDRE
        defaultAPGIIIPlanteShouldNotBeFound("ordre.in=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllAPGIIIPlantesByOrdreIsNullOrNotNull() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);

        // Get all the aPGIIIPlanteList where ordre is not null
        defaultAPGIIIPlanteShouldBeFound("ordre.specified=true");

        // Get all the aPGIIIPlanteList where ordre is null
        defaultAPGIIIPlanteShouldNotBeFound("ordre.specified=false");
    }

    @Test
    @Transactional
    void getAllAPGIIIPlantesByOrdreContainsSomething() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);

        // Get all the aPGIIIPlanteList where ordre contains DEFAULT_ORDRE
        defaultAPGIIIPlanteShouldBeFound("ordre.contains=" + DEFAULT_ORDRE);

        // Get all the aPGIIIPlanteList where ordre contains UPDATED_ORDRE
        defaultAPGIIIPlanteShouldNotBeFound("ordre.contains=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllAPGIIIPlantesByOrdreNotContainsSomething() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);

        // Get all the aPGIIIPlanteList where ordre does not contain DEFAULT_ORDRE
        defaultAPGIIIPlanteShouldNotBeFound("ordre.doesNotContain=" + DEFAULT_ORDRE);

        // Get all the aPGIIIPlanteList where ordre does not contain UPDATED_ORDRE
        defaultAPGIIIPlanteShouldBeFound("ordre.doesNotContain=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllAPGIIIPlantesByFamilleIsEqualToSomething() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);

        // Get all the aPGIIIPlanteList where famille equals to DEFAULT_FAMILLE
        defaultAPGIIIPlanteShouldBeFound("famille.equals=" + DEFAULT_FAMILLE);

        // Get all the aPGIIIPlanteList where famille equals to UPDATED_FAMILLE
        defaultAPGIIIPlanteShouldNotBeFound("famille.equals=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void getAllAPGIIIPlantesByFamilleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);

        // Get all the aPGIIIPlanteList where famille not equals to DEFAULT_FAMILLE
        defaultAPGIIIPlanteShouldNotBeFound("famille.notEquals=" + DEFAULT_FAMILLE);

        // Get all the aPGIIIPlanteList where famille not equals to UPDATED_FAMILLE
        defaultAPGIIIPlanteShouldBeFound("famille.notEquals=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void getAllAPGIIIPlantesByFamilleIsInShouldWork() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);

        // Get all the aPGIIIPlanteList where famille in DEFAULT_FAMILLE or UPDATED_FAMILLE
        defaultAPGIIIPlanteShouldBeFound("famille.in=" + DEFAULT_FAMILLE + "," + UPDATED_FAMILLE);

        // Get all the aPGIIIPlanteList where famille equals to UPDATED_FAMILLE
        defaultAPGIIIPlanteShouldNotBeFound("famille.in=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void getAllAPGIIIPlantesByFamilleIsNullOrNotNull() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);

        // Get all the aPGIIIPlanteList where famille is not null
        defaultAPGIIIPlanteShouldBeFound("famille.specified=true");

        // Get all the aPGIIIPlanteList where famille is null
        defaultAPGIIIPlanteShouldNotBeFound("famille.specified=false");
    }

    @Test
    @Transactional
    void getAllAPGIIIPlantesByFamilleContainsSomething() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);

        // Get all the aPGIIIPlanteList where famille contains DEFAULT_FAMILLE
        defaultAPGIIIPlanteShouldBeFound("famille.contains=" + DEFAULT_FAMILLE);

        // Get all the aPGIIIPlanteList where famille contains UPDATED_FAMILLE
        defaultAPGIIIPlanteShouldNotBeFound("famille.contains=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void getAllAPGIIIPlantesByFamilleNotContainsSomething() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);

        // Get all the aPGIIIPlanteList where famille does not contain DEFAULT_FAMILLE
        defaultAPGIIIPlanteShouldNotBeFound("famille.doesNotContain=" + DEFAULT_FAMILLE);

        // Get all the aPGIIIPlanteList where famille does not contain UPDATED_FAMILLE
        defaultAPGIIIPlanteShouldBeFound("famille.doesNotContain=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void getAllAPGIIIPlantesBySousFamilleIsEqualToSomething() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);

        // Get all the aPGIIIPlanteList where sousFamille equals to DEFAULT_SOUS_FAMILLE
        defaultAPGIIIPlanteShouldBeFound("sousFamille.equals=" + DEFAULT_SOUS_FAMILLE);

        // Get all the aPGIIIPlanteList where sousFamille equals to UPDATED_SOUS_FAMILLE
        defaultAPGIIIPlanteShouldNotBeFound("sousFamille.equals=" + UPDATED_SOUS_FAMILLE);
    }

    @Test
    @Transactional
    void getAllAPGIIIPlantesBySousFamilleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);

        // Get all the aPGIIIPlanteList where sousFamille not equals to DEFAULT_SOUS_FAMILLE
        defaultAPGIIIPlanteShouldNotBeFound("sousFamille.notEquals=" + DEFAULT_SOUS_FAMILLE);

        // Get all the aPGIIIPlanteList where sousFamille not equals to UPDATED_SOUS_FAMILLE
        defaultAPGIIIPlanteShouldBeFound("sousFamille.notEquals=" + UPDATED_SOUS_FAMILLE);
    }

    @Test
    @Transactional
    void getAllAPGIIIPlantesBySousFamilleIsInShouldWork() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);

        // Get all the aPGIIIPlanteList where sousFamille in DEFAULT_SOUS_FAMILLE or UPDATED_SOUS_FAMILLE
        defaultAPGIIIPlanteShouldBeFound("sousFamille.in=" + DEFAULT_SOUS_FAMILLE + "," + UPDATED_SOUS_FAMILLE);

        // Get all the aPGIIIPlanteList where sousFamille equals to UPDATED_SOUS_FAMILLE
        defaultAPGIIIPlanteShouldNotBeFound("sousFamille.in=" + UPDATED_SOUS_FAMILLE);
    }

    @Test
    @Transactional
    void getAllAPGIIIPlantesBySousFamilleIsNullOrNotNull() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);

        // Get all the aPGIIIPlanteList where sousFamille is not null
        defaultAPGIIIPlanteShouldBeFound("sousFamille.specified=true");

        // Get all the aPGIIIPlanteList where sousFamille is null
        defaultAPGIIIPlanteShouldNotBeFound("sousFamille.specified=false");
    }

    @Test
    @Transactional
    void getAllAPGIIIPlantesBySousFamilleContainsSomething() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);

        // Get all the aPGIIIPlanteList where sousFamille contains DEFAULT_SOUS_FAMILLE
        defaultAPGIIIPlanteShouldBeFound("sousFamille.contains=" + DEFAULT_SOUS_FAMILLE);

        // Get all the aPGIIIPlanteList where sousFamille contains UPDATED_SOUS_FAMILLE
        defaultAPGIIIPlanteShouldNotBeFound("sousFamille.contains=" + UPDATED_SOUS_FAMILLE);
    }

    @Test
    @Transactional
    void getAllAPGIIIPlantesBySousFamilleNotContainsSomething() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);

        // Get all the aPGIIIPlanteList where sousFamille does not contain DEFAULT_SOUS_FAMILLE
        defaultAPGIIIPlanteShouldNotBeFound("sousFamille.doesNotContain=" + DEFAULT_SOUS_FAMILLE);

        // Get all the aPGIIIPlanteList where sousFamille does not contain UPDATED_SOUS_FAMILLE
        defaultAPGIIIPlanteShouldBeFound("sousFamille.doesNotContain=" + UPDATED_SOUS_FAMILLE);
    }

    @Test
    @Transactional
    void getAllAPGIIIPlantesByTribuIsEqualToSomething() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);

        // Get all the aPGIIIPlanteList where tribu equals to DEFAULT_TRIBU
        defaultAPGIIIPlanteShouldBeFound("tribu.equals=" + DEFAULT_TRIBU);

        // Get all the aPGIIIPlanteList where tribu equals to UPDATED_TRIBU
        defaultAPGIIIPlanteShouldNotBeFound("tribu.equals=" + UPDATED_TRIBU);
    }

    @Test
    @Transactional
    void getAllAPGIIIPlantesByTribuIsNotEqualToSomething() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);

        // Get all the aPGIIIPlanteList where tribu not equals to DEFAULT_TRIBU
        defaultAPGIIIPlanteShouldNotBeFound("tribu.notEquals=" + DEFAULT_TRIBU);

        // Get all the aPGIIIPlanteList where tribu not equals to UPDATED_TRIBU
        defaultAPGIIIPlanteShouldBeFound("tribu.notEquals=" + UPDATED_TRIBU);
    }

    @Test
    @Transactional
    void getAllAPGIIIPlantesByTribuIsInShouldWork() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);

        // Get all the aPGIIIPlanteList where tribu in DEFAULT_TRIBU or UPDATED_TRIBU
        defaultAPGIIIPlanteShouldBeFound("tribu.in=" + DEFAULT_TRIBU + "," + UPDATED_TRIBU);

        // Get all the aPGIIIPlanteList where tribu equals to UPDATED_TRIBU
        defaultAPGIIIPlanteShouldNotBeFound("tribu.in=" + UPDATED_TRIBU);
    }

    @Test
    @Transactional
    void getAllAPGIIIPlantesByTribuIsNullOrNotNull() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);

        // Get all the aPGIIIPlanteList where tribu is not null
        defaultAPGIIIPlanteShouldBeFound("tribu.specified=true");

        // Get all the aPGIIIPlanteList where tribu is null
        defaultAPGIIIPlanteShouldNotBeFound("tribu.specified=false");
    }

    @Test
    @Transactional
    void getAllAPGIIIPlantesByTribuContainsSomething() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);

        // Get all the aPGIIIPlanteList where tribu contains DEFAULT_TRIBU
        defaultAPGIIIPlanteShouldBeFound("tribu.contains=" + DEFAULT_TRIBU);

        // Get all the aPGIIIPlanteList where tribu contains UPDATED_TRIBU
        defaultAPGIIIPlanteShouldNotBeFound("tribu.contains=" + UPDATED_TRIBU);
    }

    @Test
    @Transactional
    void getAllAPGIIIPlantesByTribuNotContainsSomething() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);

        // Get all the aPGIIIPlanteList where tribu does not contain DEFAULT_TRIBU
        defaultAPGIIIPlanteShouldNotBeFound("tribu.doesNotContain=" + DEFAULT_TRIBU);

        // Get all the aPGIIIPlanteList where tribu does not contain UPDATED_TRIBU
        defaultAPGIIIPlanteShouldBeFound("tribu.doesNotContain=" + UPDATED_TRIBU);
    }

    @Test
    @Transactional
    void getAllAPGIIIPlantesBySousTribuIsEqualToSomething() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);

        // Get all the aPGIIIPlanteList where sousTribu equals to DEFAULT_SOUS_TRIBU
        defaultAPGIIIPlanteShouldBeFound("sousTribu.equals=" + DEFAULT_SOUS_TRIBU);

        // Get all the aPGIIIPlanteList where sousTribu equals to UPDATED_SOUS_TRIBU
        defaultAPGIIIPlanteShouldNotBeFound("sousTribu.equals=" + UPDATED_SOUS_TRIBU);
    }

    @Test
    @Transactional
    void getAllAPGIIIPlantesBySousTribuIsNotEqualToSomething() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);

        // Get all the aPGIIIPlanteList where sousTribu not equals to DEFAULT_SOUS_TRIBU
        defaultAPGIIIPlanteShouldNotBeFound("sousTribu.notEquals=" + DEFAULT_SOUS_TRIBU);

        // Get all the aPGIIIPlanteList where sousTribu not equals to UPDATED_SOUS_TRIBU
        defaultAPGIIIPlanteShouldBeFound("sousTribu.notEquals=" + UPDATED_SOUS_TRIBU);
    }

    @Test
    @Transactional
    void getAllAPGIIIPlantesBySousTribuIsInShouldWork() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);

        // Get all the aPGIIIPlanteList where sousTribu in DEFAULT_SOUS_TRIBU or UPDATED_SOUS_TRIBU
        defaultAPGIIIPlanteShouldBeFound("sousTribu.in=" + DEFAULT_SOUS_TRIBU + "," + UPDATED_SOUS_TRIBU);

        // Get all the aPGIIIPlanteList where sousTribu equals to UPDATED_SOUS_TRIBU
        defaultAPGIIIPlanteShouldNotBeFound("sousTribu.in=" + UPDATED_SOUS_TRIBU);
    }

    @Test
    @Transactional
    void getAllAPGIIIPlantesBySousTribuIsNullOrNotNull() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);

        // Get all the aPGIIIPlanteList where sousTribu is not null
        defaultAPGIIIPlanteShouldBeFound("sousTribu.specified=true");

        // Get all the aPGIIIPlanteList where sousTribu is null
        defaultAPGIIIPlanteShouldNotBeFound("sousTribu.specified=false");
    }

    @Test
    @Transactional
    void getAllAPGIIIPlantesBySousTribuContainsSomething() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);

        // Get all the aPGIIIPlanteList where sousTribu contains DEFAULT_SOUS_TRIBU
        defaultAPGIIIPlanteShouldBeFound("sousTribu.contains=" + DEFAULT_SOUS_TRIBU);

        // Get all the aPGIIIPlanteList where sousTribu contains UPDATED_SOUS_TRIBU
        defaultAPGIIIPlanteShouldNotBeFound("sousTribu.contains=" + UPDATED_SOUS_TRIBU);
    }

    @Test
    @Transactional
    void getAllAPGIIIPlantesBySousTribuNotContainsSomething() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);

        // Get all the aPGIIIPlanteList where sousTribu does not contain DEFAULT_SOUS_TRIBU
        defaultAPGIIIPlanteShouldNotBeFound("sousTribu.doesNotContain=" + DEFAULT_SOUS_TRIBU);

        // Get all the aPGIIIPlanteList where sousTribu does not contain UPDATED_SOUS_TRIBU
        defaultAPGIIIPlanteShouldBeFound("sousTribu.doesNotContain=" + UPDATED_SOUS_TRIBU);
    }

    @Test
    @Transactional
    void getAllAPGIIIPlantesByGenreIsEqualToSomething() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);

        // Get all the aPGIIIPlanteList where genre equals to DEFAULT_GENRE
        defaultAPGIIIPlanteShouldBeFound("genre.equals=" + DEFAULT_GENRE);

        // Get all the aPGIIIPlanteList where genre equals to UPDATED_GENRE
        defaultAPGIIIPlanteShouldNotBeFound("genre.equals=" + UPDATED_GENRE);
    }

    @Test
    @Transactional
    void getAllAPGIIIPlantesByGenreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);

        // Get all the aPGIIIPlanteList where genre not equals to DEFAULT_GENRE
        defaultAPGIIIPlanteShouldNotBeFound("genre.notEquals=" + DEFAULT_GENRE);

        // Get all the aPGIIIPlanteList where genre not equals to UPDATED_GENRE
        defaultAPGIIIPlanteShouldBeFound("genre.notEquals=" + UPDATED_GENRE);
    }

    @Test
    @Transactional
    void getAllAPGIIIPlantesByGenreIsInShouldWork() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);

        // Get all the aPGIIIPlanteList where genre in DEFAULT_GENRE or UPDATED_GENRE
        defaultAPGIIIPlanteShouldBeFound("genre.in=" + DEFAULT_GENRE + "," + UPDATED_GENRE);

        // Get all the aPGIIIPlanteList where genre equals to UPDATED_GENRE
        defaultAPGIIIPlanteShouldNotBeFound("genre.in=" + UPDATED_GENRE);
    }

    @Test
    @Transactional
    void getAllAPGIIIPlantesByGenreIsNullOrNotNull() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);

        // Get all the aPGIIIPlanteList where genre is not null
        defaultAPGIIIPlanteShouldBeFound("genre.specified=true");

        // Get all the aPGIIIPlanteList where genre is null
        defaultAPGIIIPlanteShouldNotBeFound("genre.specified=false");
    }

    @Test
    @Transactional
    void getAllAPGIIIPlantesByGenreContainsSomething() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);

        // Get all the aPGIIIPlanteList where genre contains DEFAULT_GENRE
        defaultAPGIIIPlanteShouldBeFound("genre.contains=" + DEFAULT_GENRE);

        // Get all the aPGIIIPlanteList where genre contains UPDATED_GENRE
        defaultAPGIIIPlanteShouldNotBeFound("genre.contains=" + UPDATED_GENRE);
    }

    @Test
    @Transactional
    void getAllAPGIIIPlantesByGenreNotContainsSomething() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);

        // Get all the aPGIIIPlanteList where genre does not contain DEFAULT_GENRE
        defaultAPGIIIPlanteShouldNotBeFound("genre.doesNotContain=" + DEFAULT_GENRE);

        // Get all the aPGIIIPlanteList where genre does not contain UPDATED_GENRE
        defaultAPGIIIPlanteShouldBeFound("genre.doesNotContain=" + UPDATED_GENRE);
    }

    @Test
    @Transactional
    void getAllAPGIIIPlantesByCladesIsEqualToSomething() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);
        Clade clades;
        if (TestUtil.findAll(em, Clade.class).isEmpty()) {
            clades = CladeResourceIT.createEntity(em);
            em.persist(clades);
            em.flush();
        } else {
            clades = TestUtil.findAll(em, Clade.class).get(0);
        }
        em.persist(clades);
        em.flush();
        aPGIIIPlante.addClades(clades);
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);
        Long cladesId = clades.getId();

        // Get all the aPGIIIPlanteList where clades equals to cladesId
        defaultAPGIIIPlanteShouldBeFound("cladesId.equals=" + cladesId);

        // Get all the aPGIIIPlanteList where clades equals to (cladesId + 1)
        defaultAPGIIIPlanteShouldNotBeFound("cladesId.equals=" + (cladesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAPGIIIPlanteShouldBeFound(String filter) throws Exception {
        restAPGIIIPlanteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aPGIIIPlante.getId().intValue())))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)))
            .andExpect(jsonPath("$.[*].famille").value(hasItem(DEFAULT_FAMILLE)))
            .andExpect(jsonPath("$.[*].sousFamille").value(hasItem(DEFAULT_SOUS_FAMILLE)))
            .andExpect(jsonPath("$.[*].tribu").value(hasItem(DEFAULT_TRIBU)))
            .andExpect(jsonPath("$.[*].sousTribu").value(hasItem(DEFAULT_SOUS_TRIBU)))
            .andExpect(jsonPath("$.[*].genre").value(hasItem(DEFAULT_GENRE)));

        // Check, that the count call also returns 1
        restAPGIIIPlanteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAPGIIIPlanteShouldNotBeFound(String filter) throws Exception {
        restAPGIIIPlanteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAPGIIIPlanteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAPGIIIPlante() throws Exception {
        // Get the aPGIIIPlante
        restAPGIIIPlanteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAPGIIIPlante() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);

        int databaseSizeBeforeUpdate = aPGIIIPlanteRepository.findAll().size();

        // Update the aPGIIIPlante
        APGIIIPlante updatedAPGIIIPlante = aPGIIIPlanteRepository.findById(aPGIIIPlante.getId()).get();
        // Disconnect from session so that the updates on updatedAPGIIIPlante are not directly saved in db
        em.detach(updatedAPGIIIPlante);
        updatedAPGIIIPlante
            .ordre(UPDATED_ORDRE)
            .famille(UPDATED_FAMILLE)
            .sousFamille(UPDATED_SOUS_FAMILLE)
            .tribu(UPDATED_TRIBU)
            .sousTribu(UPDATED_SOUS_TRIBU)
            .genre(UPDATED_GENRE);

        restAPGIIIPlanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAPGIIIPlante.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAPGIIIPlante))
            )
            .andExpect(status().isOk());

        // Validate the APGIIIPlante in the database
        List<APGIIIPlante> aPGIIIPlanteList = aPGIIIPlanteRepository.findAll();
        assertThat(aPGIIIPlanteList).hasSize(databaseSizeBeforeUpdate);
        APGIIIPlante testAPGIIIPlante = aPGIIIPlanteList.get(aPGIIIPlanteList.size() - 1);
        assertThat(testAPGIIIPlante.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testAPGIIIPlante.getFamille()).isEqualTo(UPDATED_FAMILLE);
        assertThat(testAPGIIIPlante.getSousFamille()).isEqualTo(UPDATED_SOUS_FAMILLE);
        assertThat(testAPGIIIPlante.getTribu()).isEqualTo(UPDATED_TRIBU);
        assertThat(testAPGIIIPlante.getSousTribu()).isEqualTo(UPDATED_SOUS_TRIBU);
        assertThat(testAPGIIIPlante.getGenre()).isEqualTo(UPDATED_GENRE);
    }

    @Test
    @Transactional
    void putNonExistingAPGIIIPlante() throws Exception {
        int databaseSizeBeforeUpdate = aPGIIIPlanteRepository.findAll().size();
        aPGIIIPlante.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAPGIIIPlanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aPGIIIPlante.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aPGIIIPlante))
            )
            .andExpect(status().isBadRequest());

        // Validate the APGIIIPlante in the database
        List<APGIIIPlante> aPGIIIPlanteList = aPGIIIPlanteRepository.findAll();
        assertThat(aPGIIIPlanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAPGIIIPlante() throws Exception {
        int databaseSizeBeforeUpdate = aPGIIIPlanteRepository.findAll().size();
        aPGIIIPlante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAPGIIIPlanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aPGIIIPlante))
            )
            .andExpect(status().isBadRequest());

        // Validate the APGIIIPlante in the database
        List<APGIIIPlante> aPGIIIPlanteList = aPGIIIPlanteRepository.findAll();
        assertThat(aPGIIIPlanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAPGIIIPlante() throws Exception {
        int databaseSizeBeforeUpdate = aPGIIIPlanteRepository.findAll().size();
        aPGIIIPlante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAPGIIIPlanteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aPGIIIPlante)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the APGIIIPlante in the database
        List<APGIIIPlante> aPGIIIPlanteList = aPGIIIPlanteRepository.findAll();
        assertThat(aPGIIIPlanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAPGIIIPlanteWithPatch() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);

        int databaseSizeBeforeUpdate = aPGIIIPlanteRepository.findAll().size();

        // Update the aPGIIIPlante using partial update
        APGIIIPlante partialUpdatedAPGIIIPlante = new APGIIIPlante();
        partialUpdatedAPGIIIPlante.setId(aPGIIIPlante.getId());

        partialUpdatedAPGIIIPlante.famille(UPDATED_FAMILLE).tribu(UPDATED_TRIBU).sousTribu(UPDATED_SOUS_TRIBU).genre(UPDATED_GENRE);

        restAPGIIIPlanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAPGIIIPlante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAPGIIIPlante))
            )
            .andExpect(status().isOk());

        // Validate the APGIIIPlante in the database
        List<APGIIIPlante> aPGIIIPlanteList = aPGIIIPlanteRepository.findAll();
        assertThat(aPGIIIPlanteList).hasSize(databaseSizeBeforeUpdate);
        APGIIIPlante testAPGIIIPlante = aPGIIIPlanteList.get(aPGIIIPlanteList.size() - 1);
        assertThat(testAPGIIIPlante.getOrdre()).isEqualTo(DEFAULT_ORDRE);
        assertThat(testAPGIIIPlante.getFamille()).isEqualTo(UPDATED_FAMILLE);
        assertThat(testAPGIIIPlante.getSousFamille()).isEqualTo(DEFAULT_SOUS_FAMILLE);
        assertThat(testAPGIIIPlante.getTribu()).isEqualTo(UPDATED_TRIBU);
        assertThat(testAPGIIIPlante.getSousTribu()).isEqualTo(UPDATED_SOUS_TRIBU);
        assertThat(testAPGIIIPlante.getGenre()).isEqualTo(UPDATED_GENRE);
    }

    @Test
    @Transactional
    void fullUpdateAPGIIIPlanteWithPatch() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);

        int databaseSizeBeforeUpdate = aPGIIIPlanteRepository.findAll().size();

        // Update the aPGIIIPlante using partial update
        APGIIIPlante partialUpdatedAPGIIIPlante = new APGIIIPlante();
        partialUpdatedAPGIIIPlante.setId(aPGIIIPlante.getId());

        partialUpdatedAPGIIIPlante
            .ordre(UPDATED_ORDRE)
            .famille(UPDATED_FAMILLE)
            .sousFamille(UPDATED_SOUS_FAMILLE)
            .tribu(UPDATED_TRIBU)
            .sousTribu(UPDATED_SOUS_TRIBU)
            .genre(UPDATED_GENRE);

        restAPGIIIPlanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAPGIIIPlante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAPGIIIPlante))
            )
            .andExpect(status().isOk());

        // Validate the APGIIIPlante in the database
        List<APGIIIPlante> aPGIIIPlanteList = aPGIIIPlanteRepository.findAll();
        assertThat(aPGIIIPlanteList).hasSize(databaseSizeBeforeUpdate);
        APGIIIPlante testAPGIIIPlante = aPGIIIPlanteList.get(aPGIIIPlanteList.size() - 1);
        assertThat(testAPGIIIPlante.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testAPGIIIPlante.getFamille()).isEqualTo(UPDATED_FAMILLE);
        assertThat(testAPGIIIPlante.getSousFamille()).isEqualTo(UPDATED_SOUS_FAMILLE);
        assertThat(testAPGIIIPlante.getTribu()).isEqualTo(UPDATED_TRIBU);
        assertThat(testAPGIIIPlante.getSousTribu()).isEqualTo(UPDATED_SOUS_TRIBU);
        assertThat(testAPGIIIPlante.getGenre()).isEqualTo(UPDATED_GENRE);
    }

    @Test
    @Transactional
    void patchNonExistingAPGIIIPlante() throws Exception {
        int databaseSizeBeforeUpdate = aPGIIIPlanteRepository.findAll().size();
        aPGIIIPlante.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAPGIIIPlanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aPGIIIPlante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aPGIIIPlante))
            )
            .andExpect(status().isBadRequest());

        // Validate the APGIIIPlante in the database
        List<APGIIIPlante> aPGIIIPlanteList = aPGIIIPlanteRepository.findAll();
        assertThat(aPGIIIPlanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAPGIIIPlante() throws Exception {
        int databaseSizeBeforeUpdate = aPGIIIPlanteRepository.findAll().size();
        aPGIIIPlante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAPGIIIPlanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aPGIIIPlante))
            )
            .andExpect(status().isBadRequest());

        // Validate the APGIIIPlante in the database
        List<APGIIIPlante> aPGIIIPlanteList = aPGIIIPlanteRepository.findAll();
        assertThat(aPGIIIPlanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAPGIIIPlante() throws Exception {
        int databaseSizeBeforeUpdate = aPGIIIPlanteRepository.findAll().size();
        aPGIIIPlante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAPGIIIPlanteMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(aPGIIIPlante))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the APGIIIPlante in the database
        List<APGIIIPlante> aPGIIIPlanteList = aPGIIIPlanteRepository.findAll();
        assertThat(aPGIIIPlanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAPGIIIPlante() throws Exception {
        // Initialize the database
        aPGIIIPlanteRepository.saveAndFlush(aPGIIIPlante);

        int databaseSizeBeforeDelete = aPGIIIPlanteRepository.findAll().size();

        // Delete the aPGIIIPlante
        restAPGIIIPlanteMockMvc
            .perform(delete(ENTITY_API_URL_ID, aPGIIIPlante.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<APGIIIPlante> aPGIIIPlanteList = aPGIIIPlanteRepository.findAll();
        assertThat(aPGIIIPlanteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
