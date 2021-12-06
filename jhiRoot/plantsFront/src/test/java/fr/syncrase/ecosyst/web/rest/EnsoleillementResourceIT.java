package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.Ensoleillement;
import fr.syncrase.ecosyst.repository.EnsoleillementRepository;
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
 * Integration tests for the {@link EnsoleillementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class EnsoleillementResourceIT {

    private static final String DEFAULT_ORIENTATION = "AAAAAAAAAA";
    private static final String UPDATED_ORIENTATION = "BBBBBBBBBB";

    private static final Double DEFAULT_ENSOLEILEMENT = 1D;
    private static final Double UPDATED_ENSOLEILEMENT = 2D;

    private static final String ENTITY_API_URL = "/api/ensoleillements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EnsoleillementRepository ensoleillementRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Ensoleillement ensoleillement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ensoleillement createEntity(EntityManager em) {
        Ensoleillement ensoleillement = new Ensoleillement().orientation(DEFAULT_ORIENTATION).ensoleilement(DEFAULT_ENSOLEILEMENT);
        return ensoleillement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ensoleillement createUpdatedEntity(EntityManager em) {
        Ensoleillement ensoleillement = new Ensoleillement().orientation(UPDATED_ORIENTATION).ensoleilement(UPDATED_ENSOLEILEMENT);
        return ensoleillement;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Ensoleillement.class).block();
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
        ensoleillement = createEntity(em);
    }

    @Test
    void createEnsoleillement() throws Exception {
        int databaseSizeBeforeCreate = ensoleillementRepository.findAll().collectList().block().size();
        // Create the Ensoleillement
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ensoleillement))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Ensoleillement in the database
        List<Ensoleillement> ensoleillementList = ensoleillementRepository.findAll().collectList().block();
        assertThat(ensoleillementList).hasSize(databaseSizeBeforeCreate + 1);
        Ensoleillement testEnsoleillement = ensoleillementList.get(ensoleillementList.size() - 1);
        assertThat(testEnsoleillement.getOrientation()).isEqualTo(DEFAULT_ORIENTATION);
        assertThat(testEnsoleillement.getEnsoleilement()).isEqualTo(DEFAULT_ENSOLEILEMENT);
    }

    @Test
    void createEnsoleillementWithExistingId() throws Exception {
        // Create the Ensoleillement with an existing ID
        ensoleillement.setId(1L);

        int databaseSizeBeforeCreate = ensoleillementRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ensoleillement))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Ensoleillement in the database
        List<Ensoleillement> ensoleillementList = ensoleillementRepository.findAll().collectList().block();
        assertThat(ensoleillementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllEnsoleillements() {
        // Initialize the database
        ensoleillementRepository.save(ensoleillement).block();

        // Get all the ensoleillementList
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
            .value(hasItem(ensoleillement.getId().intValue()))
            .jsonPath("$.[*].orientation")
            .value(hasItem(DEFAULT_ORIENTATION))
            .jsonPath("$.[*].ensoleilement")
            .value(hasItem(DEFAULT_ENSOLEILEMENT.doubleValue()));
    }

    @Test
    void getEnsoleillement() {
        // Initialize the database
        ensoleillementRepository.save(ensoleillement).block();

        // Get the ensoleillement
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, ensoleillement.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(ensoleillement.getId().intValue()))
            .jsonPath("$.orientation")
            .value(is(DEFAULT_ORIENTATION))
            .jsonPath("$.ensoleilement")
            .value(is(DEFAULT_ENSOLEILEMENT.doubleValue()));
    }

    @Test
    void getNonExistingEnsoleillement() {
        // Get the ensoleillement
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewEnsoleillement() throws Exception {
        // Initialize the database
        ensoleillementRepository.save(ensoleillement).block();

        int databaseSizeBeforeUpdate = ensoleillementRepository.findAll().collectList().block().size();

        // Update the ensoleillement
        Ensoleillement updatedEnsoleillement = ensoleillementRepository.findById(ensoleillement.getId()).block();
        updatedEnsoleillement.orientation(UPDATED_ORIENTATION).ensoleilement(UPDATED_ENSOLEILEMENT);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedEnsoleillement.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedEnsoleillement))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Ensoleillement in the database
        List<Ensoleillement> ensoleillementList = ensoleillementRepository.findAll().collectList().block();
        assertThat(ensoleillementList).hasSize(databaseSizeBeforeUpdate);
        Ensoleillement testEnsoleillement = ensoleillementList.get(ensoleillementList.size() - 1);
        assertThat(testEnsoleillement.getOrientation()).isEqualTo(UPDATED_ORIENTATION);
        assertThat(testEnsoleillement.getEnsoleilement()).isEqualTo(UPDATED_ENSOLEILEMENT);
    }

    @Test
    void putNonExistingEnsoleillement() throws Exception {
        int databaseSizeBeforeUpdate = ensoleillementRepository.findAll().collectList().block().size();
        ensoleillement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, ensoleillement.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ensoleillement))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Ensoleillement in the database
        List<Ensoleillement> ensoleillementList = ensoleillementRepository.findAll().collectList().block();
        assertThat(ensoleillementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchEnsoleillement() throws Exception {
        int databaseSizeBeforeUpdate = ensoleillementRepository.findAll().collectList().block().size();
        ensoleillement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ensoleillement))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Ensoleillement in the database
        List<Ensoleillement> ensoleillementList = ensoleillementRepository.findAll().collectList().block();
        assertThat(ensoleillementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamEnsoleillement() throws Exception {
        int databaseSizeBeforeUpdate = ensoleillementRepository.findAll().collectList().block().size();
        ensoleillement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(ensoleillement))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Ensoleillement in the database
        List<Ensoleillement> ensoleillementList = ensoleillementRepository.findAll().collectList().block();
        assertThat(ensoleillementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateEnsoleillementWithPatch() throws Exception {
        // Initialize the database
        ensoleillementRepository.save(ensoleillement).block();

        int databaseSizeBeforeUpdate = ensoleillementRepository.findAll().collectList().block().size();

        // Update the ensoleillement using partial update
        Ensoleillement partialUpdatedEnsoleillement = new Ensoleillement();
        partialUpdatedEnsoleillement.setId(ensoleillement.getId());

        partialUpdatedEnsoleillement.orientation(UPDATED_ORIENTATION).ensoleilement(UPDATED_ENSOLEILEMENT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEnsoleillement.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedEnsoleillement))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Ensoleillement in the database
        List<Ensoleillement> ensoleillementList = ensoleillementRepository.findAll().collectList().block();
        assertThat(ensoleillementList).hasSize(databaseSizeBeforeUpdate);
        Ensoleillement testEnsoleillement = ensoleillementList.get(ensoleillementList.size() - 1);
        assertThat(testEnsoleillement.getOrientation()).isEqualTo(UPDATED_ORIENTATION);
        assertThat(testEnsoleillement.getEnsoleilement()).isEqualTo(UPDATED_ENSOLEILEMENT);
    }

    @Test
    void fullUpdateEnsoleillementWithPatch() throws Exception {
        // Initialize the database
        ensoleillementRepository.save(ensoleillement).block();

        int databaseSizeBeforeUpdate = ensoleillementRepository.findAll().collectList().block().size();

        // Update the ensoleillement using partial update
        Ensoleillement partialUpdatedEnsoleillement = new Ensoleillement();
        partialUpdatedEnsoleillement.setId(ensoleillement.getId());

        partialUpdatedEnsoleillement.orientation(UPDATED_ORIENTATION).ensoleilement(UPDATED_ENSOLEILEMENT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEnsoleillement.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedEnsoleillement))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Ensoleillement in the database
        List<Ensoleillement> ensoleillementList = ensoleillementRepository.findAll().collectList().block();
        assertThat(ensoleillementList).hasSize(databaseSizeBeforeUpdate);
        Ensoleillement testEnsoleillement = ensoleillementList.get(ensoleillementList.size() - 1);
        assertThat(testEnsoleillement.getOrientation()).isEqualTo(UPDATED_ORIENTATION);
        assertThat(testEnsoleillement.getEnsoleilement()).isEqualTo(UPDATED_ENSOLEILEMENT);
    }

    @Test
    void patchNonExistingEnsoleillement() throws Exception {
        int databaseSizeBeforeUpdate = ensoleillementRepository.findAll().collectList().block().size();
        ensoleillement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, ensoleillement.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ensoleillement))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Ensoleillement in the database
        List<Ensoleillement> ensoleillementList = ensoleillementRepository.findAll().collectList().block();
        assertThat(ensoleillementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchEnsoleillement() throws Exception {
        int databaseSizeBeforeUpdate = ensoleillementRepository.findAll().collectList().block().size();
        ensoleillement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ensoleillement))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Ensoleillement in the database
        List<Ensoleillement> ensoleillementList = ensoleillementRepository.findAll().collectList().block();
        assertThat(ensoleillementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamEnsoleillement() throws Exception {
        int databaseSizeBeforeUpdate = ensoleillementRepository.findAll().collectList().block().size();
        ensoleillement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(ensoleillement))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Ensoleillement in the database
        List<Ensoleillement> ensoleillementList = ensoleillementRepository.findAll().collectList().block();
        assertThat(ensoleillementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteEnsoleillement() {
        // Initialize the database
        ensoleillementRepository.save(ensoleillement).block();

        int databaseSizeBeforeDelete = ensoleillementRepository.findAll().collectList().block().size();

        // Delete the ensoleillement
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, ensoleillement.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Ensoleillement> ensoleillementList = ensoleillementRepository.findAll().collectList().block();
        assertThat(ensoleillementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
