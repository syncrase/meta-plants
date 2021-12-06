package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.Strate;
import fr.syncrase.ecosyst.repository.StrateRepository;
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
 * Integration tests for the {@link StrateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class StrateResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/strates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StrateRepository strateRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Strate strate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Strate createEntity(EntityManager em) {
        Strate strate = new Strate().type(DEFAULT_TYPE);
        return strate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Strate createUpdatedEntity(EntityManager em) {
        Strate strate = new Strate().type(UPDATED_TYPE);
        return strate;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Strate.class).block();
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
        strate = createEntity(em);
    }

    @Test
    void createStrate() throws Exception {
        int databaseSizeBeforeCreate = strateRepository.findAll().collectList().block().size();
        // Create the Strate
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(strate))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Strate in the database
        List<Strate> strateList = strateRepository.findAll().collectList().block();
        assertThat(strateList).hasSize(databaseSizeBeforeCreate + 1);
        Strate testStrate = strateList.get(strateList.size() - 1);
        assertThat(testStrate.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    void createStrateWithExistingId() throws Exception {
        // Create the Strate with an existing ID
        strate.setId(1L);

        int databaseSizeBeforeCreate = strateRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(strate))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Strate in the database
        List<Strate> strateList = strateRepository.findAll().collectList().block();
        assertThat(strateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllStrates() {
        // Initialize the database
        strateRepository.save(strate).block();

        // Get all the strateList
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
            .value(hasItem(strate.getId().intValue()))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE));
    }

    @Test
    void getStrate() {
        // Initialize the database
        strateRepository.save(strate).block();

        // Get the strate
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, strate.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(strate.getId().intValue()))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE));
    }

    @Test
    void getNonExistingStrate() {
        // Get the strate
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewStrate() throws Exception {
        // Initialize the database
        strateRepository.save(strate).block();

        int databaseSizeBeforeUpdate = strateRepository.findAll().collectList().block().size();

        // Update the strate
        Strate updatedStrate = strateRepository.findById(strate.getId()).block();
        updatedStrate.type(UPDATED_TYPE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedStrate.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedStrate))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Strate in the database
        List<Strate> strateList = strateRepository.findAll().collectList().block();
        assertThat(strateList).hasSize(databaseSizeBeforeUpdate);
        Strate testStrate = strateList.get(strateList.size() - 1);
        assertThat(testStrate.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    void putNonExistingStrate() throws Exception {
        int databaseSizeBeforeUpdate = strateRepository.findAll().collectList().block().size();
        strate.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, strate.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(strate))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Strate in the database
        List<Strate> strateList = strateRepository.findAll().collectList().block();
        assertThat(strateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchStrate() throws Exception {
        int databaseSizeBeforeUpdate = strateRepository.findAll().collectList().block().size();
        strate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(strate))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Strate in the database
        List<Strate> strateList = strateRepository.findAll().collectList().block();
        assertThat(strateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamStrate() throws Exception {
        int databaseSizeBeforeUpdate = strateRepository.findAll().collectList().block().size();
        strate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(strate))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Strate in the database
        List<Strate> strateList = strateRepository.findAll().collectList().block();
        assertThat(strateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateStrateWithPatch() throws Exception {
        // Initialize the database
        strateRepository.save(strate).block();

        int databaseSizeBeforeUpdate = strateRepository.findAll().collectList().block().size();

        // Update the strate using partial update
        Strate partialUpdatedStrate = new Strate();
        partialUpdatedStrate.setId(strate.getId());

        partialUpdatedStrate.type(UPDATED_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedStrate.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedStrate))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Strate in the database
        List<Strate> strateList = strateRepository.findAll().collectList().block();
        assertThat(strateList).hasSize(databaseSizeBeforeUpdate);
        Strate testStrate = strateList.get(strateList.size() - 1);
        assertThat(testStrate.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    void fullUpdateStrateWithPatch() throws Exception {
        // Initialize the database
        strateRepository.save(strate).block();

        int databaseSizeBeforeUpdate = strateRepository.findAll().collectList().block().size();

        // Update the strate using partial update
        Strate partialUpdatedStrate = new Strate();
        partialUpdatedStrate.setId(strate.getId());

        partialUpdatedStrate.type(UPDATED_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedStrate.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedStrate))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Strate in the database
        List<Strate> strateList = strateRepository.findAll().collectList().block();
        assertThat(strateList).hasSize(databaseSizeBeforeUpdate);
        Strate testStrate = strateList.get(strateList.size() - 1);
        assertThat(testStrate.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    void patchNonExistingStrate() throws Exception {
        int databaseSizeBeforeUpdate = strateRepository.findAll().collectList().block().size();
        strate.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, strate.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(strate))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Strate in the database
        List<Strate> strateList = strateRepository.findAll().collectList().block();
        assertThat(strateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchStrate() throws Exception {
        int databaseSizeBeforeUpdate = strateRepository.findAll().collectList().block().size();
        strate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(strate))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Strate in the database
        List<Strate> strateList = strateRepository.findAll().collectList().block();
        assertThat(strateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamStrate() throws Exception {
        int databaseSizeBeforeUpdate = strateRepository.findAll().collectList().block().size();
        strate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(strate))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Strate in the database
        List<Strate> strateList = strateRepository.findAll().collectList().block();
        assertThat(strateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteStrate() {
        // Initialize the database
        strateRepository.save(strate).block();

        int databaseSizeBeforeDelete = strateRepository.findAll().collectList().block().size();

        // Delete the strate
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, strate.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Strate> strateList = strateRepository.findAll().collectList().block();
        assertThat(strateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
