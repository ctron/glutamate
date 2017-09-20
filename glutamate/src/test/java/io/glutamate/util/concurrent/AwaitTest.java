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
package io.glutamate.util.concurrent;

import static java.util.concurrent.CompletableFuture.completedFuture;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import org.junit.Assert;
import org.junit.Test;

import io.glutamate.util.concurrent.Await;

public class AwaitTest {

    @Test
    public void test1() {
        final String result = Await.await(completedFuture("Foo"), null);
        Assert.assertEquals("Foo", result);
    }

    @Test
    public void test2() {
        final String result = Await.await(completedFuture("Foo"), Duration.ofSeconds(1));
        Assert.assertEquals("Foo", result);
    }

    @Test(expected = RuntimeException.class)
    public void test3() {
        Await.await(CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(10_0000);
            } catch (final InterruptedException e) {
            }
        }), Duration.ofSeconds(1));
    }
}
