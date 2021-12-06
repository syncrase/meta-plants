package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.Plante;
import fr.syncrase.ecosyst.repository.PlanteRepository;
import fr.syncrase.ecosyst.service.EntityManager;
import fr.syncrase.ecosyst.service.PlanteService;
import java.time.Duration;
import java.util.ArrayList;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Integration tests for the {@link PlanteResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient
@WithMockUser
class PlanteResourceIT {

    private static final String DEFAULT_ENTRETIEN = "AAAAAAAAAA";
    private static final String UPDATED_ENTRETIEN = "BBBBBBBBBB";

    private static final String DEFAULT_HISTOIRE = "AAAAAAAAAA";
    private static final String UPDATED_HISTOIRE = "BBBBBBBBBB";

    private static final String DEFAULT_VITESSE_CROISSANCE = "AAAAAAAAAA";
    private static final String UPDATED_VITESSE_CROISSANCE = "BBBBBBBBBB";

    private static final String DEFAULT_EXPOSITION = "AAAAAAAAAA";
    private static final String UPDATED_EXPOSITION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/plantes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PlanteRepository planteRepository;

    @Mock
    private PlanteRepository planteRepositoryMock;

    @Mock
    private PlanteService planteServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Plante plante;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plante createEntity(EntityManager em) {
        Plante plante = new Plante()
            .entretien(DEFAULT_ENTRETIEN)
            .histoire(DEFAULT_HISTOIRE)
            .vitesseCroissance(DEFAULT_VITESSE_CROISSANCE)
            .exposition(DEFAULT_EXPOSITION);
        return plante;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plante createUpdatedEntity(EntityManager em) {
        Plante plante = new Plante()
            .entretien(UPDATED_ENTRETIEN)
            .histoire(UPDATED_HISTOIRE)
            .vitesseCroissance(UPDATED_VITESSE_CROISSANCE)
            .exposition(UPDATED_EXPOSITION);
        return plante;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll("rel_plante__noms_vernaculaires").block();
            em.deleteAll(Plante.class).block();
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
        plante = createEntity(em);
    }

    @Test
    void createPlante() throws Exception {
        int databaseSizeBeforeCreate = planteRepository.findAll().collectList().block().size();
        // Create the Plante
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(plante))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Plante in the database
        List<Plante> planteList = planteRepository.findAll().collectList().block();
        assertThat(planteList).hasSize(databaseSizeBeforeCreate + 1);
        Plante testPlante = planteList.get(planteList.size() - 1);
        assertThat(testPlante.getEntretien()).isEqualTo(DEFAULT_ENTRETIEN);
        assertThat(testPlante.getHistoire()).isEqualTo(DEFAULT_HISTOIRE);
        assertThat(testPlante.getVitesseCroissance()).isEqualTo(DEFAULT_VITESSE_CROISSANCE);
        assertThat(testPlante.getExposition()).isEqualTo(DEFAULT_EXPOSITION);
    }

    @Test
    void createPlanteWithExistingId() throws Exception {
        // Create the Plante with an existing ID
        plante.setId(1L);

        int databaseSizeBeforeCreate = planteRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(plante))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Plante in the database
        List<Plante> planteList = planteRepository.findAll().collectList().block();
        assertThat(planteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllPlantes() {
        // Initialize the database
        planteRepository.save(plante).block();

        // Get all the planteList
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
            .value(hasItem(plante.getId().intValue()))
            .jsonPath("$.[*].entretien")
            .value(hasItem(DEFAULT_ENTRETIEN))
            .jsonPath("$.[*].histoire")
            .value(hasItem(DEFAULT_HISTOIRE))
            .jsonPath("$.[*].vitesseCroissance")
            .value(hasItem(DEFAULT_VITESSE_CROISSANCE))
            .jsonPath("$.[*].exposition")
            .value(hasItem(DEFAULT_EXPOSITION));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPlantesWithEagerRelationshipsIsEnabled() {
        when(planteServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(planteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPlantesWithEagerRelationshipsIsNotEnabled() {
        when(planteServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(planteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getPlante() {
        // Initialize the database
        planteRepository.save(plante).block();

        // Get the plante
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, plante.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(plante.getId().intValue()))
            .jsonPath("$.entretien")
            .value(is(DEFAULT_ENTRETIEN))
            .jsonPath("$.histoire")
            .value(is(DEFAULT_HISTOIRE))
            .jsonPath("$.vitesseCroissance")
            .value(is(DEFAULT_VITESSE_CROISSANCE))
            .jsonPath("$.exposition")
            .value(is(DEFAULT_EXPOSITION));
    }

    @Test
    void getNonExistingPlante() {
        // Get the plante
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewPlante() throws Exception {
        // Initialize the database
        planteRepository.save(plante).block();

        int databaseSizeBeforeUpdate = planteRepository.findAll().collectList().block().size();

        // Update the plante
        Plante updatedPlante = planteRepository.findById(plante.getId()).block();
        updatedPlante
            .entretien(UPDATED_ENTRETIEN)
            .histoire(UPDATED_HISTOIRE)
            .vitesseCroissance(UPDATED_VITESSE_CROISSANCE)
            .exposition(UPDATED_EXPOSITION);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedPlante.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedPlante))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Plante in the database
        List<Plante> planteList = planteRepository.findAll().collectList().block();
        assertThat(planteList).hasSize(databaseSizeBeforeUpdate);
        Plante testPlante = planteList.get(planteList.size() - 1);
        assertThat(testPlante.getEntretien()).isEqualTo(UPDATED_ENTRETIEN);
        assertThat(testPlante.getHistoire()).isEqualTo(UPDATED_HISTOIRE);
        assertThat(testPlante.getVitesseCroissance()).isEqualTo(UPDATED_VITESSE_CROISSANCE);
        assertThat(testPlante.getExposition()).isEqualTo(UPDATED_EXPOSITION);
    }

    @Test
    void putNonExistingPlante() throws Exception {
        int databaseSizeBeforeUpdate = planteRepository.findAll().collectList().block().size();
        plante.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, plante.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(plante))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Plante in the database
        List<Plante> planteList = planteRepository.findAll().collectList().block();
        assertThat(planteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPlante() throws Exception {
        int databaseSizeBeforeUpdate = planteRepository.findAll().collectList().block().size();
        plante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(plante))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Plante in the database
        List<Plante> planteList = planteRepository.findAll().collectList().block();
        assertThat(planteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPlante() throws Exception {
        int databaseSizeBeforeUpdate = planteRepository.findAll().collectList().block().size();
        plante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(plante))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Plante in the database
        List<Plante> planteList = planteRepository.findAll().collectList().block();
        assertThat(planteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePlanteWithPatch() throws Exception {
        // Initialize the database
        planteRepository.save(plante).block();

        int databaseSizeBeforeUpdate = planteRepository.findAll().collectList().block().size();

        // Update the plante using partial update
        Plante partialUpdatedPlante = new Plante();
        partialUpdatedPlante.setId(plante.getId());

        partialUpdatedPlante.vitesseCroissance(UPDATED_VITESSE_CROISSANCE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPlante.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPlante))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Plante in the database
        List<Plante> planteList = planteRepository.findAll().collectList().block();
        assertThat(planteList).hasSize(databaseSizeBeforeUpdate);
        Plante testPlante = planteList.get(planteList.size() - 1);
        assertThat(testPlante.getEntretien()).isEqualTo(DEFAULT_ENTRETIEN);
        assertThat(testPlante.getHistoire()).isEqualTo(DEFAULT_HISTOIRE);
        assertThat(testPlante.getVitesseCroissance()).isEqualTo(UPDATED_VITESSE_CROISSANCE);
        assertThat(testPlante.getExposition()).isEqualTo(DEFAULT_EXPOSITION);
    }

    @Test
    void fullUpdatePlanteWithPatch() throws Exception {
        // Initialize the database
        planteRepository.save(plante).block();

        int databaseSizeBeforeUpdate = planteRepository.findAll().collectList().block().size();

        // Update the plante using partial update
        Plante partialUpdatedPlante = new Plante();
        partialUpdatedPlante.setId(plante.getId());

        partialUpdatedPlante
            .entretien(UPDATED_ENTRETIEN)
            .histoire(UPDATED_HISTOIRE)
            .vitesseCroissance(UPDATED_VITESSE_CROISSANCE)
            .exposition(UPDATED_EXPOSITION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPlante.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPlante))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Plante in the database
        List<Plante> planteList = planteRepository.findAll().collectList().block();
        assertThat(planteList).hasSize(databaseSizeBeforeUpdate);
        Plante testPlante = planteList.get(planteList.size() - 1);
        assertThat(testPlante.getEntretien()).isEqualTo(UPDATED_ENTRETIEN);
        assertThat(testPlante.getHistoire()).isEqualTo(UPDATED_HISTOIRE);
        assertThat(testPlante.getVitesseCroissance()).isEqualTo(UPDATED_VITESSE_CROISSANCE);
        assertThat(testPlante.getExposition()).isEqualTo(UPDATED_EXPOSITION);
    }

    @Test
    void patchNonExistingPlante() throws Exception {
        int databaseSizeBeforeUpdate = planteRepository.findAll().collectList().block().size();
        plante.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, plante.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(plante))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Plante in the database
        List<Plante> planteList = planteRepository.findAll().collectList().block();
        assertThat(planteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPlante() throws Exception {
        int databaseSizeBeforeUpdate = planteRepository.findAll().collectList().block().size();
        plante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(plante))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Plante in the database
        List<Plante> planteList = planteRepository.findAll().collectList().block();
        assertThat(planteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPlante() throws Exception {
        int databaseSizeBeforeUpdate = planteRepository.findAll().collectList().block().size();
        plante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(plante))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Plante in the database
        List<Plante> planteList = planteRepository.findAll().collectList().block();
        assertThat(planteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePlante() {
        // Initialize the database
        planteRepository.save(plante).block();

        int databaseSizeBeforeDelete = planteRepository.findAll().collectList().block().size();

        // Delete the plante
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, plante.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Plante> planteList = planteRepository.findAll().collectList().block();
        assertThat(planteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
