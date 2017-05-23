package com.github.util.time;

/**
 * @author Xs
 */
public class SystemTime {

    public static long time() {
        return System.currentTimeMillis();
    }

    public static long nanoTime() {
        return System.nanoTime();
    }
}
