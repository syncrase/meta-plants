package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.MicroEmbranchement;
import fr.syncrase.ecosyst.repository.MicroEmbranchementRepository;
import fr.syncrase.ecosyst.service.MicroEmbranchementQueryService;
import fr.syncrase.ecosyst.service.MicroEmbranchementService;
import fr.syncrase.ecosyst.service.criteria.MicroEmbranchementCriteria;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.MicroEmbranchement}.
 */
@RestController
@RequestMapping("/api")
public class MicroEmbranchementResource {

    private final Logger log = LoggerFactory.getLogger(MicroEmbranchementResource.class);

    private static final String ENTITY_NAME = "classificationMsMicroEmbranchement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MicroEmbranchementService microEmbranchementService;

    private final MicroEmbranchementRepository microEmbranchementRepository;

    private final MicroEmbranchementQueryService microEmbranchementQueryService;

    public MicroEmbranchementResource(
        MicroEmbranchementService microEmbranchementService,
        MicroEmbranchementRepository microEmbranchementRepository,
        MicroEmbranchementQueryService microEmbranchementQueryService
    ) {
        this.microEmbranchementService = microEmbranchementService;
        this.microEmbranchementRepository = microEmbranchementRepository;
        this.microEmbranchementQueryService = microEmbranchementQueryService;
    }

    /**
     * {@code POST  /micro-embranchements} : Create a new microEmbranchement.
     *
     * @param microEmbranchement the microEmbranchement to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new microEmbranchement, or with status {@code 400 (Bad Request)} if the microEmbranchement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/micro-embranchements")
    public ResponseEntity<MicroEmbranchement> createMicroEmbranchement(@Valid @RequestBody MicroEmbranchement microEmbranchement)
        throws URISyntaxException {
        log.debug("REST request to save MicroEmbranchement : {}", microEmbranchement);
        if (microEmbranchement.getId() != null) {
            throw new BadRequestAlertException("A new microEmbranchement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MicroEmbranchement result = microEmbranchementService.save(microEmbranchement);
        return ResponseEntity
            .created(new URI("/api/micro-embranchements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /micro-embranchements/:id} : Updates an existing microEmbranchement.
     *
     * @param id the id of the microEmbranchement to save.
     * @param microEmbranchement the microEmbranchement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated microEmbranchement,
     * or with status {@code 400 (Bad Request)} if the microEmbranchement is not valid,
     * or with status {@code 500 (Internal Server Error)} if the microEmbranchement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/micro-embranchements/{id}")
    public ResponseEntity<MicroEmbranchement> updateMicroEmbranchement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MicroEmbranchement microEmbranchement
    ) throws URISyntaxException {
        log.debug("REST request to update MicroEmbranchement : {}, {}", id, microEmbranchement);
        if (microEmbranchement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, microEmbranchement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!microEmbranchementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MicroEmbranchement result = microEmbranchementService.save(microEmbranchement);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, microEmbranchement.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /micro-embranchements/:id} : Partial updates given fields of an existing microEmbranchement, field will ignore if it is null
     *
     * @param id the id of the microEmbranchement to save.
     * @param microEmbranchement the microEmbranchement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated microEmbranchement,
     * or with status {@code 400 (Bad Request)} if the microEmbranchement is not valid,
     * or with status {@code 404 (Not Found)} if the microEmbranchement is not found,
     * or with status {@code 500 (Internal Server Error)} if the microEmbranchement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/micro-embranchements/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MicroEmbranchement> partialUpdateMicroEmbranchement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MicroEmbranchement microEmbranchement
    ) throws URISyntaxException {
        log.debug("REST request to partial update MicroEmbranchement partially : {}, {}", id, microEmbranchement);
        if (microEmbranchement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, microEmbranchement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!microEmbranchementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MicroEmbranchement> result = microEmbranchementService.partialUpdate(microEmbranchement);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, microEmbranchement.getId().toString())
        );
    }

    /**
     * {@code GET  /micro-embranchements} : get all the microEmbranchements.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of microEmbranchements in body.
     */
    @GetMapping("/micro-embranchements")
    public ResponseEntity<List<MicroEmbranchement>> getAllMicroEmbranchements(MicroEmbranchementCriteria criteria, Pageable pageable) {
        log.debug("REST request to get MicroEmbranchements by criteria: {}", criteria);
        Page<MicroEmbranchement> page = microEmbranchementQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /micro-embranchements/count} : count all the microEmbranchements.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/micro-embranchements/count")
    public ResponseEntity<Long> countMicroEmbranchements(MicroEmbranchementCriteria criteria) {
        log.debug("REST request to count MicroEmbranchements by criteria: {}", criteria);
        return ResponseEntity.ok().body(microEmbranchementQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /micro-embranchements/:id} : get the "id" microEmbranchement.
     *
     * @param id the id of the microEmbranchement to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the microEmbranchement, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/micro-embranchements/{id}")
    public ResponseEntity<MicroEmbranchement> getMicroEmbranchement(@PathVariable Long id) {
        log.debug("REST request to get MicroEmbranchement : {}", id);
        Optional<MicroEmbranchement> microEmbranchement = microEmbranchementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(microEmbranchement);
    }

    /**
     * {@code DELETE  /micro-embranchements/:id} : delete the "id" microEmbranchement.
     *
     * @param id the id of the microEmbranchement to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/micro-embranchements/{id}")
    public ResponseEntity<Void> deleteMicroEmbranchement(@PathVariable Long id) {
        log.debug("REST request to delete MicroEmbranchement : {}", id);
        microEmbranchementService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
