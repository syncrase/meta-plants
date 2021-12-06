package fr.syncrase.ecosyst.repository.rowmapper;

import fr.syncrase.ecosyst.domain.Semis;
import fr.syncrase.ecosyst.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Semis}, with proper type conversions.
 */
@Service
public class SemisRowMapper implements BiFunction<Row, String, Semis> {

    private final ColumnConverter converter;

    public SemisRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Semis} stored in the database.
     */
    @Override
    public Semis apply(Row row, String prefix) {
        Semis entity = new Semis();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setSemisPleineTerreId(converter.fromRow(row, prefix + "_semis_pleine_terre_id", Long.class));
        entity.setSemisSousAbrisId(converter.fromRow(row, prefix + "_semis_sous_abris_id", Long.class));
        entity.setTypeSemisId(converter.fromRow(row, prefix + "_type_semis_id", Long.class));
        entity.setGerminationId(converter.fromRow(row, prefix + "_germination_id", Long.class));
        return entity;
    }
}
