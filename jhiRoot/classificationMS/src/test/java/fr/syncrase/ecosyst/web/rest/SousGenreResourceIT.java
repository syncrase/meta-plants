package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.Genre;
import fr.syncrase.ecosyst.domain.Section;
import fr.syncrase.ecosyst.domain.SousGenre;
import fr.syncrase.ecosyst.domain.SousGenre;
import fr.syncrase.ecosyst.repository.SousGenreRepository;
import fr.syncrase.ecosyst.service.criteria.SousGenreCriteria;
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
 * Integration tests for the {@link SousGenreResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SousGenreResourceIT {

    private static final String DEFAULT_NOM_FR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_LATIN = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LATIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sous-genres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SousGenreRepository sousGenreRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSousGenreMockMvc;

    private SousGenre sousGenre;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SousGenre createEntity(EntityManager em) {
        SousGenre sousGenre = new SousGenre().nomFr(DEFAULT_NOM_FR).nomLatin(DEFAULT_NOM_LATIN);
        return sousGenre;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SousGenre createUpdatedEntity(EntityManager em) {
        SousGenre sousGenre = new SousGenre().nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);
        return sousGenre;
    }

    @BeforeEach
    public void initTest() {
        sousGenre = createEntity(em);
    }

    @Test
    @Transactional
    void createSousGenre() throws Exception {
        int databaseSizeBeforeCreate = sousGenreRepository.findAll().size();
        // Create the SousGenre
        restSousGenreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousGenre)))
            .andExpect(status().isCreated());

        // Validate the SousGenre in the database
        List<SousGenre> sousGenreList = sousGenreRepository.findAll();
        assertThat(sousGenreList).hasSize(databaseSizeBeforeCreate + 1);
        SousGenre testSousGenre = sousGenreList.get(sousGenreList.size() - 1);
        assertThat(testSousGenre.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testSousGenre.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void createSousGenreWithExistingId() throws Exception {
        // Create the SousGenre with an existing ID
        sousGenre.setId(1L);

        int databaseSizeBeforeCreate = sousGenreRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSousGenreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousGenre)))
            .andExpect(status().isBadRequest());

        // Validate the SousGenre in the database
        List<SousGenre> sousGenreList = sousGenreRepository.findAll();
        assertThat(sousGenreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomFrIsRequired() throws Exception {
        int databaseSizeBeforeTest = sousGenreRepository.findAll().size();
        // set the field null
        sousGenre.setNomFr(null);

        // Create the SousGenre, which fails.

        restSousGenreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousGenre)))
            .andExpect(status().isBadRequest());

        List<SousGenre> sousGenreList = sousGenreRepository.findAll();
        assertThat(sousGenreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSousGenres() throws Exception {
        // Initialize the database
        sousGenreRepository.saveAndFlush(sousGenre);

        // Get all the sousGenreList
        restSousGenreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sousGenre.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));
    }

    @Test
    @Transactional
    void getSousGenre() throws Exception {
        // Initialize the database
        sousGenreRepository.saveAndFlush(sousGenre);

        // Get the sousGenre
        restSousGenreMockMvc
            .perform(get(ENTITY_API_URL_ID, sousGenre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sousGenre.getId().intValue()))
            .andExpect(jsonPath("$.nomFr").value(DEFAULT_NOM_FR))
            .andExpect(jsonPath("$.nomLatin").value(DEFAULT_NOM_LATIN));
    }

    @Test
    @Transactional
    void getSousGenresByIdFiltering() throws Exception {
        // Initialize the database
        sousGenreRepository.saveAndFlush(sousGenre);

        Long id = sousGenre.getId();

        defaultSousGenreShouldBeFound("id.equals=" + id);
        defaultSousGenreShouldNotBeFound("id.notEquals=" + id);

        defaultSousGenreShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSousGenreShouldNotBeFound("id.greaterThan=" + id);

        defaultSousGenreShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSousGenreShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSousGenresByNomFrIsEqualToSomething() throws Exception {
        // Initialize the database
        sousGenreRepository.saveAndFlush(sousGenre);

        // Get all the sousGenreList where nomFr equals to DEFAULT_NOM_FR
        defaultSousGenreShouldBeFound("nomFr.equals=" + DEFAULT_NOM_FR);

        // Get all the sousGenreList where nomFr equals to UPDATED_NOM_FR
        defaultSousGenreShouldNotBeFound("nomFr.equals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousGenresByNomFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sousGenreRepository.saveAndFlush(sousGenre);

        // Get all the sousGenreList where nomFr not equals to DEFAULT_NOM_FR
        defaultSousGenreShouldNotBeFound("nomFr.notEquals=" + DEFAULT_NOM_FR);

        // Get all the sousGenreList where nomFr not equals to UPDATED_NOM_FR
        defaultSousGenreShouldBeFound("nomFr.notEquals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousGenresByNomFrIsInShouldWork() throws Exception {
        // Initialize the database
        sousGenreRepository.saveAndFlush(sousGenre);

        // Get all the sousGenreList where nomFr in DEFAULT_NOM_FR or UPDATED_NOM_FR
        defaultSousGenreShouldBeFound("nomFr.in=" + DEFAULT_NOM_FR + "," + UPDATED_NOM_FR);

        // Get all the sousGenreList where nomFr equals to UPDATED_NOM_FR
        defaultSousGenreShouldNotBeFound("nomFr.in=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousGenresByNomFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        sousGenreRepository.saveAndFlush(sousGenre);

        // Get all the sousGenreList where nomFr is not null
        defaultSousGenreShouldBeFound("nomFr.specified=true");

        // Get all the sousGenreList where nomFr is null
        defaultSousGenreShouldNotBeFound("nomFr.specified=false");
    }

    @Test
    @Transactional
    void getAllSousGenresByNomFrContainsSomething() throws Exception {
        // Initialize the database
        sousGenreRepository.saveAndFlush(sousGenre);

        // Get all the sousGenreList where nomFr contains DEFAULT_NOM_FR
        defaultSousGenreShouldBeFound("nomFr.contains=" + DEFAULT_NOM_FR);

        // Get all the sousGenreList where nomFr contains UPDATED_NOM_FR
        defaultSousGenreShouldNotBeFound("nomFr.contains=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousGenresByNomFrNotContainsSomething() throws Exception {
        // Initialize the database
        sousGenreRepository.saveAndFlush(sousGenre);

        // Get all the sousGenreList where nomFr does not contain DEFAULT_NOM_FR
        defaultSousGenreShouldNotBeFound("nomFr.doesNotContain=" + DEFAULT_NOM_FR);

        // Get all the sousGenreList where nomFr does not contain UPDATED_NOM_FR
        defaultSousGenreShouldBeFound("nomFr.doesNotContain=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousGenresByNomLatinIsEqualToSomething() throws Exception {
        // Initialize the database
        sousGenreRepository.saveAndFlush(sousGenre);

        // Get all the sousGenreList where nomLatin equals to DEFAULT_NOM_LATIN
        defaultSousGenreShouldBeFound("nomLatin.equals=" + DEFAULT_NOM_LATIN);

        // Get all the sousGenreList where nomLatin equals to UPDATED_NOM_LATIN
        defaultSousGenreShouldNotBeFound("nomLatin.equals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousGenresByNomLatinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sousGenreRepository.saveAndFlush(sousGenre);

        // Get all the sousGenreList where nomLatin not equals to DEFAULT_NOM_LATIN
        defaultSousGenreShouldNotBeFound("nomLatin.notEquals=" + DEFAULT_NOM_LATIN);

        // Get all the sousGenreList where nomLatin not equals to UPDATED_NOM_LATIN
        defaultSousGenreShouldBeFound("nomLatin.notEquals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousGenresByNomLatinIsInShouldWork() throws Exception {
        // Initialize the database
        sousGenreRepository.saveAndFlush(sousGenre);

        // Get all the sousGenreList where nomLatin in DEFAULT_NOM_LATIN or UPDATED_NOM_LATIN
        defaultSousGenreShouldBeFound("nomLatin.in=" + DEFAULT_NOM_LATIN + "," + UPDATED_NOM_LATIN);

        // Get all the sousGenreList where nomLatin equals to UPDATED_NOM_LATIN
        defaultSousGenreShouldNotBeFound("nomLatin.in=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousGenresByNomLatinIsNullOrNotNull() throws Exception {
        // Initialize the database
        sousGenreRepository.saveAndFlush(sousGenre);

        // Get all the sousGenreList where nomLatin is not null
        defaultSousGenreShouldBeFound("nomLatin.specified=true");

        // Get all the sousGenreList where nomLatin is null
        defaultSousGenreShouldNotBeFound("nomLatin.specified=false");
    }

    @Test
    @Transactional
    void getAllSousGenresByNomLatinContainsSomething() throws Exception {
        // Initialize the database
        sousGenreRepository.saveAndFlush(sousGenre);

        // Get all the sousGenreList where nomLatin contains DEFAULT_NOM_LATIN
        defaultSousGenreShouldBeFound("nomLatin.contains=" + DEFAULT_NOM_LATIN);

        // Get all the sousGenreList where nomLatin contains UPDATED_NOM_LATIN
        defaultSousGenreShouldNotBeFound("nomLatin.contains=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousGenresByNomLatinNotContainsSomething() throws Exception {
        // Initialize the database
        sousGenreRepository.saveAndFlush(sousGenre);

        // Get all the sousGenreList where nomLatin does not contain DEFAULT_NOM_LATIN
        defaultSousGenreShouldNotBeFound("nomLatin.doesNotContain=" + DEFAULT_NOM_LATIN);

        // Get all the sousGenreList where nomLatin does not contain UPDATED_NOM_LATIN
        defaultSousGenreShouldBeFound("nomLatin.doesNotContain=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousGenresBySectionsIsEqualToSomething() throws Exception {
        // Initialize the database
        sousGenreRepository.saveAndFlush(sousGenre);
        Section sections;
        if (TestUtil.findAll(em, Section.class).isEmpty()) {
            sections = SectionResourceIT.createEntity(em);
            em.persist(sections);
            em.flush();
        } else {
            sections = TestUtil.findAll(em, Section.class).get(0);
        }
        em.persist(sections);
        em.flush();
        sousGenre.addSections(sections);
        sousGenreRepository.saveAndFlush(sousGenre);
        Long sectionsId = sections.getId();

        // Get all the sousGenreList where sections equals to sectionsId
        defaultSousGenreShouldBeFound("sectionsId.equals=" + sectionsId);

        // Get all the sousGenreList where sections equals to (sectionsId + 1)
        defaultSousGenreShouldNotBeFound("sectionsId.equals=" + (sectionsId + 1));
    }

    @Test
    @Transactional
    void getAllSousGenresBySynonymesIsEqualToSomething() throws Exception {
        // Initialize the database
        sousGenreRepository.saveAndFlush(sousGenre);
        SousGenre synonymes;
        if (TestUtil.findAll(em, SousGenre.class).isEmpty()) {
            synonymes = SousGenreResourceIT.createEntity(em);
            em.persist(synonymes);
            em.flush();
        } else {
            synonymes = TestUtil.findAll(em, SousGenre.class).get(0);
        }
        em.persist(synonymes);
        em.flush();
        sousGenre.addSynonymes(synonymes);
        sousGenreRepository.saveAndFlush(sousGenre);
        Long synonymesId = synonymes.getId();

        // Get all the sousGenreList where synonymes equals to synonymesId
        defaultSousGenreShouldBeFound("synonymesId.equals=" + synonymesId);

        // Get all the sousGenreList where synonymes equals to (synonymesId + 1)
        defaultSousGenreShouldNotBeFound("synonymesId.equals=" + (synonymesId + 1));
    }

    @Test
    @Transactional
    void getAllSousGenresByGenreIsEqualToSomething() throws Exception {
        // Initialize the database
        sousGenreRepository.saveAndFlush(sousGenre);
        Genre genre;
        if (TestUtil.findAll(em, Genre.class).isEmpty()) {
            genre = GenreResourceIT.createEntity(em);
            em.persist(genre);
            em.flush();
        } else {
            genre = TestUtil.findAll(em, Genre.class).get(0);
        }
        em.persist(genre);
        em.flush();
        sousGenre.setGenre(genre);
        sousGenreRepository.saveAndFlush(sousGenre);
        Long genreId = genre.getId();

        // Get all the sousGenreList where genre equals to genreId
        defaultSousGenreShouldBeFound("genreId.equals=" + genreId);

        // Get all the sousGenreList where genre equals to (genreId + 1)
        defaultSousGenreShouldNotBeFound("genreId.equals=" + (genreId + 1));
    }

    @Test
    @Transactional
    void getAllSousGenresBySousGenreIsEqualToSomething() throws Exception {
        // Initialize the database
        sousGenreRepository.saveAndFlush(sousGenre);
        SousGenre sousGenre;
        if (TestUtil.findAll(em, SousGenre.class).isEmpty()) {
            sousGenre = SousGenreResourceIT.createEntity(em);
            em.persist(sousGenre);
            em.flush();
        } else {
            sousGenre = TestUtil.findAll(em, SousGenre.class).get(0);
        }
        em.persist(sousGenre);
        em.flush();
        sousGenre.setSousGenre(sousGenre);
        sousGenreRepository.saveAndFlush(sousGenre);
        Long sousGenreId = sousGenre.getId();

        // Get all the sousGenreList where sousGenre equals to sousGenreId
        defaultSousGenreShouldBeFound("sousGenreId.equals=" + sousGenreId);

        // Get all the sousGenreList where sousGenre equals to (sousGenreId + 1)
        defaultSousGenreShouldNotBeFound("sousGenreId.equals=" + (sousGenreId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSousGenreShouldBeFound(String filter) throws Exception {
        restSousGenreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sousGenre.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));

        // Check, that the count call also returns 1
        restSousGenreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSousGenreShouldNotBeFound(String filter) throws Exception {
        restSousGenreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSousGenreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSousGenre() throws Exception {
        // Get the sousGenre
        restSousGenreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSousGenre() throws Exception {
        // Initialize the database
        sousGenreRepository.saveAndFlush(sousGenre);

        int databaseSizeBeforeUpdate = sousGenreRepository.findAll().size();

        // Update the sousGenre
        SousGenre updatedSousGenre = sousGenreRepository.findById(sousGenre.getId()).get();
        // Disconnect from session so that the updates on updatedSousGenre are not directly saved in db
        em.detach(updatedSousGenre);
        updatedSousGenre.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restSousGenreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSousGenre.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSousGenre))
            )
            .andExpect(status().isOk());

        // Validate the SousGenre in the database
        List<SousGenre> sousGenreList = sousGenreRepository.findAll();
        assertThat(sousGenreList).hasSize(databaseSizeBeforeUpdate);
        SousGenre testSousGenre = sousGenreList.get(sousGenreList.size() - 1);
        assertThat(testSousGenre.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testSousGenre.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void putNonExistingSousGenre() throws Exception {
        int databaseSizeBeforeUpdate = sousGenreRepository.findAll().size();
        sousGenre.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSousGenreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sousGenre.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sousGenre))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousGenre in the database
        List<SousGenre> sousGenreList = sousGenreRepository.findAll();
        assertThat(sousGenreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSousGenre() throws Exception {
        int databaseSizeBeforeUpdate = sousGenreRepository.findAll().size();
        sousGenre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousGenreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sousGenre))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousGenre in the database
        List<SousGenre> sousGenreList = sousGenreRepository.findAll();
        assertThat(sousGenreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSousGenre() throws Exception {
        int databaseSizeBeforeUpdate = sousGenreRepository.findAll().size();
        sousGenre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousGenreMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousGenre)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SousGenre in the database
        List<SousGenre> sousGenreList = sousGenreRepository.findAll();
        assertThat(sousGenreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSousGenreWithPatch() throws Exception {
        // Initialize the database
        sousGenreRepository.saveAndFlush(sousGenre);

        int databaseSizeBeforeUpdate = sousGenreRepository.findAll().size();

        // Update the sousGenre using partial update
        SousGenre partialUpdatedSousGenre = new SousGenre();
        partialUpdatedSousGenre.setId(sousGenre.getId());

        partialUpdatedSousGenre.nomFr(UPDATED_NOM_FR);

        restSousGenreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSousGenre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSousGenre))
            )
            .andExpect(status().isOk());

        // Validate the SousGenre in the database
        List<SousGenre> sousGenreList = sousGenreRepository.findAll();
        assertThat(sousGenreList).hasSize(databaseSizeBeforeUpdate);
        SousGenre testSousGenre = sousGenreList.get(sousGenreList.size() - 1);
        assertThat(testSousGenre.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testSousGenre.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void fullUpdateSousGenreWithPatch() throws Exception {
        // Initialize the database
        sousGenreRepository.saveAndFlush(sousGenre);

        int databaseSizeBeforeUpdate = sousGenreRepository.findAll().size();

        // Update the sousGenre using partial update
        SousGenre partialUpdatedSousGenre = new SousGenre();
        partialUpdatedSousGenre.setId(sousGenre.getId());

        partialUpdatedSousGenre.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restSousGenreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSousGenre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSousGenre))
            )
            .andExpect(status().isOk());

        // Validate the SousGenre in the database
        List<SousGenre> sousGenreList = sousGenreRepository.findAll();
        assertThat(sousGenreList).hasSize(databaseSizeBeforeUpdate);
        SousGenre testSousGenre = sousGenreList.get(sousGenreList.size() - 1);
        assertThat(testSousGenre.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testSousGenre.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void patchNonExistingSousGenre() throws Exception {
        int databaseSizeBeforeUpdate = sousGenreRepository.findAll().size();
        sousGenre.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSousGenreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sousGenre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sousGenre))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousGenre in the database
        List<SousGenre> sousGenreList = sousGenreRepository.findAll();
        assertThat(sousGenreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSousGenre() throws Exception {
        int databaseSizeBeforeUpdate = sousGenreRepository.findAll().size();
        sousGenre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousGenreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sousGenre))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousGenre in the database
        List<SousGenre> sousGenreList = sousGenreRepository.findAll();
        assertThat(sousGenreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSousGenre() throws Exception {
        int databaseSizeBeforeUpdate = sousGenreRepository.findAll().size();
        sousGenre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousGenreMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sousGenre))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SousGenre in the database
        List<SousGenre> sousGenreList = sousGenreRepository.findAll();
        assertThat(sousGenreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSousGenre() throws Exception {
        // Initialize the database
        sousGenreRepository.saveAndFlush(sousGenre);

        int databaseSizeBeforeDelete = sousGenreRepository.findAll().size();

        // Delete the sousGenre
        restSousGenreMockMvc
            .perform(delete(ENTITY_API_URL_ID, sousGenre.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SousGenre> sousGenreList = sousGenreRepository.findAll();
        assertThat(sousGenreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
