package fr.syncrase.ecosyst.repository.rowmapper;

import fr.syncrase.ecosyst.domain.Raunkier;
import fr.syncrase.ecosyst.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Raunkier}, with proper type conversions.
 */
@Service
public class RaunkierRowMapper implements BiFunction<Row, String, Raunkier> {

    private final ColumnConverter converter;

    public RaunkierRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Raunkier} stored in the database.
     */
    @Override
    public Raunkier apply(Row row, String prefix) {
        Raunkier entity = new Raunkier();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setType(converter.fromRow(row, prefix + "_type", String.class));
        return entity;
    }
}
