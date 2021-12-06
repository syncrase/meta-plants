package fr.syncrase.ecosyst.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class GerminationSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("temps_de_germination", table, columnPrefix + "_temps_de_germination"));
        columns.add(Column.aliased("condition_de_germination", table, columnPrefix + "_condition_de_germination"));

        return columns;
    }
}
