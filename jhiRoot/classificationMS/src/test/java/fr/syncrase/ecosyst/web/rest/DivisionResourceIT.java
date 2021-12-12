package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.Division;
import fr.syncrase.ecosyst.domain.Division;
import fr.syncrase.ecosyst.domain.SousDivision;
import fr.syncrase.ecosyst.domain.SuperDivision;
import fr.syncrase.ecosyst.repository.DivisionRepository;
import fr.syncrase.ecosyst.service.criteria.DivisionCriteria;
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
 * Integration tests for the {@link DivisionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DivisionResourceIT {

    private static final String DEFAULT_NOM_FR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_LATIN = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LATIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/divisions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DivisionRepository divisionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDivisionMockMvc;

    private Division division;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Division createEntity(EntityManager em) {
        Division division = new Division().nomFr(DEFAULT_NOM_FR).nomLatin(DEFAULT_NOM_LATIN);
        return division;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Division createUpdatedEntity(EntityManager em) {
        Division division = new Division().nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);
        return division;
    }

    @BeforeEach
    public void initTest() {
        division = createEntity(em);
    }

    @Test
    @Transactional
    void createDivision() throws Exception {
        int databaseSizeBeforeCreate = divisionRepository.findAll().size();
        // Create the Division
        restDivisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(division)))
            .andExpect(status().isCreated());

        // Validate the Division in the database
        List<Division> divisionList = divisionRepository.findAll();
        assertThat(divisionList).hasSize(databaseSizeBeforeCreate + 1);
        Division testDivision = divisionList.get(divisionList.size() - 1);
        assertThat(testDivision.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testDivision.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void createDivisionWithExistingId() throws Exception {
        // Create the Division with an existing ID
        division.setId(1L);

        int databaseSizeBeforeCreate = divisionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDivisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(division)))
            .andExpect(status().isBadRequest());

        // Validate the Division in the database
        List<Division> divisionList = divisionRepository.findAll();
        assertThat(divisionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomFrIsRequired() throws Exception {
        int databaseSizeBeforeTest = divisionRepository.findAll().size();
        // set the field null
        division.setNomFr(null);

        // Create the Division, which fails.

        restDivisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(division)))
            .andExpect(status().isBadRequest());

        List<Division> divisionList = divisionRepository.findAll();
        assertThat(divisionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDivisions() throws Exception {
        // Initialize the database
        divisionRepository.saveAndFlush(division);

        // Get all the divisionList
        restDivisionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(division.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));
    }

    @Test
    @Transactional
    void getDivision() throws Exception {
        // Initialize the database
        divisionRepository.saveAndFlush(division);

        // Get the division
        restDivisionMockMvc
            .perform(get(ENTITY_API_URL_ID, division.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(division.getId().intValue()))
            .andExpect(jsonPath("$.nomFr").value(DEFAULT_NOM_FR))
            .andExpect(jsonPath("$.nomLatin").value(DEFAULT_NOM_LATIN));
    }

    @Test
    @Transactional
    void getDivisionsByIdFiltering() throws Exception {
        // Initialize the database
        divisionRepository.saveAndFlush(division);

        Long id = division.getId();

        defaultDivisionShouldBeFound("id.equals=" + id);
        defaultDivisionShouldNotBeFound("id.notEquals=" + id);

        defaultDivisionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDivisionShouldNotBeFound("id.greaterThan=" + id);

        defaultDivisionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDivisionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDivisionsByNomFrIsEqualToSomething() throws Exception {
        // Initialize the database
        divisionRepository.saveAndFlush(division);

        // Get all the divisionList where nomFr equals to DEFAULT_NOM_FR
        defaultDivisionShouldBeFound("nomFr.equals=" + DEFAULT_NOM_FR);

        // Get all the divisionList where nomFr equals to UPDATED_NOM_FR
        defaultDivisionShouldNotBeFound("nomFr.equals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllDivisionsByNomFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        divisionRepository.saveAndFlush(division);

        // Get all the divisionList where nomFr not equals to DEFAULT_NOM_FR
        defaultDivisionShouldNotBeFound("nomFr.notEquals=" + DEFAULT_NOM_FR);

        // Get all the divisionList where nomFr not equals to UPDATED_NOM_FR
        defaultDivisionShouldBeFound("nomFr.notEquals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllDivisionsByNomFrIsInShouldWork() throws Exception {
        // Initialize the database
        divisionRepository.saveAndFlush(division);

        // Get all the divisionList where nomFr in DEFAULT_NOM_FR or UPDATED_NOM_FR
        defaultDivisionShouldBeFound("nomFr.in=" + DEFAULT_NOM_FR + "," + UPDATED_NOM_FR);

        // Get all the divisionList where nomFr equals to UPDATED_NOM_FR
        defaultDivisionShouldNotBeFound("nomFr.in=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllDivisionsByNomFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        divisionRepository.saveAndFlush(division);

        // Get all the divisionList where nomFr is not null
        defaultDivisionShouldBeFound("nomFr.specified=true");

        // Get all the divisionList where nomFr is null
        defaultDivisionShouldNotBeFound("nomFr.specified=false");
    }

    @Test
    @Transactional
    void getAllDivisionsByNomFrContainsSomething() throws Exception {
        // Initialize the database
        divisionRepository.saveAndFlush(division);

        // Get all the divisionList where nomFr contains DEFAULT_NOM_FR
        defaultDivisionShouldBeFound("nomFr.contains=" + DEFAULT_NOM_FR);

        // Get all the divisionList where nomFr contains UPDATED_NOM_FR
        defaultDivisionShouldNotBeFound("nomFr.contains=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllDivisionsByNomFrNotContainsSomething() throws Exception {
        // Initialize the database
        divisionRepository.saveAndFlush(division);

        // Get all the divisionList where nomFr does not contain DEFAULT_NOM_FR
        defaultDivisionShouldNotBeFound("nomFr.doesNotContain=" + DEFAULT_NOM_FR);

        // Get all the divisionList where nomFr does not contain UPDATED_NOM_FR
        defaultDivisionShouldBeFound("nomFr.doesNotContain=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllDivisionsByNomLatinIsEqualToSomething() throws Exception {
        // Initialize the database
        divisionRepository.saveAndFlush(division);

        // Get all the divisionList where nomLatin equals to DEFAULT_NOM_LATIN
        defaultDivisionShouldBeFound("nomLatin.equals=" + DEFAULT_NOM_LATIN);

        // Get all the divisionList where nomLatin equals to UPDATED_NOM_LATIN
        defaultDivisionShouldNotBeFound("nomLatin.equals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllDivisionsByNomLatinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        divisionRepository.saveAndFlush(division);

        // Get all the divisionList where nomLatin not equals to DEFAULT_NOM_LATIN
        defaultDivisionShouldNotBeFound("nomLatin.notEquals=" + DEFAULT_NOM_LATIN);

        // Get all the divisionList where nomLatin not equals to UPDATED_NOM_LATIN
        defaultDivisionShouldBeFound("nomLatin.notEquals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllDivisionsByNomLatinIsInShouldWork() throws Exception {
        // Initialize the database
        divisionRepository.saveAndFlush(division);

        // Get all the divisionList where nomLatin in DEFAULT_NOM_LATIN or UPDATED_NOM_LATIN
        defaultDivisionShouldBeFound("nomLatin.in=" + DEFAULT_NOM_LATIN + "," + UPDATED_NOM_LATIN);

        // Get all the divisionList where nomLatin equals to UPDATED_NOM_LATIN
        defaultDivisionShouldNotBeFound("nomLatin.in=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllDivisionsByNomLatinIsNullOrNotNull() throws Exception {
        // Initialize the database
        divisionRepository.saveAndFlush(division);

        // Get all the divisionList where nomLatin is not null
        defaultDivisionShouldBeFound("nomLatin.specified=true");

        // Get all the divisionList where nomLatin is null
        defaultDivisionShouldNotBeFound("nomLatin.specified=false");
    }

    @Test
    @Transactional
    void getAllDivisionsByNomLatinContainsSomething() throws Exception {
        // Initialize the database
        divisionRepository.saveAndFlush(division);

        // Get all the divisionList where nomLatin contains DEFAULT_NOM_LATIN
        defaultDivisionShouldBeFound("nomLatin.contains=" + DEFAULT_NOM_LATIN);

        // Get all the divisionList where nomLatin contains UPDATED_NOM_LATIN
        defaultDivisionShouldNotBeFound("nomLatin.contains=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllDivisionsByNomLatinNotContainsSomething() throws Exception {
        // Initialize the database
        divisionRepository.saveAndFlush(division);

        // Get all the divisionList where nomLatin does not contain DEFAULT_NOM_LATIN
        defaultDivisionShouldNotBeFound("nomLatin.doesNotContain=" + DEFAULT_NOM_LATIN);

        // Get all the divisionList where nomLatin does not contain UPDATED_NOM_LATIN
        defaultDivisionShouldBeFound("nomLatin.doesNotContain=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllDivisionsBySousDivisionsIsEqualToSomething() throws Exception {
        // Initialize the database
        divisionRepository.saveAndFlush(division);
        SousDivision sousDivisions;
        if (TestUtil.findAll(em, SousDivision.class).isEmpty()) {
            sousDivisions = SousDivisionResourceIT.createEntity(em);
            em.persist(sousDivisions);
            em.flush();
        } else {
            sousDivisions = TestUtil.findAll(em, SousDivision.class).get(0);
        }
        em.persist(sousDivisions);
        em.flush();
        division.addSousDivisions(sousDivisions);
        divisionRepository.saveAndFlush(division);
        Long sousDivisionsId = sousDivisions.getId();

        // Get all the divisionList where sousDivisions equals to sousDivisionsId
        defaultDivisionShouldBeFound("sousDivisionsId.equals=" + sousDivisionsId);

        // Get all the divisionList where sousDivisions equals to (sousDivisionsId + 1)
        defaultDivisionShouldNotBeFound("sousDivisionsId.equals=" + (sousDivisionsId + 1));
    }

    @Test
    @Transactional
    void getAllDivisionsBySynonymesIsEqualToSomething() throws Exception {
        // Initialize the database
        divisionRepository.saveAndFlush(division);
        Division synonymes;
        if (TestUtil.findAll(em, Division.class).isEmpty()) {
            synonymes = DivisionResourceIT.createEntity(em);
            em.persist(synonymes);
            em.flush();
        } else {
            synonymes = TestUtil.findAll(em, Division.class).get(0);
        }
        em.persist(synonymes);
        em.flush();
        division.addSynonymes(synonymes);
        divisionRepository.saveAndFlush(division);
        Long synonymesId = synonymes.getId();

        // Get all the divisionList where synonymes equals to synonymesId
        defaultDivisionShouldBeFound("synonymesId.equals=" + synonymesId);

        // Get all the divisionList where synonymes equals to (synonymesId + 1)
        defaultDivisionShouldNotBeFound("synonymesId.equals=" + (synonymesId + 1));
    }

    @Test
    @Transactional
    void getAllDivisionsBySuperDivisionIsEqualToSomething() throws Exception {
        // Initialize the database
        divisionRepository.saveAndFlush(division);
        SuperDivision superDivision;
        if (TestUtil.findAll(em, SuperDivision.class).isEmpty()) {
            superDivision = SuperDivisionResourceIT.createEntity(em);
            em.persist(superDivision);
            em.flush();
        } else {
            superDivision = TestUtil.findAll(em, SuperDivision.class).get(0);
        }
        em.persist(superDivision);
        em.flush();
        division.setSuperDivision(superDivision);
        divisionRepository.saveAndFlush(division);
        Long superDivisionId = superDivision.getId();

        // Get all the divisionList where superDivision equals to superDivisionId
        defaultDivisionShouldBeFound("superDivisionId.equals=" + superDivisionId);

        // Get all the divisionList where superDivision equals to (superDivisionId + 1)
        defaultDivisionShouldNotBeFound("superDivisionId.equals=" + (superDivisionId + 1));
    }

    @Test
    @Transactional
    void getAllDivisionsByDivisionIsEqualToSomething() throws Exception {
        // Initialize the database
        divisionRepository.saveAndFlush(division);
        Division division;
        if (TestUtil.findAll(em, Division.class).isEmpty()) {
            division = DivisionResourceIT.createEntity(em);
            em.persist(division);
            em.flush();
        } else {
            division = TestUtil.findAll(em, Division.class).get(0);
        }
        em.persist(division);
        em.flush();
        division.setDivision(division);
        divisionRepository.saveAndFlush(division);
        Long divisionId = division.getId();

        // Get all the divisionList where division equals to divisionId
        defaultDivisionShouldBeFound("divisionId.equals=" + divisionId);

        // Get all the divisionList where division equals to (divisionId + 1)
        defaultDivisionShouldNotBeFound("divisionId.equals=" + (divisionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDivisionShouldBeFound(String filter) throws Exception {
        restDivisionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(division.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));

        // Check, that the count call also returns 1
        restDivisionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDivisionShouldNotBeFound(String filter) throws Exception {
        restDivisionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDivisionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDivision() throws Exception {
        // Get the division
        restDivisionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDivision() throws Exception {
        // Initialize the database
        divisionRepository.saveAndFlush(division);

        int databaseSizeBeforeUpdate = divisionRepository.findAll().size();

        // Update the division
        Division updatedDivision = divisionRepository.findById(division.getId()).get();
        // Disconnect from session so that the updates on updatedDivision are not directly saved in db
        em.detach(updatedDivision);
        updatedDivision.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restDivisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDivision.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDivision))
            )
            .andExpect(status().isOk());

        // Validate the Division in the database
        List<Division> divisionList = divisionRepository.findAll();
        assertThat(divisionList).hasSize(databaseSizeBeforeUpdate);
        Division testDivision = divisionList.get(divisionList.size() - 1);
        assertThat(testDivision.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testDivision.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void putNonExistingDivision() throws Exception {
        int databaseSizeBeforeUpdate = divisionRepository.findAll().size();
        division.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDivisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, division.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(division))
            )
            .andExpect(status().isBadRequest());

        // Validate the Division in the database
        List<Division> divisionList = divisionRepository.findAll();
        assertThat(divisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDivision() throws Exception {
        int databaseSizeBeforeUpdate = divisionRepository.findAll().size();
        division.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDivisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(division))
            )
            .andExpect(status().isBadRequest());

        // Validate the Division in the database
        List<Division> divisionList = divisionRepository.findAll();
        assertThat(divisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDivision() throws Exception {
        int databaseSizeBeforeUpdate = divisionRepository.findAll().size();
        division.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDivisionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(division)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Division in the database
        List<Division> divisionList = divisionRepository.findAll();
        assertThat(divisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDivisionWithPatch() throws Exception {
        // Initialize the database
        divisionRepository.saveAndFlush(division);

        int databaseSizeBeforeUpdate = divisionRepository.findAll().size();

        // Update the division using partial update
        Division partialUpdatedDivision = new Division();
        partialUpdatedDivision.setId(division.getId());

        partialUpdatedDivision.nomLatin(UPDATED_NOM_LATIN);

        restDivisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDivision.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDivision))
            )
            .andExpect(status().isOk());

        // Validate the Division in the database
        List<Division> divisionList = divisionRepository.findAll();
        assertThat(divisionList).hasSize(databaseSizeBeforeUpdate);
        Division testDivision = divisionList.get(divisionList.size() - 1);
        assertThat(testDivision.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testDivision.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void fullUpdateDivisionWithPatch() throws Exception {
        // Initialize the database
        divisionRepository.saveAndFlush(division);

        int databaseSizeBeforeUpdate = divisionRepository.findAll().size();

        // Update the division using partial update
        Division partialUpdatedDivision = new Division();
        partialUpdatedDivision.setId(division.getId());

        partialUpdatedDivision.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restDivisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDivision.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDivision))
            )
            .andExpect(status().isOk());

        // Validate the Division in the database
        List<Division> divisionList = divisionRepository.findAll();
        assertThat(divisionList).hasSize(databaseSizeBeforeUpdate);
        Division testDivision = divisionList.get(divisionList.size() - 1);
        assertThat(testDivision.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testDivision.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void patchNonExistingDivision() throws Exception {
        int databaseSizeBeforeUpdate = divisionRepository.findAll().size();
        division.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDivisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, division.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(division))
            )
            .andExpect(status().isBadRequest());

        // Validate the Division in the database
        List<Division> divisionList = divisionRepository.findAll();
        assertThat(divisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDivision() throws Exception {
        int databaseSizeBeforeUpdate = divisionRepository.findAll().size();
        division.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDivisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(division))
            )
            .andExpect(status().isBadRequest());

        // Validate the Division in the database
        List<Division> divisionList = divisionRepository.findAll();
        assertThat(divisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDivision() throws Exception {
        int databaseSizeBeforeUpdate = divisionRepository.findAll().size();
        division.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDivisionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(division)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Division in the database
        List<Division> divisionList = divisionRepository.findAll();
        assertThat(divisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDivision() throws Exception {
        // Initialize the database
        divisionRepository.saveAndFlush(division);

        int databaseSizeBeforeDelete = divisionRepository.findAll().size();

        // Delete the division
        restDivisionMockMvc
            .perform(delete(ENTITY_API_URL_ID, division.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Division> divisionList = divisionRepository.findAll();
        assertThat(divisionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
