package fr.syncrase.ecosyst.repository.rowmapper;

import fr.syncrase.ecosyst.domain.Feuillage;
import fr.syncrase.ecosyst.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Feuillage}, with proper type conversions.
 */
@Service
public class FeuillageRowMapper implements BiFunction<Row, String, Feuillage> {

    private final ColumnConverter converter;

    public FeuillageRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Feuillage} stored in the database.
     */
    @Override
    public Feuillage apply(Row row, String prefix) {
        Feuillage entity = new Feuillage();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setType(converter.fromRow(row, prefix + "_type", String.class));
        return entity;
    }
}
