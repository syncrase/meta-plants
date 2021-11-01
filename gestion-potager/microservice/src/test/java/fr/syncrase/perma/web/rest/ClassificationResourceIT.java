package fr.syncrase.perma.web.rest;

import fr.syncrase.perma.MicroserviceApp;
import fr.syncrase.perma.config.TestSecurityConfiguration;
import fr.syncrase.perma.domain.Classification;
import fr.syncrase.perma.domain.Raunkier;
import fr.syncrase.perma.domain.Cronquist;
import fr.syncrase.perma.domain.APGI;
import fr.syncrase.perma.domain.APGII;
import fr.syncrase.perma.domain.APGIII;
import fr.syncrase.perma.domain.APGIV;
import fr.syncrase.perma.repository.ClassificationRepository;
import fr.syncrase.perma.service.ClassificationService;
import fr.syncrase.perma.service.dto.ClassificationDTO;
import fr.syncrase.perma.service.mapper.ClassificationMapper;
import fr.syncrase.perma.service.dto.ClassificationCriteria;
import fr.syncrase.perma.service.ClassificationQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ClassificationResource} REST controller.
 */
@SpringBootTest(classes = { MicroserviceApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class ClassificationResourceIT {

    @Autowired
    private ClassificationRepository classificationRepository;

    @Autowired
    private ClassificationMapper classificationMapper;

    @Autowired
    private ClassificationService classificationService;

    @Autowired
    private ClassificationQueryService classificationQueryService;

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
        Classification classification = new Classification();
        return classification;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Classification createUpdatedEntity(EntityManager em) {
        Classification classification = new Classification();
        return classification;
    }

    @BeforeEach
    public void initTest() {
        classification = createEntity(em);
    }

    @Test
    @Transactional
    public void createClassification() throws Exception {
        int databaseSizeBeforeCreate = classificationRepository.findAll().size();
        // Create the Classification
        ClassificationDTO classificationDTO = classificationMapper.toDto(classification);
        restClassificationMockMvc.perform(post("/api/classifications").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(classificationDTO)))
            .andExpect(status().isCreated());

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeCreate + 1);
        Classification testClassification = classificationList.get(classificationList.size() - 1);
    }

    @Test
    @Transactional
    public void createClassificationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = classificationRepository.findAll().size();

        // Create the Classification with an existing ID
        classification.setId(1L);
        ClassificationDTO classificationDTO = classificationMapper.toDto(classification);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClassificationMockMvc.perform(post("/api/classifications").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(classificationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllClassifications() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classification);

        // Get all the classificationList
        restClassificationMockMvc.perform(get("/api/classifications?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classification.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getClassification() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classification);

        // Get the classification
        restClassificationMockMvc.perform(get("/api/classifications/{id}", classification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(classification.getId().intValue()));
    }


    @Test
    @Transactional
    public void getClassificationsByIdFiltering() throws Exception {
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
    public void getAllClassificationsByRaunkierIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classification);
        Raunkier raunkier = RaunkierResourceIT.createEntity(em);
        em.persist(raunkier);
        em.flush();
        classification.setRaunkier(raunkier);
        classificationRepository.saveAndFlush(classification);
        Long raunkierId = raunkier.getId();

        // Get all the classificationList where raunkier equals to raunkierId
        defaultClassificationShouldBeFound("raunkierId.equals=" + raunkierId);

        // Get all the classificationList where raunkier equals to raunkierId + 1
        defaultClassificationShouldNotBeFound("raunkierId.equals=" + (raunkierId + 1));
    }


    @Test
    @Transactional
    public void getAllClassificationsByCronquistIsEqualToSomething() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classification);
        Cronquist cronquist = CronquistResourceIT.createEntity(em);
        em.persist(cronquist);
        em.flush();
        classification.setCronquist(cronquist);
        classificationRepository.saveAndFlush(classification);
        Long cronquistId = cronquist.getId();

        // Get all the classificationList where cronquist equals to cronquistId
        defaultClassificationShouldBeFound("cronquistId.equals=" + cronquistId);

        // Get all the classificationList where cronquist equals to cronquistId + 1
        defaultClassificationShouldNotBeFound("cronquistId.equals=" + (cronquistId + 1));
    }


    @Test
    @Transactional
    public void getAllClassificationsByApg1IsEqualToSomething() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classification);
        APGI apg1 = APGIResourceIT.createEntity(em);
        em.persist(apg1);
        em.flush();
        classification.setApg1(apg1);
        classificationRepository.saveAndFlush(classification);
        Long apg1Id = apg1.getId();

        // Get all the classificationList where apg1 equals to apg1Id
        defaultClassificationShouldBeFound("apg1Id.equals=" + apg1Id);

        // Get all the classificationList where apg1 equals to apg1Id + 1
        defaultClassificationShouldNotBeFound("apg1Id.equals=" + (apg1Id + 1));
    }


    @Test
    @Transactional
    public void getAllClassificationsByApg2IsEqualToSomething() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classification);
        APGII apg2 = APGIIResourceIT.createEntity(em);
        em.persist(apg2);
        em.flush();
        classification.setApg2(apg2);
        classificationRepository.saveAndFlush(classification);
        Long apg2Id = apg2.getId();

        // Get all the classificationList where apg2 equals to apg2Id
        defaultClassificationShouldBeFound("apg2Id.equals=" + apg2Id);

        // Get all the classificationList where apg2 equals to apg2Id + 1
        defaultClassificationShouldNotBeFound("apg2Id.equals=" + (apg2Id + 1));
    }


    @Test
    @Transactional
    public void getAllClassificationsByApg3IsEqualToSomething() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classification);
        APGIII apg3 = APGIIIResourceIT.createEntity(em);
        em.persist(apg3);
        em.flush();
        classification.setApg3(apg3);
        classificationRepository.saveAndFlush(classification);
        Long apg3Id = apg3.getId();

        // Get all the classificationList where apg3 equals to apg3Id
        defaultClassificationShouldBeFound("apg3Id.equals=" + apg3Id);

        // Get all the classificationList where apg3 equals to apg3Id + 1
        defaultClassificationShouldNotBeFound("apg3Id.equals=" + (apg3Id + 1));
    }


    @Test
    @Transactional
    public void getAllClassificationsByApg4IsEqualToSomething() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classification);
        APGIV apg4 = APGIVResourceIT.createEntity(em);
        em.persist(apg4);
        em.flush();
        classification.setApg4(apg4);
        classificationRepository.saveAndFlush(classification);
        Long apg4Id = apg4.getId();

        // Get all the classificationList where apg4 equals to apg4Id
        defaultClassificationShouldBeFound("apg4Id.equals=" + apg4Id);

        // Get all the classificationList where apg4 equals to apg4Id + 1
        defaultClassificationShouldNotBeFound("apg4Id.equals=" + (apg4Id + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClassificationShouldBeFound(String filter) throws Exception {
        restClassificationMockMvc.perform(get("/api/classifications?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classification.getId().intValue())));

        // Check, that the count call also returns 1
        restClassificationMockMvc.perform(get("/api/classifications/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClassificationShouldNotBeFound(String filter) throws Exception {
        restClassificationMockMvc.perform(get("/api/classifications?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClassificationMockMvc.perform(get("/api/classifications/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingClassification() throws Exception {
        // Get the classification
        restClassificationMockMvc.perform(get("/api/classifications/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClassification() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classification);

        int databaseSizeBeforeUpdate = classificationRepository.findAll().size();

        // Update the classification
        Classification updatedClassification = classificationRepository.findById(classification.getId()).get();
        // Disconnect from session so that the updates on updatedClassification are not directly saved in db
        em.detach(updatedClassification);
        ClassificationDTO classificationDTO = classificationMapper.toDto(updatedClassification);

        restClassificationMockMvc.perform(put("/api/classifications").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(classificationDTO)))
            .andExpect(status().isOk());

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeUpdate);
        Classification testClassification = classificationList.get(classificationList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingClassification() throws Exception {
        int databaseSizeBeforeUpdate = classificationRepository.findAll().size();

        // Create the Classification
        ClassificationDTO classificationDTO = classificationMapper.toDto(classification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassificationMockMvc.perform(put("/api/classifications").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(classificationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteClassification() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classification);

        int databaseSizeBeforeDelete = classificationRepository.findAll().size();

        // Delete the classification
        restClassificationMockMvc.perform(delete("/api/classifications/{id}", classification.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Classification> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
