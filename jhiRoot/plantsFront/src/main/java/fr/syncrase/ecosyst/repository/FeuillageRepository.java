package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.Feuillage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Feuillage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FeuillageRepository extends R2dbcRepository<Feuillage, Long>, FeuillageRepositoryInternal {
    Flux<Feuillage> findAllBy(Pageable pageable);

    // just to avoid having unambigous methods
    @Override
    Flux<Feuillage> findAll();

    @Override
    Mono<Feuillage> findById(Long id);

    @Override
    <S extends Feuillage> Mono<S> save(S entity);
}

interface FeuillageRepositoryInternal {
    <S extends Feuillage> Mono<S> insert(S entity);
    <S extends Feuillage> Mono<S> save(S entity);
    Mono<Integer> update(Feuillage entity);

    Flux<Feuillage> findAll();
    Mono<Feuillage> findById(Long id);
    Flux<Feuillage> findAllBy(Pageable pageable);
    Flux<Feuillage> findAllBy(Pageable pageable, Criteria criteria);
}
