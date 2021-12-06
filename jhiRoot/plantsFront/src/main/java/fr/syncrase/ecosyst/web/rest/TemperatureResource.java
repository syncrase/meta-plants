package fr.syncrase.ecosyst.web.rest;

import fr.syncrase.ecosyst.domain.Temperature;
import fr.syncrase.ecosyst.repository.TemperatureRepository;
import fr.syncrase.ecosyst.service.TemperatureService;
import fr.syncrase.ecosyst.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link fr.syncrase.ecosyst.domain.Temperature}.
 */
@RestController
@RequestMapping("/api")
public class TemperatureResource {

    private final Logger log = LoggerFactory.getLogger(TemperatureResource.class);

    private static final String ENTITY_NAME = "temperature";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TemperatureService temperatureService;

    private final TemperatureRepository temperatureRepository;

    public TemperatureResource(TemperatureService temperatureService, TemperatureRepository temperatureRepository) {
        this.temperatureService = temperatureService;
        this.temperatureRepository = temperatureRepository;
    }

    /**
     * {@code POST  /temperatures} : Create a new temperature.
     *
     * @param temperature the temperature to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new temperature, or with status {@code 400 (Bad Request)} if the temperature has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/temperatures")
    public Mono<ResponseEntity<Temperature>> createTemperature(@RequestBody Temperature temperature) throws URISyntaxException {
        log.debug("REST request to save Temperature : {}", temperature);
        if (temperature.getId() != null) {
            throw new BadRequestAlertException("A new temperature cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return temperatureService
            .save(temperature)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/temperatures/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /temperatures/:id} : Updates an existing temperature.
     *
     * @param id the id of the temperature to save.
     * @param temperature the temperature to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated temperature,
     * or with status {@code 400 (Bad Request)} if the temperature is not valid,
     * or with status {@code 500 (Internal Server Error)} if the temperature couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/temperatures/{id}")
    public Mono<ResponseEntity<Temperature>> updateTemperature(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Temperature temperature
    ) throws URISyntaxException {
        log.debug("REST request to update Temperature : {}, {}", id, temperature);
        if (temperature.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, temperature.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return temperatureRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return temperatureService
                    .save(temperature)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /temperatures/:id} : Partial updates given fields of an existing temperature, field will ignore if it is null
     *
     * @param id the id of the temperature to save.
     * @param temperature the temperature to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated temperature,
     * or with status {@code 400 (Bad Request)} if the temperature is not valid,
     * or with status {@code 404 (Not Found)} if the temperature is not found,
     * or with status {@code 500 (Internal Server Error)} if the temperature couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/temperatures/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Temperature>> partialUpdateTemperature(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Temperature temperature
    ) throws URISyntaxException {
        log.debug("REST request to partial update Temperature partially : {}, {}", id, temperature);
        if (temperature.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, temperature.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return temperatureRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Temperature> result = temperatureService.partialUpdate(temperature);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /temperatures} : get all the temperatures.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of temperatures in body.
     */
    @GetMapping("/temperatures")
    public Mono<ResponseEntity<List<Temperature>>> getAllTemperatures(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of Temperatures");
        return temperatureService
            .countAll()
            .zipWith(temperatureService.findAll(pageable).collectList())
            .map(countWithEntities -> {
                return ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            UriComponentsBuilder.fromHttpRequest(request),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2());
            });
    }

    /**
     * {@code GET  /temperatures/:id} : get the "id" temperature.
     *
     * @param id the id of the temperature to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the temperature, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/temperatures/{id}")
    public Mono<ResponseEntity<Temperature>> getTemperature(@PathVariable Long id) {
        log.debug("REST request to get Temperature : {}", id);
        Mono<Temperature> temperature = temperatureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(temperature);
    }

    /**
     * {@code DELETE  /temperatures/:id} : delete the "id" temperature.
     *
     * @param id the id of the temperature to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/temperatures/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteTemperature(@PathVariable Long id) {
        log.debug("REST request to delete Temperature : {}", id);
        return temperatureService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
