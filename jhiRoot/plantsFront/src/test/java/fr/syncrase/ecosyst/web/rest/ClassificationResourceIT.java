package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.Classification;
import fr.syncrase.ecosyst.repository.ClassificationRepository;
import fr.syncrase.ecosyst.service.EntityManager;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link ClassificationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
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
    private WebTestClient webTestClient;

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

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Classification.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        classification = createEntity(em);
    }

    @Test
    void createClassification() throws Exception {
        int databaseSizeBeforeCreate = classificationRepository.findAll().collectList().block().size();
        // Create the Classification
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(classification))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll().collectList().block();
        assertThat(classificationList).hasSize(databaseSizeBeforeCreate + 1);
        Classification testClassification = classificationList.get(classificationList.size() - 1);
        assertThat(testClassification.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    void createClassificationWithExistingId() throws Exception {
        // Create the Classification with an existing ID
        classification.setId(1L);

        int databaseSizeBeforeCreate = classificationRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(classification))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll().collectList().block();
        assertThat(classificationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllClassifications() {
        // Initialize the database
        classificationRepository.save(classification).block();

        // Get all the classificationList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(classification.getId().intValue()))
            .jsonPath("$.[*].nomLatin")
            .value(hasItem(DEFAULT_NOM_LATIN));
    }

    @Test
    void getClassification() {
        // Initialize the database
        classificationRepository.save(classification).block();

        // Get the classification
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, classification.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(classification.getId().intValue()))
            .jsonPath("$.nomLatin")
            .value(is(DEFAULT_NOM_LATIN));
    }

    @Test
    void getNonExistingClassification() {
        // Get the classification
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewClassification() throws Exception {
        // Initialize the database
        classificationRepository.save(classification).block();

        int databaseSizeBeforeUpdate = classificationRepository.findAll().collectList().block().size();

        // Update the classification
        Classification updatedClassification = classificationRepository.findById(classification.getId()).block();
        updatedClassification.nomLatin(UPDATED_NOM_LATIN);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedClassification.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedClassification))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll().collectList().block();
        assertThat(classificationList).hasSize(databaseSizeBeforeUpdate);
        Classification testClassification = classificationList.get(classificationList.size() - 1);
        assertThat(testClassification.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    void putNonExistingClassification() throws Exception {
        int databaseSizeBeforeUpdate = classificationRepository.findAll().collectList().block().size();
        classification.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, classification.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(classification))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll().collectList().block();
        assertThat(classificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchClassification() throws Exception {
        int databaseSizeBeforeUpdate = classificationRepository.findAll().collectList().block().size();
        classification.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(classification))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll().collectList().block();
        assertThat(classificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamClassification() throws Exception {
        int databaseSizeBeforeUpdate = classificationRepository.findAll().collectList().block().size();
        classification.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(classification))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll().collectList().block();
        assertThat(classificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateClassificationWithPatch() throws Exception {
        // Initialize the database
        classificationRepository.save(classification).block();

        int databaseSizeBeforeUpdate = classificationRepository.findAll().collectList().block().size();

        // Update the classification using partial update
        Classification partialUpdatedClassification = new Classification();
        partialUpdatedClassification.setId(classification.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedClassification.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedClassification))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll().collectList().block();
        assertThat(classificationList).hasSize(databaseSizeBeforeUpdate);
        Classification testClassification = classificationList.get(classificationList.size() - 1);
        assertThat(testClassification.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    void fullUpdateClassificationWithPatch() throws Exception {
        // Initialize the database
        classificationRepository.save(classification).block();

        int databaseSizeBeforeUpdate = classificationRepository.findAll().collectList().block().size();

        // Update the classification using partial update
        Classification partialUpdatedClassification = new Classification();
        partialUpdatedClassification.setId(classification.getId());

        partialUpdatedClassification.nomLatin(UPDATED_NOM_LATIN);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedClassification.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedClassification))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll().collectList().block();
        assertThat(classificationList).hasSize(databaseSizeBeforeUpdate);
        Classification testClassification = classificationList.get(classificationList.size() - 1);
        assertThat(testClassification.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    void patchNonExistingClassification() throws Exception {
        int databaseSizeBeforeUpdate = classificationRepository.findAll().collectList().block().size();
        classification.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, classification.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(classification))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll().collectList().block();
        assertThat(classificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchClassification() throws Exception {
        int databaseSizeBeforeUpdate = classificationRepository.findAll().collectList().block().size();
        classification.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(classification))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll().collectList().block();
        assertThat(classificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamClassification() throws Exception {
        int databaseSizeBeforeUpdate = classificationRepository.findAll().collectList().block().size();
        classification.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(classification))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll().collectList().block();
        assertThat(classificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteClassification() {
        // Initialize the database
        classificationRepository.save(classification).block();

        int databaseSizeBeforeDelete = classificationRepository.findAll().collectList().block().size();

        // Delete the classification
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, classification.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Classification> classificationList = classificationRepository.findAll().collectList().block();
        assertThat(classificationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
