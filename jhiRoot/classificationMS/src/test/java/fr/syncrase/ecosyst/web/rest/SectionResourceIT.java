package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.Section;
import fr.syncrase.ecosyst.domain.Section;
import fr.syncrase.ecosyst.domain.SousGenre;
import fr.syncrase.ecosyst.domain.SousSection;
import fr.syncrase.ecosyst.repository.SectionRepository;
import fr.syncrase.ecosyst.service.criteria.SectionCriteria;
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
 * Integration tests for the {@link SectionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SectionResourceIT {

    private static final String DEFAULT_NOM_FR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_LATIN = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LATIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sections";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSectionMockMvc;

    private Section section;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Section createEntity(EntityManager em) {
        Section section = new Section().nomFr(DEFAULT_NOM_FR).nomLatin(DEFAULT_NOM_LATIN);
        return section;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Section createUpdatedEntity(EntityManager em) {
        Section section = new Section().nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);
        return section;
    }

    @BeforeEach
    public void initTest() {
        section = createEntity(em);
    }

    @Test
    @Transactional
    void createSection() throws Exception {
        int databaseSizeBeforeCreate = sectionRepository.findAll().size();
        // Create the Section
        restSectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(section)))
            .andExpect(status().isCreated());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeCreate + 1);
        Section testSection = sectionList.get(sectionList.size() - 1);
        assertThat(testSection.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testSection.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void createSectionWithExistingId() throws Exception {
        // Create the Section with an existing ID
        section.setId(1L);

        int databaseSizeBeforeCreate = sectionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(section)))
            .andExpect(status().isBadRequest());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomFrIsRequired() throws Exception {
        int databaseSizeBeforeTest = sectionRepository.findAll().size();
        // set the field null
        section.setNomFr(null);

        // Create the Section, which fails.

        restSectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(section)))
            .andExpect(status().isBadRequest());

        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSections() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList
        restSectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(section.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));
    }

    @Test
    @Transactional
    void getSection() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get the section
        restSectionMockMvc
            .perform(get(ENTITY_API_URL_ID, section.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(section.getId().intValue()))
            .andExpect(jsonPath("$.nomFr").value(DEFAULT_NOM_FR))
            .andExpect(jsonPath("$.nomLatin").value(DEFAULT_NOM_LATIN));
    }

    @Test
    @Transactional
    void getSectionsByIdFiltering() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        Long id = section.getId();

        defaultSectionShouldBeFound("id.equals=" + id);
        defaultSectionShouldNotBeFound("id.notEquals=" + id);

        defaultSectionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSectionShouldNotBeFound("id.greaterThan=" + id);

        defaultSectionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSectionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSectionsByNomFrIsEqualToSomething() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where nomFr equals to DEFAULT_NOM_FR
        defaultSectionShouldBeFound("nomFr.equals=" + DEFAULT_NOM_FR);

        // Get all the sectionList where nomFr equals to UPDATED_NOM_FR
        defaultSectionShouldNotBeFound("nomFr.equals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSectionsByNomFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where nomFr not equals to DEFAULT_NOM_FR
        defaultSectionShouldNotBeFound("nomFr.notEquals=" + DEFAULT_NOM_FR);

        // Get all the sectionList where nomFr not equals to UPDATED_NOM_FR
        defaultSectionShouldBeFound("nomFr.notEquals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSectionsByNomFrIsInShouldWork() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where nomFr in DEFAULT_NOM_FR or UPDATED_NOM_FR
        defaultSectionShouldBeFound("nomFr.in=" + DEFAULT_NOM_FR + "," + UPDATED_NOM_FR);

        // Get all the sectionList where nomFr equals to UPDATED_NOM_FR
        defaultSectionShouldNotBeFound("nomFr.in=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSectionsByNomFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where nomFr is not null
        defaultSectionShouldBeFound("nomFr.specified=true");

        // Get all the sectionList where nomFr is null
        defaultSectionShouldNotBeFound("nomFr.specified=false");
    }

    @Test
    @Transactional
    void getAllSectionsByNomFrContainsSomething() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where nomFr contains DEFAULT_NOM_FR
        defaultSectionShouldBeFound("nomFr.contains=" + DEFAULT_NOM_FR);

        // Get all the sectionList where nomFr contains UPDATED_NOM_FR
        defaultSectionShouldNotBeFound("nomFr.contains=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSectionsByNomFrNotContainsSomething() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where nomFr does not contain DEFAULT_NOM_FR
        defaultSectionShouldNotBeFound("nomFr.doesNotContain=" + DEFAULT_NOM_FR);

        // Get all the sectionList where nomFr does not contain UPDATED_NOM_FR
        defaultSectionShouldBeFound("nomFr.doesNotContain=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllSectionsByNomLatinIsEqualToSomething() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where nomLatin equals to DEFAULT_NOM_LATIN
        defaultSectionShouldBeFound("nomLatin.equals=" + DEFAULT_NOM_LATIN);

        // Get all the sectionList where nomLatin equals to UPDATED_NOM_LATIN
        defaultSectionShouldNotBeFound("nomLatin.equals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSectionsByNomLatinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where nomLatin not equals to DEFAULT_NOM_LATIN
        defaultSectionShouldNotBeFound("nomLatin.notEquals=" + DEFAULT_NOM_LATIN);

        // Get all the sectionList where nomLatin not equals to UPDATED_NOM_LATIN
        defaultSectionShouldBeFound("nomLatin.notEquals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSectionsByNomLatinIsInShouldWork() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where nomLatin in DEFAULT_NOM_LATIN or UPDATED_NOM_LATIN
        defaultSectionShouldBeFound("nomLatin.in=" + DEFAULT_NOM_LATIN + "," + UPDATED_NOM_LATIN);

        // Get all the sectionList where nomLatin equals to UPDATED_NOM_LATIN
        defaultSectionShouldNotBeFound("nomLatin.in=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSectionsByNomLatinIsNullOrNotNull() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where nomLatin is not null
        defaultSectionShouldBeFound("nomLatin.specified=true");

        // Get all the sectionList where nomLatin is null
        defaultSectionShouldNotBeFound("nomLatin.specified=false");
    }

    @Test
    @Transactional
    void getAllSectionsByNomLatinContainsSomething() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where nomLatin contains DEFAULT_NOM_LATIN
        defaultSectionShouldBeFound("nomLatin.contains=" + DEFAULT_NOM_LATIN);

        // Get all the sectionList where nomLatin contains UPDATED_NOM_LATIN
        defaultSectionShouldNotBeFound("nomLatin.contains=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSectionsByNomLatinNotContainsSomething() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sectionList where nomLatin does not contain DEFAULT_NOM_LATIN
        defaultSectionShouldNotBeFound("nomLatin.doesNotContain=" + DEFAULT_NOM_LATIN);

        // Get all the sectionList where nomLatin does not contain UPDATED_NOM_LATIN
        defaultSectionShouldBeFound("nomLatin.doesNotContain=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllSectionsBySousSectionsIsEqualToSomething() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);
        SousSection sousSections;
        if (TestUtil.findAll(em, SousSection.class).isEmpty()) {
            sousSections = SousSectionResourceIT.createEntity(em);
            em.persist(sousSections);
            em.flush();
        } else {
            sousSections = TestUtil.findAll(em, SousSection.class).get(0);
        }
        em.persist(sousSections);
        em.flush();
        section.addSousSections(sousSections);
        sectionRepository.saveAndFlush(section);
        Long sousSectionsId = sousSections.getId();

        // Get all the sectionList where sousSections equals to sousSectionsId
        defaultSectionShouldBeFound("sousSectionsId.equals=" + sousSectionsId);

        // Get all the sectionList where sousSections equals to (sousSectionsId + 1)
        defaultSectionShouldNotBeFound("sousSectionsId.equals=" + (sousSectionsId + 1));
    }

    @Test
    @Transactional
    void getAllSectionsBySynonymesIsEqualToSomething() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);
        Section synonymes;
        if (TestUtil.findAll(em, Section.class).isEmpty()) {
            synonymes = SectionResourceIT.createEntity(em);
            em.persist(synonymes);
            em.flush();
        } else {
            synonymes = TestUtil.findAll(em, Section.class).get(0);
        }
        em.persist(synonymes);
        em.flush();
        section.addSynonymes(synonymes);
        sectionRepository.saveAndFlush(section);
        Long synonymesId = synonymes.getId();

        // Get all the sectionList where synonymes equals to synonymesId
        defaultSectionShouldBeFound("synonymesId.equals=" + synonymesId);

        // Get all the sectionList where synonymes equals to (synonymesId + 1)
        defaultSectionShouldNotBeFound("synonymesId.equals=" + (synonymesId + 1));
    }

    @Test
    @Transactional
    void getAllSectionsBySousGenreIsEqualToSomething() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);
        SousGenre sousGenre;
        if (TestUtil.findAll(em, SousGenre.class).isEmpty()) {
            sousGenre = SousGenreResourceIT.createEntity(em);
            em.persist(sousGenre);
            em.flush();
        } else {
            sousGenre = TestUtil.findAll(em, SousGenre.class).get(0);
        }
        em.persist(sousGenre);
        em.flush();
        section.setSousGenre(sousGenre);
        sectionRepository.saveAndFlush(section);
        Long sousGenreId = sousGenre.getId();

        // Get all the sectionList where sousGenre equals to sousGenreId
        defaultSectionShouldBeFound("sousGenreId.equals=" + sousGenreId);

        // Get all the sectionList where sousGenre equals to (sousGenreId + 1)
        defaultSectionShouldNotBeFound("sousGenreId.equals=" + (sousGenreId + 1));
    }

    @Test
    @Transactional
    void getAllSectionsBySectionIsEqualToSomething() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);
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
        section.setSection(section);
        sectionRepository.saveAndFlush(section);
        Long sectionId = section.getId();

        // Get all the sectionList where section equals to sectionId
        defaultSectionShouldBeFound("sectionId.equals=" + sectionId);

        // Get all the sectionList where section equals to (sectionId + 1)
        defaultSectionShouldNotBeFound("sectionId.equals=" + (sectionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSectionShouldBeFound(String filter) throws Exception {
        restSectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(section.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));

        // Check, that the count call also returns 1
        restSectionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSectionShouldNotBeFound(String filter) throws Exception {
        restSectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSectionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSection() throws Exception {
        // Get the section
        restSectionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSection() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        int databaseSizeBeforeUpdate = sectionRepository.findAll().size();

        // Update the section
        Section updatedSection = sectionRepository.findById(section.getId()).get();
        // Disconnect from session so that the updates on updatedSection are not directly saved in db
        em.detach(updatedSection);
        updatedSection.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restSectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSection.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSection))
            )
            .andExpect(status().isOk());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
        Section testSection = sectionList.get(sectionList.size() - 1);
        assertThat(testSection.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testSection.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void putNonExistingSection() throws Exception {
        int databaseSizeBeforeUpdate = sectionRepository.findAll().size();
        section.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, section.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(section))
            )
            .andExpect(status().isBadRequest());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSection() throws Exception {
        int databaseSizeBeforeUpdate = sectionRepository.findAll().size();
        section.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(section))
            )
            .andExpect(status().isBadRequest());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSection() throws Exception {
        int databaseSizeBeforeUpdate = sectionRepository.findAll().size();
        section.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSectionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(section)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSectionWithPatch() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        int databaseSizeBeforeUpdate = sectionRepository.findAll().size();

        // Update the section using partial update
        Section partialUpdatedSection = new Section();
        partialUpdatedSection.setId(section.getId());

        partialUpdatedSection.nomLatin(UPDATED_NOM_LATIN);

        restSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSection.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSection))
            )
            .andExpect(status().isOk());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
        Section testSection = sectionList.get(sectionList.size() - 1);
        assertThat(testSection.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testSection.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void fullUpdateSectionWithPatch() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        int databaseSizeBeforeUpdate = sectionRepository.findAll().size();

        // Update the section using partial update
        Section partialUpdatedSection = new Section();
        partialUpdatedSection.setId(section.getId());

        partialUpdatedSection.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSection.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSection))
            )
            .andExpect(status().isOk());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
        Section testSection = sectionList.get(sectionList.size() - 1);
        assertThat(testSection.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testSection.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void patchNonExistingSection() throws Exception {
        int databaseSizeBeforeUpdate = sectionRepository.findAll().size();
        section.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, section.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(section))
            )
            .andExpect(status().isBadRequest());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSection() throws Exception {
        int databaseSizeBeforeUpdate = sectionRepository.findAll().size();
        section.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(section))
            )
            .andExpect(status().isBadRequest());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSection() throws Exception {
        int databaseSizeBeforeUpdate = sectionRepository.findAll().size();
        section.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSectionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(section)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Section in the database
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSection() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        int databaseSizeBeforeDelete = sectionRepository.findAll().size();

        // Delete the section
        restSectionMockMvc
            .perform(delete(ENTITY_API_URL_ID, section.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Section> sectionList = sectionRepository.findAll();
        assertThat(sectionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
