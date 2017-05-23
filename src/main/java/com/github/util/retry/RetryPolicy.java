package com.github.util.retry;


import com.github.util.Sleeper;

public interface RetryPolicy {

    boolean allowRetry(int retryCount, long elapsedTimeMs, Sleeper sleeper);

}
