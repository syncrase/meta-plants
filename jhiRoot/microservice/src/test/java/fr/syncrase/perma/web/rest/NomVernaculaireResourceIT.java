package fr.syncrase.perma.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.perma.IntegrationTest;
import fr.syncrase.perma.domain.NomVernaculaire;
import fr.syncrase.perma.domain.Plante;
import fr.syncrase.perma.repository.NomVernaculaireRepository;
import fr.syncrase.perma.service.criteria.NomVernaculaireCriteria;
import fr.syncrase.perma.service.dto.NomVernaculaireDTO;
import fr.syncrase.perma.service.mapper.NomVernaculaireMapper;
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
 * Integration tests for the {@link NomVernaculaireResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NomVernaculaireResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/nom-vernaculaires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NomVernaculaireRepository nomVernaculaireRepository;

    @Autowired
    private NomVernaculaireMapper nomVernaculaireMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNomVernaculaireMockMvc;

    private NomVernaculaire nomVernaculaire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NomVernaculaire createEntity(EntityManager em) {
        NomVernaculaire nomVernaculaire = new NomVernaculaire().nom(DEFAULT_NOM).description(DEFAULT_DESCRIPTION);
        return nomVernaculaire;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NomVernaculaire createUpdatedEntity(EntityManager em) {
        NomVernaculaire nomVernaculaire = new NomVernaculaire().nom(UPDATED_NOM).description(UPDATED_DESCRIPTION);
        return nomVernaculaire;
    }

    @BeforeEach
    public void initTest() {
        nomVernaculaire = createEntity(em);
    }

    @Test
    @Transactional
    void createNomVernaculaire() throws Exception {
        int databaseSizeBeforeCreate = nomVernaculaireRepository.findAll().size();
        // Create the NomVernaculaire
        NomVernaculaireDTO nomVernaculaireDTO = nomVernaculaireMapper.toDto(nomVernaculaire);
        restNomVernaculaireMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nomVernaculaireDTO))
            )
            .andExpect(status().isCreated());

        // Validate the NomVernaculaire in the database
        List<NomVernaculaire> nomVernaculaireList = nomVernaculaireRepository.findAll();
        assertThat(nomVernaculaireList).hasSize(databaseSizeBeforeCreate + 1);
        NomVernaculaire testNomVernaculaire = nomVernaculaireList.get(nomVernaculaireList.size() - 1);
        assertThat(testNomVernaculaire.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testNomVernaculaire.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createNomVernaculaireWithExistingId() throws Exception {
        // Create the NomVernaculaire with an existing ID
        nomVernaculaire.setId(1L);
        NomVernaculaireDTO nomVernaculaireDTO = nomVernaculaireMapper.toDto(nomVernaculaire);

        int databaseSizeBeforeCreate = nomVernaculaireRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNomVernaculaireMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nomVernaculaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NomVernaculaire in the database
        List<NomVernaculaire> nomVernaculaireList = nomVernaculaireRepository.findAll();
        assertThat(nomVernaculaireList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = nomVernaculaireRepository.findAll().size();
        // set the field null
        nomVernaculaire.setNom(null);

        // Create the NomVernaculaire, which fails.
        NomVernaculaireDTO nomVernaculaireDTO = nomVernaculaireMapper.toDto(nomVernaculaire);

        restNomVernaculaireMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nomVernaculaireDTO))
            )
            .andExpect(status().isBadRequest());

        List<NomVernaculaire> nomVernaculaireList = nomVernaculaireRepository.findAll();
        assertThat(nomVernaculaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNomVernaculaires() throws Exception {
        // Initialize the database
        nomVernaculaireRepository.saveAndFlush(nomVernaculaire);

        // Get all the nomVernaculaireList
        restNomVernaculaireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nomVernaculaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getNomVernaculaire() throws Exception {
        // Initialize the database
        nomVernaculaireRepository.saveAndFlush(nomVernaculaire);

        // Get the nomVernaculaire
        restNomVernaculaireMockMvc
            .perform(get(ENTITY_API_URL_ID, nomVernaculaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nomVernaculaire.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNomVernaculairesByIdFiltering() throws Exception {
        // Initialize the database
        nomVernaculaireRepository.saveAndFlush(nomVernaculaire);

        Long id = nomVernaculaire.getId();

        defaultNomVernaculaireShouldBeFound("id.equals=" + id);
        defaultNomVernaculaireShouldNotBeFound("id.notEquals=" + id);

        defaultNomVernaculaireShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultNomVernaculaireShouldNotBeFound("id.greaterThan=" + id);

        defaultNomVernaculaireShouldBeFound("id.lessThanOrEqual=" + id);
        defaultNomVernaculaireShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNomVernaculairesByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        nomVernaculaireRepository.saveAndFlush(nomVernaculaire);

        // Get all the nomVernaculaireList where nom equals to DEFAULT_NOM
        defaultNomVernaculaireShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the nomVernaculaireList where nom equals to UPDATED_NOM
        defaultNomVernaculaireShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllNomVernaculairesByNomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        nomVernaculaireRepository.saveAndFlush(nomVernaculaire);

        // Get all the nomVernaculaireList where nom not equals to DEFAULT_NOM
        defaultNomVernaculaireShouldNotBeFound("nom.notEquals=" + DEFAULT_NOM);

        // Get all the nomVernaculaireList where nom not equals to UPDATED_NOM
        defaultNomVernaculaireShouldBeFound("nom.notEquals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllNomVernaculairesByNomIsInShouldWork() throws Exception {
        // Initialize the database
        nomVernaculaireRepository.saveAndFlush(nomVernaculaire);

        // Get all the nomVernaculaireList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultNomVernaculaireShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the nomVernaculaireList where nom equals to UPDATED_NOM
        defaultNomVernaculaireShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllNomVernaculairesByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        nomVernaculaireRepository.saveAndFlush(nomVernaculaire);

        // Get all the nomVernaculaireList where nom is not null
        defaultNomVernaculaireShouldBeFound("nom.specified=true");

        // Get all the nomVernaculaireList where nom is null
        defaultNomVernaculaireShouldNotBeFound("nom.specified=false");
    }

    @Test
    @Transactional
    void getAllNomVernaculairesByNomContainsSomething() throws Exception {
        // Initialize the database
        nomVernaculaireRepository.saveAndFlush(nomVernaculaire);

        // Get all the nomVernaculaireList where nom contains DEFAULT_NOM
        defaultNomVernaculaireShouldBeFound("nom.contains=" + DEFAULT_NOM);

        // Get all the nomVernaculaireList where nom contains UPDATED_NOM
        defaultNomVernaculaireShouldNotBeFound("nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllNomVernaculairesByNomNotContainsSomething() throws Exception {
        // Initialize the database
        nomVernaculaireRepository.saveAndFlush(nomVernaculaire);

        // Get all the nomVernaculaireList where nom does not contain DEFAULT_NOM
        defaultNomVernaculaireShouldNotBeFound("nom.doesNotContain=" + DEFAULT_NOM);

        // Get all the nomVernaculaireList where nom does not contain UPDATED_NOM
        defaultNomVernaculaireShouldBeFound("nom.doesNotContain=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllNomVernaculairesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        nomVernaculaireRepository.saveAndFlush(nomVernaculaire);

        // Get all the nomVernaculaireList where description equals to DEFAULT_DESCRIPTION
        defaultNomVernaculaireShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the nomVernaculaireList where description equals to UPDATED_DESCRIPTION
        defaultNomVernaculaireShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllNomVernaculairesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        nomVernaculaireRepository.saveAndFlush(nomVernaculaire);

        // Get all the nomVernaculaireList where description not equals to DEFAULT_DESCRIPTION
        defaultNomVernaculaireShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the nomVernaculaireList where description not equals to UPDATED_DESCRIPTION
        defaultNomVernaculaireShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllNomVernaculairesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        nomVernaculaireRepository.saveAndFlush(nomVernaculaire);

        // Get all the nomVernaculaireList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultNomVernaculaireShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the nomVernaculaireList where description equals to UPDATED_DESCRIPTION
        defaultNomVernaculaireShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllNomVernaculairesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        nomVernaculaireRepository.saveAndFlush(nomVernaculaire);

        // Get all the nomVernaculaireList where description is not null
        defaultNomVernaculaireShouldBeFound("description.specified=true");

        // Get all the nomVernaculaireList where description is null
        defaultNomVernaculaireShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllNomVernaculairesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        nomVernaculaireRepository.saveAndFlush(nomVernaculaire);

        // Get all the nomVernaculaireList where description contains DEFAULT_DESCRIPTION
        defaultNomVernaculaireShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the nomVernaculaireList where description contains UPDATED_DESCRIPTION
        defaultNomVernaculaireShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllNomVernaculairesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        nomVernaculaireRepository.saveAndFlush(nomVernaculaire);

        // Get all the nomVernaculaireList where description does not contain DEFAULT_DESCRIPTION
        defaultNomVernaculaireShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the nomVernaculaireList where description does not contain UPDATED_DESCRIPTION
        defaultNomVernaculaireShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllNomVernaculairesByPlantesIsEqualToSomething() throws Exception {
        // Initialize the database
        nomVernaculaireRepository.saveAndFlush(nomVernaculaire);
        Plante plantes;
        if (TestUtil.findAll(em, Plante.class).isEmpty()) {
            plantes = PlanteResourceIT.createEntity(em);
            em.persist(plantes);
            em.flush();
        } else {
            plantes = TestUtil.findAll(em, Plante.class).get(0);
        }
        em.persist(plantes);
        em.flush();
        nomVernaculaire.addPlantes(plantes);
        nomVernaculaireRepository.saveAndFlush(nomVernaculaire);
        Long plantesId = plantes.getId();

        // Get all the nomVernaculaireList where plantes equals to plantesId
        defaultNomVernaculaireShouldBeFound("plantesId.equals=" + plantesId);

        // Get all the nomVernaculaireList where plantes equals to (plantesId + 1)
        defaultNomVernaculaireShouldNotBeFound("plantesId.equals=" + (plantesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNomVernaculaireShouldBeFound(String filter) throws Exception {
        restNomVernaculaireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nomVernaculaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restNomVernaculaireMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNomVernaculaireShouldNotBeFound(String filter) throws Exception {
        restNomVernaculaireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNomVernaculaireMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNomVernaculaire() throws Exception {
        // Get the nomVernaculaire
        restNomVernaculaireMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewNomVernaculaire() throws Exception {
        // Initialize the database
        nomVernaculaireRepository.saveAndFlush(nomVernaculaire);

        int databaseSizeBeforeUpdate = nomVernaculaireRepository.findAll().size();

        // Update the nomVernaculaire
        NomVernaculaire updatedNomVernaculaire = nomVernaculaireRepository.findById(nomVernaculaire.getId()).get();
        // Disconnect from session so that the updates on updatedNomVernaculaire are not directly saved in db
        em.detach(updatedNomVernaculaire);
        updatedNomVernaculaire.nom(UPDATED_NOM).description(UPDATED_DESCRIPTION);
        NomVernaculaireDTO nomVernaculaireDTO = nomVernaculaireMapper.toDto(updatedNomVernaculaire);

        restNomVernaculaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nomVernaculaireDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nomVernaculaireDTO))
            )
            .andExpect(status().isOk());

        // Validate the NomVernaculaire in the database
        List<NomVernaculaire> nomVernaculaireList = nomVernaculaireRepository.findAll();
        assertThat(nomVernaculaireList).hasSize(databaseSizeBeforeUpdate);
        NomVernaculaire testNomVernaculaire = nomVernaculaireList.get(nomVernaculaireList.size() - 1);
        assertThat(testNomVernaculaire.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testNomVernaculaire.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingNomVernaculaire() throws Exception {
        int databaseSizeBeforeUpdate = nomVernaculaireRepository.findAll().size();
        nomVernaculaire.setId(count.incrementAndGet());

        // Create the NomVernaculaire
        NomVernaculaireDTO nomVernaculaireDTO = nomVernaculaireMapper.toDto(nomVernaculaire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNomVernaculaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nomVernaculaireDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nomVernaculaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NomVernaculaire in the database
        List<NomVernaculaire> nomVernaculaireList = nomVernaculaireRepository.findAll();
        assertThat(nomVernaculaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNomVernaculaire() throws Exception {
        int databaseSizeBeforeUpdate = nomVernaculaireRepository.findAll().size();
        nomVernaculaire.setId(count.incrementAndGet());

        // Create the NomVernaculaire
        NomVernaculaireDTO nomVernaculaireDTO = nomVernaculaireMapper.toDto(nomVernaculaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNomVernaculaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nomVernaculaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NomVernaculaire in the database
        List<NomVernaculaire> nomVernaculaireList = nomVernaculaireRepository.findAll();
        assertThat(nomVernaculaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNomVernaculaire() throws Exception {
        int databaseSizeBeforeUpdate = nomVernaculaireRepository.findAll().size();
        nomVernaculaire.setId(count.incrementAndGet());

        // Create the NomVernaculaire
        NomVernaculaireDTO nomVernaculaireDTO = nomVernaculaireMapper.toDto(nomVernaculaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNomVernaculaireMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nomVernaculaireDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NomVernaculaire in the database
        List<NomVernaculaire> nomVernaculaireList = nomVernaculaireRepository.findAll();
        assertThat(nomVernaculaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNomVernaculaireWithPatch() throws Exception {
        // Initialize the database
        nomVernaculaireRepository.saveAndFlush(nomVernaculaire);

        int databaseSizeBeforeUpdate = nomVernaculaireRepository.findAll().size();

        // Update the nomVernaculaire using partial update
        NomVernaculaire partialUpdatedNomVernaculaire = new NomVernaculaire();
        partialUpdatedNomVernaculaire.setId(nomVernaculaire.getId());

        partialUpdatedNomVernaculaire.nom(UPDATED_NOM);

        restNomVernaculaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNomVernaculaire.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNomVernaculaire))
            )
            .andExpect(status().isOk());

        // Validate the NomVernaculaire in the database
        List<NomVernaculaire> nomVernaculaireList = nomVernaculaireRepository.findAll();
        assertThat(nomVernaculaireList).hasSize(databaseSizeBeforeUpdate);
        NomVernaculaire testNomVernaculaire = nomVernaculaireList.get(nomVernaculaireList.size() - 1);
        assertThat(testNomVernaculaire.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testNomVernaculaire.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateNomVernaculaireWithPatch() throws Exception {
        // Initialize the database
        nomVernaculaireRepository.saveAndFlush(nomVernaculaire);

        int databaseSizeBeforeUpdate = nomVernaculaireRepository.findAll().size();

        // Update the nomVernaculaire using partial update
        NomVernaculaire partialUpdatedNomVernaculaire = new NomVernaculaire();
        partialUpdatedNomVernaculaire.setId(nomVernaculaire.getId());

        partialUpdatedNomVernaculaire.nom(UPDATED_NOM).description(UPDATED_DESCRIPTION);

        restNomVernaculaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNomVernaculaire.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNomVernaculaire))
            )
            .andExpect(status().isOk());

        // Validate the NomVernaculaire in the database
        List<NomVernaculaire> nomVernaculaireList = nomVernaculaireRepository.findAll();
        assertThat(nomVernaculaireList).hasSize(databaseSizeBeforeUpdate);
        NomVernaculaire testNomVernaculaire = nomVernaculaireList.get(nomVernaculaireList.size() - 1);
        assertThat(testNomVernaculaire.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testNomVernaculaire.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingNomVernaculaire() throws Exception {
        int databaseSizeBeforeUpdate = nomVernaculaireRepository.findAll().size();
        nomVernaculaire.setId(count.incrementAndGet());

        // Create the NomVernaculaire
        NomVernaculaireDTO nomVernaculaireDTO = nomVernaculaireMapper.toDto(nomVernaculaire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNomVernaculaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nomVernaculaireDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nomVernaculaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NomVernaculaire in the database
        List<NomVernaculaire> nomVernaculaireList = nomVernaculaireRepository.findAll();
        assertThat(nomVernaculaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNomVernaculaire() throws Exception {
        int databaseSizeBeforeUpdate = nomVernaculaireRepository.findAll().size();
        nomVernaculaire.setId(count.incrementAndGet());

        // Create the NomVernaculaire
        NomVernaculaireDTO nomVernaculaireDTO = nomVernaculaireMapper.toDto(nomVernaculaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNomVernaculaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nomVernaculaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NomVernaculaire in the database
        List<NomVernaculaire> nomVernaculaireList = nomVernaculaireRepository.findAll();
        assertThat(nomVernaculaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNomVernaculaire() throws Exception {
        int databaseSizeBeforeUpdate = nomVernaculaireRepository.findAll().size();
        nomVernaculaire.setId(count.incrementAndGet());

        // Create the NomVernaculaire
        NomVernaculaireDTO nomVernaculaireDTO = nomVernaculaireMapper.toDto(nomVernaculaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNomVernaculaireMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nomVernaculaireDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NomVernaculaire in the database
        List<NomVernaculaire> nomVernaculaireList = nomVernaculaireRepository.findAll();
        assertThat(nomVernaculaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNomVernaculaire() throws Exception {
        // Initialize the database
        nomVernaculaireRepository.saveAndFlush(nomVernaculaire);

        int databaseSizeBeforeDelete = nomVernaculaireRepository.findAll().size();

        // Delete the nomVernaculaire
        restNomVernaculaireMockMvc
            .perform(delete(ENTITY_API_URL_ID, nomVernaculaire.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<NomVernaculaire> nomVernaculaireList = nomVernaculaireRepository.findAll();
        assertThat(nomVernaculaireList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
