package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.Regne;
import fr.syncrase.ecosyst.repository.RegneRepository;
import fr.syncrase.ecosyst.service.RegneQueryService;
import fr.syncrase.ecosyst.service.RegneService;
import fr.syncrase.ecosyst.service.criteria.RegneCriteria;
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
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.Regne}.
 */
@RestController
@RequestMapping("/api")
public class RegneResource {

    private final Logger log = LoggerFactory.getLogger(RegneResource.class);

    private static final String ENTITY_NAME = "classificationMsRegne";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RegneService regneService;

    private final RegneRepository regneRepository;

    private final RegneQueryService regneQueryService;

    public RegneResource(RegneService regneService, RegneRepository regneRepository, RegneQueryService regneQueryService) {
        this.regneService = regneService;
        this.regneRepository = regneRepository;
        this.regneQueryService = regneQueryService;
    }

    /**
     * {@code POST  /regnes} : Create a new regne.
     *
     * @param regne the regne to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new regne, or with status {@code 400 (Bad Request)} if the regne has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/regnes")
    public ResponseEntity<Regne> createRegne(@Valid @RequestBody Regne regne) throws URISyntaxException {
        log.debug("REST request to save Regne : {}", regne);
        if (regne.getId() != null) {
            throw new BadRequestAlertException("A new regne cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Regne result = regneService.save(regne);
        return ResponseEntity
            .created(new URI("/api/regnes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /regnes/:id} : Updates an existing regne.
     *
     * @param id the id of the regne to save.
     * @param regne the regne to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated regne,
     * or with status {@code 400 (Bad Request)} if the regne is not valid,
     * or with status {@code 500 (Internal Server Error)} if the regne couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/regnes/{id}")
    public ResponseEntity<Regne> updateRegne(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Regne regne)
        throws URISyntaxException {
        log.debug("REST request to update Regne : {}, {}", id, regne);
        if (regne.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, regne.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!regneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Regne result = regneService.save(regne);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, regne.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /regnes/:id} : Partial updates given fields of an existing regne, field will ignore if it is null
     *
     * @param id the id of the regne to save.
     * @param regne the regne to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated regne,
     * or with status {@code 400 (Bad Request)} if the regne is not valid,
     * or with status {@code 404 (Not Found)} if the regne is not found,
     * or with status {@code 500 (Internal Server Error)} if the regne couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/regnes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Regne> partialUpdateRegne(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Regne regne
    ) throws URISyntaxException {
        log.debug("REST request to partial update Regne partially : {}, {}", id, regne);
        if (regne.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, regne.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!regneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Regne> result = regneService.partialUpdate(regne);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, regne.getId().toString())
        );
    }

    /**
     * {@code GET  /regnes} : get all the regnes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of regnes in body.
     */
    @GetMapping("/regnes")
    public ResponseEntity<List<Regne>> getAllRegnes(RegneCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Regnes by criteria: {}", criteria);
        Page<Regne> page = regneQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /regnes/count} : count all the regnes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/regnes/count")
    public ResponseEntity<Long> countRegnes(RegneCriteria criteria) {
        log.debug("REST request to count Regnes by criteria: {}", criteria);
        return ResponseEntity.ok().body(regneQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /regnes/:id} : get the "id" regne.
     *
     * @param id the id of the regne to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the regne, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/regnes/{id}")
    public ResponseEntity<Regne> getRegne(@PathVariable Long id) {
        log.debug("REST request to get Regne : {}", id);
        Optional<Regne> regne = regneService.findOne(id);
        return ResponseUtil.wrapOrNotFound(regne);
    }

    /**
     * {@code DELETE  /regnes/:id} : delete the "id" regne.
     *
     * @param id the id of the regne to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/regnes/{id}")
    public ResponseEntity<Void> deleteRegne(@PathVariable Long id) {
        log.debug("REST request to delete Regne : {}", id);
        regneService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
