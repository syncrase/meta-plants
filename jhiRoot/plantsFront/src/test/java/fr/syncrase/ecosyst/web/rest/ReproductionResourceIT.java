package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.Reproduction;
import fr.syncrase.ecosyst.repository.ReproductionRepository;
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
 * Integration tests for the {@link ReproductionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class ReproductionResourceIT {

    private static final String DEFAULT_VITESSE = "AAAAAAAAAA";
    private static final String UPDATED_VITESSE = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/reproductions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReproductionRepository reproductionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Reproduction reproduction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reproduction createEntity(EntityManager em) {
        Reproduction reproduction = new Reproduction().vitesse(DEFAULT_VITESSE).type(DEFAULT_TYPE);
        return reproduction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reproduction createUpdatedEntity(EntityManager em) {
        Reproduction reproduction = new Reproduction().vitesse(UPDATED_VITESSE).type(UPDATED_TYPE);
        return reproduction;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Reproduction.class).block();
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
        reproduction = createEntity(em);
    }

    @Test
    void createReproduction() throws Exception {
        int databaseSizeBeforeCreate = reproductionRepository.findAll().collectList().block().size();
        // Create the Reproduction
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(reproduction))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Reproduction in the database
        List<Reproduction> reproductionList = reproductionRepository.findAll().collectList().block();
        assertThat(reproductionList).hasSize(databaseSizeBeforeCreate + 1);
        Reproduction testReproduction = reproductionList.get(reproductionList.size() - 1);
        assertThat(testReproduction.getVitesse()).isEqualTo(DEFAULT_VITESSE);
        assertThat(testReproduction.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    void createReproductionWithExistingId() throws Exception {
        // Create the Reproduction with an existing ID
        reproduction.setId(1L);

        int databaseSizeBeforeCreate = reproductionRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(reproduction))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Reproduction in the database
        List<Reproduction> reproductionList = reproductionRepository.findAll().collectList().block();
        assertThat(reproductionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllReproductions() {
        // Initialize the database
        reproductionRepository.save(reproduction).block();

        // Get all the reproductionList
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
            .value(hasItem(reproduction.getId().intValue()))
            .jsonPath("$.[*].vitesse")
            .value(hasItem(DEFAULT_VITESSE))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE));
    }

    @Test
    void getReproduction() {
        // Initialize the database
        reproductionRepository.save(reproduction).block();

        // Get the reproduction
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, reproduction.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(reproduction.getId().intValue()))
            .jsonPath("$.vitesse")
            .value(is(DEFAULT_VITESSE))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE));
    }

    @Test
    void getNonExistingReproduction() {
        // Get the reproduction
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewReproduction() throws Exception {
        // Initialize the database
        reproductionRepository.save(reproduction).block();

        int databaseSizeBeforeUpdate = reproductionRepository.findAll().collectList().block().size();

        // Update the reproduction
        Reproduction updatedReproduction = reproductionRepository.findById(reproduction.getId()).block();
        updatedReproduction.vitesse(UPDATED_VITESSE).type(UPDATED_TYPE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedReproduction.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedReproduction))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Reproduction in the database
        List<Reproduction> reproductionList = reproductionRepository.findAll().collectList().block();
        assertThat(reproductionList).hasSize(databaseSizeBeforeUpdate);
        Reproduction testReproduction = reproductionList.get(reproductionList.size() - 1);
        assertThat(testReproduction.getVitesse()).isEqualTo(UPDATED_VITESSE);
        assertThat(testReproduction.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    void putNonExistingReproduction() throws Exception {
        int databaseSizeBeforeUpdate = reproductionRepository.findAll().collectList().block().size();
        reproduction.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, reproduction.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(reproduction))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Reproduction in the database
        List<Reproduction> reproductionList = reproductionRepository.findAll().collectList().block();
        assertThat(reproductionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchReproduction() throws Exception {
        int databaseSizeBeforeUpdate = reproductionRepository.findAll().collectList().block().size();
        reproduction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(reproduction))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Reproduction in the database
        List<Reproduction> reproductionList = reproductionRepository.findAll().collectList().block();
        assertThat(reproductionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamReproduction() throws Exception {
        int databaseSizeBeforeUpdate = reproductionRepository.findAll().collectList().block().size();
        reproduction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(reproduction))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Reproduction in the database
        List<Reproduction> reproductionList = reproductionRepository.findAll().collectList().block();
        assertThat(reproductionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateReproductionWithPatch() throws Exception {
        // Initialize the database
        reproductionRepository.save(reproduction).block();

        int databaseSizeBeforeUpdate = reproductionRepository.findAll().collectList().block().size();

        // Update the reproduction using partial update
        Reproduction partialUpdatedReproduction = new Reproduction();
        partialUpdatedReproduction.setId(reproduction.getId());

        partialUpdatedReproduction.type(UPDATED_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedReproduction.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedReproduction))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Reproduction in the database
        List<Reproduction> reproductionList = reproductionRepository.findAll().collectList().block();
        assertThat(reproductionList).hasSize(databaseSizeBeforeUpdate);
        Reproduction testReproduction = reproductionList.get(reproductionList.size() - 1);
        assertThat(testReproduction.getVitesse()).isEqualTo(DEFAULT_VITESSE);
        assertThat(testReproduction.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    void fullUpdateReproductionWithPatch() throws Exception {
        // Initialize the database
        reproductionRepository.save(reproduction).block();

        int databaseSizeBeforeUpdate = reproductionRepository.findAll().collectList().block().size();

        // Update the reproduction using partial update
        Reproduction partialUpdatedReproduction = new Reproduction();
        partialUpdatedReproduction.setId(reproduction.getId());

        partialUpdatedReproduction.vitesse(UPDATED_VITESSE).type(UPDATED_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedReproduction.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedReproduction))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Reproduction in the database
        List<Reproduction> reproductionList = reproductionRepository.findAll().collectList().block();
        assertThat(reproductionList).hasSize(databaseSizeBeforeUpdate);
        Reproduction testReproduction = reproductionList.get(reproductionList.size() - 1);
        assertThat(testReproduction.getVitesse()).isEqualTo(UPDATED_VITESSE);
        assertThat(testReproduction.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    void patchNonExistingReproduction() throws Exception {
        int databaseSizeBeforeUpdate = reproductionRepository.findAll().collectList().block().size();
        reproduction.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, reproduction.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(reproduction))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Reproduction in the database
        List<Reproduction> reproductionList = reproductionRepository.findAll().collectList().block();
        assertThat(reproductionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchReproduction() throws Exception {
        int databaseSizeBeforeUpdate = reproductionRepository.findAll().collectList().block().size();
        reproduction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(reproduction))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Reproduction in the database
        List<Reproduction> reproductionList = reproductionRepository.findAll().collectList().block();
        assertThat(reproductionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamReproduction() throws Exception {
        int databaseSizeBeforeUpdate = reproductionRepository.findAll().collectList().block().size();
        reproduction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(reproduction))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Reproduction in the database
        List<Reproduction> reproductionList = reproductionRepository.findAll().collectList().block();
        assertThat(reproductionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteReproduction() {
        // Initialize the database
        reproductionRepository.save(reproduction).block();

        int databaseSizeBeforeDelete = reproductionRepository.findAll().collectList().block().size();

        // Delete the reproduction
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, reproduction.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Reproduction> reproductionList = reproductionRepository.findAll().collectList().block();
        assertThat(reproductionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
