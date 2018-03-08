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

import java.util.Objects;

/**
 * Helper for working with {@link ClassLoader}s.
 */
public final class ClassLoaders {

    private ClassLoaders() {
    }

    /**
     * Execute code having a specific "context class loader" set. <br>
     *
     * This method will execute some code setting and restoring the context class
     * loader before/after the execution. The context class loader is set by
     * {@code Thread.currentThread().setContextClassLoader()}.
     *
     * The class loader may be null. See
     * {@link Thread#setContextClassLoader(ClassLoader)} for more details about
     * that.
     *
     * @param classLoader
     *            The class loader to set, may be {@code null}
     * @param callable
     *            the callable to execute
     * @return The result of the callable
     * @throws X
     *             The exception type of the callable
     */
    public static final <T, X extends Throwable> T callWithClassLoader(final ClassLoader classLoader,
            final ThrowingCallable<T, X> callable) throws X {

        Objects.requireNonNull(callable);

        final ClassLoader ccl = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(classLoader);
            return callable.call();
        } finally {
            Thread.currentThread().setContextClassLoader(ccl);
        }
    }

    /**
     * Execute code having a specific "context class loader" set.
     *
     * @see #callWithClassLoader(ClassLoader, ThrowingCallable)
     *
     * @param clazz
     *            The class to get the class loader from. May be {@code null}.
     * @param callable
     *            the callable to execute
     * @return The result of the callable
     * @throws X
     *             The exception type of the callable
     */
    public static final <T, X extends Throwable> T callWithClassLoader(final Class<?> clazz,
            final ThrowingCallable<T, X> callable) throws X {
        return callWithClassLoader(clazz == null ? null : clazz.getClassLoader(), callable);
    }

    /**
     * Execute code having a specific "context class loader" set.
     *
     * @see #callWithClassLoader(ClassLoader, ThrowingCallable)
     * @param classLoader
     *            The class loader to set, may be {@code null}
     * @param runnable
     *            the runnable to execute
     * @throws X
     *             The exception type of the callable
     */
    public static final <X extends Throwable> void runWithClassLoader(final ClassLoader classLoader,
            final ThrowingRunnable<X> runnable) throws X {

        callWithClassLoader(classLoader, () -> {
            runnable.run();
            return null;
        });

    }

    /**
     * Execute code having a specific "context class loader" set.
     *
     * @see #callWithClassLoader(ClassLoader, ThrowingCallable)
     * @param clazz
     *            The class to get the class loader from. May be {@code null}.
     * @param runnable
     *            the runnable to execute
     * @throws X
     *             The exception type of the callable
     */
    public static final <X extends Throwable> void runWithClassLoader(final Class<?> clazz,
            final ThrowingRunnable<X> runnable) throws X {
        runWithClassLoader(clazz == null ? null : clazz.getClassLoader(), runnable);
    }

};