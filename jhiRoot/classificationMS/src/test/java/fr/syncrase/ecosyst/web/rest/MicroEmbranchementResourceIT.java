package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.InfraEmbranchement;
import fr.syncrase.ecosyst.domain.MicroEmbranchement;
import fr.syncrase.ecosyst.domain.MicroEmbranchement;
import fr.syncrase.ecosyst.domain.SuperClasse;
import fr.syncrase.ecosyst.repository.MicroEmbranchementRepository;
import fr.syncrase.ecosyst.service.criteria.MicroEmbranchementCriteria;
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
 * Integration tests for the {@link MicroEmbranchementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MicroEmbranchementResourceIT {

    private static final String DEFAULT_NOM_FR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_LATIN = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LATIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/micro-embranchements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MicroEmbranchementRepository microEmbranchementRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMicroEmbranchementMockMvc;

    private MicroEmbranchement microEmbranchement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MicroEmbranchement createEntity(EntityManager em) {
        MicroEmbranchement microEmbranchement = new MicroEmbranchement().nomFr(DEFAULT_NOM_FR).nomLatin(DEFAULT_NOM_LATIN);
        return microEmbranchement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MicroEmbranchement createUpdatedEntity(EntityManager em) {
        MicroEmbranchement microEmbranchement = new MicroEmbranchement().nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);
        return microEmbranchement;
    }

    @BeforeEach
    public void initTest() {
        microEmbranchement = createEntity(em);
    }

    @Test
    @Transactional
    void createMicroEmbranchement() throws Exception {
        int databaseSizeBeforeCreate = microEmbranchementRepository.findAll().size();
        // Create the MicroEmbranchement
        restMicroEmbranchementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(microEmbranchement))
            )
            .andExpect(status().isCreated());

        // Validate the MicroEmbranchement in the database
        List<MicroEmbranchement> microEmbranchementList = microEmbranchementRepository.findAll();
        assertThat(microEmbranchementList).hasSize(databaseSizeBeforeCreate + 1);
        MicroEmbranchement testMicroEmbranchement = microEmbranchementList.get(microEmbranchementList.size() - 1);
        assertThat(testMicroEmbranchement.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testMicroEmbranchement.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void createMicroEmbranchementWithExistingId() throws Exception {
        // Create the MicroEmbranchement with an existing ID
        microEmbranchement.setId(1L);

        int databaseSizeBeforeCreate = microEmbranchementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMicroEmbranchementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(microEmbranchement))
            )
            .andExpect(status().isBadRequest());

        // Validate the MicroEmbranchement in the database
        List<MicroEmbranchement> microEmbranchementList = microEmbranchementRepository.findAll();
        assertThat(microEmbranchementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomFrIsRequired() throws Exception {
        int databaseSizeBeforeTest = microEmbranchementRepository.findAll().size();
        // set the field null
        microEmbranchement.setNomFr(null);

        // Create the MicroEmbranchement, which fails.

        restMicroEmbranchementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(microEmbranchement))
            )
            .andExpect(status().isBadRequest());

        List<MicroEmbranchement> microEmbranchementList = microEmbranchementRepository.findAll();
        assertThat(microEmbranchementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMicroEmbranchements() throws Exception {
        // Initialize the database
        microEmbranchementRepository.saveAndFlush(microEmbranchement);

        // Get all the microEmbranchementList
        restMicroEmbranchementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(microEmbranchement.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));
    }

    @Test
    @Transactional
    void getMicroEmbranchement() throws Exception {
        // Initialize the database
        microEmbranchementRepository.saveAndFlush(microEmbranchement);

        // Get the microEmbranchement
        restMicroEmbranchementMockMvc
            .perform(get(ENTITY_API_URL_ID, microEmbranchement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(microEmbranchement.getId().intValue()))
            .andExpect(jsonPath("$.nomFr").value(DEFAULT_NOM_FR))
            .andExpect(jsonPath("$.nomLatin").value(DEFAULT_NOM_LATIN));
    }

    @Test
    @Transactional
    void getMicroEmbranchementsByIdFiltering() throws Exception {
        // Initialize the database
        microEmbranchementRepository.saveAndFlush(microEmbranchement);

        Long id = microEmbranchement.getId();

        defaultMicroEmbranchementShouldBeFound("id.equals=" + id);
        defaultMicroEmbranchementShouldNotBeFound("id.notEquals=" + id);

        defaultMicroEmbranchementShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMicroEmbranchementShouldNotBeFound("id.greaterThan=" + id);

        defaultMicroEmbranchementShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMicroEmbranchementShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMicroEmbranchementsByNomFrIsEqualToSomething() throws Exception {
        // Initialize the database
        microEmbranchementRepository.saveAndFlush(microEmbranchement);

        // Get all the microEmbranchementList where nomFr equals to DEFAULT_NOM_FR
        defaultMicroEmbranchementShouldBeFound("nomFr.equals=" + DEFAULT_NOM_FR);

        // Get all the microEmbranchementList where nomFr equals to UPDATED_NOM_FR
        defaultMicroEmbranchementShouldNotBeFound("nomFr.equals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllMicroEmbranchementsByNomFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        microEmbranchementRepository.saveAndFlush(microEmbranchement);

        // Get all the microEmbranchementList where nomFr not equals to DEFAULT_NOM_FR
        defaultMicroEmbranchementShouldNotBeFound("nomFr.notEquals=" + DEFAULT_NOM_FR);

        // Get all the microEmbranchementList where nomFr not equals to UPDATED_NOM_FR
        defaultMicroEmbranchementShouldBeFound("nomFr.notEquals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllMicroEmbranchementsByNomFrIsInShouldWork() throws Exception {
        // Initialize the database
        microEmbranchementRepository.saveAndFlush(microEmbranchement);

        // Get all the microEmbranchementList where nomFr in DEFAULT_NOM_FR or UPDATED_NOM_FR
        defaultMicroEmbranchementShouldBeFound("nomFr.in=" + DEFAULT_NOM_FR + "," + UPDATED_NOM_FR);

        // Get all the microEmbranchementList where nomFr equals to UPDATED_NOM_FR
        defaultMicroEmbranchementShouldNotBeFound("nomFr.in=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllMicroEmbranchementsByNomFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        microEmbranchementRepository.saveAndFlush(microEmbranchement);

        // Get all the microEmbranchementList where nomFr is not null
        defaultMicroEmbranchementShouldBeFound("nomFr.specified=true");

        // Get all the microEmbranchementList where nomFr is null
        defaultMicroEmbranchementShouldNotBeFound("nomFr.specified=false");
    }

    @Test
    @Transactional
    void getAllMicroEmbranchementsByNomFrContainsSomething() throws Exception {
        // Initialize the database
        microEmbranchementRepository.saveAndFlush(microEmbranchement);

        // Get all the microEmbranchementList where nomFr contains DEFAULT_NOM_FR
        defaultMicroEmbranchementShouldBeFound("nomFr.contains=" + DEFAULT_NOM_FR);

        // Get all the microEmbranchementList where nomFr contains UPDATED_NOM_FR
        defaultMicroEmbranchementShouldNotBeFound("nomFr.contains=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllMicroEmbranchementsByNomFrNotContainsSomething() throws Exception {
        // Initialize the database
        microEmbranchementRepository.saveAndFlush(microEmbranchement);

        // Get all the microEmbranchementList where nomFr does not contain DEFAULT_NOM_FR
        defaultMicroEmbranchementShouldNotBeFound("nomFr.doesNotContain=" + DEFAULT_NOM_FR);

        // Get all the microEmbranchementList where nomFr does not contain UPDATED_NOM_FR
        defaultMicroEmbranchementShouldBeFound("nomFr.doesNotContain=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllMicroEmbranchementsByNomLatinIsEqualToSomething() throws Exception {
        // Initialize the database
        microEmbranchementRepository.saveAndFlush(microEmbranchement);

        // Get all the microEmbranchementList where nomLatin equals to DEFAULT_NOM_LATIN
        defaultMicroEmbranchementShouldBeFound("nomLatin.equals=" + DEFAULT_NOM_LATIN);

        // Get all the microEmbranchementList where nomLatin equals to UPDATED_NOM_LATIN
        defaultMicroEmbranchementShouldNotBeFound("nomLatin.equals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllMicroEmbranchementsByNomLatinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        microEmbranchementRepository.saveAndFlush(microEmbranchement);

        // Get all the microEmbranchementList where nomLatin not equals to DEFAULT_NOM_LATIN
        defaultMicroEmbranchementShouldNotBeFound("nomLatin.notEquals=" + DEFAULT_NOM_LATIN);

        // Get all the microEmbranchementList where nomLatin not equals to UPDATED_NOM_LATIN
        defaultMicroEmbranchementShouldBeFound("nomLatin.notEquals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllMicroEmbranchementsByNomLatinIsInShouldWork() throws Exception {
        // Initialize the database
        microEmbranchementRepository.saveAndFlush(microEmbranchement);

        // Get all the microEmbranchementList where nomLatin in DEFAULT_NOM_LATIN or UPDATED_NOM_LATIN
        defaultMicroEmbranchementShouldBeFound("nomLatin.in=" + DEFAULT_NOM_LATIN + "," + UPDATED_NOM_LATIN);

        // Get all the microEmbranchementList where nomLatin equals to UPDATED_NOM_LATIN
        defaultMicroEmbranchementShouldNotBeFound("nomLatin.in=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllMicroEmbranchementsByNomLatinIsNullOrNotNull() throws Exception {
        // Initialize the database
        microEmbranchementRepository.saveAndFlush(microEmbranchement);

        // Get all the microEmbranchementList where nomLatin is not null
        defaultMicroEmbranchementShouldBeFound("nomLatin.specified=true");

        // Get all the microEmbranchementList where nomLatin is null
        defaultMicroEmbranchementShouldNotBeFound("nomLatin.specified=false");
    }

    @Test
    @Transactional
    void getAllMicroEmbranchementsByNomLatinContainsSomething() throws Exception {
        // Initialize the database
        microEmbranchementRepository.saveAndFlush(microEmbranchement);

        // Get all the microEmbranchementList where nomLatin contains DEFAULT_NOM_LATIN
        defaultMicroEmbranchementShouldBeFound("nomLatin.contains=" + DEFAULT_NOM_LATIN);

        // Get all the microEmbranchementList where nomLatin contains UPDATED_NOM_LATIN
        defaultMicroEmbranchementShouldNotBeFound("nomLatin.contains=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllMicroEmbranchementsByNomLatinNotContainsSomething() throws Exception {
        // Initialize the database
        microEmbranchementRepository.saveAndFlush(microEmbranchement);

        // Get all the microEmbranchementList where nomLatin does not contain DEFAULT_NOM_LATIN
        defaultMicroEmbranchementShouldNotBeFound("nomLatin.doesNotContain=" + DEFAULT_NOM_LATIN);

        // Get all the microEmbranchementList where nomLatin does not contain UPDATED_NOM_LATIN
        defaultMicroEmbranchementShouldBeFound("nomLatin.doesNotContain=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllMicroEmbranchementsBySuperClassesIsEqualToSomething() throws Exception {
        // Initialize the database
        microEmbranchementRepository.saveAndFlush(microEmbranchement);
        SuperClasse superClasses;
        if (TestUtil.findAll(em, SuperClasse.class).isEmpty()) {
            superClasses = SuperClasseResourceIT.createEntity(em);
            em.persist(superClasses);
            em.flush();
        } else {
            superClasses = TestUtil.findAll(em, SuperClasse.class).get(0);
        }
        em.persist(superClasses);
        em.flush();
        microEmbranchement.addSuperClasses(superClasses);
        microEmbranchementRepository.saveAndFlush(microEmbranchement);
        Long superClassesId = superClasses.getId();

        // Get all the microEmbranchementList where superClasses equals to superClassesId
        defaultMicroEmbranchementShouldBeFound("superClassesId.equals=" + superClassesId);

        // Get all the microEmbranchementList where superClasses equals to (superClassesId + 1)
        defaultMicroEmbranchementShouldNotBeFound("superClassesId.equals=" + (superClassesId + 1));
    }

    @Test
    @Transactional
    void getAllMicroEmbranchementsBySynonymesIsEqualToSomething() throws Exception {
        // Initialize the database
        microEmbranchementRepository.saveAndFlush(microEmbranchement);
        MicroEmbranchement synonymes;
        if (TestUtil.findAll(em, MicroEmbranchement.class).isEmpty()) {
            synonymes = MicroEmbranchementResourceIT.createEntity(em);
            em.persist(synonymes);
            em.flush();
        } else {
            synonymes = TestUtil.findAll(em, MicroEmbranchement.class).get(0);
        }
        em.persist(synonymes);
        em.flush();
        microEmbranchement.addSynonymes(synonymes);
        microEmbranchementRepository.saveAndFlush(microEmbranchement);
        Long synonymesId = synonymes.getId();

        // Get all the microEmbranchementList where synonymes equals to synonymesId
        defaultMicroEmbranchementShouldBeFound("synonymesId.equals=" + synonymesId);

        // Get all the microEmbranchementList where synonymes equals to (synonymesId + 1)
        defaultMicroEmbranchementShouldNotBeFound("synonymesId.equals=" + (synonymesId + 1));
    }

    @Test
    @Transactional
    void getAllMicroEmbranchementsByInfraEmbranchementIsEqualToSomething() throws Exception {
        // Initialize the database
        microEmbranchementRepository.saveAndFlush(microEmbranchement);
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
        microEmbranchement.setInfraEmbranchement(infraEmbranchement);
        microEmbranchementRepository.saveAndFlush(microEmbranchement);
        Long infraEmbranchementId = infraEmbranchement.getId();

        // Get all the microEmbranchementList where infraEmbranchement equals to infraEmbranchementId
        defaultMicroEmbranchementShouldBeFound("infraEmbranchementId.equals=" + infraEmbranchementId);

        // Get all the microEmbranchementList where infraEmbranchement equals to (infraEmbranchementId + 1)
        defaultMicroEmbranchementShouldNotBeFound("infraEmbranchementId.equals=" + (infraEmbranchementId + 1));
    }

    @Test
    @Transactional
    void getAllMicroEmbranchementsByMicroEmbranchementIsEqualToSomething() throws Exception {
        // Initialize the database
        microEmbranchementRepository.saveAndFlush(microEmbranchement);
        MicroEmbranchement microEmbranchement;
        if (TestUtil.findAll(em, MicroEmbranchement.class).isEmpty()) {
            microEmbranchement = MicroEmbranchementResourceIT.createEntity(em);
            em.persist(microEmbranchement);
            em.flush();
        } else {
            microEmbranchement = TestUtil.findAll(em, MicroEmbranchement.class).get(0);
        }
        em.persist(microEmbranchement);
        em.flush();
        microEmbranchement.setMicroEmbranchement(microEmbranchement);
        microEmbranchementRepository.saveAndFlush(microEmbranchement);
        Long microEmbranchementId = microEmbranchement.getId();

        // Get all the microEmbranchementList where microEmbranchement equals to microEmbranchementId
        defaultMicroEmbranchementShouldBeFound("microEmbranchementId.equals=" + microEmbranchementId);

        // Get all the microEmbranchementList where microEmbranchement equals to (microEmbranchementId + 1)
        defaultMicroEmbranchementShouldNotBeFound("microEmbranchementId.equals=" + (microEmbranchementId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMicroEmbranchementShouldBeFound(String filter) throws Exception {
        restMicroEmbranchementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(microEmbranchement.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));

        // Check, that the count call also returns 1
        restMicroEmbranchementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMicroEmbranchementShouldNotBeFound(String filter) throws Exception {
        restMicroEmbranchementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMicroEmbranchementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMicroEmbranchement() throws Exception {
        // Get the microEmbranchement
        restMicroEmbranchementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMicroEmbranchement() throws Exception {
        // Initialize the database
        microEmbranchementRepository.saveAndFlush(microEmbranchement);

        int databaseSizeBeforeUpdate = microEmbranchementRepository.findAll().size();

        // Update the microEmbranchement
        MicroEmbranchement updatedMicroEmbranchement = microEmbranchementRepository.findById(microEmbranchement.getId()).get();
        // Disconnect from session so that the updates on updatedMicroEmbranchement are not directly saved in db
        em.detach(updatedMicroEmbranchement);
        updatedMicroEmbranchement.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restMicroEmbranchementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMicroEmbranchement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMicroEmbranchement))
            )
            .andExpect(status().isOk());

        // Validate the MicroEmbranchement in the database
        List<MicroEmbranchement> microEmbranchementList = microEmbranchementRepository.findAll();
        assertThat(microEmbranchementList).hasSize(databaseSizeBeforeUpdate);
        MicroEmbranchement testMicroEmbranchement = microEmbranchementList.get(microEmbranchementList.size() - 1);
        assertThat(testMicroEmbranchement.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testMicroEmbranchement.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void putNonExistingMicroEmbranchement() throws Exception {
        int databaseSizeBeforeUpdate = microEmbranchementRepository.findAll().size();
        microEmbranchement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMicroEmbranchementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, microEmbranchement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(microEmbranchement))
            )
            .andExpect(status().isBadRequest());

        // Validate the MicroEmbranchement in the database
        List<MicroEmbranchement> microEmbranchementList = microEmbranchementRepository.findAll();
        assertThat(microEmbranchementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMicroEmbranchement() throws Exception {
        int databaseSizeBeforeUpdate = microEmbranchementRepository.findAll().size();
        microEmbranchement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMicroEmbranchementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(microEmbranchement))
            )
            .andExpect(status().isBadRequest());

        // Validate the MicroEmbranchement in the database
        List<MicroEmbranchement> microEmbranchementList = microEmbranchementRepository.findAll();
        assertThat(microEmbranchementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMicroEmbranchement() throws Exception {
        int databaseSizeBeforeUpdate = microEmbranchementRepository.findAll().size();
        microEmbranchement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMicroEmbranchementMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(microEmbranchement))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MicroEmbranchement in the database
        List<MicroEmbranchement> microEmbranchementList = microEmbranchementRepository.findAll();
        assertThat(microEmbranchementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMicroEmbranchementWithPatch() throws Exception {
        // Initialize the database
        microEmbranchementRepository.saveAndFlush(microEmbranchement);

        int databaseSizeBeforeUpdate = microEmbranchementRepository.findAll().size();

        // Update the microEmbranchement using partial update
        MicroEmbranchement partialUpdatedMicroEmbranchement = new MicroEmbranchement();
        partialUpdatedMicroEmbranchement.setId(microEmbranchement.getId());

        partialUpdatedMicroEmbranchement.nomFr(UPDATED_NOM_FR);

        restMicroEmbranchementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMicroEmbranchement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMicroEmbranchement))
            )
            .andExpect(status().isOk());

        // Validate the MicroEmbranchement in the database
        List<MicroEmbranchement> microEmbranchementList = microEmbranchementRepository.findAll();
        assertThat(microEmbranchementList).hasSize(databaseSizeBeforeUpdate);
        MicroEmbranchement testMicroEmbranchement = microEmbranchementList.get(microEmbranchementList.size() - 1);
        assertThat(testMicroEmbranchement.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testMicroEmbranchement.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void fullUpdateMicroEmbranchementWithPatch() throws Exception {
        // Initialize the database
        microEmbranchementRepository.saveAndFlush(microEmbranchement);

        int databaseSizeBeforeUpdate = microEmbranchementRepository.findAll().size();

        // Update the microEmbranchement using partial update
        MicroEmbranchement partialUpdatedMicroEmbranchement = new MicroEmbranchement();
        partialUpdatedMicroEmbranchement.setId(microEmbranchement.getId());

        partialUpdatedMicroEmbranchement.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restMicroEmbranchementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMicroEmbranchement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMicroEmbranchement))
            )
            .andExpect(status().isOk());

        // Validate the MicroEmbranchement in the database
        List<MicroEmbranchement> microEmbranchementList = microEmbranchementRepository.findAll();
        assertThat(microEmbranchementList).hasSize(databaseSizeBeforeUpdate);
        MicroEmbranchement testMicroEmbranchement = microEmbranchementList.get(microEmbranchementList.size() - 1);
        assertThat(testMicroEmbranchement.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testMicroEmbranchement.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void patchNonExistingMicroEmbranchement() throws Exception {
        int databaseSizeBeforeUpdate = microEmbranchementRepository.findAll().size();
        microEmbranchement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMicroEmbranchementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, microEmbranchement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(microEmbranchement))
            )
            .andExpect(status().isBadRequest());

        // Validate the MicroEmbranchement in the database
        List<MicroEmbranchement> microEmbranchementList = microEmbranchementRepository.findAll();
        assertThat(microEmbranchementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMicroEmbranchement() throws Exception {
        int databaseSizeBeforeUpdate = microEmbranchementRepository.findAll().size();
        microEmbranchement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMicroEmbranchementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(microEmbranchement))
            )
            .andExpect(status().isBadRequest());

        // Validate the MicroEmbranchement in the database
        List<MicroEmbranchement> microEmbranchementList = microEmbranchementRepository.findAll();
        assertThat(microEmbranchementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMicroEmbranchement() throws Exception {
        int databaseSizeBeforeUpdate = microEmbranchementRepository.findAll().size();
        microEmbranchement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMicroEmbranchementMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(microEmbranchement))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MicroEmbranchement in the database
        List<MicroEmbranchement> microEmbranchementList = microEmbranchementRepository.findAll();
        assertThat(microEmbranchementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMicroEmbranchement() throws Exception {
        // Initialize the database
        microEmbranchementRepository.saveAndFlush(microEmbranchement);

        int databaseSizeBeforeDelete = microEmbranchementRepository.findAll().size();

        // Delete the microEmbranchement
        restMicroEmbranchementMockMvc
            .perform(delete(ENTITY_API_URL_ID, microEmbranchement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MicroEmbranchement> microEmbranchementList = microEmbranchementRepository.findAll();
        assertThat(microEmbranchementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
