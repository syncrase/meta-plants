package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.SousVariete;
import fr.syncrase.ecosyst.repository.SousVarieteRepository;
import fr.syncrase.ecosyst.service.SousVarieteQueryService;
import fr.syncrase.ecosyst.service.SousVarieteService;
import fr.syncrase.ecosyst.service.criteria.SousVarieteCriteria;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.SousVariete}.
 */
@RestController
@RequestMapping("/api")
public class SousVarieteResource {

    private final Logger log = LoggerFactory.getLogger(SousVarieteResource.class);

    private static final String ENTITY_NAME = "classificationMsSousVariete";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SousVarieteService sousVarieteService;

    private final SousVarieteRepository sousVarieteRepository;

    private final SousVarieteQueryService sousVarieteQueryService;

    public SousVarieteResource(
        SousVarieteService sousVarieteService,
        SousVarieteRepository sousVarieteRepository,
        SousVarieteQueryService sousVarieteQueryService
    ) {
        this.sousVarieteService = sousVarieteService;
        this.sousVarieteRepository = sousVarieteRepository;
        this.sousVarieteQueryService = sousVarieteQueryService;
    }

    /**
     * {@code POST  /sous-varietes} : Create a new sousVariete.
     *
     * @param sousVariete the sousVariete to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sousVariete, or with status {@code 400 (Bad Request)} if the sousVariete has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sous-varietes")
    public ResponseEntity<SousVariete> createSousVariete(@Valid @RequestBody SousVariete sousVariete) throws URISyntaxException {
        log.debug("REST request to save SousVariete : {}", sousVariete);
        if (sousVariete.getId() != null) {
            throw new BadRequestAlertException("A new sousVariete cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SousVariete result = sousVarieteService.save(sousVariete);
        return ResponseEntity
            .created(new URI("/api/sous-varietes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sous-varietes/:id} : Updates an existing sousVariete.
     *
     * @param id the id of the sousVariete to save.
     * @param sousVariete the sousVariete to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sousVariete,
     * or with status {@code 400 (Bad Request)} if the sousVariete is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sousVariete couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sous-varietes/{id}")
    public ResponseEntity<SousVariete> updateSousVariete(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SousVariete sousVariete
    ) throws URISyntaxException {
        log.debug("REST request to update SousVariete : {}, {}", id, sousVariete);
        if (sousVariete.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sousVariete.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sousVarieteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SousVariete result = sousVarieteService.save(sousVariete);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sousVariete.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sous-varietes/:id} : Partial updates given fields of an existing sousVariete, field will ignore if it is null
     *
     * @param id the id of the sousVariete to save.
     * @param sousVariete the sousVariete to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sousVariete,
     * or with status {@code 400 (Bad Request)} if the sousVariete is not valid,
     * or with status {@code 404 (Not Found)} if the sousVariete is not found,
     * or with status {@code 500 (Internal Server Error)} if the sousVariete couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sous-varietes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SousVariete> partialUpdateSousVariete(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SousVariete sousVariete
    ) throws URISyntaxException {
        log.debug("REST request to partial update SousVariete partially : {}, {}", id, sousVariete);
        if (sousVariete.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sousVariete.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sousVarieteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SousVariete> result = sousVarieteService.partialUpdate(sousVariete);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sousVariete.getId().toString())
        );
    }

    /**
     * {@code GET  /sous-varietes} : get all the sousVarietes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sousVarietes in body.
     */
    @GetMapping("/sous-varietes")
    public ResponseEntity<List<SousVariete>> getAllSousVarietes(SousVarieteCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SousVarietes by criteria: {}", criteria);
        Page<SousVariete> page = sousVarieteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sous-varietes/count} : count all the sousVarietes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/sous-varietes/count")
    public ResponseEntity<Long> countSousVarietes(SousVarieteCriteria criteria) {
        log.debug("REST request to count SousVarietes by criteria: {}", criteria);
        return ResponseEntity.ok().body(sousVarieteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sous-varietes/:id} : get the "id" sousVariete.
     *
     * @param id the id of the sousVariete to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sousVariete, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sous-varietes/{id}")
    public ResponseEntity<SousVariete> getSousVariete(@PathVariable Long id) {
        log.debug("REST request to get SousVariete : {}", id);
        Optional<SousVariete> sousVariete = sousVarieteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sousVariete);
    }

    /**
     * {@code DELETE  /sous-varietes/:id} : delete the "id" sousVariete.
     *
     * @param id the id of the sousVariete to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sous-varietes/{id}")
    public ResponseEntity<Void> deleteSousVariete(@PathVariable Long id) {
        log.debug("REST request to delete SousVariete : {}", id);
        sousVarieteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
