package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.Variete;
import fr.syncrase.ecosyst.repository.VarieteRepository;
import fr.syncrase.ecosyst.service.VarieteQueryService;
import fr.syncrase.ecosyst.service.VarieteService;
import fr.syncrase.ecosyst.service.criteria.VarieteCriteria;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.Variete}.
 */
@RestController
@RequestMapping("/api")
public class VarieteResource {

    private final Logger log = LoggerFactory.getLogger(VarieteResource.class);

    private static final String ENTITY_NAME = "classificationMsVariete";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VarieteService varieteService;

    private final VarieteRepository varieteRepository;

    private final VarieteQueryService varieteQueryService;

    public VarieteResource(VarieteService varieteService, VarieteRepository varieteRepository, VarieteQueryService varieteQueryService) {
        this.varieteService = varieteService;
        this.varieteRepository = varieteRepository;
        this.varieteQueryService = varieteQueryService;
    }

    /**
     * {@code POST  /varietes} : Create a new variete.
     *
     * @param variete the variete to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new variete, or with status {@code 400 (Bad Request)} if the variete has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/varietes")
    public ResponseEntity<Variete> createVariete(@Valid @RequestBody Variete variete) throws URISyntaxException {
        log.debug("REST request to save Variete : {}", variete);
        if (variete.getId() != null) {
            throw new BadRequestAlertException("A new variete cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Variete result = varieteService.save(variete);
        return ResponseEntity
            .created(new URI("/api/varietes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /varietes/:id} : Updates an existing variete.
     *
     * @param id the id of the variete to save.
     * @param variete the variete to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated variete,
     * or with status {@code 400 (Bad Request)} if the variete is not valid,
     * or with status {@code 500 (Internal Server Error)} if the variete couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/varietes/{id}")
    public ResponseEntity<Variete> updateVariete(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Variete variete
    ) throws URISyntaxException {
        log.debug("REST request to update Variete : {}, {}", id, variete);
        if (variete.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, variete.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!varieteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Variete result = varieteService.save(variete);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, variete.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /varietes/:id} : Partial updates given fields of an existing variete, field will ignore if it is null
     *
     * @param id the id of the variete to save.
     * @param variete the variete to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated variete,
     * or with status {@code 400 (Bad Request)} if the variete is not valid,
     * or with status {@code 404 (Not Found)} if the variete is not found,
     * or with status {@code 500 (Internal Server Error)} if the variete couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/varietes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Variete> partialUpdateVariete(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Variete variete
    ) throws URISyntaxException {
        log.debug("REST request to partial update Variete partially : {}, {}", id, variete);
        if (variete.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, variete.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!varieteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Variete> result = varieteService.partialUpdate(variete);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, variete.getId().toString())
        );
    }

    /**
     * {@code GET  /varietes} : get all the varietes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of varietes in body.
     */
    @GetMapping("/varietes")
    public ResponseEntity<List<Variete>> getAllVarietes(VarieteCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Varietes by criteria: {}", criteria);
        Page<Variete> page = varieteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /varietes/count} : count all the varietes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/varietes/count")
    public ResponseEntity<Long> countVarietes(VarieteCriteria criteria) {
        log.debug("REST request to count Varietes by criteria: {}", criteria);
        return ResponseEntity.ok().body(varieteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /varietes/:id} : get the "id" variete.
     *
     * @param id the id of the variete to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the variete, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/varietes/{id}")
    public ResponseEntity<Variete> getVariete(@PathVariable Long id) {
        log.debug("REST request to get Variete : {}", id);
        Optional<Variete> variete = varieteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(variete);
    }

    /**
     * {@code DELETE  /varietes/:id} : delete the "id" variete.
     *
     * @param id the id of the variete to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/varietes/{id}")
    public ResponseEntity<Void> deleteVariete(@PathVariable Long id) {
        log.debug("REST request to delete Variete : {}", id);
        varieteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
