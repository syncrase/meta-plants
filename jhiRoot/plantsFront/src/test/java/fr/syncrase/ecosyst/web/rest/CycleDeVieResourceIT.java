package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.CycleDeVie;
import fr.syncrase.ecosyst.repository.CycleDeVieRepository;
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
 * Integration tests for the {@link CycleDeVieResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class CycleDeVieResourceIT {

    private static final String ENTITY_API_URL = "/api/cycle-de-vies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CycleDeVieRepository cycleDeVieRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private CycleDeVie cycleDeVie;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CycleDeVie createEntity(EntityManager em) {
        CycleDeVie cycleDeVie = new CycleDeVie();
        return cycleDeVie;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CycleDeVie createUpdatedEntity(EntityManager em) {
        CycleDeVie cycleDeVie = new CycleDeVie();
        return cycleDeVie;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(CycleDeVie.class).block();
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
        cycleDeVie = createEntity(em);
    }

    @Test
    void createCycleDeVie() throws Exception {
        int databaseSizeBeforeCreate = cycleDeVieRepository.findAll().collectList().block().size();
        // Create the CycleDeVie
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cycleDeVie))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the CycleDeVie in the database
        List<CycleDeVie> cycleDeVieList = cycleDeVieRepository.findAll().collectList().block();
        assertThat(cycleDeVieList).hasSize(databaseSizeBeforeCreate + 1);
        CycleDeVie testCycleDeVie = cycleDeVieList.get(cycleDeVieList.size() - 1);
    }

    @Test
    void createCycleDeVieWithExistingId() throws Exception {
        // Create the CycleDeVie with an existing ID
        cycleDeVie.setId(1L);

        int databaseSizeBeforeCreate = cycleDeVieRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cycleDeVie))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CycleDeVie in the database
        List<CycleDeVie> cycleDeVieList = cycleDeVieRepository.findAll().collectList().block();
        assertThat(cycleDeVieList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllCycleDeVies() {
        // Initialize the database
        cycleDeVieRepository.save(cycleDeVie).block();

        // Get all the cycleDeVieList
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
            .value(hasItem(cycleDeVie.getId().intValue()));
    }

    @Test
    void getCycleDeVie() {
        // Initialize the database
        cycleDeVieRepository.save(cycleDeVie).block();

        // Get the cycleDeVie
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, cycleDeVie.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(cycleDeVie.getId().intValue()));
    }

    @Test
    void getNonExistingCycleDeVie() {
        // Get the cycleDeVie
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewCycleDeVie() throws Exception {
        // Initialize the database
        cycleDeVieRepository.save(cycleDeVie).block();

        int databaseSizeBeforeUpdate = cycleDeVieRepository.findAll().collectList().block().size();

        // Update the cycleDeVie
        CycleDeVie updatedCycleDeVie = cycleDeVieRepository.findById(cycleDeVie.getId()).block();

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedCycleDeVie.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedCycleDeVie))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CycleDeVie in the database
        List<CycleDeVie> cycleDeVieList = cycleDeVieRepository.findAll().collectList().block();
        assertThat(cycleDeVieList).hasSize(databaseSizeBeforeUpdate);
        CycleDeVie testCycleDeVie = cycleDeVieList.get(cycleDeVieList.size() - 1);
    }

    @Test
    void putNonExistingCycleDeVie() throws Exception {
        int databaseSizeBeforeUpdate = cycleDeVieRepository.findAll().collectList().block().size();
        cycleDeVie.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, cycleDeVie.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cycleDeVie))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CycleDeVie in the database
        List<CycleDeVie> cycleDeVieList = cycleDeVieRepository.findAll().collectList().block();
        assertThat(cycleDeVieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCycleDeVie() throws Exception {
        int databaseSizeBeforeUpdate = cycleDeVieRepository.findAll().collectList().block().size();
        cycleDeVie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cycleDeVie))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CycleDeVie in the database
        List<CycleDeVie> cycleDeVieList = cycleDeVieRepository.findAll().collectList().block();
        assertThat(cycleDeVieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCycleDeVie() throws Exception {
        int databaseSizeBeforeUpdate = cycleDeVieRepository.findAll().collectList().block().size();
        cycleDeVie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cycleDeVie))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CycleDeVie in the database
        List<CycleDeVie> cycleDeVieList = cycleDeVieRepository.findAll().collectList().block();
        assertThat(cycleDeVieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCycleDeVieWithPatch() throws Exception {
        // Initialize the database
        cycleDeVieRepository.save(cycleDeVie).block();

        int databaseSizeBeforeUpdate = cycleDeVieRepository.findAll().collectList().block().size();

        // Update the cycleDeVie using partial update
        CycleDeVie partialUpdatedCycleDeVie = new CycleDeVie();
        partialUpdatedCycleDeVie.setId(cycleDeVie.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCycleDeVie.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCycleDeVie))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CycleDeVie in the database
        List<CycleDeVie> cycleDeVieList = cycleDeVieRepository.findAll().collectList().block();
        assertThat(cycleDeVieList).hasSize(databaseSizeBeforeUpdate);
        CycleDeVie testCycleDeVie = cycleDeVieList.get(cycleDeVieList.size() - 1);
    }

    @Test
    void fullUpdateCycleDeVieWithPatch() throws Exception {
        // Initialize the database
        cycleDeVieRepository.save(cycleDeVie).block();

        int databaseSizeBeforeUpdate = cycleDeVieRepository.findAll().collectList().block().size();

        // Update the cycleDeVie using partial update
        CycleDeVie partialUpdatedCycleDeVie = new CycleDeVie();
        partialUpdatedCycleDeVie.setId(cycleDeVie.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCycleDeVie.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCycleDeVie))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CycleDeVie in the database
        List<CycleDeVie> cycleDeVieList = cycleDeVieRepository.findAll().collectList().block();
        assertThat(cycleDeVieList).hasSize(databaseSizeBeforeUpdate);
        CycleDeVie testCycleDeVie = cycleDeVieList.get(cycleDeVieList.size() - 1);
    }

    @Test
    void patchNonExistingCycleDeVie() throws Exception {
        int databaseSizeBeforeUpdate = cycleDeVieRepository.findAll().collectList().block().size();
        cycleDeVie.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, cycleDeVie.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(cycleDeVie))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CycleDeVie in the database
        List<CycleDeVie> cycleDeVieList = cycleDeVieRepository.findAll().collectList().block();
        assertThat(cycleDeVieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCycleDeVie() throws Exception {
        int databaseSizeBeforeUpdate = cycleDeVieRepository.findAll().collectList().block().size();
        cycleDeVie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(cycleDeVie))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CycleDeVie in the database
        List<CycleDeVie> cycleDeVieList = cycleDeVieRepository.findAll().collectList().block();
        assertThat(cycleDeVieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCycleDeVie() throws Exception {
        int databaseSizeBeforeUpdate = cycleDeVieRepository.findAll().collectList().block().size();
        cycleDeVie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(cycleDeVie))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CycleDeVie in the database
        List<CycleDeVie> cycleDeVieList = cycleDeVieRepository.findAll().collectList().block();
        assertThat(cycleDeVieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCycleDeVie() {
        // Initialize the database
        cycleDeVieRepository.save(cycleDeVie).block();

        int databaseSizeBeforeDelete = cycleDeVieRepository.findAll().collectList().block().size();

        // Delete the cycleDeVie
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, cycleDeVie.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<CycleDeVie> cycleDeVieList = cycleDeVieRepository.findAll().collectList().block();
        assertThat(cycleDeVieList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
