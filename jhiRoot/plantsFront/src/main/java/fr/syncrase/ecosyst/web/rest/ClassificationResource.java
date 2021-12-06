package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.Classification;
import fr.syncrase.ecosyst.repository.ClassificationRepository;
import fr.syncrase.ecosyst.service.ClassificationService;
import fr.syncrase.ecosyst.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.Classification}.
 */
@RestController
@RequestMapping("/api")
public class ClassificationResource {

    private final Logger log = LoggerFactory.getLogger(ClassificationResource.class);

    private static final String ENTITY_NAME = "classification";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClassificationService classificationService;

    private final ClassificationRepository classificationRepository;

    public ClassificationResource(ClassificationService classificationService, ClassificationRepository classificationRepository) {
        this.classificationService = classificationService;
        this.classificationRepository = classificationRepository;
    }

    /**
     * {@code POST  /classifications} : Create a new classification.
     *
     * @param classification the classification to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new classification, or with status {@code 400 (Bad Request)} if the classification has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/classifications")
    public Mono<ResponseEntity<Classification>> createClassification(@RequestBody Classification classification) throws URISyntaxException {
        log.debug("REST request to save Classification : {}", classification);
        if (classification.getId() != null) {
            throw new BadRequestAlertException("A new classification cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return classificationService
            .save(classification)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/classifications/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /classifications/:id} : Updates an existing classification.
     *
     * @param id the id of the classification to save.
     * @param classification the classification to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated classification,
     * or with status {@code 400 (Bad Request)} if the classification is not valid,
     * or with status {@code 500 (Internal Server Error)} if the classification couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/classifications/{id}")
    public Mono<ResponseEntity<Classification>> updateClassification(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Classification classification
    ) throws URISyntaxException {
        log.debug("REST request to update Classification : {}, {}", id, classification);
        if (classification.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, classification.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return classificationRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return classificationService
                    .save(classification)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /classifications/:id} : Partial updates given fields of an existing classification, field will ignore if it is null
     *
     * @param id the id of the classification to save.
     * @param classification the classification to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated classification,
     * or with status {@code 400 (Bad Request)} if the classification is not valid,
     * or with status {@code 404 (Not Found)} if the classification is not found,
     * or with status {@code 500 (Internal Server Error)} if the classification couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/classifications/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Classification>> partialUpdateClassification(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Classification classification
    ) throws URISyntaxException {
        log.debug("REST request to partial update Classification partially : {}, {}", id, classification);
        if (classification.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, classification.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return classificationRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Classification> result = classificationService.partialUpdate(classification);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /classifications} : get all the classifications.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of classifications in body.
     */
    @GetMapping("/classifications")
    public Mono<ResponseEntity<List<Classification>>> getAllClassifications(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of Classifications");
        return classificationService
            .countAll()
            .zipWith(classificationService.findAll(pageable).collectList())
            .map(countWithEntities -> {
                return ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            UriComponentsBuilder.fromHttpRequest(request),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2());
            });
    }

    /**
     * {@code GET  /classifications/:id} : get the "id" classification.
     *
     * @param id the id of the classification to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the classification, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/classifications/{id}")
    public Mono<ResponseEntity<Classification>> getClassification(@PathVariable Long id) {
        log.debug("REST request to get Classification : {}", id);
        Mono<Classification> classification = classificationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(classification);
    }

    /**
     * {@code DELETE  /classifications/:id} : delete the "id" classification.
     *
     * @param id the id of the classification to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/classifications/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteClassification(@PathVariable Long id) {
        log.debug("REST request to delete Classification : {}", id);
        return classificationService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
