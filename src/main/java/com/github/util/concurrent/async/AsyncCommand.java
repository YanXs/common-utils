package com.github.util.concurrent.async;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Xs
 */
public abstract class AsyncCommand<T> {


    public static final String COMMON_KEY = "common-command";

    public static final int COMMON_CORE_SIZE = 64;

    private final CommandExecutor<T> commandExecutor;

    protected final AtomicReference<State> state = new AtomicReference<State>(State.LATENT);

    protected enum State {
        LATENT, STARTED, FINISHED
    }

    public AsyncCommand(String key, int coreSize) {
        commandExecutor = initExecutor(key, coreSize);
    }

    public AsyncCommand() {
        this(COMMON_KEY, COMMON_CORE_SIZE);
    }


    private CommandExecutor<T> initExecutor(String key, int coreSize) {
        return CommandExecutorFactory.getCommandExecutor(key, coreSize);
    }

    public ListenableFuture<T> queue() {
        checkState();
        return commandExecutor.executeCommand(new Callable<T>() {
            @Override
            public T call() throws Exception {
                try {
                    return run();
                } finally {
                    state.compareAndSet(State.STARTED, State.FINISHED);
                }
            }
        });
    }

    public T execute() {
        try {
            return (T) queue().get();
        } catch (Exception e) {
            throw new AsyncCommandExecutionException(e);
        }
    }

    protected void checkState() {
        Preconditions.checkState(state.compareAndSet(State.LATENT, State.STARTED), "command could not be executed more than once");
    }

    public abstract T run() throws Exception;
}
