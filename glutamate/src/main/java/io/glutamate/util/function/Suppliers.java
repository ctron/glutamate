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
package io.glutamate.util.function;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

@NonNullByDefault
public final class Suppliers {
    private Suppliers() {
    }

    public static <@Nullable T> Supplier<T> constant(final T value) {
        return () -> value;
    }

    public static IntSupplier constant(final int value) {
        return () -> value;
    }

    public static LongSupplier constant(final long value) {
        return () -> value;
    }

    public static DoubleSupplier constant(final double value) {
        return () -> value;
    }

    public static BooleanSupplier constant(final boolean value) {
        return () -> value;
    }

    public static BooleanSupplier ofTrue() {
        return () -> true;
    }

    public static BooleanSupplier ofFalse() {
        return () -> false;
    }

}
