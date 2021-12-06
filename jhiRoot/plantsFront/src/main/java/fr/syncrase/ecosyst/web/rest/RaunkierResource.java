package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.Raunkier;
import fr.syncrase.ecosyst.repository.RaunkierRepository;
import fr.syncrase.ecosyst.service.RaunkierService;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.Raunkier}.
 */
@RestController
@RequestMapping("/api")
public class RaunkierResource {

    private final Logger log = LoggerFactory.getLogger(RaunkierResource.class);

    private static final String ENTITY_NAME = "raunkier";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RaunkierService raunkierService;

    private final RaunkierRepository raunkierRepository;

    public RaunkierResource(RaunkierService raunkierService, RaunkierRepository raunkierRepository) {
        this.raunkierService = raunkierService;
        this.raunkierRepository = raunkierRepository;
    }

    /**
     * {@code POST  /raunkiers} : Create a new raunkier.
     *
     * @param raunkier the raunkier to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new raunkier, or with status {@code 400 (Bad Request)} if the raunkier has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/raunkiers")
    public Mono<ResponseEntity<Raunkier>> createRaunkier(@Valid @RequestBody Raunkier raunkier) throws URISyntaxException {
        log.debug("REST request to save Raunkier : {}", raunkier);
        if (raunkier.getId() != null) {
            throw new BadRequestAlertException("A new raunkier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return raunkierService
            .save(raunkier)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/raunkiers/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /raunkiers/:id} : Updates an existing raunkier.
     *
     * @param id the id of the raunkier to save.
     * @param raunkier the raunkier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated raunkier,
     * or with status {@code 400 (Bad Request)} if the raunkier is not valid,
     * or with status {@code 500 (Internal Server Error)} if the raunkier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/raunkiers/{id}")
    public Mono<ResponseEntity<Raunkier>> updateRaunkier(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Raunkier raunkier
    ) throws URISyntaxException {
        log.debug("REST request to update Raunkier : {}, {}", id, raunkier);
        if (raunkier.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, raunkier.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return raunkierRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return raunkierService
                    .save(raunkier)
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
     * {@code PATCH  /raunkiers/:id} : Partial updates given fields of an existing raunkier, field will ignore if it is null
     *
     * @param id the id of the raunkier to save.
     * @param raunkier the raunkier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated raunkier,
     * or with status {@code 400 (Bad Request)} if the raunkier is not valid,
     * or with status {@code 404 (Not Found)} if the raunkier is not found,
     * or with status {@code 500 (Internal Server Error)} if the raunkier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/raunkiers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Raunkier>> partialUpdateRaunkier(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Raunkier raunkier
    ) throws URISyntaxException {
        log.debug("REST request to partial update Raunkier partially : {}, {}", id, raunkier);
        if (raunkier.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, raunkier.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return raunkierRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Raunkier> result = raunkierService.partialUpdate(raunkier);

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
     * {@code GET  /raunkiers} : get all the raunkiers.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of raunkiers in body.
     */
    @GetMapping("/raunkiers")
    public Mono<ResponseEntity<List<Raunkier>>> getAllRaunkiers(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of Raunkiers");
        return raunkierService
            .countAll()
            .zipWith(raunkierService.findAll(pageable).collectList())
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
     * {@code GET  /raunkiers/:id} : get the "id" raunkier.
     *
     * @param id the id of the raunkier to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the raunkier, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/raunkiers/{id}")
    public Mono<ResponseEntity<Raunkier>> getRaunkier(@PathVariable Long id) {
        log.debug("REST request to get Raunkier : {}", id);
        Mono<Raunkier> raunkier = raunkierService.findOne(id);
        return ResponseUtil.wrapOrNotFound(raunkier);
    }

    /**
     * {@code DELETE  /raunkiers/:id} : delete the "id" raunkier.
     *
     * @param id the id of the raunkier to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/raunkiers/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteRaunkier(@PathVariable Long id) {
        log.debug("REST request to delete Raunkier : {}", id);
        return raunkierService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
