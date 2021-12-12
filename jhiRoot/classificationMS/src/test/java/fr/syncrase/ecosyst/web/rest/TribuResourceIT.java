package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.SousFamille;
import fr.syncrase.ecosyst.domain.SousTribu;
import fr.syncrase.ecosyst.domain.Tribu;
import fr.syncrase.ecosyst.domain.Tribu;
import fr.syncrase.ecosyst.repository.TribuRepository;
import fr.syncrase.ecosyst.service.criteria.TribuCriteria;
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
 * Integration tests for the {@link TribuResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TribuResourceIT {

    private static final String DEFAULT_NOM_FR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_LATIN = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LATIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tribus";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TribuRepository tribuRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTribuMockMvc;

    private Tribu tribu;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tribu createEntity(EntityManager em) {
        Tribu tribu = new Tribu().nomFr(DEFAULT_NOM_FR).nomLatin(DEFAULT_NOM_LATIN);
        return tribu;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tribu createUpdatedEntity(EntityManager em) {
        Tribu tribu = new Tribu().nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);
        return tribu;
    }

    @BeforeEach
    public void initTest() {
        tribu = createEntity(em);
    }

    @Test
    @Transactional
    void createTribu() throws Exception {
        int databaseSizeBeforeCreate = tribuRepository.findAll().size();
        // Create the Tribu
        restTribuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tribu)))
            .andExpect(status().isCreated());

        // Validate the Tribu in the database
        List<Tribu> tribuList = tribuRepository.findAll();
        assertThat(tribuList).hasSize(databaseSizeBeforeCreate + 1);
        Tribu testTribu = tribuList.get(tribuList.size() - 1);
        assertThat(testTribu.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testTribu.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void createTribuWithExistingId() throws Exception {
        // Create the Tribu with an existing ID
        tribu.setId(1L);

        int databaseSizeBeforeCreate = tribuRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTribuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tribu)))
            .andExpect(status().isBadRequest());

        // Validate the Tribu in the database
        List<Tribu> tribuList = tribuRepository.findAll();
        assertThat(tribuList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomFrIsRequired() throws Exception {
        int databaseSizeBeforeTest = tribuRepository.findAll().size();
        // set the field null
        tribu.setNomFr(null);

        // Create the Tribu, which fails.

        restTribuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tribu)))
            .andExpect(status().isBadRequest());

        List<Tribu> tribuList = tribuRepository.findAll();
        assertThat(tribuList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTribus() throws Exception {
        // Initialize the database
        tribuRepository.saveAndFlush(tribu);

        // Get all the tribuList
        restTribuMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tribu.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));
    }

    @Test
    @Transactional
    void getTribu() throws Exception {
        // Initialize the database
        tribuRepository.saveAndFlush(tribu);

        // Get the tribu
        restTribuMockMvc
            .perform(get(ENTITY_API_URL_ID, tribu.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tribu.getId().intValue()))
            .andExpect(jsonPath("$.nomFr").value(DEFAULT_NOM_FR))
            .andExpect(jsonPath("$.nomLatin").value(DEFAULT_NOM_LATIN));
    }

    @Test
    @Transactional
    void getTribusByIdFiltering() throws Exception {
        // Initialize the database
        tribuRepository.saveAndFlush(tribu);

        Long id = tribu.getId();

        defaultTribuShouldBeFound("id.equals=" + id);
        defaultTribuShouldNotBeFound("id.notEquals=" + id);

        defaultTribuShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTribuShouldNotBeFound("id.greaterThan=" + id);

        defaultTribuShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTribuShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTribusByNomFrIsEqualToSomething() throws Exception {
        // Initialize the database
        tribuRepository.saveAndFlush(tribu);

        // Get all the tribuList where nomFr equals to DEFAULT_NOM_FR
        defaultTribuShouldBeFound("nomFr.equals=" + DEFAULT_NOM_FR);

        // Get all the tribuList where nomFr equals to UPDATED_NOM_FR
        defaultTribuShouldNotBeFound("nomFr.equals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllTribusByNomFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tribuRepository.saveAndFlush(tribu);

        // Get all the tribuList where nomFr not equals to DEFAULT_NOM_FR
        defaultTribuShouldNotBeFound("nomFr.notEquals=" + DEFAULT_NOM_FR);

        // Get all the tribuList where nomFr not equals to UPDATED_NOM_FR
        defaultTribuShouldBeFound("nomFr.notEquals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllTribusByNomFrIsInShouldWork() throws Exception {
        // Initialize the database
        tribuRepository.saveAndFlush(tribu);

        // Get all the tribuList where nomFr in DEFAULT_NOM_FR or UPDATED_NOM_FR
        defaultTribuShouldBeFound("nomFr.in=" + DEFAULT_NOM_FR + "," + UPDATED_NOM_FR);

        // Get all the tribuList where nomFr equals to UPDATED_NOM_FR
        defaultTribuShouldNotBeFound("nomFr.in=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllTribusByNomFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        tribuRepository.saveAndFlush(tribu);

        // Get all the tribuList where nomFr is not null
        defaultTribuShouldBeFound("nomFr.specified=true");

        // Get all the tribuList where nomFr is null
        defaultTribuShouldNotBeFound("nomFr.specified=false");
    }

    @Test
    @Transactional
    void getAllTribusByNomFrContainsSomething() throws Exception {
        // Initialize the database
        tribuRepository.saveAndFlush(tribu);

        // Get all the tribuList where nomFr contains DEFAULT_NOM_FR
        defaultTribuShouldBeFound("nomFr.contains=" + DEFAULT_NOM_FR);

        // Get all the tribuList where nomFr contains UPDATED_NOM_FR
        defaultTribuShouldNotBeFound("nomFr.contains=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllTribusByNomFrNotContainsSomething() throws Exception {
        // Initialize the database
        tribuRepository.saveAndFlush(tribu);

        // Get all the tribuList where nomFr does not contain DEFAULT_NOM_FR
        defaultTribuShouldNotBeFound("nomFr.doesNotContain=" + DEFAULT_NOM_FR);

        // Get all the tribuList where nomFr does not contain UPDATED_NOM_FR
        defaultTribuShouldBeFound("nomFr.doesNotContain=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllTribusByNomLatinIsEqualToSomething() throws Exception {
        // Initialize the database
        tribuRepository.saveAndFlush(tribu);

        // Get all the tribuList where nomLatin equals to DEFAULT_NOM_LATIN
        defaultTribuShouldBeFound("nomLatin.equals=" + DEFAULT_NOM_LATIN);

        // Get all the tribuList where nomLatin equals to UPDATED_NOM_LATIN
        defaultTribuShouldNotBeFound("nomLatin.equals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllTribusByNomLatinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        tribuRepository.saveAndFlush(tribu);

        // Get all the tribuList where nomLatin not equals to DEFAULT_NOM_LATIN
        defaultTribuShouldNotBeFound("nomLatin.notEquals=" + DEFAULT_NOM_LATIN);

        // Get all the tribuList where nomLatin not equals to UPDATED_NOM_LATIN
        defaultTribuShouldBeFound("nomLatin.notEquals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllTribusByNomLatinIsInShouldWork() throws Exception {
        // Initialize the database
        tribuRepository.saveAndFlush(tribu);

        // Get all the tribuList where nomLatin in DEFAULT_NOM_LATIN or UPDATED_NOM_LATIN
        defaultTribuShouldBeFound("nomLatin.in=" + DEFAULT_NOM_LATIN + "," + UPDATED_NOM_LATIN);

        // Get all the tribuList where nomLatin equals to UPDATED_NOM_LATIN
        defaultTribuShouldNotBeFound("nomLatin.in=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllTribusByNomLatinIsNullOrNotNull() throws Exception {
        // Initialize the database
        tribuRepository.saveAndFlush(tribu);

        // Get all the tribuList where nomLatin is not null
        defaultTribuShouldBeFound("nomLatin.specified=true");

        // Get all the tribuList where nomLatin is null
        defaultTribuShouldNotBeFound("nomLatin.specified=false");
    }

    @Test
    @Transactional
    void getAllTribusByNomLatinContainsSomething() throws Exception {
        // Initialize the database
        tribuRepository.saveAndFlush(tribu);

        // Get all the tribuList where nomLatin contains DEFAULT_NOM_LATIN
        defaultTribuShouldBeFound("nomLatin.contains=" + DEFAULT_NOM_LATIN);

        // Get all the tribuList where nomLatin contains UPDATED_NOM_LATIN
        defaultTribuShouldNotBeFound("nomLatin.contains=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllTribusByNomLatinNotContainsSomething() throws Exception {
        // Initialize the database
        tribuRepository.saveAndFlush(tribu);

        // Get all the tribuList where nomLatin does not contain DEFAULT_NOM_LATIN
        defaultTribuShouldNotBeFound("nomLatin.doesNotContain=" + DEFAULT_NOM_LATIN);

        // Get all the tribuList where nomLatin does not contain UPDATED_NOM_LATIN
        defaultTribuShouldBeFound("nomLatin.doesNotContain=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllTribusBySousTribusIsEqualToSomething() throws Exception {
        // Initialize the database
        tribuRepository.saveAndFlush(tribu);
        SousTribu sousTribus;
        if (TestUtil.findAll(em, SousTribu.class).isEmpty()) {
            sousTribus = SousTribuResourceIT.createEntity(em);
            em.persist(sousTribus);
            em.flush();
        } else {
            sousTribus = TestUtil.findAll(em, SousTribu.class).get(0);
        }
        em.persist(sousTribus);
        em.flush();
        tribu.addSousTribus(sousTribus);
        tribuRepository.saveAndFlush(tribu);
        Long sousTribusId = sousTribus.getId();

        // Get all the tribuList where sousTribus equals to sousTribusId
        defaultTribuShouldBeFound("sousTribusId.equals=" + sousTribusId);

        // Get all the tribuList where sousTribus equals to (sousTribusId + 1)
        defaultTribuShouldNotBeFound("sousTribusId.equals=" + (sousTribusId + 1));
    }

    @Test
    @Transactional
    void getAllTribusBySynonymesIsEqualToSomething() throws Exception {
        // Initialize the database
        tribuRepository.saveAndFlush(tribu);
        Tribu synonymes;
        if (TestUtil.findAll(em, Tribu.class).isEmpty()) {
            synonymes = TribuResourceIT.createEntity(em);
            em.persist(synonymes);
            em.flush();
        } else {
            synonymes = TestUtil.findAll(em, Tribu.class).get(0);
        }
        em.persist(synonymes);
        em.flush();
        tribu.addSynonymes(synonymes);
        tribuRepository.saveAndFlush(tribu);
        Long synonymesId = synonymes.getId();

        // Get all the tribuList where synonymes equals to synonymesId
        defaultTribuShouldBeFound("synonymesId.equals=" + synonymesId);

        // Get all the tribuList where synonymes equals to (synonymesId + 1)
        defaultTribuShouldNotBeFound("synonymesId.equals=" + (synonymesId + 1));
    }

    @Test
    @Transactional
    void getAllTribusBySousFamilleIsEqualToSomething() throws Exception {
        // Initialize the database
        tribuRepository.saveAndFlush(tribu);
        SousFamille sousFamille;
        if (TestUtil.findAll(em, SousFamille.class).isEmpty()) {
            sousFamille = SousFamilleResourceIT.createEntity(em);
            em.persist(sousFamille);
            em.flush();
        } else {
            sousFamille = TestUtil.findAll(em, SousFamille.class).get(0);
        }
        em.persist(sousFamille);
        em.flush();
        tribu.setSousFamille(sousFamille);
        tribuRepository.saveAndFlush(tribu);
        Long sousFamilleId = sousFamille.getId();

        // Get all the tribuList where sousFamille equals to sousFamilleId
        defaultTribuShouldBeFound("sousFamilleId.equals=" + sousFamilleId);

        // Get all the tribuList where sousFamille equals to (sousFamilleId + 1)
        defaultTribuShouldNotBeFound("sousFamilleId.equals=" + (sousFamilleId + 1));
    }

    @Test
    @Transactional
    void getAllTribusByTribuIsEqualToSomething() throws Exception {
        // Initialize the database
        tribuRepository.saveAndFlush(tribu);
        Tribu tribu;
        if (TestUtil.findAll(em, Tribu.class).isEmpty()) {
            tribu = TribuResourceIT.createEntity(em);
            em.persist(tribu);
            em.flush();
        } else {
            tribu = TestUtil.findAll(em, Tribu.class).get(0);
        }
        em.persist(tribu);
        em.flush();
        tribu.setTribu(tribu);
        tribuRepository.saveAndFlush(tribu);
        Long tribuId = tribu.getId();

        // Get all the tribuList where tribu equals to tribuId
        defaultTribuShouldBeFound("tribuId.equals=" + tribuId);

        // Get all the tribuList where tribu equals to (tribuId + 1)
        defaultTribuShouldNotBeFound("tribuId.equals=" + (tribuId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTribuShouldBeFound(String filter) throws Exception {
        restTribuMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tribu.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));

        // Check, that the count call also returns 1
        restTribuMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTribuShouldNotBeFound(String filter) throws Exception {
        restTribuMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTribuMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTribu() throws Exception {
        // Get the tribu
        restTribuMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTribu() throws Exception {
        // Initialize the database
        tribuRepository.saveAndFlush(tribu);

        int databaseSizeBeforeUpdate = tribuRepository.findAll().size();

        // Update the tribu
        Tribu updatedTribu = tribuRepository.findById(tribu.getId()).get();
        // Disconnect from session so that the updates on updatedTribu are not directly saved in db
        em.detach(updatedTribu);
        updatedTribu.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restTribuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTribu.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTribu))
            )
            .andExpect(status().isOk());

        // Validate the Tribu in the database
        List<Tribu> tribuList = tribuRepository.findAll();
        assertThat(tribuList).hasSize(databaseSizeBeforeUpdate);
        Tribu testTribu = tribuList.get(tribuList.size() - 1);
        assertThat(testTribu.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testTribu.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void putNonExistingTribu() throws Exception {
        int databaseSizeBeforeUpdate = tribuRepository.findAll().size();
        tribu.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTribuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tribu.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tribu))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tribu in the database
        List<Tribu> tribuList = tribuRepository.findAll();
        assertThat(tribuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTribu() throws Exception {
        int databaseSizeBeforeUpdate = tribuRepository.findAll().size();
        tribu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTribuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tribu))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tribu in the database
        List<Tribu> tribuList = tribuRepository.findAll();
        assertThat(tribuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTribu() throws Exception {
        int databaseSizeBeforeUpdate = tribuRepository.findAll().size();
        tribu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTribuMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tribu)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tribu in the database
        List<Tribu> tribuList = tribuRepository.findAll();
        assertThat(tribuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTribuWithPatch() throws Exception {
        // Initialize the database
        tribuRepository.saveAndFlush(tribu);

        int databaseSizeBeforeUpdate = tribuRepository.findAll().size();

        // Update the tribu using partial update
        Tribu partialUpdatedTribu = new Tribu();
        partialUpdatedTribu.setId(tribu.getId());

        partialUpdatedTribu.nomLatin(UPDATED_NOM_LATIN);

        restTribuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTribu.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTribu))
            )
            .andExpect(status().isOk());

        // Validate the Tribu in the database
        List<Tribu> tribuList = tribuRepository.findAll();
        assertThat(tribuList).hasSize(databaseSizeBeforeUpdate);
        Tribu testTribu = tribuList.get(tribuList.size() - 1);
        assertThat(testTribu.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testTribu.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void fullUpdateTribuWithPatch() throws Exception {
        // Initialize the database
        tribuRepository.saveAndFlush(tribu);

        int databaseSizeBeforeUpdate = tribuRepository.findAll().size();

        // Update the tribu using partial update
        Tribu partialUpdatedTribu = new Tribu();
        partialUpdatedTribu.setId(tribu.getId());

        partialUpdatedTribu.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restTribuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTribu.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTribu))
            )
            .andExpect(status().isOk());

        // Validate the Tribu in the database
        List<Tribu> tribuList = tribuRepository.findAll();
        assertThat(tribuList).hasSize(databaseSizeBeforeUpdate);
        Tribu testTribu = tribuList.get(tribuList.size() - 1);
        assertThat(testTribu.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testTribu.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void patchNonExistingTribu() throws Exception {
        int databaseSizeBeforeUpdate = tribuRepository.findAll().size();
        tribu.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTribuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tribu.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tribu))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tribu in the database
        List<Tribu> tribuList = tribuRepository.findAll();
        assertThat(tribuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTribu() throws Exception {
        int databaseSizeBeforeUpdate = tribuRepository.findAll().size();
        tribu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTribuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tribu))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tribu in the database
        List<Tribu> tribuList = tribuRepository.findAll();
        assertThat(tribuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTribu() throws Exception {
        int databaseSizeBeforeUpdate = tribuRepository.findAll().size();
        tribu.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTribuMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tribu)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tribu in the database
        List<Tribu> tribuList = tribuRepository.findAll();
        assertThat(tribuList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTribu() throws Exception {
        // Initialize the database
        tribuRepository.saveAndFlush(tribu);

        int databaseSizeBeforeDelete = tribuRepository.findAll().size();

        // Delete the tribu
        restTribuMockMvc
            .perform(delete(ENTITY_API_URL_ID, tribu.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tribu> tribuList = tribuRepository.findAll();
        assertThat(tribuList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
