package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.SousGenre;
import fr.syncrase.ecosyst.repository.SousGenreRepository;
import fr.syncrase.ecosyst.service.SousGenreQueryService;
import fr.syncrase.ecosyst.service.SousGenreService;
import fr.syncrase.ecosyst.service.criteria.SousGenreCriteria;
import fr.syncrase.ecosyst.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.SousGenre}.
 */
@RestController
@RequestMapping("/api")
public class SousGenreResource {

    private final Logger log = LoggerFactory.getLogger(SousGenreResource.class);

    private static final String ENTITY_NAME = "classificationMsSousGenre";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SousGenreService sousGenreService;

    private final SousGenreRepository sousGenreRepository;

    private final SousGenreQueryService sousGenreQueryService;

    public SousGenreResource(
        SousGenreService sousGenreService,
        SousGenreRepository sousGenreRepository,
        SousGenreQueryService sousGenreQueryService
    ) {
        this.sousGenreService = sousGenreService;
        this.sousGenreRepository = sousGenreRepository;
        this.sousGenreQueryService = sousGenreQueryService;
    }

    /**
     * {@code POST  /sous-genres} : Create a new sousGenre.
     *
     * @param sousGenre the sousGenre to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sousGenre, or with status {@code 400 (Bad Request)} if the sousGenre has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sous-genres")
    public ResponseEntity<SousGenre> createSousGenre(@Valid @RequestBody SousGenre sousGenre) throws URISyntaxException {
        log.debug("REST request to save SousGenre : {}", sousGenre);
        if (sousGenre.getId() != null) {
            throw new BadRequestAlertException("A new sousGenre cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SousGenre result = sousGenreService.save(sousGenre);
        return ResponseEntity
            .created(new URI("/api/sous-genres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sous-genres/:id} : Updates an existing sousGenre.
     *
     * @param id the id of the sousGenre to save.
     * @param sousGenre the sousGenre to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sousGenre,
     * or with status {@code 400 (Bad Request)} if the sousGenre is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sousGenre couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sous-genres/{id}")
    public ResponseEntity<SousGenre> updateSousGenre(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SousGenre sousGenre
    ) throws URISyntaxException {
        log.debug("REST request to update SousGenre : {}, {}", id, sousGenre);
        if (sousGenre.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sousGenre.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sousGenreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SousGenre result = sousGenreService.save(sousGenre);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sousGenre.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sous-genres/:id} : Partial updates given fields of an existing sousGenre, field will ignore if it is null
     *
     * @param id the id of the sousGenre to save.
     * @param sousGenre the sousGenre to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sousGenre,
     * or with status {@code 400 (Bad Request)} if the sousGenre is not valid,
     * or with status {@code 404 (Not Found)} if the sousGenre is not found,
     * or with status {@code 500 (Internal Server Error)} if the sousGenre couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sous-genres/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SousGenre> partialUpdateSousGenre(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SousGenre sousGenre
    ) throws URISyntaxException {
        log.debug("REST request to partial update SousGenre partially : {}, {}", id, sousGenre);
        if (sousGenre.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sousGenre.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sousGenreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SousGenre> result = sousGenreService.partialUpdate(sousGenre);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sousGenre.getId().toString())
        );
    }

    /**
     * {@code GET  /sous-genres} : get all the sousGenres.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sousGenres in body.
     */
    @GetMapping("/sous-genres")
    public ResponseEntity<List<SousGenre>> getAllSousGenres(SousGenreCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SousGenres by criteria: {}", criteria);
        Page<SousGenre> page = sousGenreQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sous-genres/count} : count all the sousGenres.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/sous-genres/count")
    public ResponseEntity<Long> countSousGenres(SousGenreCriteria criteria) {
        log.debug("REST request to count SousGenres by criteria: {}", criteria);
        return ResponseEntity.ok().body(sousGenreQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sous-genres/:id} : get the "id" sousGenre.
     *
     * @param id the id of the sousGenre to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sousGenre, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sous-genres/{id}")
    public ResponseEntity<SousGenre> getSousGenre(@PathVariable Long id) {
        log.debug("REST request to get SousGenre : {}", id);
        Optional<SousGenre> sousGenre = sousGenreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sousGenre);
    }

    /**
     * {@code DELETE  /sous-genres/:id} : delete the "id" sousGenre.
     *
     * @param id the id of the sousGenre to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sous-genres/{id}")
    public ResponseEntity<Void> deleteSousGenre(@PathVariable Long id) {
        log.debug("REST request to delete SousGenre : {}", id);
        sousGenreService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
