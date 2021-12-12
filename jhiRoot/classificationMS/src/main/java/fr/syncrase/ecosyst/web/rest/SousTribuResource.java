package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.SousTribu;
import fr.syncrase.ecosyst.repository.SousTribuRepository;
import fr.syncrase.ecosyst.service.SousTribuQueryService;
import fr.syncrase.ecosyst.service.SousTribuService;
import fr.syncrase.ecosyst.service.criteria.SousTribuCriteria;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.SousTribu}.
 */
@RestController
@RequestMapping("/api")
public class SousTribuResource {

    private final Logger log = LoggerFactory.getLogger(SousTribuResource.class);

    private static final String ENTITY_NAME = "classificationMsSousTribu";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SousTribuService sousTribuService;

    private final SousTribuRepository sousTribuRepository;

    private final SousTribuQueryService sousTribuQueryService;

    public SousTribuResource(
        SousTribuService sousTribuService,
        SousTribuRepository sousTribuRepository,
        SousTribuQueryService sousTribuQueryService
    ) {
        this.sousTribuService = sousTribuService;
        this.sousTribuRepository = sousTribuRepository;
        this.sousTribuQueryService = sousTribuQueryService;
    }

    /**
     * {@code POST  /sous-tribus} : Create a new sousTribu.
     *
     * @param sousTribu the sousTribu to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sousTribu, or with status {@code 400 (Bad Request)} if the sousTribu has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sous-tribus")
    public ResponseEntity<SousTribu> createSousTribu(@Valid @RequestBody SousTribu sousTribu) throws URISyntaxException {
        log.debug("REST request to save SousTribu : {}", sousTribu);
        if (sousTribu.getId() != null) {
            throw new BadRequestAlertException("A new sousTribu cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SousTribu result = sousTribuService.save(sousTribu);
        return ResponseEntity
            .created(new URI("/api/sous-tribus/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sous-tribus/:id} : Updates an existing sousTribu.
     *
     * @param id the id of the sousTribu to save.
     * @param sousTribu the sousTribu to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sousTribu,
     * or with status {@code 400 (Bad Request)} if the sousTribu is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sousTribu couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sous-tribus/{id}")
    public ResponseEntity<SousTribu> updateSousTribu(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SousTribu sousTribu
    ) throws URISyntaxException {
        log.debug("REST request to update SousTribu : {}, {}", id, sousTribu);
        if (sousTribu.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sousTribu.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sousTribuRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SousTribu result = sousTribuService.save(sousTribu);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sousTribu.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sous-tribus/:id} : Partial updates given fields of an existing sousTribu, field will ignore if it is null
     *
     * @param id the id of the sousTribu to save.
     * @param sousTribu the sousTribu to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sousTribu,
     * or with status {@code 400 (Bad Request)} if the sousTribu is not valid,
     * or with status {@code 404 (Not Found)} if the sousTribu is not found,
     * or with status {@code 500 (Internal Server Error)} if the sousTribu couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sous-tribus/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SousTribu> partialUpdateSousTribu(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SousTribu sousTribu
    ) throws URISyntaxException {
        log.debug("REST request to partial update SousTribu partially : {}, {}", id, sousTribu);
        if (sousTribu.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sousTribu.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sousTribuRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SousTribu> result = sousTribuService.partialUpdate(sousTribu);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sousTribu.getId().toString())
        );
    }

    /**
     * {@code GET  /sous-tribus} : get all the sousTribus.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sousTribus in body.
     */
    @GetMapping("/sous-tribus")
    public ResponseEntity<List<SousTribu>> getAllSousTribus(SousTribuCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SousTribus by criteria: {}", criteria);
        Page<SousTribu> page = sousTribuQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sous-tribus/count} : count all the sousTribus.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/sous-tribus/count")
    public ResponseEntity<Long> countSousTribus(SousTribuCriteria criteria) {
        log.debug("REST request to count SousTribus by criteria: {}", criteria);
        return ResponseEntity.ok().body(sousTribuQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sous-tribus/:id} : get the "id" sousTribu.
     *
     * @param id the id of the sousTribu to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sousTribu, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sous-tribus/{id}")
    public ResponseEntity<SousTribu> getSousTribu(@PathVariable Long id) {
        log.debug("REST request to get SousTribu : {}", id);
        Optional<SousTribu> sousTribu = sousTribuService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sousTribu);
    }

    /**
     * {@code DELETE  /sous-tribus/:id} : delete the "id" sousTribu.
     *
     * @param id the id of the sousTribu to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sous-tribus/{id}")
    public ResponseEntity<Void> deleteSousTribu(@PathVariable Long id) {
        log.debug("REST request to delete SousTribu : {}", id);
        sousTribuService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
