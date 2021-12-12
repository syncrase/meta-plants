package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.Classe;
import fr.syncrase.ecosyst.domain.MicroEmbranchement;
import fr.syncrase.ecosyst.domain.SuperClasse;
import fr.syncrase.ecosyst.domain.SuperClasse;
import fr.syncrase.ecosyst.repository.SuperClasseRepository;
import fr.syncrase.ecosyst.service.criteria.SuperClasseCriteria;
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
 * Integration tests for the {@link SuperClasseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SuperClasseResourceIT {

    private static final String DEFAULT_NOM_FR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_LATIN = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LATIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/super-classes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SuperClasseRepository superClasseRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSuperClasseMockMvc;

    private SuperClasse superClasse;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SuperClasse createEntity(EntityManager em) {
        SuperClasse superClasse = new SuperClasse().nomFr(DEFAULT_NOM_FR).nomLatin(DEFAULT_NOM_LATIN);
        return superClasse;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SuperClasse createUpdatedEntity(EntityManager em) {
        SuperClasse superClasse = new SuperClasse().nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);
        return superClasse;
    }

    @BeforeEach
    public void initTest() {
        superClasse = createEntity(em);
    }

    @Test
    @Transactional
    void createSuperClasse() throws Exception {
        int databaseSizeBeforeCreate = superClasseRepository.findAll().size();
        // Create the SuperClasse
        restSuperClasseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(superClasse)))
            .andExpect(status().isCreated());

        // Validate the SuperClasse in the database
        List<SuperClasse> superClasseList = superClasseRepository.findAll();
        assertThat(superClasseList).hasSize(databaseSizeBeforeCreate + 1);
        SuperClasse testSuperClasse = superClasseList.get(superClasseList.size() - 1);
        assertThat(testSuperClasse.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testSuperClasse.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void createSuperClasseWithExistingId() throws Exception {
        // Create the SuperClasse with an existing ID
        superClasse.setId(1L);

        int databaseSizeBeforeCreate = superClasseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSuperClasseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(superClasse)))
            .andExpect(status().isBadRequest());

        // Validate the SuperClasse in the database
        List<SuperClasse> superClasseList = superClasseRepository.findAll();
        assertThat(superClasseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomFrIsRequired() throws Exception {
        int databaseSizeBeforeTest = superClasseRepository.findAll().size();
        // set the field null
        superClasse.setNomFr(null);

        // Create the SuperClasse, which fails.

        restSuperClasseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(superClasse)))
            .andExpect(status().isBadRequest());

        List<SuperClasse> superClasseList = superClasseRepository.findAll();
        assertThat(superClasseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSuperClasses() throws Exception {
        // Initialize the database
        superClasseRepository.saveAndFlush(superClasse);

        // Get all the superClasseList
        restSuperClasseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(superClasse.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));
    }

    @Test
    @Transactional
    void getSuperClasse() throws Exception {
        // Initialize the database
        superClasseRepository.saveAndFlush(superClasse);

        // Get the superClasse
        restSuperClasseMockMvc
            .perform(get(ENTITY_API_URL_ID, superClasse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(superClasse.getId().intValue()))
            .andExpect(jsonPath("$.nomFr").value(DEFAULT_NOM_FR))
            .andExpect(jsonPath("$.nomLatin").value(DEFAULT_NOM_LATIN));
    }

    @Test
    @Transactional
    void getSuperClassesByIdFiltering() throws Exception {
        // Initialize the database
        superClasseRepository.saveAndFlush(superClasse);

        Long id = superClasse.getId();

        defaultSuperClasseShouldBeFound("id.equals=" + id);
        defaultSuperClasseShouldNotBeFound("id.notEquals=" + id);

        defaultSuperClasseShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSuperClasseShouldNotBeFound("id.greaterThan=" + id);

        defaultSuperClasseShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSuperClasseShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSuperClassesByNomFrIsEqualToSomething() throws Exception {
        // Initialize the database
        superClasseRepository.saveAndFlush(superClasse);

        // Get all the superClasseList where nomFr equals to DEFAULT_NOM_FR
        defaultSuperClasseShouldBeFound("nomFr.equals=" + DEFAULT_NOM_FR);

        // Get all the superClasseList where nomFr equals to UPDATED_NOM_FR
        defaultSuperClasseShouldNotBeFound("nomFr.equals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSuperClassesByNomFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        superClasseRepository.saveAndFlush(superClasse);

        // Get all the superClasseList where nomFr not equals to DEFAULT_NOM_FR
        defaultSuperClasseShouldNotBeFound("nomFr.notEquals=" + DEFAULT_NOM_FR);

        // Get all the superClasseList where nomFr not equals to UPDATED_NOM_FR
        defaultSuperClasseShouldBeFound("nomFr.notEquals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSuperClassesByNomFrIsInShouldWork() throws Exception {
        // Initialize the database
        superClasseRepository.saveAndFlush(superClasse);

        // Get all the superClasseList where nomFr in DEFAULT_NOM_FR or UPDATED_NOM_FR
        defaultSuperClasseShouldBeFound("nomFr.in=" + DEFAULT_NOM_FR + "," + UPDATED_NOM_FR);

        // Get all the superClasseList where nomFr equals to UPDATED_NOM_FR
        defaultSuperClasseShouldNotBeFound("nomFr.in=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSuperClassesByNomFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        superClasseRepository.saveAndFlush(superClasse);

        // Get all the superClasseList where nomFr is not null
        defaultSuperClasseShouldBeFound("nomFr.specified=true");

        // Get all the superClasseList where nomFr is null
        defaultSuperClasseShouldNotBeFound("nomFr.specified=false");
    }

    @Test
    @Transactional
    void getAllSuperClassesByNomFrContainsSomething() throws Exception {
        // Initialize the database
        superClasseRepository.saveAndFlush(superClasse);

        // Get all the superClasseList where nomFr contains DEFAULT_NOM_FR
        defaultSuperClasseShouldBeFound("nomFr.contains=" + DEFAULT_NOM_FR);

        // Get all the superClasseList where nomFr contains UPDATED_NOM_FR
        defaultSuperClasseShouldNotBeFound("nomFr.contains=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSuperClassesByNomFrNotContainsSomething() throws Exception {
        // Initialize the database
        superClasseRepository.saveAndFlush(superClasse);

        // Get all the superClasseList where nomFr does not contain DEFAULT_NOM_FR
        defaultSuperClasseShouldNotBeFound("nomFr.doesNotContain=" + DEFAULT_NOM_FR);

        // Get all the superClasseList where nomFr does not contain UPDATED_NOM_FR
        defaultSuperClasseShouldBeFound("nomFr.doesNotContain=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSuperClassesByNomLatinIsEqualToSomething() throws Exception {
        // Initialize the database
        superClasseRepository.saveAndFlush(superClasse);

        // Get all the superClasseList where nomLatin equals to DEFAULT_NOM_LATIN
        defaultSuperClasseShouldBeFound("nomLatin.equals=" + DEFAULT_NOM_LATIN);

        // Get all the superClasseList where nomLatin equals to UPDATED_NOM_LATIN
        defaultSuperClasseShouldNotBeFound("nomLatin.equals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSuperClassesByNomLatinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        superClasseRepository.saveAndFlush(superClasse);

        // Get all the superClasseList where nomLatin not equals to DEFAULT_NOM_LATIN
        defaultSuperClasseShouldNotBeFound("nomLatin.notEquals=" + DEFAULT_NOM_LATIN);

        // Get all the superClasseList where nomLatin not equals to UPDATED_NOM_LATIN
        defaultSuperClasseShouldBeFound("nomLatin.notEquals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSuperClassesByNomLatinIsInShouldWork() throws Exception {
        // Initialize the database
        superClasseRepository.saveAndFlush(superClasse);

        // Get all the superClasseList where nomLatin in DEFAULT_NOM_LATIN or UPDATED_NOM_LATIN
        defaultSuperClasseShouldBeFound("nomLatin.in=" + DEFAULT_NOM_LATIN + "," + UPDATED_NOM_LATIN);

        // Get all the superClasseList where nomLatin equals to UPDATED_NOM_LATIN
        defaultSuperClasseShouldNotBeFound("nomLatin.in=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSuperClassesByNomLatinIsNullOrNotNull() throws Exception {
        // Initialize the database
        superClasseRepository.saveAndFlush(superClasse);

        // Get all the superClasseList where nomLatin is not null
        defaultSuperClasseShouldBeFound("nomLatin.specified=true");

        // Get all the superClasseList where nomLatin is null
        defaultSuperClasseShouldNotBeFound("nomLatin.specified=false");
    }

    @Test
    @Transactional
    void getAllSuperClassesByNomLatinContainsSomething() throws Exception {
        // Initialize the database
        superClasseRepository.saveAndFlush(superClasse);

        // Get all the superClasseList where nomLatin contains DEFAULT_NOM_LATIN
        defaultSuperClasseShouldBeFound("nomLatin.contains=" + DEFAULT_NOM_LATIN);

        // Get all the superClasseList where nomLatin contains UPDATED_NOM_LATIN
        defaultSuperClasseShouldNotBeFound("nomLatin.contains=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSuperClassesByNomLatinNotContainsSomething() throws Exception {
        // Initialize the database
        superClasseRepository.saveAndFlush(superClasse);

        // Get all the superClasseList where nomLatin does not contain DEFAULT_NOM_LATIN
        defaultSuperClasseShouldNotBeFound("nomLatin.doesNotContain=" + DEFAULT_NOM_LATIN);

        // Get all the superClasseList where nomLatin does not contain UPDATED_NOM_LATIN
        defaultSuperClasseShouldBeFound("nomLatin.doesNotContain=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSuperClassesByClassesIsEqualToSomething() throws Exception {
        // Initialize the database
        superClasseRepository.saveAndFlush(superClasse);
        Classe classes;
        if (TestUtil.findAll(em, Classe.class).isEmpty()) {
            classes = ClasseResourceIT.createEntity(em);
            em.persist(classes);
            em.flush();
        } else {
            classes = TestUtil.findAll(em, Classe.class).get(0);
        }
        em.persist(classes);
        em.flush();
        superClasse.addClasses(classes);
        superClasseRepository.saveAndFlush(superClasse);
        Long classesId = classes.getId();

        // Get all the superClasseList where classes equals to classesId
        defaultSuperClasseShouldBeFound("classesId.equals=" + classesId);

        // Get all the superClasseList where classes equals to (classesId + 1)
        defaultSuperClasseShouldNotBeFound("classesId.equals=" + (classesId + 1));
    }

    @Test
    @Transactional
    void getAllSuperClassesBySynonymesIsEqualToSomething() throws Exception {
        // Initialize the database
        superClasseRepository.saveAndFlush(superClasse);
        SuperClasse synonymes;
        if (TestUtil.findAll(em, SuperClasse.class).isEmpty()) {
            synonymes = SuperClasseResourceIT.createEntity(em);
            em.persist(synonymes);
            em.flush();
        } else {
            synonymes = TestUtil.findAll(em, SuperClasse.class).get(0);
        }
        em.persist(synonymes);
        em.flush();
        superClasse.addSynonymes(synonymes);
        superClasseRepository.saveAndFlush(superClasse);
        Long synonymesId = synonymes.getId();

        // Get all the superClasseList where synonymes equals to synonymesId
        defaultSuperClasseShouldBeFound("synonymesId.equals=" + synonymesId);

        // Get all the superClasseList where synonymes equals to (synonymesId + 1)
        defaultSuperClasseShouldNotBeFound("synonymesId.equals=" + (synonymesId + 1));
    }

    @Test
    @Transactional
    void getAllSuperClassesByMicroEmbranchementIsEqualToSomething() throws Exception {
        // Initialize the database
        superClasseRepository.saveAndFlush(superClasse);
        MicroEmbranchement microEmbranchement;
        if (TestUtil.findAll(em, MicroEmbranchement.class).isEmpty()) {
            microEmbranchement = MicroEmbranchementResourceIT.createEntity(em);
            em.persist(microEmbranchement);
            em.flush();
        } else {
            microEmbranchement = TestUtil.findAll(em, MicroEmbranchement.class).get(0);
        }
        em.persist(microEmbranchement);
        em.flush();
        superClasse.setMicroEmbranchement(microEmbranchement);
        superClasseRepository.saveAndFlush(superClasse);
        Long microEmbranchementId = microEmbranchement.getId();

        // Get all the superClasseList where microEmbranchement equals to microEmbranchementId
        defaultSuperClasseShouldBeFound("microEmbranchementId.equals=" + microEmbranchementId);

        // Get all the superClasseList where microEmbranchement equals to (microEmbranchementId + 1)
        defaultSuperClasseShouldNotBeFound("microEmbranchementId.equals=" + (microEmbranchementId + 1));
    }

    @Test
    @Transactional
    void getAllSuperClassesBySuperClasseIsEqualToSomething() throws Exception {
        // Initialize the database
        superClasseRepository.saveAndFlush(superClasse);
        SuperClasse superClasse;
        if (TestUtil.findAll(em, SuperClasse.class).isEmpty()) {
            superClasse = SuperClasseResourceIT.createEntity(em);
            em.persist(superClasse);
            em.flush();
        } else {
            superClasse = TestUtil.findAll(em, SuperClasse.class).get(0);
        }
        em.persist(superClasse);
        em.flush();
        superClasse.setSuperClasse(superClasse);
        superClasseRepository.saveAndFlush(superClasse);
        Long superClasseId = superClasse.getId();

        // Get all the superClasseList where superClasse equals to superClasseId
        defaultSuperClasseShouldBeFound("superClasseId.equals=" + superClasseId);

        // Get all the superClasseList where superClasse equals to (superClasseId + 1)
        defaultSuperClasseShouldNotBeFound("superClasseId.equals=" + (superClasseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSuperClasseShouldBeFound(String filter) throws Exception {
        restSuperClasseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(superClasse.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));

        // Check, that the count call also returns 1
        restSuperClasseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSuperClasseShouldNotBeFound(String filter) throws Exception {
        restSuperClasseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSuperClasseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSuperClasse() throws Exception {
        // Get the superClasse
        restSuperClasseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSuperClasse() throws Exception {
        // Initialize the database
        superClasseRepository.saveAndFlush(superClasse);

        int databaseSizeBeforeUpdate = superClasseRepository.findAll().size();

        // Update the superClasse
        SuperClasse updatedSuperClasse = superClasseRepository.findById(superClasse.getId()).get();
        // Disconnect from session so that the updates on updatedSuperClasse are not directly saved in db
        em.detach(updatedSuperClasse);
        updatedSuperClasse.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restSuperClasseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSuperClasse.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSuperClasse))
            )
            .andExpect(status().isOk());

        // Validate the SuperClasse in the database
        List<SuperClasse> superClasseList = superClasseRepository.findAll();
        assertThat(superClasseList).hasSize(databaseSizeBeforeUpdate);
        SuperClasse testSuperClasse = superClasseList.get(superClasseList.size() - 1);
        assertThat(testSuperClasse.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testSuperClasse.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void putNonExistingSuperClasse() throws Exception {
        int databaseSizeBeforeUpdate = superClasseRepository.findAll().size();
        superClasse.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSuperClasseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, superClasse.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(superClasse))
            )
            .andExpect(status().isBadRequest());

        // Validate the SuperClasse in the database
        List<SuperClasse> superClasseList = superClasseRepository.findAll();
        assertThat(superClasseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSuperClasse() throws Exception {
        int databaseSizeBeforeUpdate = superClasseRepository.findAll().size();
        superClasse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuperClasseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(superClasse))
            )
            .andExpect(status().isBadRequest());

        // Validate the SuperClasse in the database
        List<SuperClasse> superClasseList = superClasseRepository.findAll();
        assertThat(superClasseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSuperClasse() throws Exception {
        int databaseSizeBeforeUpdate = superClasseRepository.findAll().size();
        superClasse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuperClasseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(superClasse)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SuperClasse in the database
        List<SuperClasse> superClasseList = superClasseRepository.findAll();
        assertThat(superClasseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSuperClasseWithPatch() throws Exception {
        // Initialize the database
        superClasseRepository.saveAndFlush(superClasse);

        int databaseSizeBeforeUpdate = superClasseRepository.findAll().size();

        // Update the superClasse using partial update
        SuperClasse partialUpdatedSuperClasse = new SuperClasse();
        partialUpdatedSuperClasse.setId(superClasse.getId());

        partialUpdatedSuperClasse.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restSuperClasseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSuperClasse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSuperClasse))
            )
            .andExpect(status().isOk());

        // Validate the SuperClasse in the database
        List<SuperClasse> superClasseList = superClasseRepository.findAll();
        assertThat(superClasseList).hasSize(databaseSizeBeforeUpdate);
        SuperClasse testSuperClasse = superClasseList.get(superClasseList.size() - 1);
        assertThat(testSuperClasse.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testSuperClasse.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void fullUpdateSuperClasseWithPatch() throws Exception {
        // Initialize the database
        superClasseRepository.saveAndFlush(superClasse);

        int databaseSizeBeforeUpdate = superClasseRepository.findAll().size();

        // Update the superClasse using partial update
        SuperClasse partialUpdatedSuperClasse = new SuperClasse();
        partialUpdatedSuperClasse.setId(superClasse.getId());

        partialUpdatedSuperClasse.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restSuperClasseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSuperClasse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSuperClasse))
            )
            .andExpect(status().isOk());

        // Validate the SuperClasse in the database
        List<SuperClasse> superClasseList = superClasseRepository.findAll();
        assertThat(superClasseList).hasSize(databaseSizeBeforeUpdate);
        SuperClasse testSuperClasse = superClasseList.get(superClasseList.size() - 1);
        assertThat(testSuperClasse.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testSuperClasse.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void patchNonExistingSuperClasse() throws Exception {
        int databaseSizeBeforeUpdate = superClasseRepository.findAll().size();
        superClasse.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSuperClasseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, superClasse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(superClasse))
            )
            .andExpect(status().isBadRequest());

        // Validate the SuperClasse in the database
        List<SuperClasse> superClasseList = superClasseRepository.findAll();
        assertThat(superClasseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSuperClasse() throws Exception {
        int databaseSizeBeforeUpdate = superClasseRepository.findAll().size();
        superClasse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuperClasseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(superClasse))
            )
            .andExpect(status().isBadRequest());

        // Validate the SuperClasse in the database
        List<SuperClasse> superClasseList = superClasseRepository.findAll();
        assertThat(superClasseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSuperClasse() throws Exception {
        int databaseSizeBeforeUpdate = superClasseRepository.findAll().size();
        superClasse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuperClasseMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(superClasse))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SuperClasse in the database
        List<SuperClasse> superClasseList = superClasseRepository.findAll();
        assertThat(superClasseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSuperClasse() throws Exception {
        // Initialize the database
        superClasseRepository.saveAndFlush(superClasse);

        int databaseSizeBeforeDelete = superClasseRepository.findAll().size();

        // Delete the superClasse
        restSuperClasseMockMvc
            .perform(delete(ENTITY_API_URL_ID, superClasse.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SuperClasse> superClasseList = superClasseRepository.findAll();
        assertThat(superClasseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
