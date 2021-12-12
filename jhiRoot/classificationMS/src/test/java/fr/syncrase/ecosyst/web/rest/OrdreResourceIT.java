package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.Ordre;
import fr.syncrase.ecosyst.domain.Ordre;
import fr.syncrase.ecosyst.domain.SousOrdre;
import fr.syncrase.ecosyst.domain.SuperOrdre;
import fr.syncrase.ecosyst.repository.OrdreRepository;
import fr.syncrase.ecosyst.service.criteria.OrdreCriteria;
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
 * Integration tests for the {@link OrdreResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrdreResourceIT {

    private static final String DEFAULT_NOM_FR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_LATIN = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LATIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ordres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrdreRepository ordreRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrdreMockMvc;

    private Ordre ordre;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ordre createEntity(EntityManager em) {
        Ordre ordre = new Ordre().nomFr(DEFAULT_NOM_FR).nomLatin(DEFAULT_NOM_LATIN);
        return ordre;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ordre createUpdatedEntity(EntityManager em) {
        Ordre ordre = new Ordre().nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);
        return ordre;
    }

    @BeforeEach
    public void initTest() {
        ordre = createEntity(em);
    }

    @Test
    @Transactional
    void createOrdre() throws Exception {
        int databaseSizeBeforeCreate = ordreRepository.findAll().size();
        // Create the Ordre
        restOrdreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordre)))
            .andExpect(status().isCreated());

        // Validate the Ordre in the database
        List<Ordre> ordreList = ordreRepository.findAll();
        assertThat(ordreList).hasSize(databaseSizeBeforeCreate + 1);
        Ordre testOrdre = ordreList.get(ordreList.size() - 1);
        assertThat(testOrdre.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testOrdre.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void createOrdreWithExistingId() throws Exception {
        // Create the Ordre with an existing ID
        ordre.setId(1L);

        int databaseSizeBeforeCreate = ordreRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordre)))
            .andExpect(status().isBadRequest());

        // Validate the Ordre in the database
        List<Ordre> ordreList = ordreRepository.findAll();
        assertThat(ordreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomFrIsRequired() throws Exception {
        int databaseSizeBeforeTest = ordreRepository.findAll().size();
        // set the field null
        ordre.setNomFr(null);

        // Create the Ordre, which fails.

        restOrdreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordre)))
            .andExpect(status().isBadRequest());

        List<Ordre> ordreList = ordreRepository.findAll();
        assertThat(ordreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrdres() throws Exception {
        // Initialize the database
        ordreRepository.saveAndFlush(ordre);

        // Get all the ordreList
        restOrdreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordre.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));
    }

    @Test
    @Transactional
    void getOrdre() throws Exception {
        // Initialize the database
        ordreRepository.saveAndFlush(ordre);

        // Get the ordre
        restOrdreMockMvc
            .perform(get(ENTITY_API_URL_ID, ordre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ordre.getId().intValue()))
            .andExpect(jsonPath("$.nomFr").value(DEFAULT_NOM_FR))
            .andExpect(jsonPath("$.nomLatin").value(DEFAULT_NOM_LATIN));
    }

    @Test
    @Transactional
    void getOrdresByIdFiltering() throws Exception {
        // Initialize the database
        ordreRepository.saveAndFlush(ordre);

        Long id = ordre.getId();

        defaultOrdreShouldBeFound("id.equals=" + id);
        defaultOrdreShouldNotBeFound("id.notEquals=" + id);

        defaultOrdreShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOrdreShouldNotBeFound("id.greaterThan=" + id);

        defaultOrdreShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOrdreShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrdresByNomFrIsEqualToSomething() throws Exception {
        // Initialize the database
        ordreRepository.saveAndFlush(ordre);

        // Get all the ordreList where nomFr equals to DEFAULT_NOM_FR
        defaultOrdreShouldBeFound("nomFr.equals=" + DEFAULT_NOM_FR);

        // Get all the ordreList where nomFr equals to UPDATED_NOM_FR
        defaultOrdreShouldNotBeFound("nomFr.equals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllOrdresByNomFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ordreRepository.saveAndFlush(ordre);

        // Get all the ordreList where nomFr not equals to DEFAULT_NOM_FR
        defaultOrdreShouldNotBeFound("nomFr.notEquals=" + DEFAULT_NOM_FR);

        // Get all the ordreList where nomFr not equals to UPDATED_NOM_FR
        defaultOrdreShouldBeFound("nomFr.notEquals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllOrdresByNomFrIsInShouldWork() throws Exception {
        // Initialize the database
        ordreRepository.saveAndFlush(ordre);

        // Get all the ordreList where nomFr in DEFAULT_NOM_FR or UPDATED_NOM_FR
        defaultOrdreShouldBeFound("nomFr.in=" + DEFAULT_NOM_FR + "," + UPDATED_NOM_FR);

        // Get all the ordreList where nomFr equals to UPDATED_NOM_FR
        defaultOrdreShouldNotBeFound("nomFr.in=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllOrdresByNomFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordreRepository.saveAndFlush(ordre);

        // Get all the ordreList where nomFr is not null
        defaultOrdreShouldBeFound("nomFr.specified=true");

        // Get all the ordreList where nomFr is null
        defaultOrdreShouldNotBeFound("nomFr.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdresByNomFrContainsSomething() throws Exception {
        // Initialize the database
        ordreRepository.saveAndFlush(ordre);

        // Get all the ordreList where nomFr contains DEFAULT_NOM_FR
        defaultOrdreShouldBeFound("nomFr.contains=" + DEFAULT_NOM_FR);

        // Get all the ordreList where nomFr contains UPDATED_NOM_FR
        defaultOrdreShouldNotBeFound("nomFr.contains=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllOrdresByNomFrNotContainsSomething() throws Exception {
        // Initialize the database
        ordreRepository.saveAndFlush(ordre);

        // Get all the ordreList where nomFr does not contain DEFAULT_NOM_FR
        defaultOrdreShouldNotBeFound("nomFr.doesNotContain=" + DEFAULT_NOM_FR);

        // Get all the ordreList where nomFr does not contain UPDATED_NOM_FR
        defaultOrdreShouldBeFound("nomFr.doesNotContain=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllOrdresByNomLatinIsEqualToSomething() throws Exception {
        // Initialize the database
        ordreRepository.saveAndFlush(ordre);

        // Get all the ordreList where nomLatin equals to DEFAULT_NOM_LATIN
        defaultOrdreShouldBeFound("nomLatin.equals=" + DEFAULT_NOM_LATIN);

        // Get all the ordreList where nomLatin equals to UPDATED_NOM_LATIN
        defaultOrdreShouldNotBeFound("nomLatin.equals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllOrdresByNomLatinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ordreRepository.saveAndFlush(ordre);

        // Get all the ordreList where nomLatin not equals to DEFAULT_NOM_LATIN
        defaultOrdreShouldNotBeFound("nomLatin.notEquals=" + DEFAULT_NOM_LATIN);

        // Get all the ordreList where nomLatin not equals to UPDATED_NOM_LATIN
        defaultOrdreShouldBeFound("nomLatin.notEquals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllOrdresByNomLatinIsInShouldWork() throws Exception {
        // Initialize the database
        ordreRepository.saveAndFlush(ordre);

        // Get all the ordreList where nomLatin in DEFAULT_NOM_LATIN or UPDATED_NOM_LATIN
        defaultOrdreShouldBeFound("nomLatin.in=" + DEFAULT_NOM_LATIN + "," + UPDATED_NOM_LATIN);

        // Get all the ordreList where nomLatin equals to UPDATED_NOM_LATIN
        defaultOrdreShouldNotBeFound("nomLatin.in=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllOrdresByNomLatinIsNullOrNotNull() throws Exception {
        // Initialize the database
        ordreRepository.saveAndFlush(ordre);

        // Get all the ordreList where nomLatin is not null
        defaultOrdreShouldBeFound("nomLatin.specified=true");

        // Get all the ordreList where nomLatin is null
        defaultOrdreShouldNotBeFound("nomLatin.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdresByNomLatinContainsSomething() throws Exception {
        // Initialize the database
        ordreRepository.saveAndFlush(ordre);

        // Get all the ordreList where nomLatin contains DEFAULT_NOM_LATIN
        defaultOrdreShouldBeFound("nomLatin.contains=" + DEFAULT_NOM_LATIN);

        // Get all the ordreList where nomLatin contains UPDATED_NOM_LATIN
        defaultOrdreShouldNotBeFound("nomLatin.contains=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllOrdresByNomLatinNotContainsSomething() throws Exception {
        // Initialize the database
        ordreRepository.saveAndFlush(ordre);

        // Get all the ordreList where nomLatin does not contain DEFAULT_NOM_LATIN
        defaultOrdreShouldNotBeFound("nomLatin.doesNotContain=" + DEFAULT_NOM_LATIN);

        // Get all the ordreList where nomLatin does not contain UPDATED_NOM_LATIN
        defaultOrdreShouldBeFound("nomLatin.doesNotContain=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllOrdresBySousOrdresIsEqualToSomething() throws Exception {
        // Initialize the database
        ordreRepository.saveAndFlush(ordre);
        SousOrdre sousOrdres;
        if (TestUtil.findAll(em, SousOrdre.class).isEmpty()) {
            sousOrdres = SousOrdreResourceIT.createEntity(em);
            em.persist(sousOrdres);
            em.flush();
        } else {
            sousOrdres = TestUtil.findAll(em, SousOrdre.class).get(0);
        }
        em.persist(sousOrdres);
        em.flush();
        ordre.addSousOrdres(sousOrdres);
        ordreRepository.saveAndFlush(ordre);
        Long sousOrdresId = sousOrdres.getId();

        // Get all the ordreList where sousOrdres equals to sousOrdresId
        defaultOrdreShouldBeFound("sousOrdresId.equals=" + sousOrdresId);

        // Get all the ordreList where sousOrdres equals to (sousOrdresId + 1)
        defaultOrdreShouldNotBeFound("sousOrdresId.equals=" + (sousOrdresId + 1));
    }

    @Test
    @Transactional
    void getAllOrdresBySynonymesIsEqualToSomething() throws Exception {
        // Initialize the database
        ordreRepository.saveAndFlush(ordre);
        Ordre synonymes;
        if (TestUtil.findAll(em, Ordre.class).isEmpty()) {
            synonymes = OrdreResourceIT.createEntity(em);
            em.persist(synonymes);
            em.flush();
        } else {
            synonymes = TestUtil.findAll(em, Ordre.class).get(0);
        }
        em.persist(synonymes);
        em.flush();
        ordre.addSynonymes(synonymes);
        ordreRepository.saveAndFlush(ordre);
        Long synonymesId = synonymes.getId();

        // Get all the ordreList where synonymes equals to synonymesId
        defaultOrdreShouldBeFound("synonymesId.equals=" + synonymesId);

        // Get all the ordreList where synonymes equals to (synonymesId + 1)
        defaultOrdreShouldNotBeFound("synonymesId.equals=" + (synonymesId + 1));
    }

    @Test
    @Transactional
    void getAllOrdresBySuperOrdreIsEqualToSomething() throws Exception {
        // Initialize the database
        ordreRepository.saveAndFlush(ordre);
        SuperOrdre superOrdre;
        if (TestUtil.findAll(em, SuperOrdre.class).isEmpty()) {
            superOrdre = SuperOrdreResourceIT.createEntity(em);
            em.persist(superOrdre);
            em.flush();
        } else {
            superOrdre = TestUtil.findAll(em, SuperOrdre.class).get(0);
        }
        em.persist(superOrdre);
        em.flush();
        ordre.setSuperOrdre(superOrdre);
        ordreRepository.saveAndFlush(ordre);
        Long superOrdreId = superOrdre.getId();

        // Get all the ordreList where superOrdre equals to superOrdreId
        defaultOrdreShouldBeFound("superOrdreId.equals=" + superOrdreId);

        // Get all the ordreList where superOrdre equals to (superOrdreId + 1)
        defaultOrdreShouldNotBeFound("superOrdreId.equals=" + (superOrdreId + 1));
    }

    @Test
    @Transactional
    void getAllOrdresByOrdreIsEqualToSomething() throws Exception {
        // Initialize the database
        ordreRepository.saveAndFlush(ordre);
        Ordre ordre;
        if (TestUtil.findAll(em, Ordre.class).isEmpty()) {
            ordre = OrdreResourceIT.createEntity(em);
            em.persist(ordre);
            em.flush();
        } else {
            ordre = TestUtil.findAll(em, Ordre.class).get(0);
        }
        em.persist(ordre);
        em.flush();
        ordre.setOrdre(ordre);
        ordreRepository.saveAndFlush(ordre);
        Long ordreId = ordre.getId();

        // Get all the ordreList where ordre equals to ordreId
        defaultOrdreShouldBeFound("ordreId.equals=" + ordreId);

        // Get all the ordreList where ordre equals to (ordreId + 1)
        defaultOrdreShouldNotBeFound("ordreId.equals=" + (ordreId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrdreShouldBeFound(String filter) throws Exception {
        restOrdreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordre.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));

        // Check, that the count call also returns 1
        restOrdreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrdreShouldNotBeFound(String filter) throws Exception {
        restOrdreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrdreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrdre() throws Exception {
        // Get the ordre
        restOrdreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrdre() throws Exception {
        // Initialize the database
        ordreRepository.saveAndFlush(ordre);

        int databaseSizeBeforeUpdate = ordreRepository.findAll().size();

        // Update the ordre
        Ordre updatedOrdre = ordreRepository.findById(ordre.getId()).get();
        // Disconnect from session so that the updates on updatedOrdre are not directly saved in db
        em.detach(updatedOrdre);
        updatedOrdre.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restOrdreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrdre.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrdre))
            )
            .andExpect(status().isOk());

        // Validate the Ordre in the database
        List<Ordre> ordreList = ordreRepository.findAll();
        assertThat(ordreList).hasSize(databaseSizeBeforeUpdate);
        Ordre testOrdre = ordreList.get(ordreList.size() - 1);
        assertThat(testOrdre.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testOrdre.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void putNonExistingOrdre() throws Exception {
        int databaseSizeBeforeUpdate = ordreRepository.findAll().size();
        ordre.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordre.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ordre in the database
        List<Ordre> ordreList = ordreRepository.findAll();
        assertThat(ordreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrdre() throws Exception {
        int databaseSizeBeforeUpdate = ordreRepository.findAll().size();
        ordre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ordre in the database
        List<Ordre> ordreList = ordreRepository.findAll();
        assertThat(ordreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrdre() throws Exception {
        int databaseSizeBeforeUpdate = ordreRepository.findAll().size();
        ordre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdreMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordre)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ordre in the database
        List<Ordre> ordreList = ordreRepository.findAll();
        assertThat(ordreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrdreWithPatch() throws Exception {
        // Initialize the database
        ordreRepository.saveAndFlush(ordre);

        int databaseSizeBeforeUpdate = ordreRepository.findAll().size();

        // Update the ordre using partial update
        Ordre partialUpdatedOrdre = new Ordre();
        partialUpdatedOrdre.setId(ordre.getId());

        partialUpdatedOrdre.nomFr(UPDATED_NOM_FR);

        restOrdreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdre))
            )
            .andExpect(status().isOk());

        // Validate the Ordre in the database
        List<Ordre> ordreList = ordreRepository.findAll();
        assertThat(ordreList).hasSize(databaseSizeBeforeUpdate);
        Ordre testOrdre = ordreList.get(ordreList.size() - 1);
        assertThat(testOrdre.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testOrdre.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void fullUpdateOrdreWithPatch() throws Exception {
        // Initialize the database
        ordreRepository.saveAndFlush(ordre);

        int databaseSizeBeforeUpdate = ordreRepository.findAll().size();

        // Update the ordre using partial update
        Ordre partialUpdatedOrdre = new Ordre();
        partialUpdatedOrdre.setId(ordre.getId());

        partialUpdatedOrdre.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restOrdreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdre))
            )
            .andExpect(status().isOk());

        // Validate the Ordre in the database
        List<Ordre> ordreList = ordreRepository.findAll();
        assertThat(ordreList).hasSize(databaseSizeBeforeUpdate);
        Ordre testOrdre = ordreList.get(ordreList.size() - 1);
        assertThat(testOrdre.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testOrdre.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void patchNonExistingOrdre() throws Exception {
        int databaseSizeBeforeUpdate = ordreRepository.findAll().size();
        ordre.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ordre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ordre in the database
        List<Ordre> ordreList = ordreRepository.findAll();
        assertThat(ordreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrdre() throws Exception {
        int databaseSizeBeforeUpdate = ordreRepository.findAll().size();
        ordre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ordre in the database
        List<Ordre> ordreList = ordreRepository.findAll();
        assertThat(ordreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrdre() throws Exception {
        int databaseSizeBeforeUpdate = ordreRepository.findAll().size();
        ordre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdreMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ordre)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ordre in the database
        List<Ordre> ordreList = ordreRepository.findAll();
        assertThat(ordreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrdre() throws Exception {
        // Initialize the database
        ordreRepository.saveAndFlush(ordre);

        int databaseSizeBeforeDelete = ordreRepository.findAll().size();

        // Delete the ordre
        restOrdreMockMvc
            .perform(delete(ENTITY_API_URL_ID, ordre.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ordre> ordreList = ordreRepository.findAll();
        assertThat(ordreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
