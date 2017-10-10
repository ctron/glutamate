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
package io.glutamate.util;

import java.util.Objects;
import java.util.Optional;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

/**
 * Helper for work with {@link Optional}s
 */
public final class Optionals {

    private Optionals() {
    }

    /**
     * Compares both values, when present
     *
     * @param first
     *            value
     * @param second
     *            value
     * @return {@code true} if the first value is present and
     *         {@link Object#equals(Object)} to the second value or the first value
     *         is not present and the second value is {@code null}. Any other case
     *         returns {@code false}.
     */
    public static boolean presentAndEqual(final @Nullable Optional<?> first, final @Nullable Object second) {
        Objects.requireNonNull(first);

        if (!first.isPresent()) {
            return second == null;
        }

        @NonNull
        final Object v = first.get();

        return v.equals(second);
    }
}
