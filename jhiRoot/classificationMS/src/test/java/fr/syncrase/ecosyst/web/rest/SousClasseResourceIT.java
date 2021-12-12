package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.Classe;
import fr.syncrase.ecosyst.domain.InfraClasse;
import fr.syncrase.ecosyst.domain.SousClasse;
import fr.syncrase.ecosyst.domain.SousClasse;
import fr.syncrase.ecosyst.repository.SousClasseRepository;
import fr.syncrase.ecosyst.service.criteria.SousClasseCriteria;
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
 * Integration tests for the {@link SousClasseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SousClasseResourceIT {

    private static final String DEFAULT_NOM_FR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_LATIN = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LATIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sous-classes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SousClasseRepository sousClasseRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSousClasseMockMvc;

    private SousClasse sousClasse;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SousClasse createEntity(EntityManager em) {
        SousClasse sousClasse = new SousClasse().nomFr(DEFAULT_NOM_FR).nomLatin(DEFAULT_NOM_LATIN);
        return sousClasse;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SousClasse createUpdatedEntity(EntityManager em) {
        SousClasse sousClasse = new SousClasse().nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);
        return sousClasse;
    }

    @BeforeEach
    public void initTest() {
        sousClasse = createEntity(em);
    }

    @Test
    @Transactional
    void createSousClasse() throws Exception {
        int databaseSizeBeforeCreate = sousClasseRepository.findAll().size();
        // Create the SousClasse
        restSousClasseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousClasse)))
            .andExpect(status().isCreated());

        // Validate the SousClasse in the database
        List<SousClasse> sousClasseList = sousClasseRepository.findAll();
        assertThat(sousClasseList).hasSize(databaseSizeBeforeCreate + 1);
        SousClasse testSousClasse = sousClasseList.get(sousClasseList.size() - 1);
        assertThat(testSousClasse.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testSousClasse.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void createSousClasseWithExistingId() throws Exception {
        // Create the SousClasse with an existing ID
        sousClasse.setId(1L);

        int databaseSizeBeforeCreate = sousClasseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSousClasseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousClasse)))
            .andExpect(status().isBadRequest());

        // Validate the SousClasse in the database
        List<SousClasse> sousClasseList = sousClasseRepository.findAll();
        assertThat(sousClasseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomFrIsRequired() throws Exception {
        int databaseSizeBeforeTest = sousClasseRepository.findAll().size();
        // set the field null
        sousClasse.setNomFr(null);

        // Create the SousClasse, which fails.

        restSousClasseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousClasse)))
            .andExpect(status().isBadRequest());

        List<SousClasse> sousClasseList = sousClasseRepository.findAll();
        assertThat(sousClasseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSousClasses() throws Exception {
        // Initialize the database
        sousClasseRepository.saveAndFlush(sousClasse);

        // Get all the sousClasseList
        restSousClasseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sousClasse.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));
    }

    @Test
    @Transactional
    void getSousClasse() throws Exception {
        // Initialize the database
        sousClasseRepository.saveAndFlush(sousClasse);

        // Get the sousClasse
        restSousClasseMockMvc
            .perform(get(ENTITY_API_URL_ID, sousClasse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sousClasse.getId().intValue()))
            .andExpect(jsonPath("$.nomFr").value(DEFAULT_NOM_FR))
            .andExpect(jsonPath("$.nomLatin").value(DEFAULT_NOM_LATIN));
    }

    @Test
    @Transactional
    void getSousClassesByIdFiltering() throws Exception {
        // Initialize the database
        sousClasseRepository.saveAndFlush(sousClasse);

        Long id = sousClasse.getId();

        defaultSousClasseShouldBeFound("id.equals=" + id);
        defaultSousClasseShouldNotBeFound("id.notEquals=" + id);

        defaultSousClasseShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSousClasseShouldNotBeFound("id.greaterThan=" + id);

        defaultSousClasseShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSousClasseShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSousClassesByNomFrIsEqualToSomething() throws Exception {
        // Initialize the database
        sousClasseRepository.saveAndFlush(sousClasse);

        // Get all the sousClasseList where nomFr equals to DEFAULT_NOM_FR
        defaultSousClasseShouldBeFound("nomFr.equals=" + DEFAULT_NOM_FR);

        // Get all the sousClasseList where nomFr equals to UPDATED_NOM_FR
        defaultSousClasseShouldNotBeFound("nomFr.equals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousClassesByNomFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sousClasseRepository.saveAndFlush(sousClasse);

        // Get all the sousClasseList where nomFr not equals to DEFAULT_NOM_FR
        defaultSousClasseShouldNotBeFound("nomFr.notEquals=" + DEFAULT_NOM_FR);

        // Get all the sousClasseList where nomFr not equals to UPDATED_NOM_FR
        defaultSousClasseShouldBeFound("nomFr.notEquals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousClassesByNomFrIsInShouldWork() throws Exception {
        // Initialize the database
        sousClasseRepository.saveAndFlush(sousClasse);

        // Get all the sousClasseList where nomFr in DEFAULT_NOM_FR or UPDATED_NOM_FR
        defaultSousClasseShouldBeFound("nomFr.in=" + DEFAULT_NOM_FR + "," + UPDATED_NOM_FR);

        // Get all the sousClasseList where nomFr equals to UPDATED_NOM_FR
        defaultSousClasseShouldNotBeFound("nomFr.in=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousClassesByNomFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        sousClasseRepository.saveAndFlush(sousClasse);

        // Get all the sousClasseList where nomFr is not null
        defaultSousClasseShouldBeFound("nomFr.specified=true");

        // Get all the sousClasseList where nomFr is null
        defaultSousClasseShouldNotBeFound("nomFr.specified=false");
    }

    @Test
    @Transactional
    void getAllSousClassesByNomFrContainsSomething() throws Exception {
        // Initialize the database
        sousClasseRepository.saveAndFlush(sousClasse);

        // Get all the sousClasseList where nomFr contains DEFAULT_NOM_FR
        defaultSousClasseShouldBeFound("nomFr.contains=" + DEFAULT_NOM_FR);

        // Get all the sousClasseList where nomFr contains UPDATED_NOM_FR
        defaultSousClasseShouldNotBeFound("nomFr.contains=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousClassesByNomFrNotContainsSomething() throws Exception {
        // Initialize the database
        sousClasseRepository.saveAndFlush(sousClasse);

        // Get all the sousClasseList where nomFr does not contain DEFAULT_NOM_FR
        defaultSousClasseShouldNotBeFound("nomFr.doesNotContain=" + DEFAULT_NOM_FR);

        // Get all the sousClasseList where nomFr does not contain UPDATED_NOM_FR
        defaultSousClasseShouldBeFound("nomFr.doesNotContain=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousClassesByNomLatinIsEqualToSomething() throws Exception {
        // Initialize the database
        sousClasseRepository.saveAndFlush(sousClasse);

        // Get all the sousClasseList where nomLatin equals to DEFAULT_NOM_LATIN
        defaultSousClasseShouldBeFound("nomLatin.equals=" + DEFAULT_NOM_LATIN);

        // Get all the sousClasseList where nomLatin equals to UPDATED_NOM_LATIN
        defaultSousClasseShouldNotBeFound("nomLatin.equals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousClassesByNomLatinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sousClasseRepository.saveAndFlush(sousClasse);

        // Get all the sousClasseList where nomLatin not equals to DEFAULT_NOM_LATIN
        defaultSousClasseShouldNotBeFound("nomLatin.notEquals=" + DEFAULT_NOM_LATIN);

        // Get all the sousClasseList where nomLatin not equals to UPDATED_NOM_LATIN
        defaultSousClasseShouldBeFound("nomLatin.notEquals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousClassesByNomLatinIsInShouldWork() throws Exception {
        // Initialize the database
        sousClasseRepository.saveAndFlush(sousClasse);

        // Get all the sousClasseList where nomLatin in DEFAULT_NOM_LATIN or UPDATED_NOM_LATIN
        defaultSousClasseShouldBeFound("nomLatin.in=" + DEFAULT_NOM_LATIN + "," + UPDATED_NOM_LATIN);

        // Get all the sousClasseList where nomLatin equals to UPDATED_NOM_LATIN
        defaultSousClasseShouldNotBeFound("nomLatin.in=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousClassesByNomLatinIsNullOrNotNull() throws Exception {
        // Initialize the database
        sousClasseRepository.saveAndFlush(sousClasse);

        // Get all the sousClasseList where nomLatin is not null
        defaultSousClasseShouldBeFound("nomLatin.specified=true");

        // Get all the sousClasseList where nomLatin is null
        defaultSousClasseShouldNotBeFound("nomLatin.specified=false");
    }

    @Test
    @Transactional
    void getAllSousClassesByNomLatinContainsSomething() throws Exception {
        // Initialize the database
        sousClasseRepository.saveAndFlush(sousClasse);

        // Get all the sousClasseList where nomLatin contains DEFAULT_NOM_LATIN
        defaultSousClasseShouldBeFound("nomLatin.contains=" + DEFAULT_NOM_LATIN);

        // Get all the sousClasseList where nomLatin contains UPDATED_NOM_LATIN
        defaultSousClasseShouldNotBeFound("nomLatin.contains=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousClassesByNomLatinNotContainsSomething() throws Exception {
        // Initialize the database
        sousClasseRepository.saveAndFlush(sousClasse);

        // Get all the sousClasseList where nomLatin does not contain DEFAULT_NOM_LATIN
        defaultSousClasseShouldNotBeFound("nomLatin.doesNotContain=" + DEFAULT_NOM_LATIN);

        // Get all the sousClasseList where nomLatin does not contain UPDATED_NOM_LATIN
        defaultSousClasseShouldBeFound("nomLatin.doesNotContain=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousClassesByInfraClassesIsEqualToSomething() throws Exception {
        // Initialize the database
        sousClasseRepository.saveAndFlush(sousClasse);
        InfraClasse infraClasses;
        if (TestUtil.findAll(em, InfraClasse.class).isEmpty()) {
            infraClasses = InfraClasseResourceIT.createEntity(em);
            em.persist(infraClasses);
            em.flush();
        } else {
            infraClasses = TestUtil.findAll(em, InfraClasse.class).get(0);
        }
        em.persist(infraClasses);
        em.flush();
        sousClasse.addInfraClasses(infraClasses);
        sousClasseRepository.saveAndFlush(sousClasse);
        Long infraClassesId = infraClasses.getId();

        // Get all the sousClasseList where infraClasses equals to infraClassesId
        defaultSousClasseShouldBeFound("infraClassesId.equals=" + infraClassesId);

        // Get all the sousClasseList where infraClasses equals to (infraClassesId + 1)
        defaultSousClasseShouldNotBeFound("infraClassesId.equals=" + (infraClassesId + 1));
    }

    @Test
    @Transactional
    void getAllSousClassesBySynonymesIsEqualToSomething() throws Exception {
        // Initialize the database
        sousClasseRepository.saveAndFlush(sousClasse);
        SousClasse synonymes;
        if (TestUtil.findAll(em, SousClasse.class).isEmpty()) {
            synonymes = SousClasseResourceIT.createEntity(em);
            em.persist(synonymes);
            em.flush();
        } else {
            synonymes = TestUtil.findAll(em, SousClasse.class).get(0);
        }
        em.persist(synonymes);
        em.flush();
        sousClasse.addSynonymes(synonymes);
        sousClasseRepository.saveAndFlush(sousClasse);
        Long synonymesId = synonymes.getId();

        // Get all the sousClasseList where synonymes equals to synonymesId
        defaultSousClasseShouldBeFound("synonymesId.equals=" + synonymesId);

        // Get all the sousClasseList where synonymes equals to (synonymesId + 1)
        defaultSousClasseShouldNotBeFound("synonymesId.equals=" + (synonymesId + 1));
    }

    @Test
    @Transactional
    void getAllSousClassesByClasseIsEqualToSomething() throws Exception {
        // Initialize the database
        sousClasseRepository.saveAndFlush(sousClasse);
        Classe classe;
        if (TestUtil.findAll(em, Classe.class).isEmpty()) {
            classe = ClasseResourceIT.createEntity(em);
            em.persist(classe);
            em.flush();
        } else {
            classe = TestUtil.findAll(em, Classe.class).get(0);
        }
        em.persist(classe);
        em.flush();
        sousClasse.setClasse(classe);
        sousClasseRepository.saveAndFlush(sousClasse);
        Long classeId = classe.getId();

        // Get all the sousClasseList where classe equals to classeId
        defaultSousClasseShouldBeFound("classeId.equals=" + classeId);

        // Get all the sousClasseList where classe equals to (classeId + 1)
        defaultSousClasseShouldNotBeFound("classeId.equals=" + (classeId + 1));
    }

    @Test
    @Transactional
    void getAllSousClassesBySousClasseIsEqualToSomething() throws Exception {
        // Initialize the database
        sousClasseRepository.saveAndFlush(sousClasse);
        SousClasse sousClasse;
        if (TestUtil.findAll(em, SousClasse.class).isEmpty()) {
            sousClasse = SousClasseResourceIT.createEntity(em);
            em.persist(sousClasse);
            em.flush();
        } else {
            sousClasse = TestUtil.findAll(em, SousClasse.class).get(0);
        }
        em.persist(sousClasse);
        em.flush();
        sousClasse.setSousClasse(sousClasse);
        sousClasseRepository.saveAndFlush(sousClasse);
        Long sousClasseId = sousClasse.getId();

        // Get all the sousClasseList where sousClasse equals to sousClasseId
        defaultSousClasseShouldBeFound("sousClasseId.equals=" + sousClasseId);

        // Get all the sousClasseList where sousClasse equals to (sousClasseId + 1)
        defaultSousClasseShouldNotBeFound("sousClasseId.equals=" + (sousClasseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSousClasseShouldBeFound(String filter) throws Exception {
        restSousClasseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sousClasse.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));

        // Check, that the count call also returns 1
        restSousClasseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSousClasseShouldNotBeFound(String filter) throws Exception {
        restSousClasseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSousClasseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSousClasse() throws Exception {
        // Get the sousClasse
        restSousClasseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSousClasse() throws Exception {
        // Initialize the database
        sousClasseRepository.saveAndFlush(sousClasse);

        int databaseSizeBeforeUpdate = sousClasseRepository.findAll().size();

        // Update the sousClasse
        SousClasse updatedSousClasse = sousClasseRepository.findById(sousClasse.getId()).get();
        // Disconnect from session so that the updates on updatedSousClasse are not directly saved in db
        em.detach(updatedSousClasse);
        updatedSousClasse.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restSousClasseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSousClasse.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSousClasse))
            )
            .andExpect(status().isOk());

        // Validate the SousClasse in the database
        List<SousClasse> sousClasseList = sousClasseRepository.findAll();
        assertThat(sousClasseList).hasSize(databaseSizeBeforeUpdate);
        SousClasse testSousClasse = sousClasseList.get(sousClasseList.size() - 1);
        assertThat(testSousClasse.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testSousClasse.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void putNonExistingSousClasse() throws Exception {
        int databaseSizeBeforeUpdate = sousClasseRepository.findAll().size();
        sousClasse.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSousClasseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sousClasse.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sousClasse))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousClasse in the database
        List<SousClasse> sousClasseList = sousClasseRepository.findAll();
        assertThat(sousClasseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSousClasse() throws Exception {
        int databaseSizeBeforeUpdate = sousClasseRepository.findAll().size();
        sousClasse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousClasseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sousClasse))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousClasse in the database
        List<SousClasse> sousClasseList = sousClasseRepository.findAll();
        assertThat(sousClasseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSousClasse() throws Exception {
        int databaseSizeBeforeUpdate = sousClasseRepository.findAll().size();
        sousClasse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousClasseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousClasse)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SousClasse in the database
        List<SousClasse> sousClasseList = sousClasseRepository.findAll();
        assertThat(sousClasseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSousClasseWithPatch() throws Exception {
        // Initialize the database
        sousClasseRepository.saveAndFlush(sousClasse);

        int databaseSizeBeforeUpdate = sousClasseRepository.findAll().size();

        // Update the sousClasse using partial update
        SousClasse partialUpdatedSousClasse = new SousClasse();
        partialUpdatedSousClasse.setId(sousClasse.getId());

        restSousClasseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSousClasse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSousClasse))
            )
            .andExpect(status().isOk());

        // Validate the SousClasse in the database
        List<SousClasse> sousClasseList = sousClasseRepository.findAll();
        assertThat(sousClasseList).hasSize(databaseSizeBeforeUpdate);
        SousClasse testSousClasse = sousClasseList.get(sousClasseList.size() - 1);
        assertThat(testSousClasse.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testSousClasse.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void fullUpdateSousClasseWithPatch() throws Exception {
        // Initialize the database
        sousClasseRepository.saveAndFlush(sousClasse);

        int databaseSizeBeforeUpdate = sousClasseRepository.findAll().size();

        // Update the sousClasse using partial update
        SousClasse partialUpdatedSousClasse = new SousClasse();
        partialUpdatedSousClasse.setId(sousClasse.getId());

        partialUpdatedSousClasse.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restSousClasseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSousClasse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSousClasse))
            )
            .andExpect(status().isOk());

        // Validate the SousClasse in the database
        List<SousClasse> sousClasseList = sousClasseRepository.findAll();
        assertThat(sousClasseList).hasSize(databaseSizeBeforeUpdate);
        SousClasse testSousClasse = sousClasseList.get(sousClasseList.size() - 1);
        assertThat(testSousClasse.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testSousClasse.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void patchNonExistingSousClasse() throws Exception {
        int databaseSizeBeforeUpdate = sousClasseRepository.findAll().size();
        sousClasse.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSousClasseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sousClasse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sousClasse))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousClasse in the database
        List<SousClasse> sousClasseList = sousClasseRepository.findAll();
        assertThat(sousClasseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSousClasse() throws Exception {
        int databaseSizeBeforeUpdate = sousClasseRepository.findAll().size();
        sousClasse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousClasseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sousClasse))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousClasse in the database
        List<SousClasse> sousClasseList = sousClasseRepository.findAll();
        assertThat(sousClasseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSousClasse() throws Exception {
        int databaseSizeBeforeUpdate = sousClasseRepository.findAll().size();
        sousClasse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousClasseMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sousClasse))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SousClasse in the database
        List<SousClasse> sousClasseList = sousClasseRepository.findAll();
        assertThat(sousClasseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSousClasse() throws Exception {
        // Initialize the database
        sousClasseRepository.saveAndFlush(sousClasse);

        int databaseSizeBeforeDelete = sousClasseRepository.findAll().size();

        // Delete the sousClasse
        restSousClasseMockMvc
            .perform(delete(ENTITY_API_URL_ID, sousClasse.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SousClasse> sousClasseList = sousClasseRepository.findAll();
        assertThat(sousClasseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
