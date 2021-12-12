package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.SuperDivision;
import fr.syncrase.ecosyst.repository.SuperDivisionRepository;
import fr.syncrase.ecosyst.service.SuperDivisionQueryService;
import fr.syncrase.ecosyst.service.SuperDivisionService;
import fr.syncrase.ecosyst.service.criteria.SuperDivisionCriteria;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.SuperDivision}.
 */
@RestController
@RequestMapping("/api")
public class SuperDivisionResource {

    private final Logger log = LoggerFactory.getLogger(SuperDivisionResource.class);

    private static final String ENTITY_NAME = "classificationMsSuperDivision";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SuperDivisionService superDivisionService;

    private final SuperDivisionRepository superDivisionRepository;

    private final SuperDivisionQueryService superDivisionQueryService;

    public SuperDivisionResource(
        SuperDivisionService superDivisionService,
        SuperDivisionRepository superDivisionRepository,
        SuperDivisionQueryService superDivisionQueryService
    ) {
        this.superDivisionService = superDivisionService;
        this.superDivisionRepository = superDivisionRepository;
        this.superDivisionQueryService = superDivisionQueryService;
    }

    /**
     * {@code POST  /super-divisions} : Create a new superDivision.
     *
     * @param superDivision the superDivision to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new superDivision, or with status {@code 400 (Bad Request)} if the superDivision has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/super-divisions")
    public ResponseEntity<SuperDivision> createSuperDivision(@Valid @RequestBody SuperDivision superDivision) throws URISyntaxException {
        log.debug("REST request to save SuperDivision : {}", superDivision);
        if (superDivision.getId() != null) {
            throw new BadRequestAlertException("A new superDivision cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SuperDivision result = superDivisionService.save(superDivision);
        return ResponseEntity
            .created(new URI("/api/super-divisions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /super-divisions/:id} : Updates an existing superDivision.
     *
     * @param id the id of the superDivision to save.
     * @param superDivision the superDivision to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated superDivision,
     * or with status {@code 400 (Bad Request)} if the superDivision is not valid,
     * or with status {@code 500 (Internal Server Error)} if the superDivision couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/super-divisions/{id}")
    public ResponseEntity<SuperDivision> updateSuperDivision(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SuperDivision superDivision
    ) throws URISyntaxException {
        log.debug("REST request to update SuperDivision : {}, {}", id, superDivision);
        if (superDivision.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, superDivision.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!superDivisionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SuperDivision result = superDivisionService.save(superDivision);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, superDivision.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /super-divisions/:id} : Partial updates given fields of an existing superDivision, field will ignore if it is null
     *
     * @param id the id of the superDivision to save.
     * @param superDivision the superDivision to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated superDivision,
     * or with status {@code 400 (Bad Request)} if the superDivision is not valid,
     * or with status {@code 404 (Not Found)} if the superDivision is not found,
     * or with status {@code 500 (Internal Server Error)} if the superDivision couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/super-divisions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SuperDivision> partialUpdateSuperDivision(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SuperDivision superDivision
    ) throws URISyntaxException {
        log.debug("REST request to partial update SuperDivision partially : {}, {}", id, superDivision);
        if (superDivision.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, superDivision.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!superDivisionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SuperDivision> result = superDivisionService.partialUpdate(superDivision);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, superDivision.getId().toString())
        );
    }

    /**
     * {@code GET  /super-divisions} : get all the superDivisions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of superDivisions in body.
     */
    @GetMapping("/super-divisions")
    public ResponseEntity<List<SuperDivision>> getAllSuperDivisions(SuperDivisionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SuperDivisions by criteria: {}", criteria);
        Page<SuperDivision> page = superDivisionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /super-divisions/count} : count all the superDivisions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/super-divisions/count")
    public ResponseEntity<Long> countSuperDivisions(SuperDivisionCriteria criteria) {
        log.debug("REST request to count SuperDivisions by criteria: {}", criteria);
        return ResponseEntity.ok().body(superDivisionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /super-divisions/:id} : get the "id" superDivision.
     *
     * @param id the id of the superDivision to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the superDivision, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/super-divisions/{id}")
    public ResponseEntity<SuperDivision> getSuperDivision(@PathVariable Long id) {
        log.debug("REST request to get SuperDivision : {}", id);
        Optional<SuperDivision> superDivision = superDivisionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(superDivision);
    }

    /**
     * {@code DELETE  /super-divisions/:id} : delete the "id" superDivision.
     *
     * @param id the id of the superDivision to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/super-divisions/{id}")
    public ResponseEntity<Void> deleteSuperDivision(@PathVariable Long id) {
        log.debug("REST request to delete SuperDivision : {}", id);
        superDivisionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
