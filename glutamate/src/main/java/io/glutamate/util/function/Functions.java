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

import java.util.Locale;
import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 * Helpers to work with {@link Function}s.
 *
 * @see BiFunctions
 */
@NonNullByDefault
public final class Functions {
    private Functions() {
    }

    /**
     * Create a function from {@link String#format(String, Object...)}.
     *
     * @param format
     *            the format to use
     * @param <T>
     *            type of the function
     * @return a function which passes the values into
     *         {@link String#format(String, Object...)}
     *
     * @see BiFunctions#formatter(String)
     */
    public static <T> Function<T, String> formatter(final String format) {
        return value -> String.format(format, value);
    }

    /**
     * Create a function from {@link String#format(String, Object...)}.
     *
     * @param locale
     *            the locale to use
     * @param format
     *            the format to use
     * @param <T>
     *            type of the function
     * @return a function which passes the values into
     *         {@link String#format(String, Object...)}
     *
     * @see BiFunctions#formatter(Locale, String)
     */
    public static <T> Function<T, String> formatter(@Nullable final Locale locale, final String format) {
        return value -> String.format(locale, format, value);
    }
}
