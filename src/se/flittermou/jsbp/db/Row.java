package se.flittermou.jsbp.db;

import java.util.ArrayList;
import java.util.List;

public class Row {
    private List<Column> m_columns = new ArrayList<>();

    public Row(List<Column> columns) {
        m_columns = columns;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Column col : m_columns) {
            sb.append(col.toString()).append(" ");
        }
        return sb.toString();
    }

    public String valuesToString() {
        StringBuilder sb = new StringBuilder();
        for (Column col : m_columns) {
            sb.append(col.getValue()).append(" ");
        }
        return sb.toString();
    }

    public Column getColumn(int column) {
        return m_columns.get(column);
    }
}
