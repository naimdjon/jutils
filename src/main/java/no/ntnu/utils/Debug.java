package no.ntnu.utils;

import java.util.Arrays;

public class Debug {
    public static final boolean DEBUG = true;
    public static final boolean INFO = true;
    public static boolean VERBOSE = false;
    public static final Debug instance = new Debug();
    //private static final org.apache.log4j.Logger l=org.apache.log4j.Logger.getLogger(Debug.class);


    public static Debug d(String msg, Object... param) {
        return d(String.format(msg, param));
    }

    public static Debug d(Object... msg) {
        return d(Arrays.toString(msg));
    }

    public static Debug dr(Object msg) {
        System.out.print(msg.toString().concat("\r"));
        return instance;
    }

    public static Debug d(Object msg) {
        if (!DEBUG) return instance;
        final StackTraceElement[] stack = new Throwable().getStackTrace();
        String ste = stack[1].toString();
        if (ste.contains("Debug.java")) ste = stack[2].toString();
        ste = ste.substring(ste.indexOf("("), ste.indexOf(")") + 1);
        String s = new java.text.SimpleDateFormat("dd.MM.yyy HH:mm:ss").format(new java.util.Date());
        String s2 = " - ".concat(ste).concat(": ").concat(msg == null ? "null" : msg.toString());
        final String s3 = s.concat(s2).concat("\n");
        System.out.print(s3);
        //l.debug(msg.toString());
        return instance;
    }

    public static Debug i(String msg, Object... param) {
        return i(String.format(msg, param));
    }

    public static Debug w(String msg, Object... param) {
        return i(String.format(msg, param));
    }

    public static Debug ii(Object msg) {
        if (!INFO)
            System.out.println(msg);
        return instance;
    }

    public static Debug i(Object msg) {
        if (!INFO) return instance;
        final StackTraceElement[] stack = new Throwable().getStackTrace();
        String ste = stack[1].toString();
        if (ste.contains("Debug.java")) ste = stack[2].toString();
        ste = ste.substring(ste.indexOf("("), ste.indexOf(")") + 1);
        String s = new java.text.SimpleDateFormat("dd.MM.yyy HH:mm:ss").format(new java.util.Date());
        String s2 = " - ".concat(ste).concat(": ").concat(msg != null ? msg.toString() : null);
        final String s3 = s.concat(s2).concat("\n");
        System.out.print(s3);
        //l.debug(msg.toString());
        return instance;
    }

    public static Debug e(String msg, Object... param) {
        return e(String.format(msg, param));
    }

    public static Debug e(String msg) {
        final StackTraceElement[] stack = new Throwable().getStackTrace();
        String ste = stack[1].toString();
        if (ste.contains("Debug.java")) ste = stack[2].toString();
        ste = ste.substring(ste.indexOf("("), ste.indexOf(")") + 1);
        String s = new java.text.SimpleDateFormat("dd.MM.yy HH:mm:ss").format(new java.util.Date());
        String s2 = " - ".concat(ste).concat(": ").concat(msg.toString());
        final String s3 = s.concat(s2).concat("\n");
        System.err.print(s3);
        //l.debug(msg.toString());
        return instance;

    }

    public static void w(Object msg) {
        System.err.println(msg);
        //l.debug(msg.toString());
    }

    public static void dot() {
        System.out.print(".");
    }

}
