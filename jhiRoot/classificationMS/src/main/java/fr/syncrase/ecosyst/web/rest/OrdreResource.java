package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.Ordre;
import fr.syncrase.ecosyst.repository.OrdreRepository;
import fr.syncrase.ecosyst.service.OrdreQueryService;
import fr.syncrase.ecosyst.service.OrdreService;
import fr.syncrase.ecosyst.service.criteria.OrdreCriteria;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.Ordre}.
 */
@RestController
@RequestMapping("/api")
public class OrdreResource {

    private final Logger log = LoggerFactory.getLogger(OrdreResource.class);

    private static final String ENTITY_NAME = "classificationMsOrdre";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrdreService ordreService;

    private final OrdreRepository ordreRepository;

    private final OrdreQueryService ordreQueryService;

    public OrdreResource(OrdreService ordreService, OrdreRepository ordreRepository, OrdreQueryService ordreQueryService) {
        this.ordreService = ordreService;
        this.ordreRepository = ordreRepository;
        this.ordreQueryService = ordreQueryService;
    }

    /**
     * {@code POST  /ordres} : Create a new ordre.
     *
     * @param ordre the ordre to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ordre, or with status {@code 400 (Bad Request)} if the ordre has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ordres")
    public ResponseEntity<Ordre> createOrdre(@Valid @RequestBody Ordre ordre) throws URISyntaxException {
        log.debug("REST request to save Ordre : {}", ordre);
        if (ordre.getId() != null) {
            throw new BadRequestAlertException("A new ordre cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ordre result = ordreService.save(ordre);
        return ResponseEntity
            .created(new URI("/api/ordres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ordres/:id} : Updates an existing ordre.
     *
     * @param id the id of the ordre to save.
     * @param ordre the ordre to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordre,
     * or with status {@code 400 (Bad Request)} if the ordre is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ordre couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ordres/{id}")
    public ResponseEntity<Ordre> updateOrdre(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Ordre ordre)
        throws URISyntaxException {
        log.debug("REST request to update Ordre : {}, {}", id, ordre);
        if (ordre.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordre.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Ordre result = ordreService.save(ordre);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ordre.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ordres/:id} : Partial updates given fields of an existing ordre, field will ignore if it is null
     *
     * @param id the id of the ordre to save.
     * @param ordre the ordre to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordre,
     * or with status {@code 400 (Bad Request)} if the ordre is not valid,
     * or with status {@code 404 (Not Found)} if the ordre is not found,
     * or with status {@code 500 (Internal Server Error)} if the ordre couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ordres/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Ordre> partialUpdateOrdre(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Ordre ordre
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ordre partially : {}, {}", id, ordre);
        if (ordre.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordre.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Ordre> result = ordreService.partialUpdate(ordre);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ordre.getId().toString())
        );
    }

    /**
     * {@code GET  /ordres} : get all the ordres.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordres in body.
     */
    @GetMapping("/ordres")
    public ResponseEntity<List<Ordre>> getAllOrdres(OrdreCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Ordres by criteria: {}", criteria);
        Page<Ordre> page = ordreQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ordres/count} : count all the ordres.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/ordres/count")
    public ResponseEntity<Long> countOrdres(OrdreCriteria criteria) {
        log.debug("REST request to count Ordres by criteria: {}", criteria);
        return ResponseEntity.ok().body(ordreQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ordres/:id} : get the "id" ordre.
     *
     * @param id the id of the ordre to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ordre, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ordres/{id}")
    public ResponseEntity<Ordre> getOrdre(@PathVariable Long id) {
        log.debug("REST request to get Ordre : {}", id);
        Optional<Ordre> ordre = ordreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ordre);
    }

    /**
     * {@code DELETE  /ordres/:id} : delete the "id" ordre.
     *
     * @param id the id of the ordre to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ordres/{id}")
    public ResponseEntity<Void> deleteOrdre(@PathVariable Long id) {
        log.debug("REST request to delete Ordre : {}", id);
        ordreService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
