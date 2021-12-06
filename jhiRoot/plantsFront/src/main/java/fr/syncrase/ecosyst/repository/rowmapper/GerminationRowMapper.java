package fr.syncrase.ecosyst.repository.rowmapper;

import fr.syncrase.ecosyst.domain.Germination;
import fr.syncrase.ecosyst.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Germination}, with proper type conversions.
 */
@Service
public class GerminationRowMapper implements BiFunction<Row, String, Germination> {

    private final ColumnConverter converter;

    public GerminationRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Germination} stored in the database.
     */
    @Override
    public Germination apply(Row row, String prefix) {
        Germination entity = new Germination();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setTempsDeGermination(converter.fromRow(row, prefix + "_temps_de_germination", String.class));
        entity.setConditionDeGermination(converter.fromRow(row, prefix + "_condition_de_germination", String.class));
        return entity;
    }
}
