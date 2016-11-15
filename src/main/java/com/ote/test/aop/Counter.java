package com.ote.test.aop;

import java.util.concurrent.atomic.AtomicLong;

public final class Counter {

    private final static AtomicLong Value = new AtomicLong(0);

    public static long nextValue() {
        return Value.getAndIncrement();
    }
}
