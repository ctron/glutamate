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
package io.glutamate.util.concurrent;

import java.util.Objects;
import java.util.concurrent.CompletionStage;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * A closable version of the {@link CompletionStage}
 *
 * @param <T>
 *            the type of the result
 */
@NonNullByDefault
public interface CloseableCompletionStage<T> extends CompletionStage<T>, AutoCloseable {

    /**
     * . Create a new {@link CloseableCompletionStage} from a
     * {@link CompletionStage}
     *
     * @param stage
     *            the stage to wrap
     * @param closeHandler
     *            the close handler which will be called once when the stage is
     *            closed
     * @param <T>
     *            the result of the stage
     * @return the new closable stage
     */
    public static <T> CloseableCompletionStage<T> of(final CompletionStage<T> stage, final AutoCloseable closeHandler) {
        Objects.requireNonNull(stage);
        Objects.requireNonNull(closeHandler);

        return new CloseableCompletionStageImpl<>(stage, closeHandler);
    }

    /**
     * Create a new {@link CloseableCompletionStage} from a {@link CompletionStage}.
     *
     * @param stage
     *            the stage to wrap
     * @param <T>
     *            the result of the stage
     * @return the new closable stage
     */
    public static <T> CloseableCompletionStage<T> of(final CompletionStage<T> stage) {
        return of(stage, () -> {
        });
    }
}
