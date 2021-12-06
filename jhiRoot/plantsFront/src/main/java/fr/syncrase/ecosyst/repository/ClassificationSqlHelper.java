package fr.syncrase.ecosyst.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ClassificationSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("nom_latin", table, columnPrefix + "_nom_latin"));

        columns.add(Column.aliased("raunkier_id", table, columnPrefix + "_raunkier_id"));
        columns.add(Column.aliased("cronquist_id", table, columnPrefix + "_cronquist_id"));
        columns.add(Column.aliased("apg1_id", table, columnPrefix + "_apg1_id"));
        columns.add(Column.aliased("apg2_id", table, columnPrefix + "_apg2_id"));
        columns.add(Column.aliased("apg3_id", table, columnPrefix + "_apg3_id"));
        columns.add(Column.aliased("apg4_id", table, columnPrefix + "_apg4_id"));
        return columns;
    }
}
