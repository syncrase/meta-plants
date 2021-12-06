package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.Reproduction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Reproduction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReproductionRepository extends R2dbcRepository<Reproduction, Long>, ReproductionRepositoryInternal {
    Flux<Reproduction> findAllBy(Pageable pageable);

    // just to avoid having unambigous methods
    @Override
    Flux<Reproduction> findAll();

    @Override
    Mono<Reproduction> findById(Long id);

    @Override
    <S extends Reproduction> Mono<S> save(S entity);
}

interface ReproductionRepositoryInternal {
    <S extends Reproduction> Mono<S> insert(S entity);
    <S extends Reproduction> Mono<S> save(S entity);
    Mono<Integer> update(Reproduction entity);

    Flux<Reproduction> findAll();
    Mono<Reproduction> findById(Long id);
    Flux<Reproduction> findAllBy(Pageable pageable);
    Flux<Reproduction> findAllBy(Pageable pageable, Criteria criteria);
}
