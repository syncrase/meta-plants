package fr.syncrase.ecosyst.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class CycleDeVieSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));

        columns.add(Column.aliased("semis_id", table, columnPrefix + "_semis_id"));
        columns.add(Column.aliased("apparition_feuilles_id", table, columnPrefix + "_apparition_feuilles_id"));
        columns.add(Column.aliased("floraison_id", table, columnPrefix + "_floraison_id"));
        columns.add(Column.aliased("recolte_id", table, columnPrefix + "_recolte_id"));
        columns.add(Column.aliased("croissance_id", table, columnPrefix + "_croissance_id"));
        columns.add(Column.aliased("maturite_id", table, columnPrefix + "_maturite_id"));
        columns.add(Column.aliased("plantation_id", table, columnPrefix + "_plantation_id"));
        columns.add(Column.aliased("rempotage_id", table, columnPrefix + "_rempotage_id"));
        columns.add(Column.aliased("reproduction_id", table, columnPrefix + "_reproduction_id"));
        return columns;
    }
}
