package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.Plante;
import fr.syncrase.ecosyst.repository.PlanteRepository;
import fr.syncrase.ecosyst.service.PlanteService;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.Plante}.
 */
@RestController
@RequestMapping("/api")
public class PlanteResource {

    private final Logger log = LoggerFactory.getLogger(PlanteResource.class);

    private static final String ENTITY_NAME = "plante";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlanteService planteService;

    private final PlanteRepository planteRepository;

    public PlanteResource(PlanteService planteService, PlanteRepository planteRepository) {
        this.planteService = planteService;
        this.planteRepository = planteRepository;
    }

    /**
     * {@code POST  /plantes} : Create a new plante.
     *
     * @param plante the plante to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new plante, or with status {@code 400 (Bad Request)} if the plante has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/plantes")
    public Mono<ResponseEntity<Plante>> createPlante(@RequestBody Plante plante) throws URISyntaxException {
        log.debug("REST request to save Plante : {}", plante);
        if (plante.getId() != null) {
            throw new BadRequestAlertException("A new plante cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return planteService
            .save(plante)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/plantes/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /plantes/:id} : Updates an existing plante.
     *
     * @param id the id of the plante to save.
     * @param plante the plante to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated plante,
     * or with status {@code 400 (Bad Request)} if the plante is not valid,
     * or with status {@code 500 (Internal Server Error)} if the plante couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/plantes/{id}")
    public Mono<ResponseEntity<Plante>> updatePlante(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Plante plante
    ) throws URISyntaxException {
        log.debug("REST request to update Plante : {}, {}", id, plante);
        if (plante.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, plante.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return planteRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return planteService
                    .save(plante)
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
     * {@code PATCH  /plantes/:id} : Partial updates given fields of an existing plante, field will ignore if it is null
     *
     * @param id the id of the plante to save.
     * @param plante the plante to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated plante,
     * or with status {@code 400 (Bad Request)} if the plante is not valid,
     * or with status {@code 404 (Not Found)} if the plante is not found,
     * or with status {@code 500 (Internal Server Error)} if the plante couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/plantes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Plante>> partialUpdatePlante(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Plante plante
    ) throws URISyntaxException {
        log.debug("REST request to partial update Plante partially : {}, {}", id, plante);
        if (plante.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, plante.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return planteRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Plante> result = planteService.partialUpdate(plante);

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
     * {@code GET  /plantes} : get all the plantes.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of plantes in body.
     */
    @GetMapping("/plantes")
    public Mono<ResponseEntity<List<Plante>>> getAllPlantes(
        Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Plantes");
        return planteService
            .countAll()
            .zipWith(planteService.findAll(pageable).collectList())
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
     * {@code GET  /plantes/:id} : get the "id" plante.
     *
     * @param id the id of the plante to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the plante, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/plantes/{id}")
    public Mono<ResponseEntity<Plante>> getPlante(@PathVariable Long id) {
        log.debug("REST request to get Plante : {}", id);
        Mono<Plante> plante = planteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(plante);
    }

    /**
     * {@code DELETE  /plantes/:id} : delete the "id" plante.
     *
     * @param id the id of the plante to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/plantes/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deletePlante(@PathVariable Long id) {
        log.debug("REST request to delete Plante : {}", id);
        return planteService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
