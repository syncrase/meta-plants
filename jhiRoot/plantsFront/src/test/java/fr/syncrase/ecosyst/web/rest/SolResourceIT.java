package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.Sol;
import fr.syncrase.ecosyst.repository.SolRepository;
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
 * Integration tests for the {@link SolResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class SolResourceIT {

    private static final Double DEFAULT_PH_MIN = 1D;
    private static final Double UPDATED_PH_MIN = 2D;

    private static final Double DEFAULT_PH_MAX = 1D;
    private static final Double UPDATED_PH_MAX = 2D;

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_RICHESSE = "AAAAAAAAAA";
    private static final String UPDATED_RICHESSE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sols";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SolRepository solRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Sol sol;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sol createEntity(EntityManager em) {
        Sol sol = new Sol().phMin(DEFAULT_PH_MIN).phMax(DEFAULT_PH_MAX).type(DEFAULT_TYPE).richesse(DEFAULT_RICHESSE);
        return sol;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sol createUpdatedEntity(EntityManager em) {
        Sol sol = new Sol().phMin(UPDATED_PH_MIN).phMax(UPDATED_PH_MAX).type(UPDATED_TYPE).richesse(UPDATED_RICHESSE);
        return sol;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Sol.class).block();
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
        sol = createEntity(em);
    }

    @Test
    void createSol() throws Exception {
        int databaseSizeBeforeCreate = solRepository.findAll().collectList().block().size();
        // Create the Sol
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sol))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Sol in the database
        List<Sol> solList = solRepository.findAll().collectList().block();
        assertThat(solList).hasSize(databaseSizeBeforeCreate + 1);
        Sol testSol = solList.get(solList.size() - 1);
        assertThat(testSol.getPhMin()).isEqualTo(DEFAULT_PH_MIN);
        assertThat(testSol.getPhMax()).isEqualTo(DEFAULT_PH_MAX);
        assertThat(testSol.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testSol.getRichesse()).isEqualTo(DEFAULT_RICHESSE);
    }

    @Test
    void createSolWithExistingId() throws Exception {
        // Create the Sol with an existing ID
        sol.setId(1L);

        int databaseSizeBeforeCreate = solRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sol))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Sol in the database
        List<Sol> solList = solRepository.findAll().collectList().block();
        assertThat(solList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllSols() {
        // Initialize the database
        solRepository.save(sol).block();

        // Get all the solList
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
            .value(hasItem(sol.getId().intValue()))
            .jsonPath("$.[*].phMin")
            .value(hasItem(DEFAULT_PH_MIN.doubleValue()))
            .jsonPath("$.[*].phMax")
            .value(hasItem(DEFAULT_PH_MAX.doubleValue()))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE))
            .jsonPath("$.[*].richesse")
            .value(hasItem(DEFAULT_RICHESSE));
    }

    @Test
    void getSol() {
        // Initialize the database
        solRepository.save(sol).block();

        // Get the sol
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, sol.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(sol.getId().intValue()))
            .jsonPath("$.phMin")
            .value(is(DEFAULT_PH_MIN.doubleValue()))
            .jsonPath("$.phMax")
            .value(is(DEFAULT_PH_MAX.doubleValue()))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE))
            .jsonPath("$.richesse")
            .value(is(DEFAULT_RICHESSE));
    }

    @Test
    void getNonExistingSol() {
        // Get the sol
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewSol() throws Exception {
        // Initialize the database
        solRepository.save(sol).block();

        int databaseSizeBeforeUpdate = solRepository.findAll().collectList().block().size();

        // Update the sol
        Sol updatedSol = solRepository.findById(sol.getId()).block();
        updatedSol.phMin(UPDATED_PH_MIN).phMax(UPDATED_PH_MAX).type(UPDATED_TYPE).richesse(UPDATED_RICHESSE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedSol.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedSol))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Sol in the database
        List<Sol> solList = solRepository.findAll().collectList().block();
        assertThat(solList).hasSize(databaseSizeBeforeUpdate);
        Sol testSol = solList.get(solList.size() - 1);
        assertThat(testSol.getPhMin()).isEqualTo(UPDATED_PH_MIN);
        assertThat(testSol.getPhMax()).isEqualTo(UPDATED_PH_MAX);
        assertThat(testSol.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testSol.getRichesse()).isEqualTo(UPDATED_RICHESSE);
    }

    @Test
    void putNonExistingSol() throws Exception {
        int databaseSizeBeforeUpdate = solRepository.findAll().collectList().block().size();
        sol.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, sol.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sol))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Sol in the database
        List<Sol> solList = solRepository.findAll().collectList().block();
        assertThat(solList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSol() throws Exception {
        int databaseSizeBeforeUpdate = solRepository.findAll().collectList().block().size();
        sol.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sol))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Sol in the database
        List<Sol> solList = solRepository.findAll().collectList().block();
        assertThat(solList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSol() throws Exception {
        int databaseSizeBeforeUpdate = solRepository.findAll().collectList().block().size();
        sol.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sol))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Sol in the database
        List<Sol> solList = solRepository.findAll().collectList().block();
        assertThat(solList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSolWithPatch() throws Exception {
        // Initialize the database
        solRepository.save(sol).block();

        int databaseSizeBeforeUpdate = solRepository.findAll().collectList().block().size();

        // Update the sol using partial update
        Sol partialUpdatedSol = new Sol();
        partialUpdatedSol.setId(sol.getId());

        partialUpdatedSol.phMin(UPDATED_PH_MIN).phMax(UPDATED_PH_MAX).type(UPDATED_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSol.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSol))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Sol in the database
        List<Sol> solList = solRepository.findAll().collectList().block();
        assertThat(solList).hasSize(databaseSizeBeforeUpdate);
        Sol testSol = solList.get(solList.size() - 1);
        assertThat(testSol.getPhMin()).isEqualTo(UPDATED_PH_MIN);
        assertThat(testSol.getPhMax()).isEqualTo(UPDATED_PH_MAX);
        assertThat(testSol.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testSol.getRichesse()).isEqualTo(DEFAULT_RICHESSE);
    }

    @Test
    void fullUpdateSolWithPatch() throws Exception {
        // Initialize the database
        solRepository.save(sol).block();

        int databaseSizeBeforeUpdate = solRepository.findAll().collectList().block().size();

        // Update the sol using partial update
        Sol partialUpdatedSol = new Sol();
        partialUpdatedSol.setId(sol.getId());

        partialUpdatedSol.phMin(UPDATED_PH_MIN).phMax(UPDATED_PH_MAX).type(UPDATED_TYPE).richesse(UPDATED_RICHESSE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSol.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSol))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Sol in the database
        List<Sol> solList = solRepository.findAll().collectList().block();
        assertThat(solList).hasSize(databaseSizeBeforeUpdate);
        Sol testSol = solList.get(solList.size() - 1);
        assertThat(testSol.getPhMin()).isEqualTo(UPDATED_PH_MIN);
        assertThat(testSol.getPhMax()).isEqualTo(UPDATED_PH_MAX);
        assertThat(testSol.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testSol.getRichesse()).isEqualTo(UPDATED_RICHESSE);
    }

    @Test
    void patchNonExistingSol() throws Exception {
        int databaseSizeBeforeUpdate = solRepository.findAll().collectList().block().size();
        sol.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, sol.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(sol))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Sol in the database
        List<Sol> solList = solRepository.findAll().collectList().block();
        assertThat(solList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSol() throws Exception {
        int databaseSizeBeforeUpdate = solRepository.findAll().collectList().block().size();
        sol.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(sol))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Sol in the database
        List<Sol> solList = solRepository.findAll().collectList().block();
        assertThat(solList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSol() throws Exception {
        int databaseSizeBeforeUpdate = solRepository.findAll().collectList().block().size();
        sol.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(sol))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Sol in the database
        List<Sol> solList = solRepository.findAll().collectList().block();
        assertThat(solList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSol() {
        // Initialize the database
        solRepository.save(sol).block();

        int databaseSizeBeforeDelete = solRepository.findAll().collectList().block().size();

        // Delete the sol
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, sol.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Sol> solList = solRepository.findAll().collectList().block();
        assertThat(solList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
