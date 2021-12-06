package fr.syncrase.ecosyst.repository.rowmapper;

import fr.syncrase.ecosyst.domain.Ensoleillement;
import fr.syncrase.ecosyst.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Ensoleillement}, with proper type conversions.
 */
@Service
public class EnsoleillementRowMapper implements BiFunction<Row, String, Ensoleillement> {

    private final ColumnConverter converter;

    public EnsoleillementRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Ensoleillement} stored in the database.
     */
    @Override
    public Ensoleillement apply(Row row, String prefix) {
        Ensoleillement entity = new Ensoleillement();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setOrientation(converter.fromRow(row, prefix + "_orientation", String.class));
        entity.setEnsoleilement(converter.fromRow(row, prefix + "_ensoleilement", Double.class));
        entity.setPlanteId(converter.fromRow(row, prefix + "_plante_id", Long.class));
        return entity;
    }
}
