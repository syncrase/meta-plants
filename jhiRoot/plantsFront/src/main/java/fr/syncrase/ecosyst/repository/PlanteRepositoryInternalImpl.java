package fr.syncrase.ecosyst.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import fr.syncrase.ecosyst.domain.NomVernaculaire;
import fr.syncrase.ecosyst.domain.Plante;
import fr.syncrase.ecosyst.repository.rowmapper.ClassificationRowMapper;
import fr.syncrase.ecosyst.repository.rowmapper.CycleDeVieRowMapper;
import fr.syncrase.ecosyst.repository.rowmapper.FeuillageRowMapper;
import fr.syncrase.ecosyst.repository.rowmapper.PlanteRowMapper;
import fr.syncrase.ecosyst.repository.rowmapper.RacineRowMapper;
import fr.syncrase.ecosyst.repository.rowmapper.StrateRowMapper;
import fr.syncrase.ecosyst.repository.rowmapper.TemperatureRowMapper;
import fr.syncrase.ecosyst.service.EntityManager;
import fr.syncrase.ecosyst.service.EntityManager.LinkTable;
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
 * Spring Data SQL reactive custom repository implementation for the Plante entity.
 */
@SuppressWarnings("unused")
class PlanteRepositoryInternalImpl implements PlanteRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CycleDeVieRowMapper cycledevieMapper;
    private final ClassificationRowMapper classificationMapper;
    private final TemperatureRowMapper temperatureMapper;
    private final RacineRowMapper racineMapper;
    private final StrateRowMapper strateMapper;
    private final FeuillageRowMapper feuillageMapper;
    private final PlanteRowMapper planteMapper;

    private static final Table entityTable = Table.aliased("plante", EntityManager.ENTITY_ALIAS);
    private static final Table cycleDeVieTable = Table.aliased("cycle_de_vie", "cycleDeVie");
    private static final Table classificationTable = Table.aliased("classification", "classification");
    private static final Table temperatureTable = Table.aliased("temperature", "temperature");
    private static final Table racineTable = Table.aliased("racine", "racine");
    private static final Table strateTable = Table.aliased("strate", "strate");
    private static final Table feuillageTable = Table.aliased("feuillage", "feuillage");

    private static final EntityManager.LinkTable nomsVernaculairesLink = new LinkTable(
        "rel_plante__noms_vernaculaires",
        "plante_id",
        "noms_vernaculaires_id"
    );

    public PlanteRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CycleDeVieRowMapper cycledevieMapper,
        ClassificationRowMapper classificationMapper,
        TemperatureRowMapper temperatureMapper,
        RacineRowMapper racineMapper,
        StrateRowMapper strateMapper,
        FeuillageRowMapper feuillageMapper,
        PlanteRowMapper planteMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.cycledevieMapper = cycledevieMapper;
        this.classificationMapper = classificationMapper;
        this.temperatureMapper = temperatureMapper;
        this.racineMapper = racineMapper;
        this.strateMapper = strateMapper;
        this.feuillageMapper = feuillageMapper;
        this.planteMapper = planteMapper;
    }

    @Override
    public Flux<Plante> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<Plante> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<Plante> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = PlanteSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(CycleDeVieSqlHelper.getColumns(cycleDeVieTable, "cycleDeVie"));
        columns.addAll(ClassificationSqlHelper.getColumns(classificationTable, "classification"));
        columns.addAll(TemperatureSqlHelper.getColumns(temperatureTable, "temperature"));
        columns.addAll(RacineSqlHelper.getColumns(racineTable, "racine"));
        columns.addAll(StrateSqlHelper.getColumns(strateTable, "strate"));
        columns.addAll(FeuillageSqlHelper.getColumns(feuillageTable, "feuillage"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(cycleDeVieTable)
            .on(Column.create("cycle_de_vie_id", entityTable))
            .equals(Column.create("id", cycleDeVieTable))
            .leftOuterJoin(classificationTable)
            .on(Column.create("classification_id", entityTable))
            .equals(Column.create("id", classificationTable))
            .leftOuterJoin(temperatureTable)
            .on(Column.create("temperature_id", entityTable))
            .equals(Column.create("id", temperatureTable))
            .leftOuterJoin(racineTable)
            .on(Column.create("racine_id", entityTable))
            .equals(Column.create("id", racineTable))
            .leftOuterJoin(strateTable)
            .on(Column.create("strate_id", entityTable))
            .equals(Column.create("id", strateTable))
            .leftOuterJoin(feuillageTable)
            .on(Column.create("feuillage_id", entityTable))
            .equals(Column.create("id", feuillageTable));

        String select = entityManager.createSelect(selectFrom, Plante.class, pageable, criteria);
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
    public Flux<Plante> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<Plante> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    @Override
    public Mono<Plante> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Plante> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Plante> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Plante process(Row row, RowMetadata metadata) {
        Plante entity = planteMapper.apply(row, "e");
        entity.setCycleDeVie(cycledevieMapper.apply(row, "cycleDeVie"));
        entity.setClassification(classificationMapper.apply(row, "classification"));
        entity.setTemperature(temperatureMapper.apply(row, "temperature"));
        entity.setRacine(racineMapper.apply(row, "racine"));
        entity.setStrate(strateMapper.apply(row, "strate"));
        entity.setFeuillage(feuillageMapper.apply(row, "feuillage"));
        return entity;
    }

    @Override
    public <S extends Plante> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends Plante> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity).flatMap(savedEntity -> updateRelations(savedEntity));
        } else {
            return update(entity)
                .map(numberOfUpdates -> {
                    if (numberOfUpdates.intValue() <= 0) {
                        throw new IllegalStateException("Unable to update Plante with id = " + entity.getId());
                    }
                    return entity;
                })
                .then(updateRelations(entity));
        }
    }

    @Override
    public Mono<Integer> update(Plante entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }

    @Override
    public Mono<Void> deleteById(Long entityId) {
        return deleteRelations(entityId)
            .then(r2dbcEntityTemplate.delete(Plante.class).matching(query(where("id").is(entityId))).all().then());
    }

    protected <S extends Plante> Mono<S> updateRelations(S entity) {
        Mono<Void> result = entityManager
            .updateLinkTable(nomsVernaculairesLink, entity.getId(), entity.getNomsVernaculaires().stream().map(NomVernaculaire::getId))
            .then();
        return result.thenReturn(entity);
    }

    protected Mono<Void> deleteRelations(Long entityId) {
        return entityManager.deleteFromLinkTable(nomsVernaculairesLink, entityId);
    }
}
