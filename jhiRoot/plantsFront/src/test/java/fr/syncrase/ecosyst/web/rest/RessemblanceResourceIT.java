package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.Ressemblance;
import fr.syncrase.ecosyst.repository.RessemblanceRepository;
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
 * Integration tests for the {@link RessemblanceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class RessemblanceResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ressemblances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RessemblanceRepository ressemblanceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Ressemblance ressemblance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ressemblance createEntity(EntityManager em) {
        Ressemblance ressemblance = new Ressemblance().description(DEFAULT_DESCRIPTION);
        return ressemblance;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ressemblance createUpdatedEntity(EntityManager em) {
        Ressemblance ressemblance = new Ressemblance().description(UPDATED_DESCRIPTION);
        return ressemblance;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Ressemblance.class).block();
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
        ressemblance = createEntity(em);
    }

    @Test
    void createRessemblance() throws Exception {
        int databaseSizeBeforeCreate = ressemblanceRepository.findAll().collectList().block().size();
        // Create the Ressemblance
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ressemblance))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Ressemblance in the database
        List<Ressemblance> ressemblanceList = ressemblanceRepository.findAll().collectList().block();
        assertThat(ressemblanceList).hasSize(databaseSizeBeforeCreate + 1);
        Ressemblance testRessemblance = ressemblanceList.get(ressemblanceList.size() - 1);
        assertThat(testRessemblance.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void createRessemblanceWithExistingId() throws Exception {
        // Create the Ressemblance with an existing ID
        ressemblance.setId(1L);

        int databaseSizeBeforeCreate = ressemblanceRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ressemblance))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Ressemblance in the database
        List<Ressemblance> ressemblanceList = ressemblanceRepository.findAll().collectList().block();
        assertThat(ressemblanceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllRessemblances() {
        // Initialize the database
        ressemblanceRepository.save(ressemblance).block();

        // Get all the ressemblanceList
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
            .value(hasItem(ressemblance.getId().intValue()))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION));
    }

    @Test
    void getRessemblance() {
        // Initialize the database
        ressemblanceRepository.save(ressemblance).block();

        // Get the ressemblance
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, ressemblance.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(ressemblance.getId().intValue()))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION));
    }

    @Test
    void getNonExistingRessemblance() {
        // Get the ressemblance
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewRessemblance() throws Exception {
        // Initialize the database
        ressemblanceRepository.save(ressemblance).block();

        int databaseSizeBeforeUpdate = ressemblanceRepository.findAll().collectList().block().size();

        // Update the ressemblance
        Ressemblance updatedRessemblance = ressemblanceRepository.findById(ressemblance.getId()).block();
        updatedRessemblance.description(UPDATED_DESCRIPTION);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedRessemblance.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedRessemblance))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Ressemblance in the database
        List<Ressemblance> ressemblanceList = ressemblanceRepository.findAll().collectList().block();
        assertThat(ressemblanceList).hasSize(databaseSizeBeforeUpdate);
        Ressemblance testRessemblance = ressemblanceList.get(ressemblanceList.size() - 1);
        assertThat(testRessemblance.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void putNonExistingRessemblance() throws Exception {
        int databaseSizeBeforeUpdate = ressemblanceRepository.findAll().collectList().block().size();
        ressemblance.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, ressemblance.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ressemblance))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Ressemblance in the database
        List<Ressemblance> ressemblanceList = ressemblanceRepository.findAll().collectList().block();
        assertThat(ressemblanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchRessemblance() throws Exception {
        int databaseSizeBeforeUpdate = ressemblanceRepository.findAll().collectList().block().size();
        ressemblance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ressemblance))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Ressemblance in the database
        List<Ressemblance> ressemblanceList = ressemblanceRepository.findAll().collectList().block();
        assertThat(ressemblanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamRessemblance() throws Exception {
        int databaseSizeBeforeUpdate = ressemblanceRepository.findAll().collectList().block().size();
        ressemblance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ressemblance))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Ressemblance in the database
        List<Ressemblance> ressemblanceList = ressemblanceRepository.findAll().collectList().block();
        assertThat(ressemblanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateRessemblanceWithPatch() throws Exception {
        // Initialize the database
        ressemblanceRepository.save(ressemblance).block();

        int databaseSizeBeforeUpdate = ressemblanceRepository.findAll().collectList().block().size();

        // Update the ressemblance using partial update
        Ressemblance partialUpdatedRessemblance = new Ressemblance();
        partialUpdatedRessemblance.setId(ressemblance.getId());

        partialUpdatedRessemblance.description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRessemblance.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRessemblance))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Ressemblance in the database
        List<Ressemblance> ressemblanceList = ressemblanceRepository.findAll().collectList().block();
        assertThat(ressemblanceList).hasSize(databaseSizeBeforeUpdate);
        Ressemblance testRessemblance = ressemblanceList.get(ressemblanceList.size() - 1);
        assertThat(testRessemblance.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void fullUpdateRessemblanceWithPatch() throws Exception {
        // Initialize the database
        ressemblanceRepository.save(ressemblance).block();

        int databaseSizeBeforeUpdate = ressemblanceRepository.findAll().collectList().block().size();

        // Update the ressemblance using partial update
        Ressemblance partialUpdatedRessemblance = new Ressemblance();
        partialUpdatedRessemblance.setId(ressemblance.getId());

        partialUpdatedRessemblance.description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRessemblance.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRessemblance))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Ressemblance in the database
        List<Ressemblance> ressemblanceList = ressemblanceRepository.findAll().collectList().block();
        assertThat(ressemblanceList).hasSize(databaseSizeBeforeUpdate);
        Ressemblance testRessemblance = ressemblanceList.get(ressemblanceList.size() - 1);
        assertThat(testRessemblance.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void patchNonExistingRessemblance() throws Exception {
        int databaseSizeBeforeUpdate = ressemblanceRepository.findAll().collectList().block().size();
        ressemblance.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, ressemblance.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ressemblance))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Ressemblance in the database
        List<Ressemblance> ressemblanceList = ressemblanceRepository.findAll().collectList().block();
        assertThat(ressemblanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchRessemblance() throws Exception {
        int databaseSizeBeforeUpdate = ressemblanceRepository.findAll().collectList().block().size();
        ressemblance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ressemblance))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Ressemblance in the database
        List<Ressemblance> ressemblanceList = ressemblanceRepository.findAll().collectList().block();
        assertThat(ressemblanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamRessemblance() throws Exception {
        int databaseSizeBeforeUpdate = ressemblanceRepository.findAll().collectList().block().size();
        ressemblance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ressemblance))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Ressemblance in the database
        List<Ressemblance> ressemblanceList = ressemblanceRepository.findAll().collectList().block();
        assertThat(ressemblanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteRessemblance() {
        // Initialize the database
        ressemblanceRepository.save(ressemblance).block();

        int databaseSizeBeforeDelete = ressemblanceRepository.findAll().collectList().block().size();

        // Delete the ressemblance
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, ressemblance.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Ressemblance> ressemblanceList = ressemblanceRepository.findAll().collectList().block();
        assertThat(ressemblanceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
