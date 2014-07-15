package no.ntnu.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Formatter {
    public static final int MB = 1048576;

    private static NumberFormat nf = new DecimalFormat("###,###,###,###");
    private static final SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    public static String format(long number) {
        return nf.format(number);
    }

    public static String format(int number) {
        return nf.format(number);
    }

    public static String fmt(int number) {
        return nf.format(number);
    }

    public static String nowPretty() {
        return datePretty(System.currentTimeMillis());
    }

    public static synchronized String datePretty(long number) {
        return df.format(new Date(number));
    }

    public static synchronized String now() {
        return nowPretty();
    }

    public static long toMB(final long nrOfBytes) {
        return toMegaByte(nrOfBytes);
    }

    public static long toMegaByte(final long nrOfBytes) {
        return nrOfBytes / MB;
    }

}
