package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.RaunkierPlante;
import fr.syncrase.ecosyst.repository.RaunkierPlanteRepository;
import fr.syncrase.ecosyst.service.RaunkierPlanteQueryService;
import fr.syncrase.ecosyst.service.RaunkierPlanteService;
import fr.syncrase.ecosyst.service.criteria.RaunkierPlanteCriteria;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.RaunkierPlante}.
 */
@RestController
@RequestMapping("/api")
public class RaunkierPlanteResource {

    private final Logger log = LoggerFactory.getLogger(RaunkierPlanteResource.class);

    private static final String ENTITY_NAME = "plantsMsRaunkierPlante";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RaunkierPlanteService raunkierPlanteService;

    private final RaunkierPlanteRepository raunkierPlanteRepository;

    private final RaunkierPlanteQueryService raunkierPlanteQueryService;

    public RaunkierPlanteResource(
        RaunkierPlanteService raunkierPlanteService,
        RaunkierPlanteRepository raunkierPlanteRepository,
        RaunkierPlanteQueryService raunkierPlanteQueryService
    ) {
        this.raunkierPlanteService = raunkierPlanteService;
        this.raunkierPlanteRepository = raunkierPlanteRepository;
        this.raunkierPlanteQueryService = raunkierPlanteQueryService;
    }

    /**
     * {@code POST  /raunkier-plantes} : Create a new raunkierPlante.
     *
     * @param raunkierPlante the raunkierPlante to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new raunkierPlante, or with status {@code 400 (Bad Request)} if the raunkierPlante has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/raunkier-plantes")
    public ResponseEntity<RaunkierPlante> createRaunkierPlante(@Valid @RequestBody RaunkierPlante raunkierPlante)
        throws URISyntaxException {
        log.debug("REST request to save RaunkierPlante : {}", raunkierPlante);
        if (raunkierPlante.getId() != null) {
            throw new BadRequestAlertException("A new raunkierPlante cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RaunkierPlante result = raunkierPlanteService.save(raunkierPlante);
        return ResponseEntity
            .created(new URI("/api/raunkier-plantes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /raunkier-plantes/:id} : Updates an existing raunkierPlante.
     *
     * @param id the id of the raunkierPlante to save.
     * @param raunkierPlante the raunkierPlante to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated raunkierPlante,
     * or with status {@code 400 (Bad Request)} if the raunkierPlante is not valid,
     * or with status {@code 500 (Internal Server Error)} if the raunkierPlante couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/raunkier-plantes/{id}")
    public ResponseEntity<RaunkierPlante> updateRaunkierPlante(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RaunkierPlante raunkierPlante
    ) throws URISyntaxException {
        log.debug("REST request to update RaunkierPlante : {}, {}", id, raunkierPlante);
        if (raunkierPlante.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, raunkierPlante.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!raunkierPlanteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RaunkierPlante result = raunkierPlanteService.save(raunkierPlante);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, raunkierPlante.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /raunkier-plantes/:id} : Partial updates given fields of an existing raunkierPlante, field will ignore if it is null
     *
     * @param id the id of the raunkierPlante to save.
     * @param raunkierPlante the raunkierPlante to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated raunkierPlante,
     * or with status {@code 400 (Bad Request)} if the raunkierPlante is not valid,
     * or with status {@code 404 (Not Found)} if the raunkierPlante is not found,
     * or with status {@code 500 (Internal Server Error)} if the raunkierPlante couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/raunkier-plantes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RaunkierPlante> partialUpdateRaunkierPlante(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RaunkierPlante raunkierPlante
    ) throws URISyntaxException {
        log.debug("REST request to partial update RaunkierPlante partially : {}, {}", id, raunkierPlante);
        if (raunkierPlante.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, raunkierPlante.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!raunkierPlanteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RaunkierPlante> result = raunkierPlanteService.partialUpdate(raunkierPlante);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, raunkierPlante.getId().toString())
        );
    }

    /**
     * {@code GET  /raunkier-plantes} : get all the raunkierPlantes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of raunkierPlantes in body.
     */
    @GetMapping("/raunkier-plantes")
    public ResponseEntity<List<RaunkierPlante>> getAllRaunkierPlantes(RaunkierPlanteCriteria criteria, Pageable pageable) {
        log.debug("REST request to get RaunkierPlantes by criteria: {}", criteria);
        Page<RaunkierPlante> page = raunkierPlanteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /raunkier-plantes/count} : count all the raunkierPlantes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/raunkier-plantes/count")
    public ResponseEntity<Long> countRaunkierPlantes(RaunkierPlanteCriteria criteria) {
        log.debug("REST request to count RaunkierPlantes by criteria: {}", criteria);
        return ResponseEntity.ok().body(raunkierPlanteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /raunkier-plantes/:id} : get the "id" raunkierPlante.
     *
     * @param id the id of the raunkierPlante to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the raunkierPlante, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/raunkier-plantes/{id}")
    public ResponseEntity<RaunkierPlante> getRaunkierPlante(@PathVariable Long id) {
        log.debug("REST request to get RaunkierPlante : {}", id);
        Optional<RaunkierPlante> raunkierPlante = raunkierPlanteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(raunkierPlante);
    }

    /**
     * {@code DELETE  /raunkier-plantes/:id} : delete the "id" raunkierPlante.
     *
     * @param id the id of the raunkierPlante to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/raunkier-plantes/{id}")
    public ResponseEntity<Void> deleteRaunkierPlante(@PathVariable Long id) {
        log.debug("REST request to delete RaunkierPlante : {}", id);
        raunkierPlanteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
