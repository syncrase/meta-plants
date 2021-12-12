package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.InfraOrdre;
import fr.syncrase.ecosyst.domain.Ordre;
import fr.syncrase.ecosyst.domain.SousOrdre;
import fr.syncrase.ecosyst.domain.SousOrdre;
import fr.syncrase.ecosyst.repository.SousOrdreRepository;
import fr.syncrase.ecosyst.service.criteria.SousOrdreCriteria;
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
 * Integration tests for the {@link SousOrdreResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SousOrdreResourceIT {

    private static final String DEFAULT_NOM_FR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_LATIN = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LATIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sous-ordres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SousOrdreRepository sousOrdreRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSousOrdreMockMvc;

    private SousOrdre sousOrdre;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SousOrdre createEntity(EntityManager em) {
        SousOrdre sousOrdre = new SousOrdre().nomFr(DEFAULT_NOM_FR).nomLatin(DEFAULT_NOM_LATIN);
        return sousOrdre;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SousOrdre createUpdatedEntity(EntityManager em) {
        SousOrdre sousOrdre = new SousOrdre().nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);
        return sousOrdre;
    }

    @BeforeEach
    public void initTest() {
        sousOrdre = createEntity(em);
    }

    @Test
    @Transactional
    void createSousOrdre() throws Exception {
        int databaseSizeBeforeCreate = sousOrdreRepository.findAll().size();
        // Create the SousOrdre
        restSousOrdreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousOrdre)))
            .andExpect(status().isCreated());

        // Validate the SousOrdre in the database
        List<SousOrdre> sousOrdreList = sousOrdreRepository.findAll();
        assertThat(sousOrdreList).hasSize(databaseSizeBeforeCreate + 1);
        SousOrdre testSousOrdre = sousOrdreList.get(sousOrdreList.size() - 1);
        assertThat(testSousOrdre.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testSousOrdre.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void createSousOrdreWithExistingId() throws Exception {
        // Create the SousOrdre with an existing ID
        sousOrdre.setId(1L);

        int databaseSizeBeforeCreate = sousOrdreRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSousOrdreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousOrdre)))
            .andExpect(status().isBadRequest());

        // Validate the SousOrdre in the database
        List<SousOrdre> sousOrdreList = sousOrdreRepository.findAll();
        assertThat(sousOrdreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomFrIsRequired() throws Exception {
        int databaseSizeBeforeTest = sousOrdreRepository.findAll().size();
        // set the field null
        sousOrdre.setNomFr(null);

        // Create the SousOrdre, which fails.

        restSousOrdreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousOrdre)))
            .andExpect(status().isBadRequest());

        List<SousOrdre> sousOrdreList = sousOrdreRepository.findAll();
        assertThat(sousOrdreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSousOrdres() throws Exception {
        // Initialize the database
        sousOrdreRepository.saveAndFlush(sousOrdre);

        // Get all the sousOrdreList
        restSousOrdreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sousOrdre.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));
    }

    @Test
    @Transactional
    void getSousOrdre() throws Exception {
        // Initialize the database
        sousOrdreRepository.saveAndFlush(sousOrdre);

        // Get the sousOrdre
        restSousOrdreMockMvc
            .perform(get(ENTITY_API_URL_ID, sousOrdre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sousOrdre.getId().intValue()))
            .andExpect(jsonPath("$.nomFr").value(DEFAULT_NOM_FR))
            .andExpect(jsonPath("$.nomLatin").value(DEFAULT_NOM_LATIN));
    }

    @Test
    @Transactional
    void getSousOrdresByIdFiltering() throws Exception {
        // Initialize the database
        sousOrdreRepository.saveAndFlush(sousOrdre);

        Long id = sousOrdre.getId();

        defaultSousOrdreShouldBeFound("id.equals=" + id);
        defaultSousOrdreShouldNotBeFound("id.notEquals=" + id);

        defaultSousOrdreShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSousOrdreShouldNotBeFound("id.greaterThan=" + id);

        defaultSousOrdreShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSousOrdreShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSousOrdresByNomFrIsEqualToSomething() throws Exception {
        // Initialize the database
        sousOrdreRepository.saveAndFlush(sousOrdre);

        // Get all the sousOrdreList where nomFr equals to DEFAULT_NOM_FR
        defaultSousOrdreShouldBeFound("nomFr.equals=" + DEFAULT_NOM_FR);

        // Get all the sousOrdreList where nomFr equals to UPDATED_NOM_FR
        defaultSousOrdreShouldNotBeFound("nomFr.equals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousOrdresByNomFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sousOrdreRepository.saveAndFlush(sousOrdre);

        // Get all the sousOrdreList where nomFr not equals to DEFAULT_NOM_FR
        defaultSousOrdreShouldNotBeFound("nomFr.notEquals=" + DEFAULT_NOM_FR);

        // Get all the sousOrdreList where nomFr not equals to UPDATED_NOM_FR
        defaultSousOrdreShouldBeFound("nomFr.notEquals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousOrdresByNomFrIsInShouldWork() throws Exception {
        // Initialize the database
        sousOrdreRepository.saveAndFlush(sousOrdre);

        // Get all the sousOrdreList where nomFr in DEFAULT_NOM_FR or UPDATED_NOM_FR
        defaultSousOrdreShouldBeFound("nomFr.in=" + DEFAULT_NOM_FR + "," + UPDATED_NOM_FR);

        // Get all the sousOrdreList where nomFr equals to UPDATED_NOM_FR
        defaultSousOrdreShouldNotBeFound("nomFr.in=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousOrdresByNomFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        sousOrdreRepository.saveAndFlush(sousOrdre);

        // Get all the sousOrdreList where nomFr is not null
        defaultSousOrdreShouldBeFound("nomFr.specified=true");

        // Get all the sousOrdreList where nomFr is null
        defaultSousOrdreShouldNotBeFound("nomFr.specified=false");
    }

    @Test
    @Transactional
    void getAllSousOrdresByNomFrContainsSomething() throws Exception {
        // Initialize the database
        sousOrdreRepository.saveAndFlush(sousOrdre);

        // Get all the sousOrdreList where nomFr contains DEFAULT_NOM_FR
        defaultSousOrdreShouldBeFound("nomFr.contains=" + DEFAULT_NOM_FR);

        // Get all the sousOrdreList where nomFr contains UPDATED_NOM_FR
        defaultSousOrdreShouldNotBeFound("nomFr.contains=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousOrdresByNomFrNotContainsSomething() throws Exception {
        // Initialize the database
        sousOrdreRepository.saveAndFlush(sousOrdre);

        // Get all the sousOrdreList where nomFr does not contain DEFAULT_NOM_FR
        defaultSousOrdreShouldNotBeFound("nomFr.doesNotContain=" + DEFAULT_NOM_FR);

        // Get all the sousOrdreList where nomFr does not contain UPDATED_NOM_FR
        defaultSousOrdreShouldBeFound("nomFr.doesNotContain=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousOrdresByNomLatinIsEqualToSomething() throws Exception {
        // Initialize the database
        sousOrdreRepository.saveAndFlush(sousOrdre);

        // Get all the sousOrdreList where nomLatin equals to DEFAULT_NOM_LATIN
        defaultSousOrdreShouldBeFound("nomLatin.equals=" + DEFAULT_NOM_LATIN);

        // Get all the sousOrdreList where nomLatin equals to UPDATED_NOM_LATIN
        defaultSousOrdreShouldNotBeFound("nomLatin.equals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousOrdresByNomLatinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sousOrdreRepository.saveAndFlush(sousOrdre);

        // Get all the sousOrdreList where nomLatin not equals to DEFAULT_NOM_LATIN
        defaultSousOrdreShouldNotBeFound("nomLatin.notEquals=" + DEFAULT_NOM_LATIN);

        // Get all the sousOrdreList where nomLatin not equals to UPDATED_NOM_LATIN
        defaultSousOrdreShouldBeFound("nomLatin.notEquals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousOrdresByNomLatinIsInShouldWork() throws Exception {
        // Initialize the database
        sousOrdreRepository.saveAndFlush(sousOrdre);

        // Get all the sousOrdreList where nomLatin in DEFAULT_NOM_LATIN or UPDATED_NOM_LATIN
        defaultSousOrdreShouldBeFound("nomLatin.in=" + DEFAULT_NOM_LATIN + "," + UPDATED_NOM_LATIN);

        // Get all the sousOrdreList where nomLatin equals to UPDATED_NOM_LATIN
        defaultSousOrdreShouldNotBeFound("nomLatin.in=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousOrdresByNomLatinIsNullOrNotNull() throws Exception {
        // Initialize the database
        sousOrdreRepository.saveAndFlush(sousOrdre);

        // Get all the sousOrdreList where nomLatin is not null
        defaultSousOrdreShouldBeFound("nomLatin.specified=true");

        // Get all the sousOrdreList where nomLatin is null
        defaultSousOrdreShouldNotBeFound("nomLatin.specified=false");
    }

    @Test
    @Transactional
    void getAllSousOrdresByNomLatinContainsSomething() throws Exception {
        // Initialize the database
        sousOrdreRepository.saveAndFlush(sousOrdre);

        // Get all the sousOrdreList where nomLatin contains DEFAULT_NOM_LATIN
        defaultSousOrdreShouldBeFound("nomLatin.contains=" + DEFAULT_NOM_LATIN);

        // Get all the sousOrdreList where nomLatin contains UPDATED_NOM_LATIN
        defaultSousOrdreShouldNotBeFound("nomLatin.contains=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousOrdresByNomLatinNotContainsSomething() throws Exception {
        // Initialize the database
        sousOrdreRepository.saveAndFlush(sousOrdre);

        // Get all the sousOrdreList where nomLatin does not contain DEFAULT_NOM_LATIN
        defaultSousOrdreShouldNotBeFound("nomLatin.doesNotContain=" + DEFAULT_NOM_LATIN);

        // Get all the sousOrdreList where nomLatin does not contain UPDATED_NOM_LATIN
        defaultSousOrdreShouldBeFound("nomLatin.doesNotContain=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousOrdresByInfraOrdresIsEqualToSomething() throws Exception {
        // Initialize the database
        sousOrdreRepository.saveAndFlush(sousOrdre);
        InfraOrdre infraOrdres;
        if (TestUtil.findAll(em, InfraOrdre.class).isEmpty()) {
            infraOrdres = InfraOrdreResourceIT.createEntity(em);
            em.persist(infraOrdres);
            em.flush();
        } else {
            infraOrdres = TestUtil.findAll(em, InfraOrdre.class).get(0);
        }
        em.persist(infraOrdres);
        em.flush();
        sousOrdre.addInfraOrdres(infraOrdres);
        sousOrdreRepository.saveAndFlush(sousOrdre);
        Long infraOrdresId = infraOrdres.getId();

        // Get all the sousOrdreList where infraOrdres equals to infraOrdresId
        defaultSousOrdreShouldBeFound("infraOrdresId.equals=" + infraOrdresId);

        // Get all the sousOrdreList where infraOrdres equals to (infraOrdresId + 1)
        defaultSousOrdreShouldNotBeFound("infraOrdresId.equals=" + (infraOrdresId + 1));
    }

    @Test
    @Transactional
    void getAllSousOrdresBySynonymesIsEqualToSomething() throws Exception {
        // Initialize the database
        sousOrdreRepository.saveAndFlush(sousOrdre);
        SousOrdre synonymes;
        if (TestUtil.findAll(em, SousOrdre.class).isEmpty()) {
            synonymes = SousOrdreResourceIT.createEntity(em);
            em.persist(synonymes);
            em.flush();
        } else {
            synonymes = TestUtil.findAll(em, SousOrdre.class).get(0);
        }
        em.persist(synonymes);
        em.flush();
        sousOrdre.addSynonymes(synonymes);
        sousOrdreRepository.saveAndFlush(sousOrdre);
        Long synonymesId = synonymes.getId();

        // Get all the sousOrdreList where synonymes equals to synonymesId
        defaultSousOrdreShouldBeFound("synonymesId.equals=" + synonymesId);

        // Get all the sousOrdreList where synonymes equals to (synonymesId + 1)
        defaultSousOrdreShouldNotBeFound("synonymesId.equals=" + (synonymesId + 1));
    }

    @Test
    @Transactional
    void getAllSousOrdresByOrdreIsEqualToSomething() throws Exception {
        // Initialize the database
        sousOrdreRepository.saveAndFlush(sousOrdre);
        Ordre ordre;
        if (TestUtil.findAll(em, Ordre.class).isEmpty()) {
            ordre = OrdreResourceIT.createEntity(em);
            em.persist(ordre);
            em.flush();
        } else {
            ordre = TestUtil.findAll(em, Ordre.class).get(0);
        }
        em.persist(ordre);
        em.flush();
        sousOrdre.setOrdre(ordre);
        sousOrdreRepository.saveAndFlush(sousOrdre);
        Long ordreId = ordre.getId();

        // Get all the sousOrdreList where ordre equals to ordreId
        defaultSousOrdreShouldBeFound("ordreId.equals=" + ordreId);

        // Get all the sousOrdreList where ordre equals to (ordreId + 1)
        defaultSousOrdreShouldNotBeFound("ordreId.equals=" + (ordreId + 1));
    }

    @Test
    @Transactional
    void getAllSousOrdresBySousOrdreIsEqualToSomething() throws Exception {
        // Initialize the database
        sousOrdreRepository.saveAndFlush(sousOrdre);
        SousOrdre sousOrdre;
        if (TestUtil.findAll(em, SousOrdre.class).isEmpty()) {
            sousOrdre = SousOrdreResourceIT.createEntity(em);
            em.persist(sousOrdre);
            em.flush();
        } else {
            sousOrdre = TestUtil.findAll(em, SousOrdre.class).get(0);
        }
        em.persist(sousOrdre);
        em.flush();
        sousOrdre.setSousOrdre(sousOrdre);
        sousOrdreRepository.saveAndFlush(sousOrdre);
        Long sousOrdreId = sousOrdre.getId();

        // Get all the sousOrdreList where sousOrdre equals to sousOrdreId
        defaultSousOrdreShouldBeFound("sousOrdreId.equals=" + sousOrdreId);

        // Get all the sousOrdreList where sousOrdre equals to (sousOrdreId + 1)
        defaultSousOrdreShouldNotBeFound("sousOrdreId.equals=" + (sousOrdreId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSousOrdreShouldBeFound(String filter) throws Exception {
        restSousOrdreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sousOrdre.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));

        // Check, that the count call also returns 1
        restSousOrdreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSousOrdreShouldNotBeFound(String filter) throws Exception {
        restSousOrdreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSousOrdreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSousOrdre() throws Exception {
        // Get the sousOrdre
        restSousOrdreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSousOrdre() throws Exception {
        // Initialize the database
        sousOrdreRepository.saveAndFlush(sousOrdre);

        int databaseSizeBeforeUpdate = sousOrdreRepository.findAll().size();

        // Update the sousOrdre
        SousOrdre updatedSousOrdre = sousOrdreRepository.findById(sousOrdre.getId()).get();
        // Disconnect from session so that the updates on updatedSousOrdre are not directly saved in db
        em.detach(updatedSousOrdre);
        updatedSousOrdre.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restSousOrdreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSousOrdre.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSousOrdre))
            )
            .andExpect(status().isOk());

        // Validate the SousOrdre in the database
        List<SousOrdre> sousOrdreList = sousOrdreRepository.findAll();
        assertThat(sousOrdreList).hasSize(databaseSizeBeforeUpdate);
        SousOrdre testSousOrdre = sousOrdreList.get(sousOrdreList.size() - 1);
        assertThat(testSousOrdre.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testSousOrdre.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void putNonExistingSousOrdre() throws Exception {
        int databaseSizeBeforeUpdate = sousOrdreRepository.findAll().size();
        sousOrdre.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSousOrdreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sousOrdre.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sousOrdre))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousOrdre in the database
        List<SousOrdre> sousOrdreList = sousOrdreRepository.findAll();
        assertThat(sousOrdreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSousOrdre() throws Exception {
        int databaseSizeBeforeUpdate = sousOrdreRepository.findAll().size();
        sousOrdre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousOrdreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sousOrdre))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousOrdre in the database
        List<SousOrdre> sousOrdreList = sousOrdreRepository.findAll();
        assertThat(sousOrdreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSousOrdre() throws Exception {
        int databaseSizeBeforeUpdate = sousOrdreRepository.findAll().size();
        sousOrdre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousOrdreMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousOrdre)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SousOrdre in the database
        List<SousOrdre> sousOrdreList = sousOrdreRepository.findAll();
        assertThat(sousOrdreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSousOrdreWithPatch() throws Exception {
        // Initialize the database
        sousOrdreRepository.saveAndFlush(sousOrdre);

        int databaseSizeBeforeUpdate = sousOrdreRepository.findAll().size();

        // Update the sousOrdre using partial update
        SousOrdre partialUpdatedSousOrdre = new SousOrdre();
        partialUpdatedSousOrdre.setId(sousOrdre.getId());

        restSousOrdreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSousOrdre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSousOrdre))
            )
            .andExpect(status().isOk());

        // Validate the SousOrdre in the database
        List<SousOrdre> sousOrdreList = sousOrdreRepository.findAll();
        assertThat(sousOrdreList).hasSize(databaseSizeBeforeUpdate);
        SousOrdre testSousOrdre = sousOrdreList.get(sousOrdreList.size() - 1);
        assertThat(testSousOrdre.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testSousOrdre.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void fullUpdateSousOrdreWithPatch() throws Exception {
        // Initialize the database
        sousOrdreRepository.saveAndFlush(sousOrdre);

        int databaseSizeBeforeUpdate = sousOrdreRepository.findAll().size();

        // Update the sousOrdre using partial update
        SousOrdre partialUpdatedSousOrdre = new SousOrdre();
        partialUpdatedSousOrdre.setId(sousOrdre.getId());

        partialUpdatedSousOrdre.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restSousOrdreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSousOrdre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSousOrdre))
            )
            .andExpect(status().isOk());

        // Validate the SousOrdre in the database
        List<SousOrdre> sousOrdreList = sousOrdreRepository.findAll();
        assertThat(sousOrdreList).hasSize(databaseSizeBeforeUpdate);
        SousOrdre testSousOrdre = sousOrdreList.get(sousOrdreList.size() - 1);
        assertThat(testSousOrdre.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testSousOrdre.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void patchNonExistingSousOrdre() throws Exception {
        int databaseSizeBeforeUpdate = sousOrdreRepository.findAll().size();
        sousOrdre.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSousOrdreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sousOrdre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sousOrdre))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousOrdre in the database
        List<SousOrdre> sousOrdreList = sousOrdreRepository.findAll();
        assertThat(sousOrdreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSousOrdre() throws Exception {
        int databaseSizeBeforeUpdate = sousOrdreRepository.findAll().size();
        sousOrdre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousOrdreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sousOrdre))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousOrdre in the database
        List<SousOrdre> sousOrdreList = sousOrdreRepository.findAll();
        assertThat(sousOrdreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSousOrdre() throws Exception {
        int databaseSizeBeforeUpdate = sousOrdreRepository.findAll().size();
        sousOrdre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousOrdreMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sousOrdre))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SousOrdre in the database
        List<SousOrdre> sousOrdreList = sousOrdreRepository.findAll();
        assertThat(sousOrdreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSousOrdre() throws Exception {
        // Initialize the database
        sousOrdreRepository.saveAndFlush(sousOrdre);

        int databaseSizeBeforeDelete = sousOrdreRepository.findAll().size();

        // Delete the sousOrdre
        restSousOrdreMockMvc
            .perform(delete(ENTITY_API_URL_ID, sousOrdre.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SousOrdre> sousOrdreList = sousOrdreRepository.findAll();
        assertThat(sousOrdreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
