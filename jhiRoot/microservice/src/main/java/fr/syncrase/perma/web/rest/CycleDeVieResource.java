package fr.syncrase.perma.web.rest;

import fr.syncrase.perma.repository.CycleDeVieRepository;
import fr.syncrase.perma.service.CycleDeVieQueryService;
import fr.syncrase.perma.service.CycleDeVieService;
import fr.syncrase.perma.service.criteria.CycleDeVieCriteria;
import fr.syncrase.perma.service.dto.CycleDeVieDTO;
import fr.syncrase.perma.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link fr.syncrase.perma.domain.CycleDeVie}.
 */
@RestController
@RequestMapping("/api")
public class CycleDeVieResource {

    private final Logger log = LoggerFactory.getLogger(CycleDeVieResource.class);

    private static final String ENTITY_NAME = "microserviceCycleDeVie";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CycleDeVieService cycleDeVieService;

    private final CycleDeVieRepository cycleDeVieRepository;

    private final CycleDeVieQueryService cycleDeVieQueryService;

    public CycleDeVieResource(
        CycleDeVieService cycleDeVieService,
        CycleDeVieRepository cycleDeVieRepository,
        CycleDeVieQueryService cycleDeVieQueryService
    ) {
        this.cycleDeVieService = cycleDeVieService;
        this.cycleDeVieRepository = cycleDeVieRepository;
        this.cycleDeVieQueryService = cycleDeVieQueryService;
    }

    /**
     * {@code POST  /cycle-de-vies} : Create a new cycleDeVie.
     *
     * @param cycleDeVieDTO the cycleDeVieDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cycleDeVieDTO, or with status {@code 400 (Bad Request)} if the cycleDeVie has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cycle-de-vies")
    public ResponseEntity<CycleDeVieDTO> createCycleDeVie(@RequestBody CycleDeVieDTO cycleDeVieDTO) throws URISyntaxException {
        log.debug("REST request to save CycleDeVie : {}", cycleDeVieDTO);
        if (cycleDeVieDTO.getId() != null) {
            throw new BadRequestAlertException("A new cycleDeVie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CycleDeVieDTO result = cycleDeVieService.save(cycleDeVieDTO);
        return ResponseEntity
            .created(new URI("/api/cycle-de-vies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cycle-de-vies/:id} : Updates an existing cycleDeVie.
     *
     * @param id the id of the cycleDeVieDTO to save.
     * @param cycleDeVieDTO the cycleDeVieDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cycleDeVieDTO,
     * or with status {@code 400 (Bad Request)} if the cycleDeVieDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cycleDeVieDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cycle-de-vies/{id}")
    public ResponseEntity<CycleDeVieDTO> updateCycleDeVie(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CycleDeVieDTO cycleDeVieDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CycleDeVie : {}, {}", id, cycleDeVieDTO);
        if (cycleDeVieDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cycleDeVieDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cycleDeVieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CycleDeVieDTO result = cycleDeVieService.save(cycleDeVieDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cycleDeVieDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cycle-de-vies/:id} : Partial updates given fields of an existing cycleDeVie, field will ignore if it is null
     *
     * @param id the id of the cycleDeVieDTO to save.
     * @param cycleDeVieDTO the cycleDeVieDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cycleDeVieDTO,
     * or with status {@code 400 (Bad Request)} if the cycleDeVieDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cycleDeVieDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cycleDeVieDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cycle-de-vies/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CycleDeVieDTO> partialUpdateCycleDeVie(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CycleDeVieDTO cycleDeVieDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CycleDeVie partially : {}, {}", id, cycleDeVieDTO);
        if (cycleDeVieDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cycleDeVieDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cycleDeVieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CycleDeVieDTO> result = cycleDeVieService.partialUpdate(cycleDeVieDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cycleDeVieDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cycle-de-vies} : get all the cycleDeVies.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cycleDeVies in body.
     */
    @GetMapping("/cycle-de-vies")
    public ResponseEntity<List<CycleDeVieDTO>> getAllCycleDeVies(CycleDeVieCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CycleDeVies by criteria: {}", criteria);
        Page<CycleDeVieDTO> page = cycleDeVieQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cycle-de-vies/count} : count all the cycleDeVies.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/cycle-de-vies/count")
    public ResponseEntity<Long> countCycleDeVies(CycleDeVieCriteria criteria) {
        log.debug("REST request to count CycleDeVies by criteria: {}", criteria);
        return ResponseEntity.ok().body(cycleDeVieQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cycle-de-vies/:id} : get the "id" cycleDeVie.
     *
     * @param id the id of the cycleDeVieDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cycleDeVieDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cycle-de-vies/{id}")
    public ResponseEntity<CycleDeVieDTO> getCycleDeVie(@PathVariable Long id) {
        log.debug("REST request to get CycleDeVie : {}", id);
        Optional<CycleDeVieDTO> cycleDeVieDTO = cycleDeVieService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cycleDeVieDTO);
    }

    /**
     * {@code DELETE  /cycle-de-vies/:id} : delete the "id" cycleDeVie.
     *
     * @param id the id of the cycleDeVieDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cycle-de-vies/{id}")
    public ResponseEntity<Void> deleteCycleDeVie(@PathVariable Long id) {
        log.debug("REST request to delete CycleDeVie : {}", id);
        cycleDeVieService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
