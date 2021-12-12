package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.InfraClasse;
import fr.syncrase.ecosyst.domain.Ordre;
import fr.syncrase.ecosyst.domain.SuperOrdre;
import fr.syncrase.ecosyst.domain.SuperOrdre;
import fr.syncrase.ecosyst.repository.SuperOrdreRepository;
import fr.syncrase.ecosyst.service.criteria.SuperOrdreCriteria;
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
 * Integration tests for the {@link SuperOrdreResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SuperOrdreResourceIT {

    private static final String DEFAULT_NOM_FR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_LATIN = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LATIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/super-ordres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SuperOrdreRepository superOrdreRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSuperOrdreMockMvc;

    private SuperOrdre superOrdre;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SuperOrdre createEntity(EntityManager em) {
        SuperOrdre superOrdre = new SuperOrdre().nomFr(DEFAULT_NOM_FR).nomLatin(DEFAULT_NOM_LATIN);
        return superOrdre;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SuperOrdre createUpdatedEntity(EntityManager em) {
        SuperOrdre superOrdre = new SuperOrdre().nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);
        return superOrdre;
    }

    @BeforeEach
    public void initTest() {
        superOrdre = createEntity(em);
    }

    @Test
    @Transactional
    void createSuperOrdre() throws Exception {
        int databaseSizeBeforeCreate = superOrdreRepository.findAll().size();
        // Create the SuperOrdre
        restSuperOrdreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(superOrdre)))
            .andExpect(status().isCreated());

        // Validate the SuperOrdre in the database
        List<SuperOrdre> superOrdreList = superOrdreRepository.findAll();
        assertThat(superOrdreList).hasSize(databaseSizeBeforeCreate + 1);
        SuperOrdre testSuperOrdre = superOrdreList.get(superOrdreList.size() - 1);
        assertThat(testSuperOrdre.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testSuperOrdre.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void createSuperOrdreWithExistingId() throws Exception {
        // Create the SuperOrdre with an existing ID
        superOrdre.setId(1L);

        int databaseSizeBeforeCreate = superOrdreRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSuperOrdreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(superOrdre)))
            .andExpect(status().isBadRequest());

        // Validate the SuperOrdre in the database
        List<SuperOrdre> superOrdreList = superOrdreRepository.findAll();
        assertThat(superOrdreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomFrIsRequired() throws Exception {
        int databaseSizeBeforeTest = superOrdreRepository.findAll().size();
        // set the field null
        superOrdre.setNomFr(null);

        // Create the SuperOrdre, which fails.

        restSuperOrdreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(superOrdre)))
            .andExpect(status().isBadRequest());

        List<SuperOrdre> superOrdreList = superOrdreRepository.findAll();
        assertThat(superOrdreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSuperOrdres() throws Exception {
        // Initialize the database
        superOrdreRepository.saveAndFlush(superOrdre);

        // Get all the superOrdreList
        restSuperOrdreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(superOrdre.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));
    }

    @Test
    @Transactional
    void getSuperOrdre() throws Exception {
        // Initialize the database
        superOrdreRepository.saveAndFlush(superOrdre);

        // Get the superOrdre
        restSuperOrdreMockMvc
            .perform(get(ENTITY_API_URL_ID, superOrdre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(superOrdre.getId().intValue()))
            .andExpect(jsonPath("$.nomFr").value(DEFAULT_NOM_FR))
            .andExpect(jsonPath("$.nomLatin").value(DEFAULT_NOM_LATIN));
    }

    @Test
    @Transactional
    void getSuperOrdresByIdFiltering() throws Exception {
        // Initialize the database
        superOrdreRepository.saveAndFlush(superOrdre);

        Long id = superOrdre.getId();

        defaultSuperOrdreShouldBeFound("id.equals=" + id);
        defaultSuperOrdreShouldNotBeFound("id.notEquals=" + id);

        defaultSuperOrdreShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSuperOrdreShouldNotBeFound("id.greaterThan=" + id);

        defaultSuperOrdreShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSuperOrdreShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSuperOrdresByNomFrIsEqualToSomething() throws Exception {
        // Initialize the database
        superOrdreRepository.saveAndFlush(superOrdre);

        // Get all the superOrdreList where nomFr equals to DEFAULT_NOM_FR
        defaultSuperOrdreShouldBeFound("nomFr.equals=" + DEFAULT_NOM_FR);

        // Get all the superOrdreList where nomFr equals to UPDATED_NOM_FR
        defaultSuperOrdreShouldNotBeFound("nomFr.equals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSuperOrdresByNomFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        superOrdreRepository.saveAndFlush(superOrdre);

        // Get all the superOrdreList where nomFr not equals to DEFAULT_NOM_FR
        defaultSuperOrdreShouldNotBeFound("nomFr.notEquals=" + DEFAULT_NOM_FR);

        // Get all the superOrdreList where nomFr not equals to UPDATED_NOM_FR
        defaultSuperOrdreShouldBeFound("nomFr.notEquals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSuperOrdresByNomFrIsInShouldWork() throws Exception {
        // Initialize the database
        superOrdreRepository.saveAndFlush(superOrdre);

        // Get all the superOrdreList where nomFr in DEFAULT_NOM_FR or UPDATED_NOM_FR
        defaultSuperOrdreShouldBeFound("nomFr.in=" + DEFAULT_NOM_FR + "," + UPDATED_NOM_FR);

        // Get all the superOrdreList where nomFr equals to UPDATED_NOM_FR
        defaultSuperOrdreShouldNotBeFound("nomFr.in=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSuperOrdresByNomFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        superOrdreRepository.saveAndFlush(superOrdre);

        // Get all the superOrdreList where nomFr is not null
        defaultSuperOrdreShouldBeFound("nomFr.specified=true");

        // Get all the superOrdreList where nomFr is null
        defaultSuperOrdreShouldNotBeFound("nomFr.specified=false");
    }

    @Test
    @Transactional
    void getAllSuperOrdresByNomFrContainsSomething() throws Exception {
        // Initialize the database
        superOrdreRepository.saveAndFlush(superOrdre);

        // Get all the superOrdreList where nomFr contains DEFAULT_NOM_FR
        defaultSuperOrdreShouldBeFound("nomFr.contains=" + DEFAULT_NOM_FR);

        // Get all the superOrdreList where nomFr contains UPDATED_NOM_FR
        defaultSuperOrdreShouldNotBeFound("nomFr.contains=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSuperOrdresByNomFrNotContainsSomething() throws Exception {
        // Initialize the database
        superOrdreRepository.saveAndFlush(superOrdre);

        // Get all the superOrdreList where nomFr does not contain DEFAULT_NOM_FR
        defaultSuperOrdreShouldNotBeFound("nomFr.doesNotContain=" + DEFAULT_NOM_FR);

        // Get all the superOrdreList where nomFr does not contain UPDATED_NOM_FR
        defaultSuperOrdreShouldBeFound("nomFr.doesNotContain=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSuperOrdresByNomLatinIsEqualToSomething() throws Exception {
        // Initialize the database
        superOrdreRepository.saveAndFlush(superOrdre);

        // Get all the superOrdreList where nomLatin equals to DEFAULT_NOM_LATIN
        defaultSuperOrdreShouldBeFound("nomLatin.equals=" + DEFAULT_NOM_LATIN);

        // Get all the superOrdreList where nomLatin equals to UPDATED_NOM_LATIN
        defaultSuperOrdreShouldNotBeFound("nomLatin.equals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSuperOrdresByNomLatinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        superOrdreRepository.saveAndFlush(superOrdre);

        // Get all the superOrdreList where nomLatin not equals to DEFAULT_NOM_LATIN
        defaultSuperOrdreShouldNotBeFound("nomLatin.notEquals=" + DEFAULT_NOM_LATIN);

        // Get all the superOrdreList where nomLatin not equals to UPDATED_NOM_LATIN
        defaultSuperOrdreShouldBeFound("nomLatin.notEquals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSuperOrdresByNomLatinIsInShouldWork() throws Exception {
        // Initialize the database
        superOrdreRepository.saveAndFlush(superOrdre);

        // Get all the superOrdreList where nomLatin in DEFAULT_NOM_LATIN or UPDATED_NOM_LATIN
        defaultSuperOrdreShouldBeFound("nomLatin.in=" + DEFAULT_NOM_LATIN + "," + UPDATED_NOM_LATIN);

        // Get all the superOrdreList where nomLatin equals to UPDATED_NOM_LATIN
        defaultSuperOrdreShouldNotBeFound("nomLatin.in=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSuperOrdresByNomLatinIsNullOrNotNull() throws Exception {
        // Initialize the database
        superOrdreRepository.saveAndFlush(superOrdre);

        // Get all the superOrdreList where nomLatin is not null
        defaultSuperOrdreShouldBeFound("nomLatin.specified=true");

        // Get all the superOrdreList where nomLatin is null
        defaultSuperOrdreShouldNotBeFound("nomLatin.specified=false");
    }

    @Test
    @Transactional
    void getAllSuperOrdresByNomLatinContainsSomething() throws Exception {
        // Initialize the database
        superOrdreRepository.saveAndFlush(superOrdre);

        // Get all the superOrdreList where nomLatin contains DEFAULT_NOM_LATIN
        defaultSuperOrdreShouldBeFound("nomLatin.contains=" + DEFAULT_NOM_LATIN);

        // Get all the superOrdreList where nomLatin contains UPDATED_NOM_LATIN
        defaultSuperOrdreShouldNotBeFound("nomLatin.contains=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSuperOrdresByNomLatinNotContainsSomething() throws Exception {
        // Initialize the database
        superOrdreRepository.saveAndFlush(superOrdre);

        // Get all the superOrdreList where nomLatin does not contain DEFAULT_NOM_LATIN
        defaultSuperOrdreShouldNotBeFound("nomLatin.doesNotContain=" + DEFAULT_NOM_LATIN);

        // Get all the superOrdreList where nomLatin does not contain UPDATED_NOM_LATIN
        defaultSuperOrdreShouldBeFound("nomLatin.doesNotContain=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSuperOrdresByOrdresIsEqualToSomething() throws Exception {
        // Initialize the database
        superOrdreRepository.saveAndFlush(superOrdre);
        Ordre ordres;
        if (TestUtil.findAll(em, Ordre.class).isEmpty()) {
            ordres = OrdreResourceIT.createEntity(em);
            em.persist(ordres);
            em.flush();
        } else {
            ordres = TestUtil.findAll(em, Ordre.class).get(0);
        }
        em.persist(ordres);
        em.flush();
        superOrdre.addOrdres(ordres);
        superOrdreRepository.saveAndFlush(superOrdre);
        Long ordresId = ordres.getId();

        // Get all the superOrdreList where ordres equals to ordresId
        defaultSuperOrdreShouldBeFound("ordresId.equals=" + ordresId);

        // Get all the superOrdreList where ordres equals to (ordresId + 1)
        defaultSuperOrdreShouldNotBeFound("ordresId.equals=" + (ordresId + 1));
    }

    @Test
    @Transactional
    void getAllSuperOrdresBySynonymesIsEqualToSomething() throws Exception {
        // Initialize the database
        superOrdreRepository.saveAndFlush(superOrdre);
        SuperOrdre synonymes;
        if (TestUtil.findAll(em, SuperOrdre.class).isEmpty()) {
            synonymes = SuperOrdreResourceIT.createEntity(em);
            em.persist(synonymes);
            em.flush();
        } else {
            synonymes = TestUtil.findAll(em, SuperOrdre.class).get(0);
        }
        em.persist(synonymes);
        em.flush();
        superOrdre.addSynonymes(synonymes);
        superOrdreRepository.saveAndFlush(superOrdre);
        Long synonymesId = synonymes.getId();

        // Get all the superOrdreList where synonymes equals to synonymesId
        defaultSuperOrdreShouldBeFound("synonymesId.equals=" + synonymesId);

        // Get all the superOrdreList where synonymes equals to (synonymesId + 1)
        defaultSuperOrdreShouldNotBeFound("synonymesId.equals=" + (synonymesId + 1));
    }

    @Test
    @Transactional
    void getAllSuperOrdresByInfraClasseIsEqualToSomething() throws Exception {
        // Initialize the database
        superOrdreRepository.saveAndFlush(superOrdre);
        InfraClasse infraClasse;
        if (TestUtil.findAll(em, InfraClasse.class).isEmpty()) {
            infraClasse = InfraClasseResourceIT.createEntity(em);
            em.persist(infraClasse);
            em.flush();
        } else {
            infraClasse = TestUtil.findAll(em, InfraClasse.class).get(0);
        }
        em.persist(infraClasse);
        em.flush();
        superOrdre.setInfraClasse(infraClasse);
        superOrdreRepository.saveAndFlush(superOrdre);
        Long infraClasseId = infraClasse.getId();

        // Get all the superOrdreList where infraClasse equals to infraClasseId
        defaultSuperOrdreShouldBeFound("infraClasseId.equals=" + infraClasseId);

        // Get all the superOrdreList where infraClasse equals to (infraClasseId + 1)
        defaultSuperOrdreShouldNotBeFound("infraClasseId.equals=" + (infraClasseId + 1));
    }

    @Test
    @Transactional
    void getAllSuperOrdresBySuperOrdreIsEqualToSomething() throws Exception {
        // Initialize the database
        superOrdreRepository.saveAndFlush(superOrdre);
        SuperOrdre superOrdre;
        if (TestUtil.findAll(em, SuperOrdre.class).isEmpty()) {
            superOrdre = SuperOrdreResourceIT.createEntity(em);
            em.persist(superOrdre);
            em.flush();
        } else {
            superOrdre = TestUtil.findAll(em, SuperOrdre.class).get(0);
        }
        em.persist(superOrdre);
        em.flush();
        superOrdre.setSuperOrdre(superOrdre);
        superOrdreRepository.saveAndFlush(superOrdre);
        Long superOrdreId = superOrdre.getId();

        // Get all the superOrdreList where superOrdre equals to superOrdreId
        defaultSuperOrdreShouldBeFound("superOrdreId.equals=" + superOrdreId);

        // Get all the superOrdreList where superOrdre equals to (superOrdreId + 1)
        defaultSuperOrdreShouldNotBeFound("superOrdreId.equals=" + (superOrdreId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSuperOrdreShouldBeFound(String filter) throws Exception {
        restSuperOrdreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(superOrdre.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));

        // Check, that the count call also returns 1
        restSuperOrdreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSuperOrdreShouldNotBeFound(String filter) throws Exception {
        restSuperOrdreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSuperOrdreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSuperOrdre() throws Exception {
        // Get the superOrdre
        restSuperOrdreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSuperOrdre() throws Exception {
        // Initialize the database
        superOrdreRepository.saveAndFlush(superOrdre);

        int databaseSizeBeforeUpdate = superOrdreRepository.findAll().size();

        // Update the superOrdre
        SuperOrdre updatedSuperOrdre = superOrdreRepository.findById(superOrdre.getId()).get();
        // Disconnect from session so that the updates on updatedSuperOrdre are not directly saved in db
        em.detach(updatedSuperOrdre);
        updatedSuperOrdre.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restSuperOrdreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSuperOrdre.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSuperOrdre))
            )
            .andExpect(status().isOk());

        // Validate the SuperOrdre in the database
        List<SuperOrdre> superOrdreList = superOrdreRepository.findAll();
        assertThat(superOrdreList).hasSize(databaseSizeBeforeUpdate);
        SuperOrdre testSuperOrdre = superOrdreList.get(superOrdreList.size() - 1);
        assertThat(testSuperOrdre.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testSuperOrdre.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void putNonExistingSuperOrdre() throws Exception {
        int databaseSizeBeforeUpdate = superOrdreRepository.findAll().size();
        superOrdre.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSuperOrdreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, superOrdre.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(superOrdre))
            )
            .andExpect(status().isBadRequest());

        // Validate the SuperOrdre in the database
        List<SuperOrdre> superOrdreList = superOrdreRepository.findAll();
        assertThat(superOrdreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSuperOrdre() throws Exception {
        int databaseSizeBeforeUpdate = superOrdreRepository.findAll().size();
        superOrdre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuperOrdreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(superOrdre))
            )
            .andExpect(status().isBadRequest());

        // Validate the SuperOrdre in the database
        List<SuperOrdre> superOrdreList = superOrdreRepository.findAll();
        assertThat(superOrdreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSuperOrdre() throws Exception {
        int databaseSizeBeforeUpdate = superOrdreRepository.findAll().size();
        superOrdre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuperOrdreMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(superOrdre)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SuperOrdre in the database
        List<SuperOrdre> superOrdreList = superOrdreRepository.findAll();
        assertThat(superOrdreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSuperOrdreWithPatch() throws Exception {
        // Initialize the database
        superOrdreRepository.saveAndFlush(superOrdre);

        int databaseSizeBeforeUpdate = superOrdreRepository.findAll().size();

        // Update the superOrdre using partial update
        SuperOrdre partialUpdatedSuperOrdre = new SuperOrdre();
        partialUpdatedSuperOrdre.setId(superOrdre.getId());

        partialUpdatedSuperOrdre.nomFr(UPDATED_NOM_FR);

        restSuperOrdreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSuperOrdre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSuperOrdre))
            )
            .andExpect(status().isOk());

        // Validate the SuperOrdre in the database
        List<SuperOrdre> superOrdreList = superOrdreRepository.findAll();
        assertThat(superOrdreList).hasSize(databaseSizeBeforeUpdate);
        SuperOrdre testSuperOrdre = superOrdreList.get(superOrdreList.size() - 1);
        assertThat(testSuperOrdre.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testSuperOrdre.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void fullUpdateSuperOrdreWithPatch() throws Exception {
        // Initialize the database
        superOrdreRepository.saveAndFlush(superOrdre);

        int databaseSizeBeforeUpdate = superOrdreRepository.findAll().size();

        // Update the superOrdre using partial update
        SuperOrdre partialUpdatedSuperOrdre = new SuperOrdre();
        partialUpdatedSuperOrdre.setId(superOrdre.getId());

        partialUpdatedSuperOrdre.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restSuperOrdreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSuperOrdre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSuperOrdre))
            )
            .andExpect(status().isOk());

        // Validate the SuperOrdre in the database
        List<SuperOrdre> superOrdreList = superOrdreRepository.findAll();
        assertThat(superOrdreList).hasSize(databaseSizeBeforeUpdate);
        SuperOrdre testSuperOrdre = superOrdreList.get(superOrdreList.size() - 1);
        assertThat(testSuperOrdre.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testSuperOrdre.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void patchNonExistingSuperOrdre() throws Exception {
        int databaseSizeBeforeUpdate = superOrdreRepository.findAll().size();
        superOrdre.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSuperOrdreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, superOrdre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(superOrdre))
            )
            .andExpect(status().isBadRequest());

        // Validate the SuperOrdre in the database
        List<SuperOrdre> superOrdreList = superOrdreRepository.findAll();
        assertThat(superOrdreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSuperOrdre() throws Exception {
        int databaseSizeBeforeUpdate = superOrdreRepository.findAll().size();
        superOrdre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuperOrdreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(superOrdre))
            )
            .andExpect(status().isBadRequest());

        // Validate the SuperOrdre in the database
        List<SuperOrdre> superOrdreList = superOrdreRepository.findAll();
        assertThat(superOrdreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSuperOrdre() throws Exception {
        int databaseSizeBeforeUpdate = superOrdreRepository.findAll().size();
        superOrdre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuperOrdreMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(superOrdre))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SuperOrdre in the database
        List<SuperOrdre> superOrdreList = superOrdreRepository.findAll();
        assertThat(superOrdreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSuperOrdre() throws Exception {
        // Initialize the database
        superOrdreRepository.saveAndFlush(superOrdre);

        int databaseSizeBeforeDelete = superOrdreRepository.findAll().size();

        // Delete the superOrdre
        restSuperOrdreMockMvc
            .perform(delete(ENTITY_API_URL_ID, superOrdre.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SuperOrdre> superOrdreList = superOrdreRepository.findAll();
        assertThat(superOrdreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
