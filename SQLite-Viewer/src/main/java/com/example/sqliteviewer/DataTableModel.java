package com.example.sqliteviewer;

import javax.swing.table.AbstractTableModel;
import java.util.Map;

public class DataTableModel extends AbstractTableModel {

    private final String[] columnsNames;
    private final Map<Integer, Object[]> data;

    public DataTableModel(String[] columnsNames, Map<Integer, Object[]> data) {
        this.columnsNames = columnsNames;
        this.data = data;
    }

    @Override
    public String getColumnName(int column) {
        return columnsNames[column];
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnsNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex)[columnIndex];
    }
}
