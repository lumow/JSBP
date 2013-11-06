package se.flittermou.jsbp.core;

import java.io.*;
import java.net.URL;

public class Webapi {
    public static final String xmlUrl = "http://www.systembolaget.se/Assortment.aspx?Format=Xml";
    public static final String localAssortmentFile = "assortment.xml";

    private static boolean done = false;

    private static synchronized void setDone(boolean b) {
        done = b;
    }

    private static synchronized boolean getDone() {
        return done;
    }

    public static void update() {
        System.out.print("Updating local data file...");

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(xmlUrl);
                    InputStream is = url.openStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    PrintWriter pw = new PrintWriter(new File(localAssortmentFile));
                    int c;
                    while ((c = br.read()) != -1) {
                        pw.write((char) c);
                    }
                    pw.flush();
                    setDone(true);
                } catch (IOException e) {
                    e.printStackTrace();
                    setDone(true);
                }
            }
        });
        long time1 = System.currentTimeMillis();
        t.start();

        while (!done) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // EAT IT
            }
            System.out.print(".");
        }
        long time2 = System.currentTimeMillis();
        System.out.println("\nFinished updating!");
        System.out.println("Update took: " + ((time2 - time1) / 1000) + " seconds.");
    }

    public static File getFile() {
        return new File(localAssortmentFile);
    }
}
