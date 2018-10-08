/*******************************************************************************
 * Copyright (c) 2018 Red Hat Inc and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Jens Reimann - initial API and implementation
 *******************************************************************************/

package io.glutamate.lang;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 * Tools for working with environment variables.
 */
@NonNullByDefault
public final class Environment {

    private Environment() {
    }

    /**
     * Get the value of an environment variable.
     *
     * @param name
     *            The name of the variable to get.
     * @return The optional value of the value.
     */
    public static Optional<@Nullable String> get(final String name) {
        @SuppressWarnings("null")
        final @NonNull Optional<@Nullable String> result = Optional.ofNullable(System.getenv(name));
        return result;
    }

    /**
     * Get a required value, or fail with an exception.
     *
     * @param name
     *            The name of the variable to get.
     * @return The value of the environment variable, never returns {@code null}.
     * @throws IllegalArgumentException
     *             If the environment variable is unset.
     */
    public static String getRequired(final String name) {
        @SuppressWarnings("null")
        final String result = (@NonNull String) get(name)
                .orElseThrow(
                        () -> new IllegalArgumentException(String.format("Missing environment variable: '%s'", name)));
        return result;
    }

    /**
     * Get an environment variable converted to a type.
     *
     * @param name
     *            The name of the variable to get.
     * @param converter
     *            The converter to apply.
     * @param <T>
     *            The type to convert to
     * @return The optional value of the variable, converted to the target type.
     */
    public static <@Nullable T> Optional<T> getAs(final String name, final Function<String, T> converter) {
        @SuppressWarnings("null")
        final Function<@Nullable String, T> cvt = (Function<@Nullable String, T>) converter;
        @SuppressWarnings("null")
        final @NonNull Optional<@Nullable T> result = get(name).map(cvt);
        return result;
    }

    /**
     * Get an environment variable converted to a type.
     *
     * @param name
     *            The name of the variable to get.
     * @param defaultValue
     *            The default value.
     * @param converter
     *            The converter to apply.
     * @param <T>
     *            The type to convert to
     * @return The value of the variable, converted to the target type, or the
     *         default value.
     */
    public static <@Nullable T> T getAs(final String name, final T defaultValue, final Function<String, T> converter) {
        @SuppressWarnings("null")
        final Function<@Nullable String, T> cvt = (Function<@Nullable String, T>) converter;
        return get(name).map(cvt).orElse(defaultValue);
    }

    /**
     * Consume an environment variable, converted to a type.
     *
     * @param name
     *            The name of the variable to consume.
     * @param converter
     *            The converter to apply.
     * @param <T>
     *            The type to convert to
     * @param consumer
     *            The consumer which will be called if the value is set.
     */
    public static <@Nullable T> void consumeAs(final String name, final Function<String, T> converter,
            final Consumer<T> consumer) {
        getAs(name, converter).ifPresent(consumer);
    }

    /**
     * Call the runnable if the environment variable is "true".
     *
     * <p>
     * An environment variable is considered "true" if it is set and
     * {@link Boolean#parseBoolean(String)} returns {@code true}.
     * </p>
     *
     * @param name
     *            The name of the variable to test.
     * @param runnable
     *            The runnable to call if the variable is "true".
     */
    public static void is(final String name, final Runnable runnable) {
        if (Boolean.TRUE.equals(getAs(name, false, Boolean::parseBoolean))) {
            runnable.run();
        }
    }

}