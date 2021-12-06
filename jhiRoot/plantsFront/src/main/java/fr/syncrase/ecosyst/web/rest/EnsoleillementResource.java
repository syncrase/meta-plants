package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.Ensoleillement;
import fr.syncrase.ecosyst.repository.EnsoleillementRepository;
import fr.syncrase.ecosyst.service.EnsoleillementService;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.Ensoleillement}.
 */
@RestController
@RequestMapping("/api")
public class EnsoleillementResource {

    private final Logger log = LoggerFactory.getLogger(EnsoleillementResource.class);

    private static final String ENTITY_NAME = "ensoleillement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EnsoleillementService ensoleillementService;

    private final EnsoleillementRepository ensoleillementRepository;

    public EnsoleillementResource(EnsoleillementService ensoleillementService, EnsoleillementRepository ensoleillementRepository) {
        this.ensoleillementService = ensoleillementService;
        this.ensoleillementRepository = ensoleillementRepository;
    }

    /**
     * {@code POST  /ensoleillements} : Create a new ensoleillement.
     *
     * @param ensoleillement the ensoleillement to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ensoleillement, or with status {@code 400 (Bad Request)} if the ensoleillement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ensoleillements")
    public Mono<ResponseEntity<Ensoleillement>> createEnsoleillement(@RequestBody Ensoleillement ensoleillement) throws URISyntaxException {
        log.debug("REST request to save Ensoleillement : {}", ensoleillement);
        if (ensoleillement.getId() != null) {
            throw new BadRequestAlertException("A new ensoleillement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return ensoleillementService
            .save(ensoleillement)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/ensoleillements/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /ensoleillements/:id} : Updates an existing ensoleillement.
     *
     * @param id the id of the ensoleillement to save.
     * @param ensoleillement the ensoleillement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ensoleillement,
     * or with status {@code 400 (Bad Request)} if the ensoleillement is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ensoleillement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ensoleillements/{id}")
    public Mono<ResponseEntity<Ensoleillement>> updateEnsoleillement(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Ensoleillement ensoleillement
    ) throws URISyntaxException {
        log.debug("REST request to update Ensoleillement : {}, {}", id, ensoleillement);
        if (ensoleillement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ensoleillement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return ensoleillementRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return ensoleillementService
                    .save(ensoleillement)
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
     * {@code PATCH  /ensoleillements/:id} : Partial updates given fields of an existing ensoleillement, field will ignore if it is null
     *
     * @param id the id of the ensoleillement to save.
     * @param ensoleillement the ensoleillement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ensoleillement,
     * or with status {@code 400 (Bad Request)} if the ensoleillement is not valid,
     * or with status {@code 404 (Not Found)} if the ensoleillement is not found,
     * or with status {@code 500 (Internal Server Error)} if the ensoleillement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ensoleillements/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Ensoleillement>> partialUpdateEnsoleillement(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Ensoleillement ensoleillement
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ensoleillement partially : {}, {}", id, ensoleillement);
        if (ensoleillement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ensoleillement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return ensoleillementRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Ensoleillement> result = ensoleillementService.partialUpdate(ensoleillement);

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
     * {@code GET  /ensoleillements} : get all the ensoleillements.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ensoleillements in body.
     */
    @GetMapping("/ensoleillements")
    public Mono<ResponseEntity<List<Ensoleillement>>> getAllEnsoleillements(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of Ensoleillements");
        return ensoleillementService
            .countAll()
            .zipWith(ensoleillementService.findAll(pageable).collectList())
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
     * {@code GET  /ensoleillements/:id} : get the "id" ensoleillement.
     *
     * @param id the id of the ensoleillement to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ensoleillement, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ensoleillements/{id}")
    public Mono<ResponseEntity<Ensoleillement>> getEnsoleillement(@PathVariable Long id) {
        log.debug("REST request to get Ensoleillement : {}", id);
        Mono<Ensoleillement> ensoleillement = ensoleillementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ensoleillement);
    }

    /**
     * {@code DELETE  /ensoleillements/:id} : delete the "id" ensoleillement.
     *
     * @param id the id of the ensoleillement to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ensoleillements/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteEnsoleillement(@PathVariable Long id) {
        log.debug("REST request to delete Ensoleillement : {}", id);
        return ensoleillementService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
