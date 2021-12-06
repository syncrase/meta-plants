package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.CycleDeVie;
import fr.syncrase.ecosyst.repository.CycleDeVieRepository;
import fr.syncrase.ecosyst.service.CycleDeVieService;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.CycleDeVie}.
 */
@RestController
@RequestMapping("/api")
public class CycleDeVieResource {

    private final Logger log = LoggerFactory.getLogger(CycleDeVieResource.class);

    private static final String ENTITY_NAME = "cycleDeVie";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CycleDeVieService cycleDeVieService;

    private final CycleDeVieRepository cycleDeVieRepository;

    public CycleDeVieResource(CycleDeVieService cycleDeVieService, CycleDeVieRepository cycleDeVieRepository) {
        this.cycleDeVieService = cycleDeVieService;
        this.cycleDeVieRepository = cycleDeVieRepository;
    }

    /**
     * {@code POST  /cycle-de-vies} : Create a new cycleDeVie.
     *
     * @param cycleDeVie the cycleDeVie to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cycleDeVie, or with status {@code 400 (Bad Request)} if the cycleDeVie has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cycle-de-vies")
    public Mono<ResponseEntity<CycleDeVie>> createCycleDeVie(@RequestBody CycleDeVie cycleDeVie) throws URISyntaxException {
        log.debug("REST request to save CycleDeVie : {}", cycleDeVie);
        if (cycleDeVie.getId() != null) {
            throw new BadRequestAlertException("A new cycleDeVie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return cycleDeVieService
            .save(cycleDeVie)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/cycle-de-vies/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /cycle-de-vies/:id} : Updates an existing cycleDeVie.
     *
     * @param id the id of the cycleDeVie to save.
     * @param cycleDeVie the cycleDeVie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cycleDeVie,
     * or with status {@code 400 (Bad Request)} if the cycleDeVie is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cycleDeVie couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cycle-de-vies/{id}")
    public Mono<ResponseEntity<CycleDeVie>> updateCycleDeVie(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CycleDeVie cycleDeVie
    ) throws URISyntaxException {
        log.debug("REST request to update CycleDeVie : {}, {}", id, cycleDeVie);
        if (cycleDeVie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cycleDeVie.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return cycleDeVieRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return cycleDeVieService
                    .save(cycleDeVie)
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
     * {@code PATCH  /cycle-de-vies/:id} : Partial updates given fields of an existing cycleDeVie, field will ignore if it is null
     *
     * @param id the id of the cycleDeVie to save.
     * @param cycleDeVie the cycleDeVie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cycleDeVie,
     * or with status {@code 400 (Bad Request)} if the cycleDeVie is not valid,
     * or with status {@code 404 (Not Found)} if the cycleDeVie is not found,
     * or with status {@code 500 (Internal Server Error)} if the cycleDeVie couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cycle-de-vies/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<CycleDeVie>> partialUpdateCycleDeVie(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CycleDeVie cycleDeVie
    ) throws URISyntaxException {
        log.debug("REST request to partial update CycleDeVie partially : {}, {}", id, cycleDeVie);
        if (cycleDeVie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cycleDeVie.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return cycleDeVieRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<CycleDeVie> result = cycleDeVieService.partialUpdate(cycleDeVie);

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
     * {@code GET  /cycle-de-vies} : get all the cycleDeVies.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cycleDeVies in body.
     */
    @GetMapping("/cycle-de-vies")
    public Mono<ResponseEntity<List<CycleDeVie>>> getAllCycleDeVies(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of CycleDeVies");
        return cycleDeVieService
            .countAll()
            .zipWith(cycleDeVieService.findAll(pageable).collectList())
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
     * {@code GET  /cycle-de-vies/:id} : get the "id" cycleDeVie.
     *
     * @param id the id of the cycleDeVie to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cycleDeVie, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cycle-de-vies/{id}")
    public Mono<ResponseEntity<CycleDeVie>> getCycleDeVie(@PathVariable Long id) {
        log.debug("REST request to get CycleDeVie : {}", id);
        Mono<CycleDeVie> cycleDeVie = cycleDeVieService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cycleDeVie);
    }

    /**
     * {@code DELETE  /cycle-de-vies/:id} : delete the "id" cycleDeVie.
     *
     * @param id the id of the cycleDeVie to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cycle-de-vies/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteCycleDeVie(@PathVariable Long id) {
        log.debug("REST request to delete CycleDeVie : {}", id);
        return cycleDeVieService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
