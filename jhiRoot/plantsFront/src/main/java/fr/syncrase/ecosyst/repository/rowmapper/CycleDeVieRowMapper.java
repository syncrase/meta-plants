package fr.syncrase.ecosyst.repository.rowmapper;

import fr.syncrase.ecosyst.domain.CycleDeVie;
import fr.syncrase.ecosyst.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link CycleDeVie}, with proper type conversions.
 */
@Service
public class CycleDeVieRowMapper implements BiFunction<Row, String, CycleDeVie> {

    private final ColumnConverter converter;

    public CycleDeVieRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link CycleDeVie} stored in the database.
     */
    @Override
    public CycleDeVie apply(Row row, String prefix) {
        CycleDeVie entity = new CycleDeVie();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setSemisId(converter.fromRow(row, prefix + "_semis_id", Long.class));
        entity.setApparitionFeuillesId(converter.fromRow(row, prefix + "_apparition_feuilles_id", Long.class));
        entity.setFloraisonId(converter.fromRow(row, prefix + "_floraison_id", Long.class));
        entity.setRecolteId(converter.fromRow(row, prefix + "_recolte_id", Long.class));
        entity.setCroissanceId(converter.fromRow(row, prefix + "_croissance_id", Long.class));
        entity.setMaturiteId(converter.fromRow(row, prefix + "_maturite_id", Long.class));
        entity.setPlantationId(converter.fromRow(row, prefix + "_plantation_id", Long.class));
        entity.setRempotageId(converter.fromRow(row, prefix + "_rempotage_id", Long.class));
        entity.setReproductionId(converter.fromRow(row, prefix + "_reproduction_id", Long.class));
        return entity;
    }
}
