package fr.syncrase.ecosyst.repository.rowmapper;

import fr.syncrase.ecosyst.domain.Cronquist;
import fr.syncrase.ecosyst.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Cronquist}, with proper type conversions.
 */
@Service
public class CronquistRowMapper implements BiFunction<Row, String, Cronquist> {

    private final ColumnConverter converter;

    public CronquistRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Cronquist} stored in the database.
     */
    @Override
    public Cronquist apply(Row row, String prefix) {
        Cronquist entity = new Cronquist();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setRegne(converter.fromRow(row, prefix + "_regne", String.class));
        entity.setSousRegne(converter.fromRow(row, prefix + "_sous_regne", String.class));
        entity.setDivision(converter.fromRow(row, prefix + "_division", String.class));
        entity.setClasse(converter.fromRow(row, prefix + "_classe", String.class));
        entity.setSousClasse(converter.fromRow(row, prefix + "_sous_classe", String.class));
        entity.setOrdre(converter.fromRow(row, prefix + "_ordre", String.class));
        entity.setFamille(converter.fromRow(row, prefix + "_famille", String.class));
        entity.setGenre(converter.fromRow(row, prefix + "_genre", String.class));
        entity.setEspece(converter.fromRow(row, prefix + "_espece", String.class));
        return entity;
    }
}
