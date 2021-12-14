package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.CronquistPlante;
import fr.syncrase.ecosyst.repository.CronquistPlanteRepository;
import fr.syncrase.ecosyst.service.CronquistPlanteQueryService;
import fr.syncrase.ecosyst.service.CronquistPlanteService;
import fr.syncrase.ecosyst.service.criteria.CronquistPlanteCriteria;
import fr.syncrase.ecosyst.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.CronquistPlante}.
 */
@RestController
@RequestMapping("/api")
public class CronquistPlanteResource {

    private final Logger log = LoggerFactory.getLogger(CronquistPlanteResource.class);

    private static final String ENTITY_NAME = "plantsMsCronquistPlante";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CronquistPlanteService cronquistPlanteService;

    private final CronquistPlanteRepository cronquistPlanteRepository;

    private final CronquistPlanteQueryService cronquistPlanteQueryService;

    public CronquistPlanteResource(
        CronquistPlanteService cronquistPlanteService,
        CronquistPlanteRepository cronquistPlanteRepository,
        CronquistPlanteQueryService cronquistPlanteQueryService
    ) {
        this.cronquistPlanteService = cronquistPlanteService;
        this.cronquistPlanteRepository = cronquistPlanteRepository;
        this.cronquistPlanteQueryService = cronquistPlanteQueryService;
    }

    /**
     * {@code POST  /cronquist-plantes} : Create a new cronquistPlante.
     *
     * @param cronquistPlante the cronquistPlante to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cronquistPlante, or with status {@code 400 (Bad Request)} if the cronquistPlante has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cronquist-plantes")
    public ResponseEntity<CronquistPlante> createCronquistPlante(@RequestBody CronquistPlante cronquistPlante) throws URISyntaxException {
        log.debug("REST request to save CronquistPlante : {}", cronquistPlante);
        if (cronquistPlante.getId() != null) {
            throw new BadRequestAlertException("A new cronquistPlante cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CronquistPlante result = cronquistPlanteService.save(cronquistPlante);
        return ResponseEntity
            .created(new URI("/api/cronquist-plantes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cronquist-plantes/:id} : Updates an existing cronquistPlante.
     *
     * @param id the id of the cronquistPlante to save.
     * @param cronquistPlante the cronquistPlante to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cronquistPlante,
     * or with status {@code 400 (Bad Request)} if the cronquistPlante is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cronquistPlante couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cronquist-plantes/{id}")
    public ResponseEntity<CronquistPlante> updateCronquistPlante(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CronquistPlante cronquistPlante
    ) throws URISyntaxException {
        log.debug("REST request to update CronquistPlante : {}, {}", id, cronquistPlante);
        if (cronquistPlante.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cronquistPlante.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cronquistPlanteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CronquistPlante result = cronquistPlanteService.save(cronquistPlante);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cronquistPlante.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cronquist-plantes/:id} : Partial updates given fields of an existing cronquistPlante, field will ignore if it is null
     *
     * @param id the id of the cronquistPlante to save.
     * @param cronquistPlante the cronquistPlante to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cronquistPlante,
     * or with status {@code 400 (Bad Request)} if the cronquistPlante is not valid,
     * or with status {@code 404 (Not Found)} if the cronquistPlante is not found,
     * or with status {@code 500 (Internal Server Error)} if the cronquistPlante couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cronquist-plantes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CronquistPlante> partialUpdateCronquistPlante(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CronquistPlante cronquistPlante
    ) throws URISyntaxException {
        log.debug("REST request to partial update CronquistPlante partially : {}, {}", id, cronquistPlante);
        if (cronquistPlante.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cronquistPlante.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cronquistPlanteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CronquistPlante> result = cronquistPlanteService.partialUpdate(cronquistPlante);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cronquistPlante.getId().toString())
        );
    }

    /**
     * {@code GET  /cronquist-plantes} : get all the cronquistPlantes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cronquistPlantes in body.
     */
    @GetMapping("/cronquist-plantes")
    public ResponseEntity<List<CronquistPlante>> getAllCronquistPlantes(CronquistPlanteCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CronquistPlantes by criteria: {}", criteria);
        Page<CronquistPlante> page = cronquistPlanteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cronquist-plantes/count} : count all the cronquistPlantes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/cronquist-plantes/count")
    public ResponseEntity<Long> countCronquistPlantes(CronquistPlanteCriteria criteria) {
        log.debug("REST request to count CronquistPlantes by criteria: {}", criteria);
        return ResponseEntity.ok().body(cronquistPlanteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cronquist-plantes/:id} : get the "id" cronquistPlante.
     *
     * @param id the id of the cronquistPlante to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cronquistPlante, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cronquist-plantes/{id}")
    public ResponseEntity<CronquistPlante> getCronquistPlante(@PathVariable Long id) {
        log.debug("REST request to get CronquistPlante : {}", id);
        Optional<CronquistPlante> cronquistPlante = cronquistPlanteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cronquistPlante);
    }

    /**
     * {@code DELETE  /cronquist-plantes/:id} : delete the "id" cronquistPlante.
     *
     * @param id the id of the cronquistPlante to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cronquist-plantes/{id}")
    public ResponseEntity<Void> deleteCronquistPlante(@PathVariable Long id) {
        log.debug("REST request to delete CronquistPlante : {}", id);
        cronquistPlanteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
