package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.APGIV;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the APGIV entity.
 */
@SuppressWarnings("unused")
@Repository
public interface APGIVRepository extends R2dbcRepository<APGIV, Long>, APGIVRepositoryInternal {
    Flux<APGIV> findAllBy(Pageable pageable);

    // just to avoid having unambigous methods
    @Override
    Flux<APGIV> findAll();

    @Override
    Mono<APGIV> findById(Long id);

    @Override
    <S extends APGIV> Mono<S> save(S entity);
}

interface APGIVRepositoryInternal {
    <S extends APGIV> Mono<S> insert(S entity);
    <S extends APGIV> Mono<S> save(S entity);
    Mono<Integer> update(APGIV entity);

    Flux<APGIV> findAll();
    Mono<APGIV> findById(Long id);
    Flux<APGIV> findAllBy(Pageable pageable);
    Flux<APGIV> findAllBy(Pageable pageable, Criteria criteria);
}
