package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.Forme;
import fr.syncrase.ecosyst.domain.Forme;
import fr.syncrase.ecosyst.domain.SousForme;
import fr.syncrase.ecosyst.domain.SousVariete;
import fr.syncrase.ecosyst.repository.FormeRepository;
import fr.syncrase.ecosyst.service.criteria.FormeCriteria;
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
 * Integration tests for the {@link FormeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FormeResourceIT {

    private static final String DEFAULT_NOM_FR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_LATIN = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LATIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/formes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FormeRepository formeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFormeMockMvc;

    private Forme forme;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Forme createEntity(EntityManager em) {
        Forme forme = new Forme().nomFr(DEFAULT_NOM_FR).nomLatin(DEFAULT_NOM_LATIN);
        return forme;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Forme createUpdatedEntity(EntityManager em) {
        Forme forme = new Forme().nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);
        return forme;
    }

    @BeforeEach
    public void initTest() {
        forme = createEntity(em);
    }

    @Test
    @Transactional
    void createForme() throws Exception {
        int databaseSizeBeforeCreate = formeRepository.findAll().size();
        // Create the Forme
        restFormeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(forme)))
            .andExpect(status().isCreated());

        // Validate the Forme in the database
        List<Forme> formeList = formeRepository.findAll();
        assertThat(formeList).hasSize(databaseSizeBeforeCreate + 1);
        Forme testForme = formeList.get(formeList.size() - 1);
        assertThat(testForme.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testForme.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void createFormeWithExistingId() throws Exception {
        // Create the Forme with an existing ID
        forme.setId(1L);

        int databaseSizeBeforeCreate = formeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(forme)))
            .andExpect(status().isBadRequest());

        // Validate the Forme in the database
        List<Forme> formeList = formeRepository.findAll();
        assertThat(formeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomFrIsRequired() throws Exception {
        int databaseSizeBeforeTest = formeRepository.findAll().size();
        // set the field null
        forme.setNomFr(null);

        // Create the Forme, which fails.

        restFormeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(forme)))
            .andExpect(status().isBadRequest());

        List<Forme> formeList = formeRepository.findAll();
        assertThat(formeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFormes() throws Exception {
        // Initialize the database
        formeRepository.saveAndFlush(forme);

        // Get all the formeList
        restFormeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(forme.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));
    }

    @Test
    @Transactional
    void getForme() throws Exception {
        // Initialize the database
        formeRepository.saveAndFlush(forme);

        // Get the forme
        restFormeMockMvc
            .perform(get(ENTITY_API_URL_ID, forme.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(forme.getId().intValue()))
            .andExpect(jsonPath("$.nomFr").value(DEFAULT_NOM_FR))
            .andExpect(jsonPath("$.nomLatin").value(DEFAULT_NOM_LATIN));
    }

    @Test
    @Transactional
    void getFormesByIdFiltering() throws Exception {
        // Initialize the database
        formeRepository.saveAndFlush(forme);

        Long id = forme.getId();

        defaultFormeShouldBeFound("id.equals=" + id);
        defaultFormeShouldNotBeFound("id.notEquals=" + id);

        defaultFormeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFormeShouldNotBeFound("id.greaterThan=" + id);

        defaultFormeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFormeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFormesByNomFrIsEqualToSomething() throws Exception {
        // Initialize the database
        formeRepository.saveAndFlush(forme);

        // Get all the formeList where nomFr equals to DEFAULT_NOM_FR
        defaultFormeShouldBeFound("nomFr.equals=" + DEFAULT_NOM_FR);

        // Get all the formeList where nomFr equals to UPDATED_NOM_FR
        defaultFormeShouldNotBeFound("nomFr.equals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllFormesByNomFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        formeRepository.saveAndFlush(forme);

        // Get all the formeList where nomFr not equals to DEFAULT_NOM_FR
        defaultFormeShouldNotBeFound("nomFr.notEquals=" + DEFAULT_NOM_FR);

        // Get all the formeList where nomFr not equals to UPDATED_NOM_FR
        defaultFormeShouldBeFound("nomFr.notEquals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllFormesByNomFrIsInShouldWork() throws Exception {
        // Initialize the database
        formeRepository.saveAndFlush(forme);

        // Get all the formeList where nomFr in DEFAULT_NOM_FR or UPDATED_NOM_FR
        defaultFormeShouldBeFound("nomFr.in=" + DEFAULT_NOM_FR + "," + UPDATED_NOM_FR);

        // Get all the formeList where nomFr equals to UPDATED_NOM_FR
        defaultFormeShouldNotBeFound("nomFr.in=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllFormesByNomFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        formeRepository.saveAndFlush(forme);

        // Get all the formeList where nomFr is not null
        defaultFormeShouldBeFound("nomFr.specified=true");

        // Get all the formeList where nomFr is null
        defaultFormeShouldNotBeFound("nomFr.specified=false");
    }

    @Test
    @Transactional
    void getAllFormesByNomFrContainsSomething() throws Exception {
        // Initialize the database
        formeRepository.saveAndFlush(forme);

        // Get all the formeList where nomFr contains DEFAULT_NOM_FR
        defaultFormeShouldBeFound("nomFr.contains=" + DEFAULT_NOM_FR);

        // Get all the formeList where nomFr contains UPDATED_NOM_FR
        defaultFormeShouldNotBeFound("nomFr.contains=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllFormesByNomFrNotContainsSomething() throws Exception {
        // Initialize the database
        formeRepository.saveAndFlush(forme);

        // Get all the formeList where nomFr does not contain DEFAULT_NOM_FR
        defaultFormeShouldNotBeFound("nomFr.doesNotContain=" + DEFAULT_NOM_FR);

        // Get all the formeList where nomFr does not contain UPDATED_NOM_FR
        defaultFormeShouldBeFound("nomFr.doesNotContain=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllFormesByNomLatinIsEqualToSomething() throws Exception {
        // Initialize the database
        formeRepository.saveAndFlush(forme);

        // Get all the formeList where nomLatin equals to DEFAULT_NOM_LATIN
        defaultFormeShouldBeFound("nomLatin.equals=" + DEFAULT_NOM_LATIN);

        // Get all the formeList where nomLatin equals to UPDATED_NOM_LATIN
        defaultFormeShouldNotBeFound("nomLatin.equals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllFormesByNomLatinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        formeRepository.saveAndFlush(forme);

        // Get all the formeList where nomLatin not equals to DEFAULT_NOM_LATIN
        defaultFormeShouldNotBeFound("nomLatin.notEquals=" + DEFAULT_NOM_LATIN);

        // Get all the formeList where nomLatin not equals to UPDATED_NOM_LATIN
        defaultFormeShouldBeFound("nomLatin.notEquals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllFormesByNomLatinIsInShouldWork() throws Exception {
        // Initialize the database
        formeRepository.saveAndFlush(forme);

        // Get all the formeList where nomLatin in DEFAULT_NOM_LATIN or UPDATED_NOM_LATIN
        defaultFormeShouldBeFound("nomLatin.in=" + DEFAULT_NOM_LATIN + "," + UPDATED_NOM_LATIN);

        // Get all the formeList where nomLatin equals to UPDATED_NOM_LATIN
        defaultFormeShouldNotBeFound("nomLatin.in=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllFormesByNomLatinIsNullOrNotNull() throws Exception {
        // Initialize the database
        formeRepository.saveAndFlush(forme);

        // Get all the formeList where nomLatin is not null
        defaultFormeShouldBeFound("nomLatin.specified=true");

        // Get all the formeList where nomLatin is null
        defaultFormeShouldNotBeFound("nomLatin.specified=false");
    }

    @Test
    @Transactional
    void getAllFormesByNomLatinContainsSomething() throws Exception {
        // Initialize the database
        formeRepository.saveAndFlush(forme);

        // Get all the formeList where nomLatin contains DEFAULT_NOM_LATIN
        defaultFormeShouldBeFound("nomLatin.contains=" + DEFAULT_NOM_LATIN);

        // Get all the formeList where nomLatin contains UPDATED_NOM_LATIN
        defaultFormeShouldNotBeFound("nomLatin.contains=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllFormesByNomLatinNotContainsSomething() throws Exception {
        // Initialize the database
        formeRepository.saveAndFlush(forme);

        // Get all the formeList where nomLatin does not contain DEFAULT_NOM_LATIN
        defaultFormeShouldNotBeFound("nomLatin.doesNotContain=" + DEFAULT_NOM_LATIN);

        // Get all the formeList where nomLatin does not contain UPDATED_NOM_LATIN
        defaultFormeShouldBeFound("nomLatin.doesNotContain=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllFormesBySousFormesIsEqualToSomething() throws Exception {
        // Initialize the database
        formeRepository.saveAndFlush(forme);
        SousForme sousFormes;
        if (TestUtil.findAll(em, SousForme.class).isEmpty()) {
            sousFormes = SousFormeResourceIT.createEntity(em);
            em.persist(sousFormes);
            em.flush();
        } else {
            sousFormes = TestUtil.findAll(em, SousForme.class).get(0);
        }
        em.persist(sousFormes);
        em.flush();
        forme.addSousFormes(sousFormes);
        formeRepository.saveAndFlush(forme);
        Long sousFormesId = sousFormes.getId();

        // Get all the formeList where sousFormes equals to sousFormesId
        defaultFormeShouldBeFound("sousFormesId.equals=" + sousFormesId);

        // Get all the formeList where sousFormes equals to (sousFormesId + 1)
        defaultFormeShouldNotBeFound("sousFormesId.equals=" + (sousFormesId + 1));
    }

    @Test
    @Transactional
    void getAllFormesBySynonymesIsEqualToSomething() throws Exception {
        // Initialize the database
        formeRepository.saveAndFlush(forme);
        Forme synonymes;
        if (TestUtil.findAll(em, Forme.class).isEmpty()) {
            synonymes = FormeResourceIT.createEntity(em);
            em.persist(synonymes);
            em.flush();
        } else {
            synonymes = TestUtil.findAll(em, Forme.class).get(0);
        }
        em.persist(synonymes);
        em.flush();
        forme.addSynonymes(synonymes);
        formeRepository.saveAndFlush(forme);
        Long synonymesId = synonymes.getId();

        // Get all the formeList where synonymes equals to synonymesId
        defaultFormeShouldBeFound("synonymesId.equals=" + synonymesId);

        // Get all the formeList where synonymes equals to (synonymesId + 1)
        defaultFormeShouldNotBeFound("synonymesId.equals=" + (synonymesId + 1));
    }

    @Test
    @Transactional
    void getAllFormesBySousVarieteIsEqualToSomething() throws Exception {
        // Initialize the database
        formeRepository.saveAndFlush(forme);
        SousVariete sousVariete;
        if (TestUtil.findAll(em, SousVariete.class).isEmpty()) {
            sousVariete = SousVarieteResourceIT.createEntity(em);
            em.persist(sousVariete);
            em.flush();
        } else {
            sousVariete = TestUtil.findAll(em, SousVariete.class).get(0);
        }
        em.persist(sousVariete);
        em.flush();
        forme.setSousVariete(sousVariete);
        formeRepository.saveAndFlush(forme);
        Long sousVarieteId = sousVariete.getId();

        // Get all the formeList where sousVariete equals to sousVarieteId
        defaultFormeShouldBeFound("sousVarieteId.equals=" + sousVarieteId);

        // Get all the formeList where sousVariete equals to (sousVarieteId + 1)
        defaultFormeShouldNotBeFound("sousVarieteId.equals=" + (sousVarieteId + 1));
    }

    @Test
    @Transactional
    void getAllFormesByFormeIsEqualToSomething() throws Exception {
        // Initialize the database
        formeRepository.saveAndFlush(forme);
        Forme forme;
        if (TestUtil.findAll(em, Forme.class).isEmpty()) {
            forme = FormeResourceIT.createEntity(em);
            em.persist(forme);
            em.flush();
        } else {
            forme = TestUtil.findAll(em, Forme.class).get(0);
        }
        em.persist(forme);
        em.flush();
        forme.setForme(forme);
        formeRepository.saveAndFlush(forme);
        Long formeId = forme.getId();

        // Get all the formeList where forme equals to formeId
        defaultFormeShouldBeFound("formeId.equals=" + formeId);

        // Get all the formeList where forme equals to (formeId + 1)
        defaultFormeShouldNotBeFound("formeId.equals=" + (formeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFormeShouldBeFound(String filter) throws Exception {
        restFormeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(forme.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));

        // Check, that the count call also returns 1
        restFormeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFormeShouldNotBeFound(String filter) throws Exception {
        restFormeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFormeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingForme() throws Exception {
        // Get the forme
        restFormeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewForme() throws Exception {
        // Initialize the database
        formeRepository.saveAndFlush(forme);

        int databaseSizeBeforeUpdate = formeRepository.findAll().size();

        // Update the forme
        Forme updatedForme = formeRepository.findById(forme.getId()).get();
        // Disconnect from session so that the updates on updatedForme are not directly saved in db
        em.detach(updatedForme);
        updatedForme.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restFormeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedForme.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedForme))
            )
            .andExpect(status().isOk());

        // Validate the Forme in the database
        List<Forme> formeList = formeRepository.findAll();
        assertThat(formeList).hasSize(databaseSizeBeforeUpdate);
        Forme testForme = formeList.get(formeList.size() - 1);
        assertThat(testForme.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testForme.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void putNonExistingForme() throws Exception {
        int databaseSizeBeforeUpdate = formeRepository.findAll().size();
        forme.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, forme.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(forme))
            )
            .andExpect(status().isBadRequest());

        // Validate the Forme in the database
        List<Forme> formeList = formeRepository.findAll();
        assertThat(formeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchForme() throws Exception {
        int databaseSizeBeforeUpdate = formeRepository.findAll().size();
        forme.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(forme))
            )
            .andExpect(status().isBadRequest());

        // Validate the Forme in the database
        List<Forme> formeList = formeRepository.findAll();
        assertThat(formeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamForme() throws Exception {
        int databaseSizeBeforeUpdate = formeRepository.findAll().size();
        forme.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(forme)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Forme in the database
        List<Forme> formeList = formeRepository.findAll();
        assertThat(formeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFormeWithPatch() throws Exception {
        // Initialize the database
        formeRepository.saveAndFlush(forme);

        int databaseSizeBeforeUpdate = formeRepository.findAll().size();

        // Update the forme using partial update
        Forme partialUpdatedForme = new Forme();
        partialUpdatedForme.setId(forme.getId());

        partialUpdatedForme.nomFr(UPDATED_NOM_FR);

        restFormeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedForme.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedForme))
            )
            .andExpect(status().isOk());

        // Validate the Forme in the database
        List<Forme> formeList = formeRepository.findAll();
        assertThat(formeList).hasSize(databaseSizeBeforeUpdate);
        Forme testForme = formeList.get(formeList.size() - 1);
        assertThat(testForme.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testForme.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void fullUpdateFormeWithPatch() throws Exception {
        // Initialize the database
        formeRepository.saveAndFlush(forme);

        int databaseSizeBeforeUpdate = formeRepository.findAll().size();

        // Update the forme using partial update
        Forme partialUpdatedForme = new Forme();
        partialUpdatedForme.setId(forme.getId());

        partialUpdatedForme.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restFormeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedForme.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedForme))
            )
            .andExpect(status().isOk());

        // Validate the Forme in the database
        List<Forme> formeList = formeRepository.findAll();
        assertThat(formeList).hasSize(databaseSizeBeforeUpdate);
        Forme testForme = formeList.get(formeList.size() - 1);
        assertThat(testForme.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testForme.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void patchNonExistingForme() throws Exception {
        int databaseSizeBeforeUpdate = formeRepository.findAll().size();
        forme.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, forme.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(forme))
            )
            .andExpect(status().isBadRequest());

        // Validate the Forme in the database
        List<Forme> formeList = formeRepository.findAll();
        assertThat(formeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchForme() throws Exception {
        int databaseSizeBeforeUpdate = formeRepository.findAll().size();
        forme.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(forme))
            )
            .andExpect(status().isBadRequest());

        // Validate the Forme in the database
        List<Forme> formeList = formeRepository.findAll();
        assertThat(formeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamForme() throws Exception {
        int databaseSizeBeforeUpdate = formeRepository.findAll().size();
        forme.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(forme)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Forme in the database
        List<Forme> formeList = formeRepository.findAll();
        assertThat(formeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteForme() throws Exception {
        // Initialize the database
        formeRepository.saveAndFlush(forme);

        int databaseSizeBeforeDelete = formeRepository.findAll().size();

        // Delete the forme
        restFormeMockMvc
            .perform(delete(ENTITY_API_URL_ID, forme.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Forme> formeList = formeRepository.findAll();
        assertThat(formeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
