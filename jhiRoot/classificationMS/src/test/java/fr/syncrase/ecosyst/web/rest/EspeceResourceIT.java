package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.Espece;
import fr.syncrase.ecosyst.domain.Espece;
import fr.syncrase.ecosyst.domain.SousEspece;
import fr.syncrase.ecosyst.domain.SousSection;
import fr.syncrase.ecosyst.repository.EspeceRepository;
import fr.syncrase.ecosyst.service.criteria.EspeceCriteria;
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
 * Integration tests for the {@link EspeceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EspeceResourceIT {

    private static final String DEFAULT_NOM_FR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_LATIN = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LATIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/especes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EspeceRepository especeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEspeceMockMvc;

    private Espece espece;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Espece createEntity(EntityManager em) {
        Espece espece = new Espece().nomFr(DEFAULT_NOM_FR).nomLatin(DEFAULT_NOM_LATIN);
        return espece;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Espece createUpdatedEntity(EntityManager em) {
        Espece espece = new Espece().nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);
        return espece;
    }

    @BeforeEach
    public void initTest() {
        espece = createEntity(em);
    }

    @Test
    @Transactional
    void createEspece() throws Exception {
        int databaseSizeBeforeCreate = especeRepository.findAll().size();
        // Create the Espece
        restEspeceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(espece)))
            .andExpect(status().isCreated());

        // Validate the Espece in the database
        List<Espece> especeList = especeRepository.findAll();
        assertThat(especeList).hasSize(databaseSizeBeforeCreate + 1);
        Espece testEspece = especeList.get(especeList.size() - 1);
        assertThat(testEspece.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testEspece.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void createEspeceWithExistingId() throws Exception {
        // Create the Espece with an existing ID
        espece.setId(1L);

        int databaseSizeBeforeCreate = especeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEspeceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(espece)))
            .andExpect(status().isBadRequest());

        // Validate the Espece in the database
        List<Espece> especeList = especeRepository.findAll();
        assertThat(especeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomFrIsRequired() throws Exception {
        int databaseSizeBeforeTest = especeRepository.findAll().size();
        // set the field null
        espece.setNomFr(null);

        // Create the Espece, which fails.

        restEspeceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(espece)))
            .andExpect(status().isBadRequest());

        List<Espece> especeList = especeRepository.findAll();
        assertThat(especeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEspeces() throws Exception {
        // Initialize the database
        especeRepository.saveAndFlush(espece);

        // Get all the especeList
        restEspeceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(espece.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));
    }

    @Test
    @Transactional
    void getEspece() throws Exception {
        // Initialize the database
        especeRepository.saveAndFlush(espece);

        // Get the espece
        restEspeceMockMvc
            .perform(get(ENTITY_API_URL_ID, espece.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(espece.getId().intValue()))
            .andExpect(jsonPath("$.nomFr").value(DEFAULT_NOM_FR))
            .andExpect(jsonPath("$.nomLatin").value(DEFAULT_NOM_LATIN));
    }

    @Test
    @Transactional
    void getEspecesByIdFiltering() throws Exception {
        // Initialize the database
        especeRepository.saveAndFlush(espece);

        Long id = espece.getId();

        defaultEspeceShouldBeFound("id.equals=" + id);
        defaultEspeceShouldNotBeFound("id.notEquals=" + id);

        defaultEspeceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEspeceShouldNotBeFound("id.greaterThan=" + id);

        defaultEspeceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEspeceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEspecesByNomFrIsEqualToSomething() throws Exception {
        // Initialize the database
        especeRepository.saveAndFlush(espece);

        // Get all the especeList where nomFr equals to DEFAULT_NOM_FR
        defaultEspeceShouldBeFound("nomFr.equals=" + DEFAULT_NOM_FR);

        // Get all the especeList where nomFr equals to UPDATED_NOM_FR
        defaultEspeceShouldNotBeFound("nomFr.equals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllEspecesByNomFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        especeRepository.saveAndFlush(espece);

        // Get all the especeList where nomFr not equals to DEFAULT_NOM_FR
        defaultEspeceShouldNotBeFound("nomFr.notEquals=" + DEFAULT_NOM_FR);

        // Get all the especeList where nomFr not equals to UPDATED_NOM_FR
        defaultEspeceShouldBeFound("nomFr.notEquals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllEspecesByNomFrIsInShouldWork() throws Exception {
        // Initialize the database
        especeRepository.saveAndFlush(espece);

        // Get all the especeList where nomFr in DEFAULT_NOM_FR or UPDATED_NOM_FR
        defaultEspeceShouldBeFound("nomFr.in=" + DEFAULT_NOM_FR + "," + UPDATED_NOM_FR);

        // Get all the especeList where nomFr equals to UPDATED_NOM_FR
        defaultEspeceShouldNotBeFound("nomFr.in=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllEspecesByNomFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        especeRepository.saveAndFlush(espece);

        // Get all the especeList where nomFr is not null
        defaultEspeceShouldBeFound("nomFr.specified=true");

        // Get all the especeList where nomFr is null
        defaultEspeceShouldNotBeFound("nomFr.specified=false");
    }

    @Test
    @Transactional
    void getAllEspecesByNomFrContainsSomething() throws Exception {
        // Initialize the database
        especeRepository.saveAndFlush(espece);

        // Get all the especeList where nomFr contains DEFAULT_NOM_FR
        defaultEspeceShouldBeFound("nomFr.contains=" + DEFAULT_NOM_FR);

        // Get all the especeList where nomFr contains UPDATED_NOM_FR
        defaultEspeceShouldNotBeFound("nomFr.contains=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllEspecesByNomFrNotContainsSomething() throws Exception {
        // Initialize the database
        especeRepository.saveAndFlush(espece);

        // Get all the especeList where nomFr does not contain DEFAULT_NOM_FR
        defaultEspeceShouldNotBeFound("nomFr.doesNotContain=" + DEFAULT_NOM_FR);

        // Get all the especeList where nomFr does not contain UPDATED_NOM_FR
        defaultEspeceShouldBeFound("nomFr.doesNotContain=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllEspecesByNomLatinIsEqualToSomething() throws Exception {
        // Initialize the database
        especeRepository.saveAndFlush(espece);

        // Get all the especeList where nomLatin equals to DEFAULT_NOM_LATIN
        defaultEspeceShouldBeFound("nomLatin.equals=" + DEFAULT_NOM_LATIN);

        // Get all the especeList where nomLatin equals to UPDATED_NOM_LATIN
        defaultEspeceShouldNotBeFound("nomLatin.equals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllEspecesByNomLatinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        especeRepository.saveAndFlush(espece);

        // Get all the especeList where nomLatin not equals to DEFAULT_NOM_LATIN
        defaultEspeceShouldNotBeFound("nomLatin.notEquals=" + DEFAULT_NOM_LATIN);

        // Get all the especeList where nomLatin not equals to UPDATED_NOM_LATIN
        defaultEspeceShouldBeFound("nomLatin.notEquals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllEspecesByNomLatinIsInShouldWork() throws Exception {
        // Initialize the database
        especeRepository.saveAndFlush(espece);

        // Get all the especeList where nomLatin in DEFAULT_NOM_LATIN or UPDATED_NOM_LATIN
        defaultEspeceShouldBeFound("nomLatin.in=" + DEFAULT_NOM_LATIN + "," + UPDATED_NOM_LATIN);

        // Get all the especeList where nomLatin equals to UPDATED_NOM_LATIN
        defaultEspeceShouldNotBeFound("nomLatin.in=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllEspecesByNomLatinIsNullOrNotNull() throws Exception {
        // Initialize the database
        especeRepository.saveAndFlush(espece);

        // Get all the especeList where nomLatin is not null
        defaultEspeceShouldBeFound("nomLatin.specified=true");

        // Get all the especeList where nomLatin is null
        defaultEspeceShouldNotBeFound("nomLatin.specified=false");
    }

    @Test
    @Transactional
    void getAllEspecesByNomLatinContainsSomething() throws Exception {
        // Initialize the database
        especeRepository.saveAndFlush(espece);

        // Get all the especeList where nomLatin contains DEFAULT_NOM_LATIN
        defaultEspeceShouldBeFound("nomLatin.contains=" + DEFAULT_NOM_LATIN);

        // Get all the especeList where nomLatin contains UPDATED_NOM_LATIN
        defaultEspeceShouldNotBeFound("nomLatin.contains=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllEspecesByNomLatinNotContainsSomething() throws Exception {
        // Initialize the database
        especeRepository.saveAndFlush(espece);

        // Get all the especeList where nomLatin does not contain DEFAULT_NOM_LATIN
        defaultEspeceShouldNotBeFound("nomLatin.doesNotContain=" + DEFAULT_NOM_LATIN);

        // Get all the especeList where nomLatin does not contain UPDATED_NOM_LATIN
        defaultEspeceShouldBeFound("nomLatin.doesNotContain=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllEspecesBySousEspecesIsEqualToSomething() throws Exception {
        // Initialize the database
        especeRepository.saveAndFlush(espece);
        SousEspece sousEspeces;
        if (TestUtil.findAll(em, SousEspece.class).isEmpty()) {
            sousEspeces = SousEspeceResourceIT.createEntity(em);
            em.persist(sousEspeces);
            em.flush();
        } else {
            sousEspeces = TestUtil.findAll(em, SousEspece.class).get(0);
        }
        em.persist(sousEspeces);
        em.flush();
        espece.addSousEspeces(sousEspeces);
        especeRepository.saveAndFlush(espece);
        Long sousEspecesId = sousEspeces.getId();

        // Get all the especeList where sousEspeces equals to sousEspecesId
        defaultEspeceShouldBeFound("sousEspecesId.equals=" + sousEspecesId);

        // Get all the especeList where sousEspeces equals to (sousEspecesId + 1)
        defaultEspeceShouldNotBeFound("sousEspecesId.equals=" + (sousEspecesId + 1));
    }

    @Test
    @Transactional
    void getAllEspecesBySynonymesIsEqualToSomething() throws Exception {
        // Initialize the database
        especeRepository.saveAndFlush(espece);
        Espece synonymes;
        if (TestUtil.findAll(em, Espece.class).isEmpty()) {
            synonymes = EspeceResourceIT.createEntity(em);
            em.persist(synonymes);
            em.flush();
        } else {
            synonymes = TestUtil.findAll(em, Espece.class).get(0);
        }
        em.persist(synonymes);
        em.flush();
        espece.addSynonymes(synonymes);
        especeRepository.saveAndFlush(espece);
        Long synonymesId = synonymes.getId();

        // Get all the especeList where synonymes equals to synonymesId
        defaultEspeceShouldBeFound("synonymesId.equals=" + synonymesId);

        // Get all the especeList where synonymes equals to (synonymesId + 1)
        defaultEspeceShouldNotBeFound("synonymesId.equals=" + (synonymesId + 1));
    }

    @Test
    @Transactional
    void getAllEspecesBySousSectionIsEqualToSomething() throws Exception {
        // Initialize the database
        especeRepository.saveAndFlush(espece);
        SousSection sousSection;
        if (TestUtil.findAll(em, SousSection.class).isEmpty()) {
            sousSection = SousSectionResourceIT.createEntity(em);
            em.persist(sousSection);
            em.flush();
        } else {
            sousSection = TestUtil.findAll(em, SousSection.class).get(0);
        }
        em.persist(sousSection);
        em.flush();
        espece.setSousSection(sousSection);
        especeRepository.saveAndFlush(espece);
        Long sousSectionId = sousSection.getId();

        // Get all the especeList where sousSection equals to sousSectionId
        defaultEspeceShouldBeFound("sousSectionId.equals=" + sousSectionId);

        // Get all the especeList where sousSection equals to (sousSectionId + 1)
        defaultEspeceShouldNotBeFound("sousSectionId.equals=" + (sousSectionId + 1));
    }

    @Test
    @Transactional
    void getAllEspecesByEspeceIsEqualToSomething() throws Exception {
        // Initialize the database
        especeRepository.saveAndFlush(espece);
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
        espece.setEspece(espece);
        especeRepository.saveAndFlush(espece);
        Long especeId = espece.getId();

        // Get all the especeList where espece equals to especeId
        defaultEspeceShouldBeFound("especeId.equals=" + especeId);

        // Get all the especeList where espece equals to (especeId + 1)
        defaultEspeceShouldNotBeFound("especeId.equals=" + (especeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEspeceShouldBeFound(String filter) throws Exception {
        restEspeceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(espece.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));

        // Check, that the count call also returns 1
        restEspeceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEspeceShouldNotBeFound(String filter) throws Exception {
        restEspeceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEspeceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEspece() throws Exception {
        // Get the espece
        restEspeceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEspece() throws Exception {
        // Initialize the database
        especeRepository.saveAndFlush(espece);

        int databaseSizeBeforeUpdate = especeRepository.findAll().size();

        // Update the espece
        Espece updatedEspece = especeRepository.findById(espece.getId()).get();
        // Disconnect from session so that the updates on updatedEspece are not directly saved in db
        em.detach(updatedEspece);
        updatedEspece.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restEspeceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEspece.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEspece))
            )
            .andExpect(status().isOk());

        // Validate the Espece in the database
        List<Espece> especeList = especeRepository.findAll();
        assertThat(especeList).hasSize(databaseSizeBeforeUpdate);
        Espece testEspece = especeList.get(especeList.size() - 1);
        assertThat(testEspece.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testEspece.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void putNonExistingEspece() throws Exception {
        int databaseSizeBeforeUpdate = especeRepository.findAll().size();
        espece.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEspeceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, espece.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(espece))
            )
            .andExpect(status().isBadRequest());

        // Validate the Espece in the database
        List<Espece> especeList = especeRepository.findAll();
        assertThat(especeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEspece() throws Exception {
        int databaseSizeBeforeUpdate = especeRepository.findAll().size();
        espece.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEspeceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(espece))
            )
            .andExpect(status().isBadRequest());

        // Validate the Espece in the database
        List<Espece> especeList = especeRepository.findAll();
        assertThat(especeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEspece() throws Exception {
        int databaseSizeBeforeUpdate = especeRepository.findAll().size();
        espece.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEspeceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(espece)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Espece in the database
        List<Espece> especeList = especeRepository.findAll();
        assertThat(especeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEspeceWithPatch() throws Exception {
        // Initialize the database
        especeRepository.saveAndFlush(espece);

        int databaseSizeBeforeUpdate = especeRepository.findAll().size();

        // Update the espece using partial update
        Espece partialUpdatedEspece = new Espece();
        partialUpdatedEspece.setId(espece.getId());

        partialUpdatedEspece.nomFr(UPDATED_NOM_FR);

        restEspeceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEspece.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEspece))
            )
            .andExpect(status().isOk());

        // Validate the Espece in the database
        List<Espece> especeList = especeRepository.findAll();
        assertThat(especeList).hasSize(databaseSizeBeforeUpdate);
        Espece testEspece = especeList.get(especeList.size() - 1);
        assertThat(testEspece.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testEspece.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void fullUpdateEspeceWithPatch() throws Exception {
        // Initialize the database
        especeRepository.saveAndFlush(espece);

        int databaseSizeBeforeUpdate = especeRepository.findAll().size();

        // Update the espece using partial update
        Espece partialUpdatedEspece = new Espece();
        partialUpdatedEspece.setId(espece.getId());

        partialUpdatedEspece.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restEspeceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEspece.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEspece))
            )
            .andExpect(status().isOk());

        // Validate the Espece in the database
        List<Espece> especeList = especeRepository.findAll();
        assertThat(especeList).hasSize(databaseSizeBeforeUpdate);
        Espece testEspece = especeList.get(especeList.size() - 1);
        assertThat(testEspece.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testEspece.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void patchNonExistingEspece() throws Exception {
        int databaseSizeBeforeUpdate = especeRepository.findAll().size();
        espece.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEspeceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, espece.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(espece))
            )
            .andExpect(status().isBadRequest());

        // Validate the Espece in the database
        List<Espece> especeList = especeRepository.findAll();
        assertThat(especeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEspece() throws Exception {
        int databaseSizeBeforeUpdate = especeRepository.findAll().size();
        espece.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEspeceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(espece))
            )
            .andExpect(status().isBadRequest());

        // Validate the Espece in the database
        List<Espece> especeList = especeRepository.findAll();
        assertThat(especeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEspece() throws Exception {
        int databaseSizeBeforeUpdate = especeRepository.findAll().size();
        espece.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEspeceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(espece)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Espece in the database
        List<Espece> especeList = especeRepository.findAll();
        assertThat(especeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEspece() throws Exception {
        // Initialize the database
        especeRepository.saveAndFlush(espece);

        int databaseSizeBeforeDelete = especeRepository.findAll().size();

        // Delete the espece
        restEspeceMockMvc
            .perform(delete(ENTITY_API_URL_ID, espece.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Espece> especeList = especeRepository.findAll();
        assertThat(especeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
