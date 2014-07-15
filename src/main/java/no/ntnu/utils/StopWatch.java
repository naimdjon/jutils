package no.ntnu.utils;

import java.text.DecimalFormat;
import java.util.SortedMap;

import static java.lang.System.currentTimeMillis;
import static no.ntnu.utils.Col.treeMap;

public class StopWatch {

    private long started;
    private long stopped;
    private SortedMap<Long, String> laps = treeMap();
    private DecimalFormat df = new DecimalFormat("#.###");//for formatting floats

    public StopWatch() {
        this(true);
    }

    public StopWatch(boolean autostart) {
        if (autostart)
            start();
    }

    private long getLast() {
        if (laps.size() == 0)
            return started;
        return laps.lastKey();
    }

    public long lapDiff() {
        long start = getLast();
        long stop = lap().getLast();
        return stop - start;
    }

    public StopWatch lapPrintSec(String msg) {
        float duration = (float) lapDiff();
        float diffSec = duration / 1000.0f;
        System.out.println(StringUtils.cat(String.valueOf(msg), " \t", df.format(diffSec), " sec."));
        return this;
    }

    public StopWatch lapPrint(String msg) {
        long start = getLast();
        lap(msg);
        long stop = getLast();
        System.out.println(StringUtils.cat("[", msg, String.valueOf(stop - start), " ms. ] "));
        return this;
    }

    public StopWatch lap(String msg) {
        laps.put(currentTimeMillis(), msg);
        return this;
    }

    public StopWatch lap() {
        return lap(null);
    }

    public StopWatch restart() {
        return reset().start();
    }

    public StopWatch reset() {
        laps.clear();
        started = stopped = 0;
        return this;
    }

    public StopWatch start() {
        this.started = currentTimeMillis();
        return this;
    }

    public StopWatch stop() {
        this.stopped = currentTimeMillis();
        return this;
    }

    public long diff() {
        return stopped - started;
    }

    public StopWatch stopAndPrint() {
        return stopAndPrint("done:");
    }

    public StopWatch stopAndPrint(String msg) {
        stop();
        System.out.println(StringUtils.cat(msg, " ", this.toString()));
        return this;
    }

    public static void main(String[] args) {
        StopWatch sw = new StopWatch();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sw.stopAndPrint("test::: ");
        System.out.println("before started:" + sw.started);
        System.out.println("stopped:" + sw.stopped);
        sw.reset();
        System.out.println("started:" + sw.started);
        System.out.println("stopped:" + sw.stopped);
    }

    @Override
    public String toString() {
        return "diff:" + diff() + " ms.";
    }
}
