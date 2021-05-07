package com.example.sqliteviewer;

import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database implements AutoCloseable {

    public static final String ALL_ROWS_QUERY = "SELECT * FROM %s;";

    private static final String DB_TABLES_QUERY = "SELECT name FROM sqlite_master " +
            "WHERE type ='table' AND name NOT LIKE 'sqlite_%'";
    private static final String URL = "jdbc:sqlite:%s";

    private Connection connection;

    public Database(String fileName) throws SQLException {
        connect(fileName);
    }

    private void connect(String fileName) throws SQLException {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(String.format(URL, fileName));
        connection = dataSource.getConnection();
    }

    public Connection getConnexion() {
        return this.connection;
    }

    public List<String> getTables() throws SQLException {
        List<String> list = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(DB_TABLES_QUERY);
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                list.add(name);
            }
            return list;
        }
    }

    public DataTableModel executeQuery(String query) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            ResultSetMetaData metaData = resultSet.getMetaData();

            int columnCount = metaData.getColumnCount();
            String[] columns = new String[columnCount];
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                columns[i] = metaData.getColumnName(i + 1);
            }

            Map<Integer, Object[]> data = new HashMap<>();
            int i = 0;
            while (resultSet.next()) {
                Object[] row = new Object[columnCount];
                for (int j = 0; j < columnCount; j++) {
                    row[j] = resultSet.getObject(j + 1);
                }
                data.put(i++, row);
            }
            return new DataTableModel(columns, data);
        }

    }

    @Override
    public void close() throws Exception {
        this.connection.close();
    }
}
