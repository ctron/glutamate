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

import org.eclipse.jdt.annotation.NonNull;

/**
 * A consumer throwing an Exception.
 *
 * @param <T>
 *            The value type of the consumer
 */
@FunctionalInterface
public interface ThrowingConsumer<T> {
    /**
     * Consume the value, yummy!
     *
     * @param t
     *            the value to consume
     * @throws Exception
     *             if anything goes wrong
     */
    public void consume(T t) throws Exception;

    /**
     * Get a no-op consumer.
     *
     * @return A no-op consumer
     */
    public static <T> @NonNull ThrowingConsumer<T> empty() {
        return (x) -> {
        };
    };
}
