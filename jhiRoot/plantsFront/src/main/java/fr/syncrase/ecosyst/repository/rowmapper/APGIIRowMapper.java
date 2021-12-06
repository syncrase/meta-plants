package fr.syncrase.ecosyst.repository.rowmapper;

import fr.syncrase.ecosyst.domain.APGII;
import fr.syncrase.ecosyst.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link APGII}, with proper type conversions.
 */
@Service
public class APGIIRowMapper implements BiFunction<Row, String, APGII> {

    private final ColumnConverter converter;

    public APGIIRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link APGII} stored in the database.
     */
    @Override
    public APGII apply(Row row, String prefix) {
        APGII entity = new APGII();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setOrdre(converter.fromRow(row, prefix + "_ordre", String.class));
        entity.setFamille(converter.fromRow(row, prefix + "_famille", String.class));
        return entity;
    }
}
