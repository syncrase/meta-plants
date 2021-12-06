package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.Strate;
import fr.syncrase.ecosyst.repository.StrateRepository;
import fr.syncrase.ecosyst.service.StrateService;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.Strate}.
 */
@RestController
@RequestMapping("/api")
public class StrateResource {

    private final Logger log = LoggerFactory.getLogger(StrateResource.class);

    private static final String ENTITY_NAME = "strate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StrateService strateService;

    private final StrateRepository strateRepository;

    public StrateResource(StrateService strateService, StrateRepository strateRepository) {
        this.strateService = strateService;
        this.strateRepository = strateRepository;
    }

    /**
     * {@code POST  /strates} : Create a new strate.
     *
     * @param strate the strate to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new strate, or with status {@code 400 (Bad Request)} if the strate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/strates")
    public Mono<ResponseEntity<Strate>> createStrate(@RequestBody Strate strate) throws URISyntaxException {
        log.debug("REST request to save Strate : {}", strate);
        if (strate.getId() != null) {
            throw new BadRequestAlertException("A new strate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return strateService
            .save(strate)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/strates/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /strates/:id} : Updates an existing strate.
     *
     * @param id the id of the strate to save.
     * @param strate the strate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated strate,
     * or with status {@code 400 (Bad Request)} if the strate is not valid,
     * or with status {@code 500 (Internal Server Error)} if the strate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/strates/{id}")
    public Mono<ResponseEntity<Strate>> updateStrate(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Strate strate
    ) throws URISyntaxException {
        log.debug("REST request to update Strate : {}, {}", id, strate);
        if (strate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, strate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return strateRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return strateService
                    .save(strate)
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
     * {@code PATCH  /strates/:id} : Partial updates given fields of an existing strate, field will ignore if it is null
     *
     * @param id the id of the strate to save.
     * @param strate the strate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated strate,
     * or with status {@code 400 (Bad Request)} if the strate is not valid,
     * or with status {@code 404 (Not Found)} if the strate is not found,
     * or with status {@code 500 (Internal Server Error)} if the strate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/strates/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Strate>> partialUpdateStrate(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Strate strate
    ) throws URISyntaxException {
        log.debug("REST request to partial update Strate partially : {}, {}", id, strate);
        if (strate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, strate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return strateRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Strate> result = strateService.partialUpdate(strate);

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
     * {@code GET  /strates} : get all the strates.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of strates in body.
     */
    @GetMapping("/strates")
    public Mono<ResponseEntity<List<Strate>>> getAllStrates(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of Strates");
        return strateService
            .countAll()
            .zipWith(strateService.findAll(pageable).collectList())
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
     * {@code GET  /strates/:id} : get the "id" strate.
     *
     * @param id the id of the strate to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the strate, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/strates/{id}")
    public Mono<ResponseEntity<Strate>> getStrate(@PathVariable Long id) {
        log.debug("REST request to get Strate : {}", id);
        Mono<Strate> strate = strateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(strate);
    }

    /**
     * {@code DELETE  /strates/:id} : delete the "id" strate.
     *
     * @param id the id of the strate to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/strates/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteStrate(@PathVariable Long id) {
        log.debug("REST request to delete Strate : {}", id);
        return strateService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
