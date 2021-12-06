package fr.syncrase.ecosyst.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import fr.syncrase.ecosyst.domain.PeriodeAnnee;
import fr.syncrase.ecosyst.repository.rowmapper.MoisRowMapper;
import fr.syncrase.ecosyst.repository.rowmapper.PeriodeAnneeRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the PeriodeAnnee entity.
 */
@SuppressWarnings("unused")
class PeriodeAnneeRepositoryInternalImpl implements PeriodeAnneeRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final MoisRowMapper moisMapper;
    private final PeriodeAnneeRowMapper periodeanneeMapper;

    private static final Table entityTable = Table.aliased("periode_annee", EntityManager.ENTITY_ALIAS);
    private static final Table debutTable = Table.aliased("mois", "debut");
    private static final Table finTable = Table.aliased("mois", "fin");

    public PeriodeAnneeRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        MoisRowMapper moisMapper,
        PeriodeAnneeRowMapper periodeanneeMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.moisMapper = moisMapper;
        this.periodeanneeMapper = periodeanneeMapper;
    }

    @Override
    public Flux<PeriodeAnnee> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<PeriodeAnnee> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<PeriodeAnnee> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = PeriodeAnneeSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(MoisSqlHelper.getColumns(debutTable, "debut"));
        columns.addAll(MoisSqlHelper.getColumns(finTable, "fin"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(debutTable)
            .on(Column.create("debut_id", entityTable))
            .equals(Column.create("id", debutTable))
            .leftOuterJoin(finTable)
            .on(Column.create("fin_id", entityTable))
            .equals(Column.create("id", finTable));

        String select = entityManager.createSelect(selectFrom, PeriodeAnnee.class, pageable, criteria);
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
    public Flux<PeriodeAnnee> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<PeriodeAnnee> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private PeriodeAnnee process(Row row, RowMetadata metadata) {
        PeriodeAnnee entity = periodeanneeMapper.apply(row, "e");
        entity.setDebut(moisMapper.apply(row, "debut"));
        entity.setFin(moisMapper.apply(row, "fin"));
        return entity;
    }

    @Override
    public <S extends PeriodeAnnee> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends PeriodeAnnee> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(numberOfUpdates -> {
                    if (numberOfUpdates.intValue() <= 0) {
                        throw new IllegalStateException("Unable to update PeriodeAnnee with id = " + entity.getId());
                    }
                    return entity;
                });
        }
    }

    @Override
    public Mono<Integer> update(PeriodeAnnee entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}
