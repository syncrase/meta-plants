package fr.syncrase.ecosyst.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class SemisSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));

        columns.add(Column.aliased("semis_pleine_terre_id", table, columnPrefix + "_semis_pleine_terre_id"));
        columns.add(Column.aliased("semis_sous_abris_id", table, columnPrefix + "_semis_sous_abris_id"));
        columns.add(Column.aliased("type_semis_id", table, columnPrefix + "_type_semis_id"));
        columns.add(Column.aliased("germination_id", table, columnPrefix + "_germination_id"));
        return columns;
    }
}
