package fr.syncrase.perma.web.rest;

import fr.syncrase.perma.service.APGIIIService;
import fr.syncrase.perma.web.rest.errors.BadRequestAlertException;
import fr.syncrase.perma.service.dto.APGIIIDTO;
import fr.syncrase.perma.service.dto.APGIIICriteria;
import fr.syncrase.perma.service.APGIIIQueryService;

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
 * REST controller for managing {@link fr.syncrase.perma.domain.APGIII}.
 */
@RestController
@RequestMapping("/api")
public class APGIIIResource {

    private final Logger log = LoggerFactory.getLogger(APGIIIResource.class);

    private static final String ENTITY_NAME = "microserviceApgiii";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final APGIIIService aPGIIIService;

    private final APGIIIQueryService aPGIIIQueryService;

    public APGIIIResource(APGIIIService aPGIIIService, APGIIIQueryService aPGIIIQueryService) {
        this.aPGIIIService = aPGIIIService;
        this.aPGIIIQueryService = aPGIIIQueryService;
    }

    /**
     * {@code POST  /apgiiis} : Create a new aPGIII.
     *
     * @param aPGIIIDTO the aPGIIIDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aPGIIIDTO, or with status {@code 400 (Bad Request)} if the aPGIII has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/apgiiis")
    public ResponseEntity<APGIIIDTO> createAPGIII(@Valid @RequestBody APGIIIDTO aPGIIIDTO) throws URISyntaxException {
        log.debug("REST request to save APGIII : {}", aPGIIIDTO);
        if (aPGIIIDTO.getId() != null) {
            throw new BadRequestAlertException("A new aPGIII cannot already have an ID", ENTITY_NAME, "idexists");
        }
        APGIIIDTO result = aPGIIIService.save(aPGIIIDTO);
        return ResponseEntity.created(new URI("/api/apgiiis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /apgiiis} : Updates an existing aPGIII.
     *
     * @param aPGIIIDTO the aPGIIIDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aPGIIIDTO,
     * or with status {@code 400 (Bad Request)} if the aPGIIIDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aPGIIIDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/apgiiis")
    public ResponseEntity<APGIIIDTO> updateAPGIII(@Valid @RequestBody APGIIIDTO aPGIIIDTO) throws URISyntaxException {
        log.debug("REST request to update APGIII : {}", aPGIIIDTO);
        if (aPGIIIDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        APGIIIDTO result = aPGIIIService.save(aPGIIIDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aPGIIIDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /apgiiis} : get all the aPGIIIS.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aPGIIIS in body.
     */
    @GetMapping("/apgiiis")
    public ResponseEntity<List<APGIIIDTO>> getAllAPGIIIS(APGIIICriteria criteria, Pageable pageable) {
        log.debug("REST request to get APGIIIS by criteria: {}", criteria);
        Page<APGIIIDTO> page = aPGIIIQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /apgiiis/count} : count all the aPGIIIS.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/apgiiis/count")
    public ResponseEntity<Long> countAPGIIIS(APGIIICriteria criteria) {
        log.debug("REST request to count APGIIIS by criteria: {}", criteria);
        return ResponseEntity.ok().body(aPGIIIQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /apgiiis/:id} : get the "id" aPGIII.
     *
     * @param id the id of the aPGIIIDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aPGIIIDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/apgiiis/{id}")
    public ResponseEntity<APGIIIDTO> getAPGIII(@PathVariable Long id) {
        log.debug("REST request to get APGIII : {}", id);
        Optional<APGIIIDTO> aPGIIIDTO = aPGIIIService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aPGIIIDTO);
    }

    /**
     * {@code DELETE  /apgiiis/:id} : delete the "id" aPGIII.
     *
     * @param id the id of the aPGIIIDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/apgiiis/{id}")
    public ResponseEntity<Void> deleteAPGIII(@PathVariable Long id) {
        log.debug("REST request to delete APGIII : {}", id);
        aPGIIIService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
