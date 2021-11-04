package fr.syncrase.perma.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.perma.IntegrationTest;
import fr.syncrase.perma.domain.Exposition;
import fr.syncrase.perma.domain.Plante;
import fr.syncrase.perma.repository.ExpositionRepository;
import fr.syncrase.perma.service.criteria.ExpositionCriteria;
import fr.syncrase.perma.service.dto.ExpositionDTO;
import fr.syncrase.perma.service.mapper.ExpositionMapper;
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
 * Integration tests for the {@link ExpositionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExpositionResourceIT {

    private static final String DEFAULT_VALEUR = "AAAAAAAAAA";
    private static final String UPDATED_VALEUR = "BBBBBBBBBB";

    private static final Double DEFAULT_ENSOLEILEMENT = 1D;
    private static final Double UPDATED_ENSOLEILEMENT = 2D;
    private static final Double SMALLER_ENSOLEILEMENT = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/expositions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExpositionRepository expositionRepository;

    @Autowired
    private ExpositionMapper expositionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExpositionMockMvc;

    private Exposition exposition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Exposition createEntity(EntityManager em) {
        Exposition exposition = new Exposition().valeur(DEFAULT_VALEUR).ensoleilement(DEFAULT_ENSOLEILEMENT);
        return exposition;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Exposition createUpdatedEntity(EntityManager em) {
        Exposition exposition = new Exposition().valeur(UPDATED_VALEUR).ensoleilement(UPDATED_ENSOLEILEMENT);
        return exposition;
    }

    @BeforeEach
    public void initTest() {
        exposition = createEntity(em);
    }

    @Test
    @Transactional
    void createExposition() throws Exception {
        int databaseSizeBeforeCreate = expositionRepository.findAll().size();
        // Create the Exposition
        ExpositionDTO expositionDTO = expositionMapper.toDto(exposition);
        restExpositionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(expositionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Exposition in the database
        List<Exposition> expositionList = expositionRepository.findAll();
        assertThat(expositionList).hasSize(databaseSizeBeforeCreate + 1);
        Exposition testExposition = expositionList.get(expositionList.size() - 1);
        assertThat(testExposition.getValeur()).isEqualTo(DEFAULT_VALEUR);
        assertThat(testExposition.getEnsoleilement()).isEqualTo(DEFAULT_ENSOLEILEMENT);
    }

    @Test
    @Transactional
    void createExpositionWithExistingId() throws Exception {
        // Create the Exposition with an existing ID
        exposition.setId(1L);
        ExpositionDTO expositionDTO = expositionMapper.toDto(exposition);

        int databaseSizeBeforeCreate = expositionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExpositionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(expositionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exposition in the database
        List<Exposition> expositionList = expositionRepository.findAll();
        assertThat(expositionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllExpositions() throws Exception {
        // Initialize the database
        expositionRepository.saveAndFlush(exposition);

        // Get all the expositionList
        restExpositionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exposition.getId().intValue())))
            .andExpect(jsonPath("$.[*].valeur").value(hasItem(DEFAULT_VALEUR)))
            .andExpect(jsonPath("$.[*].ensoleilement").value(hasItem(DEFAULT_ENSOLEILEMENT.doubleValue())));
    }

    @Test
    @Transactional
    void getExposition() throws Exception {
        // Initialize the database
        expositionRepository.saveAndFlush(exposition);

        // Get the exposition
        restExpositionMockMvc
            .perform(get(ENTITY_API_URL_ID, exposition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(exposition.getId().intValue()))
            .andExpect(jsonPath("$.valeur").value(DEFAULT_VALEUR))
            .andExpect(jsonPath("$.ensoleilement").value(DEFAULT_ENSOLEILEMENT.doubleValue()));
    }

    @Test
    @Transactional
    void getExpositionsByIdFiltering() throws Exception {
        // Initialize the database
        expositionRepository.saveAndFlush(exposition);

        Long id = exposition.getId();

        defaultExpositionShouldBeFound("id.equals=" + id);
        defaultExpositionShouldNotBeFound("id.notEquals=" + id);

        defaultExpositionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultExpositionShouldNotBeFound("id.greaterThan=" + id);

        defaultExpositionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultExpositionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllExpositionsByValeurIsEqualToSomething() throws Exception {
        // Initialize the database
        expositionRepository.saveAndFlush(exposition);

        // Get all the expositionList where valeur equals to DEFAULT_VALEUR
        defaultExpositionShouldBeFound("valeur.equals=" + DEFAULT_VALEUR);

        // Get all the expositionList where valeur equals to UPDATED_VALEUR
        defaultExpositionShouldNotBeFound("valeur.equals=" + UPDATED_VALEUR);
    }

    @Test
    @Transactional
    void getAllExpositionsByValeurIsNotEqualToSomething() throws Exception {
        // Initialize the database
        expositionRepository.saveAndFlush(exposition);

        // Get all the expositionList where valeur not equals to DEFAULT_VALEUR
        defaultExpositionShouldNotBeFound("valeur.notEquals=" + DEFAULT_VALEUR);

        // Get all the expositionList where valeur not equals to UPDATED_VALEUR
        defaultExpositionShouldBeFound("valeur.notEquals=" + UPDATED_VALEUR);
    }

    @Test
    @Transactional
    void getAllExpositionsByValeurIsInShouldWork() throws Exception {
        // Initialize the database
        expositionRepository.saveAndFlush(exposition);

        // Get all the expositionList where valeur in DEFAULT_VALEUR or UPDATED_VALEUR
        defaultExpositionShouldBeFound("valeur.in=" + DEFAULT_VALEUR + "," + UPDATED_VALEUR);

        // Get all the expositionList where valeur equals to UPDATED_VALEUR
        defaultExpositionShouldNotBeFound("valeur.in=" + UPDATED_VALEUR);
    }

    @Test
    @Transactional
    void getAllExpositionsByValeurIsNullOrNotNull() throws Exception {
        // Initialize the database
        expositionRepository.saveAndFlush(exposition);

        // Get all the expositionList where valeur is not null
        defaultExpositionShouldBeFound("valeur.specified=true");

        // Get all the expositionList where valeur is null
        defaultExpositionShouldNotBeFound("valeur.specified=false");
    }

    @Test
    @Transactional
    void getAllExpositionsByValeurContainsSomething() throws Exception {
        // Initialize the database
        expositionRepository.saveAndFlush(exposition);

        // Get all the expositionList where valeur contains DEFAULT_VALEUR
        defaultExpositionShouldBeFound("valeur.contains=" + DEFAULT_VALEUR);

        // Get all the expositionList where valeur contains UPDATED_VALEUR
        defaultExpositionShouldNotBeFound("valeur.contains=" + UPDATED_VALEUR);
    }

    @Test
    @Transactional
    void getAllExpositionsByValeurNotContainsSomething() throws Exception {
        // Initialize the database
        expositionRepository.saveAndFlush(exposition);

        // Get all the expositionList where valeur does not contain DEFAULT_VALEUR
        defaultExpositionShouldNotBeFound("valeur.doesNotContain=" + DEFAULT_VALEUR);

        // Get all the expositionList where valeur does not contain UPDATED_VALEUR
        defaultExpositionShouldBeFound("valeur.doesNotContain=" + UPDATED_VALEUR);
    }

    @Test
    @Transactional
    void getAllExpositionsByEnsoleilementIsEqualToSomething() throws Exception {
        // Initialize the database
        expositionRepository.saveAndFlush(exposition);

        // Get all the expositionList where ensoleilement equals to DEFAULT_ENSOLEILEMENT
        defaultExpositionShouldBeFound("ensoleilement.equals=" + DEFAULT_ENSOLEILEMENT);

        // Get all the expositionList where ensoleilement equals to UPDATED_ENSOLEILEMENT
        defaultExpositionShouldNotBeFound("ensoleilement.equals=" + UPDATED_ENSOLEILEMENT);
    }

    @Test
    @Transactional
    void getAllExpositionsByEnsoleilementIsNotEqualToSomething() throws Exception {
        // Initialize the database
        expositionRepository.saveAndFlush(exposition);

        // Get all the expositionList where ensoleilement not equals to DEFAULT_ENSOLEILEMENT
        defaultExpositionShouldNotBeFound("ensoleilement.notEquals=" + DEFAULT_ENSOLEILEMENT);

        // Get all the expositionList where ensoleilement not equals to UPDATED_ENSOLEILEMENT
        defaultExpositionShouldBeFound("ensoleilement.notEquals=" + UPDATED_ENSOLEILEMENT);
    }

    @Test
    @Transactional
    void getAllExpositionsByEnsoleilementIsInShouldWork() throws Exception {
        // Initialize the database
        expositionRepository.saveAndFlush(exposition);

        // Get all the expositionList where ensoleilement in DEFAULT_ENSOLEILEMENT or UPDATED_ENSOLEILEMENT
        defaultExpositionShouldBeFound("ensoleilement.in=" + DEFAULT_ENSOLEILEMENT + "," + UPDATED_ENSOLEILEMENT);

        // Get all the expositionList where ensoleilement equals to UPDATED_ENSOLEILEMENT
        defaultExpositionShouldNotBeFound("ensoleilement.in=" + UPDATED_ENSOLEILEMENT);
    }

    @Test
    @Transactional
    void getAllExpositionsByEnsoleilementIsNullOrNotNull() throws Exception {
        // Initialize the database
        expositionRepository.saveAndFlush(exposition);

        // Get all the expositionList where ensoleilement is not null
        defaultExpositionShouldBeFound("ensoleilement.specified=true");

        // Get all the expositionList where ensoleilement is null
        defaultExpositionShouldNotBeFound("ensoleilement.specified=false");
    }

    @Test
    @Transactional
    void getAllExpositionsByEnsoleilementIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        expositionRepository.saveAndFlush(exposition);

        // Get all the expositionList where ensoleilement is greater than or equal to DEFAULT_ENSOLEILEMENT
        defaultExpositionShouldBeFound("ensoleilement.greaterThanOrEqual=" + DEFAULT_ENSOLEILEMENT);

        // Get all the expositionList where ensoleilement is greater than or equal to UPDATED_ENSOLEILEMENT
        defaultExpositionShouldNotBeFound("ensoleilement.greaterThanOrEqual=" + UPDATED_ENSOLEILEMENT);
    }

    @Test
    @Transactional
    void getAllExpositionsByEnsoleilementIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        expositionRepository.saveAndFlush(exposition);

        // Get all the expositionList where ensoleilement is less than or equal to DEFAULT_ENSOLEILEMENT
        defaultExpositionShouldBeFound("ensoleilement.lessThanOrEqual=" + DEFAULT_ENSOLEILEMENT);

        // Get all the expositionList where ensoleilement is less than or equal to SMALLER_ENSOLEILEMENT
        defaultExpositionShouldNotBeFound("ensoleilement.lessThanOrEqual=" + SMALLER_ENSOLEILEMENT);
    }

    @Test
    @Transactional
    void getAllExpositionsByEnsoleilementIsLessThanSomething() throws Exception {
        // Initialize the database
        expositionRepository.saveAndFlush(exposition);

        // Get all the expositionList where ensoleilement is less than DEFAULT_ENSOLEILEMENT
        defaultExpositionShouldNotBeFound("ensoleilement.lessThan=" + DEFAULT_ENSOLEILEMENT);

        // Get all the expositionList where ensoleilement is less than UPDATED_ENSOLEILEMENT
        defaultExpositionShouldBeFound("ensoleilement.lessThan=" + UPDATED_ENSOLEILEMENT);
    }

    @Test
    @Transactional
    void getAllExpositionsByEnsoleilementIsGreaterThanSomething() throws Exception {
        // Initialize the database
        expositionRepository.saveAndFlush(exposition);

        // Get all the expositionList where ensoleilement is greater than DEFAULT_ENSOLEILEMENT
        defaultExpositionShouldNotBeFound("ensoleilement.greaterThan=" + DEFAULT_ENSOLEILEMENT);

        // Get all the expositionList where ensoleilement is greater than SMALLER_ENSOLEILEMENT
        defaultExpositionShouldBeFound("ensoleilement.greaterThan=" + SMALLER_ENSOLEILEMENT);
    }

    @Test
    @Transactional
    void getAllExpositionsByPlanteIsEqualToSomething() throws Exception {
        // Initialize the database
        expositionRepository.saveAndFlush(exposition);
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
        exposition.setPlante(plante);
        expositionRepository.saveAndFlush(exposition);
        Long planteId = plante.getId();

        // Get all the expositionList where plante equals to planteId
        defaultExpositionShouldBeFound("planteId.equals=" + planteId);

        // Get all the expositionList where plante equals to (planteId + 1)
        defaultExpositionShouldNotBeFound("planteId.equals=" + (planteId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExpositionShouldBeFound(String filter) throws Exception {
        restExpositionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exposition.getId().intValue())))
            .andExpect(jsonPath("$.[*].valeur").value(hasItem(DEFAULT_VALEUR)))
            .andExpect(jsonPath("$.[*].ensoleilement").value(hasItem(DEFAULT_ENSOLEILEMENT.doubleValue())));

        // Check, that the count call also returns 1
        restExpositionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExpositionShouldNotBeFound(String filter) throws Exception {
        restExpositionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExpositionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingExposition() throws Exception {
        // Get the exposition
        restExpositionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewExposition() throws Exception {
        // Initialize the database
        expositionRepository.saveAndFlush(exposition);

        int databaseSizeBeforeUpdate = expositionRepository.findAll().size();

        // Update the exposition
        Exposition updatedExposition = expositionRepository.findById(exposition.getId()).get();
        // Disconnect from session so that the updates on updatedExposition are not directly saved in db
        em.detach(updatedExposition);
        updatedExposition.valeur(UPDATED_VALEUR).ensoleilement(UPDATED_ENSOLEILEMENT);
        ExpositionDTO expositionDTO = expositionMapper.toDto(updatedExposition);

        restExpositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, expositionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(expositionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Exposition in the database
        List<Exposition> expositionList = expositionRepository.findAll();
        assertThat(expositionList).hasSize(databaseSizeBeforeUpdate);
        Exposition testExposition = expositionList.get(expositionList.size() - 1);
        assertThat(testExposition.getValeur()).isEqualTo(UPDATED_VALEUR);
        assertThat(testExposition.getEnsoleilement()).isEqualTo(UPDATED_ENSOLEILEMENT);
    }

    @Test
    @Transactional
    void putNonExistingExposition() throws Exception {
        int databaseSizeBeforeUpdate = expositionRepository.findAll().size();
        exposition.setId(count.incrementAndGet());

        // Create the Exposition
        ExpositionDTO expositionDTO = expositionMapper.toDto(exposition);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, expositionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(expositionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exposition in the database
        List<Exposition> expositionList = expositionRepository.findAll();
        assertThat(expositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExposition() throws Exception {
        int databaseSizeBeforeUpdate = expositionRepository.findAll().size();
        exposition.setId(count.incrementAndGet());

        // Create the Exposition
        ExpositionDTO expositionDTO = expositionMapper.toDto(exposition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(expositionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exposition in the database
        List<Exposition> expositionList = expositionRepository.findAll();
        assertThat(expositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExposition() throws Exception {
        int databaseSizeBeforeUpdate = expositionRepository.findAll().size();
        exposition.setId(count.incrementAndGet());

        // Create the Exposition
        ExpositionDTO expositionDTO = expositionMapper.toDto(exposition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpositionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(expositionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Exposition in the database
        List<Exposition> expositionList = expositionRepository.findAll();
        assertThat(expositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExpositionWithPatch() throws Exception {
        // Initialize the database
        expositionRepository.saveAndFlush(exposition);

        int databaseSizeBeforeUpdate = expositionRepository.findAll().size();

        // Update the exposition using partial update
        Exposition partialUpdatedExposition = new Exposition();
        partialUpdatedExposition.setId(exposition.getId());

        partialUpdatedExposition.valeur(UPDATED_VALEUR).ensoleilement(UPDATED_ENSOLEILEMENT);

        restExpositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExposition.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExposition))
            )
            .andExpect(status().isOk());

        // Validate the Exposition in the database
        List<Exposition> expositionList = expositionRepository.findAll();
        assertThat(expositionList).hasSize(databaseSizeBeforeUpdate);
        Exposition testExposition = expositionList.get(expositionList.size() - 1);
        assertThat(testExposition.getValeur()).isEqualTo(UPDATED_VALEUR);
        assertThat(testExposition.getEnsoleilement()).isEqualTo(UPDATED_ENSOLEILEMENT);
    }

    @Test
    @Transactional
    void fullUpdateExpositionWithPatch() throws Exception {
        // Initialize the database
        expositionRepository.saveAndFlush(exposition);

        int databaseSizeBeforeUpdate = expositionRepository.findAll().size();

        // Update the exposition using partial update
        Exposition partialUpdatedExposition = new Exposition();
        partialUpdatedExposition.setId(exposition.getId());

        partialUpdatedExposition.valeur(UPDATED_VALEUR).ensoleilement(UPDATED_ENSOLEILEMENT);

        restExpositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExposition.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExposition))
            )
            .andExpect(status().isOk());

        // Validate the Exposition in the database
        List<Exposition> expositionList = expositionRepository.findAll();
        assertThat(expositionList).hasSize(databaseSizeBeforeUpdate);
        Exposition testExposition = expositionList.get(expositionList.size() - 1);
        assertThat(testExposition.getValeur()).isEqualTo(UPDATED_VALEUR);
        assertThat(testExposition.getEnsoleilement()).isEqualTo(UPDATED_ENSOLEILEMENT);
    }

    @Test
    @Transactional
    void patchNonExistingExposition() throws Exception {
        int databaseSizeBeforeUpdate = expositionRepository.findAll().size();
        exposition.setId(count.incrementAndGet());

        // Create the Exposition
        ExpositionDTO expositionDTO = expositionMapper.toDto(exposition);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, expositionDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(expositionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exposition in the database
        List<Exposition> expositionList = expositionRepository.findAll();
        assertThat(expositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExposition() throws Exception {
        int databaseSizeBeforeUpdate = expositionRepository.findAll().size();
        exposition.setId(count.incrementAndGet());

        // Create the Exposition
        ExpositionDTO expositionDTO = expositionMapper.toDto(exposition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(expositionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exposition in the database
        List<Exposition> expositionList = expositionRepository.findAll();
        assertThat(expositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExposition() throws Exception {
        int databaseSizeBeforeUpdate = expositionRepository.findAll().size();
        exposition.setId(count.incrementAndGet());

        // Create the Exposition
        ExpositionDTO expositionDTO = expositionMapper.toDto(exposition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpositionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(expositionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Exposition in the database
        List<Exposition> expositionList = expositionRepository.findAll();
        assertThat(expositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExposition() throws Exception {
        // Initialize the database
        expositionRepository.saveAndFlush(exposition);

        int databaseSizeBeforeDelete = expositionRepository.findAll().size();

        // Delete the exposition
        restExpositionMockMvc
            .perform(delete(ENTITY_API_URL_ID, exposition.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Exposition> expositionList = expositionRepository.findAll();
        assertThat(expositionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
