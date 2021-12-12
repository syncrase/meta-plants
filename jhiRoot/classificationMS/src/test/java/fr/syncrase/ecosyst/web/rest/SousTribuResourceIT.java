package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.Genre;
import fr.syncrase.ecosyst.domain.SousTribu;
import fr.syncrase.ecosyst.domain.SousTribu;
import fr.syncrase.ecosyst.domain.Tribu;
import fr.syncrase.ecosyst.repository.SousTribuRepository;
import fr.syncrase.ecosyst.service.criteria.SousTribuCriteria;
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
 * Integration tests for the {@link SousTribuResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SousTribuResourceIT {

    private static final String DEFAULT_NOM_FR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_LATIN = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LATIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sous-tribus";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SousTribuRepository sousTribuRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSousTribuMockMvc;

    private SousTribu sousTribu;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SousTribu createEntity(EntityManager em) {
        SousTribu sousTribu = new SousTribu().nomFr(DEFAULT_NOM_FR).nomLatin(DEFAULT_NOM_LATIN);
        return sousTribu;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SousTribu createUpdatedEntity(EntityManager em) {
        SousTribu sousTribu = new SousTribu().nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);
        return sousTribu;
    }

    @BeforeEach
    public void initTest() {
        sousTribu = createEntity(em);
    }

    @Test
    @Transactional
    void createSousTribu() throws Exception {
        int databaseSizeBeforeCreate = sousTribuRepository.findAll().size();
        // Create the SousTribu
        restSousTribuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousTribu)))
            .andExpect(status().isCreated());

        // Validate the SousTribu in the database
        List<SousTribu> sousTribuList = sousTribuRepository.findAll();
        assertThat(sousTribuList).hasSize(databaseSizeBeforeCreate + 1);
        SousTribu testSousTribu = sousTribuList.get(sousTribuList.size() - 1);
        assertThat(testSousTribu.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testSousTribu.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void createSousTribuWithExistingId() throws Exception {
        // Create the SousTribu with an existing ID
        sousTribu.setId(1L);

        int databaseSizeBeforeCreate = sousTribuRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSousTribuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousTribu)))
            .andExpect(status().isBadRequest());

        // Validate the SousTribu in the database
        List<SousTribu> sousTribuList = sousTribuRepository.findAll();
        assertThat(sousTribuList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomFrIsRequired() throws Exception {
        int databaseSizeBeforeTest = sousTribuRepository.findAll().size();
        // set the field null
        sousTribu.setNomFr(null);

        // Create the SousTribu, which fails.

        restSousTribuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousTribu)))
            .andExpect(status().isBadRequest());

        List<SousTribu> sousTribuList = sousTribuRepository.findAll();
        assertThat(sousTribuList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSousTribus() throws Exception {
        // Initialize the database
        sousTribuRepository.saveAndFlush(sousTribu);

        // Get all the sousTribuList
        restSousTribuMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sousTribu.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));
    }

    @Test
    @Transactional
    void getSousTribu() throws Exception {
        // Initialize the database
        sousTribuRepository.saveAndFlush(sousTribu);

        // Get the sousTribu
        restSousTribuMockMvc
            .perform(get(ENTITY_API_URL_ID, sousTribu.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sousTribu.getId().intValue()))
            .andExpect(jsonPath("$.nomFr").value(DEFAULT_NOM_FR))
            .andExpect(jsonPath("$.nomLatin").value(DEFAULT_NOM_LATIN));
    }

    @Test
    @Transactional
    void getSousTribusByIdFiltering() throws Exception {
        // Initialize the database
        sousTribuRepository.saveAndFlush(sousTribu);

        Long id = sousTribu.getId();

        defaultSousTribuShouldBeFound("id.equals=" + id);
        defaultSousTribuShouldNotBeFound("id.notEquals=" + id);

        defaultSousTribuShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSousTribuShouldNotBeFound("id.greaterThan=" + id);

        defaultSousTribuShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSousTribuShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSousTribusByNomFrIsEqualToSomething() throws Exception {
        // Initialize the database
        sousTribuRepository.saveAndFlush(sousTribu);

        // Get all the sousTribuList where nomFr equals to DEFAULT_NOM_FR
        defaultSousTribuShouldBeFound("nomFr.equals=" + DEFAULT_NOM_FR);

        // Get all the sousTribuList where nomFr equals to UPDATED_NOM_FR
        defaultSousTribuShouldNotBeFound("nomFr.equals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousTribusByNomFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sousTribuRepository.saveAndFlush(sousTribu);

        // Get all the sousTribuList where nomFr not equals to DEFAULT_NOM_FR
        defaultSousTribuShouldNotBeFound("nomFr.notEquals=" + DEFAULT_NOM_FR);

        // Get all the sousTribuList where nomFr not equals to UPDATED_NOM_FR
        defaultSousTribuShouldBeFound("nomFr.notEquals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousTribusByNomFrIsInShouldWork() throws Exception {
        // Initialize the database
        sousTribuRepository.saveAndFlush(sousTribu);

        // Get all the sousTribuList where nomFr in DEFAULT_NOM_FR or UPDATED_NOM_FR
        defaultSousTribuShouldBeFound("nomFr.in=" + DEFAULT_NOM_FR + "," + UPDATED_NOM_FR);

        // Get all the sousTribuList where nomFr equals to UPDATED_NOM_FR
        defaultSousTribuShouldNotBeFound("nomFr.in=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousTribusByNomFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        sousTribuRepository.saveAndFlush(sousTribu);

        // Get all the sousTribuList where nomFr is not null
        defaultSousTribuShouldBeFound("nomFr.specified=true");

        // Get all the sousTribuList where nomFr is null
        defaultSousTribuShouldNotBeFound("nomFr.specified=false");
    }

    @Test
    @Transactional
    void getAllSousTribusByNomFrContainsSomething() throws Exception {
        // Initialize the database
        sousTribuRepository.saveAndFlush(sousTribu);

        // Get all the sousTribuList where nomFr contains DEFAULT_NOM_FR
        defaultSousTribuShouldBeFound("nomFr.contains=" + DEFAULT_NOM_FR);

        // Get all the sousTribuList where nomFr contains UPDATED_NOM_FR
        defaultSousTribuShouldNotBeFound("nomFr.contains=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousTribusByNomFrNotContainsSomething() throws Exception {
        // Initialize the database
        sousTribuRepository.saveAndFlush(sousTribu);

        // Get all the sousTribuList where nomFr does not contain DEFAULT_NOM_FR
        defaultSousTribuShouldNotBeFound("nomFr.doesNotContain=" + DEFAULT_NOM_FR);

        // Get all the sousTribuList where nomFr does not contain UPDATED_NOM_FR
        defaultSousTribuShouldBeFound("nomFr.doesNotContain=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousTribusByNomLatinIsEqualToSomething() throws Exception {
        // Initialize the database
        sousTribuRepository.saveAndFlush(sousTribu);

        // Get all the sousTribuList where nomLatin equals to DEFAULT_NOM_LATIN
        defaultSousTribuShouldBeFound("nomLatin.equals=" + DEFAULT_NOM_LATIN);

        // Get all the sousTribuList where nomLatin equals to UPDATED_NOM_LATIN
        defaultSousTribuShouldNotBeFound("nomLatin.equals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousTribusByNomLatinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sousTribuRepository.saveAndFlush(sousTribu);

        // Get all the sousTribuList where nomLatin not equals to DEFAULT_NOM_LATIN
        defaultSousTribuShouldNotBeFound("nomLatin.notEquals=" + DEFAULT_NOM_LATIN);

        // Get all the sousTribuList where nomLatin not equals to UPDATED_NOM_LATIN
        defaultSousTribuShouldBeFound("nomLatin.notEquals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousTribusByNomLatinIsInShouldWork() throws Exception {
        // Initialize the database
        sousTribuRepository.saveAndFlush(sousTribu);

        // Get all the sousTribuList where nomLatin in DEFAULT_NOM_LATIN or UPDATED_NOM_LATIN
        defaultSousTribuShouldBeFound("nomLatin.in=" + DEFAULT_NOM_LATIN + "," + UPDATED_NOM_LATIN);

        // Get all the sousTribuList where nomLatin equals to UPDATED_NOM_LATIN
        defaultSousTribuShouldNotBeFound("nomLatin.in=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousTribusByNomLatinIsNullOrNotNull() throws Exception {
        // Initialize the database
        sousTribuRepository.saveAndFlush(sousTribu);

        // Get all the sousTribuList where nomLatin is not null
        defaultSousTribuShouldBeFound("nomLatin.specified=true");

        // Get all the sousTribuList where nomLatin is null
        defaultSousTribuShouldNotBeFound("nomLatin.specified=false");
    }

    @Test
    @Transactional
    void getAllSousTribusByNomLatinContainsSomething() throws Exception {
        // Initialize the database
        sousTribuRepository.saveAndFlush(sousTribu);

        // Get all the sousTribuList where nomLatin contains DEFAULT_NOM_LATIN
        defaultSousTribuShouldBeFound("nomLatin.contains=" + DEFAULT_NOM_LATIN);

        // Get all the sousTribuList where nomLatin contains UPDATED_NOM_LATIN
        defaultSousTribuShouldNotBeFound("nomLatin.contains=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousTribusByNomLatinNotContainsSomething() throws Exception {
        // Initialize the database
        sousTribuRepository.saveAndFlush(sousTribu);

        // Get all the sousTribuList where nomLatin does not contain DEFAULT_NOM_LATIN
        defaultSousTribuShouldNotBeFound("nomLatin.doesNotContain=" + DEFAULT_NOM_LATIN);

        // Get all the sousTribuList where nomLatin does not contain UPDATED_NOM_LATIN
        defaultSousTribuShouldBeFound("nomLatin.doesNotContain=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousTribusByGenresIsEqualToSomething() throws Exception {
        // Initialize the database
        sousTribuRepository.saveAndFlush(sousTribu);
        Genre genres;
        if (TestUtil.findAll(em, Genre.class).isEmpty()) {
            genres = GenreResourceIT.createEntity(em);
            em.persist(genres);
            em.flush();
        } else {
            genres = TestUtil.findAll(em, Genre.class).get(0);
        }
        em.persist(genres);
        em.flush();
        sousTribu.addGenres(genres);
        sousTribuRepository.saveAndFlush(sousTribu);
        Long genresId = genres.getId();

        // Get all the sousTribuList where genres equals to genresId
        defaultSousTribuShouldBeFound("genresId.equals=" + genresId);

        // Get all the sousTribuList where genres equals to (genresId + 1)
        defaultSousTribuShouldNotBeFound("genresId.equals=" + (genresId + 1));
    }

    @Test
    @Transactional
    void getAllSousTribusBySynonymesIsEqualToSomething() throws Exception {
        // Initialize the database
        sousTribuRepository.saveAndFlush(sousTribu);
        SousTribu synonymes;
        if (TestUtil.findAll(em, SousTribu.class).isEmpty()) {
            synonymes = SousTribuResourceIT.createEntity(em);
            em.persist(synonymes);
            em.flush();
        } else {
            synonymes = TestUtil.findAll(em, SousTribu.class).get(0);
        }
        em.persist(synonymes);
        em.flush();
        sousTribu.addSynonymes(synonymes);
        sousTribuRepository.saveAndFlush(sousTribu);
        Long synonymesId = synonymes.getId();

        // Get all the sousTribuList where synonymes equals to synonymesId
        defaultSousTribuShouldBeFound("synonymesId.equals=" + synonymesId);

        // Get all the sousTribuList where synonymes equals to (synonymesId + 1)
        defaultSousTribuShouldNotBeFound("synonymesId.equals=" + (synonymesId + 1));
    }

    @Test
    @Transactional
    void getAllSousTribusByTribuIsEqualToSomething() throws Exception {
        // Initialize the database
        sousTribuRepository.saveAndFlush(sousTribu);
        Tribu tribu;
        if (TestUtil.findAll(em, Tribu.class).isEmpty()) {
            tribu = TribuResourceIT.createEntity(em);
            em.persist(tribu);
            em.flush();
        } else {
            tribu = TestUtil.findAll(em, Tribu.class).get(0);
        }
        em.persist(tribu);
        em.flush();
        sousTribu.setTribu(tribu);
        sousTribuRepository.saveAndFlush(sousTribu);
        Long tribuId = tribu.getId();

        // Get all the sousTribuList where tribu equals to tribuId
        defaultSousTribuShouldBeFound("tribuId.equals=" + tribuId);

        // Get all the sousTribuList where tribu equals to (tribuId + 1)
        defaultSousTribuShouldNotBeFound("tribuId.equals=" + (tribuId + 1));
    }

    @Test
    @Transactional
    void getAllSousTribusBySousTribuIsEqualToSomething() throws Exception {
        // Initialize the database
        sousTribuRepository.saveAndFlush(sousTribu);
        SousTribu sousTribu;
        if (TestUtil.findAll(em, SousTribu.class).isEmpty()) {
            sousTribu = SousTribuResourceIT.createEntity(em);
            em.persist(sousTribu);
            em.flush();
        } else {
            sousTribu = TestUtil.findAll(em, SousTribu.class).get(0);
        }
        em.persist(sousTribu);
        em.flush();
        sousTribu.setSousTribu(sousTribu);
        sousTribuRepository.saveAndFlush(sousTribu);
        Long sousTribuId = sousTribu.getId();

        // Get all the sousTribuList where sousTribu equals to sousTribuId
        defaultSousTribuShouldBeFound("sousTribuId.equals=" + sousTribuId);

        // Get all the sousTribuList where sousTribu equals to (sousTribuId + 1)
        defaultSousTribuShouldNotBeFound("sousTribuId.equals=" + (sousTribuId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSousTribuShouldBeFound(String filter) throws Exception {
        restSousTribuMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sousTribu.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));

        // Check, that the count call also returns 1
        restSousTribuMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSousTribuShouldNotBeFound(String filter) throws Exception {
        restSousTribuMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSousTribuMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSousTribu() throws Exception {
        // Get the sousTribu
        restSousTribuMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSousTribu() throws Exception {
        // Initialize the database
        sousTribuRepository.saveAndFlush(sousTribu);

        int databaseSizeBeforeUpdate = sousTribuRepository.findAll().size();

        // Update the sousTribu
        SousTribu updatedSousTribu = sousTribuRepository.findById(sousTribu.getId()).get();
        // Disconnect from session so that the updates on updatedSousTribu are not directly saved in db
        em.detach(updatedSousTribu);
        updatedSousTribu.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restSousTribuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSousTribu.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSousTribu))
            )
            .andExpect(status().isOk());

        // Validate the SousTribu in the database
        List<SousTribu> sousTribuList = sousTribuRepository.findAll();
        assertThat(sousTribuList).hasSize(databaseSizeBeforeUpdate);
        SousTribu testSousTribu = sousTribuList.get(sousTribuList.size() - 1);
        assertThat(testSousTribu.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testSousTribu.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void putNonExistingSousTribu() throws Exception {
        int databaseSizeBeforeUpdate = sousTribuRepository.findAll().size();
        sousTribu.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSousTribuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sousTribu.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sousTribu))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousTribu in the database
        List<SousTribu> sousTribuList = sousTribuRepository.findAll();
        assertThat(sousTribuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSousTribu() throws Exception {
        int databaseSizeBeforeUpdate = sousTribuRepository.findAll().size();
        sousTribu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousTribuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sousTribu))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousTribu in the database
        List<SousTribu> sousTribuList = sousTribuRepository.findAll();
        assertThat(sousTribuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSousTribu() throws Exception {
        int databaseSizeBeforeUpdate = sousTribuRepository.findAll().size();
        sousTribu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousTribuMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousTribu)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SousTribu in the database
        List<SousTribu> sousTribuList = sousTribuRepository.findAll();
        assertThat(sousTribuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSousTribuWithPatch() throws Exception {
        // Initialize the database
        sousTribuRepository.saveAndFlush(sousTribu);

        int databaseSizeBeforeUpdate = sousTribuRepository.findAll().size();

        // Update the sousTribu using partial update
        SousTribu partialUpdatedSousTribu = new SousTribu();
        partialUpdatedSousTribu.setId(sousTribu.getId());

        partialUpdatedSousTribu.nomFr(UPDATED_NOM_FR);

        restSousTribuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSousTribu.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSousTribu))
            )
            .andExpect(status().isOk());

        // Validate the SousTribu in the database
        List<SousTribu> sousTribuList = sousTribuRepository.findAll();
        assertThat(sousTribuList).hasSize(databaseSizeBeforeUpdate);
        SousTribu testSousTribu = sousTribuList.get(sousTribuList.size() - 1);
        assertThat(testSousTribu.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testSousTribu.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void fullUpdateSousTribuWithPatch() throws Exception {
        // Initialize the database
        sousTribuRepository.saveAndFlush(sousTribu);

        int databaseSizeBeforeUpdate = sousTribuRepository.findAll().size();

        // Update the sousTribu using partial update
        SousTribu partialUpdatedSousTribu = new SousTribu();
        partialUpdatedSousTribu.setId(sousTribu.getId());

        partialUpdatedSousTribu.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restSousTribuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSousTribu.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSousTribu))
            )
            .andExpect(status().isOk());

        // Validate the SousTribu in the database
        List<SousTribu> sousTribuList = sousTribuRepository.findAll();
        assertThat(sousTribuList).hasSize(databaseSizeBeforeUpdate);
        SousTribu testSousTribu = sousTribuList.get(sousTribuList.size() - 1);
        assertThat(testSousTribu.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testSousTribu.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void patchNonExistingSousTribu() throws Exception {
        int databaseSizeBeforeUpdate = sousTribuRepository.findAll().size();
        sousTribu.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSousTribuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sousTribu.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sousTribu))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousTribu in the database
        List<SousTribu> sousTribuList = sousTribuRepository.findAll();
        assertThat(sousTribuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSousTribu() throws Exception {
        int databaseSizeBeforeUpdate = sousTribuRepository.findAll().size();
        sousTribu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousTribuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sousTribu))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousTribu in the database
        List<SousTribu> sousTribuList = sousTribuRepository.findAll();
        assertThat(sousTribuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSousTribu() throws Exception {
        int databaseSizeBeforeUpdate = sousTribuRepository.findAll().size();
        sousTribu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousTribuMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sousTribu))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SousTribu in the database
        List<SousTribu> sousTribuList = sousTribuRepository.findAll();
        assertThat(sousTribuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSousTribu() throws Exception {
        // Initialize the database
        sousTribuRepository.saveAndFlush(sousTribu);

        int databaseSizeBeforeDelete = sousTribuRepository.findAll().size();

        // Delete the sousTribu
        restSousTribuMockMvc
            .perform(delete(ENTITY_API_URL_ID, sousTribu.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SousTribu> sousTribuList = sousTribuRepository.findAll();
        assertThat(sousTribuList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
