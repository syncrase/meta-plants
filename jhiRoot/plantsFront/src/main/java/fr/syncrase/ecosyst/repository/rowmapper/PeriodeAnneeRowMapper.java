package fr.syncrase.ecosyst.repository.rowmapper;

import fr.syncrase.ecosyst.domain.PeriodeAnnee;
import fr.syncrase.ecosyst.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link PeriodeAnnee}, with proper type conversions.
 */
@Service
public class PeriodeAnneeRowMapper implements BiFunction<Row, String, PeriodeAnnee> {

    private final ColumnConverter converter;

    public PeriodeAnneeRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link PeriodeAnnee} stored in the database.
     */
    @Override
    public PeriodeAnnee apply(Row row, String prefix) {
        PeriodeAnnee entity = new PeriodeAnnee();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setDebutId(converter.fromRow(row, prefix + "_debut_id", Long.class));
        entity.setFinId(converter.fromRow(row, prefix + "_fin_id", Long.class));
        return entity;
    }
}
