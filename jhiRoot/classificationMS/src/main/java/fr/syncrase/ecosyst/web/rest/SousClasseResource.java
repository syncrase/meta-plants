package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.SousClasse;
import fr.syncrase.ecosyst.repository.SousClasseRepository;
import fr.syncrase.ecosyst.service.SousClasseQueryService;
import fr.syncrase.ecosyst.service.SousClasseService;
import fr.syncrase.ecosyst.service.criteria.SousClasseCriteria;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.SousClasse}.
 */
@RestController
@RequestMapping("/api")
public class SousClasseResource {

    private final Logger log = LoggerFactory.getLogger(SousClasseResource.class);

    private static final String ENTITY_NAME = "classificationMsSousClasse";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SousClasseService sousClasseService;

    private final SousClasseRepository sousClasseRepository;

    private final SousClasseQueryService sousClasseQueryService;

    public SousClasseResource(
        SousClasseService sousClasseService,
        SousClasseRepository sousClasseRepository,
        SousClasseQueryService sousClasseQueryService
    ) {
        this.sousClasseService = sousClasseService;
        this.sousClasseRepository = sousClasseRepository;
        this.sousClasseQueryService = sousClasseQueryService;
    }

    /**
     * {@code POST  /sous-classes} : Create a new sousClasse.
     *
     * @param sousClasse the sousClasse to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sousClasse, or with status {@code 400 (Bad Request)} if the sousClasse has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sous-classes")
    public ResponseEntity<SousClasse> createSousClasse(@Valid @RequestBody SousClasse sousClasse) throws URISyntaxException {
        log.debug("REST request to save SousClasse : {}", sousClasse);
        if (sousClasse.getId() != null) {
            throw new BadRequestAlertException("A new sousClasse cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SousClasse result = sousClasseService.save(sousClasse);
        return ResponseEntity
            .created(new URI("/api/sous-classes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sous-classes/:id} : Updates an existing sousClasse.
     *
     * @param id the id of the sousClasse to save.
     * @param sousClasse the sousClasse to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sousClasse,
     * or with status {@code 400 (Bad Request)} if the sousClasse is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sousClasse couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sous-classes/{id}")
    public ResponseEntity<SousClasse> updateSousClasse(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SousClasse sousClasse
    ) throws URISyntaxException {
        log.debug("REST request to update SousClasse : {}, {}", id, sousClasse);
        if (sousClasse.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sousClasse.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sousClasseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SousClasse result = sousClasseService.save(sousClasse);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sousClasse.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sous-classes/:id} : Partial updates given fields of an existing sousClasse, field will ignore if it is null
     *
     * @param id the id of the sousClasse to save.
     * @param sousClasse the sousClasse to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sousClasse,
     * or with status {@code 400 (Bad Request)} if the sousClasse is not valid,
     * or with status {@code 404 (Not Found)} if the sousClasse is not found,
     * or with status {@code 500 (Internal Server Error)} if the sousClasse couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sous-classes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SousClasse> partialUpdateSousClasse(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SousClasse sousClasse
    ) throws URISyntaxException {
        log.debug("REST request to partial update SousClasse partially : {}, {}", id, sousClasse);
        if (sousClasse.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sousClasse.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sousClasseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SousClasse> result = sousClasseService.partialUpdate(sousClasse);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sousClasse.getId().toString())
        );
    }

    /**
     * {@code GET  /sous-classes} : get all the sousClasses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sousClasses in body.
     */
    @GetMapping("/sous-classes")
    public ResponseEntity<List<SousClasse>> getAllSousClasses(SousClasseCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SousClasses by criteria: {}", criteria);
        Page<SousClasse> page = sousClasseQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sous-classes/count} : count all the sousClasses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/sous-classes/count")
    public ResponseEntity<Long> countSousClasses(SousClasseCriteria criteria) {
        log.debug("REST request to count SousClasses by criteria: {}", criteria);
        return ResponseEntity.ok().body(sousClasseQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sous-classes/:id} : get the "id" sousClasse.
     *
     * @param id the id of the sousClasse to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sousClasse, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sous-classes/{id}")
    public ResponseEntity<SousClasse> getSousClasse(@PathVariable Long id) {
        log.debug("REST request to get SousClasse : {}", id);
        Optional<SousClasse> sousClasse = sousClasseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sousClasse);
    }

    /**
     * {@code DELETE  /sous-classes/:id} : delete the "id" sousClasse.
     *
     * @param id the id of the sousClasse to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sous-classes/{id}")
    public ResponseEntity<Void> deleteSousClasse(@PathVariable Long id) {
        log.debug("REST request to delete SousClasse : {}", id);
        sousClasseService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
