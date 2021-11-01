package fr.syncrase.perma.web.rest;

import fr.syncrase.perma.MicroserviceApp;
import fr.syncrase.perma.config.TestSecurityConfiguration;
import fr.syncrase.perma.domain.APGI;
import fr.syncrase.perma.repository.APGIRepository;
import fr.syncrase.perma.service.APGIService;
import fr.syncrase.perma.service.dto.APGIDTO;
import fr.syncrase.perma.service.mapper.APGIMapper;
import fr.syncrase.perma.service.dto.APGICriteria;
import fr.syncrase.perma.service.APGIQueryService;

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
 * Integration tests for the {@link APGIResource} REST controller.
 */
@SpringBootTest(classes = { MicroserviceApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class APGIResourceIT {

    private static final String DEFAULT_ORDRE = "AAAAAAAAAA";
    private static final String UPDATED_ORDRE = "BBBBBBBBBB";

    private static final String DEFAULT_FAMILLE = "AAAAAAAAAA";
    private static final String UPDATED_FAMILLE = "BBBBBBBBBB";

    @Autowired
    private APGIRepository aPGIRepository;

    @Autowired
    private APGIMapper aPGIMapper;

    @Autowired
    private APGIService aPGIService;

    @Autowired
    private APGIQueryService aPGIQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAPGIMockMvc;

    private APGI aPGI;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static APGI createEntity(EntityManager em) {
        APGI aPGI = new APGI()
            .ordre(DEFAULT_ORDRE)
            .famille(DEFAULT_FAMILLE);
        return aPGI;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static APGI createUpdatedEntity(EntityManager em) {
        APGI aPGI = new APGI()
            .ordre(UPDATED_ORDRE)
            .famille(UPDATED_FAMILLE);
        return aPGI;
    }

    @BeforeEach
    public void initTest() {
        aPGI = createEntity(em);
    }

    @Test
    @Transactional
    public void createAPGI() throws Exception {
        int databaseSizeBeforeCreate = aPGIRepository.findAll().size();
        // Create the APGI
        APGIDTO aPGIDTO = aPGIMapper.toDto(aPGI);
        restAPGIMockMvc.perform(post("/api/apgis").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(aPGIDTO)))
            .andExpect(status().isCreated());

        // Validate the APGI in the database
        List<APGI> aPGIList = aPGIRepository.findAll();
        assertThat(aPGIList).hasSize(databaseSizeBeforeCreate + 1);
        APGI testAPGI = aPGIList.get(aPGIList.size() - 1);
        assertThat(testAPGI.getOrdre()).isEqualTo(DEFAULT_ORDRE);
        assertThat(testAPGI.getFamille()).isEqualTo(DEFAULT_FAMILLE);
    }

    @Test
    @Transactional
    public void createAPGIWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = aPGIRepository.findAll().size();

        // Create the APGI with an existing ID
        aPGI.setId(1L);
        APGIDTO aPGIDTO = aPGIMapper.toDto(aPGI);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAPGIMockMvc.perform(post("/api/apgis").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(aPGIDTO)))
            .andExpect(status().isBadRequest());

        // Validate the APGI in the database
        List<APGI> aPGIList = aPGIRepository.findAll();
        assertThat(aPGIList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkOrdreIsRequired() throws Exception {
        int databaseSizeBeforeTest = aPGIRepository.findAll().size();
        // set the field null
        aPGI.setOrdre(null);

        // Create the APGI, which fails.
        APGIDTO aPGIDTO = aPGIMapper.toDto(aPGI);


        restAPGIMockMvc.perform(post("/api/apgis").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(aPGIDTO)))
            .andExpect(status().isBadRequest());

        List<APGI> aPGIList = aPGIRepository.findAll();
        assertThat(aPGIList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFamilleIsRequired() throws Exception {
        int databaseSizeBeforeTest = aPGIRepository.findAll().size();
        // set the field null
        aPGI.setFamille(null);

        // Create the APGI, which fails.
        APGIDTO aPGIDTO = aPGIMapper.toDto(aPGI);


        restAPGIMockMvc.perform(post("/api/apgis").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(aPGIDTO)))
            .andExpect(status().isBadRequest());

        List<APGI> aPGIList = aPGIRepository.findAll();
        assertThat(aPGIList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAPGIS() throws Exception {
        // Initialize the database
        aPGIRepository.saveAndFlush(aPGI);

        // Get all the aPGIList
        restAPGIMockMvc.perform(get("/api/apgis?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aPGI.getId().intValue())))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)))
            .andExpect(jsonPath("$.[*].famille").value(hasItem(DEFAULT_FAMILLE)));
    }
    
    @Test
    @Transactional
    public void getAPGI() throws Exception {
        // Initialize the database
        aPGIRepository.saveAndFlush(aPGI);

        // Get the aPGI
        restAPGIMockMvc.perform(get("/api/apgis/{id}", aPGI.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aPGI.getId().intValue()))
            .andExpect(jsonPath("$.ordre").value(DEFAULT_ORDRE))
            .andExpect(jsonPath("$.famille").value(DEFAULT_FAMILLE));
    }


    @Test
    @Transactional
    public void getAPGISByIdFiltering() throws Exception {
        // Initialize the database
        aPGIRepository.saveAndFlush(aPGI);

        Long id = aPGI.getId();

        defaultAPGIShouldBeFound("id.equals=" + id);
        defaultAPGIShouldNotBeFound("id.notEquals=" + id);

        defaultAPGIShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAPGIShouldNotBeFound("id.greaterThan=" + id);

        defaultAPGIShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAPGIShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAPGISByOrdreIsEqualToSomething() throws Exception {
        // Initialize the database
        aPGIRepository.saveAndFlush(aPGI);

        // Get all the aPGIList where ordre equals to DEFAULT_ORDRE
        defaultAPGIShouldBeFound("ordre.equals=" + DEFAULT_ORDRE);

        // Get all the aPGIList where ordre equals to UPDATED_ORDRE
        defaultAPGIShouldNotBeFound("ordre.equals=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    public void getAllAPGISByOrdreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        aPGIRepository.saveAndFlush(aPGI);

        // Get all the aPGIList where ordre not equals to DEFAULT_ORDRE
        defaultAPGIShouldNotBeFound("ordre.notEquals=" + DEFAULT_ORDRE);

        // Get all the aPGIList where ordre not equals to UPDATED_ORDRE
        defaultAPGIShouldBeFound("ordre.notEquals=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    public void getAllAPGISByOrdreIsInShouldWork() throws Exception {
        // Initialize the database
        aPGIRepository.saveAndFlush(aPGI);

        // Get all the aPGIList where ordre in DEFAULT_ORDRE or UPDATED_ORDRE
        defaultAPGIShouldBeFound("ordre.in=" + DEFAULT_ORDRE + "," + UPDATED_ORDRE);

        // Get all the aPGIList where ordre equals to UPDATED_ORDRE
        defaultAPGIShouldNotBeFound("ordre.in=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    public void getAllAPGISByOrdreIsNullOrNotNull() throws Exception {
        // Initialize the database
        aPGIRepository.saveAndFlush(aPGI);

        // Get all the aPGIList where ordre is not null
        defaultAPGIShouldBeFound("ordre.specified=true");

        // Get all the aPGIList where ordre is null
        defaultAPGIShouldNotBeFound("ordre.specified=false");
    }
                @Test
    @Transactional
    public void getAllAPGISByOrdreContainsSomething() throws Exception {
        // Initialize the database
        aPGIRepository.saveAndFlush(aPGI);

        // Get all the aPGIList where ordre contains DEFAULT_ORDRE
        defaultAPGIShouldBeFound("ordre.contains=" + DEFAULT_ORDRE);

        // Get all the aPGIList where ordre contains UPDATED_ORDRE
        defaultAPGIShouldNotBeFound("ordre.contains=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    public void getAllAPGISByOrdreNotContainsSomething() throws Exception {
        // Initialize the database
        aPGIRepository.saveAndFlush(aPGI);

        // Get all the aPGIList where ordre does not contain DEFAULT_ORDRE
        defaultAPGIShouldNotBeFound("ordre.doesNotContain=" + DEFAULT_ORDRE);

        // Get all the aPGIList where ordre does not contain UPDATED_ORDRE
        defaultAPGIShouldBeFound("ordre.doesNotContain=" + UPDATED_ORDRE);
    }


    @Test
    @Transactional
    public void getAllAPGISByFamilleIsEqualToSomething() throws Exception {
        // Initialize the database
        aPGIRepository.saveAndFlush(aPGI);

        // Get all the aPGIList where famille equals to DEFAULT_FAMILLE
        defaultAPGIShouldBeFound("famille.equals=" + DEFAULT_FAMILLE);

        // Get all the aPGIList where famille equals to UPDATED_FAMILLE
        defaultAPGIShouldNotBeFound("famille.equals=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    public void getAllAPGISByFamilleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        aPGIRepository.saveAndFlush(aPGI);

        // Get all the aPGIList where famille not equals to DEFAULT_FAMILLE
        defaultAPGIShouldNotBeFound("famille.notEquals=" + DEFAULT_FAMILLE);

        // Get all the aPGIList where famille not equals to UPDATED_FAMILLE
        defaultAPGIShouldBeFound("famille.notEquals=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    public void getAllAPGISByFamilleIsInShouldWork() throws Exception {
        // Initialize the database
        aPGIRepository.saveAndFlush(aPGI);

        // Get all the aPGIList where famille in DEFAULT_FAMILLE or UPDATED_FAMILLE
        defaultAPGIShouldBeFound("famille.in=" + DEFAULT_FAMILLE + "," + UPDATED_FAMILLE);

        // Get all the aPGIList where famille equals to UPDATED_FAMILLE
        defaultAPGIShouldNotBeFound("famille.in=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    public void getAllAPGISByFamilleIsNullOrNotNull() throws Exception {
        // Initialize the database
        aPGIRepository.saveAndFlush(aPGI);

        // Get all the aPGIList where famille is not null
        defaultAPGIShouldBeFound("famille.specified=true");

        // Get all the aPGIList where famille is null
        defaultAPGIShouldNotBeFound("famille.specified=false");
    }
                @Test
    @Transactional
    public void getAllAPGISByFamilleContainsSomething() throws Exception {
        // Initialize the database
        aPGIRepository.saveAndFlush(aPGI);

        // Get all the aPGIList where famille contains DEFAULT_FAMILLE
        defaultAPGIShouldBeFound("famille.contains=" + DEFAULT_FAMILLE);

        // Get all the aPGIList where famille contains UPDATED_FAMILLE
        defaultAPGIShouldNotBeFound("famille.contains=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    public void getAllAPGISByFamilleNotContainsSomething() throws Exception {
        // Initialize the database
        aPGIRepository.saveAndFlush(aPGI);

        // Get all the aPGIList where famille does not contain DEFAULT_FAMILLE
        defaultAPGIShouldNotBeFound("famille.doesNotContain=" + DEFAULT_FAMILLE);

        // Get all the aPGIList where famille does not contain UPDATED_FAMILLE
        defaultAPGIShouldBeFound("famille.doesNotContain=" + UPDATED_FAMILLE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAPGIShouldBeFound(String filter) throws Exception {
        restAPGIMockMvc.perform(get("/api/apgis?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aPGI.getId().intValue())))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)))
            .andExpect(jsonPath("$.[*].famille").value(hasItem(DEFAULT_FAMILLE)));

        // Check, that the count call also returns 1
        restAPGIMockMvc.perform(get("/api/apgis/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAPGIShouldNotBeFound(String filter) throws Exception {
        restAPGIMockMvc.perform(get("/api/apgis?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAPGIMockMvc.perform(get("/api/apgis/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingAPGI() throws Exception {
        // Get the aPGI
        restAPGIMockMvc.perform(get("/api/apgis/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAPGI() throws Exception {
        // Initialize the database
        aPGIRepository.saveAndFlush(aPGI);

        int databaseSizeBeforeUpdate = aPGIRepository.findAll().size();

        // Update the aPGI
        APGI updatedAPGI = aPGIRepository.findById(aPGI.getId()).get();
        // Disconnect from session so that the updates on updatedAPGI are not directly saved in db
        em.detach(updatedAPGI);
        updatedAPGI
            .ordre(UPDATED_ORDRE)
            .famille(UPDATED_FAMILLE);
        APGIDTO aPGIDTO = aPGIMapper.toDto(updatedAPGI);

        restAPGIMockMvc.perform(put("/api/apgis").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(aPGIDTO)))
            .andExpect(status().isOk());

        // Validate the APGI in the database
        List<APGI> aPGIList = aPGIRepository.findAll();
        assertThat(aPGIList).hasSize(databaseSizeBeforeUpdate);
        APGI testAPGI = aPGIList.get(aPGIList.size() - 1);
        assertThat(testAPGI.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testAPGI.getFamille()).isEqualTo(UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    public void updateNonExistingAPGI() throws Exception {
        int databaseSizeBeforeUpdate = aPGIRepository.findAll().size();

        // Create the APGI
        APGIDTO aPGIDTO = aPGIMapper.toDto(aPGI);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAPGIMockMvc.perform(put("/api/apgis").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(aPGIDTO)))
            .andExpect(status().isBadRequest());

        // Validate the APGI in the database
        List<APGI> aPGIList = aPGIRepository.findAll();
        assertThat(aPGIList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAPGI() throws Exception {
        // Initialize the database
        aPGIRepository.saveAndFlush(aPGI);

        int databaseSizeBeforeDelete = aPGIRepository.findAll().size();

        // Delete the aPGI
        restAPGIMockMvc.perform(delete("/api/apgis/{id}", aPGI.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<APGI> aPGIList = aPGIRepository.findAll();
        assertThat(aPGIList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
