package fr.syncrase.perma.web.rest;

import fr.syncrase.perma.MicroserviceApp;
import fr.syncrase.perma.config.TestSecurityConfiguration;
import fr.syncrase.perma.domain.CycleDeVie;
import fr.syncrase.perma.domain.Semis;
import fr.syncrase.perma.domain.PeriodeAnnee;
import fr.syncrase.perma.repository.CycleDeVieRepository;
import fr.syncrase.perma.service.CycleDeVieService;
import fr.syncrase.perma.service.dto.CycleDeVieDTO;
import fr.syncrase.perma.service.mapper.CycleDeVieMapper;
import fr.syncrase.perma.service.dto.CycleDeVieCriteria;
import fr.syncrase.perma.service.CycleDeVieQueryService;

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
 * Integration tests for the {@link CycleDeVieResource} REST controller.
 */
@SpringBootTest(classes = { MicroserviceApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class CycleDeVieResourceIT {

    private static final String DEFAULT_VITESSE_DE_CROISSANCE = "AAAAAAAAAA";
    private static final String UPDATED_VITESSE_DE_CROISSANCE = "BBBBBBBBBB";

    @Autowired
    private CycleDeVieRepository cycleDeVieRepository;

    @Autowired
    private CycleDeVieMapper cycleDeVieMapper;

    @Autowired
    private CycleDeVieService cycleDeVieService;

    @Autowired
    private CycleDeVieQueryService cycleDeVieQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCycleDeVieMockMvc;

    private CycleDeVie cycleDeVie;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CycleDeVie createEntity(EntityManager em) {
        CycleDeVie cycleDeVie = new CycleDeVie()
            .vitesseDeCroissance(DEFAULT_VITESSE_DE_CROISSANCE);
        return cycleDeVie;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CycleDeVie createUpdatedEntity(EntityManager em) {
        CycleDeVie cycleDeVie = new CycleDeVie()
            .vitesseDeCroissance(UPDATED_VITESSE_DE_CROISSANCE);
        return cycleDeVie;
    }

    @BeforeEach
    public void initTest() {
        cycleDeVie = createEntity(em);
    }

    @Test
    @Transactional
    public void createCycleDeVie() throws Exception {
        int databaseSizeBeforeCreate = cycleDeVieRepository.findAll().size();
        // Create the CycleDeVie
        CycleDeVieDTO cycleDeVieDTO = cycleDeVieMapper.toDto(cycleDeVie);
        restCycleDeVieMockMvc.perform(post("/api/cycle-de-vies").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cycleDeVieDTO)))
            .andExpect(status().isCreated());

        // Validate the CycleDeVie in the database
        List<CycleDeVie> cycleDeVieList = cycleDeVieRepository.findAll();
        assertThat(cycleDeVieList).hasSize(databaseSizeBeforeCreate + 1);
        CycleDeVie testCycleDeVie = cycleDeVieList.get(cycleDeVieList.size() - 1);
        assertThat(testCycleDeVie.getVitesseDeCroissance()).isEqualTo(DEFAULT_VITESSE_DE_CROISSANCE);
    }

    @Test
    @Transactional
    public void createCycleDeVieWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cycleDeVieRepository.findAll().size();

        // Create the CycleDeVie with an existing ID
        cycleDeVie.setId(1L);
        CycleDeVieDTO cycleDeVieDTO = cycleDeVieMapper.toDto(cycleDeVie);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCycleDeVieMockMvc.perform(post("/api/cycle-de-vies").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cycleDeVieDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CycleDeVie in the database
        List<CycleDeVie> cycleDeVieList = cycleDeVieRepository.findAll();
        assertThat(cycleDeVieList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCycleDeVies() throws Exception {
        // Initialize the database
        cycleDeVieRepository.saveAndFlush(cycleDeVie);

        // Get all the cycleDeVieList
        restCycleDeVieMockMvc.perform(get("/api/cycle-de-vies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cycleDeVie.getId().intValue())))
            .andExpect(jsonPath("$.[*].vitesseDeCroissance").value(hasItem(DEFAULT_VITESSE_DE_CROISSANCE)));
    }
    
    @Test
    @Transactional
    public void getCycleDeVie() throws Exception {
        // Initialize the database
        cycleDeVieRepository.saveAndFlush(cycleDeVie);

        // Get the cycleDeVie
        restCycleDeVieMockMvc.perform(get("/api/cycle-de-vies/{id}", cycleDeVie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cycleDeVie.getId().intValue()))
            .andExpect(jsonPath("$.vitesseDeCroissance").value(DEFAULT_VITESSE_DE_CROISSANCE));
    }


    @Test
    @Transactional
    public void getCycleDeViesByIdFiltering() throws Exception {
        // Initialize the database
        cycleDeVieRepository.saveAndFlush(cycleDeVie);

        Long id = cycleDeVie.getId();

        defaultCycleDeVieShouldBeFound("id.equals=" + id);
        defaultCycleDeVieShouldNotBeFound("id.notEquals=" + id);

        defaultCycleDeVieShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCycleDeVieShouldNotBeFound("id.greaterThan=" + id);

        defaultCycleDeVieShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCycleDeVieShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCycleDeViesByVitesseDeCroissanceIsEqualToSomething() throws Exception {
        // Initialize the database
        cycleDeVieRepository.saveAndFlush(cycleDeVie);

        // Get all the cycleDeVieList where vitesseDeCroissance equals to DEFAULT_VITESSE_DE_CROISSANCE
        defaultCycleDeVieShouldBeFound("vitesseDeCroissance.equals=" + DEFAULT_VITESSE_DE_CROISSANCE);

        // Get all the cycleDeVieList where vitesseDeCroissance equals to UPDATED_VITESSE_DE_CROISSANCE
        defaultCycleDeVieShouldNotBeFound("vitesseDeCroissance.equals=" + UPDATED_VITESSE_DE_CROISSANCE);
    }

    @Test
    @Transactional
    public void getAllCycleDeViesByVitesseDeCroissanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cycleDeVieRepository.saveAndFlush(cycleDeVie);

        // Get all the cycleDeVieList where vitesseDeCroissance not equals to DEFAULT_VITESSE_DE_CROISSANCE
        defaultCycleDeVieShouldNotBeFound("vitesseDeCroissance.notEquals=" + DEFAULT_VITESSE_DE_CROISSANCE);

        // Get all the cycleDeVieList where vitesseDeCroissance not equals to UPDATED_VITESSE_DE_CROISSANCE
        defaultCycleDeVieShouldBeFound("vitesseDeCroissance.notEquals=" + UPDATED_VITESSE_DE_CROISSANCE);
    }

    @Test
    @Transactional
    public void getAllCycleDeViesByVitesseDeCroissanceIsInShouldWork() throws Exception {
        // Initialize the database
        cycleDeVieRepository.saveAndFlush(cycleDeVie);

        // Get all the cycleDeVieList where vitesseDeCroissance in DEFAULT_VITESSE_DE_CROISSANCE or UPDATED_VITESSE_DE_CROISSANCE
        defaultCycleDeVieShouldBeFound("vitesseDeCroissance.in=" + DEFAULT_VITESSE_DE_CROISSANCE + "," + UPDATED_VITESSE_DE_CROISSANCE);

        // Get all the cycleDeVieList where vitesseDeCroissance equals to UPDATED_VITESSE_DE_CROISSANCE
        defaultCycleDeVieShouldNotBeFound("vitesseDeCroissance.in=" + UPDATED_VITESSE_DE_CROISSANCE);
    }

    @Test
    @Transactional
    public void getAllCycleDeViesByVitesseDeCroissanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        cycleDeVieRepository.saveAndFlush(cycleDeVie);

        // Get all the cycleDeVieList where vitesseDeCroissance is not null
        defaultCycleDeVieShouldBeFound("vitesseDeCroissance.specified=true");

        // Get all the cycleDeVieList where vitesseDeCroissance is null
        defaultCycleDeVieShouldNotBeFound("vitesseDeCroissance.specified=false");
    }
                @Test
    @Transactional
    public void getAllCycleDeViesByVitesseDeCroissanceContainsSomething() throws Exception {
        // Initialize the database
        cycleDeVieRepository.saveAndFlush(cycleDeVie);

        // Get all the cycleDeVieList where vitesseDeCroissance contains DEFAULT_VITESSE_DE_CROISSANCE
        defaultCycleDeVieShouldBeFound("vitesseDeCroissance.contains=" + DEFAULT_VITESSE_DE_CROISSANCE);

        // Get all the cycleDeVieList where vitesseDeCroissance contains UPDATED_VITESSE_DE_CROISSANCE
        defaultCycleDeVieShouldNotBeFound("vitesseDeCroissance.contains=" + UPDATED_VITESSE_DE_CROISSANCE);
    }

    @Test
    @Transactional
    public void getAllCycleDeViesByVitesseDeCroissanceNotContainsSomething() throws Exception {
        // Initialize the database
        cycleDeVieRepository.saveAndFlush(cycleDeVie);

        // Get all the cycleDeVieList where vitesseDeCroissance does not contain DEFAULT_VITESSE_DE_CROISSANCE
        defaultCycleDeVieShouldNotBeFound("vitesseDeCroissance.doesNotContain=" + DEFAULT_VITESSE_DE_CROISSANCE);

        // Get all the cycleDeVieList where vitesseDeCroissance does not contain UPDATED_VITESSE_DE_CROISSANCE
        defaultCycleDeVieShouldBeFound("vitesseDeCroissance.doesNotContain=" + UPDATED_VITESSE_DE_CROISSANCE);
    }


    @Test
    @Transactional
    public void getAllCycleDeViesBySemisIsEqualToSomething() throws Exception {
        // Initialize the database
        cycleDeVieRepository.saveAndFlush(cycleDeVie);
        Semis semis = SemisResourceIT.createEntity(em);
        em.persist(semis);
        em.flush();
        cycleDeVie.setSemis(semis);
        cycleDeVieRepository.saveAndFlush(cycleDeVie);
        Long semisId = semis.getId();

        // Get all the cycleDeVieList where semis equals to semisId
        defaultCycleDeVieShouldBeFound("semisId.equals=" + semisId);

        // Get all the cycleDeVieList where semis equals to semisId + 1
        defaultCycleDeVieShouldNotBeFound("semisId.equals=" + (semisId + 1));
    }


    @Test
    @Transactional
    public void getAllCycleDeViesByApparitionFeuillesIsEqualToSomething() throws Exception {
        // Initialize the database
        cycleDeVieRepository.saveAndFlush(cycleDeVie);
        PeriodeAnnee apparitionFeuilles = PeriodeAnneeResourceIT.createEntity(em);
        em.persist(apparitionFeuilles);
        em.flush();
        cycleDeVie.setApparitionFeuilles(apparitionFeuilles);
        cycleDeVieRepository.saveAndFlush(cycleDeVie);
        Long apparitionFeuillesId = apparitionFeuilles.getId();

        // Get all the cycleDeVieList where apparitionFeuilles equals to apparitionFeuillesId
        defaultCycleDeVieShouldBeFound("apparitionFeuillesId.equals=" + apparitionFeuillesId);

        // Get all the cycleDeVieList where apparitionFeuilles equals to apparitionFeuillesId + 1
        defaultCycleDeVieShouldNotBeFound("apparitionFeuillesId.equals=" + (apparitionFeuillesId + 1));
    }


    @Test
    @Transactional
    public void getAllCycleDeViesByFloraisonIsEqualToSomething() throws Exception {
        // Initialize the database
        cycleDeVieRepository.saveAndFlush(cycleDeVie);
        PeriodeAnnee floraison = PeriodeAnneeResourceIT.createEntity(em);
        em.persist(floraison);
        em.flush();
        cycleDeVie.setFloraison(floraison);
        cycleDeVieRepository.saveAndFlush(cycleDeVie);
        Long floraisonId = floraison.getId();

        // Get all the cycleDeVieList where floraison equals to floraisonId
        defaultCycleDeVieShouldBeFound("floraisonId.equals=" + floraisonId);

        // Get all the cycleDeVieList where floraison equals to floraisonId + 1
        defaultCycleDeVieShouldNotBeFound("floraisonId.equals=" + (floraisonId + 1));
    }


    @Test
    @Transactional
    public void getAllCycleDeViesByRecolteIsEqualToSomething() throws Exception {
        // Initialize the database
        cycleDeVieRepository.saveAndFlush(cycleDeVie);
        PeriodeAnnee recolte = PeriodeAnneeResourceIT.createEntity(em);
        em.persist(recolte);
        em.flush();
        cycleDeVie.setRecolte(recolte);
        cycleDeVieRepository.saveAndFlush(cycleDeVie);
        Long recolteId = recolte.getId();

        // Get all the cycleDeVieList where recolte equals to recolteId
        defaultCycleDeVieShouldBeFound("recolteId.equals=" + recolteId);

        // Get all the cycleDeVieList where recolte equals to recolteId + 1
        defaultCycleDeVieShouldNotBeFound("recolteId.equals=" + (recolteId + 1));
    }


    @Test
    @Transactional
    public void getAllCycleDeViesByCroissanceIsEqualToSomething() throws Exception {
        // Initialize the database
        cycleDeVieRepository.saveAndFlush(cycleDeVie);
        PeriodeAnnee croissance = PeriodeAnneeResourceIT.createEntity(em);
        em.persist(croissance);
        em.flush();
        cycleDeVie.setCroissance(croissance);
        cycleDeVieRepository.saveAndFlush(cycleDeVie);
        Long croissanceId = croissance.getId();

        // Get all the cycleDeVieList where croissance equals to croissanceId
        defaultCycleDeVieShouldBeFound("croissanceId.equals=" + croissanceId);

        // Get all the cycleDeVieList where croissance equals to croissanceId + 1
        defaultCycleDeVieShouldNotBeFound("croissanceId.equals=" + (croissanceId + 1));
    }


    @Test
    @Transactional
    public void getAllCycleDeViesByMaturiteIsEqualToSomething() throws Exception {
        // Initialize the database
        cycleDeVieRepository.saveAndFlush(cycleDeVie);
        PeriodeAnnee maturite = PeriodeAnneeResourceIT.createEntity(em);
        em.persist(maturite);
        em.flush();
        cycleDeVie.setMaturite(maturite);
        cycleDeVieRepository.saveAndFlush(cycleDeVie);
        Long maturiteId = maturite.getId();

        // Get all the cycleDeVieList where maturite equals to maturiteId
        defaultCycleDeVieShouldBeFound("maturiteId.equals=" + maturiteId);

        // Get all the cycleDeVieList where maturite equals to maturiteId + 1
        defaultCycleDeVieShouldNotBeFound("maturiteId.equals=" + (maturiteId + 1));
    }


    @Test
    @Transactional
    public void getAllCycleDeViesByPlantationIsEqualToSomething() throws Exception {
        // Initialize the database
        cycleDeVieRepository.saveAndFlush(cycleDeVie);
        PeriodeAnnee plantation = PeriodeAnneeResourceIT.createEntity(em);
        em.persist(plantation);
        em.flush();
        cycleDeVie.setPlantation(plantation);
        cycleDeVieRepository.saveAndFlush(cycleDeVie);
        Long plantationId = plantation.getId();

        // Get all the cycleDeVieList where plantation equals to plantationId
        defaultCycleDeVieShouldBeFound("plantationId.equals=" + plantationId);

        // Get all the cycleDeVieList where plantation equals to plantationId + 1
        defaultCycleDeVieShouldNotBeFound("plantationId.equals=" + (plantationId + 1));
    }


    @Test
    @Transactional
    public void getAllCycleDeViesByRempotageIsEqualToSomething() throws Exception {
        // Initialize the database
        cycleDeVieRepository.saveAndFlush(cycleDeVie);
        PeriodeAnnee rempotage = PeriodeAnneeResourceIT.createEntity(em);
        em.persist(rempotage);
        em.flush();
        cycleDeVie.setRempotage(rempotage);
        cycleDeVieRepository.saveAndFlush(cycleDeVie);
        Long rempotageId = rempotage.getId();

        // Get all the cycleDeVieList where rempotage equals to rempotageId
        defaultCycleDeVieShouldBeFound("rempotageId.equals=" + rempotageId);

        // Get all the cycleDeVieList where rempotage equals to rempotageId + 1
        defaultCycleDeVieShouldNotBeFound("rempotageId.equals=" + (rempotageId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCycleDeVieShouldBeFound(String filter) throws Exception {
        restCycleDeVieMockMvc.perform(get("/api/cycle-de-vies?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cycleDeVie.getId().intValue())))
            .andExpect(jsonPath("$.[*].vitesseDeCroissance").value(hasItem(DEFAULT_VITESSE_DE_CROISSANCE)));

        // Check, that the count call also returns 1
        restCycleDeVieMockMvc.perform(get("/api/cycle-de-vies/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCycleDeVieShouldNotBeFound(String filter) throws Exception {
        restCycleDeVieMockMvc.perform(get("/api/cycle-de-vies?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCycleDeVieMockMvc.perform(get("/api/cycle-de-vies/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingCycleDeVie() throws Exception {
        // Get the cycleDeVie
        restCycleDeVieMockMvc.perform(get("/api/cycle-de-vies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCycleDeVie() throws Exception {
        // Initialize the database
        cycleDeVieRepository.saveAndFlush(cycleDeVie);

        int databaseSizeBeforeUpdate = cycleDeVieRepository.findAll().size();

        // Update the cycleDeVie
        CycleDeVie updatedCycleDeVie = cycleDeVieRepository.findById(cycleDeVie.getId()).get();
        // Disconnect from session so that the updates on updatedCycleDeVie are not directly saved in db
        em.detach(updatedCycleDeVie);
        updatedCycleDeVie
            .vitesseDeCroissance(UPDATED_VITESSE_DE_CROISSANCE);
        CycleDeVieDTO cycleDeVieDTO = cycleDeVieMapper.toDto(updatedCycleDeVie);

        restCycleDeVieMockMvc.perform(put("/api/cycle-de-vies").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cycleDeVieDTO)))
            .andExpect(status().isOk());

        // Validate the CycleDeVie in the database
        List<CycleDeVie> cycleDeVieList = cycleDeVieRepository.findAll();
        assertThat(cycleDeVieList).hasSize(databaseSizeBeforeUpdate);
        CycleDeVie testCycleDeVie = cycleDeVieList.get(cycleDeVieList.size() - 1);
        assertThat(testCycleDeVie.getVitesseDeCroissance()).isEqualTo(UPDATED_VITESSE_DE_CROISSANCE);
    }

    @Test
    @Transactional
    public void updateNonExistingCycleDeVie() throws Exception {
        int databaseSizeBeforeUpdate = cycleDeVieRepository.findAll().size();

        // Create the CycleDeVie
        CycleDeVieDTO cycleDeVieDTO = cycleDeVieMapper.toDto(cycleDeVie);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCycleDeVieMockMvc.perform(put("/api/cycle-de-vies").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cycleDeVieDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CycleDeVie in the database
        List<CycleDeVie> cycleDeVieList = cycleDeVieRepository.findAll();
        assertThat(cycleDeVieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCycleDeVie() throws Exception {
        // Initialize the database
        cycleDeVieRepository.saveAndFlush(cycleDeVie);

        int databaseSizeBeforeDelete = cycleDeVieRepository.findAll().size();

        // Delete the cycleDeVie
        restCycleDeVieMockMvc.perform(delete("/api/cycle-de-vies/{id}", cycleDeVie.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CycleDeVie> cycleDeVieList = cycleDeVieRepository.findAll();
        assertThat(cycleDeVieList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
