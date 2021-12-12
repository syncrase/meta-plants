package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.Forme;
import fr.syncrase.ecosyst.repository.FormeRepository;
import fr.syncrase.ecosyst.service.FormeQueryService;
import fr.syncrase.ecosyst.service.FormeService;
import fr.syncrase.ecosyst.service.criteria.FormeCriteria;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.Forme}.
 */
@RestController
@RequestMapping("/api")
public class FormeResource {

    private final Logger log = LoggerFactory.getLogger(FormeResource.class);

    private static final String ENTITY_NAME = "classificationMsForme";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FormeService formeService;

    private final FormeRepository formeRepository;

    private final FormeQueryService formeQueryService;

    public FormeResource(FormeService formeService, FormeRepository formeRepository, FormeQueryService formeQueryService) {
        this.formeService = formeService;
        this.formeRepository = formeRepository;
        this.formeQueryService = formeQueryService;
    }

    /**
     * {@code POST  /formes} : Create a new forme.
     *
     * @param forme the forme to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new forme, or with status {@code 400 (Bad Request)} if the forme has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/formes")
    public ResponseEntity<Forme> createForme(@Valid @RequestBody Forme forme) throws URISyntaxException {
        log.debug("REST request to save Forme : {}", forme);
        if (forme.getId() != null) {
            throw new BadRequestAlertException("A new forme cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Forme result = formeService.save(forme);
        return ResponseEntity
            .created(new URI("/api/formes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /formes/:id} : Updates an existing forme.
     *
     * @param id the id of the forme to save.
     * @param forme the forme to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated forme,
     * or with status {@code 400 (Bad Request)} if the forme is not valid,
     * or with status {@code 500 (Internal Server Error)} if the forme couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/formes/{id}")
    public ResponseEntity<Forme> updateForme(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Forme forme)
        throws URISyntaxException {
        log.debug("REST request to update Forme : {}, {}", id, forme);
        if (forme.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, forme.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Forme result = formeService.save(forme);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, forme.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /formes/:id} : Partial updates given fields of an existing forme, field will ignore if it is null
     *
     * @param id the id of the forme to save.
     * @param forme the forme to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated forme,
     * or with status {@code 400 (Bad Request)} if the forme is not valid,
     * or with status {@code 404 (Not Found)} if the forme is not found,
     * or with status {@code 500 (Internal Server Error)} if the forme couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/formes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Forme> partialUpdateForme(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Forme forme
    ) throws URISyntaxException {
        log.debug("REST request to partial update Forme partially : {}, {}", id, forme);
        if (forme.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, forme.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Forme> result = formeService.partialUpdate(forme);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, forme.getId().toString())
        );
    }

    /**
     * {@code GET  /formes} : get all the formes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of formes in body.
     */
    @GetMapping("/formes")
    public ResponseEntity<List<Forme>> getAllFormes(FormeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Formes by criteria: {}", criteria);
        Page<Forme> page = formeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /formes/count} : count all the formes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/formes/count")
    public ResponseEntity<Long> countFormes(FormeCriteria criteria) {
        log.debug("REST request to count Formes by criteria: {}", criteria);
        return ResponseEntity.ok().body(formeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /formes/:id} : get the "id" forme.
     *
     * @param id the id of the forme to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the forme, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/formes/{id}")
    public ResponseEntity<Forme> getForme(@PathVariable Long id) {
        log.debug("REST request to get Forme : {}", id);
        Optional<Forme> forme = formeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(forme);
    }

    /**
     * {@code DELETE  /formes/:id} : delete the "id" forme.
     *
     * @param id the id of the forme to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/formes/{id}")
    public ResponseEntity<Void> deleteForme(@PathVariable Long id) {
        log.debug("REST request to delete Forme : {}", id);
        formeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
