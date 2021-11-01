package fr.syncrase.perma.web.rest;

import fr.syncrase.perma.service.RaunkierService;
import fr.syncrase.perma.web.rest.errors.BadRequestAlertException;
import fr.syncrase.perma.service.dto.RaunkierDTO;
import fr.syncrase.perma.service.dto.RaunkierCriteria;
import fr.syncrase.perma.service.RaunkierQueryService;

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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link fr.syncrase.perma.domain.Raunkier}.
 */
@RestController
@RequestMapping("/api")
public class RaunkierResource {

    private final Logger log = LoggerFactory.getLogger(RaunkierResource.class);

    private static final String ENTITY_NAME = "microserviceRaunkier";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RaunkierService raunkierService;

    private final RaunkierQueryService raunkierQueryService;

    public RaunkierResource(RaunkierService raunkierService, RaunkierQueryService raunkierQueryService) {
        this.raunkierService = raunkierService;
        this.raunkierQueryService = raunkierQueryService;
    }

    /**
     * {@code POST  /raunkiers} : Create a new raunkier.
     *
     * @param raunkierDTO the raunkierDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new raunkierDTO, or with status {@code 400 (Bad Request)} if the raunkier has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/raunkiers")
    public ResponseEntity<RaunkierDTO> createRaunkier(@Valid @RequestBody RaunkierDTO raunkierDTO) throws URISyntaxException {
        log.debug("REST request to save Raunkier : {}", raunkierDTO);
        if (raunkierDTO.getId() != null) {
            throw new BadRequestAlertException("A new raunkier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RaunkierDTO result = raunkierService.save(raunkierDTO);
        return ResponseEntity.created(new URI("/api/raunkiers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /raunkiers} : Updates an existing raunkier.
     *
     * @param raunkierDTO the raunkierDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated raunkierDTO,
     * or with status {@code 400 (Bad Request)} if the raunkierDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the raunkierDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/raunkiers")
    public ResponseEntity<RaunkierDTO> updateRaunkier(@Valid @RequestBody RaunkierDTO raunkierDTO) throws URISyntaxException {
        log.debug("REST request to update Raunkier : {}", raunkierDTO);
        if (raunkierDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RaunkierDTO result = raunkierService.save(raunkierDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, raunkierDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /raunkiers} : get all the raunkiers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of raunkiers in body.
     */
    @GetMapping("/raunkiers")
    public ResponseEntity<List<RaunkierDTO>> getAllRaunkiers(RaunkierCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Raunkiers by criteria: {}", criteria);
        Page<RaunkierDTO> page = raunkierQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /raunkiers/count} : count all the raunkiers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/raunkiers/count")
    public ResponseEntity<Long> countRaunkiers(RaunkierCriteria criteria) {
        log.debug("REST request to count Raunkiers by criteria: {}", criteria);
        return ResponseEntity.ok().body(raunkierQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /raunkiers/:id} : get the "id" raunkier.
     *
     * @param id the id of the raunkierDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the raunkierDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/raunkiers/{id}")
    public ResponseEntity<RaunkierDTO> getRaunkier(@PathVariable Long id) {
        log.debug("REST request to get Raunkier : {}", id);
        Optional<RaunkierDTO> raunkierDTO = raunkierService.findOne(id);
        return ResponseUtil.wrapOrNotFound(raunkierDTO);
    }

    /**
     * {@code DELETE  /raunkiers/:id} : delete the "id" raunkier.
     *
     * @param id the id of the raunkierDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/raunkiers/{id}")
    public ResponseEntity<Void> deleteRaunkier(@PathVariable Long id) {
        log.debug("REST request to delete Raunkier : {}", id);
        raunkierService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
