package fr.syncrase.perma.web.rest;

import fr.syncrase.perma.service.APGIService;
import fr.syncrase.perma.web.rest.errors.BadRequestAlertException;
import fr.syncrase.perma.service.dto.APGIDTO;
import fr.syncrase.perma.service.dto.APGICriteria;
import fr.syncrase.perma.service.APGIQueryService;

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
 * REST controller for managing {@link fr.syncrase.perma.domain.APGI}.
 */
@RestController
@RequestMapping("/api")
public class APGIResource {

    private final Logger log = LoggerFactory.getLogger(APGIResource.class);

    private static final String ENTITY_NAME = "microserviceApgi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final APGIService aPGIService;

    private final APGIQueryService aPGIQueryService;

    public APGIResource(APGIService aPGIService, APGIQueryService aPGIQueryService) {
        this.aPGIService = aPGIService;
        this.aPGIQueryService = aPGIQueryService;
    }

    /**
     * {@code POST  /apgis} : Create a new aPGI.
     *
     * @param aPGIDTO the aPGIDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aPGIDTO, or with status {@code 400 (Bad Request)} if the aPGI has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/apgis")
    public ResponseEntity<APGIDTO> createAPGI(@Valid @RequestBody APGIDTO aPGIDTO) throws URISyntaxException {
        log.debug("REST request to save APGI : {}", aPGIDTO);
        if (aPGIDTO.getId() != null) {
            throw new BadRequestAlertException("A new aPGI cannot already have an ID", ENTITY_NAME, "idexists");
        }
        APGIDTO result = aPGIService.save(aPGIDTO);
        return ResponseEntity.created(new URI("/api/apgis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /apgis} : Updates an existing aPGI.
     *
     * @param aPGIDTO the aPGIDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aPGIDTO,
     * or with status {@code 400 (Bad Request)} if the aPGIDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aPGIDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/apgis")
    public ResponseEntity<APGIDTO> updateAPGI(@Valid @RequestBody APGIDTO aPGIDTO) throws URISyntaxException {
        log.debug("REST request to update APGI : {}", aPGIDTO);
        if (aPGIDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        APGIDTO result = aPGIService.save(aPGIDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aPGIDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /apgis} : get all the aPGIS.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aPGIS in body.
     */
    @GetMapping("/apgis")
    public ResponseEntity<List<APGIDTO>> getAllAPGIS(APGICriteria criteria, Pageable pageable) {
        log.debug("REST request to get APGIS by criteria: {}", criteria);
        Page<APGIDTO> page = aPGIQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /apgis/count} : count all the aPGIS.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/apgis/count")
    public ResponseEntity<Long> countAPGIS(APGICriteria criteria) {
        log.debug("REST request to count APGIS by criteria: {}", criteria);
        return ResponseEntity.ok().body(aPGIQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /apgis/:id} : get the "id" aPGI.
     *
     * @param id the id of the aPGIDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aPGIDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/apgis/{id}")
    public ResponseEntity<APGIDTO> getAPGI(@PathVariable Long id) {
        log.debug("REST request to get APGI : {}", id);
        Optional<APGIDTO> aPGIDTO = aPGIService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aPGIDTO);
    }

    /**
     * {@code DELETE  /apgis/:id} : delete the "id" aPGI.
     *
     * @param id the id of the aPGIDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/apgis/{id}")
    public ResponseEntity<Void> deleteAPGI(@PathVariable Long id) {
        log.debug("REST request to delete APGI : {}", id);
        aPGIService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
