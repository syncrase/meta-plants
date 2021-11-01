package fr.syncrase.perma.web.rest;

import fr.syncrase.perma.MicroserviceApp;
import fr.syncrase.perma.config.TestSecurityConfiguration;
import fr.syncrase.perma.domain.Sol;
import fr.syncrase.perma.repository.SolRepository;
import fr.syncrase.perma.service.SolService;
import fr.syncrase.perma.service.dto.SolDTO;
import fr.syncrase.perma.service.mapper.SolMapper;
import fr.syncrase.perma.service.dto.SolCriteria;
import fr.syncrase.perma.service.SolQueryService;

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
 * Integration tests for the {@link SolResource} REST controller.
 */
@SpringBootTest(classes = { MicroserviceApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class SolResourceIT {

    private static final Double DEFAULT_ACIDITE = 1D;
    private static final Double UPDATED_ACIDITE = 2D;
    private static final Double SMALLER_ACIDITE = 1D - 1D;

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    @Autowired
    private SolRepository solRepository;

    @Autowired
    private SolMapper solMapper;

    @Autowired
    private SolService solService;

    @Autowired
    private SolQueryService solQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSolMockMvc;

    private Sol sol;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sol createEntity(EntityManager em) {
        Sol sol = new Sol()
            .acidite(DEFAULT_ACIDITE)
            .type(DEFAULT_TYPE);
        return sol;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sol createUpdatedEntity(EntityManager em) {
        Sol sol = new Sol()
            .acidite(UPDATED_ACIDITE)
            .type(UPDATED_TYPE);
        return sol;
    }

    @BeforeEach
    public void initTest() {
        sol = createEntity(em);
    }

    @Test
    @Transactional
    public void createSol() throws Exception {
        int databaseSizeBeforeCreate = solRepository.findAll().size();
        // Create the Sol
        SolDTO solDTO = solMapper.toDto(sol);
        restSolMockMvc.perform(post("/api/sols").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(solDTO)))
            .andExpect(status().isCreated());

        // Validate the Sol in the database
        List<Sol> solList = solRepository.findAll();
        assertThat(solList).hasSize(databaseSizeBeforeCreate + 1);
        Sol testSol = solList.get(solList.size() - 1);
        assertThat(testSol.getAcidite()).isEqualTo(DEFAULT_ACIDITE);
        assertThat(testSol.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createSolWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = solRepository.findAll().size();

        // Create the Sol with an existing ID
        sol.setId(1L);
        SolDTO solDTO = solMapper.toDto(sol);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSolMockMvc.perform(post("/api/sols").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(solDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Sol in the database
        List<Sol> solList = solRepository.findAll();
        assertThat(solList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllSols() throws Exception {
        // Initialize the database
        solRepository.saveAndFlush(sol);

        // Get all the solList
        restSolMockMvc.perform(get("/api/sols?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sol.getId().intValue())))
            .andExpect(jsonPath("$.[*].acidite").value(hasItem(DEFAULT_ACIDITE.doubleValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }
    
    @Test
    @Transactional
    public void getSol() throws Exception {
        // Initialize the database
        solRepository.saveAndFlush(sol);

        // Get the sol
        restSolMockMvc.perform(get("/api/sols/{id}", sol.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sol.getId().intValue()))
            .andExpect(jsonPath("$.acidite").value(DEFAULT_ACIDITE.doubleValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }


    @Test
    @Transactional
    public void getSolsByIdFiltering() throws Exception {
        // Initialize the database
        solRepository.saveAndFlush(sol);

        Long id = sol.getId();

        defaultSolShouldBeFound("id.equals=" + id);
        defaultSolShouldNotBeFound("id.notEquals=" + id);

        defaultSolShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSolShouldNotBeFound("id.greaterThan=" + id);

        defaultSolShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSolShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllSolsByAciditeIsEqualToSomething() throws Exception {
        // Initialize the database
        solRepository.saveAndFlush(sol);

        // Get all the solList where acidite equals to DEFAULT_ACIDITE
        defaultSolShouldBeFound("acidite.equals=" + DEFAULT_ACIDITE);

        // Get all the solList where acidite equals to UPDATED_ACIDITE
        defaultSolShouldNotBeFound("acidite.equals=" + UPDATED_ACIDITE);
    }

    @Test
    @Transactional
    public void getAllSolsByAciditeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        solRepository.saveAndFlush(sol);

        // Get all the solList where acidite not equals to DEFAULT_ACIDITE
        defaultSolShouldNotBeFound("acidite.notEquals=" + DEFAULT_ACIDITE);

        // Get all the solList where acidite not equals to UPDATED_ACIDITE
        defaultSolShouldBeFound("acidite.notEquals=" + UPDATED_ACIDITE);
    }

    @Test
    @Transactional
    public void getAllSolsByAciditeIsInShouldWork() throws Exception {
        // Initialize the database
        solRepository.saveAndFlush(sol);

        // Get all the solList where acidite in DEFAULT_ACIDITE or UPDATED_ACIDITE
        defaultSolShouldBeFound("acidite.in=" + DEFAULT_ACIDITE + "," + UPDATED_ACIDITE);

        // Get all the solList where acidite equals to UPDATED_ACIDITE
        defaultSolShouldNotBeFound("acidite.in=" + UPDATED_ACIDITE);
    }

    @Test
    @Transactional
    public void getAllSolsByAciditeIsNullOrNotNull() throws Exception {
        // Initialize the database
        solRepository.saveAndFlush(sol);

        // Get all the solList where acidite is not null
        defaultSolShouldBeFound("acidite.specified=true");

        // Get all the solList where acidite is null
        defaultSolShouldNotBeFound("acidite.specified=false");
    }

    @Test
    @Transactional
    public void getAllSolsByAciditeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        solRepository.saveAndFlush(sol);

        // Get all the solList where acidite is greater than or equal to DEFAULT_ACIDITE
        defaultSolShouldBeFound("acidite.greaterThanOrEqual=" + DEFAULT_ACIDITE);

        // Get all the solList where acidite is greater than or equal to UPDATED_ACIDITE
        defaultSolShouldNotBeFound("acidite.greaterThanOrEqual=" + UPDATED_ACIDITE);
    }

    @Test
    @Transactional
    public void getAllSolsByAciditeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        solRepository.saveAndFlush(sol);

        // Get all the solList where acidite is less than or equal to DEFAULT_ACIDITE
        defaultSolShouldBeFound("acidite.lessThanOrEqual=" + DEFAULT_ACIDITE);

        // Get all the solList where acidite is less than or equal to SMALLER_ACIDITE
        defaultSolShouldNotBeFound("acidite.lessThanOrEqual=" + SMALLER_ACIDITE);
    }

    @Test
    @Transactional
    public void getAllSolsByAciditeIsLessThanSomething() throws Exception {
        // Initialize the database
        solRepository.saveAndFlush(sol);

        // Get all the solList where acidite is less than DEFAULT_ACIDITE
        defaultSolShouldNotBeFound("acidite.lessThan=" + DEFAULT_ACIDITE);

        // Get all the solList where acidite is less than UPDATED_ACIDITE
        defaultSolShouldBeFound("acidite.lessThan=" + UPDATED_ACIDITE);
    }

    @Test
    @Transactional
    public void getAllSolsByAciditeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        solRepository.saveAndFlush(sol);

        // Get all the solList where acidite is greater than DEFAULT_ACIDITE
        defaultSolShouldNotBeFound("acidite.greaterThan=" + DEFAULT_ACIDITE);

        // Get all the solList where acidite is greater than SMALLER_ACIDITE
        defaultSolShouldBeFound("acidite.greaterThan=" + SMALLER_ACIDITE);
    }


    @Test
    @Transactional
    public void getAllSolsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        solRepository.saveAndFlush(sol);

        // Get all the solList where type equals to DEFAULT_TYPE
        defaultSolShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the solList where type equals to UPDATED_TYPE
        defaultSolShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllSolsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        solRepository.saveAndFlush(sol);

        // Get all the solList where type not equals to DEFAULT_TYPE
        defaultSolShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the solList where type not equals to UPDATED_TYPE
        defaultSolShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllSolsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        solRepository.saveAndFlush(sol);

        // Get all the solList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultSolShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the solList where type equals to UPDATED_TYPE
        defaultSolShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllSolsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        solRepository.saveAndFlush(sol);

        // Get all the solList where type is not null
        defaultSolShouldBeFound("type.specified=true");

        // Get all the solList where type is null
        defaultSolShouldNotBeFound("type.specified=false");
    }
                @Test
    @Transactional
    public void getAllSolsByTypeContainsSomething() throws Exception {
        // Initialize the database
        solRepository.saveAndFlush(sol);

        // Get all the solList where type contains DEFAULT_TYPE
        defaultSolShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the solList where type contains UPDATED_TYPE
        defaultSolShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllSolsByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        solRepository.saveAndFlush(sol);

        // Get all the solList where type does not contain DEFAULT_TYPE
        defaultSolShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the solList where type does not contain UPDATED_TYPE
        defaultSolShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSolShouldBeFound(String filter) throws Exception {
        restSolMockMvc.perform(get("/api/sols?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sol.getId().intValue())))
            .andExpect(jsonPath("$.[*].acidite").value(hasItem(DEFAULT_ACIDITE.doubleValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));

        // Check, that the count call also returns 1
        restSolMockMvc.perform(get("/api/sols/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSolShouldNotBeFound(String filter) throws Exception {
        restSolMockMvc.perform(get("/api/sols?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSolMockMvc.perform(get("/api/sols/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingSol() throws Exception {
        // Get the sol
        restSolMockMvc.perform(get("/api/sols/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSol() throws Exception {
        // Initialize the database
        solRepository.saveAndFlush(sol);

        int databaseSizeBeforeUpdate = solRepository.findAll().size();

        // Update the sol
        Sol updatedSol = solRepository.findById(sol.getId()).get();
        // Disconnect from session so that the updates on updatedSol are not directly saved in db
        em.detach(updatedSol);
        updatedSol
            .acidite(UPDATED_ACIDITE)
            .type(UPDATED_TYPE);
        SolDTO solDTO = solMapper.toDto(updatedSol);

        restSolMockMvc.perform(put("/api/sols").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(solDTO)))
            .andExpect(status().isOk());

        // Validate the Sol in the database
        List<Sol> solList = solRepository.findAll();
        assertThat(solList).hasSize(databaseSizeBeforeUpdate);
        Sol testSol = solList.get(solList.size() - 1);
        assertThat(testSol.getAcidite()).isEqualTo(UPDATED_ACIDITE);
        assertThat(testSol.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingSol() throws Exception {
        int databaseSizeBeforeUpdate = solRepository.findAll().size();

        // Create the Sol
        SolDTO solDTO = solMapper.toDto(sol);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSolMockMvc.perform(put("/api/sols").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(solDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Sol in the database
        List<Sol> solList = solRepository.findAll();
        assertThat(solList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSol() throws Exception {
        // Initialize the database
        solRepository.saveAndFlush(sol);

        int databaseSizeBeforeDelete = solRepository.findAll().size();

        // Delete the sol
        restSolMockMvc.perform(delete("/api/sols/{id}", sol.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sol> solList = solRepository.findAll();
        assertThat(solList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
