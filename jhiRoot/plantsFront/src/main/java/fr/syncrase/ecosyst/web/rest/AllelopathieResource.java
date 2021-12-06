package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.Allelopathie;
import fr.syncrase.ecosyst.repository.AllelopathieRepository;
import fr.syncrase.ecosyst.service.AllelopathieService;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.Allelopathie}.
 */
@RestController
@RequestMapping("/api")
public class AllelopathieResource {

    private final Logger log = LoggerFactory.getLogger(AllelopathieResource.class);

    private static final String ENTITY_NAME = "allelopathie";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AllelopathieService allelopathieService;

    private final AllelopathieRepository allelopathieRepository;

    public AllelopathieResource(AllelopathieService allelopathieService, AllelopathieRepository allelopathieRepository) {
        this.allelopathieService = allelopathieService;
        this.allelopathieRepository = allelopathieRepository;
    }

    /**
     * {@code POST  /allelopathies} : Create a new allelopathie.
     *
     * @param allelopathie the allelopathie to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new allelopathie, or with status {@code 400 (Bad Request)} if the allelopathie has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/allelopathies")
    public Mono<ResponseEntity<Allelopathie>> createAllelopathie(@Valid @RequestBody Allelopathie allelopathie) throws URISyntaxException {
        log.debug("REST request to save Allelopathie : {}", allelopathie);
        if (allelopathie.getId() != null) {
            throw new BadRequestAlertException("A new allelopathie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return allelopathieService
            .save(allelopathie)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/allelopathies/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /allelopathies/:id} : Updates an existing allelopathie.
     *
     * @param id the id of the allelopathie to save.
     * @param allelopathie the allelopathie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated allelopathie,
     * or with status {@code 400 (Bad Request)} if the allelopathie is not valid,
     * or with status {@code 500 (Internal Server Error)} if the allelopathie couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/allelopathies/{id}")
    public Mono<ResponseEntity<Allelopathie>> updateAllelopathie(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Allelopathie allelopathie
    ) throws URISyntaxException {
        log.debug("REST request to update Allelopathie : {}, {}", id, allelopathie);
        if (allelopathie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, allelopathie.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return allelopathieRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return allelopathieService
                    .save(allelopathie)
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
     * {@code PATCH  /allelopathies/:id} : Partial updates given fields of an existing allelopathie, field will ignore if it is null
     *
     * @param id the id of the allelopathie to save.
     * @param allelopathie the allelopathie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated allelopathie,
     * or with status {@code 400 (Bad Request)} if the allelopathie is not valid,
     * or with status {@code 404 (Not Found)} if the allelopathie is not found,
     * or with status {@code 500 (Internal Server Error)} if the allelopathie couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/allelopathies/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Allelopathie>> partialUpdateAllelopathie(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Allelopathie allelopathie
    ) throws URISyntaxException {
        log.debug("REST request to partial update Allelopathie partially : {}, {}", id, allelopathie);
        if (allelopathie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, allelopathie.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return allelopathieRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Allelopathie> result = allelopathieService.partialUpdate(allelopathie);

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
     * {@code GET  /allelopathies} : get all the allelopathies.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of allelopathies in body.
     */
    @GetMapping("/allelopathies")
    public Mono<ResponseEntity<List<Allelopathie>>> getAllAllelopathies(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of Allelopathies");
        return allelopathieService
            .countAll()
            .zipWith(allelopathieService.findAll(pageable).collectList())
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
     * {@code GET  /allelopathies/:id} : get the "id" allelopathie.
     *
     * @param id the id of the allelopathie to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the allelopathie, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/allelopathies/{id}")
    public Mono<ResponseEntity<Allelopathie>> getAllelopathie(@PathVariable Long id) {
        log.debug("REST request to get Allelopathie : {}", id);
        Mono<Allelopathie> allelopathie = allelopathieService.findOne(id);
        return ResponseUtil.wrapOrNotFound(allelopathie);
    }

    /**
     * {@code DELETE  /allelopathies/:id} : delete the "id" allelopathie.
     *
     * @param id the id of the allelopathie to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/allelopathies/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteAllelopathie(@PathVariable Long id) {
        log.debug("REST request to delete Allelopathie : {}", id);
        return allelopathieService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
