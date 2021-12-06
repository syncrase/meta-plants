package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.Mois;
import fr.syncrase.ecosyst.repository.MoisRepository;
import fr.syncrase.ecosyst.service.MoisService;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.Mois}.
 */
@RestController
@RequestMapping("/api")
public class MoisResource {

    private final Logger log = LoggerFactory.getLogger(MoisResource.class);

    private static final String ENTITY_NAME = "mois";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MoisService moisService;

    private final MoisRepository moisRepository;

    public MoisResource(MoisService moisService, MoisRepository moisRepository) {
        this.moisService = moisService;
        this.moisRepository = moisRepository;
    }

    /**
     * {@code POST  /mois} : Create a new mois.
     *
     * @param mois the mois to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mois, or with status {@code 400 (Bad Request)} if the mois has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mois")
    public Mono<ResponseEntity<Mois>> createMois(@Valid @RequestBody Mois mois) throws URISyntaxException {
        log.debug("REST request to save Mois : {}", mois);
        if (mois.getId() != null) {
            throw new BadRequestAlertException("A new mois cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return moisService
            .save(mois)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/mois/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /mois/:id} : Updates an existing mois.
     *
     * @param id the id of the mois to save.
     * @param mois the mois to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mois,
     * or with status {@code 400 (Bad Request)} if the mois is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mois couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mois/{id}")
    public Mono<ResponseEntity<Mois>> updateMois(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Mois mois
    ) throws URISyntaxException {
        log.debug("REST request to update Mois : {}, {}", id, mois);
        if (mois.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mois.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return moisRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return moisService
                    .save(mois)
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
     * {@code PATCH  /mois/:id} : Partial updates given fields of an existing mois, field will ignore if it is null
     *
     * @param id the id of the mois to save.
     * @param mois the mois to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mois,
     * or with status {@code 400 (Bad Request)} if the mois is not valid,
     * or with status {@code 404 (Not Found)} if the mois is not found,
     * or with status {@code 500 (Internal Server Error)} if the mois couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/mois/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Mois>> partialUpdateMois(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Mois mois
    ) throws URISyntaxException {
        log.debug("REST request to partial update Mois partially : {}, {}", id, mois);
        if (mois.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mois.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return moisRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Mois> result = moisService.partialUpdate(mois);

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
     * {@code GET  /mois} : get all the mois.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mois in body.
     */
    @GetMapping("/mois")
    public Mono<ResponseEntity<List<Mois>>> getAllMois(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of Mois");
        return moisService
            .countAll()
            .zipWith(moisService.findAll(pageable).collectList())
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
     * {@code GET  /mois/:id} : get the "id" mois.
     *
     * @param id the id of the mois to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mois, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mois/{id}")
    public Mono<ResponseEntity<Mois>> getMois(@PathVariable Long id) {
        log.debug("REST request to get Mois : {}", id);
        Mono<Mois> mois = moisService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mois);
    }

    /**
     * {@code DELETE  /mois/:id} : delete the "id" mois.
     *
     * @param id the id of the mois to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mois/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteMois(@PathVariable Long id) {
        log.debug("REST request to delete Mois : {}", id);
        return moisService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
