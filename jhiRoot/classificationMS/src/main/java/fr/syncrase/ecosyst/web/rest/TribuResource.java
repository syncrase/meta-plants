package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.Tribu;
import fr.syncrase.ecosyst.repository.TribuRepository;
import fr.syncrase.ecosyst.service.TribuQueryService;
import fr.syncrase.ecosyst.service.TribuService;
import fr.syncrase.ecosyst.service.criteria.TribuCriteria;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.Tribu}.
 */
@RestController
@RequestMapping("/api")
public class TribuResource {

    private final Logger log = LoggerFactory.getLogger(TribuResource.class);

    private static final String ENTITY_NAME = "classificationMsTribu";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TribuService tribuService;

    private final TribuRepository tribuRepository;

    private final TribuQueryService tribuQueryService;

    public TribuResource(TribuService tribuService, TribuRepository tribuRepository, TribuQueryService tribuQueryService) {
        this.tribuService = tribuService;
        this.tribuRepository = tribuRepository;
        this.tribuQueryService = tribuQueryService;
    }

    /**
     * {@code POST  /tribus} : Create a new tribu.
     *
     * @param tribu the tribu to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tribu, or with status {@code 400 (Bad Request)} if the tribu has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tribus")
    public ResponseEntity<Tribu> createTribu(@Valid @RequestBody Tribu tribu) throws URISyntaxException {
        log.debug("REST request to save Tribu : {}", tribu);
        if (tribu.getId() != null) {
            throw new BadRequestAlertException("A new tribu cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Tribu result = tribuService.save(tribu);
        return ResponseEntity
            .created(new URI("/api/tribus/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tribus/:id} : Updates an existing tribu.
     *
     * @param id the id of the tribu to save.
     * @param tribu the tribu to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tribu,
     * or with status {@code 400 (Bad Request)} if the tribu is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tribu couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tribus/{id}")
    public ResponseEntity<Tribu> updateTribu(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Tribu tribu)
        throws URISyntaxException {
        log.debug("REST request to update Tribu : {}, {}", id, tribu);
        if (tribu.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tribu.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tribuRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Tribu result = tribuService.save(tribu);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tribu.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tribus/:id} : Partial updates given fields of an existing tribu, field will ignore if it is null
     *
     * @param id the id of the tribu to save.
     * @param tribu the tribu to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tribu,
     * or with status {@code 400 (Bad Request)} if the tribu is not valid,
     * or with status {@code 404 (Not Found)} if the tribu is not found,
     * or with status {@code 500 (Internal Server Error)} if the tribu couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tribus/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Tribu> partialUpdateTribu(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Tribu tribu
    ) throws URISyntaxException {
        log.debug("REST request to partial update Tribu partially : {}, {}", id, tribu);
        if (tribu.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tribu.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tribuRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Tribu> result = tribuService.partialUpdate(tribu);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tribu.getId().toString())
        );
    }

    /**
     * {@code GET  /tribus} : get all the tribus.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tribus in body.
     */
    @GetMapping("/tribus")
    public ResponseEntity<List<Tribu>> getAllTribus(TribuCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Tribus by criteria: {}", criteria);
        Page<Tribu> page = tribuQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tribus/count} : count all the tribus.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tribus/count")
    public ResponseEntity<Long> countTribus(TribuCriteria criteria) {
        log.debug("REST request to count Tribus by criteria: {}", criteria);
        return ResponseEntity.ok().body(tribuQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tribus/:id} : get the "id" tribu.
     *
     * @param id the id of the tribu to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tribu, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tribus/{id}")
    public ResponseEntity<Tribu> getTribu(@PathVariable Long id) {
        log.debug("REST request to get Tribu : {}", id);
        Optional<Tribu> tribu = tribuService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tribu);
    }

    /**
     * {@code DELETE  /tribus/:id} : delete the "id" tribu.
     *
     * @param id the id of the tribu to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tribus/{id}")
    public ResponseEntity<Void> deleteTribu(@PathVariable Long id) {
        log.debug("REST request to delete Tribu : {}", id);
        tribuService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
