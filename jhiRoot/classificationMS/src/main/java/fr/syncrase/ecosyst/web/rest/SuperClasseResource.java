package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.SuperClasse;
import fr.syncrase.ecosyst.repository.SuperClasseRepository;
import fr.syncrase.ecosyst.service.SuperClasseQueryService;
import fr.syncrase.ecosyst.service.SuperClasseService;
import fr.syncrase.ecosyst.service.criteria.SuperClasseCriteria;
import fr.syncrase.ecosyst.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.SuperClasse}.
 */
@RestController
@RequestMapping("/api")
public class SuperClasseResource {

    private final Logger log = LoggerFactory.getLogger(SuperClasseResource.class);

    private static final String ENTITY_NAME = "classificationMsSuperClasse";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SuperClasseService superClasseService;

    private final SuperClasseRepository superClasseRepository;

    private final SuperClasseQueryService superClasseQueryService;

    public SuperClasseResource(
        SuperClasseService superClasseService,
        SuperClasseRepository superClasseRepository,
        SuperClasseQueryService superClasseQueryService
    ) {
        this.superClasseService = superClasseService;
        this.superClasseRepository = superClasseRepository;
        this.superClasseQueryService = superClasseQueryService;
    }

    /**
     * {@code POST  /super-classes} : Create a new superClasse.
     *
     * @param superClasse the superClasse to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new superClasse, or with status {@code 400 (Bad Request)} if the superClasse has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/super-classes")
    public ResponseEntity<SuperClasse> createSuperClasse(@Valid @RequestBody SuperClasse superClasse) throws URISyntaxException {
        log.debug("REST request to save SuperClasse : {}", superClasse);
        if (superClasse.getId() != null) {
            throw new BadRequestAlertException("A new superClasse cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SuperClasse result = superClasseService.save(superClasse);
        return ResponseEntity
            .created(new URI("/api/super-classes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /super-classes/:id} : Updates an existing superClasse.
     *
     * @param id the id of the superClasse to save.
     * @param superClasse the superClasse to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated superClasse,
     * or with status {@code 400 (Bad Request)} if the superClasse is not valid,
     * or with status {@code 500 (Internal Server Error)} if the superClasse couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/super-classes/{id}")
    public ResponseEntity<SuperClasse> updateSuperClasse(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SuperClasse superClasse
    ) throws URISyntaxException {
        log.debug("REST request to update SuperClasse : {}, {}", id, superClasse);
        if (superClasse.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, superClasse.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!superClasseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SuperClasse result = superClasseService.save(superClasse);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, superClasse.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /super-classes/:id} : Partial updates given fields of an existing superClasse, field will ignore if it is null
     *
     * @param id the id of the superClasse to save.
     * @param superClasse the superClasse to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated superClasse,
     * or with status {@code 400 (Bad Request)} if the superClasse is not valid,
     * or with status {@code 404 (Not Found)} if the superClasse is not found,
     * or with status {@code 500 (Internal Server Error)} if the superClasse couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/super-classes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SuperClasse> partialUpdateSuperClasse(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SuperClasse superClasse
    ) throws URISyntaxException {
        log.debug("REST request to partial update SuperClasse partially : {}, {}", id, superClasse);
        if (superClasse.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, superClasse.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!superClasseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SuperClasse> result = superClasseService.partialUpdate(superClasse);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, superClasse.getId().toString())
        );
    }

    /**
     * {@code GET  /super-classes} : get all the superClasses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of superClasses in body.
     */
    @GetMapping("/super-classes")
    public ResponseEntity<List<SuperClasse>> getAllSuperClasses(SuperClasseCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SuperClasses by criteria: {}", criteria);
        Page<SuperClasse> page = superClasseQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /super-classes/count} : count all the superClasses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/super-classes/count")
    public ResponseEntity<Long> countSuperClasses(SuperClasseCriteria criteria) {
        log.debug("REST request to count SuperClasses by criteria: {}", criteria);
        return ResponseEntity.ok().body(superClasseQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /super-classes/:id} : get the "id" superClasse.
     *
     * @param id the id of the superClasse to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the superClasse, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/super-classes/{id}")
    public ResponseEntity<SuperClasse> getSuperClasse(@PathVariable Long id) {
        log.debug("REST request to get SuperClasse : {}", id);
        Optional<SuperClasse> superClasse = superClasseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(superClasse);
    }

    /**
     * {@code DELETE  /super-classes/:id} : delete the "id" superClasse.
     *
     * @param id the id of the superClasse to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/super-classes/{id}")
    public ResponseEntity<Void> deleteSuperClasse(@PathVariable Long id) {
        log.debug("REST request to delete SuperClasse : {}", id);
        superClasseService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
