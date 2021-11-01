package fr.syncrase.perma.web.rest;

import fr.syncrase.perma.MicroserviceApp;
import fr.syncrase.perma.config.TestSecurityConfiguration;
import fr.syncrase.perma.domain.Ressemblance;
import fr.syncrase.perma.domain.Plante;
import fr.syncrase.perma.repository.RessemblanceRepository;
import fr.syncrase.perma.service.RessemblanceService;
import fr.syncrase.perma.service.dto.RessemblanceDTO;
import fr.syncrase.perma.service.mapper.RessemblanceMapper;
import fr.syncrase.perma.service.dto.RessemblanceCriteria;
import fr.syncrase.perma.service.RessemblanceQueryService;

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
 * Integration tests for the {@link RessemblanceResource} REST controller.
 */
@SpringBootTest(classes = { MicroserviceApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class RessemblanceResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private RessemblanceRepository ressemblanceRepository;

    @Autowired
    private RessemblanceMapper ressemblanceMapper;

    @Autowired
    private RessemblanceService ressemblanceService;

    @Autowired
    private RessemblanceQueryService ressemblanceQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRessemblanceMockMvc;

    private Ressemblance ressemblance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ressemblance createEntity(EntityManager em) {
        Ressemblance ressemblance = new Ressemblance()
            .description(DEFAULT_DESCRIPTION);
        return ressemblance;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ressemblance createUpdatedEntity(EntityManager em) {
        Ressemblance ressemblance = new Ressemblance()
            .description(UPDATED_DESCRIPTION);
        return ressemblance;
    }

    @BeforeEach
    public void initTest() {
        ressemblance = createEntity(em);
    }

    @Test
    @Transactional
    public void createRessemblance() throws Exception {
        int databaseSizeBeforeCreate = ressemblanceRepository.findAll().size();
        // Create the Ressemblance
        RessemblanceDTO ressemblanceDTO = ressemblanceMapper.toDto(ressemblance);
        restRessemblanceMockMvc.perform(post("/api/ressemblances").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ressemblanceDTO)))
            .andExpect(status().isCreated());

        // Validate the Ressemblance in the database
        List<Ressemblance> ressemblanceList = ressemblanceRepository.findAll();
        assertThat(ressemblanceList).hasSize(databaseSizeBeforeCreate + 1);
        Ressemblance testRessemblance = ressemblanceList.get(ressemblanceList.size() - 1);
        assertThat(testRessemblance.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createRessemblanceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ressemblanceRepository.findAll().size();

        // Create the Ressemblance with an existing ID
        ressemblance.setId(1L);
        RessemblanceDTO ressemblanceDTO = ressemblanceMapper.toDto(ressemblance);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRessemblanceMockMvc.perform(post("/api/ressemblances").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ressemblanceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ressemblance in the database
        List<Ressemblance> ressemblanceList = ressemblanceRepository.findAll();
        assertThat(ressemblanceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllRessemblances() throws Exception {
        // Initialize the database
        ressemblanceRepository.saveAndFlush(ressemblance);

        // Get all the ressemblanceList
        restRessemblanceMockMvc.perform(get("/api/ressemblances?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ressemblance.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getRessemblance() throws Exception {
        // Initialize the database
        ressemblanceRepository.saveAndFlush(ressemblance);

        // Get the ressemblance
        restRessemblanceMockMvc.perform(get("/api/ressemblances/{id}", ressemblance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ressemblance.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }


    @Test
    @Transactional
    public void getRessemblancesByIdFiltering() throws Exception {
        // Initialize the database
        ressemblanceRepository.saveAndFlush(ressemblance);

        Long id = ressemblance.getId();

        defaultRessemblanceShouldBeFound("id.equals=" + id);
        defaultRessemblanceShouldNotBeFound("id.notEquals=" + id);

        defaultRessemblanceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRessemblanceShouldNotBeFound("id.greaterThan=" + id);

        defaultRessemblanceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRessemblanceShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllRessemblancesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        ressemblanceRepository.saveAndFlush(ressemblance);

        // Get all the ressemblanceList where description equals to DEFAULT_DESCRIPTION
        defaultRessemblanceShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the ressemblanceList where description equals to UPDATED_DESCRIPTION
        defaultRessemblanceShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllRessemblancesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ressemblanceRepository.saveAndFlush(ressemblance);

        // Get all the ressemblanceList where description not equals to DEFAULT_DESCRIPTION
        defaultRessemblanceShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the ressemblanceList where description not equals to UPDATED_DESCRIPTION
        defaultRessemblanceShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllRessemblancesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        ressemblanceRepository.saveAndFlush(ressemblance);

        // Get all the ressemblanceList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultRessemblanceShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the ressemblanceList where description equals to UPDATED_DESCRIPTION
        defaultRessemblanceShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllRessemblancesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        ressemblanceRepository.saveAndFlush(ressemblance);

        // Get all the ressemblanceList where description is not null
        defaultRessemblanceShouldBeFound("description.specified=true");

        // Get all the ressemblanceList where description is null
        defaultRessemblanceShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllRessemblancesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        ressemblanceRepository.saveAndFlush(ressemblance);

        // Get all the ressemblanceList where description contains DEFAULT_DESCRIPTION
        defaultRessemblanceShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the ressemblanceList where description contains UPDATED_DESCRIPTION
        defaultRessemblanceShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllRessemblancesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        ressemblanceRepository.saveAndFlush(ressemblance);

        // Get all the ressemblanceList where description does not contain DEFAULT_DESCRIPTION
        defaultRessemblanceShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the ressemblanceList where description does not contain UPDATED_DESCRIPTION
        defaultRessemblanceShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllRessemblancesByConfusionIsEqualToSomething() throws Exception {
        // Initialize the database
        ressemblanceRepository.saveAndFlush(ressemblance);
        Plante confusion = PlanteResourceIT.createEntity(em);
        em.persist(confusion);
        em.flush();
        ressemblance.setConfusion(confusion);
        ressemblanceRepository.saveAndFlush(ressemblance);
        Long confusionId = confusion.getId();

        // Get all the ressemblanceList where confusion equals to confusionId
        defaultRessemblanceShouldBeFound("confusionId.equals=" + confusionId);

        // Get all the ressemblanceList where confusion equals to confusionId + 1
        defaultRessemblanceShouldNotBeFound("confusionId.equals=" + (confusionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRessemblanceShouldBeFound(String filter) throws Exception {
        restRessemblanceMockMvc.perform(get("/api/ressemblances?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ressemblance.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restRessemblanceMockMvc.perform(get("/api/ressemblances/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRessemblanceShouldNotBeFound(String filter) throws Exception {
        restRessemblanceMockMvc.perform(get("/api/ressemblances?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRessemblanceMockMvc.perform(get("/api/ressemblances/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingRessemblance() throws Exception {
        // Get the ressemblance
        restRessemblanceMockMvc.perform(get("/api/ressemblances/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRessemblance() throws Exception {
        // Initialize the database
        ressemblanceRepository.saveAndFlush(ressemblance);

        int databaseSizeBeforeUpdate = ressemblanceRepository.findAll().size();

        // Update the ressemblance
        Ressemblance updatedRessemblance = ressemblanceRepository.findById(ressemblance.getId()).get();
        // Disconnect from session so that the updates on updatedRessemblance are not directly saved in db
        em.detach(updatedRessemblance);
        updatedRessemblance
            .description(UPDATED_DESCRIPTION);
        RessemblanceDTO ressemblanceDTO = ressemblanceMapper.toDto(updatedRessemblance);

        restRessemblanceMockMvc.perform(put("/api/ressemblances").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ressemblanceDTO)))
            .andExpect(status().isOk());

        // Validate the Ressemblance in the database
        List<Ressemblance> ressemblanceList = ressemblanceRepository.findAll();
        assertThat(ressemblanceList).hasSize(databaseSizeBeforeUpdate);
        Ressemblance testRessemblance = ressemblanceList.get(ressemblanceList.size() - 1);
        assertThat(testRessemblance.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingRessemblance() throws Exception {
        int databaseSizeBeforeUpdate = ressemblanceRepository.findAll().size();

        // Create the Ressemblance
        RessemblanceDTO ressemblanceDTO = ressemblanceMapper.toDto(ressemblance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRessemblanceMockMvc.perform(put("/api/ressemblances").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(ressemblanceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ressemblance in the database
        List<Ressemblance> ressemblanceList = ressemblanceRepository.findAll();
        assertThat(ressemblanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRessemblance() throws Exception {
        // Initialize the database
        ressemblanceRepository.saveAndFlush(ressemblance);

        int databaseSizeBeforeDelete = ressemblanceRepository.findAll().size();

        // Delete the ressemblance
        restRessemblanceMockMvc.perform(delete("/api/ressemblances/{id}", ressemblance.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ressemblance> ressemblanceList = ressemblanceRepository.findAll();
        assertThat(ressemblanceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
