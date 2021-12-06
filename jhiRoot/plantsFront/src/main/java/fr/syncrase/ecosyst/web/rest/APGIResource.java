package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.APGI;
import fr.syncrase.ecosyst.repository.APGIRepository;
import fr.syncrase.ecosyst.service.APGIService;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.APGI}.
 */
@RestController
@RequestMapping("/api")
public class APGIResource {

    private final Logger log = LoggerFactory.getLogger(APGIResource.class);

    private static final String ENTITY_NAME = "aPGI";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final APGIService aPGIService;

    private final APGIRepository aPGIRepository;

    public APGIResource(APGIService aPGIService, APGIRepository aPGIRepository) {
        this.aPGIService = aPGIService;
        this.aPGIRepository = aPGIRepository;
    }

    /**
     * {@code POST  /apgis} : Create a new aPGI.
     *
     * @param aPGI the aPGI to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aPGI, or with status {@code 400 (Bad Request)} if the aPGI has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/apgis")
    public Mono<ResponseEntity<APGI>> createAPGI(@Valid @RequestBody APGI aPGI) throws URISyntaxException {
        log.debug("REST request to save APGI : {}", aPGI);
        if (aPGI.getId() != null) {
            throw new BadRequestAlertException("A new aPGI cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return aPGIService
            .save(aPGI)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/apgis/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /apgis/:id} : Updates an existing aPGI.
     *
     * @param id the id of the aPGI to save.
     * @param aPGI the aPGI to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aPGI,
     * or with status {@code 400 (Bad Request)} if the aPGI is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aPGI couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/apgis/{id}")
    public Mono<ResponseEntity<APGI>> updateAPGI(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody APGI aPGI
    ) throws URISyntaxException {
        log.debug("REST request to update APGI : {}, {}", id, aPGI);
        if (aPGI.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aPGI.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return aPGIRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return aPGIService
                    .save(aPGI)
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
     * {@code PATCH  /apgis/:id} : Partial updates given fields of an existing aPGI, field will ignore if it is null
     *
     * @param id the id of the aPGI to save.
     * @param aPGI the aPGI to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aPGI,
     * or with status {@code 400 (Bad Request)} if the aPGI is not valid,
     * or with status {@code 404 (Not Found)} if the aPGI is not found,
     * or with status {@code 500 (Internal Server Error)} if the aPGI couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/apgis/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<APGI>> partialUpdateAPGI(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody APGI aPGI
    ) throws URISyntaxException {
        log.debug("REST request to partial update APGI partially : {}, {}", id, aPGI);
        if (aPGI.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aPGI.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return aPGIRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<APGI> result = aPGIService.partialUpdate(aPGI);

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
     * {@code GET  /apgis} : get all the aPGIS.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aPGIS in body.
     */
    @GetMapping("/apgis")
    public Mono<ResponseEntity<List<APGI>>> getAllAPGIS(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of APGIS");
        return aPGIService
            .countAll()
            .zipWith(aPGIService.findAll(pageable).collectList())
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
     * {@code GET  /apgis/:id} : get the "id" aPGI.
     *
     * @param id the id of the aPGI to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aPGI, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/apgis/{id}")
    public Mono<ResponseEntity<APGI>> getAPGI(@PathVariable Long id) {
        log.debug("REST request to get APGI : {}", id);
        Mono<APGI> aPGI = aPGIService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aPGI);
    }

    /**
     * {@code DELETE  /apgis/:id} : delete the "id" aPGI.
     *
     * @param id the id of the aPGI to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/apgis/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteAPGI(@PathVariable Long id) {
        log.debug("REST request to delete APGI : {}", id);
        return aPGIService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
