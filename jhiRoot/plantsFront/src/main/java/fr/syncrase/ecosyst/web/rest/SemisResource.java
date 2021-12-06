package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.Semis;
import fr.syncrase.ecosyst.repository.SemisRepository;
import fr.syncrase.ecosyst.service.SemisService;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.Semis}.
 */
@RestController
@RequestMapping("/api")
public class SemisResource {

    private final Logger log = LoggerFactory.getLogger(SemisResource.class);

    private static final String ENTITY_NAME = "semis";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SemisService semisService;

    private final SemisRepository semisRepository;

    public SemisResource(SemisService semisService, SemisRepository semisRepository) {
        this.semisService = semisService;
        this.semisRepository = semisRepository;
    }

    /**
     * {@code POST  /semis} : Create a new semis.
     *
     * @param semis the semis to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new semis, or with status {@code 400 (Bad Request)} if the semis has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/semis")
    public Mono<ResponseEntity<Semis>> createSemis(@RequestBody Semis semis) throws URISyntaxException {
        log.debug("REST request to save Semis : {}", semis);
        if (semis.getId() != null) {
            throw new BadRequestAlertException("A new semis cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return semisService
            .save(semis)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/semis/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /semis/:id} : Updates an existing semis.
     *
     * @param id the id of the semis to save.
     * @param semis the semis to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated semis,
     * or with status {@code 400 (Bad Request)} if the semis is not valid,
     * or with status {@code 500 (Internal Server Error)} if the semis couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/semis/{id}")
    public Mono<ResponseEntity<Semis>> updateSemis(@PathVariable(value = "id", required = false) final Long id, @RequestBody Semis semis)
        throws URISyntaxException {
        log.debug("REST request to update Semis : {}, {}", id, semis);
        if (semis.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, semis.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return semisRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return semisService
                    .save(semis)
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
     * {@code PATCH  /semis/:id} : Partial updates given fields of an existing semis, field will ignore if it is null
     *
     * @param id the id of the semis to save.
     * @param semis the semis to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated semis,
     * or with status {@code 400 (Bad Request)} if the semis is not valid,
     * or with status {@code 404 (Not Found)} if the semis is not found,
     * or with status {@code 500 (Internal Server Error)} if the semis couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/semis/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Semis>> partialUpdateSemis(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Semis semis
    ) throws URISyntaxException {
        log.debug("REST request to partial update Semis partially : {}, {}", id, semis);
        if (semis.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, semis.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return semisRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Semis> result = semisService.partialUpdate(semis);

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
     * {@code GET  /semis} : get all the semis.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of semis in body.
     */
    @GetMapping("/semis")
    public Mono<ResponseEntity<List<Semis>>> getAllSemis(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of Semis");
        return semisService
            .countAll()
            .zipWith(semisService.findAll(pageable).collectList())
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
     * {@code GET  /semis/:id} : get the "id" semis.
     *
     * @param id the id of the semis to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the semis, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/semis/{id}")
    public Mono<ResponseEntity<Semis>> getSemis(@PathVariable Long id) {
        log.debug("REST request to get Semis : {}", id);
        Mono<Semis> semis = semisService.findOne(id);
        return ResponseUtil.wrapOrNotFound(semis);
    }

    /**
     * {@code DELETE  /semis/:id} : delete the "id" semis.
     *
     * @param id the id of the semis to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/semis/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteSemis(@PathVariable Long id) {
        log.debug("REST request to delete Semis : {}", id);
        return semisService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
