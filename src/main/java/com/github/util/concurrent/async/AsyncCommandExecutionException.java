package com.github.util.concurrent.async;

/**
 * @author Xs
 */
public class AsyncCommandExecutionException extends RuntimeException {

    public AsyncCommandExecutionException(Throwable cause) {
        super(cause);
    }

    public AsyncCommandExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public AsyncCommandExecutionException(String message) {
        super(message);
    }
}
