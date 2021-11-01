package fr.syncrase.perma.web.rest;

import fr.syncrase.perma.MicroserviceApp;
import fr.syncrase.perma.config.TestSecurityConfiguration;
import fr.syncrase.perma.domain.PeriodeAnnee;
import fr.syncrase.perma.domain.Mois;
import fr.syncrase.perma.repository.PeriodeAnneeRepository;
import fr.syncrase.perma.service.PeriodeAnneeService;
import fr.syncrase.perma.service.dto.PeriodeAnneeDTO;
import fr.syncrase.perma.service.mapper.PeriodeAnneeMapper;
import fr.syncrase.perma.service.dto.PeriodeAnneeCriteria;
import fr.syncrase.perma.service.PeriodeAnneeQueryService;

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
 * Integration tests for the {@link PeriodeAnneeResource} REST controller.
 */
@SpringBootTest(classes = { MicroserviceApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class PeriodeAnneeResourceIT {

    @Autowired
    private PeriodeAnneeRepository periodeAnneeRepository;

    @Autowired
    private PeriodeAnneeMapper periodeAnneeMapper;

    @Autowired
    private PeriodeAnneeService periodeAnneeService;

    @Autowired
    private PeriodeAnneeQueryService periodeAnneeQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPeriodeAnneeMockMvc;

    private PeriodeAnnee periodeAnnee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PeriodeAnnee createEntity(EntityManager em) {
        PeriodeAnnee periodeAnnee = new PeriodeAnnee();
        // Add required entity
        Mois mois;
        if (TestUtil.findAll(em, Mois.class).isEmpty()) {
            mois = MoisResourceIT.createEntity(em);
            em.persist(mois);
            em.flush();
        } else {
            mois = TestUtil.findAll(em, Mois.class).get(0);
        }
        periodeAnnee.setDebut(mois);
        // Add required entity
        periodeAnnee.setFin(mois);
        return periodeAnnee;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PeriodeAnnee createUpdatedEntity(EntityManager em) {
        PeriodeAnnee periodeAnnee = new PeriodeAnnee();
        // Add required entity
        Mois mois;
        if (TestUtil.findAll(em, Mois.class).isEmpty()) {
            mois = MoisResourceIT.createUpdatedEntity(em);
            em.persist(mois);
            em.flush();
        } else {
            mois = TestUtil.findAll(em, Mois.class).get(0);
        }
        periodeAnnee.setDebut(mois);
        // Add required entity
        periodeAnnee.setFin(mois);
        return periodeAnnee;
    }

    @BeforeEach
    public void initTest() {
        periodeAnnee = createEntity(em);
    }

    @Test
    @Transactional
    public void createPeriodeAnnee() throws Exception {
        int databaseSizeBeforeCreate = periodeAnneeRepository.findAll().size();
        // Create the PeriodeAnnee
        PeriodeAnneeDTO periodeAnneeDTO = periodeAnneeMapper.toDto(periodeAnnee);
        restPeriodeAnneeMockMvc.perform(post("/api/periode-annees").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(periodeAnneeDTO)))
            .andExpect(status().isCreated());

        // Validate the PeriodeAnnee in the database
        List<PeriodeAnnee> periodeAnneeList = periodeAnneeRepository.findAll();
        assertThat(periodeAnneeList).hasSize(databaseSizeBeforeCreate + 1);
        PeriodeAnnee testPeriodeAnnee = periodeAnneeList.get(periodeAnneeList.size() - 1);
    }

    @Test
    @Transactional
    public void createPeriodeAnneeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = periodeAnneeRepository.findAll().size();

        // Create the PeriodeAnnee with an existing ID
        periodeAnnee.setId(1L);
        PeriodeAnneeDTO periodeAnneeDTO = periodeAnneeMapper.toDto(periodeAnnee);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPeriodeAnneeMockMvc.perform(post("/api/periode-annees").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(periodeAnneeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PeriodeAnnee in the database
        List<PeriodeAnnee> periodeAnneeList = periodeAnneeRepository.findAll();
        assertThat(periodeAnneeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPeriodeAnnees() throws Exception {
        // Initialize the database
        periodeAnneeRepository.saveAndFlush(periodeAnnee);

        // Get all the periodeAnneeList
        restPeriodeAnneeMockMvc.perform(get("/api/periode-annees?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(periodeAnnee.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getPeriodeAnnee() throws Exception {
        // Initialize the database
        periodeAnneeRepository.saveAndFlush(periodeAnnee);

        // Get the periodeAnnee
        restPeriodeAnneeMockMvc.perform(get("/api/periode-annees/{id}", periodeAnnee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(periodeAnnee.getId().intValue()));
    }


    @Test
    @Transactional
    public void getPeriodeAnneesByIdFiltering() throws Exception {
        // Initialize the database
        periodeAnneeRepository.saveAndFlush(periodeAnnee);

        Long id = periodeAnnee.getId();

        defaultPeriodeAnneeShouldBeFound("id.equals=" + id);
        defaultPeriodeAnneeShouldNotBeFound("id.notEquals=" + id);

        defaultPeriodeAnneeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPeriodeAnneeShouldNotBeFound("id.greaterThan=" + id);

        defaultPeriodeAnneeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPeriodeAnneeShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPeriodeAnneesByDebutIsEqualToSomething() throws Exception {
        // Get already existing entity
        Mois debut = periodeAnnee.getDebut();
        periodeAnneeRepository.saveAndFlush(periodeAnnee);
        Long debutId = debut.getId();

        // Get all the periodeAnneeList where debut equals to debutId
        defaultPeriodeAnneeShouldBeFound("debutId.equals=" + debutId);

        // Get all the periodeAnneeList where debut equals to debutId + 1
        defaultPeriodeAnneeShouldNotBeFound("debutId.equals=" + (debutId + 1));
    }


    @Test
    @Transactional
    public void getAllPeriodeAnneesByFinIsEqualToSomething() throws Exception {
        // Get already existing entity
        Mois fin = periodeAnnee.getFin();
        periodeAnneeRepository.saveAndFlush(periodeAnnee);
        Long finId = fin.getId();

        // Get all the periodeAnneeList where fin equals to finId
        defaultPeriodeAnneeShouldBeFound("finId.equals=" + finId);

        // Get all the periodeAnneeList where fin equals to finId + 1
        defaultPeriodeAnneeShouldNotBeFound("finId.equals=" + (finId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPeriodeAnneeShouldBeFound(String filter) throws Exception {
        restPeriodeAnneeMockMvc.perform(get("/api/periode-annees?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(periodeAnnee.getId().intValue())));

        // Check, that the count call also returns 1
        restPeriodeAnneeMockMvc.perform(get("/api/periode-annees/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPeriodeAnneeShouldNotBeFound(String filter) throws Exception {
        restPeriodeAnneeMockMvc.perform(get("/api/periode-annees?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPeriodeAnneeMockMvc.perform(get("/api/periode-annees/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingPeriodeAnnee() throws Exception {
        // Get the periodeAnnee
        restPeriodeAnneeMockMvc.perform(get("/api/periode-annees/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePeriodeAnnee() throws Exception {
        // Initialize the database
        periodeAnneeRepository.saveAndFlush(periodeAnnee);

        int databaseSizeBeforeUpdate = periodeAnneeRepository.findAll().size();

        // Update the periodeAnnee
        PeriodeAnnee updatedPeriodeAnnee = periodeAnneeRepository.findById(periodeAnnee.getId()).get();
        // Disconnect from session so that the updates on updatedPeriodeAnnee are not directly saved in db
        em.detach(updatedPeriodeAnnee);
        PeriodeAnneeDTO periodeAnneeDTO = periodeAnneeMapper.toDto(updatedPeriodeAnnee);

        restPeriodeAnneeMockMvc.perform(put("/api/periode-annees").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(periodeAnneeDTO)))
            .andExpect(status().isOk());

        // Validate the PeriodeAnnee in the database
        List<PeriodeAnnee> periodeAnneeList = periodeAnneeRepository.findAll();
        assertThat(periodeAnneeList).hasSize(databaseSizeBeforeUpdate);
        PeriodeAnnee testPeriodeAnnee = periodeAnneeList.get(periodeAnneeList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingPeriodeAnnee() throws Exception {
        int databaseSizeBeforeUpdate = periodeAnneeRepository.findAll().size();

        // Create the PeriodeAnnee
        PeriodeAnneeDTO periodeAnneeDTO = periodeAnneeMapper.toDto(periodeAnnee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeriodeAnneeMockMvc.perform(put("/api/periode-annees").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(periodeAnneeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PeriodeAnnee in the database
        List<PeriodeAnnee> periodeAnneeList = periodeAnneeRepository.findAll();
        assertThat(periodeAnneeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePeriodeAnnee() throws Exception {
        // Initialize the database
        periodeAnneeRepository.saveAndFlush(periodeAnnee);

        int databaseSizeBeforeDelete = periodeAnneeRepository.findAll().size();

        // Delete the periodeAnnee
        restPeriodeAnneeMockMvc.perform(delete("/api/periode-annees/{id}", periodeAnnee.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PeriodeAnnee> periodeAnneeList = periodeAnneeRepository.findAll();
        assertThat(periodeAnneeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
