package se.flittermou.jsbp.db;

import java.sql.*;
import java.util.List;

public class DB {
    private final String dbName = "systembolaget.db";
    private Connection c = null;

    public void connect() throws SQLException {
        if (c != null && !c.isClosed()) {
            return;
        }

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("Couldn't find class: " + e);
            System.exit(1);
        }
        c = DriverManager.getConnection("jdbc:sqlite:" + dbName);
        System.out.println("Connected to " + dbName + " successfully!");
    }

    public void createTable(String name, List<Column> columns) throws SQLException {
        if (c.isClosed()) {
            throw new SQLException("Not connected to database.");
        }
        Statement statement = c.createStatement();

        StringBuilder sql = new StringBuilder();
        sql.append("create table " + name + " (");
        for (Column column : columns) {
            sql.append(column.toString()).append(",");
        }
        sql.deleteCharAt(sql.lastIndexOf(","));
        sql.append(")");
        System.out.println(sql);
        statement.executeUpdate(sql.toString());
        statement.close();
    }

    public void deleteTable(String name) throws SQLException {
        if (c.isClosed()) {
            throw new SQLException("Not connected to database");
        }
        Statement statement = c.createStatement();
        String sql = "drop table " + name;
        statement.executeUpdate(sql);
        statement.close();
    }

    public void insert(String table, String query) throws SQLException {

    }

    public void select() throws SQLException {

    }

    public ResultSet query(String query) throws SQLException {
        if (c == null || c.isClosed()) {
            throw new NullPointerException("Connection is null or closed");
        }
        Statement statement = c.createStatement();
        ResultSet rs = statement.executeQuery(query);
        statement.close();
        return rs;
    }

    public void close() throws SQLException {
        c.close();
    }

    public Connection getConnection() {
        return c;
    }
}
