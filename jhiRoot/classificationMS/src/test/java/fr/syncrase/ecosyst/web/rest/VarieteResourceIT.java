package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.SousEspece;
import fr.syncrase.ecosyst.domain.SousVariete;
import fr.syncrase.ecosyst.domain.Variete;
import fr.syncrase.ecosyst.domain.Variete;
import fr.syncrase.ecosyst.repository.VarieteRepository;
import fr.syncrase.ecosyst.service.criteria.VarieteCriteria;
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
 * Integration tests for the {@link VarieteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VarieteResourceIT {

    private static final String DEFAULT_NOM_FR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_LATIN = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LATIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/varietes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VarieteRepository varieteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVarieteMockMvc;

    private Variete variete;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Variete createEntity(EntityManager em) {
        Variete variete = new Variete().nomFr(DEFAULT_NOM_FR).nomLatin(DEFAULT_NOM_LATIN);
        return variete;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Variete createUpdatedEntity(EntityManager em) {
        Variete variete = new Variete().nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);
        return variete;
    }

    @BeforeEach
    public void initTest() {
        variete = createEntity(em);
    }

    @Test
    @Transactional
    void createVariete() throws Exception {
        int databaseSizeBeforeCreate = varieteRepository.findAll().size();
        // Create the Variete
        restVarieteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(variete)))
            .andExpect(status().isCreated());

        // Validate the Variete in the database
        List<Variete> varieteList = varieteRepository.findAll();
        assertThat(varieteList).hasSize(databaseSizeBeforeCreate + 1);
        Variete testVariete = varieteList.get(varieteList.size() - 1);
        assertThat(testVariete.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testVariete.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void createVarieteWithExistingId() throws Exception {
        // Create the Variete with an existing ID
        variete.setId(1L);

        int databaseSizeBeforeCreate = varieteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVarieteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(variete)))
            .andExpect(status().isBadRequest());

        // Validate the Variete in the database
        List<Variete> varieteList = varieteRepository.findAll();
        assertThat(varieteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomFrIsRequired() throws Exception {
        int databaseSizeBeforeTest = varieteRepository.findAll().size();
        // set the field null
        variete.setNomFr(null);

        // Create the Variete, which fails.

        restVarieteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(variete)))
            .andExpect(status().isBadRequest());

        List<Variete> varieteList = varieteRepository.findAll();
        assertThat(varieteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVarietes() throws Exception {
        // Initialize the database
        varieteRepository.saveAndFlush(variete);

        // Get all the varieteList
        restVarieteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(variete.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));
    }

    @Test
    @Transactional
    void getVariete() throws Exception {
        // Initialize the database
        varieteRepository.saveAndFlush(variete);

        // Get the variete
        restVarieteMockMvc
            .perform(get(ENTITY_API_URL_ID, variete.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(variete.getId().intValue()))
            .andExpect(jsonPath("$.nomFr").value(DEFAULT_NOM_FR))
            .andExpect(jsonPath("$.nomLatin").value(DEFAULT_NOM_LATIN));
    }

    @Test
    @Transactional
    void getVarietesByIdFiltering() throws Exception {
        // Initialize the database
        varieteRepository.saveAndFlush(variete);

        Long id = variete.getId();

        defaultVarieteShouldBeFound("id.equals=" + id);
        defaultVarieteShouldNotBeFound("id.notEquals=" + id);

        defaultVarieteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultVarieteShouldNotBeFound("id.greaterThan=" + id);

        defaultVarieteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultVarieteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVarietesByNomFrIsEqualToSomething() throws Exception {
        // Initialize the database
        varieteRepository.saveAndFlush(variete);

        // Get all the varieteList where nomFr equals to DEFAULT_NOM_FR
        defaultVarieteShouldBeFound("nomFr.equals=" + DEFAULT_NOM_FR);

        // Get all the varieteList where nomFr equals to UPDATED_NOM_FR
        defaultVarieteShouldNotBeFound("nomFr.equals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllVarietesByNomFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        varieteRepository.saveAndFlush(variete);

        // Get all the varieteList where nomFr not equals to DEFAULT_NOM_FR
        defaultVarieteShouldNotBeFound("nomFr.notEquals=" + DEFAULT_NOM_FR);

        // Get all the varieteList where nomFr not equals to UPDATED_NOM_FR
        defaultVarieteShouldBeFound("nomFr.notEquals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllVarietesByNomFrIsInShouldWork() throws Exception {
        // Initialize the database
        varieteRepository.saveAndFlush(variete);

        // Get all the varieteList where nomFr in DEFAULT_NOM_FR or UPDATED_NOM_FR
        defaultVarieteShouldBeFound("nomFr.in=" + DEFAULT_NOM_FR + "," + UPDATED_NOM_FR);

        // Get all the varieteList where nomFr equals to UPDATED_NOM_FR
        defaultVarieteShouldNotBeFound("nomFr.in=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllVarietesByNomFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        varieteRepository.saveAndFlush(variete);

        // Get all the varieteList where nomFr is not null
        defaultVarieteShouldBeFound("nomFr.specified=true");

        // Get all the varieteList where nomFr is null
        defaultVarieteShouldNotBeFound("nomFr.specified=false");
    }

    @Test
    @Transactional
    void getAllVarietesByNomFrContainsSomething() throws Exception {
        // Initialize the database
        varieteRepository.saveAndFlush(variete);

        // Get all the varieteList where nomFr contains DEFAULT_NOM_FR
        defaultVarieteShouldBeFound("nomFr.contains=" + DEFAULT_NOM_FR);

        // Get all the varieteList where nomFr contains UPDATED_NOM_FR
        defaultVarieteShouldNotBeFound("nomFr.contains=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllVarietesByNomFrNotContainsSomething() throws Exception {
        // Initialize the database
        varieteRepository.saveAndFlush(variete);

        // Get all the varieteList where nomFr does not contain DEFAULT_NOM_FR
        defaultVarieteShouldNotBeFound("nomFr.doesNotContain=" + DEFAULT_NOM_FR);

        // Get all the varieteList where nomFr does not contain UPDATED_NOM_FR
        defaultVarieteShouldBeFound("nomFr.doesNotContain=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllVarietesByNomLatinIsEqualToSomething() throws Exception {
        // Initialize the database
        varieteRepository.saveAndFlush(variete);

        // Get all the varieteList where nomLatin equals to DEFAULT_NOM_LATIN
        defaultVarieteShouldBeFound("nomLatin.equals=" + DEFAULT_NOM_LATIN);

        // Get all the varieteList where nomLatin equals to UPDATED_NOM_LATIN
        defaultVarieteShouldNotBeFound("nomLatin.equals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllVarietesByNomLatinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        varieteRepository.saveAndFlush(variete);

        // Get all the varieteList where nomLatin not equals to DEFAULT_NOM_LATIN
        defaultVarieteShouldNotBeFound("nomLatin.notEquals=" + DEFAULT_NOM_LATIN);

        // Get all the varieteList where nomLatin not equals to UPDATED_NOM_LATIN
        defaultVarieteShouldBeFound("nomLatin.notEquals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllVarietesByNomLatinIsInShouldWork() throws Exception {
        // Initialize the database
        varieteRepository.saveAndFlush(variete);

        // Get all the varieteList where nomLatin in DEFAULT_NOM_LATIN or UPDATED_NOM_LATIN
        defaultVarieteShouldBeFound("nomLatin.in=" + DEFAULT_NOM_LATIN + "," + UPDATED_NOM_LATIN);

        // Get all the varieteList where nomLatin equals to UPDATED_NOM_LATIN
        defaultVarieteShouldNotBeFound("nomLatin.in=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllVarietesByNomLatinIsNullOrNotNull() throws Exception {
        // Initialize the database
        varieteRepository.saveAndFlush(variete);

        // Get all the varieteList where nomLatin is not null
        defaultVarieteShouldBeFound("nomLatin.specified=true");

        // Get all the varieteList where nomLatin is null
        defaultVarieteShouldNotBeFound("nomLatin.specified=false");
    }

    @Test
    @Transactional
    void getAllVarietesByNomLatinContainsSomething() throws Exception {
        // Initialize the database
        varieteRepository.saveAndFlush(variete);

        // Get all the varieteList where nomLatin contains DEFAULT_NOM_LATIN
        defaultVarieteShouldBeFound("nomLatin.contains=" + DEFAULT_NOM_LATIN);

        // Get all the varieteList where nomLatin contains UPDATED_NOM_LATIN
        defaultVarieteShouldNotBeFound("nomLatin.contains=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllVarietesByNomLatinNotContainsSomething() throws Exception {
        // Initialize the database
        varieteRepository.saveAndFlush(variete);

        // Get all the varieteList where nomLatin does not contain DEFAULT_NOM_LATIN
        defaultVarieteShouldNotBeFound("nomLatin.doesNotContain=" + DEFAULT_NOM_LATIN);

        // Get all the varieteList where nomLatin does not contain UPDATED_NOM_LATIN
        defaultVarieteShouldBeFound("nomLatin.doesNotContain=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllVarietesBySousVarietesIsEqualToSomething() throws Exception {
        // Initialize the database
        varieteRepository.saveAndFlush(variete);
        SousVariete sousVarietes;
        if (TestUtil.findAll(em, SousVariete.class).isEmpty()) {
            sousVarietes = SousVarieteResourceIT.createEntity(em);
            em.persist(sousVarietes);
            em.flush();
        } else {
            sousVarietes = TestUtil.findAll(em, SousVariete.class).get(0);
        }
        em.persist(sousVarietes);
        em.flush();
        variete.addSousVarietes(sousVarietes);
        varieteRepository.saveAndFlush(variete);
        Long sousVarietesId = sousVarietes.getId();

        // Get all the varieteList where sousVarietes equals to sousVarietesId
        defaultVarieteShouldBeFound("sousVarietesId.equals=" + sousVarietesId);

        // Get all the varieteList where sousVarietes equals to (sousVarietesId + 1)
        defaultVarieteShouldNotBeFound("sousVarietesId.equals=" + (sousVarietesId + 1));
    }

    @Test
    @Transactional
    void getAllVarietesBySynonymesIsEqualToSomething() throws Exception {
        // Initialize the database
        varieteRepository.saveAndFlush(variete);
        Variete synonymes;
        if (TestUtil.findAll(em, Variete.class).isEmpty()) {
            synonymes = VarieteResourceIT.createEntity(em);
            em.persist(synonymes);
            em.flush();
        } else {
            synonymes = TestUtil.findAll(em, Variete.class).get(0);
        }
        em.persist(synonymes);
        em.flush();
        variete.addSynonymes(synonymes);
        varieteRepository.saveAndFlush(variete);
        Long synonymesId = synonymes.getId();

        // Get all the varieteList where synonymes equals to synonymesId
        defaultVarieteShouldBeFound("synonymesId.equals=" + synonymesId);

        // Get all the varieteList where synonymes equals to (synonymesId + 1)
        defaultVarieteShouldNotBeFound("synonymesId.equals=" + (synonymesId + 1));
    }

    @Test
    @Transactional
    void getAllVarietesBySousEspeceIsEqualToSomething() throws Exception {
        // Initialize the database
        varieteRepository.saveAndFlush(variete);
        SousEspece sousEspece;
        if (TestUtil.findAll(em, SousEspece.class).isEmpty()) {
            sousEspece = SousEspeceResourceIT.createEntity(em);
            em.persist(sousEspece);
            em.flush();
        } else {
            sousEspece = TestUtil.findAll(em, SousEspece.class).get(0);
        }
        em.persist(sousEspece);
        em.flush();
        variete.setSousEspece(sousEspece);
        varieteRepository.saveAndFlush(variete);
        Long sousEspeceId = sousEspece.getId();

        // Get all the varieteList where sousEspece equals to sousEspeceId
        defaultVarieteShouldBeFound("sousEspeceId.equals=" + sousEspeceId);

        // Get all the varieteList where sousEspece equals to (sousEspeceId + 1)
        defaultVarieteShouldNotBeFound("sousEspeceId.equals=" + (sousEspeceId + 1));
    }

    @Test
    @Transactional
    void getAllVarietesByVarieteIsEqualToSomething() throws Exception {
        // Initialize the database
        varieteRepository.saveAndFlush(variete);
        Variete variete;
        if (TestUtil.findAll(em, Variete.class).isEmpty()) {
            variete = VarieteResourceIT.createEntity(em);
            em.persist(variete);
            em.flush();
        } else {
            variete = TestUtil.findAll(em, Variete.class).get(0);
        }
        em.persist(variete);
        em.flush();
        variete.setVariete(variete);
        varieteRepository.saveAndFlush(variete);
        Long varieteId = variete.getId();

        // Get all the varieteList where variete equals to varieteId
        defaultVarieteShouldBeFound("varieteId.equals=" + varieteId);

        // Get all the varieteList where variete equals to (varieteId + 1)
        defaultVarieteShouldNotBeFound("varieteId.equals=" + (varieteId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVarieteShouldBeFound(String filter) throws Exception {
        restVarieteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(variete.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));

        // Check, that the count call also returns 1
        restVarieteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVarieteShouldNotBeFound(String filter) throws Exception {
        restVarieteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVarieteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVariete() throws Exception {
        // Get the variete
        restVarieteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVariete() throws Exception {
        // Initialize the database
        varieteRepository.saveAndFlush(variete);

        int databaseSizeBeforeUpdate = varieteRepository.findAll().size();

        // Update the variete
        Variete updatedVariete = varieteRepository.findById(variete.getId()).get();
        // Disconnect from session so that the updates on updatedVariete are not directly saved in db
        em.detach(updatedVariete);
        updatedVariete.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restVarieteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVariete.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVariete))
            )
            .andExpect(status().isOk());

        // Validate the Variete in the database
        List<Variete> varieteList = varieteRepository.findAll();
        assertThat(varieteList).hasSize(databaseSizeBeforeUpdate);
        Variete testVariete = varieteList.get(varieteList.size() - 1);
        assertThat(testVariete.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testVariete.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void putNonExistingVariete() throws Exception {
        int databaseSizeBeforeUpdate = varieteRepository.findAll().size();
        variete.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVarieteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, variete.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(variete))
            )
            .andExpect(status().isBadRequest());

        // Validate the Variete in the database
        List<Variete> varieteList = varieteRepository.findAll();
        assertThat(varieteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVariete() throws Exception {
        int databaseSizeBeforeUpdate = varieteRepository.findAll().size();
        variete.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVarieteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(variete))
            )
            .andExpect(status().isBadRequest());

        // Validate the Variete in the database
        List<Variete> varieteList = varieteRepository.findAll();
        assertThat(varieteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVariete() throws Exception {
        int databaseSizeBeforeUpdate = varieteRepository.findAll().size();
        variete.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVarieteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(variete)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Variete in the database
        List<Variete> varieteList = varieteRepository.findAll();
        assertThat(varieteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVarieteWithPatch() throws Exception {
        // Initialize the database
        varieteRepository.saveAndFlush(variete);

        int databaseSizeBeforeUpdate = varieteRepository.findAll().size();

        // Update the variete using partial update
        Variete partialUpdatedVariete = new Variete();
        partialUpdatedVariete.setId(variete.getId());

        partialUpdatedVariete.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restVarieteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVariete.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVariete))
            )
            .andExpect(status().isOk());

        // Validate the Variete in the database
        List<Variete> varieteList = varieteRepository.findAll();
        assertThat(varieteList).hasSize(databaseSizeBeforeUpdate);
        Variete testVariete = varieteList.get(varieteList.size() - 1);
        assertThat(testVariete.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testVariete.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void fullUpdateVarieteWithPatch() throws Exception {
        // Initialize the database
        varieteRepository.saveAndFlush(variete);

        int databaseSizeBeforeUpdate = varieteRepository.findAll().size();

        // Update the variete using partial update
        Variete partialUpdatedVariete = new Variete();
        partialUpdatedVariete.setId(variete.getId());

        partialUpdatedVariete.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restVarieteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVariete.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVariete))
            )
            .andExpect(status().isOk());

        // Validate the Variete in the database
        List<Variete> varieteList = varieteRepository.findAll();
        assertThat(varieteList).hasSize(databaseSizeBeforeUpdate);
        Variete testVariete = varieteList.get(varieteList.size() - 1);
        assertThat(testVariete.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testVariete.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void patchNonExistingVariete() throws Exception {
        int databaseSizeBeforeUpdate = varieteRepository.findAll().size();
        variete.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVarieteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, variete.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(variete))
            )
            .andExpect(status().isBadRequest());

        // Validate the Variete in the database
        List<Variete> varieteList = varieteRepository.findAll();
        assertThat(varieteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVariete() throws Exception {
        int databaseSizeBeforeUpdate = varieteRepository.findAll().size();
        variete.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVarieteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(variete))
            )
            .andExpect(status().isBadRequest());

        // Validate the Variete in the database
        List<Variete> varieteList = varieteRepository.findAll();
        assertThat(varieteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVariete() throws Exception {
        int databaseSizeBeforeUpdate = varieteRepository.findAll().size();
        variete.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVarieteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(variete)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Variete in the database
        List<Variete> varieteList = varieteRepository.findAll();
        assertThat(varieteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVariete() throws Exception {
        // Initialize the database
        varieteRepository.saveAndFlush(variete);

        int databaseSizeBeforeDelete = varieteRepository.findAll().size();

        // Delete the variete
        restVarieteMockMvc
            .perform(delete(ENTITY_API_URL_ID, variete.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Variete> varieteList = varieteRepository.findAll();
        assertThat(varieteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
