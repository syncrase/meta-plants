package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.Mois;
import fr.syncrase.ecosyst.domain.PeriodeAnnee;
import fr.syncrase.ecosyst.repository.PeriodeAnneeRepository;
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
 * Integration tests for the {@link PeriodeAnneeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class PeriodeAnneeResourceIT {

    private static final String ENTITY_API_URL = "/api/periode-annees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PeriodeAnneeRepository periodeAnneeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private PeriodeAnnee periodeAnnee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PeriodeAnnee createEntity(EntityManager em) {
        PeriodeAnnee periodeAnnee = new PeriodeAnnee();
        // Add required entity
        Mois mois;
        mois = em.insert(MoisResourceIT.createEntity(em)).block();
        periodeAnnee.setDebut(mois);
        // Add required entity
        periodeAnnee.setFin(mois);
        return periodeAnnee;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PeriodeAnnee createUpdatedEntity(EntityManager em) {
        PeriodeAnnee periodeAnnee = new PeriodeAnnee();
        // Add required entity
        Mois mois;
        mois = em.insert(MoisResourceIT.createUpdatedEntity(em)).block();
        periodeAnnee.setDebut(mois);
        // Add required entity
        periodeAnnee.setFin(mois);
        return periodeAnnee;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(PeriodeAnnee.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        MoisResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        periodeAnnee = createEntity(em);
    }

    @Test
    void createPeriodeAnnee() throws Exception {
        int databaseSizeBeforeCreate = periodeAnneeRepository.findAll().collectList().block().size();
        // Create the PeriodeAnnee
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(periodeAnnee))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the PeriodeAnnee in the database
        List<PeriodeAnnee> periodeAnneeList = periodeAnneeRepository.findAll().collectList().block();
        assertThat(periodeAnneeList).hasSize(databaseSizeBeforeCreate + 1);
        PeriodeAnnee testPeriodeAnnee = periodeAnneeList.get(periodeAnneeList.size() - 1);
    }

    @Test
    void createPeriodeAnneeWithExistingId() throws Exception {
        // Create the PeriodeAnnee with an existing ID
        periodeAnnee.setId(1L);

        int databaseSizeBeforeCreate = periodeAnneeRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(periodeAnnee))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PeriodeAnnee in the database
        List<PeriodeAnnee> periodeAnneeList = periodeAnneeRepository.findAll().collectList().block();
        assertThat(periodeAnneeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllPeriodeAnnees() {
        // Initialize the database
        periodeAnneeRepository.save(periodeAnnee).block();

        // Get all the periodeAnneeList
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
            .value(hasItem(periodeAnnee.getId().intValue()));
    }

    @Test
    void getPeriodeAnnee() {
        // Initialize the database
        periodeAnneeRepository.save(periodeAnnee).block();

        // Get the periodeAnnee
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, periodeAnnee.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(periodeAnnee.getId().intValue()));
    }

    @Test
    void getNonExistingPeriodeAnnee() {
        // Get the periodeAnnee
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewPeriodeAnnee() throws Exception {
        // Initialize the database
        periodeAnneeRepository.save(periodeAnnee).block();

        int databaseSizeBeforeUpdate = periodeAnneeRepository.findAll().collectList().block().size();

        // Update the periodeAnnee
        PeriodeAnnee updatedPeriodeAnnee = periodeAnneeRepository.findById(periodeAnnee.getId()).block();

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedPeriodeAnnee.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedPeriodeAnnee))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PeriodeAnnee in the database
        List<PeriodeAnnee> periodeAnneeList = periodeAnneeRepository.findAll().collectList().block();
        assertThat(periodeAnneeList).hasSize(databaseSizeBeforeUpdate);
        PeriodeAnnee testPeriodeAnnee = periodeAnneeList.get(periodeAnneeList.size() - 1);
    }

    @Test
    void putNonExistingPeriodeAnnee() throws Exception {
        int databaseSizeBeforeUpdate = periodeAnneeRepository.findAll().collectList().block().size();
        periodeAnnee.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, periodeAnnee.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(periodeAnnee))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PeriodeAnnee in the database
        List<PeriodeAnnee> periodeAnneeList = periodeAnneeRepository.findAll().collectList().block();
        assertThat(periodeAnneeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPeriodeAnnee() throws Exception {
        int databaseSizeBeforeUpdate = periodeAnneeRepository.findAll().collectList().block().size();
        periodeAnnee.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(periodeAnnee))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PeriodeAnnee in the database
        List<PeriodeAnnee> periodeAnneeList = periodeAnneeRepository.findAll().collectList().block();
        assertThat(periodeAnneeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPeriodeAnnee() throws Exception {
        int databaseSizeBeforeUpdate = periodeAnneeRepository.findAll().collectList().block().size();
        periodeAnnee.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(periodeAnnee))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PeriodeAnnee in the database
        List<PeriodeAnnee> periodeAnneeList = periodeAnneeRepository.findAll().collectList().block();
        assertThat(periodeAnneeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePeriodeAnneeWithPatch() throws Exception {
        // Initialize the database
        periodeAnneeRepository.save(periodeAnnee).block();

        int databaseSizeBeforeUpdate = periodeAnneeRepository.findAll().collectList().block().size();

        // Update the periodeAnnee using partial update
        PeriodeAnnee partialUpdatedPeriodeAnnee = new PeriodeAnnee();
        partialUpdatedPeriodeAnnee.setId(periodeAnnee.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPeriodeAnnee.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPeriodeAnnee))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PeriodeAnnee in the database
        List<PeriodeAnnee> periodeAnneeList = periodeAnneeRepository.findAll().collectList().block();
        assertThat(periodeAnneeList).hasSize(databaseSizeBeforeUpdate);
        PeriodeAnnee testPeriodeAnnee = periodeAnneeList.get(periodeAnneeList.size() - 1);
    }

    @Test
    void fullUpdatePeriodeAnneeWithPatch() throws Exception {
        // Initialize the database
        periodeAnneeRepository.save(periodeAnnee).block();

        int databaseSizeBeforeUpdate = periodeAnneeRepository.findAll().collectList().block().size();

        // Update the periodeAnnee using partial update
        PeriodeAnnee partialUpdatedPeriodeAnnee = new PeriodeAnnee();
        partialUpdatedPeriodeAnnee.setId(periodeAnnee.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPeriodeAnnee.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPeriodeAnnee))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PeriodeAnnee in the database
        List<PeriodeAnnee> periodeAnneeList = periodeAnneeRepository.findAll().collectList().block();
        assertThat(periodeAnneeList).hasSize(databaseSizeBeforeUpdate);
        PeriodeAnnee testPeriodeAnnee = periodeAnneeList.get(periodeAnneeList.size() - 1);
    }

    @Test
    void patchNonExistingPeriodeAnnee() throws Exception {
        int databaseSizeBeforeUpdate = periodeAnneeRepository.findAll().collectList().block().size();
        periodeAnnee.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, periodeAnnee.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(periodeAnnee))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PeriodeAnnee in the database
        List<PeriodeAnnee> periodeAnneeList = periodeAnneeRepository.findAll().collectList().block();
        assertThat(periodeAnneeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPeriodeAnnee() throws Exception {
        int databaseSizeBeforeUpdate = periodeAnneeRepository.findAll().collectList().block().size();
        periodeAnnee.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(periodeAnnee))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PeriodeAnnee in the database
        List<PeriodeAnnee> periodeAnneeList = periodeAnneeRepository.findAll().collectList().block();
        assertThat(periodeAnneeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPeriodeAnnee() throws Exception {
        int databaseSizeBeforeUpdate = periodeAnneeRepository.findAll().collectList().block().size();
        periodeAnnee.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(periodeAnnee))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PeriodeAnnee in the database
        List<PeriodeAnnee> periodeAnneeList = periodeAnneeRepository.findAll().collectList().block();
        assertThat(periodeAnneeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePeriodeAnnee() {
        // Initialize the database
        periodeAnneeRepository.save(periodeAnnee).block();

        int databaseSizeBeforeDelete = periodeAnneeRepository.findAll().collectList().block().size();

        // Delete the periodeAnnee
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, periodeAnnee.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<PeriodeAnnee> periodeAnneeList = periodeAnneeRepository.findAll().collectList().block();
        assertThat(periodeAnneeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
