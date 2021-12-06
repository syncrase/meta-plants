package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.Feuillage;
import fr.syncrase.ecosyst.repository.FeuillageRepository;
import fr.syncrase.ecosyst.service.FeuillageService;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.Feuillage}.
 */
@RestController
@RequestMapping("/api")
public class FeuillageResource {

    private final Logger log = LoggerFactory.getLogger(FeuillageResource.class);

    private static final String ENTITY_NAME = "feuillage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FeuillageService feuillageService;

    private final FeuillageRepository feuillageRepository;

    public FeuillageResource(FeuillageService feuillageService, FeuillageRepository feuillageRepository) {
        this.feuillageService = feuillageService;
        this.feuillageRepository = feuillageRepository;
    }

    /**
     * {@code POST  /feuillages} : Create a new feuillage.
     *
     * @param feuillage the feuillage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new feuillage, or with status {@code 400 (Bad Request)} if the feuillage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/feuillages")
    public Mono<ResponseEntity<Feuillage>> createFeuillage(@RequestBody Feuillage feuillage) throws URISyntaxException {
        log.debug("REST request to save Feuillage : {}", feuillage);
        if (feuillage.getId() != null) {
            throw new BadRequestAlertException("A new feuillage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return feuillageService
            .save(feuillage)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/feuillages/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /feuillages/:id} : Updates an existing feuillage.
     *
     * @param id the id of the feuillage to save.
     * @param feuillage the feuillage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feuillage,
     * or with status {@code 400 (Bad Request)} if the feuillage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the feuillage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/feuillages/{id}")
    public Mono<ResponseEntity<Feuillage>> updateFeuillage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Feuillage feuillage
    ) throws URISyntaxException {
        log.debug("REST request to update Feuillage : {}, {}", id, feuillage);
        if (feuillage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feuillage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return feuillageRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return feuillageService
                    .save(feuillage)
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
     * {@code PATCH  /feuillages/:id} : Partial updates given fields of an existing feuillage, field will ignore if it is null
     *
     * @param id the id of the feuillage to save.
     * @param feuillage the feuillage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feuillage,
     * or with status {@code 400 (Bad Request)} if the feuillage is not valid,
     * or with status {@code 404 (Not Found)} if the feuillage is not found,
     * or with status {@code 500 (Internal Server Error)} if the feuillage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/feuillages/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Feuillage>> partialUpdateFeuillage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Feuillage feuillage
    ) throws URISyntaxException {
        log.debug("REST request to partial update Feuillage partially : {}, {}", id, feuillage);
        if (feuillage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feuillage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return feuillageRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Feuillage> result = feuillageService.partialUpdate(feuillage);

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
     * {@code GET  /feuillages} : get all the feuillages.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of feuillages in body.
     */
    @GetMapping("/feuillages")
    public Mono<ResponseEntity<List<Feuillage>>> getAllFeuillages(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of Feuillages");
        return feuillageService
            .countAll()
            .zipWith(feuillageService.findAll(pageable).collectList())
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
     * {@code GET  /feuillages/:id} : get the "id" feuillage.
     *
     * @param id the id of the feuillage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the feuillage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/feuillages/{id}")
    public Mono<ResponseEntity<Feuillage>> getFeuillage(@PathVariable Long id) {
        log.debug("REST request to get Feuillage : {}", id);
        Mono<Feuillage> feuillage = feuillageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(feuillage);
    }

    /**
     * {@code DELETE  /feuillages/:id} : delete the "id" feuillage.
     *
     * @param id the id of the feuillage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/feuillages/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteFeuillage(@PathVariable Long id) {
        log.debug("REST request to delete Feuillage : {}", id);
        return feuillageService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
