package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.SuperRegne;
import fr.syncrase.ecosyst.repository.SuperRegneRepository;
import fr.syncrase.ecosyst.service.SuperRegneQueryService;
import fr.syncrase.ecosyst.service.SuperRegneService;
import fr.syncrase.ecosyst.service.criteria.SuperRegneCriteria;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.SuperRegne}.
 */
@RestController
@RequestMapping("/api")
public class SuperRegneResource {

    private final Logger log = LoggerFactory.getLogger(SuperRegneResource.class);

    private static final String ENTITY_NAME = "classificationMsSuperRegne";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SuperRegneService superRegneService;

    private final SuperRegneRepository superRegneRepository;

    private final SuperRegneQueryService superRegneQueryService;

    public SuperRegneResource(
        SuperRegneService superRegneService,
        SuperRegneRepository superRegneRepository,
        SuperRegneQueryService superRegneQueryService
    ) {
        this.superRegneService = superRegneService;
        this.superRegneRepository = superRegneRepository;
        this.superRegneQueryService = superRegneQueryService;
    }

    /**
     * {@code POST  /super-regnes} : Create a new superRegne.
     *
     * @param superRegne the superRegne to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new superRegne, or with status {@code 400 (Bad Request)} if the superRegne has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/super-regnes")
    public ResponseEntity<SuperRegne> createSuperRegne(@Valid @RequestBody SuperRegne superRegne) throws URISyntaxException {
        log.debug("REST request to save SuperRegne : {}", superRegne);
        if (superRegne.getId() != null) {
            throw new BadRequestAlertException("A new superRegne cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SuperRegne result = superRegneService.save(superRegne);
        return ResponseEntity
            .created(new URI("/api/super-regnes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /super-regnes/:id} : Updates an existing superRegne.
     *
     * @param id the id of the superRegne to save.
     * @param superRegne the superRegne to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated superRegne,
     * or with status {@code 400 (Bad Request)} if the superRegne is not valid,
     * or with status {@code 500 (Internal Server Error)} if the superRegne couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/super-regnes/{id}")
    public ResponseEntity<SuperRegne> updateSuperRegne(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SuperRegne superRegne
    ) throws URISyntaxException {
        log.debug("REST request to update SuperRegne : {}, {}", id, superRegne);
        if (superRegne.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, superRegne.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!superRegneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SuperRegne result = superRegneService.save(superRegne);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, superRegne.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /super-regnes/:id} : Partial updates given fields of an existing superRegne, field will ignore if it is null
     *
     * @param id the id of the superRegne to save.
     * @param superRegne the superRegne to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated superRegne,
     * or with status {@code 400 (Bad Request)} if the superRegne is not valid,
     * or with status {@code 404 (Not Found)} if the superRegne is not found,
     * or with status {@code 500 (Internal Server Error)} if the superRegne couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/super-regnes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SuperRegne> partialUpdateSuperRegne(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SuperRegne superRegne
    ) throws URISyntaxException {
        log.debug("REST request to partial update SuperRegne partially : {}, {}", id, superRegne);
        if (superRegne.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, superRegne.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!superRegneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SuperRegne> result = superRegneService.partialUpdate(superRegne);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, superRegne.getId().toString())
        );
    }

    /**
     * {@code GET  /super-regnes} : get all the superRegnes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of superRegnes in body.
     */
    @GetMapping("/super-regnes")
    public ResponseEntity<List<SuperRegne>> getAllSuperRegnes(SuperRegneCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SuperRegnes by criteria: {}", criteria);
        Page<SuperRegne> page = superRegneQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /super-regnes/count} : count all the superRegnes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/super-regnes/count")
    public ResponseEntity<Long> countSuperRegnes(SuperRegneCriteria criteria) {
        log.debug("REST request to count SuperRegnes by criteria: {}", criteria);
        return ResponseEntity.ok().body(superRegneQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /super-regnes/:id} : get the "id" superRegne.
     *
     * @param id the id of the superRegne to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the superRegne, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/super-regnes/{id}")
    public ResponseEntity<SuperRegne> getSuperRegne(@PathVariable Long id) {
        log.debug("REST request to get SuperRegne : {}", id);
        Optional<SuperRegne> superRegne = superRegneService.findOne(id);
        return ResponseUtil.wrapOrNotFound(superRegne);
    }

    /**
     * {@code DELETE  /super-regnes/:id} : delete the "id" superRegne.
     *
     * @param id the id of the superRegne to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/super-regnes/{id}")
    public ResponseEntity<Void> deleteSuperRegne(@PathVariable Long id) {
        log.debug("REST request to delete SuperRegne : {}", id);
        superRegneService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
