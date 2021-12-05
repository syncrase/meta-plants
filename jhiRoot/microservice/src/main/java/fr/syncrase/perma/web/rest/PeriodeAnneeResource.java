package fr.syncrase.perma.web.rest;

import fr.syncrase.perma.repository.PeriodeAnneeRepository;
import fr.syncrase.perma.service.PeriodeAnneeQueryService;
import fr.syncrase.perma.service.PeriodeAnneeService;
import fr.syncrase.perma.service.criteria.PeriodeAnneeCriteria;
import fr.syncrase.perma.service.dto.PeriodeAnneeDTO;
import fr.syncrase.perma.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link fr.syncrase.perma.domain.PeriodeAnnee}.
 */
@RestController
@RequestMapping("/api")
public class PeriodeAnneeResource {

    private final Logger log = LoggerFactory.getLogger(PeriodeAnneeResource.class);

    private static final String ENTITY_NAME = "microservicePeriodeAnnee";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PeriodeAnneeService periodeAnneeService;

    private final PeriodeAnneeRepository periodeAnneeRepository;

    private final PeriodeAnneeQueryService periodeAnneeQueryService;

    public PeriodeAnneeResource(
        PeriodeAnneeService periodeAnneeService,
        PeriodeAnneeRepository periodeAnneeRepository,
        PeriodeAnneeQueryService periodeAnneeQueryService
    ) {
        this.periodeAnneeService = periodeAnneeService;
        this.periodeAnneeRepository = periodeAnneeRepository;
        this.periodeAnneeQueryService = periodeAnneeQueryService;
    }

    /**
     * {@code POST  /periode-annees} : Create a new periodeAnnee.
     *
     * @param periodeAnneeDTO the periodeAnneeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new periodeAnneeDTO, or with status {@code 400 (Bad Request)} if the periodeAnnee has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/periode-annees")
    public ResponseEntity<PeriodeAnneeDTO> createPeriodeAnnee(@Valid @RequestBody PeriodeAnneeDTO periodeAnneeDTO)
        throws URISyntaxException {
        log.debug("REST request to save PeriodeAnnee : {}", periodeAnneeDTO);
        if (periodeAnneeDTO.getId() != null) {
            throw new BadRequestAlertException("A new periodeAnnee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PeriodeAnneeDTO result = periodeAnneeService.save(periodeAnneeDTO);
        return ResponseEntity
            .created(new URI("/api/periode-annees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /periode-annees/:id} : Updates an existing periodeAnnee.
     *
     * @param id the id of the periodeAnneeDTO to save.
     * @param periodeAnneeDTO the periodeAnneeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated periodeAnneeDTO,
     * or with status {@code 400 (Bad Request)} if the periodeAnneeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the periodeAnneeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/periode-annees/{id}")
    public ResponseEntity<PeriodeAnneeDTO> updatePeriodeAnnee(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PeriodeAnneeDTO periodeAnneeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PeriodeAnnee : {}, {}", id, periodeAnneeDTO);
        if (periodeAnneeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, periodeAnneeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!periodeAnneeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PeriodeAnneeDTO result = periodeAnneeService.save(periodeAnneeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, periodeAnneeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /periode-annees/:id} : Partial updates given fields of an existing periodeAnnee, field will ignore if it is null
     *
     * @param id the id of the periodeAnneeDTO to save.
     * @param periodeAnneeDTO the periodeAnneeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated periodeAnneeDTO,
     * or with status {@code 400 (Bad Request)} if the periodeAnneeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the periodeAnneeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the periodeAnneeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/periode-annees/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PeriodeAnneeDTO> partialUpdatePeriodeAnnee(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PeriodeAnneeDTO periodeAnneeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PeriodeAnnee partially : {}, {}", id, periodeAnneeDTO);
        if (periodeAnneeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, periodeAnneeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!periodeAnneeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PeriodeAnneeDTO> result = periodeAnneeService.partialUpdate(periodeAnneeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, periodeAnneeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /periode-annees} : get all the periodeAnnees.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of periodeAnnees in body.
     */
    @GetMapping("/periode-annees")
    public ResponseEntity<List<PeriodeAnneeDTO>> getAllPeriodeAnnees(PeriodeAnneeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PeriodeAnnees by criteria: {}", criteria);
        Page<PeriodeAnneeDTO> page = periodeAnneeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /periode-annees/count} : count all the periodeAnnees.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/periode-annees/count")
    public ResponseEntity<Long> countPeriodeAnnees(PeriodeAnneeCriteria criteria) {
        log.debug("REST request to count PeriodeAnnees by criteria: {}", criteria);
        return ResponseEntity.ok().body(periodeAnneeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /periode-annees/:id} : get the "id" periodeAnnee.
     *
     * @param id the id of the periodeAnneeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the periodeAnneeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/periode-annees/{id}")
    public ResponseEntity<PeriodeAnneeDTO> getPeriodeAnnee(@PathVariable Long id) {
        log.debug("REST request to get PeriodeAnnee : {}", id);
        Optional<PeriodeAnneeDTO> periodeAnneeDTO = periodeAnneeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(periodeAnneeDTO);
    }

    /**
     * {@code DELETE  /periode-annees/:id} : delete the "id" periodeAnnee.
     *
     * @param id the id of the periodeAnneeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/periode-annees/{id}")
    public ResponseEntity<Void> deletePeriodeAnnee(@PathVariable Long id) {
        log.debug("REST request to delete PeriodeAnnee : {}", id);
        periodeAnneeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
