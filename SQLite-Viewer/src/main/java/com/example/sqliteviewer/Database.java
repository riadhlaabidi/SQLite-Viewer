package com.example.sqliteviewer;

import org.sqlite.SQLiteDataSource;

import javax.swing.table.TableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database implements AutoCloseable {

    public static final String ALL_ROWS_QUERY = "SELECT * FROM %s;";

    private Connection connection;

    public Database(String fileName) throws SQLException {
        connect(fileName);
    }

    private void connect(String fileName) throws SQLException {
        final String url = "jdbc:sqlite:%s";
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(String.format(url, fileName));
        connection = dataSource.getConnection();
    }

    public List<String> getTableNames() throws SQLException {
        final String query = "SELECT name FROM sqlite_master " +
                "WHERE type ='table' AND name NOT LIKE 'sqlite_%'";
        List<String> list = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                list.add(name);
            }
            return list;
        }
    }

    public TableModel getTableModel(String query) throws SQLException {
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
