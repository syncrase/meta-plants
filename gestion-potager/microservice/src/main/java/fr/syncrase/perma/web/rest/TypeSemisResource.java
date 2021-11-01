package fr.syncrase.perma.web.rest;

import fr.syncrase.perma.service.TypeSemisService;
import fr.syncrase.perma.web.rest.errors.BadRequestAlertException;
import fr.syncrase.perma.service.dto.TypeSemisDTO;
import fr.syncrase.perma.service.dto.TypeSemisCriteria;
import fr.syncrase.perma.service.TypeSemisQueryService;

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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link fr.syncrase.perma.domain.TypeSemis}.
 */
@RestController
@RequestMapping("/api")
public class TypeSemisResource {

    private final Logger log = LoggerFactory.getLogger(TypeSemisResource.class);

    private static final String ENTITY_NAME = "microserviceTypeSemis";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeSemisService typeSemisService;

    private final TypeSemisQueryService typeSemisQueryService;

    public TypeSemisResource(TypeSemisService typeSemisService, TypeSemisQueryService typeSemisQueryService) {
        this.typeSemisService = typeSemisService;
        this.typeSemisQueryService = typeSemisQueryService;
    }

    /**
     * {@code POST  /type-semis} : Create a new typeSemis.
     *
     * @param typeSemisDTO the typeSemisDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeSemisDTO, or with status {@code 400 (Bad Request)} if the typeSemis has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/type-semis")
    public ResponseEntity<TypeSemisDTO> createTypeSemis(@RequestBody TypeSemisDTO typeSemisDTO) throws URISyntaxException {
        log.debug("REST request to save TypeSemis : {}", typeSemisDTO);
        if (typeSemisDTO.getId() != null) {
            throw new BadRequestAlertException("A new typeSemis cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeSemisDTO result = typeSemisService.save(typeSemisDTO);
        return ResponseEntity.created(new URI("/api/type-semis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-semis} : Updates an existing typeSemis.
     *
     * @param typeSemisDTO the typeSemisDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeSemisDTO,
     * or with status {@code 400 (Bad Request)} if the typeSemisDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeSemisDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/type-semis")
    public ResponseEntity<TypeSemisDTO> updateTypeSemis(@RequestBody TypeSemisDTO typeSemisDTO) throws URISyntaxException {
        log.debug("REST request to update TypeSemis : {}", typeSemisDTO);
        if (typeSemisDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TypeSemisDTO result = typeSemisService.save(typeSemisDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeSemisDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /type-semis} : get all the typeSemis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeSemis in body.
     */
    @GetMapping("/type-semis")
    public ResponseEntity<List<TypeSemisDTO>> getAllTypeSemis(TypeSemisCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TypeSemis by criteria: {}", criteria);
        Page<TypeSemisDTO> page = typeSemisQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /type-semis/count} : count all the typeSemis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/type-semis/count")
    public ResponseEntity<Long> countTypeSemis(TypeSemisCriteria criteria) {
        log.debug("REST request to count TypeSemis by criteria: {}", criteria);
        return ResponseEntity.ok().body(typeSemisQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /type-semis/:id} : get the "id" typeSemis.
     *
     * @param id the id of the typeSemisDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeSemisDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/type-semis/{id}")
    public ResponseEntity<TypeSemisDTO> getTypeSemis(@PathVariable Long id) {
        log.debug("REST request to get TypeSemis : {}", id);
        Optional<TypeSemisDTO> typeSemisDTO = typeSemisService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeSemisDTO);
    }

    /**
     * {@code DELETE  /type-semis/:id} : delete the "id" typeSemis.
     *
     * @param id the id of the typeSemisDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/type-semis/{id}")
    public ResponseEntity<Void> deleteTypeSemis(@PathVariable Long id) {
        log.debug("REST request to delete TypeSemis : {}", id);
        typeSemisService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
