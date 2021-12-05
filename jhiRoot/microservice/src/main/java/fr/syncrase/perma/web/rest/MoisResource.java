package fr.syncrase.perma.web.rest;

import fr.syncrase.perma.repository.MoisRepository;
import fr.syncrase.perma.service.MoisQueryService;
import fr.syncrase.perma.service.MoisService;
import fr.syncrase.perma.service.criteria.MoisCriteria;
import fr.syncrase.perma.service.dto.MoisDTO;
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
 * REST controller for managing {@link fr.syncrase.perma.domain.Mois}.
 */
@RestController
@RequestMapping("/api")
public class MoisResource {

    private final Logger log = LoggerFactory.getLogger(MoisResource.class);

    private static final String ENTITY_NAME = "microserviceMois";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MoisService moisService;

    private final MoisRepository moisRepository;

    private final MoisQueryService moisQueryService;

    public MoisResource(MoisService moisService, MoisRepository moisRepository, MoisQueryService moisQueryService) {
        this.moisService = moisService;
        this.moisRepository = moisRepository;
        this.moisQueryService = moisQueryService;
    }

    /**
     * {@code POST  /mois} : Create a new mois.
     *
     * @param moisDTO the moisDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new moisDTO, or with status {@code 400 (Bad Request)} if the mois has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mois")
    public ResponseEntity<MoisDTO> createMois(@Valid @RequestBody MoisDTO moisDTO) throws URISyntaxException {
        log.debug("REST request to save Mois : {}", moisDTO);
        if (moisDTO.getId() != null) {
            throw new BadRequestAlertException("A new mois cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MoisDTO result = moisService.save(moisDTO);
        return ResponseEntity
            .created(new URI("/api/mois/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mois/:id} : Updates an existing mois.
     *
     * @param id the id of the moisDTO to save.
     * @param moisDTO the moisDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moisDTO,
     * or with status {@code 400 (Bad Request)} if the moisDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the moisDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mois/{id}")
    public ResponseEntity<MoisDTO> updateMois(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MoisDTO moisDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Mois : {}, {}", id, moisDTO);
        if (moisDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, moisDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!moisRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MoisDTO result = moisService.save(moisDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, moisDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /mois/:id} : Partial updates given fields of an existing mois, field will ignore if it is null
     *
     * @param id the id of the moisDTO to save.
     * @param moisDTO the moisDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moisDTO,
     * or with status {@code 400 (Bad Request)} if the moisDTO is not valid,
     * or with status {@code 404 (Not Found)} if the moisDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the moisDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/mois/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MoisDTO> partialUpdateMois(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MoisDTO moisDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Mois partially : {}, {}", id, moisDTO);
        if (moisDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, moisDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!moisRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MoisDTO> result = moisService.partialUpdate(moisDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, moisDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /mois} : get all the mois.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mois in body.
     */
    @GetMapping("/mois")
    public ResponseEntity<List<MoisDTO>> getAllMois(MoisCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Mois by criteria: {}", criteria);
        Page<MoisDTO> page = moisQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /mois/count} : count all the mois.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/mois/count")
    public ResponseEntity<Long> countMois(MoisCriteria criteria) {
        log.debug("REST request to count Mois by criteria: {}", criteria);
        return ResponseEntity.ok().body(moisQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /mois/:id} : get the "id" mois.
     *
     * @param id the id of the moisDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the moisDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mois/{id}")
    public ResponseEntity<MoisDTO> getMois(@PathVariable Long id) {
        log.debug("REST request to get Mois : {}", id);
        Optional<MoisDTO> moisDTO = moisService.findOne(id);
        return ResponseUtil.wrapOrNotFound(moisDTO);
    }

    /**
     * {@code DELETE  /mois/:id} : delete the "id" mois.
     *
     * @param id the id of the moisDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mois/{id}")
    public ResponseEntity<Void> deleteMois(@PathVariable Long id) {
        log.debug("REST request to delete Mois : {}", id);
        moisService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
