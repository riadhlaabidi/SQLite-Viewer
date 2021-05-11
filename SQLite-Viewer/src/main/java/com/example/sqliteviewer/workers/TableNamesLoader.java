package com.example.sqliteviewer.workers;

import com.example.sqliteviewer.Database;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TableNamesLoader extends SwingWorker<List<String>, Void> {

    private final String filename;
    private final JComboBox<String> tableSelect;

    public TableNamesLoader(String filename, JComboBox<String> tableSelect) {
        this.filename = filename;
        this.tableSelect = tableSelect;
    }

    @Override
    protected List<String> doInBackground() throws Exception {
        try (Database database = new Database(filename)) {
            return database.getTableNames();
        }
    }

    @Override
    protected void done() {
        try {
            tableSelect.removeAllItems();
            get().forEach(tableSelect::addItem);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
