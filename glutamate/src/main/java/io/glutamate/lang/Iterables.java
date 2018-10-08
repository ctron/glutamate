/*******************************************************************************
 * Copyright (c) 2018 Red Hat Inc and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Jens Reimann - initial API and implementation
 *******************************************************************************/
package io.glutamate.lang;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * Helpers for working with {@link Iterable}s.
 */
public final class Iterables {
    private Iterables() {
    }

    public static <T> Iterable<T> from(final Enumeration<T> enumeration) {

        if (enumeration == null) {
            return null;
        }

        return new Iterable<T>() {

            @Override
            public Iterator<T> iterator() {
                return new Iterator<T>() {

                    @Override
                    public boolean hasNext() {
                        return enumeration.hasMoreElements();
                    }

                    @Override
                    public T next() {
                        return enumeration.nextElement();
                    }
                };
            }
        };
    }
}
