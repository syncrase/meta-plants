package fr.syncrase.perma.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.perma.IntegrationTest;
import fr.syncrase.perma.domain.Feuillage;
import fr.syncrase.perma.domain.Plante;
import fr.syncrase.perma.repository.FeuillageRepository;
import fr.syncrase.perma.service.criteria.FeuillageCriteria;
import fr.syncrase.perma.service.dto.FeuillageDTO;
import fr.syncrase.perma.service.mapper.FeuillageMapper;
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
 * Integration tests for the {@link FeuillageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FeuillageResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/feuillages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FeuillageRepository feuillageRepository;

    @Autowired
    private FeuillageMapper feuillageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFeuillageMockMvc;

    private Feuillage feuillage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Feuillage createEntity(EntityManager em) {
        Feuillage feuillage = new Feuillage().type(DEFAULT_TYPE);
        return feuillage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Feuillage createUpdatedEntity(EntityManager em) {
        Feuillage feuillage = new Feuillage().type(UPDATED_TYPE);
        return feuillage;
    }

    @BeforeEach
    public void initTest() {
        feuillage = createEntity(em);
    }

    @Test
    @Transactional
    void createFeuillage() throws Exception {
        int databaseSizeBeforeCreate = feuillageRepository.findAll().size();
        // Create the Feuillage
        FeuillageDTO feuillageDTO = feuillageMapper.toDto(feuillage);
        restFeuillageMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(feuillageDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Feuillage in the database
        List<Feuillage> feuillageList = feuillageRepository.findAll();
        assertThat(feuillageList).hasSize(databaseSizeBeforeCreate + 1);
        Feuillage testFeuillage = feuillageList.get(feuillageList.size() - 1);
        assertThat(testFeuillage.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void createFeuillageWithExistingId() throws Exception {
        // Create the Feuillage with an existing ID
        feuillage.setId(1L);
        FeuillageDTO feuillageDTO = feuillageMapper.toDto(feuillage);

        int databaseSizeBeforeCreate = feuillageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFeuillageMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(feuillageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Feuillage in the database
        List<Feuillage> feuillageList = feuillageRepository.findAll();
        assertThat(feuillageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFeuillages() throws Exception {
        // Initialize the database
        feuillageRepository.saveAndFlush(feuillage);

        // Get all the feuillageList
        restFeuillageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(feuillage.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }

    @Test
    @Transactional
    void getFeuillage() throws Exception {
        // Initialize the database
        feuillageRepository.saveAndFlush(feuillage);

        // Get the feuillage
        restFeuillageMockMvc
            .perform(get(ENTITY_API_URL_ID, feuillage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(feuillage.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }

    @Test
    @Transactional
    void getFeuillagesByIdFiltering() throws Exception {
        // Initialize the database
        feuillageRepository.saveAndFlush(feuillage);

        Long id = feuillage.getId();

        defaultFeuillageShouldBeFound("id.equals=" + id);
        defaultFeuillageShouldNotBeFound("id.notEquals=" + id);

        defaultFeuillageShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFeuillageShouldNotBeFound("id.greaterThan=" + id);

        defaultFeuillageShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFeuillageShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFeuillagesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        feuillageRepository.saveAndFlush(feuillage);

        // Get all the feuillageList where type equals to DEFAULT_TYPE
        defaultFeuillageShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the feuillageList where type equals to UPDATED_TYPE
        defaultFeuillageShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllFeuillagesByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        feuillageRepository.saveAndFlush(feuillage);

        // Get all the feuillageList where type not equals to DEFAULT_TYPE
        defaultFeuillageShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the feuillageList where type not equals to UPDATED_TYPE
        defaultFeuillageShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllFeuillagesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        feuillageRepository.saveAndFlush(feuillage);

        // Get all the feuillageList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultFeuillageShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the feuillageList where type equals to UPDATED_TYPE
        defaultFeuillageShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllFeuillagesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        feuillageRepository.saveAndFlush(feuillage);

        // Get all the feuillageList where type is not null
        defaultFeuillageShouldBeFound("type.specified=true");

        // Get all the feuillageList where type is null
        defaultFeuillageShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllFeuillagesByTypeContainsSomething() throws Exception {
        // Initialize the database
        feuillageRepository.saveAndFlush(feuillage);

        // Get all the feuillageList where type contains DEFAULT_TYPE
        defaultFeuillageShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the feuillageList where type contains UPDATED_TYPE
        defaultFeuillageShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllFeuillagesByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        feuillageRepository.saveAndFlush(feuillage);

        // Get all the feuillageList where type does not contain DEFAULT_TYPE
        defaultFeuillageShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the feuillageList where type does not contain UPDATED_TYPE
        defaultFeuillageShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllFeuillagesByPlanteIsEqualToSomething() throws Exception {
        // Initialize the database
        feuillageRepository.saveAndFlush(feuillage);
        Plante plante;
        if (TestUtil.findAll(em, Plante.class).isEmpty()) {
            plante = PlanteResourceIT.createEntity(em);
            em.persist(plante);
            em.flush();
        } else {
            plante = TestUtil.findAll(em, Plante.class).get(0);
        }
        em.persist(plante);
        em.flush();
        feuillage.addPlante(plante);
        feuillageRepository.saveAndFlush(feuillage);
        Long planteId = plante.getId();

        // Get all the feuillageList where plante equals to planteId
        defaultFeuillageShouldBeFound("planteId.equals=" + planteId);

        // Get all the feuillageList where plante equals to (planteId + 1)
        defaultFeuillageShouldNotBeFound("planteId.equals=" + (planteId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFeuillageShouldBeFound(String filter) throws Exception {
        restFeuillageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(feuillage.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));

        // Check, that the count call also returns 1
        restFeuillageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFeuillageShouldNotBeFound(String filter) throws Exception {
        restFeuillageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFeuillageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFeuillage() throws Exception {
        // Get the feuillage
        restFeuillageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFeuillage() throws Exception {
        // Initialize the database
        feuillageRepository.saveAndFlush(feuillage);

        int databaseSizeBeforeUpdate = feuillageRepository.findAll().size();

        // Update the feuillage
        Feuillage updatedFeuillage = feuillageRepository.findById(feuillage.getId()).get();
        // Disconnect from session so that the updates on updatedFeuillage are not directly saved in db
        em.detach(updatedFeuillage);
        updatedFeuillage.type(UPDATED_TYPE);
        FeuillageDTO feuillageDTO = feuillageMapper.toDto(updatedFeuillage);

        restFeuillageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, feuillageDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(feuillageDTO))
            )
            .andExpect(status().isOk());

        // Validate the Feuillage in the database
        List<Feuillage> feuillageList = feuillageRepository.findAll();
        assertThat(feuillageList).hasSize(databaseSizeBeforeUpdate);
        Feuillage testFeuillage = feuillageList.get(feuillageList.size() - 1);
        assertThat(testFeuillage.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingFeuillage() throws Exception {
        int databaseSizeBeforeUpdate = feuillageRepository.findAll().size();
        feuillage.setId(count.incrementAndGet());

        // Create the Feuillage
        FeuillageDTO feuillageDTO = feuillageMapper.toDto(feuillage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeuillageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, feuillageDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(feuillageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Feuillage in the database
        List<Feuillage> feuillageList = feuillageRepository.findAll();
        assertThat(feuillageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFeuillage() throws Exception {
        int databaseSizeBeforeUpdate = feuillageRepository.findAll().size();
        feuillage.setId(count.incrementAndGet());

        // Create the Feuillage
        FeuillageDTO feuillageDTO = feuillageMapper.toDto(feuillage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeuillageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(feuillageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Feuillage in the database
        List<Feuillage> feuillageList = feuillageRepository.findAll();
        assertThat(feuillageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFeuillage() throws Exception {
        int databaseSizeBeforeUpdate = feuillageRepository.findAll().size();
        feuillage.setId(count.incrementAndGet());

        // Create the Feuillage
        FeuillageDTO feuillageDTO = feuillageMapper.toDto(feuillage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeuillageMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(feuillageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Feuillage in the database
        List<Feuillage> feuillageList = feuillageRepository.findAll();
        assertThat(feuillageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFeuillageWithPatch() throws Exception {
        // Initialize the database
        feuillageRepository.saveAndFlush(feuillage);

        int databaseSizeBeforeUpdate = feuillageRepository.findAll().size();

        // Update the feuillage using partial update
        Feuillage partialUpdatedFeuillage = new Feuillage();
        partialUpdatedFeuillage.setId(feuillage.getId());

        restFeuillageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeuillage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFeuillage))
            )
            .andExpect(status().isOk());

        // Validate the Feuillage in the database
        List<Feuillage> feuillageList = feuillageRepository.findAll();
        assertThat(feuillageList).hasSize(databaseSizeBeforeUpdate);
        Feuillage testFeuillage = feuillageList.get(feuillageList.size() - 1);
        assertThat(testFeuillage.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateFeuillageWithPatch() throws Exception {
        // Initialize the database
        feuillageRepository.saveAndFlush(feuillage);

        int databaseSizeBeforeUpdate = feuillageRepository.findAll().size();

        // Update the feuillage using partial update
        Feuillage partialUpdatedFeuillage = new Feuillage();
        partialUpdatedFeuillage.setId(feuillage.getId());

        partialUpdatedFeuillage.type(UPDATED_TYPE);

        restFeuillageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeuillage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFeuillage))
            )
            .andExpect(status().isOk());

        // Validate the Feuillage in the database
        List<Feuillage> feuillageList = feuillageRepository.findAll();
        assertThat(feuillageList).hasSize(databaseSizeBeforeUpdate);
        Feuillage testFeuillage = feuillageList.get(feuillageList.size() - 1);
        assertThat(testFeuillage.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingFeuillage() throws Exception {
        int databaseSizeBeforeUpdate = feuillageRepository.findAll().size();
        feuillage.setId(count.incrementAndGet());

        // Create the Feuillage
        FeuillageDTO feuillageDTO = feuillageMapper.toDto(feuillage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeuillageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, feuillageDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(feuillageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Feuillage in the database
        List<Feuillage> feuillageList = feuillageRepository.findAll();
        assertThat(feuillageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFeuillage() throws Exception {
        int databaseSizeBeforeUpdate = feuillageRepository.findAll().size();
        feuillage.setId(count.incrementAndGet());

        // Create the Feuillage
        FeuillageDTO feuillageDTO = feuillageMapper.toDto(feuillage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeuillageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(feuillageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Feuillage in the database
        List<Feuillage> feuillageList = feuillageRepository.findAll();
        assertThat(feuillageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFeuillage() throws Exception {
        int databaseSizeBeforeUpdate = feuillageRepository.findAll().size();
        feuillage.setId(count.incrementAndGet());

        // Create the Feuillage
        FeuillageDTO feuillageDTO = feuillageMapper.toDto(feuillage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeuillageMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(feuillageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Feuillage in the database
        List<Feuillage> feuillageList = feuillageRepository.findAll();
        assertThat(feuillageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFeuillage() throws Exception {
        // Initialize the database
        feuillageRepository.saveAndFlush(feuillage);

        int databaseSizeBeforeDelete = feuillageRepository.findAll().size();

        // Delete the feuillage
        restFeuillageMockMvc
            .perform(delete(ENTITY_API_URL_ID, feuillage.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Feuillage> feuillageList = feuillageRepository.findAll();
        assertThat(feuillageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
