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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Assert;
import org.junit.Test;

public class DurationsTest {
    @Test
    public void testMilliseconds() {
        assertMilliseconds(Duration.ofNanos(0), 0);
        assertMilliseconds(Duration.ofNanos(100), 0);
        assertMilliseconds(Duration.ofNanos(1_000), 0);

        assertMilliseconds(Duration.ofNanos(1_000_000), 1);
        assertMilliseconds(Duration.ofNanos(1_000_000_000), 1_000);

        assertMilliseconds(Duration.ofMillis(100), 100);
        assertMilliseconds(Duration.ofMillis(1_000), 1_000);

        assertMilliseconds(Duration.ofMillis(0), 0);
        assertMilliseconds(Duration.ofMillis(100), 100);
        assertMilliseconds(Duration.ofMillis(1_000), 1_000);

        assertMilliseconds(Duration.ofSeconds(1), 1_000);
        assertMilliseconds(Duration.ofSeconds(1_000), 1_000_000);
    }

    private static void assertMilliseconds(final Duration duration, final long expected) {
        final AtomicLong result = new AtomicLong();
        Durations.consumeMilliseconds(duration, result::set);
        Assert.assertEquals(expected, result.get());
    }

    @Test
    public void testTimeAndUnit() {
        assertTimeAndUnit(Duration.ofNanos(1), 1);
        assertTimeAndUnit(Duration.ofNanos(1_000), 1_000);

        assertTimeAndUnit(Duration.ofMillis(1), 1_000_000);
        assertTimeAndUnit(Duration.ofMillis(1_000), 1_000_000_000);

        assertTimeAndUnit(Duration.ofSeconds(1), 1 * 1_000_000_000);
        assertTimeAndUnit(Duration.ofSeconds(1_000), 1_000L * 1_000_000_000L);
    }

    private void assertTimeAndUnit(final Duration duration, final long expectedNanos) {
        final AtomicLong timeResult = new AtomicLong();
        final AtomicReference<TimeUnit> unitResult = new AtomicReference<>();

        Durations.consume(duration, (time, unit) -> {
            timeResult.set(time);
            unitResult.set(unit);
        });

        Assert.assertEquals(expectedNanos, unitResult.get().toNanos(timeResult.get()));
    }
}
