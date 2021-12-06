package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.Plante;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Plante entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlanteRepository extends R2dbcRepository<Plante, Long>, PlanteRepositoryInternal {
    Flux<Plante> findAllBy(Pageable pageable);

    @Override
    Mono<Plante> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Plante> findAllWithEagerRelationships();

    @Override
    Flux<Plante> findAllWithEagerRelationships(Pageable page);

    @Override
    Mono<Void> deleteById(Long id);

    @Query("SELECT * FROM plante entity WHERE entity.cycle_de_vie_id = :id")
    Flux<Plante> findByCycleDeVie(Long id);

    @Query("SELECT * FROM plante entity WHERE entity.cycle_de_vie_id IS NULL")
    Flux<Plante> findAllWhereCycleDeVieIsNull();

    @Query("SELECT * FROM plante entity WHERE entity.classification_id = :id")
    Flux<Plante> findByClassification(Long id);

    @Query("SELECT * FROM plante entity WHERE entity.classification_id IS NULL")
    Flux<Plante> findAllWhereClassificationIsNull();

    @Query(
        "SELECT entity.* FROM plante entity JOIN rel_plante__noms_vernaculaires joinTable ON entity.id = joinTable.plante_id WHERE joinTable.noms_vernaculaires_id = :id"
    )
    Flux<Plante> findByNomsVernaculaires(Long id);

    @Query("SELECT * FROM plante entity WHERE entity.temperature_id = :id")
    Flux<Plante> findByTemperature(Long id);

    @Query("SELECT * FROM plante entity WHERE entity.temperature_id IS NULL")
    Flux<Plante> findAllWhereTemperatureIsNull();

    @Query("SELECT * FROM plante entity WHERE entity.racine_id = :id")
    Flux<Plante> findByRacine(Long id);

    @Query("SELECT * FROM plante entity WHERE entity.racine_id IS NULL")
    Flux<Plante> findAllWhereRacineIsNull();

    @Query("SELECT * FROM plante entity WHERE entity.strate_id = :id")
    Flux<Plante> findByStrate(Long id);

    @Query("SELECT * FROM plante entity WHERE entity.strate_id IS NULL")
    Flux<Plante> findAllWhereStrateIsNull();

    @Query("SELECT * FROM plante entity WHERE entity.feuillage_id = :id")
    Flux<Plante> findByFeuillage(Long id);

    @Query("SELECT * FROM plante entity WHERE entity.feuillage_id IS NULL")
    Flux<Plante> findAllWhereFeuillageIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<Plante> findAll();

    @Override
    Mono<Plante> findById(Long id);

    @Override
    <S extends Plante> Mono<S> save(S entity);
}

interface PlanteRepositoryInternal {
    <S extends Plante> Mono<S> insert(S entity);
    <S extends Plante> Mono<S> save(S entity);
    Mono<Integer> update(Plante entity);

    Flux<Plante> findAll();
    Mono<Plante> findById(Long id);
    Flux<Plante> findAllBy(Pageable pageable);
    Flux<Plante> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Plante> findOneWithEagerRelationships(Long id);

    Flux<Plante> findAllWithEagerRelationships();

    Flux<Plante> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
