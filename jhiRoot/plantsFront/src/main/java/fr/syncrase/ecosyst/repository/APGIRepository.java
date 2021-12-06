package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.APGI;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the APGI entity.
 */
@SuppressWarnings("unused")
@Repository
public interface APGIRepository extends R2dbcRepository<APGI, Long>, APGIRepositoryInternal {
    Flux<APGI> findAllBy(Pageable pageable);

    // just to avoid having unambigous methods
    @Override
    Flux<APGI> findAll();

    @Override
    Mono<APGI> findById(Long id);

    @Override
    <S extends APGI> Mono<S> save(S entity);
}

interface APGIRepositoryInternal {
    <S extends APGI> Mono<S> insert(S entity);
    <S extends APGI> Mono<S> save(S entity);
    Mono<Integer> update(APGI entity);

    Flux<APGI> findAll();
    Mono<APGI> findById(Long id);
    Flux<APGI> findAllBy(Pageable pageable);
    Flux<APGI> findAllBy(Pageable pageable, Criteria criteria);
}
