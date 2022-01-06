package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.ClassificationNom;
import fr.syncrase.ecosyst.domain.CronquistRank;
import fr.syncrase.ecosyst.repository.ClassificationNomRepository;
import fr.syncrase.ecosyst.service.criteria.ClassificationNomCriteria;
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
 * Integration tests for the {@link ClassificationNomResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClassificationNomResourceIT {

    private static final String DEFAULT_NOM_FR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_LATIN = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LATIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/classification-noms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClassificationNomRepository classificationNomRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClassificationNomMockMvc;

    private ClassificationNom classificationNom;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassificationNom createEntity(EntityManager em) {
        ClassificationNom classificationNom = new ClassificationNom().nomFr(DEFAULT_NOM_FR).nomLatin(DEFAULT_NOM_LATIN);
        // Add required entity
        CronquistRank cronquistRank;
        if (TestUtil.findAll(em, CronquistRank.class).isEmpty()) {
            cronquistRank = CronquistRankResourceIT.createEntity(em);
            em.persist(cronquistRank);
            em.flush();
        } else {
            cronquistRank = TestUtil.findAll(em, CronquistRank.class).get(0);
        }
        classificationNom.setCronquistRank(cronquistRank);
        return classificationNom;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassificationNom createUpdatedEntity(EntityManager em) {
        ClassificationNom classificationNom = new ClassificationNom().nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);
        // Add required entity
        CronquistRank cronquistRank;
        if (TestUtil.findAll(em, CronquistRank.class).isEmpty()) {
            cronquistRank = CronquistRankResourceIT.createUpdatedEntity(em);
            em.persist(cronquistRank);
            em.flush();
        } else {
            cronquistRank = TestUtil.findAll(em, CronquistRank.class).get(0);
        }
        classificationNom.setCronquistRank(cronquistRank);
        return classificationNom;
    }

    @BeforeEach
    public void initTest() {
        classificationNom = createEntity(em);
    }

    @Test
    @Transactional
    void createClassificationNom() throws Exception {
        int databaseSizeBeforeCreate = classificationNomRepository.findAll().size();
        // Create the ClassificationNom
        restClassificationNomMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classificationNom))
            )
            .andExpect(status().isCreated());

        // Validate the ClassificationNom in the database
        List<ClassificationNom> classificationNomList = classificationNomRepository.findAll();
        assertThat(classificationNomList).hasSize(databaseSizeBeforeCreate + 1);
        ClassificationNom testClassificationNom = classificationNomList.get(classificationNomList.size() - 1);
        assertThat(testClassificationNom.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testClassificationNom.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void createClassificationNomWithExistingId() throws Exception {
        // Create the ClassificationNom with an existing ID
        classificationNom.setId(1L);

        int databaseSizeBeforeCreate = classificationNomRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClassificationNomMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classificationNom))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassificationNom in the database
        List<ClassificationNom> classificationNomList = classificationNomRepository.findAll();
        assertThat(classificationNomList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllClassificationNoms() throws Exception {
        // Initialize the database
        classificationNomRepository.saveAndFlush(classificationNom);

        // Get all the classificationNomList
        restClassificationNomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classificationNom.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));
    }

    @Test
    @Transactional
    void getClassificationNom() throws Exception {
        // Initialize the database
        classificationNomRepository.saveAndFlush(classificationNom);

        // Get the classificationNom
        restClassificationNomMockMvc
            .perform(get(ENTITY_API_URL_ID, classificationNom.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(classificationNom.getId().intValue()))
            .andExpect(jsonPath("$.nomFr").value(DEFAULT_NOM_FR))
            .andExpect(jsonPath("$.nomLatin").value(DEFAULT_NOM_LATIN));
    }

    @Test
    @Transactional
    void getClassificationNomsByIdFiltering() throws Exception {
        // Initialize the database
        classificationNomRepository.saveAndFlush(classificationNom);

        Long id = classificationNom.getId();

        defaultClassificationNomShouldBeFound("id.equals=" + id);
        defaultClassificationNomShouldNotBeFound("id.notEquals=" + id);

        defaultClassificationNomShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultClassificationNomShouldNotBeFound("id.greaterThan=" + id);

        defaultClassificationNomShouldBeFound("id.lessThanOrEqual=" + id);
        defaultClassificationNomShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllClassificationNomsByNomFrIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationNomRepository.saveAndFlush(classificationNom);

        // Get all the classificationNomList where nomFr equals to DEFAULT_NOM_FR
        defaultClassificationNomShouldBeFound("nomFr.equals=" + DEFAULT_NOM_FR);

        // Get all the classificationNomList where nomFr equals to UPDATED_NOM_FR
        defaultClassificationNomShouldNotBeFound("nomFr.equals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllClassificationNomsByNomFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classificationNomRepository.saveAndFlush(classificationNom);

        // Get all the classificationNomList where nomFr not equals to DEFAULT_NOM_FR
        defaultClassificationNomShouldNotBeFound("nomFr.notEquals=" + DEFAULT_NOM_FR);

        // Get all the classificationNomList where nomFr not equals to UPDATED_NOM_FR
        defaultClassificationNomShouldBeFound("nomFr.notEquals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllClassificationNomsByNomFrIsInShouldWork() throws Exception {
        // Initialize the database
        classificationNomRepository.saveAndFlush(classificationNom);

        // Get all the classificationNomList where nomFr in DEFAULT_NOM_FR or UPDATED_NOM_FR
        defaultClassificationNomShouldBeFound("nomFr.in=" + DEFAULT_NOM_FR + "," + UPDATED_NOM_FR);

        // Get all the classificationNomList where nomFr equals to UPDATED_NOM_FR
        defaultClassificationNomShouldNotBeFound("nomFr.in=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllClassificationNomsByNomFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        classificationNomRepository.saveAndFlush(classificationNom);

        // Get all the classificationNomList where nomFr is not null
        defaultClassificationNomShouldBeFound("nomFr.specified=true");

        // Get all the classificationNomList where nomFr is null
        defaultClassificationNomShouldNotBeFound("nomFr.specified=false");
    }

    @Test
    @Transactional
    void getAllClassificationNomsByNomFrContainsSomething() throws Exception {
        // Initialize the database
        classificationNomRepository.saveAndFlush(classificationNom);

        // Get all the classificationNomList where nomFr contains DEFAULT_NOM_FR
        defaultClassificationNomShouldBeFound("nomFr.contains=" + DEFAULT_NOM_FR);

        // Get all the classificationNomList where nomFr contains UPDATED_NOM_FR
        defaultClassificationNomShouldNotBeFound("nomFr.contains=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllClassificationNomsByNomFrNotContainsSomething() throws Exception {
        // Initialize the database
        classificationNomRepository.saveAndFlush(classificationNom);

        // Get all the classificationNomList where nomFr does not contain DEFAULT_NOM_FR
        defaultClassificationNomShouldNotBeFound("nomFr.doesNotContain=" + DEFAULT_NOM_FR);

        // Get all the classificationNomList where nomFr does not contain UPDATED_NOM_FR
        defaultClassificationNomShouldBeFound("nomFr.doesNotContain=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllClassificationNomsByNomLatinIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationNomRepository.saveAndFlush(classificationNom);

        // Get all the classificationNomList where nomLatin equals to DEFAULT_NOM_LATIN
        defaultClassificationNomShouldBeFound("nomLatin.equals=" + DEFAULT_NOM_LATIN);

        // Get all the classificationNomList where nomLatin equals to UPDATED_NOM_LATIN
        defaultClassificationNomShouldNotBeFound("nomLatin.equals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllClassificationNomsByNomLatinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classificationNomRepository.saveAndFlush(classificationNom);

        // Get all the classificationNomList where nomLatin not equals to DEFAULT_NOM_LATIN
        defaultClassificationNomShouldNotBeFound("nomLatin.notEquals=" + DEFAULT_NOM_LATIN);

        // Get all the classificationNomList where nomLatin not equals to UPDATED_NOM_LATIN
        defaultClassificationNomShouldBeFound("nomLatin.notEquals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllClassificationNomsByNomLatinIsInShouldWork() throws Exception {
        // Initialize the database
        classificationNomRepository.saveAndFlush(classificationNom);

        // Get all the classificationNomList where nomLatin in DEFAULT_NOM_LATIN or UPDATED_NOM_LATIN
        defaultClassificationNomShouldBeFound("nomLatin.in=" + DEFAULT_NOM_LATIN + "," + UPDATED_NOM_LATIN);

        // Get all the classificationNomList where nomLatin equals to UPDATED_NOM_LATIN
        defaultClassificationNomShouldNotBeFound("nomLatin.in=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllClassificationNomsByNomLatinIsNullOrNotNull() throws Exception {
        // Initialize the database
        classificationNomRepository.saveAndFlush(classificationNom);

        // Get all the classificationNomList where nomLatin is not null
        defaultClassificationNomShouldBeFound("nomLatin.specified=true");

        // Get all the classificationNomList where nomLatin is null
        defaultClassificationNomShouldNotBeFound("nomLatin.specified=false");
    }

    @Test
    @Transactional
    void getAllClassificationNomsByNomLatinContainsSomething() throws Exception {
        // Initialize the database
        classificationNomRepository.saveAndFlush(classificationNom);

        // Get all the classificationNomList where nomLatin contains DEFAULT_NOM_LATIN
        defaultClassificationNomShouldBeFound("nomLatin.contains=" + DEFAULT_NOM_LATIN);

        // Get all the classificationNomList where nomLatin contains UPDATED_NOM_LATIN
        defaultClassificationNomShouldNotBeFound("nomLatin.contains=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllClassificationNomsByNomLatinNotContainsSomething() throws Exception {
        // Initialize the database
        classificationNomRepository.saveAndFlush(classificationNom);

        // Get all the classificationNomList where nomLatin does not contain DEFAULT_NOM_LATIN
        defaultClassificationNomShouldNotBeFound("nomLatin.doesNotContain=" + DEFAULT_NOM_LATIN);

        // Get all the classificationNomList where nomLatin does not contain UPDATED_NOM_LATIN
        defaultClassificationNomShouldBeFound("nomLatin.doesNotContain=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllClassificationNomsByCronquistRankIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationNomRepository.saveAndFlush(classificationNom);
        CronquistRank cronquistRank;
        if (TestUtil.findAll(em, CronquistRank.class).isEmpty()) {
            cronquistRank = CronquistRankResourceIT.createEntity(em);
            em.persist(cronquistRank);
            em.flush();
        } else {
            cronquistRank = TestUtil.findAll(em, CronquistRank.class).get(0);
        }
        em.persist(cronquistRank);
        em.flush();
        classificationNom.setCronquistRank(cronquistRank);
        classificationNomRepository.saveAndFlush(classificationNom);
        Long cronquistRankId = cronquistRank.getId();

        // Get all the classificationNomList where cronquistRank equals to cronquistRankId
        defaultClassificationNomShouldBeFound("cronquistRankId.equals=" + cronquistRankId);

        // Get all the classificationNomList where cronquistRank equals to (cronquistRankId + 1)
        defaultClassificationNomShouldNotBeFound("cronquistRankId.equals=" + (cronquistRankId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClassificationNomShouldBeFound(String filter) throws Exception {
        restClassificationNomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classificationNom.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));

        // Check, that the count call also returns 1
        restClassificationNomMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClassificationNomShouldNotBeFound(String filter) throws Exception {
        restClassificationNomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClassificationNomMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingClassificationNom() throws Exception {
        // Get the classificationNom
        restClassificationNomMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewClassificationNom() throws Exception {
        // Initialize the database
        classificationNomRepository.saveAndFlush(classificationNom);

        int databaseSizeBeforeUpdate = classificationNomRepository.findAll().size();

        // Update the classificationNom
        ClassificationNom updatedClassificationNom = classificationNomRepository.findById(classificationNom.getId()).get();
        // Disconnect from session so that the updates on updatedClassificationNom are not directly saved in db
        em.detach(updatedClassificationNom);
        updatedClassificationNom.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restClassificationNomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedClassificationNom.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedClassificationNom))
            )
            .andExpect(status().isOk());

        // Validate the ClassificationNom in the database
        List<ClassificationNom> classificationNomList = classificationNomRepository.findAll();
        assertThat(classificationNomList).hasSize(databaseSizeBeforeUpdate);
        ClassificationNom testClassificationNom = classificationNomList.get(classificationNomList.size() - 1);
        assertThat(testClassificationNom.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testClassificationNom.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void putNonExistingClassificationNom() throws Exception {
        int databaseSizeBeforeUpdate = classificationNomRepository.findAll().size();
        classificationNom.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassificationNomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, classificationNom.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classificationNom))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassificationNom in the database
        List<ClassificationNom> classificationNomList = classificationNomRepository.findAll();
        assertThat(classificationNomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClassificationNom() throws Exception {
        int databaseSizeBeforeUpdate = classificationNomRepository.findAll().size();
        classificationNom.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassificationNomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classificationNom))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassificationNom in the database
        List<ClassificationNom> classificationNomList = classificationNomRepository.findAll();
        assertThat(classificationNomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClassificationNom() throws Exception {
        int databaseSizeBeforeUpdate = classificationNomRepository.findAll().size();
        classificationNom.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassificationNomMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classificationNom))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClassificationNom in the database
        List<ClassificationNom> classificationNomList = classificationNomRepository.findAll();
        assertThat(classificationNomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClassificationNomWithPatch() throws Exception {
        // Initialize the database
        classificationNomRepository.saveAndFlush(classificationNom);

        int databaseSizeBeforeUpdate = classificationNomRepository.findAll().size();

        // Update the classificationNom using partial update
        ClassificationNom partialUpdatedClassificationNom = new ClassificationNom();
        partialUpdatedClassificationNom.setId(classificationNom.getId());

        partialUpdatedClassificationNom.nomLatin(UPDATED_NOM_LATIN);

        restClassificationNomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClassificationNom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClassificationNom))
            )
            .andExpect(status().isOk());

        // Validate the ClassificationNom in the database
        List<ClassificationNom> classificationNomList = classificationNomRepository.findAll();
        assertThat(classificationNomList).hasSize(databaseSizeBeforeUpdate);
        ClassificationNom testClassificationNom = classificationNomList.get(classificationNomList.size() - 1);
        assertThat(testClassificationNom.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testClassificationNom.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void fullUpdateClassificationNomWithPatch() throws Exception {
        // Initialize the database
        classificationNomRepository.saveAndFlush(classificationNom);

        int databaseSizeBeforeUpdate = classificationNomRepository.findAll().size();

        // Update the classificationNom using partial update
        ClassificationNom partialUpdatedClassificationNom = new ClassificationNom();
        partialUpdatedClassificationNom.setId(classificationNom.getId());

        partialUpdatedClassificationNom.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restClassificationNomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClassificationNom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClassificationNom))
            )
            .andExpect(status().isOk());

        // Validate the ClassificationNom in the database
        List<ClassificationNom> classificationNomList = classificationNomRepository.findAll();
        assertThat(classificationNomList).hasSize(databaseSizeBeforeUpdate);
        ClassificationNom testClassificationNom = classificationNomList.get(classificationNomList.size() - 1);
        assertThat(testClassificationNom.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testClassificationNom.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void patchNonExistingClassificationNom() throws Exception {
        int databaseSizeBeforeUpdate = classificationNomRepository.findAll().size();
        classificationNom.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassificationNomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, classificationNom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(classificationNom))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassificationNom in the database
        List<ClassificationNom> classificationNomList = classificationNomRepository.findAll();
        assertThat(classificationNomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClassificationNom() throws Exception {
        int databaseSizeBeforeUpdate = classificationNomRepository.findAll().size();
        classificationNom.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassificationNomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(classificationNom))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClassificationNom in the database
        List<ClassificationNom> classificationNomList = classificationNomRepository.findAll();
        assertThat(classificationNomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClassificationNom() throws Exception {
        int databaseSizeBeforeUpdate = classificationNomRepository.findAll().size();
        classificationNom.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassificationNomMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(classificationNom))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClassificationNom in the database
        List<ClassificationNom> classificationNomList = classificationNomRepository.findAll();
        assertThat(classificationNomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClassificationNom() throws Exception {
        // Initialize the database
        classificationNomRepository.saveAndFlush(classificationNom);

        int databaseSizeBeforeDelete = classificationNomRepository.findAll().size();

        // Delete the classificationNom
        restClassificationNomMockMvc
            .perform(delete(ENTITY_API_URL_ID, classificationNom.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ClassificationNom> classificationNomList = classificationNomRepository.findAll();
        assertThat(classificationNomList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
