package se.flittermou.jsbp.core;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.net.URL;

public class Webapi {
    public static final String BYTES_PROP = "Bytes";
    private long byteCount = 0L;
    private long updateInterval = 1L << 10;
    private long nextReport = updateInterval;
    private DownloadInfo dlInfo = new DownloadInfo(0, 0);
    private DownloadInfo lastDlInfo = new DownloadInfo(0, 0);
    private PropertyChangeSupport support = new PropertyChangeSupport(this);
    private final String xmlUrl = "http://www.systembolaget.se/Assortment.aspx?Format=Xml";
    private final String localAssortmentFile = "assortment.xml";

    public boolean update() {
        try {
            File file = new File(localAssortmentFile);
            System.out.println("Updating local XML file.");
            URL url = new URL(xmlUrl);
            InputStream is = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            PrintWriter pw = new PrintWriter(file);
            long time1 = System.currentTimeMillis();
            int c;
            while ((c = br.read()) != -1) {
                pw.write((char) c);
                byteCount++;
                if (byteCount > nextReport) {
                    long time2 = System.currentTimeMillis();
                    dlInfo = new DownloadInfo(time2 - time1, byteCount);
                    support.firePropertyChange(BYTES_PROP, lastDlInfo, dlInfo);
                    lastDlInfo = dlInfo;
                    nextReport += updateInterval;
                }
            }
            pw.flush();
            System.out.println("XML file '" + file.getName() + "' (" + (file.length() / 1000) + " kB) downloaded.");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public File getFile() {
        return new File(localAssortmentFile);
    }

    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        support.addPropertyChangeListener(property, listener);
    }

    public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
        support.removePropertyChangeListener(property, listener);
    }

    public void setUpdateInterval(long bytes) {
        updateInterval = bytes;
        nextReport = updateInterval;
    }
}
