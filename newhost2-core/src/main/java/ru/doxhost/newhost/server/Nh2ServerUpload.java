package ru.doxhost.newhost.server;

import java.text.DecimalFormat;

/**
 * @author Eugene Kirin
 */
public final class Nh2ServerUpload {

    private Nh2ServerUpload() {
    }

    public static final int KB = 1024;
    public static final int MB = 1024 * KB;

    /**
     * Default value
     */
    public static final int ALLOWED_50_MB = 50 * MB;

    public static String toHuman(long size) {
        if(size <= 0) return "0";
        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static String allowedSizeToUpload() {
        return toHuman(ALLOWED_50_MB);
    }

    public static int allowedSize() {
        return ALLOWED_50_MB; // TODO - need to set from config.
    }
}