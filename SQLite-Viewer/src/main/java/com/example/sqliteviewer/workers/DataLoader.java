package com.example.sqliteviewer.workers;

import com.example.sqliteviewer.Database;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public class DataLoader extends SwingWorker<TableModel, Void> {

    private final String filename;
    private final String query;
    private final JTable table;

    public DataLoader(String filename, String query, JTable table) {
        this.filename = filename;
        this.query = query;
        this.table = table;
    }

    @Override
    protected TableModel doInBackground() throws Exception {
        try (Database database = new Database(filename)) {
            return database.getTableModel(query);
        } catch (SQLException e) {
            SwingUtilities.invokeAndWait(() -> {
                JOptionPane.showMessageDialog(new Frame(), e.getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE);
            });
            return new DefaultTableModel();
        }
    }

    @Override
    protected void done() {
        try {
            table.setModel(get());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
