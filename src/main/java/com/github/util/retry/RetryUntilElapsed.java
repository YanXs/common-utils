package com.github.util.retry;


import com.github.util.Sleeper;

public class RetryUntilElapsed extends RetryNTimes {

    private final int maxElapsedTimeMs;

    public RetryUntilElapsed(int maxElapsedTimeMs, int sleepMsBetweenRetries) {
        super(Integer.MAX_VALUE, sleepMsBetweenRetries);
        this.maxElapsedTimeMs = maxElapsedTimeMs;
    }

    @Override
    public boolean allowRetry(int retryCount, long elapsedTimeMs, Sleeper sleeper) {
        return super.allowRetry(retryCount, elapsedTimeMs, sleeper) && (elapsedTimeMs < maxElapsedTimeMs);
    }
}
