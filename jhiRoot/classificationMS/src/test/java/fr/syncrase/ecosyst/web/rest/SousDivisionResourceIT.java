package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.Division;
import fr.syncrase.ecosyst.domain.InfraEmbranchement;
import fr.syncrase.ecosyst.domain.SousDivision;
import fr.syncrase.ecosyst.domain.SousDivision;
import fr.syncrase.ecosyst.repository.SousDivisionRepository;
import fr.syncrase.ecosyst.service.criteria.SousDivisionCriteria;
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
 * Integration tests for the {@link SousDivisionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SousDivisionResourceIT {

    private static final String DEFAULT_NOM_FR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_LATIN = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LATIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sous-divisions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SousDivisionRepository sousDivisionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSousDivisionMockMvc;

    private SousDivision sousDivision;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SousDivision createEntity(EntityManager em) {
        SousDivision sousDivision = new SousDivision().nomFr(DEFAULT_NOM_FR).nomLatin(DEFAULT_NOM_LATIN);
        return sousDivision;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SousDivision createUpdatedEntity(EntityManager em) {
        SousDivision sousDivision = new SousDivision().nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);
        return sousDivision;
    }

    @BeforeEach
    public void initTest() {
        sousDivision = createEntity(em);
    }

    @Test
    @Transactional
    void createSousDivision() throws Exception {
        int databaseSizeBeforeCreate = sousDivisionRepository.findAll().size();
        // Create the SousDivision
        restSousDivisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousDivision)))
            .andExpect(status().isCreated());

        // Validate the SousDivision in the database
        List<SousDivision> sousDivisionList = sousDivisionRepository.findAll();
        assertThat(sousDivisionList).hasSize(databaseSizeBeforeCreate + 1);
        SousDivision testSousDivision = sousDivisionList.get(sousDivisionList.size() - 1);
        assertThat(testSousDivision.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testSousDivision.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void createSousDivisionWithExistingId() throws Exception {
        // Create the SousDivision with an existing ID
        sousDivision.setId(1L);

        int databaseSizeBeforeCreate = sousDivisionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSousDivisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousDivision)))
            .andExpect(status().isBadRequest());

        // Validate the SousDivision in the database
        List<SousDivision> sousDivisionList = sousDivisionRepository.findAll();
        assertThat(sousDivisionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomFrIsRequired() throws Exception {
        int databaseSizeBeforeTest = sousDivisionRepository.findAll().size();
        // set the field null
        sousDivision.setNomFr(null);

        // Create the SousDivision, which fails.

        restSousDivisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousDivision)))
            .andExpect(status().isBadRequest());

        List<SousDivision> sousDivisionList = sousDivisionRepository.findAll();
        assertThat(sousDivisionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSousDivisions() throws Exception {
        // Initialize the database
        sousDivisionRepository.saveAndFlush(sousDivision);

        // Get all the sousDivisionList
        restSousDivisionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sousDivision.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));
    }

    @Test
    @Transactional
    void getSousDivision() throws Exception {
        // Initialize the database
        sousDivisionRepository.saveAndFlush(sousDivision);

        // Get the sousDivision
        restSousDivisionMockMvc
            .perform(get(ENTITY_API_URL_ID, sousDivision.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sousDivision.getId().intValue()))
            .andExpect(jsonPath("$.nomFr").value(DEFAULT_NOM_FR))
            .andExpect(jsonPath("$.nomLatin").value(DEFAULT_NOM_LATIN));
    }

    @Test
    @Transactional
    void getSousDivisionsByIdFiltering() throws Exception {
        // Initialize the database
        sousDivisionRepository.saveAndFlush(sousDivision);

        Long id = sousDivision.getId();

        defaultSousDivisionShouldBeFound("id.equals=" + id);
        defaultSousDivisionShouldNotBeFound("id.notEquals=" + id);

        defaultSousDivisionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSousDivisionShouldNotBeFound("id.greaterThan=" + id);

        defaultSousDivisionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSousDivisionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSousDivisionsByNomFrIsEqualToSomething() throws Exception {
        // Initialize the database
        sousDivisionRepository.saveAndFlush(sousDivision);

        // Get all the sousDivisionList where nomFr equals to DEFAULT_NOM_FR
        defaultSousDivisionShouldBeFound("nomFr.equals=" + DEFAULT_NOM_FR);

        // Get all the sousDivisionList where nomFr equals to UPDATED_NOM_FR
        defaultSousDivisionShouldNotBeFound("nomFr.equals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousDivisionsByNomFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sousDivisionRepository.saveAndFlush(sousDivision);

        // Get all the sousDivisionList where nomFr not equals to DEFAULT_NOM_FR
        defaultSousDivisionShouldNotBeFound("nomFr.notEquals=" + DEFAULT_NOM_FR);

        // Get all the sousDivisionList where nomFr not equals to UPDATED_NOM_FR
        defaultSousDivisionShouldBeFound("nomFr.notEquals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousDivisionsByNomFrIsInShouldWork() throws Exception {
        // Initialize the database
        sousDivisionRepository.saveAndFlush(sousDivision);

        // Get all the sousDivisionList where nomFr in DEFAULT_NOM_FR or UPDATED_NOM_FR
        defaultSousDivisionShouldBeFound("nomFr.in=" + DEFAULT_NOM_FR + "," + UPDATED_NOM_FR);

        // Get all the sousDivisionList where nomFr equals to UPDATED_NOM_FR
        defaultSousDivisionShouldNotBeFound("nomFr.in=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousDivisionsByNomFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        sousDivisionRepository.saveAndFlush(sousDivision);

        // Get all the sousDivisionList where nomFr is not null
        defaultSousDivisionShouldBeFound("nomFr.specified=true");

        // Get all the sousDivisionList where nomFr is null
        defaultSousDivisionShouldNotBeFound("nomFr.specified=false");
    }

    @Test
    @Transactional
    void getAllSousDivisionsByNomFrContainsSomething() throws Exception {
        // Initialize the database
        sousDivisionRepository.saveAndFlush(sousDivision);

        // Get all the sousDivisionList where nomFr contains DEFAULT_NOM_FR
        defaultSousDivisionShouldBeFound("nomFr.contains=" + DEFAULT_NOM_FR);

        // Get all the sousDivisionList where nomFr contains UPDATED_NOM_FR
        defaultSousDivisionShouldNotBeFound("nomFr.contains=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousDivisionsByNomFrNotContainsSomething() throws Exception {
        // Initialize the database
        sousDivisionRepository.saveAndFlush(sousDivision);

        // Get all the sousDivisionList where nomFr does not contain DEFAULT_NOM_FR
        defaultSousDivisionShouldNotBeFound("nomFr.doesNotContain=" + DEFAULT_NOM_FR);

        // Get all the sousDivisionList where nomFr does not contain UPDATED_NOM_FR
        defaultSousDivisionShouldBeFound("nomFr.doesNotContain=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousDivisionsByNomLatinIsEqualToSomething() throws Exception {
        // Initialize the database
        sousDivisionRepository.saveAndFlush(sousDivision);

        // Get all the sousDivisionList where nomLatin equals to DEFAULT_NOM_LATIN
        defaultSousDivisionShouldBeFound("nomLatin.equals=" + DEFAULT_NOM_LATIN);

        // Get all the sousDivisionList where nomLatin equals to UPDATED_NOM_LATIN
        defaultSousDivisionShouldNotBeFound("nomLatin.equals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousDivisionsByNomLatinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sousDivisionRepository.saveAndFlush(sousDivision);

        // Get all the sousDivisionList where nomLatin not equals to DEFAULT_NOM_LATIN
        defaultSousDivisionShouldNotBeFound("nomLatin.notEquals=" + DEFAULT_NOM_LATIN);

        // Get all the sousDivisionList where nomLatin not equals to UPDATED_NOM_LATIN
        defaultSousDivisionShouldBeFound("nomLatin.notEquals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousDivisionsByNomLatinIsInShouldWork() throws Exception {
        // Initialize the database
        sousDivisionRepository.saveAndFlush(sousDivision);

        // Get all the sousDivisionList where nomLatin in DEFAULT_NOM_LATIN or UPDATED_NOM_LATIN
        defaultSousDivisionShouldBeFound("nomLatin.in=" + DEFAULT_NOM_LATIN + "," + UPDATED_NOM_LATIN);

        // Get all the sousDivisionList where nomLatin equals to UPDATED_NOM_LATIN
        defaultSousDivisionShouldNotBeFound("nomLatin.in=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousDivisionsByNomLatinIsNullOrNotNull() throws Exception {
        // Initialize the database
        sousDivisionRepository.saveAndFlush(sousDivision);

        // Get all the sousDivisionList where nomLatin is not null
        defaultSousDivisionShouldBeFound("nomLatin.specified=true");

        // Get all the sousDivisionList where nomLatin is null
        defaultSousDivisionShouldNotBeFound("nomLatin.specified=false");
    }

    @Test
    @Transactional
    void getAllSousDivisionsByNomLatinContainsSomething() throws Exception {
        // Initialize the database
        sousDivisionRepository.saveAndFlush(sousDivision);

        // Get all the sousDivisionList where nomLatin contains DEFAULT_NOM_LATIN
        defaultSousDivisionShouldBeFound("nomLatin.contains=" + DEFAULT_NOM_LATIN);

        // Get all the sousDivisionList where nomLatin contains UPDATED_NOM_LATIN
        defaultSousDivisionShouldNotBeFound("nomLatin.contains=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousDivisionsByNomLatinNotContainsSomething() throws Exception {
        // Initialize the database
        sousDivisionRepository.saveAndFlush(sousDivision);

        // Get all the sousDivisionList where nomLatin does not contain DEFAULT_NOM_LATIN
        defaultSousDivisionShouldNotBeFound("nomLatin.doesNotContain=" + DEFAULT_NOM_LATIN);

        // Get all the sousDivisionList where nomLatin does not contain UPDATED_NOM_LATIN
        defaultSousDivisionShouldBeFound("nomLatin.doesNotContain=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousDivisionsByInfraEmbranchementsIsEqualToSomething() throws Exception {
        // Initialize the database
        sousDivisionRepository.saveAndFlush(sousDivision);
        InfraEmbranchement infraEmbranchements;
        if (TestUtil.findAll(em, InfraEmbranchement.class).isEmpty()) {
            infraEmbranchements = InfraEmbranchementResourceIT.createEntity(em);
            em.persist(infraEmbranchements);
            em.flush();
        } else {
            infraEmbranchements = TestUtil.findAll(em, InfraEmbranchement.class).get(0);
        }
        em.persist(infraEmbranchements);
        em.flush();
        sousDivision.addInfraEmbranchements(infraEmbranchements);
        sousDivisionRepository.saveAndFlush(sousDivision);
        Long infraEmbranchementsId = infraEmbranchements.getId();

        // Get all the sousDivisionList where infraEmbranchements equals to infraEmbranchementsId
        defaultSousDivisionShouldBeFound("infraEmbranchementsId.equals=" + infraEmbranchementsId);

        // Get all the sousDivisionList where infraEmbranchements equals to (infraEmbranchementsId + 1)
        defaultSousDivisionShouldNotBeFound("infraEmbranchementsId.equals=" + (infraEmbranchementsId + 1));
    }

    @Test
    @Transactional
    void getAllSousDivisionsBySynonymesIsEqualToSomething() throws Exception {
        // Initialize the database
        sousDivisionRepository.saveAndFlush(sousDivision);
        SousDivision synonymes;
        if (TestUtil.findAll(em, SousDivision.class).isEmpty()) {
            synonymes = SousDivisionResourceIT.createEntity(em);
            em.persist(synonymes);
            em.flush();
        } else {
            synonymes = TestUtil.findAll(em, SousDivision.class).get(0);
        }
        em.persist(synonymes);
        em.flush();
        sousDivision.addSynonymes(synonymes);
        sousDivisionRepository.saveAndFlush(sousDivision);
        Long synonymesId = synonymes.getId();

        // Get all the sousDivisionList where synonymes equals to synonymesId
        defaultSousDivisionShouldBeFound("synonymesId.equals=" + synonymesId);

        // Get all the sousDivisionList where synonymes equals to (synonymesId + 1)
        defaultSousDivisionShouldNotBeFound("synonymesId.equals=" + (synonymesId + 1));
    }

    @Test
    @Transactional
    void getAllSousDivisionsByDivisionIsEqualToSomething() throws Exception {
        // Initialize the database
        sousDivisionRepository.saveAndFlush(sousDivision);
        Division division;
        if (TestUtil.findAll(em, Division.class).isEmpty()) {
            division = DivisionResourceIT.createEntity(em);
            em.persist(division);
            em.flush();
        } else {
            division = TestUtil.findAll(em, Division.class).get(0);
        }
        em.persist(division);
        em.flush();
        sousDivision.setDivision(division);
        sousDivisionRepository.saveAndFlush(sousDivision);
        Long divisionId = division.getId();

        // Get all the sousDivisionList where division equals to divisionId
        defaultSousDivisionShouldBeFound("divisionId.equals=" + divisionId);

        // Get all the sousDivisionList where division equals to (divisionId + 1)
        defaultSousDivisionShouldNotBeFound("divisionId.equals=" + (divisionId + 1));
    }

    @Test
    @Transactional
    void getAllSousDivisionsBySousDivisionIsEqualToSomething() throws Exception {
        // Initialize the database
        sousDivisionRepository.saveAndFlush(sousDivision);
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
        sousDivision.setSousDivision(sousDivision);
        sousDivisionRepository.saveAndFlush(sousDivision);
        Long sousDivisionId = sousDivision.getId();

        // Get all the sousDivisionList where sousDivision equals to sousDivisionId
        defaultSousDivisionShouldBeFound("sousDivisionId.equals=" + sousDivisionId);

        // Get all the sousDivisionList where sousDivision equals to (sousDivisionId + 1)
        defaultSousDivisionShouldNotBeFound("sousDivisionId.equals=" + (sousDivisionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSousDivisionShouldBeFound(String filter) throws Exception {
        restSousDivisionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sousDivision.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));

        // Check, that the count call also returns 1
        restSousDivisionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSousDivisionShouldNotBeFound(String filter) throws Exception {
        restSousDivisionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSousDivisionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSousDivision() throws Exception {
        // Get the sousDivision
        restSousDivisionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSousDivision() throws Exception {
        // Initialize the database
        sousDivisionRepository.saveAndFlush(sousDivision);

        int databaseSizeBeforeUpdate = sousDivisionRepository.findAll().size();

        // Update the sousDivision
        SousDivision updatedSousDivision = sousDivisionRepository.findById(sousDivision.getId()).get();
        // Disconnect from session so that the updates on updatedSousDivision are not directly saved in db
        em.detach(updatedSousDivision);
        updatedSousDivision.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restSousDivisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSousDivision.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSousDivision))
            )
            .andExpect(status().isOk());

        // Validate the SousDivision in the database
        List<SousDivision> sousDivisionList = sousDivisionRepository.findAll();
        assertThat(sousDivisionList).hasSize(databaseSizeBeforeUpdate);
        SousDivision testSousDivision = sousDivisionList.get(sousDivisionList.size() - 1);
        assertThat(testSousDivision.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testSousDivision.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void putNonExistingSousDivision() throws Exception {
        int databaseSizeBeforeUpdate = sousDivisionRepository.findAll().size();
        sousDivision.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSousDivisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sousDivision.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sousDivision))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousDivision in the database
        List<SousDivision> sousDivisionList = sousDivisionRepository.findAll();
        assertThat(sousDivisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSousDivision() throws Exception {
        int databaseSizeBeforeUpdate = sousDivisionRepository.findAll().size();
        sousDivision.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousDivisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sousDivision))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousDivision in the database
        List<SousDivision> sousDivisionList = sousDivisionRepository.findAll();
        assertThat(sousDivisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSousDivision() throws Exception {
        int databaseSizeBeforeUpdate = sousDivisionRepository.findAll().size();
        sousDivision.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousDivisionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousDivision)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SousDivision in the database
        List<SousDivision> sousDivisionList = sousDivisionRepository.findAll();
        assertThat(sousDivisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSousDivisionWithPatch() throws Exception {
        // Initialize the database
        sousDivisionRepository.saveAndFlush(sousDivision);

        int databaseSizeBeforeUpdate = sousDivisionRepository.findAll().size();

        // Update the sousDivision using partial update
        SousDivision partialUpdatedSousDivision = new SousDivision();
        partialUpdatedSousDivision.setId(sousDivision.getId());

        partialUpdatedSousDivision.nomLatin(UPDATED_NOM_LATIN);

        restSousDivisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSousDivision.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSousDivision))
            )
            .andExpect(status().isOk());

        // Validate the SousDivision in the database
        List<SousDivision> sousDivisionList = sousDivisionRepository.findAll();
        assertThat(sousDivisionList).hasSize(databaseSizeBeforeUpdate);
        SousDivision testSousDivision = sousDivisionList.get(sousDivisionList.size() - 1);
        assertThat(testSousDivision.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testSousDivision.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void fullUpdateSousDivisionWithPatch() throws Exception {
        // Initialize the database
        sousDivisionRepository.saveAndFlush(sousDivision);

        int databaseSizeBeforeUpdate = sousDivisionRepository.findAll().size();

        // Update the sousDivision using partial update
        SousDivision partialUpdatedSousDivision = new SousDivision();
        partialUpdatedSousDivision.setId(sousDivision.getId());

        partialUpdatedSousDivision.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restSousDivisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSousDivision.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSousDivision))
            )
            .andExpect(status().isOk());

        // Validate the SousDivision in the database
        List<SousDivision> sousDivisionList = sousDivisionRepository.findAll();
        assertThat(sousDivisionList).hasSize(databaseSizeBeforeUpdate);
        SousDivision testSousDivision = sousDivisionList.get(sousDivisionList.size() - 1);
        assertThat(testSousDivision.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testSousDivision.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void patchNonExistingSousDivision() throws Exception {
        int databaseSizeBeforeUpdate = sousDivisionRepository.findAll().size();
        sousDivision.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSousDivisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sousDivision.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sousDivision))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousDivision in the database
        List<SousDivision> sousDivisionList = sousDivisionRepository.findAll();
        assertThat(sousDivisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSousDivision() throws Exception {
        int databaseSizeBeforeUpdate = sousDivisionRepository.findAll().size();
        sousDivision.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousDivisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sousDivision))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousDivision in the database
        List<SousDivision> sousDivisionList = sousDivisionRepository.findAll();
        assertThat(sousDivisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSousDivision() throws Exception {
        int databaseSizeBeforeUpdate = sousDivisionRepository.findAll().size();
        sousDivision.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousDivisionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sousDivision))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SousDivision in the database
        List<SousDivision> sousDivisionList = sousDivisionRepository.findAll();
        assertThat(sousDivisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSousDivision() throws Exception {
        // Initialize the database
        sousDivisionRepository.saveAndFlush(sousDivision);

        int databaseSizeBeforeDelete = sousDivisionRepository.findAll().size();

        // Delete the sousDivision
        restSousDivisionMockMvc
            .perform(delete(ENTITY_API_URL_ID, sousDivision.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SousDivision> sousDivisionList = sousDivisionRepository.findAll();
        assertThat(sousDivisionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
