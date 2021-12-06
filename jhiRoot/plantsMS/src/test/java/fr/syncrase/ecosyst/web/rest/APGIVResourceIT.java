package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.APGIV;
import fr.syncrase.ecosyst.repository.APGIVRepository;
import fr.syncrase.ecosyst.service.criteria.APGIVCriteria;
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
 * Integration tests for the {@link APGIVResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class APGIVResourceIT {

    private static final String DEFAULT_ORDRE = "AAAAAAAAAA";
    private static final String UPDATED_ORDRE = "BBBBBBBBBB";

    private static final String DEFAULT_FAMILLE = "AAAAAAAAAA";
    private static final String UPDATED_FAMILLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/apgivs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private APGIVRepository aPGIVRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAPGIVMockMvc;

    private APGIV aPGIV;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static APGIV createEntity(EntityManager em) {
        APGIV aPGIV = new APGIV().ordre(DEFAULT_ORDRE).famille(DEFAULT_FAMILLE);
        return aPGIV;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static APGIV createUpdatedEntity(EntityManager em) {
        APGIV aPGIV = new APGIV().ordre(UPDATED_ORDRE).famille(UPDATED_FAMILLE);
        return aPGIV;
    }

    @BeforeEach
    public void initTest() {
        aPGIV = createEntity(em);
    }

    @Test
    @Transactional
    void createAPGIV() throws Exception {
        int databaseSizeBeforeCreate = aPGIVRepository.findAll().size();
        // Create the APGIV
        restAPGIVMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aPGIV)))
            .andExpect(status().isCreated());

        // Validate the APGIV in the database
        List<APGIV> aPGIVList = aPGIVRepository.findAll();
        assertThat(aPGIVList).hasSize(databaseSizeBeforeCreate + 1);
        APGIV testAPGIV = aPGIVList.get(aPGIVList.size() - 1);
        assertThat(testAPGIV.getOrdre()).isEqualTo(DEFAULT_ORDRE);
        assertThat(testAPGIV.getFamille()).isEqualTo(DEFAULT_FAMILLE);
    }

    @Test
    @Transactional
    void createAPGIVWithExistingId() throws Exception {
        // Create the APGIV with an existing ID
        aPGIV.setId(1L);

        int databaseSizeBeforeCreate = aPGIVRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAPGIVMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aPGIV)))
            .andExpect(status().isBadRequest());

        // Validate the APGIV in the database
        List<APGIV> aPGIVList = aPGIVRepository.findAll();
        assertThat(aPGIVList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrdreIsRequired() throws Exception {
        int databaseSizeBeforeTest = aPGIVRepository.findAll().size();
        // set the field null
        aPGIV.setOrdre(null);

        // Create the APGIV, which fails.

        restAPGIVMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aPGIV)))
            .andExpect(status().isBadRequest());

        List<APGIV> aPGIVList = aPGIVRepository.findAll();
        assertThat(aPGIVList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFamilleIsRequired() throws Exception {
        int databaseSizeBeforeTest = aPGIVRepository.findAll().size();
        // set the field null
        aPGIV.setFamille(null);

        // Create the APGIV, which fails.

        restAPGIVMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aPGIV)))
            .andExpect(status().isBadRequest());

        List<APGIV> aPGIVList = aPGIVRepository.findAll();
        assertThat(aPGIVList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAPGIVS() throws Exception {
        // Initialize the database
        aPGIVRepository.saveAndFlush(aPGIV);

        // Get all the aPGIVList
        restAPGIVMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aPGIV.getId().intValue())))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)))
            .andExpect(jsonPath("$.[*].famille").value(hasItem(DEFAULT_FAMILLE)));
    }

    @Test
    @Transactional
    void getAPGIV() throws Exception {
        // Initialize the database
        aPGIVRepository.saveAndFlush(aPGIV);

        // Get the aPGIV
        restAPGIVMockMvc
            .perform(get(ENTITY_API_URL_ID, aPGIV.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aPGIV.getId().intValue()))
            .andExpect(jsonPath("$.ordre").value(DEFAULT_ORDRE))
            .andExpect(jsonPath("$.famille").value(DEFAULT_FAMILLE));
    }

    @Test
    @Transactional
    void getAPGIVSByIdFiltering() throws Exception {
        // Initialize the database
        aPGIVRepository.saveAndFlush(aPGIV);

        Long id = aPGIV.getId();

        defaultAPGIVShouldBeFound("id.equals=" + id);
        defaultAPGIVShouldNotBeFound("id.notEquals=" + id);

        defaultAPGIVShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAPGIVShouldNotBeFound("id.greaterThan=" + id);

        defaultAPGIVShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAPGIVShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAPGIVSByOrdreIsEqualToSomething() throws Exception {
        // Initialize the database
        aPGIVRepository.saveAndFlush(aPGIV);

        // Get all the aPGIVList where ordre equals to DEFAULT_ORDRE
        defaultAPGIVShouldBeFound("ordre.equals=" + DEFAULT_ORDRE);

        // Get all the aPGIVList where ordre equals to UPDATED_ORDRE
        defaultAPGIVShouldNotBeFound("ordre.equals=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllAPGIVSByOrdreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        aPGIVRepository.saveAndFlush(aPGIV);

        // Get all the aPGIVList where ordre not equals to DEFAULT_ORDRE
        defaultAPGIVShouldNotBeFound("ordre.notEquals=" + DEFAULT_ORDRE);

        // Get all the aPGIVList where ordre not equals to UPDATED_ORDRE
        defaultAPGIVShouldBeFound("ordre.notEquals=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllAPGIVSByOrdreIsInShouldWork() throws Exception {
        // Initialize the database
        aPGIVRepository.saveAndFlush(aPGIV);

        // Get all the aPGIVList where ordre in DEFAULT_ORDRE or UPDATED_ORDRE
        defaultAPGIVShouldBeFound("ordre.in=" + DEFAULT_ORDRE + "," + UPDATED_ORDRE);

        // Get all the aPGIVList where ordre equals to UPDATED_ORDRE
        defaultAPGIVShouldNotBeFound("ordre.in=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllAPGIVSByOrdreIsNullOrNotNull() throws Exception {
        // Initialize the database
        aPGIVRepository.saveAndFlush(aPGIV);

        // Get all the aPGIVList where ordre is not null
        defaultAPGIVShouldBeFound("ordre.specified=true");

        // Get all the aPGIVList where ordre is null
        defaultAPGIVShouldNotBeFound("ordre.specified=false");
    }

    @Test
    @Transactional
    void getAllAPGIVSByOrdreContainsSomething() throws Exception {
        // Initialize the database
        aPGIVRepository.saveAndFlush(aPGIV);

        // Get all the aPGIVList where ordre contains DEFAULT_ORDRE
        defaultAPGIVShouldBeFound("ordre.contains=" + DEFAULT_ORDRE);

        // Get all the aPGIVList where ordre contains UPDATED_ORDRE
        defaultAPGIVShouldNotBeFound("ordre.contains=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllAPGIVSByOrdreNotContainsSomething() throws Exception {
        // Initialize the database
        aPGIVRepository.saveAndFlush(aPGIV);

        // Get all the aPGIVList where ordre does not contain DEFAULT_ORDRE
        defaultAPGIVShouldNotBeFound("ordre.doesNotContain=" + DEFAULT_ORDRE);

        // Get all the aPGIVList where ordre does not contain UPDATED_ORDRE
        defaultAPGIVShouldBeFound("ordre.doesNotContain=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllAPGIVSByFamilleIsEqualToSomething() throws Exception {
        // Initialize the database
        aPGIVRepository.saveAndFlush(aPGIV);

        // Get all the aPGIVList where famille equals to DEFAULT_FAMILLE
        defaultAPGIVShouldBeFound("famille.equals=" + DEFAULT_FAMILLE);

        // Get all the aPGIVList where famille equals to UPDATED_FAMILLE
        defaultAPGIVShouldNotBeFound("famille.equals=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void getAllAPGIVSByFamilleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        aPGIVRepository.saveAndFlush(aPGIV);

        // Get all the aPGIVList where famille not equals to DEFAULT_FAMILLE
        defaultAPGIVShouldNotBeFound("famille.notEquals=" + DEFAULT_FAMILLE);

        // Get all the aPGIVList where famille not equals to UPDATED_FAMILLE
        defaultAPGIVShouldBeFound("famille.notEquals=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void getAllAPGIVSByFamilleIsInShouldWork() throws Exception {
        // Initialize the database
        aPGIVRepository.saveAndFlush(aPGIV);

        // Get all the aPGIVList where famille in DEFAULT_FAMILLE or UPDATED_FAMILLE
        defaultAPGIVShouldBeFound("famille.in=" + DEFAULT_FAMILLE + "," + UPDATED_FAMILLE);

        // Get all the aPGIVList where famille equals to UPDATED_FAMILLE
        defaultAPGIVShouldNotBeFound("famille.in=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void getAllAPGIVSByFamilleIsNullOrNotNull() throws Exception {
        // Initialize the database
        aPGIVRepository.saveAndFlush(aPGIV);

        // Get all the aPGIVList where famille is not null
        defaultAPGIVShouldBeFound("famille.specified=true");

        // Get all the aPGIVList where famille is null
        defaultAPGIVShouldNotBeFound("famille.specified=false");
    }

    @Test
    @Transactional
    void getAllAPGIVSByFamilleContainsSomething() throws Exception {
        // Initialize the database
        aPGIVRepository.saveAndFlush(aPGIV);

        // Get all the aPGIVList where famille contains DEFAULT_FAMILLE
        defaultAPGIVShouldBeFound("famille.contains=" + DEFAULT_FAMILLE);

        // Get all the aPGIVList where famille contains UPDATED_FAMILLE
        defaultAPGIVShouldNotBeFound("famille.contains=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void getAllAPGIVSByFamilleNotContainsSomething() throws Exception {
        // Initialize the database
        aPGIVRepository.saveAndFlush(aPGIV);

        // Get all the aPGIVList where famille does not contain DEFAULT_FAMILLE
        defaultAPGIVShouldNotBeFound("famille.doesNotContain=" + DEFAULT_FAMILLE);

        // Get all the aPGIVList where famille does not contain UPDATED_FAMILLE
        defaultAPGIVShouldBeFound("famille.doesNotContain=" + UPDATED_FAMILLE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAPGIVShouldBeFound(String filter) throws Exception {
        restAPGIVMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aPGIV.getId().intValue())))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)))
            .andExpect(jsonPath("$.[*].famille").value(hasItem(DEFAULT_FAMILLE)));

        // Check, that the count call also returns 1
        restAPGIVMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAPGIVShouldNotBeFound(String filter) throws Exception {
        restAPGIVMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAPGIVMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAPGIV() throws Exception {
        // Get the aPGIV
        restAPGIVMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAPGIV() throws Exception {
        // Initialize the database
        aPGIVRepository.saveAndFlush(aPGIV);

        int databaseSizeBeforeUpdate = aPGIVRepository.findAll().size();

        // Update the aPGIV
        APGIV updatedAPGIV = aPGIVRepository.findById(aPGIV.getId()).get();
        // Disconnect from session so that the updates on updatedAPGIV are not directly saved in db
        em.detach(updatedAPGIV);
        updatedAPGIV.ordre(UPDATED_ORDRE).famille(UPDATED_FAMILLE);

        restAPGIVMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAPGIV.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAPGIV))
            )
            .andExpect(status().isOk());

        // Validate the APGIV in the database
        List<APGIV> aPGIVList = aPGIVRepository.findAll();
        assertThat(aPGIVList).hasSize(databaseSizeBeforeUpdate);
        APGIV testAPGIV = aPGIVList.get(aPGIVList.size() - 1);
        assertThat(testAPGIV.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testAPGIV.getFamille()).isEqualTo(UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void putNonExistingAPGIV() throws Exception {
        int databaseSizeBeforeUpdate = aPGIVRepository.findAll().size();
        aPGIV.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAPGIVMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aPGIV.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aPGIV))
            )
            .andExpect(status().isBadRequest());

        // Validate the APGIV in the database
        List<APGIV> aPGIVList = aPGIVRepository.findAll();
        assertThat(aPGIVList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAPGIV() throws Exception {
        int databaseSizeBeforeUpdate = aPGIVRepository.findAll().size();
        aPGIV.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAPGIVMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aPGIV))
            )
            .andExpect(status().isBadRequest());

        // Validate the APGIV in the database
        List<APGIV> aPGIVList = aPGIVRepository.findAll();
        assertThat(aPGIVList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAPGIV() throws Exception {
        int databaseSizeBeforeUpdate = aPGIVRepository.findAll().size();
        aPGIV.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAPGIVMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aPGIV)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the APGIV in the database
        List<APGIV> aPGIVList = aPGIVRepository.findAll();
        assertThat(aPGIVList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAPGIVWithPatch() throws Exception {
        // Initialize the database
        aPGIVRepository.saveAndFlush(aPGIV);

        int databaseSizeBeforeUpdate = aPGIVRepository.findAll().size();

        // Update the aPGIV using partial update
        APGIV partialUpdatedAPGIV = new APGIV();
        partialUpdatedAPGIV.setId(aPGIV.getId());

        partialUpdatedAPGIV.famille(UPDATED_FAMILLE);

        restAPGIVMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAPGIV.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAPGIV))
            )
            .andExpect(status().isOk());

        // Validate the APGIV in the database
        List<APGIV> aPGIVList = aPGIVRepository.findAll();
        assertThat(aPGIVList).hasSize(databaseSizeBeforeUpdate);
        APGIV testAPGIV = aPGIVList.get(aPGIVList.size() - 1);
        assertThat(testAPGIV.getOrdre()).isEqualTo(DEFAULT_ORDRE);
        assertThat(testAPGIV.getFamille()).isEqualTo(UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void fullUpdateAPGIVWithPatch() throws Exception {
        // Initialize the database
        aPGIVRepository.saveAndFlush(aPGIV);

        int databaseSizeBeforeUpdate = aPGIVRepository.findAll().size();

        // Update the aPGIV using partial update
        APGIV partialUpdatedAPGIV = new APGIV();
        partialUpdatedAPGIV.setId(aPGIV.getId());

        partialUpdatedAPGIV.ordre(UPDATED_ORDRE).famille(UPDATED_FAMILLE);

        restAPGIVMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAPGIV.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAPGIV))
            )
            .andExpect(status().isOk());

        // Validate the APGIV in the database
        List<APGIV> aPGIVList = aPGIVRepository.findAll();
        assertThat(aPGIVList).hasSize(databaseSizeBeforeUpdate);
        APGIV testAPGIV = aPGIVList.get(aPGIVList.size() - 1);
        assertThat(testAPGIV.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testAPGIV.getFamille()).isEqualTo(UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void patchNonExistingAPGIV() throws Exception {
        int databaseSizeBeforeUpdate = aPGIVRepository.findAll().size();
        aPGIV.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAPGIVMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aPGIV.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aPGIV))
            )
            .andExpect(status().isBadRequest());

        // Validate the APGIV in the database
        List<APGIV> aPGIVList = aPGIVRepository.findAll();
        assertThat(aPGIVList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAPGIV() throws Exception {
        int databaseSizeBeforeUpdate = aPGIVRepository.findAll().size();
        aPGIV.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAPGIVMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aPGIV))
            )
            .andExpect(status().isBadRequest());

        // Validate the APGIV in the database
        List<APGIV> aPGIVList = aPGIVRepository.findAll();
        assertThat(aPGIVList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAPGIV() throws Exception {
        int databaseSizeBeforeUpdate = aPGIVRepository.findAll().size();
        aPGIV.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAPGIVMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(aPGIV)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the APGIV in the database
        List<APGIV> aPGIVList = aPGIVRepository.findAll();
        assertThat(aPGIVList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAPGIV() throws Exception {
        // Initialize the database
        aPGIVRepository.saveAndFlush(aPGIV);

        int databaseSizeBeforeDelete = aPGIVRepository.findAll().size();

        // Delete the aPGIV
        restAPGIVMockMvc
            .perform(delete(ENTITY_API_URL_ID, aPGIV.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<APGIV> aPGIVList = aPGIVRepository.findAll();
        assertThat(aPGIVList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
