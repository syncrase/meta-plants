package fr.syncrase.ecosyst.repository.rowmapper;

import fr.syncrase.ecosyst.domain.APGI;
import fr.syncrase.ecosyst.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link APGI}, with proper type conversions.
 */
@Service
public class APGIRowMapper implements BiFunction<Row, String, APGI> {

    private final ColumnConverter converter;

    public APGIRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link APGI} stored in the database.
     */
    @Override
    public APGI apply(Row row, String prefix) {
        APGI entity = new APGI();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setOrdre(converter.fromRow(row, prefix + "_ordre", String.class));
        entity.setFamille(converter.fromRow(row, prefix + "_famille", String.class));
        return entity;
    }
}
