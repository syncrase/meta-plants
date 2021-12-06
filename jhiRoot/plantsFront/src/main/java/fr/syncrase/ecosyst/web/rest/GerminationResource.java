package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.Germination;
import fr.syncrase.ecosyst.repository.GerminationRepository;
import fr.syncrase.ecosyst.service.GerminationService;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.Germination}.
 */
@RestController
@RequestMapping("/api")
public class GerminationResource {

    private final Logger log = LoggerFactory.getLogger(GerminationResource.class);

    private static final String ENTITY_NAME = "germination";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GerminationService germinationService;

    private final GerminationRepository germinationRepository;

    public GerminationResource(GerminationService germinationService, GerminationRepository germinationRepository) {
        this.germinationService = germinationService;
        this.germinationRepository = germinationRepository;
    }

    /**
     * {@code POST  /germinations} : Create a new germination.
     *
     * @param germination the germination to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new germination, or with status {@code 400 (Bad Request)} if the germination has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/germinations")
    public Mono<ResponseEntity<Germination>> createGermination(@RequestBody Germination germination) throws URISyntaxException {
        log.debug("REST request to save Germination : {}", germination);
        if (germination.getId() != null) {
            throw new BadRequestAlertException("A new germination cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return germinationService
            .save(germination)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/germinations/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /germinations/:id} : Updates an existing germination.
     *
     * @param id the id of the germination to save.
     * @param germination the germination to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated germination,
     * or with status {@code 400 (Bad Request)} if the germination is not valid,
     * or with status {@code 500 (Internal Server Error)} if the germination couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/germinations/{id}")
    public Mono<ResponseEntity<Germination>> updateGermination(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Germination germination
    ) throws URISyntaxException {
        log.debug("REST request to update Germination : {}, {}", id, germination);
        if (germination.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, germination.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return germinationRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return germinationService
                    .save(germination)
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
     * {@code PATCH  /germinations/:id} : Partial updates given fields of an existing germination, field will ignore if it is null
     *
     * @param id the id of the germination to save.
     * @param germination the germination to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated germination,
     * or with status {@code 400 (Bad Request)} if the germination is not valid,
     * or with status {@code 404 (Not Found)} if the germination is not found,
     * or with status {@code 500 (Internal Server Error)} if the germination couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/germinations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Germination>> partialUpdateGermination(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Germination germination
    ) throws URISyntaxException {
        log.debug("REST request to partial update Germination partially : {}, {}", id, germination);
        if (germination.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, germination.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return germinationRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Germination> result = germinationService.partialUpdate(germination);

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
     * {@code GET  /germinations} : get all the germinations.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of germinations in body.
     */
    @GetMapping("/germinations")
    public Mono<ResponseEntity<List<Germination>>> getAllGerminations(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of Germinations");
        return germinationService
            .countAll()
            .zipWith(germinationService.findAll(pageable).collectList())
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
     * {@code GET  /germinations/:id} : get the "id" germination.
     *
     * @param id the id of the germination to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the germination, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/germinations/{id}")
    public Mono<ResponseEntity<Germination>> getGermination(@PathVariable Long id) {
        log.debug("REST request to get Germination : {}", id);
        Mono<Germination> germination = germinationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(germination);
    }

    /**
     * {@code DELETE  /germinations/:id} : delete the "id" germination.
     *
     * @param id the id of the germination to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/germinations/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteGermination(@PathVariable Long id) {
        log.debug("REST request to delete Germination : {}", id);
        return germinationService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
