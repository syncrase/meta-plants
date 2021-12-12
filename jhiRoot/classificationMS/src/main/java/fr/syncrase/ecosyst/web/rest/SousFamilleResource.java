package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.SousFamille;
import fr.syncrase.ecosyst.repository.SousFamilleRepository;
import fr.syncrase.ecosyst.service.SousFamilleQueryService;
import fr.syncrase.ecosyst.service.SousFamilleService;
import fr.syncrase.ecosyst.service.criteria.SousFamilleCriteria;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.SousFamille}.
 */
@RestController
@RequestMapping("/api")
public class SousFamilleResource {

    private final Logger log = LoggerFactory.getLogger(SousFamilleResource.class);

    private static final String ENTITY_NAME = "classificationMsSousFamille";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SousFamilleService sousFamilleService;

    private final SousFamilleRepository sousFamilleRepository;

    private final SousFamilleQueryService sousFamilleQueryService;

    public SousFamilleResource(
        SousFamilleService sousFamilleService,
        SousFamilleRepository sousFamilleRepository,
        SousFamilleQueryService sousFamilleQueryService
    ) {
        this.sousFamilleService = sousFamilleService;
        this.sousFamilleRepository = sousFamilleRepository;
        this.sousFamilleQueryService = sousFamilleQueryService;
    }

    /**
     * {@code POST  /sous-familles} : Create a new sousFamille.
     *
     * @param sousFamille the sousFamille to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sousFamille, or with status {@code 400 (Bad Request)} if the sousFamille has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sous-familles")
    public ResponseEntity<SousFamille> createSousFamille(@Valid @RequestBody SousFamille sousFamille) throws URISyntaxException {
        log.debug("REST request to save SousFamille : {}", sousFamille);
        if (sousFamille.getId() != null) {
            throw new BadRequestAlertException("A new sousFamille cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SousFamille result = sousFamilleService.save(sousFamille);
        return ResponseEntity
            .created(new URI("/api/sous-familles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sous-familles/:id} : Updates an existing sousFamille.
     *
     * @param id the id of the sousFamille to save.
     * @param sousFamille the sousFamille to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sousFamille,
     * or with status {@code 400 (Bad Request)} if the sousFamille is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sousFamille couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sous-familles/{id}")
    public ResponseEntity<SousFamille> updateSousFamille(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SousFamille sousFamille
    ) throws URISyntaxException {
        log.debug("REST request to update SousFamille : {}, {}", id, sousFamille);
        if (sousFamille.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sousFamille.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sousFamilleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SousFamille result = sousFamilleService.save(sousFamille);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sousFamille.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sous-familles/:id} : Partial updates given fields of an existing sousFamille, field will ignore if it is null
     *
     * @param id the id of the sousFamille to save.
     * @param sousFamille the sousFamille to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sousFamille,
     * or with status {@code 400 (Bad Request)} if the sousFamille is not valid,
     * or with status {@code 404 (Not Found)} if the sousFamille is not found,
     * or with status {@code 500 (Internal Server Error)} if the sousFamille couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sous-familles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SousFamille> partialUpdateSousFamille(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SousFamille sousFamille
    ) throws URISyntaxException {
        log.debug("REST request to partial update SousFamille partially : {}, {}", id, sousFamille);
        if (sousFamille.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sousFamille.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sousFamilleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SousFamille> result = sousFamilleService.partialUpdate(sousFamille);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sousFamille.getId().toString())
        );
    }

    /**
     * {@code GET  /sous-familles} : get all the sousFamilles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sousFamilles in body.
     */
    @GetMapping("/sous-familles")
    public ResponseEntity<List<SousFamille>> getAllSousFamilles(SousFamilleCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SousFamilles by criteria: {}", criteria);
        Page<SousFamille> page = sousFamilleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sous-familles/count} : count all the sousFamilles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/sous-familles/count")
    public ResponseEntity<Long> countSousFamilles(SousFamilleCriteria criteria) {
        log.debug("REST request to count SousFamilles by criteria: {}", criteria);
        return ResponseEntity.ok().body(sousFamilleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sous-familles/:id} : get the "id" sousFamille.
     *
     * @param id the id of the sousFamille to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sousFamille, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sous-familles/{id}")
    public ResponseEntity<SousFamille> getSousFamille(@PathVariable Long id) {
        log.debug("REST request to get SousFamille : {}", id);
        Optional<SousFamille> sousFamille = sousFamilleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sousFamille);
    }

    /**
     * {@code DELETE  /sous-familles/:id} : delete the "id" sousFamille.
     *
     * @param id the id of the sousFamille to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sous-familles/{id}")
    public ResponseEntity<Void> deleteSousFamille(@PathVariable Long id) {
        log.debug("REST request to delete SousFamille : {}", id);
        sousFamilleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
