package fr.syncrase.ecosyst.repository.rowmapper;

import fr.syncrase.ecosyst.domain.Plante;
import fr.syncrase.ecosyst.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Plante}, with proper type conversions.
 */
@Service
public class PlanteRowMapper implements BiFunction<Row, String, Plante> {

    private final ColumnConverter converter;

    public PlanteRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Plante} stored in the database.
     */
    @Override
    public Plante apply(Row row, String prefix) {
        Plante entity = new Plante();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setEntretien(converter.fromRow(row, prefix + "_entretien", String.class));
        entity.setHistoire(converter.fromRow(row, prefix + "_histoire", String.class));
        entity.setVitesseCroissance(converter.fromRow(row, prefix + "_vitesse_croissance", String.class));
        entity.setExposition(converter.fromRow(row, prefix + "_exposition", String.class));
        entity.setCycleDeVieId(converter.fromRow(row, prefix + "_cycle_de_vie_id", Long.class));
        entity.setClassificationId(converter.fromRow(row, prefix + "_classification_id", Long.class));
        entity.setTemperatureId(converter.fromRow(row, prefix + "_temperature_id", Long.class));
        entity.setRacineId(converter.fromRow(row, prefix + "_racine_id", Long.class));
        entity.setStrateId(converter.fromRow(row, prefix + "_strate_id", Long.class));
        entity.setFeuillageId(converter.fromRow(row, prefix + "_feuillage_id", Long.class));
        return entity;
    }
}
