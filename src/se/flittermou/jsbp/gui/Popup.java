package se.flittermou.jsbp.gui;

import se.flittermou.jsbp.core.ArticleTable;
import se.flittermou.jsbp.core.DownloadInfo;
import se.flittermou.jsbp.core.SBParser;
import se.flittermou.jsbp.core.Webapi;
import se.flittermou.jsbp.db.DB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.Map;

public class Popup extends JDialog implements PropertyChangeListener {
    private final JPanel panel = new JPanel(new FlowLayout());
    private final JTextField staticTextField = new JTextField("Laddar ner fil...");
    private final JTextField downloadTextField = new JTextField("Nedladdat (kB): , Hastighet (kB/s): ");
    private final JProgressBar progressBar = new JProgressBar();
    private final JButton okButton = new JButton("OK");
    private final Webapi webapi = new Webapi();
    private final DB db;

    public Popup(final DB db) {
        this.db = db;
        setTitle("Uppdaterar databasen");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(500, 100);
        webapi.addPropertyChangeListener(Webapi.BYTES_PROP, this);
        staticTextField.setEnabled(false);
        downloadTextField.setEnabled(false);
        progressBar.setIndeterminate(true);
        okButton.setEnabled(false);
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disposeWindow();
            }
        });
        panel.add(staticTextField);
        panel.add(downloadTextField);
        panel.add(progressBar);
        panel.add(okButton);
        add(panel);
    }

    public void updateDB() {
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                webapi.update();
                SBParser parser = new SBParser();
                ArticleTable at = null;
                try {
                    staticTextField.setText("Parsar innehåll...");
                    parser.parse(webapi.getFile());
                    at = new ArticleTable(db);
                    at.delete();
                    at.create();
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
                staticTextField.setText("Uppdaterar lokal databas...");
                java.util.List<Map<String, String>> allArticles = parser.getAllArticles();
                db.getConnection().setAutoCommit(false);
                for (Map<String, String> map : allArticles) {
                    at.insert(map);
                }
                db.getConnection().commit();
                db.getConnection().setAutoCommit(true);
                staticTextField.setText("Klart!");
                progressBar.setIndeterminate(false);
                progressBar.setValue(100);
                okButton.setEnabled(true);
                return true;
            }
        };
        worker.execute();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName() == Webapi.BYTES_PROP) {
            DownloadInfo dlInfo = (DownloadInfo) evt.getNewValue();
            if (dlInfo != null) {
                downloadTextField.setText("Nedladdat (kB): " + dlInfo.getSize() + ", Hastighet (kB/s): " + dlInfo.getDownloadSpeed());
            }
        }
    }

    private void disposeWindow() {
        dispose();
    }
}
