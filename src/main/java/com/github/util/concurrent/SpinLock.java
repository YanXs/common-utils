package com.github.util.concurrent;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Xs
 */
public class SpinLock {

    private AtomicReference<Thread> owner = new AtomicReference<Thread>();
    private int count;

    public void lock() {
        Thread currentThread = Thread.currentThread();
        if (currentThread == owner.get()) {
            ++count;
            return;
        }
        // 自旋
        while (!owner.compareAndSet(null, currentThread)) {
        }
    }


    public void unlock() {
        Thread currentThread = Thread.currentThread();
        if (currentThread == owner.get()) {
            if (count != 0) {
                --count;
            } else {
                owner.compareAndSet(currentThread, null);
            }
        }
    }
}
