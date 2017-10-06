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
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import io.glutamate.time.Durations;

/**
 * Helper for awaiting the completion of asynchronous operations.
 */
@NonNullByDefault
public final class Await {
    private Await() {
    }

    /**
     * Await the completion of a completion stage.
     * <p>
     * Any error which gets thrown by the stage or the waiting process (even the
     * timeout) will be wrapped in a {@link RuntimeException}. If the cause of the
     * failure is important it is present in the cause of the RuntimeException.
     *
     * @param stage
     *            the stage to wait for
     * @param milliseconds
     *            The number of milliseconds to wait for. If the value is negative
     *            or zero it will be an infinite wait.
     * @return the result of the completion stage
     */
    public static <T> T await(final CompletionStage<T> stage, final long milliseconds) {

        Objects.requireNonNull(stage);

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

    /**
     * Await the completion of a completion stage.
     * <p>
     * Any error which gets thrown by the stage or the waiting process (even the
     * timeout) will be wrapped in a {@link RuntimeException}. If the cause of the
     * failure is important it is present in the cause of the RuntimeException.
     *
     * @param stage
     *            the stage to wait for
     * @param duration
     *            The duration to wait for. If the value is {@code null} or zero, it
     *            will wait forever. The method will use the absolute value of the
     *            duration, so negative durations will be converted into positive
     *            durations.
     * @return the result of the completion stage
     */
    public static <T> T await(final CompletionStage<T> stage, @Nullable final Duration duration) {

        final CompletableFuture<T> future = stage.toCompletableFuture();

        try {

            if (duration != null && !duration.isZero()) {
                return Durations.map(duration.abs(), future::get);
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
