package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.InfraRegne;
import fr.syncrase.ecosyst.domain.InfraRegne;
import fr.syncrase.ecosyst.domain.Rameau;
import fr.syncrase.ecosyst.domain.SuperDivision;
import fr.syncrase.ecosyst.repository.InfraRegneRepository;
import fr.syncrase.ecosyst.service.criteria.InfraRegneCriteria;
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
 * Integration tests for the {@link InfraRegneResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InfraRegneResourceIT {

    private static final String DEFAULT_NOM_FR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_LATIN = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LATIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/infra-regnes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InfraRegneRepository infraRegneRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInfraRegneMockMvc;

    private InfraRegne infraRegne;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InfraRegne createEntity(EntityManager em) {
        InfraRegne infraRegne = new InfraRegne().nomFr(DEFAULT_NOM_FR).nomLatin(DEFAULT_NOM_LATIN);
        return infraRegne;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InfraRegne createUpdatedEntity(EntityManager em) {
        InfraRegne infraRegne = new InfraRegne().nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);
        return infraRegne;
    }

    @BeforeEach
    public void initTest() {
        infraRegne = createEntity(em);
    }

    @Test
    @Transactional
    void createInfraRegne() throws Exception {
        int databaseSizeBeforeCreate = infraRegneRepository.findAll().size();
        // Create the InfraRegne
        restInfraRegneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(infraRegne)))
            .andExpect(status().isCreated());

        // Validate the InfraRegne in the database
        List<InfraRegne> infraRegneList = infraRegneRepository.findAll();
        assertThat(infraRegneList).hasSize(databaseSizeBeforeCreate + 1);
        InfraRegne testInfraRegne = infraRegneList.get(infraRegneList.size() - 1);
        assertThat(testInfraRegne.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testInfraRegne.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void createInfraRegneWithExistingId() throws Exception {
        // Create the InfraRegne with an existing ID
        infraRegne.setId(1L);

        int databaseSizeBeforeCreate = infraRegneRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInfraRegneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(infraRegne)))
            .andExpect(status().isBadRequest());

        // Validate the InfraRegne in the database
        List<InfraRegne> infraRegneList = infraRegneRepository.findAll();
        assertThat(infraRegneList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomFrIsRequired() throws Exception {
        int databaseSizeBeforeTest = infraRegneRepository.findAll().size();
        // set the field null
        infraRegne.setNomFr(null);

        // Create the InfraRegne, which fails.

        restInfraRegneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(infraRegne)))
            .andExpect(status().isBadRequest());

        List<InfraRegne> infraRegneList = infraRegneRepository.findAll();
        assertThat(infraRegneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInfraRegnes() throws Exception {
        // Initialize the database
        infraRegneRepository.saveAndFlush(infraRegne);

        // Get all the infraRegneList
        restInfraRegneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(infraRegne.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));
    }

    @Test
    @Transactional
    void getInfraRegne() throws Exception {
        // Initialize the database
        infraRegneRepository.saveAndFlush(infraRegne);

        // Get the infraRegne
        restInfraRegneMockMvc
            .perform(get(ENTITY_API_URL_ID, infraRegne.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(infraRegne.getId().intValue()))
            .andExpect(jsonPath("$.nomFr").value(DEFAULT_NOM_FR))
            .andExpect(jsonPath("$.nomLatin").value(DEFAULT_NOM_LATIN));
    }

    @Test
    @Transactional
    void getInfraRegnesByIdFiltering() throws Exception {
        // Initialize the database
        infraRegneRepository.saveAndFlush(infraRegne);

        Long id = infraRegne.getId();

        defaultInfraRegneShouldBeFound("id.equals=" + id);
        defaultInfraRegneShouldNotBeFound("id.notEquals=" + id);

        defaultInfraRegneShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultInfraRegneShouldNotBeFound("id.greaterThan=" + id);

        defaultInfraRegneShouldBeFound("id.lessThanOrEqual=" + id);
        defaultInfraRegneShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInfraRegnesByNomFrIsEqualToSomething() throws Exception {
        // Initialize the database
        infraRegneRepository.saveAndFlush(infraRegne);

        // Get all the infraRegneList where nomFr equals to DEFAULT_NOM_FR
        defaultInfraRegneShouldBeFound("nomFr.equals=" + DEFAULT_NOM_FR);

        // Get all the infraRegneList where nomFr equals to UPDATED_NOM_FR
        defaultInfraRegneShouldNotBeFound("nomFr.equals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllInfraRegnesByNomFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        infraRegneRepository.saveAndFlush(infraRegne);

        // Get all the infraRegneList where nomFr not equals to DEFAULT_NOM_FR
        defaultInfraRegneShouldNotBeFound("nomFr.notEquals=" + DEFAULT_NOM_FR);

        // Get all the infraRegneList where nomFr not equals to UPDATED_NOM_FR
        defaultInfraRegneShouldBeFound("nomFr.notEquals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllInfraRegnesByNomFrIsInShouldWork() throws Exception {
        // Initialize the database
        infraRegneRepository.saveAndFlush(infraRegne);

        // Get all the infraRegneList where nomFr in DEFAULT_NOM_FR or UPDATED_NOM_FR
        defaultInfraRegneShouldBeFound("nomFr.in=" + DEFAULT_NOM_FR + "," + UPDATED_NOM_FR);

        // Get all the infraRegneList where nomFr equals to UPDATED_NOM_FR
        defaultInfraRegneShouldNotBeFound("nomFr.in=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllInfraRegnesByNomFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        infraRegneRepository.saveAndFlush(infraRegne);

        // Get all the infraRegneList where nomFr is not null
        defaultInfraRegneShouldBeFound("nomFr.specified=true");

        // Get all the infraRegneList where nomFr is null
        defaultInfraRegneShouldNotBeFound("nomFr.specified=false");
    }

    @Test
    @Transactional
    void getAllInfraRegnesByNomFrContainsSomething() throws Exception {
        // Initialize the database
        infraRegneRepository.saveAndFlush(infraRegne);

        // Get all the infraRegneList where nomFr contains DEFAULT_NOM_FR
        defaultInfraRegneShouldBeFound("nomFr.contains=" + DEFAULT_NOM_FR);

        // Get all the infraRegneList where nomFr contains UPDATED_NOM_FR
        defaultInfraRegneShouldNotBeFound("nomFr.contains=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllInfraRegnesByNomFrNotContainsSomething() throws Exception {
        // Initialize the database
        infraRegneRepository.saveAndFlush(infraRegne);

        // Get all the infraRegneList where nomFr does not contain DEFAULT_NOM_FR
        defaultInfraRegneShouldNotBeFound("nomFr.doesNotContain=" + DEFAULT_NOM_FR);

        // Get all the infraRegneList where nomFr does not contain UPDATED_NOM_FR
        defaultInfraRegneShouldBeFound("nomFr.doesNotContain=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllInfraRegnesByNomLatinIsEqualToSomething() throws Exception {
        // Initialize the database
        infraRegneRepository.saveAndFlush(infraRegne);

        // Get all the infraRegneList where nomLatin equals to DEFAULT_NOM_LATIN
        defaultInfraRegneShouldBeFound("nomLatin.equals=" + DEFAULT_NOM_LATIN);

        // Get all the infraRegneList where nomLatin equals to UPDATED_NOM_LATIN
        defaultInfraRegneShouldNotBeFound("nomLatin.equals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllInfraRegnesByNomLatinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        infraRegneRepository.saveAndFlush(infraRegne);

        // Get all the infraRegneList where nomLatin not equals to DEFAULT_NOM_LATIN
        defaultInfraRegneShouldNotBeFound("nomLatin.notEquals=" + DEFAULT_NOM_LATIN);

        // Get all the infraRegneList where nomLatin not equals to UPDATED_NOM_LATIN
        defaultInfraRegneShouldBeFound("nomLatin.notEquals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllInfraRegnesByNomLatinIsInShouldWork() throws Exception {
        // Initialize the database
        infraRegneRepository.saveAndFlush(infraRegne);

        // Get all the infraRegneList where nomLatin in DEFAULT_NOM_LATIN or UPDATED_NOM_LATIN
        defaultInfraRegneShouldBeFound("nomLatin.in=" + DEFAULT_NOM_LATIN + "," + UPDATED_NOM_LATIN);

        // Get all the infraRegneList where nomLatin equals to UPDATED_NOM_LATIN
        defaultInfraRegneShouldNotBeFound("nomLatin.in=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllInfraRegnesByNomLatinIsNullOrNotNull() throws Exception {
        // Initialize the database
        infraRegneRepository.saveAndFlush(infraRegne);

        // Get all the infraRegneList where nomLatin is not null
        defaultInfraRegneShouldBeFound("nomLatin.specified=true");

        // Get all the infraRegneList where nomLatin is null
        defaultInfraRegneShouldNotBeFound("nomLatin.specified=false");
    }

    @Test
    @Transactional
    void getAllInfraRegnesByNomLatinContainsSomething() throws Exception {
        // Initialize the database
        infraRegneRepository.saveAndFlush(infraRegne);

        // Get all the infraRegneList where nomLatin contains DEFAULT_NOM_LATIN
        defaultInfraRegneShouldBeFound("nomLatin.contains=" + DEFAULT_NOM_LATIN);

        // Get all the infraRegneList where nomLatin contains UPDATED_NOM_LATIN
        defaultInfraRegneShouldNotBeFound("nomLatin.contains=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllInfraRegnesByNomLatinNotContainsSomething() throws Exception {
        // Initialize the database
        infraRegneRepository.saveAndFlush(infraRegne);

        // Get all the infraRegneList where nomLatin does not contain DEFAULT_NOM_LATIN
        defaultInfraRegneShouldNotBeFound("nomLatin.doesNotContain=" + DEFAULT_NOM_LATIN);

        // Get all the infraRegneList where nomLatin does not contain UPDATED_NOM_LATIN
        defaultInfraRegneShouldBeFound("nomLatin.doesNotContain=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllInfraRegnesBySuperDivisionsIsEqualToSomething() throws Exception {
        // Initialize the database
        infraRegneRepository.saveAndFlush(infraRegne);
        SuperDivision superDivisions;
        if (TestUtil.findAll(em, SuperDivision.class).isEmpty()) {
            superDivisions = SuperDivisionResourceIT.createEntity(em);
            em.persist(superDivisions);
            em.flush();
        } else {
            superDivisions = TestUtil.findAll(em, SuperDivision.class).get(0);
        }
        em.persist(superDivisions);
        em.flush();
        infraRegne.addSuperDivisions(superDivisions);
        infraRegneRepository.saveAndFlush(infraRegne);
        Long superDivisionsId = superDivisions.getId();

        // Get all the infraRegneList where superDivisions equals to superDivisionsId
        defaultInfraRegneShouldBeFound("superDivisionsId.equals=" + superDivisionsId);

        // Get all the infraRegneList where superDivisions equals to (superDivisionsId + 1)
        defaultInfraRegneShouldNotBeFound("superDivisionsId.equals=" + (superDivisionsId + 1));
    }

    @Test
    @Transactional
    void getAllInfraRegnesBySynonymesIsEqualToSomething() throws Exception {
        // Initialize the database
        infraRegneRepository.saveAndFlush(infraRegne);
        InfraRegne synonymes;
        if (TestUtil.findAll(em, InfraRegne.class).isEmpty()) {
            synonymes = InfraRegneResourceIT.createEntity(em);
            em.persist(synonymes);
            em.flush();
        } else {
            synonymes = TestUtil.findAll(em, InfraRegne.class).get(0);
        }
        em.persist(synonymes);
        em.flush();
        infraRegne.addSynonymes(synonymes);
        infraRegneRepository.saveAndFlush(infraRegne);
        Long synonymesId = synonymes.getId();

        // Get all the infraRegneList where synonymes equals to synonymesId
        defaultInfraRegneShouldBeFound("synonymesId.equals=" + synonymesId);

        // Get all the infraRegneList where synonymes equals to (synonymesId + 1)
        defaultInfraRegneShouldNotBeFound("synonymesId.equals=" + (synonymesId + 1));
    }

    @Test
    @Transactional
    void getAllInfraRegnesByRameauIsEqualToSomething() throws Exception {
        // Initialize the database
        infraRegneRepository.saveAndFlush(infraRegne);
        Rameau rameau;
        if (TestUtil.findAll(em, Rameau.class).isEmpty()) {
            rameau = RameauResourceIT.createEntity(em);
            em.persist(rameau);
            em.flush();
        } else {
            rameau = TestUtil.findAll(em, Rameau.class).get(0);
        }
        em.persist(rameau);
        em.flush();
        infraRegne.setRameau(rameau);
        infraRegneRepository.saveAndFlush(infraRegne);
        Long rameauId = rameau.getId();

        // Get all the infraRegneList where rameau equals to rameauId
        defaultInfraRegneShouldBeFound("rameauId.equals=" + rameauId);

        // Get all the infraRegneList where rameau equals to (rameauId + 1)
        defaultInfraRegneShouldNotBeFound("rameauId.equals=" + (rameauId + 1));
    }

    @Test
    @Transactional
    void getAllInfraRegnesByInfraRegneIsEqualToSomething() throws Exception {
        // Initialize the database
        infraRegneRepository.saveAndFlush(infraRegne);
        InfraRegne infraRegne;
        if (TestUtil.findAll(em, InfraRegne.class).isEmpty()) {
            infraRegne = InfraRegneResourceIT.createEntity(em);
            em.persist(infraRegne);
            em.flush();
        } else {
            infraRegne = TestUtil.findAll(em, InfraRegne.class).get(0);
        }
        em.persist(infraRegne);
        em.flush();
        infraRegne.setInfraRegne(infraRegne);
        infraRegneRepository.saveAndFlush(infraRegne);
        Long infraRegneId = infraRegne.getId();

        // Get all the infraRegneList where infraRegne equals to infraRegneId
        defaultInfraRegneShouldBeFound("infraRegneId.equals=" + infraRegneId);

        // Get all the infraRegneList where infraRegne equals to (infraRegneId + 1)
        defaultInfraRegneShouldNotBeFound("infraRegneId.equals=" + (infraRegneId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInfraRegneShouldBeFound(String filter) throws Exception {
        restInfraRegneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(infraRegne.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));

        // Check, that the count call also returns 1
        restInfraRegneMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInfraRegneShouldNotBeFound(String filter) throws Exception {
        restInfraRegneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInfraRegneMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInfraRegne() throws Exception {
        // Get the infraRegne
        restInfraRegneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInfraRegne() throws Exception {
        // Initialize the database
        infraRegneRepository.saveAndFlush(infraRegne);

        int databaseSizeBeforeUpdate = infraRegneRepository.findAll().size();

        // Update the infraRegne
        InfraRegne updatedInfraRegne = infraRegneRepository.findById(infraRegne.getId()).get();
        // Disconnect from session so that the updates on updatedInfraRegne are not directly saved in db
        em.detach(updatedInfraRegne);
        updatedInfraRegne.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restInfraRegneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInfraRegne.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedInfraRegne))
            )
            .andExpect(status().isOk());

        // Validate the InfraRegne in the database
        List<InfraRegne> infraRegneList = infraRegneRepository.findAll();
        assertThat(infraRegneList).hasSize(databaseSizeBeforeUpdate);
        InfraRegne testInfraRegne = infraRegneList.get(infraRegneList.size() - 1);
        assertThat(testInfraRegne.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testInfraRegne.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void putNonExistingInfraRegne() throws Exception {
        int databaseSizeBeforeUpdate = infraRegneRepository.findAll().size();
        infraRegne.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInfraRegneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, infraRegne.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(infraRegne))
            )
            .andExpect(status().isBadRequest());

        // Validate the InfraRegne in the database
        List<InfraRegne> infraRegneList = infraRegneRepository.findAll();
        assertThat(infraRegneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInfraRegne() throws Exception {
        int databaseSizeBeforeUpdate = infraRegneRepository.findAll().size();
        infraRegne.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInfraRegneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(infraRegne))
            )
            .andExpect(status().isBadRequest());

        // Validate the InfraRegne in the database
        List<InfraRegne> infraRegneList = infraRegneRepository.findAll();
        assertThat(infraRegneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInfraRegne() throws Exception {
        int databaseSizeBeforeUpdate = infraRegneRepository.findAll().size();
        infraRegne.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInfraRegneMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(infraRegne)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InfraRegne in the database
        List<InfraRegne> infraRegneList = infraRegneRepository.findAll();
        assertThat(infraRegneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInfraRegneWithPatch() throws Exception {
        // Initialize the database
        infraRegneRepository.saveAndFlush(infraRegne);

        int databaseSizeBeforeUpdate = infraRegneRepository.findAll().size();

        // Update the infraRegne using partial update
        InfraRegne partialUpdatedInfraRegne = new InfraRegne();
        partialUpdatedInfraRegne.setId(infraRegne.getId());

        partialUpdatedInfraRegne.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restInfraRegneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInfraRegne.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInfraRegne))
            )
            .andExpect(status().isOk());

        // Validate the InfraRegne in the database
        List<InfraRegne> infraRegneList = infraRegneRepository.findAll();
        assertThat(infraRegneList).hasSize(databaseSizeBeforeUpdate);
        InfraRegne testInfraRegne = infraRegneList.get(infraRegneList.size() - 1);
        assertThat(testInfraRegne.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testInfraRegne.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void fullUpdateInfraRegneWithPatch() throws Exception {
        // Initialize the database
        infraRegneRepository.saveAndFlush(infraRegne);

        int databaseSizeBeforeUpdate = infraRegneRepository.findAll().size();

        // Update the infraRegne using partial update
        InfraRegne partialUpdatedInfraRegne = new InfraRegne();
        partialUpdatedInfraRegne.setId(infraRegne.getId());

        partialUpdatedInfraRegne.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restInfraRegneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInfraRegne.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInfraRegne))
            )
            .andExpect(status().isOk());

        // Validate the InfraRegne in the database
        List<InfraRegne> infraRegneList = infraRegneRepository.findAll();
        assertThat(infraRegneList).hasSize(databaseSizeBeforeUpdate);
        InfraRegne testInfraRegne = infraRegneList.get(infraRegneList.size() - 1);
        assertThat(testInfraRegne.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testInfraRegne.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void patchNonExistingInfraRegne() throws Exception {
        int databaseSizeBeforeUpdate = infraRegneRepository.findAll().size();
        infraRegne.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInfraRegneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, infraRegne.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(infraRegne))
            )
            .andExpect(status().isBadRequest());

        // Validate the InfraRegne in the database
        List<InfraRegne> infraRegneList = infraRegneRepository.findAll();
        assertThat(infraRegneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInfraRegne() throws Exception {
        int databaseSizeBeforeUpdate = infraRegneRepository.findAll().size();
        infraRegne.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInfraRegneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(infraRegne))
            )
            .andExpect(status().isBadRequest());

        // Validate the InfraRegne in the database
        List<InfraRegne> infraRegneList = infraRegneRepository.findAll();
        assertThat(infraRegneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInfraRegne() throws Exception {
        int databaseSizeBeforeUpdate = infraRegneRepository.findAll().size();
        infraRegne.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInfraRegneMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(infraRegne))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InfraRegne in the database
        List<InfraRegne> infraRegneList = infraRegneRepository.findAll();
        assertThat(infraRegneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInfraRegne() throws Exception {
        // Initialize the database
        infraRegneRepository.saveAndFlush(infraRegne);

        int databaseSizeBeforeDelete = infraRegneRepository.findAll().size();

        // Delete the infraRegne
        restInfraRegneMockMvc
            .perform(delete(ENTITY_API_URL_ID, infraRegne.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InfraRegne> infraRegneList = infraRegneRepository.findAll();
        assertThat(infraRegneList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
