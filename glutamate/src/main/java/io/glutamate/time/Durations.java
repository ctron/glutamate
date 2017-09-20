/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Jens Reimann - initial API and implementation
 *******************************************************************************/
package io.glutamate.time;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public final class Durations {

    private Durations() {
    }

    @FunctionalInterface
    public interface ConsumeMilliseconds<X extends Throwable> {
        public void consume(long milliseconds) throws X;
    }

    @FunctionalInterface
    public interface ConsumeTimeAndUnit<X extends Throwable> {
        public void consume(long time, TimeUnit unit) throws X;
    }

    @FunctionalInterface
    public interface MapTimeAndUnit<X extends Throwable, T> {
        public T map(long time, TimeUnit unit) throws X;
    }

    public static void sleep(final Duration duration) throws InterruptedException {
        consumeMilliseconds(duration, Thread::sleep);
    }

    public static long toMillis(final Duration duration) {
        Objects.requireNonNull(duration);

        try {
            return duration.toMillis();
        } catch (final ArithmeticException e) {
            return Long.MAX_VALUE;
        }
    }

    public static long toNanos(final Duration duration) {
        Objects.requireNonNull(duration);

        try {
            return duration.toNanos();
        } catch (final ArithmeticException e) {
            return Long.MAX_VALUE;
        }
    }

    public static <X extends Exception> void consumeMilliseconds(final Duration duration,
            final ConsumeMilliseconds<X> consumer) throws X {

        Objects.requireNonNull(duration);
        Objects.requireNonNull(consumer);

        consumer.consume(toMillis(duration.abs()));
    }

    public static <X extends Exception> void consume(final Duration duration,
            final ConsumeTimeAndUnit<X> consumer) throws X {

        Objects.requireNonNull(duration);
        Objects.requireNonNull(consumer);

        map(duration, (time, unit) -> {
            consumer.consume(time, unit);
            return null;
        });

    }

    public static <X extends Exception, T> T map(final Duration duration,
            final MapTimeAndUnit<X, T> map) throws X {

        Objects.requireNonNull(duration);
        Objects.requireNonNull(map);

        final Duration abs = duration.abs();

        final int nanos = abs.getNano();
        final long seconds = abs.getSeconds();

        if (nanos == 0) {
            return map.map(seconds, TimeUnit.SECONDS);
        }

        final long rem = nanos % 1_000_000;
        if (rem == 0) {
            return map.map(seconds * 1_000 + nanos / 1_000_000, TimeUnit.MILLISECONDS);
        } else {
            return map.map(seconds * 1_000_000_000 + nanos, TimeUnit.NANOSECONDS);
        }
    }
}
