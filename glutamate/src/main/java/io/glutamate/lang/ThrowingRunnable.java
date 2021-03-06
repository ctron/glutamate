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
package io.glutamate.lang;

/**
 * A runnable throwing an exception.
 *
 * @param <X>
 *            The exception type this runnable might throw
 */
@FunctionalInterface
public interface ThrowingRunnable<X extends Throwable> {
    /**
     * Run boy run
     *
     * @throws X
     *             if anything goes wrong
     */
    public void run() throws X;
}
