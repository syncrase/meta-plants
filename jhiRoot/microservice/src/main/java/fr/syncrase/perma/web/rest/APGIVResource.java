package fr.syncrase.perma.web.rest;

import fr.syncrase.perma.repository.APGIVRepository;
import fr.syncrase.perma.service.APGIVQueryService;
import fr.syncrase.perma.service.APGIVService;
import fr.syncrase.perma.service.criteria.APGIVCriteria;
import fr.syncrase.perma.service.dto.APGIVDTO;
import fr.syncrase.perma.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link fr.syncrase.perma.domain.APGIV}.
 */
@RestController
@RequestMapping("/api")
public class APGIVResource {

    private final Logger log = LoggerFactory.getLogger(APGIVResource.class);

    private static final String ENTITY_NAME = "microserviceApgiv";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final APGIVService aPGIVService;

    private final APGIVRepository aPGIVRepository;

    private final APGIVQueryService aPGIVQueryService;

    public APGIVResource(APGIVService aPGIVService, APGIVRepository aPGIVRepository, APGIVQueryService aPGIVQueryService) {
        this.aPGIVService = aPGIVService;
        this.aPGIVRepository = aPGIVRepository;
        this.aPGIVQueryService = aPGIVQueryService;
    }

    /**
     * {@code POST  /apgivs} : Create a new aPGIV.
     *
     * @param aPGIVDTO the aPGIVDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aPGIVDTO, or with status {@code 400 (Bad Request)} if the aPGIV has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/apgivs")
    public ResponseEntity<APGIVDTO> createAPGIV(@Valid @RequestBody APGIVDTO aPGIVDTO) throws URISyntaxException {
        log.debug("REST request to save APGIV : {}", aPGIVDTO);
        if (aPGIVDTO.getId() != null) {
            throw new BadRequestAlertException("A new aPGIV cannot already have an ID", ENTITY_NAME, "idexists");
        }
        APGIVDTO result = aPGIVService.save(aPGIVDTO);
        return ResponseEntity
            .created(new URI("/api/apgivs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /apgivs/:id} : Updates an existing aPGIV.
     *
     * @param id the id of the aPGIVDTO to save.
     * @param aPGIVDTO the aPGIVDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aPGIVDTO,
     * or with status {@code 400 (Bad Request)} if the aPGIVDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aPGIVDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/apgivs/{id}")
    public ResponseEntity<APGIVDTO> updateAPGIV(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody APGIVDTO aPGIVDTO
    ) throws URISyntaxException {
        log.debug("REST request to update APGIV : {}, {}", id, aPGIVDTO);
        if (aPGIVDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aPGIVDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aPGIVRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        APGIVDTO result = aPGIVService.save(aPGIVDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aPGIVDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /apgivs/:id} : Partial updates given fields of an existing aPGIV, field will ignore if it is null
     *
     * @param id the id of the aPGIVDTO to save.
     * @param aPGIVDTO the aPGIVDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aPGIVDTO,
     * or with status {@code 400 (Bad Request)} if the aPGIVDTO is not valid,
     * or with status {@code 404 (Not Found)} if the aPGIVDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the aPGIVDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/apgivs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<APGIVDTO> partialUpdateAPGIV(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody APGIVDTO aPGIVDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update APGIV partially : {}, {}", id, aPGIVDTO);
        if (aPGIVDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aPGIVDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aPGIVRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<APGIVDTO> result = aPGIVService.partialUpdate(aPGIVDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aPGIVDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /apgivs} : get all the aPGIVS.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aPGIVS in body.
     */
    @GetMapping("/apgivs")
    public ResponseEntity<List<APGIVDTO>> getAllAPGIVS(APGIVCriteria criteria, Pageable pageable) {
        log.debug("REST request to get APGIVS by criteria: {}", criteria);
        Page<APGIVDTO> page = aPGIVQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /apgivs/count} : count all the aPGIVS.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/apgivs/count")
    public ResponseEntity<Long> countAPGIVS(APGIVCriteria criteria) {
        log.debug("REST request to count APGIVS by criteria: {}", criteria);
        return ResponseEntity.ok().body(aPGIVQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /apgivs/:id} : get the "id" aPGIV.
     *
     * @param id the id of the aPGIVDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aPGIVDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/apgivs/{id}")
    public ResponseEntity<APGIVDTO> getAPGIV(@PathVariable Long id) {
        log.debug("REST request to get APGIV : {}", id);
        Optional<APGIVDTO> aPGIVDTO = aPGIVService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aPGIVDTO);
    }

    /**
     * {@code DELETE  /apgivs/:id} : delete the "id" aPGIV.
     *
     * @param id the id of the aPGIVDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/apgivs/{id}")
    public ResponseEntity<Void> deleteAPGIV(@PathVariable Long id) {
        log.debug("REST request to delete APGIV : {}", id);
        aPGIVService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
