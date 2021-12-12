package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.Forme;
import fr.syncrase.ecosyst.domain.SousVariete;
import fr.syncrase.ecosyst.domain.SousVariete;
import fr.syncrase.ecosyst.domain.Variete;
import fr.syncrase.ecosyst.repository.SousVarieteRepository;
import fr.syncrase.ecosyst.service.criteria.SousVarieteCriteria;
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
 * Integration tests for the {@link SousVarieteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SousVarieteResourceIT {

    private static final String DEFAULT_NOM_FR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_LATIN = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LATIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sous-varietes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SousVarieteRepository sousVarieteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSousVarieteMockMvc;

    private SousVariete sousVariete;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SousVariete createEntity(EntityManager em) {
        SousVariete sousVariete = new SousVariete().nomFr(DEFAULT_NOM_FR).nomLatin(DEFAULT_NOM_LATIN);
        return sousVariete;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SousVariete createUpdatedEntity(EntityManager em) {
        SousVariete sousVariete = new SousVariete().nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);
        return sousVariete;
    }

    @BeforeEach
    public void initTest() {
        sousVariete = createEntity(em);
    }

    @Test
    @Transactional
    void createSousVariete() throws Exception {
        int databaseSizeBeforeCreate = sousVarieteRepository.findAll().size();
        // Create the SousVariete
        restSousVarieteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousVariete)))
            .andExpect(status().isCreated());

        // Validate the SousVariete in the database
        List<SousVariete> sousVarieteList = sousVarieteRepository.findAll();
        assertThat(sousVarieteList).hasSize(databaseSizeBeforeCreate + 1);
        SousVariete testSousVariete = sousVarieteList.get(sousVarieteList.size() - 1);
        assertThat(testSousVariete.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testSousVariete.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void createSousVarieteWithExistingId() throws Exception {
        // Create the SousVariete with an existing ID
        sousVariete.setId(1L);

        int databaseSizeBeforeCreate = sousVarieteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSousVarieteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousVariete)))
            .andExpect(status().isBadRequest());

        // Validate the SousVariete in the database
        List<SousVariete> sousVarieteList = sousVarieteRepository.findAll();
        assertThat(sousVarieteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomFrIsRequired() throws Exception {
        int databaseSizeBeforeTest = sousVarieteRepository.findAll().size();
        // set the field null
        sousVariete.setNomFr(null);

        // Create the SousVariete, which fails.

        restSousVarieteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousVariete)))
            .andExpect(status().isBadRequest());

        List<SousVariete> sousVarieteList = sousVarieteRepository.findAll();
        assertThat(sousVarieteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSousVarietes() throws Exception {
        // Initialize the database
        sousVarieteRepository.saveAndFlush(sousVariete);

        // Get all the sousVarieteList
        restSousVarieteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sousVariete.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));
    }

    @Test
    @Transactional
    void getSousVariete() throws Exception {
        // Initialize the database
        sousVarieteRepository.saveAndFlush(sousVariete);

        // Get the sousVariete
        restSousVarieteMockMvc
            .perform(get(ENTITY_API_URL_ID, sousVariete.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sousVariete.getId().intValue()))
            .andExpect(jsonPath("$.nomFr").value(DEFAULT_NOM_FR))
            .andExpect(jsonPath("$.nomLatin").value(DEFAULT_NOM_LATIN));
    }

    @Test
    @Transactional
    void getSousVarietesByIdFiltering() throws Exception {
        // Initialize the database
        sousVarieteRepository.saveAndFlush(sousVariete);

        Long id = sousVariete.getId();

        defaultSousVarieteShouldBeFound("id.equals=" + id);
        defaultSousVarieteShouldNotBeFound("id.notEquals=" + id);

        defaultSousVarieteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSousVarieteShouldNotBeFound("id.greaterThan=" + id);

        defaultSousVarieteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSousVarieteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSousVarietesByNomFrIsEqualToSomething() throws Exception {
        // Initialize the database
        sousVarieteRepository.saveAndFlush(sousVariete);

        // Get all the sousVarieteList where nomFr equals to DEFAULT_NOM_FR
        defaultSousVarieteShouldBeFound("nomFr.equals=" + DEFAULT_NOM_FR);

        // Get all the sousVarieteList where nomFr equals to UPDATED_NOM_FR
        defaultSousVarieteShouldNotBeFound("nomFr.equals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousVarietesByNomFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sousVarieteRepository.saveAndFlush(sousVariete);

        // Get all the sousVarieteList where nomFr not equals to DEFAULT_NOM_FR
        defaultSousVarieteShouldNotBeFound("nomFr.notEquals=" + DEFAULT_NOM_FR);

        // Get all the sousVarieteList where nomFr not equals to UPDATED_NOM_FR
        defaultSousVarieteShouldBeFound("nomFr.notEquals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousVarietesByNomFrIsInShouldWork() throws Exception {
        // Initialize the database
        sousVarieteRepository.saveAndFlush(sousVariete);

        // Get all the sousVarieteList where nomFr in DEFAULT_NOM_FR or UPDATED_NOM_FR
        defaultSousVarieteShouldBeFound("nomFr.in=" + DEFAULT_NOM_FR + "," + UPDATED_NOM_FR);

        // Get all the sousVarieteList where nomFr equals to UPDATED_NOM_FR
        defaultSousVarieteShouldNotBeFound("nomFr.in=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousVarietesByNomFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        sousVarieteRepository.saveAndFlush(sousVariete);

        // Get all the sousVarieteList where nomFr is not null
        defaultSousVarieteShouldBeFound("nomFr.specified=true");

        // Get all the sousVarieteList where nomFr is null
        defaultSousVarieteShouldNotBeFound("nomFr.specified=false");
    }

    @Test
    @Transactional
    void getAllSousVarietesByNomFrContainsSomething() throws Exception {
        // Initialize the database
        sousVarieteRepository.saveAndFlush(sousVariete);

        // Get all the sousVarieteList where nomFr contains DEFAULT_NOM_FR
        defaultSousVarieteShouldBeFound("nomFr.contains=" + DEFAULT_NOM_FR);

        // Get all the sousVarieteList where nomFr contains UPDATED_NOM_FR
        defaultSousVarieteShouldNotBeFound("nomFr.contains=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousVarietesByNomFrNotContainsSomething() throws Exception {
        // Initialize the database
        sousVarieteRepository.saveAndFlush(sousVariete);

        // Get all the sousVarieteList where nomFr does not contain DEFAULT_NOM_FR
        defaultSousVarieteShouldNotBeFound("nomFr.doesNotContain=" + DEFAULT_NOM_FR);

        // Get all the sousVarieteList where nomFr does not contain UPDATED_NOM_FR
        defaultSousVarieteShouldBeFound("nomFr.doesNotContain=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousVarietesByNomLatinIsEqualToSomething() throws Exception {
        // Initialize the database
        sousVarieteRepository.saveAndFlush(sousVariete);

        // Get all the sousVarieteList where nomLatin equals to DEFAULT_NOM_LATIN
        defaultSousVarieteShouldBeFound("nomLatin.equals=" + DEFAULT_NOM_LATIN);

        // Get all the sousVarieteList where nomLatin equals to UPDATED_NOM_LATIN
        defaultSousVarieteShouldNotBeFound("nomLatin.equals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousVarietesByNomLatinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sousVarieteRepository.saveAndFlush(sousVariete);

        // Get all the sousVarieteList where nomLatin not equals to DEFAULT_NOM_LATIN
        defaultSousVarieteShouldNotBeFound("nomLatin.notEquals=" + DEFAULT_NOM_LATIN);

        // Get all the sousVarieteList where nomLatin not equals to UPDATED_NOM_LATIN
        defaultSousVarieteShouldBeFound("nomLatin.notEquals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousVarietesByNomLatinIsInShouldWork() throws Exception {
        // Initialize the database
        sousVarieteRepository.saveAndFlush(sousVariete);

        // Get all the sousVarieteList where nomLatin in DEFAULT_NOM_LATIN or UPDATED_NOM_LATIN
        defaultSousVarieteShouldBeFound("nomLatin.in=" + DEFAULT_NOM_LATIN + "," + UPDATED_NOM_LATIN);

        // Get all the sousVarieteList where nomLatin equals to UPDATED_NOM_LATIN
        defaultSousVarieteShouldNotBeFound("nomLatin.in=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousVarietesByNomLatinIsNullOrNotNull() throws Exception {
        // Initialize the database
        sousVarieteRepository.saveAndFlush(sousVariete);

        // Get all the sousVarieteList where nomLatin is not null
        defaultSousVarieteShouldBeFound("nomLatin.specified=true");

        // Get all the sousVarieteList where nomLatin is null
        defaultSousVarieteShouldNotBeFound("nomLatin.specified=false");
    }

    @Test
    @Transactional
    void getAllSousVarietesByNomLatinContainsSomething() throws Exception {
        // Initialize the database
        sousVarieteRepository.saveAndFlush(sousVariete);

        // Get all the sousVarieteList where nomLatin contains DEFAULT_NOM_LATIN
        defaultSousVarieteShouldBeFound("nomLatin.contains=" + DEFAULT_NOM_LATIN);

        // Get all the sousVarieteList where nomLatin contains UPDATED_NOM_LATIN
        defaultSousVarieteShouldNotBeFound("nomLatin.contains=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousVarietesByNomLatinNotContainsSomething() throws Exception {
        // Initialize the database
        sousVarieteRepository.saveAndFlush(sousVariete);

        // Get all the sousVarieteList where nomLatin does not contain DEFAULT_NOM_LATIN
        defaultSousVarieteShouldNotBeFound("nomLatin.doesNotContain=" + DEFAULT_NOM_LATIN);

        // Get all the sousVarieteList where nomLatin does not contain UPDATED_NOM_LATIN
        defaultSousVarieteShouldBeFound("nomLatin.doesNotContain=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousVarietesByFormesIsEqualToSomething() throws Exception {
        // Initialize the database
        sousVarieteRepository.saveAndFlush(sousVariete);
        Forme formes;
        if (TestUtil.findAll(em, Forme.class).isEmpty()) {
            formes = FormeResourceIT.createEntity(em);
            em.persist(formes);
            em.flush();
        } else {
            formes = TestUtil.findAll(em, Forme.class).get(0);
        }
        em.persist(formes);
        em.flush();
        sousVariete.addFormes(formes);
        sousVarieteRepository.saveAndFlush(sousVariete);
        Long formesId = formes.getId();

        // Get all the sousVarieteList where formes equals to formesId
        defaultSousVarieteShouldBeFound("formesId.equals=" + formesId);

        // Get all the sousVarieteList where formes equals to (formesId + 1)
        defaultSousVarieteShouldNotBeFound("formesId.equals=" + (formesId + 1));
    }

    @Test
    @Transactional
    void getAllSousVarietesBySynonymesIsEqualToSomething() throws Exception {
        // Initialize the database
        sousVarieteRepository.saveAndFlush(sousVariete);
        SousVariete synonymes;
        if (TestUtil.findAll(em, SousVariete.class).isEmpty()) {
            synonymes = SousVarieteResourceIT.createEntity(em);
            em.persist(synonymes);
            em.flush();
        } else {
            synonymes = TestUtil.findAll(em, SousVariete.class).get(0);
        }
        em.persist(synonymes);
        em.flush();
        sousVariete.addSynonymes(synonymes);
        sousVarieteRepository.saveAndFlush(sousVariete);
        Long synonymesId = synonymes.getId();

        // Get all the sousVarieteList where synonymes equals to synonymesId
        defaultSousVarieteShouldBeFound("synonymesId.equals=" + synonymesId);

        // Get all the sousVarieteList where synonymes equals to (synonymesId + 1)
        defaultSousVarieteShouldNotBeFound("synonymesId.equals=" + (synonymesId + 1));
    }

    @Test
    @Transactional
    void getAllSousVarietesByVarieteIsEqualToSomething() throws Exception {
        // Initialize the database
        sousVarieteRepository.saveAndFlush(sousVariete);
        Variete variete;
        if (TestUtil.findAll(em, Variete.class).isEmpty()) {
            variete = VarieteResourceIT.createEntity(em);
            em.persist(variete);
            em.flush();
        } else {
            variete = TestUtil.findAll(em, Variete.class).get(0);
        }
        em.persist(variete);
        em.flush();
        sousVariete.setVariete(variete);
        sousVarieteRepository.saveAndFlush(sousVariete);
        Long varieteId = variete.getId();

        // Get all the sousVarieteList where variete equals to varieteId
        defaultSousVarieteShouldBeFound("varieteId.equals=" + varieteId);

        // Get all the sousVarieteList where variete equals to (varieteId + 1)
        defaultSousVarieteShouldNotBeFound("varieteId.equals=" + (varieteId + 1));
    }

    @Test
    @Transactional
    void getAllSousVarietesBySousVarieteIsEqualToSomething() throws Exception {
        // Initialize the database
        sousVarieteRepository.saveAndFlush(sousVariete);
        SousVariete sousVariete;
        if (TestUtil.findAll(em, SousVariete.class).isEmpty()) {
            sousVariete = SousVarieteResourceIT.createEntity(em);
            em.persist(sousVariete);
            em.flush();
        } else {
            sousVariete = TestUtil.findAll(em, SousVariete.class).get(0);
        }
        em.persist(sousVariete);
        em.flush();
        sousVariete.setSousVariete(sousVariete);
        sousVarieteRepository.saveAndFlush(sousVariete);
        Long sousVarieteId = sousVariete.getId();

        // Get all the sousVarieteList where sousVariete equals to sousVarieteId
        defaultSousVarieteShouldBeFound("sousVarieteId.equals=" + sousVarieteId);

        // Get all the sousVarieteList where sousVariete equals to (sousVarieteId + 1)
        defaultSousVarieteShouldNotBeFound("sousVarieteId.equals=" + (sousVarieteId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSousVarieteShouldBeFound(String filter) throws Exception {
        restSousVarieteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sousVariete.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));

        // Check, that the count call also returns 1
        restSousVarieteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSousVarieteShouldNotBeFound(String filter) throws Exception {
        restSousVarieteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSousVarieteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSousVariete() throws Exception {
        // Get the sousVariete
        restSousVarieteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSousVariete() throws Exception {
        // Initialize the database
        sousVarieteRepository.saveAndFlush(sousVariete);

        int databaseSizeBeforeUpdate = sousVarieteRepository.findAll().size();

        // Update the sousVariete
        SousVariete updatedSousVariete = sousVarieteRepository.findById(sousVariete.getId()).get();
        // Disconnect from session so that the updates on updatedSousVariete are not directly saved in db
        em.detach(updatedSousVariete);
        updatedSousVariete.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restSousVarieteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSousVariete.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSousVariete))
            )
            .andExpect(status().isOk());

        // Validate the SousVariete in the database
        List<SousVariete> sousVarieteList = sousVarieteRepository.findAll();
        assertThat(sousVarieteList).hasSize(databaseSizeBeforeUpdate);
        SousVariete testSousVariete = sousVarieteList.get(sousVarieteList.size() - 1);
        assertThat(testSousVariete.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testSousVariete.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void putNonExistingSousVariete() throws Exception {
        int databaseSizeBeforeUpdate = sousVarieteRepository.findAll().size();
        sousVariete.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSousVarieteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sousVariete.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sousVariete))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousVariete in the database
        List<SousVariete> sousVarieteList = sousVarieteRepository.findAll();
        assertThat(sousVarieteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSousVariete() throws Exception {
        int databaseSizeBeforeUpdate = sousVarieteRepository.findAll().size();
        sousVariete.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousVarieteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sousVariete))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousVariete in the database
        List<SousVariete> sousVarieteList = sousVarieteRepository.findAll();
        assertThat(sousVarieteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSousVariete() throws Exception {
        int databaseSizeBeforeUpdate = sousVarieteRepository.findAll().size();
        sousVariete.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousVarieteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousVariete)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SousVariete in the database
        List<SousVariete> sousVarieteList = sousVarieteRepository.findAll();
        assertThat(sousVarieteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSousVarieteWithPatch() throws Exception {
        // Initialize the database
        sousVarieteRepository.saveAndFlush(sousVariete);

        int databaseSizeBeforeUpdate = sousVarieteRepository.findAll().size();

        // Update the sousVariete using partial update
        SousVariete partialUpdatedSousVariete = new SousVariete();
        partialUpdatedSousVariete.setId(sousVariete.getId());

        restSousVarieteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSousVariete.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSousVariete))
            )
            .andExpect(status().isOk());

        // Validate the SousVariete in the database
        List<SousVariete> sousVarieteList = sousVarieteRepository.findAll();
        assertThat(sousVarieteList).hasSize(databaseSizeBeforeUpdate);
        SousVariete testSousVariete = sousVarieteList.get(sousVarieteList.size() - 1);
        assertThat(testSousVariete.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testSousVariete.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void fullUpdateSousVarieteWithPatch() throws Exception {
        // Initialize the database
        sousVarieteRepository.saveAndFlush(sousVariete);

        int databaseSizeBeforeUpdate = sousVarieteRepository.findAll().size();

        // Update the sousVariete using partial update
        SousVariete partialUpdatedSousVariete = new SousVariete();
        partialUpdatedSousVariete.setId(sousVariete.getId());

        partialUpdatedSousVariete.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restSousVarieteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSousVariete.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSousVariete))
            )
            .andExpect(status().isOk());

        // Validate the SousVariete in the database
        List<SousVariete> sousVarieteList = sousVarieteRepository.findAll();
        assertThat(sousVarieteList).hasSize(databaseSizeBeforeUpdate);
        SousVariete testSousVariete = sousVarieteList.get(sousVarieteList.size() - 1);
        assertThat(testSousVariete.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testSousVariete.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void patchNonExistingSousVariete() throws Exception {
        int databaseSizeBeforeUpdate = sousVarieteRepository.findAll().size();
        sousVariete.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSousVarieteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sousVariete.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sousVariete))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousVariete in the database
        List<SousVariete> sousVarieteList = sousVarieteRepository.findAll();
        assertThat(sousVarieteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSousVariete() throws Exception {
        int databaseSizeBeforeUpdate = sousVarieteRepository.findAll().size();
        sousVariete.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousVarieteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sousVariete))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousVariete in the database
        List<SousVariete> sousVarieteList = sousVarieteRepository.findAll();
        assertThat(sousVarieteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSousVariete() throws Exception {
        int databaseSizeBeforeUpdate = sousVarieteRepository.findAll().size();
        sousVariete.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousVarieteMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sousVariete))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SousVariete in the database
        List<SousVariete> sousVarieteList = sousVarieteRepository.findAll();
        assertThat(sousVarieteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSousVariete() throws Exception {
        // Initialize the database
        sousVarieteRepository.saveAndFlush(sousVariete);

        int databaseSizeBeforeDelete = sousVarieteRepository.findAll().size();

        // Delete the sousVariete
        restSousVarieteMockMvc
            .perform(delete(ENTITY_API_URL_ID, sousVariete.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SousVariete> sousVarieteList = sousVarieteRepository.findAll();
        assertThat(sousVarieteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
