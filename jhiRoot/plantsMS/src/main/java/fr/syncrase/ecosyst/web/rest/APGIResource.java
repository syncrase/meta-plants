package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.APGI;
import fr.syncrase.ecosyst.repository.APGIRepository;
import fr.syncrase.ecosyst.service.APGIQueryService;
import fr.syncrase.ecosyst.service.APGIService;
import fr.syncrase.ecosyst.service.criteria.APGICriteria;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.APGI}.
 */
@RestController
@RequestMapping("/api")
public class APGIResource {

    private final Logger log = LoggerFactory.getLogger(APGIResource.class);

    private static final String ENTITY_NAME = "plantsMsapgi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final APGIService aPGIService;

    private final APGIRepository aPGIRepository;

    private final APGIQueryService aPGIQueryService;

    public APGIResource(APGIService aPGIService, APGIRepository aPGIRepository, APGIQueryService aPGIQueryService) {
        this.aPGIService = aPGIService;
        this.aPGIRepository = aPGIRepository;
        this.aPGIQueryService = aPGIQueryService;
    }

    /**
     * {@code POST  /apgis} : Create a new aPGI.
     *
     * @param aPGI the aPGI to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aPGI, or with status {@code 400 (Bad Request)} if the aPGI has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/apgis")
    public ResponseEntity<APGI> createAPGI(@Valid @RequestBody APGI aPGI) throws URISyntaxException {
        log.debug("REST request to save APGI : {}", aPGI);
        if (aPGI.getId() != null) {
            throw new BadRequestAlertException("A new aPGI cannot already have an ID", ENTITY_NAME, "idexists");
        }
        APGI result = aPGIService.save(aPGI);
        return ResponseEntity
            .created(new URI("/api/apgis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /apgis/:id} : Updates an existing aPGI.
     *
     * @param id the id of the aPGI to save.
     * @param aPGI the aPGI to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aPGI,
     * or with status {@code 400 (Bad Request)} if the aPGI is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aPGI couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/apgis/{id}")
    public ResponseEntity<APGI> updateAPGI(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody APGI aPGI)
        throws URISyntaxException {
        log.debug("REST request to update APGI : {}, {}", id, aPGI);
        if (aPGI.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aPGI.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aPGIRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        APGI result = aPGIService.save(aPGI);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, aPGI.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /apgis/:id} : Partial updates given fields of an existing aPGI, field will ignore if it is null
     *
     * @param id the id of the aPGI to save.
     * @param aPGI the aPGI to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aPGI,
     * or with status {@code 400 (Bad Request)} if the aPGI is not valid,
     * or with status {@code 404 (Not Found)} if the aPGI is not found,
     * or with status {@code 500 (Internal Server Error)} if the aPGI couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/apgis/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<APGI> partialUpdateAPGI(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody APGI aPGI
    ) throws URISyntaxException {
        log.debug("REST request to partial update APGI partially : {}, {}", id, aPGI);
        if (aPGI.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aPGI.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aPGIRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<APGI> result = aPGIService.partialUpdate(aPGI);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, aPGI.getId().toString())
        );
    }

    /**
     * {@code GET  /apgis} : get all the aPGIS.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aPGIS in body.
     */
    @GetMapping("/apgis")
    public ResponseEntity<List<APGI>> getAllAPGIS(APGICriteria criteria, Pageable pageable) {
        log.debug("REST request to get APGIS by criteria: {}", criteria);
        Page<APGI> page = aPGIQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /apgis/count} : count all the aPGIS.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/apgis/count")
    public ResponseEntity<Long> countAPGIS(APGICriteria criteria) {
        log.debug("REST request to count APGIS by criteria: {}", criteria);
        return ResponseEntity.ok().body(aPGIQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /apgis/:id} : get the "id" aPGI.
     *
     * @param id the id of the aPGI to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aPGI, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/apgis/{id}")
    public ResponseEntity<APGI> getAPGI(@PathVariable Long id) {
        log.debug("REST request to get APGI : {}", id);
        Optional<APGI> aPGI = aPGIService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aPGI);
    }

    /**
     * {@code DELETE  /apgis/:id} : delete the "id" aPGI.
     *
     * @param id the id of the aPGI to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/apgis/{id}")
    public ResponseEntity<Void> deleteAPGI(@PathVariable Long id) {
        log.debug("REST request to delete APGI : {}", id);
        aPGIService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
