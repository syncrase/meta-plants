package fr.syncrase.ecosyst.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class AllelopathieSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("type", table, columnPrefix + "_type"));
        columns.add(Column.aliased("description", table, columnPrefix + "_description"));
        columns.add(Column.aliased("impact", table, columnPrefix + "_impact"));

        columns.add(Column.aliased("cible_id", table, columnPrefix + "_cible_id"));
        columns.add(Column.aliased("origine_id", table, columnPrefix + "_origine_id"));
        return columns;
    }
}
