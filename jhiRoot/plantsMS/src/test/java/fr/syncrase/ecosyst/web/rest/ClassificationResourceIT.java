package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.APGI;
import fr.syncrase.ecosyst.domain.APGII;
import fr.syncrase.ecosyst.domain.APGIII;
import fr.syncrase.ecosyst.domain.APGIV;
import fr.syncrase.ecosyst.domain.Classification;
import fr.syncrase.ecosyst.domain.Cronquist;
import fr.syncrase.ecosyst.domain.Raunkier;
import fr.syncrase.ecosyst.repository.ClassificationRepository;
import fr.syncrase.ecosyst.service.criteria.ClassificationCriteria;
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
 * Integration tests for the {@link ClassificationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClassificationResourceIT {

    private static final String DEFAULT_NOM_LATIN = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LATIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/classifications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClassificationRepository classificationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClassificationMockMvc;

    private Classification classification;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Classification createEntity(EntityManager em) {
        Classification classification = new Classification().nomLatin(DEFAULT_NOM_LATIN);
        return classification;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Classification createUpdatedEntity(EntityManager em) {
        Classification classification = new Classification().nomLatin(UPDATED_NOM_LATIN);
        return classification;
    }

    @BeforeEach
    public void initTest() {
        classification = createEntity(em);
    }

    @Test
    @Transactional
    void createClassification() throws Exception {
        int databaseSizeBeforeCreate = classificationRepository.findAll().size();
        // Create the Classification
        restClassificationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classification))
            )
            .andExpect(status().isCreated());

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeCreate + 1);
        Classification testClassification = classificationList.get(classificationList.size() - 1);
        assertThat(testClassification.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void createClassificationWithExistingId() throws Exception {
        // Create the Classification with an existing ID
        classification.setId(1L);

        int databaseSizeBeforeCreate = classificationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClassificationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classification))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllClassifications() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classification);

        // Get all the classificationList
        restClassificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classification.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));
    }

    @Test
    @Transactional
    void getClassification() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classification);

        // Get the classification
        restClassificationMockMvc
            .perform(get(ENTITY_API_URL_ID, classification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(classification.getId().intValue()))
            .andExpect(jsonPath("$.nomLatin").value(DEFAULT_NOM_LATIN));
    }

    @Test
    @Transactional
    void getClassificationsByIdFiltering() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classification);

        Long id = classification.getId();

        defaultClassificationShouldBeFound("id.equals=" + id);
        defaultClassificationShouldNotBeFound("id.notEquals=" + id);

        defaultClassificationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultClassificationShouldNotBeFound("id.greaterThan=" + id);

        defaultClassificationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultClassificationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllClassificationsByNomLatinIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classification);

        // Get all the classificationList where nomLatin equals to DEFAULT_NOM_LATIN
        defaultClassificationShouldBeFound("nomLatin.equals=" + DEFAULT_NOM_LATIN);

        // Get all the classificationList where nomLatin equals to UPDATED_NOM_LATIN
        defaultClassificationShouldNotBeFound("nomLatin.equals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllClassificationsByNomLatinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classification);

        // Get all the classificationList where nomLatin not equals to DEFAULT_NOM_LATIN
        defaultClassificationShouldNotBeFound("nomLatin.notEquals=" + DEFAULT_NOM_LATIN);

        // Get all the classificationList where nomLatin not equals to UPDATED_NOM_LATIN
        defaultClassificationShouldBeFound("nomLatin.notEquals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllClassificationsByNomLatinIsInShouldWork() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classification);

        // Get all the classificationList where nomLatin in DEFAULT_NOM_LATIN or UPDATED_NOM_LATIN
        defaultClassificationShouldBeFound("nomLatin.in=" + DEFAULT_NOM_LATIN + "," + UPDATED_NOM_LATIN);

        // Get all the classificationList where nomLatin equals to UPDATED_NOM_LATIN
        defaultClassificationShouldNotBeFound("nomLatin.in=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllClassificationsByNomLatinIsNullOrNotNull() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classification);

        // Get all the classificationList where nomLatin is not null
        defaultClassificationShouldBeFound("nomLatin.specified=true");

        // Get all the classificationList where nomLatin is null
        defaultClassificationShouldNotBeFound("nomLatin.specified=false");
    }

    @Test
    @Transactional
    void getAllClassificationsByNomLatinContainsSomething() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classification);

        // Get all the classificationList where nomLatin contains DEFAULT_NOM_LATIN
        defaultClassificationShouldBeFound("nomLatin.contains=" + DEFAULT_NOM_LATIN);

        // Get all the classificationList where nomLatin contains UPDATED_NOM_LATIN
        defaultClassificationShouldNotBeFound("nomLatin.contains=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllClassificationsByNomLatinNotContainsSomething() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classification);

        // Get all the classificationList where nomLatin does not contain DEFAULT_NOM_LATIN
        defaultClassificationShouldNotBeFound("nomLatin.doesNotContain=" + DEFAULT_NOM_LATIN);

        // Get all the classificationList where nomLatin does not contain UPDATED_NOM_LATIN
        defaultClassificationShouldBeFound("nomLatin.doesNotContain=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllClassificationsByRaunkierIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classification);
        Raunkier raunkier;
        if (TestUtil.findAll(em, Raunkier.class).isEmpty()) {
            raunkier = RaunkierResourceIT.createEntity(em);
            em.persist(raunkier);
            em.flush();
        } else {
            raunkier = TestUtil.findAll(em, Raunkier.class).get(0);
        }
        em.persist(raunkier);
        em.flush();
        classification.setRaunkier(raunkier);
        classificationRepository.saveAndFlush(classification);
        Long raunkierId = raunkier.getId();

        // Get all the classificationList where raunkier equals to raunkierId
        defaultClassificationShouldBeFound("raunkierId.equals=" + raunkierId);

        // Get all the classificationList where raunkier equals to (raunkierId + 1)
        defaultClassificationShouldNotBeFound("raunkierId.equals=" + (raunkierId + 1));
    }

    @Test
    @Transactional
    void getAllClassificationsByCronquistIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classification);
        Cronquist cronquist;
        if (TestUtil.findAll(em, Cronquist.class).isEmpty()) {
            cronquist = CronquistResourceIT.createEntity(em);
            em.persist(cronquist);
            em.flush();
        } else {
            cronquist = TestUtil.findAll(em, Cronquist.class).get(0);
        }
        em.persist(cronquist);
        em.flush();
        classification.setCronquist(cronquist);
        classificationRepository.saveAndFlush(classification);
        Long cronquistId = cronquist.getId();

        // Get all the classificationList where cronquist equals to cronquistId
        defaultClassificationShouldBeFound("cronquistId.equals=" + cronquistId);

        // Get all the classificationList where cronquist equals to (cronquistId + 1)
        defaultClassificationShouldNotBeFound("cronquistId.equals=" + (cronquistId + 1));
    }

    @Test
    @Transactional
    void getAllClassificationsByApg1IsEqualToSomething() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classification);
        APGI apg1;
        if (TestUtil.findAll(em, APGI.class).isEmpty()) {
            apg1 = APGIResourceIT.createEntity(em);
            em.persist(apg1);
            em.flush();
        } else {
            apg1 = TestUtil.findAll(em, APGI.class).get(0);
        }
        em.persist(apg1);
        em.flush();
        classification.setApg1(apg1);
        classificationRepository.saveAndFlush(classification);
        Long apg1Id = apg1.getId();

        // Get all the classificationList where apg1 equals to apg1Id
        defaultClassificationShouldBeFound("apg1Id.equals=" + apg1Id);

        // Get all the classificationList where apg1 equals to (apg1Id + 1)
        defaultClassificationShouldNotBeFound("apg1Id.equals=" + (apg1Id + 1));
    }

    @Test
    @Transactional
    void getAllClassificationsByApg2IsEqualToSomething() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classification);
        APGII apg2;
        if (TestUtil.findAll(em, APGII.class).isEmpty()) {
            apg2 = APGIIResourceIT.createEntity(em);
            em.persist(apg2);
            em.flush();
        } else {
            apg2 = TestUtil.findAll(em, APGII.class).get(0);
        }
        em.persist(apg2);
        em.flush();
        classification.setApg2(apg2);
        classificationRepository.saveAndFlush(classification);
        Long apg2Id = apg2.getId();

        // Get all the classificationList where apg2 equals to apg2Id
        defaultClassificationShouldBeFound("apg2Id.equals=" + apg2Id);

        // Get all the classificationList where apg2 equals to (apg2Id + 1)
        defaultClassificationShouldNotBeFound("apg2Id.equals=" + (apg2Id + 1));
    }

    @Test
    @Transactional
    void getAllClassificationsByApg3IsEqualToSomething() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classification);
        APGIII apg3;
        if (TestUtil.findAll(em, APGIII.class).isEmpty()) {
            apg3 = APGIIIResourceIT.createEntity(em);
            em.persist(apg3);
            em.flush();
        } else {
            apg3 = TestUtil.findAll(em, APGIII.class).get(0);
        }
        em.persist(apg3);
        em.flush();
        classification.setApg3(apg3);
        classificationRepository.saveAndFlush(classification);
        Long apg3Id = apg3.getId();

        // Get all the classificationList where apg3 equals to apg3Id
        defaultClassificationShouldBeFound("apg3Id.equals=" + apg3Id);

        // Get all the classificationList where apg3 equals to (apg3Id + 1)
        defaultClassificationShouldNotBeFound("apg3Id.equals=" + (apg3Id + 1));
    }

    @Test
    @Transactional
    void getAllClassificationsByApg4IsEqualToSomething() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classification);
        APGIV apg4;
        if (TestUtil.findAll(em, APGIV.class).isEmpty()) {
            apg4 = APGIVResourceIT.createEntity(em);
            em.persist(apg4);
            em.flush();
        } else {
            apg4 = TestUtil.findAll(em, APGIV.class).get(0);
        }
        em.persist(apg4);
        em.flush();
        classification.setApg4(apg4);
        classificationRepository.saveAndFlush(classification);
        Long apg4Id = apg4.getId();

        // Get all the classificationList where apg4 equals to apg4Id
        defaultClassificationShouldBeFound("apg4Id.equals=" + apg4Id);

        // Get all the classificationList where apg4 equals to (apg4Id + 1)
        defaultClassificationShouldNotBeFound("apg4Id.equals=" + (apg4Id + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClassificationShouldBeFound(String filter) throws Exception {
        restClassificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classification.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));

        // Check, that the count call also returns 1
        restClassificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClassificationShouldNotBeFound(String filter) throws Exception {
        restClassificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClassificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingClassification() throws Exception {
        // Get the classification
        restClassificationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewClassification() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classification);

        int databaseSizeBeforeUpdate = classificationRepository.findAll().size();

        // Update the classification
        Classification updatedClassification = classificationRepository.findById(classification.getId()).get();
        // Disconnect from session so that the updates on updatedClassification are not directly saved in db
        em.detach(updatedClassification);
        updatedClassification.nomLatin(UPDATED_NOM_LATIN);

        restClassificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedClassification.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedClassification))
            )
            .andExpect(status().isOk());

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeUpdate);
        Classification testClassification = classificationList.get(classificationList.size() - 1);
        assertThat(testClassification.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void putNonExistingClassification() throws Exception {
        int databaseSizeBeforeUpdate = classificationRepository.findAll().size();
        classification.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, classification.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classification))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClassification() throws Exception {
        int databaseSizeBeforeUpdate = classificationRepository.findAll().size();
        classification.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classification))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClassification() throws Exception {
        int databaseSizeBeforeUpdate = classificationRepository.findAll().size();
        classification.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassificationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classification)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClassificationWithPatch() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classification);

        int databaseSizeBeforeUpdate = classificationRepository.findAll().size();

        // Update the classification using partial update
        Classification partialUpdatedClassification = new Classification();
        partialUpdatedClassification.setId(classification.getId());

        restClassificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClassification.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClassification))
            )
            .andExpect(status().isOk());

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeUpdate);
        Classification testClassification = classificationList.get(classificationList.size() - 1);
        assertThat(testClassification.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void fullUpdateClassificationWithPatch() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classification);

        int databaseSizeBeforeUpdate = classificationRepository.findAll().size();

        // Update the classification using partial update
        Classification partialUpdatedClassification = new Classification();
        partialUpdatedClassification.setId(classification.getId());

        partialUpdatedClassification.nomLatin(UPDATED_NOM_LATIN);

        restClassificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClassification.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClassification))
            )
            .andExpect(status().isOk());

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeUpdate);
        Classification testClassification = classificationList.get(classificationList.size() - 1);
        assertThat(testClassification.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void patchNonExistingClassification() throws Exception {
        int databaseSizeBeforeUpdate = classificationRepository.findAll().size();
        classification.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, classification.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(classification))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClassification() throws Exception {
        int databaseSizeBeforeUpdate = classificationRepository.findAll().size();
        classification.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(classification))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClassification() throws Exception {
        int databaseSizeBeforeUpdate = classificationRepository.findAll().size();
        classification.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClassificationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(classification))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClassification() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classification);

        int databaseSizeBeforeDelete = classificationRepository.findAll().size();

        // Delete the classification
        restClassificationMockMvc
            .perform(delete(ENTITY_API_URL_ID, classification.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Classification> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
