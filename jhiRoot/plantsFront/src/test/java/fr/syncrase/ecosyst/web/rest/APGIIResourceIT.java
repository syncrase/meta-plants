package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.APGII;
import fr.syncrase.ecosyst.repository.APGIIRepository;
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
 * Integration tests for the {@link APGIIResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class APGIIResourceIT {

    private static final String DEFAULT_ORDRE = "AAAAAAAAAA";
    private static final String UPDATED_ORDRE = "BBBBBBBBBB";

    private static final String DEFAULT_FAMILLE = "AAAAAAAAAA";
    private static final String UPDATED_FAMILLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/apgiis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private APGIIRepository aPGIIRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private APGII aPGII;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static APGII createEntity(EntityManager em) {
        APGII aPGII = new APGII().ordre(DEFAULT_ORDRE).famille(DEFAULT_FAMILLE);
        return aPGII;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static APGII createUpdatedEntity(EntityManager em) {
        APGII aPGII = new APGII().ordre(UPDATED_ORDRE).famille(UPDATED_FAMILLE);
        return aPGII;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(APGII.class).block();
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
        aPGII = createEntity(em);
    }

    @Test
    void createAPGII() throws Exception {
        int databaseSizeBeforeCreate = aPGIIRepository.findAll().collectList().block().size();
        // Create the APGII
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(aPGII))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the APGII in the database
        List<APGII> aPGIIList = aPGIIRepository.findAll().collectList().block();
        assertThat(aPGIIList).hasSize(databaseSizeBeforeCreate + 1);
        APGII testAPGII = aPGIIList.get(aPGIIList.size() - 1);
        assertThat(testAPGII.getOrdre()).isEqualTo(DEFAULT_ORDRE);
        assertThat(testAPGII.getFamille()).isEqualTo(DEFAULT_FAMILLE);
    }

    @Test
    void createAPGIIWithExistingId() throws Exception {
        // Create the APGII with an existing ID
        aPGII.setId(1L);

        int databaseSizeBeforeCreate = aPGIIRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(aPGII))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the APGII in the database
        List<APGII> aPGIIList = aPGIIRepository.findAll().collectList().block();
        assertThat(aPGIIList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkOrdreIsRequired() throws Exception {
        int databaseSizeBeforeTest = aPGIIRepository.findAll().collectList().block().size();
        // set the field null
        aPGII.setOrdre(null);

        // Create the APGII, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(aPGII))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<APGII> aPGIIList = aPGIIRepository.findAll().collectList().block();
        assertThat(aPGIIList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkFamilleIsRequired() throws Exception {
        int databaseSizeBeforeTest = aPGIIRepository.findAll().collectList().block().size();
        // set the field null
        aPGII.setFamille(null);

        // Create the APGII, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(aPGII))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<APGII> aPGIIList = aPGIIRepository.findAll().collectList().block();
        assertThat(aPGIIList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllAPGIIS() {
        // Initialize the database
        aPGIIRepository.save(aPGII).block();

        // Get all the aPGIIList
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
            .value(hasItem(aPGII.getId().intValue()))
            .jsonPath("$.[*].ordre")
            .value(hasItem(DEFAULT_ORDRE))
            .jsonPath("$.[*].famille")
            .value(hasItem(DEFAULT_FAMILLE));
    }

    @Test
    void getAPGII() {
        // Initialize the database
        aPGIIRepository.save(aPGII).block();

        // Get the aPGII
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, aPGII.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(aPGII.getId().intValue()))
            .jsonPath("$.ordre")
            .value(is(DEFAULT_ORDRE))
            .jsonPath("$.famille")
            .value(is(DEFAULT_FAMILLE));
    }

    @Test
    void getNonExistingAPGII() {
        // Get the aPGII
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewAPGII() throws Exception {
        // Initialize the database
        aPGIIRepository.save(aPGII).block();

        int databaseSizeBeforeUpdate = aPGIIRepository.findAll().collectList().block().size();

        // Update the aPGII
        APGII updatedAPGII = aPGIIRepository.findById(aPGII.getId()).block();
        updatedAPGII.ordre(UPDATED_ORDRE).famille(UPDATED_FAMILLE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedAPGII.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedAPGII))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the APGII in the database
        List<APGII> aPGIIList = aPGIIRepository.findAll().collectList().block();
        assertThat(aPGIIList).hasSize(databaseSizeBeforeUpdate);
        APGII testAPGII = aPGIIList.get(aPGIIList.size() - 1);
        assertThat(testAPGII.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testAPGII.getFamille()).isEqualTo(UPDATED_FAMILLE);
    }

    @Test
    void putNonExistingAPGII() throws Exception {
        int databaseSizeBeforeUpdate = aPGIIRepository.findAll().collectList().block().size();
        aPGII.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, aPGII.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(aPGII))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the APGII in the database
        List<APGII> aPGIIList = aPGIIRepository.findAll().collectList().block();
        assertThat(aPGIIList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAPGII() throws Exception {
        int databaseSizeBeforeUpdate = aPGIIRepository.findAll().collectList().block().size();
        aPGII.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(aPGII))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the APGII in the database
        List<APGII> aPGIIList = aPGIIRepository.findAll().collectList().block();
        assertThat(aPGIIList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAPGII() throws Exception {
        int databaseSizeBeforeUpdate = aPGIIRepository.findAll().collectList().block().size();
        aPGII.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(aPGII))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the APGII in the database
        List<APGII> aPGIIList = aPGIIRepository.findAll().collectList().block();
        assertThat(aPGIIList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAPGIIWithPatch() throws Exception {
        // Initialize the database
        aPGIIRepository.save(aPGII).block();

        int databaseSizeBeforeUpdate = aPGIIRepository.findAll().collectList().block().size();

        // Update the aPGII using partial update
        APGII partialUpdatedAPGII = new APGII();
        partialUpdatedAPGII.setId(aPGII.getId());

        partialUpdatedAPGII.ordre(UPDATED_ORDRE).famille(UPDATED_FAMILLE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAPGII.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAPGII))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the APGII in the database
        List<APGII> aPGIIList = aPGIIRepository.findAll().collectList().block();
        assertThat(aPGIIList).hasSize(databaseSizeBeforeUpdate);
        APGII testAPGII = aPGIIList.get(aPGIIList.size() - 1);
        assertThat(testAPGII.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testAPGII.getFamille()).isEqualTo(UPDATED_FAMILLE);
    }

    @Test
    void fullUpdateAPGIIWithPatch() throws Exception {
        // Initialize the database
        aPGIIRepository.save(aPGII).block();

        int databaseSizeBeforeUpdate = aPGIIRepository.findAll().collectList().block().size();

        // Update the aPGII using partial update
        APGII partialUpdatedAPGII = new APGII();
        partialUpdatedAPGII.setId(aPGII.getId());

        partialUpdatedAPGII.ordre(UPDATED_ORDRE).famille(UPDATED_FAMILLE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAPGII.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAPGII))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the APGII in the database
        List<APGII> aPGIIList = aPGIIRepository.findAll().collectList().block();
        assertThat(aPGIIList).hasSize(databaseSizeBeforeUpdate);
        APGII testAPGII = aPGIIList.get(aPGIIList.size() - 1);
        assertThat(testAPGII.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testAPGII.getFamille()).isEqualTo(UPDATED_FAMILLE);
    }

    @Test
    void patchNonExistingAPGII() throws Exception {
        int databaseSizeBeforeUpdate = aPGIIRepository.findAll().collectList().block().size();
        aPGII.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, aPGII.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(aPGII))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the APGII in the database
        List<APGII> aPGIIList = aPGIIRepository.findAll().collectList().block();
        assertThat(aPGIIList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAPGII() throws Exception {
        int databaseSizeBeforeUpdate = aPGIIRepository.findAll().collectList().block().size();
        aPGII.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(aPGII))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the APGII in the database
        List<APGII> aPGIIList = aPGIIRepository.findAll().collectList().block();
        assertThat(aPGIIList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAPGII() throws Exception {
        int databaseSizeBeforeUpdate = aPGIIRepository.findAll().collectList().block().size();
        aPGII.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(aPGII))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the APGII in the database
        List<APGII> aPGIIList = aPGIIRepository.findAll().collectList().block();
        assertThat(aPGIIList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAPGII() {
        // Initialize the database
        aPGIIRepository.save(aPGII).block();

        int databaseSizeBeforeDelete = aPGIIRepository.findAll().collectList().block().size();

        // Delete the aPGII
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, aPGII.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<APGII> aPGIIList = aPGIIRepository.findAll().collectList().block();
        assertThat(aPGIIList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
