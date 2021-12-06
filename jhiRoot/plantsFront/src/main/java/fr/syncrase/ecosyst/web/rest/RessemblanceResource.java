package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.Ressemblance;
import fr.syncrase.ecosyst.repository.RessemblanceRepository;
import fr.syncrase.ecosyst.service.RessemblanceService;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.Ressemblance}.
 */
@RestController
@RequestMapping("/api")
public class RessemblanceResource {

    private final Logger log = LoggerFactory.getLogger(RessemblanceResource.class);

    private static final String ENTITY_NAME = "ressemblance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RessemblanceService ressemblanceService;

    private final RessemblanceRepository ressemblanceRepository;

    public RessemblanceResource(RessemblanceService ressemblanceService, RessemblanceRepository ressemblanceRepository) {
        this.ressemblanceService = ressemblanceService;
        this.ressemblanceRepository = ressemblanceRepository;
    }

    /**
     * {@code POST  /ressemblances} : Create a new ressemblance.
     *
     * @param ressemblance the ressemblance to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ressemblance, or with status {@code 400 (Bad Request)} if the ressemblance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ressemblances")
    public Mono<ResponseEntity<Ressemblance>> createRessemblance(@RequestBody Ressemblance ressemblance) throws URISyntaxException {
        log.debug("REST request to save Ressemblance : {}", ressemblance);
        if (ressemblance.getId() != null) {
            throw new BadRequestAlertException("A new ressemblance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return ressemblanceService
            .save(ressemblance)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/ressemblances/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /ressemblances/:id} : Updates an existing ressemblance.
     *
     * @param id the id of the ressemblance to save.
     * @param ressemblance the ressemblance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ressemblance,
     * or with status {@code 400 (Bad Request)} if the ressemblance is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ressemblance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ressemblances/{id}")
    public Mono<ResponseEntity<Ressemblance>> updateRessemblance(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Ressemblance ressemblance
    ) throws URISyntaxException {
        log.debug("REST request to update Ressemblance : {}, {}", id, ressemblance);
        if (ressemblance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ressemblance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return ressemblanceRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return ressemblanceService
                    .save(ressemblance)
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
     * {@code PATCH  /ressemblances/:id} : Partial updates given fields of an existing ressemblance, field will ignore if it is null
     *
     * @param id the id of the ressemblance to save.
     * @param ressemblance the ressemblance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ressemblance,
     * or with status {@code 400 (Bad Request)} if the ressemblance is not valid,
     * or with status {@code 404 (Not Found)} if the ressemblance is not found,
     * or with status {@code 500 (Internal Server Error)} if the ressemblance couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ressemblances/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Ressemblance>> partialUpdateRessemblance(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Ressemblance ressemblance
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ressemblance partially : {}, {}", id, ressemblance);
        if (ressemblance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ressemblance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return ressemblanceRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Ressemblance> result = ressemblanceService.partialUpdate(ressemblance);

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
     * {@code GET  /ressemblances} : get all the ressemblances.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ressemblances in body.
     */
    @GetMapping("/ressemblances")
    public Mono<ResponseEntity<List<Ressemblance>>> getAllRessemblances(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of Ressemblances");
        return ressemblanceService
            .countAll()
            .zipWith(ressemblanceService.findAll(pageable).collectList())
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
     * {@code GET  /ressemblances/:id} : get the "id" ressemblance.
     *
     * @param id the id of the ressemblance to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ressemblance, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ressemblances/{id}")
    public Mono<ResponseEntity<Ressemblance>> getRessemblance(@PathVariable Long id) {
        log.debug("REST request to get Ressemblance : {}", id);
        Mono<Ressemblance> ressemblance = ressemblanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ressemblance);
    }

    /**
     * {@code DELETE  /ressemblances/:id} : delete the "id" ressemblance.
     *
     * @param id the id of the ressemblance to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ressemblances/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteRessemblance(@PathVariable Long id) {
        log.debug("REST request to delete Ressemblance : {}", id);
        return ressemblanceService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
