package fr.syncrase.perma.web.rest;

import fr.syncrase.perma.MicroserviceApp;
import fr.syncrase.perma.config.TestSecurityConfiguration;
import fr.syncrase.perma.domain.APGIV;
import fr.syncrase.perma.repository.APGIVRepository;
import fr.syncrase.perma.service.APGIVService;
import fr.syncrase.perma.service.dto.APGIVDTO;
import fr.syncrase.perma.service.mapper.APGIVMapper;
import fr.syncrase.perma.service.dto.APGIVCriteria;
import fr.syncrase.perma.service.APGIVQueryService;

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
 * Integration tests for the {@link APGIVResource} REST controller.
 */
@SpringBootTest(classes = { MicroserviceApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class APGIVResourceIT {

    private static final String DEFAULT_ORDRE = "AAAAAAAAAA";
    private static final String UPDATED_ORDRE = "BBBBBBBBBB";

    private static final String DEFAULT_FAMILLE = "AAAAAAAAAA";
    private static final String UPDATED_FAMILLE = "BBBBBBBBBB";

    @Autowired
    private APGIVRepository aPGIVRepository;

    @Autowired
    private APGIVMapper aPGIVMapper;

    @Autowired
    private APGIVService aPGIVService;

    @Autowired
    private APGIVQueryService aPGIVQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAPGIVMockMvc;

    private APGIV aPGIV;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static APGIV createEntity(EntityManager em) {
        APGIV aPGIV = new APGIV()
            .ordre(DEFAULT_ORDRE)
            .famille(DEFAULT_FAMILLE);
        return aPGIV;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static APGIV createUpdatedEntity(EntityManager em) {
        APGIV aPGIV = new APGIV()
            .ordre(UPDATED_ORDRE)
            .famille(UPDATED_FAMILLE);
        return aPGIV;
    }

    @BeforeEach
    public void initTest() {
        aPGIV = createEntity(em);
    }

    @Test
    @Transactional
    public void createAPGIV() throws Exception {
        int databaseSizeBeforeCreate = aPGIVRepository.findAll().size();
        // Create the APGIV
        APGIVDTO aPGIVDTO = aPGIVMapper.toDto(aPGIV);
        restAPGIVMockMvc.perform(post("/api/apgivs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(aPGIVDTO)))
            .andExpect(status().isCreated());

        // Validate the APGIV in the database
        List<APGIV> aPGIVList = aPGIVRepository.findAll();
        assertThat(aPGIVList).hasSize(databaseSizeBeforeCreate + 1);
        APGIV testAPGIV = aPGIVList.get(aPGIVList.size() - 1);
        assertThat(testAPGIV.getOrdre()).isEqualTo(DEFAULT_ORDRE);
        assertThat(testAPGIV.getFamille()).isEqualTo(DEFAULT_FAMILLE);
    }

    @Test
    @Transactional
    public void createAPGIVWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = aPGIVRepository.findAll().size();

        // Create the APGIV with an existing ID
        aPGIV.setId(1L);
        APGIVDTO aPGIVDTO = aPGIVMapper.toDto(aPGIV);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAPGIVMockMvc.perform(post("/api/apgivs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(aPGIVDTO)))
            .andExpect(status().isBadRequest());

        // Validate the APGIV in the database
        List<APGIV> aPGIVList = aPGIVRepository.findAll();
        assertThat(aPGIVList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkOrdreIsRequired() throws Exception {
        int databaseSizeBeforeTest = aPGIVRepository.findAll().size();
        // set the field null
        aPGIV.setOrdre(null);

        // Create the APGIV, which fails.
        APGIVDTO aPGIVDTO = aPGIVMapper.toDto(aPGIV);


        restAPGIVMockMvc.perform(post("/api/apgivs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(aPGIVDTO)))
            .andExpect(status().isBadRequest());

        List<APGIV> aPGIVList = aPGIVRepository.findAll();
        assertThat(aPGIVList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFamilleIsRequired() throws Exception {
        int databaseSizeBeforeTest = aPGIVRepository.findAll().size();
        // set the field null
        aPGIV.setFamille(null);

        // Create the APGIV, which fails.
        APGIVDTO aPGIVDTO = aPGIVMapper.toDto(aPGIV);


        restAPGIVMockMvc.perform(post("/api/apgivs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(aPGIVDTO)))
            .andExpect(status().isBadRequest());

        List<APGIV> aPGIVList = aPGIVRepository.findAll();
        assertThat(aPGIVList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAPGIVS() throws Exception {
        // Initialize the database
        aPGIVRepository.saveAndFlush(aPGIV);

        // Get all the aPGIVList
        restAPGIVMockMvc.perform(get("/api/apgivs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aPGIV.getId().intValue())))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)))
            .andExpect(jsonPath("$.[*].famille").value(hasItem(DEFAULT_FAMILLE)));
    }
    
    @Test
    @Transactional
    public void getAPGIV() throws Exception {
        // Initialize the database
        aPGIVRepository.saveAndFlush(aPGIV);

        // Get the aPGIV
        restAPGIVMockMvc.perform(get("/api/apgivs/{id}", aPGIV.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aPGIV.getId().intValue()))
            .andExpect(jsonPath("$.ordre").value(DEFAULT_ORDRE))
            .andExpect(jsonPath("$.famille").value(DEFAULT_FAMILLE));
    }


    @Test
    @Transactional
    public void getAPGIVSByIdFiltering() throws Exception {
        // Initialize the database
        aPGIVRepository.saveAndFlush(aPGIV);

        Long id = aPGIV.getId();

        defaultAPGIVShouldBeFound("id.equals=" + id);
        defaultAPGIVShouldNotBeFound("id.notEquals=" + id);

        defaultAPGIVShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAPGIVShouldNotBeFound("id.greaterThan=" + id);

        defaultAPGIVShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAPGIVShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAPGIVSByOrdreIsEqualToSomething() throws Exception {
        // Initialize the database
        aPGIVRepository.saveAndFlush(aPGIV);

        // Get all the aPGIVList where ordre equals to DEFAULT_ORDRE
        defaultAPGIVShouldBeFound("ordre.equals=" + DEFAULT_ORDRE);

        // Get all the aPGIVList where ordre equals to UPDATED_ORDRE
        defaultAPGIVShouldNotBeFound("ordre.equals=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    public void getAllAPGIVSByOrdreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        aPGIVRepository.saveAndFlush(aPGIV);

        // Get all the aPGIVList where ordre not equals to DEFAULT_ORDRE
        defaultAPGIVShouldNotBeFound("ordre.notEquals=" + DEFAULT_ORDRE);

        // Get all the aPGIVList where ordre not equals to UPDATED_ORDRE
        defaultAPGIVShouldBeFound("ordre.notEquals=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    public void getAllAPGIVSByOrdreIsInShouldWork() throws Exception {
        // Initialize the database
        aPGIVRepository.saveAndFlush(aPGIV);

        // Get all the aPGIVList where ordre in DEFAULT_ORDRE or UPDATED_ORDRE
        defaultAPGIVShouldBeFound("ordre.in=" + DEFAULT_ORDRE + "," + UPDATED_ORDRE);

        // Get all the aPGIVList where ordre equals to UPDATED_ORDRE
        defaultAPGIVShouldNotBeFound("ordre.in=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    public void getAllAPGIVSByOrdreIsNullOrNotNull() throws Exception {
        // Initialize the database
        aPGIVRepository.saveAndFlush(aPGIV);

        // Get all the aPGIVList where ordre is not null
        defaultAPGIVShouldBeFound("ordre.specified=true");

        // Get all the aPGIVList where ordre is null
        defaultAPGIVShouldNotBeFound("ordre.specified=false");
    }
                @Test
    @Transactional
    public void getAllAPGIVSByOrdreContainsSomething() throws Exception {
        // Initialize the database
        aPGIVRepository.saveAndFlush(aPGIV);

        // Get all the aPGIVList where ordre contains DEFAULT_ORDRE
        defaultAPGIVShouldBeFound("ordre.contains=" + DEFAULT_ORDRE);

        // Get all the aPGIVList where ordre contains UPDATED_ORDRE
        defaultAPGIVShouldNotBeFound("ordre.contains=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    public void getAllAPGIVSByOrdreNotContainsSomething() throws Exception {
        // Initialize the database
        aPGIVRepository.saveAndFlush(aPGIV);

        // Get all the aPGIVList where ordre does not contain DEFAULT_ORDRE
        defaultAPGIVShouldNotBeFound("ordre.doesNotContain=" + DEFAULT_ORDRE);

        // Get all the aPGIVList where ordre does not contain UPDATED_ORDRE
        defaultAPGIVShouldBeFound("ordre.doesNotContain=" + UPDATED_ORDRE);
    }


    @Test
    @Transactional
    public void getAllAPGIVSByFamilleIsEqualToSomething() throws Exception {
        // Initialize the database
        aPGIVRepository.saveAndFlush(aPGIV);

        // Get all the aPGIVList where famille equals to DEFAULT_FAMILLE
        defaultAPGIVShouldBeFound("famille.equals=" + DEFAULT_FAMILLE);

        // Get all the aPGIVList where famille equals to UPDATED_FAMILLE
        defaultAPGIVShouldNotBeFound("famille.equals=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    public void getAllAPGIVSByFamilleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        aPGIVRepository.saveAndFlush(aPGIV);

        // Get all the aPGIVList where famille not equals to DEFAULT_FAMILLE
        defaultAPGIVShouldNotBeFound("famille.notEquals=" + DEFAULT_FAMILLE);

        // Get all the aPGIVList where famille not equals to UPDATED_FAMILLE
        defaultAPGIVShouldBeFound("famille.notEquals=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    public void getAllAPGIVSByFamilleIsInShouldWork() throws Exception {
        // Initialize the database
        aPGIVRepository.saveAndFlush(aPGIV);

        // Get all the aPGIVList where famille in DEFAULT_FAMILLE or UPDATED_FAMILLE
        defaultAPGIVShouldBeFound("famille.in=" + DEFAULT_FAMILLE + "," + UPDATED_FAMILLE);

        // Get all the aPGIVList where famille equals to UPDATED_FAMILLE
        defaultAPGIVShouldNotBeFound("famille.in=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    public void getAllAPGIVSByFamilleIsNullOrNotNull() throws Exception {
        // Initialize the database
        aPGIVRepository.saveAndFlush(aPGIV);

        // Get all the aPGIVList where famille is not null
        defaultAPGIVShouldBeFound("famille.specified=true");

        // Get all the aPGIVList where famille is null
        defaultAPGIVShouldNotBeFound("famille.specified=false");
    }
                @Test
    @Transactional
    public void getAllAPGIVSByFamilleContainsSomething() throws Exception {
        // Initialize the database
        aPGIVRepository.saveAndFlush(aPGIV);

        // Get all the aPGIVList where famille contains DEFAULT_FAMILLE
        defaultAPGIVShouldBeFound("famille.contains=" + DEFAULT_FAMILLE);

        // Get all the aPGIVList where famille contains UPDATED_FAMILLE
        defaultAPGIVShouldNotBeFound("famille.contains=" + UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    public void getAllAPGIVSByFamilleNotContainsSomething() throws Exception {
        // Initialize the database
        aPGIVRepository.saveAndFlush(aPGIV);

        // Get all the aPGIVList where famille does not contain DEFAULT_FAMILLE
        defaultAPGIVShouldNotBeFound("famille.doesNotContain=" + DEFAULT_FAMILLE);

        // Get all the aPGIVList where famille does not contain UPDATED_FAMILLE
        defaultAPGIVShouldBeFound("famille.doesNotContain=" + UPDATED_FAMILLE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAPGIVShouldBeFound(String filter) throws Exception {
        restAPGIVMockMvc.perform(get("/api/apgivs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aPGIV.getId().intValue())))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)))
            .andExpect(jsonPath("$.[*].famille").value(hasItem(DEFAULT_FAMILLE)));

        // Check, that the count call also returns 1
        restAPGIVMockMvc.perform(get("/api/apgivs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAPGIVShouldNotBeFound(String filter) throws Exception {
        restAPGIVMockMvc.perform(get("/api/apgivs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAPGIVMockMvc.perform(get("/api/apgivs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingAPGIV() throws Exception {
        // Get the aPGIV
        restAPGIVMockMvc.perform(get("/api/apgivs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAPGIV() throws Exception {
        // Initialize the database
        aPGIVRepository.saveAndFlush(aPGIV);

        int databaseSizeBeforeUpdate = aPGIVRepository.findAll().size();

        // Update the aPGIV
        APGIV updatedAPGIV = aPGIVRepository.findById(aPGIV.getId()).get();
        // Disconnect from session so that the updates on updatedAPGIV are not directly saved in db
        em.detach(updatedAPGIV);
        updatedAPGIV
            .ordre(UPDATED_ORDRE)
            .famille(UPDATED_FAMILLE);
        APGIVDTO aPGIVDTO = aPGIVMapper.toDto(updatedAPGIV);

        restAPGIVMockMvc.perform(put("/api/apgivs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(aPGIVDTO)))
            .andExpect(status().isOk());

        // Validate the APGIV in the database
        List<APGIV> aPGIVList = aPGIVRepository.findAll();
        assertThat(aPGIVList).hasSize(databaseSizeBeforeUpdate);
        APGIV testAPGIV = aPGIVList.get(aPGIVList.size() - 1);
        assertThat(testAPGIV.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testAPGIV.getFamille()).isEqualTo(UPDATED_FAMILLE);
    }

    @Test
    @Transactional
    public void updateNonExistingAPGIV() throws Exception {
        int databaseSizeBeforeUpdate = aPGIVRepository.findAll().size();

        // Create the APGIV
        APGIVDTO aPGIVDTO = aPGIVMapper.toDto(aPGIV);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAPGIVMockMvc.perform(put("/api/apgivs").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(aPGIVDTO)))
            .andExpect(status().isBadRequest());

        // Validate the APGIV in the database
        List<APGIV> aPGIVList = aPGIVRepository.findAll();
        assertThat(aPGIVList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAPGIV() throws Exception {
        // Initialize the database
        aPGIVRepository.saveAndFlush(aPGIV);

        int databaseSizeBeforeDelete = aPGIVRepository.findAll().size();

        // Delete the aPGIV
        restAPGIVMockMvc.perform(delete("/api/apgivs/{id}", aPGIV.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<APGIV> aPGIVList = aPGIVRepository.findAll();
        assertThat(aPGIVList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
