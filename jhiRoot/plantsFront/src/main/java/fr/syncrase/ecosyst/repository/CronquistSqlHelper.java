package fr.syncrase.ecosyst.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class CronquistSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("regne", table, columnPrefix + "_regne"));
        columns.add(Column.aliased("sous_regne", table, columnPrefix + "_sous_regne"));
        columns.add(Column.aliased("division", table, columnPrefix + "_division"));
        columns.add(Column.aliased("classe", table, columnPrefix + "_classe"));
        columns.add(Column.aliased("sous_classe", table, columnPrefix + "_sous_classe"));
        columns.add(Column.aliased("ordre", table, columnPrefix + "_ordre"));
        columns.add(Column.aliased("famille", table, columnPrefix + "_famille"));
        columns.add(Column.aliased("genre", table, columnPrefix + "_genre"));
        columns.add(Column.aliased("espece", table, columnPrefix + "_espece"));

        return columns;
    }
}
