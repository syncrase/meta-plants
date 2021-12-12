package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.Genre;
import fr.syncrase.ecosyst.repository.GenreRepository;
import fr.syncrase.ecosyst.service.GenreQueryService;
import fr.syncrase.ecosyst.service.GenreService;
import fr.syncrase.ecosyst.service.criteria.GenreCriteria;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.Genre}.
 */
@RestController
@RequestMapping("/api")
public class GenreResource {

    private final Logger log = LoggerFactory.getLogger(GenreResource.class);

    private static final String ENTITY_NAME = "classificationMsGenre";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GenreService genreService;

    private final GenreRepository genreRepository;

    private final GenreQueryService genreQueryService;

    public GenreResource(GenreService genreService, GenreRepository genreRepository, GenreQueryService genreQueryService) {
        this.genreService = genreService;
        this.genreRepository = genreRepository;
        this.genreQueryService = genreQueryService;
    }

    /**
     * {@code POST  /genres} : Create a new genre.
     *
     * @param genre the genre to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new genre, or with status {@code 400 (Bad Request)} if the genre has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/genres")
    public ResponseEntity<Genre> createGenre(@Valid @RequestBody Genre genre) throws URISyntaxException {
        log.debug("REST request to save Genre : {}", genre);
        if (genre.getId() != null) {
            throw new BadRequestAlertException("A new genre cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Genre result = genreService.save(genre);
        return ResponseEntity
            .created(new URI("/api/genres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /genres/:id} : Updates an existing genre.
     *
     * @param id the id of the genre to save.
     * @param genre the genre to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated genre,
     * or with status {@code 400 (Bad Request)} if the genre is not valid,
     * or with status {@code 500 (Internal Server Error)} if the genre couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/genres/{id}")
    public ResponseEntity<Genre> updateGenre(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Genre genre)
        throws URISyntaxException {
        log.debug("REST request to update Genre : {}, {}", id, genre);
        if (genre.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, genre.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!genreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Genre result = genreService.save(genre);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, genre.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /genres/:id} : Partial updates given fields of an existing genre, field will ignore if it is null
     *
     * @param id the id of the genre to save.
     * @param genre the genre to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated genre,
     * or with status {@code 400 (Bad Request)} if the genre is not valid,
     * or with status {@code 404 (Not Found)} if the genre is not found,
     * or with status {@code 500 (Internal Server Error)} if the genre couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/genres/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Genre> partialUpdateGenre(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Genre genre
    ) throws URISyntaxException {
        log.debug("REST request to partial update Genre partially : {}, {}", id, genre);
        if (genre.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, genre.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!genreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Genre> result = genreService.partialUpdate(genre);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, genre.getId().toString())
        );
    }

    /**
     * {@code GET  /genres} : get all the genres.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of genres in body.
     */
    @GetMapping("/genres")
    public ResponseEntity<List<Genre>> getAllGenres(GenreCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Genres by criteria: {}", criteria);
        Page<Genre> page = genreQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /genres/count} : count all the genres.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/genres/count")
    public ResponseEntity<Long> countGenres(GenreCriteria criteria) {
        log.debug("REST request to count Genres by criteria: {}", criteria);
        return ResponseEntity.ok().body(genreQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /genres/:id} : get the "id" genre.
     *
     * @param id the id of the genre to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the genre, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/genres/{id}")
    public ResponseEntity<Genre> getGenre(@PathVariable Long id) {
        log.debug("REST request to get Genre : {}", id);
        Optional<Genre> genre = genreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(genre);
    }

    /**
     * {@code DELETE  /genres/:id} : delete the "id" genre.
     *
     * @param id the id of the genre to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/genres/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        log.debug("REST request to delete Genre : {}", id);
        genreService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
