package fr.syncrase.perma.web.rest;

import fr.syncrase.perma.MicroserviceApp;
import fr.syncrase.perma.config.TestSecurityConfiguration;
import fr.syncrase.perma.domain.APGIII;
import fr.syncrase.perma.repository.APGIIIRepository;
import fr.syncrase.perma.service.APGIIIService;
import fr.syncrase.perma.service.dto.APGIIIDTO;
import fr.syncrase.perma.service.mapper.APGIIIMapper;
import fr.syncrase.perma.service.dto.APGIIICriteria;
import fr.syncrase.perma.service.APGIIIQueryService;

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
 * Integration tests for the {@link APGIIIResource} REST controller.
 */
@SpringBootTest(classes = { MicroserviceApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class APGIIIResourceIT {

    private static final String DEFAULT_ORDRE = "AAAAAAAAAA";
    private static final String UPDATED_ORDRE = "BBBBBBBBBB";

    private static final String DEFAULT_FAMILLE = "AAAAAAAAAA";
    private static final String UPDATED_FAMILLE = "BBBBBBBBBB";

    @Autowired
    private APGIIIRepository aPGIIIRepository;

    @Autowired
    private APGIIIMapper aPGIIIMapper;

    @Autowired
    private APGIIIService aPGIIIService;

    @Autowired
    private APGIIIQueryService aPGIIIQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAPGIIIMockMvc;

    private APGIII aPGIII;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static APGIII createEntity(EntityManager em) {
        APGIII aPGIII = new APGIII()
            .ordre(DEFAULT_ORDRE)
            .famille(DEFAULT_FAMILLE);
        return aPGIII;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static APGIII createUpdatedEntity(EntityManager em) {
        APGIII aPGIII = new APGIII()
            .ordre(UPDATED_ORDRE)
            .famille(UPDATED_FAMILLE);
        return aPGIII;
    }

    @BeforeEach
    public void initTest() {
        aPGIII = createEntity(em);
    }

    @Test
    @Transactional
    public void createAPGIII() throws Exception {
        int databaseSizeBeforeCreate = aPGIIIRepository.findAll().size();
        // Create the APGIII
        APGIIIDTO aPGIIIDTO = aPGIIIMapper.toDto(aPGIII);
        restAPGIIIMockMvc.perform(post("/api/apgiiis").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(aPGIIIDTO)))
            .andExpect(status().isCreated());

        // Validate the APGIII in the database
        List<APGIII> aPGIIIList = aPGIIIRepository.findAll();
        assertThat(aPGIIIList).hasSize(databaseSizeBeforeCreate + 1);
        APGIII testAPGIII = aPGIIIList.get(aPGIIIList.size() - 1);
        assertThat(testAPGIII.getOrdre()).isEqualTo(DEFAULT_ORDRE);
        assertThat(testAPGIII.getFamille()).isEqualTo(DEFAULT_FAMILLE);
    }

    @Test
    @Transactional
    public void createAPGIIIWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = aPGIIIRepository.findAll().size();

        // Create the APGIII with an existing ID
        aPGIII.setId(1L);
        APGIIIDTO aPGIIIDTO = aPGIIIMapper.toDto(aPGIII);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAPGIIIMockMvc.perform(post("/api/apgiiis").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(aPGIIIDTO)))
            .andExpect(status().isBadRequest());

        // Validate the APGIII in the database
        List<APGIII> aPGIIIList = aPGIIIRepository.findAll();
        assertThat(aPGIIIList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkOrdreIsRequired() throws Exception {
        int databaseSizeBeforeTest = aPGIIIRepository.findAll().size();
        // set the field null
        aPGIII.setOrdre(null);

        // Create the APGIII, which fails.
        APGIIIDTO aPGIIIDTO = aPGIIIMapper.toDto(aPGIII);


        restAPGIIIMockMvc.perform(post("/api/apgiiis").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(aPGIIIDTO)))
            .andExpect(status().isBadRequest());

        List<APGIII> aPGIIIList = aPGIIIRepository.findAll();
        assertThat(aPGIIIList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFamilleIsRequired() throws Exception {
        int databaseSizeBeforeTest = aPGIIIRepository.findAll().size();
        // set the field null
        aPGIII.setFamille(null);

        // Create the APGIII, which fails.
        APGIIIDTO aPGIIIDTO = aPGIIIMapper.toDto(aPGIII);


        restAPGIIIMockMvc.perform(post("/api/apgiiis").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(aPGIIIDTO)))
            .andExpect(status().isBadRequest());

        List<APGIII> aPGIIIList = aPGIIIRepository.findAll();
        assertThat(aPGIIIList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAPGIIIS() throws Exception {
        // Initialize the database
        aPGIIIRepository.saveAndFlush(aPGIII);

        // Get all the aPGIIIList
        restAPGIIIMockMvc.perform(get("/api/apgiiis?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aPGIII.getId().intValue())))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)))
            .andExpect(jsonPath("$.[*].famille").value(hasItem(DEFAULT_FAMILLE)));
    }
    
    @Test
    @Transactional
    public void getAPGIII() throws Exception {
        // Initialize the database
        aPGIIIRepository.saveAndFlush(aPGIII);

        // Get the aPGIII
        restAPGIIIMockMvc.perform(get("/api/apgiiis/{id}", aPGIII.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aPGIII.getId().intValue()))
            .andExpect(jsonPath("$.ordre").value(DEFAULT_ORDRE))
            .andExpect(jsonPath("$.famille").value(DEFAULT_FAMILLE));
    }


    @Test
    @Transactional
    public void getAPGIIISByIdFiltering() throws Exception {
        // Initialize the database
        aPGIIIRepository.saveAndFlush(aPGIII);

        Long id = aPGIII.getId();

        defaultAPGIIIShouldBeFound("id.equals=" + id);
        defaultAPGIIIShouldNotBeFound("id.notEquals=" + id);

        defaultAPGIIIShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAPGIIIShouldNotBeFound("id.greaterThan=" + id);

        defaultAPGIIIShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAPGIIIShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAPGIIISByOrdreIsEqualToSomething() throws Exception {
        // Initialize the database
        aPGIIIRepository.saveAndFlush(aPGIII);

        // Get all the aPGIIIList where ordre equals to DEFAULT_ORDRE
        defaultAPGIIIShouldBeFound("ordre.equals=" + DEFAULT_ORDRE);

        // Get all the aPGIIIList where ordre equals to UPDATED_ORDRE
        defaultAPGIIIShouldNotBeFound("ordre.equals=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    public void getAllAPGIIISByOrdreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        aPGIIIRepository.saveAndFlush(aPGIII);

        // Get all the aPGIIIList where ordre not equals to DEFAULT_ORDRE
        defaultAPGIIIShouldNotBeFound("ordre.notEquals=" + DEFAULT_ORDRE);

        // Get all the aPGIIIList where ordre not equals to UPDATED_ORDRE
        defaultAPGIIIShouldBeFound("ordre.notEquals=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    public void getAllAPGIIISByOrdreIsInShouldWork() throws Exception {
        // Initialize the database
        aPGIIIRepository.saveAndFlush(aPGIII);

        // Get all the aPGIIIList where ordre in DEFAULT_ORDRE or UPDATED_ORDRE
        defaultAPGIIIShouldBeFound("ordre.in=" + DEFAULT_ORDRE + "," + UPDATED_ORDRE);

        // Get all the aPGIIIList where ordre equals to UPDATED_ORDRE
        defaultAPGIIIShouldNotBeFound("ordre.in=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    public void getAllAPGIIISByOrdreIsNullOrNotNull() throws Exception {
        // Initialize the database
        aPGIIIRepository.saveAndFlush(aPGIII);

        // Get all the aPGIIIList where ordre is not null
        defaultAPGIIIShouldBeFound("ordre.specified=true");

        // Get all the aPGIIIList where ordre is null
        defaultAPGIIIShouldNotBeFound("ordre.specified=false");
    }
                @Test
    @Transactional
    public void getAllAPGIIISByOrdreContainsSomething() throws Exception {
        // Initialize the database
        aPGIIIRepository.saveAndFlush(aPGIII);

        // Get all the aPGIIIList where ordre contains DEFAULT_ORDRE
        defaultAPGIIIShouldBeFound("ordre.contains=" + DEFAULT_ORDRE);

        // Get all the aPGIIIList where ordre contains UPDATED_ORDRE
        defaultAPGIIIShouldNotBeFound("ordre.contains=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    public void getAllAPGIIISByOrdreNotContainsSomething() throws Exception {
        // Initialize the database
        aPGIIIRepository.saveAndFlush(aPGIII);

        // Get all the aPGIIIList where ordre does not contain DEFAULT_ORDRE
        defaultAPGIIIShouldNotBeFound("ordre.doesNotContain=" + DEFAULT_ORDRE);

        // Get all the aPGIIIList where ordre does not contain UPDATED_ORDRE
        defaultAPGIIIShouldBeFound("ordre.doesNotContain=" + UPDATED_ORDRE);
    }


    @Test
    @Transactional
    public void getAllAPGIIISByFamilleIsEqualToSomething() throws Exception {
        // Initialize the database
        aPGIIIRepository.saveAndFlush(aPGIII);

        // Get all the aPGIIIList where famille equals to DEFAULT_FAMILLE
        defaultAPGIIIShouldBeFound("famille.equals=" + DEFAULT_FAMILLE);

        // Get all the aPGIIIList where famille equals to UPDATED_FAMILLE
        defaultAPGIIIShouldNotBeFound("famille.equals=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    public void getAllAPGIIISByFamilleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        aPGIIIRepository.saveAndFlush(aPGIII);

        // Get all the aPGIIIList where famille not equals to DEFAULT_FAMILLE
        defaultAPGIIIShouldNotBeFound("famille.notEquals=" + DEFAULT_FAMILLE);

        // Get all the aPGIIIList where famille not equals to UPDATED_FAMILLE
        defaultAPGIIIShouldBeFound("famille.notEquals=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    public void getAllAPGIIISByFamilleIsInShouldWork() throws Exception {
        // Initialize the database
        aPGIIIRepository.saveAndFlush(aPGIII);

        // Get all the aPGIIIList where famille in DEFAULT_FAMILLE or UPDATED_FAMILLE
        defaultAPGIIIShouldBeFound("famille.in=" + DEFAULT_FAMILLE + "," + UPDATED_FAMILLE);

        // Get all the aPGIIIList where famille equals to UPDATED_FAMILLE
        defaultAPGIIIShouldNotBeFound("famille.in=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    public void getAllAPGIIISByFamilleIsNullOrNotNull() throws Exception {
        // Initialize the database
        aPGIIIRepository.saveAndFlush(aPGIII);

        // Get all the aPGIIIList where famille is not null
        defaultAPGIIIShouldBeFound("famille.specified=true");

        // Get all the aPGIIIList where famille is null
        defaultAPGIIIShouldNotBeFound("famille.specified=false");
    }
                @Test
    @Transactional
    public void getAllAPGIIISByFamilleContainsSomething() throws Exception {
        // Initialize the database
        aPGIIIRepository.saveAndFlush(aPGIII);

        // Get all the aPGIIIList where famille contains DEFAULT_FAMILLE
        defaultAPGIIIShouldBeFound("famille.contains=" + DEFAULT_FAMILLE);

        // Get all the aPGIIIList where famille contains UPDATED_FAMILLE
        defaultAPGIIIShouldNotBeFound("famille.contains=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    public void getAllAPGIIISByFamilleNotContainsSomething() throws Exception {
        // Initialize the database
        aPGIIIRepository.saveAndFlush(aPGIII);

        // Get all the aPGIIIList where famille does not contain DEFAULT_FAMILLE
        defaultAPGIIIShouldNotBeFound("famille.doesNotContain=" + DEFAULT_FAMILLE);

        // Get all the aPGIIIList where famille does not contain UPDATED_FAMILLE
        defaultAPGIIIShouldBeFound("famille.doesNotContain=" + UPDATED_FAMILLE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAPGIIIShouldBeFound(String filter) throws Exception {
        restAPGIIIMockMvc.perform(get("/api/apgiiis?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aPGIII.getId().intValue())))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)))
            .andExpect(jsonPath("$.[*].famille").value(hasItem(DEFAULT_FAMILLE)));

        // Check, that the count call also returns 1
        restAPGIIIMockMvc.perform(get("/api/apgiiis/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAPGIIIShouldNotBeFound(String filter) throws Exception {
        restAPGIIIMockMvc.perform(get("/api/apgiiis?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAPGIIIMockMvc.perform(get("/api/apgiiis/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingAPGIII() throws Exception {
        // Get the aPGIII
        restAPGIIIMockMvc.perform(get("/api/apgiiis/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAPGIII() throws Exception {
        // Initialize the database
        aPGIIIRepository.saveAndFlush(aPGIII);

        int databaseSizeBeforeUpdate = aPGIIIRepository.findAll().size();

        // Update the aPGIII
        APGIII updatedAPGIII = aPGIIIRepository.findById(aPGIII.getId()).get();
        // Disconnect from session so that the updates on updatedAPGIII are not directly saved in db
        em.detach(updatedAPGIII);
        updatedAPGIII
            .ordre(UPDATED_ORDRE)
            .famille(UPDATED_FAMILLE);
        APGIIIDTO aPGIIIDTO = aPGIIIMapper.toDto(updatedAPGIII);

        restAPGIIIMockMvc.perform(put("/api/apgiiis").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(aPGIIIDTO)))
            .andExpect(status().isOk());

        // Validate the APGIII in the database
        List<APGIII> aPGIIIList = aPGIIIRepository.findAll();
        assertThat(aPGIIIList).hasSize(databaseSizeBeforeUpdate);
        APGIII testAPGIII = aPGIIIList.get(aPGIIIList.size() - 1);
        assertThat(testAPGIII.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testAPGIII.getFamille()).isEqualTo(UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    public void updateNonExistingAPGIII() throws Exception {
        int databaseSizeBeforeUpdate = aPGIIIRepository.findAll().size();

        // Create the APGIII
        APGIIIDTO aPGIIIDTO = aPGIIIMapper.toDto(aPGIII);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAPGIIIMockMvc.perform(put("/api/apgiiis").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(aPGIIIDTO)))
            .andExpect(status().isBadRequest());

        // Validate the APGIII in the database
        List<APGIII> aPGIIIList = aPGIIIRepository.findAll();
        assertThat(aPGIIIList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAPGIII() throws Exception {
        // Initialize the database
        aPGIIIRepository.saveAndFlush(aPGIII);

        int databaseSizeBeforeDelete = aPGIIIRepository.findAll().size();

        // Delete the aPGIII
        restAPGIIIMockMvc.perform(delete("/api/apgiiis/{id}", aPGIII.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<APGIII> aPGIIIList = aPGIIIRepository.findAll();
        assertThat(aPGIIIList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
