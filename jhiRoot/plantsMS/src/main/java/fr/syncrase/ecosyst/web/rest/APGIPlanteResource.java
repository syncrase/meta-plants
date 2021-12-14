package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.APGIPlante;
import fr.syncrase.ecosyst.repository.APGIPlanteRepository;
import fr.syncrase.ecosyst.service.APGIPlanteQueryService;
import fr.syncrase.ecosyst.service.APGIPlanteService;
import fr.syncrase.ecosyst.service.criteria.APGIPlanteCriteria;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.APGIPlante}.
 */
@RestController
@RequestMapping("/api")
public class APGIPlanteResource {

    private final Logger log = LoggerFactory.getLogger(APGIPlanteResource.class);

    private static final String ENTITY_NAME = "plantsMsapgiPlante";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final APGIPlanteService aPGIPlanteService;

    private final APGIPlanteRepository aPGIPlanteRepository;

    private final APGIPlanteQueryService aPGIPlanteQueryService;

    public APGIPlanteResource(
        APGIPlanteService aPGIPlanteService,
        APGIPlanteRepository aPGIPlanteRepository,
        APGIPlanteQueryService aPGIPlanteQueryService
    ) {
        this.aPGIPlanteService = aPGIPlanteService;
        this.aPGIPlanteRepository = aPGIPlanteRepository;
        this.aPGIPlanteQueryService = aPGIPlanteQueryService;
    }

    /**
     * {@code POST  /apgi-plantes} : Create a new aPGIPlante.
     *
     * @param aPGIPlante the aPGIPlante to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aPGIPlante, or with status {@code 400 (Bad Request)} if the aPGIPlante has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/apgi-plantes")
    public ResponseEntity<APGIPlante> createAPGIPlante(@Valid @RequestBody APGIPlante aPGIPlante) throws URISyntaxException {
        log.debug("REST request to save APGIPlante : {}", aPGIPlante);
        if (aPGIPlante.getId() != null) {
            throw new BadRequestAlertException("A new aPGIPlante cannot already have an ID", ENTITY_NAME, "idexists");
        }
        APGIPlante result = aPGIPlanteService.save(aPGIPlante);
        return ResponseEntity
            .created(new URI("/api/apgi-plantes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /apgi-plantes/:id} : Updates an existing aPGIPlante.
     *
     * @param id the id of the aPGIPlante to save.
     * @param aPGIPlante the aPGIPlante to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aPGIPlante,
     * or with status {@code 400 (Bad Request)} if the aPGIPlante is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aPGIPlante couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/apgi-plantes/{id}")
    public ResponseEntity<APGIPlante> updateAPGIPlante(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody APGIPlante aPGIPlante
    ) throws URISyntaxException {
        log.debug("REST request to update APGIPlante : {}, {}", id, aPGIPlante);
        if (aPGIPlante.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aPGIPlante.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aPGIPlanteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        APGIPlante result = aPGIPlanteService.save(aPGIPlante);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, aPGIPlante.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /apgi-plantes/:id} : Partial updates given fields of an existing aPGIPlante, field will ignore if it is null
     *
     * @param id the id of the aPGIPlante to save.
     * @param aPGIPlante the aPGIPlante to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aPGIPlante,
     * or with status {@code 400 (Bad Request)} if the aPGIPlante is not valid,
     * or with status {@code 404 (Not Found)} if the aPGIPlante is not found,
     * or with status {@code 500 (Internal Server Error)} if the aPGIPlante couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/apgi-plantes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<APGIPlante> partialUpdateAPGIPlante(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody APGIPlante aPGIPlante
    ) throws URISyntaxException {
        log.debug("REST request to partial update APGIPlante partially : {}, {}", id, aPGIPlante);
        if (aPGIPlante.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aPGIPlante.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aPGIPlanteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<APGIPlante> result = aPGIPlanteService.partialUpdate(aPGIPlante);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, aPGIPlante.getId().toString())
        );
    }

    /**
     * {@code GET  /apgi-plantes} : get all the aPGIPlantes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aPGIPlantes in body.
     */
    @GetMapping("/apgi-plantes")
    public ResponseEntity<List<APGIPlante>> getAllAPGIPlantes(APGIPlanteCriteria criteria, Pageable pageable) {
        log.debug("REST request to get APGIPlantes by criteria: {}", criteria);
        Page<APGIPlante> page = aPGIPlanteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /apgi-plantes/count} : count all the aPGIPlantes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/apgi-plantes/count")
    public ResponseEntity<Long> countAPGIPlantes(APGIPlanteCriteria criteria) {
        log.debug("REST request to count APGIPlantes by criteria: {}", criteria);
        return ResponseEntity.ok().body(aPGIPlanteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /apgi-plantes/:id} : get the "id" aPGIPlante.
     *
     * @param id the id of the aPGIPlante to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aPGIPlante, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/apgi-plantes/{id}")
    public ResponseEntity<APGIPlante> getAPGIPlante(@PathVariable Long id) {
        log.debug("REST request to get APGIPlante : {}", id);
        Optional<APGIPlante> aPGIPlante = aPGIPlanteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aPGIPlante);
    }

    /**
     * {@code DELETE  /apgi-plantes/:id} : delete the "id" aPGIPlante.
     *
     * @param id the id of the aPGIPlante to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/apgi-plantes/{id}")
    public ResponseEntity<Void> deleteAPGIPlante(@PathVariable Long id) {
        log.debug("REST request to delete APGIPlante : {}", id);
        aPGIPlanteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
