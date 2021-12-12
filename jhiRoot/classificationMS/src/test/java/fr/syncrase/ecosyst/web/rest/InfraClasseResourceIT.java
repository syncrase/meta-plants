package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.InfraClasse;
import fr.syncrase.ecosyst.domain.InfraClasse;
import fr.syncrase.ecosyst.domain.SousClasse;
import fr.syncrase.ecosyst.domain.SuperOrdre;
import fr.syncrase.ecosyst.repository.InfraClasseRepository;
import fr.syncrase.ecosyst.service.criteria.InfraClasseCriteria;
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
 * Integration tests for the {@link InfraClasseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InfraClasseResourceIT {

    private static final String DEFAULT_NOM_FR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_LATIN = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LATIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/infra-classes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InfraClasseRepository infraClasseRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInfraClasseMockMvc;

    private InfraClasse infraClasse;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InfraClasse createEntity(EntityManager em) {
        InfraClasse infraClasse = new InfraClasse().nomFr(DEFAULT_NOM_FR).nomLatin(DEFAULT_NOM_LATIN);
        return infraClasse;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InfraClasse createUpdatedEntity(EntityManager em) {
        InfraClasse infraClasse = new InfraClasse().nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);
        return infraClasse;
    }

    @BeforeEach
    public void initTest() {
        infraClasse = createEntity(em);
    }

    @Test
    @Transactional
    void createInfraClasse() throws Exception {
        int databaseSizeBeforeCreate = infraClasseRepository.findAll().size();
        // Create the InfraClasse
        restInfraClasseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(infraClasse)))
            .andExpect(status().isCreated());

        // Validate the InfraClasse in the database
        List<InfraClasse> infraClasseList = infraClasseRepository.findAll();
        assertThat(infraClasseList).hasSize(databaseSizeBeforeCreate + 1);
        InfraClasse testInfraClasse = infraClasseList.get(infraClasseList.size() - 1);
        assertThat(testInfraClasse.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testInfraClasse.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void createInfraClasseWithExistingId() throws Exception {
        // Create the InfraClasse with an existing ID
        infraClasse.setId(1L);

        int databaseSizeBeforeCreate = infraClasseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInfraClasseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(infraClasse)))
            .andExpect(status().isBadRequest());

        // Validate the InfraClasse in the database
        List<InfraClasse> infraClasseList = infraClasseRepository.findAll();
        assertThat(infraClasseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomFrIsRequired() throws Exception {
        int databaseSizeBeforeTest = infraClasseRepository.findAll().size();
        // set the field null
        infraClasse.setNomFr(null);

        // Create the InfraClasse, which fails.

        restInfraClasseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(infraClasse)))
            .andExpect(status().isBadRequest());

        List<InfraClasse> infraClasseList = infraClasseRepository.findAll();
        assertThat(infraClasseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInfraClasses() throws Exception {
        // Initialize the database
        infraClasseRepository.saveAndFlush(infraClasse);

        // Get all the infraClasseList
        restInfraClasseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(infraClasse.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));
    }

    @Test
    @Transactional
    void getInfraClasse() throws Exception {
        // Initialize the database
        infraClasseRepository.saveAndFlush(infraClasse);

        // Get the infraClasse
        restInfraClasseMockMvc
            .perform(get(ENTITY_API_URL_ID, infraClasse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(infraClasse.getId().intValue()))
            .andExpect(jsonPath("$.nomFr").value(DEFAULT_NOM_FR))
            .andExpect(jsonPath("$.nomLatin").value(DEFAULT_NOM_LATIN));
    }

    @Test
    @Transactional
    void getInfraClassesByIdFiltering() throws Exception {
        // Initialize the database
        infraClasseRepository.saveAndFlush(infraClasse);

        Long id = infraClasse.getId();

        defaultInfraClasseShouldBeFound("id.equals=" + id);
        defaultInfraClasseShouldNotBeFound("id.notEquals=" + id);

        defaultInfraClasseShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInfraClasseShouldNotBeFound("id.greaterThan=" + id);

        defaultInfraClasseShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInfraClasseShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInfraClassesByNomFrIsEqualToSomething() throws Exception {
        // Initialize the database
        infraClasseRepository.saveAndFlush(infraClasse);

        // Get all the infraClasseList where nomFr equals to DEFAULT_NOM_FR
        defaultInfraClasseShouldBeFound("nomFr.equals=" + DEFAULT_NOM_FR);

        // Get all the infraClasseList where nomFr equals to UPDATED_NOM_FR
        defaultInfraClasseShouldNotBeFound("nomFr.equals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllInfraClassesByNomFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        infraClasseRepository.saveAndFlush(infraClasse);

        // Get all the infraClasseList where nomFr not equals to DEFAULT_NOM_FR
        defaultInfraClasseShouldNotBeFound("nomFr.notEquals=" + DEFAULT_NOM_FR);

        // Get all the infraClasseList where nomFr not equals to UPDATED_NOM_FR
        defaultInfraClasseShouldBeFound("nomFr.notEquals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllInfraClassesByNomFrIsInShouldWork() throws Exception {
        // Initialize the database
        infraClasseRepository.saveAndFlush(infraClasse);

        // Get all the infraClasseList where nomFr in DEFAULT_NOM_FR or UPDATED_NOM_FR
        defaultInfraClasseShouldBeFound("nomFr.in=" + DEFAULT_NOM_FR + "," + UPDATED_NOM_FR);

        // Get all the infraClasseList where nomFr equals to UPDATED_NOM_FR
        defaultInfraClasseShouldNotBeFound("nomFr.in=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllInfraClassesByNomFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        infraClasseRepository.saveAndFlush(infraClasse);

        // Get all the infraClasseList where nomFr is not null
        defaultInfraClasseShouldBeFound("nomFr.specified=true");

        // Get all the infraClasseList where nomFr is null
        defaultInfraClasseShouldNotBeFound("nomFr.specified=false");
    }

    @Test
    @Transactional
    void getAllInfraClassesByNomFrContainsSomething() throws Exception {
        // Initialize the database
        infraClasseRepository.saveAndFlush(infraClasse);

        // Get all the infraClasseList where nomFr contains DEFAULT_NOM_FR
        defaultInfraClasseShouldBeFound("nomFr.contains=" + DEFAULT_NOM_FR);

        // Get all the infraClasseList where nomFr contains UPDATED_NOM_FR
        defaultInfraClasseShouldNotBeFound("nomFr.contains=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllInfraClassesByNomFrNotContainsSomething() throws Exception {
        // Initialize the database
        infraClasseRepository.saveAndFlush(infraClasse);

        // Get all the infraClasseList where nomFr does not contain DEFAULT_NOM_FR
        defaultInfraClasseShouldNotBeFound("nomFr.doesNotContain=" + DEFAULT_NOM_FR);

        // Get all the infraClasseList where nomFr does not contain UPDATED_NOM_FR
        defaultInfraClasseShouldBeFound("nomFr.doesNotContain=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllInfraClassesByNomLatinIsEqualToSomething() throws Exception {
        // Initialize the database
        infraClasseRepository.saveAndFlush(infraClasse);

        // Get all the infraClasseList where nomLatin equals to DEFAULT_NOM_LATIN
        defaultInfraClasseShouldBeFound("nomLatin.equals=" + DEFAULT_NOM_LATIN);

        // Get all the infraClasseList where nomLatin equals to UPDATED_NOM_LATIN
        defaultInfraClasseShouldNotBeFound("nomLatin.equals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllInfraClassesByNomLatinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        infraClasseRepository.saveAndFlush(infraClasse);

        // Get all the infraClasseList where nomLatin not equals to DEFAULT_NOM_LATIN
        defaultInfraClasseShouldNotBeFound("nomLatin.notEquals=" + DEFAULT_NOM_LATIN);

        // Get all the infraClasseList where nomLatin not equals to UPDATED_NOM_LATIN
        defaultInfraClasseShouldBeFound("nomLatin.notEquals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllInfraClassesByNomLatinIsInShouldWork() throws Exception {
        // Initialize the database
        infraClasseRepository.saveAndFlush(infraClasse);

        // Get all the infraClasseList where nomLatin in DEFAULT_NOM_LATIN or UPDATED_NOM_LATIN
        defaultInfraClasseShouldBeFound("nomLatin.in=" + DEFAULT_NOM_LATIN + "," + UPDATED_NOM_LATIN);

        // Get all the infraClasseList where nomLatin equals to UPDATED_NOM_LATIN
        defaultInfraClasseShouldNotBeFound("nomLatin.in=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllInfraClassesByNomLatinIsNullOrNotNull() throws Exception {
        // Initialize the database
        infraClasseRepository.saveAndFlush(infraClasse);

        // Get all the infraClasseList where nomLatin is not null
        defaultInfraClasseShouldBeFound("nomLatin.specified=true");

        // Get all the infraClasseList where nomLatin is null
        defaultInfraClasseShouldNotBeFound("nomLatin.specified=false");
    }

    @Test
    @Transactional
    void getAllInfraClassesByNomLatinContainsSomething() throws Exception {
        // Initialize the database
        infraClasseRepository.saveAndFlush(infraClasse);

        // Get all the infraClasseList where nomLatin contains DEFAULT_NOM_LATIN
        defaultInfraClasseShouldBeFound("nomLatin.contains=" + DEFAULT_NOM_LATIN);

        // Get all the infraClasseList where nomLatin contains UPDATED_NOM_LATIN
        defaultInfraClasseShouldNotBeFound("nomLatin.contains=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllInfraClassesByNomLatinNotContainsSomething() throws Exception {
        // Initialize the database
        infraClasseRepository.saveAndFlush(infraClasse);

        // Get all the infraClasseList where nomLatin does not contain DEFAULT_NOM_LATIN
        defaultInfraClasseShouldNotBeFound("nomLatin.doesNotContain=" + DEFAULT_NOM_LATIN);

        // Get all the infraClasseList where nomLatin does not contain UPDATED_NOM_LATIN
        defaultInfraClasseShouldBeFound("nomLatin.doesNotContain=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllInfraClassesBySuperOrdresIsEqualToSomething() throws Exception {
        // Initialize the database
        infraClasseRepository.saveAndFlush(infraClasse);
        SuperOrdre superOrdres;
        if (TestUtil.findAll(em, SuperOrdre.class).isEmpty()) {
            superOrdres = SuperOrdreResourceIT.createEntity(em);
            em.persist(superOrdres);
            em.flush();
        } else {
            superOrdres = TestUtil.findAll(em, SuperOrdre.class).get(0);
        }
        em.persist(superOrdres);
        em.flush();
        infraClasse.addSuperOrdres(superOrdres);
        infraClasseRepository.saveAndFlush(infraClasse);
        Long superOrdresId = superOrdres.getId();

        // Get all the infraClasseList where superOrdres equals to superOrdresId
        defaultInfraClasseShouldBeFound("superOrdresId.equals=" + superOrdresId);

        // Get all the infraClasseList where superOrdres equals to (superOrdresId + 1)
        defaultInfraClasseShouldNotBeFound("superOrdresId.equals=" + (superOrdresId + 1));
    }

    @Test
    @Transactional
    void getAllInfraClassesBySynonymesIsEqualToSomething() throws Exception {
        // Initialize the database
        infraClasseRepository.saveAndFlush(infraClasse);
        InfraClasse synonymes;
        if (TestUtil.findAll(em, InfraClasse.class).isEmpty()) {
            synonymes = InfraClasseResourceIT.createEntity(em);
            em.persist(synonymes);
            em.flush();
        } else {
            synonymes = TestUtil.findAll(em, InfraClasse.class).get(0);
        }
        em.persist(synonymes);
        em.flush();
        infraClasse.addSynonymes(synonymes);
        infraClasseRepository.saveAndFlush(infraClasse);
        Long synonymesId = synonymes.getId();

        // Get all the infraClasseList where synonymes equals to synonymesId
        defaultInfraClasseShouldBeFound("synonymesId.equals=" + synonymesId);

        // Get all the infraClasseList where synonymes equals to (synonymesId + 1)
        defaultInfraClasseShouldNotBeFound("synonymesId.equals=" + (synonymesId + 1));
    }

    @Test
    @Transactional
    void getAllInfraClassesBySousClasseIsEqualToSomething() throws Exception {
        // Initialize the database
        infraClasseRepository.saveAndFlush(infraClasse);
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
        infraClasse.setSousClasse(sousClasse);
        infraClasseRepository.saveAndFlush(infraClasse);
        Long sousClasseId = sousClasse.getId();

        // Get all the infraClasseList where sousClasse equals to sousClasseId
        defaultInfraClasseShouldBeFound("sousClasseId.equals=" + sousClasseId);

        // Get all the infraClasseList where sousClasse equals to (sousClasseId + 1)
        defaultInfraClasseShouldNotBeFound("sousClasseId.equals=" + (sousClasseId + 1));
    }

    @Test
    @Transactional
    void getAllInfraClassesByInfraClasseIsEqualToSomething() throws Exception {
        // Initialize the database
        infraClasseRepository.saveAndFlush(infraClasse);
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
        infraClasse.setInfraClasse(infraClasse);
        infraClasseRepository.saveAndFlush(infraClasse);
        Long infraClasseId = infraClasse.getId();

        // Get all the infraClasseList where infraClasse equals to infraClasseId
        defaultInfraClasseShouldBeFound("infraClasseId.equals=" + infraClasseId);

        // Get all the infraClasseList where infraClasse equals to (infraClasseId + 1)
        defaultInfraClasseShouldNotBeFound("infraClasseId.equals=" + (infraClasseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInfraClasseShouldBeFound(String filter) throws Exception {
        restInfraClasseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(infraClasse.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));

        // Check, that the count call also returns 1
        restInfraClasseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInfraClasseShouldNotBeFound(String filter) throws Exception {
        restInfraClasseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInfraClasseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInfraClasse() throws Exception {
        // Get the infraClasse
        restInfraClasseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInfraClasse() throws Exception {
        // Initialize the database
        infraClasseRepository.saveAndFlush(infraClasse);

        int databaseSizeBeforeUpdate = infraClasseRepository.findAll().size();

        // Update the infraClasse
        InfraClasse updatedInfraClasse = infraClasseRepository.findById(infraClasse.getId()).get();
        // Disconnect from session so that the updates on updatedInfraClasse are not directly saved in db
        em.detach(updatedInfraClasse);
        updatedInfraClasse.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restInfraClasseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInfraClasse.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedInfraClasse))
            )
            .andExpect(status().isOk());

        // Validate the InfraClasse in the database
        List<InfraClasse> infraClasseList = infraClasseRepository.findAll();
        assertThat(infraClasseList).hasSize(databaseSizeBeforeUpdate);
        InfraClasse testInfraClasse = infraClasseList.get(infraClasseList.size() - 1);
        assertThat(testInfraClasse.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testInfraClasse.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void putNonExistingInfraClasse() throws Exception {
        int databaseSizeBeforeUpdate = infraClasseRepository.findAll().size();
        infraClasse.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInfraClasseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, infraClasse.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(infraClasse))
            )
            .andExpect(status().isBadRequest());

        // Validate the InfraClasse in the database
        List<InfraClasse> infraClasseList = infraClasseRepository.findAll();
        assertThat(infraClasseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInfraClasse() throws Exception {
        int databaseSizeBeforeUpdate = infraClasseRepository.findAll().size();
        infraClasse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInfraClasseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(infraClasse))
            )
            .andExpect(status().isBadRequest());

        // Validate the InfraClasse in the database
        List<InfraClasse> infraClasseList = infraClasseRepository.findAll();
        assertThat(infraClasseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInfraClasse() throws Exception {
        int databaseSizeBeforeUpdate = infraClasseRepository.findAll().size();
        infraClasse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInfraClasseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(infraClasse)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InfraClasse in the database
        List<InfraClasse> infraClasseList = infraClasseRepository.findAll();
        assertThat(infraClasseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInfraClasseWithPatch() throws Exception {
        // Initialize the database
        infraClasseRepository.saveAndFlush(infraClasse);

        int databaseSizeBeforeUpdate = infraClasseRepository.findAll().size();

        // Update the infraClasse using partial update
        InfraClasse partialUpdatedInfraClasse = new InfraClasse();
        partialUpdatedInfraClasse.setId(infraClasse.getId());

        restInfraClasseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInfraClasse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInfraClasse))
            )
            .andExpect(status().isOk());

        // Validate the InfraClasse in the database
        List<InfraClasse> infraClasseList = infraClasseRepository.findAll();
        assertThat(infraClasseList).hasSize(databaseSizeBeforeUpdate);
        InfraClasse testInfraClasse = infraClasseList.get(infraClasseList.size() - 1);
        assertThat(testInfraClasse.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testInfraClasse.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void fullUpdateInfraClasseWithPatch() throws Exception {
        // Initialize the database
        infraClasseRepository.saveAndFlush(infraClasse);

        int databaseSizeBeforeUpdate = infraClasseRepository.findAll().size();

        // Update the infraClasse using partial update
        InfraClasse partialUpdatedInfraClasse = new InfraClasse();
        partialUpdatedInfraClasse.setId(infraClasse.getId());

        partialUpdatedInfraClasse.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restInfraClasseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInfraClasse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInfraClasse))
            )
            .andExpect(status().isOk());

        // Validate the InfraClasse in the database
        List<InfraClasse> infraClasseList = infraClasseRepository.findAll();
        assertThat(infraClasseList).hasSize(databaseSizeBeforeUpdate);
        InfraClasse testInfraClasse = infraClasseList.get(infraClasseList.size() - 1);
        assertThat(testInfraClasse.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testInfraClasse.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void patchNonExistingInfraClasse() throws Exception {
        int databaseSizeBeforeUpdate = infraClasseRepository.findAll().size();
        infraClasse.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInfraClasseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, infraClasse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(infraClasse))
            )
            .andExpect(status().isBadRequest());

        // Validate the InfraClasse in the database
        List<InfraClasse> infraClasseList = infraClasseRepository.findAll();
        assertThat(infraClasseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInfraClasse() throws Exception {
        int databaseSizeBeforeUpdate = infraClasseRepository.findAll().size();
        infraClasse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInfraClasseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(infraClasse))
            )
            .andExpect(status().isBadRequest());

        // Validate the InfraClasse in the database
        List<InfraClasse> infraClasseList = infraClasseRepository.findAll();
        assertThat(infraClasseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInfraClasse() throws Exception {
        int databaseSizeBeforeUpdate = infraClasseRepository.findAll().size();
        infraClasse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInfraClasseMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(infraClasse))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InfraClasse in the database
        List<InfraClasse> infraClasseList = infraClasseRepository.findAll();
        assertThat(infraClasseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInfraClasse() throws Exception {
        // Initialize the database
        infraClasseRepository.saveAndFlush(infraClasse);

        int databaseSizeBeforeDelete = infraClasseRepository.findAll().size();

        // Delete the infraClasse
        restInfraClasseMockMvc
            .perform(delete(ENTITY_API_URL_ID, infraClasse.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InfraClasse> infraClasseList = infraClasseRepository.findAll();
        assertThat(infraClasseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
