package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.Germination;
import fr.syncrase.ecosyst.repository.GerminationRepository;
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
 * Integration tests for the {@link GerminationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class GerminationResourceIT {

    private static final String DEFAULT_TEMPS_DE_GERMINATION = "AAAAAAAAAA";
    private static final String UPDATED_TEMPS_DE_GERMINATION = "BBBBBBBBBB";

    private static final String DEFAULT_CONDITION_DE_GERMINATION = "AAAAAAAAAA";
    private static final String UPDATED_CONDITION_DE_GERMINATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/germinations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GerminationRepository germinationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Germination germination;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Germination createEntity(EntityManager em) {
        Germination germination = new Germination()
            .tempsDeGermination(DEFAULT_TEMPS_DE_GERMINATION)
            .conditionDeGermination(DEFAULT_CONDITION_DE_GERMINATION);
        return germination;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Germination createUpdatedEntity(EntityManager em) {
        Germination germination = new Germination()
            .tempsDeGermination(UPDATED_TEMPS_DE_GERMINATION)
            .conditionDeGermination(UPDATED_CONDITION_DE_GERMINATION);
        return germination;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Germination.class).block();
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
        germination = createEntity(em);
    }

    @Test
    void createGermination() throws Exception {
        int databaseSizeBeforeCreate = germinationRepository.findAll().collectList().block().size();
        // Create the Germination
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(germination))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Germination in the database
        List<Germination> germinationList = germinationRepository.findAll().collectList().block();
        assertThat(germinationList).hasSize(databaseSizeBeforeCreate + 1);
        Germination testGermination = germinationList.get(germinationList.size() - 1);
        assertThat(testGermination.getTempsDeGermination()).isEqualTo(DEFAULT_TEMPS_DE_GERMINATION);
        assertThat(testGermination.getConditionDeGermination()).isEqualTo(DEFAULT_CONDITION_DE_GERMINATION);
    }

    @Test
    void createGerminationWithExistingId() throws Exception {
        // Create the Germination with an existing ID
        germination.setId(1L);

        int databaseSizeBeforeCreate = germinationRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(germination))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Germination in the database
        List<Germination> germinationList = germinationRepository.findAll().collectList().block();
        assertThat(germinationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllGerminations() {
        // Initialize the database
        germinationRepository.save(germination).block();

        // Get all the germinationList
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
            .value(hasItem(germination.getId().intValue()))
            .jsonPath("$.[*].tempsDeGermination")
            .value(hasItem(DEFAULT_TEMPS_DE_GERMINATION))
            .jsonPath("$.[*].conditionDeGermination")
            .value(hasItem(DEFAULT_CONDITION_DE_GERMINATION));
    }

    @Test
    void getGermination() {
        // Initialize the database
        germinationRepository.save(germination).block();

        // Get the germination
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, germination.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(germination.getId().intValue()))
            .jsonPath("$.tempsDeGermination")
            .value(is(DEFAULT_TEMPS_DE_GERMINATION))
            .jsonPath("$.conditionDeGermination")
            .value(is(DEFAULT_CONDITION_DE_GERMINATION));
    }

    @Test
    void getNonExistingGermination() {
        // Get the germination
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewGermination() throws Exception {
        // Initialize the database
        germinationRepository.save(germination).block();

        int databaseSizeBeforeUpdate = germinationRepository.findAll().collectList().block().size();

        // Update the germination
        Germination updatedGermination = germinationRepository.findById(germination.getId()).block();
        updatedGermination.tempsDeGermination(UPDATED_TEMPS_DE_GERMINATION).conditionDeGermination(UPDATED_CONDITION_DE_GERMINATION);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedGermination.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedGermination))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Germination in the database
        List<Germination> germinationList = germinationRepository.findAll().collectList().block();
        assertThat(germinationList).hasSize(databaseSizeBeforeUpdate);
        Germination testGermination = germinationList.get(germinationList.size() - 1);
        assertThat(testGermination.getTempsDeGermination()).isEqualTo(UPDATED_TEMPS_DE_GERMINATION);
        assertThat(testGermination.getConditionDeGermination()).isEqualTo(UPDATED_CONDITION_DE_GERMINATION);
    }

    @Test
    void putNonExistingGermination() throws Exception {
        int databaseSizeBeforeUpdate = germinationRepository.findAll().collectList().block().size();
        germination.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, germination.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(germination))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Germination in the database
        List<Germination> germinationList = germinationRepository.findAll().collectList().block();
        assertThat(germinationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchGermination() throws Exception {
        int databaseSizeBeforeUpdate = germinationRepository.findAll().collectList().block().size();
        germination.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(germination))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Germination in the database
        List<Germination> germinationList = germinationRepository.findAll().collectList().block();
        assertThat(germinationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamGermination() throws Exception {
        int databaseSizeBeforeUpdate = germinationRepository.findAll().collectList().block().size();
        germination.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(germination))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Germination in the database
        List<Germination> germinationList = germinationRepository.findAll().collectList().block();
        assertThat(germinationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateGerminationWithPatch() throws Exception {
        // Initialize the database
        germinationRepository.save(germination).block();

        int databaseSizeBeforeUpdate = germinationRepository.findAll().collectList().block().size();

        // Update the germination using partial update
        Germination partialUpdatedGermination = new Germination();
        partialUpdatedGermination.setId(germination.getId());

        partialUpdatedGermination.conditionDeGermination(UPDATED_CONDITION_DE_GERMINATION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedGermination.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedGermination))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Germination in the database
        List<Germination> germinationList = germinationRepository.findAll().collectList().block();
        assertThat(germinationList).hasSize(databaseSizeBeforeUpdate);
        Germination testGermination = germinationList.get(germinationList.size() - 1);
        assertThat(testGermination.getTempsDeGermination()).isEqualTo(DEFAULT_TEMPS_DE_GERMINATION);
        assertThat(testGermination.getConditionDeGermination()).isEqualTo(UPDATED_CONDITION_DE_GERMINATION);
    }

    @Test
    void fullUpdateGerminationWithPatch() throws Exception {
        // Initialize the database
        germinationRepository.save(germination).block();

        int databaseSizeBeforeUpdate = germinationRepository.findAll().collectList().block().size();

        // Update the germination using partial update
        Germination partialUpdatedGermination = new Germination();
        partialUpdatedGermination.setId(germination.getId());

        partialUpdatedGermination.tempsDeGermination(UPDATED_TEMPS_DE_GERMINATION).conditionDeGermination(UPDATED_CONDITION_DE_GERMINATION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedGermination.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedGermination))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Germination in the database
        List<Germination> germinationList = germinationRepository.findAll().collectList().block();
        assertThat(germinationList).hasSize(databaseSizeBeforeUpdate);
        Germination testGermination = germinationList.get(germinationList.size() - 1);
        assertThat(testGermination.getTempsDeGermination()).isEqualTo(UPDATED_TEMPS_DE_GERMINATION);
        assertThat(testGermination.getConditionDeGermination()).isEqualTo(UPDATED_CONDITION_DE_GERMINATION);
    }

    @Test
    void patchNonExistingGermination() throws Exception {
        int databaseSizeBeforeUpdate = germinationRepository.findAll().collectList().block().size();
        germination.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, germination.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(germination))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Germination in the database
        List<Germination> germinationList = germinationRepository.findAll().collectList().block();
        assertThat(germinationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchGermination() throws Exception {
        int databaseSizeBeforeUpdate = germinationRepository.findAll().collectList().block().size();
        germination.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(germination))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Germination in the database
        List<Germination> germinationList = germinationRepository.findAll().collectList().block();
        assertThat(germinationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamGermination() throws Exception {
        int databaseSizeBeforeUpdate = germinationRepository.findAll().collectList().block().size();
        germination.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(germination))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Germination in the database
        List<Germination> germinationList = germinationRepository.findAll().collectList().block();
        assertThat(germinationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteGermination() {
        // Initialize the database
        germinationRepository.save(germination).block();

        int databaseSizeBeforeDelete = germinationRepository.findAll().collectList().block().size();

        // Delete the germination
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, germination.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Germination> germinationList = germinationRepository.findAll().collectList().block();
        assertThat(germinationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
