package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.Cronquist;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Cronquist entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CronquistRepository extends R2dbcRepository<Cronquist, Long>, CronquistRepositoryInternal {
    Flux<Cronquist> findAllBy(Pageable pageable);

    // just to avoid having unambigous methods
    @Override
    Flux<Cronquist> findAll();

    @Override
    Mono<Cronquist> findById(Long id);

    @Override
    <S extends Cronquist> Mono<S> save(S entity);
}

interface CronquistRepositoryInternal {
    <S extends Cronquist> Mono<S> insert(S entity);
    <S extends Cronquist> Mono<S> save(S entity);
    Mono<Integer> update(Cronquist entity);

    Flux<Cronquist> findAll();
    Mono<Cronquist> findById(Long id);
    Flux<Cronquist> findAllBy(Pageable pageable);
    Flux<Cronquist> findAllBy(Pageable pageable, Criteria criteria);
}
