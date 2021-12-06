package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.Feuillage;
import fr.syncrase.ecosyst.repository.FeuillageRepository;
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
 * Integration tests for the {@link FeuillageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class FeuillageResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/feuillages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FeuillageRepository feuillageRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Feuillage feuillage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Feuillage createEntity(EntityManager em) {
        Feuillage feuillage = new Feuillage().type(DEFAULT_TYPE);
        return feuillage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Feuillage createUpdatedEntity(EntityManager em) {
        Feuillage feuillage = new Feuillage().type(UPDATED_TYPE);
        return feuillage;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Feuillage.class).block();
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
        feuillage = createEntity(em);
    }

    @Test
    void createFeuillage() throws Exception {
        int databaseSizeBeforeCreate = feuillageRepository.findAll().collectList().block().size();
        // Create the Feuillage
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(feuillage))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Feuillage in the database
        List<Feuillage> feuillageList = feuillageRepository.findAll().collectList().block();
        assertThat(feuillageList).hasSize(databaseSizeBeforeCreate + 1);
        Feuillage testFeuillage = feuillageList.get(feuillageList.size() - 1);
        assertThat(testFeuillage.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    void createFeuillageWithExistingId() throws Exception {
        // Create the Feuillage with an existing ID
        feuillage.setId(1L);

        int databaseSizeBeforeCreate = feuillageRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(feuillage))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Feuillage in the database
        List<Feuillage> feuillageList = feuillageRepository.findAll().collectList().block();
        assertThat(feuillageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllFeuillages() {
        // Initialize the database
        feuillageRepository.save(feuillage).block();

        // Get all the feuillageList
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
            .value(hasItem(feuillage.getId().intValue()))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE));
    }

    @Test
    void getFeuillage() {
        // Initialize the database
        feuillageRepository.save(feuillage).block();

        // Get the feuillage
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, feuillage.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(feuillage.getId().intValue()))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE));
    }

    @Test
    void getNonExistingFeuillage() {
        // Get the feuillage
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewFeuillage() throws Exception {
        // Initialize the database
        feuillageRepository.save(feuillage).block();

        int databaseSizeBeforeUpdate = feuillageRepository.findAll().collectList().block().size();

        // Update the feuillage
        Feuillage updatedFeuillage = feuillageRepository.findById(feuillage.getId()).block();
        updatedFeuillage.type(UPDATED_TYPE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedFeuillage.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedFeuillage))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Feuillage in the database
        List<Feuillage> feuillageList = feuillageRepository.findAll().collectList().block();
        assertThat(feuillageList).hasSize(databaseSizeBeforeUpdate);
        Feuillage testFeuillage = feuillageList.get(feuillageList.size() - 1);
        assertThat(testFeuillage.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    void putNonExistingFeuillage() throws Exception {
        int databaseSizeBeforeUpdate = feuillageRepository.findAll().collectList().block().size();
        feuillage.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, feuillage.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(feuillage))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Feuillage in the database
        List<Feuillage> feuillageList = feuillageRepository.findAll().collectList().block();
        assertThat(feuillageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchFeuillage() throws Exception {
        int databaseSizeBeforeUpdate = feuillageRepository.findAll().collectList().block().size();
        feuillage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(feuillage))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Feuillage in the database
        List<Feuillage> feuillageList = feuillageRepository.findAll().collectList().block();
        assertThat(feuillageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamFeuillage() throws Exception {
        int databaseSizeBeforeUpdate = feuillageRepository.findAll().collectList().block().size();
        feuillage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(feuillage))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Feuillage in the database
        List<Feuillage> feuillageList = feuillageRepository.findAll().collectList().block();
        assertThat(feuillageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateFeuillageWithPatch() throws Exception {
        // Initialize the database
        feuillageRepository.save(feuillage).block();

        int databaseSizeBeforeUpdate = feuillageRepository.findAll().collectList().block().size();

        // Update the feuillage using partial update
        Feuillage partialUpdatedFeuillage = new Feuillage();
        partialUpdatedFeuillage.setId(feuillage.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFeuillage.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFeuillage))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Feuillage in the database
        List<Feuillage> feuillageList = feuillageRepository.findAll().collectList().block();
        assertThat(feuillageList).hasSize(databaseSizeBeforeUpdate);
        Feuillage testFeuillage = feuillageList.get(feuillageList.size() - 1);
        assertThat(testFeuillage.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    void fullUpdateFeuillageWithPatch() throws Exception {
        // Initialize the database
        feuillageRepository.save(feuillage).block();

        int databaseSizeBeforeUpdate = feuillageRepository.findAll().collectList().block().size();

        // Update the feuillage using partial update
        Feuillage partialUpdatedFeuillage = new Feuillage();
        partialUpdatedFeuillage.setId(feuillage.getId());

        partialUpdatedFeuillage.type(UPDATED_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFeuillage.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFeuillage))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Feuillage in the database
        List<Feuillage> feuillageList = feuillageRepository.findAll().collectList().block();
        assertThat(feuillageList).hasSize(databaseSizeBeforeUpdate);
        Feuillage testFeuillage = feuillageList.get(feuillageList.size() - 1);
        assertThat(testFeuillage.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    void patchNonExistingFeuillage() throws Exception {
        int databaseSizeBeforeUpdate = feuillageRepository.findAll().collectList().block().size();
        feuillage.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, feuillage.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(feuillage))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Feuillage in the database
        List<Feuillage> feuillageList = feuillageRepository.findAll().collectList().block();
        assertThat(feuillageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchFeuillage() throws Exception {
        int databaseSizeBeforeUpdate = feuillageRepository.findAll().collectList().block().size();
        feuillage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(feuillage))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Feuillage in the database
        List<Feuillage> feuillageList = feuillageRepository.findAll().collectList().block();
        assertThat(feuillageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamFeuillage() throws Exception {
        int databaseSizeBeforeUpdate = feuillageRepository.findAll().collectList().block().size();
        feuillage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(feuillage))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Feuillage in the database
        List<Feuillage> feuillageList = feuillageRepository.findAll().collectList().block();
        assertThat(feuillageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteFeuillage() {
        // Initialize the database
        feuillageRepository.save(feuillage).block();

        int databaseSizeBeforeDelete = feuillageRepository.findAll().collectList().block().size();

        // Delete the feuillage
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, feuillage.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Feuillage> feuillageList = feuillageRepository.findAll().collectList().block();
        assertThat(feuillageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
