package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.Mois;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Mois entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MoisRepository extends R2dbcRepository<Mois, Long>, MoisRepositoryInternal {
    Flux<Mois> findAllBy(Pageable pageable);

    // just to avoid having unambigous methods
    @Override
    Flux<Mois> findAll();

    @Override
    Mono<Mois> findById(Long id);

    @Override
    <S extends Mois> Mono<S> save(S entity);
}

interface MoisRepositoryInternal {
    <S extends Mois> Mono<S> insert(S entity);
    <S extends Mois> Mono<S> save(S entity);
    Mono<Integer> update(Mois entity);

    Flux<Mois> findAll();
    Mono<Mois> findById(Long id);
    Flux<Mois> findAllBy(Pageable pageable);
    Flux<Mois> findAllBy(Pageable pageable, Criteria criteria);
}
