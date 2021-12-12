package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.SousSection;
import fr.syncrase.ecosyst.repository.SousSectionRepository;
import fr.syncrase.ecosyst.service.SousSectionQueryService;
import fr.syncrase.ecosyst.service.SousSectionService;
import fr.syncrase.ecosyst.service.criteria.SousSectionCriteria;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.SousSection}.
 */
@RestController
@RequestMapping("/api")
public class SousSectionResource {

    private final Logger log = LoggerFactory.getLogger(SousSectionResource.class);

    private static final String ENTITY_NAME = "classificationMsSousSection";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SousSectionService sousSectionService;

    private final SousSectionRepository sousSectionRepository;

    private final SousSectionQueryService sousSectionQueryService;

    public SousSectionResource(
        SousSectionService sousSectionService,
        SousSectionRepository sousSectionRepository,
        SousSectionQueryService sousSectionQueryService
    ) {
        this.sousSectionService = sousSectionService;
        this.sousSectionRepository = sousSectionRepository;
        this.sousSectionQueryService = sousSectionQueryService;
    }

    /**
     * {@code POST  /sous-sections} : Create a new sousSection.
     *
     * @param sousSection the sousSection to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sousSection, or with status {@code 400 (Bad Request)} if the sousSection has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sous-sections")
    public ResponseEntity<SousSection> createSousSection(@Valid @RequestBody SousSection sousSection) throws URISyntaxException {
        log.debug("REST request to save SousSection : {}", sousSection);
        if (sousSection.getId() != null) {
            throw new BadRequestAlertException("A new sousSection cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SousSection result = sousSectionService.save(sousSection);
        return ResponseEntity
            .created(new URI("/api/sous-sections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sous-sections/:id} : Updates an existing sousSection.
     *
     * @param id the id of the sousSection to save.
     * @param sousSection the sousSection to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sousSection,
     * or with status {@code 400 (Bad Request)} if the sousSection is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sousSection couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sous-sections/{id}")
    public ResponseEntity<SousSection> updateSousSection(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SousSection sousSection
    ) throws URISyntaxException {
        log.debug("REST request to update SousSection : {}, {}", id, sousSection);
        if (sousSection.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sousSection.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sousSectionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SousSection result = sousSectionService.save(sousSection);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sousSection.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sous-sections/:id} : Partial updates given fields of an existing sousSection, field will ignore if it is null
     *
     * @param id the id of the sousSection to save.
     * @param sousSection the sousSection to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sousSection,
     * or with status {@code 400 (Bad Request)} if the sousSection is not valid,
     * or with status {@code 404 (Not Found)} if the sousSection is not found,
     * or with status {@code 500 (Internal Server Error)} if the sousSection couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sous-sections/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SousSection> partialUpdateSousSection(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SousSection sousSection
    ) throws URISyntaxException {
        log.debug("REST request to partial update SousSection partially : {}, {}", id, sousSection);
        if (sousSection.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sousSection.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sousSectionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SousSection> result = sousSectionService.partialUpdate(sousSection);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sousSection.getId().toString())
        );
    }

    /**
     * {@code GET  /sous-sections} : get all the sousSections.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sousSections in body.
     */
    @GetMapping("/sous-sections")
    public ResponseEntity<List<SousSection>> getAllSousSections(SousSectionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SousSections by criteria: {}", criteria);
        Page<SousSection> page = sousSectionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sous-sections/count} : count all the sousSections.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/sous-sections/count")
    public ResponseEntity<Long> countSousSections(SousSectionCriteria criteria) {
        log.debug("REST request to count SousSections by criteria: {}", criteria);
        return ResponseEntity.ok().body(sousSectionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sous-sections/:id} : get the "id" sousSection.
     *
     * @param id the id of the sousSection to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sousSection, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sous-sections/{id}")
    public ResponseEntity<SousSection> getSousSection(@PathVariable Long id) {
        log.debug("REST request to get SousSection : {}", id);
        Optional<SousSection> sousSection = sousSectionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sousSection);
    }

    /**
     * {@code DELETE  /sous-sections/:id} : delete the "id" sousSection.
     *
     * @param id the id of the sousSection to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sous-sections/{id}")
    public ResponseEntity<Void> deleteSousSection(@PathVariable Long id) {
        log.debug("REST request to delete SousSection : {}", id);
        sousSectionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
