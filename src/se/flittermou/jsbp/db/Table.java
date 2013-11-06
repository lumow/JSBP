package se.flittermou.jsbp.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Table {
    private String table_name;
    private List<Column> m_columns;
    private DB db;

    public Table(DB db, String name, List<Column> columns) {
        table_name = name;
        m_columns = columns;
        this.db = db;
    }

    public void create() throws SQLException {
        if (db == null) {
            throw new NullPointerException("Database is null");
        }
        Connection c = db.getConnection();
        if (c.isClosed()) {
            throw new SQLException("Not connected to database.");
        }
        Statement statement = c.createStatement();

        StringBuilder sql = new StringBuilder();
        sql.append("create table " + table_name + " (");
        for (Column column : m_columns) {
            sql.append(column.toString()).append(",");
        }
        sql.deleteCharAt(sql.lastIndexOf(","));
        sql.append(")");
        statement.executeUpdate(sql.toString());
        statement.close();
    }

    public void delete() throws SQLException {
        if (db == null) {
            throw new NullPointerException("Database is null");
        }
        Connection c = db.getConnection();
        if (c.isClosed()) {
            throw new SQLException("Not connected to database");
        }
        Statement statement = c.createStatement();
        String sql = "drop table " + table_name;
        statement.executeUpdate(sql);
        statement.close();
    }

    public List<String> getColumns() {
        List<String> list = new ArrayList<>();
        for (Column col : m_columns) {
            list.add(col.getName());
        }
        return list;
    }

    private Column getColumn(String name) {
        for (Column col : m_columns) {
            if (col.getName().equals(name)) return col;
        }
        return null;
    }

    public void insert(Map<String, String> valuesMap) throws SQLException {
        if (valuesMap.isEmpty()) {
            return;
        }

        Connection conn = db.getConnection();
        conn.setAutoCommit(false);
        Statement statement = conn.createStatement();

        StringBuilder sb = new StringBuilder();
        Set<String> keys = valuesMap.keySet();
        Collection<String> values = valuesMap.values();

        sb.append("insert into ").append(table_name).append("(");
        for (String key : keys) {
            sb.append(key).append(", ");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append(") values (");

        for (String value : values) {
            sb.append("'").append(value).append("', ");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append(")");

        statement.executeUpdate(sb.toString());
        statement.close();
        conn.commit();
        conn.setAutoCommit(true);
    }

    public List<Row> select(List<String> columns, String whereClause) throws SQLException {
        if (db == null) {
            throw new NullPointerException("Database is null");
        }
        Connection c = db.getConnection();
        if (c.isClosed()) {
            throw new SQLException("Not connected to database");
        }
        Statement statement = c.createStatement();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        for (String col : columns) {
            sb.append(col).append(", ");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append(" FROM ").append(table_name);

        if (!whereClause.isEmpty()) {
            sb.append(" where ").append(whereClause);
        }

        ResultSet rs = statement.executeQuery(sb.toString());

        List<Row> rows = new ArrayList<>();
        while (rs.next()) {
            List<Column> columnList = new ArrayList<>();
            for (String col : columns) {
                Column current_col = getColumn(col);
                Column tmp_col = Column.create(current_col.getName(), current_col.getType());
                if (tmp_col == null) {
                    throw new IllegalArgumentException("No such column.");
                }
                tmp_col.setValue(rs);
                columnList.add(tmp_col);
            }
            rows.add(new Row(columnList));
        }

        statement.close();
        return rows;
    }

    public int getSize() throws SQLException {
        if (db == null) {
            throw new NullPointerException("Database is null");
        }
        Connection c = db.getConnection();
        if (c.isClosed()) {
            throw new SQLException("Not connected to database");
        }
        Statement statement = c.createStatement();
        ResultSet rs = statement.executeQuery("select count(nr) from " + table_name);
        int size = rs.getInt("count(nr)");
        statement.close();
        return size;
    }
}
