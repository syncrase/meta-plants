package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.Allelopathie;
import fr.syncrase.ecosyst.repository.AllelopathieRepository;
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
 * Integration tests for the {@link AllelopathieResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class AllelopathieResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_IMPACT = -10;
    private static final Integer UPDATED_IMPACT = -9;

    private static final String ENTITY_API_URL = "/api/allelopathies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AllelopathieRepository allelopathieRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Allelopathie allelopathie;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Allelopathie createEntity(EntityManager em) {
        Allelopathie allelopathie = new Allelopathie().type(DEFAULT_TYPE).description(DEFAULT_DESCRIPTION).impact(DEFAULT_IMPACT);
        return allelopathie;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Allelopathie createUpdatedEntity(EntityManager em) {
        Allelopathie allelopathie = new Allelopathie().type(UPDATED_TYPE).description(UPDATED_DESCRIPTION).impact(UPDATED_IMPACT);
        return allelopathie;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Allelopathie.class).block();
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
        allelopathie = createEntity(em);
    }

    @Test
    void createAllelopathie() throws Exception {
        int databaseSizeBeforeCreate = allelopathieRepository.findAll().collectList().block().size();
        // Create the Allelopathie
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(allelopathie))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Allelopathie in the database
        List<Allelopathie> allelopathieList = allelopathieRepository.findAll().collectList().block();
        assertThat(allelopathieList).hasSize(databaseSizeBeforeCreate + 1);
        Allelopathie testAllelopathie = allelopathieList.get(allelopathieList.size() - 1);
        assertThat(testAllelopathie.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testAllelopathie.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAllelopathie.getImpact()).isEqualTo(DEFAULT_IMPACT);
    }

    @Test
    void createAllelopathieWithExistingId() throws Exception {
        // Create the Allelopathie with an existing ID
        allelopathie.setId(1L);

        int databaseSizeBeforeCreate = allelopathieRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(allelopathie))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Allelopathie in the database
        List<Allelopathie> allelopathieList = allelopathieRepository.findAll().collectList().block();
        assertThat(allelopathieList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = allelopathieRepository.findAll().collectList().block().size();
        // set the field null
        allelopathie.setType(null);

        // Create the Allelopathie, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(allelopathie))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Allelopathie> allelopathieList = allelopathieRepository.findAll().collectList().block();
        assertThat(allelopathieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllAllelopathies() {
        // Initialize the database
        allelopathieRepository.save(allelopathie).block();

        // Get all the allelopathieList
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
            .value(hasItem(allelopathie.getId().intValue()))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].impact")
            .value(hasItem(DEFAULT_IMPACT));
    }

    @Test
    void getAllelopathie() {
        // Initialize the database
        allelopathieRepository.save(allelopathie).block();

        // Get the allelopathie
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, allelopathie.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(allelopathie.getId().intValue()))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.impact")
            .value(is(DEFAULT_IMPACT));
    }

    @Test
    void getNonExistingAllelopathie() {
        // Get the allelopathie
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewAllelopathie() throws Exception {
        // Initialize the database
        allelopathieRepository.save(allelopathie).block();

        int databaseSizeBeforeUpdate = allelopathieRepository.findAll().collectList().block().size();

        // Update the allelopathie
        Allelopathie updatedAllelopathie = allelopathieRepository.findById(allelopathie.getId()).block();
        updatedAllelopathie.type(UPDATED_TYPE).description(UPDATED_DESCRIPTION).impact(UPDATED_IMPACT);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedAllelopathie.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedAllelopathie))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Allelopathie in the database
        List<Allelopathie> allelopathieList = allelopathieRepository.findAll().collectList().block();
        assertThat(allelopathieList).hasSize(databaseSizeBeforeUpdate);
        Allelopathie testAllelopathie = allelopathieList.get(allelopathieList.size() - 1);
        assertThat(testAllelopathie.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAllelopathie.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAllelopathie.getImpact()).isEqualTo(UPDATED_IMPACT);
    }

    @Test
    void putNonExistingAllelopathie() throws Exception {
        int databaseSizeBeforeUpdate = allelopathieRepository.findAll().collectList().block().size();
        allelopathie.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, allelopathie.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(allelopathie))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Allelopathie in the database
        List<Allelopathie> allelopathieList = allelopathieRepository.findAll().collectList().block();
        assertThat(allelopathieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAllelopathie() throws Exception {
        int databaseSizeBeforeUpdate = allelopathieRepository.findAll().collectList().block().size();
        allelopathie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(allelopathie))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Allelopathie in the database
        List<Allelopathie> allelopathieList = allelopathieRepository.findAll().collectList().block();
        assertThat(allelopathieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAllelopathie() throws Exception {
        int databaseSizeBeforeUpdate = allelopathieRepository.findAll().collectList().block().size();
        allelopathie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(allelopathie))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Allelopathie in the database
        List<Allelopathie> allelopathieList = allelopathieRepository.findAll().collectList().block();
        assertThat(allelopathieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAllelopathieWithPatch() throws Exception {
        // Initialize the database
        allelopathieRepository.save(allelopathie).block();

        int databaseSizeBeforeUpdate = allelopathieRepository.findAll().collectList().block().size();

        // Update the allelopathie using partial update
        Allelopathie partialUpdatedAllelopathie = new Allelopathie();
        partialUpdatedAllelopathie.setId(allelopathie.getId());

        partialUpdatedAllelopathie.type(UPDATED_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAllelopathie.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAllelopathie))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Allelopathie in the database
        List<Allelopathie> allelopathieList = allelopathieRepository.findAll().collectList().block();
        assertThat(allelopathieList).hasSize(databaseSizeBeforeUpdate);
        Allelopathie testAllelopathie = allelopathieList.get(allelopathieList.size() - 1);
        assertThat(testAllelopathie.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAllelopathie.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAllelopathie.getImpact()).isEqualTo(DEFAULT_IMPACT);
    }

    @Test
    void fullUpdateAllelopathieWithPatch() throws Exception {
        // Initialize the database
        allelopathieRepository.save(allelopathie).block();

        int databaseSizeBeforeUpdate = allelopathieRepository.findAll().collectList().block().size();

        // Update the allelopathie using partial update
        Allelopathie partialUpdatedAllelopathie = new Allelopathie();
        partialUpdatedAllelopathie.setId(allelopathie.getId());

        partialUpdatedAllelopathie.type(UPDATED_TYPE).description(UPDATED_DESCRIPTION).impact(UPDATED_IMPACT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAllelopathie.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAllelopathie))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Allelopathie in the database
        List<Allelopathie> allelopathieList = allelopathieRepository.findAll().collectList().block();
        assertThat(allelopathieList).hasSize(databaseSizeBeforeUpdate);
        Allelopathie testAllelopathie = allelopathieList.get(allelopathieList.size() - 1);
        assertThat(testAllelopathie.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAllelopathie.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAllelopathie.getImpact()).isEqualTo(UPDATED_IMPACT);
    }

    @Test
    void patchNonExistingAllelopathie() throws Exception {
        int databaseSizeBeforeUpdate = allelopathieRepository.findAll().collectList().block().size();
        allelopathie.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, allelopathie.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(allelopathie))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Allelopathie in the database
        List<Allelopathie> allelopathieList = allelopathieRepository.findAll().collectList().block();
        assertThat(allelopathieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAllelopathie() throws Exception {
        int databaseSizeBeforeUpdate = allelopathieRepository.findAll().collectList().block().size();
        allelopathie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(allelopathie))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Allelopathie in the database
        List<Allelopathie> allelopathieList = allelopathieRepository.findAll().collectList().block();
        assertThat(allelopathieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAllelopathie() throws Exception {
        int databaseSizeBeforeUpdate = allelopathieRepository.findAll().collectList().block().size();
        allelopathie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(allelopathie))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Allelopathie in the database
        List<Allelopathie> allelopathieList = allelopathieRepository.findAll().collectList().block();
        assertThat(allelopathieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAllelopathie() {
        // Initialize the database
        allelopathieRepository.save(allelopathie).block();

        int databaseSizeBeforeDelete = allelopathieRepository.findAll().collectList().block().size();

        // Delete the allelopathie
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, allelopathie.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Allelopathie> allelopathieList = allelopathieRepository.findAll().collectList().block();
        assertThat(allelopathieList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
