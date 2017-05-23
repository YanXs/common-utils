package com.github.util.retry;

import com.github.util.Sleeper;
import com.github.util.time.SystemTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public class RetryLoop {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private final long startTimeMs = SystemTime.nanoTime();

    private final RetryPolicy retryPolicy;

    private volatile boolean isDone = false;

    private int retryCount = 0;

    private static final Sleeper sleeper = Sleeper.JUST;

    RetryLoop(RetryPolicy retryPolicy) {
        this.retryPolicy = retryPolicy;
    }

    public static RetryLoop newRetryLoop(RetryPolicy retryPolicy) {
        return new RetryLoop(retryPolicy);
    }

    public static Sleeper getDefaultRetrySleeper() {
        return sleeper;
    }

    public void callWithLoop(Callable<Boolean> proc) throws Exception {
        while (shouldRetry()) {
            try {
                if (proc.call()) {
                    markComplete();
                }
            } catch (Exception e) {
                takeException(e);
            }
        }
    }

    public boolean isDone() {
        return isDone;
    }

    public <T> T callWithRetry(Callable<T> proc) throws Exception {
        T result = null;
        while (shouldContinue()) {
            try {
                result = proc.call();
                markComplete();
            } catch (Exception e) {
                takeException(e);
            }
        }
        return result;
    }


    public boolean shouldContinue() {
        return !isDone;
    }

    public boolean shouldRetry() {
        return !isDone && allowRetry();
    }

    public void markComplete() {
        isDone = true;
    }

    public static boolean isRetryException(Throwable exception) {
        return exception instanceof RetryableException;
    }

    private boolean allowRetry() {
        return retryPolicy.allowRetry(retryCount++, SystemTime.nanoTime() - startTimeMs, sleeper);
    }

    public void takeException(Exception exception) throws Exception {
        boolean rethrow = true;
        if (isRetryException(exception)) {
            LOGGER.info("Retry-able exception received", exception);
            if (allowRetry()) {
                LOGGER.info("Retrying operation");
                rethrow = false;
            } else {
                LOGGER.info("Retry policy not allowing retry");
            }
        }
        if (rethrow) {
            throw exception;
        }
    }

    public static class RetryableException extends Exception {
        public RetryableException(String message) {
            super(message);
        }

        public RetryableException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
