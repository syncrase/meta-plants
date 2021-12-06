package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.APGIII;
import fr.syncrase.ecosyst.repository.APGIIIRepository;
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
 * Integration tests for the {@link APGIIIResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class APGIIIResourceIT {

    private static final String DEFAULT_ORDRE = "AAAAAAAAAA";
    private static final String UPDATED_ORDRE = "BBBBBBBBBB";

    private static final String DEFAULT_FAMILLE = "AAAAAAAAAA";
    private static final String UPDATED_FAMILLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/apgiiis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private APGIIIRepository aPGIIIRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private APGIII aPGIII;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static APGIII createEntity(EntityManager em) {
        APGIII aPGIII = new APGIII().ordre(DEFAULT_ORDRE).famille(DEFAULT_FAMILLE);
        return aPGIII;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static APGIII createUpdatedEntity(EntityManager em) {
        APGIII aPGIII = new APGIII().ordre(UPDATED_ORDRE).famille(UPDATED_FAMILLE);
        return aPGIII;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(APGIII.class).block();
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
        aPGIII = createEntity(em);
    }

    @Test
    void createAPGIII() throws Exception {
        int databaseSizeBeforeCreate = aPGIIIRepository.findAll().collectList().block().size();
        // Create the APGIII
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(aPGIII))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the APGIII in the database
        List<APGIII> aPGIIIList = aPGIIIRepository.findAll().collectList().block();
        assertThat(aPGIIIList).hasSize(databaseSizeBeforeCreate + 1);
        APGIII testAPGIII = aPGIIIList.get(aPGIIIList.size() - 1);
        assertThat(testAPGIII.getOrdre()).isEqualTo(DEFAULT_ORDRE);
        assertThat(testAPGIII.getFamille()).isEqualTo(DEFAULT_FAMILLE);
    }

    @Test
    void createAPGIIIWithExistingId() throws Exception {
        // Create the APGIII with an existing ID
        aPGIII.setId(1L);

        int databaseSizeBeforeCreate = aPGIIIRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(aPGIII))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the APGIII in the database
        List<APGIII> aPGIIIList = aPGIIIRepository.findAll().collectList().block();
        assertThat(aPGIIIList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkOrdreIsRequired() throws Exception {
        int databaseSizeBeforeTest = aPGIIIRepository.findAll().collectList().block().size();
        // set the field null
        aPGIII.setOrdre(null);

        // Create the APGIII, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(aPGIII))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<APGIII> aPGIIIList = aPGIIIRepository.findAll().collectList().block();
        assertThat(aPGIIIList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkFamilleIsRequired() throws Exception {
        int databaseSizeBeforeTest = aPGIIIRepository.findAll().collectList().block().size();
        // set the field null
        aPGIII.setFamille(null);

        // Create the APGIII, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(aPGIII))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<APGIII> aPGIIIList = aPGIIIRepository.findAll().collectList().block();
        assertThat(aPGIIIList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllAPGIIIS() {
        // Initialize the database
        aPGIIIRepository.save(aPGIII).block();

        // Get all the aPGIIIList
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
            .value(hasItem(aPGIII.getId().intValue()))
            .jsonPath("$.[*].ordre")
            .value(hasItem(DEFAULT_ORDRE))
            .jsonPath("$.[*].famille")
            .value(hasItem(DEFAULT_FAMILLE));
    }

    @Test
    void getAPGIII() {
        // Initialize the database
        aPGIIIRepository.save(aPGIII).block();

        // Get the aPGIII
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, aPGIII.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(aPGIII.getId().intValue()))
            .jsonPath("$.ordre")
            .value(is(DEFAULT_ORDRE))
            .jsonPath("$.famille")
            .value(is(DEFAULT_FAMILLE));
    }

    @Test
    void getNonExistingAPGIII() {
        // Get the aPGIII
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewAPGIII() throws Exception {
        // Initialize the database
        aPGIIIRepository.save(aPGIII).block();

        int databaseSizeBeforeUpdate = aPGIIIRepository.findAll().collectList().block().size();

        // Update the aPGIII
        APGIII updatedAPGIII = aPGIIIRepository.findById(aPGIII.getId()).block();
        updatedAPGIII.ordre(UPDATED_ORDRE).famille(UPDATED_FAMILLE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedAPGIII.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedAPGIII))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the APGIII in the database
        List<APGIII> aPGIIIList = aPGIIIRepository.findAll().collectList().block();
        assertThat(aPGIIIList).hasSize(databaseSizeBeforeUpdate);
        APGIII testAPGIII = aPGIIIList.get(aPGIIIList.size() - 1);
        assertThat(testAPGIII.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testAPGIII.getFamille()).isEqualTo(UPDATED_FAMILLE);
    }

    @Test
    void putNonExistingAPGIII() throws Exception {
        int databaseSizeBeforeUpdate = aPGIIIRepository.findAll().collectList().block().size();
        aPGIII.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, aPGIII.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(aPGIII))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the APGIII in the database
        List<APGIII> aPGIIIList = aPGIIIRepository.findAll().collectList().block();
        assertThat(aPGIIIList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAPGIII() throws Exception {
        int databaseSizeBeforeUpdate = aPGIIIRepository.findAll().collectList().block().size();
        aPGIII.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(aPGIII))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the APGIII in the database
        List<APGIII> aPGIIIList = aPGIIIRepository.findAll().collectList().block();
        assertThat(aPGIIIList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAPGIII() throws Exception {
        int databaseSizeBeforeUpdate = aPGIIIRepository.findAll().collectList().block().size();
        aPGIII.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(aPGIII))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the APGIII in the database
        List<APGIII> aPGIIIList = aPGIIIRepository.findAll().collectList().block();
        assertThat(aPGIIIList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAPGIIIWithPatch() throws Exception {
        // Initialize the database
        aPGIIIRepository.save(aPGIII).block();

        int databaseSizeBeforeUpdate = aPGIIIRepository.findAll().collectList().block().size();

        // Update the aPGIII using partial update
        APGIII partialUpdatedAPGIII = new APGIII();
        partialUpdatedAPGIII.setId(aPGIII.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAPGIII.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAPGIII))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the APGIII in the database
        List<APGIII> aPGIIIList = aPGIIIRepository.findAll().collectList().block();
        assertThat(aPGIIIList).hasSize(databaseSizeBeforeUpdate);
        APGIII testAPGIII = aPGIIIList.get(aPGIIIList.size() - 1);
        assertThat(testAPGIII.getOrdre()).isEqualTo(DEFAULT_ORDRE);
        assertThat(testAPGIII.getFamille()).isEqualTo(DEFAULT_FAMILLE);
    }

    @Test
    void fullUpdateAPGIIIWithPatch() throws Exception {
        // Initialize the database
        aPGIIIRepository.save(aPGIII).block();

        int databaseSizeBeforeUpdate = aPGIIIRepository.findAll().collectList().block().size();

        // Update the aPGIII using partial update
        APGIII partialUpdatedAPGIII = new APGIII();
        partialUpdatedAPGIII.setId(aPGIII.getId());

        partialUpdatedAPGIII.ordre(UPDATED_ORDRE).famille(UPDATED_FAMILLE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAPGIII.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAPGIII))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the APGIII in the database
        List<APGIII> aPGIIIList = aPGIIIRepository.findAll().collectList().block();
        assertThat(aPGIIIList).hasSize(databaseSizeBeforeUpdate);
        APGIII testAPGIII = aPGIIIList.get(aPGIIIList.size() - 1);
        assertThat(testAPGIII.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testAPGIII.getFamille()).isEqualTo(UPDATED_FAMILLE);
    }

    @Test
    void patchNonExistingAPGIII() throws Exception {
        int databaseSizeBeforeUpdate = aPGIIIRepository.findAll().collectList().block().size();
        aPGIII.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, aPGIII.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(aPGIII))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the APGIII in the database
        List<APGIII> aPGIIIList = aPGIIIRepository.findAll().collectList().block();
        assertThat(aPGIIIList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAPGIII() throws Exception {
        int databaseSizeBeforeUpdate = aPGIIIRepository.findAll().collectList().block().size();
        aPGIII.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(aPGIII))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the APGIII in the database
        List<APGIII> aPGIIIList = aPGIIIRepository.findAll().collectList().block();
        assertThat(aPGIIIList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAPGIII() throws Exception {
        int databaseSizeBeforeUpdate = aPGIIIRepository.findAll().collectList().block().size();
        aPGIII.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(aPGIII))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the APGIII in the database
        List<APGIII> aPGIIIList = aPGIIIRepository.findAll().collectList().block();
        assertThat(aPGIIIList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAPGIII() {
        // Initialize the database
        aPGIIIRepository.save(aPGIII).block();

        int databaseSizeBeforeDelete = aPGIIIRepository.findAll().collectList().block().size();

        // Delete the aPGIII
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, aPGIII.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<APGIII> aPGIIIList = aPGIIIRepository.findAll().collectList().block();
        assertThat(aPGIIIList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
