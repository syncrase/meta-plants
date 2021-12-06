package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.Semis;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Semis entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SemisRepository extends R2dbcRepository<Semis, Long>, SemisRepositoryInternal {
    Flux<Semis> findAllBy(Pageable pageable);

    @Query("SELECT * FROM semis entity WHERE entity.semis_pleine_terre_id = :id")
    Flux<Semis> findBySemisPleineTerre(Long id);

    @Query("SELECT * FROM semis entity WHERE entity.semis_pleine_terre_id IS NULL")
    Flux<Semis> findAllWhereSemisPleineTerreIsNull();

    @Query("SELECT * FROM semis entity WHERE entity.semis_sous_abris_id = :id")
    Flux<Semis> findBySemisSousAbris(Long id);

    @Query("SELECT * FROM semis entity WHERE entity.semis_sous_abris_id IS NULL")
    Flux<Semis> findAllWhereSemisSousAbrisIsNull();

    @Query("SELECT * FROM semis entity WHERE entity.type_semis_id = :id")
    Flux<Semis> findByTypeSemis(Long id);

    @Query("SELECT * FROM semis entity WHERE entity.type_semis_id IS NULL")
    Flux<Semis> findAllWhereTypeSemisIsNull();

    @Query("SELECT * FROM semis entity WHERE entity.germination_id = :id")
    Flux<Semis> findByGermination(Long id);

    @Query("SELECT * FROM semis entity WHERE entity.germination_id IS NULL")
    Flux<Semis> findAllWhereGerminationIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<Semis> findAll();

    @Override
    Mono<Semis> findById(Long id);

    @Override
    <S extends Semis> Mono<S> save(S entity);
}

interface SemisRepositoryInternal {
    <S extends Semis> Mono<S> insert(S entity);
    <S extends Semis> Mono<S> save(S entity);
    Mono<Integer> update(Semis entity);

    Flux<Semis> findAll();
    Mono<Semis> findById(Long id);
    Flux<Semis> findAllBy(Pageable pageable);
    Flux<Semis> findAllBy(Pageable pageable, Criteria criteria);
}
