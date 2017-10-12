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

import static io.glutamate.lang.Suppress.exception;
import static io.glutamate.lang.Suppress.runtime;

import java.io.IOException;

import org.assertj.core.api.Assertions;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.junit.Test;

/**
 * Unit tests for {@link Suppress}.
 */
@NonNullByDefault
public class SuppressTest {

    @Test
    public void test1() {

        try (Suppress<RuntimeException> s = runtime()) {
            s.run(() -> {
            });
        }

    }

    @Test
    public void test2() throws Exception {

        try (Suppress<Exception> s = exception()) {
            s.run(() -> {
            });
        }

    }

    @Test
    public void test3() throws Exception {

        Assertions.assertThatThrownBy(() -> {
            try (Suppress<RuntimeException> s = runtime()) {
                s.run(() -> {
                    throw new IOException("Foo bar");
                });
                s.run(() -> {
                    throw new IOException("Foo bar 2");
                });
            }
        })
                .isInstanceOf(Exception.class)
                .hasMessage("java.io.IOException: Foo bar")
                .hasCauseInstanceOf(IOException.class)
                .hasSuppressedException(new IOException("Foo bar 2"));

    }

    @Test
    public void test4() throws Exception {

        Assertions.assertThatThrownBy(() -> {
            try (Suppress<RuntimeException> s = runtime()) {
                s.run(() -> {
                    throw new RuntimeException("Foo bar");
                });
                s.run(() -> {
                    throw new IOException("Foo bar 2");
                });
            }
        })
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Foo bar")
                .hasNoCause()
                .hasSuppressedException(new IOException("Foo bar 2"));

    }
}
