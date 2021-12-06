package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.Temperature;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Temperature entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TemperatureRepository extends R2dbcRepository<Temperature, Long>, TemperatureRepositoryInternal {
    Flux<Temperature> findAllBy(Pageable pageable);

    // just to avoid having unambigous methods
    @Override
    Flux<Temperature> findAll();

    @Override
    Mono<Temperature> findById(Long id);

    @Override
    <S extends Temperature> Mono<S> save(S entity);
}

interface TemperatureRepositoryInternal {
    <S extends Temperature> Mono<S> insert(S entity);
    <S extends Temperature> Mono<S> save(S entity);
    Mono<Integer> update(Temperature entity);

    Flux<Temperature> findAll();
    Mono<Temperature> findById(Long id);
    Flux<Temperature> findAllBy(Pageable pageable);
    Flux<Temperature> findAllBy(Pageable pageable, Criteria criteria);
}
