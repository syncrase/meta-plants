package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.CronquistRank;
import fr.syncrase.ecosyst.domain.CronquistRank;
import fr.syncrase.ecosyst.domain.Url;
import fr.syncrase.ecosyst.domain.enumeration.CronquistTaxonomikRanks;
import fr.syncrase.ecosyst.repository.CronquistRankRepository;
import fr.syncrase.ecosyst.service.criteria.CronquistRankCriteria;
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
 * Integration tests for the {@link CronquistRankResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CronquistRankResourceIT {

    private static final CronquistTaxonomikRanks DEFAULT_RANK = CronquistTaxonomikRanks.SUPERREGNE;
    private static final CronquistTaxonomikRanks UPDATED_RANK = CronquistTaxonomikRanks.REGNE;

    private static final String DEFAULT_NOM_FR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_LANTIN = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LANTIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cronquist-ranks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CronquistRankRepository cronquistRankRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCronquistRankMockMvc;

    private CronquistRank cronquistRank;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CronquistRank createEntity(EntityManager em) {
        CronquistRank cronquistRank = new CronquistRank().rank(DEFAULT_RANK).nomFr(DEFAULT_NOM_FR).nomLantin(DEFAULT_NOM_LANTIN);
        return cronquistRank;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CronquistRank createUpdatedEntity(EntityManager em) {
        CronquistRank cronquistRank = new CronquistRank().rank(UPDATED_RANK).nomFr(UPDATED_NOM_FR).nomLantin(UPDATED_NOM_LANTIN);
        return cronquistRank;
    }

    @BeforeEach
    public void initTest() {
        cronquistRank = createEntity(em);
    }

    @Test
    @Transactional
    void createCronquistRank() throws Exception {
        int databaseSizeBeforeCreate = cronquistRankRepository.findAll().size();
        // Create the CronquistRank
        restCronquistRankMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cronquistRank)))
            .andExpect(status().isCreated());

        // Validate the CronquistRank in the database
        List<CronquistRank> cronquistRankList = cronquistRankRepository.findAll();
        assertThat(cronquistRankList).hasSize(databaseSizeBeforeCreate + 1);
        CronquistRank testCronquistRank = cronquistRankList.get(cronquistRankList.size() - 1);
        assertThat(testCronquistRank.getRank()).isEqualTo(DEFAULT_RANK);
        assertThat(testCronquistRank.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testCronquistRank.getNomLantin()).isEqualTo(DEFAULT_NOM_LANTIN);
    }

    @Test
    @Transactional
    void createCronquistRankWithExistingId() throws Exception {
        // Create the CronquistRank with an existing ID
        cronquistRank.setId(1L);

        int databaseSizeBeforeCreate = cronquistRankRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCronquistRankMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cronquistRank)))
            .andExpect(status().isBadRequest());

        // Validate the CronquistRank in the database
        List<CronquistRank> cronquistRankList = cronquistRankRepository.findAll();
        assertThat(cronquistRankList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRankIsRequired() throws Exception {
        int databaseSizeBeforeTest = cronquistRankRepository.findAll().size();
        // set the field null
        cronquistRank.setRank(null);

        // Create the CronquistRank, which fails.

        restCronquistRankMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cronquistRank)))
            .andExpect(status().isBadRequest());

        List<CronquistRank> cronquistRankList = cronquistRankRepository.findAll();
        assertThat(cronquistRankList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCronquistRanks() throws Exception {
        // Initialize the database
        cronquistRankRepository.saveAndFlush(cronquistRank);

        // Get all the cronquistRankList
        restCronquistRankMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cronquistRank.getId().intValue())))
            .andExpect(jsonPath("$.[*].rank").value(hasItem(DEFAULT_RANK.toString())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLantin").value(hasItem(DEFAULT_NOM_LANTIN)));
    }

    @Test
    @Transactional
    void getCronquistRank() throws Exception {
        // Initialize the database
        cronquistRankRepository.saveAndFlush(cronquistRank);

        // Get the cronquistRank
        restCronquistRankMockMvc
            .perform(get(ENTITY_API_URL_ID, cronquistRank.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cronquistRank.getId().intValue()))
            .andExpect(jsonPath("$.rank").value(DEFAULT_RANK.toString()))
            .andExpect(jsonPath("$.nomFr").value(DEFAULT_NOM_FR))
            .andExpect(jsonPath("$.nomLantin").value(DEFAULT_NOM_LANTIN));
    }

    @Test
    @Transactional
    void getCronquistRanksByIdFiltering() throws Exception {
        // Initialize the database
        cronquistRankRepository.saveAndFlush(cronquistRank);

        Long id = cronquistRank.getId();

        defaultCronquistRankShouldBeFound("id.equals=" + id);
        defaultCronquistRankShouldNotBeFound("id.notEquals=" + id);

        defaultCronquistRankShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCronquistRankShouldNotBeFound("id.greaterThan=" + id);

        defaultCronquistRankShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCronquistRankShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCronquistRanksByRankIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistRankRepository.saveAndFlush(cronquistRank);

        // Get all the cronquistRankList where rank equals to DEFAULT_RANK
        defaultCronquistRankShouldBeFound("rank.equals=" + DEFAULT_RANK);

        // Get all the cronquistRankList where rank equals to UPDATED_RANK
        defaultCronquistRankShouldNotBeFound("rank.equals=" + UPDATED_RANK);
    }

    @Test
    @Transactional
    void getAllCronquistRanksByRankIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistRankRepository.saveAndFlush(cronquistRank);

        // Get all the cronquistRankList where rank not equals to DEFAULT_RANK
        defaultCronquistRankShouldNotBeFound("rank.notEquals=" + DEFAULT_RANK);

        // Get all the cronquistRankList where rank not equals to UPDATED_RANK
        defaultCronquistRankShouldBeFound("rank.notEquals=" + UPDATED_RANK);
    }

    @Test
    @Transactional
    void getAllCronquistRanksByRankIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistRankRepository.saveAndFlush(cronquistRank);

        // Get all the cronquistRankList where rank in DEFAULT_RANK or UPDATED_RANK
        defaultCronquistRankShouldBeFound("rank.in=" + DEFAULT_RANK + "," + UPDATED_RANK);

        // Get all the cronquistRankList where rank equals to UPDATED_RANK
        defaultCronquistRankShouldNotBeFound("rank.in=" + UPDATED_RANK);
    }

    @Test
    @Transactional
    void getAllCronquistRanksByRankIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistRankRepository.saveAndFlush(cronquistRank);

        // Get all the cronquistRankList where rank is not null
        defaultCronquistRankShouldBeFound("rank.specified=true");

        // Get all the cronquistRankList where rank is null
        defaultCronquistRankShouldNotBeFound("rank.specified=false");
    }

    @Test
    @Transactional
    void getAllCronquistRanksByNomFrIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistRankRepository.saveAndFlush(cronquistRank);

        // Get all the cronquistRankList where nomFr equals to DEFAULT_NOM_FR
        defaultCronquistRankShouldBeFound("nomFr.equals=" + DEFAULT_NOM_FR);

        // Get all the cronquistRankList where nomFr equals to UPDATED_NOM_FR
        defaultCronquistRankShouldNotBeFound("nomFr.equals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllCronquistRanksByNomFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistRankRepository.saveAndFlush(cronquistRank);

        // Get all the cronquistRankList where nomFr not equals to DEFAULT_NOM_FR
        defaultCronquistRankShouldNotBeFound("nomFr.notEquals=" + DEFAULT_NOM_FR);

        // Get all the cronquistRankList where nomFr not equals to UPDATED_NOM_FR
        defaultCronquistRankShouldBeFound("nomFr.notEquals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllCronquistRanksByNomFrIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistRankRepository.saveAndFlush(cronquistRank);

        // Get all the cronquistRankList where nomFr in DEFAULT_NOM_FR or UPDATED_NOM_FR
        defaultCronquistRankShouldBeFound("nomFr.in=" + DEFAULT_NOM_FR + "," + UPDATED_NOM_FR);

        // Get all the cronquistRankList where nomFr equals to UPDATED_NOM_FR
        defaultCronquistRankShouldNotBeFound("nomFr.in=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllCronquistRanksByNomFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistRankRepository.saveAndFlush(cronquistRank);

        // Get all the cronquistRankList where nomFr is not null
        defaultCronquistRankShouldBeFound("nomFr.specified=true");

        // Get all the cronquistRankList where nomFr is null
        defaultCronquistRankShouldNotBeFound("nomFr.specified=false");
    }

    @Test
    @Transactional
    void getAllCronquistRanksByNomFrContainsSomething() throws Exception {
        // Initialize the database
        cronquistRankRepository.saveAndFlush(cronquistRank);

        // Get all the cronquistRankList where nomFr contains DEFAULT_NOM_FR
        defaultCronquistRankShouldBeFound("nomFr.contains=" + DEFAULT_NOM_FR);

        // Get all the cronquistRankList where nomFr contains UPDATED_NOM_FR
        defaultCronquistRankShouldNotBeFound("nomFr.contains=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllCronquistRanksByNomFrNotContainsSomething() throws Exception {
        // Initialize the database
        cronquistRankRepository.saveAndFlush(cronquistRank);

        // Get all the cronquistRankList where nomFr does not contain DEFAULT_NOM_FR
        defaultCronquistRankShouldNotBeFound("nomFr.doesNotContain=" + DEFAULT_NOM_FR);

        // Get all the cronquistRankList where nomFr does not contain UPDATED_NOM_FR
        defaultCronquistRankShouldBeFound("nomFr.doesNotContain=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllCronquistRanksByNomLantinIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistRankRepository.saveAndFlush(cronquistRank);

        // Get all the cronquistRankList where nomLantin equals to DEFAULT_NOM_LANTIN
        defaultCronquistRankShouldBeFound("nomLantin.equals=" + DEFAULT_NOM_LANTIN);

        // Get all the cronquistRankList where nomLantin equals to UPDATED_NOM_LANTIN
        defaultCronquistRankShouldNotBeFound("nomLantin.equals=" + UPDATED_NOM_LANTIN);
    }

    @Test
    @Transactional
    void getAllCronquistRanksByNomLantinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cronquistRankRepository.saveAndFlush(cronquistRank);

        // Get all the cronquistRankList where nomLantin not equals to DEFAULT_NOM_LANTIN
        defaultCronquistRankShouldNotBeFound("nomLantin.notEquals=" + DEFAULT_NOM_LANTIN);

        // Get all the cronquistRankList where nomLantin not equals to UPDATED_NOM_LANTIN
        defaultCronquistRankShouldBeFound("nomLantin.notEquals=" + UPDATED_NOM_LANTIN);
    }

    @Test
    @Transactional
    void getAllCronquistRanksByNomLantinIsInShouldWork() throws Exception {
        // Initialize the database
        cronquistRankRepository.saveAndFlush(cronquistRank);

        // Get all the cronquistRankList where nomLantin in DEFAULT_NOM_LANTIN or UPDATED_NOM_LANTIN
        defaultCronquistRankShouldBeFound("nomLantin.in=" + DEFAULT_NOM_LANTIN + "," + UPDATED_NOM_LANTIN);

        // Get all the cronquistRankList where nomLantin equals to UPDATED_NOM_LANTIN
        defaultCronquistRankShouldNotBeFound("nomLantin.in=" + UPDATED_NOM_LANTIN);
    }

    @Test
    @Transactional
    void getAllCronquistRanksByNomLantinIsNullOrNotNull() throws Exception {
        // Initialize the database
        cronquistRankRepository.saveAndFlush(cronquistRank);

        // Get all the cronquistRankList where nomLantin is not null
        defaultCronquistRankShouldBeFound("nomLantin.specified=true");

        // Get all the cronquistRankList where nomLantin is null
        defaultCronquistRankShouldNotBeFound("nomLantin.specified=false");
    }

    @Test
    @Transactional
    void getAllCronquistRanksByNomLantinContainsSomething() throws Exception {
        // Initialize the database
        cronquistRankRepository.saveAndFlush(cronquistRank);

        // Get all the cronquistRankList where nomLantin contains DEFAULT_NOM_LANTIN
        defaultCronquistRankShouldBeFound("nomLantin.contains=" + DEFAULT_NOM_LANTIN);

        // Get all the cronquistRankList where nomLantin contains UPDATED_NOM_LANTIN
        defaultCronquistRankShouldNotBeFound("nomLantin.contains=" + UPDATED_NOM_LANTIN);
    }

    @Test
    @Transactional
    void getAllCronquistRanksByNomLantinNotContainsSomething() throws Exception {
        // Initialize the database
        cronquistRankRepository.saveAndFlush(cronquistRank);

        // Get all the cronquistRankList where nomLantin does not contain DEFAULT_NOM_LANTIN
        defaultCronquistRankShouldNotBeFound("nomLantin.doesNotContain=" + DEFAULT_NOM_LANTIN);

        // Get all the cronquistRankList where nomLantin does not contain UPDATED_NOM_LANTIN
        defaultCronquistRankShouldBeFound("nomLantin.doesNotContain=" + UPDATED_NOM_LANTIN);
    }

    @Test
    @Transactional
    void getAllCronquistRanksByChildrenIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistRankRepository.saveAndFlush(cronquistRank);
        CronquistRank children;
        if (TestUtil.findAll(em, CronquistRank.class).isEmpty()) {
            children = CronquistRankResourceIT.createEntity(em);
            em.persist(children);
            em.flush();
        } else {
            children = TestUtil.findAll(em, CronquistRank.class).get(0);
        }
        em.persist(children);
        em.flush();
        cronquistRank.addChildren(children);
        cronquistRankRepository.saveAndFlush(cronquistRank);
        Long childrenId = children.getId();

        // Get all the cronquistRankList where children equals to childrenId
        defaultCronquistRankShouldBeFound("childrenId.equals=" + childrenId);

        // Get all the cronquistRankList where children equals to (childrenId + 1)
        defaultCronquistRankShouldNotBeFound("childrenId.equals=" + (childrenId + 1));
    }

    @Test
    @Transactional
    void getAllCronquistRanksByUrlsIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistRankRepository.saveAndFlush(cronquistRank);
        Url urls;
        if (TestUtil.findAll(em, Url.class).isEmpty()) {
            urls = UrlResourceIT.createEntity(em);
            em.persist(urls);
            em.flush();
        } else {
            urls = TestUtil.findAll(em, Url.class).get(0);
        }
        em.persist(urls);
        em.flush();
        cronquistRank.addUrls(urls);
        cronquistRankRepository.saveAndFlush(cronquistRank);
        Long urlsId = urls.getId();

        // Get all the cronquistRankList where urls equals to urlsId
        defaultCronquistRankShouldBeFound("urlsId.equals=" + urlsId);

        // Get all the cronquistRankList where urls equals to (urlsId + 1)
        defaultCronquistRankShouldNotBeFound("urlsId.equals=" + (urlsId + 1));
    }

    @Test
    @Transactional
    void getAllCronquistRanksBySynonymesIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistRankRepository.saveAndFlush(cronquistRank);
        CronquistRank synonymes;
        if (TestUtil.findAll(em, CronquistRank.class).isEmpty()) {
            synonymes = CronquistRankResourceIT.createEntity(em);
            em.persist(synonymes);
            em.flush();
        } else {
            synonymes = TestUtil.findAll(em, CronquistRank.class).get(0);
        }
        em.persist(synonymes);
        em.flush();
        cronquistRank.addSynonymes(synonymes);
        cronquistRankRepository.saveAndFlush(cronquistRank);
        Long synonymesId = synonymes.getId();

        // Get all the cronquistRankList where synonymes equals to synonymesId
        defaultCronquistRankShouldBeFound("synonymesId.equals=" + synonymesId);

        // Get all the cronquistRankList where synonymes equals to (synonymesId + 1)
        defaultCronquistRankShouldNotBeFound("synonymesId.equals=" + (synonymesId + 1));
    }

    @Test
    @Transactional
    void getAllCronquistRanksByParentIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistRankRepository.saveAndFlush(cronquistRank);
        CronquistRank parent;
        if (TestUtil.findAll(em, CronquistRank.class).isEmpty()) {
            parent = CronquistRankResourceIT.createEntity(em);
            em.persist(parent);
            em.flush();
        } else {
            parent = TestUtil.findAll(em, CronquistRank.class).get(0);
        }
        em.persist(parent);
        em.flush();
        cronquistRank.setParent(parent);
        cronquistRankRepository.saveAndFlush(cronquistRank);
        Long parentId = parent.getId();

        // Get all the cronquistRankList where parent equals to parentId
        defaultCronquistRankShouldBeFound("parentId.equals=" + parentId);

        // Get all the cronquistRankList where parent equals to (parentId + 1)
        defaultCronquistRankShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }

    @Test
    @Transactional
    void getAllCronquistRanksByCronquistRankIsEqualToSomething() throws Exception {
        // Initialize the database
        cronquistRankRepository.saveAndFlush(cronquistRank);
        CronquistRank cronquistRank;
        if (TestUtil.findAll(em, CronquistRank.class).isEmpty()) {
            cronquistRank = CronquistRankResourceIT.createEntity(em);
            em.persist(cronquistRank);
            em.flush();
        } else {
            cronquistRank = TestUtil.findAll(em, CronquistRank.class).get(0);
        }
        em.persist(cronquistRank);
        em.flush();
        cronquistRank.setCronquistRank(cronquistRank);
        cronquistRankRepository.saveAndFlush(cronquistRank);
        Long cronquistRankId = cronquistRank.getId();

        // Get all the cronquistRankList where cronquistRank equals to cronquistRankId
        defaultCronquistRankShouldBeFound("cronquistRankId.equals=" + cronquistRankId);

        // Get all the cronquistRankList where cronquistRank equals to (cronquistRankId + 1)
        defaultCronquistRankShouldNotBeFound("cronquistRankId.equals=" + (cronquistRankId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCronquistRankShouldBeFound(String filter) throws Exception {
        restCronquistRankMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cronquistRank.getId().intValue())))
            .andExpect(jsonPath("$.[*].rank").value(hasItem(DEFAULT_RANK.toString())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLantin").value(hasItem(DEFAULT_NOM_LANTIN)));

        // Check, that the count call also returns 1
        restCronquistRankMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCronquistRankShouldNotBeFound(String filter) throws Exception {
        restCronquistRankMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCronquistRankMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCronquistRank() throws Exception {
        // Get the cronquistRank
        restCronquistRankMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCronquistRank() throws Exception {
        // Initialize the database
        cronquistRankRepository.saveAndFlush(cronquistRank);

        int databaseSizeBeforeUpdate = cronquistRankRepository.findAll().size();

        // Update the cronquistRank
        CronquistRank updatedCronquistRank = cronquistRankRepository.findById(cronquistRank.getId()).get();
        // Disconnect from session so that the updates on updatedCronquistRank are not directly saved in db
        em.detach(updatedCronquistRank);
        updatedCronquistRank.rank(UPDATED_RANK).nomFr(UPDATED_NOM_FR).nomLantin(UPDATED_NOM_LANTIN);

        restCronquistRankMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCronquistRank.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCronquistRank))
            )
            .andExpect(status().isOk());

        // Validate the CronquistRank in the database
        List<CronquistRank> cronquistRankList = cronquistRankRepository.findAll();
        assertThat(cronquistRankList).hasSize(databaseSizeBeforeUpdate);
        CronquistRank testCronquistRank = cronquistRankList.get(cronquistRankList.size() - 1);
        assertThat(testCronquistRank.getRank()).isEqualTo(UPDATED_RANK);
        assertThat(testCronquistRank.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testCronquistRank.getNomLantin()).isEqualTo(UPDATED_NOM_LANTIN);
    }

    @Test
    @Transactional
    void putNonExistingCronquistRank() throws Exception {
        int databaseSizeBeforeUpdate = cronquistRankRepository.findAll().size();
        cronquistRank.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCronquistRankMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cronquistRank.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cronquistRank))
            )
            .andExpect(status().isBadRequest());

        // Validate the CronquistRank in the database
        List<CronquistRank> cronquistRankList = cronquistRankRepository.findAll();
        assertThat(cronquistRankList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCronquistRank() throws Exception {
        int databaseSizeBeforeUpdate = cronquistRankRepository.findAll().size();
        cronquistRank.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCronquistRankMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cronquistRank))
            )
            .andExpect(status().isBadRequest());

        // Validate the CronquistRank in the database
        List<CronquistRank> cronquistRankList = cronquistRankRepository.findAll();
        assertThat(cronquistRankList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCronquistRank() throws Exception {
        int databaseSizeBeforeUpdate = cronquistRankRepository.findAll().size();
        cronquistRank.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCronquistRankMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cronquistRank)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CronquistRank in the database
        List<CronquistRank> cronquistRankList = cronquistRankRepository.findAll();
        assertThat(cronquistRankList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCronquistRankWithPatch() throws Exception {
        // Initialize the database
        cronquistRankRepository.saveAndFlush(cronquistRank);

        int databaseSizeBeforeUpdate = cronquistRankRepository.findAll().size();

        // Update the cronquistRank using partial update
        CronquistRank partialUpdatedCronquistRank = new CronquistRank();
        partialUpdatedCronquistRank.setId(cronquistRank.getId());

        partialUpdatedCronquistRank.nomLantin(UPDATED_NOM_LANTIN);

        restCronquistRankMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCronquistRank.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCronquistRank))
            )
            .andExpect(status().isOk());

        // Validate the CronquistRank in the database
        List<CronquistRank> cronquistRankList = cronquistRankRepository.findAll();
        assertThat(cronquistRankList).hasSize(databaseSizeBeforeUpdate);
        CronquistRank testCronquistRank = cronquistRankList.get(cronquistRankList.size() - 1);
        assertThat(testCronquistRank.getRank()).isEqualTo(DEFAULT_RANK);
        assertThat(testCronquistRank.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testCronquistRank.getNomLantin()).isEqualTo(UPDATED_NOM_LANTIN);
    }

    @Test
    @Transactional
    void fullUpdateCronquistRankWithPatch() throws Exception {
        // Initialize the database
        cronquistRankRepository.saveAndFlush(cronquistRank);

        int databaseSizeBeforeUpdate = cronquistRankRepository.findAll().size();

        // Update the cronquistRank using partial update
        CronquistRank partialUpdatedCronquistRank = new CronquistRank();
        partialUpdatedCronquistRank.setId(cronquistRank.getId());

        partialUpdatedCronquistRank.rank(UPDATED_RANK).nomFr(UPDATED_NOM_FR).nomLantin(UPDATED_NOM_LANTIN);

        restCronquistRankMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCronquistRank.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCronquistRank))
            )
            .andExpect(status().isOk());

        // Validate the CronquistRank in the database
        List<CronquistRank> cronquistRankList = cronquistRankRepository.findAll();
        assertThat(cronquistRankList).hasSize(databaseSizeBeforeUpdate);
        CronquistRank testCronquistRank = cronquistRankList.get(cronquistRankList.size() - 1);
        assertThat(testCronquistRank.getRank()).isEqualTo(UPDATED_RANK);
        assertThat(testCronquistRank.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testCronquistRank.getNomLantin()).isEqualTo(UPDATED_NOM_LANTIN);
    }

    @Test
    @Transactional
    void patchNonExistingCronquistRank() throws Exception {
        int databaseSizeBeforeUpdate = cronquistRankRepository.findAll().size();
        cronquistRank.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCronquistRankMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cronquistRank.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cronquistRank))
            )
            .andExpect(status().isBadRequest());

        // Validate the CronquistRank in the database
        List<CronquistRank> cronquistRankList = cronquistRankRepository.findAll();
        assertThat(cronquistRankList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCronquistRank() throws Exception {
        int databaseSizeBeforeUpdate = cronquistRankRepository.findAll().size();
        cronquistRank.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCronquistRankMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cronquistRank))
            )
            .andExpect(status().isBadRequest());

        // Validate the CronquistRank in the database
        List<CronquistRank> cronquistRankList = cronquistRankRepository.findAll();
        assertThat(cronquistRankList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCronquistRank() throws Exception {
        int databaseSizeBeforeUpdate = cronquistRankRepository.findAll().size();
        cronquistRank.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCronquistRankMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cronquistRank))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CronquistRank in the database
        List<CronquistRank> cronquistRankList = cronquistRankRepository.findAll();
        assertThat(cronquistRankList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCronquistRank() throws Exception {
        // Initialize the database
        cronquistRankRepository.saveAndFlush(cronquistRank);

        int databaseSizeBeforeDelete = cronquistRankRepository.findAll().size();

        // Delete the cronquistRank
        restCronquistRankMockMvc
            .perform(delete(ENTITY_API_URL_ID, cronquistRank.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CronquistRank> cronquistRankList = cronquistRankRepository.findAll();
        assertThat(cronquistRankList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
