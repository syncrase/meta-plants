package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.APGIIIPlante;
import fr.syncrase.ecosyst.domain.Clade;
import fr.syncrase.ecosyst.repository.CladeRepository;
import fr.syncrase.ecosyst.service.criteria.CladeCriteria;
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
 * Integration tests for the {@link CladeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CladeResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/clades";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CladeRepository cladeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCladeMockMvc;

    private Clade clade;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Clade createEntity(EntityManager em) {
        Clade clade = new Clade().nom(DEFAULT_NOM);
        return clade;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Clade createUpdatedEntity(EntityManager em) {
        Clade clade = new Clade().nom(UPDATED_NOM);
        return clade;
    }

    @BeforeEach
    public void initTest() {
        clade = createEntity(em);
    }

    @Test
    @Transactional
    void createClade() throws Exception {
        int databaseSizeBeforeCreate = cladeRepository.findAll().size();
        // Create the Clade
        restCladeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clade)))
            .andExpect(status().isCreated());

        // Validate the Clade in the database
        List<Clade> cladeList = cladeRepository.findAll();
        assertThat(cladeList).hasSize(databaseSizeBeforeCreate + 1);
        Clade testClade = cladeList.get(cladeList.size() - 1);
        assertThat(testClade.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    @Transactional
    void createCladeWithExistingId() throws Exception {
        // Create the Clade with an existing ID
        clade.setId(1L);

        int databaseSizeBeforeCreate = cladeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCladeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clade)))
            .andExpect(status().isBadRequest());

        // Validate the Clade in the database
        List<Clade> cladeList = cladeRepository.findAll();
        assertThat(cladeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = cladeRepository.findAll().size();
        // set the field null
        clade.setNom(null);

        // Create the Clade, which fails.

        restCladeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clade)))
            .andExpect(status().isBadRequest());

        List<Clade> cladeList = cladeRepository.findAll();
        assertThat(cladeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClades() throws Exception {
        // Initialize the database
        cladeRepository.saveAndFlush(clade);

        // Get all the cladeList
        restCladeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clade.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));
    }

    @Test
    @Transactional
    void getClade() throws Exception {
        // Initialize the database
        cladeRepository.saveAndFlush(clade);

        // Get the clade
        restCladeMockMvc
            .perform(get(ENTITY_API_URL_ID, clade.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(clade.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM));
    }

    @Test
    @Transactional
    void getCladesByIdFiltering() throws Exception {
        // Initialize the database
        cladeRepository.saveAndFlush(clade);

        Long id = clade.getId();

        defaultCladeShouldBeFound("id.equals=" + id);
        defaultCladeShouldNotBeFound("id.notEquals=" + id);

        defaultCladeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCladeShouldNotBeFound("id.greaterThan=" + id);

        defaultCladeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCladeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCladesByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        cladeRepository.saveAndFlush(clade);

        // Get all the cladeList where nom equals to DEFAULT_NOM
        defaultCladeShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the cladeList where nom equals to UPDATED_NOM
        defaultCladeShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllCladesByNomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cladeRepository.saveAndFlush(clade);

        // Get all the cladeList where nom not equals to DEFAULT_NOM
        defaultCladeShouldNotBeFound("nom.notEquals=" + DEFAULT_NOM);

        // Get all the cladeList where nom not equals to UPDATED_NOM
        defaultCladeShouldBeFound("nom.notEquals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllCladesByNomIsInShouldWork() throws Exception {
        // Initialize the database
        cladeRepository.saveAndFlush(clade);

        // Get all the cladeList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultCladeShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the cladeList where nom equals to UPDATED_NOM
        defaultCladeShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllCladesByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        cladeRepository.saveAndFlush(clade);

        // Get all the cladeList where nom is not null
        defaultCladeShouldBeFound("nom.specified=true");

        // Get all the cladeList where nom is null
        defaultCladeShouldNotBeFound("nom.specified=false");
    }

    @Test
    @Transactional
    void getAllCladesByNomContainsSomething() throws Exception {
        // Initialize the database
        cladeRepository.saveAndFlush(clade);

        // Get all the cladeList where nom contains DEFAULT_NOM
        defaultCladeShouldBeFound("nom.contains=" + DEFAULT_NOM);

        // Get all the cladeList where nom contains UPDATED_NOM
        defaultCladeShouldNotBeFound("nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllCladesByNomNotContainsSomething() throws Exception {
        // Initialize the database
        cladeRepository.saveAndFlush(clade);

        // Get all the cladeList where nom does not contain DEFAULT_NOM
        defaultCladeShouldNotBeFound("nom.doesNotContain=" + DEFAULT_NOM);

        // Get all the cladeList where nom does not contain UPDATED_NOM
        defaultCladeShouldBeFound("nom.doesNotContain=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllCladesByApgiiisIsEqualToSomething() throws Exception {
        // Initialize the database
        cladeRepository.saveAndFlush(clade);
        APGIIIPlante apgiiis;
        if (TestUtil.findAll(em, APGIIIPlante.class).isEmpty()) {
            apgiiis = APGIIIPlanteResourceIT.createEntity(em);
            em.persist(apgiiis);
            em.flush();
        } else {
            apgiiis = TestUtil.findAll(em, APGIIIPlante.class).get(0);
        }
        em.persist(apgiiis);
        em.flush();
        clade.addApgiiis(apgiiis);
        cladeRepository.saveAndFlush(clade);
        Long apgiiisId = apgiiis.getId();

        // Get all the cladeList where apgiiis equals to apgiiisId
        defaultCladeShouldBeFound("apgiiisId.equals=" + apgiiisId);

        // Get all the cladeList where apgiiis equals to (apgiiisId + 1)
        defaultCladeShouldNotBeFound("apgiiisId.equals=" + (apgiiisId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCladeShouldBeFound(String filter) throws Exception {
        restCladeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clade.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));

        // Check, that the count call also returns 1
        restCladeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCladeShouldNotBeFound(String filter) throws Exception {
        restCladeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCladeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingClade() throws Exception {
        // Get the clade
        restCladeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewClade() throws Exception {
        // Initialize the database
        cladeRepository.saveAndFlush(clade);

        int databaseSizeBeforeUpdate = cladeRepository.findAll().size();

        // Update the clade
        Clade updatedClade = cladeRepository.findById(clade.getId()).get();
        // Disconnect from session so that the updates on updatedClade are not directly saved in db
        em.detach(updatedClade);
        updatedClade.nom(UPDATED_NOM);

        restCladeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedClade.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedClade))
            )
            .andExpect(status().isOk());

        // Validate the Clade in the database
        List<Clade> cladeList = cladeRepository.findAll();
        assertThat(cladeList).hasSize(databaseSizeBeforeUpdate);
        Clade testClade = cladeList.get(cladeList.size() - 1);
        assertThat(testClade.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void putNonExistingClade() throws Exception {
        int databaseSizeBeforeUpdate = cladeRepository.findAll().size();
        clade.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCladeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clade.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clade))
            )
            .andExpect(status().isBadRequest());

        // Validate the Clade in the database
        List<Clade> cladeList = cladeRepository.findAll();
        assertThat(cladeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClade() throws Exception {
        int databaseSizeBeforeUpdate = cladeRepository.findAll().size();
        clade.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCladeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clade))
            )
            .andExpect(status().isBadRequest());

        // Validate the Clade in the database
        List<Clade> cladeList = cladeRepository.findAll();
        assertThat(cladeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClade() throws Exception {
        int databaseSizeBeforeUpdate = cladeRepository.findAll().size();
        clade.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCladeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clade)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Clade in the database
        List<Clade> cladeList = cladeRepository.findAll();
        assertThat(cladeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCladeWithPatch() throws Exception {
        // Initialize the database
        cladeRepository.saveAndFlush(clade);

        int databaseSizeBeforeUpdate = cladeRepository.findAll().size();

        // Update the clade using partial update
        Clade partialUpdatedClade = new Clade();
        partialUpdatedClade.setId(clade.getId());

        partialUpdatedClade.nom(UPDATED_NOM);

        restCladeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClade.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClade))
            )
            .andExpect(status().isOk());

        // Validate the Clade in the database
        List<Clade> cladeList = cladeRepository.findAll();
        assertThat(cladeList).hasSize(databaseSizeBeforeUpdate);
        Clade testClade = cladeList.get(cladeList.size() - 1);
        assertThat(testClade.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void fullUpdateCladeWithPatch() throws Exception {
        // Initialize the database
        cladeRepository.saveAndFlush(clade);

        int databaseSizeBeforeUpdate = cladeRepository.findAll().size();

        // Update the clade using partial update
        Clade partialUpdatedClade = new Clade();
        partialUpdatedClade.setId(clade.getId());

        partialUpdatedClade.nom(UPDATED_NOM);

        restCladeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClade.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClade))
            )
            .andExpect(status().isOk());

        // Validate the Clade in the database
        List<Clade> cladeList = cladeRepository.findAll();
        assertThat(cladeList).hasSize(databaseSizeBeforeUpdate);
        Clade testClade = cladeList.get(cladeList.size() - 1);
        assertThat(testClade.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    void patchNonExistingClade() throws Exception {
        int databaseSizeBeforeUpdate = cladeRepository.findAll().size();
        clade.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCladeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, clade.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clade))
            )
            .andExpect(status().isBadRequest());

        // Validate the Clade in the database
        List<Clade> cladeList = cladeRepository.findAll();
        assertThat(cladeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClade() throws Exception {
        int databaseSizeBeforeUpdate = cladeRepository.findAll().size();
        clade.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCladeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clade))
            )
            .andExpect(status().isBadRequest());

        // Validate the Clade in the database
        List<Clade> cladeList = cladeRepository.findAll();
        assertThat(cladeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClade() throws Exception {
        int databaseSizeBeforeUpdate = cladeRepository.findAll().size();
        clade.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCladeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(clade)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Clade in the database
        List<Clade> cladeList = cladeRepository.findAll();
        assertThat(cladeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClade() throws Exception {
        // Initialize the database
        cladeRepository.saveAndFlush(clade);

        int databaseSizeBeforeDelete = cladeRepository.findAll().size();

        // Delete the clade
        restCladeMockMvc
            .perform(delete(ENTITY_API_URL_ID, clade.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Clade> cladeList = cladeRepository.findAll();
        assertThat(cladeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
