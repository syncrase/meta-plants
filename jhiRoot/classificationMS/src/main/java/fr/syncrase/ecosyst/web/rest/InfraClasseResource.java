package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.InfraClasse;
import fr.syncrase.ecosyst.repository.InfraClasseRepository;
import fr.syncrase.ecosyst.service.InfraClasseQueryService;
import fr.syncrase.ecosyst.service.InfraClasseService;
import fr.syncrase.ecosyst.service.criteria.InfraClasseCriteria;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.InfraClasse}.
 */
@RestController
@RequestMapping("/api")
public class InfraClasseResource {

    private final Logger log = LoggerFactory.getLogger(InfraClasseResource.class);

    private static final String ENTITY_NAME = "classificationMsInfraClasse";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InfraClasseService infraClasseService;

    private final InfraClasseRepository infraClasseRepository;

    private final InfraClasseQueryService infraClasseQueryService;

    public InfraClasseResource(
        InfraClasseService infraClasseService,
        InfraClasseRepository infraClasseRepository,
        InfraClasseQueryService infraClasseQueryService
    ) {
        this.infraClasseService = infraClasseService;
        this.infraClasseRepository = infraClasseRepository;
        this.infraClasseQueryService = infraClasseQueryService;
    }

    /**
     * {@code POST  /infra-classes} : Create a new infraClasse.
     *
     * @param infraClasse the infraClasse to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new infraClasse, or with status {@code 400 (Bad Request)} if the infraClasse has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/infra-classes")
    public ResponseEntity<InfraClasse> createInfraClasse(@Valid @RequestBody InfraClasse infraClasse) throws URISyntaxException {
        log.debug("REST request to save InfraClasse : {}", infraClasse);
        if (infraClasse.getId() != null) {
            throw new BadRequestAlertException("A new infraClasse cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InfraClasse result = infraClasseService.save(infraClasse);
        return ResponseEntity
            .created(new URI("/api/infra-classes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /infra-classes/:id} : Updates an existing infraClasse.
     *
     * @param id the id of the infraClasse to save.
     * @param infraClasse the infraClasse to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated infraClasse,
     * or with status {@code 400 (Bad Request)} if the infraClasse is not valid,
     * or with status {@code 500 (Internal Server Error)} if the infraClasse couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/infra-classes/{id}")
    public ResponseEntity<InfraClasse> updateInfraClasse(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InfraClasse infraClasse
    ) throws URISyntaxException {
        log.debug("REST request to update InfraClasse : {}, {}", id, infraClasse);
        if (infraClasse.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, infraClasse.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!infraClasseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InfraClasse result = infraClasseService.save(infraClasse);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, infraClasse.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /infra-classes/:id} : Partial updates given fields of an existing infraClasse, field will ignore if it is null
     *
     * @param id the id of the infraClasse to save.
     * @param infraClasse the infraClasse to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated infraClasse,
     * or with status {@code 400 (Bad Request)} if the infraClasse is not valid,
     * or with status {@code 404 (Not Found)} if the infraClasse is not found,
     * or with status {@code 500 (Internal Server Error)} if the infraClasse couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/infra-classes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InfraClasse> partialUpdateInfraClasse(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InfraClasse infraClasse
    ) throws URISyntaxException {
        log.debug("REST request to partial update InfraClasse partially : {}, {}", id, infraClasse);
        if (infraClasse.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, infraClasse.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!infraClasseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InfraClasse> result = infraClasseService.partialUpdate(infraClasse);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, infraClasse.getId().toString())
        );
    }

    /**
     * {@code GET  /infra-classes} : get all the infraClasses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of infraClasses in body.
     */
    @GetMapping("/infra-classes")
    public ResponseEntity<List<InfraClasse>> getAllInfraClasses(InfraClasseCriteria criteria, Pageable pageable) {
        log.debug("REST request to get InfraClasses by criteria: {}", criteria);
        Page<InfraClasse> page = infraClasseQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /infra-classes/count} : count all the infraClasses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/infra-classes/count")
    public ResponseEntity<Long> countInfraClasses(InfraClasseCriteria criteria) {
        log.debug("REST request to count InfraClasses by criteria: {}", criteria);
        return ResponseEntity.ok().body(infraClasseQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /infra-classes/:id} : get the "id" infraClasse.
     *
     * @param id the id of the infraClasse to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the infraClasse, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/infra-classes/{id}")
    public ResponseEntity<InfraClasse> getInfraClasse(@PathVariable Long id) {
        log.debug("REST request to get InfraClasse : {}", id);
        Optional<InfraClasse> infraClasse = infraClasseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(infraClasse);
    }

    /**
     * {@code DELETE  /infra-classes/:id} : delete the "id" infraClasse.
     *
     * @param id the id of the infraClasse to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/infra-classes/{id}")
    public ResponseEntity<Void> deleteInfraClasse(@PathVariable Long id) {
        log.debug("REST request to delete InfraClasse : {}", id);
        infraClasseService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
