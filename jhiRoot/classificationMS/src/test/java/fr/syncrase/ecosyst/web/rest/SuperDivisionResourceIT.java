package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.Division;
import fr.syncrase.ecosyst.domain.InfraRegne;
import fr.syncrase.ecosyst.domain.SuperDivision;
import fr.syncrase.ecosyst.domain.SuperDivision;
import fr.syncrase.ecosyst.repository.SuperDivisionRepository;
import fr.syncrase.ecosyst.service.criteria.SuperDivisionCriteria;
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
 * Integration tests for the {@link SuperDivisionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SuperDivisionResourceIT {

    private static final String DEFAULT_NOM_FR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_LATIN = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LATIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/super-divisions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SuperDivisionRepository superDivisionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSuperDivisionMockMvc;

    private SuperDivision superDivision;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SuperDivision createEntity(EntityManager em) {
        SuperDivision superDivision = new SuperDivision().nomFr(DEFAULT_NOM_FR).nomLatin(DEFAULT_NOM_LATIN);
        return superDivision;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SuperDivision createUpdatedEntity(EntityManager em) {
        SuperDivision superDivision = new SuperDivision().nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);
        return superDivision;
    }

    @BeforeEach
    public void initTest() {
        superDivision = createEntity(em);
    }

    @Test
    @Transactional
    void createSuperDivision() throws Exception {
        int databaseSizeBeforeCreate = superDivisionRepository.findAll().size();
        // Create the SuperDivision
        restSuperDivisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(superDivision)))
            .andExpect(status().isCreated());

        // Validate the SuperDivision in the database
        List<SuperDivision> superDivisionList = superDivisionRepository.findAll();
        assertThat(superDivisionList).hasSize(databaseSizeBeforeCreate + 1);
        SuperDivision testSuperDivision = superDivisionList.get(superDivisionList.size() - 1);
        assertThat(testSuperDivision.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testSuperDivision.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void createSuperDivisionWithExistingId() throws Exception {
        // Create the SuperDivision with an existing ID
        superDivision.setId(1L);

        int databaseSizeBeforeCreate = superDivisionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSuperDivisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(superDivision)))
            .andExpect(status().isBadRequest());

        // Validate the SuperDivision in the database
        List<SuperDivision> superDivisionList = superDivisionRepository.findAll();
        assertThat(superDivisionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomFrIsRequired() throws Exception {
        int databaseSizeBeforeTest = superDivisionRepository.findAll().size();
        // set the field null
        superDivision.setNomFr(null);

        // Create the SuperDivision, which fails.

        restSuperDivisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(superDivision)))
            .andExpect(status().isBadRequest());

        List<SuperDivision> superDivisionList = superDivisionRepository.findAll();
        assertThat(superDivisionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSuperDivisions() throws Exception {
        // Initialize the database
        superDivisionRepository.saveAndFlush(superDivision);

        // Get all the superDivisionList
        restSuperDivisionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(superDivision.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));
    }

    @Test
    @Transactional
    void getSuperDivision() throws Exception {
        // Initialize the database
        superDivisionRepository.saveAndFlush(superDivision);

        // Get the superDivision
        restSuperDivisionMockMvc
            .perform(get(ENTITY_API_URL_ID, superDivision.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(superDivision.getId().intValue()))
            .andExpect(jsonPath("$.nomFr").value(DEFAULT_NOM_FR))
            .andExpect(jsonPath("$.nomLatin").value(DEFAULT_NOM_LATIN));
    }

    @Test
    @Transactional
    void getSuperDivisionsByIdFiltering() throws Exception {
        // Initialize the database
        superDivisionRepository.saveAndFlush(superDivision);

        Long id = superDivision.getId();

        defaultSuperDivisionShouldBeFound("id.equals=" + id);
        defaultSuperDivisionShouldNotBeFound("id.notEquals=" + id);

        defaultSuperDivisionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSuperDivisionShouldNotBeFound("id.greaterThan=" + id);

        defaultSuperDivisionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSuperDivisionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSuperDivisionsByNomFrIsEqualToSomething() throws Exception {
        // Initialize the database
        superDivisionRepository.saveAndFlush(superDivision);

        // Get all the superDivisionList where nomFr equals to DEFAULT_NOM_FR
        defaultSuperDivisionShouldBeFound("nomFr.equals=" + DEFAULT_NOM_FR);

        // Get all the superDivisionList where nomFr equals to UPDATED_NOM_FR
        defaultSuperDivisionShouldNotBeFound("nomFr.equals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSuperDivisionsByNomFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        superDivisionRepository.saveAndFlush(superDivision);

        // Get all the superDivisionList where nomFr not equals to DEFAULT_NOM_FR
        defaultSuperDivisionShouldNotBeFound("nomFr.notEquals=" + DEFAULT_NOM_FR);

        // Get all the superDivisionList where nomFr not equals to UPDATED_NOM_FR
        defaultSuperDivisionShouldBeFound("nomFr.notEquals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSuperDivisionsByNomFrIsInShouldWork() throws Exception {
        // Initialize the database
        superDivisionRepository.saveAndFlush(superDivision);

        // Get all the superDivisionList where nomFr in DEFAULT_NOM_FR or UPDATED_NOM_FR
        defaultSuperDivisionShouldBeFound("nomFr.in=" + DEFAULT_NOM_FR + "," + UPDATED_NOM_FR);

        // Get all the superDivisionList where nomFr equals to UPDATED_NOM_FR
        defaultSuperDivisionShouldNotBeFound("nomFr.in=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSuperDivisionsByNomFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        superDivisionRepository.saveAndFlush(superDivision);

        // Get all the superDivisionList where nomFr is not null
        defaultSuperDivisionShouldBeFound("nomFr.specified=true");

        // Get all the superDivisionList where nomFr is null
        defaultSuperDivisionShouldNotBeFound("nomFr.specified=false");
    }

    @Test
    @Transactional
    void getAllSuperDivisionsByNomFrContainsSomething() throws Exception {
        // Initialize the database
        superDivisionRepository.saveAndFlush(superDivision);

        // Get all the superDivisionList where nomFr contains DEFAULT_NOM_FR
        defaultSuperDivisionShouldBeFound("nomFr.contains=" + DEFAULT_NOM_FR);

        // Get all the superDivisionList where nomFr contains UPDATED_NOM_FR
        defaultSuperDivisionShouldNotBeFound("nomFr.contains=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSuperDivisionsByNomFrNotContainsSomething() throws Exception {
        // Initialize the database
        superDivisionRepository.saveAndFlush(superDivision);

        // Get all the superDivisionList where nomFr does not contain DEFAULT_NOM_FR
        defaultSuperDivisionShouldNotBeFound("nomFr.doesNotContain=" + DEFAULT_NOM_FR);

        // Get all the superDivisionList where nomFr does not contain UPDATED_NOM_FR
        defaultSuperDivisionShouldBeFound("nomFr.doesNotContain=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSuperDivisionsByNomLatinIsEqualToSomething() throws Exception {
        // Initialize the database
        superDivisionRepository.saveAndFlush(superDivision);

        // Get all the superDivisionList where nomLatin equals to DEFAULT_NOM_LATIN
        defaultSuperDivisionShouldBeFound("nomLatin.equals=" + DEFAULT_NOM_LATIN);

        // Get all the superDivisionList where nomLatin equals to UPDATED_NOM_LATIN
        defaultSuperDivisionShouldNotBeFound("nomLatin.equals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSuperDivisionsByNomLatinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        superDivisionRepository.saveAndFlush(superDivision);

        // Get all the superDivisionList where nomLatin not equals to DEFAULT_NOM_LATIN
        defaultSuperDivisionShouldNotBeFound("nomLatin.notEquals=" + DEFAULT_NOM_LATIN);

        // Get all the superDivisionList where nomLatin not equals to UPDATED_NOM_LATIN
        defaultSuperDivisionShouldBeFound("nomLatin.notEquals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSuperDivisionsByNomLatinIsInShouldWork() throws Exception {
        // Initialize the database
        superDivisionRepository.saveAndFlush(superDivision);

        // Get all the superDivisionList where nomLatin in DEFAULT_NOM_LATIN or UPDATED_NOM_LATIN
        defaultSuperDivisionShouldBeFound("nomLatin.in=" + DEFAULT_NOM_LATIN + "," + UPDATED_NOM_LATIN);

        // Get all the superDivisionList where nomLatin equals to UPDATED_NOM_LATIN
        defaultSuperDivisionShouldNotBeFound("nomLatin.in=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSuperDivisionsByNomLatinIsNullOrNotNull() throws Exception {
        // Initialize the database
        superDivisionRepository.saveAndFlush(superDivision);

        // Get all the superDivisionList where nomLatin is not null
        defaultSuperDivisionShouldBeFound("nomLatin.specified=true");

        // Get all the superDivisionList where nomLatin is null
        defaultSuperDivisionShouldNotBeFound("nomLatin.specified=false");
    }

    @Test
    @Transactional
    void getAllSuperDivisionsByNomLatinContainsSomething() throws Exception {
        // Initialize the database
        superDivisionRepository.saveAndFlush(superDivision);

        // Get all the superDivisionList where nomLatin contains DEFAULT_NOM_LATIN
        defaultSuperDivisionShouldBeFound("nomLatin.contains=" + DEFAULT_NOM_LATIN);

        // Get all the superDivisionList where nomLatin contains UPDATED_NOM_LATIN
        defaultSuperDivisionShouldNotBeFound("nomLatin.contains=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSuperDivisionsByNomLatinNotContainsSomething() throws Exception {
        // Initialize the database
        superDivisionRepository.saveAndFlush(superDivision);

        // Get all the superDivisionList where nomLatin does not contain DEFAULT_NOM_LATIN
        defaultSuperDivisionShouldNotBeFound("nomLatin.doesNotContain=" + DEFAULT_NOM_LATIN);

        // Get all the superDivisionList where nomLatin does not contain UPDATED_NOM_LATIN
        defaultSuperDivisionShouldBeFound("nomLatin.doesNotContain=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSuperDivisionsByDivisionsIsEqualToSomething() throws Exception {
        // Initialize the database
        superDivisionRepository.saveAndFlush(superDivision);
        Division divisions;
        if (TestUtil.findAll(em, Division.class).isEmpty()) {
            divisions = DivisionResourceIT.createEntity(em);
            em.persist(divisions);
            em.flush();
        } else {
            divisions = TestUtil.findAll(em, Division.class).get(0);
        }
        em.persist(divisions);
        em.flush();
        superDivision.addDivisions(divisions);
        superDivisionRepository.saveAndFlush(superDivision);
        Long divisionsId = divisions.getId();

        // Get all the superDivisionList where divisions equals to divisionsId
        defaultSuperDivisionShouldBeFound("divisionsId.equals=" + divisionsId);

        // Get all the superDivisionList where divisions equals to (divisionsId + 1)
        defaultSuperDivisionShouldNotBeFound("divisionsId.equals=" + (divisionsId + 1));
    }

    @Test
    @Transactional
    void getAllSuperDivisionsBySynonymesIsEqualToSomething() throws Exception {
        // Initialize the database
        superDivisionRepository.saveAndFlush(superDivision);
        SuperDivision synonymes;
        if (TestUtil.findAll(em, SuperDivision.class).isEmpty()) {
            synonymes = SuperDivisionResourceIT.createEntity(em);
            em.persist(synonymes);
            em.flush();
        } else {
            synonymes = TestUtil.findAll(em, SuperDivision.class).get(0);
        }
        em.persist(synonymes);
        em.flush();
        superDivision.addSynonymes(synonymes);
        superDivisionRepository.saveAndFlush(superDivision);
        Long synonymesId = synonymes.getId();

        // Get all the superDivisionList where synonymes equals to synonymesId
        defaultSuperDivisionShouldBeFound("synonymesId.equals=" + synonymesId);

        // Get all the superDivisionList where synonymes equals to (synonymesId + 1)
        defaultSuperDivisionShouldNotBeFound("synonymesId.equals=" + (synonymesId + 1));
    }

    @Test
    @Transactional
    void getAllSuperDivisionsByInfraRegneIsEqualToSomething() throws Exception {
        // Initialize the database
        superDivisionRepository.saveAndFlush(superDivision);
        InfraRegne infraRegne;
        if (TestUtil.findAll(em, InfraRegne.class).isEmpty()) {
            infraRegne = InfraRegneResourceIT.createEntity(em);
            em.persist(infraRegne);
            em.flush();
        } else {
            infraRegne = TestUtil.findAll(em, InfraRegne.class).get(0);
        }
        em.persist(infraRegne);
        em.flush();
        superDivision.setInfraRegne(infraRegne);
        superDivisionRepository.saveAndFlush(superDivision);
        Long infraRegneId = infraRegne.getId();

        // Get all the superDivisionList where infraRegne equals to infraRegneId
        defaultSuperDivisionShouldBeFound("infraRegneId.equals=" + infraRegneId);

        // Get all the superDivisionList where infraRegne equals to (infraRegneId + 1)
        defaultSuperDivisionShouldNotBeFound("infraRegneId.equals=" + (infraRegneId + 1));
    }

    @Test
    @Transactional
    void getAllSuperDivisionsBySuperDivisionIsEqualToSomething() throws Exception {
        // Initialize the database
        superDivisionRepository.saveAndFlush(superDivision);
        SuperDivision superDivision;
        if (TestUtil.findAll(em, SuperDivision.class).isEmpty()) {
            superDivision = SuperDivisionResourceIT.createEntity(em);
            em.persist(superDivision);
            em.flush();
        } else {
            superDivision = TestUtil.findAll(em, SuperDivision.class).get(0);
        }
        em.persist(superDivision);
        em.flush();
        superDivision.setSuperDivision(superDivision);
        superDivisionRepository.saveAndFlush(superDivision);
        Long superDivisionId = superDivision.getId();

        // Get all the superDivisionList where superDivision equals to superDivisionId
        defaultSuperDivisionShouldBeFound("superDivisionId.equals=" + superDivisionId);

        // Get all the superDivisionList where superDivision equals to (superDivisionId + 1)
        defaultSuperDivisionShouldNotBeFound("superDivisionId.equals=" + (superDivisionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSuperDivisionShouldBeFound(String filter) throws Exception {
        restSuperDivisionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(superDivision.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));

        // Check, that the count call also returns 1
        restSuperDivisionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSuperDivisionShouldNotBeFound(String filter) throws Exception {
        restSuperDivisionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSuperDivisionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSuperDivision() throws Exception {
        // Get the superDivision
        restSuperDivisionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSuperDivision() throws Exception {
        // Initialize the database
        superDivisionRepository.saveAndFlush(superDivision);

        int databaseSizeBeforeUpdate = superDivisionRepository.findAll().size();

        // Update the superDivision
        SuperDivision updatedSuperDivision = superDivisionRepository.findById(superDivision.getId()).get();
        // Disconnect from session so that the updates on updatedSuperDivision are not directly saved in db
        em.detach(updatedSuperDivision);
        updatedSuperDivision.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restSuperDivisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSuperDivision.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSuperDivision))
            )
            .andExpect(status().isOk());

        // Validate the SuperDivision in the database
        List<SuperDivision> superDivisionList = superDivisionRepository.findAll();
        assertThat(superDivisionList).hasSize(databaseSizeBeforeUpdate);
        SuperDivision testSuperDivision = superDivisionList.get(superDivisionList.size() - 1);
        assertThat(testSuperDivision.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testSuperDivision.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void putNonExistingSuperDivision() throws Exception {
        int databaseSizeBeforeUpdate = superDivisionRepository.findAll().size();
        superDivision.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSuperDivisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, superDivision.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(superDivision))
            )
            .andExpect(status().isBadRequest());

        // Validate the SuperDivision in the database
        List<SuperDivision> superDivisionList = superDivisionRepository.findAll();
        assertThat(superDivisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSuperDivision() throws Exception {
        int databaseSizeBeforeUpdate = superDivisionRepository.findAll().size();
        superDivision.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuperDivisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(superDivision))
            )
            .andExpect(status().isBadRequest());

        // Validate the SuperDivision in the database
        List<SuperDivision> superDivisionList = superDivisionRepository.findAll();
        assertThat(superDivisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSuperDivision() throws Exception {
        int databaseSizeBeforeUpdate = superDivisionRepository.findAll().size();
        superDivision.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuperDivisionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(superDivision)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SuperDivision in the database
        List<SuperDivision> superDivisionList = superDivisionRepository.findAll();
        assertThat(superDivisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSuperDivisionWithPatch() throws Exception {
        // Initialize the database
        superDivisionRepository.saveAndFlush(superDivision);

        int databaseSizeBeforeUpdate = superDivisionRepository.findAll().size();

        // Update the superDivision using partial update
        SuperDivision partialUpdatedSuperDivision = new SuperDivision();
        partialUpdatedSuperDivision.setId(superDivision.getId());

        partialUpdatedSuperDivision.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restSuperDivisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSuperDivision.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSuperDivision))
            )
            .andExpect(status().isOk());

        // Validate the SuperDivision in the database
        List<SuperDivision> superDivisionList = superDivisionRepository.findAll();
        assertThat(superDivisionList).hasSize(databaseSizeBeforeUpdate);
        SuperDivision testSuperDivision = superDivisionList.get(superDivisionList.size() - 1);
        assertThat(testSuperDivision.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testSuperDivision.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void fullUpdateSuperDivisionWithPatch() throws Exception {
        // Initialize the database
        superDivisionRepository.saveAndFlush(superDivision);

        int databaseSizeBeforeUpdate = superDivisionRepository.findAll().size();

        // Update the superDivision using partial update
        SuperDivision partialUpdatedSuperDivision = new SuperDivision();
        partialUpdatedSuperDivision.setId(superDivision.getId());

        partialUpdatedSuperDivision.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restSuperDivisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSuperDivision.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSuperDivision))
            )
            .andExpect(status().isOk());

        // Validate the SuperDivision in the database
        List<SuperDivision> superDivisionList = superDivisionRepository.findAll();
        assertThat(superDivisionList).hasSize(databaseSizeBeforeUpdate);
        SuperDivision testSuperDivision = superDivisionList.get(superDivisionList.size() - 1);
        assertThat(testSuperDivision.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testSuperDivision.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void patchNonExistingSuperDivision() throws Exception {
        int databaseSizeBeforeUpdate = superDivisionRepository.findAll().size();
        superDivision.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSuperDivisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, superDivision.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(superDivision))
            )
            .andExpect(status().isBadRequest());

        // Validate the SuperDivision in the database
        List<SuperDivision> superDivisionList = superDivisionRepository.findAll();
        assertThat(superDivisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSuperDivision() throws Exception {
        int databaseSizeBeforeUpdate = superDivisionRepository.findAll().size();
        superDivision.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuperDivisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(superDivision))
            )
            .andExpect(status().isBadRequest());

        // Validate the SuperDivision in the database
        List<SuperDivision> superDivisionList = superDivisionRepository.findAll();
        assertThat(superDivisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSuperDivision() throws Exception {
        int databaseSizeBeforeUpdate = superDivisionRepository.findAll().size();
        superDivision.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuperDivisionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(superDivision))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SuperDivision in the database
        List<SuperDivision> superDivisionList = superDivisionRepository.findAll();
        assertThat(superDivisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSuperDivision() throws Exception {
        // Initialize the database
        superDivisionRepository.saveAndFlush(superDivision);

        int databaseSizeBeforeDelete = superDivisionRepository.findAll().size();

        // Delete the superDivision
        restSuperDivisionMockMvc
            .perform(delete(ENTITY_API_URL_ID, superDivision.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SuperDivision> superDivisionList = superDivisionRepository.findAll();
        assertThat(superDivisionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
