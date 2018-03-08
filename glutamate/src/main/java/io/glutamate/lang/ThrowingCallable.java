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

/**
 * A callable throwing an specified exception type.
 *
 * @param <T>
 *            The type of the return value
 * @param <X>
 *            The exception type this callable might throw
 */
@FunctionalInterface
public interface ThrowingCallable<T, X extends Throwable> {
    /**
     * Call me al
     *
     * @return The result
     *
     * @throws X
     *             if anything goes wrong
     */
    public T call() throws X;
}
