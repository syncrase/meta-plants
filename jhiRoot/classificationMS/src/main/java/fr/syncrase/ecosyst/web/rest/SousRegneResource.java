package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.SousRegne;
import fr.syncrase.ecosyst.repository.SousRegneRepository;
import fr.syncrase.ecosyst.service.SousRegneQueryService;
import fr.syncrase.ecosyst.service.SousRegneService;
import fr.syncrase.ecosyst.service.criteria.SousRegneCriteria;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.SousRegne}.
 */
@RestController
@RequestMapping("/api")
public class SousRegneResource {

    private final Logger log = LoggerFactory.getLogger(SousRegneResource.class);

    private static final String ENTITY_NAME = "classificationMsSousRegne";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SousRegneService sousRegneService;

    private final SousRegneRepository sousRegneRepository;

    private final SousRegneQueryService sousRegneQueryService;

    public SousRegneResource(
        SousRegneService sousRegneService,
        SousRegneRepository sousRegneRepository,
        SousRegneQueryService sousRegneQueryService
    ) {
        this.sousRegneService = sousRegneService;
        this.sousRegneRepository = sousRegneRepository;
        this.sousRegneQueryService = sousRegneQueryService;
    }

    /**
     * {@code POST  /sous-regnes} : Create a new sousRegne.
     *
     * @param sousRegne the sousRegne to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sousRegne, or with status {@code 400 (Bad Request)} if the sousRegne has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sous-regnes")
    public ResponseEntity<SousRegne> createSousRegne(@Valid @RequestBody SousRegne sousRegne) throws URISyntaxException {
        log.debug("REST request to save SousRegne : {}", sousRegne);
        if (sousRegne.getId() != null) {
            throw new BadRequestAlertException("A new sousRegne cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SousRegne result = sousRegneService.save(sousRegne);
        return ResponseEntity
            .created(new URI("/api/sous-regnes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sous-regnes/:id} : Updates an existing sousRegne.
     *
     * @param id the id of the sousRegne to save.
     * @param sousRegne the sousRegne to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sousRegne,
     * or with status {@code 400 (Bad Request)} if the sousRegne is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sousRegne couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sous-regnes/{id}")
    public ResponseEntity<SousRegne> updateSousRegne(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SousRegne sousRegne
    ) throws URISyntaxException {
        log.debug("REST request to update SousRegne : {}, {}", id, sousRegne);
        if (sousRegne.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sousRegne.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sousRegneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SousRegne result = sousRegneService.save(sousRegne);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sousRegne.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sous-regnes/:id} : Partial updates given fields of an existing sousRegne, field will ignore if it is null
     *
     * @param id the id of the sousRegne to save.
     * @param sousRegne the sousRegne to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sousRegne,
     * or with status {@code 400 (Bad Request)} if the sousRegne is not valid,
     * or with status {@code 404 (Not Found)} if the sousRegne is not found,
     * or with status {@code 500 (Internal Server Error)} if the sousRegne couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sous-regnes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SousRegne> partialUpdateSousRegne(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SousRegne sousRegne
    ) throws URISyntaxException {
        log.debug("REST request to partial update SousRegne partially : {}, {}", id, sousRegne);
        if (sousRegne.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sousRegne.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sousRegneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SousRegne> result = sousRegneService.partialUpdate(sousRegne);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sousRegne.getId().toString())
        );
    }

    /**
     * {@code GET  /sous-regnes} : get all the sousRegnes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sousRegnes in body.
     */
    @GetMapping("/sous-regnes")
    public ResponseEntity<List<SousRegne>> getAllSousRegnes(SousRegneCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SousRegnes by criteria: {}", criteria);
        Page<SousRegne> page = sousRegneQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sous-regnes/count} : count all the sousRegnes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/sous-regnes/count")
    public ResponseEntity<Long> countSousRegnes(SousRegneCriteria criteria) {
        log.debug("REST request to count SousRegnes by criteria: {}", criteria);
        return ResponseEntity.ok().body(sousRegneQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sous-regnes/:id} : get the "id" sousRegne.
     *
     * @param id the id of the sousRegne to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sousRegne, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sous-regnes/{id}")
    public ResponseEntity<SousRegne> getSousRegne(@PathVariable Long id) {
        log.debug("REST request to get SousRegne : {}", id);
        Optional<SousRegne> sousRegne = sousRegneService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sousRegne);
    }

    /**
     * {@code DELETE  /sous-regnes/:id} : delete the "id" sousRegne.
     *
     * @param id the id of the sousRegne to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sous-regnes/{id}")
    public ResponseEntity<Void> deleteSousRegne(@PathVariable Long id) {
        log.debug("REST request to delete SousRegne : {}", id);
        sousRegneService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
