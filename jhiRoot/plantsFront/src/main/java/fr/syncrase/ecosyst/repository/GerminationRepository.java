package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.Germination;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Germination entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GerminationRepository extends R2dbcRepository<Germination, Long>, GerminationRepositoryInternal {
    Flux<Germination> findAllBy(Pageable pageable);

    // just to avoid having unambigous methods
    @Override
    Flux<Germination> findAll();

    @Override
    Mono<Germination> findById(Long id);

    @Override
    <S extends Germination> Mono<S> save(S entity);
}

interface GerminationRepositoryInternal {
    <S extends Germination> Mono<S> insert(S entity);
    <S extends Germination> Mono<S> save(S entity);
    Mono<Integer> update(Germination entity);

    Flux<Germination> findAll();
    Mono<Germination> findById(Long id);
    Flux<Germination> findAllBy(Pageable pageable);
    Flux<Germination> findAllBy(Pageable pageable, Criteria criteria);
}
