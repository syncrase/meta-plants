package fr.syncrase.perma.web.rest;

import fr.syncrase.perma.repository.RacineRepository;
import fr.syncrase.perma.service.RacineQueryService;
import fr.syncrase.perma.service.RacineService;
import fr.syncrase.perma.service.criteria.RacineCriteria;
import fr.syncrase.perma.service.dto.RacineDTO;
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
 * REST controller for managing {@link fr.syncrase.perma.domain.Racine}.
 */
@RestController
@RequestMapping("/api")
public class RacineResource {

    private final Logger log = LoggerFactory.getLogger(RacineResource.class);

    private static final String ENTITY_NAME = "microserviceRacine";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RacineService racineService;

    private final RacineRepository racineRepository;

    private final RacineQueryService racineQueryService;

    public RacineResource(RacineService racineService, RacineRepository racineRepository, RacineQueryService racineQueryService) {
        this.racineService = racineService;
        this.racineRepository = racineRepository;
        this.racineQueryService = racineQueryService;
    }

    /**
     * {@code POST  /racines} : Create a new racine.
     *
     * @param racineDTO the racineDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new racineDTO, or with status {@code 400 (Bad Request)} if the racine has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/racines")
    public ResponseEntity<RacineDTO> createRacine(@RequestBody RacineDTO racineDTO) throws URISyntaxException {
        log.debug("REST request to save Racine : {}", racineDTO);
        if (racineDTO.getId() != null) {
            throw new BadRequestAlertException("A new racine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RacineDTO result = racineService.save(racineDTO);
        return ResponseEntity
            .created(new URI("/api/racines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /racines/:id} : Updates an existing racine.
     *
     * @param id the id of the racineDTO to save.
     * @param racineDTO the racineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated racineDTO,
     * or with status {@code 400 (Bad Request)} if the racineDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the racineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/racines/{id}")
    public ResponseEntity<RacineDTO> updateRacine(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RacineDTO racineDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Racine : {}, {}", id, racineDTO);
        if (racineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, racineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!racineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RacineDTO result = racineService.save(racineDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, racineDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /racines/:id} : Partial updates given fields of an existing racine, field will ignore if it is null
     *
     * @param id the id of the racineDTO to save.
     * @param racineDTO the racineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated racineDTO,
     * or with status {@code 400 (Bad Request)} if the racineDTO is not valid,
     * or with status {@code 404 (Not Found)} if the racineDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the racineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/racines/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RacineDTO> partialUpdateRacine(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RacineDTO racineDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Racine partially : {}, {}", id, racineDTO);
        if (racineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, racineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!racineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RacineDTO> result = racineService.partialUpdate(racineDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, racineDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /racines} : get all the racines.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of racines in body.
     */
    @GetMapping("/racines")
    public ResponseEntity<List<RacineDTO>> getAllRacines(RacineCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Racines by criteria: {}", criteria);
        Page<RacineDTO> page = racineQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /racines/count} : count all the racines.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/racines/count")
    public ResponseEntity<Long> countRacines(RacineCriteria criteria) {
        log.debug("REST request to count Racines by criteria: {}", criteria);
        return ResponseEntity.ok().body(racineQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /racines/:id} : get the "id" racine.
     *
     * @param id the id of the racineDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the racineDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/racines/{id}")
    public ResponseEntity<RacineDTO> getRacine(@PathVariable Long id) {
        log.debug("REST request to get Racine : {}", id);
        Optional<RacineDTO> racineDTO = racineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(racineDTO);
    }

    /**
     * {@code DELETE  /racines/:id} : delete the "id" racine.
     *
     * @param id the id of the racineDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/racines/{id}")
    public ResponseEntity<Void> deleteRacine(@PathVariable Long id) {
        log.debug("REST request to delete Racine : {}", id);
        racineService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
