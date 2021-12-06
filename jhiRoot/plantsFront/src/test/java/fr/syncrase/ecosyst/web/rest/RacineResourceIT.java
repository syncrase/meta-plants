package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.Racine;
import fr.syncrase.ecosyst.repository.RacineRepository;
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
 * Integration tests for the {@link RacineResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class RacineResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/racines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RacineRepository racineRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Racine racine;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Racine createEntity(EntityManager em) {
        Racine racine = new Racine().type(DEFAULT_TYPE);
        return racine;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Racine createUpdatedEntity(EntityManager em) {
        Racine racine = new Racine().type(UPDATED_TYPE);
        return racine;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Racine.class).block();
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
        racine = createEntity(em);
    }

    @Test
    void createRacine() throws Exception {
        int databaseSizeBeforeCreate = racineRepository.findAll().collectList().block().size();
        // Create the Racine
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(racine))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Racine in the database
        List<Racine> racineList = racineRepository.findAll().collectList().block();
        assertThat(racineList).hasSize(databaseSizeBeforeCreate + 1);
        Racine testRacine = racineList.get(racineList.size() - 1);
        assertThat(testRacine.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    void createRacineWithExistingId() throws Exception {
        // Create the Racine with an existing ID
        racine.setId(1L);

        int databaseSizeBeforeCreate = racineRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(racine))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Racine in the database
        List<Racine> racineList = racineRepository.findAll().collectList().block();
        assertThat(racineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllRacines() {
        // Initialize the database
        racineRepository.save(racine).block();

        // Get all the racineList
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
            .value(hasItem(racine.getId().intValue()))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE));
    }

    @Test
    void getRacine() {
        // Initialize the database
        racineRepository.save(racine).block();

        // Get the racine
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, racine.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(racine.getId().intValue()))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE));
    }

    @Test
    void getNonExistingRacine() {
        // Get the racine
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewRacine() throws Exception {
        // Initialize the database
        racineRepository.save(racine).block();

        int databaseSizeBeforeUpdate = racineRepository.findAll().collectList().block().size();

        // Update the racine
        Racine updatedRacine = racineRepository.findById(racine.getId()).block();
        updatedRacine.type(UPDATED_TYPE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedRacine.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedRacine))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Racine in the database
        List<Racine> racineList = racineRepository.findAll().collectList().block();
        assertThat(racineList).hasSize(databaseSizeBeforeUpdate);
        Racine testRacine = racineList.get(racineList.size() - 1);
        assertThat(testRacine.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    void putNonExistingRacine() throws Exception {
        int databaseSizeBeforeUpdate = racineRepository.findAll().collectList().block().size();
        racine.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, racine.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(racine))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Racine in the database
        List<Racine> racineList = racineRepository.findAll().collectList().block();
        assertThat(racineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchRacine() throws Exception {
        int databaseSizeBeforeUpdate = racineRepository.findAll().collectList().block().size();
        racine.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(racine))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Racine in the database
        List<Racine> racineList = racineRepository.findAll().collectList().block();
        assertThat(racineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamRacine() throws Exception {
        int databaseSizeBeforeUpdate = racineRepository.findAll().collectList().block().size();
        racine.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(racine))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Racine in the database
        List<Racine> racineList = racineRepository.findAll().collectList().block();
        assertThat(racineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateRacineWithPatch() throws Exception {
        // Initialize the database
        racineRepository.save(racine).block();

        int databaseSizeBeforeUpdate = racineRepository.findAll().collectList().block().size();

        // Update the racine using partial update
        Racine partialUpdatedRacine = new Racine();
        partialUpdatedRacine.setId(racine.getId());

        partialUpdatedRacine.type(UPDATED_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRacine.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRacine))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Racine in the database
        List<Racine> racineList = racineRepository.findAll().collectList().block();
        assertThat(racineList).hasSize(databaseSizeBeforeUpdate);
        Racine testRacine = racineList.get(racineList.size() - 1);
        assertThat(testRacine.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    void fullUpdateRacineWithPatch() throws Exception {
        // Initialize the database
        racineRepository.save(racine).block();

        int databaseSizeBeforeUpdate = racineRepository.findAll().collectList().block().size();

        // Update the racine using partial update
        Racine partialUpdatedRacine = new Racine();
        partialUpdatedRacine.setId(racine.getId());

        partialUpdatedRacine.type(UPDATED_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRacine.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRacine))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Racine in the database
        List<Racine> racineList = racineRepository.findAll().collectList().block();
        assertThat(racineList).hasSize(databaseSizeBeforeUpdate);
        Racine testRacine = racineList.get(racineList.size() - 1);
        assertThat(testRacine.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    void patchNonExistingRacine() throws Exception {
        int databaseSizeBeforeUpdate = racineRepository.findAll().collectList().block().size();
        racine.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, racine.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(racine))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Racine in the database
        List<Racine> racineList = racineRepository.findAll().collectList().block();
        assertThat(racineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchRacine() throws Exception {
        int databaseSizeBeforeUpdate = racineRepository.findAll().collectList().block().size();
        racine.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(racine))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Racine in the database
        List<Racine> racineList = racineRepository.findAll().collectList().block();
        assertThat(racineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamRacine() throws Exception {
        int databaseSizeBeforeUpdate = racineRepository.findAll().collectList().block().size();
        racine.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(racine))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Racine in the database
        List<Racine> racineList = racineRepository.findAll().collectList().block();
        assertThat(racineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteRacine() {
        // Initialize the database
        racineRepository.save(racine).block();

        int databaseSizeBeforeDelete = racineRepository.findAll().collectList().block().size();

        // Delete the racine
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, racine.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Racine> racineList = racineRepository.findAll().collectList().block();
        assertThat(racineList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
