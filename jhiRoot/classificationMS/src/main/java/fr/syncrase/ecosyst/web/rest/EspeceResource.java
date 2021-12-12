package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.Espece;
import fr.syncrase.ecosyst.repository.EspeceRepository;
import fr.syncrase.ecosyst.service.EspeceQueryService;
import fr.syncrase.ecosyst.service.EspeceService;
import fr.syncrase.ecosyst.service.criteria.EspeceCriteria;
import fr.syncrase.ecosyst.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.Espece}.
 */
@RestController
@RequestMapping("/api")
public class EspeceResource {

    private final Logger log = LoggerFactory.getLogger(EspeceResource.class);

    private static final String ENTITY_NAME = "classificationMsEspece";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EspeceService especeService;

    private final EspeceRepository especeRepository;

    private final EspeceQueryService especeQueryService;

    public EspeceResource(EspeceService especeService, EspeceRepository especeRepository, EspeceQueryService especeQueryService) {
        this.especeService = especeService;
        this.especeRepository = especeRepository;
        this.especeQueryService = especeQueryService;
    }

    /**
     * {@code POST  /especes} : Create a new espece.
     *
     * @param espece the espece to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new espece, or with status {@code 400 (Bad Request)} if the espece has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/especes")
    public ResponseEntity<Espece> createEspece(@Valid @RequestBody Espece espece) throws URISyntaxException {
        log.debug("REST request to save Espece : {}", espece);
        if (espece.getId() != null) {
            throw new BadRequestAlertException("A new espece cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Espece result = especeService.save(espece);
        return ResponseEntity
            .created(new URI("/api/especes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /especes/:id} : Updates an existing espece.
     *
     * @param id the id of the espece to save.
     * @param espece the espece to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated espece,
     * or with status {@code 400 (Bad Request)} if the espece is not valid,
     * or with status {@code 500 (Internal Server Error)} if the espece couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/especes/{id}")
    public ResponseEntity<Espece> updateEspece(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Espece espece
    ) throws URISyntaxException {
        log.debug("REST request to update Espece : {}, {}", id, espece);
        if (espece.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, espece.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!especeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Espece result = especeService.save(espece);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, espece.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /especes/:id} : Partial updates given fields of an existing espece, field will ignore if it is null
     *
     * @param id the id of the espece to save.
     * @param espece the espece to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated espece,
     * or with status {@code 400 (Bad Request)} if the espece is not valid,
     * or with status {@code 404 (Not Found)} if the espece is not found,
     * or with status {@code 500 (Internal Server Error)} if the espece couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/especes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Espece> partialUpdateEspece(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Espece espece
    ) throws URISyntaxException {
        log.debug("REST request to partial update Espece partially : {}, {}", id, espece);
        if (espece.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, espece.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!especeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Espece> result = especeService.partialUpdate(espece);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, espece.getId().toString())
        );
    }

    /**
     * {@code GET  /especes} : get all the especes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of especes in body.
     */
    @GetMapping("/especes")
    public ResponseEntity<List<Espece>> getAllEspeces(EspeceCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Especes by criteria: {}", criteria);
        Page<Espece> page = especeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /especes/count} : count all the especes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/especes/count")
    public ResponseEntity<Long> countEspeces(EspeceCriteria criteria) {
        log.debug("REST request to count Especes by criteria: {}", criteria);
        return ResponseEntity.ok().body(especeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /especes/:id} : get the "id" espece.
     *
     * @param id the id of the espece to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the espece, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/especes/{id}")
    public ResponseEntity<Espece> getEspece(@PathVariable Long id) {
        log.debug("REST request to get Espece : {}", id);
        Optional<Espece> espece = especeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(espece);
    }

    /**
     * {@code DELETE  /especes/:id} : delete the "id" espece.
     *
     * @param id the id of the espece to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/especes/{id}")
    public ResponseEntity<Void> deleteEspece(@PathVariable Long id) {
        log.debug("REST request to delete Espece : {}", id);
        especeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
