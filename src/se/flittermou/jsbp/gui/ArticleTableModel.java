package se.flittermou.jsbp.gui;

import se.flittermou.jsbp.core.ArticleTable;
import se.flittermou.jsbp.db.DB;
import se.flittermou.jsbp.db.Row;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ArticleTableModel extends AbstractTableModel {

    public ArticleTableModel(DB db) {
        ArticleTable at = new ArticleTable(db);
        columnNames = at.getColumns().toArray();
    }

    private final Object[] columnNames;
    private List<Row> rows = new ArrayList<>();

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return rows.size();
    }

    public String getColumnName(int col) {
        return String.valueOf(columnNames[col]);
    }

    public Object getValueAt(int row, int col) {
        return String.valueOf(rows.get(row).getColumn(col).getValue());
    }

    public Class getColumnClass(int c) {
        return String.class;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }
}
