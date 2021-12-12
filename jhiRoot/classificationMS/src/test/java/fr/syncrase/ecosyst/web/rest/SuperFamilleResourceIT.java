package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.Famille;
import fr.syncrase.ecosyst.domain.MicroOrdre;
import fr.syncrase.ecosyst.domain.SuperFamille;
import fr.syncrase.ecosyst.domain.SuperFamille;
import fr.syncrase.ecosyst.repository.SuperFamilleRepository;
import fr.syncrase.ecosyst.service.criteria.SuperFamilleCriteria;
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
 * Integration tests for the {@link SuperFamilleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SuperFamilleResourceIT {

    private static final String DEFAULT_NOM_FR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_LATIN = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LATIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/super-familles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SuperFamilleRepository superFamilleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSuperFamilleMockMvc;

    private SuperFamille superFamille;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SuperFamille createEntity(EntityManager em) {
        SuperFamille superFamille = new SuperFamille().nomFr(DEFAULT_NOM_FR).nomLatin(DEFAULT_NOM_LATIN);
        return superFamille;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SuperFamille createUpdatedEntity(EntityManager em) {
        SuperFamille superFamille = new SuperFamille().nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);
        return superFamille;
    }

    @BeforeEach
    public void initTest() {
        superFamille = createEntity(em);
    }

    @Test
    @Transactional
    void createSuperFamille() throws Exception {
        int databaseSizeBeforeCreate = superFamilleRepository.findAll().size();
        // Create the SuperFamille
        restSuperFamilleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(superFamille)))
            .andExpect(status().isCreated());

        // Validate the SuperFamille in the database
        List<SuperFamille> superFamilleList = superFamilleRepository.findAll();
        assertThat(superFamilleList).hasSize(databaseSizeBeforeCreate + 1);
        SuperFamille testSuperFamille = superFamilleList.get(superFamilleList.size() - 1);
        assertThat(testSuperFamille.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testSuperFamille.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void createSuperFamilleWithExistingId() throws Exception {
        // Create the SuperFamille with an existing ID
        superFamille.setId(1L);

        int databaseSizeBeforeCreate = superFamilleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSuperFamilleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(superFamille)))
            .andExpect(status().isBadRequest());

        // Validate the SuperFamille in the database
        List<SuperFamille> superFamilleList = superFamilleRepository.findAll();
        assertThat(superFamilleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomFrIsRequired() throws Exception {
        int databaseSizeBeforeTest = superFamilleRepository.findAll().size();
        // set the field null
        superFamille.setNomFr(null);

        // Create the SuperFamille, which fails.

        restSuperFamilleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(superFamille)))
            .andExpect(status().isBadRequest());

        List<SuperFamille> superFamilleList = superFamilleRepository.findAll();
        assertThat(superFamilleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSuperFamilles() throws Exception {
        // Initialize the database
        superFamilleRepository.saveAndFlush(superFamille);

        // Get all the superFamilleList
        restSuperFamilleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(superFamille.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));
    }

    @Test
    @Transactional
    void getSuperFamille() throws Exception {
        // Initialize the database
        superFamilleRepository.saveAndFlush(superFamille);

        // Get the superFamille
        restSuperFamilleMockMvc
            .perform(get(ENTITY_API_URL_ID, superFamille.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(superFamille.getId().intValue()))
            .andExpect(jsonPath("$.nomFr").value(DEFAULT_NOM_FR))
            .andExpect(jsonPath("$.nomLatin").value(DEFAULT_NOM_LATIN));
    }

    @Test
    @Transactional
    void getSuperFamillesByIdFiltering() throws Exception {
        // Initialize the database
        superFamilleRepository.saveAndFlush(superFamille);

        Long id = superFamille.getId();

        defaultSuperFamilleShouldBeFound("id.equals=" + id);
        defaultSuperFamilleShouldNotBeFound("id.notEquals=" + id);

        defaultSuperFamilleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSuperFamilleShouldNotBeFound("id.greaterThan=" + id);

        defaultSuperFamilleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSuperFamilleShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSuperFamillesByNomFrIsEqualToSomething() throws Exception {
        // Initialize the database
        superFamilleRepository.saveAndFlush(superFamille);

        // Get all the superFamilleList where nomFr equals to DEFAULT_NOM_FR
        defaultSuperFamilleShouldBeFound("nomFr.equals=" + DEFAULT_NOM_FR);

        // Get all the superFamilleList where nomFr equals to UPDATED_NOM_FR
        defaultSuperFamilleShouldNotBeFound("nomFr.equals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSuperFamillesByNomFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        superFamilleRepository.saveAndFlush(superFamille);

        // Get all the superFamilleList where nomFr not equals to DEFAULT_NOM_FR
        defaultSuperFamilleShouldNotBeFound("nomFr.notEquals=" + DEFAULT_NOM_FR);

        // Get all the superFamilleList where nomFr not equals to UPDATED_NOM_FR
        defaultSuperFamilleShouldBeFound("nomFr.notEquals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSuperFamillesByNomFrIsInShouldWork() throws Exception {
        // Initialize the database
        superFamilleRepository.saveAndFlush(superFamille);

        // Get all the superFamilleList where nomFr in DEFAULT_NOM_FR or UPDATED_NOM_FR
        defaultSuperFamilleShouldBeFound("nomFr.in=" + DEFAULT_NOM_FR + "," + UPDATED_NOM_FR);

        // Get all the superFamilleList where nomFr equals to UPDATED_NOM_FR
        defaultSuperFamilleShouldNotBeFound("nomFr.in=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSuperFamillesByNomFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        superFamilleRepository.saveAndFlush(superFamille);

        // Get all the superFamilleList where nomFr is not null
        defaultSuperFamilleShouldBeFound("nomFr.specified=true");

        // Get all the superFamilleList where nomFr is null
        defaultSuperFamilleShouldNotBeFound("nomFr.specified=false");
    }

    @Test
    @Transactional
    void getAllSuperFamillesByNomFrContainsSomething() throws Exception {
        // Initialize the database
        superFamilleRepository.saveAndFlush(superFamille);

        // Get all the superFamilleList where nomFr contains DEFAULT_NOM_FR
        defaultSuperFamilleShouldBeFound("nomFr.contains=" + DEFAULT_NOM_FR);

        // Get all the superFamilleList where nomFr contains UPDATED_NOM_FR
        defaultSuperFamilleShouldNotBeFound("nomFr.contains=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSuperFamillesByNomFrNotContainsSomething() throws Exception {
        // Initialize the database
        superFamilleRepository.saveAndFlush(superFamille);

        // Get all the superFamilleList where nomFr does not contain DEFAULT_NOM_FR
        defaultSuperFamilleShouldNotBeFound("nomFr.doesNotContain=" + DEFAULT_NOM_FR);

        // Get all the superFamilleList where nomFr does not contain UPDATED_NOM_FR
        defaultSuperFamilleShouldBeFound("nomFr.doesNotContain=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSuperFamillesByNomLatinIsEqualToSomething() throws Exception {
        // Initialize the database
        superFamilleRepository.saveAndFlush(superFamille);

        // Get all the superFamilleList where nomLatin equals to DEFAULT_NOM_LATIN
        defaultSuperFamilleShouldBeFound("nomLatin.equals=" + DEFAULT_NOM_LATIN);

        // Get all the superFamilleList where nomLatin equals to UPDATED_NOM_LATIN
        defaultSuperFamilleShouldNotBeFound("nomLatin.equals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSuperFamillesByNomLatinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        superFamilleRepository.saveAndFlush(superFamille);

        // Get all the superFamilleList where nomLatin not equals to DEFAULT_NOM_LATIN
        defaultSuperFamilleShouldNotBeFound("nomLatin.notEquals=" + DEFAULT_NOM_LATIN);

        // Get all the superFamilleList where nomLatin not equals to UPDATED_NOM_LATIN
        defaultSuperFamilleShouldBeFound("nomLatin.notEquals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSuperFamillesByNomLatinIsInShouldWork() throws Exception {
        // Initialize the database
        superFamilleRepository.saveAndFlush(superFamille);

        // Get all the superFamilleList where nomLatin in DEFAULT_NOM_LATIN or UPDATED_NOM_LATIN
        defaultSuperFamilleShouldBeFound("nomLatin.in=" + DEFAULT_NOM_LATIN + "," + UPDATED_NOM_LATIN);

        // Get all the superFamilleList where nomLatin equals to UPDATED_NOM_LATIN
        defaultSuperFamilleShouldNotBeFound("nomLatin.in=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSuperFamillesByNomLatinIsNullOrNotNull() throws Exception {
        // Initialize the database
        superFamilleRepository.saveAndFlush(superFamille);

        // Get all the superFamilleList where nomLatin is not null
        defaultSuperFamilleShouldBeFound("nomLatin.specified=true");

        // Get all the superFamilleList where nomLatin is null
        defaultSuperFamilleShouldNotBeFound("nomLatin.specified=false");
    }

    @Test
    @Transactional
    void getAllSuperFamillesByNomLatinContainsSomething() throws Exception {
        // Initialize the database
        superFamilleRepository.saveAndFlush(superFamille);

        // Get all the superFamilleList where nomLatin contains DEFAULT_NOM_LATIN
        defaultSuperFamilleShouldBeFound("nomLatin.contains=" + DEFAULT_NOM_LATIN);

        // Get all the superFamilleList where nomLatin contains UPDATED_NOM_LATIN
        defaultSuperFamilleShouldNotBeFound("nomLatin.contains=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSuperFamillesByNomLatinNotContainsSomething() throws Exception {
        // Initialize the database
        superFamilleRepository.saveAndFlush(superFamille);

        // Get all the superFamilleList where nomLatin does not contain DEFAULT_NOM_LATIN
        defaultSuperFamilleShouldNotBeFound("nomLatin.doesNotContain=" + DEFAULT_NOM_LATIN);

        // Get all the superFamilleList where nomLatin does not contain UPDATED_NOM_LATIN
        defaultSuperFamilleShouldBeFound("nomLatin.doesNotContain=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSuperFamillesByFamillesIsEqualToSomething() throws Exception {
        // Initialize the database
        superFamilleRepository.saveAndFlush(superFamille);
        Famille familles;
        if (TestUtil.findAll(em, Famille.class).isEmpty()) {
            familles = FamilleResourceIT.createEntity(em);
            em.persist(familles);
            em.flush();
        } else {
            familles = TestUtil.findAll(em, Famille.class).get(0);
        }
        em.persist(familles);
        em.flush();
        superFamille.addFamilles(familles);
        superFamilleRepository.saveAndFlush(superFamille);
        Long famillesId = familles.getId();

        // Get all the superFamilleList where familles equals to famillesId
        defaultSuperFamilleShouldBeFound("famillesId.equals=" + famillesId);

        // Get all the superFamilleList where familles equals to (famillesId + 1)
        defaultSuperFamilleShouldNotBeFound("famillesId.equals=" + (famillesId + 1));
    }

    @Test
    @Transactional
    void getAllSuperFamillesBySynonymesIsEqualToSomething() throws Exception {
        // Initialize the database
        superFamilleRepository.saveAndFlush(superFamille);
        SuperFamille synonymes;
        if (TestUtil.findAll(em, SuperFamille.class).isEmpty()) {
            synonymes = SuperFamilleResourceIT.createEntity(em);
            em.persist(synonymes);
            em.flush();
        } else {
            synonymes = TestUtil.findAll(em, SuperFamille.class).get(0);
        }
        em.persist(synonymes);
        em.flush();
        superFamille.addSynonymes(synonymes);
        superFamilleRepository.saveAndFlush(superFamille);
        Long synonymesId = synonymes.getId();

        // Get all the superFamilleList where synonymes equals to synonymesId
        defaultSuperFamilleShouldBeFound("synonymesId.equals=" + synonymesId);

        // Get all the superFamilleList where synonymes equals to (synonymesId + 1)
        defaultSuperFamilleShouldNotBeFound("synonymesId.equals=" + (synonymesId + 1));
    }

    @Test
    @Transactional
    void getAllSuperFamillesByMicroOrdreIsEqualToSomething() throws Exception {
        // Initialize the database
        superFamilleRepository.saveAndFlush(superFamille);
        MicroOrdre microOrdre;
        if (TestUtil.findAll(em, MicroOrdre.class).isEmpty()) {
            microOrdre = MicroOrdreResourceIT.createEntity(em);
            em.persist(microOrdre);
            em.flush();
        } else {
            microOrdre = TestUtil.findAll(em, MicroOrdre.class).get(0);
        }
        em.persist(microOrdre);
        em.flush();
        superFamille.setMicroOrdre(microOrdre);
        superFamilleRepository.saveAndFlush(superFamille);
        Long microOrdreId = microOrdre.getId();

        // Get all the superFamilleList where microOrdre equals to microOrdreId
        defaultSuperFamilleShouldBeFound("microOrdreId.equals=" + microOrdreId);

        // Get all the superFamilleList where microOrdre equals to (microOrdreId + 1)
        defaultSuperFamilleShouldNotBeFound("microOrdreId.equals=" + (microOrdreId + 1));
    }

    @Test
    @Transactional
    void getAllSuperFamillesBySuperFamilleIsEqualToSomething() throws Exception {
        // Initialize the database
        superFamilleRepository.saveAndFlush(superFamille);
        SuperFamille superFamille;
        if (TestUtil.findAll(em, SuperFamille.class).isEmpty()) {
            superFamille = SuperFamilleResourceIT.createEntity(em);
            em.persist(superFamille);
            em.flush();
        } else {
            superFamille = TestUtil.findAll(em, SuperFamille.class).get(0);
        }
        em.persist(superFamille);
        em.flush();
        superFamille.setSuperFamille(superFamille);
        superFamilleRepository.saveAndFlush(superFamille);
        Long superFamilleId = superFamille.getId();

        // Get all the superFamilleList where superFamille equals to superFamilleId
        defaultSuperFamilleShouldBeFound("superFamilleId.equals=" + superFamilleId);

        // Get all the superFamilleList where superFamille equals to (superFamilleId + 1)
        defaultSuperFamilleShouldNotBeFound("superFamilleId.equals=" + (superFamilleId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSuperFamilleShouldBeFound(String filter) throws Exception {
        restSuperFamilleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(superFamille.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));

        // Check, that the count call also returns 1
        restSuperFamilleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSuperFamilleShouldNotBeFound(String filter) throws Exception {
        restSuperFamilleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSuperFamilleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSuperFamille() throws Exception {
        // Get the superFamille
        restSuperFamilleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSuperFamille() throws Exception {
        // Initialize the database
        superFamilleRepository.saveAndFlush(superFamille);

        int databaseSizeBeforeUpdate = superFamilleRepository.findAll().size();

        // Update the superFamille
        SuperFamille updatedSuperFamille = superFamilleRepository.findById(superFamille.getId()).get();
        // Disconnect from session so that the updates on updatedSuperFamille are not directly saved in db
        em.detach(updatedSuperFamille);
        updatedSuperFamille.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restSuperFamilleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSuperFamille.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSuperFamille))
            )
            .andExpect(status().isOk());

        // Validate the SuperFamille in the database
        List<SuperFamille> superFamilleList = superFamilleRepository.findAll();
        assertThat(superFamilleList).hasSize(databaseSizeBeforeUpdate);
        SuperFamille testSuperFamille = superFamilleList.get(superFamilleList.size() - 1);
        assertThat(testSuperFamille.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testSuperFamille.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void putNonExistingSuperFamille() throws Exception {
        int databaseSizeBeforeUpdate = superFamilleRepository.findAll().size();
        superFamille.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSuperFamilleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, superFamille.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(superFamille))
            )
            .andExpect(status().isBadRequest());

        // Validate the SuperFamille in the database
        List<SuperFamille> superFamilleList = superFamilleRepository.findAll();
        assertThat(superFamilleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSuperFamille() throws Exception {
        int databaseSizeBeforeUpdate = superFamilleRepository.findAll().size();
        superFamille.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuperFamilleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(superFamille))
            )
            .andExpect(status().isBadRequest());

        // Validate the SuperFamille in the database
        List<SuperFamille> superFamilleList = superFamilleRepository.findAll();
        assertThat(superFamilleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSuperFamille() throws Exception {
        int databaseSizeBeforeUpdate = superFamilleRepository.findAll().size();
        superFamille.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuperFamilleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(superFamille)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SuperFamille in the database
        List<SuperFamille> superFamilleList = superFamilleRepository.findAll();
        assertThat(superFamilleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSuperFamilleWithPatch() throws Exception {
        // Initialize the database
        superFamilleRepository.saveAndFlush(superFamille);

        int databaseSizeBeforeUpdate = superFamilleRepository.findAll().size();

        // Update the superFamille using partial update
        SuperFamille partialUpdatedSuperFamille = new SuperFamille();
        partialUpdatedSuperFamille.setId(superFamille.getId());

        partialUpdatedSuperFamille.nomLatin(UPDATED_NOM_LATIN);

        restSuperFamilleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSuperFamille.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSuperFamille))
            )
            .andExpect(status().isOk());

        // Validate the SuperFamille in the database
        List<SuperFamille> superFamilleList = superFamilleRepository.findAll();
        assertThat(superFamilleList).hasSize(databaseSizeBeforeUpdate);
        SuperFamille testSuperFamille = superFamilleList.get(superFamilleList.size() - 1);
        assertThat(testSuperFamille.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testSuperFamille.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void fullUpdateSuperFamilleWithPatch() throws Exception {
        // Initialize the database
        superFamilleRepository.saveAndFlush(superFamille);

        int databaseSizeBeforeUpdate = superFamilleRepository.findAll().size();

        // Update the superFamille using partial update
        SuperFamille partialUpdatedSuperFamille = new SuperFamille();
        partialUpdatedSuperFamille.setId(superFamille.getId());

        partialUpdatedSuperFamille.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restSuperFamilleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSuperFamille.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSuperFamille))
            )
            .andExpect(status().isOk());

        // Validate the SuperFamille in the database
        List<SuperFamille> superFamilleList = superFamilleRepository.findAll();
        assertThat(superFamilleList).hasSize(databaseSizeBeforeUpdate);
        SuperFamille testSuperFamille = superFamilleList.get(superFamilleList.size() - 1);
        assertThat(testSuperFamille.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testSuperFamille.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void patchNonExistingSuperFamille() throws Exception {
        int databaseSizeBeforeUpdate = superFamilleRepository.findAll().size();
        superFamille.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSuperFamilleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, superFamille.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(superFamille))
            )
            .andExpect(status().isBadRequest());

        // Validate the SuperFamille in the database
        List<SuperFamille> superFamilleList = superFamilleRepository.findAll();
        assertThat(superFamilleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSuperFamille() throws Exception {
        int databaseSizeBeforeUpdate = superFamilleRepository.findAll().size();
        superFamille.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuperFamilleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(superFamille))
            )
            .andExpect(status().isBadRequest());

        // Validate the SuperFamille in the database
        List<SuperFamille> superFamilleList = superFamilleRepository.findAll();
        assertThat(superFamilleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSuperFamille() throws Exception {
        int databaseSizeBeforeUpdate = superFamilleRepository.findAll().size();
        superFamille.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuperFamilleMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(superFamille))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SuperFamille in the database
        List<SuperFamille> superFamilleList = superFamilleRepository.findAll();
        assertThat(superFamilleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSuperFamille() throws Exception {
        // Initialize the database
        superFamilleRepository.saveAndFlush(superFamille);

        int databaseSizeBeforeDelete = superFamilleRepository.findAll().size();

        // Delete the superFamille
        restSuperFamilleMockMvc
            .perform(delete(ENTITY_API_URL_ID, superFamille.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SuperFamille> superFamilleList = superFamilleRepository.findAll();
        assertThat(superFamilleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
