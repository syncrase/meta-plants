package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.Genre;
import fr.syncrase.ecosyst.domain.Genre;
import fr.syncrase.ecosyst.domain.SousGenre;
import fr.syncrase.ecosyst.domain.SousTribu;
import fr.syncrase.ecosyst.repository.GenreRepository;
import fr.syncrase.ecosyst.service.criteria.GenreCriteria;
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
 * Integration tests for the {@link GenreResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GenreResourceIT {

    private static final String DEFAULT_NOM_FR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_LATIN = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LATIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/genres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGenreMockMvc;

    private Genre genre;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Genre createEntity(EntityManager em) {
        Genre genre = new Genre().nomFr(DEFAULT_NOM_FR).nomLatin(DEFAULT_NOM_LATIN);
        return genre;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Genre createUpdatedEntity(EntityManager em) {
        Genre genre = new Genre().nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);
        return genre;
    }

    @BeforeEach
    public void initTest() {
        genre = createEntity(em);
    }

    @Test
    @Transactional
    void createGenre() throws Exception {
        int databaseSizeBeforeCreate = genreRepository.findAll().size();
        // Create the Genre
        restGenreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(genre)))
            .andExpect(status().isCreated());

        // Validate the Genre in the database
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeCreate + 1);
        Genre testGenre = genreList.get(genreList.size() - 1);
        assertThat(testGenre.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testGenre.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void createGenreWithExistingId() throws Exception {
        // Create the Genre with an existing ID
        genre.setId(1L);

        int databaseSizeBeforeCreate = genreRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGenreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(genre)))
            .andExpect(status().isBadRequest());

        // Validate the Genre in the database
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomFrIsRequired() throws Exception {
        int databaseSizeBeforeTest = genreRepository.findAll().size();
        // set the field null
        genre.setNomFr(null);

        // Create the Genre, which fails.

        restGenreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(genre)))
            .andExpect(status().isBadRequest());

        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGenres() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);

        // Get all the genreList
        restGenreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(genre.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));
    }

    @Test
    @Transactional
    void getGenre() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);

        // Get the genre
        restGenreMockMvc
            .perform(get(ENTITY_API_URL_ID, genre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(genre.getId().intValue()))
            .andExpect(jsonPath("$.nomFr").value(DEFAULT_NOM_FR))
            .andExpect(jsonPath("$.nomLatin").value(DEFAULT_NOM_LATIN));
    }

    @Test
    @Transactional
    void getGenresByIdFiltering() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);

        Long id = genre.getId();

        defaultGenreShouldBeFound("id.equals=" + id);
        defaultGenreShouldNotBeFound("id.notEquals=" + id);

        defaultGenreShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultGenreShouldNotBeFound("id.greaterThan=" + id);

        defaultGenreShouldBeFound("id.lessThanOrEqual=" + id);
        defaultGenreShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGenresByNomFrIsEqualToSomething() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);

        // Get all the genreList where nomFr equals to DEFAULT_NOM_FR
        defaultGenreShouldBeFound("nomFr.equals=" + DEFAULT_NOM_FR);

        // Get all the genreList where nomFr equals to UPDATED_NOM_FR
        defaultGenreShouldNotBeFound("nomFr.equals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllGenresByNomFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);

        // Get all the genreList where nomFr not equals to DEFAULT_NOM_FR
        defaultGenreShouldNotBeFound("nomFr.notEquals=" + DEFAULT_NOM_FR);

        // Get all the genreList where nomFr not equals to UPDATED_NOM_FR
        defaultGenreShouldBeFound("nomFr.notEquals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllGenresByNomFrIsInShouldWork() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);

        // Get all the genreList where nomFr in DEFAULT_NOM_FR or UPDATED_NOM_FR
        defaultGenreShouldBeFound("nomFr.in=" + DEFAULT_NOM_FR + "," + UPDATED_NOM_FR);

        // Get all the genreList where nomFr equals to UPDATED_NOM_FR
        defaultGenreShouldNotBeFound("nomFr.in=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllGenresByNomFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);

        // Get all the genreList where nomFr is not null
        defaultGenreShouldBeFound("nomFr.specified=true");

        // Get all the genreList where nomFr is null
        defaultGenreShouldNotBeFound("nomFr.specified=false");
    }

    @Test
    @Transactional
    void getAllGenresByNomFrContainsSomething() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);

        // Get all the genreList where nomFr contains DEFAULT_NOM_FR
        defaultGenreShouldBeFound("nomFr.contains=" + DEFAULT_NOM_FR);

        // Get all the genreList where nomFr contains UPDATED_NOM_FR
        defaultGenreShouldNotBeFound("nomFr.contains=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllGenresByNomFrNotContainsSomething() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);

        // Get all the genreList where nomFr does not contain DEFAULT_NOM_FR
        defaultGenreShouldNotBeFound("nomFr.doesNotContain=" + DEFAULT_NOM_FR);

        // Get all the genreList where nomFr does not contain UPDATED_NOM_FR
        defaultGenreShouldBeFound("nomFr.doesNotContain=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllGenresByNomLatinIsEqualToSomething() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);

        // Get all the genreList where nomLatin equals to DEFAULT_NOM_LATIN
        defaultGenreShouldBeFound("nomLatin.equals=" + DEFAULT_NOM_LATIN);

        // Get all the genreList where nomLatin equals to UPDATED_NOM_LATIN
        defaultGenreShouldNotBeFound("nomLatin.equals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllGenresByNomLatinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);

        // Get all the genreList where nomLatin not equals to DEFAULT_NOM_LATIN
        defaultGenreShouldNotBeFound("nomLatin.notEquals=" + DEFAULT_NOM_LATIN);

        // Get all the genreList where nomLatin not equals to UPDATED_NOM_LATIN
        defaultGenreShouldBeFound("nomLatin.notEquals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllGenresByNomLatinIsInShouldWork() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);

        // Get all the genreList where nomLatin in DEFAULT_NOM_LATIN or UPDATED_NOM_LATIN
        defaultGenreShouldBeFound("nomLatin.in=" + DEFAULT_NOM_LATIN + "," + UPDATED_NOM_LATIN);

        // Get all the genreList where nomLatin equals to UPDATED_NOM_LATIN
        defaultGenreShouldNotBeFound("nomLatin.in=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllGenresByNomLatinIsNullOrNotNull() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);

        // Get all the genreList where nomLatin is not null
        defaultGenreShouldBeFound("nomLatin.specified=true");

        // Get all the genreList where nomLatin is null
        defaultGenreShouldNotBeFound("nomLatin.specified=false");
    }

    @Test
    @Transactional
    void getAllGenresByNomLatinContainsSomething() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);

        // Get all the genreList where nomLatin contains DEFAULT_NOM_LATIN
        defaultGenreShouldBeFound("nomLatin.contains=" + DEFAULT_NOM_LATIN);

        // Get all the genreList where nomLatin contains UPDATED_NOM_LATIN
        defaultGenreShouldNotBeFound("nomLatin.contains=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllGenresByNomLatinNotContainsSomething() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);

        // Get all the genreList where nomLatin does not contain DEFAULT_NOM_LATIN
        defaultGenreShouldNotBeFound("nomLatin.doesNotContain=" + DEFAULT_NOM_LATIN);

        // Get all the genreList where nomLatin does not contain UPDATED_NOM_LATIN
        defaultGenreShouldBeFound("nomLatin.doesNotContain=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllGenresBySousGenresIsEqualToSomething() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);
        SousGenre sousGenres;
        if (TestUtil.findAll(em, SousGenre.class).isEmpty()) {
            sousGenres = SousGenreResourceIT.createEntity(em);
            em.persist(sousGenres);
            em.flush();
        } else {
            sousGenres = TestUtil.findAll(em, SousGenre.class).get(0);
        }
        em.persist(sousGenres);
        em.flush();
        genre.addSousGenres(sousGenres);
        genreRepository.saveAndFlush(genre);
        Long sousGenresId = sousGenres.getId();

        // Get all the genreList where sousGenres equals to sousGenresId
        defaultGenreShouldBeFound("sousGenresId.equals=" + sousGenresId);

        // Get all the genreList where sousGenres equals to (sousGenresId + 1)
        defaultGenreShouldNotBeFound("sousGenresId.equals=" + (sousGenresId + 1));
    }

    @Test
    @Transactional
    void getAllGenresBySynonymesIsEqualToSomething() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);
        Genre synonymes;
        if (TestUtil.findAll(em, Genre.class).isEmpty()) {
            synonymes = GenreResourceIT.createEntity(em);
            em.persist(synonymes);
            em.flush();
        } else {
            synonymes = TestUtil.findAll(em, Genre.class).get(0);
        }
        em.persist(synonymes);
        em.flush();
        genre.addSynonymes(synonymes);
        genreRepository.saveAndFlush(genre);
        Long synonymesId = synonymes.getId();

        // Get all the genreList where synonymes equals to synonymesId
        defaultGenreShouldBeFound("synonymesId.equals=" + synonymesId);

        // Get all the genreList where synonymes equals to (synonymesId + 1)
        defaultGenreShouldNotBeFound("synonymesId.equals=" + (synonymesId + 1));
    }

    @Test
    @Transactional
    void getAllGenresBySousTribuIsEqualToSomething() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);
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
        genre.setSousTribu(sousTribu);
        genreRepository.saveAndFlush(genre);
        Long sousTribuId = sousTribu.getId();

        // Get all the genreList where sousTribu equals to sousTribuId
        defaultGenreShouldBeFound("sousTribuId.equals=" + sousTribuId);

        // Get all the genreList where sousTribu equals to (sousTribuId + 1)
        defaultGenreShouldNotBeFound("sousTribuId.equals=" + (sousTribuId + 1));
    }

    @Test
    @Transactional
    void getAllGenresByGenreIsEqualToSomething() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);
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
        genre.setGenre(genre);
        genreRepository.saveAndFlush(genre);
        Long genreId = genre.getId();

        // Get all the genreList where genre equals to genreId
        defaultGenreShouldBeFound("genreId.equals=" + genreId);

        // Get all the genreList where genre equals to (genreId + 1)
        defaultGenreShouldNotBeFound("genreId.equals=" + (genreId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGenreShouldBeFound(String filter) throws Exception {
        restGenreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(genre.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));

        // Check, that the count call also returns 1
        restGenreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGenreShouldNotBeFound(String filter) throws Exception {
        restGenreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGenreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGenre() throws Exception {
        // Get the genre
        restGenreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGenre() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);

        int databaseSizeBeforeUpdate = genreRepository.findAll().size();

        // Update the genre
        Genre updatedGenre = genreRepository.findById(genre.getId()).get();
        // Disconnect from session so that the updates on updatedGenre are not directly saved in db
        em.detach(updatedGenre);
        updatedGenre.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restGenreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGenre.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGenre))
            )
            .andExpect(status().isOk());

        // Validate the Genre in the database
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeUpdate);
        Genre testGenre = genreList.get(genreList.size() - 1);
        assertThat(testGenre.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testGenre.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void putNonExistingGenre() throws Exception {
        int databaseSizeBeforeUpdate = genreRepository.findAll().size();
        genre.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGenreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, genre.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(genre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Genre in the database
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGenre() throws Exception {
        int databaseSizeBeforeUpdate = genreRepository.findAll().size();
        genre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(genre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Genre in the database
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGenre() throws Exception {
        int databaseSizeBeforeUpdate = genreRepository.findAll().size();
        genre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenreMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(genre)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Genre in the database
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGenreWithPatch() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);

        int databaseSizeBeforeUpdate = genreRepository.findAll().size();

        // Update the genre using partial update
        Genre partialUpdatedGenre = new Genre();
        partialUpdatedGenre.setId(genre.getId());

        partialUpdatedGenre.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restGenreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGenre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGenre))
            )
            .andExpect(status().isOk());

        // Validate the Genre in the database
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeUpdate);
        Genre testGenre = genreList.get(genreList.size() - 1);
        assertThat(testGenre.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testGenre.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void fullUpdateGenreWithPatch() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);

        int databaseSizeBeforeUpdate = genreRepository.findAll().size();

        // Update the genre using partial update
        Genre partialUpdatedGenre = new Genre();
        partialUpdatedGenre.setId(genre.getId());

        partialUpdatedGenre.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restGenreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGenre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGenre))
            )
            .andExpect(status().isOk());

        // Validate the Genre in the database
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeUpdate);
        Genre testGenre = genreList.get(genreList.size() - 1);
        assertThat(testGenre.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testGenre.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void patchNonExistingGenre() throws Exception {
        int databaseSizeBeforeUpdate = genreRepository.findAll().size();
        genre.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGenreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, genre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(genre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Genre in the database
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGenre() throws Exception {
        int databaseSizeBeforeUpdate = genreRepository.findAll().size();
        genre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(genre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Genre in the database
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGenre() throws Exception {
        int databaseSizeBeforeUpdate = genreRepository.findAll().size();
        genre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenreMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(genre)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Genre in the database
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGenre() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);

        int databaseSizeBeforeDelete = genreRepository.findAll().size();

        // Delete the genre
        restGenreMockMvc
            .perform(delete(ENTITY_API_URL_ID, genre.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
