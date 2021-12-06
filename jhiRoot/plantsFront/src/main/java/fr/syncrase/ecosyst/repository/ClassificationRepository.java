package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.Classification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Classification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClassificationRepository extends R2dbcRepository<Classification, Long>, ClassificationRepositoryInternal {
    Flux<Classification> findAllBy(Pageable pageable);

    @Query("SELECT * FROM classification entity WHERE entity.raunkier_id = :id")
    Flux<Classification> findByRaunkier(Long id);

    @Query("SELECT * FROM classification entity WHERE entity.raunkier_id IS NULL")
    Flux<Classification> findAllWhereRaunkierIsNull();

    @Query("SELECT * FROM classification entity WHERE entity.cronquist_id = :id")
    Flux<Classification> findByCronquist(Long id);

    @Query("SELECT * FROM classification entity WHERE entity.cronquist_id IS NULL")
    Flux<Classification> findAllWhereCronquistIsNull();

    @Query("SELECT * FROM classification entity WHERE entity.apg1_id = :id")
    Flux<Classification> findByApg1(Long id);

    @Query("SELECT * FROM classification entity WHERE entity.apg1_id IS NULL")
    Flux<Classification> findAllWhereApg1IsNull();

    @Query("SELECT * FROM classification entity WHERE entity.apg2_id = :id")
    Flux<Classification> findByApg2(Long id);

    @Query("SELECT * FROM classification entity WHERE entity.apg2_id IS NULL")
    Flux<Classification> findAllWhereApg2IsNull();

    @Query("SELECT * FROM classification entity WHERE entity.apg3_id = :id")
    Flux<Classification> findByApg3(Long id);

    @Query("SELECT * FROM classification entity WHERE entity.apg3_id IS NULL")
    Flux<Classification> findAllWhereApg3IsNull();

    @Query("SELECT * FROM classification entity WHERE entity.apg4_id = :id")
    Flux<Classification> findByApg4(Long id);

    @Query("SELECT * FROM classification entity WHERE entity.apg4_id IS NULL")
    Flux<Classification> findAllWhereApg4IsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<Classification> findAll();

    @Override
    Mono<Classification> findById(Long id);

    @Override
    <S extends Classification> Mono<S> save(S entity);
}

interface ClassificationRepositoryInternal {
    <S extends Classification> Mono<S> insert(S entity);
    <S extends Classification> Mono<S> save(S entity);
    Mono<Integer> update(Classification entity);

    Flux<Classification> findAll();
    Mono<Classification> findById(Long id);
    Flux<Classification> findAllBy(Pageable pageable);
    Flux<Classification> findAllBy(Pageable pageable, Criteria criteria);
}
