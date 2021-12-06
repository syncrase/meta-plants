package fr.syncrase.ecosyst.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import fr.syncrase.ecosyst.domain.Classification;
import fr.syncrase.ecosyst.repository.rowmapper.APGIIIRowMapper;
import fr.syncrase.ecosyst.repository.rowmapper.APGIIRowMapper;
import fr.syncrase.ecosyst.repository.rowmapper.APGIRowMapper;
import fr.syncrase.ecosyst.repository.rowmapper.APGIVRowMapper;
import fr.syncrase.ecosyst.repository.rowmapper.ClassificationRowMapper;
import fr.syncrase.ecosyst.repository.rowmapper.CronquistRowMapper;
import fr.syncrase.ecosyst.repository.rowmapper.RaunkierRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the Classification entity.
 */
@SuppressWarnings("unused")
class ClassificationRepositoryInternalImpl implements ClassificationRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final RaunkierRowMapper raunkierMapper;
    private final CronquistRowMapper cronquistMapper;
    private final APGIRowMapper apgiMapper;
    private final APGIIRowMapper apgiiMapper;
    private final APGIIIRowMapper apgiiiMapper;
    private final APGIVRowMapper apgivMapper;
    private final ClassificationRowMapper classificationMapper;

    private static final Table entityTable = Table.aliased("classification", EntityManager.ENTITY_ALIAS);
    private static final Table raunkierTable = Table.aliased("raunkier", "raunkier");
    private static final Table cronquistTable = Table.aliased("cronquist", "cronquist");
    private static final Table apg1Table = Table.aliased("apgi", "apg1");
    private static final Table apg2Table = Table.aliased("apgii", "apg2");
    private static final Table apg3Table = Table.aliased("apgiii", "apg3");
    private static final Table apg4Table = Table.aliased("apgiv", "apg4");

    public ClassificationRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        RaunkierRowMapper raunkierMapper,
        CronquistRowMapper cronquistMapper,
        APGIRowMapper apgiMapper,
        APGIIRowMapper apgiiMapper,
        APGIIIRowMapper apgiiiMapper,
        APGIVRowMapper apgivMapper,
        ClassificationRowMapper classificationMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.raunkierMapper = raunkierMapper;
        this.cronquistMapper = cronquistMapper;
        this.apgiMapper = apgiMapper;
        this.apgiiMapper = apgiiMapper;
        this.apgiiiMapper = apgiiiMapper;
        this.apgivMapper = apgivMapper;
        this.classificationMapper = classificationMapper;
    }

    @Override
    public Flux<Classification> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<Classification> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<Classification> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = ClassificationSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(RaunkierSqlHelper.getColumns(raunkierTable, "raunkier"));
        columns.addAll(CronquistSqlHelper.getColumns(cronquistTable, "cronquist"));
        columns.addAll(APGISqlHelper.getColumns(apg1Table, "apg1"));
        columns.addAll(APGIISqlHelper.getColumns(apg2Table, "apg2"));
        columns.addAll(APGIIISqlHelper.getColumns(apg3Table, "apg3"));
        columns.addAll(APGIVSqlHelper.getColumns(apg4Table, "apg4"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(raunkierTable)
            .on(Column.create("raunkier_id", entityTable))
            .equals(Column.create("id", raunkierTable))
            .leftOuterJoin(cronquistTable)
            .on(Column.create("cronquist_id", entityTable))
            .equals(Column.create("id", cronquistTable))
            .leftOuterJoin(apg1Table)
            .on(Column.create("apg1_id", entityTable))
            .equals(Column.create("id", apg1Table))
            .leftOuterJoin(apg2Table)
            .on(Column.create("apg2_id", entityTable))
            .equals(Column.create("id", apg2Table))
            .leftOuterJoin(apg3Table)
            .on(Column.create("apg3_id", entityTable))
            .equals(Column.create("id", apg3Table))
            .leftOuterJoin(apg4Table)
            .on(Column.create("apg4_id", entityTable))
            .equals(Column.create("id", apg4Table));

        String select = entityManager.createSelect(selectFrom, Classification.class, pageable, criteria);
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
    public Flux<Classification> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<Classification> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private Classification process(Row row, RowMetadata metadata) {
        Classification entity = classificationMapper.apply(row, "e");
        entity.setRaunkier(raunkierMapper.apply(row, "raunkier"));
        entity.setCronquist(cronquistMapper.apply(row, "cronquist"));
        entity.setApg1(apgiMapper.apply(row, "apg1"));
        entity.setApg2(apgiiMapper.apply(row, "apg2"));
        entity.setApg3(apgiiiMapper.apply(row, "apg3"));
        entity.setApg4(apgivMapper.apply(row, "apg4"));
        return entity;
    }

    @Override
    public <S extends Classification> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends Classification> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(numberOfUpdates -> {
                    if (numberOfUpdates.intValue() <= 0) {
                        throw new IllegalStateException("Unable to update Classification with id = " + entity.getId());
                    }
                    return entity;
                });
        }
    }

    @Override
    public Mono<Integer> update(Classification entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}
