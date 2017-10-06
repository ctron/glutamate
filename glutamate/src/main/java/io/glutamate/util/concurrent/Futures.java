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

import java.util.concurrent.CompletableFuture;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * Helpers for working with all kind of "futures".
 */
@NonNullByDefault
public final class Futures {

    private Futures() {
    }

    /**
     * Create a new, completed, succeeded future.
     * <p>
     * This method is here for symmetry with {@link Futures#failed(Throwable)} and
     * simply calls {@link CompletableFuture#completedFuture(Object)}.
     *
     * @param value
     *            The value of success
     * @param <T>
     *            the future type
     * @return A new, completed, succeeded future
     */
    public static <T> CompletableFuture<T> succeeded(final T value) {
        return CompletableFuture.completedFuture(value);
    }

    /**
     * Create a new, completed, failed future.
     * <p>
     * This method is the companion of
     * {@link CompletableFuture#completedFuture(Object)}, but instead of creating a
     * succeeded future, it creates a failed future.
     *
     * @param error
     *            The error
     * @param <T>
     *            the future type
     * @return A new, completed, failed future instance
     */
    public static <T> CompletableFuture<T> failed(final Throwable error) {

        final CompletableFuture<T> result = new CompletableFuture<>();
        result.completeExceptionally(error);
        return result;

    }
}
