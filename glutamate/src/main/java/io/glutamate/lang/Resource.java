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

import java.util.Objects;
import java.util.concurrent.ExecutorService;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * A resource, which may be managed or used.
 * <p>
 * The intention of this class is to make is easier for classes to work with
 * resources which could either be managed or simply used, not requiring the
 * actual class to keep track of this information.
 *
 * @param <T>
 *            The type of the resource
 *
 */
@NonNullByDefault
public final class Resource<T> implements AutoCloseable {

    private final T resource;
    private final ThrowingConsumer<T> closeHandler;

    private Resource(final T resource, final ThrowingConsumer<T> closeHandler) {
        this.resource = resource;
        this.closeHandler = closeHandler;
    }

    @Override
    public void close() throws Exception {
        this.closeHandler.consume(this.resource);
    }

    /**
     * Get the actual resource
     *
     * @return the resource
     */
    public T get() {
        return this.resource;
    }

    /**
     * Create a "used" resource wrapper.
     * <p>
     * A resource which is "used" will not be closed when the Resource instance gets
     * closed.
     *
     * @param resource
     *            The resource to wrap
     * @return A new resource instance
     */
    public static <T> Resource<T> use(final T resource) {
        Objects.requireNonNull(resource);
        return new Resource<>(resource, ThrowingConsumer.empty());
    }

    /**
     * Create a "managed" resource wrapper.
     * <p>
     * A resource which is "managed" will be closed when the Resource instance gets
     * closed.
     *
     * @param closeable
     *            The closable to wrap.
     * @return A new resource instance
     */
    public static <T extends AutoCloseable> Resource<T> manage(final T closeable) {
        Objects.requireNonNull(closeable);
        return new Resource<>(closeable, AutoCloseable::close);
    }

    /**
     * Create a "managed" resource wrapper.
     * <p>
     * A resource which is "managed" will be closed when the Resource instance gets
     * closed.
     *
     * @param resource
     *            The resource to manage
     * @param closeHandler
     *            the code which will close the resource
     * @return A new resource instance
     */
    public static <T> Resource<T> manage(final T resource, final ThrowingConsumer<T> closeHandler) {

        Objects.requireNonNull(resource);
        Objects.requireNonNull(closeHandler);

        return new Resource<>(resource, closeHandler);
    }

    /**
     * Create a "managed" resource wrapper for an {@link ExecutorService}
     * <p>
     * A resource which is "managed" will be closed when the Resource instance gets
     * closed. Closing the resource will issue a call to
     * {@link ExecutorService#shutdown()}, but it will not wait for the shutdown to
     * complete.
     *
     * @param resource
     *            The resource to manage
     * @return A new resource instance
     */
    public static <T extends ExecutorService> Resource<T> manage(final T resource) {
        return manage(resource, ExecutorService::shutdown);
    }
}
