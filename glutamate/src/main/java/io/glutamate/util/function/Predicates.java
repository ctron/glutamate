/*******************************************************************************
 * Copyright (c) 2019 Red Hat Inc and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Jens Reimann - initial API and implementation
 *******************************************************************************/
package io.glutamate.util.function;

import java.util.function.Predicate;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * Helpers to work with {@link Predicate}s.
 */
@NonNullByDefault
public final class Predicates {

    private Predicates() {
    }

    /**
     * Return a predicate that always passes.
     *
     * @return A predicate which will always pass.
     */
    public static <T> Predicate<T> pass() {
        return x -> true;
    }

    /**
     * Return a predicate that never passes.
     *
     * @return A predicate which will never pass.
     */
    public static <T> Predicate<T> fail() {
        return x -> false;
    }
}
