package fr.syncrase.perma.web.rest;

import fr.syncrase.perma.repository.GerminationRepository;
import fr.syncrase.perma.service.GerminationQueryService;
import fr.syncrase.perma.service.GerminationService;
import fr.syncrase.perma.service.criteria.GerminationCriteria;
import fr.syncrase.perma.service.dto.GerminationDTO;
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
 * REST controller for managing {@link fr.syncrase.perma.domain.Germination}.
 */
@RestController
@RequestMapping("/api")
public class GerminationResource {

    private final Logger log = LoggerFactory.getLogger(GerminationResource.class);

    private static final String ENTITY_NAME = "microserviceGermination";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GerminationService germinationService;

    private final GerminationRepository germinationRepository;

    private final GerminationQueryService germinationQueryService;

    public GerminationResource(
        GerminationService germinationService,
        GerminationRepository germinationRepository,
        GerminationQueryService germinationQueryService
    ) {
        this.germinationService = germinationService;
        this.germinationRepository = germinationRepository;
        this.germinationQueryService = germinationQueryService;
    }

    /**
     * {@code POST  /germinations} : Create a new germination.
     *
     * @param germinationDTO the germinationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new germinationDTO, or with status {@code 400 (Bad Request)} if the germination has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/germinations")
    public ResponseEntity<GerminationDTO> createGermination(@RequestBody GerminationDTO germinationDTO) throws URISyntaxException {
        log.debug("REST request to save Germination : {}", germinationDTO);
        if (germinationDTO.getId() != null) {
            throw new BadRequestAlertException("A new germination cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GerminationDTO result = germinationService.save(germinationDTO);
        return ResponseEntity
            .created(new URI("/api/germinations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /germinations/:id} : Updates an existing germination.
     *
     * @param id the id of the germinationDTO to save.
     * @param germinationDTO the germinationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated germinationDTO,
     * or with status {@code 400 (Bad Request)} if the germinationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the germinationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/germinations/{id}")
    public ResponseEntity<GerminationDTO> updateGermination(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GerminationDTO germinationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Germination : {}, {}", id, germinationDTO);
        if (germinationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, germinationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!germinationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GerminationDTO result = germinationService.save(germinationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, germinationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /germinations/:id} : Partial updates given fields of an existing germination, field will ignore if it is null
     *
     * @param id the id of the germinationDTO to save.
     * @param germinationDTO the germinationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated germinationDTO,
     * or with status {@code 400 (Bad Request)} if the germinationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the germinationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the germinationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/germinations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GerminationDTO> partialUpdateGermination(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GerminationDTO germinationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Germination partially : {}, {}", id, germinationDTO);
        if (germinationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, germinationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!germinationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GerminationDTO> result = germinationService.partialUpdate(germinationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, germinationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /germinations} : get all the germinations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of germinations in body.
     */
    @GetMapping("/germinations")
    public ResponseEntity<List<GerminationDTO>> getAllGerminations(GerminationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Germinations by criteria: {}", criteria);
        Page<GerminationDTO> page = germinationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /germinations/count} : count all the germinations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/germinations/count")
    public ResponseEntity<Long> countGerminations(GerminationCriteria criteria) {
        log.debug("REST request to count Germinations by criteria: {}", criteria);
        return ResponseEntity.ok().body(germinationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /germinations/:id} : get the "id" germination.
     *
     * @param id the id of the germinationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the germinationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/germinations/{id}")
    public ResponseEntity<GerminationDTO> getGermination(@PathVariable Long id) {
        log.debug("REST request to get Germination : {}", id);
        Optional<GerminationDTO> germinationDTO = germinationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(germinationDTO);
    }

    /**
     * {@code DELETE  /germinations/:id} : delete the "id" germination.
     *
     * @param id the id of the germinationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/germinations/{id}")
    public ResponseEntity<Void> deleteGermination(@PathVariable Long id) {
        log.debug("REST request to delete Germination : {}", id);
        germinationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
