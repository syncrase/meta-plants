package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.InfraRegne;
import fr.syncrase.ecosyst.domain.Rameau;
import fr.syncrase.ecosyst.domain.Rameau;
import fr.syncrase.ecosyst.domain.SousRegne;
import fr.syncrase.ecosyst.repository.RameauRepository;
import fr.syncrase.ecosyst.service.criteria.RameauCriteria;
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
 * Integration tests for the {@link RameauResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RameauResourceIT {

    private static final String DEFAULT_NOM_FR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_LATIN = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LATIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/rameaus";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RameauRepository rameauRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRameauMockMvc;

    private Rameau rameau;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rameau createEntity(EntityManager em) {
        Rameau rameau = new Rameau().nomFr(DEFAULT_NOM_FR).nomLatin(DEFAULT_NOM_LATIN);
        return rameau;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rameau createUpdatedEntity(EntityManager em) {
        Rameau rameau = new Rameau().nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);
        return rameau;
    }

    @BeforeEach
    public void initTest() {
        rameau = createEntity(em);
    }

    @Test
    @Transactional
    void createRameau() throws Exception {
        int databaseSizeBeforeCreate = rameauRepository.findAll().size();
        // Create the Rameau
        restRameauMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rameau)))
            .andExpect(status().isCreated());

        // Validate the Rameau in the database
        List<Rameau> rameauList = rameauRepository.findAll();
        assertThat(rameauList).hasSize(databaseSizeBeforeCreate + 1);
        Rameau testRameau = rameauList.get(rameauList.size() - 1);
        assertThat(testRameau.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testRameau.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void createRameauWithExistingId() throws Exception {
        // Create the Rameau with an existing ID
        rameau.setId(1L);

        int databaseSizeBeforeCreate = rameauRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRameauMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rameau)))
            .andExpect(status().isBadRequest());

        // Validate the Rameau in the database
        List<Rameau> rameauList = rameauRepository.findAll();
        assertThat(rameauList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomFrIsRequired() throws Exception {
        int databaseSizeBeforeTest = rameauRepository.findAll().size();
        // set the field null
        rameau.setNomFr(null);

        // Create the Rameau, which fails.

        restRameauMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rameau)))
            .andExpect(status().isBadRequest());

        List<Rameau> rameauList = rameauRepository.findAll();
        assertThat(rameauList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRameaus() throws Exception {
        // Initialize the database
        rameauRepository.saveAndFlush(rameau);

        // Get all the rameauList
        restRameauMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rameau.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));
    }

    @Test
    @Transactional
    void getRameau() throws Exception {
        // Initialize the database
        rameauRepository.saveAndFlush(rameau);

        // Get the rameau
        restRameauMockMvc
            .perform(get(ENTITY_API_URL_ID, rameau.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rameau.getId().intValue()))
            .andExpect(jsonPath("$.nomFr").value(DEFAULT_NOM_FR))
            .andExpect(jsonPath("$.nomLatin").value(DEFAULT_NOM_LATIN));
    }

    @Test
    @Transactional
    void getRameausByIdFiltering() throws Exception {
        // Initialize the database
        rameauRepository.saveAndFlush(rameau);

        Long id = rameau.getId();

        defaultRameauShouldBeFound("id.equals=" + id);
        defaultRameauShouldNotBeFound("id.notEquals=" + id);

        defaultRameauShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRameauShouldNotBeFound("id.greaterThan=" + id);

        defaultRameauShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRameauShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRameausByNomFrIsEqualToSomething() throws Exception {
        // Initialize the database
        rameauRepository.saveAndFlush(rameau);

        // Get all the rameauList where nomFr equals to DEFAULT_NOM_FR
        defaultRameauShouldBeFound("nomFr.equals=" + DEFAULT_NOM_FR);

        // Get all the rameauList where nomFr equals to UPDATED_NOM_FR
        defaultRameauShouldNotBeFound("nomFr.equals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllRameausByNomFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rameauRepository.saveAndFlush(rameau);

        // Get all the rameauList where nomFr not equals to DEFAULT_NOM_FR
        defaultRameauShouldNotBeFound("nomFr.notEquals=" + DEFAULT_NOM_FR);

        // Get all the rameauList where nomFr not equals to UPDATED_NOM_FR
        defaultRameauShouldBeFound("nomFr.notEquals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllRameausByNomFrIsInShouldWork() throws Exception {
        // Initialize the database
        rameauRepository.saveAndFlush(rameau);

        // Get all the rameauList where nomFr in DEFAULT_NOM_FR or UPDATED_NOM_FR
        defaultRameauShouldBeFound("nomFr.in=" + DEFAULT_NOM_FR + "," + UPDATED_NOM_FR);

        // Get all the rameauList where nomFr equals to UPDATED_NOM_FR
        defaultRameauShouldNotBeFound("nomFr.in=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllRameausByNomFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        rameauRepository.saveAndFlush(rameau);

        // Get all the rameauList where nomFr is not null
        defaultRameauShouldBeFound("nomFr.specified=true");

        // Get all the rameauList where nomFr is null
        defaultRameauShouldNotBeFound("nomFr.specified=false");
    }

    @Test
    @Transactional
    void getAllRameausByNomFrContainsSomething() throws Exception {
        // Initialize the database
        rameauRepository.saveAndFlush(rameau);

        // Get all the rameauList where nomFr contains DEFAULT_NOM_FR
        defaultRameauShouldBeFound("nomFr.contains=" + DEFAULT_NOM_FR);

        // Get all the rameauList where nomFr contains UPDATED_NOM_FR
        defaultRameauShouldNotBeFound("nomFr.contains=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllRameausByNomFrNotContainsSomething() throws Exception {
        // Initialize the database
        rameauRepository.saveAndFlush(rameau);

        // Get all the rameauList where nomFr does not contain DEFAULT_NOM_FR
        defaultRameauShouldNotBeFound("nomFr.doesNotContain=" + DEFAULT_NOM_FR);

        // Get all the rameauList where nomFr does not contain UPDATED_NOM_FR
        defaultRameauShouldBeFound("nomFr.doesNotContain=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllRameausByNomLatinIsEqualToSomething() throws Exception {
        // Initialize the database
        rameauRepository.saveAndFlush(rameau);

        // Get all the rameauList where nomLatin equals to DEFAULT_NOM_LATIN
        defaultRameauShouldBeFound("nomLatin.equals=" + DEFAULT_NOM_LATIN);

        // Get all the rameauList where nomLatin equals to UPDATED_NOM_LATIN
        defaultRameauShouldNotBeFound("nomLatin.equals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllRameausByNomLatinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        rameauRepository.saveAndFlush(rameau);

        // Get all the rameauList where nomLatin not equals to DEFAULT_NOM_LATIN
        defaultRameauShouldNotBeFound("nomLatin.notEquals=" + DEFAULT_NOM_LATIN);

        // Get all the rameauList where nomLatin not equals to UPDATED_NOM_LATIN
        defaultRameauShouldBeFound("nomLatin.notEquals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllRameausByNomLatinIsInShouldWork() throws Exception {
        // Initialize the database
        rameauRepository.saveAndFlush(rameau);

        // Get all the rameauList where nomLatin in DEFAULT_NOM_LATIN or UPDATED_NOM_LATIN
        defaultRameauShouldBeFound("nomLatin.in=" + DEFAULT_NOM_LATIN + "," + UPDATED_NOM_LATIN);

        // Get all the rameauList where nomLatin equals to UPDATED_NOM_LATIN
        defaultRameauShouldNotBeFound("nomLatin.in=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllRameausByNomLatinIsNullOrNotNull() throws Exception {
        // Initialize the database
        rameauRepository.saveAndFlush(rameau);

        // Get all the rameauList where nomLatin is not null
        defaultRameauShouldBeFound("nomLatin.specified=true");

        // Get all the rameauList where nomLatin is null
        defaultRameauShouldNotBeFound("nomLatin.specified=false");
    }

    @Test
    @Transactional
    void getAllRameausByNomLatinContainsSomething() throws Exception {
        // Initialize the database
        rameauRepository.saveAndFlush(rameau);

        // Get all the rameauList where nomLatin contains DEFAULT_NOM_LATIN
        defaultRameauShouldBeFound("nomLatin.contains=" + DEFAULT_NOM_LATIN);

        // Get all the rameauList where nomLatin contains UPDATED_NOM_LATIN
        defaultRameauShouldNotBeFound("nomLatin.contains=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllRameausByNomLatinNotContainsSomething() throws Exception {
        // Initialize the database
        rameauRepository.saveAndFlush(rameau);

        // Get all the rameauList where nomLatin does not contain DEFAULT_NOM_LATIN
        defaultRameauShouldNotBeFound("nomLatin.doesNotContain=" + DEFAULT_NOM_LATIN);

        // Get all the rameauList where nomLatin does not contain UPDATED_NOM_LATIN
        defaultRameauShouldBeFound("nomLatin.doesNotContain=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllRameausByInfraRegnesIsEqualToSomething() throws Exception {
        // Initialize the database
        rameauRepository.saveAndFlush(rameau);
        InfraRegne infraRegnes;
        if (TestUtil.findAll(em, InfraRegne.class).isEmpty()) {
            infraRegnes = InfraRegneResourceIT.createEntity(em);
            em.persist(infraRegnes);
            em.flush();
        } else {
            infraRegnes = TestUtil.findAll(em, InfraRegne.class).get(0);
        }
        em.persist(infraRegnes);
        em.flush();
        rameau.addInfraRegnes(infraRegnes);
        rameauRepository.saveAndFlush(rameau);
        Long infraRegnesId = infraRegnes.getId();

        // Get all the rameauList where infraRegnes equals to infraRegnesId
        defaultRameauShouldBeFound("infraRegnesId.equals=" + infraRegnesId);

        // Get all the rameauList where infraRegnes equals to (infraRegnesId + 1)
        defaultRameauShouldNotBeFound("infraRegnesId.equals=" + (infraRegnesId + 1));
    }

    @Test
    @Transactional
    void getAllRameausBySynonymesIsEqualToSomething() throws Exception {
        // Initialize the database
        rameauRepository.saveAndFlush(rameau);
        Rameau synonymes;
        if (TestUtil.findAll(em, Rameau.class).isEmpty()) {
            synonymes = RameauResourceIT.createEntity(em);
            em.persist(synonymes);
            em.flush();
        } else {
            synonymes = TestUtil.findAll(em, Rameau.class).get(0);
        }
        em.persist(synonymes);
        em.flush();
        rameau.addSynonymes(synonymes);
        rameauRepository.saveAndFlush(rameau);
        Long synonymesId = synonymes.getId();

        // Get all the rameauList where synonymes equals to synonymesId
        defaultRameauShouldBeFound("synonymesId.equals=" + synonymesId);

        // Get all the rameauList where synonymes equals to (synonymesId + 1)
        defaultRameauShouldNotBeFound("synonymesId.equals=" + (synonymesId + 1));
    }

    @Test
    @Transactional
    void getAllRameausBySousRegneIsEqualToSomething() throws Exception {
        // Initialize the database
        rameauRepository.saveAndFlush(rameau);
        SousRegne sousRegne;
        if (TestUtil.findAll(em, SousRegne.class).isEmpty()) {
            sousRegne = SousRegneResourceIT.createEntity(em);
            em.persist(sousRegne);
            em.flush();
        } else {
            sousRegne = TestUtil.findAll(em, SousRegne.class).get(0);
        }
        em.persist(sousRegne);
        em.flush();
        rameau.setSousRegne(sousRegne);
        rameauRepository.saveAndFlush(rameau);
        Long sousRegneId = sousRegne.getId();

        // Get all the rameauList where sousRegne equals to sousRegneId
        defaultRameauShouldBeFound("sousRegneId.equals=" + sousRegneId);

        // Get all the rameauList where sousRegne equals to (sousRegneId + 1)
        defaultRameauShouldNotBeFound("sousRegneId.equals=" + (sousRegneId + 1));
    }

    @Test
    @Transactional
    void getAllRameausByRameauIsEqualToSomething() throws Exception {
        // Initialize the database
        rameauRepository.saveAndFlush(rameau);
        Rameau rameau;
        if (TestUtil.findAll(em, Rameau.class).isEmpty()) {
            rameau = RameauResourceIT.createEntity(em);
            em.persist(rameau);
            em.flush();
        } else {
            rameau = TestUtil.findAll(em, Rameau.class).get(0);
        }
        em.persist(rameau);
        em.flush();
        rameau.setRameau(rameau);
        rameauRepository.saveAndFlush(rameau);
        Long rameauId = rameau.getId();

        // Get all the rameauList where rameau equals to rameauId
        defaultRameauShouldBeFound("rameauId.equals=" + rameauId);

        // Get all the rameauList where rameau equals to (rameauId + 1)
        defaultRameauShouldNotBeFound("rameauId.equals=" + (rameauId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRameauShouldBeFound(String filter) throws Exception {
        restRameauMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rameau.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));

        // Check, that the count call also returns 1
        restRameauMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRameauShouldNotBeFound(String filter) throws Exception {
        restRameauMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRameauMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRameau() throws Exception {
        // Get the rameau
        restRameauMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRameau() throws Exception {
        // Initialize the database
        rameauRepository.saveAndFlush(rameau);

        int databaseSizeBeforeUpdate = rameauRepository.findAll().size();

        // Update the rameau
        Rameau updatedRameau = rameauRepository.findById(rameau.getId()).get();
        // Disconnect from session so that the updates on updatedRameau are not directly saved in db
        em.detach(updatedRameau);
        updatedRameau.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restRameauMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRameau.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRameau))
            )
            .andExpect(status().isOk());

        // Validate the Rameau in the database
        List<Rameau> rameauList = rameauRepository.findAll();
        assertThat(rameauList).hasSize(databaseSizeBeforeUpdate);
        Rameau testRameau = rameauList.get(rameauList.size() - 1);
        assertThat(testRameau.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testRameau.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void putNonExistingRameau() throws Exception {
        int databaseSizeBeforeUpdate = rameauRepository.findAll().size();
        rameau.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRameauMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rameau.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rameau))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rameau in the database
        List<Rameau> rameauList = rameauRepository.findAll();
        assertThat(rameauList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRameau() throws Exception {
        int databaseSizeBeforeUpdate = rameauRepository.findAll().size();
        rameau.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRameauMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rameau))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rameau in the database
        List<Rameau> rameauList = rameauRepository.findAll();
        assertThat(rameauList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRameau() throws Exception {
        int databaseSizeBeforeUpdate = rameauRepository.findAll().size();
        rameau.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRameauMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rameau)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rameau in the database
        List<Rameau> rameauList = rameauRepository.findAll();
        assertThat(rameauList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRameauWithPatch() throws Exception {
        // Initialize the database
        rameauRepository.saveAndFlush(rameau);

        int databaseSizeBeforeUpdate = rameauRepository.findAll().size();

        // Update the rameau using partial update
        Rameau partialUpdatedRameau = new Rameau();
        partialUpdatedRameau.setId(rameau.getId());

        partialUpdatedRameau.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restRameauMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRameau.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRameau))
            )
            .andExpect(status().isOk());

        // Validate the Rameau in the database
        List<Rameau> rameauList = rameauRepository.findAll();
        assertThat(rameauList).hasSize(databaseSizeBeforeUpdate);
        Rameau testRameau = rameauList.get(rameauList.size() - 1);
        assertThat(testRameau.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testRameau.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void fullUpdateRameauWithPatch() throws Exception {
        // Initialize the database
        rameauRepository.saveAndFlush(rameau);

        int databaseSizeBeforeUpdate = rameauRepository.findAll().size();

        // Update the rameau using partial update
        Rameau partialUpdatedRameau = new Rameau();
        partialUpdatedRameau.setId(rameau.getId());

        partialUpdatedRameau.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restRameauMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRameau.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRameau))
            )
            .andExpect(status().isOk());

        // Validate the Rameau in the database
        List<Rameau> rameauList = rameauRepository.findAll();
        assertThat(rameauList).hasSize(databaseSizeBeforeUpdate);
        Rameau testRameau = rameauList.get(rameauList.size() - 1);
        assertThat(testRameau.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testRameau.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void patchNonExistingRameau() throws Exception {
        int databaseSizeBeforeUpdate = rameauRepository.findAll().size();
        rameau.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRameauMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rameau.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rameau))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rameau in the database
        List<Rameau> rameauList = rameauRepository.findAll();
        assertThat(rameauList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRameau() throws Exception {
        int databaseSizeBeforeUpdate = rameauRepository.findAll().size();
        rameau.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRameauMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rameau))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rameau in the database
        List<Rameau> rameauList = rameauRepository.findAll();
        assertThat(rameauList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRameau() throws Exception {
        int databaseSizeBeforeUpdate = rameauRepository.findAll().size();
        rameau.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRameauMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(rameau)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rameau in the database
        List<Rameau> rameauList = rameauRepository.findAll();
        assertThat(rameauList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRameau() throws Exception {
        // Initialize the database
        rameauRepository.saveAndFlush(rameau);

        int databaseSizeBeforeDelete = rameauRepository.findAll().size();

        // Delete the rameau
        restRameauMockMvc
            .perform(delete(ENTITY_API_URL_ID, rameau.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Rameau> rameauList = rameauRepository.findAll();
        assertThat(rameauList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
