package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.APGIIPlante;
import fr.syncrase.ecosyst.repository.APGIIPlanteRepository;
import fr.syncrase.ecosyst.service.criteria.APGIIPlanteCriteria;
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
 * Integration tests for the {@link APGIIPlanteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class APGIIPlanteResourceIT {

    private static final String DEFAULT_ORDRE = "AAAAAAAAAA";
    private static final String UPDATED_ORDRE = "BBBBBBBBBB";

    private static final String DEFAULT_FAMILLE = "AAAAAAAAAA";
    private static final String UPDATED_FAMILLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/apgii-plantes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private APGIIPlanteRepository aPGIIPlanteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAPGIIPlanteMockMvc;

    private APGIIPlante aPGIIPlante;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static APGIIPlante createEntity(EntityManager em) {
        APGIIPlante aPGIIPlante = new APGIIPlante().ordre(DEFAULT_ORDRE).famille(DEFAULT_FAMILLE);
        return aPGIIPlante;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static APGIIPlante createUpdatedEntity(EntityManager em) {
        APGIIPlante aPGIIPlante = new APGIIPlante().ordre(UPDATED_ORDRE).famille(UPDATED_FAMILLE);
        return aPGIIPlante;
    }

    @BeforeEach
    public void initTest() {
        aPGIIPlante = createEntity(em);
    }

    @Test
    @Transactional
    void createAPGIIPlante() throws Exception {
        int databaseSizeBeforeCreate = aPGIIPlanteRepository.findAll().size();
        // Create the APGIIPlante
        restAPGIIPlanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aPGIIPlante)))
            .andExpect(status().isCreated());

        // Validate the APGIIPlante in the database
        List<APGIIPlante> aPGIIPlanteList = aPGIIPlanteRepository.findAll();
        assertThat(aPGIIPlanteList).hasSize(databaseSizeBeforeCreate + 1);
        APGIIPlante testAPGIIPlante = aPGIIPlanteList.get(aPGIIPlanteList.size() - 1);
        assertThat(testAPGIIPlante.getOrdre()).isEqualTo(DEFAULT_ORDRE);
        assertThat(testAPGIIPlante.getFamille()).isEqualTo(DEFAULT_FAMILLE);
    }

    @Test
    @Transactional
    void createAPGIIPlanteWithExistingId() throws Exception {
        // Create the APGIIPlante with an existing ID
        aPGIIPlante.setId(1L);

        int databaseSizeBeforeCreate = aPGIIPlanteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAPGIIPlanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aPGIIPlante)))
            .andExpect(status().isBadRequest());

        // Validate the APGIIPlante in the database
        List<APGIIPlante> aPGIIPlanteList = aPGIIPlanteRepository.findAll();
        assertThat(aPGIIPlanteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrdreIsRequired() throws Exception {
        int databaseSizeBeforeTest = aPGIIPlanteRepository.findAll().size();
        // set the field null
        aPGIIPlante.setOrdre(null);

        // Create the APGIIPlante, which fails.

        restAPGIIPlanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aPGIIPlante)))
            .andExpect(status().isBadRequest());

        List<APGIIPlante> aPGIIPlanteList = aPGIIPlanteRepository.findAll();
        assertThat(aPGIIPlanteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFamilleIsRequired() throws Exception {
        int databaseSizeBeforeTest = aPGIIPlanteRepository.findAll().size();
        // set the field null
        aPGIIPlante.setFamille(null);

        // Create the APGIIPlante, which fails.

        restAPGIIPlanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aPGIIPlante)))
            .andExpect(status().isBadRequest());

        List<APGIIPlante> aPGIIPlanteList = aPGIIPlanteRepository.findAll();
        assertThat(aPGIIPlanteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAPGIIPlantes() throws Exception {
        // Initialize the database
        aPGIIPlanteRepository.saveAndFlush(aPGIIPlante);

        // Get all the aPGIIPlanteList
        restAPGIIPlanteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aPGIIPlante.getId().intValue())))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)))
            .andExpect(jsonPath("$.[*].famille").value(hasItem(DEFAULT_FAMILLE)));
    }

    @Test
    @Transactional
    void getAPGIIPlante() throws Exception {
        // Initialize the database
        aPGIIPlanteRepository.saveAndFlush(aPGIIPlante);

        // Get the aPGIIPlante
        restAPGIIPlanteMockMvc
            .perform(get(ENTITY_API_URL_ID, aPGIIPlante.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aPGIIPlante.getId().intValue()))
            .andExpect(jsonPath("$.ordre").value(DEFAULT_ORDRE))
            .andExpect(jsonPath("$.famille").value(DEFAULT_FAMILLE));
    }

    @Test
    @Transactional
    void getAPGIIPlantesByIdFiltering() throws Exception {
        // Initialize the database
        aPGIIPlanteRepository.saveAndFlush(aPGIIPlante);

        Long id = aPGIIPlante.getId();

        defaultAPGIIPlanteShouldBeFound("id.equals=" + id);
        defaultAPGIIPlanteShouldNotBeFound("id.notEquals=" + id);

        defaultAPGIIPlanteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAPGIIPlanteShouldNotBeFound("id.greaterThan=" + id);

        defaultAPGIIPlanteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAPGIIPlanteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAPGIIPlantesByOrdreIsEqualToSomething() throws Exception {
        // Initialize the database
        aPGIIPlanteRepository.saveAndFlush(aPGIIPlante);

        // Get all the aPGIIPlanteList where ordre equals to DEFAULT_ORDRE
        defaultAPGIIPlanteShouldBeFound("ordre.equals=" + DEFAULT_ORDRE);

        // Get all the aPGIIPlanteList where ordre equals to UPDATED_ORDRE
        defaultAPGIIPlanteShouldNotBeFound("ordre.equals=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllAPGIIPlantesByOrdreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        aPGIIPlanteRepository.saveAndFlush(aPGIIPlante);

        // Get all the aPGIIPlanteList where ordre not equals to DEFAULT_ORDRE
        defaultAPGIIPlanteShouldNotBeFound("ordre.notEquals=" + DEFAULT_ORDRE);

        // Get all the aPGIIPlanteList where ordre not equals to UPDATED_ORDRE
        defaultAPGIIPlanteShouldBeFound("ordre.notEquals=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllAPGIIPlantesByOrdreIsInShouldWork() throws Exception {
        // Initialize the database
        aPGIIPlanteRepository.saveAndFlush(aPGIIPlante);

        // Get all the aPGIIPlanteList where ordre in DEFAULT_ORDRE or UPDATED_ORDRE
        defaultAPGIIPlanteShouldBeFound("ordre.in=" + DEFAULT_ORDRE + "," + UPDATED_ORDRE);

        // Get all the aPGIIPlanteList where ordre equals to UPDATED_ORDRE
        defaultAPGIIPlanteShouldNotBeFound("ordre.in=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllAPGIIPlantesByOrdreIsNullOrNotNull() throws Exception {
        // Initialize the database
        aPGIIPlanteRepository.saveAndFlush(aPGIIPlante);

        // Get all the aPGIIPlanteList where ordre is not null
        defaultAPGIIPlanteShouldBeFound("ordre.specified=true");

        // Get all the aPGIIPlanteList where ordre is null
        defaultAPGIIPlanteShouldNotBeFound("ordre.specified=false");
    }

    @Test
    @Transactional
    void getAllAPGIIPlantesByOrdreContainsSomething() throws Exception {
        // Initialize the database
        aPGIIPlanteRepository.saveAndFlush(aPGIIPlante);

        // Get all the aPGIIPlanteList where ordre contains DEFAULT_ORDRE
        defaultAPGIIPlanteShouldBeFound("ordre.contains=" + DEFAULT_ORDRE);

        // Get all the aPGIIPlanteList where ordre contains UPDATED_ORDRE
        defaultAPGIIPlanteShouldNotBeFound("ordre.contains=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllAPGIIPlantesByOrdreNotContainsSomething() throws Exception {
        // Initialize the database
        aPGIIPlanteRepository.saveAndFlush(aPGIIPlante);

        // Get all the aPGIIPlanteList where ordre does not contain DEFAULT_ORDRE
        defaultAPGIIPlanteShouldNotBeFound("ordre.doesNotContain=" + DEFAULT_ORDRE);

        // Get all the aPGIIPlanteList where ordre does not contain UPDATED_ORDRE
        defaultAPGIIPlanteShouldBeFound("ordre.doesNotContain=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllAPGIIPlantesByFamilleIsEqualToSomething() throws Exception {
        // Initialize the database
        aPGIIPlanteRepository.saveAndFlush(aPGIIPlante);

        // Get all the aPGIIPlanteList where famille equals to DEFAULT_FAMILLE
        defaultAPGIIPlanteShouldBeFound("famille.equals=" + DEFAULT_FAMILLE);

        // Get all the aPGIIPlanteList where famille equals to UPDATED_FAMILLE
        defaultAPGIIPlanteShouldNotBeFound("famille.equals=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void getAllAPGIIPlantesByFamilleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        aPGIIPlanteRepository.saveAndFlush(aPGIIPlante);

        // Get all the aPGIIPlanteList where famille not equals to DEFAULT_FAMILLE
        defaultAPGIIPlanteShouldNotBeFound("famille.notEquals=" + DEFAULT_FAMILLE);

        // Get all the aPGIIPlanteList where famille not equals to UPDATED_FAMILLE
        defaultAPGIIPlanteShouldBeFound("famille.notEquals=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void getAllAPGIIPlantesByFamilleIsInShouldWork() throws Exception {
        // Initialize the database
        aPGIIPlanteRepository.saveAndFlush(aPGIIPlante);

        // Get all the aPGIIPlanteList where famille in DEFAULT_FAMILLE or UPDATED_FAMILLE
        defaultAPGIIPlanteShouldBeFound("famille.in=" + DEFAULT_FAMILLE + "," + UPDATED_FAMILLE);

        // Get all the aPGIIPlanteList where famille equals to UPDATED_FAMILLE
        defaultAPGIIPlanteShouldNotBeFound("famille.in=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void getAllAPGIIPlantesByFamilleIsNullOrNotNull() throws Exception {
        // Initialize the database
        aPGIIPlanteRepository.saveAndFlush(aPGIIPlante);

        // Get all the aPGIIPlanteList where famille is not null
        defaultAPGIIPlanteShouldBeFound("famille.specified=true");

        // Get all the aPGIIPlanteList where famille is null
        defaultAPGIIPlanteShouldNotBeFound("famille.specified=false");
    }

    @Test
    @Transactional
    void getAllAPGIIPlantesByFamilleContainsSomething() throws Exception {
        // Initialize the database
        aPGIIPlanteRepository.saveAndFlush(aPGIIPlante);

        // Get all the aPGIIPlanteList where famille contains DEFAULT_FAMILLE
        defaultAPGIIPlanteShouldBeFound("famille.contains=" + DEFAULT_FAMILLE);

        // Get all the aPGIIPlanteList where famille contains UPDATED_FAMILLE
        defaultAPGIIPlanteShouldNotBeFound("famille.contains=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void getAllAPGIIPlantesByFamilleNotContainsSomething() throws Exception {
        // Initialize the database
        aPGIIPlanteRepository.saveAndFlush(aPGIIPlante);

        // Get all the aPGIIPlanteList where famille does not contain DEFAULT_FAMILLE
        defaultAPGIIPlanteShouldNotBeFound("famille.doesNotContain=" + DEFAULT_FAMILLE);

        // Get all the aPGIIPlanteList where famille does not contain UPDATED_FAMILLE
        defaultAPGIIPlanteShouldBeFound("famille.doesNotContain=" + UPDATED_FAMILLE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAPGIIPlanteShouldBeFound(String filter) throws Exception {
        restAPGIIPlanteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aPGIIPlante.getId().intValue())))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)))
            .andExpect(jsonPath("$.[*].famille").value(hasItem(DEFAULT_FAMILLE)));

        // Check, that the count call also returns 1
        restAPGIIPlanteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAPGIIPlanteShouldNotBeFound(String filter) throws Exception {
        restAPGIIPlanteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAPGIIPlanteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAPGIIPlante() throws Exception {
        // Get the aPGIIPlante
        restAPGIIPlanteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAPGIIPlante() throws Exception {
        // Initialize the database
        aPGIIPlanteRepository.saveAndFlush(aPGIIPlante);

        int databaseSizeBeforeUpdate = aPGIIPlanteRepository.findAll().size();

        // Update the aPGIIPlante
        APGIIPlante updatedAPGIIPlante = aPGIIPlanteRepository.findById(aPGIIPlante.getId()).get();
        // Disconnect from session so that the updates on updatedAPGIIPlante are not directly saved in db
        em.detach(updatedAPGIIPlante);
        updatedAPGIIPlante.ordre(UPDATED_ORDRE).famille(UPDATED_FAMILLE);

        restAPGIIPlanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAPGIIPlante.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAPGIIPlante))
            )
            .andExpect(status().isOk());

        // Validate the APGIIPlante in the database
        List<APGIIPlante> aPGIIPlanteList = aPGIIPlanteRepository.findAll();
        assertThat(aPGIIPlanteList).hasSize(databaseSizeBeforeUpdate);
        APGIIPlante testAPGIIPlante = aPGIIPlanteList.get(aPGIIPlanteList.size() - 1);
        assertThat(testAPGIIPlante.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testAPGIIPlante.getFamille()).isEqualTo(UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void putNonExistingAPGIIPlante() throws Exception {
        int databaseSizeBeforeUpdate = aPGIIPlanteRepository.findAll().size();
        aPGIIPlante.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAPGIIPlanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aPGIIPlante.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aPGIIPlante))
            )
            .andExpect(status().isBadRequest());

        // Validate the APGIIPlante in the database
        List<APGIIPlante> aPGIIPlanteList = aPGIIPlanteRepository.findAll();
        assertThat(aPGIIPlanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAPGIIPlante() throws Exception {
        int databaseSizeBeforeUpdate = aPGIIPlanteRepository.findAll().size();
        aPGIIPlante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAPGIIPlanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aPGIIPlante))
            )
            .andExpect(status().isBadRequest());

        // Validate the APGIIPlante in the database
        List<APGIIPlante> aPGIIPlanteList = aPGIIPlanteRepository.findAll();
        assertThat(aPGIIPlanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAPGIIPlante() throws Exception {
        int databaseSizeBeforeUpdate = aPGIIPlanteRepository.findAll().size();
        aPGIIPlante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAPGIIPlanteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aPGIIPlante)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the APGIIPlante in the database
        List<APGIIPlante> aPGIIPlanteList = aPGIIPlanteRepository.findAll();
        assertThat(aPGIIPlanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAPGIIPlanteWithPatch() throws Exception {
        // Initialize the database
        aPGIIPlanteRepository.saveAndFlush(aPGIIPlante);

        int databaseSizeBeforeUpdate = aPGIIPlanteRepository.findAll().size();

        // Update the aPGIIPlante using partial update
        APGIIPlante partialUpdatedAPGIIPlante = new APGIIPlante();
        partialUpdatedAPGIIPlante.setId(aPGIIPlante.getId());

        partialUpdatedAPGIIPlante.ordre(UPDATED_ORDRE);

        restAPGIIPlanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAPGIIPlante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAPGIIPlante))
            )
            .andExpect(status().isOk());

        // Validate the APGIIPlante in the database
        List<APGIIPlante> aPGIIPlanteList = aPGIIPlanteRepository.findAll();
        assertThat(aPGIIPlanteList).hasSize(databaseSizeBeforeUpdate);
        APGIIPlante testAPGIIPlante = aPGIIPlanteList.get(aPGIIPlanteList.size() - 1);
        assertThat(testAPGIIPlante.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testAPGIIPlante.getFamille()).isEqualTo(DEFAULT_FAMILLE);
    }

    @Test
    @Transactional
    void fullUpdateAPGIIPlanteWithPatch() throws Exception {
        // Initialize the database
        aPGIIPlanteRepository.saveAndFlush(aPGIIPlante);

        int databaseSizeBeforeUpdate = aPGIIPlanteRepository.findAll().size();

        // Update the aPGIIPlante using partial update
        APGIIPlante partialUpdatedAPGIIPlante = new APGIIPlante();
        partialUpdatedAPGIIPlante.setId(aPGIIPlante.getId());

        partialUpdatedAPGIIPlante.ordre(UPDATED_ORDRE).famille(UPDATED_FAMILLE);

        restAPGIIPlanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAPGIIPlante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAPGIIPlante))
            )
            .andExpect(status().isOk());

        // Validate the APGIIPlante in the database
        List<APGIIPlante> aPGIIPlanteList = aPGIIPlanteRepository.findAll();
        assertThat(aPGIIPlanteList).hasSize(databaseSizeBeforeUpdate);
        APGIIPlante testAPGIIPlante = aPGIIPlanteList.get(aPGIIPlanteList.size() - 1);
        assertThat(testAPGIIPlante.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testAPGIIPlante.getFamille()).isEqualTo(UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void patchNonExistingAPGIIPlante() throws Exception {
        int databaseSizeBeforeUpdate = aPGIIPlanteRepository.findAll().size();
        aPGIIPlante.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAPGIIPlanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aPGIIPlante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aPGIIPlante))
            )
            .andExpect(status().isBadRequest());

        // Validate the APGIIPlante in the database
        List<APGIIPlante> aPGIIPlanteList = aPGIIPlanteRepository.findAll();
        assertThat(aPGIIPlanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAPGIIPlante() throws Exception {
        int databaseSizeBeforeUpdate = aPGIIPlanteRepository.findAll().size();
        aPGIIPlante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAPGIIPlanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aPGIIPlante))
            )
            .andExpect(status().isBadRequest());

        // Validate the APGIIPlante in the database
        List<APGIIPlante> aPGIIPlanteList = aPGIIPlanteRepository.findAll();
        assertThat(aPGIIPlanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAPGIIPlante() throws Exception {
        int databaseSizeBeforeUpdate = aPGIIPlanteRepository.findAll().size();
        aPGIIPlante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAPGIIPlanteMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(aPGIIPlante))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the APGIIPlante in the database
        List<APGIIPlante> aPGIIPlanteList = aPGIIPlanteRepository.findAll();
        assertThat(aPGIIPlanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAPGIIPlante() throws Exception {
        // Initialize the database
        aPGIIPlanteRepository.saveAndFlush(aPGIIPlante);

        int databaseSizeBeforeDelete = aPGIIPlanteRepository.findAll().size();

        // Delete the aPGIIPlante
        restAPGIIPlanteMockMvc
            .perform(delete(ENTITY_API_URL_ID, aPGIIPlante.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<APGIIPlante> aPGIIPlanteList = aPGIIPlanteRepository.findAll();
        assertThat(aPGIIPlanteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
