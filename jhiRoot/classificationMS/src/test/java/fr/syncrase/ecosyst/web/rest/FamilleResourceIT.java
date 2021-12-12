package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.Famille;
import fr.syncrase.ecosyst.domain.Famille;
import fr.syncrase.ecosyst.domain.SousFamille;
import fr.syncrase.ecosyst.domain.SuperFamille;
import fr.syncrase.ecosyst.repository.FamilleRepository;
import fr.syncrase.ecosyst.service.criteria.FamilleCriteria;
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
 * Integration tests for the {@link FamilleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FamilleResourceIT {

    private static final String DEFAULT_NOM_FR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_LATIN = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LATIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/familles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FamilleRepository familleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFamilleMockMvc;

    private Famille famille;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Famille createEntity(EntityManager em) {
        Famille famille = new Famille().nomFr(DEFAULT_NOM_FR).nomLatin(DEFAULT_NOM_LATIN);
        return famille;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Famille createUpdatedEntity(EntityManager em) {
        Famille famille = new Famille().nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);
        return famille;
    }

    @BeforeEach
    public void initTest() {
        famille = createEntity(em);
    }

    @Test
    @Transactional
    void createFamille() throws Exception {
        int databaseSizeBeforeCreate = familleRepository.findAll().size();
        // Create the Famille
        restFamilleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(famille)))
            .andExpect(status().isCreated());

        // Validate the Famille in the database
        List<Famille> familleList = familleRepository.findAll();
        assertThat(familleList).hasSize(databaseSizeBeforeCreate + 1);
        Famille testFamille = familleList.get(familleList.size() - 1);
        assertThat(testFamille.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testFamille.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void createFamilleWithExistingId() throws Exception {
        // Create the Famille with an existing ID
        famille.setId(1L);

        int databaseSizeBeforeCreate = familleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFamilleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(famille)))
            .andExpect(status().isBadRequest());

        // Validate the Famille in the database
        List<Famille> familleList = familleRepository.findAll();
        assertThat(familleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomFrIsRequired() throws Exception {
        int databaseSizeBeforeTest = familleRepository.findAll().size();
        // set the field null
        famille.setNomFr(null);

        // Create the Famille, which fails.

        restFamilleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(famille)))
            .andExpect(status().isBadRequest());

        List<Famille> familleList = familleRepository.findAll();
        assertThat(familleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFamilles() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList
        restFamilleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(famille.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));
    }

    @Test
    @Transactional
    void getFamille() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get the famille
        restFamilleMockMvc
            .perform(get(ENTITY_API_URL_ID, famille.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(famille.getId().intValue()))
            .andExpect(jsonPath("$.nomFr").value(DEFAULT_NOM_FR))
            .andExpect(jsonPath("$.nomLatin").value(DEFAULT_NOM_LATIN));
    }

    @Test
    @Transactional
    void getFamillesByIdFiltering() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        Long id = famille.getId();

        defaultFamilleShouldBeFound("id.equals=" + id);
        defaultFamilleShouldNotBeFound("id.notEquals=" + id);

        defaultFamilleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFamilleShouldNotBeFound("id.greaterThan=" + id);

        defaultFamilleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFamilleShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFamillesByNomFrIsEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where nomFr equals to DEFAULT_NOM_FR
        defaultFamilleShouldBeFound("nomFr.equals=" + DEFAULT_NOM_FR);

        // Get all the familleList where nomFr equals to UPDATED_NOM_FR
        defaultFamilleShouldNotBeFound("nomFr.equals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllFamillesByNomFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where nomFr not equals to DEFAULT_NOM_FR
        defaultFamilleShouldNotBeFound("nomFr.notEquals=" + DEFAULT_NOM_FR);

        // Get all the familleList where nomFr not equals to UPDATED_NOM_FR
        defaultFamilleShouldBeFound("nomFr.notEquals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllFamillesByNomFrIsInShouldWork() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where nomFr in DEFAULT_NOM_FR or UPDATED_NOM_FR
        defaultFamilleShouldBeFound("nomFr.in=" + DEFAULT_NOM_FR + "," + UPDATED_NOM_FR);

        // Get all the familleList where nomFr equals to UPDATED_NOM_FR
        defaultFamilleShouldNotBeFound("nomFr.in=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllFamillesByNomFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where nomFr is not null
        defaultFamilleShouldBeFound("nomFr.specified=true");

        // Get all the familleList where nomFr is null
        defaultFamilleShouldNotBeFound("nomFr.specified=false");
    }

    @Test
    @Transactional
    void getAllFamillesByNomFrContainsSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where nomFr contains DEFAULT_NOM_FR
        defaultFamilleShouldBeFound("nomFr.contains=" + DEFAULT_NOM_FR);

        // Get all the familleList where nomFr contains UPDATED_NOM_FR
        defaultFamilleShouldNotBeFound("nomFr.contains=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllFamillesByNomFrNotContainsSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where nomFr does not contain DEFAULT_NOM_FR
        defaultFamilleShouldNotBeFound("nomFr.doesNotContain=" + DEFAULT_NOM_FR);

        // Get all the familleList where nomFr does not contain UPDATED_NOM_FR
        defaultFamilleShouldBeFound("nomFr.doesNotContain=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllFamillesByNomLatinIsEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where nomLatin equals to DEFAULT_NOM_LATIN
        defaultFamilleShouldBeFound("nomLatin.equals=" + DEFAULT_NOM_LATIN);

        // Get all the familleList where nomLatin equals to UPDATED_NOM_LATIN
        defaultFamilleShouldNotBeFound("nomLatin.equals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllFamillesByNomLatinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where nomLatin not equals to DEFAULT_NOM_LATIN
        defaultFamilleShouldNotBeFound("nomLatin.notEquals=" + DEFAULT_NOM_LATIN);

        // Get all the familleList where nomLatin not equals to UPDATED_NOM_LATIN
        defaultFamilleShouldBeFound("nomLatin.notEquals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllFamillesByNomLatinIsInShouldWork() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where nomLatin in DEFAULT_NOM_LATIN or UPDATED_NOM_LATIN
        defaultFamilleShouldBeFound("nomLatin.in=" + DEFAULT_NOM_LATIN + "," + UPDATED_NOM_LATIN);

        // Get all the familleList where nomLatin equals to UPDATED_NOM_LATIN
        defaultFamilleShouldNotBeFound("nomLatin.in=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllFamillesByNomLatinIsNullOrNotNull() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where nomLatin is not null
        defaultFamilleShouldBeFound("nomLatin.specified=true");

        // Get all the familleList where nomLatin is null
        defaultFamilleShouldNotBeFound("nomLatin.specified=false");
    }

    @Test
    @Transactional
    void getAllFamillesByNomLatinContainsSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where nomLatin contains DEFAULT_NOM_LATIN
        defaultFamilleShouldBeFound("nomLatin.contains=" + DEFAULT_NOM_LATIN);

        // Get all the familleList where nomLatin contains UPDATED_NOM_LATIN
        defaultFamilleShouldNotBeFound("nomLatin.contains=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllFamillesByNomLatinNotContainsSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        // Get all the familleList where nomLatin does not contain DEFAULT_NOM_LATIN
        defaultFamilleShouldNotBeFound("nomLatin.doesNotContain=" + DEFAULT_NOM_LATIN);

        // Get all the familleList where nomLatin does not contain UPDATED_NOM_LATIN
        defaultFamilleShouldBeFound("nomLatin.doesNotContain=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllFamillesBySousFamillesIsEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);
        SousFamille sousFamilles;
        if (TestUtil.findAll(em, SousFamille.class).isEmpty()) {
            sousFamilles = SousFamilleResourceIT.createEntity(em);
            em.persist(sousFamilles);
            em.flush();
        } else {
            sousFamilles = TestUtil.findAll(em, SousFamille.class).get(0);
        }
        em.persist(sousFamilles);
        em.flush();
        famille.addSousFamilles(sousFamilles);
        familleRepository.saveAndFlush(famille);
        Long sousFamillesId = sousFamilles.getId();

        // Get all the familleList where sousFamilles equals to sousFamillesId
        defaultFamilleShouldBeFound("sousFamillesId.equals=" + sousFamillesId);

        // Get all the familleList where sousFamilles equals to (sousFamillesId + 1)
        defaultFamilleShouldNotBeFound("sousFamillesId.equals=" + (sousFamillesId + 1));
    }

    @Test
    @Transactional
    void getAllFamillesBySynonymesIsEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);
        Famille synonymes;
        if (TestUtil.findAll(em, Famille.class).isEmpty()) {
            synonymes = FamilleResourceIT.createEntity(em);
            em.persist(synonymes);
            em.flush();
        } else {
            synonymes = TestUtil.findAll(em, Famille.class).get(0);
        }
        em.persist(synonymes);
        em.flush();
        famille.addSynonymes(synonymes);
        familleRepository.saveAndFlush(famille);
        Long synonymesId = synonymes.getId();

        // Get all the familleList where synonymes equals to synonymesId
        defaultFamilleShouldBeFound("synonymesId.equals=" + synonymesId);

        // Get all the familleList where synonymes equals to (synonymesId + 1)
        defaultFamilleShouldNotBeFound("synonymesId.equals=" + (synonymesId + 1));
    }

    @Test
    @Transactional
    void getAllFamillesBySuperFamilleIsEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);
        SuperFamille superFamille;
        if (TestUtil.findAll(em, SuperFamille.class).isEmpty()) {
            superFamille = SuperFamilleResourceIT.createEntity(em);
            em.persist(superFamille);
            em.flush();
        } else {
            superFamille = TestUtil.findAll(em, SuperFamille.class).get(0);
        }
        em.persist(superFamille);
        em.flush();
        famille.setSuperFamille(superFamille);
        familleRepository.saveAndFlush(famille);
        Long superFamilleId = superFamille.getId();

        // Get all the familleList where superFamille equals to superFamilleId
        defaultFamilleShouldBeFound("superFamilleId.equals=" + superFamilleId);

        // Get all the familleList where superFamille equals to (superFamilleId + 1)
        defaultFamilleShouldNotBeFound("superFamilleId.equals=" + (superFamilleId + 1));
    }

    @Test
    @Transactional
    void getAllFamillesByFamilleIsEqualToSomething() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);
        Famille famille;
        if (TestUtil.findAll(em, Famille.class).isEmpty()) {
            famille = FamilleResourceIT.createEntity(em);
            em.persist(famille);
            em.flush();
        } else {
            famille = TestUtil.findAll(em, Famille.class).get(0);
        }
        em.persist(famille);
        em.flush();
        famille.setFamille(famille);
        familleRepository.saveAndFlush(famille);
        Long familleId = famille.getId();

        // Get all the familleList where famille equals to familleId
        defaultFamilleShouldBeFound("familleId.equals=" + familleId);

        // Get all the familleList where famille equals to (familleId + 1)
        defaultFamilleShouldNotBeFound("familleId.equals=" + (familleId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFamilleShouldBeFound(String filter) throws Exception {
        restFamilleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(famille.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));

        // Check, that the count call also returns 1
        restFamilleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFamilleShouldNotBeFound(String filter) throws Exception {
        restFamilleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFamilleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFamille() throws Exception {
        // Get the famille
        restFamilleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFamille() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        int databaseSizeBeforeUpdate = familleRepository.findAll().size();

        // Update the famille
        Famille updatedFamille = familleRepository.findById(famille.getId()).get();
        // Disconnect from session so that the updates on updatedFamille are not directly saved in db
        em.detach(updatedFamille);
        updatedFamille.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restFamilleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFamille.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFamille))
            )
            .andExpect(status().isOk());

        // Validate the Famille in the database
        List<Famille> familleList = familleRepository.findAll();
        assertThat(familleList).hasSize(databaseSizeBeforeUpdate);
        Famille testFamille = familleList.get(familleList.size() - 1);
        assertThat(testFamille.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testFamille.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void putNonExistingFamille() throws Exception {
        int databaseSizeBeforeUpdate = familleRepository.findAll().size();
        famille.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFamilleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, famille.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(famille))
            )
            .andExpect(status().isBadRequest());

        // Validate the Famille in the database
        List<Famille> familleList = familleRepository.findAll();
        assertThat(familleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFamille() throws Exception {
        int databaseSizeBeforeUpdate = familleRepository.findAll().size();
        famille.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(famille))
            )
            .andExpect(status().isBadRequest());

        // Validate the Famille in the database
        List<Famille> familleList = familleRepository.findAll();
        assertThat(familleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFamille() throws Exception {
        int databaseSizeBeforeUpdate = familleRepository.findAll().size();
        famille.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(famille)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Famille in the database
        List<Famille> familleList = familleRepository.findAll();
        assertThat(familleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFamilleWithPatch() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        int databaseSizeBeforeUpdate = familleRepository.findAll().size();

        // Update the famille using partial update
        Famille partialUpdatedFamille = new Famille();
        partialUpdatedFamille.setId(famille.getId());

        partialUpdatedFamille.nomLatin(UPDATED_NOM_LATIN);

        restFamilleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFamille.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFamille))
            )
            .andExpect(status().isOk());

        // Validate the Famille in the database
        List<Famille> familleList = familleRepository.findAll();
        assertThat(familleList).hasSize(databaseSizeBeforeUpdate);
        Famille testFamille = familleList.get(familleList.size() - 1);
        assertThat(testFamille.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testFamille.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void fullUpdateFamilleWithPatch() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        int databaseSizeBeforeUpdate = familleRepository.findAll().size();

        // Update the famille using partial update
        Famille partialUpdatedFamille = new Famille();
        partialUpdatedFamille.setId(famille.getId());

        partialUpdatedFamille.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restFamilleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFamille.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFamille))
            )
            .andExpect(status().isOk());

        // Validate the Famille in the database
        List<Famille> familleList = familleRepository.findAll();
        assertThat(familleList).hasSize(databaseSizeBeforeUpdate);
        Famille testFamille = familleList.get(familleList.size() - 1);
        assertThat(testFamille.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testFamille.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void patchNonExistingFamille() throws Exception {
        int databaseSizeBeforeUpdate = familleRepository.findAll().size();
        famille.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFamilleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, famille.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(famille))
            )
            .andExpect(status().isBadRequest());

        // Validate the Famille in the database
        List<Famille> familleList = familleRepository.findAll();
        assertThat(familleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFamille() throws Exception {
        int databaseSizeBeforeUpdate = familleRepository.findAll().size();
        famille.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(famille))
            )
            .andExpect(status().isBadRequest());

        // Validate the Famille in the database
        List<Famille> familleList = familleRepository.findAll();
        assertThat(familleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFamille() throws Exception {
        int databaseSizeBeforeUpdate = familleRepository.findAll().size();
        famille.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(famille)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Famille in the database
        List<Famille> familleList = familleRepository.findAll();
        assertThat(familleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFamille() throws Exception {
        // Initialize the database
        familleRepository.saveAndFlush(famille);

        int databaseSizeBeforeDelete = familleRepository.findAll().size();

        // Delete the famille
        restFamilleMockMvc
            .perform(delete(ENTITY_API_URL_ID, famille.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Famille> familleList = familleRepository.findAll();
        assertThat(familleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
