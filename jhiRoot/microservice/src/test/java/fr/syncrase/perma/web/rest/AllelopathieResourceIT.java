package fr.syncrase.perma.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.perma.IntegrationTest;
import fr.syncrase.perma.domain.Allelopathie;
import fr.syncrase.perma.domain.Plante;
import fr.syncrase.perma.repository.AllelopathieRepository;
import fr.syncrase.perma.service.criteria.AllelopathieCriteria;
import fr.syncrase.perma.service.dto.AllelopathieDTO;
import fr.syncrase.perma.service.mapper.AllelopathieMapper;
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
 * Integration tests for the {@link AllelopathieResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AllelopathieResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/allelopathies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AllelopathieRepository allelopathieRepository;

    @Autowired
    private AllelopathieMapper allelopathieMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAllelopathieMockMvc;

    private Allelopathie allelopathie;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Allelopathie createEntity(EntityManager em) {
        Allelopathie allelopathie = new Allelopathie().type(DEFAULT_TYPE).description(DEFAULT_DESCRIPTION);
        return allelopathie;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Allelopathie createUpdatedEntity(EntityManager em) {
        Allelopathie allelopathie = new Allelopathie().type(UPDATED_TYPE).description(UPDATED_DESCRIPTION);
        return allelopathie;
    }

    @BeforeEach
    public void initTest() {
        allelopathie = createEntity(em);
    }

    @Test
    @Transactional
    void createAllelopathie() throws Exception {
        int databaseSizeBeforeCreate = allelopathieRepository.findAll().size();
        // Create the Allelopathie
        AllelopathieDTO allelopathieDTO = allelopathieMapper.toDto(allelopathie);
        restAllelopathieMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(allelopathieDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Allelopathie in the database
        List<Allelopathie> allelopathieList = allelopathieRepository.findAll();
        assertThat(allelopathieList).hasSize(databaseSizeBeforeCreate + 1);
        Allelopathie testAllelopathie = allelopathieList.get(allelopathieList.size() - 1);
        assertThat(testAllelopathie.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testAllelopathie.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createAllelopathieWithExistingId() throws Exception {
        // Create the Allelopathie with an existing ID
        allelopathie.setId(1L);
        AllelopathieDTO allelopathieDTO = allelopathieMapper.toDto(allelopathie);

        int databaseSizeBeforeCreate = allelopathieRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAllelopathieMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(allelopathieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Allelopathie in the database
        List<Allelopathie> allelopathieList = allelopathieRepository.findAll();
        assertThat(allelopathieList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = allelopathieRepository.findAll().size();
        // set the field null
        allelopathie.setType(null);

        // Create the Allelopathie, which fails.
        AllelopathieDTO allelopathieDTO = allelopathieMapper.toDto(allelopathie);

        restAllelopathieMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(allelopathieDTO))
            )
            .andExpect(status().isBadRequest());

        List<Allelopathie> allelopathieList = allelopathieRepository.findAll();
        assertThat(allelopathieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAllelopathies() throws Exception {
        // Initialize the database
        allelopathieRepository.saveAndFlush(allelopathie);

        // Get all the allelopathieList
        restAllelopathieMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(allelopathie.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getAllelopathie() throws Exception {
        // Initialize the database
        allelopathieRepository.saveAndFlush(allelopathie);

        // Get the allelopathie
        restAllelopathieMockMvc
            .perform(get(ENTITY_API_URL_ID, allelopathie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(allelopathie.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getAllelopathiesByIdFiltering() throws Exception {
        // Initialize the database
        allelopathieRepository.saveAndFlush(allelopathie);

        Long id = allelopathie.getId();

        defaultAllelopathieShouldBeFound("id.equals=" + id);
        defaultAllelopathieShouldNotBeFound("id.notEquals=" + id);

        defaultAllelopathieShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAllelopathieShouldNotBeFound("id.greaterThan=" + id);

        defaultAllelopathieShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAllelopathieShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAllelopathiesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        allelopathieRepository.saveAndFlush(allelopathie);

        // Get all the allelopathieList where type equals to DEFAULT_TYPE
        defaultAllelopathieShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the allelopathieList where type equals to UPDATED_TYPE
        defaultAllelopathieShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllAllelopathiesByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        allelopathieRepository.saveAndFlush(allelopathie);

        // Get all the allelopathieList where type not equals to DEFAULT_TYPE
        defaultAllelopathieShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the allelopathieList where type not equals to UPDATED_TYPE
        defaultAllelopathieShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllAllelopathiesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        allelopathieRepository.saveAndFlush(allelopathie);

        // Get all the allelopathieList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultAllelopathieShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the allelopathieList where type equals to UPDATED_TYPE
        defaultAllelopathieShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllAllelopathiesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        allelopathieRepository.saveAndFlush(allelopathie);

        // Get all the allelopathieList where type is not null
        defaultAllelopathieShouldBeFound("type.specified=true");

        // Get all the allelopathieList where type is null
        defaultAllelopathieShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllAllelopathiesByTypeContainsSomething() throws Exception {
        // Initialize the database
        allelopathieRepository.saveAndFlush(allelopathie);

        // Get all the allelopathieList where type contains DEFAULT_TYPE
        defaultAllelopathieShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the allelopathieList where type contains UPDATED_TYPE
        defaultAllelopathieShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllAllelopathiesByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        allelopathieRepository.saveAndFlush(allelopathie);

        // Get all the allelopathieList where type does not contain DEFAULT_TYPE
        defaultAllelopathieShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the allelopathieList where type does not contain UPDATED_TYPE
        defaultAllelopathieShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllAllelopathiesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        allelopathieRepository.saveAndFlush(allelopathie);

        // Get all the allelopathieList where description equals to DEFAULT_DESCRIPTION
        defaultAllelopathieShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the allelopathieList where description equals to UPDATED_DESCRIPTION
        defaultAllelopathieShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAllelopathiesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        allelopathieRepository.saveAndFlush(allelopathie);

        // Get all the allelopathieList where description not equals to DEFAULT_DESCRIPTION
        defaultAllelopathieShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the allelopathieList where description not equals to UPDATED_DESCRIPTION
        defaultAllelopathieShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAllelopathiesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        allelopathieRepository.saveAndFlush(allelopathie);

        // Get all the allelopathieList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultAllelopathieShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the allelopathieList where description equals to UPDATED_DESCRIPTION
        defaultAllelopathieShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAllelopathiesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        allelopathieRepository.saveAndFlush(allelopathie);

        // Get all the allelopathieList where description is not null
        defaultAllelopathieShouldBeFound("description.specified=true");

        // Get all the allelopathieList where description is null
        defaultAllelopathieShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllAllelopathiesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        allelopathieRepository.saveAndFlush(allelopathie);

        // Get all the allelopathieList where description contains DEFAULT_DESCRIPTION
        defaultAllelopathieShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the allelopathieList where description contains UPDATED_DESCRIPTION
        defaultAllelopathieShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAllelopathiesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        allelopathieRepository.saveAndFlush(allelopathie);

        // Get all the allelopathieList where description does not contain DEFAULT_DESCRIPTION
        defaultAllelopathieShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the allelopathieList where description does not contain UPDATED_DESCRIPTION
        defaultAllelopathieShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAllelopathiesByCibleIsEqualToSomething() throws Exception {
        // Initialize the database
        allelopathieRepository.saveAndFlush(allelopathie);
        Plante cible;
        if (TestUtil.findAll(em, Plante.class).isEmpty()) {
            cible = PlanteResourceIT.createEntity(em);
            em.persist(cible);
            em.flush();
        } else {
            cible = TestUtil.findAll(em, Plante.class).get(0);
        }
        em.persist(cible);
        em.flush();
        allelopathie.setCible(cible);
        allelopathieRepository.saveAndFlush(allelopathie);
        Long cibleId = cible.getId();

        // Get all the allelopathieList where cible equals to cibleId
        defaultAllelopathieShouldBeFound("cibleId.equals=" + cibleId);

        // Get all the allelopathieList where cible equals to (cibleId + 1)
        defaultAllelopathieShouldNotBeFound("cibleId.equals=" + (cibleId + 1));
    }

    @Test
    @Transactional
    void getAllAllelopathiesByOrigineIsEqualToSomething() throws Exception {
        // Initialize the database
        allelopathieRepository.saveAndFlush(allelopathie);
        Plante origine;
        if (TestUtil.findAll(em, Plante.class).isEmpty()) {
            origine = PlanteResourceIT.createEntity(em);
            em.persist(origine);
            em.flush();
        } else {
            origine = TestUtil.findAll(em, Plante.class).get(0);
        }
        em.persist(origine);
        em.flush();
        allelopathie.setOrigine(origine);
        allelopathieRepository.saveAndFlush(allelopathie);
        Long origineId = origine.getId();

        // Get all the allelopathieList where origine equals to origineId
        defaultAllelopathieShouldBeFound("origineId.equals=" + origineId);

        // Get all the allelopathieList where origine equals to (origineId + 1)
        defaultAllelopathieShouldNotBeFound("origineId.equals=" + (origineId + 1));
    }

    @Test
    @Transactional
    void getAllAllelopathiesByInteractionIsEqualToSomething() throws Exception {
        // Initialize the database
        allelopathieRepository.saveAndFlush(allelopathie);
        Plante interaction;
        if (TestUtil.findAll(em, Plante.class).isEmpty()) {
            interaction = PlanteResourceIT.createEntity(em);
            em.persist(interaction);
            em.flush();
        } else {
            interaction = TestUtil.findAll(em, Plante.class).get(0);
        }
        em.persist(interaction);
        em.flush();
        allelopathie.setInteraction(interaction);
        allelopathieRepository.saveAndFlush(allelopathie);
        Long interactionId = interaction.getId();

        // Get all the allelopathieList where interaction equals to interactionId
        defaultAllelopathieShouldBeFound("interactionId.equals=" + interactionId);

        // Get all the allelopathieList where interaction equals to (interactionId + 1)
        defaultAllelopathieShouldNotBeFound("interactionId.equals=" + (interactionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAllelopathieShouldBeFound(String filter) throws Exception {
        restAllelopathieMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(allelopathie.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restAllelopathieMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAllelopathieShouldNotBeFound(String filter) throws Exception {
        restAllelopathieMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAllelopathieMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAllelopathie() throws Exception {
        // Get the allelopathie
        restAllelopathieMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAllelopathie() throws Exception {
        // Initialize the database
        allelopathieRepository.saveAndFlush(allelopathie);

        int databaseSizeBeforeUpdate = allelopathieRepository.findAll().size();

        // Update the allelopathie
        Allelopathie updatedAllelopathie = allelopathieRepository.findById(allelopathie.getId()).get();
        // Disconnect from session so that the updates on updatedAllelopathie are not directly saved in db
        em.detach(updatedAllelopathie);
        updatedAllelopathie.type(UPDATED_TYPE).description(UPDATED_DESCRIPTION);
        AllelopathieDTO allelopathieDTO = allelopathieMapper.toDto(updatedAllelopathie);

        restAllelopathieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, allelopathieDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(allelopathieDTO))
            )
            .andExpect(status().isOk());

        // Validate the Allelopathie in the database
        List<Allelopathie> allelopathieList = allelopathieRepository.findAll();
        assertThat(allelopathieList).hasSize(databaseSizeBeforeUpdate);
        Allelopathie testAllelopathie = allelopathieList.get(allelopathieList.size() - 1);
        assertThat(testAllelopathie.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAllelopathie.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingAllelopathie() throws Exception {
        int databaseSizeBeforeUpdate = allelopathieRepository.findAll().size();
        allelopathie.setId(count.incrementAndGet());

        // Create the Allelopathie
        AllelopathieDTO allelopathieDTO = allelopathieMapper.toDto(allelopathie);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAllelopathieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, allelopathieDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(allelopathieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Allelopathie in the database
        List<Allelopathie> allelopathieList = allelopathieRepository.findAll();
        assertThat(allelopathieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAllelopathie() throws Exception {
        int databaseSizeBeforeUpdate = allelopathieRepository.findAll().size();
        allelopathie.setId(count.incrementAndGet());

        // Create the Allelopathie
        AllelopathieDTO allelopathieDTO = allelopathieMapper.toDto(allelopathie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAllelopathieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(allelopathieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Allelopathie in the database
        List<Allelopathie> allelopathieList = allelopathieRepository.findAll();
        assertThat(allelopathieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAllelopathie() throws Exception {
        int databaseSizeBeforeUpdate = allelopathieRepository.findAll().size();
        allelopathie.setId(count.incrementAndGet());

        // Create the Allelopathie
        AllelopathieDTO allelopathieDTO = allelopathieMapper.toDto(allelopathie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAllelopathieMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(allelopathieDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Allelopathie in the database
        List<Allelopathie> allelopathieList = allelopathieRepository.findAll();
        assertThat(allelopathieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAllelopathieWithPatch() throws Exception {
        // Initialize the database
        allelopathieRepository.saveAndFlush(allelopathie);

        int databaseSizeBeforeUpdate = allelopathieRepository.findAll().size();

        // Update the allelopathie using partial update
        Allelopathie partialUpdatedAllelopathie = new Allelopathie();
        partialUpdatedAllelopathie.setId(allelopathie.getId());

        partialUpdatedAllelopathie.type(UPDATED_TYPE);

        restAllelopathieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAllelopathie.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAllelopathie))
            )
            .andExpect(status().isOk());

        // Validate the Allelopathie in the database
        List<Allelopathie> allelopathieList = allelopathieRepository.findAll();
        assertThat(allelopathieList).hasSize(databaseSizeBeforeUpdate);
        Allelopathie testAllelopathie = allelopathieList.get(allelopathieList.size() - 1);
        assertThat(testAllelopathie.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAllelopathie.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateAllelopathieWithPatch() throws Exception {
        // Initialize the database
        allelopathieRepository.saveAndFlush(allelopathie);

        int databaseSizeBeforeUpdate = allelopathieRepository.findAll().size();

        // Update the allelopathie using partial update
        Allelopathie partialUpdatedAllelopathie = new Allelopathie();
        partialUpdatedAllelopathie.setId(allelopathie.getId());

        partialUpdatedAllelopathie.type(UPDATED_TYPE).description(UPDATED_DESCRIPTION);

        restAllelopathieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAllelopathie.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAllelopathie))
            )
            .andExpect(status().isOk());

        // Validate the Allelopathie in the database
        List<Allelopathie> allelopathieList = allelopathieRepository.findAll();
        assertThat(allelopathieList).hasSize(databaseSizeBeforeUpdate);
        Allelopathie testAllelopathie = allelopathieList.get(allelopathieList.size() - 1);
        assertThat(testAllelopathie.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAllelopathie.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingAllelopathie() throws Exception {
        int databaseSizeBeforeUpdate = allelopathieRepository.findAll().size();
        allelopathie.setId(count.incrementAndGet());

        // Create the Allelopathie
        AllelopathieDTO allelopathieDTO = allelopathieMapper.toDto(allelopathie);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAllelopathieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, allelopathieDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(allelopathieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Allelopathie in the database
        List<Allelopathie> allelopathieList = allelopathieRepository.findAll();
        assertThat(allelopathieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAllelopathie() throws Exception {
        int databaseSizeBeforeUpdate = allelopathieRepository.findAll().size();
        allelopathie.setId(count.incrementAndGet());

        // Create the Allelopathie
        AllelopathieDTO allelopathieDTO = allelopathieMapper.toDto(allelopathie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAllelopathieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(allelopathieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Allelopathie in the database
        List<Allelopathie> allelopathieList = allelopathieRepository.findAll();
        assertThat(allelopathieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAllelopathie() throws Exception {
        int databaseSizeBeforeUpdate = allelopathieRepository.findAll().size();
        allelopathie.setId(count.incrementAndGet());

        // Create the Allelopathie
        AllelopathieDTO allelopathieDTO = allelopathieMapper.toDto(allelopathie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAllelopathieMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(allelopathieDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Allelopathie in the database
        List<Allelopathie> allelopathieList = allelopathieRepository.findAll();
        assertThat(allelopathieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAllelopathie() throws Exception {
        // Initialize the database
        allelopathieRepository.saveAndFlush(allelopathie);

        int databaseSizeBeforeDelete = allelopathieRepository.findAll().size();

        // Delete the allelopathie
        restAllelopathieMockMvc
            .perform(delete(ENTITY_API_URL_ID, allelopathie.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Allelopathie> allelopathieList = allelopathieRepository.findAll();
        assertThat(allelopathieList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
