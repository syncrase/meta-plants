package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.APGIII;
import fr.syncrase.ecosyst.repository.APGIIIRepository;
import fr.syncrase.ecosyst.service.APGIIIQueryService;
import fr.syncrase.ecosyst.service.APGIIIService;
import fr.syncrase.ecosyst.service.criteria.APGIIICriteria;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.APGIII}.
 */
@RestController
@RequestMapping("/api")
public class APGIIIResource {

    private final Logger log = LoggerFactory.getLogger(APGIIIResource.class);

    private static final String ENTITY_NAME = "plantsMsapgiii";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final APGIIIService aPGIIIService;

    private final APGIIIRepository aPGIIIRepository;

    private final APGIIIQueryService aPGIIIQueryService;

    public APGIIIResource(APGIIIService aPGIIIService, APGIIIRepository aPGIIIRepository, APGIIIQueryService aPGIIIQueryService) {
        this.aPGIIIService = aPGIIIService;
        this.aPGIIIRepository = aPGIIIRepository;
        this.aPGIIIQueryService = aPGIIIQueryService;
    }

    /**
     * {@code POST  /apgiiis} : Create a new aPGIII.
     *
     * @param aPGIII the aPGIII to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aPGIII, or with status {@code 400 (Bad Request)} if the aPGIII has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/apgiiis")
    public ResponseEntity<APGIII> createAPGIII(@Valid @RequestBody APGIII aPGIII) throws URISyntaxException {
        log.debug("REST request to save APGIII : {}", aPGIII);
        if (aPGIII.getId() != null) {
            throw new BadRequestAlertException("A new aPGIII cannot already have an ID", ENTITY_NAME, "idexists");
        }
        APGIII result = aPGIIIService.save(aPGIII);
        return ResponseEntity
            .created(new URI("/api/apgiiis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /apgiiis/:id} : Updates an existing aPGIII.
     *
     * @param id the id of the aPGIII to save.
     * @param aPGIII the aPGIII to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aPGIII,
     * or with status {@code 400 (Bad Request)} if the aPGIII is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aPGIII couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/apgiiis/{id}")
    public ResponseEntity<APGIII> updateAPGIII(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody APGIII aPGIII
    ) throws URISyntaxException {
        log.debug("REST request to update APGIII : {}, {}", id, aPGIII);
        if (aPGIII.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aPGIII.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aPGIIIRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        APGIII result = aPGIIIService.save(aPGIII);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, aPGIII.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /apgiiis/:id} : Partial updates given fields of an existing aPGIII, field will ignore if it is null
     *
     * @param id the id of the aPGIII to save.
     * @param aPGIII the aPGIII to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aPGIII,
     * or with status {@code 400 (Bad Request)} if the aPGIII is not valid,
     * or with status {@code 404 (Not Found)} if the aPGIII is not found,
     * or with status {@code 500 (Internal Server Error)} if the aPGIII couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/apgiiis/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<APGIII> partialUpdateAPGIII(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody APGIII aPGIII
    ) throws URISyntaxException {
        log.debug("REST request to partial update APGIII partially : {}, {}", id, aPGIII);
        if (aPGIII.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aPGIII.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aPGIIIRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<APGIII> result = aPGIIIService.partialUpdate(aPGIII);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, aPGIII.getId().toString())
        );
    }

    /**
     * {@code GET  /apgiiis} : get all the aPGIIIS.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aPGIIIS in body.
     */
    @GetMapping("/apgiiis")
    public ResponseEntity<List<APGIII>> getAllAPGIIIS(APGIIICriteria criteria, Pageable pageable) {
        log.debug("REST request to get APGIIIS by criteria: {}", criteria);
        Page<APGIII> page = aPGIIIQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /apgiiis/count} : count all the aPGIIIS.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/apgiiis/count")
    public ResponseEntity<Long> countAPGIIIS(APGIIICriteria criteria) {
        log.debug("REST request to count APGIIIS by criteria: {}", criteria);
        return ResponseEntity.ok().body(aPGIIIQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /apgiiis/:id} : get the "id" aPGIII.
     *
     * @param id the id of the aPGIII to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aPGIII, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/apgiiis/{id}")
    public ResponseEntity<APGIII> getAPGIII(@PathVariable Long id) {
        log.debug("REST request to get APGIII : {}", id);
        Optional<APGIII> aPGIII = aPGIIIService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aPGIII);
    }

    /**
     * {@code DELETE  /apgiiis/:id} : delete the "id" aPGIII.
     *
     * @param id the id of the aPGIII to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/apgiiis/{id}")
    public ResponseEntity<Void> deleteAPGIII(@PathVariable Long id) {
        log.debug("REST request to delete APGIII : {}", id);
        aPGIIIService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
