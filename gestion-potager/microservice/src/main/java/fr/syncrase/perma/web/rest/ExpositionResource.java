package fr.syncrase.perma.web.rest;

import fr.syncrase.perma.repository.ExpositionRepository;
import fr.syncrase.perma.service.ExpositionQueryService;
import fr.syncrase.perma.service.ExpositionService;
import fr.syncrase.perma.service.criteria.ExpositionCriteria;
import fr.syncrase.perma.service.dto.ExpositionDTO;
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
 * REST controller for managing {@link fr.syncrase.perma.domain.Exposition}.
 */
@RestController
@RequestMapping("/api")
public class ExpositionResource {

    private final Logger log = LoggerFactory.getLogger(ExpositionResource.class);

    private static final String ENTITY_NAME = "microserviceExposition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExpositionService expositionService;

    private final ExpositionRepository expositionRepository;

    private final ExpositionQueryService expositionQueryService;

    public ExpositionResource(
        ExpositionService expositionService,
        ExpositionRepository expositionRepository,
        ExpositionQueryService expositionQueryService
    ) {
        this.expositionService = expositionService;
        this.expositionRepository = expositionRepository;
        this.expositionQueryService = expositionQueryService;
    }

    /**
     * {@code POST  /expositions} : Create a new exposition.
     *
     * @param expositionDTO the expositionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new expositionDTO, or with status {@code 400 (Bad Request)} if the exposition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/expositions")
    public ResponseEntity<ExpositionDTO> createExposition(@RequestBody ExpositionDTO expositionDTO) throws URISyntaxException {
        log.debug("REST request to save Exposition : {}", expositionDTO);
        if (expositionDTO.getId() != null) {
            throw new BadRequestAlertException("A new exposition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExpositionDTO result = expositionService.save(expositionDTO);
        return ResponseEntity
            .created(new URI("/api/expositions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /expositions/:id} : Updates an existing exposition.
     *
     * @param id the id of the expositionDTO to save.
     * @param expositionDTO the expositionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated expositionDTO,
     * or with status {@code 400 (Bad Request)} if the expositionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the expositionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/expositions/{id}")
    public ResponseEntity<ExpositionDTO> updateExposition(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ExpositionDTO expositionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Exposition : {}, {}", id, expositionDTO);
        if (expositionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, expositionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!expositionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ExpositionDTO result = expositionService.save(expositionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, expositionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /expositions/:id} : Partial updates given fields of an existing exposition, field will ignore if it is null
     *
     * @param id the id of the expositionDTO to save.
     * @param expositionDTO the expositionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated expositionDTO,
     * or with status {@code 400 (Bad Request)} if the expositionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the expositionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the expositionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/expositions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExpositionDTO> partialUpdateExposition(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ExpositionDTO expositionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Exposition partially : {}, {}", id, expositionDTO);
        if (expositionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, expositionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!expositionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExpositionDTO> result = expositionService.partialUpdate(expositionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, expositionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /expositions} : get all the expositions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of expositions in body.
     */
    @GetMapping("/expositions")
    public ResponseEntity<List<ExpositionDTO>> getAllExpositions(ExpositionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Expositions by criteria: {}", criteria);
        Page<ExpositionDTO> page = expositionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /expositions/count} : count all the expositions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/expositions/count")
    public ResponseEntity<Long> countExpositions(ExpositionCriteria criteria) {
        log.debug("REST request to count Expositions by criteria: {}", criteria);
        return ResponseEntity.ok().body(expositionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /expositions/:id} : get the "id" exposition.
     *
     * @param id the id of the expositionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the expositionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/expositions/{id}")
    public ResponseEntity<ExpositionDTO> getExposition(@PathVariable Long id) {
        log.debug("REST request to get Exposition : {}", id);
        Optional<ExpositionDTO> expositionDTO = expositionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(expositionDTO);
    }

    /**
     * {@code DELETE  /expositions/:id} : delete the "id" exposition.
     *
     * @param id the id of the expositionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/expositions/{id}")
    public ResponseEntity<Void> deleteExposition(@PathVariable Long id) {
        log.debug("REST request to delete Exposition : {}", id);
        expositionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
