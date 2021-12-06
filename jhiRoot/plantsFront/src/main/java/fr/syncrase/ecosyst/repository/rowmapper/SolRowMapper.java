package fr.syncrase.ecosyst.repository.rowmapper;

import fr.syncrase.ecosyst.domain.Sol;
import fr.syncrase.ecosyst.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Sol}, with proper type conversions.
 */
@Service
public class SolRowMapper implements BiFunction<Row, String, Sol> {

    private final ColumnConverter converter;

    public SolRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Sol} stored in the database.
     */
    @Override
    public Sol apply(Row row, String prefix) {
        Sol entity = new Sol();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setPhMin(converter.fromRow(row, prefix + "_ph_min", Double.class));
        entity.setPhMax(converter.fromRow(row, prefix + "_ph_max", Double.class));
        entity.setType(converter.fromRow(row, prefix + "_type", String.class));
        entity.setRichesse(converter.fromRow(row, prefix + "_richesse", String.class));
        entity.setPlanteId(converter.fromRow(row, prefix + "_plante_id", Long.class));
        return entity;
    }
}
