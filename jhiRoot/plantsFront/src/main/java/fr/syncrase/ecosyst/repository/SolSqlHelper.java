package fr.syncrase.ecosyst.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class SolSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("ph_min", table, columnPrefix + "_ph_min"));
        columns.add(Column.aliased("ph_max", table, columnPrefix + "_ph_max"));
        columns.add(Column.aliased("type", table, columnPrefix + "_type"));
        columns.add(Column.aliased("richesse", table, columnPrefix + "_richesse"));

        columns.add(Column.aliased("plante_id", table, columnPrefix + "_plante_id"));
        return columns;
    }
}
