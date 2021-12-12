package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.InfraOrdre;
import fr.syncrase.ecosyst.domain.InfraOrdre;
import fr.syncrase.ecosyst.domain.MicroOrdre;
import fr.syncrase.ecosyst.domain.SousOrdre;
import fr.syncrase.ecosyst.repository.InfraOrdreRepository;
import fr.syncrase.ecosyst.service.criteria.InfraOrdreCriteria;
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
 * Integration tests for the {@link InfraOrdreResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InfraOrdreResourceIT {

    private static final String DEFAULT_NOM_FR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_LATIN = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LATIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/infra-ordres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InfraOrdreRepository infraOrdreRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInfraOrdreMockMvc;

    private InfraOrdre infraOrdre;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InfraOrdre createEntity(EntityManager em) {
        InfraOrdre infraOrdre = new InfraOrdre().nomFr(DEFAULT_NOM_FR).nomLatin(DEFAULT_NOM_LATIN);
        return infraOrdre;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InfraOrdre createUpdatedEntity(EntityManager em) {
        InfraOrdre infraOrdre = new InfraOrdre().nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);
        return infraOrdre;
    }

    @BeforeEach
    public void initTest() {
        infraOrdre = createEntity(em);
    }

    @Test
    @Transactional
    void createInfraOrdre() throws Exception {
        int databaseSizeBeforeCreate = infraOrdreRepository.findAll().size();
        // Create the InfraOrdre
        restInfraOrdreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(infraOrdre)))
            .andExpect(status().isCreated());

        // Validate the InfraOrdre in the database
        List<InfraOrdre> infraOrdreList = infraOrdreRepository.findAll();
        assertThat(infraOrdreList).hasSize(databaseSizeBeforeCreate + 1);
        InfraOrdre testInfraOrdre = infraOrdreList.get(infraOrdreList.size() - 1);
        assertThat(testInfraOrdre.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testInfraOrdre.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void createInfraOrdreWithExistingId() throws Exception {
        // Create the InfraOrdre with an existing ID
        infraOrdre.setId(1L);

        int databaseSizeBeforeCreate = infraOrdreRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInfraOrdreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(infraOrdre)))
            .andExpect(status().isBadRequest());

        // Validate the InfraOrdre in the database
        List<InfraOrdre> infraOrdreList = infraOrdreRepository.findAll();
        assertThat(infraOrdreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomFrIsRequired() throws Exception {
        int databaseSizeBeforeTest = infraOrdreRepository.findAll().size();
        // set the field null
        infraOrdre.setNomFr(null);

        // Create the InfraOrdre, which fails.

        restInfraOrdreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(infraOrdre)))
            .andExpect(status().isBadRequest());

        List<InfraOrdre> infraOrdreList = infraOrdreRepository.findAll();
        assertThat(infraOrdreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInfraOrdres() throws Exception {
        // Initialize the database
        infraOrdreRepository.saveAndFlush(infraOrdre);

        // Get all the infraOrdreList
        restInfraOrdreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(infraOrdre.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));
    }

    @Test
    @Transactional
    void getInfraOrdre() throws Exception {
        // Initialize the database
        infraOrdreRepository.saveAndFlush(infraOrdre);

        // Get the infraOrdre
        restInfraOrdreMockMvc
            .perform(get(ENTITY_API_URL_ID, infraOrdre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(infraOrdre.getId().intValue()))
            .andExpect(jsonPath("$.nomFr").value(DEFAULT_NOM_FR))
            .andExpect(jsonPath("$.nomLatin").value(DEFAULT_NOM_LATIN));
    }

    @Test
    @Transactional
    void getInfraOrdresByIdFiltering() throws Exception {
        // Initialize the database
        infraOrdreRepository.saveAndFlush(infraOrdre);

        Long id = infraOrdre.getId();

        defaultInfraOrdreShouldBeFound("id.equals=" + id);
        defaultInfraOrdreShouldNotBeFound("id.notEquals=" + id);

        defaultInfraOrdreShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInfraOrdreShouldNotBeFound("id.greaterThan=" + id);

        defaultInfraOrdreShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInfraOrdreShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInfraOrdresByNomFrIsEqualToSomething() throws Exception {
        // Initialize the database
        infraOrdreRepository.saveAndFlush(infraOrdre);

        // Get all the infraOrdreList where nomFr equals to DEFAULT_NOM_FR
        defaultInfraOrdreShouldBeFound("nomFr.equals=" + DEFAULT_NOM_FR);

        // Get all the infraOrdreList where nomFr equals to UPDATED_NOM_FR
        defaultInfraOrdreShouldNotBeFound("nomFr.equals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllInfraOrdresByNomFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        infraOrdreRepository.saveAndFlush(infraOrdre);

        // Get all the infraOrdreList where nomFr not equals to DEFAULT_NOM_FR
        defaultInfraOrdreShouldNotBeFound("nomFr.notEquals=" + DEFAULT_NOM_FR);

        // Get all the infraOrdreList where nomFr not equals to UPDATED_NOM_FR
        defaultInfraOrdreShouldBeFound("nomFr.notEquals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllInfraOrdresByNomFrIsInShouldWork() throws Exception {
        // Initialize the database
        infraOrdreRepository.saveAndFlush(infraOrdre);

        // Get all the infraOrdreList where nomFr in DEFAULT_NOM_FR or UPDATED_NOM_FR
        defaultInfraOrdreShouldBeFound("nomFr.in=" + DEFAULT_NOM_FR + "," + UPDATED_NOM_FR);

        // Get all the infraOrdreList where nomFr equals to UPDATED_NOM_FR
        defaultInfraOrdreShouldNotBeFound("nomFr.in=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllInfraOrdresByNomFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        infraOrdreRepository.saveAndFlush(infraOrdre);

        // Get all the infraOrdreList where nomFr is not null
        defaultInfraOrdreShouldBeFound("nomFr.specified=true");

        // Get all the infraOrdreList where nomFr is null
        defaultInfraOrdreShouldNotBeFound("nomFr.specified=false");
    }

    @Test
    @Transactional
    void getAllInfraOrdresByNomFrContainsSomething() throws Exception {
        // Initialize the database
        infraOrdreRepository.saveAndFlush(infraOrdre);

        // Get all the infraOrdreList where nomFr contains DEFAULT_NOM_FR
        defaultInfraOrdreShouldBeFound("nomFr.contains=" + DEFAULT_NOM_FR);

        // Get all the infraOrdreList where nomFr contains UPDATED_NOM_FR
        defaultInfraOrdreShouldNotBeFound("nomFr.contains=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllInfraOrdresByNomFrNotContainsSomething() throws Exception {
        // Initialize the database
        infraOrdreRepository.saveAndFlush(infraOrdre);

        // Get all the infraOrdreList where nomFr does not contain DEFAULT_NOM_FR
        defaultInfraOrdreShouldNotBeFound("nomFr.doesNotContain=" + DEFAULT_NOM_FR);

        // Get all the infraOrdreList where nomFr does not contain UPDATED_NOM_FR
        defaultInfraOrdreShouldBeFound("nomFr.doesNotContain=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllInfraOrdresByNomLatinIsEqualToSomething() throws Exception {
        // Initialize the database
        infraOrdreRepository.saveAndFlush(infraOrdre);

        // Get all the infraOrdreList where nomLatin equals to DEFAULT_NOM_LATIN
        defaultInfraOrdreShouldBeFound("nomLatin.equals=" + DEFAULT_NOM_LATIN);

        // Get all the infraOrdreList where nomLatin equals to UPDATED_NOM_LATIN
        defaultInfraOrdreShouldNotBeFound("nomLatin.equals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllInfraOrdresByNomLatinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        infraOrdreRepository.saveAndFlush(infraOrdre);

        // Get all the infraOrdreList where nomLatin not equals to DEFAULT_NOM_LATIN
        defaultInfraOrdreShouldNotBeFound("nomLatin.notEquals=" + DEFAULT_NOM_LATIN);

        // Get all the infraOrdreList where nomLatin not equals to UPDATED_NOM_LATIN
        defaultInfraOrdreShouldBeFound("nomLatin.notEquals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllInfraOrdresByNomLatinIsInShouldWork() throws Exception {
        // Initialize the database
        infraOrdreRepository.saveAndFlush(infraOrdre);

        // Get all the infraOrdreList where nomLatin in DEFAULT_NOM_LATIN or UPDATED_NOM_LATIN
        defaultInfraOrdreShouldBeFound("nomLatin.in=" + DEFAULT_NOM_LATIN + "," + UPDATED_NOM_LATIN);

        // Get all the infraOrdreList where nomLatin equals to UPDATED_NOM_LATIN
        defaultInfraOrdreShouldNotBeFound("nomLatin.in=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllInfraOrdresByNomLatinIsNullOrNotNull() throws Exception {
        // Initialize the database
        infraOrdreRepository.saveAndFlush(infraOrdre);

        // Get all the infraOrdreList where nomLatin is not null
        defaultInfraOrdreShouldBeFound("nomLatin.specified=true");

        // Get all the infraOrdreList where nomLatin is null
        defaultInfraOrdreShouldNotBeFound("nomLatin.specified=false");
    }

    @Test
    @Transactional
    void getAllInfraOrdresByNomLatinContainsSomething() throws Exception {
        // Initialize the database
        infraOrdreRepository.saveAndFlush(infraOrdre);

        // Get all the infraOrdreList where nomLatin contains DEFAULT_NOM_LATIN
        defaultInfraOrdreShouldBeFound("nomLatin.contains=" + DEFAULT_NOM_LATIN);

        // Get all the infraOrdreList where nomLatin contains UPDATED_NOM_LATIN
        defaultInfraOrdreShouldNotBeFound("nomLatin.contains=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllInfraOrdresByNomLatinNotContainsSomething() throws Exception {
        // Initialize the database
        infraOrdreRepository.saveAndFlush(infraOrdre);

        // Get all the infraOrdreList where nomLatin does not contain DEFAULT_NOM_LATIN
        defaultInfraOrdreShouldNotBeFound("nomLatin.doesNotContain=" + DEFAULT_NOM_LATIN);

        // Get all the infraOrdreList where nomLatin does not contain UPDATED_NOM_LATIN
        defaultInfraOrdreShouldBeFound("nomLatin.doesNotContain=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllInfraOrdresByMicroOrdresIsEqualToSomething() throws Exception {
        // Initialize the database
        infraOrdreRepository.saveAndFlush(infraOrdre);
        MicroOrdre microOrdres;
        if (TestUtil.findAll(em, MicroOrdre.class).isEmpty()) {
            microOrdres = MicroOrdreResourceIT.createEntity(em);
            em.persist(microOrdres);
            em.flush();
        } else {
            microOrdres = TestUtil.findAll(em, MicroOrdre.class).get(0);
        }
        em.persist(microOrdres);
        em.flush();
        infraOrdre.addMicroOrdres(microOrdres);
        infraOrdreRepository.saveAndFlush(infraOrdre);
        Long microOrdresId = microOrdres.getId();

        // Get all the infraOrdreList where microOrdres equals to microOrdresId
        defaultInfraOrdreShouldBeFound("microOrdresId.equals=" + microOrdresId);

        // Get all the infraOrdreList where microOrdres equals to (microOrdresId + 1)
        defaultInfraOrdreShouldNotBeFound("microOrdresId.equals=" + (microOrdresId + 1));
    }

    @Test
    @Transactional
    void getAllInfraOrdresBySynonymesIsEqualToSomething() throws Exception {
        // Initialize the database
        infraOrdreRepository.saveAndFlush(infraOrdre);
        InfraOrdre synonymes;
        if (TestUtil.findAll(em, InfraOrdre.class).isEmpty()) {
            synonymes = InfraOrdreResourceIT.createEntity(em);
            em.persist(synonymes);
            em.flush();
        } else {
            synonymes = TestUtil.findAll(em, InfraOrdre.class).get(0);
        }
        em.persist(synonymes);
        em.flush();
        infraOrdre.addSynonymes(synonymes);
        infraOrdreRepository.saveAndFlush(infraOrdre);
        Long synonymesId = synonymes.getId();

        // Get all the infraOrdreList where synonymes equals to synonymesId
        defaultInfraOrdreShouldBeFound("synonymesId.equals=" + synonymesId);

        // Get all the infraOrdreList where synonymes equals to (synonymesId + 1)
        defaultInfraOrdreShouldNotBeFound("synonymesId.equals=" + (synonymesId + 1));
    }

    @Test
    @Transactional
    void getAllInfraOrdresBySousOrdreIsEqualToSomething() throws Exception {
        // Initialize the database
        infraOrdreRepository.saveAndFlush(infraOrdre);
        SousOrdre sousOrdre;
        if (TestUtil.findAll(em, SousOrdre.class).isEmpty()) {
            sousOrdre = SousOrdreResourceIT.createEntity(em);
            em.persist(sousOrdre);
            em.flush();
        } else {
            sousOrdre = TestUtil.findAll(em, SousOrdre.class).get(0);
        }
        em.persist(sousOrdre);
        em.flush();
        infraOrdre.setSousOrdre(sousOrdre);
        infraOrdreRepository.saveAndFlush(infraOrdre);
        Long sousOrdreId = sousOrdre.getId();

        // Get all the infraOrdreList where sousOrdre equals to sousOrdreId
        defaultInfraOrdreShouldBeFound("sousOrdreId.equals=" + sousOrdreId);

        // Get all the infraOrdreList where sousOrdre equals to (sousOrdreId + 1)
        defaultInfraOrdreShouldNotBeFound("sousOrdreId.equals=" + (sousOrdreId + 1));
    }

    @Test
    @Transactional
    void getAllInfraOrdresByInfraOrdreIsEqualToSomething() throws Exception {
        // Initialize the database
        infraOrdreRepository.saveAndFlush(infraOrdre);
        InfraOrdre infraOrdre;
        if (TestUtil.findAll(em, InfraOrdre.class).isEmpty()) {
            infraOrdre = InfraOrdreResourceIT.createEntity(em);
            em.persist(infraOrdre);
            em.flush();
        } else {
            infraOrdre = TestUtil.findAll(em, InfraOrdre.class).get(0);
        }
        em.persist(infraOrdre);
        em.flush();
        infraOrdre.setInfraOrdre(infraOrdre);
        infraOrdreRepository.saveAndFlush(infraOrdre);
        Long infraOrdreId = infraOrdre.getId();

        // Get all the infraOrdreList where infraOrdre equals to infraOrdreId
        defaultInfraOrdreShouldBeFound("infraOrdreId.equals=" + infraOrdreId);

        // Get all the infraOrdreList where infraOrdre equals to (infraOrdreId + 1)
        defaultInfraOrdreShouldNotBeFound("infraOrdreId.equals=" + (infraOrdreId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInfraOrdreShouldBeFound(String filter) throws Exception {
        restInfraOrdreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(infraOrdre.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));

        // Check, that the count call also returns 1
        restInfraOrdreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInfraOrdreShouldNotBeFound(String filter) throws Exception {
        restInfraOrdreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInfraOrdreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInfraOrdre() throws Exception {
        // Get the infraOrdre
        restInfraOrdreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInfraOrdre() throws Exception {
        // Initialize the database
        infraOrdreRepository.saveAndFlush(infraOrdre);

        int databaseSizeBeforeUpdate = infraOrdreRepository.findAll().size();

        // Update the infraOrdre
        InfraOrdre updatedInfraOrdre = infraOrdreRepository.findById(infraOrdre.getId()).get();
        // Disconnect from session so that the updates on updatedInfraOrdre are not directly saved in db
        em.detach(updatedInfraOrdre);
        updatedInfraOrdre.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restInfraOrdreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInfraOrdre.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedInfraOrdre))
            )
            .andExpect(status().isOk());

        // Validate the InfraOrdre in the database
        List<InfraOrdre> infraOrdreList = infraOrdreRepository.findAll();
        assertThat(infraOrdreList).hasSize(databaseSizeBeforeUpdate);
        InfraOrdre testInfraOrdre = infraOrdreList.get(infraOrdreList.size() - 1);
        assertThat(testInfraOrdre.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testInfraOrdre.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void putNonExistingInfraOrdre() throws Exception {
        int databaseSizeBeforeUpdate = infraOrdreRepository.findAll().size();
        infraOrdre.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInfraOrdreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, infraOrdre.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(infraOrdre))
            )
            .andExpect(status().isBadRequest());

        // Validate the InfraOrdre in the database
        List<InfraOrdre> infraOrdreList = infraOrdreRepository.findAll();
        assertThat(infraOrdreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInfraOrdre() throws Exception {
        int databaseSizeBeforeUpdate = infraOrdreRepository.findAll().size();
        infraOrdre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInfraOrdreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(infraOrdre))
            )
            .andExpect(status().isBadRequest());

        // Validate the InfraOrdre in the database
        List<InfraOrdre> infraOrdreList = infraOrdreRepository.findAll();
        assertThat(infraOrdreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInfraOrdre() throws Exception {
        int databaseSizeBeforeUpdate = infraOrdreRepository.findAll().size();
        infraOrdre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInfraOrdreMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(infraOrdre)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InfraOrdre in the database
        List<InfraOrdre> infraOrdreList = infraOrdreRepository.findAll();
        assertThat(infraOrdreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInfraOrdreWithPatch() throws Exception {
        // Initialize the database
        infraOrdreRepository.saveAndFlush(infraOrdre);

        int databaseSizeBeforeUpdate = infraOrdreRepository.findAll().size();

        // Update the infraOrdre using partial update
        InfraOrdre partialUpdatedInfraOrdre = new InfraOrdre();
        partialUpdatedInfraOrdre.setId(infraOrdre.getId());

        partialUpdatedInfraOrdre.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restInfraOrdreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInfraOrdre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInfraOrdre))
            )
            .andExpect(status().isOk());

        // Validate the InfraOrdre in the database
        List<InfraOrdre> infraOrdreList = infraOrdreRepository.findAll();
        assertThat(infraOrdreList).hasSize(databaseSizeBeforeUpdate);
        InfraOrdre testInfraOrdre = infraOrdreList.get(infraOrdreList.size() - 1);
        assertThat(testInfraOrdre.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testInfraOrdre.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void fullUpdateInfraOrdreWithPatch() throws Exception {
        // Initialize the database
        infraOrdreRepository.saveAndFlush(infraOrdre);

        int databaseSizeBeforeUpdate = infraOrdreRepository.findAll().size();

        // Update the infraOrdre using partial update
        InfraOrdre partialUpdatedInfraOrdre = new InfraOrdre();
        partialUpdatedInfraOrdre.setId(infraOrdre.getId());

        partialUpdatedInfraOrdre.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restInfraOrdreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInfraOrdre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInfraOrdre))
            )
            .andExpect(status().isOk());

        // Validate the InfraOrdre in the database
        List<InfraOrdre> infraOrdreList = infraOrdreRepository.findAll();
        assertThat(infraOrdreList).hasSize(databaseSizeBeforeUpdate);
        InfraOrdre testInfraOrdre = infraOrdreList.get(infraOrdreList.size() - 1);
        assertThat(testInfraOrdre.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testInfraOrdre.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void patchNonExistingInfraOrdre() throws Exception {
        int databaseSizeBeforeUpdate = infraOrdreRepository.findAll().size();
        infraOrdre.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInfraOrdreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, infraOrdre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(infraOrdre))
            )
            .andExpect(status().isBadRequest());

        // Validate the InfraOrdre in the database
        List<InfraOrdre> infraOrdreList = infraOrdreRepository.findAll();
        assertThat(infraOrdreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInfraOrdre() throws Exception {
        int databaseSizeBeforeUpdate = infraOrdreRepository.findAll().size();
        infraOrdre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInfraOrdreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(infraOrdre))
            )
            .andExpect(status().isBadRequest());

        // Validate the InfraOrdre in the database
        List<InfraOrdre> infraOrdreList = infraOrdreRepository.findAll();
        assertThat(infraOrdreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInfraOrdre() throws Exception {
        int databaseSizeBeforeUpdate = infraOrdreRepository.findAll().size();
        infraOrdre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInfraOrdreMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(infraOrdre))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InfraOrdre in the database
        List<InfraOrdre> infraOrdreList = infraOrdreRepository.findAll();
        assertThat(infraOrdreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInfraOrdre() throws Exception {
        // Initialize the database
        infraOrdreRepository.saveAndFlush(infraOrdre);

        int databaseSizeBeforeDelete = infraOrdreRepository.findAll().size();

        // Delete the infraOrdre
        restInfraOrdreMockMvc
            .perform(delete(ENTITY_API_URL_ID, infraOrdre.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InfraOrdre> infraOrdreList = infraOrdreRepository.findAll();
        assertThat(infraOrdreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
