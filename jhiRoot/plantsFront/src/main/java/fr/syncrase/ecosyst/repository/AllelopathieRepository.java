package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.Allelopathie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Allelopathie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AllelopathieRepository extends R2dbcRepository<Allelopathie, Long>, AllelopathieRepositoryInternal {
    Flux<Allelopathie> findAllBy(Pageable pageable);

    @Query("SELECT * FROM allelopathie entity WHERE entity.cible_id = :id")
    Flux<Allelopathie> findByCible(Long id);

    @Query("SELECT * FROM allelopathie entity WHERE entity.cible_id IS NULL")
    Flux<Allelopathie> findAllWhereCibleIsNull();

    @Query("SELECT * FROM allelopathie entity WHERE entity.origine_id = :id")
    Flux<Allelopathie> findByOrigine(Long id);

    @Query("SELECT * FROM allelopathie entity WHERE entity.origine_id IS NULL")
    Flux<Allelopathie> findAllWhereOrigineIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<Allelopathie> findAll();

    @Override
    Mono<Allelopathie> findById(Long id);

    @Override
    <S extends Allelopathie> Mono<S> save(S entity);
}

interface AllelopathieRepositoryInternal {
    <S extends Allelopathie> Mono<S> insert(S entity);
    <S extends Allelopathie> Mono<S> save(S entity);
    Mono<Integer> update(Allelopathie entity);

    Flux<Allelopathie> findAll();
    Mono<Allelopathie> findById(Long id);
    Flux<Allelopathie> findAllBy(Pageable pageable);
    Flux<Allelopathie> findAllBy(Pageable pageable, Criteria criteria);
}
