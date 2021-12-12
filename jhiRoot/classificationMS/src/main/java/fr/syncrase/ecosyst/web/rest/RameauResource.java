package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.Rameau;
import fr.syncrase.ecosyst.repository.RameauRepository;
import fr.syncrase.ecosyst.service.RameauQueryService;
import fr.syncrase.ecosyst.service.RameauService;
import fr.syncrase.ecosyst.service.criteria.RameauCriteria;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.Rameau}.
 */
@RestController
@RequestMapping("/api")
public class RameauResource {

    private final Logger log = LoggerFactory.getLogger(RameauResource.class);

    private static final String ENTITY_NAME = "classificationMsRameau";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RameauService rameauService;

    private final RameauRepository rameauRepository;

    private final RameauQueryService rameauQueryService;

    public RameauResource(RameauService rameauService, RameauRepository rameauRepository, RameauQueryService rameauQueryService) {
        this.rameauService = rameauService;
        this.rameauRepository = rameauRepository;
        this.rameauQueryService = rameauQueryService;
    }

    /**
     * {@code POST  /rameaus} : Create a new rameau.
     *
     * @param rameau the rameau to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rameau, or with status {@code 400 (Bad Request)} if the rameau has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rameaus")
    public ResponseEntity<Rameau> createRameau(@Valid @RequestBody Rameau rameau) throws URISyntaxException {
        log.debug("REST request to save Rameau : {}", rameau);
        if (rameau.getId() != null) {
            throw new BadRequestAlertException("A new rameau cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Rameau result = rameauService.save(rameau);
        return ResponseEntity
            .created(new URI("/api/rameaus/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rameaus/:id} : Updates an existing rameau.
     *
     * @param id the id of the rameau to save.
     * @param rameau the rameau to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rameau,
     * or with status {@code 400 (Bad Request)} if the rameau is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rameau couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rameaus/{id}")
    public ResponseEntity<Rameau> updateRameau(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Rameau rameau
    ) throws URISyntaxException {
        log.debug("REST request to update Rameau : {}, {}", id, rameau);
        if (rameau.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rameau.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rameauRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Rameau result = rameauService.save(rameau);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, rameau.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rameaus/:id} : Partial updates given fields of an existing rameau, field will ignore if it is null
     *
     * @param id the id of the rameau to save.
     * @param rameau the rameau to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rameau,
     * or with status {@code 400 (Bad Request)} if the rameau is not valid,
     * or with status {@code 404 (Not Found)} if the rameau is not found,
     * or with status {@code 500 (Internal Server Error)} if the rameau couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rameaus/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Rameau> partialUpdateRameau(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Rameau rameau
    ) throws URISyntaxException {
        log.debug("REST request to partial update Rameau partially : {}, {}", id, rameau);
        if (rameau.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rameau.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rameauRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Rameau> result = rameauService.partialUpdate(rameau);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, rameau.getId().toString())
        );
    }

    /**
     * {@code GET  /rameaus} : get all the rameaus.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rameaus in body.
     */
    @GetMapping("/rameaus")
    public ResponseEntity<List<Rameau>> getAllRameaus(RameauCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Rameaus by criteria: {}", criteria);
        Page<Rameau> page = rameauQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rameaus/count} : count all the rameaus.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/rameaus/count")
    public ResponseEntity<Long> countRameaus(RameauCriteria criteria) {
        log.debug("REST request to count Rameaus by criteria: {}", criteria);
        return ResponseEntity.ok().body(rameauQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /rameaus/:id} : get the "id" rameau.
     *
     * @param id the id of the rameau to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rameau, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rameaus/{id}")
    public ResponseEntity<Rameau> getRameau(@PathVariable Long id) {
        log.debug("REST request to get Rameau : {}", id);
        Optional<Rameau> rameau = rameauService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rameau);
    }

    /**
     * {@code DELETE  /rameaus/:id} : delete the "id" rameau.
     *
     * @param id the id of the rameau to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rameaus/{id}")
    public ResponseEntity<Void> deleteRameau(@PathVariable Long id) {
        log.debug("REST request to delete Rameau : {}", id);
        rameauService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
