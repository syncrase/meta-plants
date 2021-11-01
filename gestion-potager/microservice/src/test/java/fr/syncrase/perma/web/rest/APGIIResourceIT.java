package fr.syncrase.perma.web.rest;

import fr.syncrase.perma.MicroserviceApp;
import fr.syncrase.perma.config.TestSecurityConfiguration;
import fr.syncrase.perma.domain.APGII;
import fr.syncrase.perma.repository.APGIIRepository;
import fr.syncrase.perma.service.APGIIService;
import fr.syncrase.perma.service.dto.APGIIDTO;
import fr.syncrase.perma.service.mapper.APGIIMapper;
import fr.syncrase.perma.service.dto.APGIICriteria;
import fr.syncrase.perma.service.APGIIQueryService;

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
 * Integration tests for the {@link APGIIResource} REST controller.
 */
@SpringBootTest(classes = { MicroserviceApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class APGIIResourceIT {

    private static final String DEFAULT_ORDRE = "AAAAAAAAAA";
    private static final String UPDATED_ORDRE = "BBBBBBBBBB";

    private static final String DEFAULT_FAMILLE = "AAAAAAAAAA";
    private static final String UPDATED_FAMILLE = "BBBBBBBBBB";

    @Autowired
    private APGIIRepository aPGIIRepository;

    @Autowired
    private APGIIMapper aPGIIMapper;

    @Autowired
    private APGIIService aPGIIService;

    @Autowired
    private APGIIQueryService aPGIIQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAPGIIMockMvc;

    private APGII aPGII;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static APGII createEntity(EntityManager em) {
        APGII aPGII = new APGII()
            .ordre(DEFAULT_ORDRE)
            .famille(DEFAULT_FAMILLE);
        return aPGII;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static APGII createUpdatedEntity(EntityManager em) {
        APGII aPGII = new APGII()
            .ordre(UPDATED_ORDRE)
            .famille(UPDATED_FAMILLE);
        return aPGII;
    }

    @BeforeEach
    public void initTest() {
        aPGII = createEntity(em);
    }

    @Test
    @Transactional
    public void createAPGII() throws Exception {
        int databaseSizeBeforeCreate = aPGIIRepository.findAll().size();
        // Create the APGII
        APGIIDTO aPGIIDTO = aPGIIMapper.toDto(aPGII);
        restAPGIIMockMvc.perform(post("/api/apgiis").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(aPGIIDTO)))
            .andExpect(status().isCreated());

        // Validate the APGII in the database
        List<APGII> aPGIIList = aPGIIRepository.findAll();
        assertThat(aPGIIList).hasSize(databaseSizeBeforeCreate + 1);
        APGII testAPGII = aPGIIList.get(aPGIIList.size() - 1);
        assertThat(testAPGII.getOrdre()).isEqualTo(DEFAULT_ORDRE);
        assertThat(testAPGII.getFamille()).isEqualTo(DEFAULT_FAMILLE);
    }

    @Test
    @Transactional
    public void createAPGIIWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = aPGIIRepository.findAll().size();

        // Create the APGII with an existing ID
        aPGII.setId(1L);
        APGIIDTO aPGIIDTO = aPGIIMapper.toDto(aPGII);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAPGIIMockMvc.perform(post("/api/apgiis").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(aPGIIDTO)))
            .andExpect(status().isBadRequest());

        // Validate the APGII in the database
        List<APGII> aPGIIList = aPGIIRepository.findAll();
        assertThat(aPGIIList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkOrdreIsRequired() throws Exception {
        int databaseSizeBeforeTest = aPGIIRepository.findAll().size();
        // set the field null
        aPGII.setOrdre(null);

        // Create the APGII, which fails.
        APGIIDTO aPGIIDTO = aPGIIMapper.toDto(aPGII);


        restAPGIIMockMvc.perform(post("/api/apgiis").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(aPGIIDTO)))
            .andExpect(status().isBadRequest());

        List<APGII> aPGIIList = aPGIIRepository.findAll();
        assertThat(aPGIIList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFamilleIsRequired() throws Exception {
        int databaseSizeBeforeTest = aPGIIRepository.findAll().size();
        // set the field null
        aPGII.setFamille(null);

        // Create the APGII, which fails.
        APGIIDTO aPGIIDTO = aPGIIMapper.toDto(aPGII);


        restAPGIIMockMvc.perform(post("/api/apgiis").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(aPGIIDTO)))
            .andExpect(status().isBadRequest());

        List<APGII> aPGIIList = aPGIIRepository.findAll();
        assertThat(aPGIIList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAPGIIS() throws Exception {
        // Initialize the database
        aPGIIRepository.saveAndFlush(aPGII);

        // Get all the aPGIIList
        restAPGIIMockMvc.perform(get("/api/apgiis?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aPGII.getId().intValue())))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)))
            .andExpect(jsonPath("$.[*].famille").value(hasItem(DEFAULT_FAMILLE)));
    }
    
    @Test
    @Transactional
    public void getAPGII() throws Exception {
        // Initialize the database
        aPGIIRepository.saveAndFlush(aPGII);

        // Get the aPGII
        restAPGIIMockMvc.perform(get("/api/apgiis/{id}", aPGII.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aPGII.getId().intValue()))
            .andExpect(jsonPath("$.ordre").value(DEFAULT_ORDRE))
            .andExpect(jsonPath("$.famille").value(DEFAULT_FAMILLE));
    }


    @Test
    @Transactional
    public void getAPGIISByIdFiltering() throws Exception {
        // Initialize the database
        aPGIIRepository.saveAndFlush(aPGII);

        Long id = aPGII.getId();

        defaultAPGIIShouldBeFound("id.equals=" + id);
        defaultAPGIIShouldNotBeFound("id.notEquals=" + id);

        defaultAPGIIShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAPGIIShouldNotBeFound("id.greaterThan=" + id);

        defaultAPGIIShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAPGIIShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAPGIISByOrdreIsEqualToSomething() throws Exception {
        // Initialize the database
        aPGIIRepository.saveAndFlush(aPGII);

        // Get all the aPGIIList where ordre equals to DEFAULT_ORDRE
        defaultAPGIIShouldBeFound("ordre.equals=" + DEFAULT_ORDRE);

        // Get all the aPGIIList where ordre equals to UPDATED_ORDRE
        defaultAPGIIShouldNotBeFound("ordre.equals=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    public void getAllAPGIISByOrdreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        aPGIIRepository.saveAndFlush(aPGII);

        // Get all the aPGIIList where ordre not equals to DEFAULT_ORDRE
        defaultAPGIIShouldNotBeFound("ordre.notEquals=" + DEFAULT_ORDRE);

        // Get all the aPGIIList where ordre not equals to UPDATED_ORDRE
        defaultAPGIIShouldBeFound("ordre.notEquals=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    public void getAllAPGIISByOrdreIsInShouldWork() throws Exception {
        // Initialize the database
        aPGIIRepository.saveAndFlush(aPGII);

        // Get all the aPGIIList where ordre in DEFAULT_ORDRE or UPDATED_ORDRE
        defaultAPGIIShouldBeFound("ordre.in=" + DEFAULT_ORDRE + "," + UPDATED_ORDRE);

        // Get all the aPGIIList where ordre equals to UPDATED_ORDRE
        defaultAPGIIShouldNotBeFound("ordre.in=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    public void getAllAPGIISByOrdreIsNullOrNotNull() throws Exception {
        // Initialize the database
        aPGIIRepository.saveAndFlush(aPGII);

        // Get all the aPGIIList where ordre is not null
        defaultAPGIIShouldBeFound("ordre.specified=true");

        // Get all the aPGIIList where ordre is null
        defaultAPGIIShouldNotBeFound("ordre.specified=false");
    }
                @Test
    @Transactional
    public void getAllAPGIISByOrdreContainsSomething() throws Exception {
        // Initialize the database
        aPGIIRepository.saveAndFlush(aPGII);

        // Get all the aPGIIList where ordre contains DEFAULT_ORDRE
        defaultAPGIIShouldBeFound("ordre.contains=" + DEFAULT_ORDRE);

        // Get all the aPGIIList where ordre contains UPDATED_ORDRE
        defaultAPGIIShouldNotBeFound("ordre.contains=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    public void getAllAPGIISByOrdreNotContainsSomething() throws Exception {
        // Initialize the database
        aPGIIRepository.saveAndFlush(aPGII);

        // Get all the aPGIIList where ordre does not contain DEFAULT_ORDRE
        defaultAPGIIShouldNotBeFound("ordre.doesNotContain=" + DEFAULT_ORDRE);

        // Get all the aPGIIList where ordre does not contain UPDATED_ORDRE
        defaultAPGIIShouldBeFound("ordre.doesNotContain=" + UPDATED_ORDRE);
    }


    @Test
    @Transactional
    public void getAllAPGIISByFamilleIsEqualToSomething() throws Exception {
        // Initialize the database
        aPGIIRepository.saveAndFlush(aPGII);

        // Get all the aPGIIList where famille equals to DEFAULT_FAMILLE
        defaultAPGIIShouldBeFound("famille.equals=" + DEFAULT_FAMILLE);

        // Get all the aPGIIList where famille equals to UPDATED_FAMILLE
        defaultAPGIIShouldNotBeFound("famille.equals=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    public void getAllAPGIISByFamilleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        aPGIIRepository.saveAndFlush(aPGII);

        // Get all the aPGIIList where famille not equals to DEFAULT_FAMILLE
        defaultAPGIIShouldNotBeFound("famille.notEquals=" + DEFAULT_FAMILLE);

        // Get all the aPGIIList where famille not equals to UPDATED_FAMILLE
        defaultAPGIIShouldBeFound("famille.notEquals=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    public void getAllAPGIISByFamilleIsInShouldWork() throws Exception {
        // Initialize the database
        aPGIIRepository.saveAndFlush(aPGII);

        // Get all the aPGIIList where famille in DEFAULT_FAMILLE or UPDATED_FAMILLE
        defaultAPGIIShouldBeFound("famille.in=" + DEFAULT_FAMILLE + "," + UPDATED_FAMILLE);

        // Get all the aPGIIList where famille equals to UPDATED_FAMILLE
        defaultAPGIIShouldNotBeFound("famille.in=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    public void getAllAPGIISByFamilleIsNullOrNotNull() throws Exception {
        // Initialize the database
        aPGIIRepository.saveAndFlush(aPGII);

        // Get all the aPGIIList where famille is not null
        defaultAPGIIShouldBeFound("famille.specified=true");

        // Get all the aPGIIList where famille is null
        defaultAPGIIShouldNotBeFound("famille.specified=false");
    }
                @Test
    @Transactional
    public void getAllAPGIISByFamilleContainsSomething() throws Exception {
        // Initialize the database
        aPGIIRepository.saveAndFlush(aPGII);

        // Get all the aPGIIList where famille contains DEFAULT_FAMILLE
        defaultAPGIIShouldBeFound("famille.contains=" + DEFAULT_FAMILLE);

        // Get all the aPGIIList where famille contains UPDATED_FAMILLE
        defaultAPGIIShouldNotBeFound("famille.contains=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    public void getAllAPGIISByFamilleNotContainsSomething() throws Exception {
        // Initialize the database
        aPGIIRepository.saveAndFlush(aPGII);

        // Get all the aPGIIList where famille does not contain DEFAULT_FAMILLE
        defaultAPGIIShouldNotBeFound("famille.doesNotContain=" + DEFAULT_FAMILLE);

        // Get all the aPGIIList where famille does not contain UPDATED_FAMILLE
        defaultAPGIIShouldBeFound("famille.doesNotContain=" + UPDATED_FAMILLE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAPGIIShouldBeFound(String filter) throws Exception {
        restAPGIIMockMvc.perform(get("/api/apgiis?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aPGII.getId().intValue())))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)))
            .andExpect(jsonPath("$.[*].famille").value(hasItem(DEFAULT_FAMILLE)));

        // Check, that the count call also returns 1
        restAPGIIMockMvc.perform(get("/api/apgiis/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAPGIIShouldNotBeFound(String filter) throws Exception {
        restAPGIIMockMvc.perform(get("/api/apgiis?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAPGIIMockMvc.perform(get("/api/apgiis/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingAPGII() throws Exception {
        // Get the aPGII
        restAPGIIMockMvc.perform(get("/api/apgiis/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAPGII() throws Exception {
        // Initialize the database
        aPGIIRepository.saveAndFlush(aPGII);

        int databaseSizeBeforeUpdate = aPGIIRepository.findAll().size();

        // Update the aPGII
        APGII updatedAPGII = aPGIIRepository.findById(aPGII.getId()).get();
        // Disconnect from session so that the updates on updatedAPGII are not directly saved in db
        em.detach(updatedAPGII);
        updatedAPGII
            .ordre(UPDATED_ORDRE)
            .famille(UPDATED_FAMILLE);
        APGIIDTO aPGIIDTO = aPGIIMapper.toDto(updatedAPGII);

        restAPGIIMockMvc.perform(put("/api/apgiis").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(aPGIIDTO)))
            .andExpect(status().isOk());

        // Validate the APGII in the database
        List<APGII> aPGIIList = aPGIIRepository.findAll();
        assertThat(aPGIIList).hasSize(databaseSizeBeforeUpdate);
        APGII testAPGII = aPGIIList.get(aPGIIList.size() - 1);
        assertThat(testAPGII.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testAPGII.getFamille()).isEqualTo(UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    public void updateNonExistingAPGII() throws Exception {
        int databaseSizeBeforeUpdate = aPGIIRepository.findAll().size();

        // Create the APGII
        APGIIDTO aPGIIDTO = aPGIIMapper.toDto(aPGII);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAPGIIMockMvc.perform(put("/api/apgiis").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(aPGIIDTO)))
            .andExpect(status().isBadRequest());

        // Validate the APGII in the database
        List<APGII> aPGIIList = aPGIIRepository.findAll();
        assertThat(aPGIIList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAPGII() throws Exception {
        // Initialize the database
        aPGIIRepository.saveAndFlush(aPGII);

        int databaseSizeBeforeDelete = aPGIIRepository.findAll().size();

        // Delete the aPGII
        restAPGIIMockMvc.perform(delete("/api/apgiis/{id}", aPGII.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<APGII> aPGIIList = aPGIIRepository.findAll();
        assertThat(aPGIIList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
