package com.example.sqliteviewer;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void close() throws Exception {
        this.connection.close();
    }
}
