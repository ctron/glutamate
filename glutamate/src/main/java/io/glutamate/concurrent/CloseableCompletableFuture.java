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
package io.glutamate.concurrent;

import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.jdt.annotation.Nullable;

public class CloseableCompletableFuture<T> extends CompletableFuture<T> implements CloseableCompletionStage<T> {

    private static class Entry {
        @Nullable
        Entry next;
        @Nullable
        Runnable runnable;
        boolean closed;
    }

    private final AtomicReference<Entry> closing = new AtomicReference<>();

    @Override
    public CloseableCompletableFuture<T> toCompletableFuture() {
        return this;
    }

    public void whenClosed(final Runnable runnable) {
        Objects.requireNonNull(runnable);

        final Entry next = new Entry();
        next.runnable = runnable;

        do {
            next.next = this.closing.get();
            final Entry nextNext = next.next;
            if (nextNext != null && nextNext.closed) {
                // handle right now
                runnable.run();
                return;
            }

        } while (!this.closing.compareAndSet(next.next, next));
    }

    @Override
    public void close() throws Exception {
        final Entry entry = new Entry();
        entry.closed = true;

        LinkedList<Throwable> errors = null;
        Entry current = this.closing.getAndSet(entry);
        while (current != null && !current.closed) {

            try {
                final Runnable runnable = current.runnable;
                if (runnable != null) {
                    runnable.run();
                }
            } catch (final Throwable e) {
                if (errors == null) {
                    errors = new LinkedList<>();
                }
                errors.add(e);
            }
            current = current.next;

        }

        if (errors != null) {
            final Throwable e = errors.pollFirst();
            final Exception first;
            if (e instanceof Exception) {
                first = (Exception) e;
            } else {
                first = new Exception();
            }

            errors.forEach(first::addSuppressed);
            throw first;
        }
    }

}
