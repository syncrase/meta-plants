package fr.syncrase.ecosyst.repository.rowmapper;

import fr.syncrase.ecosyst.domain.Classification;
import fr.syncrase.ecosyst.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Classification}, with proper type conversions.
 */
@Service
public class ClassificationRowMapper implements BiFunction<Row, String, Classification> {

    private final ColumnConverter converter;

    public ClassificationRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Classification} stored in the database.
     */
    @Override
    public Classification apply(Row row, String prefix) {
        Classification entity = new Classification();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNomLatin(converter.fromRow(row, prefix + "_nom_latin", String.class));
        entity.setRaunkierId(converter.fromRow(row, prefix + "_raunkier_id", Long.class));
        entity.setCronquistId(converter.fromRow(row, prefix + "_cronquist_id", Long.class));
        entity.setApg1Id(converter.fromRow(row, prefix + "_apg1_id", Long.class));
        entity.setApg2Id(converter.fromRow(row, prefix + "_apg2_id", Long.class));
        entity.setApg3Id(converter.fromRow(row, prefix + "_apg3_id", Long.class));
        entity.setApg4Id(converter.fromRow(row, prefix + "_apg4_id", Long.class));
        return entity;
    }
}
