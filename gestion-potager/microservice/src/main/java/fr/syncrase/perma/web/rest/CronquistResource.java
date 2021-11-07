package fr.syncrase.perma.web.rest;

import fr.syncrase.perma.repository.CronquistRepository;
import fr.syncrase.perma.service.CronquistQueryService;
import fr.syncrase.perma.service.CronquistService;
import fr.syncrase.perma.service.criteria.CronquistCriteria;
import fr.syncrase.perma.service.dto.CronquistDTO;
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
 * REST controller for managing {@link fr.syncrase.perma.domain.Cronquist}.
 */
@RestController
@RequestMapping("/api")
public class CronquistResource {

    private final Logger log = LoggerFactory.getLogger(CronquistResource.class);

    private static final String ENTITY_NAME = "microserviceCronquist";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CronquistService cronquistService;

    private final CronquistRepository cronquistRepository;

    private final CronquistQueryService cronquistQueryService;

    public CronquistResource(
        CronquistService cronquistService,
        CronquistRepository cronquistRepository,
        CronquistQueryService cronquistQueryService
    ) {
        this.cronquistService = cronquistService;
        this.cronquistRepository = cronquistRepository;
        this.cronquistQueryService = cronquistQueryService;
    }

    /**
     * {@code POST  /cronquists} : Create a new cronquist.
     *
     * @param cronquistDTO the cronquistDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cronquistDTO, or with status {@code 400 (Bad Request)} if the cronquist has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cronquists")
    public ResponseEntity<CronquistDTO> createCronquist(@RequestBody CronquistDTO cronquistDTO) throws URISyntaxException {
        log.debug("REST request to save Cronquist : {}", cronquistDTO);
        if (cronquistDTO.getId() != null) {
            throw new BadRequestAlertException("A new cronquist cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CronquistDTO result = cronquistService.save(cronquistDTO);
        return ResponseEntity
            .created(new URI("/api/cronquists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cronquists/:id} : Updates an existing cronquist.
     *
     * @param id the id of the cronquistDTO to save.
     * @param cronquistDTO the cronquistDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cronquistDTO,
     * or with status {@code 400 (Bad Request)} if the cronquistDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cronquistDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cronquists/{id}")
    public ResponseEntity<CronquistDTO> updateCronquist(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CronquistDTO cronquistDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Cronquist : {}, {}", id, cronquistDTO);
        if (cronquistDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cronquistDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cronquistRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CronquistDTO result = cronquistService.save(cronquistDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cronquistDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cronquists/:id} : Partial updates given fields of an existing cronquist, field will ignore if it is null
     *
     * @param id the id of the cronquistDTO to save.
     * @param cronquistDTO the cronquistDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cronquistDTO,
     * or with status {@code 400 (Bad Request)} if the cronquistDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cronquistDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cronquistDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cronquists/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CronquistDTO> partialUpdateCronquist(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CronquistDTO cronquistDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Cronquist partially : {}, {}", id, cronquistDTO);
        if (cronquistDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cronquistDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cronquistRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CronquistDTO> result = cronquistService.partialUpdate(cronquistDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cronquistDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cronquists} : get all the cronquists.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cronquists in body.
     */
    @GetMapping("/cronquists")
    public ResponseEntity<List<CronquistDTO>> getAllCronquists(CronquistCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Cronquists by criteria: {}", criteria);
        Page<CronquistDTO> page = cronquistQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cronquists/count} : count all the cronquists.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/cronquists/count")
    public ResponseEntity<Long> countCronquists(CronquistCriteria criteria) {
        log.debug("REST request to count Cronquists by criteria: {}", criteria);
        return ResponseEntity.ok().body(cronquistQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cronquists/:id} : get the "id" cronquist.
     *
     * @param id the id of the cronquistDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cronquistDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cronquists/{id}")
    public ResponseEntity<CronquistDTO> getCronquist(@PathVariable Long id) {
        log.debug("REST request to get Cronquist : {}", id);
        Optional<CronquistDTO> cronquistDTO = cronquistService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cronquistDTO);
    }

    /**
     * {@code DELETE  /cronquists/:id} : delete the "id" cronquist.
     *
     * @param id the id of the cronquistDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cronquists/{id}")
    public ResponseEntity<Void> deleteCronquist(@PathVariable Long id) {
        log.debug("REST request to delete Cronquist : {}", id);
        cronquistService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
