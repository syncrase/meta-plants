package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.ClassificationNom;
import fr.syncrase.ecosyst.repository.ClassificationNomRepository;
import fr.syncrase.ecosyst.service.ClassificationNomQueryService;
import fr.syncrase.ecosyst.service.ClassificationNomService;
import fr.syncrase.ecosyst.service.criteria.ClassificationNomCriteria;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.ClassificationNom}.
 */
@RestController
@RequestMapping("/api")
public class ClassificationNomResource {

    private final Logger log = LoggerFactory.getLogger(ClassificationNomResource.class);

    private static final String ENTITY_NAME = "classificationMsClassificationNom";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClassificationNomService classificationNomService;

    private final ClassificationNomRepository classificationNomRepository;

    private final ClassificationNomQueryService classificationNomQueryService;

    public ClassificationNomResource(
        ClassificationNomService classificationNomService,
        ClassificationNomRepository classificationNomRepository,
        ClassificationNomQueryService classificationNomQueryService
    ) {
        this.classificationNomService = classificationNomService;
        this.classificationNomRepository = classificationNomRepository;
        this.classificationNomQueryService = classificationNomQueryService;
    }

    /**
     * {@code POST  /classification-noms} : Create a new classificationNom.
     *
     * @param classificationNom the classificationNom to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new classificationNom, or with status {@code 400 (Bad Request)} if the classificationNom has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/classification-noms")
    public ResponseEntity<ClassificationNom> createClassificationNom(@Valid @RequestBody ClassificationNom classificationNom)
        throws URISyntaxException {
        log.debug("REST request to save ClassificationNom : {}", classificationNom);
        if (classificationNom.getId() != null) {
            throw new BadRequestAlertException("A new classificationNom cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClassificationNom result = classificationNomService.save(classificationNom);
        return ResponseEntity
            .created(new URI("/api/classification-noms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /classification-noms/:id} : Updates an existing classificationNom.
     *
     * @param id the id of the classificationNom to save.
     * @param classificationNom the classificationNom to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated classificationNom,
     * or with status {@code 400 (Bad Request)} if the classificationNom is not valid,
     * or with status {@code 500 (Internal Server Error)} if the classificationNom couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/classification-noms/{id}")
    public ResponseEntity<ClassificationNom> updateClassificationNom(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ClassificationNom classificationNom
    ) throws URISyntaxException {
        log.debug("REST request to update ClassificationNom : {}, {}", id, classificationNom);
        if (classificationNom.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, classificationNom.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!classificationNomRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ClassificationNom result = classificationNomService.save(classificationNom);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, classificationNom.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /classification-noms/:id} : Partial updates given fields of an existing classificationNom, field will ignore if it is null
     *
     * @param id the id of the classificationNom to save.
     * @param classificationNom the classificationNom to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated classificationNom,
     * or with status {@code 400 (Bad Request)} if the classificationNom is not valid,
     * or with status {@code 404 (Not Found)} if the classificationNom is not found,
     * or with status {@code 500 (Internal Server Error)} if the classificationNom couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/classification-noms/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ClassificationNom> partialUpdateClassificationNom(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ClassificationNom classificationNom
    ) throws URISyntaxException {
        log.debug("REST request to partial update ClassificationNom partially : {}, {}", id, classificationNom);
        if (classificationNom.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, classificationNom.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!classificationNomRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ClassificationNom> result = classificationNomService.partialUpdate(classificationNom);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, classificationNom.getId().toString())
        );
    }

    /**
     * {@code GET  /classification-noms} : get all the classificationNoms.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of classificationNoms in body.
     */
    @GetMapping("/classification-noms")
    public ResponseEntity<List<ClassificationNom>> getAllClassificationNoms(ClassificationNomCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ClassificationNoms by criteria: {}", criteria);
        Page<ClassificationNom> page = classificationNomQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /classification-noms/count} : count all the classificationNoms.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/classification-noms/count")
    public ResponseEntity<Long> countClassificationNoms(ClassificationNomCriteria criteria) {
        log.debug("REST request to count ClassificationNoms by criteria: {}", criteria);
        return ResponseEntity.ok().body(classificationNomQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /classification-noms/:id} : get the "id" classificationNom.
     *
     * @param id the id of the classificationNom to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the classificationNom, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/classification-noms/{id}")
    public ResponseEntity<ClassificationNom> getClassificationNom(@PathVariable Long id) {
        log.debug("REST request to get ClassificationNom : {}", id);
        Optional<ClassificationNom> classificationNom = classificationNomService.findOne(id);
        return ResponseUtil.wrapOrNotFound(classificationNom);
    }

    /**
     * {@code DELETE  /classification-noms/:id} : delete the "id" classificationNom.
     *
     * @param id the id of the classificationNom to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/classification-noms/{id}")
    public ResponseEntity<Void> deleteClassificationNom(@PathVariable Long id) {
        log.debug("REST request to delete ClassificationNom : {}", id);
        classificationNomService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
