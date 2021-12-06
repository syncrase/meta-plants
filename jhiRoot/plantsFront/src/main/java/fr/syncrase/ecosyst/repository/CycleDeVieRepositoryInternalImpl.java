package fr.syncrase.ecosyst.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import fr.syncrase.ecosyst.domain.CycleDeVie;
import fr.syncrase.ecosyst.repository.rowmapper.CycleDeVieRowMapper;
import fr.syncrase.ecosyst.repository.rowmapper.PeriodeAnneeRowMapper;
import fr.syncrase.ecosyst.repository.rowmapper.ReproductionRowMapper;
import fr.syncrase.ecosyst.repository.rowmapper.SemisRowMapper;
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
 * Spring Data SQL reactive custom repository implementation for the CycleDeVie entity.
 */
@SuppressWarnings("unused")
class CycleDeVieRepositoryInternalImpl implements CycleDeVieRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final SemisRowMapper semisMapper;
    private final PeriodeAnneeRowMapper periodeanneeMapper;
    private final ReproductionRowMapper reproductionMapper;
    private final CycleDeVieRowMapper cycledevieMapper;

    private static final Table entityTable = Table.aliased("cycle_de_vie", EntityManager.ENTITY_ALIAS);
    private static final Table semisTable = Table.aliased("semis", "semis");
    private static final Table apparitionFeuillesTable = Table.aliased("periode_annee", "apparitionFeuilles");
    private static final Table floraisonTable = Table.aliased("periode_annee", "floraison");
    private static final Table recolteTable = Table.aliased("periode_annee", "recolte");
    private static final Table croissanceTable = Table.aliased("periode_annee", "croissance");
    private static final Table maturiteTable = Table.aliased("periode_annee", "maturite");
    private static final Table plantationTable = Table.aliased("periode_annee", "plantation");
    private static final Table rempotageTable = Table.aliased("periode_annee", "rempotage");
    private static final Table reproductionTable = Table.aliased("reproduction", "reproduction");

    public CycleDeVieRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        SemisRowMapper semisMapper,
        PeriodeAnneeRowMapper periodeanneeMapper,
        ReproductionRowMapper reproductionMapper,
        CycleDeVieRowMapper cycledevieMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.semisMapper = semisMapper;
        this.periodeanneeMapper = periodeanneeMapper;
        this.reproductionMapper = reproductionMapper;
        this.cycledevieMapper = cycledevieMapper;
    }

    @Override
    public Flux<CycleDeVie> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<CycleDeVie> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<CycleDeVie> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = CycleDeVieSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(SemisSqlHelper.getColumns(semisTable, "semis"));
        columns.addAll(PeriodeAnneeSqlHelper.getColumns(apparitionFeuillesTable, "apparitionFeuilles"));
        columns.addAll(PeriodeAnneeSqlHelper.getColumns(floraisonTable, "floraison"));
        columns.addAll(PeriodeAnneeSqlHelper.getColumns(recolteTable, "recolte"));
        columns.addAll(PeriodeAnneeSqlHelper.getColumns(croissanceTable, "croissance"));
        columns.addAll(PeriodeAnneeSqlHelper.getColumns(maturiteTable, "maturite"));
        columns.addAll(PeriodeAnneeSqlHelper.getColumns(plantationTable, "plantation"));
        columns.addAll(PeriodeAnneeSqlHelper.getColumns(rempotageTable, "rempotage"));
        columns.addAll(ReproductionSqlHelper.getColumns(reproductionTable, "reproduction"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(semisTable)
            .on(Column.create("semis_id", entityTable))
            .equals(Column.create("id", semisTable))
            .leftOuterJoin(apparitionFeuillesTable)
            .on(Column.create("apparition_feuilles_id", entityTable))
            .equals(Column.create("id", apparitionFeuillesTable))
            .leftOuterJoin(floraisonTable)
            .on(Column.create("floraison_id", entityTable))
            .equals(Column.create("id", floraisonTable))
            .leftOuterJoin(recolteTable)
            .on(Column.create("recolte_id", entityTable))
            .equals(Column.create("id", recolteTable))
            .leftOuterJoin(croissanceTable)
            .on(Column.create("croissance_id", entityTable))
            .equals(Column.create("id", croissanceTable))
            .leftOuterJoin(maturiteTable)
            .on(Column.create("maturite_id", entityTable))
            .equals(Column.create("id", maturiteTable))
            .leftOuterJoin(plantationTable)
            .on(Column.create("plantation_id", entityTable))
            .equals(Column.create("id", plantationTable))
            .leftOuterJoin(rempotageTable)
            .on(Column.create("rempotage_id", entityTable))
            .equals(Column.create("id", rempotageTable))
            .leftOuterJoin(reproductionTable)
            .on(Column.create("reproduction_id", entityTable))
            .equals(Column.create("id", reproductionTable));

        String select = entityManager.createSelect(selectFrom, CycleDeVie.class, pageable, criteria);
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
    public Flux<CycleDeVie> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<CycleDeVie> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private CycleDeVie process(Row row, RowMetadata metadata) {
        CycleDeVie entity = cycledevieMapper.apply(row, "e");
        entity.setSemis(semisMapper.apply(row, "semis"));
        entity.setApparitionFeuilles(periodeanneeMapper.apply(row, "apparitionFeuilles"));
        entity.setFloraison(periodeanneeMapper.apply(row, "floraison"));
        entity.setRecolte(periodeanneeMapper.apply(row, "recolte"));
        entity.setCroissance(periodeanneeMapper.apply(row, "croissance"));
        entity.setMaturite(periodeanneeMapper.apply(row, "maturite"));
        entity.setPlantation(periodeanneeMapper.apply(row, "plantation"));
        entity.setRempotage(periodeanneeMapper.apply(row, "rempotage"));
        entity.setReproduction(reproductionMapper.apply(row, "reproduction"));
        return entity;
    }

    @Override
    public <S extends CycleDeVie> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends CycleDeVie> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(numberOfUpdates -> {
                    if (numberOfUpdates.intValue() <= 0) {
                        throw new IllegalStateException("Unable to update CycleDeVie with id = " + entity.getId());
                    }
                    return entity;
                });
        }
    }

    @Override
    public Mono<Integer> update(CycleDeVie entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}
