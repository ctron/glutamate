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

import java.util.Objects;
import java.util.concurrent.CompletionStage;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.glutamate.internal.concurrent.CloseableCompletionStageImpl;

@NonNullByDefault
public interface CloseableCompletionStage<T> extends CompletionStage<T>, AutoCloseable {

    public static <T> CloseableCompletionStage<T> of(final CompletionStage<T> stage, final AutoCloseable closeHandler) {
        Objects.requireNonNull(stage);
        Objects.requireNonNull(closeHandler);

        return new CloseableCompletionStageImpl<>(stage, closeHandler);
    }

    public static <T> CloseableCompletionStage<T> of(final CompletionStage<T> stage) {
        return of(stage, () -> {
        });
    }
}
