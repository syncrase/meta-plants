package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.SousForme;
import fr.syncrase.ecosyst.repository.SousFormeRepository;
import fr.syncrase.ecosyst.service.SousFormeQueryService;
import fr.syncrase.ecosyst.service.SousFormeService;
import fr.syncrase.ecosyst.service.criteria.SousFormeCriteria;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.SousForme}.
 */
@RestController
@RequestMapping("/api")
public class SousFormeResource {

    private final Logger log = LoggerFactory.getLogger(SousFormeResource.class);

    private static final String ENTITY_NAME = "classificationMsSousForme";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SousFormeService sousFormeService;

    private final SousFormeRepository sousFormeRepository;

    private final SousFormeQueryService sousFormeQueryService;

    public SousFormeResource(
        SousFormeService sousFormeService,
        SousFormeRepository sousFormeRepository,
        SousFormeQueryService sousFormeQueryService
    ) {
        this.sousFormeService = sousFormeService;
        this.sousFormeRepository = sousFormeRepository;
        this.sousFormeQueryService = sousFormeQueryService;
    }

    /**
     * {@code POST  /sous-formes} : Create a new sousForme.
     *
     * @param sousForme the sousForme to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sousForme, or with status {@code 400 (Bad Request)} if the sousForme has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sous-formes")
    public ResponseEntity<SousForme> createSousForme(@Valid @RequestBody SousForme sousForme) throws URISyntaxException {
        log.debug("REST request to save SousForme : {}", sousForme);
        if (sousForme.getId() != null) {
            throw new BadRequestAlertException("A new sousForme cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SousForme result = sousFormeService.save(sousForme);
        return ResponseEntity
            .created(new URI("/api/sous-formes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sous-formes/:id} : Updates an existing sousForme.
     *
     * @param id the id of the sousForme to save.
     * @param sousForme the sousForme to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sousForme,
     * or with status {@code 400 (Bad Request)} if the sousForme is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sousForme couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sous-formes/{id}")
    public ResponseEntity<SousForme> updateSousForme(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SousForme sousForme
    ) throws URISyntaxException {
        log.debug("REST request to update SousForme : {}, {}", id, sousForme);
        if (sousForme.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sousForme.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sousFormeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SousForme result = sousFormeService.save(sousForme);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sousForme.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sous-formes/:id} : Partial updates given fields of an existing sousForme, field will ignore if it is null
     *
     * @param id the id of the sousForme to save.
     * @param sousForme the sousForme to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sousForme,
     * or with status {@code 400 (Bad Request)} if the sousForme is not valid,
     * or with status {@code 404 (Not Found)} if the sousForme is not found,
     * or with status {@code 500 (Internal Server Error)} if the sousForme couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sous-formes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SousForme> partialUpdateSousForme(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SousForme sousForme
    ) throws URISyntaxException {
        log.debug("REST request to partial update SousForme partially : {}, {}", id, sousForme);
        if (sousForme.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sousForme.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sousFormeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SousForme> result = sousFormeService.partialUpdate(sousForme);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sousForme.getId().toString())
        );
    }

    /**
     * {@code GET  /sous-formes} : get all the sousFormes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sousFormes in body.
     */
    @GetMapping("/sous-formes")
    public ResponseEntity<List<SousForme>> getAllSousFormes(SousFormeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SousFormes by criteria: {}", criteria);
        Page<SousForme> page = sousFormeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sous-formes/count} : count all the sousFormes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/sous-formes/count")
    public ResponseEntity<Long> countSousFormes(SousFormeCriteria criteria) {
        log.debug("REST request to count SousFormes by criteria: {}", criteria);
        return ResponseEntity.ok().body(sousFormeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sous-formes/:id} : get the "id" sousForme.
     *
     * @param id the id of the sousForme to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sousForme, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sous-formes/{id}")
    public ResponseEntity<SousForme> getSousForme(@PathVariable Long id) {
        log.debug("REST request to get SousForme : {}", id);
        Optional<SousForme> sousForme = sousFormeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sousForme);
    }

    /**
     * {@code DELETE  /sous-formes/:id} : delete the "id" sousForme.
     *
     * @param id the id of the sousForme to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sous-formes/{id}")
    public ResponseEntity<Void> deleteSousForme(@PathVariable Long id) {
        log.debug("REST request to delete SousForme : {}", id);
        sousFormeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
