package com.example.sqliteviewer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SQLiteViewer extends JFrame {

    public SQLiteViewer() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);
        setResizable(true);
        setLayout(new BorderLayout(0, 16));
        setLocationRelativeTo(null);
        setTitle("SQLite Viewer");
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        Font jetbrainsMonoFont = new Font("Jetbrains Mono", Font.BOLD, 14);

        Insets topLeft8Padding = new Insets(8, 8, 0, 0);
        Insets top8Padding = new Insets(8, 0, 0, 0);
        Insets left8Padding = new Insets(0, 8, 0, 0);

        JPanel topFormPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        topFormPanel.setBorder(new EmptyBorder(8, 8, 8, 8));

        JLabel fileNameLabel = new JLabel("Filename");
        fileNameLabel.setFont(jetbrainsMonoFont);
        constraints.weightx = 0.0;
        constraints.gridx = 0;
        constraints.gridy = 0;
        topFormPanel.add(fileNameLabel, constraints);

        JTextField fileNameTextField = new JTextField();
        fileNameLabel.setLabelFor(fileNameTextField);
        fileNameTextField.setName("FileNameTextField");
        fileNameTextField.setFont(jetbrainsMonoFont.deriveFont(Font.PLAIN));
        fileNameTextField.setColumns(20);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = left8Padding;
        constraints.weightx = 1.0;
        constraints.gridx = 1;
        topFormPanel.add(fileNameTextField, constraints);

        JButton openFileButton = new JButton("Open");
        openFileButton.setName("OpenFileButton");
        openFileButton.setFont(jetbrainsMonoFont);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.0;
        constraints.gridx = 2;
        topFormPanel.add(openFileButton, constraints);

        JLabel tableLabel = new JLabel("Table");
        tableLabel.setFont(jetbrainsMonoFont);
        constraints.fill = GridBagConstraints.NONE;
        constraints.insets = top8Padding;
        constraints.gridx = 0;
        constraints.gridy = 1;
        topFormPanel.add(tableLabel, constraints);

        JComboBox<String> tableSelect = new JComboBox<>();
        tableLabel.setLabelFor(tableSelect);
        tableSelect.setName("TablesComboBox");
        tableSelect.setFont(jetbrainsMonoFont);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = topLeft8Padding;
        constraints.weightx = 1.0;
        constraints.gridx = 1;
        topFormPanel.add(tableSelect, constraints);

        JLabel queryLabel = new JLabel("Query");
        queryLabel.setFont(jetbrainsMonoFont);
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.insets = top8Padding;
        constraints.weightx = 0.0;
        constraints.gridx = 0;
        constraints.gridy = 2;
        topFormPanel.add(queryLabel, constraints);

        JTextArea queryTextArea = new JTextArea();
        queryLabel.setLabelFor(queryTextArea);
        queryTextArea.setName("QueryTextArea");
        queryTextArea.setRows(5);
        queryTextArea.setFont(jetbrainsMonoFont.deriveFont(Font.PLAIN));
        queryTextArea.setWrapStyleWord(true);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = topLeft8Padding;
        constraints.weightx = 1.0;
        constraints.gridx = 1;
        JScrollPane queryTextScroll = new JScrollPane(queryTextArea);
        topFormPanel.add(queryTextScroll, constraints);

        JButton executeQueryButton = new JButton("Execute");
        executeQueryButton.setName("ExecuteQueryButton");
        executeQueryButton.setFont(jetbrainsMonoFont);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.insets = topLeft8Padding;
        constraints.weightx = 0.0;
        constraints.gridx = 2;
        topFormPanel.add(executeQueryButton, constraints);

        add(topFormPanel, BorderLayout.PAGE_START);

        JTable table = new JTable();
        table.setName("Table");
        table.setFont(jetbrainsMonoFont.deriveFont(Font.PLAIN));
        table.setFillsViewportHeight(true);
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setBorder(new EmptyBorder(8, 8, 8, 8));
        add(tableScrollPane, BorderLayout.CENTER);


        openFileButton.addActionListener(event -> {
            try (Database database = new Database(fileNameTextField.getText())) {
                tableSelect.removeAllItems();
                database.getTables().forEach(tableSelect::addItem);
                queryTextArea.setText(String.format(Database.ALL_ROWS_QUERY, tableSelect.getSelectedItem()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        tableSelect.addItemListener(event -> {
            queryTextArea.setText(String.format(Database.ALL_ROWS_QUERY, event.getItem().toString()));
        });

        executeQueryButton.addActionListener(event -> {
            try (Database database = new Database(fileNameTextField.getText())) {
                DataTableModel tableModel = database.executeQuery(
                        queryTextArea.getText(),
                        (String) tableSelect.getSelectedItem()
                );
                table.setModel(tableModel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
