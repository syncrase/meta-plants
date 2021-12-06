package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.Semis;
import fr.syncrase.ecosyst.repository.SemisRepository;
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
 * Integration tests for the {@link SemisResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class SemisResourceIT {

    private static final String ENTITY_API_URL = "/api/semis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SemisRepository semisRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Semis semis;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Semis createEntity(EntityManager em) {
        Semis semis = new Semis();
        return semis;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Semis createUpdatedEntity(EntityManager em) {
        Semis semis = new Semis();
        return semis;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Semis.class).block();
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
        semis = createEntity(em);
    }

    @Test
    void createSemis() throws Exception {
        int databaseSizeBeforeCreate = semisRepository.findAll().collectList().block().size();
        // Create the Semis
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(semis))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Semis in the database
        List<Semis> semisList = semisRepository.findAll().collectList().block();
        assertThat(semisList).hasSize(databaseSizeBeforeCreate + 1);
        Semis testSemis = semisList.get(semisList.size() - 1);
    }

    @Test
    void createSemisWithExistingId() throws Exception {
        // Create the Semis with an existing ID
        semis.setId(1L);

        int databaseSizeBeforeCreate = semisRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(semis))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Semis in the database
        List<Semis> semisList = semisRepository.findAll().collectList().block();
        assertThat(semisList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllSemis() {
        // Initialize the database
        semisRepository.save(semis).block();

        // Get all the semisList
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
            .value(hasItem(semis.getId().intValue()));
    }

    @Test
    void getSemis() {
        // Initialize the database
        semisRepository.save(semis).block();

        // Get the semis
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, semis.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(semis.getId().intValue()));
    }

    @Test
    void getNonExistingSemis() {
        // Get the semis
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewSemis() throws Exception {
        // Initialize the database
        semisRepository.save(semis).block();

        int databaseSizeBeforeUpdate = semisRepository.findAll().collectList().block().size();

        // Update the semis
        Semis updatedSemis = semisRepository.findById(semis.getId()).block();

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedSemis.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedSemis))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Semis in the database
        List<Semis> semisList = semisRepository.findAll().collectList().block();
        assertThat(semisList).hasSize(databaseSizeBeforeUpdate);
        Semis testSemis = semisList.get(semisList.size() - 1);
    }

    @Test
    void putNonExistingSemis() throws Exception {
        int databaseSizeBeforeUpdate = semisRepository.findAll().collectList().block().size();
        semis.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, semis.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(semis))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Semis in the database
        List<Semis> semisList = semisRepository.findAll().collectList().block();
        assertThat(semisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSemis() throws Exception {
        int databaseSizeBeforeUpdate = semisRepository.findAll().collectList().block().size();
        semis.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(semis))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Semis in the database
        List<Semis> semisList = semisRepository.findAll().collectList().block();
        assertThat(semisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSemis() throws Exception {
        int databaseSizeBeforeUpdate = semisRepository.findAll().collectList().block().size();
        semis.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(semis))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Semis in the database
        List<Semis> semisList = semisRepository.findAll().collectList().block();
        assertThat(semisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSemisWithPatch() throws Exception {
        // Initialize the database
        semisRepository.save(semis).block();

        int databaseSizeBeforeUpdate = semisRepository.findAll().collectList().block().size();

        // Update the semis using partial update
        Semis partialUpdatedSemis = new Semis();
        partialUpdatedSemis.setId(semis.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSemis.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSemis))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Semis in the database
        List<Semis> semisList = semisRepository.findAll().collectList().block();
        assertThat(semisList).hasSize(databaseSizeBeforeUpdate);
        Semis testSemis = semisList.get(semisList.size() - 1);
    }

    @Test
    void fullUpdateSemisWithPatch() throws Exception {
        // Initialize the database
        semisRepository.save(semis).block();

        int databaseSizeBeforeUpdate = semisRepository.findAll().collectList().block().size();

        // Update the semis using partial update
        Semis partialUpdatedSemis = new Semis();
        partialUpdatedSemis.setId(semis.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSemis.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSemis))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Semis in the database
        List<Semis> semisList = semisRepository.findAll().collectList().block();
        assertThat(semisList).hasSize(databaseSizeBeforeUpdate);
        Semis testSemis = semisList.get(semisList.size() - 1);
    }

    @Test
    void patchNonExistingSemis() throws Exception {
        int databaseSizeBeforeUpdate = semisRepository.findAll().collectList().block().size();
        semis.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, semis.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(semis))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Semis in the database
        List<Semis> semisList = semisRepository.findAll().collectList().block();
        assertThat(semisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSemis() throws Exception {
        int databaseSizeBeforeUpdate = semisRepository.findAll().collectList().block().size();
        semis.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(semis))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Semis in the database
        List<Semis> semisList = semisRepository.findAll().collectList().block();
        assertThat(semisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSemis() throws Exception {
        int databaseSizeBeforeUpdate = semisRepository.findAll().collectList().block().size();
        semis.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(semis))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Semis in the database
        List<Semis> semisList = semisRepository.findAll().collectList().block();
        assertThat(semisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSemis() {
        // Initialize the database
        semisRepository.save(semis).block();

        int databaseSizeBeforeDelete = semisRepository.findAll().collectList().block().size();

        // Delete the semis
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, semis.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Semis> semisList = semisRepository.findAll().collectList().block();
        assertThat(semisList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
