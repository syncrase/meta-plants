package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.SousDivision;
import fr.syncrase.ecosyst.repository.SousDivisionRepository;
import fr.syncrase.ecosyst.service.SousDivisionQueryService;
import fr.syncrase.ecosyst.service.SousDivisionService;
import fr.syncrase.ecosyst.service.criteria.SousDivisionCriteria;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.SousDivision}.
 */
@RestController
@RequestMapping("/api")
public class SousDivisionResource {

    private final Logger log = LoggerFactory.getLogger(SousDivisionResource.class);

    private static final String ENTITY_NAME = "classificationMsSousDivision";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SousDivisionService sousDivisionService;

    private final SousDivisionRepository sousDivisionRepository;

    private final SousDivisionQueryService sousDivisionQueryService;

    public SousDivisionResource(
        SousDivisionService sousDivisionService,
        SousDivisionRepository sousDivisionRepository,
        SousDivisionQueryService sousDivisionQueryService
    ) {
        this.sousDivisionService = sousDivisionService;
        this.sousDivisionRepository = sousDivisionRepository;
        this.sousDivisionQueryService = sousDivisionQueryService;
    }

    /**
     * {@code POST  /sous-divisions} : Create a new sousDivision.
     *
     * @param sousDivision the sousDivision to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sousDivision, or with status {@code 400 (Bad Request)} if the sousDivision has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sous-divisions")
    public ResponseEntity<SousDivision> createSousDivision(@Valid @RequestBody SousDivision sousDivision) throws URISyntaxException {
        log.debug("REST request to save SousDivision : {}", sousDivision);
        if (sousDivision.getId() != null) {
            throw new BadRequestAlertException("A new sousDivision cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SousDivision result = sousDivisionService.save(sousDivision);
        return ResponseEntity
            .created(new URI("/api/sous-divisions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sous-divisions/:id} : Updates an existing sousDivision.
     *
     * @param id the id of the sousDivision to save.
     * @param sousDivision the sousDivision to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sousDivision,
     * or with status {@code 400 (Bad Request)} if the sousDivision is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sousDivision couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sous-divisions/{id}")
    public ResponseEntity<SousDivision> updateSousDivision(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SousDivision sousDivision
    ) throws URISyntaxException {
        log.debug("REST request to update SousDivision : {}, {}", id, sousDivision);
        if (sousDivision.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sousDivision.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sousDivisionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SousDivision result = sousDivisionService.save(sousDivision);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sousDivision.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sous-divisions/:id} : Partial updates given fields of an existing sousDivision, field will ignore if it is null
     *
     * @param id the id of the sousDivision to save.
     * @param sousDivision the sousDivision to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sousDivision,
     * or with status {@code 400 (Bad Request)} if the sousDivision is not valid,
     * or with status {@code 404 (Not Found)} if the sousDivision is not found,
     * or with status {@code 500 (Internal Server Error)} if the sousDivision couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sous-divisions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SousDivision> partialUpdateSousDivision(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SousDivision sousDivision
    ) throws URISyntaxException {
        log.debug("REST request to partial update SousDivision partially : {}, {}", id, sousDivision);
        if (sousDivision.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sousDivision.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sousDivisionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SousDivision> result = sousDivisionService.partialUpdate(sousDivision);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sousDivision.getId().toString())
        );
    }

    /**
     * {@code GET  /sous-divisions} : get all the sousDivisions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sousDivisions in body.
     */
    @GetMapping("/sous-divisions")
    public ResponseEntity<List<SousDivision>> getAllSousDivisions(SousDivisionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SousDivisions by criteria: {}", criteria);
        Page<SousDivision> page = sousDivisionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sous-divisions/count} : count all the sousDivisions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/sous-divisions/count")
    public ResponseEntity<Long> countSousDivisions(SousDivisionCriteria criteria) {
        log.debug("REST request to count SousDivisions by criteria: {}", criteria);
        return ResponseEntity.ok().body(sousDivisionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sous-divisions/:id} : get the "id" sousDivision.
     *
     * @param id the id of the sousDivision to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sousDivision, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sous-divisions/{id}")
    public ResponseEntity<SousDivision> getSousDivision(@PathVariable Long id) {
        log.debug("REST request to get SousDivision : {}", id);
        Optional<SousDivision> sousDivision = sousDivisionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sousDivision);
    }

    /**
     * {@code DELETE  /sous-divisions/:id} : delete the "id" sousDivision.
     *
     * @param id the id of the sousDivision to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sous-divisions/{id}")
    public ResponseEntity<Void> deleteSousDivision(@PathVariable Long id) {
        log.debug("REST request to delete SousDivision : {}", id);
        sousDivisionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
