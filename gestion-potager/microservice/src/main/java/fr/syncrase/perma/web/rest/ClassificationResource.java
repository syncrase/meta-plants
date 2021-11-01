package fr.syncrase.perma.web.rest;

import fr.syncrase.perma.service.ClassificationService;
import fr.syncrase.perma.web.rest.errors.BadRequestAlertException;
import fr.syncrase.perma.service.dto.ClassificationDTO;
import fr.syncrase.perma.service.dto.ClassificationCriteria;
import fr.syncrase.perma.service.ClassificationQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link fr.syncrase.perma.domain.Classification}.
 */
@RestController
@RequestMapping("/api")
public class ClassificationResource {

    private final Logger log = LoggerFactory.getLogger(ClassificationResource.class);

    private static final String ENTITY_NAME = "microserviceClassification";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClassificationService classificationService;

    private final ClassificationQueryService classificationQueryService;

    public ClassificationResource(ClassificationService classificationService, ClassificationQueryService classificationQueryService) {
        this.classificationService = classificationService;
        this.classificationQueryService = classificationQueryService;
    }

    /**
     * {@code POST  /classifications} : Create a new classification.
     *
     * @param classificationDTO the classificationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new classificationDTO, or with status {@code 400 (Bad Request)} if the classification has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/classifications")
    public ResponseEntity<ClassificationDTO> createClassification(@RequestBody ClassificationDTO classificationDTO) throws URISyntaxException {
        log.debug("REST request to save Classification : {}", classificationDTO);
        if (classificationDTO.getId() != null) {
            throw new BadRequestAlertException("A new classification cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClassificationDTO result = classificationService.save(classificationDTO);
        return ResponseEntity.created(new URI("/api/classifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /classifications} : Updates an existing classification.
     *
     * @param classificationDTO the classificationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated classificationDTO,
     * or with status {@code 400 (Bad Request)} if the classificationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the classificationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/classifications")
    public ResponseEntity<ClassificationDTO> updateClassification(@RequestBody ClassificationDTO classificationDTO) throws URISyntaxException {
        log.debug("REST request to update Classification : {}", classificationDTO);
        if (classificationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ClassificationDTO result = classificationService.save(classificationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, classificationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /classifications} : get all the classifications.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of classifications in body.
     */
    @GetMapping("/classifications")
    public ResponseEntity<List<ClassificationDTO>> getAllClassifications(ClassificationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Classifications by criteria: {}", criteria);
        Page<ClassificationDTO> page = classificationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /classifications/count} : count all the classifications.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/classifications/count")
    public ResponseEntity<Long> countClassifications(ClassificationCriteria criteria) {
        log.debug("REST request to count Classifications by criteria: {}", criteria);
        return ResponseEntity.ok().body(classificationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /classifications/:id} : get the "id" classification.
     *
     * @param id the id of the classificationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the classificationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/classifications/{id}")
    public ResponseEntity<ClassificationDTO> getClassification(@PathVariable Long id) {
        log.debug("REST request to get Classification : {}", id);
        Optional<ClassificationDTO> classificationDTO = classificationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(classificationDTO);
    }

    /**
     * {@code DELETE  /classifications/:id} : delete the "id" classification.
     *
     * @param id the id of the classificationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/classifications/{id}")
    public ResponseEntity<Void> deleteClassification(@PathVariable Long id) {
        log.debug("REST request to delete Classification : {}", id);
        classificationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
