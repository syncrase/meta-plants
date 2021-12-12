package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.InfraEmbranchement;
import fr.syncrase.ecosyst.domain.InfraEmbranchement;
import fr.syncrase.ecosyst.domain.MicroEmbranchement;
import fr.syncrase.ecosyst.domain.SousDivision;
import fr.syncrase.ecosyst.repository.InfraEmbranchementRepository;
import fr.syncrase.ecosyst.service.criteria.InfraEmbranchementCriteria;
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
 * Integration tests for the {@link InfraEmbranchementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InfraEmbranchementResourceIT {

    private static final String DEFAULT_NOM_FR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_LATIN = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LATIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/infra-embranchements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InfraEmbranchementRepository infraEmbranchementRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInfraEmbranchementMockMvc;

    private InfraEmbranchement infraEmbranchement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InfraEmbranchement createEntity(EntityManager em) {
        InfraEmbranchement infraEmbranchement = new InfraEmbranchement().nomFr(DEFAULT_NOM_FR).nomLatin(DEFAULT_NOM_LATIN);
        return infraEmbranchement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InfraEmbranchement createUpdatedEntity(EntityManager em) {
        InfraEmbranchement infraEmbranchement = new InfraEmbranchement().nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);
        return infraEmbranchement;
    }

    @BeforeEach
    public void initTest() {
        infraEmbranchement = createEntity(em);
    }

    @Test
    @Transactional
    void createInfraEmbranchement() throws Exception {
        int databaseSizeBeforeCreate = infraEmbranchementRepository.findAll().size();
        // Create the InfraEmbranchement
        restInfraEmbranchementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(infraEmbranchement))
            )
            .andExpect(status().isCreated());

        // Validate the InfraEmbranchement in the database
        List<InfraEmbranchement> infraEmbranchementList = infraEmbranchementRepository.findAll();
        assertThat(infraEmbranchementList).hasSize(databaseSizeBeforeCreate + 1);
        InfraEmbranchement testInfraEmbranchement = infraEmbranchementList.get(infraEmbranchementList.size() - 1);
        assertThat(testInfraEmbranchement.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testInfraEmbranchement.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void createInfraEmbranchementWithExistingId() throws Exception {
        // Create the InfraEmbranchement with an existing ID
        infraEmbranchement.setId(1L);

        int databaseSizeBeforeCreate = infraEmbranchementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInfraEmbranchementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(infraEmbranchement))
            )
            .andExpect(status().isBadRequest());

        // Validate the InfraEmbranchement in the database
        List<InfraEmbranchement> infraEmbranchementList = infraEmbranchementRepository.findAll();
        assertThat(infraEmbranchementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomFrIsRequired() throws Exception {
        int databaseSizeBeforeTest = infraEmbranchementRepository.findAll().size();
        // set the field null
        infraEmbranchement.setNomFr(null);

        // Create the InfraEmbranchement, which fails.

        restInfraEmbranchementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(infraEmbranchement))
            )
            .andExpect(status().isBadRequest());

        List<InfraEmbranchement> infraEmbranchementList = infraEmbranchementRepository.findAll();
        assertThat(infraEmbranchementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInfraEmbranchements() throws Exception {
        // Initialize the database
        infraEmbranchementRepository.saveAndFlush(infraEmbranchement);

        // Get all the infraEmbranchementList
        restInfraEmbranchementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(infraEmbranchement.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));
    }

    @Test
    @Transactional
    void getInfraEmbranchement() throws Exception {
        // Initialize the database
        infraEmbranchementRepository.saveAndFlush(infraEmbranchement);

        // Get the infraEmbranchement
        restInfraEmbranchementMockMvc
            .perform(get(ENTITY_API_URL_ID, infraEmbranchement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(infraEmbranchement.getId().intValue()))
            .andExpect(jsonPath("$.nomFr").value(DEFAULT_NOM_FR))
            .andExpect(jsonPath("$.nomLatin").value(DEFAULT_NOM_LATIN));
    }

    @Test
    @Transactional
    void getInfraEmbranchementsByIdFiltering() throws Exception {
        // Initialize the database
        infraEmbranchementRepository.saveAndFlush(infraEmbranchement);

        Long id = infraEmbranchement.getId();

        defaultInfraEmbranchementShouldBeFound("id.equals=" + id);
        defaultInfraEmbranchementShouldNotBeFound("id.notEquals=" + id);

        defaultInfraEmbranchementShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInfraEmbranchementShouldNotBeFound("id.greaterThan=" + id);

        defaultInfraEmbranchementShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInfraEmbranchementShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInfraEmbranchementsByNomFrIsEqualToSomething() throws Exception {
        // Initialize the database
        infraEmbranchementRepository.saveAndFlush(infraEmbranchement);

        // Get all the infraEmbranchementList where nomFr equals to DEFAULT_NOM_FR
        defaultInfraEmbranchementShouldBeFound("nomFr.equals=" + DEFAULT_NOM_FR);

        // Get all the infraEmbranchementList where nomFr equals to UPDATED_NOM_FR
        defaultInfraEmbranchementShouldNotBeFound("nomFr.equals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllInfraEmbranchementsByNomFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        infraEmbranchementRepository.saveAndFlush(infraEmbranchement);

        // Get all the infraEmbranchementList where nomFr not equals to DEFAULT_NOM_FR
        defaultInfraEmbranchementShouldNotBeFound("nomFr.notEquals=" + DEFAULT_NOM_FR);

        // Get all the infraEmbranchementList where nomFr not equals to UPDATED_NOM_FR
        defaultInfraEmbranchementShouldBeFound("nomFr.notEquals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllInfraEmbranchementsByNomFrIsInShouldWork() throws Exception {
        // Initialize the database
        infraEmbranchementRepository.saveAndFlush(infraEmbranchement);

        // Get all the infraEmbranchementList where nomFr in DEFAULT_NOM_FR or UPDATED_NOM_FR
        defaultInfraEmbranchementShouldBeFound("nomFr.in=" + DEFAULT_NOM_FR + "," + UPDATED_NOM_FR);

        // Get all the infraEmbranchementList where nomFr equals to UPDATED_NOM_FR
        defaultInfraEmbranchementShouldNotBeFound("nomFr.in=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllInfraEmbranchementsByNomFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        infraEmbranchementRepository.saveAndFlush(infraEmbranchement);

        // Get all the infraEmbranchementList where nomFr is not null
        defaultInfraEmbranchementShouldBeFound("nomFr.specified=true");

        // Get all the infraEmbranchementList where nomFr is null
        defaultInfraEmbranchementShouldNotBeFound("nomFr.specified=false");
    }

    @Test
    @Transactional
    void getAllInfraEmbranchementsByNomFrContainsSomething() throws Exception {
        // Initialize the database
        infraEmbranchementRepository.saveAndFlush(infraEmbranchement);

        // Get all the infraEmbranchementList where nomFr contains DEFAULT_NOM_FR
        defaultInfraEmbranchementShouldBeFound("nomFr.contains=" + DEFAULT_NOM_FR);

        // Get all the infraEmbranchementList where nomFr contains UPDATED_NOM_FR
        defaultInfraEmbranchementShouldNotBeFound("nomFr.contains=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllInfraEmbranchementsByNomFrNotContainsSomething() throws Exception {
        // Initialize the database
        infraEmbranchementRepository.saveAndFlush(infraEmbranchement);

        // Get all the infraEmbranchementList where nomFr does not contain DEFAULT_NOM_FR
        defaultInfraEmbranchementShouldNotBeFound("nomFr.doesNotContain=" + DEFAULT_NOM_FR);

        // Get all the infraEmbranchementList where nomFr does not contain UPDATED_NOM_FR
        defaultInfraEmbranchementShouldBeFound("nomFr.doesNotContain=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllInfraEmbranchementsByNomLatinIsEqualToSomething() throws Exception {
        // Initialize the database
        infraEmbranchementRepository.saveAndFlush(infraEmbranchement);

        // Get all the infraEmbranchementList where nomLatin equals to DEFAULT_NOM_LATIN
        defaultInfraEmbranchementShouldBeFound("nomLatin.equals=" + DEFAULT_NOM_LATIN);

        // Get all the infraEmbranchementList where nomLatin equals to UPDATED_NOM_LATIN
        defaultInfraEmbranchementShouldNotBeFound("nomLatin.equals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllInfraEmbranchementsByNomLatinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        infraEmbranchementRepository.saveAndFlush(infraEmbranchement);

        // Get all the infraEmbranchementList where nomLatin not equals to DEFAULT_NOM_LATIN
        defaultInfraEmbranchementShouldNotBeFound("nomLatin.notEquals=" + DEFAULT_NOM_LATIN);

        // Get all the infraEmbranchementList where nomLatin not equals to UPDATED_NOM_LATIN
        defaultInfraEmbranchementShouldBeFound("nomLatin.notEquals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllInfraEmbranchementsByNomLatinIsInShouldWork() throws Exception {
        // Initialize the database
        infraEmbranchementRepository.saveAndFlush(infraEmbranchement);

        // Get all the infraEmbranchementList where nomLatin in DEFAULT_NOM_LATIN or UPDATED_NOM_LATIN
        defaultInfraEmbranchementShouldBeFound("nomLatin.in=" + DEFAULT_NOM_LATIN + "," + UPDATED_NOM_LATIN);

        // Get all the infraEmbranchementList where nomLatin equals to UPDATED_NOM_LATIN
        defaultInfraEmbranchementShouldNotBeFound("nomLatin.in=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllInfraEmbranchementsByNomLatinIsNullOrNotNull() throws Exception {
        // Initialize the database
        infraEmbranchementRepository.saveAndFlush(infraEmbranchement);

        // Get all the infraEmbranchementList where nomLatin is not null
        defaultInfraEmbranchementShouldBeFound("nomLatin.specified=true");

        // Get all the infraEmbranchementList where nomLatin is null
        defaultInfraEmbranchementShouldNotBeFound("nomLatin.specified=false");
    }

    @Test
    @Transactional
    void getAllInfraEmbranchementsByNomLatinContainsSomething() throws Exception {
        // Initialize the database
        infraEmbranchementRepository.saveAndFlush(infraEmbranchement);

        // Get all the infraEmbranchementList where nomLatin contains DEFAULT_NOM_LATIN
        defaultInfraEmbranchementShouldBeFound("nomLatin.contains=" + DEFAULT_NOM_LATIN);

        // Get all the infraEmbranchementList where nomLatin contains UPDATED_NOM_LATIN
        defaultInfraEmbranchementShouldNotBeFound("nomLatin.contains=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllInfraEmbranchementsByNomLatinNotContainsSomething() throws Exception {
        // Initialize the database
        infraEmbranchementRepository.saveAndFlush(infraEmbranchement);

        // Get all the infraEmbranchementList where nomLatin does not contain DEFAULT_NOM_LATIN
        defaultInfraEmbranchementShouldNotBeFound("nomLatin.doesNotContain=" + DEFAULT_NOM_LATIN);

        // Get all the infraEmbranchementList where nomLatin does not contain UPDATED_NOM_LATIN
        defaultInfraEmbranchementShouldBeFound("nomLatin.doesNotContain=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllInfraEmbranchementsByMicroEmbranchementsIsEqualToSomething() throws Exception {
        // Initialize the database
        infraEmbranchementRepository.saveAndFlush(infraEmbranchement);
        MicroEmbranchement microEmbranchements;
        if (TestUtil.findAll(em, MicroEmbranchement.class).isEmpty()) {
            microEmbranchements = MicroEmbranchementResourceIT.createEntity(em);
            em.persist(microEmbranchements);
            em.flush();
        } else {
            microEmbranchements = TestUtil.findAll(em, MicroEmbranchement.class).get(0);
        }
        em.persist(microEmbranchements);
        em.flush();
        infraEmbranchement.addMicroEmbranchements(microEmbranchements);
        infraEmbranchementRepository.saveAndFlush(infraEmbranchement);
        Long microEmbranchementsId = microEmbranchements.getId();

        // Get all the infraEmbranchementList where microEmbranchements equals to microEmbranchementsId
        defaultInfraEmbranchementShouldBeFound("microEmbranchementsId.equals=" + microEmbranchementsId);

        // Get all the infraEmbranchementList where microEmbranchements equals to (microEmbranchementsId + 1)
        defaultInfraEmbranchementShouldNotBeFound("microEmbranchementsId.equals=" + (microEmbranchementsId + 1));
    }

    @Test
    @Transactional
    void getAllInfraEmbranchementsBySynonymesIsEqualToSomething() throws Exception {
        // Initialize the database
        infraEmbranchementRepository.saveAndFlush(infraEmbranchement);
        InfraEmbranchement synonymes;
        if (TestUtil.findAll(em, InfraEmbranchement.class).isEmpty()) {
            synonymes = InfraEmbranchementResourceIT.createEntity(em);
            em.persist(synonymes);
            em.flush();
        } else {
            synonymes = TestUtil.findAll(em, InfraEmbranchement.class).get(0);
        }
        em.persist(synonymes);
        em.flush();
        infraEmbranchement.addSynonymes(synonymes);
        infraEmbranchementRepository.saveAndFlush(infraEmbranchement);
        Long synonymesId = synonymes.getId();

        // Get all the infraEmbranchementList where synonymes equals to synonymesId
        defaultInfraEmbranchementShouldBeFound("synonymesId.equals=" + synonymesId);

        // Get all the infraEmbranchementList where synonymes equals to (synonymesId + 1)
        defaultInfraEmbranchementShouldNotBeFound("synonymesId.equals=" + (synonymesId + 1));
    }

    @Test
    @Transactional
    void getAllInfraEmbranchementsBySousDivisionIsEqualToSomething() throws Exception {
        // Initialize the database
        infraEmbranchementRepository.saveAndFlush(infraEmbranchement);
        SousDivision sousDivision;
        if (TestUtil.findAll(em, SousDivision.class).isEmpty()) {
            sousDivision = SousDivisionResourceIT.createEntity(em);
            em.persist(sousDivision);
            em.flush();
        } else {
            sousDivision = TestUtil.findAll(em, SousDivision.class).get(0);
        }
        em.persist(sousDivision);
        em.flush();
        infraEmbranchement.setSousDivision(sousDivision);
        infraEmbranchementRepository.saveAndFlush(infraEmbranchement);
        Long sousDivisionId = sousDivision.getId();

        // Get all the infraEmbranchementList where sousDivision equals to sousDivisionId
        defaultInfraEmbranchementShouldBeFound("sousDivisionId.equals=" + sousDivisionId);

        // Get all the infraEmbranchementList where sousDivision equals to (sousDivisionId + 1)
        defaultInfraEmbranchementShouldNotBeFound("sousDivisionId.equals=" + (sousDivisionId + 1));
    }

    @Test
    @Transactional
    void getAllInfraEmbranchementsByInfraEmbranchementIsEqualToSomething() throws Exception {
        // Initialize the database
        infraEmbranchementRepository.saveAndFlush(infraEmbranchement);
        InfraEmbranchement infraEmbranchement;
        if (TestUtil.findAll(em, InfraEmbranchement.class).isEmpty()) {
            infraEmbranchement = InfraEmbranchementResourceIT.createEntity(em);
            em.persist(infraEmbranchement);
            em.flush();
        } else {
            infraEmbranchement = TestUtil.findAll(em, InfraEmbranchement.class).get(0);
        }
        em.persist(infraEmbranchement);
        em.flush();
        infraEmbranchement.setInfraEmbranchement(infraEmbranchement);
        infraEmbranchementRepository.saveAndFlush(infraEmbranchement);
        Long infraEmbranchementId = infraEmbranchement.getId();

        // Get all the infraEmbranchementList where infraEmbranchement equals to infraEmbranchementId
        defaultInfraEmbranchementShouldBeFound("infraEmbranchementId.equals=" + infraEmbranchementId);

        // Get all the infraEmbranchementList where infraEmbranchement equals to (infraEmbranchementId + 1)
        defaultInfraEmbranchementShouldNotBeFound("infraEmbranchementId.equals=" + (infraEmbranchementId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInfraEmbranchementShouldBeFound(String filter) throws Exception {
        restInfraEmbranchementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(infraEmbranchement.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));

        // Check, that the count call also returns 1
        restInfraEmbranchementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInfraEmbranchementShouldNotBeFound(String filter) throws Exception {
        restInfraEmbranchementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInfraEmbranchementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInfraEmbranchement() throws Exception {
        // Get the infraEmbranchement
        restInfraEmbranchementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInfraEmbranchement() throws Exception {
        // Initialize the database
        infraEmbranchementRepository.saveAndFlush(infraEmbranchement);

        int databaseSizeBeforeUpdate = infraEmbranchementRepository.findAll().size();

        // Update the infraEmbranchement
        InfraEmbranchement updatedInfraEmbranchement = infraEmbranchementRepository.findById(infraEmbranchement.getId()).get();
        // Disconnect from session so that the updates on updatedInfraEmbranchement are not directly saved in db
        em.detach(updatedInfraEmbranchement);
        updatedInfraEmbranchement.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restInfraEmbranchementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInfraEmbranchement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedInfraEmbranchement))
            )
            .andExpect(status().isOk());

        // Validate the InfraEmbranchement in the database
        List<InfraEmbranchement> infraEmbranchementList = infraEmbranchementRepository.findAll();
        assertThat(infraEmbranchementList).hasSize(databaseSizeBeforeUpdate);
        InfraEmbranchement testInfraEmbranchement = infraEmbranchementList.get(infraEmbranchementList.size() - 1);
        assertThat(testInfraEmbranchement.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testInfraEmbranchement.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void putNonExistingInfraEmbranchement() throws Exception {
        int databaseSizeBeforeUpdate = infraEmbranchementRepository.findAll().size();
        infraEmbranchement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInfraEmbranchementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, infraEmbranchement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(infraEmbranchement))
            )
            .andExpect(status().isBadRequest());

        // Validate the InfraEmbranchement in the database
        List<InfraEmbranchement> infraEmbranchementList = infraEmbranchementRepository.findAll();
        assertThat(infraEmbranchementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInfraEmbranchement() throws Exception {
        int databaseSizeBeforeUpdate = infraEmbranchementRepository.findAll().size();
        infraEmbranchement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInfraEmbranchementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(infraEmbranchement))
            )
            .andExpect(status().isBadRequest());

        // Validate the InfraEmbranchement in the database
        List<InfraEmbranchement> infraEmbranchementList = infraEmbranchementRepository.findAll();
        assertThat(infraEmbranchementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInfraEmbranchement() throws Exception {
        int databaseSizeBeforeUpdate = infraEmbranchementRepository.findAll().size();
        infraEmbranchement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInfraEmbranchementMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(infraEmbranchement))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InfraEmbranchement in the database
        List<InfraEmbranchement> infraEmbranchementList = infraEmbranchementRepository.findAll();
        assertThat(infraEmbranchementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInfraEmbranchementWithPatch() throws Exception {
        // Initialize the database
        infraEmbranchementRepository.saveAndFlush(infraEmbranchement);

        int databaseSizeBeforeUpdate = infraEmbranchementRepository.findAll().size();

        // Update the infraEmbranchement using partial update
        InfraEmbranchement partialUpdatedInfraEmbranchement = new InfraEmbranchement();
        partialUpdatedInfraEmbranchement.setId(infraEmbranchement.getId());

        partialUpdatedInfraEmbranchement.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restInfraEmbranchementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInfraEmbranchement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInfraEmbranchement))
            )
            .andExpect(status().isOk());

        // Validate the InfraEmbranchement in the database
        List<InfraEmbranchement> infraEmbranchementList = infraEmbranchementRepository.findAll();
        assertThat(infraEmbranchementList).hasSize(databaseSizeBeforeUpdate);
        InfraEmbranchement testInfraEmbranchement = infraEmbranchementList.get(infraEmbranchementList.size() - 1);
        assertThat(testInfraEmbranchement.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testInfraEmbranchement.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void fullUpdateInfraEmbranchementWithPatch() throws Exception {
        // Initialize the database
        infraEmbranchementRepository.saveAndFlush(infraEmbranchement);

        int databaseSizeBeforeUpdate = infraEmbranchementRepository.findAll().size();

        // Update the infraEmbranchement using partial update
        InfraEmbranchement partialUpdatedInfraEmbranchement = new InfraEmbranchement();
        partialUpdatedInfraEmbranchement.setId(infraEmbranchement.getId());

        partialUpdatedInfraEmbranchement.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restInfraEmbranchementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInfraEmbranchement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInfraEmbranchement))
            )
            .andExpect(status().isOk());

        // Validate the InfraEmbranchement in the database
        List<InfraEmbranchement> infraEmbranchementList = infraEmbranchementRepository.findAll();
        assertThat(infraEmbranchementList).hasSize(databaseSizeBeforeUpdate);
        InfraEmbranchement testInfraEmbranchement = infraEmbranchementList.get(infraEmbranchementList.size() - 1);
        assertThat(testInfraEmbranchement.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testInfraEmbranchement.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void patchNonExistingInfraEmbranchement() throws Exception {
        int databaseSizeBeforeUpdate = infraEmbranchementRepository.findAll().size();
        infraEmbranchement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInfraEmbranchementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, infraEmbranchement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(infraEmbranchement))
            )
            .andExpect(status().isBadRequest());

        // Validate the InfraEmbranchement in the database
        List<InfraEmbranchement> infraEmbranchementList = infraEmbranchementRepository.findAll();
        assertThat(infraEmbranchementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInfraEmbranchement() throws Exception {
        int databaseSizeBeforeUpdate = infraEmbranchementRepository.findAll().size();
        infraEmbranchement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInfraEmbranchementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(infraEmbranchement))
            )
            .andExpect(status().isBadRequest());

        // Validate the InfraEmbranchement in the database
        List<InfraEmbranchement> infraEmbranchementList = infraEmbranchementRepository.findAll();
        assertThat(infraEmbranchementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInfraEmbranchement() throws Exception {
        int databaseSizeBeforeUpdate = infraEmbranchementRepository.findAll().size();
        infraEmbranchement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInfraEmbranchementMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(infraEmbranchement))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InfraEmbranchement in the database
        List<InfraEmbranchement> infraEmbranchementList = infraEmbranchementRepository.findAll();
        assertThat(infraEmbranchementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInfraEmbranchement() throws Exception {
        // Initialize the database
        infraEmbranchementRepository.saveAndFlush(infraEmbranchement);

        int databaseSizeBeforeDelete = infraEmbranchementRepository.findAll().size();

        // Delete the infraEmbranchement
        restInfraEmbranchementMockMvc
            .perform(delete(ENTITY_API_URL_ID, infraEmbranchement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InfraEmbranchement> infraEmbranchementList = infraEmbranchementRepository.findAll();
        assertThat(infraEmbranchementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
