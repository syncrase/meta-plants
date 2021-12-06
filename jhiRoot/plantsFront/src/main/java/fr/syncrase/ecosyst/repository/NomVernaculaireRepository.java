package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.NomVernaculaire;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the NomVernaculaire entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NomVernaculaireRepository extends R2dbcRepository<NomVernaculaire, Long>, NomVernaculaireRepositoryInternal {
    Flux<NomVernaculaire> findAllBy(Pageable pageable);

    // just to avoid having unambigous methods
    @Override
    Flux<NomVernaculaire> findAll();

    @Override
    Mono<NomVernaculaire> findById(Long id);

    @Override
    <S extends NomVernaculaire> Mono<S> save(S entity);
}

interface NomVernaculaireRepositoryInternal {
    <S extends NomVernaculaire> Mono<S> insert(S entity);
    <S extends NomVernaculaire> Mono<S> save(S entity);
    Mono<Integer> update(NomVernaculaire entity);

    Flux<NomVernaculaire> findAll();
    Mono<NomVernaculaire> findById(Long id);
    Flux<NomVernaculaire> findAllBy(Pageable pageable);
    Flux<NomVernaculaire> findAllBy(Pageable pageable, Criteria criteria);
}
