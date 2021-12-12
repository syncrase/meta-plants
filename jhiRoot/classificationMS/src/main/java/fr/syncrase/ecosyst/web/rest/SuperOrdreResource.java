package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.SuperOrdre;
import fr.syncrase.ecosyst.repository.SuperOrdreRepository;
import fr.syncrase.ecosyst.service.SuperOrdreQueryService;
import fr.syncrase.ecosyst.service.SuperOrdreService;
import fr.syncrase.ecosyst.service.criteria.SuperOrdreCriteria;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.SuperOrdre}.
 */
@RestController
@RequestMapping("/api")
public class SuperOrdreResource {

    private final Logger log = LoggerFactory.getLogger(SuperOrdreResource.class);

    private static final String ENTITY_NAME = "classificationMsSuperOrdre";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SuperOrdreService superOrdreService;

    private final SuperOrdreRepository superOrdreRepository;

    private final SuperOrdreQueryService superOrdreQueryService;

    public SuperOrdreResource(
        SuperOrdreService superOrdreService,
        SuperOrdreRepository superOrdreRepository,
        SuperOrdreQueryService superOrdreQueryService
    ) {
        this.superOrdreService = superOrdreService;
        this.superOrdreRepository = superOrdreRepository;
        this.superOrdreQueryService = superOrdreQueryService;
    }

    /**
     * {@code POST  /super-ordres} : Create a new superOrdre.
     *
     * @param superOrdre the superOrdre to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new superOrdre, or with status {@code 400 (Bad Request)} if the superOrdre has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/super-ordres")
    public ResponseEntity<SuperOrdre> createSuperOrdre(@Valid @RequestBody SuperOrdre superOrdre) throws URISyntaxException {
        log.debug("REST request to save SuperOrdre : {}", superOrdre);
        if (superOrdre.getId() != null) {
            throw new BadRequestAlertException("A new superOrdre cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SuperOrdre result = superOrdreService.save(superOrdre);
        return ResponseEntity
            .created(new URI("/api/super-ordres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /super-ordres/:id} : Updates an existing superOrdre.
     *
     * @param id the id of the superOrdre to save.
     * @param superOrdre the superOrdre to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated superOrdre,
     * or with status {@code 400 (Bad Request)} if the superOrdre is not valid,
     * or with status {@code 500 (Internal Server Error)} if the superOrdre couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/super-ordres/{id}")
    public ResponseEntity<SuperOrdre> updateSuperOrdre(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SuperOrdre superOrdre
    ) throws URISyntaxException {
        log.debug("REST request to update SuperOrdre : {}, {}", id, superOrdre);
        if (superOrdre.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, superOrdre.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!superOrdreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SuperOrdre result = superOrdreService.save(superOrdre);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, superOrdre.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /super-ordres/:id} : Partial updates given fields of an existing superOrdre, field will ignore if it is null
     *
     * @param id the id of the superOrdre to save.
     * @param superOrdre the superOrdre to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated superOrdre,
     * or with status {@code 400 (Bad Request)} if the superOrdre is not valid,
     * or with status {@code 404 (Not Found)} if the superOrdre is not found,
     * or with status {@code 500 (Internal Server Error)} if the superOrdre couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/super-ordres/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SuperOrdre> partialUpdateSuperOrdre(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SuperOrdre superOrdre
    ) throws URISyntaxException {
        log.debug("REST request to partial update SuperOrdre partially : {}, {}", id, superOrdre);
        if (superOrdre.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, superOrdre.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!superOrdreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SuperOrdre> result = superOrdreService.partialUpdate(superOrdre);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, superOrdre.getId().toString())
        );
    }

    /**
     * {@code GET  /super-ordres} : get all the superOrdres.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of superOrdres in body.
     */
    @GetMapping("/super-ordres")
    public ResponseEntity<List<SuperOrdre>> getAllSuperOrdres(SuperOrdreCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SuperOrdres by criteria: {}", criteria);
        Page<SuperOrdre> page = superOrdreQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /super-ordres/count} : count all the superOrdres.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/super-ordres/count")
    public ResponseEntity<Long> countSuperOrdres(SuperOrdreCriteria criteria) {
        log.debug("REST request to count SuperOrdres by criteria: {}", criteria);
        return ResponseEntity.ok().body(superOrdreQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /super-ordres/:id} : get the "id" superOrdre.
     *
     * @param id the id of the superOrdre to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the superOrdre, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/super-ordres/{id}")
    public ResponseEntity<SuperOrdre> getSuperOrdre(@PathVariable Long id) {
        log.debug("REST request to get SuperOrdre : {}", id);
        Optional<SuperOrdre> superOrdre = superOrdreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(superOrdre);
    }

    /**
     * {@code DELETE  /super-ordres/:id} : delete the "id" superOrdre.
     *
     * @param id the id of the superOrdre to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/super-ordres/{id}")
    public ResponseEntity<Void> deleteSuperOrdre(@PathVariable Long id) {
        log.debug("REST request to delete SuperOrdre : {}", id);
        superOrdreService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
