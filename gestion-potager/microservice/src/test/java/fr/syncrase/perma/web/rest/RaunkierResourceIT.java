package fr.syncrase.perma.web.rest;

import fr.syncrase.perma.MicroserviceApp;
import fr.syncrase.perma.config.TestSecurityConfiguration;
import fr.syncrase.perma.domain.Raunkier;
import fr.syncrase.perma.repository.RaunkierRepository;
import fr.syncrase.perma.service.RaunkierService;
import fr.syncrase.perma.service.dto.RaunkierDTO;
import fr.syncrase.perma.service.mapper.RaunkierMapper;
import fr.syncrase.perma.service.dto.RaunkierCriteria;
import fr.syncrase.perma.service.RaunkierQueryService;

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
 * Integration tests for the {@link RaunkierResource} REST controller.
 */
@SpringBootTest(classes = { MicroserviceApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class RaunkierResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    @Autowired
    private RaunkierRepository raunkierRepository;

    @Autowired
    private RaunkierMapper raunkierMapper;

    @Autowired
    private RaunkierService raunkierService;

    @Autowired
    private RaunkierQueryService raunkierQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRaunkierMockMvc;

    private Raunkier raunkier;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Raunkier createEntity(EntityManager em) {
        Raunkier raunkier = new Raunkier()
            .type(DEFAULT_TYPE);
        return raunkier;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Raunkier createUpdatedEntity(EntityManager em) {
        Raunkier raunkier = new Raunkier()
            .type(UPDATED_TYPE);
        return raunkier;
    }

    @BeforeEach
    public void initTest() {
        raunkier = createEntity(em);
    }

    @Test
    @Transactional
    public void createRaunkier() throws Exception {
        int databaseSizeBeforeCreate = raunkierRepository.findAll().size();
        // Create the Raunkier
        RaunkierDTO raunkierDTO = raunkierMapper.toDto(raunkier);
        restRaunkierMockMvc.perform(post("/api/raunkiers").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(raunkierDTO)))
            .andExpect(status().isCreated());

        // Validate the Raunkier in the database
        List<Raunkier> raunkierList = raunkierRepository.findAll();
        assertThat(raunkierList).hasSize(databaseSizeBeforeCreate + 1);
        Raunkier testRaunkier = raunkierList.get(raunkierList.size() - 1);
        assertThat(testRaunkier.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createRaunkierWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = raunkierRepository.findAll().size();

        // Create the Raunkier with an existing ID
        raunkier.setId(1L);
        RaunkierDTO raunkierDTO = raunkierMapper.toDto(raunkier);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRaunkierMockMvc.perform(post("/api/raunkiers").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(raunkierDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Raunkier in the database
        List<Raunkier> raunkierList = raunkierRepository.findAll();
        assertThat(raunkierList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = raunkierRepository.findAll().size();
        // set the field null
        raunkier.setType(null);

        // Create the Raunkier, which fails.
        RaunkierDTO raunkierDTO = raunkierMapper.toDto(raunkier);


        restRaunkierMockMvc.perform(post("/api/raunkiers").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(raunkierDTO)))
            .andExpect(status().isBadRequest());

        List<Raunkier> raunkierList = raunkierRepository.findAll();
        assertThat(raunkierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRaunkiers() throws Exception {
        // Initialize the database
        raunkierRepository.saveAndFlush(raunkier);

        // Get all the raunkierList
        restRaunkierMockMvc.perform(get("/api/raunkiers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(raunkier.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }
    
    @Test
    @Transactional
    public void getRaunkier() throws Exception {
        // Initialize the database
        raunkierRepository.saveAndFlush(raunkier);

        // Get the raunkier
        restRaunkierMockMvc.perform(get("/api/raunkiers/{id}", raunkier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(raunkier.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }


    @Test
    @Transactional
    public void getRaunkiersByIdFiltering() throws Exception {
        // Initialize the database
        raunkierRepository.saveAndFlush(raunkier);

        Long id = raunkier.getId();

        defaultRaunkierShouldBeFound("id.equals=" + id);
        defaultRaunkierShouldNotBeFound("id.notEquals=" + id);

        defaultRaunkierShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRaunkierShouldNotBeFound("id.greaterThan=" + id);

        defaultRaunkierShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRaunkierShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllRaunkiersByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        raunkierRepository.saveAndFlush(raunkier);

        // Get all the raunkierList where type equals to DEFAULT_TYPE
        defaultRaunkierShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the raunkierList where type equals to UPDATED_TYPE
        defaultRaunkierShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllRaunkiersByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        raunkierRepository.saveAndFlush(raunkier);

        // Get all the raunkierList where type not equals to DEFAULT_TYPE
        defaultRaunkierShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the raunkierList where type not equals to UPDATED_TYPE
        defaultRaunkierShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllRaunkiersByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        raunkierRepository.saveAndFlush(raunkier);

        // Get all the raunkierList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultRaunkierShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the raunkierList where type equals to UPDATED_TYPE
        defaultRaunkierShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllRaunkiersByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        raunkierRepository.saveAndFlush(raunkier);

        // Get all the raunkierList where type is not null
        defaultRaunkierShouldBeFound("type.specified=true");

        // Get all the raunkierList where type is null
        defaultRaunkierShouldNotBeFound("type.specified=false");
    }
                @Test
    @Transactional
    public void getAllRaunkiersByTypeContainsSomething() throws Exception {
        // Initialize the database
        raunkierRepository.saveAndFlush(raunkier);

        // Get all the raunkierList where type contains DEFAULT_TYPE
        defaultRaunkierShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the raunkierList where type contains UPDATED_TYPE
        defaultRaunkierShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllRaunkiersByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        raunkierRepository.saveAndFlush(raunkier);

        // Get all the raunkierList where type does not contain DEFAULT_TYPE
        defaultRaunkierShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the raunkierList where type does not contain UPDATED_TYPE
        defaultRaunkierShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRaunkierShouldBeFound(String filter) throws Exception {
        restRaunkierMockMvc.perform(get("/api/raunkiers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(raunkier.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));

        // Check, that the count call also returns 1
        restRaunkierMockMvc.perform(get("/api/raunkiers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRaunkierShouldNotBeFound(String filter) throws Exception {
        restRaunkierMockMvc.perform(get("/api/raunkiers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRaunkierMockMvc.perform(get("/api/raunkiers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingRaunkier() throws Exception {
        // Get the raunkier
        restRaunkierMockMvc.perform(get("/api/raunkiers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRaunkier() throws Exception {
        // Initialize the database
        raunkierRepository.saveAndFlush(raunkier);

        int databaseSizeBeforeUpdate = raunkierRepository.findAll().size();

        // Update the raunkier
        Raunkier updatedRaunkier = raunkierRepository.findById(raunkier.getId()).get();
        // Disconnect from session so that the updates on updatedRaunkier are not directly saved in db
        em.detach(updatedRaunkier);
        updatedRaunkier
            .type(UPDATED_TYPE);
        RaunkierDTO raunkierDTO = raunkierMapper.toDto(updatedRaunkier);

        restRaunkierMockMvc.perform(put("/api/raunkiers").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(raunkierDTO)))
            .andExpect(status().isOk());

        // Validate the Raunkier in the database
        List<Raunkier> raunkierList = raunkierRepository.findAll();
        assertThat(raunkierList).hasSize(databaseSizeBeforeUpdate);
        Raunkier testRaunkier = raunkierList.get(raunkierList.size() - 1);
        assertThat(testRaunkier.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingRaunkier() throws Exception {
        int databaseSizeBeforeUpdate = raunkierRepository.findAll().size();

        // Create the Raunkier
        RaunkierDTO raunkierDTO = raunkierMapper.toDto(raunkier);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRaunkierMockMvc.perform(put("/api/raunkiers").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(raunkierDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Raunkier in the database
        List<Raunkier> raunkierList = raunkierRepository.findAll();
        assertThat(raunkierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRaunkier() throws Exception {
        // Initialize the database
        raunkierRepository.saveAndFlush(raunkier);

        int databaseSizeBeforeDelete = raunkierRepository.findAll().size();

        // Delete the raunkier
        restRaunkierMockMvc.perform(delete("/api/raunkiers/{id}", raunkier.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Raunkier> raunkierList = raunkierRepository.findAll();
        assertThat(raunkierList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
