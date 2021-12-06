package fr.syncrase.ecosyst.repository.rowmapper;

import fr.syncrase.ecosyst.domain.Temperature;
import fr.syncrase.ecosyst.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Temperature}, with proper type conversions.
 */
@Service
public class TemperatureRowMapper implements BiFunction<Row, String, Temperature> {

    private final ColumnConverter converter;

    public TemperatureRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Temperature} stored in the database.
     */
    @Override
    public Temperature apply(Row row, String prefix) {
        Temperature entity = new Temperature();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setMin(converter.fromRow(row, prefix + "_min", Double.class));
        entity.setMax(converter.fromRow(row, prefix + "_max", Double.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setRusticite(converter.fromRow(row, prefix + "_rusticite", String.class));
        return entity;
    }
}
