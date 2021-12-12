package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.InfraEmbranchement;
import fr.syncrase.ecosyst.repository.InfraEmbranchementRepository;
import fr.syncrase.ecosyst.service.InfraEmbranchementQueryService;
import fr.syncrase.ecosyst.service.InfraEmbranchementService;
import fr.syncrase.ecosyst.service.criteria.InfraEmbranchementCriteria;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.InfraEmbranchement}.
 */
@RestController
@RequestMapping("/api")
public class InfraEmbranchementResource {

    private final Logger log = LoggerFactory.getLogger(InfraEmbranchementResource.class);

    private static final String ENTITY_NAME = "classificationMsInfraEmbranchement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InfraEmbranchementService infraEmbranchementService;

    private final InfraEmbranchementRepository infraEmbranchementRepository;

    private final InfraEmbranchementQueryService infraEmbranchementQueryService;

    public InfraEmbranchementResource(
        InfraEmbranchementService infraEmbranchementService,
        InfraEmbranchementRepository infraEmbranchementRepository,
        InfraEmbranchementQueryService infraEmbranchementQueryService
    ) {
        this.infraEmbranchementService = infraEmbranchementService;
        this.infraEmbranchementRepository = infraEmbranchementRepository;
        this.infraEmbranchementQueryService = infraEmbranchementQueryService;
    }

    /**
     * {@code POST  /infra-embranchements} : Create a new infraEmbranchement.
     *
     * @param infraEmbranchement the infraEmbranchement to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new infraEmbranchement, or with status {@code 400 (Bad Request)} if the infraEmbranchement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/infra-embranchements")
    public ResponseEntity<InfraEmbranchement> createInfraEmbranchement(@Valid @RequestBody InfraEmbranchement infraEmbranchement)
        throws URISyntaxException {
        log.debug("REST request to save InfraEmbranchement : {}", infraEmbranchement);
        if (infraEmbranchement.getId() != null) {
            throw new BadRequestAlertException("A new infraEmbranchement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InfraEmbranchement result = infraEmbranchementService.save(infraEmbranchement);
        return ResponseEntity
            .created(new URI("/api/infra-embranchements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /infra-embranchements/:id} : Updates an existing infraEmbranchement.
     *
     * @param id the id of the infraEmbranchement to save.
     * @param infraEmbranchement the infraEmbranchement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated infraEmbranchement,
     * or with status {@code 400 (Bad Request)} if the infraEmbranchement is not valid,
     * or with status {@code 500 (Internal Server Error)} if the infraEmbranchement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/infra-embranchements/{id}")
    public ResponseEntity<InfraEmbranchement> updateInfraEmbranchement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InfraEmbranchement infraEmbranchement
    ) throws URISyntaxException {
        log.debug("REST request to update InfraEmbranchement : {}, {}", id, infraEmbranchement);
        if (infraEmbranchement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, infraEmbranchement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!infraEmbranchementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InfraEmbranchement result = infraEmbranchementService.save(infraEmbranchement);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, infraEmbranchement.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /infra-embranchements/:id} : Partial updates given fields of an existing infraEmbranchement, field will ignore if it is null
     *
     * @param id the id of the infraEmbranchement to save.
     * @param infraEmbranchement the infraEmbranchement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated infraEmbranchement,
     * or with status {@code 400 (Bad Request)} if the infraEmbranchement is not valid,
     * or with status {@code 404 (Not Found)} if the infraEmbranchement is not found,
     * or with status {@code 500 (Internal Server Error)} if the infraEmbranchement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/infra-embranchements/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InfraEmbranchement> partialUpdateInfraEmbranchement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InfraEmbranchement infraEmbranchement
    ) throws URISyntaxException {
        log.debug("REST request to partial update InfraEmbranchement partially : {}, {}", id, infraEmbranchement);
        if (infraEmbranchement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, infraEmbranchement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!infraEmbranchementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InfraEmbranchement> result = infraEmbranchementService.partialUpdate(infraEmbranchement);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, infraEmbranchement.getId().toString())
        );
    }

    /**
     * {@code GET  /infra-embranchements} : get all the infraEmbranchements.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of infraEmbranchements in body.
     */
    @GetMapping("/infra-embranchements")
    public ResponseEntity<List<InfraEmbranchement>> getAllInfraEmbranchements(InfraEmbranchementCriteria criteria, Pageable pageable) {
        log.debug("REST request to get InfraEmbranchements by criteria: {}", criteria);
        Page<InfraEmbranchement> page = infraEmbranchementQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /infra-embranchements/count} : count all the infraEmbranchements.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/infra-embranchements/count")
    public ResponseEntity<Long> countInfraEmbranchements(InfraEmbranchementCriteria criteria) {
        log.debug("REST request to count InfraEmbranchements by criteria: {}", criteria);
        return ResponseEntity.ok().body(infraEmbranchementQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /infra-embranchements/:id} : get the "id" infraEmbranchement.
     *
     * @param id the id of the infraEmbranchement to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the infraEmbranchement, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/infra-embranchements/{id}")
    public ResponseEntity<InfraEmbranchement> getInfraEmbranchement(@PathVariable Long id) {
        log.debug("REST request to get InfraEmbranchement : {}", id);
        Optional<InfraEmbranchement> infraEmbranchement = infraEmbranchementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(infraEmbranchement);
    }

    /**
     * {@code DELETE  /infra-embranchements/:id} : delete the "id" infraEmbranchement.
     *
     * @param id the id of the infraEmbranchement to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/infra-embranchements/{id}")
    public ResponseEntity<Void> deleteInfraEmbranchement(@PathVariable Long id) {
        log.debug("REST request to delete InfraEmbranchement : {}", id);
        infraEmbranchementService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
