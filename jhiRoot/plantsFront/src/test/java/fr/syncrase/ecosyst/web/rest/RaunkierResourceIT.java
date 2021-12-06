package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.Raunkier;
import fr.syncrase.ecosyst.repository.RaunkierRepository;
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
 * Integration tests for the {@link RaunkierResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class RaunkierResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/raunkiers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RaunkierRepository raunkierRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Raunkier raunkier;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Raunkier createEntity(EntityManager em) {
        Raunkier raunkier = new Raunkier().type(DEFAULT_TYPE);
        return raunkier;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Raunkier createUpdatedEntity(EntityManager em) {
        Raunkier raunkier = new Raunkier().type(UPDATED_TYPE);
        return raunkier;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Raunkier.class).block();
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
        raunkier = createEntity(em);
    }

    @Test
    void createRaunkier() throws Exception {
        int databaseSizeBeforeCreate = raunkierRepository.findAll().collectList().block().size();
        // Create the Raunkier
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(raunkier))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Raunkier in the database
        List<Raunkier> raunkierList = raunkierRepository.findAll().collectList().block();
        assertThat(raunkierList).hasSize(databaseSizeBeforeCreate + 1);
        Raunkier testRaunkier = raunkierList.get(raunkierList.size() - 1);
        assertThat(testRaunkier.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    void createRaunkierWithExistingId() throws Exception {
        // Create the Raunkier with an existing ID
        raunkier.setId(1L);

        int databaseSizeBeforeCreate = raunkierRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(raunkier))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Raunkier in the database
        List<Raunkier> raunkierList = raunkierRepository.findAll().collectList().block();
        assertThat(raunkierList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = raunkierRepository.findAll().collectList().block().size();
        // set the field null
        raunkier.setType(null);

        // Create the Raunkier, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(raunkier))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Raunkier> raunkierList = raunkierRepository.findAll().collectList().block();
        assertThat(raunkierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllRaunkiers() {
        // Initialize the database
        raunkierRepository.save(raunkier).block();

        // Get all the raunkierList
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
            .value(hasItem(raunkier.getId().intValue()))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE));
    }

    @Test
    void getRaunkier() {
        // Initialize the database
        raunkierRepository.save(raunkier).block();

        // Get the raunkier
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, raunkier.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(raunkier.getId().intValue()))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE));
    }

    @Test
    void getNonExistingRaunkier() {
        // Get the raunkier
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewRaunkier() throws Exception {
        // Initialize the database
        raunkierRepository.save(raunkier).block();

        int databaseSizeBeforeUpdate = raunkierRepository.findAll().collectList().block().size();

        // Update the raunkier
        Raunkier updatedRaunkier = raunkierRepository.findById(raunkier.getId()).block();
        updatedRaunkier.type(UPDATED_TYPE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedRaunkier.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedRaunkier))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Raunkier in the database
        List<Raunkier> raunkierList = raunkierRepository.findAll().collectList().block();
        assertThat(raunkierList).hasSize(databaseSizeBeforeUpdate);
        Raunkier testRaunkier = raunkierList.get(raunkierList.size() - 1);
        assertThat(testRaunkier.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    void putNonExistingRaunkier() throws Exception {
        int databaseSizeBeforeUpdate = raunkierRepository.findAll().collectList().block().size();
        raunkier.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, raunkier.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(raunkier))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Raunkier in the database
        List<Raunkier> raunkierList = raunkierRepository.findAll().collectList().block();
        assertThat(raunkierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchRaunkier() throws Exception {
        int databaseSizeBeforeUpdate = raunkierRepository.findAll().collectList().block().size();
        raunkier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(raunkier))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Raunkier in the database
        List<Raunkier> raunkierList = raunkierRepository.findAll().collectList().block();
        assertThat(raunkierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamRaunkier() throws Exception {
        int databaseSizeBeforeUpdate = raunkierRepository.findAll().collectList().block().size();
        raunkier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(raunkier))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Raunkier in the database
        List<Raunkier> raunkierList = raunkierRepository.findAll().collectList().block();
        assertThat(raunkierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateRaunkierWithPatch() throws Exception {
        // Initialize the database
        raunkierRepository.save(raunkier).block();

        int databaseSizeBeforeUpdate = raunkierRepository.findAll().collectList().block().size();

        // Update the raunkier using partial update
        Raunkier partialUpdatedRaunkier = new Raunkier();
        partialUpdatedRaunkier.setId(raunkier.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRaunkier.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRaunkier))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Raunkier in the database
        List<Raunkier> raunkierList = raunkierRepository.findAll().collectList().block();
        assertThat(raunkierList).hasSize(databaseSizeBeforeUpdate);
        Raunkier testRaunkier = raunkierList.get(raunkierList.size() - 1);
        assertThat(testRaunkier.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    void fullUpdateRaunkierWithPatch() throws Exception {
        // Initialize the database
        raunkierRepository.save(raunkier).block();

        int databaseSizeBeforeUpdate = raunkierRepository.findAll().collectList().block().size();

        // Update the raunkier using partial update
        Raunkier partialUpdatedRaunkier = new Raunkier();
        partialUpdatedRaunkier.setId(raunkier.getId());

        partialUpdatedRaunkier.type(UPDATED_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRaunkier.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRaunkier))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Raunkier in the database
        List<Raunkier> raunkierList = raunkierRepository.findAll().collectList().block();
        assertThat(raunkierList).hasSize(databaseSizeBeforeUpdate);
        Raunkier testRaunkier = raunkierList.get(raunkierList.size() - 1);
        assertThat(testRaunkier.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    void patchNonExistingRaunkier() throws Exception {
        int databaseSizeBeforeUpdate = raunkierRepository.findAll().collectList().block().size();
        raunkier.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, raunkier.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(raunkier))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Raunkier in the database
        List<Raunkier> raunkierList = raunkierRepository.findAll().collectList().block();
        assertThat(raunkierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchRaunkier() throws Exception {
        int databaseSizeBeforeUpdate = raunkierRepository.findAll().collectList().block().size();
        raunkier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(raunkier))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Raunkier in the database
        List<Raunkier> raunkierList = raunkierRepository.findAll().collectList().block();
        assertThat(raunkierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamRaunkier() throws Exception {
        int databaseSizeBeforeUpdate = raunkierRepository.findAll().collectList().block().size();
        raunkier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(raunkier))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Raunkier in the database
        List<Raunkier> raunkierList = raunkierRepository.findAll().collectList().block();
        assertThat(raunkierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteRaunkier() {
        // Initialize the database
        raunkierRepository.save(raunkier).block();

        int databaseSizeBeforeDelete = raunkierRepository.findAll().collectList().block().size();

        // Delete the raunkier
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, raunkier.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Raunkier> raunkierList = raunkierRepository.findAll().collectList().block();
        assertThat(raunkierList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
