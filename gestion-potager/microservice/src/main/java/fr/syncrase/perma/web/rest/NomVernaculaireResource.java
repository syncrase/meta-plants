package fr.syncrase.perma.web.rest;

import fr.syncrase.perma.service.NomVernaculaireService;
import fr.syncrase.perma.web.rest.errors.BadRequestAlertException;
import fr.syncrase.perma.service.dto.NomVernaculaireDTO;
import fr.syncrase.perma.service.dto.NomVernaculaireCriteria;
import fr.syncrase.perma.service.NomVernaculaireQueryService;

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
 * REST controller for managing {@link fr.syncrase.perma.domain.NomVernaculaire}.
 */
@RestController
@RequestMapping("/api")
public class NomVernaculaireResource {

    private final Logger log = LoggerFactory.getLogger(NomVernaculaireResource.class);

    private static final String ENTITY_NAME = "microserviceNomVernaculaire";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NomVernaculaireService nomVernaculaireService;

    private final NomVernaculaireQueryService nomVernaculaireQueryService;

    public NomVernaculaireResource(NomVernaculaireService nomVernaculaireService, NomVernaculaireQueryService nomVernaculaireQueryService) {
        this.nomVernaculaireService = nomVernaculaireService;
        this.nomVernaculaireQueryService = nomVernaculaireQueryService;
    }

    /**
     * {@code POST  /nom-vernaculaires} : Create a new nomVernaculaire.
     *
     * @param nomVernaculaireDTO the nomVernaculaireDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nomVernaculaireDTO, or with status {@code 400 (Bad Request)} if the nomVernaculaire has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/nom-vernaculaires")
    public ResponseEntity<NomVernaculaireDTO> createNomVernaculaire(@Valid @RequestBody NomVernaculaireDTO nomVernaculaireDTO) throws URISyntaxException {
        log.debug("REST request to save NomVernaculaire : {}", nomVernaculaireDTO);
        if (nomVernaculaireDTO.getId() != null) {
            throw new BadRequestAlertException("A new nomVernaculaire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NomVernaculaireDTO result = nomVernaculaireService.save(nomVernaculaireDTO);
        return ResponseEntity.created(new URI("/api/nom-vernaculaires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /nom-vernaculaires} : Updates an existing nomVernaculaire.
     *
     * @param nomVernaculaireDTO the nomVernaculaireDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nomVernaculaireDTO,
     * or with status {@code 400 (Bad Request)} if the nomVernaculaireDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nomVernaculaireDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/nom-vernaculaires")
    public ResponseEntity<NomVernaculaireDTO> updateNomVernaculaire(@Valid @RequestBody NomVernaculaireDTO nomVernaculaireDTO) throws URISyntaxException {
        log.debug("REST request to update NomVernaculaire : {}", nomVernaculaireDTO);
        if (nomVernaculaireDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        NomVernaculaireDTO result = nomVernaculaireService.save(nomVernaculaireDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nomVernaculaireDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /nom-vernaculaires} : get all the nomVernaculaires.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nomVernaculaires in body.
     */
    @GetMapping("/nom-vernaculaires")
    public ResponseEntity<List<NomVernaculaireDTO>> getAllNomVernaculaires(NomVernaculaireCriteria criteria, Pageable pageable) {
        log.debug("REST request to get NomVernaculaires by criteria: {}", criteria);
        Page<NomVernaculaireDTO> page = nomVernaculaireQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /nom-vernaculaires/count} : count all the nomVernaculaires.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/nom-vernaculaires/count")
    public ResponseEntity<Long> countNomVernaculaires(NomVernaculaireCriteria criteria) {
        log.debug("REST request to count NomVernaculaires by criteria: {}", criteria);
        return ResponseEntity.ok().body(nomVernaculaireQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /nom-vernaculaires/:id} : get the "id" nomVernaculaire.
     *
     * @param id the id of the nomVernaculaireDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nomVernaculaireDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/nom-vernaculaires/{id}")
    public ResponseEntity<NomVernaculaireDTO> getNomVernaculaire(@PathVariable Long id) {
        log.debug("REST request to get NomVernaculaire : {}", id);
        Optional<NomVernaculaireDTO> nomVernaculaireDTO = nomVernaculaireService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nomVernaculaireDTO);
    }

    /**
     * {@code DELETE  /nom-vernaculaires/:id} : delete the "id" nomVernaculaire.
     *
     * @param id the id of the nomVernaculaireDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/nom-vernaculaires/{id}")
    public ResponseEntity<Void> deleteNomVernaculaire(@PathVariable Long id) {
        log.debug("REST request to delete NomVernaculaire : {}", id);
        nomVernaculaireService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
