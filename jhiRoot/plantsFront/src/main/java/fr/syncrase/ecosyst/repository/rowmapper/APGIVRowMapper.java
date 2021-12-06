package fr.syncrase.ecosyst.repository.rowmapper;

import fr.syncrase.ecosyst.domain.APGIV;
import fr.syncrase.ecosyst.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link APGIV}, with proper type conversions.
 */
@Service
public class APGIVRowMapper implements BiFunction<Row, String, APGIV> {

    private final ColumnConverter converter;

    public APGIVRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link APGIV} stored in the database.
     */
    @Override
    public APGIV apply(Row row, String prefix) {
        APGIV entity = new APGIV();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setOrdre(converter.fromRow(row, prefix + "_ordre", String.class));
        entity.setFamille(converter.fromRow(row, prefix + "_famille", String.class));
        return entity;
    }
}
