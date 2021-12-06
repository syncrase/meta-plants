package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.Strate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Strate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StrateRepository extends R2dbcRepository<Strate, Long>, StrateRepositoryInternal {
    Flux<Strate> findAllBy(Pageable pageable);

    // just to avoid having unambigous methods
    @Override
    Flux<Strate> findAll();

    @Override
    Mono<Strate> findById(Long id);

    @Override
    <S extends Strate> Mono<S> save(S entity);
}

interface StrateRepositoryInternal {
    <S extends Strate> Mono<S> insert(S entity);
    <S extends Strate> Mono<S> save(S entity);
    Mono<Integer> update(Strate entity);

    Flux<Strate> findAll();
    Mono<Strate> findById(Long id);
    Flux<Strate> findAllBy(Pageable pageable);
    Flux<Strate> findAllBy(Pageable pageable, Criteria criteria);
}
