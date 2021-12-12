package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.Espece;
import fr.syncrase.ecosyst.domain.Section;
import fr.syncrase.ecosyst.domain.SousSection;
import fr.syncrase.ecosyst.domain.SousSection;
import fr.syncrase.ecosyst.repository.SousSectionRepository;
import fr.syncrase.ecosyst.service.criteria.SousSectionCriteria;
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
 * Integration tests for the {@link SousSectionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SousSectionResourceIT {

    private static final String DEFAULT_NOM_FR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_LATIN = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LATIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sous-sections";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SousSectionRepository sousSectionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSousSectionMockMvc;

    private SousSection sousSection;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SousSection createEntity(EntityManager em) {
        SousSection sousSection = new SousSection().nomFr(DEFAULT_NOM_FR).nomLatin(DEFAULT_NOM_LATIN);
        return sousSection;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SousSection createUpdatedEntity(EntityManager em) {
        SousSection sousSection = new SousSection().nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);
        return sousSection;
    }

    @BeforeEach
    public void initTest() {
        sousSection = createEntity(em);
    }

    @Test
    @Transactional
    void createSousSection() throws Exception {
        int databaseSizeBeforeCreate = sousSectionRepository.findAll().size();
        // Create the SousSection
        restSousSectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousSection)))
            .andExpect(status().isCreated());

        // Validate the SousSection in the database
        List<SousSection> sousSectionList = sousSectionRepository.findAll();
        assertThat(sousSectionList).hasSize(databaseSizeBeforeCreate + 1);
        SousSection testSousSection = sousSectionList.get(sousSectionList.size() - 1);
        assertThat(testSousSection.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testSousSection.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void createSousSectionWithExistingId() throws Exception {
        // Create the SousSection with an existing ID
        sousSection.setId(1L);

        int databaseSizeBeforeCreate = sousSectionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSousSectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousSection)))
            .andExpect(status().isBadRequest());

        // Validate the SousSection in the database
        List<SousSection> sousSectionList = sousSectionRepository.findAll();
        assertThat(sousSectionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomFrIsRequired() throws Exception {
        int databaseSizeBeforeTest = sousSectionRepository.findAll().size();
        // set the field null
        sousSection.setNomFr(null);

        // Create the SousSection, which fails.

        restSousSectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousSection)))
            .andExpect(status().isBadRequest());

        List<SousSection> sousSectionList = sousSectionRepository.findAll();
        assertThat(sousSectionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSousSections() throws Exception {
        // Initialize the database
        sousSectionRepository.saveAndFlush(sousSection);

        // Get all the sousSectionList
        restSousSectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sousSection.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));
    }

    @Test
    @Transactional
    void getSousSection() throws Exception {
        // Initialize the database
        sousSectionRepository.saveAndFlush(sousSection);

        // Get the sousSection
        restSousSectionMockMvc
            .perform(get(ENTITY_API_URL_ID, sousSection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sousSection.getId().intValue()))
            .andExpect(jsonPath("$.nomFr").value(DEFAULT_NOM_FR))
            .andExpect(jsonPath("$.nomLatin").value(DEFAULT_NOM_LATIN));
    }

    @Test
    @Transactional
    void getSousSectionsByIdFiltering() throws Exception {
        // Initialize the database
        sousSectionRepository.saveAndFlush(sousSection);

        Long id = sousSection.getId();

        defaultSousSectionShouldBeFound("id.equals=" + id);
        defaultSousSectionShouldNotBeFound("id.notEquals=" + id);

        defaultSousSectionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSousSectionShouldNotBeFound("id.greaterThan=" + id);

        defaultSousSectionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSousSectionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSousSectionsByNomFrIsEqualToSomething() throws Exception {
        // Initialize the database
        sousSectionRepository.saveAndFlush(sousSection);

        // Get all the sousSectionList where nomFr equals to DEFAULT_NOM_FR
        defaultSousSectionShouldBeFound("nomFr.equals=" + DEFAULT_NOM_FR);

        // Get all the sousSectionList where nomFr equals to UPDATED_NOM_FR
        defaultSousSectionShouldNotBeFound("nomFr.equals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousSectionsByNomFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sousSectionRepository.saveAndFlush(sousSection);

        // Get all the sousSectionList where nomFr not equals to DEFAULT_NOM_FR
        defaultSousSectionShouldNotBeFound("nomFr.notEquals=" + DEFAULT_NOM_FR);

        // Get all the sousSectionList where nomFr not equals to UPDATED_NOM_FR
        defaultSousSectionShouldBeFound("nomFr.notEquals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousSectionsByNomFrIsInShouldWork() throws Exception {
        // Initialize the database
        sousSectionRepository.saveAndFlush(sousSection);

        // Get all the sousSectionList where nomFr in DEFAULT_NOM_FR or UPDATED_NOM_FR
        defaultSousSectionShouldBeFound("nomFr.in=" + DEFAULT_NOM_FR + "," + UPDATED_NOM_FR);

        // Get all the sousSectionList where nomFr equals to UPDATED_NOM_FR
        defaultSousSectionShouldNotBeFound("nomFr.in=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousSectionsByNomFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        sousSectionRepository.saveAndFlush(sousSection);

        // Get all the sousSectionList where nomFr is not null
        defaultSousSectionShouldBeFound("nomFr.specified=true");

        // Get all the sousSectionList where nomFr is null
        defaultSousSectionShouldNotBeFound("nomFr.specified=false");
    }

    @Test
    @Transactional
    void getAllSousSectionsByNomFrContainsSomething() throws Exception {
        // Initialize the database
        sousSectionRepository.saveAndFlush(sousSection);

        // Get all the sousSectionList where nomFr contains DEFAULT_NOM_FR
        defaultSousSectionShouldBeFound("nomFr.contains=" + DEFAULT_NOM_FR);

        // Get all the sousSectionList where nomFr contains UPDATED_NOM_FR
        defaultSousSectionShouldNotBeFound("nomFr.contains=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousSectionsByNomFrNotContainsSomething() throws Exception {
        // Initialize the database
        sousSectionRepository.saveAndFlush(sousSection);

        // Get all the sousSectionList where nomFr does not contain DEFAULT_NOM_FR
        defaultSousSectionShouldNotBeFound("nomFr.doesNotContain=" + DEFAULT_NOM_FR);

        // Get all the sousSectionList where nomFr does not contain UPDATED_NOM_FR
        defaultSousSectionShouldBeFound("nomFr.doesNotContain=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSousSectionsByNomLatinIsEqualToSomething() throws Exception {
        // Initialize the database
        sousSectionRepository.saveAndFlush(sousSection);

        // Get all the sousSectionList where nomLatin equals to DEFAULT_NOM_LATIN
        defaultSousSectionShouldBeFound("nomLatin.equals=" + DEFAULT_NOM_LATIN);

        // Get all the sousSectionList where nomLatin equals to UPDATED_NOM_LATIN
        defaultSousSectionShouldNotBeFound("nomLatin.equals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousSectionsByNomLatinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sousSectionRepository.saveAndFlush(sousSection);

        // Get all the sousSectionList where nomLatin not equals to DEFAULT_NOM_LATIN
        defaultSousSectionShouldNotBeFound("nomLatin.notEquals=" + DEFAULT_NOM_LATIN);

        // Get all the sousSectionList where nomLatin not equals to UPDATED_NOM_LATIN
        defaultSousSectionShouldBeFound("nomLatin.notEquals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousSectionsByNomLatinIsInShouldWork() throws Exception {
        // Initialize the database
        sousSectionRepository.saveAndFlush(sousSection);

        // Get all the sousSectionList where nomLatin in DEFAULT_NOM_LATIN or UPDATED_NOM_LATIN
        defaultSousSectionShouldBeFound("nomLatin.in=" + DEFAULT_NOM_LATIN + "," + UPDATED_NOM_LATIN);

        // Get all the sousSectionList where nomLatin equals to UPDATED_NOM_LATIN
        defaultSousSectionShouldNotBeFound("nomLatin.in=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousSectionsByNomLatinIsNullOrNotNull() throws Exception {
        // Initialize the database
        sousSectionRepository.saveAndFlush(sousSection);

        // Get all the sousSectionList where nomLatin is not null
        defaultSousSectionShouldBeFound("nomLatin.specified=true");

        // Get all the sousSectionList where nomLatin is null
        defaultSousSectionShouldNotBeFound("nomLatin.specified=false");
    }

    @Test
    @Transactional
    void getAllSousSectionsByNomLatinContainsSomething() throws Exception {
        // Initialize the database
        sousSectionRepository.saveAndFlush(sousSection);

        // Get all the sousSectionList where nomLatin contains DEFAULT_NOM_LATIN
        defaultSousSectionShouldBeFound("nomLatin.contains=" + DEFAULT_NOM_LATIN);

        // Get all the sousSectionList where nomLatin contains UPDATED_NOM_LATIN
        defaultSousSectionShouldNotBeFound("nomLatin.contains=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousSectionsByNomLatinNotContainsSomething() throws Exception {
        // Initialize the database
        sousSectionRepository.saveAndFlush(sousSection);

        // Get all the sousSectionList where nomLatin does not contain DEFAULT_NOM_LATIN
        defaultSousSectionShouldNotBeFound("nomLatin.doesNotContain=" + DEFAULT_NOM_LATIN);

        // Get all the sousSectionList where nomLatin does not contain UPDATED_NOM_LATIN
        defaultSousSectionShouldBeFound("nomLatin.doesNotContain=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSousSectionsByEspecesIsEqualToSomething() throws Exception {
        // Initialize the database
        sousSectionRepository.saveAndFlush(sousSection);
        Espece especes;
        if (TestUtil.findAll(em, Espece.class).isEmpty()) {
            especes = EspeceResourceIT.createEntity(em);
            em.persist(especes);
            em.flush();
        } else {
            especes = TestUtil.findAll(em, Espece.class).get(0);
        }
        em.persist(especes);
        em.flush();
        sousSection.addEspeces(especes);
        sousSectionRepository.saveAndFlush(sousSection);
        Long especesId = especes.getId();

        // Get all the sousSectionList where especes equals to especesId
        defaultSousSectionShouldBeFound("especesId.equals=" + especesId);

        // Get all the sousSectionList where especes equals to (especesId + 1)
        defaultSousSectionShouldNotBeFound("especesId.equals=" + (especesId + 1));
    }

    @Test
    @Transactional
    void getAllSousSectionsBySynonymesIsEqualToSomething() throws Exception {
        // Initialize the database
        sousSectionRepository.saveAndFlush(sousSection);
        SousSection synonymes;
        if (TestUtil.findAll(em, SousSection.class).isEmpty()) {
            synonymes = SousSectionResourceIT.createEntity(em);
            em.persist(synonymes);
            em.flush();
        } else {
            synonymes = TestUtil.findAll(em, SousSection.class).get(0);
        }
        em.persist(synonymes);
        em.flush();
        sousSection.addSynonymes(synonymes);
        sousSectionRepository.saveAndFlush(sousSection);
        Long synonymesId = synonymes.getId();

        // Get all the sousSectionList where synonymes equals to synonymesId
        defaultSousSectionShouldBeFound("synonymesId.equals=" + synonymesId);

        // Get all the sousSectionList where synonymes equals to (synonymesId + 1)
        defaultSousSectionShouldNotBeFound("synonymesId.equals=" + (synonymesId + 1));
    }

    @Test
    @Transactional
    void getAllSousSectionsBySectionIsEqualToSomething() throws Exception {
        // Initialize the database
        sousSectionRepository.saveAndFlush(sousSection);
        Section section;
        if (TestUtil.findAll(em, Section.class).isEmpty()) {
            section = SectionResourceIT.createEntity(em);
            em.persist(section);
            em.flush();
        } else {
            section = TestUtil.findAll(em, Section.class).get(0);
        }
        em.persist(section);
        em.flush();
        sousSection.setSection(section);
        sousSectionRepository.saveAndFlush(sousSection);
        Long sectionId = section.getId();

        // Get all the sousSectionList where section equals to sectionId
        defaultSousSectionShouldBeFound("sectionId.equals=" + sectionId);

        // Get all the sousSectionList where section equals to (sectionId + 1)
        defaultSousSectionShouldNotBeFound("sectionId.equals=" + (sectionId + 1));
    }

    @Test
    @Transactional
    void getAllSousSectionsBySousSectionIsEqualToSomething() throws Exception {
        // Initialize the database
        sousSectionRepository.saveAndFlush(sousSection);
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
        sousSection.setSousSection(sousSection);
        sousSectionRepository.saveAndFlush(sousSection);
        Long sousSectionId = sousSection.getId();

        // Get all the sousSectionList where sousSection equals to sousSectionId
        defaultSousSectionShouldBeFound("sousSectionId.equals=" + sousSectionId);

        // Get all the sousSectionList where sousSection equals to (sousSectionId + 1)
        defaultSousSectionShouldNotBeFound("sousSectionId.equals=" + (sousSectionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSousSectionShouldBeFound(String filter) throws Exception {
        restSousSectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sousSection.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));

        // Check, that the count call also returns 1
        restSousSectionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSousSectionShouldNotBeFound(String filter) throws Exception {
        restSousSectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSousSectionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSousSection() throws Exception {
        // Get the sousSection
        restSousSectionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSousSection() throws Exception {
        // Initialize the database
        sousSectionRepository.saveAndFlush(sousSection);

        int databaseSizeBeforeUpdate = sousSectionRepository.findAll().size();

        // Update the sousSection
        SousSection updatedSousSection = sousSectionRepository.findById(sousSection.getId()).get();
        // Disconnect from session so that the updates on updatedSousSection are not directly saved in db
        em.detach(updatedSousSection);
        updatedSousSection.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restSousSectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSousSection.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSousSection))
            )
            .andExpect(status().isOk());

        // Validate the SousSection in the database
        List<SousSection> sousSectionList = sousSectionRepository.findAll();
        assertThat(sousSectionList).hasSize(databaseSizeBeforeUpdate);
        SousSection testSousSection = sousSectionList.get(sousSectionList.size() - 1);
        assertThat(testSousSection.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testSousSection.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void putNonExistingSousSection() throws Exception {
        int databaseSizeBeforeUpdate = sousSectionRepository.findAll().size();
        sousSection.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSousSectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sousSection.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sousSection))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousSection in the database
        List<SousSection> sousSectionList = sousSectionRepository.findAll();
        assertThat(sousSectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSousSection() throws Exception {
        int databaseSizeBeforeUpdate = sousSectionRepository.findAll().size();
        sousSection.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousSectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sousSection))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousSection in the database
        List<SousSection> sousSectionList = sousSectionRepository.findAll();
        assertThat(sousSectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSousSection() throws Exception {
        int databaseSizeBeforeUpdate = sousSectionRepository.findAll().size();
        sousSection.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousSectionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sousSection)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SousSection in the database
        List<SousSection> sousSectionList = sousSectionRepository.findAll();
        assertThat(sousSectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSousSectionWithPatch() throws Exception {
        // Initialize the database
        sousSectionRepository.saveAndFlush(sousSection);

        int databaseSizeBeforeUpdate = sousSectionRepository.findAll().size();

        // Update the sousSection using partial update
        SousSection partialUpdatedSousSection = new SousSection();
        partialUpdatedSousSection.setId(sousSection.getId());

        partialUpdatedSousSection.nomLatin(UPDATED_NOM_LATIN);

        restSousSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSousSection.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSousSection))
            )
            .andExpect(status().isOk());

        // Validate the SousSection in the database
        List<SousSection> sousSectionList = sousSectionRepository.findAll();
        assertThat(sousSectionList).hasSize(databaseSizeBeforeUpdate);
        SousSection testSousSection = sousSectionList.get(sousSectionList.size() - 1);
        assertThat(testSousSection.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testSousSection.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void fullUpdateSousSectionWithPatch() throws Exception {
        // Initialize the database
        sousSectionRepository.saveAndFlush(sousSection);

        int databaseSizeBeforeUpdate = sousSectionRepository.findAll().size();

        // Update the sousSection using partial update
        SousSection partialUpdatedSousSection = new SousSection();
        partialUpdatedSousSection.setId(sousSection.getId());

        partialUpdatedSousSection.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restSousSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSousSection.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSousSection))
            )
            .andExpect(status().isOk());

        // Validate the SousSection in the database
        List<SousSection> sousSectionList = sousSectionRepository.findAll();
        assertThat(sousSectionList).hasSize(databaseSizeBeforeUpdate);
        SousSection testSousSection = sousSectionList.get(sousSectionList.size() - 1);
        assertThat(testSousSection.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testSousSection.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void patchNonExistingSousSection() throws Exception {
        int databaseSizeBeforeUpdate = sousSectionRepository.findAll().size();
        sousSection.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSousSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sousSection.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sousSection))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousSection in the database
        List<SousSection> sousSectionList = sousSectionRepository.findAll();
        assertThat(sousSectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSousSection() throws Exception {
        int databaseSizeBeforeUpdate = sousSectionRepository.findAll().size();
        sousSection.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sousSection))
            )
            .andExpect(status().isBadRequest());

        // Validate the SousSection in the database
        List<SousSection> sousSectionList = sousSectionRepository.findAll();
        assertThat(sousSectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSousSection() throws Exception {
        int databaseSizeBeforeUpdate = sousSectionRepository.findAll().size();
        sousSection.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSousSectionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sousSection))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SousSection in the database
        List<SousSection> sousSectionList = sousSectionRepository.findAll();
        assertThat(sousSectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSousSection() throws Exception {
        // Initialize the database
        sousSectionRepository.saveAndFlush(sousSection);

        int databaseSizeBeforeDelete = sousSectionRepository.findAll().size();

        // Delete the sousSection
        restSousSectionMockMvc
            .perform(delete(ENTITY_API_URL_ID, sousSection.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SousSection> sousSectionList = sousSectionRepository.findAll();
        assertThat(sousSectionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
