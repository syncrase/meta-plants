package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.Cronquist;
import fr.syncrase.ecosyst.repository.CronquistRepository;
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
 * Integration tests for the {@link CronquistResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class CronquistResourceIT {

    private static final String DEFAULT_REGNE = "AAAAAAAAAA";
    private static final String UPDATED_REGNE = "BBBBBBBBBB";

    private static final String DEFAULT_SOUS_REGNE = "AAAAAAAAAA";
    private static final String UPDATED_SOUS_REGNE = "BBBBBBBBBB";

    private static final String DEFAULT_DIVISION = "AAAAAAAAAA";
    private static final String UPDATED_DIVISION = "BBBBBBBBBB";

    private static final String DEFAULT_CLASSE = "AAAAAAAAAA";
    private static final String UPDATED_CLASSE = "BBBBBBBBBB";

    private static final String DEFAULT_SOUS_CLASSE = "AAAAAAAAAA";
    private static final String UPDATED_SOUS_CLASSE = "BBBBBBBBBB";

    private static final String DEFAULT_ORDRE = "AAAAAAAAAA";
    private static final String UPDATED_ORDRE = "BBBBBBBBBB";

    private static final String DEFAULT_FAMILLE = "AAAAAAAAAA";
    private static final String UPDATED_FAMILLE = "BBBBBBBBBB";

    private static final String DEFAULT_GENRE = "AAAAAAAAAA";
    private static final String UPDATED_GENRE = "BBBBBBBBBB";

    private static final String DEFAULT_ESPECE = "AAAAAAAAAA";
    private static final String UPDATED_ESPECE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cronquists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CronquistRepository cronquistRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Cronquist cronquist;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cronquist createEntity(EntityManager em) {
        Cronquist cronquist = new Cronquist()
            .regne(DEFAULT_REGNE)
            .sousRegne(DEFAULT_SOUS_REGNE)
            .division(DEFAULT_DIVISION)
            .classe(DEFAULT_CLASSE)
            .sousClasse(DEFAULT_SOUS_CLASSE)
            .ordre(DEFAULT_ORDRE)
            .famille(DEFAULT_FAMILLE)
            .genre(DEFAULT_GENRE)
            .espece(DEFAULT_ESPECE);
        return cronquist;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cronquist createUpdatedEntity(EntityManager em) {
        Cronquist cronquist = new Cronquist()
            .regne(UPDATED_REGNE)
            .sousRegne(UPDATED_SOUS_REGNE)
            .division(UPDATED_DIVISION)
            .classe(UPDATED_CLASSE)
            .sousClasse(UPDATED_SOUS_CLASSE)
            .ordre(UPDATED_ORDRE)
            .famille(UPDATED_FAMILLE)
            .genre(UPDATED_GENRE)
            .espece(UPDATED_ESPECE);
        return cronquist;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Cronquist.class).block();
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
        cronquist = createEntity(em);
    }

    @Test
    void createCronquist() throws Exception {
        int databaseSizeBeforeCreate = cronquistRepository.findAll().collectList().block().size();
        // Create the Cronquist
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cronquist))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Cronquist in the database
        List<Cronquist> cronquistList = cronquistRepository.findAll().collectList().block();
        assertThat(cronquistList).hasSize(databaseSizeBeforeCreate + 1);
        Cronquist testCronquist = cronquistList.get(cronquistList.size() - 1);
        assertThat(testCronquist.getRegne()).isEqualTo(DEFAULT_REGNE);
        assertThat(testCronquist.getSousRegne()).isEqualTo(DEFAULT_SOUS_REGNE);
        assertThat(testCronquist.getDivision()).isEqualTo(DEFAULT_DIVISION);
        assertThat(testCronquist.getClasse()).isEqualTo(DEFAULT_CLASSE);
        assertThat(testCronquist.getSousClasse()).isEqualTo(DEFAULT_SOUS_CLASSE);
        assertThat(testCronquist.getOrdre()).isEqualTo(DEFAULT_ORDRE);
        assertThat(testCronquist.getFamille()).isEqualTo(DEFAULT_FAMILLE);
        assertThat(testCronquist.getGenre()).isEqualTo(DEFAULT_GENRE);
        assertThat(testCronquist.getEspece()).isEqualTo(DEFAULT_ESPECE);
    }

    @Test
    void createCronquistWithExistingId() throws Exception {
        // Create the Cronquist with an existing ID
        cronquist.setId(1L);

        int databaseSizeBeforeCreate = cronquistRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cronquist))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Cronquist in the database
        List<Cronquist> cronquistList = cronquistRepository.findAll().collectList().block();
        assertThat(cronquistList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllCronquists() {
        // Initialize the database
        cronquistRepository.save(cronquist).block();

        // Get all the cronquistList
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
            .value(hasItem(cronquist.getId().intValue()))
            .jsonPath("$.[*].regne")
            .value(hasItem(DEFAULT_REGNE))
            .jsonPath("$.[*].sousRegne")
            .value(hasItem(DEFAULT_SOUS_REGNE))
            .jsonPath("$.[*].division")
            .value(hasItem(DEFAULT_DIVISION))
            .jsonPath("$.[*].classe")
            .value(hasItem(DEFAULT_CLASSE))
            .jsonPath("$.[*].sousClasse")
            .value(hasItem(DEFAULT_SOUS_CLASSE))
            .jsonPath("$.[*].ordre")
            .value(hasItem(DEFAULT_ORDRE))
            .jsonPath("$.[*].famille")
            .value(hasItem(DEFAULT_FAMILLE))
            .jsonPath("$.[*].genre")
            .value(hasItem(DEFAULT_GENRE))
            .jsonPath("$.[*].espece")
            .value(hasItem(DEFAULT_ESPECE));
    }

    @Test
    void getCronquist() {
        // Initialize the database
        cronquistRepository.save(cronquist).block();

        // Get the cronquist
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, cronquist.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(cronquist.getId().intValue()))
            .jsonPath("$.regne")
            .value(is(DEFAULT_REGNE))
            .jsonPath("$.sousRegne")
            .value(is(DEFAULT_SOUS_REGNE))
            .jsonPath("$.division")
            .value(is(DEFAULT_DIVISION))
            .jsonPath("$.classe")
            .value(is(DEFAULT_CLASSE))
            .jsonPath("$.sousClasse")
            .value(is(DEFAULT_SOUS_CLASSE))
            .jsonPath("$.ordre")
            .value(is(DEFAULT_ORDRE))
            .jsonPath("$.famille")
            .value(is(DEFAULT_FAMILLE))
            .jsonPath("$.genre")
            .value(is(DEFAULT_GENRE))
            .jsonPath("$.espece")
            .value(is(DEFAULT_ESPECE));
    }

    @Test
    void getNonExistingCronquist() {
        // Get the cronquist
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewCronquist() throws Exception {
        // Initialize the database
        cronquistRepository.save(cronquist).block();

        int databaseSizeBeforeUpdate = cronquistRepository.findAll().collectList().block().size();

        // Update the cronquist
        Cronquist updatedCronquist = cronquistRepository.findById(cronquist.getId()).block();
        updatedCronquist
            .regne(UPDATED_REGNE)
            .sousRegne(UPDATED_SOUS_REGNE)
            .division(UPDATED_DIVISION)
            .classe(UPDATED_CLASSE)
            .sousClasse(UPDATED_SOUS_CLASSE)
            .ordre(UPDATED_ORDRE)
            .famille(UPDATED_FAMILLE)
            .genre(UPDATED_GENRE)
            .espece(UPDATED_ESPECE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedCronquist.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedCronquist))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Cronquist in the database
        List<Cronquist> cronquistList = cronquistRepository.findAll().collectList().block();
        assertThat(cronquistList).hasSize(databaseSizeBeforeUpdate);
        Cronquist testCronquist = cronquistList.get(cronquistList.size() - 1);
        assertThat(testCronquist.getRegne()).isEqualTo(UPDATED_REGNE);
        assertThat(testCronquist.getSousRegne()).isEqualTo(UPDATED_SOUS_REGNE);
        assertThat(testCronquist.getDivision()).isEqualTo(UPDATED_DIVISION);
        assertThat(testCronquist.getClasse()).isEqualTo(UPDATED_CLASSE);
        assertThat(testCronquist.getSousClasse()).isEqualTo(UPDATED_SOUS_CLASSE);
        assertThat(testCronquist.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testCronquist.getFamille()).isEqualTo(UPDATED_FAMILLE);
        assertThat(testCronquist.getGenre()).isEqualTo(UPDATED_GENRE);
        assertThat(testCronquist.getEspece()).isEqualTo(UPDATED_ESPECE);
    }

    @Test
    void putNonExistingCronquist() throws Exception {
        int databaseSizeBeforeUpdate = cronquistRepository.findAll().collectList().block().size();
        cronquist.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, cronquist.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cronquist))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Cronquist in the database
        List<Cronquist> cronquistList = cronquistRepository.findAll().collectList().block();
        assertThat(cronquistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCronquist() throws Exception {
        int databaseSizeBeforeUpdate = cronquistRepository.findAll().collectList().block().size();
        cronquist.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cronquist))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Cronquist in the database
        List<Cronquist> cronquistList = cronquistRepository.findAll().collectList().block();
        assertThat(cronquistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCronquist() throws Exception {
        int databaseSizeBeforeUpdate = cronquistRepository.findAll().collectList().block().size();
        cronquist.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cronquist))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Cronquist in the database
        List<Cronquist> cronquistList = cronquistRepository.findAll().collectList().block();
        assertThat(cronquistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCronquistWithPatch() throws Exception {
        // Initialize the database
        cronquistRepository.save(cronquist).block();

        int databaseSizeBeforeUpdate = cronquistRepository.findAll().collectList().block().size();

        // Update the cronquist using partial update
        Cronquist partialUpdatedCronquist = new Cronquist();
        partialUpdatedCronquist.setId(cronquist.getId());

        partialUpdatedCronquist.sousRegne(UPDATED_SOUS_REGNE).division(UPDATED_DIVISION).classe(UPDATED_CLASSE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCronquist.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCronquist))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Cronquist in the database
        List<Cronquist> cronquistList = cronquistRepository.findAll().collectList().block();
        assertThat(cronquistList).hasSize(databaseSizeBeforeUpdate);
        Cronquist testCronquist = cronquistList.get(cronquistList.size() - 1);
        assertThat(testCronquist.getRegne()).isEqualTo(DEFAULT_REGNE);
        assertThat(testCronquist.getSousRegne()).isEqualTo(UPDATED_SOUS_REGNE);
        assertThat(testCronquist.getDivision()).isEqualTo(UPDATED_DIVISION);
        assertThat(testCronquist.getClasse()).isEqualTo(UPDATED_CLASSE);
        assertThat(testCronquist.getSousClasse()).isEqualTo(DEFAULT_SOUS_CLASSE);
        assertThat(testCronquist.getOrdre()).isEqualTo(DEFAULT_ORDRE);
        assertThat(testCronquist.getFamille()).isEqualTo(DEFAULT_FAMILLE);
        assertThat(testCronquist.getGenre()).isEqualTo(DEFAULT_GENRE);
        assertThat(testCronquist.getEspece()).isEqualTo(DEFAULT_ESPECE);
    }

    @Test
    void fullUpdateCronquistWithPatch() throws Exception {
        // Initialize the database
        cronquistRepository.save(cronquist).block();

        int databaseSizeBeforeUpdate = cronquistRepository.findAll().collectList().block().size();

        // Update the cronquist using partial update
        Cronquist partialUpdatedCronquist = new Cronquist();
        partialUpdatedCronquist.setId(cronquist.getId());

        partialUpdatedCronquist
            .regne(UPDATED_REGNE)
            .sousRegne(UPDATED_SOUS_REGNE)
            .division(UPDATED_DIVISION)
            .classe(UPDATED_CLASSE)
            .sousClasse(UPDATED_SOUS_CLASSE)
            .ordre(UPDATED_ORDRE)
            .famille(UPDATED_FAMILLE)
            .genre(UPDATED_GENRE)
            .espece(UPDATED_ESPECE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCronquist.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCronquist))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Cronquist in the database
        List<Cronquist> cronquistList = cronquistRepository.findAll().collectList().block();
        assertThat(cronquistList).hasSize(databaseSizeBeforeUpdate);
        Cronquist testCronquist = cronquistList.get(cronquistList.size() - 1);
        assertThat(testCronquist.getRegne()).isEqualTo(UPDATED_REGNE);
        assertThat(testCronquist.getSousRegne()).isEqualTo(UPDATED_SOUS_REGNE);
        assertThat(testCronquist.getDivision()).isEqualTo(UPDATED_DIVISION);
        assertThat(testCronquist.getClasse()).isEqualTo(UPDATED_CLASSE);
        assertThat(testCronquist.getSousClasse()).isEqualTo(UPDATED_SOUS_CLASSE);
        assertThat(testCronquist.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testCronquist.getFamille()).isEqualTo(UPDATED_FAMILLE);
        assertThat(testCronquist.getGenre()).isEqualTo(UPDATED_GENRE);
        assertThat(testCronquist.getEspece()).isEqualTo(UPDATED_ESPECE);
    }

    @Test
    void patchNonExistingCronquist() throws Exception {
        int databaseSizeBeforeUpdate = cronquistRepository.findAll().collectList().block().size();
        cronquist.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, cronquist.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(cronquist))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Cronquist in the database
        List<Cronquist> cronquistList = cronquistRepository.findAll().collectList().block();
        assertThat(cronquistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCronquist() throws Exception {
        int databaseSizeBeforeUpdate = cronquistRepository.findAll().collectList().block().size();
        cronquist.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(cronquist))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Cronquist in the database
        List<Cronquist> cronquistList = cronquistRepository.findAll().collectList().block();
        assertThat(cronquistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCronquist() throws Exception {
        int databaseSizeBeforeUpdate = cronquistRepository.findAll().collectList().block().size();
        cronquist.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(cronquist))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Cronquist in the database
        List<Cronquist> cronquistList = cronquistRepository.findAll().collectList().block();
        assertThat(cronquistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCronquist() {
        // Initialize the database
        cronquistRepository.save(cronquist).block();

        int databaseSizeBeforeDelete = cronquistRepository.findAll().collectList().block().size();

        // Delete the cronquist
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, cronquist.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Cronquist> cronquistList = cronquistRepository.findAll().collectList().block();
        assertThat(cronquistList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
