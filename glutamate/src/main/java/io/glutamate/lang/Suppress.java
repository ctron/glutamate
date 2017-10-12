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
package io.glutamate.lang;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 *
 * @param <X>
 *            Exception type which is thrown
 */
@NonNullByDefault
public final class Suppress<X extends Exception> implements AutoCloseable {

    private final Class<X> clazz;
    private final Function<Throwable, X> exceptionCreator;

    private @Nullable LinkedList<Throwable> errors = null;

    private Suppress(final Class<X> clazz, final Function<Throwable, X> exceptionCreator) {
        this.clazz = clazz;
        this.exceptionCreator = exceptionCreator;
    }

    private void addError(final Throwable e) {

        LinkedList<Throwable> errors = this.errors;

        if (errors == null) {
            this.errors = errors = new LinkedList<>();
        }

        errors.add(e);
    }

    @Override
    public void close() throws X {

        final LinkedList<Throwable> errors = this.errors;

        if (errors == null || errors.isEmpty()) {
            return;
        }

        final Throwable e = errors.pollFirst();

        X result;
        if (this.clazz.isAssignableFrom(e.getClass())) {
            result = this.clazz.cast(e);
        } else {
            result = this.exceptionCreator.apply(e);
        }

        errors.forEach(result::addSuppressed);
        throw result;
    }

    /**
     * Run code and suppress exceptions
     *
     * @param runnable
     *            the runnable to run
     */
    public void run(final ThrowingRunnable runnable) {
        try {
            runnable.run();
        } catch (final Exception e) {
            addError(e);
        }
    }

    /**
     * Close a resource and suppress exceptions
     *
     * @param closeable
     *            the closable to close
     */
    public void close(final AutoCloseable closeable) {
        Objects.requireNonNull(closeable);

        run(() -> closeable.close());
    }

    /**
     * Close a resource and suppress exceptions
     *
     * @param closeables
     *            the closable to close
     */
    public void close(final Iterable<? extends AutoCloseable> closeables) {
        Objects.requireNonNull(closeables);

        closeables.forEach(this::close);
    }

    /**
     * Close a resource and suppress exceptions
     *
     * @param closeables
     *            the closable to close
     */
    public void close(final @NonNull AutoCloseable... closeables) {
        Objects.requireNonNull(closeables);

        Arrays.stream(closeables).forEach(this::close);
    }

    public static <X extends Exception> Suppress<X> of(final Class<X> clazz,
            final Function<Throwable, X> exceptionCreator) {

        Objects.requireNonNull(clazz);
        Objects.requireNonNull(exceptionCreator);

        return new Suppress<>(clazz, exceptionCreator);
    }

    public static Suppress<RuntimeException> runtime() {
        return new Suppress<>(RuntimeException.class, RuntimeException::new);
    }

    public static Suppress<Exception> exception() {
        return new Suppress<>(Exception.class, Exception::new);
    }
}
