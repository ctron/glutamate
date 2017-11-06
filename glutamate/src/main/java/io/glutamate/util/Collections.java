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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * Helper for work with collections.
 */
@NonNullByDefault
public final class Collections {

    private Collections() {
    }

    /**
     * Allow creating a new map inline.
     * <p>
     * The intended use is:
     *
     * <pre>
     * <code>
     * doMyStuff ( "foo", "bar", map(map {@code ->} {
     *   map.put("key", "value");
     *   map.put("key2", "value2");
     * }));
     * </code>
     * </pre>
     *
     * @param initializer
     *            the initializer to call on the new map instance
     * @return A new map instance
     */
    public static <K, V> Map<K, V> map(final Consumer<Map<K, V>> initializer) {
        Objects.requireNonNull(initializer);

        final Map<K, V> result = new HashMap<>();
        initializer.accept(result);
        return result;
    }

}
