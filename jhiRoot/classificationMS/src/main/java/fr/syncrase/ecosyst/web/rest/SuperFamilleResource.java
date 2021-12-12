package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.SuperFamille;
import fr.syncrase.ecosyst.repository.SuperFamilleRepository;
import fr.syncrase.ecosyst.service.SuperFamilleQueryService;
import fr.syncrase.ecosyst.service.SuperFamilleService;
import fr.syncrase.ecosyst.service.criteria.SuperFamilleCriteria;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.SuperFamille}.
 */
@RestController
@RequestMapping("/api")
public class SuperFamilleResource {

    private final Logger log = LoggerFactory.getLogger(SuperFamilleResource.class);

    private static final String ENTITY_NAME = "classificationMsSuperFamille";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SuperFamilleService superFamilleService;

    private final SuperFamilleRepository superFamilleRepository;

    private final SuperFamilleQueryService superFamilleQueryService;

    public SuperFamilleResource(
        SuperFamilleService superFamilleService,
        SuperFamilleRepository superFamilleRepository,
        SuperFamilleQueryService superFamilleQueryService
    ) {
        this.superFamilleService = superFamilleService;
        this.superFamilleRepository = superFamilleRepository;
        this.superFamilleQueryService = superFamilleQueryService;
    }

    /**
     * {@code POST  /super-familles} : Create a new superFamille.
     *
     * @param superFamille the superFamille to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new superFamille, or with status {@code 400 (Bad Request)} if the superFamille has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/super-familles")
    public ResponseEntity<SuperFamille> createSuperFamille(@Valid @RequestBody SuperFamille superFamille) throws URISyntaxException {
        log.debug("REST request to save SuperFamille : {}", superFamille);
        if (superFamille.getId() != null) {
            throw new BadRequestAlertException("A new superFamille cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SuperFamille result = superFamilleService.save(superFamille);
        return ResponseEntity
            .created(new URI("/api/super-familles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /super-familles/:id} : Updates an existing superFamille.
     *
     * @param id the id of the superFamille to save.
     * @param superFamille the superFamille to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated superFamille,
     * or with status {@code 400 (Bad Request)} if the superFamille is not valid,
     * or with status {@code 500 (Internal Server Error)} if the superFamille couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/super-familles/{id}")
    public ResponseEntity<SuperFamille> updateSuperFamille(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SuperFamille superFamille
    ) throws URISyntaxException {
        log.debug("REST request to update SuperFamille : {}, {}", id, superFamille);
        if (superFamille.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, superFamille.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!superFamilleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SuperFamille result = superFamilleService.save(superFamille);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, superFamille.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /super-familles/:id} : Partial updates given fields of an existing superFamille, field will ignore if it is null
     *
     * @param id the id of the superFamille to save.
     * @param superFamille the superFamille to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated superFamille,
     * or with status {@code 400 (Bad Request)} if the superFamille is not valid,
     * or with status {@code 404 (Not Found)} if the superFamille is not found,
     * or with status {@code 500 (Internal Server Error)} if the superFamille couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/super-familles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SuperFamille> partialUpdateSuperFamille(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SuperFamille superFamille
    ) throws URISyntaxException {
        log.debug("REST request to partial update SuperFamille partially : {}, {}", id, superFamille);
        if (superFamille.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, superFamille.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!superFamilleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SuperFamille> result = superFamilleService.partialUpdate(superFamille);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, superFamille.getId().toString())
        );
    }

    /**
     * {@code GET  /super-familles} : get all the superFamilles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of superFamilles in body.
     */
    @GetMapping("/super-familles")
    public ResponseEntity<List<SuperFamille>> getAllSuperFamilles(SuperFamilleCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SuperFamilles by criteria: {}", criteria);
        Page<SuperFamille> page = superFamilleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /super-familles/count} : count all the superFamilles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/super-familles/count")
    public ResponseEntity<Long> countSuperFamilles(SuperFamilleCriteria criteria) {
        log.debug("REST request to count SuperFamilles by criteria: {}", criteria);
        return ResponseEntity.ok().body(superFamilleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /super-familles/:id} : get the "id" superFamille.
     *
     * @param id the id of the superFamille to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the superFamille, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/super-familles/{id}")
    public ResponseEntity<SuperFamille> getSuperFamille(@PathVariable Long id) {
        log.debug("REST request to get SuperFamille : {}", id);
        Optional<SuperFamille> superFamille = superFamilleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(superFamille);
    }

    /**
     * {@code DELETE  /super-familles/:id} : delete the "id" superFamille.
     *
     * @param id the id of the superFamille to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/super-familles/{id}")
    public ResponseEntity<Void> deleteSuperFamille(@PathVariable Long id) {
        log.debug("REST request to delete SuperFamille : {}", id);
        superFamilleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
