package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.APGI;
import fr.syncrase.ecosyst.repository.APGIRepository;
import fr.syncrase.ecosyst.service.criteria.APGICriteria;
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
 * Integration tests for the {@link APGIResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class APGIResourceIT {

    private static final String DEFAULT_ORDRE = "AAAAAAAAAA";
    private static final String UPDATED_ORDRE = "BBBBBBBBBB";

    private static final String DEFAULT_FAMILLE = "AAAAAAAAAA";
    private static final String UPDATED_FAMILLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/apgis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private APGIRepository aPGIRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAPGIMockMvc;

    private APGI aPGI;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static APGI createEntity(EntityManager em) {
        APGI aPGI = new APGI().ordre(DEFAULT_ORDRE).famille(DEFAULT_FAMILLE);
        return aPGI;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static APGI createUpdatedEntity(EntityManager em) {
        APGI aPGI = new APGI().ordre(UPDATED_ORDRE).famille(UPDATED_FAMILLE);
        return aPGI;
    }

    @BeforeEach
    public void initTest() {
        aPGI = createEntity(em);
    }

    @Test
    @Transactional
    void createAPGI() throws Exception {
        int databaseSizeBeforeCreate = aPGIRepository.findAll().size();
        // Create the APGI
        restAPGIMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aPGI)))
            .andExpect(status().isCreated());

        // Validate the APGI in the database
        List<APGI> aPGIList = aPGIRepository.findAll();
        assertThat(aPGIList).hasSize(databaseSizeBeforeCreate + 1);
        APGI testAPGI = aPGIList.get(aPGIList.size() - 1);
        assertThat(testAPGI.getOrdre()).isEqualTo(DEFAULT_ORDRE);
        assertThat(testAPGI.getFamille()).isEqualTo(DEFAULT_FAMILLE);
    }

    @Test
    @Transactional
    void createAPGIWithExistingId() throws Exception {
        // Create the APGI with an existing ID
        aPGI.setId(1L);

        int databaseSizeBeforeCreate = aPGIRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAPGIMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aPGI)))
            .andExpect(status().isBadRequest());

        // Validate the APGI in the database
        List<APGI> aPGIList = aPGIRepository.findAll();
        assertThat(aPGIList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrdreIsRequired() throws Exception {
        int databaseSizeBeforeTest = aPGIRepository.findAll().size();
        // set the field null
        aPGI.setOrdre(null);

        // Create the APGI, which fails.

        restAPGIMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aPGI)))
            .andExpect(status().isBadRequest());

        List<APGI> aPGIList = aPGIRepository.findAll();
        assertThat(aPGIList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFamilleIsRequired() throws Exception {
        int databaseSizeBeforeTest = aPGIRepository.findAll().size();
        // set the field null
        aPGI.setFamille(null);

        // Create the APGI, which fails.

        restAPGIMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aPGI)))
            .andExpect(status().isBadRequest());

        List<APGI> aPGIList = aPGIRepository.findAll();
        assertThat(aPGIList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAPGIS() throws Exception {
        // Initialize the database
        aPGIRepository.saveAndFlush(aPGI);

        // Get all the aPGIList
        restAPGIMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aPGI.getId().intValue())))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)))
            .andExpect(jsonPath("$.[*].famille").value(hasItem(DEFAULT_FAMILLE)));
    }

    @Test
    @Transactional
    void getAPGI() throws Exception {
        // Initialize the database
        aPGIRepository.saveAndFlush(aPGI);

        // Get the aPGI
        restAPGIMockMvc
            .perform(get(ENTITY_API_URL_ID, aPGI.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aPGI.getId().intValue()))
            .andExpect(jsonPath("$.ordre").value(DEFAULT_ORDRE))
            .andExpect(jsonPath("$.famille").value(DEFAULT_FAMILLE));
    }

    @Test
    @Transactional
    void getAPGISByIdFiltering() throws Exception {
        // Initialize the database
        aPGIRepository.saveAndFlush(aPGI);

        Long id = aPGI.getId();

        defaultAPGIShouldBeFound("id.equals=" + id);
        defaultAPGIShouldNotBeFound("id.notEquals=" + id);

        defaultAPGIShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAPGIShouldNotBeFound("id.greaterThan=" + id);

        defaultAPGIShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAPGIShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAPGISByOrdreIsEqualToSomething() throws Exception {
        // Initialize the database
        aPGIRepository.saveAndFlush(aPGI);

        // Get all the aPGIList where ordre equals to DEFAULT_ORDRE
        defaultAPGIShouldBeFound("ordre.equals=" + DEFAULT_ORDRE);

        // Get all the aPGIList where ordre equals to UPDATED_ORDRE
        defaultAPGIShouldNotBeFound("ordre.equals=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllAPGISByOrdreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        aPGIRepository.saveAndFlush(aPGI);

        // Get all the aPGIList where ordre not equals to DEFAULT_ORDRE
        defaultAPGIShouldNotBeFound("ordre.notEquals=" + DEFAULT_ORDRE);

        // Get all the aPGIList where ordre not equals to UPDATED_ORDRE
        defaultAPGIShouldBeFound("ordre.notEquals=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllAPGISByOrdreIsInShouldWork() throws Exception {
        // Initialize the database
        aPGIRepository.saveAndFlush(aPGI);

        // Get all the aPGIList where ordre in DEFAULT_ORDRE or UPDATED_ORDRE
        defaultAPGIShouldBeFound("ordre.in=" + DEFAULT_ORDRE + "," + UPDATED_ORDRE);

        // Get all the aPGIList where ordre equals to UPDATED_ORDRE
        defaultAPGIShouldNotBeFound("ordre.in=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllAPGISByOrdreIsNullOrNotNull() throws Exception {
        // Initialize the database
        aPGIRepository.saveAndFlush(aPGI);

        // Get all the aPGIList where ordre is not null
        defaultAPGIShouldBeFound("ordre.specified=true");

        // Get all the aPGIList where ordre is null
        defaultAPGIShouldNotBeFound("ordre.specified=false");
    }

    @Test
    @Transactional
    void getAllAPGISByOrdreContainsSomething() throws Exception {
        // Initialize the database
        aPGIRepository.saveAndFlush(aPGI);

        // Get all the aPGIList where ordre contains DEFAULT_ORDRE
        defaultAPGIShouldBeFound("ordre.contains=" + DEFAULT_ORDRE);

        // Get all the aPGIList where ordre contains UPDATED_ORDRE
        defaultAPGIShouldNotBeFound("ordre.contains=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllAPGISByOrdreNotContainsSomething() throws Exception {
        // Initialize the database
        aPGIRepository.saveAndFlush(aPGI);

        // Get all the aPGIList where ordre does not contain DEFAULT_ORDRE
        defaultAPGIShouldNotBeFound("ordre.doesNotContain=" + DEFAULT_ORDRE);

        // Get all the aPGIList where ordre does not contain UPDATED_ORDRE
        defaultAPGIShouldBeFound("ordre.doesNotContain=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllAPGISByFamilleIsEqualToSomething() throws Exception {
        // Initialize the database
        aPGIRepository.saveAndFlush(aPGI);

        // Get all the aPGIList where famille equals to DEFAULT_FAMILLE
        defaultAPGIShouldBeFound("famille.equals=" + DEFAULT_FAMILLE);

        // Get all the aPGIList where famille equals to UPDATED_FAMILLE
        defaultAPGIShouldNotBeFound("famille.equals=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void getAllAPGISByFamilleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        aPGIRepository.saveAndFlush(aPGI);

        // Get all the aPGIList where famille not equals to DEFAULT_FAMILLE
        defaultAPGIShouldNotBeFound("famille.notEquals=" + DEFAULT_FAMILLE);

        // Get all the aPGIList where famille not equals to UPDATED_FAMILLE
        defaultAPGIShouldBeFound("famille.notEquals=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void getAllAPGISByFamilleIsInShouldWork() throws Exception {
        // Initialize the database
        aPGIRepository.saveAndFlush(aPGI);

        // Get all the aPGIList where famille in DEFAULT_FAMILLE or UPDATED_FAMILLE
        defaultAPGIShouldBeFound("famille.in=" + DEFAULT_FAMILLE + "," + UPDATED_FAMILLE);

        // Get all the aPGIList where famille equals to UPDATED_FAMILLE
        defaultAPGIShouldNotBeFound("famille.in=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void getAllAPGISByFamilleIsNullOrNotNull() throws Exception {
        // Initialize the database
        aPGIRepository.saveAndFlush(aPGI);

        // Get all the aPGIList where famille is not null
        defaultAPGIShouldBeFound("famille.specified=true");

        // Get all the aPGIList where famille is null
        defaultAPGIShouldNotBeFound("famille.specified=false");
    }

    @Test
    @Transactional
    void getAllAPGISByFamilleContainsSomething() throws Exception {
        // Initialize the database
        aPGIRepository.saveAndFlush(aPGI);

        // Get all the aPGIList where famille contains DEFAULT_FAMILLE
        defaultAPGIShouldBeFound("famille.contains=" + DEFAULT_FAMILLE);

        // Get all the aPGIList where famille contains UPDATED_FAMILLE
        defaultAPGIShouldNotBeFound("famille.contains=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void getAllAPGISByFamilleNotContainsSomething() throws Exception {
        // Initialize the database
        aPGIRepository.saveAndFlush(aPGI);

        // Get all the aPGIList where famille does not contain DEFAULT_FAMILLE
        defaultAPGIShouldNotBeFound("famille.doesNotContain=" + DEFAULT_FAMILLE);

        // Get all the aPGIList where famille does not contain UPDATED_FAMILLE
        defaultAPGIShouldBeFound("famille.doesNotContain=" + UPDATED_FAMILLE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAPGIShouldBeFound(String filter) throws Exception {
        restAPGIMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aPGI.getId().intValue())))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)))
            .andExpect(jsonPath("$.[*].famille").value(hasItem(DEFAULT_FAMILLE)));

        // Check, that the count call also returns 1
        restAPGIMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAPGIShouldNotBeFound(String filter) throws Exception {
        restAPGIMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAPGIMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAPGI() throws Exception {
        // Get the aPGI
        restAPGIMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAPGI() throws Exception {
        // Initialize the database
        aPGIRepository.saveAndFlush(aPGI);

        int databaseSizeBeforeUpdate = aPGIRepository.findAll().size();

        // Update the aPGI
        APGI updatedAPGI = aPGIRepository.findById(aPGI.getId()).get();
        // Disconnect from session so that the updates on updatedAPGI are not directly saved in db
        em.detach(updatedAPGI);
        updatedAPGI.ordre(UPDATED_ORDRE).famille(UPDATED_FAMILLE);

        restAPGIMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAPGI.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAPGI))
            )
            .andExpect(status().isOk());

        // Validate the APGI in the database
        List<APGI> aPGIList = aPGIRepository.findAll();
        assertThat(aPGIList).hasSize(databaseSizeBeforeUpdate);
        APGI testAPGI = aPGIList.get(aPGIList.size() - 1);
        assertThat(testAPGI.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testAPGI.getFamille()).isEqualTo(UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void putNonExistingAPGI() throws Exception {
        int databaseSizeBeforeUpdate = aPGIRepository.findAll().size();
        aPGI.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAPGIMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aPGI.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aPGI))
            )
            .andExpect(status().isBadRequest());

        // Validate the APGI in the database
        List<APGI> aPGIList = aPGIRepository.findAll();
        assertThat(aPGIList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAPGI() throws Exception {
        int databaseSizeBeforeUpdate = aPGIRepository.findAll().size();
        aPGI.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAPGIMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aPGI))
            )
            .andExpect(status().isBadRequest());

        // Validate the APGI in the database
        List<APGI> aPGIList = aPGIRepository.findAll();
        assertThat(aPGIList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAPGI() throws Exception {
        int databaseSizeBeforeUpdate = aPGIRepository.findAll().size();
        aPGI.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAPGIMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aPGI)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the APGI in the database
        List<APGI> aPGIList = aPGIRepository.findAll();
        assertThat(aPGIList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAPGIWithPatch() throws Exception {
        // Initialize the database
        aPGIRepository.saveAndFlush(aPGI);

        int databaseSizeBeforeUpdate = aPGIRepository.findAll().size();

        // Update the aPGI using partial update
        APGI partialUpdatedAPGI = new APGI();
        partialUpdatedAPGI.setId(aPGI.getId());

        partialUpdatedAPGI.ordre(UPDATED_ORDRE).famille(UPDATED_FAMILLE);

        restAPGIMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAPGI.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAPGI))
            )
            .andExpect(status().isOk());

        // Validate the APGI in the database
        List<APGI> aPGIList = aPGIRepository.findAll();
        assertThat(aPGIList).hasSize(databaseSizeBeforeUpdate);
        APGI testAPGI = aPGIList.get(aPGIList.size() - 1);
        assertThat(testAPGI.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testAPGI.getFamille()).isEqualTo(UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void fullUpdateAPGIWithPatch() throws Exception {
        // Initialize the database
        aPGIRepository.saveAndFlush(aPGI);

        int databaseSizeBeforeUpdate = aPGIRepository.findAll().size();

        // Update the aPGI using partial update
        APGI partialUpdatedAPGI = new APGI();
        partialUpdatedAPGI.setId(aPGI.getId());

        partialUpdatedAPGI.ordre(UPDATED_ORDRE).famille(UPDATED_FAMILLE);

        restAPGIMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAPGI.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAPGI))
            )
            .andExpect(status().isOk());

        // Validate the APGI in the database
        List<APGI> aPGIList = aPGIRepository.findAll();
        assertThat(aPGIList).hasSize(databaseSizeBeforeUpdate);
        APGI testAPGI = aPGIList.get(aPGIList.size() - 1);
        assertThat(testAPGI.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testAPGI.getFamille()).isEqualTo(UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void patchNonExistingAPGI() throws Exception {
        int databaseSizeBeforeUpdate = aPGIRepository.findAll().size();
        aPGI.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAPGIMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aPGI.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aPGI))
            )
            .andExpect(status().isBadRequest());

        // Validate the APGI in the database
        List<APGI> aPGIList = aPGIRepository.findAll();
        assertThat(aPGIList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAPGI() throws Exception {
        int databaseSizeBeforeUpdate = aPGIRepository.findAll().size();
        aPGI.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAPGIMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aPGI))
            )
            .andExpect(status().isBadRequest());

        // Validate the APGI in the database
        List<APGI> aPGIList = aPGIRepository.findAll();
        assertThat(aPGIList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAPGI() throws Exception {
        int databaseSizeBeforeUpdate = aPGIRepository.findAll().size();
        aPGI.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAPGIMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(aPGI)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the APGI in the database
        List<APGI> aPGIList = aPGIRepository.findAll();
        assertThat(aPGIList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAPGI() throws Exception {
        // Initialize the database
        aPGIRepository.saveAndFlush(aPGI);

        int databaseSizeBeforeDelete = aPGIRepository.findAll().size();

        // Delete the aPGI
        restAPGIMockMvc
            .perform(delete(ENTITY_API_URL_ID, aPGI.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<APGI> aPGIList = aPGIRepository.findAll();
        assertThat(aPGIList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
