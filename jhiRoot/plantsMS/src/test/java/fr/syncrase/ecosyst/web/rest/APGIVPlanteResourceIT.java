package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.APGIVPlante;
import fr.syncrase.ecosyst.repository.APGIVPlanteRepository;
import fr.syncrase.ecosyst.service.criteria.APGIVPlanteCriteria;
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
 * Integration tests for the {@link APGIVPlanteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class APGIVPlanteResourceIT {

    private static final String DEFAULT_ORDRE = "AAAAAAAAAA";
    private static final String UPDATED_ORDRE = "BBBBBBBBBB";

    private static final String DEFAULT_FAMILLE = "AAAAAAAAAA";
    private static final String UPDATED_FAMILLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/apgiv-plantes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private APGIVPlanteRepository aPGIVPlanteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAPGIVPlanteMockMvc;

    private APGIVPlante aPGIVPlante;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static APGIVPlante createEntity(EntityManager em) {
        APGIVPlante aPGIVPlante = new APGIVPlante().ordre(DEFAULT_ORDRE).famille(DEFAULT_FAMILLE);
        return aPGIVPlante;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static APGIVPlante createUpdatedEntity(EntityManager em) {
        APGIVPlante aPGIVPlante = new APGIVPlante().ordre(UPDATED_ORDRE).famille(UPDATED_FAMILLE);
        return aPGIVPlante;
    }

    @BeforeEach
    public void initTest() {
        aPGIVPlante = createEntity(em);
    }

    @Test
    @Transactional
    void createAPGIVPlante() throws Exception {
        int databaseSizeBeforeCreate = aPGIVPlanteRepository.findAll().size();
        // Create the APGIVPlante
        restAPGIVPlanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aPGIVPlante)))
            .andExpect(status().isCreated());

        // Validate the APGIVPlante in the database
        List<APGIVPlante> aPGIVPlanteList = aPGIVPlanteRepository.findAll();
        assertThat(aPGIVPlanteList).hasSize(databaseSizeBeforeCreate + 1);
        APGIVPlante testAPGIVPlante = aPGIVPlanteList.get(aPGIVPlanteList.size() - 1);
        assertThat(testAPGIVPlante.getOrdre()).isEqualTo(DEFAULT_ORDRE);
        assertThat(testAPGIVPlante.getFamille()).isEqualTo(DEFAULT_FAMILLE);
    }

    @Test
    @Transactional
    void createAPGIVPlanteWithExistingId() throws Exception {
        // Create the APGIVPlante with an existing ID
        aPGIVPlante.setId(1L);

        int databaseSizeBeforeCreate = aPGIVPlanteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAPGIVPlanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aPGIVPlante)))
            .andExpect(status().isBadRequest());

        // Validate the APGIVPlante in the database
        List<APGIVPlante> aPGIVPlanteList = aPGIVPlanteRepository.findAll();
        assertThat(aPGIVPlanteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrdreIsRequired() throws Exception {
        int databaseSizeBeforeTest = aPGIVPlanteRepository.findAll().size();
        // set the field null
        aPGIVPlante.setOrdre(null);

        // Create the APGIVPlante, which fails.

        restAPGIVPlanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aPGIVPlante)))
            .andExpect(status().isBadRequest());

        List<APGIVPlante> aPGIVPlanteList = aPGIVPlanteRepository.findAll();
        assertThat(aPGIVPlanteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFamilleIsRequired() throws Exception {
        int databaseSizeBeforeTest = aPGIVPlanteRepository.findAll().size();
        // set the field null
        aPGIVPlante.setFamille(null);

        // Create the APGIVPlante, which fails.

        restAPGIVPlanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aPGIVPlante)))
            .andExpect(status().isBadRequest());

        List<APGIVPlante> aPGIVPlanteList = aPGIVPlanteRepository.findAll();
        assertThat(aPGIVPlanteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAPGIVPlantes() throws Exception {
        // Initialize the database
        aPGIVPlanteRepository.saveAndFlush(aPGIVPlante);

        // Get all the aPGIVPlanteList
        restAPGIVPlanteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aPGIVPlante.getId().intValue())))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)))
            .andExpect(jsonPath("$.[*].famille").value(hasItem(DEFAULT_FAMILLE)));
    }

    @Test
    @Transactional
    void getAPGIVPlante() throws Exception {
        // Initialize the database
        aPGIVPlanteRepository.saveAndFlush(aPGIVPlante);

        // Get the aPGIVPlante
        restAPGIVPlanteMockMvc
            .perform(get(ENTITY_API_URL_ID, aPGIVPlante.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aPGIVPlante.getId().intValue()))
            .andExpect(jsonPath("$.ordre").value(DEFAULT_ORDRE))
            .andExpect(jsonPath("$.famille").value(DEFAULT_FAMILLE));
    }

    @Test
    @Transactional
    void getAPGIVPlantesByIdFiltering() throws Exception {
        // Initialize the database
        aPGIVPlanteRepository.saveAndFlush(aPGIVPlante);

        Long id = aPGIVPlante.getId();

        defaultAPGIVPlanteShouldBeFound("id.equals=" + id);
        defaultAPGIVPlanteShouldNotBeFound("id.notEquals=" + id);

        defaultAPGIVPlanteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAPGIVPlanteShouldNotBeFound("id.greaterThan=" + id);

        defaultAPGIVPlanteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAPGIVPlanteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAPGIVPlantesByOrdreIsEqualToSomething() throws Exception {
        // Initialize the database
        aPGIVPlanteRepository.saveAndFlush(aPGIVPlante);

        // Get all the aPGIVPlanteList where ordre equals to DEFAULT_ORDRE
        defaultAPGIVPlanteShouldBeFound("ordre.equals=" + DEFAULT_ORDRE);

        // Get all the aPGIVPlanteList where ordre equals to UPDATED_ORDRE
        defaultAPGIVPlanteShouldNotBeFound("ordre.equals=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllAPGIVPlantesByOrdreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        aPGIVPlanteRepository.saveAndFlush(aPGIVPlante);

        // Get all the aPGIVPlanteList where ordre not equals to DEFAULT_ORDRE
        defaultAPGIVPlanteShouldNotBeFound("ordre.notEquals=" + DEFAULT_ORDRE);

        // Get all the aPGIVPlanteList where ordre not equals to UPDATED_ORDRE
        defaultAPGIVPlanteShouldBeFound("ordre.notEquals=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllAPGIVPlantesByOrdreIsInShouldWork() throws Exception {
        // Initialize the database
        aPGIVPlanteRepository.saveAndFlush(aPGIVPlante);

        // Get all the aPGIVPlanteList where ordre in DEFAULT_ORDRE or UPDATED_ORDRE
        defaultAPGIVPlanteShouldBeFound("ordre.in=" + DEFAULT_ORDRE + "," + UPDATED_ORDRE);

        // Get all the aPGIVPlanteList where ordre equals to UPDATED_ORDRE
        defaultAPGIVPlanteShouldNotBeFound("ordre.in=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllAPGIVPlantesByOrdreIsNullOrNotNull() throws Exception {
        // Initialize the database
        aPGIVPlanteRepository.saveAndFlush(aPGIVPlante);

        // Get all the aPGIVPlanteList where ordre is not null
        defaultAPGIVPlanteShouldBeFound("ordre.specified=true");

        // Get all the aPGIVPlanteList where ordre is null
        defaultAPGIVPlanteShouldNotBeFound("ordre.specified=false");
    }

    @Test
    @Transactional
    void getAllAPGIVPlantesByOrdreContainsSomething() throws Exception {
        // Initialize the database
        aPGIVPlanteRepository.saveAndFlush(aPGIVPlante);

        // Get all the aPGIVPlanteList where ordre contains DEFAULT_ORDRE
        defaultAPGIVPlanteShouldBeFound("ordre.contains=" + DEFAULT_ORDRE);

        // Get all the aPGIVPlanteList where ordre contains UPDATED_ORDRE
        defaultAPGIVPlanteShouldNotBeFound("ordre.contains=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllAPGIVPlantesByOrdreNotContainsSomething() throws Exception {
        // Initialize the database
        aPGIVPlanteRepository.saveAndFlush(aPGIVPlante);

        // Get all the aPGIVPlanteList where ordre does not contain DEFAULT_ORDRE
        defaultAPGIVPlanteShouldNotBeFound("ordre.doesNotContain=" + DEFAULT_ORDRE);

        // Get all the aPGIVPlanteList where ordre does not contain UPDATED_ORDRE
        defaultAPGIVPlanteShouldBeFound("ordre.doesNotContain=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllAPGIVPlantesByFamilleIsEqualToSomething() throws Exception {
        // Initialize the database
        aPGIVPlanteRepository.saveAndFlush(aPGIVPlante);

        // Get all the aPGIVPlanteList where famille equals to DEFAULT_FAMILLE
        defaultAPGIVPlanteShouldBeFound("famille.equals=" + DEFAULT_FAMILLE);

        // Get all the aPGIVPlanteList where famille equals to UPDATED_FAMILLE
        defaultAPGIVPlanteShouldNotBeFound("famille.equals=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void getAllAPGIVPlantesByFamilleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        aPGIVPlanteRepository.saveAndFlush(aPGIVPlante);

        // Get all the aPGIVPlanteList where famille not equals to DEFAULT_FAMILLE
        defaultAPGIVPlanteShouldNotBeFound("famille.notEquals=" + DEFAULT_FAMILLE);

        // Get all the aPGIVPlanteList where famille not equals to UPDATED_FAMILLE
        defaultAPGIVPlanteShouldBeFound("famille.notEquals=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void getAllAPGIVPlantesByFamilleIsInShouldWork() throws Exception {
        // Initialize the database
        aPGIVPlanteRepository.saveAndFlush(aPGIVPlante);

        // Get all the aPGIVPlanteList where famille in DEFAULT_FAMILLE or UPDATED_FAMILLE
        defaultAPGIVPlanteShouldBeFound("famille.in=" + DEFAULT_FAMILLE + "," + UPDATED_FAMILLE);

        // Get all the aPGIVPlanteList where famille equals to UPDATED_FAMILLE
        defaultAPGIVPlanteShouldNotBeFound("famille.in=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void getAllAPGIVPlantesByFamilleIsNullOrNotNull() throws Exception {
        // Initialize the database
        aPGIVPlanteRepository.saveAndFlush(aPGIVPlante);

        // Get all the aPGIVPlanteList where famille is not null
        defaultAPGIVPlanteShouldBeFound("famille.specified=true");

        // Get all the aPGIVPlanteList where famille is null
        defaultAPGIVPlanteShouldNotBeFound("famille.specified=false");
    }

    @Test
    @Transactional
    void getAllAPGIVPlantesByFamilleContainsSomething() throws Exception {
        // Initialize the database
        aPGIVPlanteRepository.saveAndFlush(aPGIVPlante);

        // Get all the aPGIVPlanteList where famille contains DEFAULT_FAMILLE
        defaultAPGIVPlanteShouldBeFound("famille.contains=" + DEFAULT_FAMILLE);

        // Get all the aPGIVPlanteList where famille contains UPDATED_FAMILLE
        defaultAPGIVPlanteShouldNotBeFound("famille.contains=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void getAllAPGIVPlantesByFamilleNotContainsSomething() throws Exception {
        // Initialize the database
        aPGIVPlanteRepository.saveAndFlush(aPGIVPlante);

        // Get all the aPGIVPlanteList where famille does not contain DEFAULT_FAMILLE
        defaultAPGIVPlanteShouldNotBeFound("famille.doesNotContain=" + DEFAULT_FAMILLE);

        // Get all the aPGIVPlanteList where famille does not contain UPDATED_FAMILLE
        defaultAPGIVPlanteShouldBeFound("famille.doesNotContain=" + UPDATED_FAMILLE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAPGIVPlanteShouldBeFound(String filter) throws Exception {
        restAPGIVPlanteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aPGIVPlante.getId().intValue())))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)))
            .andExpect(jsonPath("$.[*].famille").value(hasItem(DEFAULT_FAMILLE)));

        // Check, that the count call also returns 1
        restAPGIVPlanteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAPGIVPlanteShouldNotBeFound(String filter) throws Exception {
        restAPGIVPlanteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAPGIVPlanteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAPGIVPlante() throws Exception {
        // Get the aPGIVPlante
        restAPGIVPlanteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAPGIVPlante() throws Exception {
        // Initialize the database
        aPGIVPlanteRepository.saveAndFlush(aPGIVPlante);

        int databaseSizeBeforeUpdate = aPGIVPlanteRepository.findAll().size();

        // Update the aPGIVPlante
        APGIVPlante updatedAPGIVPlante = aPGIVPlanteRepository.findById(aPGIVPlante.getId()).get();
        // Disconnect from session so that the updates on updatedAPGIVPlante are not directly saved in db
        em.detach(updatedAPGIVPlante);
        updatedAPGIVPlante.ordre(UPDATED_ORDRE).famille(UPDATED_FAMILLE);

        restAPGIVPlanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAPGIVPlante.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAPGIVPlante))
            )
            .andExpect(status().isOk());

        // Validate the APGIVPlante in the database
        List<APGIVPlante> aPGIVPlanteList = aPGIVPlanteRepository.findAll();
        assertThat(aPGIVPlanteList).hasSize(databaseSizeBeforeUpdate);
        APGIVPlante testAPGIVPlante = aPGIVPlanteList.get(aPGIVPlanteList.size() - 1);
        assertThat(testAPGIVPlante.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testAPGIVPlante.getFamille()).isEqualTo(UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void putNonExistingAPGIVPlante() throws Exception {
        int databaseSizeBeforeUpdate = aPGIVPlanteRepository.findAll().size();
        aPGIVPlante.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAPGIVPlanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aPGIVPlante.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aPGIVPlante))
            )
            .andExpect(status().isBadRequest());

        // Validate the APGIVPlante in the database
        List<APGIVPlante> aPGIVPlanteList = aPGIVPlanteRepository.findAll();
        assertThat(aPGIVPlanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAPGIVPlante() throws Exception {
        int databaseSizeBeforeUpdate = aPGIVPlanteRepository.findAll().size();
        aPGIVPlante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAPGIVPlanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aPGIVPlante))
            )
            .andExpect(status().isBadRequest());

        // Validate the APGIVPlante in the database
        List<APGIVPlante> aPGIVPlanteList = aPGIVPlanteRepository.findAll();
        assertThat(aPGIVPlanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAPGIVPlante() throws Exception {
        int databaseSizeBeforeUpdate = aPGIVPlanteRepository.findAll().size();
        aPGIVPlante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAPGIVPlanteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aPGIVPlante)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the APGIVPlante in the database
        List<APGIVPlante> aPGIVPlanteList = aPGIVPlanteRepository.findAll();
        assertThat(aPGIVPlanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAPGIVPlanteWithPatch() throws Exception {
        // Initialize the database
        aPGIVPlanteRepository.saveAndFlush(aPGIVPlante);

        int databaseSizeBeforeUpdate = aPGIVPlanteRepository.findAll().size();

        // Update the aPGIVPlante using partial update
        APGIVPlante partialUpdatedAPGIVPlante = new APGIVPlante();
        partialUpdatedAPGIVPlante.setId(aPGIVPlante.getId());

        restAPGIVPlanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAPGIVPlante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAPGIVPlante))
            )
            .andExpect(status().isOk());

        // Validate the APGIVPlante in the database
        List<APGIVPlante> aPGIVPlanteList = aPGIVPlanteRepository.findAll();
        assertThat(aPGIVPlanteList).hasSize(databaseSizeBeforeUpdate);
        APGIVPlante testAPGIVPlante = aPGIVPlanteList.get(aPGIVPlanteList.size() - 1);
        assertThat(testAPGIVPlante.getOrdre()).isEqualTo(DEFAULT_ORDRE);
        assertThat(testAPGIVPlante.getFamille()).isEqualTo(DEFAULT_FAMILLE);
    }

    @Test
    @Transactional
    void fullUpdateAPGIVPlanteWithPatch() throws Exception {
        // Initialize the database
        aPGIVPlanteRepository.saveAndFlush(aPGIVPlante);

        int databaseSizeBeforeUpdate = aPGIVPlanteRepository.findAll().size();

        // Update the aPGIVPlante using partial update
        APGIVPlante partialUpdatedAPGIVPlante = new APGIVPlante();
        partialUpdatedAPGIVPlante.setId(aPGIVPlante.getId());

        partialUpdatedAPGIVPlante.ordre(UPDATED_ORDRE).famille(UPDATED_FAMILLE);

        restAPGIVPlanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAPGIVPlante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAPGIVPlante))
            )
            .andExpect(status().isOk());

        // Validate the APGIVPlante in the database
        List<APGIVPlante> aPGIVPlanteList = aPGIVPlanteRepository.findAll();
        assertThat(aPGIVPlanteList).hasSize(databaseSizeBeforeUpdate);
        APGIVPlante testAPGIVPlante = aPGIVPlanteList.get(aPGIVPlanteList.size() - 1);
        assertThat(testAPGIVPlante.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testAPGIVPlante.getFamille()).isEqualTo(UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void patchNonExistingAPGIVPlante() throws Exception {
        int databaseSizeBeforeUpdate = aPGIVPlanteRepository.findAll().size();
        aPGIVPlante.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAPGIVPlanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aPGIVPlante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aPGIVPlante))
            )
            .andExpect(status().isBadRequest());

        // Validate the APGIVPlante in the database
        List<APGIVPlante> aPGIVPlanteList = aPGIVPlanteRepository.findAll();
        assertThat(aPGIVPlanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAPGIVPlante() throws Exception {
        int databaseSizeBeforeUpdate = aPGIVPlanteRepository.findAll().size();
        aPGIVPlante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAPGIVPlanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aPGIVPlante))
            )
            .andExpect(status().isBadRequest());

        // Validate the APGIVPlante in the database
        List<APGIVPlante> aPGIVPlanteList = aPGIVPlanteRepository.findAll();
        assertThat(aPGIVPlanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAPGIVPlante() throws Exception {
        int databaseSizeBeforeUpdate = aPGIVPlanteRepository.findAll().size();
        aPGIVPlante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAPGIVPlanteMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(aPGIVPlante))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the APGIVPlante in the database
        List<APGIVPlante> aPGIVPlanteList = aPGIVPlanteRepository.findAll();
        assertThat(aPGIVPlanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAPGIVPlante() throws Exception {
        // Initialize the database
        aPGIVPlanteRepository.saveAndFlush(aPGIVPlante);

        int databaseSizeBeforeDelete = aPGIVPlanteRepository.findAll().size();

        // Delete the aPGIVPlante
        restAPGIVPlanteMockMvc
            .perform(delete(ENTITY_API_URL_ID, aPGIVPlante.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<APGIVPlante> aPGIVPlanteList = aPGIVPlanteRepository.findAll();
        assertThat(aPGIVPlanteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
