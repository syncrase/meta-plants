package fr.syncrase.ecosyst.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class EnsoleillementSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("orientation", table, columnPrefix + "_orientation"));
        columns.add(Column.aliased("ensoleilement", table, columnPrefix + "_ensoleilement"));

        columns.add(Column.aliased("plante_id", table, columnPrefix + "_plante_id"));
        return columns;
    }
}
