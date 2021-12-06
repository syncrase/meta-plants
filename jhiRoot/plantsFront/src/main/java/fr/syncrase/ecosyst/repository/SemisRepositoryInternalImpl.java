package fr.syncrase.ecosyst.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import fr.syncrase.ecosyst.domain.Semis;
import fr.syncrase.ecosyst.repository.rowmapper.GerminationRowMapper;
import fr.syncrase.ecosyst.repository.rowmapper.PeriodeAnneeRowMapper;
import fr.syncrase.ecosyst.repository.rowmapper.SemisRowMapper;
import fr.syncrase.ecosyst.repository.rowmapper.TypeSemisRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the Semis entity.
 */
@SuppressWarnings("unused")
class SemisRepositoryInternalImpl implements SemisRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final PeriodeAnneeRowMapper periodeanneeMapper;
    private final TypeSemisRowMapper typesemisMapper;
    private final GerminationRowMapper germinationMapper;
    private final SemisRowMapper semisMapper;

    private static final Table entityTable = Table.aliased("semis", EntityManager.ENTITY_ALIAS);
    private static final Table semisPleineTerreTable = Table.aliased("periode_annee", "semisPleineTerre");
    private static final Table semisSousAbrisTable = Table.aliased("periode_annee", "semisSousAbris");
    private static final Table typeSemisTable = Table.aliased("type_semis", "typeSemis");
    private static final Table germinationTable = Table.aliased("germination", "germination");

    public SemisRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PeriodeAnneeRowMapper periodeanneeMapper,
        TypeSemisRowMapper typesemisMapper,
        GerminationRowMapper germinationMapper,
        SemisRowMapper semisMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.periodeanneeMapper = periodeanneeMapper;
        this.typesemisMapper = typesemisMapper;
        this.germinationMapper = germinationMapper;
        this.semisMapper = semisMapper;
    }

    @Override
    public Flux<Semis> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<Semis> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<Semis> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = SemisSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(PeriodeAnneeSqlHelper.getColumns(semisPleineTerreTable, "semisPleineTerre"));
        columns.addAll(PeriodeAnneeSqlHelper.getColumns(semisSousAbrisTable, "semisSousAbris"));
        columns.addAll(TypeSemisSqlHelper.getColumns(typeSemisTable, "typeSemis"));
        columns.addAll(GerminationSqlHelper.getColumns(germinationTable, "germination"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(semisPleineTerreTable)
            .on(Column.create("semis_pleine_terre_id", entityTable))
            .equals(Column.create("id", semisPleineTerreTable))
            .leftOuterJoin(semisSousAbrisTable)
            .on(Column.create("semis_sous_abris_id", entityTable))
            .equals(Column.create("id", semisSousAbrisTable))
            .leftOuterJoin(typeSemisTable)
            .on(Column.create("type_semis_id", entityTable))
            .equals(Column.create("id", typeSemisTable))
            .leftOuterJoin(germinationTable)
            .on(Column.create("germination_id", entityTable))
            .equals(Column.create("id", germinationTable));

        String select = entityManager.createSelect(selectFrom, Semis.class, pageable, criteria);
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
    public Flux<Semis> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<Semis> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private Semis process(Row row, RowMetadata metadata) {
        Semis entity = semisMapper.apply(row, "e");
        entity.setSemisPleineTerre(periodeanneeMapper.apply(row, "semisPleineTerre"));
        entity.setSemisSousAbris(periodeanneeMapper.apply(row, "semisSousAbris"));
        entity.setTypeSemis(typesemisMapper.apply(row, "typeSemis"));
        entity.setGermination(germinationMapper.apply(row, "germination"));
        return entity;
    }

    @Override
    public <S extends Semis> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends Semis> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(numberOfUpdates -> {
                    if (numberOfUpdates.intValue() <= 0) {
                        throw new IllegalStateException("Unable to update Semis with id = " + entity.getId());
                    }
                    return entity;
                });
        }
    }

    @Override
    public Mono<Integer> update(Semis entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}
