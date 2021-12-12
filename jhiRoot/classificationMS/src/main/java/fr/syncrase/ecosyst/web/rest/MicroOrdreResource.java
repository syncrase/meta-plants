package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.MicroOrdre;
import fr.syncrase.ecosyst.repository.MicroOrdreRepository;
import fr.syncrase.ecosyst.service.MicroOrdreQueryService;
import fr.syncrase.ecosyst.service.MicroOrdreService;
import fr.syncrase.ecosyst.service.criteria.MicroOrdreCriteria;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.MicroOrdre}.
 */
@RestController
@RequestMapping("/api")
public class MicroOrdreResource {

    private final Logger log = LoggerFactory.getLogger(MicroOrdreResource.class);

    private static final String ENTITY_NAME = "classificationMsMicroOrdre";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MicroOrdreService microOrdreService;

    private final MicroOrdreRepository microOrdreRepository;

    private final MicroOrdreQueryService microOrdreQueryService;

    public MicroOrdreResource(
        MicroOrdreService microOrdreService,
        MicroOrdreRepository microOrdreRepository,
        MicroOrdreQueryService microOrdreQueryService
    ) {
        this.microOrdreService = microOrdreService;
        this.microOrdreRepository = microOrdreRepository;
        this.microOrdreQueryService = microOrdreQueryService;
    }

    /**
     * {@code POST  /micro-ordres} : Create a new microOrdre.
     *
     * @param microOrdre the microOrdre to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new microOrdre, or with status {@code 400 (Bad Request)} if the microOrdre has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/micro-ordres")
    public ResponseEntity<MicroOrdre> createMicroOrdre(@Valid @RequestBody MicroOrdre microOrdre) throws URISyntaxException {
        log.debug("REST request to save MicroOrdre : {}", microOrdre);
        if (microOrdre.getId() != null) {
            throw new BadRequestAlertException("A new microOrdre cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MicroOrdre result = microOrdreService.save(microOrdre);
        return ResponseEntity
            .created(new URI("/api/micro-ordres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /micro-ordres/:id} : Updates an existing microOrdre.
     *
     * @param id the id of the microOrdre to save.
     * @param microOrdre the microOrdre to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated microOrdre,
     * or with status {@code 400 (Bad Request)} if the microOrdre is not valid,
     * or with status {@code 500 (Internal Server Error)} if the microOrdre couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/micro-ordres/{id}")
    public ResponseEntity<MicroOrdre> updateMicroOrdre(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MicroOrdre microOrdre
    ) throws URISyntaxException {
        log.debug("REST request to update MicroOrdre : {}, {}", id, microOrdre);
        if (microOrdre.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, microOrdre.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!microOrdreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MicroOrdre result = microOrdreService.save(microOrdre);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, microOrdre.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /micro-ordres/:id} : Partial updates given fields of an existing microOrdre, field will ignore if it is null
     *
     * @param id the id of the microOrdre to save.
     * @param microOrdre the microOrdre to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated microOrdre,
     * or with status {@code 400 (Bad Request)} if the microOrdre is not valid,
     * or with status {@code 404 (Not Found)} if the microOrdre is not found,
     * or with status {@code 500 (Internal Server Error)} if the microOrdre couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/micro-ordres/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MicroOrdre> partialUpdateMicroOrdre(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MicroOrdre microOrdre
    ) throws URISyntaxException {
        log.debug("REST request to partial update MicroOrdre partially : {}, {}", id, microOrdre);
        if (microOrdre.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, microOrdre.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!microOrdreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MicroOrdre> result = microOrdreService.partialUpdate(microOrdre);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, microOrdre.getId().toString())
        );
    }

    /**
     * {@code GET  /micro-ordres} : get all the microOrdres.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of microOrdres in body.
     */
    @GetMapping("/micro-ordres")
    public ResponseEntity<List<MicroOrdre>> getAllMicroOrdres(MicroOrdreCriteria criteria, Pageable pageable) {
        log.debug("REST request to get MicroOrdres by criteria: {}", criteria);
        Page<MicroOrdre> page = microOrdreQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /micro-ordres/count} : count all the microOrdres.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/micro-ordres/count")
    public ResponseEntity<Long> countMicroOrdres(MicroOrdreCriteria criteria) {
        log.debug("REST request to count MicroOrdres by criteria: {}", criteria);
        return ResponseEntity.ok().body(microOrdreQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /micro-ordres/:id} : get the "id" microOrdre.
     *
     * @param id the id of the microOrdre to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the microOrdre, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/micro-ordres/{id}")
    public ResponseEntity<MicroOrdre> getMicroOrdre(@PathVariable Long id) {
        log.debug("REST request to get MicroOrdre : {}", id);
        Optional<MicroOrdre> microOrdre = microOrdreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(microOrdre);
    }

    /**
     * {@code DELETE  /micro-ordres/:id} : delete the "id" microOrdre.
     *
     * @param id the id of the microOrdre to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/micro-ordres/{id}")
    public ResponseEntity<Void> deleteMicroOrdre(@PathVariable Long id) {
        log.debug("REST request to delete MicroOrdre : {}", id);
        microOrdreService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
