package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.SousEspece;
import fr.syncrase.ecosyst.repository.SousEspeceRepository;
import fr.syncrase.ecosyst.service.SousEspeceQueryService;
import fr.syncrase.ecosyst.service.SousEspeceService;
import fr.syncrase.ecosyst.service.criteria.SousEspeceCriteria;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.SousEspece}.
 */
@RestController
@RequestMapping("/api")
public class SousEspeceResource {

    private final Logger log = LoggerFactory.getLogger(SousEspeceResource.class);

    private static final String ENTITY_NAME = "classificationMsSousEspece";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SousEspeceService sousEspeceService;

    private final SousEspeceRepository sousEspeceRepository;

    private final SousEspeceQueryService sousEspeceQueryService;

    public SousEspeceResource(
        SousEspeceService sousEspeceService,
        SousEspeceRepository sousEspeceRepository,
        SousEspeceQueryService sousEspeceQueryService
    ) {
        this.sousEspeceService = sousEspeceService;
        this.sousEspeceRepository = sousEspeceRepository;
        this.sousEspeceQueryService = sousEspeceQueryService;
    }

    /**
     * {@code POST  /sous-especes} : Create a new sousEspece.
     *
     * @param sousEspece the sousEspece to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sousEspece, or with status {@code 400 (Bad Request)} if the sousEspece has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sous-especes")
    public ResponseEntity<SousEspece> createSousEspece(@Valid @RequestBody SousEspece sousEspece) throws URISyntaxException {
        log.debug("REST request to save SousEspece : {}", sousEspece);
        if (sousEspece.getId() != null) {
            throw new BadRequestAlertException("A new sousEspece cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SousEspece result = sousEspeceService.save(sousEspece);
        return ResponseEntity
            .created(new URI("/api/sous-especes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sous-especes/:id} : Updates an existing sousEspece.
     *
     * @param id the id of the sousEspece to save.
     * @param sousEspece the sousEspece to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sousEspece,
     * or with status {@code 400 (Bad Request)} if the sousEspece is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sousEspece couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sous-especes/{id}")
    public ResponseEntity<SousEspece> updateSousEspece(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SousEspece sousEspece
    ) throws URISyntaxException {
        log.debug("REST request to update SousEspece : {}, {}", id, sousEspece);
        if (sousEspece.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sousEspece.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sousEspeceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SousEspece result = sousEspeceService.save(sousEspece);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sousEspece.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sous-especes/:id} : Partial updates given fields of an existing sousEspece, field will ignore if it is null
     *
     * @param id the id of the sousEspece to save.
     * @param sousEspece the sousEspece to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sousEspece,
     * or with status {@code 400 (Bad Request)} if the sousEspece is not valid,
     * or with status {@code 404 (Not Found)} if the sousEspece is not found,
     * or with status {@code 500 (Internal Server Error)} if the sousEspece couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sous-especes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SousEspece> partialUpdateSousEspece(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SousEspece sousEspece
    ) throws URISyntaxException {
        log.debug("REST request to partial update SousEspece partially : {}, {}", id, sousEspece);
        if (sousEspece.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sousEspece.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sousEspeceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SousEspece> result = sousEspeceService.partialUpdate(sousEspece);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sousEspece.getId().toString())
        );
    }

    /**
     * {@code GET  /sous-especes} : get all the sousEspeces.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sousEspeces in body.
     */
    @GetMapping("/sous-especes")
    public ResponseEntity<List<SousEspece>> getAllSousEspeces(SousEspeceCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SousEspeces by criteria: {}", criteria);
        Page<SousEspece> page = sousEspeceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sous-especes/count} : count all the sousEspeces.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/sous-especes/count")
    public ResponseEntity<Long> countSousEspeces(SousEspeceCriteria criteria) {
        log.debug("REST request to count SousEspeces by criteria: {}", criteria);
        return ResponseEntity.ok().body(sousEspeceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sous-especes/:id} : get the "id" sousEspece.
     *
     * @param id the id of the sousEspece to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sousEspece, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sous-especes/{id}")
    public ResponseEntity<SousEspece> getSousEspece(@PathVariable Long id) {
        log.debug("REST request to get SousEspece : {}", id);
        Optional<SousEspece> sousEspece = sousEspeceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sousEspece);
    }

    /**
     * {@code DELETE  /sous-especes/:id} : delete the "id" sousEspece.
     *
     * @param id the id of the sousEspece to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sous-especes/{id}")
    public ResponseEntity<Void> deleteSousEspece(@PathVariable Long id) {
        log.debug("REST request to delete SousEspece : {}", id);
        sousEspeceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
