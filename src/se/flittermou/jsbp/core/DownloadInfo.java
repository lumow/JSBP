package se.flittermou.jsbp.core;

public class DownloadInfo {
    private final long time;
    private final long byteCount;

    public DownloadInfo(long time, long byteCount) {
        this.time = time;
        this.byteCount = byteCount;
    }

    /**
     * @return Current download speed in kiloBytes / seconds.
     */
    public double getDownloadSpeed() {
        long seconds = time / 1000;
        long kiloBytes = byteCount / 1000;
        return (kiloBytes != 0 && seconds != 0) ? kiloBytes / seconds : 0;
    }

    /**
     * @return Size of data downloaded this far, in kiloBytes.
     */
    public double getSize() {
        return byteCount != 0 ? (byteCount / 1000) : 0;
    }
}
