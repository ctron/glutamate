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

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import io.glutamate.time.Durations;

@NonNullByDefault
public final class Await {
    private Await() {
    }

    public static <T> T await(final CompletionStage<T> stage, final long milliseconds) {

        final CompletableFuture<T> future = stage.toCompletableFuture();

        try {

            if (milliseconds > 0) {
                return future.get(milliseconds, TimeUnit.MILLISECONDS);
            } else {
                return future.get();
            }

        } catch (final InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T await(final CompletionStage<T> stage, @Nullable final Duration duration) {

        final CompletableFuture<T> future = stage.toCompletableFuture();

        try {

            if (duration != null && !duration.isZero()) {
                return Durations.map(duration, future::get);
            } else {
                return future.get();
            }

        } catch (final RuntimeException e) {
            throw e;
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

}
