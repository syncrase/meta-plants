package fr.syncrase.ecosyst.repository.rowmapper;

import fr.syncrase.ecosyst.domain.APGIII;
import fr.syncrase.ecosyst.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link APGIII}, with proper type conversions.
 */
@Service
public class APGIIIRowMapper implements BiFunction<Row, String, APGIII> {

    private final ColumnConverter converter;

    public APGIIIRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link APGIII} stored in the database.
     */
    @Override
    public APGIII apply(Row row, String prefix) {
        APGIII entity = new APGIII();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setOrdre(converter.fromRow(row, prefix + "_ordre", String.class));
        entity.setFamille(converter.fromRow(row, prefix + "_famille", String.class));
        return entity;
    }
}
