package fr.syncrase.ecosyst.repository.rowmapper;

import fr.syncrase.ecosyst.domain.Allelopathie;
import fr.syncrase.ecosyst.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Allelopathie}, with proper type conversions.
 */
@Service
public class AllelopathieRowMapper implements BiFunction<Row, String, Allelopathie> {

    private final ColumnConverter converter;

    public AllelopathieRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Allelopathie} stored in the database.
     */
    @Override
    public Allelopathie apply(Row row, String prefix) {
        Allelopathie entity = new Allelopathie();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setType(converter.fromRow(row, prefix + "_type", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setImpact(converter.fromRow(row, prefix + "_impact", Integer.class));
        entity.setCibleId(converter.fromRow(row, prefix + "_cible_id", Long.class));
        entity.setOrigineId(converter.fromRow(row, prefix + "_origine_id", Long.class));
        return entity;
    }
}
