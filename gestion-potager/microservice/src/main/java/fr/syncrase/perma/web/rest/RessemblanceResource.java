package fr.syncrase.perma.web.rest;

import fr.syncrase.perma.repository.RessemblanceRepository;
import fr.syncrase.perma.service.RessemblanceQueryService;
import fr.syncrase.perma.service.RessemblanceService;
import fr.syncrase.perma.service.criteria.RessemblanceCriteria;
import fr.syncrase.perma.service.dto.RessemblanceDTO;
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
 * REST controller for managing {@link fr.syncrase.perma.domain.Ressemblance}.
 */
@RestController
@RequestMapping("/api")
public class RessemblanceResource {

    private final Logger log = LoggerFactory.getLogger(RessemblanceResource.class);

    private static final String ENTITY_NAME = "microserviceRessemblance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RessemblanceService ressemblanceService;

    private final RessemblanceRepository ressemblanceRepository;

    private final RessemblanceQueryService ressemblanceQueryService;

    public RessemblanceResource(
        RessemblanceService ressemblanceService,
        RessemblanceRepository ressemblanceRepository,
        RessemblanceQueryService ressemblanceQueryService
    ) {
        this.ressemblanceService = ressemblanceService;
        this.ressemblanceRepository = ressemblanceRepository;
        this.ressemblanceQueryService = ressemblanceQueryService;
    }

    /**
     * {@code POST  /ressemblances} : Create a new ressemblance.
     *
     * @param ressemblanceDTO the ressemblanceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ressemblanceDTO, or with status {@code 400 (Bad Request)} if the ressemblance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ressemblances")
    public ResponseEntity<RessemblanceDTO> createRessemblance(@RequestBody RessemblanceDTO ressemblanceDTO) throws URISyntaxException {
        log.debug("REST request to save Ressemblance : {}", ressemblanceDTO);
        if (ressemblanceDTO.getId() != null) {
            throw new BadRequestAlertException("A new ressemblance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RessemblanceDTO result = ressemblanceService.save(ressemblanceDTO);
        return ResponseEntity
            .created(new URI("/api/ressemblances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ressemblances/:id} : Updates an existing ressemblance.
     *
     * @param id the id of the ressemblanceDTO to save.
     * @param ressemblanceDTO the ressemblanceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ressemblanceDTO,
     * or with status {@code 400 (Bad Request)} if the ressemblanceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ressemblanceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ressemblances/{id}")
    public ResponseEntity<RessemblanceDTO> updateRessemblance(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RessemblanceDTO ressemblanceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Ressemblance : {}, {}", id, ressemblanceDTO);
        if (ressemblanceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ressemblanceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ressemblanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RessemblanceDTO result = ressemblanceService.save(ressemblanceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ressemblanceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ressemblances/:id} : Partial updates given fields of an existing ressemblance, field will ignore if it is null
     *
     * @param id the id of the ressemblanceDTO to save.
     * @param ressemblanceDTO the ressemblanceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ressemblanceDTO,
     * or with status {@code 400 (Bad Request)} if the ressemblanceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ressemblanceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ressemblanceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ressemblances/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RessemblanceDTO> partialUpdateRessemblance(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RessemblanceDTO ressemblanceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ressemblance partially : {}, {}", id, ressemblanceDTO);
        if (ressemblanceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ressemblanceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ressemblanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RessemblanceDTO> result = ressemblanceService.partialUpdate(ressemblanceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ressemblanceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ressemblances} : get all the ressemblances.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ressemblances in body.
     */
    @GetMapping("/ressemblances")
    public ResponseEntity<List<RessemblanceDTO>> getAllRessemblances(RessemblanceCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Ressemblances by criteria: {}", criteria);
        Page<RessemblanceDTO> page = ressemblanceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ressemblances/count} : count all the ressemblances.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/ressemblances/count")
    public ResponseEntity<Long> countRessemblances(RessemblanceCriteria criteria) {
        log.debug("REST request to count Ressemblances by criteria: {}", criteria);
        return ResponseEntity.ok().body(ressemblanceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ressemblances/:id} : get the "id" ressemblance.
     *
     * @param id the id of the ressemblanceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ressemblanceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ressemblances/{id}")
    public ResponseEntity<RessemblanceDTO> getRessemblance(@PathVariable Long id) {
        log.debug("REST request to get Ressemblance : {}", id);
        Optional<RessemblanceDTO> ressemblanceDTO = ressemblanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ressemblanceDTO);
    }

    /**
     * {@code DELETE  /ressemblances/:id} : delete the "id" ressemblance.
     *
     * @param id the id of the ressemblanceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ressemblances/{id}")
    public ResponseEntity<Void> deleteRessemblance(@PathVariable Long id) {
        log.debug("REST request to delete Ressemblance : {}", id);
        ressemblanceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
