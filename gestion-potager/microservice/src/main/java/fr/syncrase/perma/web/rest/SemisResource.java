package fr.syncrase.perma.web.rest;

import fr.syncrase.perma.service.SemisService;
import fr.syncrase.perma.web.rest.errors.BadRequestAlertException;
import fr.syncrase.perma.service.dto.SemisDTO;
import fr.syncrase.perma.service.dto.SemisCriteria;
import fr.syncrase.perma.service.SemisQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link fr.syncrase.perma.domain.Semis}.
 */
@RestController
@RequestMapping("/api")
public class SemisResource {

    private final Logger log = LoggerFactory.getLogger(SemisResource.class);

    private static final String ENTITY_NAME = "microserviceSemis";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SemisService semisService;

    private final SemisQueryService semisQueryService;

    public SemisResource(SemisService semisService, SemisQueryService semisQueryService) {
        this.semisService = semisService;
        this.semisQueryService = semisQueryService;
    }

    /**
     * {@code POST  /semis} : Create a new semis.
     *
     * @param semisDTO the semisDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new semisDTO, or with status {@code 400 (Bad Request)} if the semis has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/semis")
    public ResponseEntity<SemisDTO> createSemis(@RequestBody SemisDTO semisDTO) throws URISyntaxException {
        log.debug("REST request to save Semis : {}", semisDTO);
        if (semisDTO.getId() != null) {
            throw new BadRequestAlertException("A new semis cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SemisDTO result = semisService.save(semisDTO);
        return ResponseEntity.created(new URI("/api/semis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /semis} : Updates an existing semis.
     *
     * @param semisDTO the semisDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated semisDTO,
     * or with status {@code 400 (Bad Request)} if the semisDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the semisDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/semis")
    public ResponseEntity<SemisDTO> updateSemis(@RequestBody SemisDTO semisDTO) throws URISyntaxException {
        log.debug("REST request to update Semis : {}", semisDTO);
        if (semisDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SemisDTO result = semisService.save(semisDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, semisDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /semis} : get all the semis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of semis in body.
     */
    @GetMapping("/semis")
    public ResponseEntity<List<SemisDTO>> getAllSemis(SemisCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Semis by criteria: {}", criteria);
        Page<SemisDTO> page = semisQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /semis/count} : count all the semis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/semis/count")
    public ResponseEntity<Long> countSemis(SemisCriteria criteria) {
        log.debug("REST request to count Semis by criteria: {}", criteria);
        return ResponseEntity.ok().body(semisQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /semis/:id} : get the "id" semis.
     *
     * @param id the id of the semisDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the semisDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/semis/{id}")
    public ResponseEntity<SemisDTO> getSemis(@PathVariable Long id) {
        log.debug("REST request to get Semis : {}", id);
        Optional<SemisDTO> semisDTO = semisService.findOne(id);
        return ResponseUtil.wrapOrNotFound(semisDTO);
    }

    /**
     * {@code DELETE  /semis/:id} : delete the "id" semis.
     *
     * @param id the id of the semisDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/semis/{id}")
    public ResponseEntity<Void> deleteSemis(@PathVariable Long id) {
        log.debug("REST request to delete Semis : {}", id);
        semisService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}