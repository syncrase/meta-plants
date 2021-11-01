package fr.syncrase.perma.web.rest;

import fr.syncrase.perma.service.CronquistService;
import fr.syncrase.perma.web.rest.errors.BadRequestAlertException;
import fr.syncrase.perma.service.dto.CronquistDTO;
import fr.syncrase.perma.service.dto.CronquistCriteria;
import fr.syncrase.perma.service.CronquistQueryService;

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

    private final CronquistQueryService cronquistQueryService;

    public CronquistResource(CronquistService cronquistService, CronquistQueryService cronquistQueryService) {
        this.cronquistService = cronquistService;
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
    public ResponseEntity<CronquistDTO> createCronquist(@Valid @RequestBody CronquistDTO cronquistDTO) throws URISyntaxException {
        log.debug("REST request to save Cronquist : {}", cronquistDTO);
        if (cronquistDTO.getId() != null) {
            throw new BadRequestAlertException("A new cronquist cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CronquistDTO result = cronquistService.save(cronquistDTO);
        return ResponseEntity.created(new URI("/api/cronquists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cronquists} : Updates an existing cronquist.
     *
     * @param cronquistDTO the cronquistDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cronquistDTO,
     * or with status {@code 400 (Bad Request)} if the cronquistDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cronquistDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cronquists")
    public ResponseEntity<CronquistDTO> updateCronquist(@Valid @RequestBody CronquistDTO cronquistDTO) throws URISyntaxException {
        log.debug("REST request to update Cronquist : {}", cronquistDTO);
        if (cronquistDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CronquistDTO result = cronquistService.save(cronquistDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cronquistDTO.getId().toString()))
            .body(result);
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
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
