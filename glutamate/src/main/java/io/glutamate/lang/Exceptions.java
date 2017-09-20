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
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public final class Exceptions {
    private Exceptions() {
    }

    public static <T> Consumer<T> ignore(final ThrowingConsumer<T> consumer) {
        Objects.requireNonNull(consumer);

        return (value) -> ignore(() -> consumer.consume(value));
    }

    public static <T> T ignore(final Callable<T> callable) {
        try {
            return callable.call();
        } catch (final RuntimeException e) {
            throw e;
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void ignore(final ThrowingRunnable runnable) {
        try {
            runnable.run();
        } catch (final RuntimeException e) {
            throw e;
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
