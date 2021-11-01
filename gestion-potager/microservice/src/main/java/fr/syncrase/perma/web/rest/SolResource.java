package fr.syncrase.perma.web.rest;

import fr.syncrase.perma.service.SolService;
import fr.syncrase.perma.web.rest.errors.BadRequestAlertException;
import fr.syncrase.perma.service.dto.SolDTO;
import fr.syncrase.perma.service.dto.SolCriteria;
import fr.syncrase.perma.service.SolQueryService;

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
 * REST controller for managing {@link fr.syncrase.perma.domain.Sol}.
 */
@RestController
@RequestMapping("/api")
public class SolResource {

    private final Logger log = LoggerFactory.getLogger(SolResource.class);

    private static final String ENTITY_NAME = "microserviceSol";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SolService solService;

    private final SolQueryService solQueryService;

    public SolResource(SolService solService, SolQueryService solQueryService) {
        this.solService = solService;
        this.solQueryService = solQueryService;
    }

    /**
     * {@code POST  /sols} : Create a new sol.
     *
     * @param solDTO the solDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new solDTO, or with status {@code 400 (Bad Request)} if the sol has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sols")
    public ResponseEntity<SolDTO> createSol(@RequestBody SolDTO solDTO) throws URISyntaxException {
        log.debug("REST request to save Sol : {}", solDTO);
        if (solDTO.getId() != null) {
            throw new BadRequestAlertException("A new sol cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SolDTO result = solService.save(solDTO);
        return ResponseEntity.created(new URI("/api/sols/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sols} : Updates an existing sol.
     *
     * @param solDTO the solDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated solDTO,
     * or with status {@code 400 (Bad Request)} if the solDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the solDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sols")
    public ResponseEntity<SolDTO> updateSol(@RequestBody SolDTO solDTO) throws URISyntaxException {
        log.debug("REST request to update Sol : {}", solDTO);
        if (solDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SolDTO result = solService.save(solDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, solDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /sols} : get all the sols.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sols in body.
     */
    @GetMapping("/sols")
    public ResponseEntity<List<SolDTO>> getAllSols(SolCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Sols by criteria: {}", criteria);
        Page<SolDTO> page = solQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sols/count} : count all the sols.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/sols/count")
    public ResponseEntity<Long> countSols(SolCriteria criteria) {
        log.debug("REST request to count Sols by criteria: {}", criteria);
        return ResponseEntity.ok().body(solQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sols/:id} : get the "id" sol.
     *
     * @param id the id of the solDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the solDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sols/{id}")
    public ResponseEntity<SolDTO> getSol(@PathVariable Long id) {
        log.debug("REST request to get Sol : {}", id);
        Optional<SolDTO> solDTO = solService.findOne(id);
        return ResponseUtil.wrapOrNotFound(solDTO);
    }

    /**
     * {@code DELETE  /sols/:id} : delete the "id" sol.
     *
     * @param id the id of the solDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sols/{id}")
    public ResponseEntity<Void> deleteSol(@PathVariable Long id) {
        log.debug("REST request to delete Sol : {}", id);
        solService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
