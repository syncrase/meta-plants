package fr.syncrase.ecosyst.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import fr.syncrase.ecosyst.domain.Ensoleillement;
import fr.syncrase.ecosyst.repository.rowmapper.EnsoleillementRowMapper;
import fr.syncrase.ecosyst.repository.rowmapper.PlanteRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the Ensoleillement entity.
 */
@SuppressWarnings("unused")
class EnsoleillementRepositoryInternalImpl implements EnsoleillementRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final PlanteRowMapper planteMapper;
    private final EnsoleillementRowMapper ensoleillementMapper;

    private static final Table entityTable = Table.aliased("ensoleillement", EntityManager.ENTITY_ALIAS);
    private static final Table planteTable = Table.aliased("plante", "plante");

    public EnsoleillementRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PlanteRowMapper planteMapper,
        EnsoleillementRowMapper ensoleillementMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.planteMapper = planteMapper;
        this.ensoleillementMapper = ensoleillementMapper;
    }

    @Override
    public Flux<Ensoleillement> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<Ensoleillement> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<Ensoleillement> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = EnsoleillementSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(PlanteSqlHelper.getColumns(planteTable, "plante"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(planteTable)
            .on(Column.create("plante_id", entityTable))
            .equals(Column.create("id", planteTable));

        String select = entityManager.createSelect(selectFrom, Ensoleillement.class, pageable, criteria);
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
    public Flux<Ensoleillement> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<Ensoleillement> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private Ensoleillement process(Row row, RowMetadata metadata) {
        Ensoleillement entity = ensoleillementMapper.apply(row, "e");
        entity.setPlante(planteMapper.apply(row, "plante"));
        return entity;
    }

    @Override
    public <S extends Ensoleillement> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends Ensoleillement> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(numberOfUpdates -> {
                    if (numberOfUpdates.intValue() <= 0) {
                        throw new IllegalStateException("Unable to update Ensoleillement with id = " + entity.getId());
                    }
                    return entity;
                });
        }
    }

    @Override
    public Mono<Integer> update(Ensoleillement entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}
