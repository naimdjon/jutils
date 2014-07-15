package no.ntnu.utils;

import java.io.Console;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;

import static java.lang.Integer.parseInt;
import static java.lang.System.console;
import static no.ntnu.utils.Col.asReverseSortedList;
import static no.ntnu.utils.Formatter.toMB;
import static no.ntnu.utils.StringUtils.*;


public class Sys {
    public static final String USER = System.getProperty("user.name");
    private static final String OS = System.getProperty("os.name").toLowerCase();
    public static final boolean isMac = contains(OS, "mac");
    public static final boolean isLinux = contains(OS, "linux");
    public static final boolean isWindows = contains(OS, "windows");


    public static void set(String property, String value) {
        System.setProperty(property, value);
    }

    public static String prop(String s, String def) {
        return System.getProperty(s, def);
    }

    public static boolean trueProp(String s) {
        return StringUtils.isTrue(System.getProperty(s));
    }

    public static int intProp(String s, int def) {
        String p = System.getProperty(s);
        if (isNumber(p)) return parseInt(p);
        return def;
    }

    public static float avgTop(Collection<Float> floats, int top) {
        List<Float> floats1 = asReverseSortedList(floats);
        int antall = 1;
        float total = 0.0f;
        for (Float aFloat : floats1) {
            if (antall++ >= top) break;
            total += aFloat;
        }
        return total / antall;
    }


    public static void pidOut() {
        System.out.println(pid());
    }

    public static String pid() {
        return ManagementFactory.getRuntimeMXBean().getName();
    }

    public static boolean anyTrue(boolean... flags) {
        for (boolean b : flags)
            if (b) return true;
        return false;
    }

    public static float avg(Collection<Float> floats) {
        int size = floats.size();
        float total = sum(floats);
        return total / (float) size;
    }

    public static float sum(Collection<Float> floats) {
        float total = 0.0f;
        for (float f : floats)
            total += f;
        return total;
    }

    @SuppressWarnings("rawtypes")
    public static void addShutdownHook(final Callable r) {
        try {
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    try {
                        r.call();
                    } catch (Exception e) {
                        System.err.println("Could not execute shutdown hook");
                    }
                }
            });
        } catch (Exception e) {
            System.err.println("shutdown hook not working!");
        }
    }

    public static void onExit(final Runnable r) {
        runOnExit(r);
    }

    public static void runOnExit(final Runnable r) {
        try {
            Runtime.getRuntime().addShutdownHook(new Thread(r));
        } catch (Exception e) {
            System.err.println("shutdown hook not working!");
        }
    }

    public static void exitOnUncaughtException() {
        try {
            Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
                public void uncaughtException(Thread arg0, Throwable arg1) {
                    Debug.e("Uncaught exception, exiting. occurred at:");
                    arg1.printStackTrace();
                    System.exit(0);
                }
            });
        } catch (Exception e) {
            System.err.println("Error setting uncaught exception handler!");
        }
    }


    public static void main(String[] args) {
        List<Float> floats = Arrays.asList(2.11f, 2.14f, 4.122f);
        System.out.println(sum(floats));
        String s = readPasswd();
        System.out.printf("passwd is %s", s);
    }

    public static String stackTraceToStr(Throwable t) {
        java.io.StringWriter str = new java.io.StringWriter();
        java.io.PrintWriter wr = new java.io.PrintWriter(str);
        t.printStackTrace(wr);
        return str.toString();
    }

    public static String readPasswd() {
        Console cons = console();
        if (cons == null) {
            //System.err.println("No console.");
            Scanner s = new Scanner(System.in);
            return trim(s.nextLine());
        }
        char[] passwd = cons.readPassword("%s", "Password:");
        if (passwd == null) {
            System.err.println("Failed to read password from console!");
            //java.util.Arrays.fill(passwd, ' ');
        }
        if (passwd == null) {
            System.err.println("No password is read...");
            return null;
        }
        return new String(passwd);
    }

    public static boolean hostnameContains(String s) {
        try {
            String canonicalHostName = java.net.InetAddress.getLocalHost().getCanonicalHostName();
            return StringUtils.contains(canonicalHostName, s);
        } catch (Exception e) {
            System.err.println("could not determine the hostname.");
        }
        return false;
    }

    public static String mem() {
        final Runtime runtime = Runtime.getRuntime();
        return String.format("mem info free=%s, total=%s, (in MBytes)", toMB(runtime.freeMemory()), toMB(runtime.totalMemory()));
    }

    public static void sysinfo() {
        meminfo();
        try {
            String hostName = java.net.InetAddress.getLocalHost().getCanonicalHostName();
            Debug.i("Hostname:" + hostName);
        } catch (Exception e) {
            System.err.println("Could not get the hostname");
        }
    }

    public static void meminfo() {
        final Runtime runtime = Runtime.getRuntime();
        long freeMemory = runtime.freeMemory();
        long totalMemory = runtime.totalMemory();
        System.out.println(String.format("mem info free=%s, total=%s, (in MBytes), %s %s", toMB(freeMemory), toMB(totalMemory), (freeMemory / totalMemory) * 100, "% used"));
    }


    public static double hypot(double a, double b) {
        double r;
        if (Math.abs(a) > Math.abs(b)) {
            r = b / a;
            r = Math.abs(a) * Math.sqrt(1 + r * r);
        } else if (b != 0) {
            r = a / b;
            r = Math.abs(b) * Math.sqrt(1 + r * r);
        } else {
            r = 0.0;
        }
        return r;
    }


    public static void exitWhenDone(final Collection<? extends Worker> workers) {
        while (true) {
            boolean allDone = true;
            //D.d("checking the state of affairs...");
            for (Worker wp : workers) {
                if (!wp.isDone()) {
                    Debug.d(wp + " is not done yet!");
                    allDone = false;
                    break;
                }
            }
            if (allDone) {
                Debug.i("Everything has finished, exiting...");
                System.exit(0);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
