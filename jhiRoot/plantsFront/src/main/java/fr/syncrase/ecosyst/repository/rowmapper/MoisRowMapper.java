package fr.syncrase.ecosyst.repository.rowmapper;

import fr.syncrase.ecosyst.domain.Mois;
import fr.syncrase.ecosyst.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Mois}, with proper type conversions.
 */
@Service
public class MoisRowMapper implements BiFunction<Row, String, Mois> {

    private final ColumnConverter converter;

    public MoisRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Mois} stored in the database.
     */
    @Override
    public Mois apply(Row row, String prefix) {
        Mois entity = new Mois();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNumero(converter.fromRow(row, prefix + "_numero", Double.class));
        entity.setNom(converter.fromRow(row, prefix + "_nom", String.class));
        return entity;
    }
}
