package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.InfraRegne;
import fr.syncrase.ecosyst.repository.InfraRegneRepository;
import fr.syncrase.ecosyst.service.InfraRegneQueryService;
import fr.syncrase.ecosyst.service.InfraRegneService;
import fr.syncrase.ecosyst.service.criteria.InfraRegneCriteria;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.InfraRegne}.
 */
@RestController
@RequestMapping("/api")
public class InfraRegneResource {

    private final Logger log = LoggerFactory.getLogger(InfraRegneResource.class);

    private static final String ENTITY_NAME = "classificationMsInfraRegne";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InfraRegneService infraRegneService;

    private final InfraRegneRepository infraRegneRepository;

    private final InfraRegneQueryService infraRegneQueryService;

    public InfraRegneResource(
        InfraRegneService infraRegneService,
        InfraRegneRepository infraRegneRepository,
        InfraRegneQueryService infraRegneQueryService
    ) {
        this.infraRegneService = infraRegneService;
        this.infraRegneRepository = infraRegneRepository;
        this.infraRegneQueryService = infraRegneQueryService;
    }

    /**
     * {@code POST  /infra-regnes} : Create a new infraRegne.
     *
     * @param infraRegne the infraRegne to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new infraRegne, or with status {@code 400 (Bad Request)} if the infraRegne has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/infra-regnes")
    public ResponseEntity<InfraRegne> createInfraRegne(@Valid @RequestBody InfraRegne infraRegne) throws URISyntaxException {
        log.debug("REST request to save InfraRegne : {}", infraRegne);
        if (infraRegne.getId() != null) {
            throw new BadRequestAlertException("A new infraRegne cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InfraRegne result = infraRegneService.save(infraRegne);
        return ResponseEntity
            .created(new URI("/api/infra-regnes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /infra-regnes/:id} : Updates an existing infraRegne.
     *
     * @param id the id of the infraRegne to save.
     * @param infraRegne the infraRegne to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated infraRegne,
     * or with status {@code 400 (Bad Request)} if the infraRegne is not valid,
     * or with status {@code 500 (Internal Server Error)} if the infraRegne couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/infra-regnes/{id}")
    public ResponseEntity<InfraRegne> updateInfraRegne(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InfraRegne infraRegne
    ) throws URISyntaxException {
        log.debug("REST request to update InfraRegne : {}, {}", id, infraRegne);
        if (infraRegne.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, infraRegne.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!infraRegneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InfraRegne result = infraRegneService.save(infraRegne);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, infraRegne.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /infra-regnes/:id} : Partial updates given fields of an existing infraRegne, field will ignore if it is null
     *
     * @param id the id of the infraRegne to save.
     * @param infraRegne the infraRegne to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated infraRegne,
     * or with status {@code 400 (Bad Request)} if the infraRegne is not valid,
     * or with status {@code 404 (Not Found)} if the infraRegne is not found,
     * or with status {@code 500 (Internal Server Error)} if the infraRegne couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/infra-regnes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InfraRegne> partialUpdateInfraRegne(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InfraRegne infraRegne
    ) throws URISyntaxException {
        log.debug("REST request to partial update InfraRegne partially : {}, {}", id, infraRegne);
        if (infraRegne.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, infraRegne.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!infraRegneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InfraRegne> result = infraRegneService.partialUpdate(infraRegne);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, infraRegne.getId().toString())
        );
    }

    /**
     * {@code GET  /infra-regnes} : get all the infraRegnes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of infraRegnes in body.
     */
    @GetMapping("/infra-regnes")
    public ResponseEntity<List<InfraRegne>> getAllInfraRegnes(InfraRegneCriteria criteria, Pageable pageable) {
        log.debug("REST request to get InfraRegnes by criteria: {}", criteria);
        Page<InfraRegne> page = infraRegneQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /infra-regnes/count} : count all the infraRegnes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/infra-regnes/count")
    public ResponseEntity<Long> countInfraRegnes(InfraRegneCriteria criteria) {
        log.debug("REST request to count InfraRegnes by criteria: {}", criteria);
        return ResponseEntity.ok().body(infraRegneQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /infra-regnes/:id} : get the "id" infraRegne.
     *
     * @param id the id of the infraRegne to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the infraRegne, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/infra-regnes/{id}")
    public ResponseEntity<InfraRegne> getInfraRegne(@PathVariable Long id) {
        log.debug("REST request to get InfraRegne : {}", id);
        Optional<InfraRegne> infraRegne = infraRegneService.findOne(id);
        return ResponseUtil.wrapOrNotFound(infraRegne);
    }

    /**
     * {@code DELETE  /infra-regnes/:id} : delete the "id" infraRegne.
     *
     * @param id the id of the infraRegne to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/infra-regnes/{id}")
    public ResponseEntity<Void> deleteInfraRegne(@PathVariable Long id) {
        log.debug("REST request to delete InfraRegne : {}", id);
        infraRegneService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
