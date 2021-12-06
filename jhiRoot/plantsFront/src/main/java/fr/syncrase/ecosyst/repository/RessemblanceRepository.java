package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.Ressemblance;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Ressemblance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RessemblanceRepository extends R2dbcRepository<Ressemblance, Long>, RessemblanceRepositoryInternal {
    Flux<Ressemblance> findAllBy(Pageable pageable);

    @Query("SELECT * FROM ressemblance entity WHERE entity.plante_ressemblant_id = :id")
    Flux<Ressemblance> findByPlanteRessemblant(Long id);

    @Query("SELECT * FROM ressemblance entity WHERE entity.plante_ressemblant_id IS NULL")
    Flux<Ressemblance> findAllWherePlanteRessemblantIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<Ressemblance> findAll();

    @Override
    Mono<Ressemblance> findById(Long id);

    @Override
    <S extends Ressemblance> Mono<S> save(S entity);
}

interface RessemblanceRepositoryInternal {
    <S extends Ressemblance> Mono<S> insert(S entity);
    <S extends Ressemblance> Mono<S> save(S entity);
    Mono<Integer> update(Ressemblance entity);

    Flux<Ressemblance> findAll();
    Mono<Ressemblance> findById(Long id);
    Flux<Ressemblance> findAllBy(Pageable pageable);
    Flux<Ressemblance> findAllBy(Pageable pageable, Criteria criteria);
}
