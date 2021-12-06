package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.Mois;
import fr.syncrase.ecosyst.repository.MoisRepository;
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
 * Integration tests for the {@link MoisResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class MoisResourceIT {

    private static final Double DEFAULT_NUMERO = 1D;
    private static final Double UPDATED_NUMERO = 2D;

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/mois";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MoisRepository moisRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Mois mois;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mois createEntity(EntityManager em) {
        Mois mois = new Mois().numero(DEFAULT_NUMERO).nom(DEFAULT_NOM);
        return mois;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mois createUpdatedEntity(EntityManager em) {
        Mois mois = new Mois().numero(UPDATED_NUMERO).nom(UPDATED_NOM);
        return mois;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Mois.class).block();
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
        mois = createEntity(em);
    }

    @Test
    void createMois() throws Exception {
        int databaseSizeBeforeCreate = moisRepository.findAll().collectList().block().size();
        // Create the Mois
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(mois))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Mois in the database
        List<Mois> moisList = moisRepository.findAll().collectList().block();
        assertThat(moisList).hasSize(databaseSizeBeforeCreate + 1);
        Mois testMois = moisList.get(moisList.size() - 1);
        assertThat(testMois.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testMois.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    void createMoisWithExistingId() throws Exception {
        // Create the Mois with an existing ID
        mois.setId(1L);

        int databaseSizeBeforeCreate = moisRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(mois))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Mois in the database
        List<Mois> moisList = moisRepository.findAll().collectList().block();
        assertThat(moisList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNumeroIsRequired() throws Exception {
        int databaseSizeBeforeTest = moisRepository.findAll().collectList().block().size();
        // set the field null
        mois.setNumero(null);

        // Create the Mois, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(mois))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Mois> moisList = moisRepository.findAll().collectList().block();
        assertThat(moisList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = moisRepository.findAll().collectList().block().size();
        // set the field null
        mois.setNom(null);

        // Create the Mois, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(mois))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Mois> moisList = moisRepository.findAll().collectList().block();
        assertThat(moisList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllMois() {
        // Initialize the database
        moisRepository.save(mois).block();

        // Get all the moisList
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
            .value(hasItem(mois.getId().intValue()))
            .jsonPath("$.[*].numero")
            .value(hasItem(DEFAULT_NUMERO.doubleValue()))
            .jsonPath("$.[*].nom")
            .value(hasItem(DEFAULT_NOM));
    }

    @Test
    void getMois() {
        // Initialize the database
        moisRepository.save(mois).block();

        // Get the mois
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, mois.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(mois.getId().intValue()))
            .jsonPath("$.numero")
            .value(is(DEFAULT_NUMERO.doubleValue()))
            .jsonPath("$.nom")
            .value(is(DEFAULT_NOM));
    }

    @Test
    void getNonExistingMois() {
        // Get the mois
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewMois() throws Exception {
        // Initialize the database
        moisRepository.save(mois).block();

        int databaseSizeBeforeUpdate = moisRepository.findAll().collectList().block().size();

        // Update the mois
        Mois updatedMois = moisRepository.findById(mois.getId()).block();
        updatedMois.numero(UPDATED_NUMERO).nom(UPDATED_NOM);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedMois.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedMois))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Mois in the database
        List<Mois> moisList = moisRepository.findAll().collectList().block();
        assertThat(moisList).hasSize(databaseSizeBeforeUpdate);
        Mois testMois = moisList.get(moisList.size() - 1);
        assertThat(testMois.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testMois.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    void putNonExistingMois() throws Exception {
        int databaseSizeBeforeUpdate = moisRepository.findAll().collectList().block().size();
        mois.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, mois.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(mois))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Mois in the database
        List<Mois> moisList = moisRepository.findAll().collectList().block();
        assertThat(moisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchMois() throws Exception {
        int databaseSizeBeforeUpdate = moisRepository.findAll().collectList().block().size();
        mois.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(mois))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Mois in the database
        List<Mois> moisList = moisRepository.findAll().collectList().block();
        assertThat(moisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamMois() throws Exception {
        int databaseSizeBeforeUpdate = moisRepository.findAll().collectList().block().size();
        mois.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(mois))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Mois in the database
        List<Mois> moisList = moisRepository.findAll().collectList().block();
        assertThat(moisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateMoisWithPatch() throws Exception {
        // Initialize the database
        moisRepository.save(mois).block();

        int databaseSizeBeforeUpdate = moisRepository.findAll().collectList().block().size();

        // Update the mois using partial update
        Mois partialUpdatedMois = new Mois();
        partialUpdatedMois.setId(mois.getId());

        partialUpdatedMois.numero(UPDATED_NUMERO).nom(UPDATED_NOM);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMois.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedMois))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Mois in the database
        List<Mois> moisList = moisRepository.findAll().collectList().block();
        assertThat(moisList).hasSize(databaseSizeBeforeUpdate);
        Mois testMois = moisList.get(moisList.size() - 1);
        assertThat(testMois.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testMois.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    void fullUpdateMoisWithPatch() throws Exception {
        // Initialize the database
        moisRepository.save(mois).block();

        int databaseSizeBeforeUpdate = moisRepository.findAll().collectList().block().size();

        // Update the mois using partial update
        Mois partialUpdatedMois = new Mois();
        partialUpdatedMois.setId(mois.getId());

        partialUpdatedMois.numero(UPDATED_NUMERO).nom(UPDATED_NOM);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMois.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedMois))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Mois in the database
        List<Mois> moisList = moisRepository.findAll().collectList().block();
        assertThat(moisList).hasSize(databaseSizeBeforeUpdate);
        Mois testMois = moisList.get(moisList.size() - 1);
        assertThat(testMois.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testMois.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    void patchNonExistingMois() throws Exception {
        int databaseSizeBeforeUpdate = moisRepository.findAll().collectList().block().size();
        mois.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, mois.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(mois))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Mois in the database
        List<Mois> moisList = moisRepository.findAll().collectList().block();
        assertThat(moisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchMois() throws Exception {
        int databaseSizeBeforeUpdate = moisRepository.findAll().collectList().block().size();
        mois.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(mois))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Mois in the database
        List<Mois> moisList = moisRepository.findAll().collectList().block();
        assertThat(moisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamMois() throws Exception {
        int databaseSizeBeforeUpdate = moisRepository.findAll().collectList().block().size();
        mois.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(mois))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Mois in the database
        List<Mois> moisList = moisRepository.findAll().collectList().block();
        assertThat(moisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteMois() {
        // Initialize the database
        moisRepository.save(mois).block();

        int databaseSizeBeforeDelete = moisRepository.findAll().collectList().block().size();

        // Delete the mois
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, mois.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Mois> moisList = moisRepository.findAll().collectList().block();
        assertThat(moisList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
