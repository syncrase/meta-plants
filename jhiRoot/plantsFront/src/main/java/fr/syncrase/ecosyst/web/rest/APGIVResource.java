package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.APGIV;
import fr.syncrase.ecosyst.repository.APGIVRepository;
import fr.syncrase.ecosyst.service.APGIVService;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.APGIV}.
 */
@RestController
@RequestMapping("/api")
public class APGIVResource {

    private final Logger log = LoggerFactory.getLogger(APGIVResource.class);

    private static final String ENTITY_NAME = "aPGIV";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final APGIVService aPGIVService;

    private final APGIVRepository aPGIVRepository;

    public APGIVResource(APGIVService aPGIVService, APGIVRepository aPGIVRepository) {
        this.aPGIVService = aPGIVService;
        this.aPGIVRepository = aPGIVRepository;
    }

    /**
     * {@code POST  /apgivs} : Create a new aPGIV.
     *
     * @param aPGIV the aPGIV to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aPGIV, or with status {@code 400 (Bad Request)} if the aPGIV has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/apgivs")
    public Mono<ResponseEntity<APGIV>> createAPGIV(@Valid @RequestBody APGIV aPGIV) throws URISyntaxException {
        log.debug("REST request to save APGIV : {}", aPGIV);
        if (aPGIV.getId() != null) {
            throw new BadRequestAlertException("A new aPGIV cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return aPGIVService
            .save(aPGIV)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/apgivs/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /apgivs/:id} : Updates an existing aPGIV.
     *
     * @param id the id of the aPGIV to save.
     * @param aPGIV the aPGIV to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aPGIV,
     * or with status {@code 400 (Bad Request)} if the aPGIV is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aPGIV couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/apgivs/{id}")
    public Mono<ResponseEntity<APGIV>> updateAPGIV(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody APGIV aPGIV
    ) throws URISyntaxException {
        log.debug("REST request to update APGIV : {}, {}", id, aPGIV);
        if (aPGIV.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aPGIV.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return aPGIVRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return aPGIVService
                    .save(aPGIV)
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
     * {@code PATCH  /apgivs/:id} : Partial updates given fields of an existing aPGIV, field will ignore if it is null
     *
     * @param id the id of the aPGIV to save.
     * @param aPGIV the aPGIV to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aPGIV,
     * or with status {@code 400 (Bad Request)} if the aPGIV is not valid,
     * or with status {@code 404 (Not Found)} if the aPGIV is not found,
     * or with status {@code 500 (Internal Server Error)} if the aPGIV couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/apgivs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<APGIV>> partialUpdateAPGIV(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody APGIV aPGIV
    ) throws URISyntaxException {
        log.debug("REST request to partial update APGIV partially : {}, {}", id, aPGIV);
        if (aPGIV.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aPGIV.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return aPGIVRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<APGIV> result = aPGIVService.partialUpdate(aPGIV);

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
     * {@code GET  /apgivs} : get all the aPGIVS.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aPGIVS in body.
     */
    @GetMapping("/apgivs")
    public Mono<ResponseEntity<List<APGIV>>> getAllAPGIVS(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of APGIVS");
        return aPGIVService
            .countAll()
            .zipWith(aPGIVService.findAll(pageable).collectList())
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
     * {@code GET  /apgivs/:id} : get the "id" aPGIV.
     *
     * @param id the id of the aPGIV to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aPGIV, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/apgivs/{id}")
    public Mono<ResponseEntity<APGIV>> getAPGIV(@PathVariable Long id) {
        log.debug("REST request to get APGIV : {}", id);
        Mono<APGIV> aPGIV = aPGIVService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aPGIV);
    }

    /**
     * {@code DELETE  /apgivs/:id} : delete the "id" aPGIV.
     *
     * @param id the id of the aPGIV to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/apgivs/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteAPGIV(@PathVariable Long id) {
        log.debug("REST request to delete APGIV : {}", id);
        return aPGIVService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
