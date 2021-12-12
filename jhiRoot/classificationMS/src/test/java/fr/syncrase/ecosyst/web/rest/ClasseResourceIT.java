package fr.syncrase.ecosyst.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.syncrase.ecosyst.IntegrationTest;
import fr.syncrase.ecosyst.domain.Classe;
import fr.syncrase.ecosyst.domain.Classe;
import fr.syncrase.ecosyst.domain.SousClasse;
import fr.syncrase.ecosyst.domain.SuperClasse;
import fr.syncrase.ecosyst.repository.ClasseRepository;
import fr.syncrase.ecosyst.service.criteria.ClasseCriteria;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ClasseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClasseResourceIT {

    private static final String DEFAULT_NOM_FR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_LATIN = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LATIN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/classes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClasseRepository classeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClasseMockMvc;

    private Classe classe;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Classe createEntity(EntityManager em) {
        Classe classe = new Classe().nomFr(DEFAULT_NOM_FR).nomLatin(DEFAULT_NOM_LATIN);
        return classe;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Classe createUpdatedEntity(EntityManager em) {
        Classe classe = new Classe().nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);
        return classe;
    }

    @BeforeEach
    public void initTest() {
        classe = createEntity(em);
    }

    @Test
    @Transactional
    void createClasse() throws Exception {
        int databaseSizeBeforeCreate = classeRepository.findAll().size();
        // Create the Classe
        restClasseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classe)))
            .andExpect(status().isCreated());

        // Validate the Classe in the database
        List<Classe> classeList = classeRepository.findAll();
        assertThat(classeList).hasSize(databaseSizeBeforeCreate + 1);
        Classe testClasse = classeList.get(classeList.size() - 1);
        assertThat(testClasse.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testClasse.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void createClasseWithExistingId() throws Exception {
        // Create the Classe with an existing ID
        classe.setId(1L);

        int databaseSizeBeforeCreate = classeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClasseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classe)))
            .andExpect(status().isBadRequest());

        // Validate the Classe in the database
        List<Classe> classeList = classeRepository.findAll();
        assertThat(classeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomFrIsRequired() throws Exception {
        int databaseSizeBeforeTest = classeRepository.findAll().size();
        // set the field null
        classe.setNomFr(null);

        // Create the Classe, which fails.

        restClasseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classe)))
            .andExpect(status().isBadRequest());

        List<Classe> classeList = classeRepository.findAll();
        assertThat(classeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClasses() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);

        // Get all the classeList
        restClasseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classe.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));
    }

    @Test
    @Transactional
    void getClasse() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);

        // Get the classe
        restClasseMockMvc
            .perform(get(ENTITY_API_URL_ID, classe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(classe.getId().intValue()))
            .andExpect(jsonPath("$.nomFr").value(DEFAULT_NOM_FR))
            .andExpect(jsonPath("$.nomLatin").value(DEFAULT_NOM_LATIN));
    }

    @Test
    @Transactional
    void getClassesByIdFiltering() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);

        Long id = classe.getId();

        defaultClasseShouldBeFound("id.equals=" + id);
        defaultClasseShouldNotBeFound("id.notEquals=" + id);

        defaultClasseShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultClasseShouldNotBeFound("id.greaterThan=" + id);

        defaultClasseShouldBeFound("id.lessThanOrEqual=" + id);
        defaultClasseShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllClassesByNomFrIsEqualToSomething() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);

        // Get all the classeList where nomFr equals to DEFAULT_NOM_FR
        defaultClasseShouldBeFound("nomFr.equals=" + DEFAULT_NOM_FR);

        // Get all the classeList where nomFr equals to UPDATED_NOM_FR
        defaultClasseShouldNotBeFound("nomFr.equals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllClassesByNomFrIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);

        // Get all the classeList where nomFr not equals to DEFAULT_NOM_FR
        defaultClasseShouldNotBeFound("nomFr.notEquals=" + DEFAULT_NOM_FR);

        // Get all the classeList where nomFr not equals to UPDATED_NOM_FR
        defaultClasseShouldBeFound("nomFr.notEquals=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllClassesByNomFrIsInShouldWork() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);

        // Get all the classeList where nomFr in DEFAULT_NOM_FR or UPDATED_NOM_FR
        defaultClasseShouldBeFound("nomFr.in=" + DEFAULT_NOM_FR + "," + UPDATED_NOM_FR);

        // Get all the classeList where nomFr equals to UPDATED_NOM_FR
        defaultClasseShouldNotBeFound("nomFr.in=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllClassesByNomFrIsNullOrNotNull() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);

        // Get all the classeList where nomFr is not null
        defaultClasseShouldBeFound("nomFr.specified=true");

        // Get all the classeList where nomFr is null
        defaultClasseShouldNotBeFound("nomFr.specified=false");
    }

    @Test
    @Transactional
    void getAllClassesByNomFrContainsSomething() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);

        // Get all the classeList where nomFr contains DEFAULT_NOM_FR
        defaultClasseShouldBeFound("nomFr.contains=" + DEFAULT_NOM_FR);

        // Get all the classeList where nomFr contains UPDATED_NOM_FR
        defaultClasseShouldNotBeFound("nomFr.contains=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllClassesByNomFrNotContainsSomething() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);

        // Get all the classeList where nomFr does not contain DEFAULT_NOM_FR
        defaultClasseShouldNotBeFound("nomFr.doesNotContain=" + DEFAULT_NOM_FR);

        // Get all the classeList where nomFr does not contain UPDATED_NOM_FR
        defaultClasseShouldBeFound("nomFr.doesNotContain=" + UPDATED_NOM_FR);
    }

    @Test
    @Transactional
    void getAllClassesByNomLatinIsEqualToSomething() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);

        // Get all the classeList where nomLatin equals to DEFAULT_NOM_LATIN
        defaultClasseShouldBeFound("nomLatin.equals=" + DEFAULT_NOM_LATIN);

        // Get all the classeList where nomLatin equals to UPDATED_NOM_LATIN
        defaultClasseShouldNotBeFound("nomLatin.equals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllClassesByNomLatinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);

        // Get all the classeList where nomLatin not equals to DEFAULT_NOM_LATIN
        defaultClasseShouldNotBeFound("nomLatin.notEquals=" + DEFAULT_NOM_LATIN);

        // Get all the classeList where nomLatin not equals to UPDATED_NOM_LATIN
        defaultClasseShouldBeFound("nomLatin.notEquals=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllClassesByNomLatinIsInShouldWork() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);

        // Get all the classeList where nomLatin in DEFAULT_NOM_LATIN or UPDATED_NOM_LATIN
        defaultClasseShouldBeFound("nomLatin.in=" + DEFAULT_NOM_LATIN + "," + UPDATED_NOM_LATIN);

        // Get all the classeList where nomLatin equals to UPDATED_NOM_LATIN
        defaultClasseShouldNotBeFound("nomLatin.in=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllClassesByNomLatinIsNullOrNotNull() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);

        // Get all the classeList where nomLatin is not null
        defaultClasseShouldBeFound("nomLatin.specified=true");

        // Get all the classeList where nomLatin is null
        defaultClasseShouldNotBeFound("nomLatin.specified=false");
    }

    @Test
    @Transactional
    void getAllClassesByNomLatinContainsSomething() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);

        // Get all the classeList where nomLatin contains DEFAULT_NOM_LATIN
        defaultClasseShouldBeFound("nomLatin.contains=" + DEFAULT_NOM_LATIN);

        // Get all the classeList where nomLatin contains UPDATED_NOM_LATIN
        defaultClasseShouldNotBeFound("nomLatin.contains=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllClassesByNomLatinNotContainsSomething() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);

        // Get all the classeList where nomLatin does not contain DEFAULT_NOM_LATIN
        defaultClasseShouldNotBeFound("nomLatin.doesNotContain=" + DEFAULT_NOM_LATIN);

        // Get all the classeList where nomLatin does not contain UPDATED_NOM_LATIN
        defaultClasseShouldBeFound("nomLatin.doesNotContain=" + UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void getAllClassesBySousClassesIsEqualToSomething() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);
        SousClasse sousClasses;
        if (TestUtil.findAll(em, SousClasse.class).isEmpty()) {
            sousClasses = SousClasseResourceIT.createEntity(em);
            em.persist(sousClasses);
            em.flush();
        } else {
            sousClasses = TestUtil.findAll(em, SousClasse.class).get(0);
        }
        em.persist(sousClasses);
        em.flush();
        classe.addSousClasses(sousClasses);
        classeRepository.saveAndFlush(classe);
        Long sousClassesId = sousClasses.getId();

        // Get all the classeList where sousClasses equals to sousClassesId
        defaultClasseShouldBeFound("sousClassesId.equals=" + sousClassesId);

        // Get all the classeList where sousClasses equals to (sousClassesId + 1)
        defaultClasseShouldNotBeFound("sousClassesId.equals=" + (sousClassesId + 1));
    }

    @Test
    @Transactional
    void getAllClassesBySynonymesIsEqualToSomething() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);
        Classe synonymes;
        if (TestUtil.findAll(em, Classe.class).isEmpty()) {
            synonymes = ClasseResourceIT.createEntity(em);
            em.persist(synonymes);
            em.flush();
        } else {
            synonymes = TestUtil.findAll(em, Classe.class).get(0);
        }
        em.persist(synonymes);
        em.flush();
        classe.addSynonymes(synonymes);
        classeRepository.saveAndFlush(classe);
        Long synonymesId = synonymes.getId();

        // Get all the classeList where synonymes equals to synonymesId
        defaultClasseShouldBeFound("synonymesId.equals=" + synonymesId);

        // Get all the classeList where synonymes equals to (synonymesId + 1)
        defaultClasseShouldNotBeFound("synonymesId.equals=" + (synonymesId + 1));
    }

    @Test
    @Transactional
    void getAllClassesBySuperClasseIsEqualToSomething() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);
        SuperClasse superClasse;
        if (TestUtil.findAll(em, SuperClasse.class).isEmpty()) {
            superClasse = SuperClasseResourceIT.createEntity(em);
            em.persist(superClasse);
            em.flush();
        } else {
            superClasse = TestUtil.findAll(em, SuperClasse.class).get(0);
        }
        em.persist(superClasse);
        em.flush();
        classe.setSuperClasse(superClasse);
        classeRepository.saveAndFlush(classe);
        Long superClasseId = superClasse.getId();

        // Get all the classeList where superClasse equals to superClasseId
        defaultClasseShouldBeFound("superClasseId.equals=" + superClasseId);

        // Get all the classeList where superClasse equals to (superClasseId + 1)
        defaultClasseShouldNotBeFound("superClasseId.equals=" + (superClasseId + 1));
    }

    @Test
    @Transactional
    void getAllClassesByClasseIsEqualToSomething() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);
        Classe classe;
        if (TestUtil.findAll(em, Classe.class).isEmpty()) {
            classe = ClasseResourceIT.createEntity(em);
            em.persist(classe);
            em.flush();
        } else {
            classe = TestUtil.findAll(em, Classe.class).get(0);
        }
        em.persist(classe);
        em.flush();
        classe.setClasse(classe);
        classeRepository.saveAndFlush(classe);
        Long classeId = classe.getId();

        // Get all the classeList where classe equals to classeId
        defaultClasseShouldBeFound("classeId.equals=" + classeId);

        // Get all the classeList where classe equals to (classeId + 1)
        defaultClasseShouldNotBeFound("classeId.equals=" + (classeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClasseShouldBeFound(String filter) throws Exception {
        restClasseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classe.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFr").value(hasItem(DEFAULT_NOM_FR)))
            .andExpect(jsonPath("$.[*].nomLatin").value(hasItem(DEFAULT_NOM_LATIN)));

        // Check, that the count call also returns 1
        restClasseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClasseShouldNotBeFound(String filter) throws Exception {
        restClasseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClasseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingClasse() throws Exception {
        // Get the classe
        restClasseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewClasse() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);

        int databaseSizeBeforeUpdate = classeRepository.findAll().size();

        // Update the classe
        Classe updatedClasse = classeRepository.findById(classe.getId()).get();
        // Disconnect from session so that the updates on updatedClasse are not directly saved in db
        em.detach(updatedClasse);
        updatedClasse.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restClasseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedClasse.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedClasse))
            )
            .andExpect(status().isOk());

        // Validate the Classe in the database
        List<Classe> classeList = classeRepository.findAll();
        assertThat(classeList).hasSize(databaseSizeBeforeUpdate);
        Classe testClasse = classeList.get(classeList.size() - 1);
        assertThat(testClasse.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testClasse.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void putNonExistingClasse() throws Exception {
        int databaseSizeBeforeUpdate = classeRepository.findAll().size();
        classe.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClasseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, classe.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classe))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classe in the database
        List<Classe> classeList = classeRepository.findAll();
        assertThat(classeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClasse() throws Exception {
        int databaseSizeBeforeUpdate = classeRepository.findAll().size();
        classe.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClasseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(classe))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classe in the database
        List<Classe> classeList = classeRepository.findAll();
        assertThat(classeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClasse() throws Exception {
        int databaseSizeBeforeUpdate = classeRepository.findAll().size();
        classe.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClasseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(classe)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Classe in the database
        List<Classe> classeList = classeRepository.findAll();
        assertThat(classeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClasseWithPatch() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);

        int databaseSizeBeforeUpdate = classeRepository.findAll().size();

        // Update the classe using partial update
        Classe partialUpdatedClasse = new Classe();
        partialUpdatedClasse.setId(classe.getId());

        restClasseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClasse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClasse))
            )
            .andExpect(status().isOk());

        // Validate the Classe in the database
        List<Classe> classeList = classeRepository.findAll();
        assertThat(classeList).hasSize(databaseSizeBeforeUpdate);
        Classe testClasse = classeList.get(classeList.size() - 1);
        assertThat(testClasse.getNomFr()).isEqualTo(DEFAULT_NOM_FR);
        assertThat(testClasse.getNomLatin()).isEqualTo(DEFAULT_NOM_LATIN);
    }

    @Test
    @Transactional
    void fullUpdateClasseWithPatch() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);

        int databaseSizeBeforeUpdate = classeRepository.findAll().size();

        // Update the classe using partial update
        Classe partialUpdatedClasse = new Classe();
        partialUpdatedClasse.setId(classe.getId());

        partialUpdatedClasse.nomFr(UPDATED_NOM_FR).nomLatin(UPDATED_NOM_LATIN);

        restClasseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClasse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClasse))
            )
            .andExpect(status().isOk());

        // Validate the Classe in the database
        List<Classe> classeList = classeRepository.findAll();
        assertThat(classeList).hasSize(databaseSizeBeforeUpdate);
        Classe testClasse = classeList.get(classeList.size() - 1);
        assertThat(testClasse.getNomFr()).isEqualTo(UPDATED_NOM_FR);
        assertThat(testClasse.getNomLatin()).isEqualTo(UPDATED_NOM_LATIN);
    }

    @Test
    @Transactional
    void patchNonExistingClasse() throws Exception {
        int databaseSizeBeforeUpdate = classeRepository.findAll().size();
        classe.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClasseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, classe.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(classe))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classe in the database
        List<Classe> classeList = classeRepository.findAll();
        assertThat(classeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClasse() throws Exception {
        int databaseSizeBeforeUpdate = classeRepository.findAll().size();
        classe.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClasseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(classe))
            )
            .andExpect(status().isBadRequest());

        // Validate the Classe in the database
        List<Classe> classeList = classeRepository.findAll();
        assertThat(classeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClasse() throws Exception {
        int databaseSizeBeforeUpdate = classeRepository.findAll().size();
        classe.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClasseMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(classe)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Classe in the database
        List<Classe> classeList = classeRepository.findAll();
        assertThat(classeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClasse() throws Exception {
        // Initialize the database
        classeRepository.saveAndFlush(classe);

        int databaseSizeBeforeDelete = classeRepository.findAll().size();

        // Delete the classe
        restClasseMockMvc
            .perform(delete(ENTITY_API_URL_ID, classe.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Classe> classeList = classeRepository.findAll();
        assertThat(classeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
