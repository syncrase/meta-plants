package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.APGIIIPlante;
import fr.syncrase.ecosyst.repository.APGIIIPlanteRepository;
import fr.syncrase.ecosyst.service.APGIIIPlanteQueryService;
import fr.syncrase.ecosyst.service.APGIIIPlanteService;
import fr.syncrase.ecosyst.service.criteria.APGIIIPlanteCriteria;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.APGIIIPlante}.
 */
@RestController
@RequestMapping("/api")
public class APGIIIPlanteResource {

    private final Logger log = LoggerFactory.getLogger(APGIIIPlanteResource.class);

    private static final String ENTITY_NAME = "plantsMsapgiiiPlante";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final APGIIIPlanteService aPGIIIPlanteService;

    private final APGIIIPlanteRepository aPGIIIPlanteRepository;

    private final APGIIIPlanteQueryService aPGIIIPlanteQueryService;

    public APGIIIPlanteResource(
        APGIIIPlanteService aPGIIIPlanteService,
        APGIIIPlanteRepository aPGIIIPlanteRepository,
        APGIIIPlanteQueryService aPGIIIPlanteQueryService
    ) {
        this.aPGIIIPlanteService = aPGIIIPlanteService;
        this.aPGIIIPlanteRepository = aPGIIIPlanteRepository;
        this.aPGIIIPlanteQueryService = aPGIIIPlanteQueryService;
    }

    /**
     * {@code POST  /apgiii-plantes} : Create a new aPGIIIPlante.
     *
     * @param aPGIIIPlante the aPGIIIPlante to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aPGIIIPlante, or with status {@code 400 (Bad Request)} if the aPGIIIPlante has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/apgiii-plantes")
    public ResponseEntity<APGIIIPlante> createAPGIIIPlante(@Valid @RequestBody APGIIIPlante aPGIIIPlante) throws URISyntaxException {
        log.debug("REST request to save APGIIIPlante : {}", aPGIIIPlante);
        if (aPGIIIPlante.getId() != null) {
            throw new BadRequestAlertException("A new aPGIIIPlante cannot already have an ID", ENTITY_NAME, "idexists");
        }
        APGIIIPlante result = aPGIIIPlanteService.save(aPGIIIPlante);
        return ResponseEntity
            .created(new URI("/api/apgiii-plantes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /apgiii-plantes/:id} : Updates an existing aPGIIIPlante.
     *
     * @param id the id of the aPGIIIPlante to save.
     * @param aPGIIIPlante the aPGIIIPlante to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aPGIIIPlante,
     * or with status {@code 400 (Bad Request)} if the aPGIIIPlante is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aPGIIIPlante couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/apgiii-plantes/{id}")
    public ResponseEntity<APGIIIPlante> updateAPGIIIPlante(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody APGIIIPlante aPGIIIPlante
    ) throws URISyntaxException {
        log.debug("REST request to update APGIIIPlante : {}, {}", id, aPGIIIPlante);
        if (aPGIIIPlante.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aPGIIIPlante.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aPGIIIPlanteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        APGIIIPlante result = aPGIIIPlanteService.save(aPGIIIPlante);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, aPGIIIPlante.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /apgiii-plantes/:id} : Partial updates given fields of an existing aPGIIIPlante, field will ignore if it is null
     *
     * @param id the id of the aPGIIIPlante to save.
     * @param aPGIIIPlante the aPGIIIPlante to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aPGIIIPlante,
     * or with status {@code 400 (Bad Request)} if the aPGIIIPlante is not valid,
     * or with status {@code 404 (Not Found)} if the aPGIIIPlante is not found,
     * or with status {@code 500 (Internal Server Error)} if the aPGIIIPlante couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/apgiii-plantes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<APGIIIPlante> partialUpdateAPGIIIPlante(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody APGIIIPlante aPGIIIPlante
    ) throws URISyntaxException {
        log.debug("REST request to partial update APGIIIPlante partially : {}, {}", id, aPGIIIPlante);
        if (aPGIIIPlante.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aPGIIIPlante.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aPGIIIPlanteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<APGIIIPlante> result = aPGIIIPlanteService.partialUpdate(aPGIIIPlante);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, aPGIIIPlante.getId().toString())
        );
    }

    /**
     * {@code GET  /apgiii-plantes} : get all the aPGIIIPlantes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aPGIIIPlantes in body.
     */
    @GetMapping("/apgiii-plantes")
    public ResponseEntity<List<APGIIIPlante>> getAllAPGIIIPlantes(APGIIIPlanteCriteria criteria, Pageable pageable) {
        log.debug("REST request to get APGIIIPlantes by criteria: {}", criteria);
        Page<APGIIIPlante> page = aPGIIIPlanteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /apgiii-plantes/count} : count all the aPGIIIPlantes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/apgiii-plantes/count")
    public ResponseEntity<Long> countAPGIIIPlantes(APGIIIPlanteCriteria criteria) {
        log.debug("REST request to count APGIIIPlantes by criteria: {}", criteria);
        return ResponseEntity.ok().body(aPGIIIPlanteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /apgiii-plantes/:id} : get the "id" aPGIIIPlante.
     *
     * @param id the id of the aPGIIIPlante to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aPGIIIPlante, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/apgiii-plantes/{id}")
    public ResponseEntity<APGIIIPlante> getAPGIIIPlante(@PathVariable Long id) {
        log.debug("REST request to get APGIIIPlante : {}", id);
        Optional<APGIIIPlante> aPGIIIPlante = aPGIIIPlanteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aPGIIIPlante);
    }

    /**
     * {@code DELETE  /apgiii-plantes/:id} : delete the "id" aPGIIIPlante.
     *
     * @param id the id of the aPGIIIPlante to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/apgiii-plantes/{id}")
    public ResponseEntity<Void> deleteAPGIIIPlante(@PathVariable Long id) {
        log.debug("REST request to delete APGIIIPlante : {}", id);
        aPGIIIPlanteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
