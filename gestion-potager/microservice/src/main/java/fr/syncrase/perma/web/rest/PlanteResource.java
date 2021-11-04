package fr.syncrase.perma.web.rest;

import fr.syncrase.perma.service.PlanteService;
import fr.syncrase.perma.web.rest.errors.BadRequestAlertException;
import fr.syncrase.perma.service.dto.PlanteDTO;
import fr.syncrase.perma.service.dto.PlanteCriteria;
import fr.syncrase.perma.service.PlanteQueryService;

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
import java.util.stream.StreamSupport;

/**
 * REST controller for managing {@link fr.syncrase.perma.domain.Plante}.
 */
@RestController
@RequestMapping("/api")
public class PlanteResource {

    private final Logger log = LoggerFactory.getLogger(PlanteResource.class);

    private static final String ENTITY_NAME = "microservicePlante";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlanteService planteService;

    private final PlanteQueryService planteQueryService;

    public PlanteResource(PlanteService planteService, PlanteQueryService planteQueryService) {
        this.planteService = planteService;
        this.planteQueryService = planteQueryService;
    }

    /**
     * {@code POST  /plantes} : Create a new plante.
     *
     * @param planteDTO the planteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new planteDTO, or with status {@code 400 (Bad Request)} if the plante has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/plantes")
    public ResponseEntity<PlanteDTO> createPlante(@Valid @RequestBody PlanteDTO planteDTO) throws URISyntaxException {
        log.debug("REST request to save Plante : {}", planteDTO);
        if (planteDTO.getId() != null) {
            throw new BadRequestAlertException("A new plante cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlanteDTO result = planteService.save(planteDTO);
        return ResponseEntity.created(new URI("/api/plantes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /plantes} : Updates an existing plante.
     *
     * @param planteDTO the planteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated planteDTO,
     * or with status {@code 400 (Bad Request)} if the planteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the planteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/plantes")
    public ResponseEntity<PlanteDTO> updatePlante(@Valid @RequestBody PlanteDTO planteDTO) throws URISyntaxException {
        log.debug("REST request to update Plante : {}", planteDTO);
        if (planteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PlanteDTO result = planteService.save(planteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, planteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /plantes} : get all the plantes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of plantes in body.
     */
    @GetMapping("/plantes")
    public ResponseEntity<List<PlanteDTO>> getAllPlantes(PlanteCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Plantes by criteria: {}", criteria);
        Page<PlanteDTO> page = planteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /plantes/count} : count all the plantes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/plantes/count")
    public ResponseEntity<Long> countPlantes(PlanteCriteria criteria) {
        log.debug("REST request to count Plantes by criteria: {}", criteria);
        return ResponseEntity.ok().body(planteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /plantes/:id} : get the "id" plante.
     *
     * @param id the id of the planteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the planteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/plantes/{id}")
    public ResponseEntity<PlanteDTO> getPlante(@PathVariable Long id) {
        log.debug("REST request to get Plante : {}", id);
        Optional<PlanteDTO> planteDTO = planteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(planteDTO);
    }

    /**
     * {@code DELETE  /plantes/:id} : delete the "id" plante.
     *
     * @param id the id of the planteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/plantes/{id}")
    public ResponseEntity<Void> deletePlante(@PathVariable Long id) {
        log.debug("REST request to delete Plante : {}", id);
        planteService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}