package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.Temperature;
import fr.syncrase.ecosyst.repository.TemperatureRepository;
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
 * Integration tests for the {@link TemperatureResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class TemperatureResourceIT {

    private static final Double DEFAULT_MIN = 1D;
    private static final Double UPDATED_MIN = 2D;

    private static final Double DEFAULT_MAX = 1D;
    private static final Double UPDATED_MAX = 2D;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_RUSTICITE = "AAAAAAAAAA";
    private static final String UPDATED_RUSTICITE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/temperatures";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TemperatureRepository temperatureRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Temperature temperature;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Temperature createEntity(EntityManager em) {
        Temperature temperature = new Temperature()
            .min(DEFAULT_MIN)
            .max(DEFAULT_MAX)
            .description(DEFAULT_DESCRIPTION)
            .rusticite(DEFAULT_RUSTICITE);
        return temperature;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Temperature createUpdatedEntity(EntityManager em) {
        Temperature temperature = new Temperature()
            .min(UPDATED_MIN)
            .max(UPDATED_MAX)
            .description(UPDATED_DESCRIPTION)
            .rusticite(UPDATED_RUSTICITE);
        return temperature;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Temperature.class).block();
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
        temperature = createEntity(em);
    }

    @Test
    void createTemperature() throws Exception {
        int databaseSizeBeforeCreate = temperatureRepository.findAll().collectList().block().size();
        // Create the Temperature
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(temperature))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Temperature in the database
        List<Temperature> temperatureList = temperatureRepository.findAll().collectList().block();
        assertThat(temperatureList).hasSize(databaseSizeBeforeCreate + 1);
        Temperature testTemperature = temperatureList.get(temperatureList.size() - 1);
        assertThat(testTemperature.getMin()).isEqualTo(DEFAULT_MIN);
        assertThat(testTemperature.getMax()).isEqualTo(DEFAULT_MAX);
        assertThat(testTemperature.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTemperature.getRusticite()).isEqualTo(DEFAULT_RUSTICITE);
    }

    @Test
    void createTemperatureWithExistingId() throws Exception {
        // Create the Temperature with an existing ID
        temperature.setId(1L);

        int databaseSizeBeforeCreate = temperatureRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(temperature))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Temperature in the database
        List<Temperature> temperatureList = temperatureRepository.findAll().collectList().block();
        assertThat(temperatureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllTemperatures() {
        // Initialize the database
        temperatureRepository.save(temperature).block();

        // Get all the temperatureList
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
            .value(hasItem(temperature.getId().intValue()))
            .jsonPath("$.[*].min")
            .value(hasItem(DEFAULT_MIN.doubleValue()))
            .jsonPath("$.[*].max")
            .value(hasItem(DEFAULT_MAX.doubleValue()))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].rusticite")
            .value(hasItem(DEFAULT_RUSTICITE));
    }

    @Test
    void getTemperature() {
        // Initialize the database
        temperatureRepository.save(temperature).block();

        // Get the temperature
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, temperature.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(temperature.getId().intValue()))
            .jsonPath("$.min")
            .value(is(DEFAULT_MIN.doubleValue()))
            .jsonPath("$.max")
            .value(is(DEFAULT_MAX.doubleValue()))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.rusticite")
            .value(is(DEFAULT_RUSTICITE));
    }

    @Test
    void getNonExistingTemperature() {
        // Get the temperature
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewTemperature() throws Exception {
        // Initialize the database
        temperatureRepository.save(temperature).block();

        int databaseSizeBeforeUpdate = temperatureRepository.findAll().collectList().block().size();

        // Update the temperature
        Temperature updatedTemperature = temperatureRepository.findById(temperature.getId()).block();
        updatedTemperature.min(UPDATED_MIN).max(UPDATED_MAX).description(UPDATED_DESCRIPTION).rusticite(UPDATED_RUSTICITE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedTemperature.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedTemperature))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Temperature in the database
        List<Temperature> temperatureList = temperatureRepository.findAll().collectList().block();
        assertThat(temperatureList).hasSize(databaseSizeBeforeUpdate);
        Temperature testTemperature = temperatureList.get(temperatureList.size() - 1);
        assertThat(testTemperature.getMin()).isEqualTo(UPDATED_MIN);
        assertThat(testTemperature.getMax()).isEqualTo(UPDATED_MAX);
        assertThat(testTemperature.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTemperature.getRusticite()).isEqualTo(UPDATED_RUSTICITE);
    }

    @Test
    void putNonExistingTemperature() throws Exception {
        int databaseSizeBeforeUpdate = temperatureRepository.findAll().collectList().block().size();
        temperature.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, temperature.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(temperature))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Temperature in the database
        List<Temperature> temperatureList = temperatureRepository.findAll().collectList().block();
        assertThat(temperatureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchTemperature() throws Exception {
        int databaseSizeBeforeUpdate = temperatureRepository.findAll().collectList().block().size();
        temperature.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(temperature))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Temperature in the database
        List<Temperature> temperatureList = temperatureRepository.findAll().collectList().block();
        assertThat(temperatureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamTemperature() throws Exception {
        int databaseSizeBeforeUpdate = temperatureRepository.findAll().collectList().block().size();
        temperature.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(temperature))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Temperature in the database
        List<Temperature> temperatureList = temperatureRepository.findAll().collectList().block();
        assertThat(temperatureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateTemperatureWithPatch() throws Exception {
        // Initialize the database
        temperatureRepository.save(temperature).block();

        int databaseSizeBeforeUpdate = temperatureRepository.findAll().collectList().block().size();

        // Update the temperature using partial update
        Temperature partialUpdatedTemperature = new Temperature();
        partialUpdatedTemperature.setId(temperature.getId());

        partialUpdatedTemperature.min(UPDATED_MIN).description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTemperature.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTemperature))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Temperature in the database
        List<Temperature> temperatureList = temperatureRepository.findAll().collectList().block();
        assertThat(temperatureList).hasSize(databaseSizeBeforeUpdate);
        Temperature testTemperature = temperatureList.get(temperatureList.size() - 1);
        assertThat(testTemperature.getMin()).isEqualTo(UPDATED_MIN);
        assertThat(testTemperature.getMax()).isEqualTo(DEFAULT_MAX);
        assertThat(testTemperature.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTemperature.getRusticite()).isEqualTo(DEFAULT_RUSTICITE);
    }

    @Test
    void fullUpdateTemperatureWithPatch() throws Exception {
        // Initialize the database
        temperatureRepository.save(temperature).block();

        int databaseSizeBeforeUpdate = temperatureRepository.findAll().collectList().block().size();

        // Update the temperature using partial update
        Temperature partialUpdatedTemperature = new Temperature();
        partialUpdatedTemperature.setId(temperature.getId());

        partialUpdatedTemperature.min(UPDATED_MIN).max(UPDATED_MAX).description(UPDATED_DESCRIPTION).rusticite(UPDATED_RUSTICITE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTemperature.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTemperature))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Temperature in the database
        List<Temperature> temperatureList = temperatureRepository.findAll().collectList().block();
        assertThat(temperatureList).hasSize(databaseSizeBeforeUpdate);
        Temperature testTemperature = temperatureList.get(temperatureList.size() - 1);
        assertThat(testTemperature.getMin()).isEqualTo(UPDATED_MIN);
        assertThat(testTemperature.getMax()).isEqualTo(UPDATED_MAX);
        assertThat(testTemperature.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTemperature.getRusticite()).isEqualTo(UPDATED_RUSTICITE);
    }

    @Test
    void patchNonExistingTemperature() throws Exception {
        int databaseSizeBeforeUpdate = temperatureRepository.findAll().collectList().block().size();
        temperature.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, temperature.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(temperature))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Temperature in the database
        List<Temperature> temperatureList = temperatureRepository.findAll().collectList().block();
        assertThat(temperatureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchTemperature() throws Exception {
        int databaseSizeBeforeUpdate = temperatureRepository.findAll().collectList().block().size();
        temperature.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(temperature))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Temperature in the database
        List<Temperature> temperatureList = temperatureRepository.findAll().collectList().block();
        assertThat(temperatureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamTemperature() throws Exception {
        int databaseSizeBeforeUpdate = temperatureRepository.findAll().collectList().block().size();
        temperature.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(temperature))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Temperature in the database
        List<Temperature> temperatureList = temperatureRepository.findAll().collectList().block();
        assertThat(temperatureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteTemperature() {
        // Initialize the database
        temperatureRepository.save(temperature).block();

        int databaseSizeBeforeDelete = temperatureRepository.findAll().collectList().block().size();

        // Delete the temperature
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, temperature.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Temperature> temperatureList = temperatureRepository.findAll().collectList().block();
        assertThat(temperatureList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
