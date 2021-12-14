package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.APGIIPlante;
import fr.syncrase.ecosyst.repository.APGIIPlanteRepository;
import fr.syncrase.ecosyst.service.APGIIPlanteQueryService;
import fr.syncrase.ecosyst.service.APGIIPlanteService;
import fr.syncrase.ecosyst.service.criteria.APGIIPlanteCriteria;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.APGIIPlante}.
 */
@RestController
@RequestMapping("/api")
public class APGIIPlanteResource {

    private final Logger log = LoggerFactory.getLogger(APGIIPlanteResource.class);

    private static final String ENTITY_NAME = "plantsMsapgiiPlante";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final APGIIPlanteService aPGIIPlanteService;

    private final APGIIPlanteRepository aPGIIPlanteRepository;

    private final APGIIPlanteQueryService aPGIIPlanteQueryService;

    public APGIIPlanteResource(
        APGIIPlanteService aPGIIPlanteService,
        APGIIPlanteRepository aPGIIPlanteRepository,
        APGIIPlanteQueryService aPGIIPlanteQueryService
    ) {
        this.aPGIIPlanteService = aPGIIPlanteService;
        this.aPGIIPlanteRepository = aPGIIPlanteRepository;
        this.aPGIIPlanteQueryService = aPGIIPlanteQueryService;
    }

    /**
     * {@code POST  /apgii-plantes} : Create a new aPGIIPlante.
     *
     * @param aPGIIPlante the aPGIIPlante to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aPGIIPlante, or with status {@code 400 (Bad Request)} if the aPGIIPlante has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/apgii-plantes")
    public ResponseEntity<APGIIPlante> createAPGIIPlante(@Valid @RequestBody APGIIPlante aPGIIPlante) throws URISyntaxException {
        log.debug("REST request to save APGIIPlante : {}", aPGIIPlante);
        if (aPGIIPlante.getId() != null) {
            throw new BadRequestAlertException("A new aPGIIPlante cannot already have an ID", ENTITY_NAME, "idexists");
        }
        APGIIPlante result = aPGIIPlanteService.save(aPGIIPlante);
        return ResponseEntity
            .created(new URI("/api/apgii-plantes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /apgii-plantes/:id} : Updates an existing aPGIIPlante.
     *
     * @param id the id of the aPGIIPlante to save.
     * @param aPGIIPlante the aPGIIPlante to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aPGIIPlante,
     * or with status {@code 400 (Bad Request)} if the aPGIIPlante is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aPGIIPlante couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/apgii-plantes/{id}")
    public ResponseEntity<APGIIPlante> updateAPGIIPlante(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody APGIIPlante aPGIIPlante
    ) throws URISyntaxException {
        log.debug("REST request to update APGIIPlante : {}, {}", id, aPGIIPlante);
        if (aPGIIPlante.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aPGIIPlante.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aPGIIPlanteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        APGIIPlante result = aPGIIPlanteService.save(aPGIIPlante);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, aPGIIPlante.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /apgii-plantes/:id} : Partial updates given fields of an existing aPGIIPlante, field will ignore if it is null
     *
     * @param id the id of the aPGIIPlante to save.
     * @param aPGIIPlante the aPGIIPlante to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aPGIIPlante,
     * or with status {@code 400 (Bad Request)} if the aPGIIPlante is not valid,
     * or with status {@code 404 (Not Found)} if the aPGIIPlante is not found,
     * or with status {@code 500 (Internal Server Error)} if the aPGIIPlante couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/apgii-plantes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<APGIIPlante> partialUpdateAPGIIPlante(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody APGIIPlante aPGIIPlante
    ) throws URISyntaxException {
        log.debug("REST request to partial update APGIIPlante partially : {}, {}", id, aPGIIPlante);
        if (aPGIIPlante.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aPGIIPlante.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aPGIIPlanteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<APGIIPlante> result = aPGIIPlanteService.partialUpdate(aPGIIPlante);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, aPGIIPlante.getId().toString())
        );
    }

    /**
     * {@code GET  /apgii-plantes} : get all the aPGIIPlantes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aPGIIPlantes in body.
     */
    @GetMapping("/apgii-plantes")
    public ResponseEntity<List<APGIIPlante>> getAllAPGIIPlantes(APGIIPlanteCriteria criteria, Pageable pageable) {
        log.debug("REST request to get APGIIPlantes by criteria: {}", criteria);
        Page<APGIIPlante> page = aPGIIPlanteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /apgii-plantes/count} : count all the aPGIIPlantes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/apgii-plantes/count")
    public ResponseEntity<Long> countAPGIIPlantes(APGIIPlanteCriteria criteria) {
        log.debug("REST request to count APGIIPlantes by criteria: {}", criteria);
        return ResponseEntity.ok().body(aPGIIPlanteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /apgii-plantes/:id} : get the "id" aPGIIPlante.
     *
     * @param id the id of the aPGIIPlante to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aPGIIPlante, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/apgii-plantes/{id}")
    public ResponseEntity<APGIIPlante> getAPGIIPlante(@PathVariable Long id) {
        log.debug("REST request to get APGIIPlante : {}", id);
        Optional<APGIIPlante> aPGIIPlante = aPGIIPlanteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aPGIIPlante);
    }

    /**
     * {@code DELETE  /apgii-plantes/:id} : delete the "id" aPGIIPlante.
     *
     * @param id the id of the aPGIIPlante to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/apgii-plantes/{id}")
    public ResponseEntity<Void> deleteAPGIIPlante(@PathVariable Long id) {
        log.debug("REST request to delete APGIIPlante : {}", id);
        aPGIIPlanteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
