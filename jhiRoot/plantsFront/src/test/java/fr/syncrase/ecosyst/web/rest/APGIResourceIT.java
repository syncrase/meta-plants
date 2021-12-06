package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.APGI;
import fr.syncrase.ecosyst.repository.APGIRepository;
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
 * Integration tests for the {@link APGIResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class APGIResourceIT {

    private static final String DEFAULT_ORDRE = "AAAAAAAAAA";
    private static final String UPDATED_ORDRE = "BBBBBBBBBB";

    private static final String DEFAULT_FAMILLE = "AAAAAAAAAA";
    private static final String UPDATED_FAMILLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/apgis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private APGIRepository aPGIRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private APGI aPGI;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static APGI createEntity(EntityManager em) {
        APGI aPGI = new APGI().ordre(DEFAULT_ORDRE).famille(DEFAULT_FAMILLE);
        return aPGI;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static APGI createUpdatedEntity(EntityManager em) {
        APGI aPGI = new APGI().ordre(UPDATED_ORDRE).famille(UPDATED_FAMILLE);
        return aPGI;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(APGI.class).block();
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
        aPGI = createEntity(em);
    }

    @Test
    void createAPGI() throws Exception {
        int databaseSizeBeforeCreate = aPGIRepository.findAll().collectList().block().size();
        // Create the APGI
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(aPGI))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the APGI in the database
        List<APGI> aPGIList = aPGIRepository.findAll().collectList().block();
        assertThat(aPGIList).hasSize(databaseSizeBeforeCreate + 1);
        APGI testAPGI = aPGIList.get(aPGIList.size() - 1);
        assertThat(testAPGI.getOrdre()).isEqualTo(DEFAULT_ORDRE);
        assertThat(testAPGI.getFamille()).isEqualTo(DEFAULT_FAMILLE);
    }

    @Test
    void createAPGIWithExistingId() throws Exception {
        // Create the APGI with an existing ID
        aPGI.setId(1L);

        int databaseSizeBeforeCreate = aPGIRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(aPGI))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the APGI in the database
        List<APGI> aPGIList = aPGIRepository.findAll().collectList().block();
        assertThat(aPGIList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkOrdreIsRequired() throws Exception {
        int databaseSizeBeforeTest = aPGIRepository.findAll().collectList().block().size();
        // set the field null
        aPGI.setOrdre(null);

        // Create the APGI, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(aPGI))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<APGI> aPGIList = aPGIRepository.findAll().collectList().block();
        assertThat(aPGIList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkFamilleIsRequired() throws Exception {
        int databaseSizeBeforeTest = aPGIRepository.findAll().collectList().block().size();
        // set the field null
        aPGI.setFamille(null);

        // Create the APGI, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(aPGI))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<APGI> aPGIList = aPGIRepository.findAll().collectList().block();
        assertThat(aPGIList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllAPGIS() {
        // Initialize the database
        aPGIRepository.save(aPGI).block();

        // Get all the aPGIList
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
            .value(hasItem(aPGI.getId().intValue()))
            .jsonPath("$.[*].ordre")
            .value(hasItem(DEFAULT_ORDRE))
            .jsonPath("$.[*].famille")
            .value(hasItem(DEFAULT_FAMILLE));
    }

    @Test
    void getAPGI() {
        // Initialize the database
        aPGIRepository.save(aPGI).block();

        // Get the aPGI
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, aPGI.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(aPGI.getId().intValue()))
            .jsonPath("$.ordre")
            .value(is(DEFAULT_ORDRE))
            .jsonPath("$.famille")
            .value(is(DEFAULT_FAMILLE));
    }

    @Test
    void getNonExistingAPGI() {
        // Get the aPGI
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewAPGI() throws Exception {
        // Initialize the database
        aPGIRepository.save(aPGI).block();

        int databaseSizeBeforeUpdate = aPGIRepository.findAll().collectList().block().size();

        // Update the aPGI
        APGI updatedAPGI = aPGIRepository.findById(aPGI.getId()).block();
        updatedAPGI.ordre(UPDATED_ORDRE).famille(UPDATED_FAMILLE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedAPGI.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedAPGI))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the APGI in the database
        List<APGI> aPGIList = aPGIRepository.findAll().collectList().block();
        assertThat(aPGIList).hasSize(databaseSizeBeforeUpdate);
        APGI testAPGI = aPGIList.get(aPGIList.size() - 1);
        assertThat(testAPGI.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testAPGI.getFamille()).isEqualTo(UPDATED_FAMILLE);
    }

    @Test
    void putNonExistingAPGI() throws Exception {
        int databaseSizeBeforeUpdate = aPGIRepository.findAll().collectList().block().size();
        aPGI.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, aPGI.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(aPGI))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the APGI in the database
        List<APGI> aPGIList = aPGIRepository.findAll().collectList().block();
        assertThat(aPGIList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAPGI() throws Exception {
        int databaseSizeBeforeUpdate = aPGIRepository.findAll().collectList().block().size();
        aPGI.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(aPGI))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the APGI in the database
        List<APGI> aPGIList = aPGIRepository.findAll().collectList().block();
        assertThat(aPGIList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAPGI() throws Exception {
        int databaseSizeBeforeUpdate = aPGIRepository.findAll().collectList().block().size();
        aPGI.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(aPGI))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the APGI in the database
        List<APGI> aPGIList = aPGIRepository.findAll().collectList().block();
        assertThat(aPGIList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAPGIWithPatch() throws Exception {
        // Initialize the database
        aPGIRepository.save(aPGI).block();

        int databaseSizeBeforeUpdate = aPGIRepository.findAll().collectList().block().size();

        // Update the aPGI using partial update
        APGI partialUpdatedAPGI = new APGI();
        partialUpdatedAPGI.setId(aPGI.getId());

        partialUpdatedAPGI.ordre(UPDATED_ORDRE).famille(UPDATED_FAMILLE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAPGI.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAPGI))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the APGI in the database
        List<APGI> aPGIList = aPGIRepository.findAll().collectList().block();
        assertThat(aPGIList).hasSize(databaseSizeBeforeUpdate);
        APGI testAPGI = aPGIList.get(aPGIList.size() - 1);
        assertThat(testAPGI.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testAPGI.getFamille()).isEqualTo(UPDATED_FAMILLE);
    }

    @Test
    void fullUpdateAPGIWithPatch() throws Exception {
        // Initialize the database
        aPGIRepository.save(aPGI).block();

        int databaseSizeBeforeUpdate = aPGIRepository.findAll().collectList().block().size();

        // Update the aPGI using partial update
        APGI partialUpdatedAPGI = new APGI();
        partialUpdatedAPGI.setId(aPGI.getId());

        partialUpdatedAPGI.ordre(UPDATED_ORDRE).famille(UPDATED_FAMILLE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAPGI.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAPGI))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the APGI in the database
        List<APGI> aPGIList = aPGIRepository.findAll().collectList().block();
        assertThat(aPGIList).hasSize(databaseSizeBeforeUpdate);
        APGI testAPGI = aPGIList.get(aPGIList.size() - 1);
        assertThat(testAPGI.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testAPGI.getFamille()).isEqualTo(UPDATED_FAMILLE);
    }

    @Test
    void patchNonExistingAPGI() throws Exception {
        int databaseSizeBeforeUpdate = aPGIRepository.findAll().collectList().block().size();
        aPGI.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, aPGI.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(aPGI))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the APGI in the database
        List<APGI> aPGIList = aPGIRepository.findAll().collectList().block();
        assertThat(aPGIList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAPGI() throws Exception {
        int databaseSizeBeforeUpdate = aPGIRepository.findAll().collectList().block().size();
        aPGI.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(aPGI))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the APGI in the database
        List<APGI> aPGIList = aPGIRepository.findAll().collectList().block();
        assertThat(aPGIList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAPGI() throws Exception {
        int databaseSizeBeforeUpdate = aPGIRepository.findAll().collectList().block().size();
        aPGI.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(aPGI))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the APGI in the database
        List<APGI> aPGIList = aPGIRepository.findAll().collectList().block();
        assertThat(aPGIList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAPGI() {
        // Initialize the database
        aPGIRepository.save(aPGI).block();

        int databaseSizeBeforeDelete = aPGIRepository.findAll().collectList().block().size();

        // Delete the aPGI
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, aPGI.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<APGI> aPGIList = aPGIRepository.findAll().collectList().block();
        assertThat(aPGIList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
