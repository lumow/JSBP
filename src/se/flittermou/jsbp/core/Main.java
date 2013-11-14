package se.flittermou.jsbp.core;

import se.flittermou.jsbp.db.DB;
import se.flittermou.jsbp.gui.GUI;

import javax.swing.*;
import java.sql.SQLException;
import java.util.Map;

import static javax.swing.UIManager.getSystemLookAndFeelClassName;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                }
                GUI gui = new GUI();
            }
        });

        /* fullUpdate();
        DB db = new DB();
        try {
            db.connect();
            ArticleTable at = new ArticleTable(db);
            System.out.println("Database contains " + at.getSize() + " items.");
            List<Row> rows = at.select(at.getColumns(), "Namn2 like \'%London Pride%\'");
            for (Row row : rows) {
                System.out.println(row.valuesToString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
    }

    public static void fullUpdate() {
        Webapi webapi = new Webapi();
        webapi.update();
        DB db = new DB();
        SBParser parser = new SBParser();
        ArticleTable at = null;

        try {
            db.connect();
            parser.parse(webapi.getFile());
            at = new ArticleTable(db);
            at.delete();
            at.create();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        System.out.println("Updating local database, this might take a while...");
        Map<String, String> insertMap = null;
        Long time1 = System.currentTimeMillis();
        while (!(insertMap = parser.getNextArticle()).isEmpty()) {
            try {
                at.insert(insertMap);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        Long time2 = System.currentTimeMillis();
        try {
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Local database updated in: " + prettyPrintTime(time2 - time1));
        try {
            System.out.println("Database contains " + at.getSize() + " items.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String prettyPrintTime(long time) {
        int seconds = (int) (time / 1000);
        int hours = seconds > 3600 ? seconds / 3600 : 0;
        int minutes = seconds > 60 ? seconds / 60 : 0;
        int rest = seconds % 60;
        return hours + " hours, " + minutes + " minutes, " + rest + " seconds";
    }
}
