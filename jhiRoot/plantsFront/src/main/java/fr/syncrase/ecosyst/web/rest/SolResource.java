package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.Sol;
import fr.syncrase.ecosyst.repository.SolRepository;
import fr.syncrase.ecosyst.service.SolService;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.Sol}.
 */
@RestController
@RequestMapping("/api")
public class SolResource {

    private final Logger log = LoggerFactory.getLogger(SolResource.class);

    private static final String ENTITY_NAME = "sol";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SolService solService;

    private final SolRepository solRepository;

    public SolResource(SolService solService, SolRepository solRepository) {
        this.solService = solService;
        this.solRepository = solRepository;
    }

    /**
     * {@code POST  /sols} : Create a new sol.
     *
     * @param sol the sol to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sol, or with status {@code 400 (Bad Request)} if the sol has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sols")
    public Mono<ResponseEntity<Sol>> createSol(@RequestBody Sol sol) throws URISyntaxException {
        log.debug("REST request to save Sol : {}", sol);
        if (sol.getId() != null) {
            throw new BadRequestAlertException("A new sol cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return solService
            .save(sol)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/sols/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /sols/:id} : Updates an existing sol.
     *
     * @param id the id of the sol to save.
     * @param sol the sol to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sol,
     * or with status {@code 400 (Bad Request)} if the sol is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sol couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sols/{id}")
    public Mono<ResponseEntity<Sol>> updateSol(@PathVariable(value = "id", required = false) final Long id, @RequestBody Sol sol)
        throws URISyntaxException {
        log.debug("REST request to update Sol : {}, {}", id, sol);
        if (sol.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sol.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return solRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return solService
                    .save(sol)
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
     * {@code PATCH  /sols/:id} : Partial updates given fields of an existing sol, field will ignore if it is null
     *
     * @param id the id of the sol to save.
     * @param sol the sol to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sol,
     * or with status {@code 400 (Bad Request)} if the sol is not valid,
     * or with status {@code 404 (Not Found)} if the sol is not found,
     * or with status {@code 500 (Internal Server Error)} if the sol couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sols/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Sol>> partialUpdateSol(@PathVariable(value = "id", required = false) final Long id, @RequestBody Sol sol)
        throws URISyntaxException {
        log.debug("REST request to partial update Sol partially : {}, {}", id, sol);
        if (sol.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sol.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return solRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Sol> result = solService.partialUpdate(sol);

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
     * {@code GET  /sols} : get all the sols.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sols in body.
     */
    @GetMapping("/sols")
    public Mono<ResponseEntity<List<Sol>>> getAllSols(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of Sols");
        return solService
            .countAll()
            .zipWith(solService.findAll(pageable).collectList())
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
     * {@code GET  /sols/:id} : get the "id" sol.
     *
     * @param id the id of the sol to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sol, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sols/{id}")
    public Mono<ResponseEntity<Sol>> getSol(@PathVariable Long id) {
        log.debug("REST request to get Sol : {}", id);
        Mono<Sol> sol = solService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sol);
    }

    /**
     * {@code DELETE  /sols/:id} : delete the "id" sol.
     *
     * @param id the id of the sol to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sols/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteSol(@PathVariable Long id) {
        log.debug("REST request to delete Sol : {}", id);
        return solService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
