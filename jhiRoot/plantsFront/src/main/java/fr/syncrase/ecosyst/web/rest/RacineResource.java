package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.Racine;
import fr.syncrase.ecosyst.repository.RacineRepository;
import fr.syncrase.ecosyst.service.RacineService;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.Racine}.
 */
@RestController
@RequestMapping("/api")
public class RacineResource {

    private final Logger log = LoggerFactory.getLogger(RacineResource.class);

    private static final String ENTITY_NAME = "racine";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RacineService racineService;

    private final RacineRepository racineRepository;

    public RacineResource(RacineService racineService, RacineRepository racineRepository) {
        this.racineService = racineService;
        this.racineRepository = racineRepository;
    }

    /**
     * {@code POST  /racines} : Create a new racine.
     *
     * @param racine the racine to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new racine, or with status {@code 400 (Bad Request)} if the racine has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/racines")
    public Mono<ResponseEntity<Racine>> createRacine(@RequestBody Racine racine) throws URISyntaxException {
        log.debug("REST request to save Racine : {}", racine);
        if (racine.getId() != null) {
            throw new BadRequestAlertException("A new racine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return racineService
            .save(racine)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/racines/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /racines/:id} : Updates an existing racine.
     *
     * @param id the id of the racine to save.
     * @param racine the racine to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated racine,
     * or with status {@code 400 (Bad Request)} if the racine is not valid,
     * or with status {@code 500 (Internal Server Error)} if the racine couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/racines/{id}")
    public Mono<ResponseEntity<Racine>> updateRacine(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Racine racine
    ) throws URISyntaxException {
        log.debug("REST request to update Racine : {}, {}", id, racine);
        if (racine.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, racine.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return racineRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return racineService
                    .save(racine)
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
     * {@code PATCH  /racines/:id} : Partial updates given fields of an existing racine, field will ignore if it is null
     *
     * @param id the id of the racine to save.
     * @param racine the racine to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated racine,
     * or with status {@code 400 (Bad Request)} if the racine is not valid,
     * or with status {@code 404 (Not Found)} if the racine is not found,
     * or with status {@code 500 (Internal Server Error)} if the racine couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/racines/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Racine>> partialUpdateRacine(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Racine racine
    ) throws URISyntaxException {
        log.debug("REST request to partial update Racine partially : {}, {}", id, racine);
        if (racine.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, racine.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return racineRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Racine> result = racineService.partialUpdate(racine);

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
     * {@code GET  /racines} : get all the racines.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of racines in body.
     */
    @GetMapping("/racines")
    public Mono<ResponseEntity<List<Racine>>> getAllRacines(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of Racines");
        return racineService
            .countAll()
            .zipWith(racineService.findAll(pageable).collectList())
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
     * {@code GET  /racines/:id} : get the "id" racine.
     *
     * @param id the id of the racine to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the racine, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/racines/{id}")
    public Mono<ResponseEntity<Racine>> getRacine(@PathVariable Long id) {
        log.debug("REST request to get Racine : {}", id);
        Mono<Racine> racine = racineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(racine);
    }

    /**
     * {@code DELETE  /racines/:id} : delete the "id" racine.
     *
     * @param id the id of the racine to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/racines/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteRacine(@PathVariable Long id) {
        log.debug("REST request to delete Racine : {}", id);
        return racineService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
