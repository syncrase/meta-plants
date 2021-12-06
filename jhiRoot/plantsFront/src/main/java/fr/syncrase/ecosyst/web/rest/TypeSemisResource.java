package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.TypeSemis;
import fr.syncrase.ecosyst.repository.TypeSemisRepository;
import fr.syncrase.ecosyst.service.TypeSemisService;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.TypeSemis}.
 */
@RestController
@RequestMapping("/api")
public class TypeSemisResource {

    private final Logger log = LoggerFactory.getLogger(TypeSemisResource.class);

    private static final String ENTITY_NAME = "typeSemis";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeSemisService typeSemisService;

    private final TypeSemisRepository typeSemisRepository;

    public TypeSemisResource(TypeSemisService typeSemisService, TypeSemisRepository typeSemisRepository) {
        this.typeSemisService = typeSemisService;
        this.typeSemisRepository = typeSemisRepository;
    }

    /**
     * {@code POST  /type-semis} : Create a new typeSemis.
     *
     * @param typeSemis the typeSemis to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeSemis, or with status {@code 400 (Bad Request)} if the typeSemis has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/type-semis")
    public Mono<ResponseEntity<TypeSemis>> createTypeSemis(@Valid @RequestBody TypeSemis typeSemis) throws URISyntaxException {
        log.debug("REST request to save TypeSemis : {}", typeSemis);
        if (typeSemis.getId() != null) {
            throw new BadRequestAlertException("A new typeSemis cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return typeSemisService
            .save(typeSemis)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/type-semis/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /type-semis/:id} : Updates an existing typeSemis.
     *
     * @param id the id of the typeSemis to save.
     * @param typeSemis the typeSemis to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeSemis,
     * or with status {@code 400 (Bad Request)} if the typeSemis is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeSemis couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/type-semis/{id}")
    public Mono<ResponseEntity<TypeSemis>> updateTypeSemis(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TypeSemis typeSemis
    ) throws URISyntaxException {
        log.debug("REST request to update TypeSemis : {}, {}", id, typeSemis);
        if (typeSemis.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeSemis.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return typeSemisRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return typeSemisService
                    .save(typeSemis)
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
     * {@code PATCH  /type-semis/:id} : Partial updates given fields of an existing typeSemis, field will ignore if it is null
     *
     * @param id the id of the typeSemis to save.
     * @param typeSemis the typeSemis to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeSemis,
     * or with status {@code 400 (Bad Request)} if the typeSemis is not valid,
     * or with status {@code 404 (Not Found)} if the typeSemis is not found,
     * or with status {@code 500 (Internal Server Error)} if the typeSemis couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/type-semis/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<TypeSemis>> partialUpdateTypeSemis(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TypeSemis typeSemis
    ) throws URISyntaxException {
        log.debug("REST request to partial update TypeSemis partially : {}, {}", id, typeSemis);
        if (typeSemis.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeSemis.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return typeSemisRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<TypeSemis> result = typeSemisService.partialUpdate(typeSemis);

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
     * {@code GET  /type-semis} : get all the typeSemis.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeSemis in body.
     */
    @GetMapping("/type-semis")
    public Mono<ResponseEntity<List<TypeSemis>>> getAllTypeSemis(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of TypeSemis");
        return typeSemisService
            .countAll()
            .zipWith(typeSemisService.findAll(pageable).collectList())
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
     * {@code GET  /type-semis/:id} : get the "id" typeSemis.
     *
     * @param id the id of the typeSemis to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeSemis, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/type-semis/{id}")
    public Mono<ResponseEntity<TypeSemis>> getTypeSemis(@PathVariable Long id) {
        log.debug("REST request to get TypeSemis : {}", id);
        Mono<TypeSemis> typeSemis = typeSemisService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeSemis);
    }

    /**
     * {@code DELETE  /type-semis/:id} : delete the "id" typeSemis.
     *
     * @param id the id of the typeSemis to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/type-semis/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteTypeSemis(@PathVariable Long id) {
        log.debug("REST request to delete TypeSemis : {}", id);
        return typeSemisService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
