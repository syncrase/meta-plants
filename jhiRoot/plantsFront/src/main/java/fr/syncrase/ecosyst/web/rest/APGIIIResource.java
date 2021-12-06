package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.APGIII;
import fr.syncrase.ecosyst.repository.APGIIIRepository;
import fr.syncrase.ecosyst.service.APGIIIService;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.APGIII}.
 */
@RestController
@RequestMapping("/api")
public class APGIIIResource {

    private final Logger log = LoggerFactory.getLogger(APGIIIResource.class);

    private static final String ENTITY_NAME = "aPGIII";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final APGIIIService aPGIIIService;

    private final APGIIIRepository aPGIIIRepository;

    public APGIIIResource(APGIIIService aPGIIIService, APGIIIRepository aPGIIIRepository) {
        this.aPGIIIService = aPGIIIService;
        this.aPGIIIRepository = aPGIIIRepository;
    }

    /**
     * {@code POST  /apgiiis} : Create a new aPGIII.
     *
     * @param aPGIII the aPGIII to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aPGIII, or with status {@code 400 (Bad Request)} if the aPGIII has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/apgiiis")
    public Mono<ResponseEntity<APGIII>> createAPGIII(@Valid @RequestBody APGIII aPGIII) throws URISyntaxException {
        log.debug("REST request to save APGIII : {}", aPGIII);
        if (aPGIII.getId() != null) {
            throw new BadRequestAlertException("A new aPGIII cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return aPGIIIService
            .save(aPGIII)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/apgiiis/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /apgiiis/:id} : Updates an existing aPGIII.
     *
     * @param id the id of the aPGIII to save.
     * @param aPGIII the aPGIII to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aPGIII,
     * or with status {@code 400 (Bad Request)} if the aPGIII is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aPGIII couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/apgiiis/{id}")
    public Mono<ResponseEntity<APGIII>> updateAPGIII(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody APGIII aPGIII
    ) throws URISyntaxException {
        log.debug("REST request to update APGIII : {}, {}", id, aPGIII);
        if (aPGIII.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aPGIII.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return aPGIIIRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return aPGIIIService
                    .save(aPGIII)
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
     * {@code PATCH  /apgiiis/:id} : Partial updates given fields of an existing aPGIII, field will ignore if it is null
     *
     * @param id the id of the aPGIII to save.
     * @param aPGIII the aPGIII to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aPGIII,
     * or with status {@code 400 (Bad Request)} if the aPGIII is not valid,
     * or with status {@code 404 (Not Found)} if the aPGIII is not found,
     * or with status {@code 500 (Internal Server Error)} if the aPGIII couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/apgiiis/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<APGIII>> partialUpdateAPGIII(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody APGIII aPGIII
    ) throws URISyntaxException {
        log.debug("REST request to partial update APGIII partially : {}, {}", id, aPGIII);
        if (aPGIII.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aPGIII.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return aPGIIIRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<APGIII> result = aPGIIIService.partialUpdate(aPGIII);

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
     * {@code GET  /apgiiis} : get all the aPGIIIS.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aPGIIIS in body.
     */
    @GetMapping("/apgiiis")
    public Mono<ResponseEntity<List<APGIII>>> getAllAPGIIIS(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of APGIIIS");
        return aPGIIIService
            .countAll()
            .zipWith(aPGIIIService.findAll(pageable).collectList())
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
     * {@code GET  /apgiiis/:id} : get the "id" aPGIII.
     *
     * @param id the id of the aPGIII to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aPGIII, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/apgiiis/{id}")
    public Mono<ResponseEntity<APGIII>> getAPGIII(@PathVariable Long id) {
        log.debug("REST request to get APGIII : {}", id);
        Mono<APGIII> aPGIII = aPGIIIService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aPGIII);
    }

    /**
     * {@code DELETE  /apgiiis/:id} : delete the "id" aPGIII.
     *
     * @param id the id of the aPGIII to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/apgiiis/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteAPGIII(@PathVariable Long id) {
        log.debug("REST request to delete APGIII : {}", id);
        return aPGIIIService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
