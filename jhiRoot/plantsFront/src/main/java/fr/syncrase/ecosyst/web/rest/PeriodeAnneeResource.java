package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.PeriodeAnnee;
import fr.syncrase.ecosyst.repository.PeriodeAnneeRepository;
import fr.syncrase.ecosyst.service.PeriodeAnneeService;
import fr.syncrase.ecosyst.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.PeriodeAnnee}.
 */
@RestController
@RequestMapping("/api")
public class PeriodeAnneeResource {

    private final Logger log = LoggerFactory.getLogger(PeriodeAnneeResource.class);

    private static final String ENTITY_NAME = "periodeAnnee";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PeriodeAnneeService periodeAnneeService;

    private final PeriodeAnneeRepository periodeAnneeRepository;

    public PeriodeAnneeResource(PeriodeAnneeService periodeAnneeService, PeriodeAnneeRepository periodeAnneeRepository) {
        this.periodeAnneeService = periodeAnneeService;
        this.periodeAnneeRepository = periodeAnneeRepository;
    }

    /**
     * {@code POST  /periode-annees} : Create a new periodeAnnee.
     *
     * @param periodeAnnee the periodeAnnee to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new periodeAnnee, or with status {@code 400 (Bad Request)} if the periodeAnnee has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/periode-annees")
    public Mono<ResponseEntity<PeriodeAnnee>> createPeriodeAnnee(@Valid @RequestBody PeriodeAnnee periodeAnnee) throws URISyntaxException {
        log.debug("REST request to save PeriodeAnnee : {}", periodeAnnee);
        if (periodeAnnee.getId() != null) {
            throw new BadRequestAlertException("A new periodeAnnee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return periodeAnneeService
            .save(periodeAnnee)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/periode-annees/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /periode-annees/:id} : Updates an existing periodeAnnee.
     *
     * @param id the id of the periodeAnnee to save.
     * @param periodeAnnee the periodeAnnee to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated periodeAnnee,
     * or with status {@code 400 (Bad Request)} if the periodeAnnee is not valid,
     * or with status {@code 500 (Internal Server Error)} if the periodeAnnee couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/periode-annees/{id}")
    public Mono<ResponseEntity<PeriodeAnnee>> updatePeriodeAnnee(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PeriodeAnnee periodeAnnee
    ) throws URISyntaxException {
        log.debug("REST request to update PeriodeAnnee : {}, {}", id, periodeAnnee);
        if (periodeAnnee.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, periodeAnnee.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return periodeAnneeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return periodeAnneeService
                    .save(periodeAnnee)
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
     * {@code PATCH  /periode-annees/:id} : Partial updates given fields of an existing periodeAnnee, field will ignore if it is null
     *
     * @param id the id of the periodeAnnee to save.
     * @param periodeAnnee the periodeAnnee to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated periodeAnnee,
     * or with status {@code 400 (Bad Request)} if the periodeAnnee is not valid,
     * or with status {@code 404 (Not Found)} if the periodeAnnee is not found,
     * or with status {@code 500 (Internal Server Error)} if the periodeAnnee couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/periode-annees/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<PeriodeAnnee>> partialUpdatePeriodeAnnee(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PeriodeAnnee periodeAnnee
    ) throws URISyntaxException {
        log.debug("REST request to partial update PeriodeAnnee partially : {}, {}", id, periodeAnnee);
        if (periodeAnnee.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, periodeAnnee.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return periodeAnneeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<PeriodeAnnee> result = periodeAnneeService.partialUpdate(periodeAnnee);

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
     * {@code GET  /periode-annees} : get all the periodeAnnees.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of periodeAnnees in body.
     */
    @GetMapping("/periode-annees")
    public Mono<ResponseEntity<List<PeriodeAnnee>>> getAllPeriodeAnnees(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of PeriodeAnnees");
        return periodeAnneeService
            .countAll()
            .zipWith(periodeAnneeService.findAll(pageable).collectList())
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
     * {@code GET  /periode-annees/:id} : get the "id" periodeAnnee.
     *
     * @param id the id of the periodeAnnee to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the periodeAnnee, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/periode-annees/{id}")
    public Mono<ResponseEntity<PeriodeAnnee>> getPeriodeAnnee(@PathVariable Long id) {
        log.debug("REST request to get PeriodeAnnee : {}", id);
        Mono<PeriodeAnnee> periodeAnnee = periodeAnneeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(periodeAnnee);
    }

    /**
     * {@code DELETE  /periode-annees/:id} : delete the "id" periodeAnnee.
     *
     * @param id the id of the periodeAnnee to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/periode-annees/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deletePeriodeAnnee(@PathVariable Long id) {
        log.debug("REST request to delete PeriodeAnnee : {}", id);
        return periodeAnneeService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
