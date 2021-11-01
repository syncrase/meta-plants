package fr.syncrase.perma.web.rest;

import fr.syncrase.perma.MicroserviceApp;
import fr.syncrase.perma.config.TestSecurityConfiguration;
import fr.syncrase.perma.domain.TypeSemis;
import fr.syncrase.perma.repository.TypeSemisRepository;
import fr.syncrase.perma.service.TypeSemisService;
import fr.syncrase.perma.service.dto.TypeSemisDTO;
import fr.syncrase.perma.service.mapper.TypeSemisMapper;
import fr.syncrase.perma.service.dto.TypeSemisCriteria;
import fr.syncrase.perma.service.TypeSemisQueryService;

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
 * Integration tests for the {@link TypeSemisResource} REST controller.
 */
@SpringBootTest(classes = { MicroserviceApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class TypeSemisResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private TypeSemisRepository typeSemisRepository;

    @Autowired
    private TypeSemisMapper typeSemisMapper;

    @Autowired
    private TypeSemisService typeSemisService;

    @Autowired
    private TypeSemisQueryService typeSemisQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeSemisMockMvc;

    private TypeSemis typeSemis;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeSemis createEntity(EntityManager em) {
        TypeSemis typeSemis = new TypeSemis()
            .description(DEFAULT_DESCRIPTION);
        return typeSemis;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeSemis createUpdatedEntity(EntityManager em) {
        TypeSemis typeSemis = new TypeSemis()
            .description(UPDATED_DESCRIPTION);
        return typeSemis;
    }

    @BeforeEach
    public void initTest() {
        typeSemis = createEntity(em);
    }

    @Test
    @Transactional
    public void createTypeSemis() throws Exception {
        int databaseSizeBeforeCreate = typeSemisRepository.findAll().size();
        // Create the TypeSemis
        TypeSemisDTO typeSemisDTO = typeSemisMapper.toDto(typeSemis);
        restTypeSemisMockMvc.perform(post("/api/type-semis").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(typeSemisDTO)))
            .andExpect(status().isCreated());

        // Validate the TypeSemis in the database
        List<TypeSemis> typeSemisList = typeSemisRepository.findAll();
        assertThat(typeSemisList).hasSize(databaseSizeBeforeCreate + 1);
        TypeSemis testTypeSemis = typeSemisList.get(typeSemisList.size() - 1);
        assertThat(testTypeSemis.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createTypeSemisWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = typeSemisRepository.findAll().size();

        // Create the TypeSemis with an existing ID
        typeSemis.setId(1L);
        TypeSemisDTO typeSemisDTO = typeSemisMapper.toDto(typeSemis);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeSemisMockMvc.perform(post("/api/type-semis").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(typeSemisDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TypeSemis in the database
        List<TypeSemis> typeSemisList = typeSemisRepository.findAll();
        assertThat(typeSemisList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTypeSemis() throws Exception {
        // Initialize the database
        typeSemisRepository.saveAndFlush(typeSemis);

        // Get all the typeSemisList
        restTypeSemisMockMvc.perform(get("/api/type-semis?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeSemis.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getTypeSemis() throws Exception {
        // Initialize the database
        typeSemisRepository.saveAndFlush(typeSemis);

        // Get the typeSemis
        restTypeSemisMockMvc.perform(get("/api/type-semis/{id}", typeSemis.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typeSemis.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }


    @Test
    @Transactional
    public void getTypeSemisByIdFiltering() throws Exception {
        // Initialize the database
        typeSemisRepository.saveAndFlush(typeSemis);

        Long id = typeSemis.getId();

        defaultTypeSemisShouldBeFound("id.equals=" + id);
        defaultTypeSemisShouldNotBeFound("id.notEquals=" + id);

        defaultTypeSemisShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTypeSemisShouldNotBeFound("id.greaterThan=" + id);

        defaultTypeSemisShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTypeSemisShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllTypeSemisByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        typeSemisRepository.saveAndFlush(typeSemis);

        // Get all the typeSemisList where description equals to DEFAULT_DESCRIPTION
        defaultTypeSemisShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the typeSemisList where description equals to UPDATED_DESCRIPTION
        defaultTypeSemisShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTypeSemisByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        typeSemisRepository.saveAndFlush(typeSemis);

        // Get all the typeSemisList where description not equals to DEFAULT_DESCRIPTION
        defaultTypeSemisShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the typeSemisList where description not equals to UPDATED_DESCRIPTION
        defaultTypeSemisShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTypeSemisByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        typeSemisRepository.saveAndFlush(typeSemis);

        // Get all the typeSemisList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultTypeSemisShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the typeSemisList where description equals to UPDATED_DESCRIPTION
        defaultTypeSemisShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTypeSemisByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        typeSemisRepository.saveAndFlush(typeSemis);

        // Get all the typeSemisList where description is not null
        defaultTypeSemisShouldBeFound("description.specified=true");

        // Get all the typeSemisList where description is null
        defaultTypeSemisShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllTypeSemisByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        typeSemisRepository.saveAndFlush(typeSemis);

        // Get all the typeSemisList where description contains DEFAULT_DESCRIPTION
        defaultTypeSemisShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the typeSemisList where description contains UPDATED_DESCRIPTION
        defaultTypeSemisShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTypeSemisByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        typeSemisRepository.saveAndFlush(typeSemis);

        // Get all the typeSemisList where description does not contain DEFAULT_DESCRIPTION
        defaultTypeSemisShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the typeSemisList where description does not contain UPDATED_DESCRIPTION
        defaultTypeSemisShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTypeSemisShouldBeFound(String filter) throws Exception {
        restTypeSemisMockMvc.perform(get("/api/type-semis?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeSemis.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restTypeSemisMockMvc.perform(get("/api/type-semis/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTypeSemisShouldNotBeFound(String filter) throws Exception {
        restTypeSemisMockMvc.perform(get("/api/type-semis?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTypeSemisMockMvc.perform(get("/api/type-semis/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingTypeSemis() throws Exception {
        // Get the typeSemis
        restTypeSemisMockMvc.perform(get("/api/type-semis/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTypeSemis() throws Exception {
        // Initialize the database
        typeSemisRepository.saveAndFlush(typeSemis);

        int databaseSizeBeforeUpdate = typeSemisRepository.findAll().size();

        // Update the typeSemis
        TypeSemis updatedTypeSemis = typeSemisRepository.findById(typeSemis.getId()).get();
        // Disconnect from session so that the updates on updatedTypeSemis are not directly saved in db
        em.detach(updatedTypeSemis);
        updatedTypeSemis
            .description(UPDATED_DESCRIPTION);
        TypeSemisDTO typeSemisDTO = typeSemisMapper.toDto(updatedTypeSemis);

        restTypeSemisMockMvc.perform(put("/api/type-semis").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(typeSemisDTO)))
            .andExpect(status().isOk());

        // Validate the TypeSemis in the database
        List<TypeSemis> typeSemisList = typeSemisRepository.findAll();
        assertThat(typeSemisList).hasSize(databaseSizeBeforeUpdate);
        TypeSemis testTypeSemis = typeSemisList.get(typeSemisList.size() - 1);
        assertThat(testTypeSemis.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingTypeSemis() throws Exception {
        int databaseSizeBeforeUpdate = typeSemisRepository.findAll().size();

        // Create the TypeSemis
        TypeSemisDTO typeSemisDTO = typeSemisMapper.toDto(typeSemis);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeSemisMockMvc.perform(put("/api/type-semis").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(typeSemisDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TypeSemis in the database
        List<TypeSemis> typeSemisList = typeSemisRepository.findAll();
        assertThat(typeSemisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTypeSemis() throws Exception {
        // Initialize the database
        typeSemisRepository.saveAndFlush(typeSemis);

        int databaseSizeBeforeDelete = typeSemisRepository.findAll().size();

        // Delete the typeSemis
        restTypeSemisMockMvc.perform(delete("/api/type-semis/{id}", typeSemis.getId()).with(csrf())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypeSemis> typeSemisList = typeSemisRepository.findAll();
        assertThat(typeSemisList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
