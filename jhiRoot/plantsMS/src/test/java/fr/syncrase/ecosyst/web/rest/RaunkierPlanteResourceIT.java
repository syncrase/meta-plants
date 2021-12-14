package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.RaunkierPlante;
import fr.syncrase.ecosyst.repository.RaunkierPlanteRepository;
import fr.syncrase.ecosyst.service.criteria.RaunkierPlanteCriteria;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RaunkierPlanteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RaunkierPlanteResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/raunkier-plantes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RaunkierPlanteRepository raunkierPlanteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRaunkierPlanteMockMvc;

    private RaunkierPlante raunkierPlante;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RaunkierPlante createEntity(EntityManager em) {
        RaunkierPlante raunkierPlante = new RaunkierPlante().type(DEFAULT_TYPE);
        return raunkierPlante;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RaunkierPlante createUpdatedEntity(EntityManager em) {
        RaunkierPlante raunkierPlante = new RaunkierPlante().type(UPDATED_TYPE);
        return raunkierPlante;
    }

    @BeforeEach
    public void initTest() {
        raunkierPlante = createEntity(em);
    }

    @Test
    @Transactional
    void createRaunkierPlante() throws Exception {
        int databaseSizeBeforeCreate = raunkierPlanteRepository.findAll().size();
        // Create the RaunkierPlante
        restRaunkierPlanteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(raunkierPlante))
            )
            .andExpect(status().isCreated());

        // Validate the RaunkierPlante in the database
        List<RaunkierPlante> raunkierPlanteList = raunkierPlanteRepository.findAll();
        assertThat(raunkierPlanteList).hasSize(databaseSizeBeforeCreate + 1);
        RaunkierPlante testRaunkierPlante = raunkierPlanteList.get(raunkierPlanteList.size() - 1);
        assertThat(testRaunkierPlante.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void createRaunkierPlanteWithExistingId() throws Exception {
        // Create the RaunkierPlante with an existing ID
        raunkierPlante.setId(1L);

        int databaseSizeBeforeCreate = raunkierPlanteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRaunkierPlanteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(raunkierPlante))
            )
            .andExpect(status().isBadRequest());

        // Validate the RaunkierPlante in the database
        List<RaunkierPlante> raunkierPlanteList = raunkierPlanteRepository.findAll();
        assertThat(raunkierPlanteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = raunkierPlanteRepository.findAll().size();
        // set the field null
        raunkierPlante.setType(null);

        // Create the RaunkierPlante, which fails.

        restRaunkierPlanteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(raunkierPlante))
            )
            .andExpect(status().isBadRequest());

        List<RaunkierPlante> raunkierPlanteList = raunkierPlanteRepository.findAll();
        assertThat(raunkierPlanteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRaunkierPlantes() throws Exception {
        // Initialize the database
        raunkierPlanteRepository.saveAndFlush(raunkierPlante);

        // Get all the raunkierPlanteList
        restRaunkierPlanteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(raunkierPlante.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }

    @Test
    @Transactional
    void getRaunkierPlante() throws Exception {
        // Initialize the database
        raunkierPlanteRepository.saveAndFlush(raunkierPlante);

        // Get the raunkierPlante
        restRaunkierPlanteMockMvc
            .perform(get(ENTITY_API_URL_ID, raunkierPlante.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(raunkierPlante.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }

    @Test
    @Transactional
    void getRaunkierPlantesByIdFiltering() throws Exception {
        // Initialize the database
        raunkierPlanteRepository.saveAndFlush(raunkierPlante);

        Long id = raunkierPlante.getId();

        defaultRaunkierPlanteShouldBeFound("id.equals=" + id);
        defaultRaunkierPlanteShouldNotBeFound("id.notEquals=" + id);

        defaultRaunkierPlanteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRaunkierPlanteShouldNotBeFound("id.greaterThan=" + id);

        defaultRaunkierPlanteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRaunkierPlanteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRaunkierPlantesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        raunkierPlanteRepository.saveAndFlush(raunkierPlante);

        // Get all the raunkierPlanteList where type equals to DEFAULT_TYPE
        defaultRaunkierPlanteShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the raunkierPlanteList where type equals to UPDATED_TYPE
        defaultRaunkierPlanteShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllRaunkierPlantesByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        raunkierPlanteRepository.saveAndFlush(raunkierPlante);

        // Get all the raunkierPlanteList where type not equals to DEFAULT_TYPE
        defaultRaunkierPlanteShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the raunkierPlanteList where type not equals to UPDATED_TYPE
        defaultRaunkierPlanteShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllRaunkierPlantesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        raunkierPlanteRepository.saveAndFlush(raunkierPlante);

        // Get all the raunkierPlanteList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultRaunkierPlanteShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the raunkierPlanteList where type equals to UPDATED_TYPE
        defaultRaunkierPlanteShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllRaunkierPlantesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        raunkierPlanteRepository.saveAndFlush(raunkierPlante);

        // Get all the raunkierPlanteList where type is not null
        defaultRaunkierPlanteShouldBeFound("type.specified=true");

        // Get all the raunkierPlanteList where type is null
        defaultRaunkierPlanteShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllRaunkierPlantesByTypeContainsSomething() throws Exception {
        // Initialize the database
        raunkierPlanteRepository.saveAndFlush(raunkierPlante);

        // Get all the raunkierPlanteList where type contains DEFAULT_TYPE
        defaultRaunkierPlanteShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the raunkierPlanteList where type contains UPDATED_TYPE
        defaultRaunkierPlanteShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllRaunkierPlantesByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        raunkierPlanteRepository.saveAndFlush(raunkierPlante);

        // Get all the raunkierPlanteList where type does not contain DEFAULT_TYPE
        defaultRaunkierPlanteShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the raunkierPlanteList where type does not contain UPDATED_TYPE
        defaultRaunkierPlanteShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRaunkierPlanteShouldBeFound(String filter) throws Exception {
        restRaunkierPlanteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(raunkierPlante.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));

        // Check, that the count call also returns 1
        restRaunkierPlanteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRaunkierPlanteShouldNotBeFound(String filter) throws Exception {
        restRaunkierPlanteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRaunkierPlanteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRaunkierPlante() throws Exception {
        // Get the raunkierPlante
        restRaunkierPlanteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRaunkierPlante() throws Exception {
        // Initialize the database
        raunkierPlanteRepository.saveAndFlush(raunkierPlante);

        int databaseSizeBeforeUpdate = raunkierPlanteRepository.findAll().size();

        // Update the raunkierPlante
        RaunkierPlante updatedRaunkierPlante = raunkierPlanteRepository.findById(raunkierPlante.getId()).get();
        // Disconnect from session so that the updates on updatedRaunkierPlante are not directly saved in db
        em.detach(updatedRaunkierPlante);
        updatedRaunkierPlante.type(UPDATED_TYPE);

        restRaunkierPlanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRaunkierPlante.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRaunkierPlante))
            )
            .andExpect(status().isOk());

        // Validate the RaunkierPlante in the database
        List<RaunkierPlante> raunkierPlanteList = raunkierPlanteRepository.findAll();
        assertThat(raunkierPlanteList).hasSize(databaseSizeBeforeUpdate);
        RaunkierPlante testRaunkierPlante = raunkierPlanteList.get(raunkierPlanteList.size() - 1);
        assertThat(testRaunkierPlante.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingRaunkierPlante() throws Exception {
        int databaseSizeBeforeUpdate = raunkierPlanteRepository.findAll().size();
        raunkierPlante.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRaunkierPlanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, raunkierPlante.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(raunkierPlante))
            )
            .andExpect(status().isBadRequest());

        // Validate the RaunkierPlante in the database
        List<RaunkierPlante> raunkierPlanteList = raunkierPlanteRepository.findAll();
        assertThat(raunkierPlanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRaunkierPlante() throws Exception {
        int databaseSizeBeforeUpdate = raunkierPlanteRepository.findAll().size();
        raunkierPlante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRaunkierPlanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(raunkierPlante))
            )
            .andExpect(status().isBadRequest());

        // Validate the RaunkierPlante in the database
        List<RaunkierPlante> raunkierPlanteList = raunkierPlanteRepository.findAll();
        assertThat(raunkierPlanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRaunkierPlante() throws Exception {
        int databaseSizeBeforeUpdate = raunkierPlanteRepository.findAll().size();
        raunkierPlante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRaunkierPlanteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(raunkierPlante)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RaunkierPlante in the database
        List<RaunkierPlante> raunkierPlanteList = raunkierPlanteRepository.findAll();
        assertThat(raunkierPlanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRaunkierPlanteWithPatch() throws Exception {
        // Initialize the database
        raunkierPlanteRepository.saveAndFlush(raunkierPlante);

        int databaseSizeBeforeUpdate = raunkierPlanteRepository.findAll().size();

        // Update the raunkierPlante using partial update
        RaunkierPlante partialUpdatedRaunkierPlante = new RaunkierPlante();
        partialUpdatedRaunkierPlante.setId(raunkierPlante.getId());

        restRaunkierPlanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRaunkierPlante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRaunkierPlante))
            )
            .andExpect(status().isOk());

        // Validate the RaunkierPlante in the database
        List<RaunkierPlante> raunkierPlanteList = raunkierPlanteRepository.findAll();
        assertThat(raunkierPlanteList).hasSize(databaseSizeBeforeUpdate);
        RaunkierPlante testRaunkierPlante = raunkierPlanteList.get(raunkierPlanteList.size() - 1);
        assertThat(testRaunkierPlante.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateRaunkierPlanteWithPatch() throws Exception {
        // Initialize the database
        raunkierPlanteRepository.saveAndFlush(raunkierPlante);

        int databaseSizeBeforeUpdate = raunkierPlanteRepository.findAll().size();

        // Update the raunkierPlante using partial update
        RaunkierPlante partialUpdatedRaunkierPlante = new RaunkierPlante();
        partialUpdatedRaunkierPlante.setId(raunkierPlante.getId());

        partialUpdatedRaunkierPlante.type(UPDATED_TYPE);

        restRaunkierPlanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRaunkierPlante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRaunkierPlante))
            )
            .andExpect(status().isOk());

        // Validate the RaunkierPlante in the database
        List<RaunkierPlante> raunkierPlanteList = raunkierPlanteRepository.findAll();
        assertThat(raunkierPlanteList).hasSize(databaseSizeBeforeUpdate);
        RaunkierPlante testRaunkierPlante = raunkierPlanteList.get(raunkierPlanteList.size() - 1);
        assertThat(testRaunkierPlante.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingRaunkierPlante() throws Exception {
        int databaseSizeBeforeUpdate = raunkierPlanteRepository.findAll().size();
        raunkierPlante.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRaunkierPlanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, raunkierPlante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(raunkierPlante))
            )
            .andExpect(status().isBadRequest());

        // Validate the RaunkierPlante in the database
        List<RaunkierPlante> raunkierPlanteList = raunkierPlanteRepository.findAll();
        assertThat(raunkierPlanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRaunkierPlante() throws Exception {
        int databaseSizeBeforeUpdate = raunkierPlanteRepository.findAll().size();
        raunkierPlante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRaunkierPlanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(raunkierPlante))
            )
            .andExpect(status().isBadRequest());

        // Validate the RaunkierPlante in the database
        List<RaunkierPlante> raunkierPlanteList = raunkierPlanteRepository.findAll();
        assertThat(raunkierPlanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRaunkierPlante() throws Exception {
        int databaseSizeBeforeUpdate = raunkierPlanteRepository.findAll().size();
        raunkierPlante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRaunkierPlanteMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(raunkierPlante))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RaunkierPlante in the database
        List<RaunkierPlante> raunkierPlanteList = raunkierPlanteRepository.findAll();
        assertThat(raunkierPlanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRaunkierPlante() throws Exception {
        // Initialize the database
        raunkierPlanteRepository.saveAndFlush(raunkierPlante);

        int databaseSizeBeforeDelete = raunkierPlanteRepository.findAll().size();

        // Delete the raunkierPlante
        restRaunkierPlanteMockMvc
            .perform(delete(ENTITY_API_URL_ID, raunkierPlante.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RaunkierPlante> raunkierPlanteList = raunkierPlanteRepository.findAll();
        assertThat(raunkierPlanteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
