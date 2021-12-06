package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.APGII;
import fr.syncrase.ecosyst.repository.APGIIRepository;
import fr.syncrase.ecosyst.service.APGIIQueryService;
import fr.syncrase.ecosyst.service.APGIIService;
import fr.syncrase.ecosyst.service.criteria.APGIICriteria;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.APGII}.
 */
@RestController
@RequestMapping("/api")
public class APGIIResource {

    private final Logger log = LoggerFactory.getLogger(APGIIResource.class);

    private static final String ENTITY_NAME = "plantsMsapgii";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final APGIIService aPGIIService;

    private final APGIIRepository aPGIIRepository;

    private final APGIIQueryService aPGIIQueryService;

    public APGIIResource(APGIIService aPGIIService, APGIIRepository aPGIIRepository, APGIIQueryService aPGIIQueryService) {
        this.aPGIIService = aPGIIService;
        this.aPGIIRepository = aPGIIRepository;
        this.aPGIIQueryService = aPGIIQueryService;
    }

    /**
     * {@code POST  /apgiis} : Create a new aPGII.
     *
     * @param aPGII the aPGII to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aPGII, or with status {@code 400 (Bad Request)} if the aPGII has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/apgiis")
    public ResponseEntity<APGII> createAPGII(@Valid @RequestBody APGII aPGII) throws URISyntaxException {
        log.debug("REST request to save APGII : {}", aPGII);
        if (aPGII.getId() != null) {
            throw new BadRequestAlertException("A new aPGII cannot already have an ID", ENTITY_NAME, "idexists");
        }
        APGII result = aPGIIService.save(aPGII);
        return ResponseEntity
            .created(new URI("/api/apgiis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /apgiis/:id} : Updates an existing aPGII.
     *
     * @param id the id of the aPGII to save.
     * @param aPGII the aPGII to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aPGII,
     * or with status {@code 400 (Bad Request)} if the aPGII is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aPGII couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/apgiis/{id}")
    public ResponseEntity<APGII> updateAPGII(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody APGII aPGII)
        throws URISyntaxException {
        log.debug("REST request to update APGII : {}, {}", id, aPGII);
        if (aPGII.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aPGII.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aPGIIRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        APGII result = aPGIIService.save(aPGII);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, aPGII.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /apgiis/:id} : Partial updates given fields of an existing aPGII, field will ignore if it is null
     *
     * @param id the id of the aPGII to save.
     * @param aPGII the aPGII to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aPGII,
     * or with status {@code 400 (Bad Request)} if the aPGII is not valid,
     * or with status {@code 404 (Not Found)} if the aPGII is not found,
     * or with status {@code 500 (Internal Server Error)} if the aPGII couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/apgiis/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<APGII> partialUpdateAPGII(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody APGII aPGII
    ) throws URISyntaxException {
        log.debug("REST request to partial update APGII partially : {}, {}", id, aPGII);
        if (aPGII.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aPGII.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aPGIIRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<APGII> result = aPGIIService.partialUpdate(aPGII);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, aPGII.getId().toString())
        );
    }

    /**
     * {@code GET  /apgiis} : get all the aPGIIS.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aPGIIS in body.
     */
    @GetMapping("/apgiis")
    public ResponseEntity<List<APGII>> getAllAPGIIS(APGIICriteria criteria, Pageable pageable) {
        log.debug("REST request to get APGIIS by criteria: {}", criteria);
        Page<APGII> page = aPGIIQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /apgiis/count} : count all the aPGIIS.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/apgiis/count")
    public ResponseEntity<Long> countAPGIIS(APGIICriteria criteria) {
        log.debug("REST request to count APGIIS by criteria: {}", criteria);
        return ResponseEntity.ok().body(aPGIIQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /apgiis/:id} : get the "id" aPGII.
     *
     * @param id the id of the aPGII to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aPGII, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/apgiis/{id}")
    public ResponseEntity<APGII> getAPGII(@PathVariable Long id) {
        log.debug("REST request to get APGII : {}", id);
        Optional<APGII> aPGII = aPGIIService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aPGII);
    }

    /**
     * {@code DELETE  /apgiis/:id} : delete the "id" aPGII.
     *
     * @param id the id of the aPGII to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/apgiis/{id}")
    public ResponseEntity<Void> deleteAPGII(@PathVariable Long id) {
        log.debug("REST request to delete APGII : {}", id);
        aPGIIService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
