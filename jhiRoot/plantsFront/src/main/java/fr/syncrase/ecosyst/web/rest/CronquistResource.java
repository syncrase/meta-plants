package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.Cronquist;
import fr.syncrase.ecosyst.repository.CronquistRepository;
import fr.syncrase.ecosyst.service.CronquistService;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.Cronquist}.
 */
@RestController
@RequestMapping("/api")
public class CronquistResource {

    private final Logger log = LoggerFactory.getLogger(CronquistResource.class);

    private static final String ENTITY_NAME = "cronquist";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CronquistService cronquistService;

    private final CronquistRepository cronquistRepository;

    public CronquistResource(CronquistService cronquistService, CronquistRepository cronquistRepository) {
        this.cronquistService = cronquistService;
        this.cronquistRepository = cronquistRepository;
    }

    /**
     * {@code POST  /cronquists} : Create a new cronquist.
     *
     * @param cronquist the cronquist to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cronquist, or with status {@code 400 (Bad Request)} if the cronquist has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cronquists")
    public Mono<ResponseEntity<Cronquist>> createCronquist(@RequestBody Cronquist cronquist) throws URISyntaxException {
        log.debug("REST request to save Cronquist : {}", cronquist);
        if (cronquist.getId() != null) {
            throw new BadRequestAlertException("A new cronquist cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return cronquistService
            .save(cronquist)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/cronquists/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /cronquists/:id} : Updates an existing cronquist.
     *
     * @param id the id of the cronquist to save.
     * @param cronquist the cronquist to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cronquist,
     * or with status {@code 400 (Bad Request)} if the cronquist is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cronquist couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cronquists/{id}")
    public Mono<ResponseEntity<Cronquist>> updateCronquist(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Cronquist cronquist
    ) throws URISyntaxException {
        log.debug("REST request to update Cronquist : {}, {}", id, cronquist);
        if (cronquist.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cronquist.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return cronquistRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return cronquistService
                    .save(cronquist)
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
     * {@code PATCH  /cronquists/:id} : Partial updates given fields of an existing cronquist, field will ignore if it is null
     *
     * @param id the id of the cronquist to save.
     * @param cronquist the cronquist to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cronquist,
     * or with status {@code 400 (Bad Request)} if the cronquist is not valid,
     * or with status {@code 404 (Not Found)} if the cronquist is not found,
     * or with status {@code 500 (Internal Server Error)} if the cronquist couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cronquists/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Cronquist>> partialUpdateCronquist(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Cronquist cronquist
    ) throws URISyntaxException {
        log.debug("REST request to partial update Cronquist partially : {}, {}", id, cronquist);
        if (cronquist.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cronquist.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return cronquistRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Cronquist> result = cronquistService.partialUpdate(cronquist);

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
     * {@code GET  /cronquists} : get all the cronquists.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cronquists in body.
     */
    @GetMapping("/cronquists")
    public Mono<ResponseEntity<List<Cronquist>>> getAllCronquists(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of Cronquists");
        return cronquistService
            .countAll()
            .zipWith(cronquistService.findAll(pageable).collectList())
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
     * {@code GET  /cronquists/:id} : get the "id" cronquist.
     *
     * @param id the id of the cronquist to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cronquist, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cronquists/{id}")
    public Mono<ResponseEntity<Cronquist>> getCronquist(@PathVariable Long id) {
        log.debug("REST request to get Cronquist : {}", id);
        Mono<Cronquist> cronquist = cronquistService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cronquist);
    }

    /**
     * {@code DELETE  /cronquists/:id} : delete the "id" cronquist.
     *
     * @param id the id of the cronquist to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cronquists/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteCronquist(@PathVariable Long id) {
        log.debug("REST request to delete Cronquist : {}", id);
        return cronquistService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
