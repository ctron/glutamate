/*******************************************************************************
 * Copyright (c) 2017, 2018 Red Hat Inc and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Jens Reimann - initial API and implementation
 *******************************************************************************/
package io.glutamate.util.function;

import java.util.Objects;
import java.util.UUID;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 * Helpers to work with {@link Supplier}s.
 */
@NonNullByDefault
public final class Suppliers {

    private static final Supplier<?> NULL_SUPPLIER = () -> null;

    private Suppliers() {
    }

    /**
     * Create a supplier returning always the provided value.
     *
     * @param value
     *            the value to return
     * @param <T>
     *            the value type
     * @return the supplier providing the value
     */
    public static <@Nullable T> Supplier<T> constant(final T value) {
        return () -> value;
    }

    /**
     * Create a supplier returning always the provided value.
     *
     * @param value
     *            the value to return
     * @return the supplier providing the value
     */
    public static IntSupplier constant(final int value) {
        return () -> value;
    }

    /**
     * Create a supplier returning always the provided value.
     *
     * @param value
     *            the value to return
     * @return the supplier providing the value
     */
    public static LongSupplier constant(final long value) {
        return () -> value;
    }

    /**
     * Create a supplier returning always the provided value.
     *
     * @param value
     *            the value to return
     * @return the supplier providing the value
     */
    public static DoubleSupplier constant(final double value) {
        return () -> value;
    }

    /**
     * Create a supplier returning always the provided value.
     *
     * @param value
     *            the value to return
     * @return the supplier providing the value
     */
    public static BooleanSupplier constant(final boolean value) {
        return () -> value;
    }

    /**
     * Create a supplier returning always {@code true}.
     *
     * @return the supplier providing {@code true}
     */
    public static BooleanSupplier ofTrue() {
        return () -> true;
    }

    /**
     * Create a supplier returning always {@code false}.
     *
     * @return the supplier providing {@code false}
     */
    public static BooleanSupplier ofFalse() {
        return () -> false;
    }

    /**
     * Provide a {@link Supplier} mapping values of another {@link Supplier}.
     *
     * @param supplier
     *            the supply to provide original values
     * @param function
     *            the mapper function
     * @param <T>
     *            the source value type
     * @param <M>
     *            the target value type
     * @return a supplier providing values of the original supplier, mapped by the
     *         mapping function
     */
    public static <T, M> Supplier<M> map(final Supplier<T> supplier, final Function<T, M> function) {
        Objects.requireNonNull(supplier);
        Objects.requireNonNull(function);

        return () -> function.apply(supplier.get());
    }

    /**
     * Create a supplier which provides UUIDs.
     * <p>
     * UUIDs are generated by {@code UUID.randomUUID()}
     *
     * @return a new supplier providing UUIDs
     */
    @SuppressWarnings("null")
    public static Supplier<UUID> uuid() {
        return () -> UUID.randomUUID();
    }

    /**
     * Create a supplier which provides UUIDs as strings.
     * <p>
     * UUIDs are generated by {@code UUID.randomUUID().toString()}
     *
     * @return a new supplier providing UUIDs
     */
    @SuppressWarnings("null")
    public static Supplier<String> uuidString() {
        return () -> UUID.randomUUID().toString();
    }

    /**
     * Create a new supplier returning {@code null}.
     *
     * @param <T>
     *            The type of the supplier
     *
     * @return A new supplier always returning {@code null}
     */
    @SuppressWarnings("unchecked")
    public static <@Nullable T> Supplier<T> empty() {
        return (Supplier<@Nullable T>) NULL_SUPPLIER;
    }

}
