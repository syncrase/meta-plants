package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.Famille;
import fr.syncrase.ecosyst.domain.SousFamille;
import fr.syncrase.ecosyst.domain.SousFamille;
import fr.syncrase.ecosyst.domain.Tribu;
import fr.syncrase.ecosyst.repository.SousFamilleRepository;
import fr.syncrase.ecosyst.service.criteria.SousFamilleCriteria;
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
 * Integration tests for the {@link SousFamilleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SousFamilleResourceIT {

    private static final String DEFAULT_NOM_FR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_LATIN = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LATIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sous-familles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SousFamilleRepository sousFamilleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSousFamilleMockMvc;

    private SousFamille sousFamille;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SousFamille createEntity(EntityManager em) {
        SousFamille sousFamille = new SousFamille().nomFr(DEFAULT_NOM_FR).nomLatin(DEFAULT_NOM_LATIN);
        return sousFamille;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SousFamille createUpdatedEntity(EntityManager em) {
        SousFamille sousFamille = new SousFamille().nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);
        return sousFamille;
    }

    @BeforeEach
    public void initTest() {
        sousFamille = createEntity(em);
    }

    @Test
    @Transactional
    void createSousFamille() throws Exception {
        int databaseSizeBeforeCreate = sousFamilleRepository.findAll().size();
        // Create the SousFamille
        restSousFamilleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousFamille)))
            .andExpect(status().isCreated());

        // Validate the SousFamille in the database
        List<SousFamille> sousFamilleList = sousFamilleRepository.findAll();
        assertThat(sousFamilleList).hasSize(databaseSizeBeforeCreate + 1);
        SousFamille testSousFamille = sousFamilleList.get(sousFamilleList.size() - 1);
        assertThat(testSousFamille.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testSousFamille.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void createSousFamilleWithExistingId() throws Exception {
        // Create the SousFamille with an existing ID
        sousFamille.setId(1L);

        int databaseSizeBeforeCreate = sousFamilleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSousFamilleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousFamille)))
            .andExpect(status().isBadRequest());

        // Validate the SousFamille in the database
        List<SousFamille> sousFamilleList = sousFamilleRepository.findAll();
        assertThat(sousFamilleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomFrIsRequired() throws Exception {
        int databaseSizeBeforeTest = sousFamilleRepository.findAll().size();
        // set the field null
        sousFamille.setNomFr(null);

        // Create the SousFamille, which fails.

        restSousFamilleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousFamille)))
            .andExpect(status().isBadRequest());

        List<SousFamille> sousFamilleList = sousFamilleRepository.findAll();
        assertThat(sousFamilleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSousFamilles() throws Exception {
        // Initialize the database
        sousFamilleRepository.saveAndFlush(sousFamille);

        // Get all the sousFamilleList
        restSousFamilleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sousFamille.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));
    }

    @Test
    @Transactional
    void getSousFamille() throws Exception {
        // Initialize the database
        sousFamilleRepository.saveAndFlush(sousFamille);

        // Get the sousFamille
        restSousFamilleMockMvc
            .perform(get(ENTITY_API_URL_ID, sousFamille.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sousFamille.getId().intValue()))
            .andExpect(jsonPath("$.nomFr").value(DEFAULT_NOM_FR))
            .andExpect(jsonPath("$.nomLatin").value(DEFAULT_NOM_LATIN));
    }

    @Test
    @Transactional
    void getSousFamillesByIdFiltering() throws Exception {
        // Initialize the database
        sousFamilleRepository.saveAndFlush(sousFamille);

        Long id = sousFamille.getId();

        defaultSousFamilleShouldBeFound("id.equals=" + id);
        defaultSousFamilleShouldNotBeFound("id.notEquals=" + id);

        defaultSousFamilleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSousFamilleShouldNotBeFound("id.greaterThan=" + id);

        defaultSousFamilleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSousFamilleShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSousFamillesByNomFrIsEqualToSomething() throws Exception {
        // Initialize the database
        sousFamilleRepository.saveAndFlush(sousFamille);

        // Get all the sousFamilleList where nomFr equals to DEFAULT_NOM_FR
        defaultSousFamilleShouldBeFound("nomFr.equals=" + DEFAULT_NOM_FR);

        // Get all the sousFamilleList where nomFr equals to UPDATED_NOM_FR
        defaultSousFamilleShouldNotBeFound("nomFr.equals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousFamillesByNomFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sousFamilleRepository.saveAndFlush(sousFamille);

        // Get all the sousFamilleList where nomFr not equals to DEFAULT_NOM_FR
        defaultSousFamilleShouldNotBeFound("nomFr.notEquals=" + DEFAULT_NOM_FR);

        // Get all the sousFamilleList where nomFr not equals to UPDATED_NOM_FR
        defaultSousFamilleShouldBeFound("nomFr.notEquals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousFamillesByNomFrIsInShouldWork() throws Exception {
        // Initialize the database
        sousFamilleRepository.saveAndFlush(sousFamille);

        // Get all the sousFamilleList where nomFr in DEFAULT_NOM_FR or UPDATED_NOM_FR
        defaultSousFamilleShouldBeFound("nomFr.in=" + DEFAULT_NOM_FR + "," + UPDATED_NOM_FR);

        // Get all the sousFamilleList where nomFr equals to UPDATED_NOM_FR
        defaultSousFamilleShouldNotBeFound("nomFr.in=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousFamillesByNomFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        sousFamilleRepository.saveAndFlush(sousFamille);

        // Get all the sousFamilleList where nomFr is not null
        defaultSousFamilleShouldBeFound("nomFr.specified=true");

        // Get all the sousFamilleList where nomFr is null
        defaultSousFamilleShouldNotBeFound("nomFr.specified=false");
    }

    @Test
    @Transactional
    void getAllSousFamillesByNomFrContainsSomething() throws Exception {
        // Initialize the database
        sousFamilleRepository.saveAndFlush(sousFamille);

        // Get all the sousFamilleList where nomFr contains DEFAULT_NOM_FR
        defaultSousFamilleShouldBeFound("nomFr.contains=" + DEFAULT_NOM_FR);

        // Get all the sousFamilleList where nomFr contains UPDATED_NOM_FR
        defaultSousFamilleShouldNotBeFound("nomFr.contains=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousFamillesByNomFrNotContainsSomething() throws Exception {
        // Initialize the database
        sousFamilleRepository.saveAndFlush(sousFamille);

        // Get all the sousFamilleList where nomFr does not contain DEFAULT_NOM_FR
        defaultSousFamilleShouldNotBeFound("nomFr.doesNotContain=" + DEFAULT_NOM_FR);

        // Get all the sousFamilleList where nomFr does not contain UPDATED_NOM_FR
        defaultSousFamilleShouldBeFound("nomFr.doesNotContain=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousFamillesByNomLatinIsEqualToSomething() throws Exception {
        // Initialize the database
        sousFamilleRepository.saveAndFlush(sousFamille);

        // Get all the sousFamilleList where nomLatin equals to DEFAULT_NOM_LATIN
        defaultSousFamilleShouldBeFound("nomLatin.equals=" + DEFAULT_NOM_LATIN);

        // Get all the sousFamilleList where nomLatin equals to UPDATED_NOM_LATIN
        defaultSousFamilleShouldNotBeFound("nomLatin.equals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousFamillesByNomLatinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sousFamilleRepository.saveAndFlush(sousFamille);

        // Get all the sousFamilleList where nomLatin not equals to DEFAULT_NOM_LATIN
        defaultSousFamilleShouldNotBeFound("nomLatin.notEquals=" + DEFAULT_NOM_LATIN);

        // Get all the sousFamilleList where nomLatin not equals to UPDATED_NOM_LATIN
        defaultSousFamilleShouldBeFound("nomLatin.notEquals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousFamillesByNomLatinIsInShouldWork() throws Exception {
        // Initialize the database
        sousFamilleRepository.saveAndFlush(sousFamille);

        // Get all the sousFamilleList where nomLatin in DEFAULT_NOM_LATIN or UPDATED_NOM_LATIN
        defaultSousFamilleShouldBeFound("nomLatin.in=" + DEFAULT_NOM_LATIN + "," + UPDATED_NOM_LATIN);

        // Get all the sousFamilleList where nomLatin equals to UPDATED_NOM_LATIN
        defaultSousFamilleShouldNotBeFound("nomLatin.in=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousFamillesByNomLatinIsNullOrNotNull() throws Exception {
        // Initialize the database
        sousFamilleRepository.saveAndFlush(sousFamille);

        // Get all the sousFamilleList where nomLatin is not null
        defaultSousFamilleShouldBeFound("nomLatin.specified=true");

        // Get all the sousFamilleList where nomLatin is null
        defaultSousFamilleShouldNotBeFound("nomLatin.specified=false");
    }

    @Test
    @Transactional
    void getAllSousFamillesByNomLatinContainsSomething() throws Exception {
        // Initialize the database
        sousFamilleRepository.saveAndFlush(sousFamille);

        // Get all the sousFamilleList where nomLatin contains DEFAULT_NOM_LATIN
        defaultSousFamilleShouldBeFound("nomLatin.contains=" + DEFAULT_NOM_LATIN);

        // Get all the sousFamilleList where nomLatin contains UPDATED_NOM_LATIN
        defaultSousFamilleShouldNotBeFound("nomLatin.contains=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousFamillesByNomLatinNotContainsSomething() throws Exception {
        // Initialize the database
        sousFamilleRepository.saveAndFlush(sousFamille);

        // Get all the sousFamilleList where nomLatin does not contain DEFAULT_NOM_LATIN
        defaultSousFamilleShouldNotBeFound("nomLatin.doesNotContain=" + DEFAULT_NOM_LATIN);

        // Get all the sousFamilleList where nomLatin does not contain UPDATED_NOM_LATIN
        defaultSousFamilleShouldBeFound("nomLatin.doesNotContain=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousFamillesByTribusIsEqualToSomething() throws Exception {
        // Initialize the database
        sousFamilleRepository.saveAndFlush(sousFamille);
        Tribu tribus;
        if (TestUtil.findAll(em, Tribu.class).isEmpty()) {
            tribus = TribuResourceIT.createEntity(em);
            em.persist(tribus);
            em.flush();
        } else {
            tribus = TestUtil.findAll(em, Tribu.class).get(0);
        }
        em.persist(tribus);
        em.flush();
        sousFamille.addTribus(tribus);
        sousFamilleRepository.saveAndFlush(sousFamille);
        Long tribusId = tribus.getId();

        // Get all the sousFamilleList where tribus equals to tribusId
        defaultSousFamilleShouldBeFound("tribusId.equals=" + tribusId);

        // Get all the sousFamilleList where tribus equals to (tribusId + 1)
        defaultSousFamilleShouldNotBeFound("tribusId.equals=" + (tribusId + 1));
    }

    @Test
    @Transactional
    void getAllSousFamillesBySynonymesIsEqualToSomething() throws Exception {
        // Initialize the database
        sousFamilleRepository.saveAndFlush(sousFamille);
        SousFamille synonymes;
        if (TestUtil.findAll(em, SousFamille.class).isEmpty()) {
            synonymes = SousFamilleResourceIT.createEntity(em);
            em.persist(synonymes);
            em.flush();
        } else {
            synonymes = TestUtil.findAll(em, SousFamille.class).get(0);
        }
        em.persist(synonymes);
        em.flush();
        sousFamille.addSynonymes(synonymes);
        sousFamilleRepository.saveAndFlush(sousFamille);
        Long synonymesId = synonymes.getId();

        // Get all the sousFamilleList where synonymes equals to synonymesId
        defaultSousFamilleShouldBeFound("synonymesId.equals=" + synonymesId);

        // Get all the sousFamilleList where synonymes equals to (synonymesId + 1)
        defaultSousFamilleShouldNotBeFound("synonymesId.equals=" + (synonymesId + 1));
    }

    @Test
    @Transactional
    void getAllSousFamillesByFamilleIsEqualToSomething() throws Exception {
        // Initialize the database
        sousFamilleRepository.saveAndFlush(sousFamille);
        Famille famille;
        if (TestUtil.findAll(em, Famille.class).isEmpty()) {
            famille = FamilleResourceIT.createEntity(em);
            em.persist(famille);
            em.flush();
        } else {
            famille = TestUtil.findAll(em, Famille.class).get(0);
        }
        em.persist(famille);
        em.flush();
        sousFamille.setFamille(famille);
        sousFamilleRepository.saveAndFlush(sousFamille);
        Long familleId = famille.getId();

        // Get all the sousFamilleList where famille equals to familleId
        defaultSousFamilleShouldBeFound("familleId.equals=" + familleId);

        // Get all the sousFamilleList where famille equals to (familleId + 1)
        defaultSousFamilleShouldNotBeFound("familleId.equals=" + (familleId + 1));
    }

    @Test
    @Transactional
    void getAllSousFamillesBySousFamilleIsEqualToSomething() throws Exception {
        // Initialize the database
        sousFamilleRepository.saveAndFlush(sousFamille);
        SousFamille sousFamille;
        if (TestUtil.findAll(em, SousFamille.class).isEmpty()) {
            sousFamille = SousFamilleResourceIT.createEntity(em);
            em.persist(sousFamille);
            em.flush();
        } else {
            sousFamille = TestUtil.findAll(em, SousFamille.class).get(0);
        }
        em.persist(sousFamille);
        em.flush();
        sousFamille.setSousFamille(sousFamille);
        sousFamilleRepository.saveAndFlush(sousFamille);
        Long sousFamilleId = sousFamille.getId();

        // Get all the sousFamilleList where sousFamille equals to sousFamilleId
        defaultSousFamilleShouldBeFound("sousFamilleId.equals=" + sousFamilleId);

        // Get all the sousFamilleList where sousFamille equals to (sousFamilleId + 1)
        defaultSousFamilleShouldNotBeFound("sousFamilleId.equals=" + (sousFamilleId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSousFamilleShouldBeFound(String filter) throws Exception {
        restSousFamilleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sousFamille.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));

        // Check, that the count call also returns 1
        restSousFamilleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSousFamilleShouldNotBeFound(String filter) throws Exception {
        restSousFamilleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSousFamilleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSousFamille() throws Exception {
        // Get the sousFamille
        restSousFamilleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSousFamille() throws Exception {
        // Initialize the database
        sousFamilleRepository.saveAndFlush(sousFamille);

        int databaseSizeBeforeUpdate = sousFamilleRepository.findAll().size();

        // Update the sousFamille
        SousFamille updatedSousFamille = sousFamilleRepository.findById(sousFamille.getId()).get();
        // Disconnect from session so that the updates on updatedSousFamille are not directly saved in db
        em.detach(updatedSousFamille);
        updatedSousFamille.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restSousFamilleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSousFamille.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSousFamille))
            )
            .andExpect(status().isOk());

        // Validate the SousFamille in the database
        List<SousFamille> sousFamilleList = sousFamilleRepository.findAll();
        assertThat(sousFamilleList).hasSize(databaseSizeBeforeUpdate);
        SousFamille testSousFamille = sousFamilleList.get(sousFamilleList.size() - 1);
        assertThat(testSousFamille.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testSousFamille.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void putNonExistingSousFamille() throws Exception {
        int databaseSizeBeforeUpdate = sousFamilleRepository.findAll().size();
        sousFamille.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSousFamilleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sousFamille.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sousFamille))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousFamille in the database
        List<SousFamille> sousFamilleList = sousFamilleRepository.findAll();
        assertThat(sousFamilleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSousFamille() throws Exception {
        int databaseSizeBeforeUpdate = sousFamilleRepository.findAll().size();
        sousFamille.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousFamilleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sousFamille))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousFamille in the database
        List<SousFamille> sousFamilleList = sousFamilleRepository.findAll();
        assertThat(sousFamilleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSousFamille() throws Exception {
        int databaseSizeBeforeUpdate = sousFamilleRepository.findAll().size();
        sousFamille.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousFamilleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousFamille)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SousFamille in the database
        List<SousFamille> sousFamilleList = sousFamilleRepository.findAll();
        assertThat(sousFamilleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSousFamilleWithPatch() throws Exception {
        // Initialize the database
        sousFamilleRepository.saveAndFlush(sousFamille);

        int databaseSizeBeforeUpdate = sousFamilleRepository.findAll().size();

        // Update the sousFamille using partial update
        SousFamille partialUpdatedSousFamille = new SousFamille();
        partialUpdatedSousFamille.setId(sousFamille.getId());

        partialUpdatedSousFamille.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restSousFamilleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSousFamille.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSousFamille))
            )
            .andExpect(status().isOk());

        // Validate the SousFamille in the database
        List<SousFamille> sousFamilleList = sousFamilleRepository.findAll();
        assertThat(sousFamilleList).hasSize(databaseSizeBeforeUpdate);
        SousFamille testSousFamille = sousFamilleList.get(sousFamilleList.size() - 1);
        assertThat(testSousFamille.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testSousFamille.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void fullUpdateSousFamilleWithPatch() throws Exception {
        // Initialize the database
        sousFamilleRepository.saveAndFlush(sousFamille);

        int databaseSizeBeforeUpdate = sousFamilleRepository.findAll().size();

        // Update the sousFamille using partial update
        SousFamille partialUpdatedSousFamille = new SousFamille();
        partialUpdatedSousFamille.setId(sousFamille.getId());

        partialUpdatedSousFamille.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restSousFamilleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSousFamille.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSousFamille))
            )
            .andExpect(status().isOk());

        // Validate the SousFamille in the database
        List<SousFamille> sousFamilleList = sousFamilleRepository.findAll();
        assertThat(sousFamilleList).hasSize(databaseSizeBeforeUpdate);
        SousFamille testSousFamille = sousFamilleList.get(sousFamilleList.size() - 1);
        assertThat(testSousFamille.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testSousFamille.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void patchNonExistingSousFamille() throws Exception {
        int databaseSizeBeforeUpdate = sousFamilleRepository.findAll().size();
        sousFamille.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSousFamilleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sousFamille.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sousFamille))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousFamille in the database
        List<SousFamille> sousFamilleList = sousFamilleRepository.findAll();
        assertThat(sousFamilleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSousFamille() throws Exception {
        int databaseSizeBeforeUpdate = sousFamilleRepository.findAll().size();
        sousFamille.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousFamilleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sousFamille))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousFamille in the database
        List<SousFamille> sousFamilleList = sousFamilleRepository.findAll();
        assertThat(sousFamilleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSousFamille() throws Exception {
        int databaseSizeBeforeUpdate = sousFamilleRepository.findAll().size();
        sousFamille.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousFamilleMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sousFamille))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SousFamille in the database
        List<SousFamille> sousFamilleList = sousFamilleRepository.findAll();
        assertThat(sousFamilleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSousFamille() throws Exception {
        // Initialize the database
        sousFamilleRepository.saveAndFlush(sousFamille);

        int databaseSizeBeforeDelete = sousFamilleRepository.findAll().size();

        // Delete the sousFamille
        restSousFamilleMockMvc
            .perform(delete(ENTITY_API_URL_ID, sousFamille.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SousFamille> sousFamilleList = sousFamilleRepository.findAll();
        assertThat(sousFamilleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
