package fr.syncrase.ecosyst.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import fr.syncrase.ecosyst.domain.Allelopathie;
import fr.syncrase.ecosyst.repository.rowmapper.AllelopathieRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the Allelopathie entity.
 */
@SuppressWarnings("unused")
class AllelopathieRepositoryInternalImpl implements AllelopathieRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final PlanteRowMapper planteMapper;
    private final AllelopathieRowMapper allelopathieMapper;

    private static final Table entityTable = Table.aliased("allelopathie", EntityManager.ENTITY_ALIAS);
    private static final Table cibleTable = Table.aliased("plante", "cible");
    private static final Table origineTable = Table.aliased("plante", "origine");

    public AllelopathieRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PlanteRowMapper planteMapper,
        AllelopathieRowMapper allelopathieMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.planteMapper = planteMapper;
        this.allelopathieMapper = allelopathieMapper;
    }

    @Override
    public Flux<Allelopathie> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<Allelopathie> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<Allelopathie> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = AllelopathieSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(PlanteSqlHelper.getColumns(cibleTable, "cible"));
        columns.addAll(PlanteSqlHelper.getColumns(origineTable, "origine"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(cibleTable)
            .on(Column.create("cible_id", entityTable))
            .equals(Column.create("id", cibleTable))
            .leftOuterJoin(origineTable)
            .on(Column.create("origine_id", entityTable))
            .equals(Column.create("id", origineTable));

        String select = entityManager.createSelect(selectFrom, Allelopathie.class, pageable, criteria);
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
    public Flux<Allelopathie> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<Allelopathie> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private Allelopathie process(Row row, RowMetadata metadata) {
        Allelopathie entity = allelopathieMapper.apply(row, "e");
        entity.setCible(planteMapper.apply(row, "cible"));
        entity.setOrigine(planteMapper.apply(row, "origine"));
        return entity;
    }

    @Override
    public <S extends Allelopathie> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends Allelopathie> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(numberOfUpdates -> {
                    if (numberOfUpdates.intValue() <= 0) {
                        throw new IllegalStateException("Unable to update Allelopathie with id = " + entity.getId());
                    }
                    return entity;
                });
        }
    }

    @Override
    public Mono<Integer> update(Allelopathie entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}
