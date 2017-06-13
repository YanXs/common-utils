package com.github.util.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;

public class TheUnsafe {

    private static final Unsafe THE_UNSAFE;

    static {
        try {
            final PrivilegedExceptionAction<Unsafe> action = new PrivilegedExceptionAction<Unsafe>() {
                public Unsafe run() throws Exception {
                    Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
                    theUnsafe.setAccessible(true);
                    return (Unsafe) theUnsafe.get(null);
                }
            };

            THE_UNSAFE = AccessController.doPrivileged(action);
        } catch (Exception e) {
            throw new RuntimeException("Unable to load unsafe", e);
        }
    }

    public static Unsafe get() {
        return THE_UNSAFE;
    }
}
