package fr.syncrase.perma.web.rest;

import fr.syncrase.perma.repository.StrateRepository;
import fr.syncrase.perma.service.StrateQueryService;
import fr.syncrase.perma.service.StrateService;
import fr.syncrase.perma.service.criteria.StrateCriteria;
import fr.syncrase.perma.service.dto.StrateDTO;
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
 * REST controller for managing {@link fr.syncrase.perma.domain.Strate}.
 */
@RestController
@RequestMapping("/api")
public class StrateResource {

    private final Logger log = LoggerFactory.getLogger(StrateResource.class);

    private static final String ENTITY_NAME = "microserviceStrate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StrateService strateService;

    private final StrateRepository strateRepository;

    private final StrateQueryService strateQueryService;

    public StrateResource(StrateService strateService, StrateRepository strateRepository, StrateQueryService strateQueryService) {
        this.strateService = strateService;
        this.strateRepository = strateRepository;
        this.strateQueryService = strateQueryService;
    }

    /**
     * {@code POST  /strates} : Create a new strate.
     *
     * @param strateDTO the strateDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new strateDTO, or with status {@code 400 (Bad Request)} if the strate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/strates")
    public ResponseEntity<StrateDTO> createStrate(@RequestBody StrateDTO strateDTO) throws URISyntaxException {
        log.debug("REST request to save Strate : {}", strateDTO);
        if (strateDTO.getId() != null) {
            throw new BadRequestAlertException("A new strate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StrateDTO result = strateService.save(strateDTO);
        return ResponseEntity
            .created(new URI("/api/strates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /strates/:id} : Updates an existing strate.
     *
     * @param id the id of the strateDTO to save.
     * @param strateDTO the strateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated strateDTO,
     * or with status {@code 400 (Bad Request)} if the strateDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the strateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/strates/{id}")
    public ResponseEntity<StrateDTO> updateStrate(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StrateDTO strateDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Strate : {}, {}", id, strateDTO);
        if (strateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, strateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!strateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StrateDTO result = strateService.save(strateDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, strateDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /strates/:id} : Partial updates given fields of an existing strate, field will ignore if it is null
     *
     * @param id the id of the strateDTO to save.
     * @param strateDTO the strateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated strateDTO,
     * or with status {@code 400 (Bad Request)} if the strateDTO is not valid,
     * or with status {@code 404 (Not Found)} if the strateDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the strateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/strates/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StrateDTO> partialUpdateStrate(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StrateDTO strateDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Strate partially : {}, {}", id, strateDTO);
        if (strateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, strateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!strateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StrateDTO> result = strateService.partialUpdate(strateDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, strateDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /strates} : get all the strates.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of strates in body.
     */
    @GetMapping("/strates")
    public ResponseEntity<List<StrateDTO>> getAllStrates(StrateCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Strates by criteria: {}", criteria);
        Page<StrateDTO> page = strateQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /strates/count} : count all the strates.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/strates/count")
    public ResponseEntity<Long> countStrates(StrateCriteria criteria) {
        log.debug("REST request to count Strates by criteria: {}", criteria);
        return ResponseEntity.ok().body(strateQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /strates/:id} : get the "id" strate.
     *
     * @param id the id of the strateDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the strateDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/strates/{id}")
    public ResponseEntity<StrateDTO> getStrate(@PathVariable Long id) {
        log.debug("REST request to get Strate : {}", id);
        Optional<StrateDTO> strateDTO = strateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(strateDTO);
    }

    /**
     * {@code DELETE  /strates/:id} : delete the "id" strate.
     *
     * @param id the id of the strateDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/strates/{id}")
    public ResponseEntity<Void> deleteStrate(@PathVariable Long id) {
        log.debug("REST request to delete Strate : {}", id);
        strateService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
