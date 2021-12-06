package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.APGIV;
import fr.syncrase.ecosyst.repository.APGIVRepository;
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
 * Integration tests for the {@link APGIVResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class APGIVResourceIT {

    private static final String DEFAULT_ORDRE = "AAAAAAAAAA";
    private static final String UPDATED_ORDRE = "BBBBBBBBBB";

    private static final String DEFAULT_FAMILLE = "AAAAAAAAAA";
    private static final String UPDATED_FAMILLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/apgivs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private APGIVRepository aPGIVRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private APGIV aPGIV;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static APGIV createEntity(EntityManager em) {
        APGIV aPGIV = new APGIV().ordre(DEFAULT_ORDRE).famille(DEFAULT_FAMILLE);
        return aPGIV;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static APGIV createUpdatedEntity(EntityManager em) {
        APGIV aPGIV = new APGIV().ordre(UPDATED_ORDRE).famille(UPDATED_FAMILLE);
        return aPGIV;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(APGIV.class).block();
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
        aPGIV = createEntity(em);
    }

    @Test
    void createAPGIV() throws Exception {
        int databaseSizeBeforeCreate = aPGIVRepository.findAll().collectList().block().size();
        // Create the APGIV
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(aPGIV))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the APGIV in the database
        List<APGIV> aPGIVList = aPGIVRepository.findAll().collectList().block();
        assertThat(aPGIVList).hasSize(databaseSizeBeforeCreate + 1);
        APGIV testAPGIV = aPGIVList.get(aPGIVList.size() - 1);
        assertThat(testAPGIV.getOrdre()).isEqualTo(DEFAULT_ORDRE);
        assertThat(testAPGIV.getFamille()).isEqualTo(DEFAULT_FAMILLE);
    }

    @Test
    void createAPGIVWithExistingId() throws Exception {
        // Create the APGIV with an existing ID
        aPGIV.setId(1L);

        int databaseSizeBeforeCreate = aPGIVRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(aPGIV))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the APGIV in the database
        List<APGIV> aPGIVList = aPGIVRepository.findAll().collectList().block();
        assertThat(aPGIVList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkOrdreIsRequired() throws Exception {
        int databaseSizeBeforeTest = aPGIVRepository.findAll().collectList().block().size();
        // set the field null
        aPGIV.setOrdre(null);

        // Create the APGIV, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(aPGIV))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<APGIV> aPGIVList = aPGIVRepository.findAll().collectList().block();
        assertThat(aPGIVList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkFamilleIsRequired() throws Exception {
        int databaseSizeBeforeTest = aPGIVRepository.findAll().collectList().block().size();
        // set the field null
        aPGIV.setFamille(null);

        // Create the APGIV, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(aPGIV))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<APGIV> aPGIVList = aPGIVRepository.findAll().collectList().block();
        assertThat(aPGIVList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllAPGIVS() {
        // Initialize the database
        aPGIVRepository.save(aPGIV).block();

        // Get all the aPGIVList
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
            .value(hasItem(aPGIV.getId().intValue()))
            .jsonPath("$.[*].ordre")
            .value(hasItem(DEFAULT_ORDRE))
            .jsonPath("$.[*].famille")
            .value(hasItem(DEFAULT_FAMILLE));
    }

    @Test
    void getAPGIV() {
        // Initialize the database
        aPGIVRepository.save(aPGIV).block();

        // Get the aPGIV
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, aPGIV.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(aPGIV.getId().intValue()))
            .jsonPath("$.ordre")
            .value(is(DEFAULT_ORDRE))
            .jsonPath("$.famille")
            .value(is(DEFAULT_FAMILLE));
    }

    @Test
    void getNonExistingAPGIV() {
        // Get the aPGIV
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewAPGIV() throws Exception {
        // Initialize the database
        aPGIVRepository.save(aPGIV).block();

        int databaseSizeBeforeUpdate = aPGIVRepository.findAll().collectList().block().size();

        // Update the aPGIV
        APGIV updatedAPGIV = aPGIVRepository.findById(aPGIV.getId()).block();
        updatedAPGIV.ordre(UPDATED_ORDRE).famille(UPDATED_FAMILLE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedAPGIV.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedAPGIV))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the APGIV in the database
        List<APGIV> aPGIVList = aPGIVRepository.findAll().collectList().block();
        assertThat(aPGIVList).hasSize(databaseSizeBeforeUpdate);
        APGIV testAPGIV = aPGIVList.get(aPGIVList.size() - 1);
        assertThat(testAPGIV.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testAPGIV.getFamille()).isEqualTo(UPDATED_FAMILLE);
    }

    @Test
    void putNonExistingAPGIV() throws Exception {
        int databaseSizeBeforeUpdate = aPGIVRepository.findAll().collectList().block().size();
        aPGIV.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, aPGIV.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(aPGIV))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the APGIV in the database
        List<APGIV> aPGIVList = aPGIVRepository.findAll().collectList().block();
        assertThat(aPGIVList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAPGIV() throws Exception {
        int databaseSizeBeforeUpdate = aPGIVRepository.findAll().collectList().block().size();
        aPGIV.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(aPGIV))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the APGIV in the database
        List<APGIV> aPGIVList = aPGIVRepository.findAll().collectList().block();
        assertThat(aPGIVList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAPGIV() throws Exception {
        int databaseSizeBeforeUpdate = aPGIVRepository.findAll().collectList().block().size();
        aPGIV.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(aPGIV))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the APGIV in the database
        List<APGIV> aPGIVList = aPGIVRepository.findAll().collectList().block();
        assertThat(aPGIVList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAPGIVWithPatch() throws Exception {
        // Initialize the database
        aPGIVRepository.save(aPGIV).block();

        int databaseSizeBeforeUpdate = aPGIVRepository.findAll().collectList().block().size();

        // Update the aPGIV using partial update
        APGIV partialUpdatedAPGIV = new APGIV();
        partialUpdatedAPGIV.setId(aPGIV.getId());

        partialUpdatedAPGIV.famille(UPDATED_FAMILLE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAPGIV.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAPGIV))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the APGIV in the database
        List<APGIV> aPGIVList = aPGIVRepository.findAll().collectList().block();
        assertThat(aPGIVList).hasSize(databaseSizeBeforeUpdate);
        APGIV testAPGIV = aPGIVList.get(aPGIVList.size() - 1);
        assertThat(testAPGIV.getOrdre()).isEqualTo(DEFAULT_ORDRE);
        assertThat(testAPGIV.getFamille()).isEqualTo(UPDATED_FAMILLE);
    }

    @Test
    void fullUpdateAPGIVWithPatch() throws Exception {
        // Initialize the database
        aPGIVRepository.save(aPGIV).block();

        int databaseSizeBeforeUpdate = aPGIVRepository.findAll().collectList().block().size();

        // Update the aPGIV using partial update
        APGIV partialUpdatedAPGIV = new APGIV();
        partialUpdatedAPGIV.setId(aPGIV.getId());

        partialUpdatedAPGIV.ordre(UPDATED_ORDRE).famille(UPDATED_FAMILLE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAPGIV.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAPGIV))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the APGIV in the database
        List<APGIV> aPGIVList = aPGIVRepository.findAll().collectList().block();
        assertThat(aPGIVList).hasSize(databaseSizeBeforeUpdate);
        APGIV testAPGIV = aPGIVList.get(aPGIVList.size() - 1);
        assertThat(testAPGIV.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testAPGIV.getFamille()).isEqualTo(UPDATED_FAMILLE);
    }

    @Test
    void patchNonExistingAPGIV() throws Exception {
        int databaseSizeBeforeUpdate = aPGIVRepository.findAll().collectList().block().size();
        aPGIV.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, aPGIV.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(aPGIV))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the APGIV in the database
        List<APGIV> aPGIVList = aPGIVRepository.findAll().collectList().block();
        assertThat(aPGIVList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAPGIV() throws Exception {
        int databaseSizeBeforeUpdate = aPGIVRepository.findAll().collectList().block().size();
        aPGIV.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(aPGIV))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the APGIV in the database
        List<APGIV> aPGIVList = aPGIVRepository.findAll().collectList().block();
        assertThat(aPGIVList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAPGIV() throws Exception {
        int databaseSizeBeforeUpdate = aPGIVRepository.findAll().collectList().block().size();
        aPGIV.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(aPGIV))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the APGIV in the database
        List<APGIV> aPGIVList = aPGIVRepository.findAll().collectList().block();
        assertThat(aPGIVList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAPGIV() {
        // Initialize the database
        aPGIVRepository.save(aPGIV).block();

        int databaseSizeBeforeDelete = aPGIVRepository.findAll().collectList().block().size();

        // Delete the aPGIV
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, aPGIV.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<APGIV> aPGIVList = aPGIVRepository.findAll().collectList().block();
        assertThat(aPGIVList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
