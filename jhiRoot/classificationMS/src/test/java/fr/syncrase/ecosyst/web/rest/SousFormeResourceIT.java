package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.Forme;
import fr.syncrase.ecosyst.domain.SousForme;
import fr.syncrase.ecosyst.domain.SousForme;
import fr.syncrase.ecosyst.repository.SousFormeRepository;
import fr.syncrase.ecosyst.service.criteria.SousFormeCriteria;
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
 * Integration tests for the {@link SousFormeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SousFormeResourceIT {

    private static final String DEFAULT_NOM_FR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_LATIN = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LATIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sous-formes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SousFormeRepository sousFormeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSousFormeMockMvc;

    private SousForme sousForme;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SousForme createEntity(EntityManager em) {
        SousForme sousForme = new SousForme().nomFr(DEFAULT_NOM_FR).nomLatin(DEFAULT_NOM_LATIN);
        return sousForme;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SousForme createUpdatedEntity(EntityManager em) {
        SousForme sousForme = new SousForme().nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);
        return sousForme;
    }

    @BeforeEach
    public void initTest() {
        sousForme = createEntity(em);
    }

    @Test
    @Transactional
    void createSousForme() throws Exception {
        int databaseSizeBeforeCreate = sousFormeRepository.findAll().size();
        // Create the SousForme
        restSousFormeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousForme)))
            .andExpect(status().isCreated());

        // Validate the SousForme in the database
        List<SousForme> sousFormeList = sousFormeRepository.findAll();
        assertThat(sousFormeList).hasSize(databaseSizeBeforeCreate + 1);
        SousForme testSousForme = sousFormeList.get(sousFormeList.size() - 1);
        assertThat(testSousForme.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testSousForme.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void createSousFormeWithExistingId() throws Exception {
        // Create the SousForme with an existing ID
        sousForme.setId(1L);

        int databaseSizeBeforeCreate = sousFormeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSousFormeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousForme)))
            .andExpect(status().isBadRequest());

        // Validate the SousForme in the database
        List<SousForme> sousFormeList = sousFormeRepository.findAll();
        assertThat(sousFormeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomFrIsRequired() throws Exception {
        int databaseSizeBeforeTest = sousFormeRepository.findAll().size();
        // set the field null
        sousForme.setNomFr(null);

        // Create the SousForme, which fails.

        restSousFormeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousForme)))
            .andExpect(status().isBadRequest());

        List<SousForme> sousFormeList = sousFormeRepository.findAll();
        assertThat(sousFormeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSousFormes() throws Exception {
        // Initialize the database
        sousFormeRepository.saveAndFlush(sousForme);

        // Get all the sousFormeList
        restSousFormeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sousForme.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));
    }

    @Test
    @Transactional
    void getSousForme() throws Exception {
        // Initialize the database
        sousFormeRepository.saveAndFlush(sousForme);

        // Get the sousForme
        restSousFormeMockMvc
            .perform(get(ENTITY_API_URL_ID, sousForme.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sousForme.getId().intValue()))
            .andExpect(jsonPath("$.nomFr").value(DEFAULT_NOM_FR))
            .andExpect(jsonPath("$.nomLatin").value(DEFAULT_NOM_LATIN));
    }

    @Test
    @Transactional
    void getSousFormesByIdFiltering() throws Exception {
        // Initialize the database
        sousFormeRepository.saveAndFlush(sousForme);

        Long id = sousForme.getId();

        defaultSousFormeShouldBeFound("id.equals=" + id);
        defaultSousFormeShouldNotBeFound("id.notEquals=" + id);

        defaultSousFormeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSousFormeShouldNotBeFound("id.greaterThan=" + id);

        defaultSousFormeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSousFormeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSousFormesByNomFrIsEqualToSomething() throws Exception {
        // Initialize the database
        sousFormeRepository.saveAndFlush(sousForme);

        // Get all the sousFormeList where nomFr equals to DEFAULT_NOM_FR
        defaultSousFormeShouldBeFound("nomFr.equals=" + DEFAULT_NOM_FR);

        // Get all the sousFormeList where nomFr equals to UPDATED_NOM_FR
        defaultSousFormeShouldNotBeFound("nomFr.equals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousFormesByNomFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sousFormeRepository.saveAndFlush(sousForme);

        // Get all the sousFormeList where nomFr not equals to DEFAULT_NOM_FR
        defaultSousFormeShouldNotBeFound("nomFr.notEquals=" + DEFAULT_NOM_FR);

        // Get all the sousFormeList where nomFr not equals to UPDATED_NOM_FR
        defaultSousFormeShouldBeFound("nomFr.notEquals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousFormesByNomFrIsInShouldWork() throws Exception {
        // Initialize the database
        sousFormeRepository.saveAndFlush(sousForme);

        // Get all the sousFormeList where nomFr in DEFAULT_NOM_FR or UPDATED_NOM_FR
        defaultSousFormeShouldBeFound("nomFr.in=" + DEFAULT_NOM_FR + "," + UPDATED_NOM_FR);

        // Get all the sousFormeList where nomFr equals to UPDATED_NOM_FR
        defaultSousFormeShouldNotBeFound("nomFr.in=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousFormesByNomFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        sousFormeRepository.saveAndFlush(sousForme);

        // Get all the sousFormeList where nomFr is not null
        defaultSousFormeShouldBeFound("nomFr.specified=true");

        // Get all the sousFormeList where nomFr is null
        defaultSousFormeShouldNotBeFound("nomFr.specified=false");
    }

    @Test
    @Transactional
    void getAllSousFormesByNomFrContainsSomething() throws Exception {
        // Initialize the database
        sousFormeRepository.saveAndFlush(sousForme);

        // Get all the sousFormeList where nomFr contains DEFAULT_NOM_FR
        defaultSousFormeShouldBeFound("nomFr.contains=" + DEFAULT_NOM_FR);

        // Get all the sousFormeList where nomFr contains UPDATED_NOM_FR
        defaultSousFormeShouldNotBeFound("nomFr.contains=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousFormesByNomFrNotContainsSomething() throws Exception {
        // Initialize the database
        sousFormeRepository.saveAndFlush(sousForme);

        // Get all the sousFormeList where nomFr does not contain DEFAULT_NOM_FR
        defaultSousFormeShouldNotBeFound("nomFr.doesNotContain=" + DEFAULT_NOM_FR);

        // Get all the sousFormeList where nomFr does not contain UPDATED_NOM_FR
        defaultSousFormeShouldBeFound("nomFr.doesNotContain=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousFormesByNomLatinIsEqualToSomething() throws Exception {
        // Initialize the database
        sousFormeRepository.saveAndFlush(sousForme);

        // Get all the sousFormeList where nomLatin equals to DEFAULT_NOM_LATIN
        defaultSousFormeShouldBeFound("nomLatin.equals=" + DEFAULT_NOM_LATIN);

        // Get all the sousFormeList where nomLatin equals to UPDATED_NOM_LATIN
        defaultSousFormeShouldNotBeFound("nomLatin.equals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousFormesByNomLatinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sousFormeRepository.saveAndFlush(sousForme);

        // Get all the sousFormeList where nomLatin not equals to DEFAULT_NOM_LATIN
        defaultSousFormeShouldNotBeFound("nomLatin.notEquals=" + DEFAULT_NOM_LATIN);

        // Get all the sousFormeList where nomLatin not equals to UPDATED_NOM_LATIN
        defaultSousFormeShouldBeFound("nomLatin.notEquals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousFormesByNomLatinIsInShouldWork() throws Exception {
        // Initialize the database
        sousFormeRepository.saveAndFlush(sousForme);

        // Get all the sousFormeList where nomLatin in DEFAULT_NOM_LATIN or UPDATED_NOM_LATIN
        defaultSousFormeShouldBeFound("nomLatin.in=" + DEFAULT_NOM_LATIN + "," + UPDATED_NOM_LATIN);

        // Get all the sousFormeList where nomLatin equals to UPDATED_NOM_LATIN
        defaultSousFormeShouldNotBeFound("nomLatin.in=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousFormesByNomLatinIsNullOrNotNull() throws Exception {
        // Initialize the database
        sousFormeRepository.saveAndFlush(sousForme);

        // Get all the sousFormeList where nomLatin is not null
        defaultSousFormeShouldBeFound("nomLatin.specified=true");

        // Get all the sousFormeList where nomLatin is null
        defaultSousFormeShouldNotBeFound("nomLatin.specified=false");
    }

    @Test
    @Transactional
    void getAllSousFormesByNomLatinContainsSomething() throws Exception {
        // Initialize the database
        sousFormeRepository.saveAndFlush(sousForme);

        // Get all the sousFormeList where nomLatin contains DEFAULT_NOM_LATIN
        defaultSousFormeShouldBeFound("nomLatin.contains=" + DEFAULT_NOM_LATIN);

        // Get all the sousFormeList where nomLatin contains UPDATED_NOM_LATIN
        defaultSousFormeShouldNotBeFound("nomLatin.contains=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousFormesByNomLatinNotContainsSomething() throws Exception {
        // Initialize the database
        sousFormeRepository.saveAndFlush(sousForme);

        // Get all the sousFormeList where nomLatin does not contain DEFAULT_NOM_LATIN
        defaultSousFormeShouldNotBeFound("nomLatin.doesNotContain=" + DEFAULT_NOM_LATIN);

        // Get all the sousFormeList where nomLatin does not contain UPDATED_NOM_LATIN
        defaultSousFormeShouldBeFound("nomLatin.doesNotContain=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousFormesBySynonymesIsEqualToSomething() throws Exception {
        // Initialize the database
        sousFormeRepository.saveAndFlush(sousForme);
        SousForme synonymes;
        if (TestUtil.findAll(em, SousForme.class).isEmpty()) {
            synonymes = SousFormeResourceIT.createEntity(em);
            em.persist(synonymes);
            em.flush();
        } else {
            synonymes = TestUtil.findAll(em, SousForme.class).get(0);
        }
        em.persist(synonymes);
        em.flush();
        sousForme.addSynonymes(synonymes);
        sousFormeRepository.saveAndFlush(sousForme);
        Long synonymesId = synonymes.getId();

        // Get all the sousFormeList where synonymes equals to synonymesId
        defaultSousFormeShouldBeFound("synonymesId.equals=" + synonymesId);

        // Get all the sousFormeList where synonymes equals to (synonymesId + 1)
        defaultSousFormeShouldNotBeFound("synonymesId.equals=" + (synonymesId + 1));
    }

    @Test
    @Transactional
    void getAllSousFormesByFormeIsEqualToSomething() throws Exception {
        // Initialize the database
        sousFormeRepository.saveAndFlush(sousForme);
        Forme forme;
        if (TestUtil.findAll(em, Forme.class).isEmpty()) {
            forme = FormeResourceIT.createEntity(em);
            em.persist(forme);
            em.flush();
        } else {
            forme = TestUtil.findAll(em, Forme.class).get(0);
        }
        em.persist(forme);
        em.flush();
        sousForme.setForme(forme);
        sousFormeRepository.saveAndFlush(sousForme);
        Long formeId = forme.getId();

        // Get all the sousFormeList where forme equals to formeId
        defaultSousFormeShouldBeFound("formeId.equals=" + formeId);

        // Get all the sousFormeList where forme equals to (formeId + 1)
        defaultSousFormeShouldNotBeFound("formeId.equals=" + (formeId + 1));
    }

    @Test
    @Transactional
    void getAllSousFormesBySousFormeIsEqualToSomething() throws Exception {
        // Initialize the database
        sousFormeRepository.saveAndFlush(sousForme);
        SousForme sousForme;
        if (TestUtil.findAll(em, SousForme.class).isEmpty()) {
            sousForme = SousFormeResourceIT.createEntity(em);
            em.persist(sousForme);
            em.flush();
        } else {
            sousForme = TestUtil.findAll(em, SousForme.class).get(0);
        }
        em.persist(sousForme);
        em.flush();
        sousForme.setSousForme(sousForme);
        sousFormeRepository.saveAndFlush(sousForme);
        Long sousFormeId = sousForme.getId();

        // Get all the sousFormeList where sousForme equals to sousFormeId
        defaultSousFormeShouldBeFound("sousFormeId.equals=" + sousFormeId);

        // Get all the sousFormeList where sousForme equals to (sousFormeId + 1)
        defaultSousFormeShouldNotBeFound("sousFormeId.equals=" + (sousFormeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSousFormeShouldBeFound(String filter) throws Exception {
        restSousFormeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sousForme.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));

        // Check, that the count call also returns 1
        restSousFormeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSousFormeShouldNotBeFound(String filter) throws Exception {
        restSousFormeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSousFormeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSousForme() throws Exception {
        // Get the sousForme
        restSousFormeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSousForme() throws Exception {
        // Initialize the database
        sousFormeRepository.saveAndFlush(sousForme);

        int databaseSizeBeforeUpdate = sousFormeRepository.findAll().size();

        // Update the sousForme
        SousForme updatedSousForme = sousFormeRepository.findById(sousForme.getId()).get();
        // Disconnect from session so that the updates on updatedSousForme are not directly saved in db
        em.detach(updatedSousForme);
        updatedSousForme.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restSousFormeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSousForme.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSousForme))
            )
            .andExpect(status().isOk());

        // Validate the SousForme in the database
        List<SousForme> sousFormeList = sousFormeRepository.findAll();
        assertThat(sousFormeList).hasSize(databaseSizeBeforeUpdate);
        SousForme testSousForme = sousFormeList.get(sousFormeList.size() - 1);
        assertThat(testSousForme.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testSousForme.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void putNonExistingSousForme() throws Exception {
        int databaseSizeBeforeUpdate = sousFormeRepository.findAll().size();
        sousForme.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSousFormeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sousForme.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sousForme))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousForme in the database
        List<SousForme> sousFormeList = sousFormeRepository.findAll();
        assertThat(sousFormeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSousForme() throws Exception {
        int databaseSizeBeforeUpdate = sousFormeRepository.findAll().size();
        sousForme.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousFormeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sousForme))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousForme in the database
        List<SousForme> sousFormeList = sousFormeRepository.findAll();
        assertThat(sousFormeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSousForme() throws Exception {
        int databaseSizeBeforeUpdate = sousFormeRepository.findAll().size();
        sousForme.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousFormeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousForme)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SousForme in the database
        List<SousForme> sousFormeList = sousFormeRepository.findAll();
        assertThat(sousFormeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSousFormeWithPatch() throws Exception {
        // Initialize the database
        sousFormeRepository.saveAndFlush(sousForme);

        int databaseSizeBeforeUpdate = sousFormeRepository.findAll().size();

        // Update the sousForme using partial update
        SousForme partialUpdatedSousForme = new SousForme();
        partialUpdatedSousForme.setId(sousForme.getId());

        partialUpdatedSousForme.nomLatin(UPDATED_NOM_LATIN);

        restSousFormeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSousForme.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSousForme))
            )
            .andExpect(status().isOk());

        // Validate the SousForme in the database
        List<SousForme> sousFormeList = sousFormeRepository.findAll();
        assertThat(sousFormeList).hasSize(databaseSizeBeforeUpdate);
        SousForme testSousForme = sousFormeList.get(sousFormeList.size() - 1);
        assertThat(testSousForme.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testSousForme.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void fullUpdateSousFormeWithPatch() throws Exception {
        // Initialize the database
        sousFormeRepository.saveAndFlush(sousForme);

        int databaseSizeBeforeUpdate = sousFormeRepository.findAll().size();

        // Update the sousForme using partial update
        SousForme partialUpdatedSousForme = new SousForme();
        partialUpdatedSousForme.setId(sousForme.getId());

        partialUpdatedSousForme.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restSousFormeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSousForme.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSousForme))
            )
            .andExpect(status().isOk());

        // Validate the SousForme in the database
        List<SousForme> sousFormeList = sousFormeRepository.findAll();
        assertThat(sousFormeList).hasSize(databaseSizeBeforeUpdate);
        SousForme testSousForme = sousFormeList.get(sousFormeList.size() - 1);
        assertThat(testSousForme.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testSousForme.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void patchNonExistingSousForme() throws Exception {
        int databaseSizeBeforeUpdate = sousFormeRepository.findAll().size();
        sousForme.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSousFormeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sousForme.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sousForme))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousForme in the database
        List<SousForme> sousFormeList = sousFormeRepository.findAll();
        assertThat(sousFormeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSousForme() throws Exception {
        int databaseSizeBeforeUpdate = sousFormeRepository.findAll().size();
        sousForme.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousFormeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sousForme))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousForme in the database
        List<SousForme> sousFormeList = sousFormeRepository.findAll();
        assertThat(sousFormeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSousForme() throws Exception {
        int databaseSizeBeforeUpdate = sousFormeRepository.findAll().size();
        sousForme.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousFormeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sousForme))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SousForme in the database
        List<SousForme> sousFormeList = sousFormeRepository.findAll();
        assertThat(sousFormeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSousForme() throws Exception {
        // Initialize the database
        sousFormeRepository.saveAndFlush(sousForme);

        int databaseSizeBeforeDelete = sousFormeRepository.findAll().size();

        // Delete the sousForme
        restSousFormeMockMvc
            .perform(delete(ENTITY_API_URL_ID, sousForme.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SousForme> sousFormeList = sousFormeRepository.findAll();
        assertThat(sousFormeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
