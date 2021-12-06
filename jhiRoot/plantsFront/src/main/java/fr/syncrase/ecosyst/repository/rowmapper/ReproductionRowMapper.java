package fr.syncrase.ecosyst.repository.rowmapper;

import fr.syncrase.ecosyst.domain.Reproduction;
import fr.syncrase.ecosyst.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Reproduction}, with proper type conversions.
 */
@Service
public class ReproductionRowMapper implements BiFunction<Row, String, Reproduction> {

    private final ColumnConverter converter;

    public ReproductionRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Reproduction} stored in the database.
     */
    @Override
    public Reproduction apply(Row row, String prefix) {
        Reproduction entity = new Reproduction();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setVitesse(converter.fromRow(row, prefix + "_vitesse", String.class));
        entity.setType(converter.fromRow(row, prefix + "_type", String.class));
        return entity;
    }
}
