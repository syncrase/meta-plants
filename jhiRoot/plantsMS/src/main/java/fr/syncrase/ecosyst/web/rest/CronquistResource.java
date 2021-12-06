package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.Cronquist;
import fr.syncrase.ecosyst.repository.CronquistRepository;
import fr.syncrase.ecosyst.service.CronquistQueryService;
import fr.syncrase.ecosyst.service.CronquistService;
import fr.syncrase.ecosyst.service.criteria.CronquistCriteria;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.Cronquist}.
 */
@RestController
@RequestMapping("/api")
public class CronquistResource {

    private final Logger log = LoggerFactory.getLogger(CronquistResource.class);

    private static final String ENTITY_NAME = "plantsMsCronquist";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CronquistService cronquistService;

    private final CronquistRepository cronquistRepository;

    private final CronquistQueryService cronquistQueryService;

    public CronquistResource(
        CronquistService cronquistService,
        CronquistRepository cronquistRepository,
        CronquistQueryService cronquistQueryService
    ) {
        this.cronquistService = cronquistService;
        this.cronquistRepository = cronquistRepository;
        this.cronquistQueryService = cronquistQueryService;
    }

    /**
     * {@code POST  /cronquists} : Create a new cronquist.
     *
     * @param cronquist the cronquist to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cronquist, or with status {@code 400 (Bad Request)} if the cronquist has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cronquists")
    public ResponseEntity<Cronquist> createCronquist(@RequestBody Cronquist cronquist) throws URISyntaxException {
        log.debug("REST request to save Cronquist : {}", cronquist);
        if (cronquist.getId() != null) {
            throw new BadRequestAlertException("A new cronquist cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cronquist result = cronquistService.save(cronquist);
        return ResponseEntity
            .created(new URI("/api/cronquists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cronquists/:id} : Updates an existing cronquist.
     *
     * @param id the id of the cronquist to save.
     * @param cronquist the cronquist to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cronquist,
     * or with status {@code 400 (Bad Request)} if the cronquist is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cronquist couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cronquists/{id}")
    public ResponseEntity<Cronquist> updateCronquist(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Cronquist cronquist
    ) throws URISyntaxException {
        log.debug("REST request to update Cronquist : {}, {}", id, cronquist);
        if (cronquist.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cronquist.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cronquistRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Cronquist result = cronquistService.save(cronquist);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cronquist.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cronquists/:id} : Partial updates given fields of an existing cronquist, field will ignore if it is null
     *
     * @param id the id of the cronquist to save.
     * @param cronquist the cronquist to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cronquist,
     * or with status {@code 400 (Bad Request)} if the cronquist is not valid,
     * or with status {@code 404 (Not Found)} if the cronquist is not found,
     * or with status {@code 500 (Internal Server Error)} if the cronquist couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cronquists/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Cronquist> partialUpdateCronquist(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Cronquist cronquist
    ) throws URISyntaxException {
        log.debug("REST request to partial update Cronquist partially : {}, {}", id, cronquist);
        if (cronquist.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cronquist.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cronquistRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Cronquist> result = cronquistService.partialUpdate(cronquist);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cronquist.getId().toString())
        );
    }

    /**
     * {@code GET  /cronquists} : get all the cronquists.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cronquists in body.
     */
    @GetMapping("/cronquists")
    public ResponseEntity<List<Cronquist>> getAllCronquists(CronquistCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Cronquists by criteria: {}", criteria);
        Page<Cronquist> page = cronquistQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cronquists/count} : count all the cronquists.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/cronquists/count")
    public ResponseEntity<Long> countCronquists(CronquistCriteria criteria) {
        log.debug("REST request to count Cronquists by criteria: {}", criteria);
        return ResponseEntity.ok().body(cronquistQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cronquists/:id} : get the "id" cronquist.
     *
     * @param id the id of the cronquist to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cronquist, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cronquists/{id}")
    public ResponseEntity<Cronquist> getCronquist(@PathVariable Long id) {
        log.debug("REST request to get Cronquist : {}", id);
        Optional<Cronquist> cronquist = cronquistService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cronquist);
    }

    /**
     * {@code DELETE  /cronquists/:id} : delete the "id" cronquist.
     *
     * @param id the id of the cronquist to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cronquists/{id}")
    public ResponseEntity<Void> deleteCronquist(@PathVariable Long id) {
        log.debug("REST request to delete Cronquist : {}", id);
        cronquistService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
