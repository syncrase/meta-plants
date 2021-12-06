package fr.syncrase.ecosyst.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class PlanteSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("entretien", table, columnPrefix + "_entretien"));
        columns.add(Column.aliased("histoire", table, columnPrefix + "_histoire"));
        columns.add(Column.aliased("vitesse_croissance", table, columnPrefix + "_vitesse_croissance"));
        columns.add(Column.aliased("exposition", table, columnPrefix + "_exposition"));

        columns.add(Column.aliased("cycle_de_vie_id", table, columnPrefix + "_cycle_de_vie_id"));
        columns.add(Column.aliased("classification_id", table, columnPrefix + "_classification_id"));
        columns.add(Column.aliased("temperature_id", table, columnPrefix + "_temperature_id"));
        columns.add(Column.aliased("racine_id", table, columnPrefix + "_racine_id"));
        columns.add(Column.aliased("strate_id", table, columnPrefix + "_strate_id"));
        columns.add(Column.aliased("feuillage_id", table, columnPrefix + "_feuillage_id"));
        return columns;
    }
}
