package fr.syncrase.ecosyst.repository.rowmapper;

import fr.syncrase.ecosyst.domain.NomVernaculaire;
import fr.syncrase.ecosyst.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link NomVernaculaire}, with proper type conversions.
 */
@Service
public class NomVernaculaireRowMapper implements BiFunction<Row, String, NomVernaculaire> {

    private final ColumnConverter converter;

    public NomVernaculaireRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link NomVernaculaire} stored in the database.
     */
    @Override
    public NomVernaculaire apply(Row row, String prefix) {
        NomVernaculaire entity = new NomVernaculaire();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNom(converter.fromRow(row, prefix + "_nom", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        return entity;
    }
}
