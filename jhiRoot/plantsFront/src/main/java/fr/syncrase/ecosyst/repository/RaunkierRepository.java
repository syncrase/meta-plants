package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.Raunkier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Raunkier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RaunkierRepository extends R2dbcRepository<Raunkier, Long>, RaunkierRepositoryInternal {
    Flux<Raunkier> findAllBy(Pageable pageable);

    // just to avoid having unambigous methods
    @Override
    Flux<Raunkier> findAll();

    @Override
    Mono<Raunkier> findById(Long id);

    @Override
    <S extends Raunkier> Mono<S> save(S entity);
}

interface RaunkierRepositoryInternal {
    <S extends Raunkier> Mono<S> insert(S entity);
    <S extends Raunkier> Mono<S> save(S entity);
    Mono<Integer> update(Raunkier entity);

    Flux<Raunkier> findAll();
    Mono<Raunkier> findById(Long id);
    Flux<Raunkier> findAllBy(Pageable pageable);
    Flux<Raunkier> findAllBy(Pageable pageable, Criteria criteria);
}
