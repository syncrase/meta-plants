package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.Regne;
import fr.syncrase.ecosyst.domain.Regne;
import fr.syncrase.ecosyst.domain.SousRegne;
import fr.syncrase.ecosyst.domain.SuperRegne;
import fr.syncrase.ecosyst.repository.RegneRepository;
import fr.syncrase.ecosyst.service.criteria.RegneCriteria;
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
 * Integration tests for the {@link RegneResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RegneResourceIT {

    private static final String DEFAULT_NOM_FR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_LATIN = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LATIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/regnes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RegneRepository regneRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRegneMockMvc;

    private Regne regne;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Regne createEntity(EntityManager em) {
        Regne regne = new Regne().nomFr(DEFAULT_NOM_FR).nomLatin(DEFAULT_NOM_LATIN);
        return regne;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Regne createUpdatedEntity(EntityManager em) {
        Regne regne = new Regne().nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);
        return regne;
    }

    @BeforeEach
    public void initTest() {
        regne = createEntity(em);
    }

    @Test
    @Transactional
    void createRegne() throws Exception {
        int databaseSizeBeforeCreate = regneRepository.findAll().size();
        // Create the Regne
        restRegneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(regne)))
            .andExpect(status().isCreated());

        // Validate the Regne in the database
        List<Regne> regneList = regneRepository.findAll();
        assertThat(regneList).hasSize(databaseSizeBeforeCreate + 1);
        Regne testRegne = regneList.get(regneList.size() - 1);
        assertThat(testRegne.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testRegne.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void createRegneWithExistingId() throws Exception {
        // Create the Regne with an existing ID
        regne.setId(1L);

        int databaseSizeBeforeCreate = regneRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRegneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(regne)))
            .andExpect(status().isBadRequest());

        // Validate the Regne in the database
        List<Regne> regneList = regneRepository.findAll();
        assertThat(regneList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomFrIsRequired() throws Exception {
        int databaseSizeBeforeTest = regneRepository.findAll().size();
        // set the field null
        regne.setNomFr(null);

        // Create the Regne, which fails.

        restRegneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(regne)))
            .andExpect(status().isBadRequest());

        List<Regne> regneList = regneRepository.findAll();
        assertThat(regneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRegnes() throws Exception {
        // Initialize the database
        regneRepository.saveAndFlush(regne);

        // Get all the regneList
        restRegneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(regne.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));
    }

    @Test
    @Transactional
    void getRegne() throws Exception {
        // Initialize the database
        regneRepository.saveAndFlush(regne);

        // Get the regne
        restRegneMockMvc
            .perform(get(ENTITY_API_URL_ID, regne.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(regne.getId().intValue()))
            .andExpect(jsonPath("$.nomFr").value(DEFAULT_NOM_FR))
            .andExpect(jsonPath("$.nomLatin").value(DEFAULT_NOM_LATIN));
    }

    @Test
    @Transactional
    void getRegnesByIdFiltering() throws Exception {
        // Initialize the database
        regneRepository.saveAndFlush(regne);

        Long id = regne.getId();

        defaultRegneShouldBeFound("id.equals=" + id);
        defaultRegneShouldNotBeFound("id.notEquals=" + id);

        defaultRegneShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRegneShouldNotBeFound("id.greaterThan=" + id);

        defaultRegneShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRegneShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRegnesByNomFrIsEqualToSomething() throws Exception {
        // Initialize the database
        regneRepository.saveAndFlush(regne);

        // Get all the regneList where nomFr equals to DEFAULT_NOM_FR
        defaultRegneShouldBeFound("nomFr.equals=" + DEFAULT_NOM_FR);

        // Get all the regneList where nomFr equals to UPDATED_NOM_FR
        defaultRegneShouldNotBeFound("nomFr.equals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllRegnesByNomFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        regneRepository.saveAndFlush(regne);

        // Get all the regneList where nomFr not equals to DEFAULT_NOM_FR
        defaultRegneShouldNotBeFound("nomFr.notEquals=" + DEFAULT_NOM_FR);

        // Get all the regneList where nomFr not equals to UPDATED_NOM_FR
        defaultRegneShouldBeFound("nomFr.notEquals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllRegnesByNomFrIsInShouldWork() throws Exception {
        // Initialize the database
        regneRepository.saveAndFlush(regne);

        // Get all the regneList where nomFr in DEFAULT_NOM_FR or UPDATED_NOM_FR
        defaultRegneShouldBeFound("nomFr.in=" + DEFAULT_NOM_FR + "," + UPDATED_NOM_FR);

        // Get all the regneList where nomFr equals to UPDATED_NOM_FR
        defaultRegneShouldNotBeFound("nomFr.in=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllRegnesByNomFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        regneRepository.saveAndFlush(regne);

        // Get all the regneList where nomFr is not null
        defaultRegneShouldBeFound("nomFr.specified=true");

        // Get all the regneList where nomFr is null
        defaultRegneShouldNotBeFound("nomFr.specified=false");
    }

    @Test
    @Transactional
    void getAllRegnesByNomFrContainsSomething() throws Exception {
        // Initialize the database
        regneRepository.saveAndFlush(regne);

        // Get all the regneList where nomFr contains DEFAULT_NOM_FR
        defaultRegneShouldBeFound("nomFr.contains=" + DEFAULT_NOM_FR);

        // Get all the regneList where nomFr contains UPDATED_NOM_FR
        defaultRegneShouldNotBeFound("nomFr.contains=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllRegnesByNomFrNotContainsSomething() throws Exception {
        // Initialize the database
        regneRepository.saveAndFlush(regne);

        // Get all the regneList where nomFr does not contain DEFAULT_NOM_FR
        defaultRegneShouldNotBeFound("nomFr.doesNotContain=" + DEFAULT_NOM_FR);

        // Get all the regneList where nomFr does not contain UPDATED_NOM_FR
        defaultRegneShouldBeFound("nomFr.doesNotContain=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllRegnesByNomLatinIsEqualToSomething() throws Exception {
        // Initialize the database
        regneRepository.saveAndFlush(regne);

        // Get all the regneList where nomLatin equals to DEFAULT_NOM_LATIN
        defaultRegneShouldBeFound("nomLatin.equals=" + DEFAULT_NOM_LATIN);

        // Get all the regneList where nomLatin equals to UPDATED_NOM_LATIN
        defaultRegneShouldNotBeFound("nomLatin.equals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllRegnesByNomLatinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        regneRepository.saveAndFlush(regne);

        // Get all the regneList where nomLatin not equals to DEFAULT_NOM_LATIN
        defaultRegneShouldNotBeFound("nomLatin.notEquals=" + DEFAULT_NOM_LATIN);

        // Get all the regneList where nomLatin not equals to UPDATED_NOM_LATIN
        defaultRegneShouldBeFound("nomLatin.notEquals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllRegnesByNomLatinIsInShouldWork() throws Exception {
        // Initialize the database
        regneRepository.saveAndFlush(regne);

        // Get all the regneList where nomLatin in DEFAULT_NOM_LATIN or UPDATED_NOM_LATIN
        defaultRegneShouldBeFound("nomLatin.in=" + DEFAULT_NOM_LATIN + "," + UPDATED_NOM_LATIN);

        // Get all the regneList where nomLatin equals to UPDATED_NOM_LATIN
        defaultRegneShouldNotBeFound("nomLatin.in=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllRegnesByNomLatinIsNullOrNotNull() throws Exception {
        // Initialize the database
        regneRepository.saveAndFlush(regne);

        // Get all the regneList where nomLatin is not null
        defaultRegneShouldBeFound("nomLatin.specified=true");

        // Get all the regneList where nomLatin is null
        defaultRegneShouldNotBeFound("nomLatin.specified=false");
    }

    @Test
    @Transactional
    void getAllRegnesByNomLatinContainsSomething() throws Exception {
        // Initialize the database
        regneRepository.saveAndFlush(regne);

        // Get all the regneList where nomLatin contains DEFAULT_NOM_LATIN
        defaultRegneShouldBeFound("nomLatin.contains=" + DEFAULT_NOM_LATIN);

        // Get all the regneList where nomLatin contains UPDATED_NOM_LATIN
        defaultRegneShouldNotBeFound("nomLatin.contains=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllRegnesByNomLatinNotContainsSomething() throws Exception {
        // Initialize the database
        regneRepository.saveAndFlush(regne);

        // Get all the regneList where nomLatin does not contain DEFAULT_NOM_LATIN
        defaultRegneShouldNotBeFound("nomLatin.doesNotContain=" + DEFAULT_NOM_LATIN);

        // Get all the regneList where nomLatin does not contain UPDATED_NOM_LATIN
        defaultRegneShouldBeFound("nomLatin.doesNotContain=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllRegnesBySousRegnesIsEqualToSomething() throws Exception {
        // Initialize the database
        regneRepository.saveAndFlush(regne);
        SousRegne sousRegnes;
        if (TestUtil.findAll(em, SousRegne.class).isEmpty()) {
            sousRegnes = SousRegneResourceIT.createEntity(em);
            em.persist(sousRegnes);
            em.flush();
        } else {
            sousRegnes = TestUtil.findAll(em, SousRegne.class).get(0);
        }
        em.persist(sousRegnes);
        em.flush();
        regne.addSousRegnes(sousRegnes);
        regneRepository.saveAndFlush(regne);
        Long sousRegnesId = sousRegnes.getId();

        // Get all the regneList where sousRegnes equals to sousRegnesId
        defaultRegneShouldBeFound("sousRegnesId.equals=" + sousRegnesId);

        // Get all the regneList where sousRegnes equals to (sousRegnesId + 1)
        defaultRegneShouldNotBeFound("sousRegnesId.equals=" + (sousRegnesId + 1));
    }

    @Test
    @Transactional
    void getAllRegnesBySynonymesIsEqualToSomething() throws Exception {
        // Initialize the database
        regneRepository.saveAndFlush(regne);
        Regne synonymes;
        if (TestUtil.findAll(em, Regne.class).isEmpty()) {
            synonymes = RegneResourceIT.createEntity(em);
            em.persist(synonymes);
            em.flush();
        } else {
            synonymes = TestUtil.findAll(em, Regne.class).get(0);
        }
        em.persist(synonymes);
        em.flush();
        regne.addSynonymes(synonymes);
        regneRepository.saveAndFlush(regne);
        Long synonymesId = synonymes.getId();

        // Get all the regneList where synonymes equals to synonymesId
        defaultRegneShouldBeFound("synonymesId.equals=" + synonymesId);

        // Get all the regneList where synonymes equals to (synonymesId + 1)
        defaultRegneShouldNotBeFound("synonymesId.equals=" + (synonymesId + 1));
    }

    @Test
    @Transactional
    void getAllRegnesBySuperRegneIsEqualToSomething() throws Exception {
        // Initialize the database
        regneRepository.saveAndFlush(regne);
        SuperRegne superRegne;
        if (TestUtil.findAll(em, SuperRegne.class).isEmpty()) {
            superRegne = SuperRegneResourceIT.createEntity(em);
            em.persist(superRegne);
            em.flush();
        } else {
            superRegne = TestUtil.findAll(em, SuperRegne.class).get(0);
        }
        em.persist(superRegne);
        em.flush();
        regne.setSuperRegne(superRegne);
        regneRepository.saveAndFlush(regne);
        Long superRegneId = superRegne.getId();

        // Get all the regneList where superRegne equals to superRegneId
        defaultRegneShouldBeFound("superRegneId.equals=" + superRegneId);

        // Get all the regneList where superRegne equals to (superRegneId + 1)
        defaultRegneShouldNotBeFound("superRegneId.equals=" + (superRegneId + 1));
    }

    @Test
    @Transactional
    void getAllRegnesByRegneIsEqualToSomething() throws Exception {
        // Initialize the database
        regneRepository.saveAndFlush(regne);
        Regne regne;
        if (TestUtil.findAll(em, Regne.class).isEmpty()) {
            regne = RegneResourceIT.createEntity(em);
            em.persist(regne);
            em.flush();
        } else {
            regne = TestUtil.findAll(em, Regne.class).get(0);
        }
        em.persist(regne);
        em.flush();
        regne.setRegne(regne);
        regneRepository.saveAndFlush(regne);
        Long regneId = regne.getId();

        // Get all the regneList where regne equals to regneId
        defaultRegneShouldBeFound("regneId.equals=" + regneId);

        // Get all the regneList where regne equals to (regneId + 1)
        defaultRegneShouldNotBeFound("regneId.equals=" + (regneId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRegneShouldBeFound(String filter) throws Exception {
        restRegneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(regne.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));

        // Check, that the count call also returns 1
        restRegneMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRegneShouldNotBeFound(String filter) throws Exception {
        restRegneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRegneMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRegne() throws Exception {
        // Get the regne
        restRegneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRegne() throws Exception {
        // Initialize the database
        regneRepository.saveAndFlush(regne);

        int databaseSizeBeforeUpdate = regneRepository.findAll().size();

        // Update the regne
        Regne updatedRegne = regneRepository.findById(regne.getId()).get();
        // Disconnect from session so that the updates on updatedRegne are not directly saved in db
        em.detach(updatedRegne);
        updatedRegne.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restRegneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRegne.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRegne))
            )
            .andExpect(status().isOk());

        // Validate the Regne in the database
        List<Regne> regneList = regneRepository.findAll();
        assertThat(regneList).hasSize(databaseSizeBeforeUpdate);
        Regne testRegne = regneList.get(regneList.size() - 1);
        assertThat(testRegne.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testRegne.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void putNonExistingRegne() throws Exception {
        int databaseSizeBeforeUpdate = regneRepository.findAll().size();
        regne.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRegneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, regne.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(regne))
            )
            .andExpect(status().isBadRequest());

        // Validate the Regne in the database
        List<Regne> regneList = regneRepository.findAll();
        assertThat(regneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRegne() throws Exception {
        int databaseSizeBeforeUpdate = regneRepository.findAll().size();
        regne.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(regne))
            )
            .andExpect(status().isBadRequest());

        // Validate the Regne in the database
        List<Regne> regneList = regneRepository.findAll();
        assertThat(regneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRegne() throws Exception {
        int databaseSizeBeforeUpdate = regneRepository.findAll().size();
        regne.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegneMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(regne)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Regne in the database
        List<Regne> regneList = regneRepository.findAll();
        assertThat(regneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRegneWithPatch() throws Exception {
        // Initialize the database
        regneRepository.saveAndFlush(regne);

        int databaseSizeBeforeUpdate = regneRepository.findAll().size();

        // Update the regne using partial update
        Regne partialUpdatedRegne = new Regne();
        partialUpdatedRegne.setId(regne.getId());

        partialUpdatedRegne.nomFr(UPDATED_NOM_FR);

        restRegneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRegne.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRegne))
            )
            .andExpect(status().isOk());

        // Validate the Regne in the database
        List<Regne> regneList = regneRepository.findAll();
        assertThat(regneList).hasSize(databaseSizeBeforeUpdate);
        Regne testRegne = regneList.get(regneList.size() - 1);
        assertThat(testRegne.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testRegne.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void fullUpdateRegneWithPatch() throws Exception {
        // Initialize the database
        regneRepository.saveAndFlush(regne);

        int databaseSizeBeforeUpdate = regneRepository.findAll().size();

        // Update the regne using partial update
        Regne partialUpdatedRegne = new Regne();
        partialUpdatedRegne.setId(regne.getId());

        partialUpdatedRegne.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restRegneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRegne.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRegne))
            )
            .andExpect(status().isOk());

        // Validate the Regne in the database
        List<Regne> regneList = regneRepository.findAll();
        assertThat(regneList).hasSize(databaseSizeBeforeUpdate);
        Regne testRegne = regneList.get(regneList.size() - 1);
        assertThat(testRegne.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testRegne.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void patchNonExistingRegne() throws Exception {
        int databaseSizeBeforeUpdate = regneRepository.findAll().size();
        regne.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRegneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, regne.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(regne))
            )
            .andExpect(status().isBadRequest());

        // Validate the Regne in the database
        List<Regne> regneList = regneRepository.findAll();
        assertThat(regneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRegne() throws Exception {
        int databaseSizeBeforeUpdate = regneRepository.findAll().size();
        regne.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(regne))
            )
            .andExpect(status().isBadRequest());

        // Validate the Regne in the database
        List<Regne> regneList = regneRepository.findAll();
        assertThat(regneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRegne() throws Exception {
        int databaseSizeBeforeUpdate = regneRepository.findAll().size();
        regne.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegneMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(regne)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Regne in the database
        List<Regne> regneList = regneRepository.findAll();
        assertThat(regneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRegne() throws Exception {
        // Initialize the database
        regneRepository.saveAndFlush(regne);

        int databaseSizeBeforeDelete = regneRepository.findAll().size();

        // Delete the regne
        restRegneMockMvc
            .perform(delete(ENTITY_API_URL_ID, regne.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Regne> regneList = regneRepository.findAll();
        assertThat(regneList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
