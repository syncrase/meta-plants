package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.NomVernaculaire;
import fr.syncrase.ecosyst.repository.NomVernaculaireRepository;
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
 * Integration tests for the {@link NomVernaculaireResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class NomVernaculaireResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/nom-vernaculaires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NomVernaculaireRepository nomVernaculaireRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private NomVernaculaire nomVernaculaire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NomVernaculaire createEntity(EntityManager em) {
        NomVernaculaire nomVernaculaire = new NomVernaculaire().nom(DEFAULT_NOM).description(DEFAULT_DESCRIPTION);
        return nomVernaculaire;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NomVernaculaire createUpdatedEntity(EntityManager em) {
        NomVernaculaire nomVernaculaire = new NomVernaculaire().nom(UPDATED_NOM).description(UPDATED_DESCRIPTION);
        return nomVernaculaire;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(NomVernaculaire.class).block();
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
        nomVernaculaire = createEntity(em);
    }

    @Test
    void createNomVernaculaire() throws Exception {
        int databaseSizeBeforeCreate = nomVernaculaireRepository.findAll().collectList().block().size();
        // Create the NomVernaculaire
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(nomVernaculaire))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the NomVernaculaire in the database
        List<NomVernaculaire> nomVernaculaireList = nomVernaculaireRepository.findAll().collectList().block();
        assertThat(nomVernaculaireList).hasSize(databaseSizeBeforeCreate + 1);
        NomVernaculaire testNomVernaculaire = nomVernaculaireList.get(nomVernaculaireList.size() - 1);
        assertThat(testNomVernaculaire.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testNomVernaculaire.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void createNomVernaculaireWithExistingId() throws Exception {
        // Create the NomVernaculaire with an existing ID
        nomVernaculaire.setId(1L);

        int databaseSizeBeforeCreate = nomVernaculaireRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(nomVernaculaire))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the NomVernaculaire in the database
        List<NomVernaculaire> nomVernaculaireList = nomVernaculaireRepository.findAll().collectList().block();
        assertThat(nomVernaculaireList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = nomVernaculaireRepository.findAll().collectList().block().size();
        // set the field null
        nomVernaculaire.setNom(null);

        // Create the NomVernaculaire, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(nomVernaculaire))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<NomVernaculaire> nomVernaculaireList = nomVernaculaireRepository.findAll().collectList().block();
        assertThat(nomVernaculaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllNomVernaculaires() {
        // Initialize the database
        nomVernaculaireRepository.save(nomVernaculaire).block();

        // Get all the nomVernaculaireList
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
            .value(hasItem(nomVernaculaire.getId().intValue()))
            .jsonPath("$.[*].nom")
            .value(hasItem(DEFAULT_NOM))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION));
    }

    @Test
    void getNomVernaculaire() {
        // Initialize the database
        nomVernaculaireRepository.save(nomVernaculaire).block();

        // Get the nomVernaculaire
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, nomVernaculaire.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(nomVernaculaire.getId().intValue()))
            .jsonPath("$.nom")
            .value(is(DEFAULT_NOM))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION));
    }

    @Test
    void getNonExistingNomVernaculaire() {
        // Get the nomVernaculaire
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewNomVernaculaire() throws Exception {
        // Initialize the database
        nomVernaculaireRepository.save(nomVernaculaire).block();

        int databaseSizeBeforeUpdate = nomVernaculaireRepository.findAll().collectList().block().size();

        // Update the nomVernaculaire
        NomVernaculaire updatedNomVernaculaire = nomVernaculaireRepository.findById(nomVernaculaire.getId()).block();
        updatedNomVernaculaire.nom(UPDATED_NOM).description(UPDATED_DESCRIPTION);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedNomVernaculaire.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedNomVernaculaire))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the NomVernaculaire in the database
        List<NomVernaculaire> nomVernaculaireList = nomVernaculaireRepository.findAll().collectList().block();
        assertThat(nomVernaculaireList).hasSize(databaseSizeBeforeUpdate);
        NomVernaculaire testNomVernaculaire = nomVernaculaireList.get(nomVernaculaireList.size() - 1);
        assertThat(testNomVernaculaire.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testNomVernaculaire.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void putNonExistingNomVernaculaire() throws Exception {
        int databaseSizeBeforeUpdate = nomVernaculaireRepository.findAll().collectList().block().size();
        nomVernaculaire.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, nomVernaculaire.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(nomVernaculaire))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the NomVernaculaire in the database
        List<NomVernaculaire> nomVernaculaireList = nomVernaculaireRepository.findAll().collectList().block();
        assertThat(nomVernaculaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchNomVernaculaire() throws Exception {
        int databaseSizeBeforeUpdate = nomVernaculaireRepository.findAll().collectList().block().size();
        nomVernaculaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(nomVernaculaire))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the NomVernaculaire in the database
        List<NomVernaculaire> nomVernaculaireList = nomVernaculaireRepository.findAll().collectList().block();
        assertThat(nomVernaculaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamNomVernaculaire() throws Exception {
        int databaseSizeBeforeUpdate = nomVernaculaireRepository.findAll().collectList().block().size();
        nomVernaculaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(nomVernaculaire))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the NomVernaculaire in the database
        List<NomVernaculaire> nomVernaculaireList = nomVernaculaireRepository.findAll().collectList().block();
        assertThat(nomVernaculaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateNomVernaculaireWithPatch() throws Exception {
        // Initialize the database
        nomVernaculaireRepository.save(nomVernaculaire).block();

        int databaseSizeBeforeUpdate = nomVernaculaireRepository.findAll().collectList().block().size();

        // Update the nomVernaculaire using partial update
        NomVernaculaire partialUpdatedNomVernaculaire = new NomVernaculaire();
        partialUpdatedNomVernaculaire.setId(nomVernaculaire.getId());

        partialUpdatedNomVernaculaire.nom(UPDATED_NOM);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedNomVernaculaire.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedNomVernaculaire))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the NomVernaculaire in the database
        List<NomVernaculaire> nomVernaculaireList = nomVernaculaireRepository.findAll().collectList().block();
        assertThat(nomVernaculaireList).hasSize(databaseSizeBeforeUpdate);
        NomVernaculaire testNomVernaculaire = nomVernaculaireList.get(nomVernaculaireList.size() - 1);
        assertThat(testNomVernaculaire.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testNomVernaculaire.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void fullUpdateNomVernaculaireWithPatch() throws Exception {
        // Initialize the database
        nomVernaculaireRepository.save(nomVernaculaire).block();

        int databaseSizeBeforeUpdate = nomVernaculaireRepository.findAll().collectList().block().size();

        // Update the nomVernaculaire using partial update
        NomVernaculaire partialUpdatedNomVernaculaire = new NomVernaculaire();
        partialUpdatedNomVernaculaire.setId(nomVernaculaire.getId());

        partialUpdatedNomVernaculaire.nom(UPDATED_NOM).description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedNomVernaculaire.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedNomVernaculaire))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the NomVernaculaire in the database
        List<NomVernaculaire> nomVernaculaireList = nomVernaculaireRepository.findAll().collectList().block();
        assertThat(nomVernaculaireList).hasSize(databaseSizeBeforeUpdate);
        NomVernaculaire testNomVernaculaire = nomVernaculaireList.get(nomVernaculaireList.size() - 1);
        assertThat(testNomVernaculaire.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testNomVernaculaire.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void patchNonExistingNomVernaculaire() throws Exception {
        int databaseSizeBeforeUpdate = nomVernaculaireRepository.findAll().collectList().block().size();
        nomVernaculaire.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, nomVernaculaire.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(nomVernaculaire))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the NomVernaculaire in the database
        List<NomVernaculaire> nomVernaculaireList = nomVernaculaireRepository.findAll().collectList().block();
        assertThat(nomVernaculaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchNomVernaculaire() throws Exception {
        int databaseSizeBeforeUpdate = nomVernaculaireRepository.findAll().collectList().block().size();
        nomVernaculaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(nomVernaculaire))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the NomVernaculaire in the database
        List<NomVernaculaire> nomVernaculaireList = nomVernaculaireRepository.findAll().collectList().block();
        assertThat(nomVernaculaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamNomVernaculaire() throws Exception {
        int databaseSizeBeforeUpdate = nomVernaculaireRepository.findAll().collectList().block().size();
        nomVernaculaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(nomVernaculaire))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the NomVernaculaire in the database
        List<NomVernaculaire> nomVernaculaireList = nomVernaculaireRepository.findAll().collectList().block();
        assertThat(nomVernaculaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteNomVernaculaire() {
        // Initialize the database
        nomVernaculaireRepository.save(nomVernaculaire).block();

        int databaseSizeBeforeDelete = nomVernaculaireRepository.findAll().collectList().block().size();

        // Delete the nomVernaculaire
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, nomVernaculaire.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<NomVernaculaire> nomVernaculaireList = nomVernaculaireRepository.findAll().collectList().block();
        assertThat(nomVernaculaireList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
