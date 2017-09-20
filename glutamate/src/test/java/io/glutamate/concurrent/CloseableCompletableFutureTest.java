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
package io.glutamate.concurrent;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

public class CloseableCompletableFutureTest {

    @Test
    public void test1() throws Exception {
        final AtomicInteger triggered = new AtomicInteger();

        final CloseableCompletableFuture<?> future = new CloseableCompletableFuture<>();
        future.whenClosed(triggered::incrementAndGet);
        future.close();

        assertEquals(1, triggered.get());
    }

    @Test
    public void test2() throws Exception {
        final AtomicInteger triggered = new AtomicInteger();

        final CloseableCompletableFuture<?> future = new CloseableCompletableFuture<>();
        future.whenClosed(triggered::incrementAndGet);
        future.close();
        future.whenClosed(triggered::incrementAndGet);

        assertEquals(2, triggered.get());
    }

    @Test
    public void testDoubleClose1() throws Exception {
        final AtomicInteger triggered = new AtomicInteger();

        final CloseableCompletableFuture<?> future = new CloseableCompletableFuture<>();
        future.whenClosed(triggered::incrementAndGet);

        future.close();
        future.close();

        assertEquals(1, triggered.get());
    }
}
