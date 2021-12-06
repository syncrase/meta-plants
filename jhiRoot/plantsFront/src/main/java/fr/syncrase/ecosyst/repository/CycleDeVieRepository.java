package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.CycleDeVie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the CycleDeVie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CycleDeVieRepository extends R2dbcRepository<CycleDeVie, Long>, CycleDeVieRepositoryInternal {
    Flux<CycleDeVie> findAllBy(Pageable pageable);

    @Query("SELECT * FROM cycle_de_vie entity WHERE entity.semis_id = :id")
    Flux<CycleDeVie> findBySemis(Long id);

    @Query("SELECT * FROM cycle_de_vie entity WHERE entity.semis_id IS NULL")
    Flux<CycleDeVie> findAllWhereSemisIsNull();

    @Query("SELECT * FROM cycle_de_vie entity WHERE entity.apparition_feuilles_id = :id")
    Flux<CycleDeVie> findByApparitionFeuilles(Long id);

    @Query("SELECT * FROM cycle_de_vie entity WHERE entity.apparition_feuilles_id IS NULL")
    Flux<CycleDeVie> findAllWhereApparitionFeuillesIsNull();

    @Query("SELECT * FROM cycle_de_vie entity WHERE entity.floraison_id = :id")
    Flux<CycleDeVie> findByFloraison(Long id);

    @Query("SELECT * FROM cycle_de_vie entity WHERE entity.floraison_id IS NULL")
    Flux<CycleDeVie> findAllWhereFloraisonIsNull();

    @Query("SELECT * FROM cycle_de_vie entity WHERE entity.recolte_id = :id")
    Flux<CycleDeVie> findByRecolte(Long id);

    @Query("SELECT * FROM cycle_de_vie entity WHERE entity.recolte_id IS NULL")
    Flux<CycleDeVie> findAllWhereRecolteIsNull();

    @Query("SELECT * FROM cycle_de_vie entity WHERE entity.croissance_id = :id")
    Flux<CycleDeVie> findByCroissance(Long id);

    @Query("SELECT * FROM cycle_de_vie entity WHERE entity.croissance_id IS NULL")
    Flux<CycleDeVie> findAllWhereCroissanceIsNull();

    @Query("SELECT * FROM cycle_de_vie entity WHERE entity.maturite_id = :id")
    Flux<CycleDeVie> findByMaturite(Long id);

    @Query("SELECT * FROM cycle_de_vie entity WHERE entity.maturite_id IS NULL")
    Flux<CycleDeVie> findAllWhereMaturiteIsNull();

    @Query("SELECT * FROM cycle_de_vie entity WHERE entity.plantation_id = :id")
    Flux<CycleDeVie> findByPlantation(Long id);

    @Query("SELECT * FROM cycle_de_vie entity WHERE entity.plantation_id IS NULL")
    Flux<CycleDeVie> findAllWherePlantationIsNull();

    @Query("SELECT * FROM cycle_de_vie entity WHERE entity.rempotage_id = :id")
    Flux<CycleDeVie> findByRempotage(Long id);

    @Query("SELECT * FROM cycle_de_vie entity WHERE entity.rempotage_id IS NULL")
    Flux<CycleDeVie> findAllWhereRempotageIsNull();

    @Query("SELECT * FROM cycle_de_vie entity WHERE entity.reproduction_id = :id")
    Flux<CycleDeVie> findByReproduction(Long id);

    @Query("SELECT * FROM cycle_de_vie entity WHERE entity.reproduction_id IS NULL")
    Flux<CycleDeVie> findAllWhereReproductionIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<CycleDeVie> findAll();

    @Override
    Mono<CycleDeVie> findById(Long id);

    @Override
    <S extends CycleDeVie> Mono<S> save(S entity);
}

interface CycleDeVieRepositoryInternal {
    <S extends CycleDeVie> Mono<S> insert(S entity);
    <S extends CycleDeVie> Mono<S> save(S entity);
    Mono<Integer> update(CycleDeVie entity);

    Flux<CycleDeVie> findAll();
    Mono<CycleDeVie> findById(Long id);
    Flux<CycleDeVie> findAllBy(Pageable pageable);
    Flux<CycleDeVie> findAllBy(Pageable pageable, Criteria criteria);
}
