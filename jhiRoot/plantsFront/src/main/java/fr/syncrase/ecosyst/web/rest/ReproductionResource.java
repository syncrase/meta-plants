package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.Reproduction;
import fr.syncrase.ecosyst.repository.ReproductionRepository;
import fr.syncrase.ecosyst.service.ReproductionService;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.Reproduction}.
 */
@RestController
@RequestMapping("/api")
public class ReproductionResource {

    private final Logger log = LoggerFactory.getLogger(ReproductionResource.class);

    private static final String ENTITY_NAME = "reproduction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReproductionService reproductionService;

    private final ReproductionRepository reproductionRepository;

    public ReproductionResource(ReproductionService reproductionService, ReproductionRepository reproductionRepository) {
        this.reproductionService = reproductionService;
        this.reproductionRepository = reproductionRepository;
    }

    /**
     * {@code POST  /reproductions} : Create a new reproduction.
     *
     * @param reproduction the reproduction to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reproduction, or with status {@code 400 (Bad Request)} if the reproduction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/reproductions")
    public Mono<ResponseEntity<Reproduction>> createReproduction(@RequestBody Reproduction reproduction) throws URISyntaxException {
        log.debug("REST request to save Reproduction : {}", reproduction);
        if (reproduction.getId() != null) {
            throw new BadRequestAlertException("A new reproduction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return reproductionService
            .save(reproduction)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/reproductions/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /reproductions/:id} : Updates an existing reproduction.
     *
     * @param id the id of the reproduction to save.
     * @param reproduction the reproduction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reproduction,
     * or with status {@code 400 (Bad Request)} if the reproduction is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reproduction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/reproductions/{id}")
    public Mono<ResponseEntity<Reproduction>> updateReproduction(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Reproduction reproduction
    ) throws URISyntaxException {
        log.debug("REST request to update Reproduction : {}, {}", id, reproduction);
        if (reproduction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reproduction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return reproductionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return reproductionService
                    .save(reproduction)
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
     * {@code PATCH  /reproductions/:id} : Partial updates given fields of an existing reproduction, field will ignore if it is null
     *
     * @param id the id of the reproduction to save.
     * @param reproduction the reproduction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reproduction,
     * or with status {@code 400 (Bad Request)} if the reproduction is not valid,
     * or with status {@code 404 (Not Found)} if the reproduction is not found,
     * or with status {@code 500 (Internal Server Error)} if the reproduction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/reproductions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Reproduction>> partialUpdateReproduction(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Reproduction reproduction
    ) throws URISyntaxException {
        log.debug("REST request to partial update Reproduction partially : {}, {}", id, reproduction);
        if (reproduction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reproduction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return reproductionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Reproduction> result = reproductionService.partialUpdate(reproduction);

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
     * {@code GET  /reproductions} : get all the reproductions.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reproductions in body.
     */
    @GetMapping("/reproductions")
    public Mono<ResponseEntity<List<Reproduction>>> getAllReproductions(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of Reproductions");
        return reproductionService
            .countAll()
            .zipWith(reproductionService.findAll(pageable).collectList())
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
     * {@code GET  /reproductions/:id} : get the "id" reproduction.
     *
     * @param id the id of the reproduction to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reproduction, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/reproductions/{id}")
    public Mono<ResponseEntity<Reproduction>> getReproduction(@PathVariable Long id) {
        log.debug("REST request to get Reproduction : {}", id);
        Mono<Reproduction> reproduction = reproductionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reproduction);
    }

    /**
     * {@code DELETE  /reproductions/:id} : delete the "id" reproduction.
     *
     * @param id the id of the reproduction to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/reproductions/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteReproduction(@PathVariable Long id) {
        log.debug("REST request to delete Reproduction : {}", id);
        return reproductionService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
