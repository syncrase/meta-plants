package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.APGIII;
import fr.syncrase.ecosyst.repository.APGIIIRepository;
import fr.syncrase.ecosyst.service.criteria.APGIIICriteria;
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
 * Integration tests for the {@link APGIIIResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class APGIIIResourceIT {

    private static final String DEFAULT_ORDRE = "AAAAAAAAAA";
    private static final String UPDATED_ORDRE = "BBBBBBBBBB";

    private static final String DEFAULT_FAMILLE = "AAAAAAAAAA";
    private static final String UPDATED_FAMILLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/apgiiis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private APGIIIRepository aPGIIIRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAPGIIIMockMvc;

    private APGIII aPGIII;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static APGIII createEntity(EntityManager em) {
        APGIII aPGIII = new APGIII().ordre(DEFAULT_ORDRE).famille(DEFAULT_FAMILLE);
        return aPGIII;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static APGIII createUpdatedEntity(EntityManager em) {
        APGIII aPGIII = new APGIII().ordre(UPDATED_ORDRE).famille(UPDATED_FAMILLE);
        return aPGIII;
    }

    @BeforeEach
    public void initTest() {
        aPGIII = createEntity(em);
    }

    @Test
    @Transactional
    void createAPGIII() throws Exception {
        int databaseSizeBeforeCreate = aPGIIIRepository.findAll().size();
        // Create the APGIII
        restAPGIIIMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aPGIII)))
            .andExpect(status().isCreated());

        // Validate the APGIII in the database
        List<APGIII> aPGIIIList = aPGIIIRepository.findAll();
        assertThat(aPGIIIList).hasSize(databaseSizeBeforeCreate + 1);
        APGIII testAPGIII = aPGIIIList.get(aPGIIIList.size() - 1);
        assertThat(testAPGIII.getOrdre()).isEqualTo(DEFAULT_ORDRE);
        assertThat(testAPGIII.getFamille()).isEqualTo(DEFAULT_FAMILLE);
    }

    @Test
    @Transactional
    void createAPGIIIWithExistingId() throws Exception {
        // Create the APGIII with an existing ID
        aPGIII.setId(1L);

        int databaseSizeBeforeCreate = aPGIIIRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAPGIIIMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aPGIII)))
            .andExpect(status().isBadRequest());

        // Validate the APGIII in the database
        List<APGIII> aPGIIIList = aPGIIIRepository.findAll();
        assertThat(aPGIIIList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrdreIsRequired() throws Exception {
        int databaseSizeBeforeTest = aPGIIIRepository.findAll().size();
        // set the field null
        aPGIII.setOrdre(null);

        // Create the APGIII, which fails.

        restAPGIIIMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aPGIII)))
            .andExpect(status().isBadRequest());

        List<APGIII> aPGIIIList = aPGIIIRepository.findAll();
        assertThat(aPGIIIList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFamilleIsRequired() throws Exception {
        int databaseSizeBeforeTest = aPGIIIRepository.findAll().size();
        // set the field null
        aPGIII.setFamille(null);

        // Create the APGIII, which fails.

        restAPGIIIMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aPGIII)))
            .andExpect(status().isBadRequest());

        List<APGIII> aPGIIIList = aPGIIIRepository.findAll();
        assertThat(aPGIIIList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAPGIIIS() throws Exception {
        // Initialize the database
        aPGIIIRepository.saveAndFlush(aPGIII);

        // Get all the aPGIIIList
        restAPGIIIMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aPGIII.getId().intValue())))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)))
            .andExpect(jsonPath("$.[*].famille").value(hasItem(DEFAULT_FAMILLE)));
    }

    @Test
    @Transactional
    void getAPGIII() throws Exception {
        // Initialize the database
        aPGIIIRepository.saveAndFlush(aPGIII);

        // Get the aPGIII
        restAPGIIIMockMvc
            .perform(get(ENTITY_API_URL_ID, aPGIII.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aPGIII.getId().intValue()))
            .andExpect(jsonPath("$.ordre").value(DEFAULT_ORDRE))
            .andExpect(jsonPath("$.famille").value(DEFAULT_FAMILLE));
    }

    @Test
    @Transactional
    void getAPGIIISByIdFiltering() throws Exception {
        // Initialize the database
        aPGIIIRepository.saveAndFlush(aPGIII);

        Long id = aPGIII.getId();

        defaultAPGIIIShouldBeFound("id.equals=" + id);
        defaultAPGIIIShouldNotBeFound("id.notEquals=" + id);

        defaultAPGIIIShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAPGIIIShouldNotBeFound("id.greaterThan=" + id);

        defaultAPGIIIShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAPGIIIShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAPGIIISByOrdreIsEqualToSomething() throws Exception {
        // Initialize the database
        aPGIIIRepository.saveAndFlush(aPGIII);

        // Get all the aPGIIIList where ordre equals to DEFAULT_ORDRE
        defaultAPGIIIShouldBeFound("ordre.equals=" + DEFAULT_ORDRE);

        // Get all the aPGIIIList where ordre equals to UPDATED_ORDRE
        defaultAPGIIIShouldNotBeFound("ordre.equals=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllAPGIIISByOrdreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        aPGIIIRepository.saveAndFlush(aPGIII);

        // Get all the aPGIIIList where ordre not equals to DEFAULT_ORDRE
        defaultAPGIIIShouldNotBeFound("ordre.notEquals=" + DEFAULT_ORDRE);

        // Get all the aPGIIIList where ordre not equals to UPDATED_ORDRE
        defaultAPGIIIShouldBeFound("ordre.notEquals=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllAPGIIISByOrdreIsInShouldWork() throws Exception {
        // Initialize the database
        aPGIIIRepository.saveAndFlush(aPGIII);

        // Get all the aPGIIIList where ordre in DEFAULT_ORDRE or UPDATED_ORDRE
        defaultAPGIIIShouldBeFound("ordre.in=" + DEFAULT_ORDRE + "," + UPDATED_ORDRE);

        // Get all the aPGIIIList where ordre equals to UPDATED_ORDRE
        defaultAPGIIIShouldNotBeFound("ordre.in=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllAPGIIISByOrdreIsNullOrNotNull() throws Exception {
        // Initialize the database
        aPGIIIRepository.saveAndFlush(aPGIII);

        // Get all the aPGIIIList where ordre is not null
        defaultAPGIIIShouldBeFound("ordre.specified=true");

        // Get all the aPGIIIList where ordre is null
        defaultAPGIIIShouldNotBeFound("ordre.specified=false");
    }

    @Test
    @Transactional
    void getAllAPGIIISByOrdreContainsSomething() throws Exception {
        // Initialize the database
        aPGIIIRepository.saveAndFlush(aPGIII);

        // Get all the aPGIIIList where ordre contains DEFAULT_ORDRE
        defaultAPGIIIShouldBeFound("ordre.contains=" + DEFAULT_ORDRE);

        // Get all the aPGIIIList where ordre contains UPDATED_ORDRE
        defaultAPGIIIShouldNotBeFound("ordre.contains=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllAPGIIISByOrdreNotContainsSomething() throws Exception {
        // Initialize the database
        aPGIIIRepository.saveAndFlush(aPGIII);

        // Get all the aPGIIIList where ordre does not contain DEFAULT_ORDRE
        defaultAPGIIIShouldNotBeFound("ordre.doesNotContain=" + DEFAULT_ORDRE);

        // Get all the aPGIIIList where ordre does not contain UPDATED_ORDRE
        defaultAPGIIIShouldBeFound("ordre.doesNotContain=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllAPGIIISByFamilleIsEqualToSomething() throws Exception {
        // Initialize the database
        aPGIIIRepository.saveAndFlush(aPGIII);

        // Get all the aPGIIIList where famille equals to DEFAULT_FAMILLE
        defaultAPGIIIShouldBeFound("famille.equals=" + DEFAULT_FAMILLE);

        // Get all the aPGIIIList where famille equals to UPDATED_FAMILLE
        defaultAPGIIIShouldNotBeFound("famille.equals=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void getAllAPGIIISByFamilleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        aPGIIIRepository.saveAndFlush(aPGIII);

        // Get all the aPGIIIList where famille not equals to DEFAULT_FAMILLE
        defaultAPGIIIShouldNotBeFound("famille.notEquals=" + DEFAULT_FAMILLE);

        // Get all the aPGIIIList where famille not equals to UPDATED_FAMILLE
        defaultAPGIIIShouldBeFound("famille.notEquals=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void getAllAPGIIISByFamilleIsInShouldWork() throws Exception {
        // Initialize the database
        aPGIIIRepository.saveAndFlush(aPGIII);

        // Get all the aPGIIIList where famille in DEFAULT_FAMILLE or UPDATED_FAMILLE
        defaultAPGIIIShouldBeFound("famille.in=" + DEFAULT_FAMILLE + "," + UPDATED_FAMILLE);

        // Get all the aPGIIIList where famille equals to UPDATED_FAMILLE
        defaultAPGIIIShouldNotBeFound("famille.in=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void getAllAPGIIISByFamilleIsNullOrNotNull() throws Exception {
        // Initialize the database
        aPGIIIRepository.saveAndFlush(aPGIII);

        // Get all the aPGIIIList where famille is not null
        defaultAPGIIIShouldBeFound("famille.specified=true");

        // Get all the aPGIIIList where famille is null
        defaultAPGIIIShouldNotBeFound("famille.specified=false");
    }

    @Test
    @Transactional
    void getAllAPGIIISByFamilleContainsSomething() throws Exception {
        // Initialize the database
        aPGIIIRepository.saveAndFlush(aPGIII);

        // Get all the aPGIIIList where famille contains DEFAULT_FAMILLE
        defaultAPGIIIShouldBeFound("famille.contains=" + DEFAULT_FAMILLE);

        // Get all the aPGIIIList where famille contains UPDATED_FAMILLE
        defaultAPGIIIShouldNotBeFound("famille.contains=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void getAllAPGIIISByFamilleNotContainsSomething() throws Exception {
        // Initialize the database
        aPGIIIRepository.saveAndFlush(aPGIII);

        // Get all the aPGIIIList where famille does not contain DEFAULT_FAMILLE
        defaultAPGIIIShouldNotBeFound("famille.doesNotContain=" + DEFAULT_FAMILLE);

        // Get all the aPGIIIList where famille does not contain UPDATED_FAMILLE
        defaultAPGIIIShouldBeFound("famille.doesNotContain=" + UPDATED_FAMILLE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAPGIIIShouldBeFound(String filter) throws Exception {
        restAPGIIIMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aPGIII.getId().intValue())))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)))
            .andExpect(jsonPath("$.[*].famille").value(hasItem(DEFAULT_FAMILLE)));

        // Check, that the count call also returns 1
        restAPGIIIMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAPGIIIShouldNotBeFound(String filter) throws Exception {
        restAPGIIIMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAPGIIIMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAPGIII() throws Exception {
        // Get the aPGIII
        restAPGIIIMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAPGIII() throws Exception {
        // Initialize the database
        aPGIIIRepository.saveAndFlush(aPGIII);

        int databaseSizeBeforeUpdate = aPGIIIRepository.findAll().size();

        // Update the aPGIII
        APGIII updatedAPGIII = aPGIIIRepository.findById(aPGIII.getId()).get();
        // Disconnect from session so that the updates on updatedAPGIII are not directly saved in db
        em.detach(updatedAPGIII);
        updatedAPGIII.ordre(UPDATED_ORDRE).famille(UPDATED_FAMILLE);

        restAPGIIIMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAPGIII.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAPGIII))
            )
            .andExpect(status().isOk());

        // Validate the APGIII in the database
        List<APGIII> aPGIIIList = aPGIIIRepository.findAll();
        assertThat(aPGIIIList).hasSize(databaseSizeBeforeUpdate);
        APGIII testAPGIII = aPGIIIList.get(aPGIIIList.size() - 1);
        assertThat(testAPGIII.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testAPGIII.getFamille()).isEqualTo(UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void putNonExistingAPGIII() throws Exception {
        int databaseSizeBeforeUpdate = aPGIIIRepository.findAll().size();
        aPGIII.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAPGIIIMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aPGIII.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aPGIII))
            )
            .andExpect(status().isBadRequest());

        // Validate the APGIII in the database
        List<APGIII> aPGIIIList = aPGIIIRepository.findAll();
        assertThat(aPGIIIList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAPGIII() throws Exception {
        int databaseSizeBeforeUpdate = aPGIIIRepository.findAll().size();
        aPGIII.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAPGIIIMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aPGIII))
            )
            .andExpect(status().isBadRequest());

        // Validate the APGIII in the database
        List<APGIII> aPGIIIList = aPGIIIRepository.findAll();
        assertThat(aPGIIIList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAPGIII() throws Exception {
        int databaseSizeBeforeUpdate = aPGIIIRepository.findAll().size();
        aPGIII.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAPGIIIMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aPGIII)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the APGIII in the database
        List<APGIII> aPGIIIList = aPGIIIRepository.findAll();
        assertThat(aPGIIIList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAPGIIIWithPatch() throws Exception {
        // Initialize the database
        aPGIIIRepository.saveAndFlush(aPGIII);

        int databaseSizeBeforeUpdate = aPGIIIRepository.findAll().size();

        // Update the aPGIII using partial update
        APGIII partialUpdatedAPGIII = new APGIII();
        partialUpdatedAPGIII.setId(aPGIII.getId());

        restAPGIIIMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAPGIII.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAPGIII))
            )
            .andExpect(status().isOk());

        // Validate the APGIII in the database
        List<APGIII> aPGIIIList = aPGIIIRepository.findAll();
        assertThat(aPGIIIList).hasSize(databaseSizeBeforeUpdate);
        APGIII testAPGIII = aPGIIIList.get(aPGIIIList.size() - 1);
        assertThat(testAPGIII.getOrdre()).isEqualTo(DEFAULT_ORDRE);
        assertThat(testAPGIII.getFamille()).isEqualTo(DEFAULT_FAMILLE);
    }

    @Test
    @Transactional
    void fullUpdateAPGIIIWithPatch() throws Exception {
        // Initialize the database
        aPGIIIRepository.saveAndFlush(aPGIII);

        int databaseSizeBeforeUpdate = aPGIIIRepository.findAll().size();

        // Update the aPGIII using partial update
        APGIII partialUpdatedAPGIII = new APGIII();
        partialUpdatedAPGIII.setId(aPGIII.getId());

        partialUpdatedAPGIII.ordre(UPDATED_ORDRE).famille(UPDATED_FAMILLE);

        restAPGIIIMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAPGIII.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAPGIII))
            )
            .andExpect(status().isOk());

        // Validate the APGIII in the database
        List<APGIII> aPGIIIList = aPGIIIRepository.findAll();
        assertThat(aPGIIIList).hasSize(databaseSizeBeforeUpdate);
        APGIII testAPGIII = aPGIIIList.get(aPGIIIList.size() - 1);
        assertThat(testAPGIII.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testAPGIII.getFamille()).isEqualTo(UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    void patchNonExistingAPGIII() throws Exception {
        int databaseSizeBeforeUpdate = aPGIIIRepository.findAll().size();
        aPGIII.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAPGIIIMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aPGIII.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aPGIII))
            )
            .andExpect(status().isBadRequest());

        // Validate the APGIII in the database
        List<APGIII> aPGIIIList = aPGIIIRepository.findAll();
        assertThat(aPGIIIList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAPGIII() throws Exception {
        int databaseSizeBeforeUpdate = aPGIIIRepository.findAll().size();
        aPGIII.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAPGIIIMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aPGIII))
            )
            .andExpect(status().isBadRequest());

        // Validate the APGIII in the database
        List<APGIII> aPGIIIList = aPGIIIRepository.findAll();
        assertThat(aPGIIIList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAPGIII() throws Exception {
        int databaseSizeBeforeUpdate = aPGIIIRepository.findAll().size();
        aPGIII.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAPGIIIMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(aPGIII)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the APGIII in the database
        List<APGIII> aPGIIIList = aPGIIIRepository.findAll();
        assertThat(aPGIIIList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAPGIII() throws Exception {
        // Initialize the database
        aPGIIIRepository.saveAndFlush(aPGIII);

        int databaseSizeBeforeDelete = aPGIIIRepository.findAll().size();

        // Delete the aPGIII
        restAPGIIIMockMvc
            .perform(delete(ENTITY_API_URL_ID, aPGIII.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<APGIII> aPGIIIList = aPGIIIRepository.findAll();
        assertThat(aPGIIIList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
