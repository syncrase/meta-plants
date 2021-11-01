package fr.syncrase.perma.web.rest;

import fr.syncrase.perma.MicroserviceApp;
import fr.syncrase.perma.config.TestSecurityConfiguration;
import fr.syncrase.perma.domain.Allelopathie;
import fr.syncrase.perma.domain.Plante;
import fr.syncrase.perma.repository.AllelopathieRepository;
import fr.syncrase.perma.service.AllelopathieService;
import fr.syncrase.perma.service.dto.AllelopathieDTO;
import fr.syncrase.perma.service.mapper.AllelopathieMapper;
import fr.syncrase.perma.service.dto.AllelopathieCriteria;
import fr.syncrase.perma.service.AllelopathieQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AllelopathieResource} REST controller.
 */
@SpringBootTest(classes = { MicroserviceApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class AllelopathieResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private AllelopathieRepository allelopathieRepository;

    @Autowired
    private AllelopathieMapper allelopathieMapper;

    @Autowired
    private AllelopathieService allelopathieService;

    @Autowired
    private AllelopathieQueryService allelopathieQueryService;

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
        Allelopathie allelopathie = new Allelopathie()
            .type(DEFAULT_TYPE)
            .description(DEFAULT_DESCRIPTION);
        return allelopathie;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Allelopathie createUpdatedEntity(EntityManager em) {
        Allelopathie allelopathie = new Allelopathie()
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION);
        return allelopathie;
    }

    @BeforeEach
    public void initTest() {
        allelopathie = createEntity(em);
    }

    @Test
    @Transactional
    public void createAllelopathie() throws Exception {
        int databaseSizeBeforeCreate = allelopathieRepository.findAll().size();
        // Create the Allelopathie
        AllelopathieDTO allelopathieDTO = allelopathieMapper.toDto(allelopathie);
        restAllelopathieMockMvc.perform(post("/api/allelopathies").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(allelopathieDTO)))
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
    public void createAllelopathieWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = allelopathieRepository.findAll().size();

        // Create the Allelopathie with an existing ID
        allelopathie.setId(1L);
        AllelopathieDTO allelopathieDTO = allelopathieMapper.toDto(allelopathie);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAllelopathieMockMvc.perform(post("/api/allelopathies").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(allelopathieDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Allelopathie in the database
        List<Allelopathie> allelopathieList = allelopathieRepository.findAll();
        assertThat(allelopathieList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = allelopathieRepository.findAll().size();
        // set the field null
        allelopathie.setType(null);

        // Create the Allelopathie, which fails.
        AllelopathieDTO allelopathieDTO = allelopathieMapper.toDto(allelopathie);


        restAllelopathieMockMvc.perform(post("/api/allelopathies").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(allelopathieDTO)))
            .andExpect(status().isBadRequest());

        List<Allelopathie> allelopathieList = allelopathieRepository.findAll();
        assertThat(allelopathieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAllelopathies() throws Exception {
        // Initialize the database
        allelopathieRepository.saveAndFlush(allelopathie);

        // Get all the allelopathieList
        restAllelopathieMockMvc.perform(get("/api/allelopathies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(allelopathie.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getAllelopathie() throws Exception {
        // Initialize the database
        allelopathieRepository.saveAndFlush(allelopathie);

        // Get the allelopathie
        restAllelopathieMockMvc.perform(get("/api/allelopathies/{id}", allelopathie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(allelopathie.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }


    @Test
    @Transactional
    public void getAllelopathiesByIdFiltering() throws Exception {
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
    public void getAllAllelopathiesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        allelopathieRepository.saveAndFlush(allelopathie);

        // Get all the allelopathieList where type equals to DEFAULT_TYPE
        defaultAllelopathieShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the allelopathieList where type equals to UPDATED_TYPE
        defaultAllelopathieShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllAllelopathiesByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        allelopathieRepository.saveAndFlush(allelopathie);

        // Get all the allelopathieList where type not equals to DEFAULT_TYPE
        defaultAllelopathieShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the allelopathieList where type not equals to UPDATED_TYPE
        defaultAllelopathieShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllAllelopathiesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        allelopathieRepository.saveAndFlush(allelopathie);

        // Get all the allelopathieList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultAllelopathieShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the allelopathieList where type equals to UPDATED_TYPE
        defaultAllelopathieShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllAllelopathiesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        allelopathieRepository.saveAndFlush(allelopathie);

        // Get all the allelopathieList where type is not null
        defaultAllelopathieShouldBeFound("type.specified=true");

        // Get all the allelopathieList where type is null
        defaultAllelopathieShouldNotBeFound("type.specified=false");
    }
                @Test
    @Transactional
    public void getAllAllelopathiesByTypeContainsSomething() throws Exception {
        // Initialize the database
        allelopathieRepository.saveAndFlush(allelopathie);

        // Get all the allelopathieList where type contains DEFAULT_TYPE
        defaultAllelopathieShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the allelopathieList where type contains UPDATED_TYPE
        defaultAllelopathieShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllAllelopathiesByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        allelopathieRepository.saveAndFlush(allelopathie);

        // Get all the allelopathieList where type does not contain DEFAULT_TYPE
        defaultAllelopathieShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the allelopathieList where type does not contain UPDATED_TYPE
        defaultAllelopathieShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }


    @Test
    @Transactional
    public void getAllAllelopathiesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        allelopathieRepository.saveAndFlush(allelopathie);

        // Get all the allelopathieList where description equals to DEFAULT_DESCRIPTION
        defaultAllelopathieShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the allelopathieList where description equals to UPDATED_DESCRIPTION
        defaultAllelopathieShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllAllelopathiesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        allelopathieRepository.saveAndFlush(allelopathie);

        // Get all the allelopathieList where description not equals to DEFAULT_DESCRIPTION
        defaultAllelopathieShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the allelopathieList where description not equals to UPDATED_DESCRIPTION
        defaultAllelopathieShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllAllelopathiesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        allelopathieRepository.saveAndFlush(allelopathie);

        // Get all the allelopathieList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultAllelopathieShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the allelopathieList where description equals to UPDATED_DESCRIPTION
        defaultAllelopathieShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllAllelopathiesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        allelopathieRepository.saveAndFlush(allelopathie);

        // Get all the allelopathieList where description is not null
        defaultAllelopathieShouldBeFound("description.specified=true");

        // Get all the allelopathieList where description is null
        defaultAllelopathieShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllAllelopathiesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        allelopathieRepository.saveAndFlush(allelopathie);

        // Get all the allelopathieList where description contains DEFAULT_DESCRIPTION
        defaultAllelopathieShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the allelopathieList where description contains UPDATED_DESCRIPTION
        defaultAllelopathieShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllAllelopathiesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        allelopathieRepository.saveAndFlush(allelopathie);

        // Get all the allelopathieList where description does not contain DEFAULT_DESCRIPTION
        defaultAllelopathieShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the allelopathieList where description does not contain UPDATED_DESCRIPTION
        defaultAllelopathieShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllAllelopathiesByCibleIsEqualToSomething() throws Exception {
        // Initialize the database
        allelopathieRepository.saveAndFlush(allelopathie);
        Plante cible = PlanteResourceIT.createEntity(em);
        em.persist(cible);
        em.flush();
        allelopathie.setCible(cible);
        allelopathieRepository.saveAndFlush(allelopathie);
        Long cibleId = cible.getId();

        // Get all the allelopathieList where cible equals to cibleId
        defaultAllelopathieShouldBeFound("cibleId.equals=" + cibleId);

        // Get all the allelopathieList where cible equals to cibleId + 1
        defaultAllelopathieShouldNotBeFound("cibleId.equals=" + (cibleId + 1));
    }


    @Test
    @Transactional
    public void getAllAllelopathiesByOrigineIsEqualToSomething() throws Exception {
        // Initialize the database
        allelopathieRepository.saveAndFlush(allelopathie);
        Plante origine = PlanteResourceIT.createEntity(em);
        em.persist(origine);
        em.flush();
        allelopathie.setOrigine(origine);
        allelopathieRepository.saveAndFlush(allelopathie);
        Long origineId = origine.getId();

        // Get all the allelopathieList where origine equals to origineId
        defaultAllelopathieShouldBeFound("origineId.equals=" + origineId);

        // Get all the allelopathieList where origine equals to origineId + 1
        defaultAllelopathieShouldNotBeFound("origineId.equals=" + (origineId + 1));
    }


    @Test
    @Transactional
    public void getAllAllelopathiesByPlanteIsEqualToSomething() throws Exception {
        // Initialize the database
        allelopathieRepository.saveAndFlush(allelopathie);
        Plante plante = PlanteResourceIT.createEntity(em);
        em.persist(plante);
        em.flush();
        allelopathie.setPlante(plante);
        allelopathieRepository.saveAndFlush(allelopathie);
        Long planteId = plante.getId();

        // Get all the allelopathieList where plante equals to planteId
        defaultAllelopathieShouldBeFound("planteId.equals=" + planteId);

        // Get all the allelopathieList where plante equals to planteId + 1
        defaultAllelopathieShouldNotBeFound("planteId.equals=" + (planteId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAllelopathieShouldBeFound(String filter) throws Exception {
        restAllelopathieMockMvc.perform(get("/api/allelopathies?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(allelopathie.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restAllelopathieMockMvc.perform(get("/api/allelopathies/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAllelopathieShouldNotBeFound(String filter) throws Exception {
        restAllelopathieMockMvc.perform(get("/api/allelopathies?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAllelopathieMockMvc.perform(get("/api/allelopathies/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingAllelopathie() throws Exception {
        // Get the allelopathie
        restAllelopathieMockMvc.perform(get("/api/allelopathies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAllelopathie() throws Exception {
        // Initialize the database
        allelopathieRepository.saveAndFlush(allelopathie);

        int databaseSizeBeforeUpdate = allelopathieRepository.findAll().size();

        // Update the allelopathie
        Allelopathie updatedAllelopathie = allelopathieRepository.findById(allelopathie.getId()).get();
        // Disconnect from session so that the updates on updatedAllelopathie are not directly saved in db
        em.detach(updatedAllelopathie);
        updatedAllelopathie
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION);
        AllelopathieDTO allelopathieDTO = allelopathieMapper.toDto(updatedAllelopathie);

        restAllelopathieMockMvc.perform(put("/api/allelopathies").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(allelopathieDTO)))
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
    public void updateNonExistingAllelopathie() throws Exception {
        int databaseSizeBeforeUpdate = allelopathieRepository.findAll().size();

        // Create the Allelopathie
        AllelopathieDTO allelopathieDTO = allelopathieMapper.toDto(allelopathie);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAllelopathieMockMvc.perform(put("/api/allelopathies").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(allelopathieDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Allelopathie in the database
        List<Allelopathie> allelopathieList = allelopathieRepository.findAll();
        assertThat(allelopathieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAllelopathie() throws Exception {
        // Initialize the database
        allelopathieRepository.saveAndFlush(allelopathie);

        int databaseSizeBeforeDelete = allelopathieRepository.findAll().size();

        // Delete the allelopathie
        restAllelopathieMockMvc.perform(delete("/api/allelopathies/{id}", allelopathie.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Allelopathie> allelopathieList = allelopathieRepository.findAll();
        assertThat(allelopathieList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
