package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.Sol;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Sol entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SolRepository extends R2dbcRepository<Sol, Long>, SolRepositoryInternal {
    Flux<Sol> findAllBy(Pageable pageable);

    @Query("SELECT * FROM sol entity WHERE entity.plante_id = :id")
    Flux<Sol> findByPlante(Long id);

    @Query("SELECT * FROM sol entity WHERE entity.plante_id IS NULL")
    Flux<Sol> findAllWherePlanteIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<Sol> findAll();

    @Override
    Mono<Sol> findById(Long id);

    @Override
    <S extends Sol> Mono<S> save(S entity);
}

interface SolRepositoryInternal {
    <S extends Sol> Mono<S> insert(S entity);
    <S extends Sol> Mono<S> save(S entity);
    Mono<Integer> update(Sol entity);

    Flux<Sol> findAll();
    Mono<Sol> findById(Long id);
    Flux<Sol> findAllBy(Pageable pageable);
    Flux<Sol> findAllBy(Pageable pageable, Criteria criteria);
}
