package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.APGII;
import fr.syncrase.ecosyst.repository.APGIIRepository;
import fr.syncrase.ecosyst.service.APGIIService;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.APGII}.
 */
@RestController
@RequestMapping("/api")
public class APGIIResource {

    private final Logger log = LoggerFactory.getLogger(APGIIResource.class);

    private static final String ENTITY_NAME = "aPGII";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final APGIIService aPGIIService;

    private final APGIIRepository aPGIIRepository;

    public APGIIResource(APGIIService aPGIIService, APGIIRepository aPGIIRepository) {
        this.aPGIIService = aPGIIService;
        this.aPGIIRepository = aPGIIRepository;
    }

    /**
     * {@code POST  /apgiis} : Create a new aPGII.
     *
     * @param aPGII the aPGII to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aPGII, or with status {@code 400 (Bad Request)} if the aPGII has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/apgiis")
    public Mono<ResponseEntity<APGII>> createAPGII(@Valid @RequestBody APGII aPGII) throws URISyntaxException {
        log.debug("REST request to save APGII : {}", aPGII);
        if (aPGII.getId() != null) {
            throw new BadRequestAlertException("A new aPGII cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return aPGIIService
            .save(aPGII)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/apgiis/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /apgiis/:id} : Updates an existing aPGII.
     *
     * @param id the id of the aPGII to save.
     * @param aPGII the aPGII to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aPGII,
     * or with status {@code 400 (Bad Request)} if the aPGII is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aPGII couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/apgiis/{id}")
    public Mono<ResponseEntity<APGII>> updateAPGII(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody APGII aPGII
    ) throws URISyntaxException {
        log.debug("REST request to update APGII : {}, {}", id, aPGII);
        if (aPGII.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aPGII.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return aPGIIRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return aPGIIService
                    .save(aPGII)
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
     * {@code PATCH  /apgiis/:id} : Partial updates given fields of an existing aPGII, field will ignore if it is null
     *
     * @param id the id of the aPGII to save.
     * @param aPGII the aPGII to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aPGII,
     * or with status {@code 400 (Bad Request)} if the aPGII is not valid,
     * or with status {@code 404 (Not Found)} if the aPGII is not found,
     * or with status {@code 500 (Internal Server Error)} if the aPGII couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/apgiis/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<APGII>> partialUpdateAPGII(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody APGII aPGII
    ) throws URISyntaxException {
        log.debug("REST request to partial update APGII partially : {}, {}", id, aPGII);
        if (aPGII.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aPGII.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return aPGIIRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<APGII> result = aPGIIService.partialUpdate(aPGII);

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
     * {@code GET  /apgiis} : get all the aPGIIS.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aPGIIS in body.
     */
    @GetMapping("/apgiis")
    public Mono<ResponseEntity<List<APGII>>> getAllAPGIIS(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of APGIIS");
        return aPGIIService
            .countAll()
            .zipWith(aPGIIService.findAll(pageable).collectList())
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
     * {@code GET  /apgiis/:id} : get the "id" aPGII.
     *
     * @param id the id of the aPGII to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aPGII, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/apgiis/{id}")
    public Mono<ResponseEntity<APGII>> getAPGII(@PathVariable Long id) {
        log.debug("REST request to get APGII : {}", id);
        Mono<APGII> aPGII = aPGIIService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aPGII);
    }

    /**
     * {@code DELETE  /apgiis/:id} : delete the "id" aPGII.
     *
     * @param id the id of the aPGII to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/apgiis/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteAPGII(@PathVariable Long id) {
        log.debug("REST request to delete APGII : {}", id);
        return aPGIIService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
