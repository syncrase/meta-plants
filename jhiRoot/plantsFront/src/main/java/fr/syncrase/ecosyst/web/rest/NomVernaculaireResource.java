package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.NomVernaculaire;
import fr.syncrase.ecosyst.repository.NomVernaculaireRepository;
import fr.syncrase.ecosyst.service.NomVernaculaireService;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.NomVernaculaire}.
 */
@RestController
@RequestMapping("/api")
public class NomVernaculaireResource {

    private final Logger log = LoggerFactory.getLogger(NomVernaculaireResource.class);

    private static final String ENTITY_NAME = "nomVernaculaire";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NomVernaculaireService nomVernaculaireService;

    private final NomVernaculaireRepository nomVernaculaireRepository;

    public NomVernaculaireResource(NomVernaculaireService nomVernaculaireService, NomVernaculaireRepository nomVernaculaireRepository) {
        this.nomVernaculaireService = nomVernaculaireService;
        this.nomVernaculaireRepository = nomVernaculaireRepository;
    }

    /**
     * {@code POST  /nom-vernaculaires} : Create a new nomVernaculaire.
     *
     * @param nomVernaculaire the nomVernaculaire to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nomVernaculaire, or with status {@code 400 (Bad Request)} if the nomVernaculaire has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/nom-vernaculaires")
    public Mono<ResponseEntity<NomVernaculaire>> createNomVernaculaire(@Valid @RequestBody NomVernaculaire nomVernaculaire)
        throws URISyntaxException {
        log.debug("REST request to save NomVernaculaire : {}", nomVernaculaire);
        if (nomVernaculaire.getId() != null) {
            throw new BadRequestAlertException("A new nomVernaculaire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return nomVernaculaireService
            .save(nomVernaculaire)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/nom-vernaculaires/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /nom-vernaculaires/:id} : Updates an existing nomVernaculaire.
     *
     * @param id the id of the nomVernaculaire to save.
     * @param nomVernaculaire the nomVernaculaire to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nomVernaculaire,
     * or with status {@code 400 (Bad Request)} if the nomVernaculaire is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nomVernaculaire couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/nom-vernaculaires/{id}")
    public Mono<ResponseEntity<NomVernaculaire>> updateNomVernaculaire(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NomVernaculaire nomVernaculaire
    ) throws URISyntaxException {
        log.debug("REST request to update NomVernaculaire : {}, {}", id, nomVernaculaire);
        if (nomVernaculaire.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nomVernaculaire.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return nomVernaculaireRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return nomVernaculaireService
                    .save(nomVernaculaire)
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
     * {@code PATCH  /nom-vernaculaires/:id} : Partial updates given fields of an existing nomVernaculaire, field will ignore if it is null
     *
     * @param id the id of the nomVernaculaire to save.
     * @param nomVernaculaire the nomVernaculaire to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nomVernaculaire,
     * or with status {@code 400 (Bad Request)} if the nomVernaculaire is not valid,
     * or with status {@code 404 (Not Found)} if the nomVernaculaire is not found,
     * or with status {@code 500 (Internal Server Error)} if the nomVernaculaire couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/nom-vernaculaires/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<NomVernaculaire>> partialUpdateNomVernaculaire(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NomVernaculaire nomVernaculaire
    ) throws URISyntaxException {
        log.debug("REST request to partial update NomVernaculaire partially : {}, {}", id, nomVernaculaire);
        if (nomVernaculaire.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nomVernaculaire.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return nomVernaculaireRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<NomVernaculaire> result = nomVernaculaireService.partialUpdate(nomVernaculaire);

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
     * {@code GET  /nom-vernaculaires} : get all the nomVernaculaires.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nomVernaculaires in body.
     */
    @GetMapping("/nom-vernaculaires")
    public Mono<ResponseEntity<List<NomVernaculaire>>> getAllNomVernaculaires(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of NomVernaculaires");
        return nomVernaculaireService
            .countAll()
            .zipWith(nomVernaculaireService.findAll(pageable).collectList())
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
     * {@code GET  /nom-vernaculaires/:id} : get the "id" nomVernaculaire.
     *
     * @param id the id of the nomVernaculaire to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nomVernaculaire, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/nom-vernaculaires/{id}")
    public Mono<ResponseEntity<NomVernaculaire>> getNomVernaculaire(@PathVariable Long id) {
        log.debug("REST request to get NomVernaculaire : {}", id);
        Mono<NomVernaculaire> nomVernaculaire = nomVernaculaireService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nomVernaculaire);
    }

    /**
     * {@code DELETE  /nom-vernaculaires/:id} : delete the "id" nomVernaculaire.
     *
     * @param id the id of the nomVernaculaire to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/nom-vernaculaires/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteNomVernaculaire(@PathVariable Long id) {
        log.debug("REST request to delete NomVernaculaire : {}", id);
        return nomVernaculaireService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
