package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.APGII;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the APGII entity.
 */
@SuppressWarnings("unused")
@Repository
public interface APGIIRepository extends R2dbcRepository<APGII, Long>, APGIIRepositoryInternal {
    Flux<APGII> findAllBy(Pageable pageable);

    // just to avoid having unambigous methods
    @Override
    Flux<APGII> findAll();

    @Override
    Mono<APGII> findById(Long id);

    @Override
    <S extends APGII> Mono<S> save(S entity);
}

interface APGIIRepositoryInternal {
    <S extends APGII> Mono<S> insert(S entity);
    <S extends APGII> Mono<S> save(S entity);
    Mono<Integer> update(APGII entity);

    Flux<APGII> findAll();
    Mono<APGII> findById(Long id);
    Flux<APGII> findAllBy(Pageable pageable);
    Flux<APGII> findAllBy(Pageable pageable, Criteria criteria);
}
