package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.Clade;
import fr.syncrase.ecosyst.repository.CladeRepository;
import fr.syncrase.ecosyst.service.CladeQueryService;
import fr.syncrase.ecosyst.service.CladeService;
import fr.syncrase.ecosyst.service.criteria.CladeCriteria;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.Clade}.
 */
@RestController
@RequestMapping("/api")
public class CladeResource {

    private final Logger log = LoggerFactory.getLogger(CladeResource.class);

    private static final String ENTITY_NAME = "plantsMsClade";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CladeService cladeService;

    private final CladeRepository cladeRepository;

    private final CladeQueryService cladeQueryService;

    public CladeResource(CladeService cladeService, CladeRepository cladeRepository, CladeQueryService cladeQueryService) {
        this.cladeService = cladeService;
        this.cladeRepository = cladeRepository;
        this.cladeQueryService = cladeQueryService;
    }

    /**
     * {@code POST  /clades} : Create a new clade.
     *
     * @param clade the clade to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new clade, or with status {@code 400 (Bad Request)} if the clade has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/clades")
    public ResponseEntity<Clade> createClade(@Valid @RequestBody Clade clade) throws URISyntaxException {
        log.debug("REST request to save Clade : {}", clade);
        if (clade.getId() != null) {
            throw new BadRequestAlertException("A new clade cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Clade result = cladeService.save(clade);
        return ResponseEntity
            .created(new URI("/api/clades/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /clades/:id} : Updates an existing clade.
     *
     * @param id the id of the clade to save.
     * @param clade the clade to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clade,
     * or with status {@code 400 (Bad Request)} if the clade is not valid,
     * or with status {@code 500 (Internal Server Error)} if the clade couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/clades/{id}")
    public ResponseEntity<Clade> updateClade(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Clade clade)
        throws URISyntaxException {
        log.debug("REST request to update Clade : {}, {}", id, clade);
        if (clade.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clade.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cladeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Clade result = cladeService.save(clade);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, clade.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /clades/:id} : Partial updates given fields of an existing clade, field will ignore if it is null
     *
     * @param id the id of the clade to save.
     * @param clade the clade to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clade,
     * or with status {@code 400 (Bad Request)} if the clade is not valid,
     * or with status {@code 404 (Not Found)} if the clade is not found,
     * or with status {@code 500 (Internal Server Error)} if the clade couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/clades/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Clade> partialUpdateClade(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Clade clade
    ) throws URISyntaxException {
        log.debug("REST request to partial update Clade partially : {}, {}", id, clade);
        if (clade.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clade.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cladeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Clade> result = cladeService.partialUpdate(clade);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, clade.getId().toString())
        );
    }

    /**
     * {@code GET  /clades} : get all the clades.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clades in body.
     */
    @GetMapping("/clades")
    public ResponseEntity<List<Clade>> getAllClades(CladeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Clades by criteria: {}", criteria);
        Page<Clade> page = cladeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /clades/count} : count all the clades.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/clades/count")
    public ResponseEntity<Long> countClades(CladeCriteria criteria) {
        log.debug("REST request to count Clades by criteria: {}", criteria);
        return ResponseEntity.ok().body(cladeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /clades/:id} : get the "id" clade.
     *
     * @param id the id of the clade to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the clade, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/clades/{id}")
    public ResponseEntity<Clade> getClade(@PathVariable Long id) {
        log.debug("REST request to get Clade : {}", id);
        Optional<Clade> clade = cladeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(clade);
    }

    /**
     * {@code DELETE  /clades/:id} : delete the "id" clade.
     *
     * @param id the id of the clade to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/clades/{id}")
    public ResponseEntity<Void> deleteClade(@PathVariable Long id) {
        log.debug("REST request to delete Clade : {}", id);
        cladeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
