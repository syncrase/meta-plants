package fr.syncrase.perma.web.rest;

import fr.syncrase.perma.service.RessemblanceService;
import fr.syncrase.perma.web.rest.errors.BadRequestAlertException;
import fr.syncrase.perma.service.dto.RessemblanceDTO;
import fr.syncrase.perma.service.dto.RessemblanceCriteria;
import fr.syncrase.perma.service.RessemblanceQueryService;

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

    private final RessemblanceQueryService ressemblanceQueryService;

    public RessemblanceResource(RessemblanceService ressemblanceService, RessemblanceQueryService ressemblanceQueryService) {
        this.ressemblanceService = ressemblanceService;
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
        return ResponseEntity.created(new URI("/api/ressemblances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ressemblances} : Updates an existing ressemblance.
     *
     * @param ressemblanceDTO the ressemblanceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ressemblanceDTO,
     * or with status {@code 400 (Bad Request)} if the ressemblanceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ressemblanceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ressemblances")
    public ResponseEntity<RessemblanceDTO> updateRessemblance(@RequestBody RessemblanceDTO ressemblanceDTO) throws URISyntaxException {
        log.debug("REST request to update Ressemblance : {}", ressemblanceDTO);
        if (ressemblanceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RessemblanceDTO result = ressemblanceService.save(ressemblanceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ressemblanceDTO.getId().toString()))
            .body(result);
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
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
