package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.APGIPlante;
import fr.syncrase.ecosyst.repository.APGIPlanteRepository;
import fr.syncrase.ecosyst.service.criteria.APGIPlanteCriteria;
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
 * Integration tests for the {@link APGIPlanteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class APGIPlanteResourceIT {

    private static final String DEFAULT_ORDRE = "AAAAAAAAAA";
    private static final String UPDATED_ORDRE = "BBBBBBBBBB";

    private static final String DEFAULT_FAMILLE = "AAAAAAAAAA";
    private static final String UPDATED_FAMILLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/apgi-plantes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private APGIPlanteRepository aPGIPlanteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAPGIPlanteMockMvc;

    private APGIPlante aPGIPlante;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static APGIPlante createEntity(EntityManager em) {
        APGIPlante aPGIPlante = new APGIPlante().ordre(DEFAULT_ORDRE).famille(DEFAULT_FAMILLE);
        return aPGIPlante;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static APGIPlante createUpdatedEntity(EntityManager em) {
        APGIPlante aPGIPlante = new APGIPlante().ordre(UPDATED_ORDRE).famille(UPDATED_FAMILLE);
        return aPGIPlante;
    }

    @BeforeEach
    public void initTest() {
        aPGIPlante = createEntity(em);
    }

    @Test
    @Transactional
    void createAPGIPlante() throws Exception {
        int databaseSizeBeforeCreate = aPGIPlanteRepository.findAll().size();
        // Create the APGIPlante
        restAPGIPlanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aPGIPlante)))
            .andExpect(status().isCreated());

        // Validate the APGIPlante in the database
        List<APGIPlante> aPGIPlanteList = aPGIPlanteRepository.findAll();
        assertThat(aPGIPlanteList).hasSize(databaseSizeBeforeCreate + 1);
        APGIPlante testAPGIPlante = aPGIPlanteList.get(aPGIPlanteList.size() - 1);
        assertThat(testAPGIPlante.getOrdre()).isEqualTo(DEFAULT_ORDRE);
        assertThat(testAPGIPlante.getFamille()).isEqualTo(DEFAULT_FAMILLE);
    }

    @Test
    @Transactional
    void createAPGIPlanteWithExistingId() throws Exception {
        // Create the APGIPlante with an existing ID
        aPGIPlante.setId(1L);

        int databaseSizeBeforeCreate = aPGIPlanteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAPGIPlanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aPGIPlante)))
            .andExpect(status().isBadRequest());

        // Validate the APGIPlante in the database
        List<APGIPlante> aPGIPlanteList = aPGIPlanteRepository.findAll();
        assertThat(aPGIPlanteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrdreIsRequired() throws Exception {
        int databaseSizeBeforeTest = aPGIPlanteRepository.findAll().size();
        // set the field null
        aPGIPlante.setOrdre(null);

        // Create the APGIPlante, which fails.

        restAPGIPlanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aPGIPlante)))
            .andExpect(status().isBadRequest());

        List<APGIPlante> aPGIPlanteList = aPGIPlanteRepository.findAll();
        assertThat(aPGIPlanteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFamilleIsRequired() throws Exception {
        int databaseSizeBeforeTest = aPGIPlanteRepository.findAll().size();
        // set the field null
        aPGIPlante.setFamille(null);

        // Create the APGIPlante, which fails.

        restAPGIPlanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aPGIPlante)))
            .andExpect(status().isBadRequest());

        List<APGIPlante> aPGIPlanteList = aPGIPlanteRepository.findAll();
        assertThat(aPGIPlanteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAPGIPlantes() throws Exception {
        // Initialize the database
        aPGIPlanteRepository.saveAndFlush(aPGIPlante);

        // Get all the aPGIPlanteList
        restAPGIPlanteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aPGIPlante.getId().intValue())))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)))
            .andExpect(jsonPath("$.[*].famille").value(hasItem(DEFAULT_FAMILLE)));
    }

    @Test
    @Transactional
    void getAPGIPlante() throws Exception {
        // Initialize the database
        aPGIPlanteRepository.saveAndFlush(aPGIPlante);

        // Get the aPGIPlante
        restAPGIPlanteMockMvc
            .perform(get(ENTITY_API_URL_ID, aPGIPlante.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aPGIPlante.getId().intValue()))
            .andExpect(jsonPath("$.ordre").value(DEFAULT_ORDRE))
            .andExpect(jsonPath("$.famille").value(DEFAULT_FAMILLE));
    }

    @Test
    @Transactional
    void getAPGIPlantesByIdFiltering() throws Exception {
        // Initialize the database
        aPGIPlanteRepository.saveAndFlush(aPGIPlante);

        Long id = aPGIPlante.getId();

        defaultAPGIPlanteShouldBeFound("id.equals=" + id);
        defaultAPGIPlanteShouldNotBeFound("id.notEquals=" + id);

        defaultAPGIPlanteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAPGIPlanteShouldNotBeFound("id.greaterThan=" + id);

        defaultAPGIPlanteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAPGIPlanteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAPGIPlantesByOrdreIsEqualToSomething() throws Exception {
        // Initialize the database
        aPGIPlanteRepository.saveAndFlush(aPGIPlante);

        // Get all the aPGIPlanteList where ordre equals to DEFAULT_ORDRE
        defaultAPGIPlanteShouldBeFound("ordre.equals=" + DEFAULT_ORDRE);

        // Get all the aPGIPlanteList where ordre equals to UPDATED_ORDRE
        defaultAPGIPlanteShouldNotBeFound("ordre.equals=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllAPGIPlantesByOrdreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        aPGIPlanteRepository.saveAndFlush(aPGIPlante);

        // Get all the aPGIPlanteList where ordre not equals to DEFAULT_ORDRE
        defaultAPGIPlanteShouldNotBeFound("ordre.notEquals=" + DEFAULT_ORDRE);

        // Get all the aPGIPlanteList where ordre not equals to UPDATED_ORDRE
        defaultAPGIPlanteShouldBeFound("ordre.notEquals=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllAPGIPlantesByOrdreIsInShouldWork() throws Exception {
        // Initialize the database
        aPGIPlanteRepository.saveAndFlush(aPGIPlante);

        // Get all the aPGIPlanteList where ordre in DEFAULT_ORDRE or UPDATED_ORDRE
        defaultAPGIPlanteShouldBeFound("ordre.in=" + DEFAULT_ORDRE + "," + UPDATED_ORDRE);

        // Get all the aPGIPlanteList where ordre equals to UPDATED_ORDRE
        defaultAPGIPlanteShouldNotBeFound("ordre.in=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllAPGIPlantesByOrdreIsNullOrNotNull() throws Exception {
        // Initialize the database
        aPGIPlanteRepository.saveAndFlush(aPGIPlante);

        // Get all the aPGIPlanteList where ordre is not null
        defaultAPGIPlanteShouldBeFound("ordre.specified=true");

        // Get all the aPGIPlanteList where ordre is null
        defaultAPGIPlanteShouldNotBeFound("ordre.specified=false");
    }

    @Test
    @Transactional
    void getAllAPGIPlantesByOrdreContainsSomething() throws Exception {
        // Initialize the database
        aPGIPlanteRepository.saveAndFlush(aPGIPlante);

        // Get all the aPGIPlanteList where ordre contains DEFAULT_ORDRE
        defaultAPGIPlanteShouldBeFound("ordre.contains=" + DEFAULT_ORDRE);

        // Get all the aPGIPlanteList where ordre contains UPDATED_ORDRE
        defaultAPGIPlanteShouldNotBeFound("ordre.contains=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllAPGIPlantesByOrdreNotContainsSomething() throws Exception {
        // Initialize the database
        aPGIPlanteRepository.saveAndFlush(aPGIPlante);

        // Get all the aPGIPlanteList where ordre does not contain DEFAULT_ORDRE
        defaultAPGIPlanteShouldNotBeFound("ordre.doesNotContain=" + DEFAULT_ORDRE);

        // Get all the aPGIPlanteList where ordre does not contain UPDATED_ORDRE
        defaultAPGIPlanteShouldBeFound("ordre.doesNotContain=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllAPGIPlantesByFamilleIsEqualToSomething() throws Exception {
        // Initialize the database
        aPGIPlanteRepository.saveAndFlush(aPGIPlante);

        // Get all the aPGIPlanteList where famille equals to DEFAULT_FAMILLE
        defaultAPGIPlanteShouldBeFound("famille.equals=" + DEFAULT_FAMILLE);

        // Get all the aPGIPlanteList where famille equals to UPDATED_FAMILLE
        defaultAPGIPlanteShouldNotBeFound("famille.equals=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void getAllAPGIPlantesByFamilleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        aPGIPlanteRepository.saveAndFlush(aPGIPlante);

        // Get all the aPGIPlanteList where famille not equals to DEFAULT_FAMILLE
        defaultAPGIPlanteShouldNotBeFound("famille.notEquals=" + DEFAULT_FAMILLE);

        // Get all the aPGIPlanteList where famille not equals to UPDATED_FAMILLE
        defaultAPGIPlanteShouldBeFound("famille.notEquals=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void getAllAPGIPlantesByFamilleIsInShouldWork() throws Exception {
        // Initialize the database
        aPGIPlanteRepository.saveAndFlush(aPGIPlante);

        // Get all the aPGIPlanteList where famille in DEFAULT_FAMILLE or UPDATED_FAMILLE
        defaultAPGIPlanteShouldBeFound("famille.in=" + DEFAULT_FAMILLE + "," + UPDATED_FAMILLE);

        // Get all the aPGIPlanteList where famille equals to UPDATED_FAMILLE
        defaultAPGIPlanteShouldNotBeFound("famille.in=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void getAllAPGIPlantesByFamilleIsNullOrNotNull() throws Exception {
        // Initialize the database
        aPGIPlanteRepository.saveAndFlush(aPGIPlante);

        // Get all the aPGIPlanteList where famille is not null
        defaultAPGIPlanteShouldBeFound("famille.specified=true");

        // Get all the aPGIPlanteList where famille is null
        defaultAPGIPlanteShouldNotBeFound("famille.specified=false");
    }

    @Test
    @Transactional
    void getAllAPGIPlantesByFamilleContainsSomething() throws Exception {
        // Initialize the database
        aPGIPlanteRepository.saveAndFlush(aPGIPlante);

        // Get all the aPGIPlanteList where famille contains DEFAULT_FAMILLE
        defaultAPGIPlanteShouldBeFound("famille.contains=" + DEFAULT_FAMILLE);

        // Get all the aPGIPlanteList where famille contains UPDATED_FAMILLE
        defaultAPGIPlanteShouldNotBeFound("famille.contains=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void getAllAPGIPlantesByFamilleNotContainsSomething() throws Exception {
        // Initialize the database
        aPGIPlanteRepository.saveAndFlush(aPGIPlante);

        // Get all the aPGIPlanteList where famille does not contain DEFAULT_FAMILLE
        defaultAPGIPlanteShouldNotBeFound("famille.doesNotContain=" + DEFAULT_FAMILLE);

        // Get all the aPGIPlanteList where famille does not contain UPDATED_FAMILLE
        defaultAPGIPlanteShouldBeFound("famille.doesNotContain=" + UPDATED_FAMILLE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAPGIPlanteShouldBeFound(String filter) throws Exception {
        restAPGIPlanteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aPGIPlante.getId().intValue())))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)))
            .andExpect(jsonPath("$.[*].famille").value(hasItem(DEFAULT_FAMILLE)));

        // Check, that the count call also returns 1
        restAPGIPlanteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAPGIPlanteShouldNotBeFound(String filter) throws Exception {
        restAPGIPlanteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAPGIPlanteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAPGIPlante() throws Exception {
        // Get the aPGIPlante
        restAPGIPlanteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAPGIPlante() throws Exception {
        // Initialize the database
        aPGIPlanteRepository.saveAndFlush(aPGIPlante);

        int databaseSizeBeforeUpdate = aPGIPlanteRepository.findAll().size();

        // Update the aPGIPlante
        APGIPlante updatedAPGIPlante = aPGIPlanteRepository.findById(aPGIPlante.getId()).get();
        // Disconnect from session so that the updates on updatedAPGIPlante are not directly saved in db
        em.detach(updatedAPGIPlante);
        updatedAPGIPlante.ordre(UPDATED_ORDRE).famille(UPDATED_FAMILLE);

        restAPGIPlanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAPGIPlante.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAPGIPlante))
            )
            .andExpect(status().isOk());

        // Validate the APGIPlante in the database
        List<APGIPlante> aPGIPlanteList = aPGIPlanteRepository.findAll();
        assertThat(aPGIPlanteList).hasSize(databaseSizeBeforeUpdate);
        APGIPlante testAPGIPlante = aPGIPlanteList.get(aPGIPlanteList.size() - 1);
        assertThat(testAPGIPlante.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testAPGIPlante.getFamille()).isEqualTo(UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void putNonExistingAPGIPlante() throws Exception {
        int databaseSizeBeforeUpdate = aPGIPlanteRepository.findAll().size();
        aPGIPlante.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAPGIPlanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aPGIPlante.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aPGIPlante))
            )
            .andExpect(status().isBadRequest());

        // Validate the APGIPlante in the database
        List<APGIPlante> aPGIPlanteList = aPGIPlanteRepository.findAll();
        assertThat(aPGIPlanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAPGIPlante() throws Exception {
        int databaseSizeBeforeUpdate = aPGIPlanteRepository.findAll().size();
        aPGIPlante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAPGIPlanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aPGIPlante))
            )
            .andExpect(status().isBadRequest());

        // Validate the APGIPlante in the database
        List<APGIPlante> aPGIPlanteList = aPGIPlanteRepository.findAll();
        assertThat(aPGIPlanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAPGIPlante() throws Exception {
        int databaseSizeBeforeUpdate = aPGIPlanteRepository.findAll().size();
        aPGIPlante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAPGIPlanteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aPGIPlante)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the APGIPlante in the database
        List<APGIPlante> aPGIPlanteList = aPGIPlanteRepository.findAll();
        assertThat(aPGIPlanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAPGIPlanteWithPatch() throws Exception {
        // Initialize the database
        aPGIPlanteRepository.saveAndFlush(aPGIPlante);

        int databaseSizeBeforeUpdate = aPGIPlanteRepository.findAll().size();

        // Update the aPGIPlante using partial update
        APGIPlante partialUpdatedAPGIPlante = new APGIPlante();
        partialUpdatedAPGIPlante.setId(aPGIPlante.getId());

        partialUpdatedAPGIPlante.famille(UPDATED_FAMILLE);

        restAPGIPlanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAPGIPlante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAPGIPlante))
            )
            .andExpect(status().isOk());

        // Validate the APGIPlante in the database
        List<APGIPlante> aPGIPlanteList = aPGIPlanteRepository.findAll();
        assertThat(aPGIPlanteList).hasSize(databaseSizeBeforeUpdate);
        APGIPlante testAPGIPlante = aPGIPlanteList.get(aPGIPlanteList.size() - 1);
        assertThat(testAPGIPlante.getOrdre()).isEqualTo(DEFAULT_ORDRE);
        assertThat(testAPGIPlante.getFamille()).isEqualTo(UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void fullUpdateAPGIPlanteWithPatch() throws Exception {
        // Initialize the database
        aPGIPlanteRepository.saveAndFlush(aPGIPlante);

        int databaseSizeBeforeUpdate = aPGIPlanteRepository.findAll().size();

        // Update the aPGIPlante using partial update
        APGIPlante partialUpdatedAPGIPlante = new APGIPlante();
        partialUpdatedAPGIPlante.setId(aPGIPlante.getId());

        partialUpdatedAPGIPlante.ordre(UPDATED_ORDRE).famille(UPDATED_FAMILLE);

        restAPGIPlanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAPGIPlante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAPGIPlante))
            )
            .andExpect(status().isOk());

        // Validate the APGIPlante in the database
        List<APGIPlante> aPGIPlanteList = aPGIPlanteRepository.findAll();
        assertThat(aPGIPlanteList).hasSize(databaseSizeBeforeUpdate);
        APGIPlante testAPGIPlante = aPGIPlanteList.get(aPGIPlanteList.size() - 1);
        assertThat(testAPGIPlante.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testAPGIPlante.getFamille()).isEqualTo(UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void patchNonExistingAPGIPlante() throws Exception {
        int databaseSizeBeforeUpdate = aPGIPlanteRepository.findAll().size();
        aPGIPlante.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAPGIPlanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aPGIPlante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aPGIPlante))
            )
            .andExpect(status().isBadRequest());

        // Validate the APGIPlante in the database
        List<APGIPlante> aPGIPlanteList = aPGIPlanteRepository.findAll();
        assertThat(aPGIPlanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAPGIPlante() throws Exception {
        int databaseSizeBeforeUpdate = aPGIPlanteRepository.findAll().size();
        aPGIPlante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAPGIPlanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aPGIPlante))
            )
            .andExpect(status().isBadRequest());

        // Validate the APGIPlante in the database
        List<APGIPlante> aPGIPlanteList = aPGIPlanteRepository.findAll();
        assertThat(aPGIPlanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAPGIPlante() throws Exception {
        int databaseSizeBeforeUpdate = aPGIPlanteRepository.findAll().size();
        aPGIPlante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAPGIPlanteMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(aPGIPlante))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the APGIPlante in the database
        List<APGIPlante> aPGIPlanteList = aPGIPlanteRepository.findAll();
        assertThat(aPGIPlanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAPGIPlante() throws Exception {
        // Initialize the database
        aPGIPlanteRepository.saveAndFlush(aPGIPlante);

        int databaseSizeBeforeDelete = aPGIPlanteRepository.findAll().size();

        // Delete the aPGIPlante
        restAPGIPlanteMockMvc
            .perform(delete(ENTITY_API_URL_ID, aPGIPlante.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<APGIPlante> aPGIPlanteList = aPGIPlanteRepository.findAll();
        assertThat(aPGIPlanteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
