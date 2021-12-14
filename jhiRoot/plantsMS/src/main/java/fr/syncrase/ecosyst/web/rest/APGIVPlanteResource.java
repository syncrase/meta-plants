package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.APGIVPlante;
import fr.syncrase.ecosyst.repository.APGIVPlanteRepository;
import fr.syncrase.ecosyst.service.APGIVPlanteQueryService;
import fr.syncrase.ecosyst.service.APGIVPlanteService;
import fr.syncrase.ecosyst.service.criteria.APGIVPlanteCriteria;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.APGIVPlante}.
 */
@RestController
@RequestMapping("/api")
public class APGIVPlanteResource {

    private final Logger log = LoggerFactory.getLogger(APGIVPlanteResource.class);

    private static final String ENTITY_NAME = "plantsMsapgivPlante";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final APGIVPlanteService aPGIVPlanteService;

    private final APGIVPlanteRepository aPGIVPlanteRepository;

    private final APGIVPlanteQueryService aPGIVPlanteQueryService;

    public APGIVPlanteResource(
        APGIVPlanteService aPGIVPlanteService,
        APGIVPlanteRepository aPGIVPlanteRepository,
        APGIVPlanteQueryService aPGIVPlanteQueryService
    ) {
        this.aPGIVPlanteService = aPGIVPlanteService;
        this.aPGIVPlanteRepository = aPGIVPlanteRepository;
        this.aPGIVPlanteQueryService = aPGIVPlanteQueryService;
    }

    /**
     * {@code POST  /apgiv-plantes} : Create a new aPGIVPlante.
     *
     * @param aPGIVPlante the aPGIVPlante to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aPGIVPlante, or with status {@code 400 (Bad Request)} if the aPGIVPlante has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/apgiv-plantes")
    public ResponseEntity<APGIVPlante> createAPGIVPlante(@Valid @RequestBody APGIVPlante aPGIVPlante) throws URISyntaxException {
        log.debug("REST request to save APGIVPlante : {}", aPGIVPlante);
        if (aPGIVPlante.getId() != null) {
            throw new BadRequestAlertException("A new aPGIVPlante cannot already have an ID", ENTITY_NAME, "idexists");
        }
        APGIVPlante result = aPGIVPlanteService.save(aPGIVPlante);
        return ResponseEntity
            .created(new URI("/api/apgiv-plantes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /apgiv-plantes/:id} : Updates an existing aPGIVPlante.
     *
     * @param id the id of the aPGIVPlante to save.
     * @param aPGIVPlante the aPGIVPlante to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aPGIVPlante,
     * or with status {@code 400 (Bad Request)} if the aPGIVPlante is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aPGIVPlante couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/apgiv-plantes/{id}")
    public ResponseEntity<APGIVPlante> updateAPGIVPlante(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody APGIVPlante aPGIVPlante
    ) throws URISyntaxException {
        log.debug("REST request to update APGIVPlante : {}, {}", id, aPGIVPlante);
        if (aPGIVPlante.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aPGIVPlante.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aPGIVPlanteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        APGIVPlante result = aPGIVPlanteService.save(aPGIVPlante);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, aPGIVPlante.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /apgiv-plantes/:id} : Partial updates given fields of an existing aPGIVPlante, field will ignore if it is null
     *
     * @param id the id of the aPGIVPlante to save.
     * @param aPGIVPlante the aPGIVPlante to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aPGIVPlante,
     * or with status {@code 400 (Bad Request)} if the aPGIVPlante is not valid,
     * or with status {@code 404 (Not Found)} if the aPGIVPlante is not found,
     * or with status {@code 500 (Internal Server Error)} if the aPGIVPlante couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/apgiv-plantes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<APGIVPlante> partialUpdateAPGIVPlante(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody APGIVPlante aPGIVPlante
    ) throws URISyntaxException {
        log.debug("REST request to partial update APGIVPlante partially : {}, {}", id, aPGIVPlante);
        if (aPGIVPlante.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aPGIVPlante.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aPGIVPlanteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<APGIVPlante> result = aPGIVPlanteService.partialUpdate(aPGIVPlante);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, aPGIVPlante.getId().toString())
        );
    }

    /**
     * {@code GET  /apgiv-plantes} : get all the aPGIVPlantes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aPGIVPlantes in body.
     */
    @GetMapping("/apgiv-plantes")
    public ResponseEntity<List<APGIVPlante>> getAllAPGIVPlantes(APGIVPlanteCriteria criteria, Pageable pageable) {
        log.debug("REST request to get APGIVPlantes by criteria: {}", criteria);
        Page<APGIVPlante> page = aPGIVPlanteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /apgiv-plantes/count} : count all the aPGIVPlantes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/apgiv-plantes/count")
    public ResponseEntity<Long> countAPGIVPlantes(APGIVPlanteCriteria criteria) {
        log.debug("REST request to count APGIVPlantes by criteria: {}", criteria);
        return ResponseEntity.ok().body(aPGIVPlanteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /apgiv-plantes/:id} : get the "id" aPGIVPlante.
     *
     * @param id the id of the aPGIVPlante to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aPGIVPlante, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/apgiv-plantes/{id}")
    public ResponseEntity<APGIVPlante> getAPGIVPlante(@PathVariable Long id) {
        log.debug("REST request to get APGIVPlante : {}", id);
        Optional<APGIVPlante> aPGIVPlante = aPGIVPlanteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aPGIVPlante);
    }

    /**
     * {@code DELETE  /apgiv-plantes/:id} : delete the "id" aPGIVPlante.
     *
     * @param id the id of the aPGIVPlante to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/apgiv-plantes/{id}")
    public ResponseEntity<Void> deleteAPGIVPlante(@PathVariable Long id) {
        log.debug("REST request to delete APGIVPlante : {}", id);
        aPGIVPlanteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
