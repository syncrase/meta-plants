package fr.syncrase.ecosyst.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import fr.syncrase.ecosyst.domain.Sol;
import fr.syncrase.ecosyst.repository.rowmapper.PlanteRowMapper;
import fr.syncrase.ecosyst.repository.rowmapper.SolRowMapper;
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
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive custom repository implementation for the Sol entity.
 */
@SuppressWarnings("unused")
class SolRepositoryInternalImpl implements SolRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final PlanteRowMapper planteMapper;
    private final SolRowMapper solMapper;

    private static final Table entityTable = Table.aliased("sol", EntityManager.ENTITY_ALIAS);
    private static final Table planteTable = Table.aliased("plante", "plante");

    public SolRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PlanteRowMapper planteMapper,
        SolRowMapper solMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.planteMapper = planteMapper;
        this.solMapper = solMapper;
    }

    @Override
    public Flux<Sol> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<Sol> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<Sol> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = SolSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(PlanteSqlHelper.getColumns(planteTable, "plante"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(planteTable)
            .on(Column.create("plante_id", entityTable))
            .equals(Column.create("id", planteTable));

        String select = entityManager.createSelect(selectFrom, Sol.class, pageable, criteria);
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
    public Flux<Sol> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<Sol> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private Sol process(Row row, RowMetadata metadata) {
        Sol entity = solMapper.apply(row, "e");
        entity.setPlante(planteMapper.apply(row, "plante"));
        return entity;
    }

    @Override
    public <S extends Sol> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends Sol> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(numberOfUpdates -> {
                    if (numberOfUpdates.intValue() <= 0) {
                        throw new IllegalStateException("Unable to update Sol with id = " + entity.getId());
                    }
                    return entity;
                });
        }
    }

    @Override
    public Mono<Integer> update(Sol entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}
