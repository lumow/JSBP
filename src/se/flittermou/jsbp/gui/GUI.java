package se.flittermou.jsbp.gui;

import se.flittermou.jsbp.core.ArticleTable;
import se.flittermou.jsbp.db.DB;
import se.flittermou.jsbp.db.Row;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;

public class GUI extends JFrame {
    private DB db = new DB();
    private final String TITLE = "Systembolaget desktop app";
    private final String URL = "http://systembolaget.se/";
    private final Dimension SIZE = new Dimension(700, 500);
    private final JPanel mainPanel = new JPanel();
    private final TextField searchField = new TextField();
    private final JButton searchButton = new JButton("Sök");
    private final JButton updateButton = new JButton("Uppdatera databasen");
    private final JTable table = new JTable(new ArticleTableModel(db));
    private final JScrollPane scrollpane = new JScrollPane(table);

    public GUI() {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    doubleClick(e);
                }
            }
        });
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        try {
            db.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle(TITLE);
        setSize(SIZE);
        GridBagConstraints c = new GridBagConstraints();
        mainPanel.setLayout(new GridBagLayout());
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDatabase();
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchFieldFired();
            }
        });
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchFieldFired();
                }
            }
        });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.1;
        c.weighty = 0.1;
        c.gridx = 0;
        c.gridy = 0;
        mainPanel.add(updateButton, c);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.9;
        c.weighty = 0.1;
        c.gridx = 1;
        c.gridy = 0;
        mainPanel.add(searchField, c);
        c.weightx = 0.1;
        c.weighty = 0;
        c.gridx = 2;
        mainPanel.add(searchButton, c);
        table.setFillsViewportHeight(true);
        c.weightx = 0;
        c.weighty = 0.8;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 3;
        mainPanel.add(scrollpane, c);
        mainPanel.setOpaque(true);
        add(mainPanel);
        setVisible(true);
    }

    private void searchFieldFired() {
        String searchText = searchField.getText();
        if (searchText.isEmpty()) {
            return;
        }
        ArticleTableModel model = (ArticleTableModel) table.getModel();
        searchField.setText("");
        ArticleTable at = new ArticleTable(db);
        try {
            List<Row> rows = at.select(at.getColumns(), "namn like '%" + searchText + "%' or namn2 like '%" + searchText + "%'");
            model.setRows(rows);
            table.updateUI();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void doubleClick(MouseEvent e) {
        ArticleTableModel tableModel = (ArticleTableModel) table.getModel();
        int selectedRow = table.getSelectedRow();
        String val = String.valueOf(tableModel.getValueAt(selectedRow, 2));
        try {
            Desktop.getDesktop().browse(new URI(URL + val));
        } catch (IOException | URISyntaxException e1) {
            e1.printStackTrace();
        }
    }

    private void updateDatabase() {
        final Popup popup = new Popup(db);
        //       popup.setModal(true);
//        popup.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        popup.setVisible(true);
        popup.updateDB();
    }

    private static String prettyPrintTime(long time) {
        int seconds = (int) (time / 1000);
        int hours = seconds > 3600 ? seconds / 3600 : 0;
        int minutes = seconds > 60 ? seconds / 60 : 0;
        int rest = seconds % 60;
        return hours + " hours, " + minutes + " minutes, " + rest + " seconds";
    }
}
