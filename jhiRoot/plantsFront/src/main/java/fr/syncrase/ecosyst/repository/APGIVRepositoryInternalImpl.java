package fr.syncrase.ecosyst.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import fr.syncrase.ecosyst.domain.APGIV;
import fr.syncrase.ecosyst.repository.rowmapper.APGIVRowMapper;
import fr.syncrase.ecosyst.service.EntityManager;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoin;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive custom repository implementation for the APGIV entity.
 */
@SuppressWarnings("unused")
class APGIVRepositoryInternalImpl implements APGIVRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final APGIVRowMapper apgivMapper;

    private static final Table entityTable = Table.aliased("apgiv", EntityManager.ENTITY_ALIAS);

    public APGIVRepositoryInternalImpl(R2dbcEntityTemplate template, EntityManager entityManager, APGIVRowMapper apgivMapper) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.apgivMapper = apgivMapper;
    }

    @Override
    public Flux<APGIV> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<APGIV> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<APGIV> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = APGIVSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        SelectFromAndJoin selectFrom = Select.builder().select(columns).from(entityTable);

        String select = entityManager.createSelect(selectFrom, APGIV.class, pageable, criteria);
        String alias = entityTable.getReferenceName().getReference();
        String selectWhere = Optional
            .ofNullable(criteria)
            .map(crit ->
                new StringBuilder(select)
                    .append(" ")
                    .append("WHERE")
                    .append(" ")
                    .append(alias)
                    .append(".")
                    .append(crit.toString())
                    .toString()
            )
            .orElse(select); // TODO remove once https://github.com/spring-projects/spring-data-jdbc/issues/907 will be fixed
        return db.sql(selectWhere).map(this::process);
    }

    @Override
    public Flux<APGIV> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<APGIV> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private APGIV process(Row row, RowMetadata metadata) {
        APGIV entity = apgivMapper.apply(row, "e");
        return entity;
    }

    @Override
    public <S extends APGIV> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends APGIV> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(numberOfUpdates -> {
                    if (numberOfUpdates.intValue() <= 0) {
                        throw new IllegalStateException("Unable to update APGIV with id = " + entity.getId());
                    }
                    return entity;
                });
        }
    }

    @Override
    public Mono<Integer> update(APGIV entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}
