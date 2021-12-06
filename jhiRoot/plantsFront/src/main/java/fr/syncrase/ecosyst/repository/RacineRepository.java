package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.Racine;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Racine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RacineRepository extends R2dbcRepository<Racine, Long>, RacineRepositoryInternal {
    Flux<Racine> findAllBy(Pageable pageable);

    // just to avoid having unambigous methods
    @Override
    Flux<Racine> findAll();

    @Override
    Mono<Racine> findById(Long id);

    @Override
    <S extends Racine> Mono<S> save(S entity);
}

interface RacineRepositoryInternal {
    <S extends Racine> Mono<S> insert(S entity);
    <S extends Racine> Mono<S> save(S entity);
    Mono<Integer> update(Racine entity);

    Flux<Racine> findAll();
    Mono<Racine> findById(Long id);
    Flux<Racine> findAllBy(Pageable pageable);
    Flux<Racine> findAllBy(Pageable pageable, Criteria criteria);
}
