package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.Rameau;
import fr.syncrase.ecosyst.domain.Regne;
import fr.syncrase.ecosyst.domain.SousRegne;
import fr.syncrase.ecosyst.domain.SousRegne;
import fr.syncrase.ecosyst.repository.SousRegneRepository;
import fr.syncrase.ecosyst.service.criteria.SousRegneCriteria;
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
 * Integration tests for the {@link SousRegneResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SousRegneResourceIT {

    private static final String DEFAULT_NOM_FR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_LATIN = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LATIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sous-regnes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SousRegneRepository sousRegneRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSousRegneMockMvc;

    private SousRegne sousRegne;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SousRegne createEntity(EntityManager em) {
        SousRegne sousRegne = new SousRegne().nomFr(DEFAULT_NOM_FR).nomLatin(DEFAULT_NOM_LATIN);
        return sousRegne;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SousRegne createUpdatedEntity(EntityManager em) {
        SousRegne sousRegne = new SousRegne().nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);
        return sousRegne;
    }

    @BeforeEach
    public void initTest() {
        sousRegne = createEntity(em);
    }

    @Test
    @Transactional
    void createSousRegne() throws Exception {
        int databaseSizeBeforeCreate = sousRegneRepository.findAll().size();
        // Create the SousRegne
        restSousRegneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousRegne)))
            .andExpect(status().isCreated());

        // Validate the SousRegne in the database
        List<SousRegne> sousRegneList = sousRegneRepository.findAll();
        assertThat(sousRegneList).hasSize(databaseSizeBeforeCreate + 1);
        SousRegne testSousRegne = sousRegneList.get(sousRegneList.size() - 1);
        assertThat(testSousRegne.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testSousRegne.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void createSousRegneWithExistingId() throws Exception {
        // Create the SousRegne with an existing ID
        sousRegne.setId(1L);

        int databaseSizeBeforeCreate = sousRegneRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSousRegneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousRegne)))
            .andExpect(status().isBadRequest());

        // Validate the SousRegne in the database
        List<SousRegne> sousRegneList = sousRegneRepository.findAll();
        assertThat(sousRegneList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomFrIsRequired() throws Exception {
        int databaseSizeBeforeTest = sousRegneRepository.findAll().size();
        // set the field null
        sousRegne.setNomFr(null);

        // Create the SousRegne, which fails.

        restSousRegneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousRegne)))
            .andExpect(status().isBadRequest());

        List<SousRegne> sousRegneList = sousRegneRepository.findAll();
        assertThat(sousRegneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSousRegnes() throws Exception {
        // Initialize the database
        sousRegneRepository.saveAndFlush(sousRegne);

        // Get all the sousRegneList
        restSousRegneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sousRegne.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));
    }

    @Test
    @Transactional
    void getSousRegne() throws Exception {
        // Initialize the database
        sousRegneRepository.saveAndFlush(sousRegne);

        // Get the sousRegne
        restSousRegneMockMvc
            .perform(get(ENTITY_API_URL_ID, sousRegne.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sousRegne.getId().intValue()))
            .andExpect(jsonPath("$.nomFr").value(DEFAULT_NOM_FR))
            .andExpect(jsonPath("$.nomLatin").value(DEFAULT_NOM_LATIN));
    }

    @Test
    @Transactional
    void getSousRegnesByIdFiltering() throws Exception {
        // Initialize the database
        sousRegneRepository.saveAndFlush(sousRegne);

        Long id = sousRegne.getId();

        defaultSousRegneShouldBeFound("id.equals=" + id);
        defaultSousRegneShouldNotBeFound("id.notEquals=" + id);

        defaultSousRegneShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSousRegneShouldNotBeFound("id.greaterThan=" + id);

        defaultSousRegneShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSousRegneShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSousRegnesByNomFrIsEqualToSomething() throws Exception {
        // Initialize the database
        sousRegneRepository.saveAndFlush(sousRegne);

        // Get all the sousRegneList where nomFr equals to DEFAULT_NOM_FR
        defaultSousRegneShouldBeFound("nomFr.equals=" + DEFAULT_NOM_FR);

        // Get all the sousRegneList where nomFr equals to UPDATED_NOM_FR
        defaultSousRegneShouldNotBeFound("nomFr.equals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousRegnesByNomFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sousRegneRepository.saveAndFlush(sousRegne);

        // Get all the sousRegneList where nomFr not equals to DEFAULT_NOM_FR
        defaultSousRegneShouldNotBeFound("nomFr.notEquals=" + DEFAULT_NOM_FR);

        // Get all the sousRegneList where nomFr not equals to UPDATED_NOM_FR
        defaultSousRegneShouldBeFound("nomFr.notEquals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousRegnesByNomFrIsInShouldWork() throws Exception {
        // Initialize the database
        sousRegneRepository.saveAndFlush(sousRegne);

        // Get all the sousRegneList where nomFr in DEFAULT_NOM_FR or UPDATED_NOM_FR
        defaultSousRegneShouldBeFound("nomFr.in=" + DEFAULT_NOM_FR + "," + UPDATED_NOM_FR);

        // Get all the sousRegneList where nomFr equals to UPDATED_NOM_FR
        defaultSousRegneShouldNotBeFound("nomFr.in=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousRegnesByNomFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        sousRegneRepository.saveAndFlush(sousRegne);

        // Get all the sousRegneList where nomFr is not null
        defaultSousRegneShouldBeFound("nomFr.specified=true");

        // Get all the sousRegneList where nomFr is null
        defaultSousRegneShouldNotBeFound("nomFr.specified=false");
    }

    @Test
    @Transactional
    void getAllSousRegnesByNomFrContainsSomething() throws Exception {
        // Initialize the database
        sousRegneRepository.saveAndFlush(sousRegne);

        // Get all the sousRegneList where nomFr contains DEFAULT_NOM_FR
        defaultSousRegneShouldBeFound("nomFr.contains=" + DEFAULT_NOM_FR);

        // Get all the sousRegneList where nomFr contains UPDATED_NOM_FR
        defaultSousRegneShouldNotBeFound("nomFr.contains=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousRegnesByNomFrNotContainsSomething() throws Exception {
        // Initialize the database
        sousRegneRepository.saveAndFlush(sousRegne);

        // Get all the sousRegneList where nomFr does not contain DEFAULT_NOM_FR
        defaultSousRegneShouldNotBeFound("nomFr.doesNotContain=" + DEFAULT_NOM_FR);

        // Get all the sousRegneList where nomFr does not contain UPDATED_NOM_FR
        defaultSousRegneShouldBeFound("nomFr.doesNotContain=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousRegnesByNomLatinIsEqualToSomething() throws Exception {
        // Initialize the database
        sousRegneRepository.saveAndFlush(sousRegne);

        // Get all the sousRegneList where nomLatin equals to DEFAULT_NOM_LATIN
        defaultSousRegneShouldBeFound("nomLatin.equals=" + DEFAULT_NOM_LATIN);

        // Get all the sousRegneList where nomLatin equals to UPDATED_NOM_LATIN
        defaultSousRegneShouldNotBeFound("nomLatin.equals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousRegnesByNomLatinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sousRegneRepository.saveAndFlush(sousRegne);

        // Get all the sousRegneList where nomLatin not equals to DEFAULT_NOM_LATIN
        defaultSousRegneShouldNotBeFound("nomLatin.notEquals=" + DEFAULT_NOM_LATIN);

        // Get all the sousRegneList where nomLatin not equals to UPDATED_NOM_LATIN
        defaultSousRegneShouldBeFound("nomLatin.notEquals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousRegnesByNomLatinIsInShouldWork() throws Exception {
        // Initialize the database
        sousRegneRepository.saveAndFlush(sousRegne);

        // Get all the sousRegneList where nomLatin in DEFAULT_NOM_LATIN or UPDATED_NOM_LATIN
        defaultSousRegneShouldBeFound("nomLatin.in=" + DEFAULT_NOM_LATIN + "," + UPDATED_NOM_LATIN);

        // Get all the sousRegneList where nomLatin equals to UPDATED_NOM_LATIN
        defaultSousRegneShouldNotBeFound("nomLatin.in=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousRegnesByNomLatinIsNullOrNotNull() throws Exception {
        // Initialize the database
        sousRegneRepository.saveAndFlush(sousRegne);

        // Get all the sousRegneList where nomLatin is not null
        defaultSousRegneShouldBeFound("nomLatin.specified=true");

        // Get all the sousRegneList where nomLatin is null
        defaultSousRegneShouldNotBeFound("nomLatin.specified=false");
    }

    @Test
    @Transactional
    void getAllSousRegnesByNomLatinContainsSomething() throws Exception {
        // Initialize the database
        sousRegneRepository.saveAndFlush(sousRegne);

        // Get all the sousRegneList where nomLatin contains DEFAULT_NOM_LATIN
        defaultSousRegneShouldBeFound("nomLatin.contains=" + DEFAULT_NOM_LATIN);

        // Get all the sousRegneList where nomLatin contains UPDATED_NOM_LATIN
        defaultSousRegneShouldNotBeFound("nomLatin.contains=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousRegnesByNomLatinNotContainsSomething() throws Exception {
        // Initialize the database
        sousRegneRepository.saveAndFlush(sousRegne);

        // Get all the sousRegneList where nomLatin does not contain DEFAULT_NOM_LATIN
        defaultSousRegneShouldNotBeFound("nomLatin.doesNotContain=" + DEFAULT_NOM_LATIN);

        // Get all the sousRegneList where nomLatin does not contain UPDATED_NOM_LATIN
        defaultSousRegneShouldBeFound("nomLatin.doesNotContain=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousRegnesByRameausIsEqualToSomething() throws Exception {
        // Initialize the database
        sousRegneRepository.saveAndFlush(sousRegne);
        Rameau rameaus;
        if (TestUtil.findAll(em, Rameau.class).isEmpty()) {
            rameaus = RameauResourceIT.createEntity(em);
            em.persist(rameaus);
            em.flush();
        } else {
            rameaus = TestUtil.findAll(em, Rameau.class).get(0);
        }
        em.persist(rameaus);
        em.flush();
        sousRegne.addRameaus(rameaus);
        sousRegneRepository.saveAndFlush(sousRegne);
        Long rameausId = rameaus.getId();

        // Get all the sousRegneList where rameaus equals to rameausId
        defaultSousRegneShouldBeFound("rameausId.equals=" + rameausId);

        // Get all the sousRegneList where rameaus equals to (rameausId + 1)
        defaultSousRegneShouldNotBeFound("rameausId.equals=" + (rameausId + 1));
    }

    @Test
    @Transactional
    void getAllSousRegnesBySynonymesIsEqualToSomething() throws Exception {
        // Initialize the database
        sousRegneRepository.saveAndFlush(sousRegne);
        SousRegne synonymes;
        if (TestUtil.findAll(em, SousRegne.class).isEmpty()) {
            synonymes = SousRegneResourceIT.createEntity(em);
            em.persist(synonymes);
            em.flush();
        } else {
            synonymes = TestUtil.findAll(em, SousRegne.class).get(0);
        }
        em.persist(synonymes);
        em.flush();
        sousRegne.addSynonymes(synonymes);
        sousRegneRepository.saveAndFlush(sousRegne);
        Long synonymesId = synonymes.getId();

        // Get all the sousRegneList where synonymes equals to synonymesId
        defaultSousRegneShouldBeFound("synonymesId.equals=" + synonymesId);

        // Get all the sousRegneList where synonymes equals to (synonymesId + 1)
        defaultSousRegneShouldNotBeFound("synonymesId.equals=" + (synonymesId + 1));
    }

    @Test
    @Transactional
    void getAllSousRegnesByRegneIsEqualToSomething() throws Exception {
        // Initialize the database
        sousRegneRepository.saveAndFlush(sousRegne);
        Regne regne;
        if (TestUtil.findAll(em, Regne.class).isEmpty()) {
            regne = RegneResourceIT.createEntity(em);
            em.persist(regne);
            em.flush();
        } else {
            regne = TestUtil.findAll(em, Regne.class).get(0);
        }
        em.persist(regne);
        em.flush();
        sousRegne.setRegne(regne);
        sousRegneRepository.saveAndFlush(sousRegne);
        Long regneId = regne.getId();

        // Get all the sousRegneList where regne equals to regneId
        defaultSousRegneShouldBeFound("regneId.equals=" + regneId);

        // Get all the sousRegneList where regne equals to (regneId + 1)
        defaultSousRegneShouldNotBeFound("regneId.equals=" + (regneId + 1));
    }

    @Test
    @Transactional
    void getAllSousRegnesBySousRegneIsEqualToSomething() throws Exception {
        // Initialize the database
        sousRegneRepository.saveAndFlush(sousRegne);
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
        sousRegne.setSousRegne(sousRegne);
        sousRegneRepository.saveAndFlush(sousRegne);
        Long sousRegneId = sousRegne.getId();

        // Get all the sousRegneList where sousRegne equals to sousRegneId
        defaultSousRegneShouldBeFound("sousRegneId.equals=" + sousRegneId);

        // Get all the sousRegneList where sousRegne equals to (sousRegneId + 1)
        defaultSousRegneShouldNotBeFound("sousRegneId.equals=" + (sousRegneId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSousRegneShouldBeFound(String filter) throws Exception {
        restSousRegneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sousRegne.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));

        // Check, that the count call also returns 1
        restSousRegneMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSousRegneShouldNotBeFound(String filter) throws Exception {
        restSousRegneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSousRegneMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSousRegne() throws Exception {
        // Get the sousRegne
        restSousRegneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSousRegne() throws Exception {
        // Initialize the database
        sousRegneRepository.saveAndFlush(sousRegne);

        int databaseSizeBeforeUpdate = sousRegneRepository.findAll().size();

        // Update the sousRegne
        SousRegne updatedSousRegne = sousRegneRepository.findById(sousRegne.getId()).get();
        // Disconnect from session so that the updates on updatedSousRegne are not directly saved in db
        em.detach(updatedSousRegne);
        updatedSousRegne.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restSousRegneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSousRegne.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSousRegne))
            )
            .andExpect(status().isOk());

        // Validate the SousRegne in the database
        List<SousRegne> sousRegneList = sousRegneRepository.findAll();
        assertThat(sousRegneList).hasSize(databaseSizeBeforeUpdate);
        SousRegne testSousRegne = sousRegneList.get(sousRegneList.size() - 1);
        assertThat(testSousRegne.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testSousRegne.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void putNonExistingSousRegne() throws Exception {
        int databaseSizeBeforeUpdate = sousRegneRepository.findAll().size();
        sousRegne.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSousRegneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sousRegne.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sousRegne))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousRegne in the database
        List<SousRegne> sousRegneList = sousRegneRepository.findAll();
        assertThat(sousRegneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSousRegne() throws Exception {
        int databaseSizeBeforeUpdate = sousRegneRepository.findAll().size();
        sousRegne.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousRegneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sousRegne))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousRegne in the database
        List<SousRegne> sousRegneList = sousRegneRepository.findAll();
        assertThat(sousRegneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSousRegne() throws Exception {
        int databaseSizeBeforeUpdate = sousRegneRepository.findAll().size();
        sousRegne.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousRegneMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousRegne)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SousRegne in the database
        List<SousRegne> sousRegneList = sousRegneRepository.findAll();
        assertThat(sousRegneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSousRegneWithPatch() throws Exception {
        // Initialize the database
        sousRegneRepository.saveAndFlush(sousRegne);

        int databaseSizeBeforeUpdate = sousRegneRepository.findAll().size();

        // Update the sousRegne using partial update
        SousRegne partialUpdatedSousRegne = new SousRegne();
        partialUpdatedSousRegne.setId(sousRegne.getId());

        partialUpdatedSousRegne.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restSousRegneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSousRegne.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSousRegne))
            )
            .andExpect(status().isOk());

        // Validate the SousRegne in the database
        List<SousRegne> sousRegneList = sousRegneRepository.findAll();
        assertThat(sousRegneList).hasSize(databaseSizeBeforeUpdate);
        SousRegne testSousRegne = sousRegneList.get(sousRegneList.size() - 1);
        assertThat(testSousRegne.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testSousRegne.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void fullUpdateSousRegneWithPatch() throws Exception {
        // Initialize the database
        sousRegneRepository.saveAndFlush(sousRegne);

        int databaseSizeBeforeUpdate = sousRegneRepository.findAll().size();

        // Update the sousRegne using partial update
        SousRegne partialUpdatedSousRegne = new SousRegne();
        partialUpdatedSousRegne.setId(sousRegne.getId());

        partialUpdatedSousRegne.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restSousRegneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSousRegne.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSousRegne))
            )
            .andExpect(status().isOk());

        // Validate the SousRegne in the database
        List<SousRegne> sousRegneList = sousRegneRepository.findAll();
        assertThat(sousRegneList).hasSize(databaseSizeBeforeUpdate);
        SousRegne testSousRegne = sousRegneList.get(sousRegneList.size() - 1);
        assertThat(testSousRegne.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testSousRegne.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void patchNonExistingSousRegne() throws Exception {
        int databaseSizeBeforeUpdate = sousRegneRepository.findAll().size();
        sousRegne.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSousRegneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sousRegne.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sousRegne))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousRegne in the database
        List<SousRegne> sousRegneList = sousRegneRepository.findAll();
        assertThat(sousRegneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSousRegne() throws Exception {
        int databaseSizeBeforeUpdate = sousRegneRepository.findAll().size();
        sousRegne.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousRegneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sousRegne))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousRegne in the database
        List<SousRegne> sousRegneList = sousRegneRepository.findAll();
        assertThat(sousRegneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSousRegne() throws Exception {
        int databaseSizeBeforeUpdate = sousRegneRepository.findAll().size();
        sousRegne.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousRegneMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sousRegne))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SousRegne in the database
        List<SousRegne> sousRegneList = sousRegneRepository.findAll();
        assertThat(sousRegneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSousRegne() throws Exception {
        // Initialize the database
        sousRegneRepository.saveAndFlush(sousRegne);

        int databaseSizeBeforeDelete = sousRegneRepository.findAll().size();

        // Delete the sousRegne
        restSousRegneMockMvc
            .perform(delete(ENTITY_API_URL_ID, sousRegne.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SousRegne> sousRegneList = sousRegneRepository.findAll();
        assertThat(sousRegneList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
