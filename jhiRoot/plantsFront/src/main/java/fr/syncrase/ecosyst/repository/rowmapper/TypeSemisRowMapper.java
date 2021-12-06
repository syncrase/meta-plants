package fr.syncrase.ecosyst.repository.rowmapper;

import fr.syncrase.ecosyst.domain.TypeSemis;
import fr.syncrase.ecosyst.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link TypeSemis}, with proper type conversions.
 */
@Service
public class TypeSemisRowMapper implements BiFunction<Row, String, TypeSemis> {

    private final ColumnConverter converter;

    public TypeSemisRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link TypeSemis} stored in the database.
     */
    @Override
    public TypeSemis apply(Row row, String prefix) {
        TypeSemis entity = new TypeSemis();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setType(converter.fromRow(row, prefix + "_type", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        return entity;
    }
}
