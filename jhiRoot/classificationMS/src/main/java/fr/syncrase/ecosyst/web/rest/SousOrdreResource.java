package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.SousOrdre;
import fr.syncrase.ecosyst.repository.SousOrdreRepository;
import fr.syncrase.ecosyst.service.SousOrdreQueryService;
import fr.syncrase.ecosyst.service.SousOrdreService;
import fr.syncrase.ecosyst.service.criteria.SousOrdreCriteria;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.SousOrdre}.
 */
@RestController
@RequestMapping("/api")
public class SousOrdreResource {

    private final Logger log = LoggerFactory.getLogger(SousOrdreResource.class);

    private static final String ENTITY_NAME = "classificationMsSousOrdre";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SousOrdreService sousOrdreService;

    private final SousOrdreRepository sousOrdreRepository;

    private final SousOrdreQueryService sousOrdreQueryService;

    public SousOrdreResource(
        SousOrdreService sousOrdreService,
        SousOrdreRepository sousOrdreRepository,
        SousOrdreQueryService sousOrdreQueryService
    ) {
        this.sousOrdreService = sousOrdreService;
        this.sousOrdreRepository = sousOrdreRepository;
        this.sousOrdreQueryService = sousOrdreQueryService;
    }

    /**
     * {@code POST  /sous-ordres} : Create a new sousOrdre.
     *
     * @param sousOrdre the sousOrdre to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sousOrdre, or with status {@code 400 (Bad Request)} if the sousOrdre has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sous-ordres")
    public ResponseEntity<SousOrdre> createSousOrdre(@Valid @RequestBody SousOrdre sousOrdre) throws URISyntaxException {
        log.debug("REST request to save SousOrdre : {}", sousOrdre);
        if (sousOrdre.getId() != null) {
            throw new BadRequestAlertException("A new sousOrdre cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SousOrdre result = sousOrdreService.save(sousOrdre);
        return ResponseEntity
            .created(new URI("/api/sous-ordres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sous-ordres/:id} : Updates an existing sousOrdre.
     *
     * @param id the id of the sousOrdre to save.
     * @param sousOrdre the sousOrdre to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sousOrdre,
     * or with status {@code 400 (Bad Request)} if the sousOrdre is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sousOrdre couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sous-ordres/{id}")
    public ResponseEntity<SousOrdre> updateSousOrdre(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SousOrdre sousOrdre
    ) throws URISyntaxException {
        log.debug("REST request to update SousOrdre : {}, {}", id, sousOrdre);
        if (sousOrdre.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sousOrdre.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sousOrdreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SousOrdre result = sousOrdreService.save(sousOrdre);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sousOrdre.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sous-ordres/:id} : Partial updates given fields of an existing sousOrdre, field will ignore if it is null
     *
     * @param id the id of the sousOrdre to save.
     * @param sousOrdre the sousOrdre to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sousOrdre,
     * or with status {@code 400 (Bad Request)} if the sousOrdre is not valid,
     * or with status {@code 404 (Not Found)} if the sousOrdre is not found,
     * or with status {@code 500 (Internal Server Error)} if the sousOrdre couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sous-ordres/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SousOrdre> partialUpdateSousOrdre(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SousOrdre sousOrdre
    ) throws URISyntaxException {
        log.debug("REST request to partial update SousOrdre partially : {}, {}", id, sousOrdre);
        if (sousOrdre.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sousOrdre.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sousOrdreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SousOrdre> result = sousOrdreService.partialUpdate(sousOrdre);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sousOrdre.getId().toString())
        );
    }

    /**
     * {@code GET  /sous-ordres} : get all the sousOrdres.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sousOrdres in body.
     */
    @GetMapping("/sous-ordres")
    public ResponseEntity<List<SousOrdre>> getAllSousOrdres(SousOrdreCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SousOrdres by criteria: {}", criteria);
        Page<SousOrdre> page = sousOrdreQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sous-ordres/count} : count all the sousOrdres.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/sous-ordres/count")
    public ResponseEntity<Long> countSousOrdres(SousOrdreCriteria criteria) {
        log.debug("REST request to count SousOrdres by criteria: {}", criteria);
        return ResponseEntity.ok().body(sousOrdreQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sous-ordres/:id} : get the "id" sousOrdre.
     *
     * @param id the id of the sousOrdre to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sousOrdre, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sous-ordres/{id}")
    public ResponseEntity<SousOrdre> getSousOrdre(@PathVariable Long id) {
        log.debug("REST request to get SousOrdre : {}", id);
        Optional<SousOrdre> sousOrdre = sousOrdreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sousOrdre);
    }

    /**
     * {@code DELETE  /sous-ordres/:id} : delete the "id" sousOrdre.
     *
     * @param id the id of the sousOrdre to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sous-ordres/{id}")
    public ResponseEntity<Void> deleteSousOrdre(@PathVariable Long id) {
        log.debug("REST request to delete SousOrdre : {}", id);
        sousOrdreService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
