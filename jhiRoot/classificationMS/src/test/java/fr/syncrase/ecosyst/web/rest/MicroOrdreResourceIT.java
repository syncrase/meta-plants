package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.InfraOrdre;
import fr.syncrase.ecosyst.domain.MicroOrdre;
import fr.syncrase.ecosyst.domain.MicroOrdre;
import fr.syncrase.ecosyst.domain.SuperFamille;
import fr.syncrase.ecosyst.repository.MicroOrdreRepository;
import fr.syncrase.ecosyst.service.criteria.MicroOrdreCriteria;
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
 * Integration tests for the {@link MicroOrdreResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MicroOrdreResourceIT {

    private static final String DEFAULT_NOM_FR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_LATIN = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LATIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/micro-ordres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MicroOrdreRepository microOrdreRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMicroOrdreMockMvc;

    private MicroOrdre microOrdre;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MicroOrdre createEntity(EntityManager em) {
        MicroOrdre microOrdre = new MicroOrdre().nomFr(DEFAULT_NOM_FR).nomLatin(DEFAULT_NOM_LATIN);
        return microOrdre;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MicroOrdre createUpdatedEntity(EntityManager em) {
        MicroOrdre microOrdre = new MicroOrdre().nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);
        return microOrdre;
    }

    @BeforeEach
    public void initTest() {
        microOrdre = createEntity(em);
    }

    @Test
    @Transactional
    void createMicroOrdre() throws Exception {
        int databaseSizeBeforeCreate = microOrdreRepository.findAll().size();
        // Create the MicroOrdre
        restMicroOrdreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(microOrdre)))
            .andExpect(status().isCreated());

        // Validate the MicroOrdre in the database
        List<MicroOrdre> microOrdreList = microOrdreRepository.findAll();
        assertThat(microOrdreList).hasSize(databaseSizeBeforeCreate + 1);
        MicroOrdre testMicroOrdre = microOrdreList.get(microOrdreList.size() - 1);
        assertThat(testMicroOrdre.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testMicroOrdre.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void createMicroOrdreWithExistingId() throws Exception {
        // Create the MicroOrdre with an existing ID
        microOrdre.setId(1L);

        int databaseSizeBeforeCreate = microOrdreRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMicroOrdreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(microOrdre)))
            .andExpect(status().isBadRequest());

        // Validate the MicroOrdre in the database
        List<MicroOrdre> microOrdreList = microOrdreRepository.findAll();
        assertThat(microOrdreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomFrIsRequired() throws Exception {
        int databaseSizeBeforeTest = microOrdreRepository.findAll().size();
        // set the field null
        microOrdre.setNomFr(null);

        // Create the MicroOrdre, which fails.

        restMicroOrdreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(microOrdre)))
            .andExpect(status().isBadRequest());

        List<MicroOrdre> microOrdreList = microOrdreRepository.findAll();
        assertThat(microOrdreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMicroOrdres() throws Exception {
        // Initialize the database
        microOrdreRepository.saveAndFlush(microOrdre);

        // Get all the microOrdreList
        restMicroOrdreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(microOrdre.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));
    }

    @Test
    @Transactional
    void getMicroOrdre() throws Exception {
        // Initialize the database
        microOrdreRepository.saveAndFlush(microOrdre);

        // Get the microOrdre
        restMicroOrdreMockMvc
            .perform(get(ENTITY_API_URL_ID, microOrdre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(microOrdre.getId().intValue()))
            .andExpect(jsonPath("$.nomFr").value(DEFAULT_NOM_FR))
            .andExpect(jsonPath("$.nomLatin").value(DEFAULT_NOM_LATIN));
    }

    @Test
    @Transactional
    void getMicroOrdresByIdFiltering() throws Exception {
        // Initialize the database
        microOrdreRepository.saveAndFlush(microOrdre);

        Long id = microOrdre.getId();

        defaultMicroOrdreShouldBeFound("id.equals=" + id);
        defaultMicroOrdreShouldNotBeFound("id.notEquals=" + id);

        defaultMicroOrdreShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMicroOrdreShouldNotBeFound("id.greaterThan=" + id);

        defaultMicroOrdreShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMicroOrdreShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMicroOrdresByNomFrIsEqualToSomething() throws Exception {
        // Initialize the database
        microOrdreRepository.saveAndFlush(microOrdre);

        // Get all the microOrdreList where nomFr equals to DEFAULT_NOM_FR
        defaultMicroOrdreShouldBeFound("nomFr.equals=" + DEFAULT_NOM_FR);

        // Get all the microOrdreList where nomFr equals to UPDATED_NOM_FR
        defaultMicroOrdreShouldNotBeFound("nomFr.equals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllMicroOrdresByNomFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        microOrdreRepository.saveAndFlush(microOrdre);

        // Get all the microOrdreList where nomFr not equals to DEFAULT_NOM_FR
        defaultMicroOrdreShouldNotBeFound("nomFr.notEquals=" + DEFAULT_NOM_FR);

        // Get all the microOrdreList where nomFr not equals to UPDATED_NOM_FR
        defaultMicroOrdreShouldBeFound("nomFr.notEquals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllMicroOrdresByNomFrIsInShouldWork() throws Exception {
        // Initialize the database
        microOrdreRepository.saveAndFlush(microOrdre);

        // Get all the microOrdreList where nomFr in DEFAULT_NOM_FR or UPDATED_NOM_FR
        defaultMicroOrdreShouldBeFound("nomFr.in=" + DEFAULT_NOM_FR + "," + UPDATED_NOM_FR);

        // Get all the microOrdreList where nomFr equals to UPDATED_NOM_FR
        defaultMicroOrdreShouldNotBeFound("nomFr.in=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllMicroOrdresByNomFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        microOrdreRepository.saveAndFlush(microOrdre);

        // Get all the microOrdreList where nomFr is not null
        defaultMicroOrdreShouldBeFound("nomFr.specified=true");

        // Get all the microOrdreList where nomFr is null
        defaultMicroOrdreShouldNotBeFound("nomFr.specified=false");
    }

    @Test
    @Transactional
    void getAllMicroOrdresByNomFrContainsSomething() throws Exception {
        // Initialize the database
        microOrdreRepository.saveAndFlush(microOrdre);

        // Get all the microOrdreList where nomFr contains DEFAULT_NOM_FR
        defaultMicroOrdreShouldBeFound("nomFr.contains=" + DEFAULT_NOM_FR);

        // Get all the microOrdreList where nomFr contains UPDATED_NOM_FR
        defaultMicroOrdreShouldNotBeFound("nomFr.contains=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllMicroOrdresByNomFrNotContainsSomething() throws Exception {
        // Initialize the database
        microOrdreRepository.saveAndFlush(microOrdre);

        // Get all the microOrdreList where nomFr does not contain DEFAULT_NOM_FR
        defaultMicroOrdreShouldNotBeFound("nomFr.doesNotContain=" + DEFAULT_NOM_FR);

        // Get all the microOrdreList where nomFr does not contain UPDATED_NOM_FR
        defaultMicroOrdreShouldBeFound("nomFr.doesNotContain=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllMicroOrdresByNomLatinIsEqualToSomething() throws Exception {
        // Initialize the database
        microOrdreRepository.saveAndFlush(microOrdre);

        // Get all the microOrdreList where nomLatin equals to DEFAULT_NOM_LATIN
        defaultMicroOrdreShouldBeFound("nomLatin.equals=" + DEFAULT_NOM_LATIN);

        // Get all the microOrdreList where nomLatin equals to UPDATED_NOM_LATIN
        defaultMicroOrdreShouldNotBeFound("nomLatin.equals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllMicroOrdresByNomLatinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        microOrdreRepository.saveAndFlush(microOrdre);

        // Get all the microOrdreList where nomLatin not equals to DEFAULT_NOM_LATIN
        defaultMicroOrdreShouldNotBeFound("nomLatin.notEquals=" + DEFAULT_NOM_LATIN);

        // Get all the microOrdreList where nomLatin not equals to UPDATED_NOM_LATIN
        defaultMicroOrdreShouldBeFound("nomLatin.notEquals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllMicroOrdresByNomLatinIsInShouldWork() throws Exception {
        // Initialize the database
        microOrdreRepository.saveAndFlush(microOrdre);

        // Get all the microOrdreList where nomLatin in DEFAULT_NOM_LATIN or UPDATED_NOM_LATIN
        defaultMicroOrdreShouldBeFound("nomLatin.in=" + DEFAULT_NOM_LATIN + "," + UPDATED_NOM_LATIN);

        // Get all the microOrdreList where nomLatin equals to UPDATED_NOM_LATIN
        defaultMicroOrdreShouldNotBeFound("nomLatin.in=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllMicroOrdresByNomLatinIsNullOrNotNull() throws Exception {
        // Initialize the database
        microOrdreRepository.saveAndFlush(microOrdre);

        // Get all the microOrdreList where nomLatin is not null
        defaultMicroOrdreShouldBeFound("nomLatin.specified=true");

        // Get all the microOrdreList where nomLatin is null
        defaultMicroOrdreShouldNotBeFound("nomLatin.specified=false");
    }

    @Test
    @Transactional
    void getAllMicroOrdresByNomLatinContainsSomething() throws Exception {
        // Initialize the database
        microOrdreRepository.saveAndFlush(microOrdre);

        // Get all the microOrdreList where nomLatin contains DEFAULT_NOM_LATIN
        defaultMicroOrdreShouldBeFound("nomLatin.contains=" + DEFAULT_NOM_LATIN);

        // Get all the microOrdreList where nomLatin contains UPDATED_NOM_LATIN
        defaultMicroOrdreShouldNotBeFound("nomLatin.contains=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllMicroOrdresByNomLatinNotContainsSomething() throws Exception {
        // Initialize the database
        microOrdreRepository.saveAndFlush(microOrdre);

        // Get all the microOrdreList where nomLatin does not contain DEFAULT_NOM_LATIN
        defaultMicroOrdreShouldNotBeFound("nomLatin.doesNotContain=" + DEFAULT_NOM_LATIN);

        // Get all the microOrdreList where nomLatin does not contain UPDATED_NOM_LATIN
        defaultMicroOrdreShouldBeFound("nomLatin.doesNotContain=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllMicroOrdresBySuperFamillesIsEqualToSomething() throws Exception {
        // Initialize the database
        microOrdreRepository.saveAndFlush(microOrdre);
        SuperFamille superFamilles;
        if (TestUtil.findAll(em, SuperFamille.class).isEmpty()) {
            superFamilles = SuperFamilleResourceIT.createEntity(em);
            em.persist(superFamilles);
            em.flush();
        } else {
            superFamilles = TestUtil.findAll(em, SuperFamille.class).get(0);
        }
        em.persist(superFamilles);
        em.flush();
        microOrdre.addSuperFamilles(superFamilles);
        microOrdreRepository.saveAndFlush(microOrdre);
        Long superFamillesId = superFamilles.getId();

        // Get all the microOrdreList where superFamilles equals to superFamillesId
        defaultMicroOrdreShouldBeFound("superFamillesId.equals=" + superFamillesId);

        // Get all the microOrdreList where superFamilles equals to (superFamillesId + 1)
        defaultMicroOrdreShouldNotBeFound("superFamillesId.equals=" + (superFamillesId + 1));
    }

    @Test
    @Transactional
    void getAllMicroOrdresBySynonymesIsEqualToSomething() throws Exception {
        // Initialize the database
        microOrdreRepository.saveAndFlush(microOrdre);
        MicroOrdre synonymes;
        if (TestUtil.findAll(em, MicroOrdre.class).isEmpty()) {
            synonymes = MicroOrdreResourceIT.createEntity(em);
            em.persist(synonymes);
            em.flush();
        } else {
            synonymes = TestUtil.findAll(em, MicroOrdre.class).get(0);
        }
        em.persist(synonymes);
        em.flush();
        microOrdre.addSynonymes(synonymes);
        microOrdreRepository.saveAndFlush(microOrdre);
        Long synonymesId = synonymes.getId();

        // Get all the microOrdreList where synonymes equals to synonymesId
        defaultMicroOrdreShouldBeFound("synonymesId.equals=" + synonymesId);

        // Get all the microOrdreList where synonymes equals to (synonymesId + 1)
        defaultMicroOrdreShouldNotBeFound("synonymesId.equals=" + (synonymesId + 1));
    }

    @Test
    @Transactional
    void getAllMicroOrdresByInfraOrdreIsEqualToSomething() throws Exception {
        // Initialize the database
        microOrdreRepository.saveAndFlush(microOrdre);
        InfraOrdre infraOrdre;
        if (TestUtil.findAll(em, InfraOrdre.class).isEmpty()) {
            infraOrdre = InfraOrdreResourceIT.createEntity(em);
            em.persist(infraOrdre);
            em.flush();
        } else {
            infraOrdre = TestUtil.findAll(em, InfraOrdre.class).get(0);
        }
        em.persist(infraOrdre);
        em.flush();
        microOrdre.setInfraOrdre(infraOrdre);
        microOrdreRepository.saveAndFlush(microOrdre);
        Long infraOrdreId = infraOrdre.getId();

        // Get all the microOrdreList where infraOrdre equals to infraOrdreId
        defaultMicroOrdreShouldBeFound("infraOrdreId.equals=" + infraOrdreId);

        // Get all the microOrdreList where infraOrdre equals to (infraOrdreId + 1)
        defaultMicroOrdreShouldNotBeFound("infraOrdreId.equals=" + (infraOrdreId + 1));
    }

    @Test
    @Transactional
    void getAllMicroOrdresByMicroOrdreIsEqualToSomething() throws Exception {
        // Initialize the database
        microOrdreRepository.saveAndFlush(microOrdre);
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
        microOrdre.setMicroOrdre(microOrdre);
        microOrdreRepository.saveAndFlush(microOrdre);
        Long microOrdreId = microOrdre.getId();

        // Get all the microOrdreList where microOrdre equals to microOrdreId
        defaultMicroOrdreShouldBeFound("microOrdreId.equals=" + microOrdreId);

        // Get all the microOrdreList where microOrdre equals to (microOrdreId + 1)
        defaultMicroOrdreShouldNotBeFound("microOrdreId.equals=" + (microOrdreId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMicroOrdreShouldBeFound(String filter) throws Exception {
        restMicroOrdreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(microOrdre.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));

        // Check, that the count call also returns 1
        restMicroOrdreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMicroOrdreShouldNotBeFound(String filter) throws Exception {
        restMicroOrdreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMicroOrdreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMicroOrdre() throws Exception {
        // Get the microOrdre
        restMicroOrdreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMicroOrdre() throws Exception {
        // Initialize the database
        microOrdreRepository.saveAndFlush(microOrdre);

        int databaseSizeBeforeUpdate = microOrdreRepository.findAll().size();

        // Update the microOrdre
        MicroOrdre updatedMicroOrdre = microOrdreRepository.findById(microOrdre.getId()).get();
        // Disconnect from session so that the updates on updatedMicroOrdre are not directly saved in db
        em.detach(updatedMicroOrdre);
        updatedMicroOrdre.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restMicroOrdreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMicroOrdre.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMicroOrdre))
            )
            .andExpect(status().isOk());

        // Validate the MicroOrdre in the database
        List<MicroOrdre> microOrdreList = microOrdreRepository.findAll();
        assertThat(microOrdreList).hasSize(databaseSizeBeforeUpdate);
        MicroOrdre testMicroOrdre = microOrdreList.get(microOrdreList.size() - 1);
        assertThat(testMicroOrdre.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testMicroOrdre.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void putNonExistingMicroOrdre() throws Exception {
        int databaseSizeBeforeUpdate = microOrdreRepository.findAll().size();
        microOrdre.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMicroOrdreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, microOrdre.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(microOrdre))
            )
            .andExpect(status().isBadRequest());

        // Validate the MicroOrdre in the database
        List<MicroOrdre> microOrdreList = microOrdreRepository.findAll();
        assertThat(microOrdreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMicroOrdre() throws Exception {
        int databaseSizeBeforeUpdate = microOrdreRepository.findAll().size();
        microOrdre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMicroOrdreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(microOrdre))
            )
            .andExpect(status().isBadRequest());

        // Validate the MicroOrdre in the database
        List<MicroOrdre> microOrdreList = microOrdreRepository.findAll();
        assertThat(microOrdreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMicroOrdre() throws Exception {
        int databaseSizeBeforeUpdate = microOrdreRepository.findAll().size();
        microOrdre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMicroOrdreMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(microOrdre)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MicroOrdre in the database
        List<MicroOrdre> microOrdreList = microOrdreRepository.findAll();
        assertThat(microOrdreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMicroOrdreWithPatch() throws Exception {
        // Initialize the database
        microOrdreRepository.saveAndFlush(microOrdre);

        int databaseSizeBeforeUpdate = microOrdreRepository.findAll().size();

        // Update the microOrdre using partial update
        MicroOrdre partialUpdatedMicroOrdre = new MicroOrdre();
        partialUpdatedMicroOrdre.setId(microOrdre.getId());

        partialUpdatedMicroOrdre.nomLatin(UPDATED_NOM_LATIN);

        restMicroOrdreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMicroOrdre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMicroOrdre))
            )
            .andExpect(status().isOk());

        // Validate the MicroOrdre in the database
        List<MicroOrdre> microOrdreList = microOrdreRepository.findAll();
        assertThat(microOrdreList).hasSize(databaseSizeBeforeUpdate);
        MicroOrdre testMicroOrdre = microOrdreList.get(microOrdreList.size() - 1);
        assertThat(testMicroOrdre.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testMicroOrdre.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void fullUpdateMicroOrdreWithPatch() throws Exception {
        // Initialize the database
        microOrdreRepository.saveAndFlush(microOrdre);

        int databaseSizeBeforeUpdate = microOrdreRepository.findAll().size();

        // Update the microOrdre using partial update
        MicroOrdre partialUpdatedMicroOrdre = new MicroOrdre();
        partialUpdatedMicroOrdre.setId(microOrdre.getId());

        partialUpdatedMicroOrdre.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restMicroOrdreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMicroOrdre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMicroOrdre))
            )
            .andExpect(status().isOk());

        // Validate the MicroOrdre in the database
        List<MicroOrdre> microOrdreList = microOrdreRepository.findAll();
        assertThat(microOrdreList).hasSize(databaseSizeBeforeUpdate);
        MicroOrdre testMicroOrdre = microOrdreList.get(microOrdreList.size() - 1);
        assertThat(testMicroOrdre.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testMicroOrdre.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void patchNonExistingMicroOrdre() throws Exception {
        int databaseSizeBeforeUpdate = microOrdreRepository.findAll().size();
        microOrdre.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMicroOrdreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, microOrdre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(microOrdre))
            )
            .andExpect(status().isBadRequest());

        // Validate the MicroOrdre in the database
        List<MicroOrdre> microOrdreList = microOrdreRepository.findAll();
        assertThat(microOrdreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMicroOrdre() throws Exception {
        int databaseSizeBeforeUpdate = microOrdreRepository.findAll().size();
        microOrdre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMicroOrdreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(microOrdre))
            )
            .andExpect(status().isBadRequest());

        // Validate the MicroOrdre in the database
        List<MicroOrdre> microOrdreList = microOrdreRepository.findAll();
        assertThat(microOrdreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMicroOrdre() throws Exception {
        int databaseSizeBeforeUpdate = microOrdreRepository.findAll().size();
        microOrdre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMicroOrdreMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(microOrdre))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MicroOrdre in the database
        List<MicroOrdre> microOrdreList = microOrdreRepository.findAll();
        assertThat(microOrdreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMicroOrdre() throws Exception {
        // Initialize the database
        microOrdreRepository.saveAndFlush(microOrdre);

        int databaseSizeBeforeDelete = microOrdreRepository.findAll().size();

        // Delete the microOrdre
        restMicroOrdreMockMvc
            .perform(delete(ENTITY_API_URL_ID, microOrdre.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MicroOrdre> microOrdreList = microOrdreRepository.findAll();
        assertThat(microOrdreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
