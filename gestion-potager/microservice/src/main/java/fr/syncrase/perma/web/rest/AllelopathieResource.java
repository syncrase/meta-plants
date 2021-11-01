package fr.syncrase.perma.web.rest;

import fr.syncrase.perma.service.AllelopathieService;
import fr.syncrase.perma.web.rest.errors.BadRequestAlertException;
import fr.syncrase.perma.service.dto.AllelopathieDTO;
import fr.syncrase.perma.service.dto.AllelopathieCriteria;
import fr.syncrase.perma.service.AllelopathieQueryService;

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
 * REST controller for managing {@link fr.syncrase.perma.domain.Allelopathie}.
 */
@RestController
@RequestMapping("/api")
public class AllelopathieResource {

    private final Logger log = LoggerFactory.getLogger(AllelopathieResource.class);

    private static final String ENTITY_NAME = "microserviceAllelopathie";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AllelopathieService allelopathieService;

    private final AllelopathieQueryService allelopathieQueryService;

    public AllelopathieResource(AllelopathieService allelopathieService, AllelopathieQueryService allelopathieQueryService) {
        this.allelopathieService = allelopathieService;
        this.allelopathieQueryService = allelopathieQueryService;
    }

    /**
     * {@code POST  /allelopathies} : Create a new allelopathie.
     *
     * @param allelopathieDTO the allelopathieDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new allelopathieDTO, or with status {@code 400 (Bad Request)} if the allelopathie has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/allelopathies")
    public ResponseEntity<AllelopathieDTO> createAllelopathie(@Valid @RequestBody AllelopathieDTO allelopathieDTO) throws URISyntaxException {
        log.debug("REST request to save Allelopathie : {}", allelopathieDTO);
        if (allelopathieDTO.getId() != null) {
            throw new BadRequestAlertException("A new allelopathie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AllelopathieDTO result = allelopathieService.save(allelopathieDTO);
        return ResponseEntity.created(new URI("/api/allelopathies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /allelopathies} : Updates an existing allelopathie.
     *
     * @param allelopathieDTO the allelopathieDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated allelopathieDTO,
     * or with status {@code 400 (Bad Request)} if the allelopathieDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the allelopathieDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/allelopathies")
    public ResponseEntity<AllelopathieDTO> updateAllelopathie(@Valid @RequestBody AllelopathieDTO allelopathieDTO) throws URISyntaxException {
        log.debug("REST request to update Allelopathie : {}", allelopathieDTO);
        if (allelopathieDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AllelopathieDTO result = allelopathieService.save(allelopathieDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, allelopathieDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /allelopathies} : get all the allelopathies.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of allelopathies in body.
     */
    @GetMapping("/allelopathies")
    public ResponseEntity<List<AllelopathieDTO>> getAllAllelopathies(AllelopathieCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Allelopathies by criteria: {}", criteria);
        Page<AllelopathieDTO> page = allelopathieQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /allelopathies/count} : count all the allelopathies.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/allelopathies/count")
    public ResponseEntity<Long> countAllelopathies(AllelopathieCriteria criteria) {
        log.debug("REST request to count Allelopathies by criteria: {}", criteria);
        return ResponseEntity.ok().body(allelopathieQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /allelopathies/:id} : get the "id" allelopathie.
     *
     * @param id the id of the allelopathieDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the allelopathieDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/allelopathies/{id}")
    public ResponseEntity<AllelopathieDTO> getAllelopathie(@PathVariable Long id) {
        log.debug("REST request to get Allelopathie : {}", id);
        Optional<AllelopathieDTO> allelopathieDTO = allelopathieService.findOne(id);
        return ResponseUtil.wrapOrNotFound(allelopathieDTO);
    }

    /**
     * {@code DELETE  /allelopathies/:id} : delete the "id" allelopathie.
     *
     * @param id the id of the allelopathieDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/allelopathies/{id}")
    public ResponseEntity<Void> deleteAllelopathie(@PathVariable Long id) {
        log.debug("REST request to delete Allelopathie : {}", id);
        allelopathieService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
