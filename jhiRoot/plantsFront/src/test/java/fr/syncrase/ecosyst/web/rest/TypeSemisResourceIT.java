package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.TypeSemis;
import fr.syncrase.ecosyst.repository.TypeSemisRepository;
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
 * Integration tests for the {@link TypeSemisResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class TypeSemisResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/type-semis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TypeSemisRepository typeSemisRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private TypeSemis typeSemis;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeSemis createEntity(EntityManager em) {
        TypeSemis typeSemis = new TypeSemis().type(DEFAULT_TYPE).description(DEFAULT_DESCRIPTION);
        return typeSemis;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeSemis createUpdatedEntity(EntityManager em) {
        TypeSemis typeSemis = new TypeSemis().type(UPDATED_TYPE).description(UPDATED_DESCRIPTION);
        return typeSemis;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(TypeSemis.class).block();
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
        typeSemis = createEntity(em);
    }

    @Test
    void createTypeSemis() throws Exception {
        int databaseSizeBeforeCreate = typeSemisRepository.findAll().collectList().block().size();
        // Create the TypeSemis
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(typeSemis))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the TypeSemis in the database
        List<TypeSemis> typeSemisList = typeSemisRepository.findAll().collectList().block();
        assertThat(typeSemisList).hasSize(databaseSizeBeforeCreate + 1);
        TypeSemis testTypeSemis = typeSemisList.get(typeSemisList.size() - 1);
        assertThat(testTypeSemis.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTypeSemis.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void createTypeSemisWithExistingId() throws Exception {
        // Create the TypeSemis with an existing ID
        typeSemis.setId(1L);

        int databaseSizeBeforeCreate = typeSemisRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(typeSemis))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TypeSemis in the database
        List<TypeSemis> typeSemisList = typeSemisRepository.findAll().collectList().block();
        assertThat(typeSemisList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = typeSemisRepository.findAll().collectList().block().size();
        // set the field null
        typeSemis.setType(null);

        // Create the TypeSemis, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(typeSemis))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TypeSemis> typeSemisList = typeSemisRepository.findAll().collectList().block();
        assertThat(typeSemisList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllTypeSemis() {
        // Initialize the database
        typeSemisRepository.save(typeSemis).block();

        // Get all the typeSemisList
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
            .value(hasItem(typeSemis.getId().intValue()))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION));
    }

    @Test
    void getTypeSemis() {
        // Initialize the database
        typeSemisRepository.save(typeSemis).block();

        // Get the typeSemis
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, typeSemis.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(typeSemis.getId().intValue()))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION));
    }

    @Test
    void getNonExistingTypeSemis() {
        // Get the typeSemis
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewTypeSemis() throws Exception {
        // Initialize the database
        typeSemisRepository.save(typeSemis).block();

        int databaseSizeBeforeUpdate = typeSemisRepository.findAll().collectList().block().size();

        // Update the typeSemis
        TypeSemis updatedTypeSemis = typeSemisRepository.findById(typeSemis.getId()).block();
        updatedTypeSemis.type(UPDATED_TYPE).description(UPDATED_DESCRIPTION);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedTypeSemis.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedTypeSemis))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TypeSemis in the database
        List<TypeSemis> typeSemisList = typeSemisRepository.findAll().collectList().block();
        assertThat(typeSemisList).hasSize(databaseSizeBeforeUpdate);
        TypeSemis testTypeSemis = typeSemisList.get(typeSemisList.size() - 1);
        assertThat(testTypeSemis.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTypeSemis.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void putNonExistingTypeSemis() throws Exception {
        int databaseSizeBeforeUpdate = typeSemisRepository.findAll().collectList().block().size();
        typeSemis.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, typeSemis.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(typeSemis))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TypeSemis in the database
        List<TypeSemis> typeSemisList = typeSemisRepository.findAll().collectList().block();
        assertThat(typeSemisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchTypeSemis() throws Exception {
        int databaseSizeBeforeUpdate = typeSemisRepository.findAll().collectList().block().size();
        typeSemis.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(typeSemis))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TypeSemis in the database
        List<TypeSemis> typeSemisList = typeSemisRepository.findAll().collectList().block();
        assertThat(typeSemisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamTypeSemis() throws Exception {
        int databaseSizeBeforeUpdate = typeSemisRepository.findAll().collectList().block().size();
        typeSemis.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(typeSemis))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TypeSemis in the database
        List<TypeSemis> typeSemisList = typeSemisRepository.findAll().collectList().block();
        assertThat(typeSemisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateTypeSemisWithPatch() throws Exception {
        // Initialize the database
        typeSemisRepository.save(typeSemis).block();

        int databaseSizeBeforeUpdate = typeSemisRepository.findAll().collectList().block().size();

        // Update the typeSemis using partial update
        TypeSemis partialUpdatedTypeSemis = new TypeSemis();
        partialUpdatedTypeSemis.setId(typeSemis.getId());

        partialUpdatedTypeSemis.type(UPDATED_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTypeSemis.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeSemis))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TypeSemis in the database
        List<TypeSemis> typeSemisList = typeSemisRepository.findAll().collectList().block();
        assertThat(typeSemisList).hasSize(databaseSizeBeforeUpdate);
        TypeSemis testTypeSemis = typeSemisList.get(typeSemisList.size() - 1);
        assertThat(testTypeSemis.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTypeSemis.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void fullUpdateTypeSemisWithPatch() throws Exception {
        // Initialize the database
        typeSemisRepository.save(typeSemis).block();

        int databaseSizeBeforeUpdate = typeSemisRepository.findAll().collectList().block().size();

        // Update the typeSemis using partial update
        TypeSemis partialUpdatedTypeSemis = new TypeSemis();
        partialUpdatedTypeSemis.setId(typeSemis.getId());

        partialUpdatedTypeSemis.type(UPDATED_TYPE).description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTypeSemis.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeSemis))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TypeSemis in the database
        List<TypeSemis> typeSemisList = typeSemisRepository.findAll().collectList().block();
        assertThat(typeSemisList).hasSize(databaseSizeBeforeUpdate);
        TypeSemis testTypeSemis = typeSemisList.get(typeSemisList.size() - 1);
        assertThat(testTypeSemis.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTypeSemis.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void patchNonExistingTypeSemis() throws Exception {
        int databaseSizeBeforeUpdate = typeSemisRepository.findAll().collectList().block().size();
        typeSemis.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, typeSemis.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(typeSemis))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TypeSemis in the database
        List<TypeSemis> typeSemisList = typeSemisRepository.findAll().collectList().block();
        assertThat(typeSemisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchTypeSemis() throws Exception {
        int databaseSizeBeforeUpdate = typeSemisRepository.findAll().collectList().block().size();
        typeSemis.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(typeSemis))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TypeSemis in the database
        List<TypeSemis> typeSemisList = typeSemisRepository.findAll().collectList().block();
        assertThat(typeSemisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamTypeSemis() throws Exception {
        int databaseSizeBeforeUpdate = typeSemisRepository.findAll().collectList().block().size();
        typeSemis.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(typeSemis))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TypeSemis in the database
        List<TypeSemis> typeSemisList = typeSemisRepository.findAll().collectList().block();
        assertThat(typeSemisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteTypeSemis() {
        // Initialize the database
        typeSemisRepository.save(typeSemis).block();

        int databaseSizeBeforeDelete = typeSemisRepository.findAll().collectList().block().size();

        // Delete the typeSemis
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, typeSemis.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<TypeSemis> typeSemisList = typeSemisRepository.findAll().collectList().block();
        assertThat(typeSemisList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
