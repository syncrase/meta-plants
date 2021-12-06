package fr.syncrase.ecosyst.repository;

import fr.syncrase.ecosyst.domain.TypeSemis;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the TypeSemis entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeSemisRepository extends R2dbcRepository<TypeSemis, Long>, TypeSemisRepositoryInternal {
    Flux<TypeSemis> findAllBy(Pageable pageable);

    // just to avoid having unambigous methods
    @Override
    Flux<TypeSemis> findAll();

    @Override
    Mono<TypeSemis> findById(Long id);

    @Override
    <S extends TypeSemis> Mono<S> save(S entity);
}

interface TypeSemisRepositoryInternal {
    <S extends TypeSemis> Mono<S> insert(S entity);
    <S extends TypeSemis> Mono<S> save(S entity);
    Mono<Integer> update(TypeSemis entity);

    Flux<TypeSemis> findAll();
    Mono<TypeSemis> findById(Long id);
    Flux<TypeSemis> findAllBy(Pageable pageable);
    Flux<TypeSemis> findAllBy(Pageable pageable, Criteria criteria);
}
