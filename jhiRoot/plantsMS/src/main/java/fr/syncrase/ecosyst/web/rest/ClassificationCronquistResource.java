package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.ClassificationCronquist;
import fr.syncrase.ecosyst.repository.ClassificationCronquistRepository;
import fr.syncrase.ecosyst.service.ClassificationCronquistQueryService;
import fr.syncrase.ecosyst.service.ClassificationCronquistService;
import fr.syncrase.ecosyst.service.criteria.ClassificationCronquistCriteria;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.ClassificationCronquist}.
 */
@RestController
@RequestMapping("/api")
public class ClassificationCronquistResource {

    private final Logger log = LoggerFactory.getLogger(ClassificationCronquistResource.class);

    private static final String ENTITY_NAME = "plantsMsClassificationCronquist";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClassificationCronquistService classificationCronquistService;

    private final ClassificationCronquistRepository classificationCronquistRepository;

    private final ClassificationCronquistQueryService classificationCronquistQueryService;

    public ClassificationCronquistResource(
        ClassificationCronquistService classificationCronquistService,
        ClassificationCronquistRepository classificationCronquistRepository,
        ClassificationCronquistQueryService classificationCronquistQueryService
    ) {
        this.classificationCronquistService = classificationCronquistService;
        this.classificationCronquistRepository = classificationCronquistRepository;
        this.classificationCronquistQueryService = classificationCronquistQueryService;
    }

    /**
     * {@code POST  /classification-cronquists} : Create a new classificationCronquist.
     *
     * @param classificationCronquist the classificationCronquist to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new classificationCronquist, or with status {@code 400 (Bad Request)} if the classificationCronquist has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/classification-cronquists")
    public ResponseEntity<ClassificationCronquist> createClassificationCronquist(
        @RequestBody ClassificationCronquist classificationCronquist
    ) throws URISyntaxException {
        log.debug("REST request to save ClassificationCronquist : {}", classificationCronquist);
        if (classificationCronquist.getId() != null) {
            throw new BadRequestAlertException("A new classificationCronquist cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClassificationCronquist result = classificationCronquistService.save(classificationCronquist);
        return ResponseEntity
            .created(new URI("/api/classification-cronquists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /classification-cronquists/:id} : Updates an existing classificationCronquist.
     *
     * @param id the id of the classificationCronquist to save.
     * @param classificationCronquist the classificationCronquist to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated classificationCronquist,
     * or with status {@code 400 (Bad Request)} if the classificationCronquist is not valid,
     * or with status {@code 500 (Internal Server Error)} if the classificationCronquist couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/classification-cronquists/{id}")
    public ResponseEntity<ClassificationCronquist> updateClassificationCronquist(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ClassificationCronquist classificationCronquist
    ) throws URISyntaxException {
        log.debug("REST request to update ClassificationCronquist : {}, {}", id, classificationCronquist);
        if (classificationCronquist.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, classificationCronquist.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!classificationCronquistRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ClassificationCronquist result = classificationCronquistService.save(classificationCronquist);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, classificationCronquist.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /classification-cronquists/:id} : Partial updates given fields of an existing classificationCronquist, field will ignore if it is null
     *
     * @param id the id of the classificationCronquist to save.
     * @param classificationCronquist the classificationCronquist to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated classificationCronquist,
     * or with status {@code 400 (Bad Request)} if the classificationCronquist is not valid,
     * or with status {@code 404 (Not Found)} if the classificationCronquist is not found,
     * or with status {@code 500 (Internal Server Error)} if the classificationCronquist couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/classification-cronquists/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ClassificationCronquist> partialUpdateClassificationCronquist(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ClassificationCronquist classificationCronquist
    ) throws URISyntaxException {
        log.debug("REST request to partial update ClassificationCronquist partially : {}, {}", id, classificationCronquist);
        if (classificationCronquist.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, classificationCronquist.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!classificationCronquistRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ClassificationCronquist> result = classificationCronquistService.partialUpdate(classificationCronquist);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, classificationCronquist.getId().toString())
        );
    }

    /**
     * {@code GET  /classification-cronquists} : get all the classificationCronquists.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of classificationCronquists in body.
     */
    @GetMapping("/classification-cronquists")
    public ResponseEntity<List<ClassificationCronquist>> getAllClassificationCronquists(
        ClassificationCronquistCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get ClassificationCronquists by criteria: {}", criteria);
        Page<ClassificationCronquist> page = classificationCronquistQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /classification-cronquists/count} : count all the classificationCronquists.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/classification-cronquists/count")
    public ResponseEntity<Long> countClassificationCronquists(ClassificationCronquistCriteria criteria) {
        log.debug("REST request to count ClassificationCronquists by criteria: {}", criteria);
        return ResponseEntity.ok().body(classificationCronquistQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /classification-cronquists/:id} : get the "id" classificationCronquist.
     *
     * @param id the id of the classificationCronquist to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the classificationCronquist, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/classification-cronquists/{id}")
    public ResponseEntity<ClassificationCronquist> getClassificationCronquist(@PathVariable Long id) {
        log.debug("REST request to get ClassificationCronquist : {}", id);
        Optional<ClassificationCronquist> classificationCronquist = classificationCronquistService.findOne(id);
        return ResponseUtil.wrapOrNotFound(classificationCronquist);
    }

    /**
     * {@code DELETE  /classification-cronquists/:id} : delete the "id" classificationCronquist.
     *
     * @param id the id of the classificationCronquist to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/classification-cronquists/{id}")
    public ResponseEntity<Void> deleteClassificationCronquist(@PathVariable Long id) {
        log.debug("REST request to delete ClassificationCronquist : {}", id);
        classificationCronquistService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
