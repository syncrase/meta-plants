package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.Espece;
import fr.syncrase.ecosyst.domain.SousEspece;
import fr.syncrase.ecosyst.domain.SousEspece;
import fr.syncrase.ecosyst.domain.Variete;
import fr.syncrase.ecosyst.repository.SousEspeceRepository;
import fr.syncrase.ecosyst.service.criteria.SousEspeceCriteria;
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
 * Integration tests for the {@link SousEspeceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SousEspeceResourceIT {

    private static final String DEFAULT_NOM_FR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_LATIN = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LATIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sous-especes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SousEspeceRepository sousEspeceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSousEspeceMockMvc;

    private SousEspece sousEspece;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SousEspece createEntity(EntityManager em) {
        SousEspece sousEspece = new SousEspece().nomFr(DEFAULT_NOM_FR).nomLatin(DEFAULT_NOM_LATIN);
        return sousEspece;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SousEspece createUpdatedEntity(EntityManager em) {
        SousEspece sousEspece = new SousEspece().nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);
        return sousEspece;
    }

    @BeforeEach
    public void initTest() {
        sousEspece = createEntity(em);
    }

    @Test
    @Transactional
    void createSousEspece() throws Exception {
        int databaseSizeBeforeCreate = sousEspeceRepository.findAll().size();
        // Create the SousEspece
        restSousEspeceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousEspece)))
            .andExpect(status().isCreated());

        // Validate the SousEspece in the database
        List<SousEspece> sousEspeceList = sousEspeceRepository.findAll();
        assertThat(sousEspeceList).hasSize(databaseSizeBeforeCreate + 1);
        SousEspece testSousEspece = sousEspeceList.get(sousEspeceList.size() - 1);
        assertThat(testSousEspece.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testSousEspece.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void createSousEspeceWithExistingId() throws Exception {
        // Create the SousEspece with an existing ID
        sousEspece.setId(1L);

        int databaseSizeBeforeCreate = sousEspeceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSousEspeceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousEspece)))
            .andExpect(status().isBadRequest());

        // Validate the SousEspece in the database
        List<SousEspece> sousEspeceList = sousEspeceRepository.findAll();
        assertThat(sousEspeceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomFrIsRequired() throws Exception {
        int databaseSizeBeforeTest = sousEspeceRepository.findAll().size();
        // set the field null
        sousEspece.setNomFr(null);

        // Create the SousEspece, which fails.

        restSousEspeceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousEspece)))
            .andExpect(status().isBadRequest());

        List<SousEspece> sousEspeceList = sousEspeceRepository.findAll();
        assertThat(sousEspeceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSousEspeces() throws Exception {
        // Initialize the database
        sousEspeceRepository.saveAndFlush(sousEspece);

        // Get all the sousEspeceList
        restSousEspeceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sousEspece.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));
    }

    @Test
    @Transactional
    void getSousEspece() throws Exception {
        // Initialize the database
        sousEspeceRepository.saveAndFlush(sousEspece);

        // Get the sousEspece
        restSousEspeceMockMvc
            .perform(get(ENTITY_API_URL_ID, sousEspece.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sousEspece.getId().intValue()))
            .andExpect(jsonPath("$.nomFr").value(DEFAULT_NOM_FR))
            .andExpect(jsonPath("$.nomLatin").value(DEFAULT_NOM_LATIN));
    }

    @Test
    @Transactional
    void getSousEspecesByIdFiltering() throws Exception {
        // Initialize the database
        sousEspeceRepository.saveAndFlush(sousEspece);

        Long id = sousEspece.getId();

        defaultSousEspeceShouldBeFound("id.equals=" + id);
        defaultSousEspeceShouldNotBeFound("id.notEquals=" + id);

        defaultSousEspeceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSousEspeceShouldNotBeFound("id.greaterThan=" + id);

        defaultSousEspeceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSousEspeceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSousEspecesByNomFrIsEqualToSomething() throws Exception {
        // Initialize the database
        sousEspeceRepository.saveAndFlush(sousEspece);

        // Get all the sousEspeceList where nomFr equals to DEFAULT_NOM_FR
        defaultSousEspeceShouldBeFound("nomFr.equals=" + DEFAULT_NOM_FR);

        // Get all the sousEspeceList where nomFr equals to UPDATED_NOM_FR
        defaultSousEspeceShouldNotBeFound("nomFr.equals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousEspecesByNomFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sousEspeceRepository.saveAndFlush(sousEspece);

        // Get all the sousEspeceList where nomFr not equals to DEFAULT_NOM_FR
        defaultSousEspeceShouldNotBeFound("nomFr.notEquals=" + DEFAULT_NOM_FR);

        // Get all the sousEspeceList where nomFr not equals to UPDATED_NOM_FR
        defaultSousEspeceShouldBeFound("nomFr.notEquals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousEspecesByNomFrIsInShouldWork() throws Exception {
        // Initialize the database
        sousEspeceRepository.saveAndFlush(sousEspece);

        // Get all the sousEspeceList where nomFr in DEFAULT_NOM_FR or UPDATED_NOM_FR
        defaultSousEspeceShouldBeFound("nomFr.in=" + DEFAULT_NOM_FR + "," + UPDATED_NOM_FR);

        // Get all the sousEspeceList where nomFr equals to UPDATED_NOM_FR
        defaultSousEspeceShouldNotBeFound("nomFr.in=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousEspecesByNomFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        sousEspeceRepository.saveAndFlush(sousEspece);

        // Get all the sousEspeceList where nomFr is not null
        defaultSousEspeceShouldBeFound("nomFr.specified=true");

        // Get all the sousEspeceList where nomFr is null
        defaultSousEspeceShouldNotBeFound("nomFr.specified=false");
    }

    @Test
    @Transactional
    void getAllSousEspecesByNomFrContainsSomething() throws Exception {
        // Initialize the database
        sousEspeceRepository.saveAndFlush(sousEspece);

        // Get all the sousEspeceList where nomFr contains DEFAULT_NOM_FR
        defaultSousEspeceShouldBeFound("nomFr.contains=" + DEFAULT_NOM_FR);

        // Get all the sousEspeceList where nomFr contains UPDATED_NOM_FR
        defaultSousEspeceShouldNotBeFound("nomFr.contains=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousEspecesByNomFrNotContainsSomething() throws Exception {
        // Initialize the database
        sousEspeceRepository.saveAndFlush(sousEspece);

        // Get all the sousEspeceList where nomFr does not contain DEFAULT_NOM_FR
        defaultSousEspeceShouldNotBeFound("nomFr.doesNotContain=" + DEFAULT_NOM_FR);

        // Get all the sousEspeceList where nomFr does not contain UPDATED_NOM_FR
        defaultSousEspeceShouldBeFound("nomFr.doesNotContain=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousEspecesByNomLatinIsEqualToSomething() throws Exception {
        // Initialize the database
        sousEspeceRepository.saveAndFlush(sousEspece);

        // Get all the sousEspeceList where nomLatin equals to DEFAULT_NOM_LATIN
        defaultSousEspeceShouldBeFound("nomLatin.equals=" + DEFAULT_NOM_LATIN);

        // Get all the sousEspeceList where nomLatin equals to UPDATED_NOM_LATIN
        defaultSousEspeceShouldNotBeFound("nomLatin.equals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousEspecesByNomLatinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sousEspeceRepository.saveAndFlush(sousEspece);

        // Get all the sousEspeceList where nomLatin not equals to DEFAULT_NOM_LATIN
        defaultSousEspeceShouldNotBeFound("nomLatin.notEquals=" + DEFAULT_NOM_LATIN);

        // Get all the sousEspeceList where nomLatin not equals to UPDATED_NOM_LATIN
        defaultSousEspeceShouldBeFound("nomLatin.notEquals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousEspecesByNomLatinIsInShouldWork() throws Exception {
        // Initialize the database
        sousEspeceRepository.saveAndFlush(sousEspece);

        // Get all the sousEspeceList where nomLatin in DEFAULT_NOM_LATIN or UPDATED_NOM_LATIN
        defaultSousEspeceShouldBeFound("nomLatin.in=" + DEFAULT_NOM_LATIN + "," + UPDATED_NOM_LATIN);

        // Get all the sousEspeceList where nomLatin equals to UPDATED_NOM_LATIN
        defaultSousEspeceShouldNotBeFound("nomLatin.in=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousEspecesByNomLatinIsNullOrNotNull() throws Exception {
        // Initialize the database
        sousEspeceRepository.saveAndFlush(sousEspece);

        // Get all the sousEspeceList where nomLatin is not null
        defaultSousEspeceShouldBeFound("nomLatin.specified=true");

        // Get all the sousEspeceList where nomLatin is null
        defaultSousEspeceShouldNotBeFound("nomLatin.specified=false");
    }

    @Test
    @Transactional
    void getAllSousEspecesByNomLatinContainsSomething() throws Exception {
        // Initialize the database
        sousEspeceRepository.saveAndFlush(sousEspece);

        // Get all the sousEspeceList where nomLatin contains DEFAULT_NOM_LATIN
        defaultSousEspeceShouldBeFound("nomLatin.contains=" + DEFAULT_NOM_LATIN);

        // Get all the sousEspeceList where nomLatin contains UPDATED_NOM_LATIN
        defaultSousEspeceShouldNotBeFound("nomLatin.contains=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousEspecesByNomLatinNotContainsSomething() throws Exception {
        // Initialize the database
        sousEspeceRepository.saveAndFlush(sousEspece);

        // Get all the sousEspeceList where nomLatin does not contain DEFAULT_NOM_LATIN
        defaultSousEspeceShouldNotBeFound("nomLatin.doesNotContain=" + DEFAULT_NOM_LATIN);

        // Get all the sousEspeceList where nomLatin does not contain UPDATED_NOM_LATIN
        defaultSousEspeceShouldBeFound("nomLatin.doesNotContain=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousEspecesByVarietesIsEqualToSomething() throws Exception {
        // Initialize the database
        sousEspeceRepository.saveAndFlush(sousEspece);
        Variete varietes;
        if (TestUtil.findAll(em, Variete.class).isEmpty()) {
            varietes = VarieteResourceIT.createEntity(em);
            em.persist(varietes);
            em.flush();
        } else {
            varietes = TestUtil.findAll(em, Variete.class).get(0);
        }
        em.persist(varietes);
        em.flush();
        sousEspece.addVarietes(varietes);
        sousEspeceRepository.saveAndFlush(sousEspece);
        Long varietesId = varietes.getId();

        // Get all the sousEspeceList where varietes equals to varietesId
        defaultSousEspeceShouldBeFound("varietesId.equals=" + varietesId);

        // Get all the sousEspeceList where varietes equals to (varietesId + 1)
        defaultSousEspeceShouldNotBeFound("varietesId.equals=" + (varietesId + 1));
    }

    @Test
    @Transactional
    void getAllSousEspecesBySynonymesIsEqualToSomething() throws Exception {
        // Initialize the database
        sousEspeceRepository.saveAndFlush(sousEspece);
        SousEspece synonymes;
        if (TestUtil.findAll(em, SousEspece.class).isEmpty()) {
            synonymes = SousEspeceResourceIT.createEntity(em);
            em.persist(synonymes);
            em.flush();
        } else {
            synonymes = TestUtil.findAll(em, SousEspece.class).get(0);
        }
        em.persist(synonymes);
        em.flush();
        sousEspece.addSynonymes(synonymes);
        sousEspeceRepository.saveAndFlush(sousEspece);
        Long synonymesId = synonymes.getId();

        // Get all the sousEspeceList where synonymes equals to synonymesId
        defaultSousEspeceShouldBeFound("synonymesId.equals=" + synonymesId);

        // Get all the sousEspeceList where synonymes equals to (synonymesId + 1)
        defaultSousEspeceShouldNotBeFound("synonymesId.equals=" + (synonymesId + 1));
    }

    @Test
    @Transactional
    void getAllSousEspecesByEspeceIsEqualToSomething() throws Exception {
        // Initialize the database
        sousEspeceRepository.saveAndFlush(sousEspece);
        Espece espece;
        if (TestUtil.findAll(em, Espece.class).isEmpty()) {
            espece = EspeceResourceIT.createEntity(em);
            em.persist(espece);
            em.flush();
        } else {
            espece = TestUtil.findAll(em, Espece.class).get(0);
        }
        em.persist(espece);
        em.flush();
        sousEspece.setEspece(espece);
        sousEspeceRepository.saveAndFlush(sousEspece);
        Long especeId = espece.getId();

        // Get all the sousEspeceList where espece equals to especeId
        defaultSousEspeceShouldBeFound("especeId.equals=" + especeId);

        // Get all the sousEspeceList where espece equals to (especeId + 1)
        defaultSousEspeceShouldNotBeFound("especeId.equals=" + (especeId + 1));
    }

    @Test
    @Transactional
    void getAllSousEspecesBySousEspeceIsEqualToSomething() throws Exception {
        // Initialize the database
        sousEspeceRepository.saveAndFlush(sousEspece);
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
        sousEspece.setSousEspece(sousEspece);
        sousEspeceRepository.saveAndFlush(sousEspece);
        Long sousEspeceId = sousEspece.getId();

        // Get all the sousEspeceList where sousEspece equals to sousEspeceId
        defaultSousEspeceShouldBeFound("sousEspeceId.equals=" + sousEspeceId);

        // Get all the sousEspeceList where sousEspece equals to (sousEspeceId + 1)
        defaultSousEspeceShouldNotBeFound("sousEspeceId.equals=" + (sousEspeceId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSousEspeceShouldBeFound(String filter) throws Exception {
        restSousEspeceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sousEspece.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));

        // Check, that the count call also returns 1
        restSousEspeceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSousEspeceShouldNotBeFound(String filter) throws Exception {
        restSousEspeceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSousEspeceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSousEspece() throws Exception {
        // Get the sousEspece
        restSousEspeceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSousEspece() throws Exception {
        // Initialize the database
        sousEspeceRepository.saveAndFlush(sousEspece);

        int databaseSizeBeforeUpdate = sousEspeceRepository.findAll().size();

        // Update the sousEspece
        SousEspece updatedSousEspece = sousEspeceRepository.findById(sousEspece.getId()).get();
        // Disconnect from session so that the updates on updatedSousEspece are not directly saved in db
        em.detach(updatedSousEspece);
        updatedSousEspece.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restSousEspeceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSousEspece.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSousEspece))
            )
            .andExpect(status().isOk());

        // Validate the SousEspece in the database
        List<SousEspece> sousEspeceList = sousEspeceRepository.findAll();
        assertThat(sousEspeceList).hasSize(databaseSizeBeforeUpdate);
        SousEspece testSousEspece = sousEspeceList.get(sousEspeceList.size() - 1);
        assertThat(testSousEspece.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testSousEspece.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void putNonExistingSousEspece() throws Exception {
        int databaseSizeBeforeUpdate = sousEspeceRepository.findAll().size();
        sousEspece.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSousEspeceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sousEspece.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sousEspece))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousEspece in the database
        List<SousEspece> sousEspeceList = sousEspeceRepository.findAll();
        assertThat(sousEspeceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSousEspece() throws Exception {
        int databaseSizeBeforeUpdate = sousEspeceRepository.findAll().size();
        sousEspece.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousEspeceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sousEspece))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousEspece in the database
        List<SousEspece> sousEspeceList = sousEspeceRepository.findAll();
        assertThat(sousEspeceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSousEspece() throws Exception {
        int databaseSizeBeforeUpdate = sousEspeceRepository.findAll().size();
        sousEspece.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousEspeceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousEspece)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SousEspece in the database
        List<SousEspece> sousEspeceList = sousEspeceRepository.findAll();
        assertThat(sousEspeceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSousEspeceWithPatch() throws Exception {
        // Initialize the database
        sousEspeceRepository.saveAndFlush(sousEspece);

        int databaseSizeBeforeUpdate = sousEspeceRepository.findAll().size();

        // Update the sousEspece using partial update
        SousEspece partialUpdatedSousEspece = new SousEspece();
        partialUpdatedSousEspece.setId(sousEspece.getId());

        restSousEspeceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSousEspece.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSousEspece))
            )
            .andExpect(status().isOk());

        // Validate the SousEspece in the database
        List<SousEspece> sousEspeceList = sousEspeceRepository.findAll();
        assertThat(sousEspeceList).hasSize(databaseSizeBeforeUpdate);
        SousEspece testSousEspece = sousEspeceList.get(sousEspeceList.size() - 1);
        assertThat(testSousEspece.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testSousEspece.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void fullUpdateSousEspeceWithPatch() throws Exception {
        // Initialize the database
        sousEspeceRepository.saveAndFlush(sousEspece);

        int databaseSizeBeforeUpdate = sousEspeceRepository.findAll().size();

        // Update the sousEspece using partial update
        SousEspece partialUpdatedSousEspece = new SousEspece();
        partialUpdatedSousEspece.setId(sousEspece.getId());

        partialUpdatedSousEspece.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restSousEspeceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSousEspece.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSousEspece))
            )
            .andExpect(status().isOk());

        // Validate the SousEspece in the database
        List<SousEspece> sousEspeceList = sousEspeceRepository.findAll();
        assertThat(sousEspeceList).hasSize(databaseSizeBeforeUpdate);
        SousEspece testSousEspece = sousEspeceList.get(sousEspeceList.size() - 1);
        assertThat(testSousEspece.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testSousEspece.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void patchNonExistingSousEspece() throws Exception {
        int databaseSizeBeforeUpdate = sousEspeceRepository.findAll().size();
        sousEspece.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSousEspeceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sousEspece.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sousEspece))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousEspece in the database
        List<SousEspece> sousEspeceList = sousEspeceRepository.findAll();
        assertThat(sousEspeceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSousEspece() throws Exception {
        int databaseSizeBeforeUpdate = sousEspeceRepository.findAll().size();
        sousEspece.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousEspeceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sousEspece))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousEspece in the database
        List<SousEspece> sousEspeceList = sousEspeceRepository.findAll();
        assertThat(sousEspeceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSousEspece() throws Exception {
        int databaseSizeBeforeUpdate = sousEspeceRepository.findAll().size();
        sousEspece.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousEspeceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sousEspece))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SousEspece in the database
        List<SousEspece> sousEspeceList = sousEspeceRepository.findAll();
        assertThat(sousEspeceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSousEspece() throws Exception {
        // Initialize the database
        sousEspeceRepository.saveAndFlush(sousEspece);

        int databaseSizeBeforeDelete = sousEspeceRepository.findAll().size();

        // Delete the sousEspece
        restSousEspeceMockMvc
            .perform(delete(ENTITY_API_URL_ID, sousEspece.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SousEspece> sousEspeceList = sousEspeceRepository.findAll();
        assertThat(sousEspeceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
