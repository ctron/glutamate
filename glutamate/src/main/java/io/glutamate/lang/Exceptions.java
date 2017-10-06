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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.NonNull;

/**
 * Helper for working with exceptions
 */
public final class Exceptions {
    private Exceptions() {
    }

    public static <T> Consumer<T> wrap(@NonNull final ThrowingConsumer<T> consumer) {
        Objects.requireNonNull(consumer);

        return (value) -> wrap(() -> consumer.consume(value));
    }

    public static <T> T wrap(@NonNull final Callable<T> callable) {
        try {
            return callable.call();
        } catch (final RuntimeException e) {
            throw e;
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void wrap(@NonNull final ThrowingRunnable runnable) {
        try {
            runnable.run();
        } catch (final RuntimeException e) {
            throw e;
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the root cause of the error.
     *
     * @param error
     *            to extract the root cause from
     * @return the root cause, or the exception itself if it doesn't have a root
     *         cause, returns {@code null} only when the input was {@code null}
     */
    public static Throwable getCause(Throwable error) {
        if (error == null) {
            return null;
        }

        while (error.getCause() != null && error.getCause() != error) {
            error = error.getCause();
        }

        return error;
    }

    /**
     * Gets the message of the root cause.
     *
     * Effectively a chained call of {@link #getCause(Throwable)} and
     * {@link #getMessage(Throwable)}
     *
     * @param error
     *            The error to inspect
     * @return The message of the root cause, returns {@code null} only when the
     *         input was {@code null}
     */

    public static String getCauseMessage(final Throwable error) {
        return getMessage(getCause(error));
    }

    /**
     * Get the message of an exception.
     *
     * @param error
     *            The error to inspect
     * @return The message or alternatively the class name, returns {@code null}
     *         only when the input was {@code null}
     */
    public static String getMessage(final Throwable error) {
        if (error == null) {
            return null;
        }

        String result;

        // try localized message

        result = error.getLocalizedMessage();
        if (result != null) {
            return result;
        }

        // try message

        result = error.getMessage();
        if (result != null) {
            return result;
        }

        // return class name

        return error.getClass().getName();
    }

    /**
     * Formats the Throwable to a string.
     * <p>
     * Returns a string like {@link Exception#printStackTrace()} would print out.
     *
     * @param error
     *            the error to format
     * @return the formatted exception, {@code null} if the input was {@code null}
     */
    public static String toString(final Throwable error) {
        if (error == null) {
            return null;
        }

        final StringWriter sw = new StringWriter();
        error.printStackTrace(new PrintWriter(sw));

        return sw.toString();
    }
}
