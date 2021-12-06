package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.APGIII;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the APGIII entity.
 */
@SuppressWarnings("unused")
@Repository
public interface APGIIIRepository extends R2dbcRepository<APGIII, Long>, APGIIIRepositoryInternal {
    Flux<APGIII> findAllBy(Pageable pageable);

    // just to avoid having unambigous methods
    @Override
    Flux<APGIII> findAll();

    @Override
    Mono<APGIII> findById(Long id);

    @Override
    <S extends APGIII> Mono<S> save(S entity);
}

interface APGIIIRepositoryInternal {
    <S extends APGIII> Mono<S> insert(S entity);
    <S extends APGIII> Mono<S> save(S entity);
    Mono<Integer> update(APGIII entity);

    Flux<APGIII> findAll();
    Mono<APGIII> findById(Long id);
    Flux<APGIII> findAllBy(Pageable pageable);
    Flux<APGIII> findAllBy(Pageable pageable, Criteria criteria);
}
