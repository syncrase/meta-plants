package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.InfraOrdre;
import fr.syncrase.ecosyst.repository.InfraOrdreRepository;
import fr.syncrase.ecosyst.service.InfraOrdreQueryService;
import fr.syncrase.ecosyst.service.InfraOrdreService;
import fr.syncrase.ecosyst.service.criteria.InfraOrdreCriteria;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.InfraOrdre}.
 */
@RestController
@RequestMapping("/api")
public class InfraOrdreResource {

    private final Logger log = LoggerFactory.getLogger(InfraOrdreResource.class);

    private static final String ENTITY_NAME = "classificationMsInfraOrdre";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InfraOrdreService infraOrdreService;

    private final InfraOrdreRepository infraOrdreRepository;

    private final InfraOrdreQueryService infraOrdreQueryService;

    public InfraOrdreResource(
        InfraOrdreService infraOrdreService,
        InfraOrdreRepository infraOrdreRepository,
        InfraOrdreQueryService infraOrdreQueryService
    ) {
        this.infraOrdreService = infraOrdreService;
        this.infraOrdreRepository = infraOrdreRepository;
        this.infraOrdreQueryService = infraOrdreQueryService;
    }

    /**
     * {@code POST  /infra-ordres} : Create a new infraOrdre.
     *
     * @param infraOrdre the infraOrdre to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new infraOrdre, or with status {@code 400 (Bad Request)} if the infraOrdre has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/infra-ordres")
    public ResponseEntity<InfraOrdre> createInfraOrdre(@Valid @RequestBody InfraOrdre infraOrdre) throws URISyntaxException {
        log.debug("REST request to save InfraOrdre : {}", infraOrdre);
        if (infraOrdre.getId() != null) {
            throw new BadRequestAlertException("A new infraOrdre cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InfraOrdre result = infraOrdreService.save(infraOrdre);
        return ResponseEntity
            .created(new URI("/api/infra-ordres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /infra-ordres/:id} : Updates an existing infraOrdre.
     *
     * @param id the id of the infraOrdre to save.
     * @param infraOrdre the infraOrdre to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated infraOrdre,
     * or with status {@code 400 (Bad Request)} if the infraOrdre is not valid,
     * or with status {@code 500 (Internal Server Error)} if the infraOrdre couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/infra-ordres/{id}")
    public ResponseEntity<InfraOrdre> updateInfraOrdre(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InfraOrdre infraOrdre
    ) throws URISyntaxException {
        log.debug("REST request to update InfraOrdre : {}, {}", id, infraOrdre);
        if (infraOrdre.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, infraOrdre.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!infraOrdreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InfraOrdre result = infraOrdreService.save(infraOrdre);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, infraOrdre.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /infra-ordres/:id} : Partial updates given fields of an existing infraOrdre, field will ignore if it is null
     *
     * @param id the id of the infraOrdre to save.
     * @param infraOrdre the infraOrdre to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated infraOrdre,
     * or with status {@code 400 (Bad Request)} if the infraOrdre is not valid,
     * or with status {@code 404 (Not Found)} if the infraOrdre is not found,
     * or with status {@code 500 (Internal Server Error)} if the infraOrdre couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/infra-ordres/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InfraOrdre> partialUpdateInfraOrdre(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InfraOrdre infraOrdre
    ) throws URISyntaxException {
        log.debug("REST request to partial update InfraOrdre partially : {}, {}", id, infraOrdre);
        if (infraOrdre.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, infraOrdre.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!infraOrdreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InfraOrdre> result = infraOrdreService.partialUpdate(infraOrdre);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, infraOrdre.getId().toString())
        );
    }

    /**
     * {@code GET  /infra-ordres} : get all the infraOrdres.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of infraOrdres in body.
     */
    @GetMapping("/infra-ordres")
    public ResponseEntity<List<InfraOrdre>> getAllInfraOrdres(InfraOrdreCriteria criteria, Pageable pageable) {
        log.debug("REST request to get InfraOrdres by criteria: {}", criteria);
        Page<InfraOrdre> page = infraOrdreQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /infra-ordres/count} : count all the infraOrdres.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/infra-ordres/count")
    public ResponseEntity<Long> countInfraOrdres(InfraOrdreCriteria criteria) {
        log.debug("REST request to count InfraOrdres by criteria: {}", criteria);
        return ResponseEntity.ok().body(infraOrdreQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /infra-ordres/:id} : get the "id" infraOrdre.
     *
     * @param id the id of the infraOrdre to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the infraOrdre, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/infra-ordres/{id}")
    public ResponseEntity<InfraOrdre> getInfraOrdre(@PathVariable Long id) {
        log.debug("REST request to get InfraOrdre : {}", id);
        Optional<InfraOrdre> infraOrdre = infraOrdreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(infraOrdre);
    }

    /**
     * {@code DELETE  /infra-ordres/:id} : delete the "id" infraOrdre.
     *
     * @param id the id of the infraOrdre to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/infra-ordres/{id}")
    public ResponseEntity<Void> deleteInfraOrdre(@PathVariable Long id) {
        log.debug("REST request to delete InfraOrdre : {}", id);
        infraOrdreService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
