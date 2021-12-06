package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.Ensoleillement;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Ensoleillement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EnsoleillementRepository extends R2dbcRepository<Ensoleillement, Long>, EnsoleillementRepositoryInternal {
    Flux<Ensoleillement> findAllBy(Pageable pageable);

    @Query("SELECT * FROM ensoleillement entity WHERE entity.plante_id = :id")
    Flux<Ensoleillement> findByPlante(Long id);

    @Query("SELECT * FROM ensoleillement entity WHERE entity.plante_id IS NULL")
    Flux<Ensoleillement> findAllWherePlanteIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<Ensoleillement> findAll();

    @Override
    Mono<Ensoleillement> findById(Long id);

    @Override
    <S extends Ensoleillement> Mono<S> save(S entity);
}

interface EnsoleillementRepositoryInternal {
    <S extends Ensoleillement> Mono<S> insert(S entity);
    <S extends Ensoleillement> Mono<S> save(S entity);
    Mono<Integer> update(Ensoleillement entity);

    Flux<Ensoleillement> findAll();
    Mono<Ensoleillement> findById(Long id);
    Flux<Ensoleillement> findAllBy(Pageable pageable);
    Flux<Ensoleillement> findAllBy(Pageable pageable, Criteria criteria);
}
