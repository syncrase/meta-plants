package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.PeriodeAnnee;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the PeriodeAnnee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PeriodeAnneeRepository extends R2dbcRepository<PeriodeAnnee, Long>, PeriodeAnneeRepositoryInternal {
    Flux<PeriodeAnnee> findAllBy(Pageable pageable);

    @Query("SELECT * FROM periode_annee entity WHERE entity.debut_id = :id")
    Flux<PeriodeAnnee> findByDebut(Long id);

    @Query("SELECT * FROM periode_annee entity WHERE entity.debut_id IS NULL")
    Flux<PeriodeAnnee> findAllWhereDebutIsNull();

    @Query("SELECT * FROM periode_annee entity WHERE entity.fin_id = :id")
    Flux<PeriodeAnnee> findByFin(Long id);

    @Query("SELECT * FROM periode_annee entity WHERE entity.fin_id IS NULL")
    Flux<PeriodeAnnee> findAllWhereFinIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<PeriodeAnnee> findAll();

    @Override
    Mono<PeriodeAnnee> findById(Long id);

    @Override
    <S extends PeriodeAnnee> Mono<S> save(S entity);
}

interface PeriodeAnneeRepositoryInternal {
    <S extends PeriodeAnnee> Mono<S> insert(S entity);
    <S extends PeriodeAnnee> Mono<S> save(S entity);
    Mono<Integer> update(PeriodeAnnee entity);

    Flux<PeriodeAnnee> findAll();
    Mono<PeriodeAnnee> findById(Long id);
    Flux<PeriodeAnnee> findAllBy(Pageable pageable);
    Flux<PeriodeAnnee> findAllBy(Pageable pageable, Criteria criteria);
}
