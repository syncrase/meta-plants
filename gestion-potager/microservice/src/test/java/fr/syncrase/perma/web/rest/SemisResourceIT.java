package fr.syncrase.perma.web.rest;

import fr.syncrase.perma.MicroserviceApp;
import fr.syncrase.perma.config.TestSecurityConfiguration;
import fr.syncrase.perma.domain.Semis;
import fr.syncrase.perma.domain.PeriodeAnnee;
import fr.syncrase.perma.domain.TypeSemis;
import fr.syncrase.perma.domain.Germination;
import fr.syncrase.perma.repository.SemisRepository;
import fr.syncrase.perma.service.SemisService;
import fr.syncrase.perma.service.dto.SemisDTO;
import fr.syncrase.perma.service.mapper.SemisMapper;
import fr.syncrase.perma.service.dto.SemisCriteria;
import fr.syncrase.perma.service.SemisQueryService;

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
 * Integration tests for the {@link SemisResource} REST controller.
 */
@SpringBootTest(classes = { MicroserviceApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class SemisResourceIT {

    @Autowired
    private SemisRepository semisRepository;

    @Autowired
    private SemisMapper semisMapper;

    @Autowired
    private SemisService semisService;

    @Autowired
    private SemisQueryService semisQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSemisMockMvc;

    private Semis semis;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Semis createEntity(EntityManager em) {
        Semis semis = new Semis();
        return semis;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Semis createUpdatedEntity(EntityManager em) {
        Semis semis = new Semis();
        return semis;
    }

    @BeforeEach
    public void initTest() {
        semis = createEntity(em);
    }

    @Test
    @Transactional
    public void createSemis() throws Exception {
        int databaseSizeBeforeCreate = semisRepository.findAll().size();
        // Create the Semis
        SemisDTO semisDTO = semisMapper.toDto(semis);
        restSemisMockMvc.perform(post("/api/semis").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(semisDTO)))
            .andExpect(status().isCreated());

        // Validate the Semis in the database
        List<Semis> semisList = semisRepository.findAll();
        assertThat(semisList).hasSize(databaseSizeBeforeCreate + 1);
        Semis testSemis = semisList.get(semisList.size() - 1);
    }

    @Test
    @Transactional
    public void createSemisWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = semisRepository.findAll().size();

        // Create the Semis with an existing ID
        semis.setId(1L);
        SemisDTO semisDTO = semisMapper.toDto(semis);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSemisMockMvc.perform(post("/api/semis").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(semisDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Semis in the database
        List<Semis> semisList = semisRepository.findAll();
        assertThat(semisList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllSemis() throws Exception {
        // Initialize the database
        semisRepository.saveAndFlush(semis);

        // Get all the semisList
        restSemisMockMvc.perform(get("/api/semis?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(semis.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getSemis() throws Exception {
        // Initialize the database
        semisRepository.saveAndFlush(semis);

        // Get the semis
        restSemisMockMvc.perform(get("/api/semis/{id}", semis.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(semis.getId().intValue()));
    }


    @Test
    @Transactional
    public void getSemisByIdFiltering() throws Exception {
        // Initialize the database
        semisRepository.saveAndFlush(semis);

        Long id = semis.getId();

        defaultSemisShouldBeFound("id.equals=" + id);
        defaultSemisShouldNotBeFound("id.notEquals=" + id);

        defaultSemisShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSemisShouldNotBeFound("id.greaterThan=" + id);

        defaultSemisShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSemisShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllSemisBySemisPleineTerreIsEqualToSomething() throws Exception {
        // Initialize the database
        semisRepository.saveAndFlush(semis);
        PeriodeAnnee semisPleineTerre = PeriodeAnneeResourceIT.createEntity(em);
        em.persist(semisPleineTerre);
        em.flush();
        semis.setSemisPleineTerre(semisPleineTerre);
        semisRepository.saveAndFlush(semis);
        Long semisPleineTerreId = semisPleineTerre.getId();

        // Get all the semisList where semisPleineTerre equals to semisPleineTerreId
        defaultSemisShouldBeFound("semisPleineTerreId.equals=" + semisPleineTerreId);

        // Get all the semisList where semisPleineTerre equals to semisPleineTerreId + 1
        defaultSemisShouldNotBeFound("semisPleineTerreId.equals=" + (semisPleineTerreId + 1));
    }


    @Test
    @Transactional
    public void getAllSemisBySemisSousAbrisIsEqualToSomething() throws Exception {
        // Initialize the database
        semisRepository.saveAndFlush(semis);
        PeriodeAnnee semisSousAbris = PeriodeAnneeResourceIT.createEntity(em);
        em.persist(semisSousAbris);
        em.flush();
        semis.setSemisSousAbris(semisSousAbris);
        semisRepository.saveAndFlush(semis);
        Long semisSousAbrisId = semisSousAbris.getId();

        // Get all the semisList where semisSousAbris equals to semisSousAbrisId
        defaultSemisShouldBeFound("semisSousAbrisId.equals=" + semisSousAbrisId);

        // Get all the semisList where semisSousAbris equals to semisSousAbrisId + 1
        defaultSemisShouldNotBeFound("semisSousAbrisId.equals=" + (semisSousAbrisId + 1));
    }


    @Test
    @Transactional
    public void getAllSemisByTypeSemisIsEqualToSomething() throws Exception {
        // Initialize the database
        semisRepository.saveAndFlush(semis);
        TypeSemis typeSemis = TypeSemisResourceIT.createEntity(em);
        em.persist(typeSemis);
        em.flush();
        semis.setTypeSemis(typeSemis);
        semisRepository.saveAndFlush(semis);
        Long typeSemisId = typeSemis.getId();

        // Get all the semisList where typeSemis equals to typeSemisId
        defaultSemisShouldBeFound("typeSemisId.equals=" + typeSemisId);

        // Get all the semisList where typeSemis equals to typeSemisId + 1
        defaultSemisShouldNotBeFound("typeSemisId.equals=" + (typeSemisId + 1));
    }


    @Test
    @Transactional
    public void getAllSemisByGerminationIsEqualToSomething() throws Exception {
        // Initialize the database
        semisRepository.saveAndFlush(semis);
        Germination germination = GerminationResourceIT.createEntity(em);
        em.persist(germination);
        em.flush();
        semis.setGermination(germination);
        semisRepository.saveAndFlush(semis);
        Long germinationId = germination.getId();

        // Get all the semisList where germination equals to germinationId
        defaultSemisShouldBeFound("germinationId.equals=" + germinationId);

        // Get all the semisList where germination equals to germinationId + 1
        defaultSemisShouldNotBeFound("germinationId.equals=" + (germinationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSemisShouldBeFound(String filter) throws Exception {
        restSemisMockMvc.perform(get("/api/semis?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(semis.getId().intValue())));

        // Check, that the count call also returns 1
        restSemisMockMvc.perform(get("/api/semis/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSemisShouldNotBeFound(String filter) throws Exception {
        restSemisMockMvc.perform(get("/api/semis?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSemisMockMvc.perform(get("/api/semis/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingSemis() throws Exception {
        // Get the semis
        restSemisMockMvc.perform(get("/api/semis/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSemis() throws Exception {
        // Initialize the database
        semisRepository.saveAndFlush(semis);

        int databaseSizeBeforeUpdate = semisRepository.findAll().size();

        // Update the semis
        Semis updatedSemis = semisRepository.findById(semis.getId()).get();
        // Disconnect from session so that the updates on updatedSemis are not directly saved in db
        em.detach(updatedSemis);
        SemisDTO semisDTO = semisMapper.toDto(updatedSemis);

        restSemisMockMvc.perform(put("/api/semis").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(semisDTO)))
            .andExpect(status().isOk());

        // Validate the Semis in the database
        List<Semis> semisList = semisRepository.findAll();
        assertThat(semisList).hasSize(databaseSizeBeforeUpdate);
        Semis testSemis = semisList.get(semisList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingSemis() throws Exception {
        int databaseSizeBeforeUpdate = semisRepository.findAll().size();

        // Create the Semis
        SemisDTO semisDTO = semisMapper.toDto(semis);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSemisMockMvc.perform(put("/api/semis").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(semisDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Semis in the database
        List<Semis> semisList = semisRepository.findAll();
        assertThat(semisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSemis() throws Exception {
        // Initialize the database
        semisRepository.saveAndFlush(semis);

        int databaseSizeBeforeDelete = semisRepository.findAll().size();

        // Delete the semis
        restSemisMockMvc.perform(delete("/api/semis/{id}", semis.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Semis> semisList = semisRepository.findAll();
        assertThat(semisList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
