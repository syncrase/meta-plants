package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.Regne;
import fr.syncrase.ecosyst.domain.SuperRegne;
import fr.syncrase.ecosyst.domain.SuperRegne;
import fr.syncrase.ecosyst.repository.SuperRegneRepository;
import fr.syncrase.ecosyst.service.criteria.SuperRegneCriteria;
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
 * Integration tests for the {@link SuperRegneResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SuperRegneResourceIT {

    private static final String DEFAULT_NOM_FR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_LATIN = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LATIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/super-regnes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SuperRegneRepository superRegneRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSuperRegneMockMvc;

    private SuperRegne superRegne;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SuperRegne createEntity(EntityManager em) {
        SuperRegne superRegne = new SuperRegne().nomFr(DEFAULT_NOM_FR).nomLatin(DEFAULT_NOM_LATIN);
        return superRegne;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SuperRegne createUpdatedEntity(EntityManager em) {
        SuperRegne superRegne = new SuperRegne().nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);
        return superRegne;
    }

    @BeforeEach
    public void initTest() {
        superRegne = createEntity(em);
    }

    @Test
    @Transactional
    void createSuperRegne() throws Exception {
        int databaseSizeBeforeCreate = superRegneRepository.findAll().size();
        // Create the SuperRegne
        restSuperRegneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(superRegne)))
            .andExpect(status().isCreated());

        // Validate the SuperRegne in the database
        List<SuperRegne> superRegneList = superRegneRepository.findAll();
        assertThat(superRegneList).hasSize(databaseSizeBeforeCreate + 1);
        SuperRegne testSuperRegne = superRegneList.get(superRegneList.size() - 1);
        assertThat(testSuperRegne.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testSuperRegne.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void createSuperRegneWithExistingId() throws Exception {
        // Create the SuperRegne with an existing ID
        superRegne.setId(1L);

        int databaseSizeBeforeCreate = superRegneRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSuperRegneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(superRegne)))
            .andExpect(status().isBadRequest());

        // Validate the SuperRegne in the database
        List<SuperRegne> superRegneList = superRegneRepository.findAll();
        assertThat(superRegneList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomFrIsRequired() throws Exception {
        int databaseSizeBeforeTest = superRegneRepository.findAll().size();
        // set the field null
        superRegne.setNomFr(null);

        // Create the SuperRegne, which fails.

        restSuperRegneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(superRegne)))
            .andExpect(status().isBadRequest());

        List<SuperRegne> superRegneList = superRegneRepository.findAll();
        assertThat(superRegneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSuperRegnes() throws Exception {
        // Initialize the database
        superRegneRepository.saveAndFlush(superRegne);

        // Get all the superRegneList
        restSuperRegneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(superRegne.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));
    }

    @Test
    @Transactional
    void getSuperRegne() throws Exception {
        // Initialize the database
        superRegneRepository.saveAndFlush(superRegne);

        // Get the superRegne
        restSuperRegneMockMvc
            .perform(get(ENTITY_API_URL_ID, superRegne.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(superRegne.getId().intValue()))
            .andExpect(jsonPath("$.nomFr").value(DEFAULT_NOM_FR))
            .andExpect(jsonPath("$.nomLatin").value(DEFAULT_NOM_LATIN));
    }

    @Test
    @Transactional
    void getSuperRegnesByIdFiltering() throws Exception {
        // Initialize the database
        superRegneRepository.saveAndFlush(superRegne);

        Long id = superRegne.getId();

        defaultSuperRegneShouldBeFound("id.equals=" + id);
        defaultSuperRegneShouldNotBeFound("id.notEquals=" + id);

        defaultSuperRegneShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSuperRegneShouldNotBeFound("id.greaterThan=" + id);

        defaultSuperRegneShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSuperRegneShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSuperRegnesByNomFrIsEqualToSomething() throws Exception {
        // Initialize the database
        superRegneRepository.saveAndFlush(superRegne);

        // Get all the superRegneList where nomFr equals to DEFAULT_NOM_FR
        defaultSuperRegneShouldBeFound("nomFr.equals=" + DEFAULT_NOM_FR);

        // Get all the superRegneList where nomFr equals to UPDATED_NOM_FR
        defaultSuperRegneShouldNotBeFound("nomFr.equals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSuperRegnesByNomFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        superRegneRepository.saveAndFlush(superRegne);

        // Get all the superRegneList where nomFr not equals to DEFAULT_NOM_FR
        defaultSuperRegneShouldNotBeFound("nomFr.notEquals=" + DEFAULT_NOM_FR);

        // Get all the superRegneList where nomFr not equals to UPDATED_NOM_FR
        defaultSuperRegneShouldBeFound("nomFr.notEquals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSuperRegnesByNomFrIsInShouldWork() throws Exception {
        // Initialize the database
        superRegneRepository.saveAndFlush(superRegne);

        // Get all the superRegneList where nomFr in DEFAULT_NOM_FR or UPDATED_NOM_FR
        defaultSuperRegneShouldBeFound("nomFr.in=" + DEFAULT_NOM_FR + "," + UPDATED_NOM_FR);

        // Get all the superRegneList where nomFr equals to UPDATED_NOM_FR
        defaultSuperRegneShouldNotBeFound("nomFr.in=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSuperRegnesByNomFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        superRegneRepository.saveAndFlush(superRegne);

        // Get all the superRegneList where nomFr is not null
        defaultSuperRegneShouldBeFound("nomFr.specified=true");

        // Get all the superRegneList where nomFr is null
        defaultSuperRegneShouldNotBeFound("nomFr.specified=false");
    }

    @Test
    @Transactional
    void getAllSuperRegnesByNomFrContainsSomething() throws Exception {
        // Initialize the database
        superRegneRepository.saveAndFlush(superRegne);

        // Get all the superRegneList where nomFr contains DEFAULT_NOM_FR
        defaultSuperRegneShouldBeFound("nomFr.contains=" + DEFAULT_NOM_FR);

        // Get all the superRegneList where nomFr contains UPDATED_NOM_FR
        defaultSuperRegneShouldNotBeFound("nomFr.contains=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSuperRegnesByNomFrNotContainsSomething() throws Exception {
        // Initialize the database
        superRegneRepository.saveAndFlush(superRegne);

        // Get all the superRegneList where nomFr does not contain DEFAULT_NOM_FR
        defaultSuperRegneShouldNotBeFound("nomFr.doesNotContain=" + DEFAULT_NOM_FR);

        // Get all the superRegneList where nomFr does not contain UPDATED_NOM_FR
        defaultSuperRegneShouldBeFound("nomFr.doesNotContain=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSuperRegnesByNomLatinIsEqualToSomething() throws Exception {
        // Initialize the database
        superRegneRepository.saveAndFlush(superRegne);

        // Get all the superRegneList where nomLatin equals to DEFAULT_NOM_LATIN
        defaultSuperRegneShouldBeFound("nomLatin.equals=" + DEFAULT_NOM_LATIN);

        // Get all the superRegneList where nomLatin equals to UPDATED_NOM_LATIN
        defaultSuperRegneShouldNotBeFound("nomLatin.equals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSuperRegnesByNomLatinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        superRegneRepository.saveAndFlush(superRegne);

        // Get all the superRegneList where nomLatin not equals to DEFAULT_NOM_LATIN
        defaultSuperRegneShouldNotBeFound("nomLatin.notEquals=" + DEFAULT_NOM_LATIN);

        // Get all the superRegneList where nomLatin not equals to UPDATED_NOM_LATIN
        defaultSuperRegneShouldBeFound("nomLatin.notEquals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSuperRegnesByNomLatinIsInShouldWork() throws Exception {
        // Initialize the database
        superRegneRepository.saveAndFlush(superRegne);

        // Get all the superRegneList where nomLatin in DEFAULT_NOM_LATIN or UPDATED_NOM_LATIN
        defaultSuperRegneShouldBeFound("nomLatin.in=" + DEFAULT_NOM_LATIN + "," + UPDATED_NOM_LATIN);

        // Get all the superRegneList where nomLatin equals to UPDATED_NOM_LATIN
        defaultSuperRegneShouldNotBeFound("nomLatin.in=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSuperRegnesByNomLatinIsNullOrNotNull() throws Exception {
        // Initialize the database
        superRegneRepository.saveAndFlush(superRegne);

        // Get all the superRegneList where nomLatin is not null
        defaultSuperRegneShouldBeFound("nomLatin.specified=true");

        // Get all the superRegneList where nomLatin is null
        defaultSuperRegneShouldNotBeFound("nomLatin.specified=false");
    }

    @Test
    @Transactional
    void getAllSuperRegnesByNomLatinContainsSomething() throws Exception {
        // Initialize the database
        superRegneRepository.saveAndFlush(superRegne);

        // Get all the superRegneList where nomLatin contains DEFAULT_NOM_LATIN
        defaultSuperRegneShouldBeFound("nomLatin.contains=" + DEFAULT_NOM_LATIN);

        // Get all the superRegneList where nomLatin contains UPDATED_NOM_LATIN
        defaultSuperRegneShouldNotBeFound("nomLatin.contains=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSuperRegnesByNomLatinNotContainsSomething() throws Exception {
        // Initialize the database
        superRegneRepository.saveAndFlush(superRegne);

        // Get all the superRegneList where nomLatin does not contain DEFAULT_NOM_LATIN
        defaultSuperRegneShouldNotBeFound("nomLatin.doesNotContain=" + DEFAULT_NOM_LATIN);

        // Get all the superRegneList where nomLatin does not contain UPDATED_NOM_LATIN
        defaultSuperRegneShouldBeFound("nomLatin.doesNotContain=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSuperRegnesByRegnesIsEqualToSomething() throws Exception {
        // Initialize the database
        superRegneRepository.saveAndFlush(superRegne);
        Regne regnes;
        if (TestUtil.findAll(em, Regne.class).isEmpty()) {
            regnes = RegneResourceIT.createEntity(em);
            em.persist(regnes);
            em.flush();
        } else {
            regnes = TestUtil.findAll(em, Regne.class).get(0);
        }
        em.persist(regnes);
        em.flush();
        superRegne.addRegnes(regnes);
        superRegneRepository.saveAndFlush(superRegne);
        Long regnesId = regnes.getId();

        // Get all the superRegneList where regnes equals to regnesId
        defaultSuperRegneShouldBeFound("regnesId.equals=" + regnesId);

        // Get all the superRegneList where regnes equals to (regnesId + 1)
        defaultSuperRegneShouldNotBeFound("regnesId.equals=" + (regnesId + 1));
    }

    @Test
    @Transactional
    void getAllSuperRegnesBySynonymesIsEqualToSomething() throws Exception {
        // Initialize the database
        superRegneRepository.saveAndFlush(superRegne);
        SuperRegne synonymes;
        if (TestUtil.findAll(em, SuperRegne.class).isEmpty()) {
            synonymes = SuperRegneResourceIT.createEntity(em);
            em.persist(synonymes);
            em.flush();
        } else {
            synonymes = TestUtil.findAll(em, SuperRegne.class).get(0);
        }
        em.persist(synonymes);
        em.flush();
        superRegne.addSynonymes(synonymes);
        superRegneRepository.saveAndFlush(superRegne);
        Long synonymesId = synonymes.getId();

        // Get all the superRegneList where synonymes equals to synonymesId
        defaultSuperRegneShouldBeFound("synonymesId.equals=" + synonymesId);

        // Get all the superRegneList where synonymes equals to (synonymesId + 1)
        defaultSuperRegneShouldNotBeFound("synonymesId.equals=" + (synonymesId + 1));
    }

    @Test
    @Transactional
    void getAllSuperRegnesBySuperRegneIsEqualToSomething() throws Exception {
        // Initialize the database
        superRegneRepository.saveAndFlush(superRegne);
        SuperRegne superRegne;
        if (TestUtil.findAll(em, SuperRegne.class).isEmpty()) {
            superRegne = SuperRegneResourceIT.createEntity(em);
            em.persist(superRegne);
            em.flush();
        } else {
            superRegne = TestUtil.findAll(em, SuperRegne.class).get(0);
        }
        em.persist(superRegne);
        em.flush();
        superRegne.setSuperRegne(superRegne);
        superRegneRepository.saveAndFlush(superRegne);
        Long superRegneId = superRegne.getId();

        // Get all the superRegneList where superRegne equals to superRegneId
        defaultSuperRegneShouldBeFound("superRegneId.equals=" + superRegneId);

        // Get all the superRegneList where superRegne equals to (superRegneId + 1)
        defaultSuperRegneShouldNotBeFound("superRegneId.equals=" + (superRegneId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSuperRegneShouldBeFound(String filter) throws Exception {
        restSuperRegneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(superRegne.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));

        // Check, that the count call also returns 1
        restSuperRegneMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSuperRegneShouldNotBeFound(String filter) throws Exception {
        restSuperRegneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSuperRegneMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSuperRegne() throws Exception {
        // Get the superRegne
        restSuperRegneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSuperRegne() throws Exception {
        // Initialize the database
        superRegneRepository.saveAndFlush(superRegne);

        int databaseSizeBeforeUpdate = superRegneRepository.findAll().size();

        // Update the superRegne
        SuperRegne updatedSuperRegne = superRegneRepository.findById(superRegne.getId()).get();
        // Disconnect from session so that the updates on updatedSuperRegne are not directly saved in db
        em.detach(updatedSuperRegne);
        updatedSuperRegne.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restSuperRegneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSuperRegne.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSuperRegne))
            )
            .andExpect(status().isOk());

        // Validate the SuperRegne in the database
        List<SuperRegne> superRegneList = superRegneRepository.findAll();
        assertThat(superRegneList).hasSize(databaseSizeBeforeUpdate);
        SuperRegne testSuperRegne = superRegneList.get(superRegneList.size() - 1);
        assertThat(testSuperRegne.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testSuperRegne.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void putNonExistingSuperRegne() throws Exception {
        int databaseSizeBeforeUpdate = superRegneRepository.findAll().size();
        superRegne.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSuperRegneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, superRegne.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(superRegne))
            )
            .andExpect(status().isBadRequest());

        // Validate the SuperRegne in the database
        List<SuperRegne> superRegneList = superRegneRepository.findAll();
        assertThat(superRegneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSuperRegne() throws Exception {
        int databaseSizeBeforeUpdate = superRegneRepository.findAll().size();
        superRegne.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuperRegneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(superRegne))
            )
            .andExpect(status().isBadRequest());

        // Validate the SuperRegne in the database
        List<SuperRegne> superRegneList = superRegneRepository.findAll();
        assertThat(superRegneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSuperRegne() throws Exception {
        int databaseSizeBeforeUpdate = superRegneRepository.findAll().size();
        superRegne.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuperRegneMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(superRegne)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SuperRegne in the database
        List<SuperRegne> superRegneList = superRegneRepository.findAll();
        assertThat(superRegneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSuperRegneWithPatch() throws Exception {
        // Initialize the database
        superRegneRepository.saveAndFlush(superRegne);

        int databaseSizeBeforeUpdate = superRegneRepository.findAll().size();

        // Update the superRegne using partial update
        SuperRegne partialUpdatedSuperRegne = new SuperRegne();
        partialUpdatedSuperRegne.setId(superRegne.getId());

        partialUpdatedSuperRegne.nomLatin(UPDATED_NOM_LATIN);

        restSuperRegneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSuperRegne.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSuperRegne))
            )
            .andExpect(status().isOk());

        // Validate the SuperRegne in the database
        List<SuperRegne> superRegneList = superRegneRepository.findAll();
        assertThat(superRegneList).hasSize(databaseSizeBeforeUpdate);
        SuperRegne testSuperRegne = superRegneList.get(superRegneList.size() - 1);
        assertThat(testSuperRegne.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testSuperRegne.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void fullUpdateSuperRegneWithPatch() throws Exception {
        // Initialize the database
        superRegneRepository.saveAndFlush(superRegne);

        int databaseSizeBeforeUpdate = superRegneRepository.findAll().size();

        // Update the superRegne using partial update
        SuperRegne partialUpdatedSuperRegne = new SuperRegne();
        partialUpdatedSuperRegne.setId(superRegne.getId());

        partialUpdatedSuperRegne.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restSuperRegneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSuperRegne.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSuperRegne))
            )
            .andExpect(status().isOk());

        // Validate the SuperRegne in the database
        List<SuperRegne> superRegneList = superRegneRepository.findAll();
        assertThat(superRegneList).hasSize(databaseSizeBeforeUpdate);
        SuperRegne testSuperRegne = superRegneList.get(superRegneList.size() - 1);
        assertThat(testSuperRegne.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testSuperRegne.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void patchNonExistingSuperRegne() throws Exception {
        int databaseSizeBeforeUpdate = superRegneRepository.findAll().size();
        superRegne.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSuperRegneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, superRegne.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(superRegne))
            )
            .andExpect(status().isBadRequest());

        // Validate the SuperRegne in the database
        List<SuperRegne> superRegneList = superRegneRepository.findAll();
        assertThat(superRegneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSuperRegne() throws Exception {
        int databaseSizeBeforeUpdate = superRegneRepository.findAll().size();
        superRegne.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuperRegneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(superRegne))
            )
            .andExpect(status().isBadRequest());

        // Validate the SuperRegne in the database
        List<SuperRegne> superRegneList = superRegneRepository.findAll();
        assertThat(superRegneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSuperRegne() throws Exception {
        int databaseSizeBeforeUpdate = superRegneRepository.findAll().size();
        superRegne.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuperRegneMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(superRegne))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SuperRegne in the database
        List<SuperRegne> superRegneList = superRegneRepository.findAll();
        assertThat(superRegneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSuperRegne() throws Exception {
        // Initialize the database
        superRegneRepository.saveAndFlush(superRegne);

        int databaseSizeBeforeDelete = superRegneRepository.findAll().size();

        // Delete the superRegne
        restSuperRegneMockMvc
            .perform(delete(ENTITY_API_URL_ID, superRegne.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SuperRegne> superRegneList = superRegneRepository.findAll();
        assertThat(superRegneList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
