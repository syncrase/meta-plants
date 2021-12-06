package fr.syncrase.ecosyst.repository.rowmapper;

import fr.syncrase.ecosyst.domain.Ressemblance;
import fr.syncrase.ecosyst.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Ressemblance}, with proper type conversions.
 */
@Service
public class RessemblanceRowMapper implements BiFunction<Row, String, Ressemblance> {

    private final ColumnConverter converter;

    public RessemblanceRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Ressemblance} stored in the database.
     */
    @Override
    public Ressemblance apply(Row row, String prefix) {
        Ressemblance entity = new Ressemblance();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setPlanteRessemblantId(converter.fromRow(row, prefix + "_plante_ressemblant_id", Long.class));
        return entity;
    }
}
