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

import static io.glutamate.lang.Exceptions.wrap;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@link Exceptions}
 */
@SuppressWarnings("null")
public class ExceptionsTest {

    /**
     * Mock method throwing exception
     *
     * @throws Exception
     *             always
     */
    public static void doStuff() throws Exception {
        throw new Exception();
    }

    /**
     * Mock method throwing exception
     *
     * @param value
     *            dummy value
     *
     * @throws Exception
     *             always
     */
    public static void doStringStuff(final String value) throws Exception {
        throw new Exception();
    }

    /**
     * Wrap a {@link Callable}
     */
    @Test(expected = RuntimeException.class)
    public void test1() {
        Exceptions.wrap(() -> {
            throw new Exception();
        });
    }

    /**
     * Wrap a {@link Callable}
     */
    @SuppressWarnings("unused")
    @Test(expected = RuntimeException.class)
    public void test2() {
        Exceptions.wrap(() -> {
            if (true) {
                throw new Exception();
            }
            return null;
        });
    }

    /**
     * Wrap a runnable
     */
    @Test(expected = RuntimeException.class)
    public void test3() {
        Exceptions.wrap(ExceptionsTest::doStuff);
    }

    /**
     * Wrap a {@link Consumer}
     */
    @Test(expected = RuntimeException.class)
    public void test4() {
        Optional.of("Foo").ifPresent(wrap(ExceptionsTest::doStringStuff));
    }

    /**
     * Test getting the root cause
     */
    @Test
    public void testCause1() {
        try {
            Exceptions.wrap(() -> {
                throw new IOException("FooBar");
            });
        } catch (final Throwable e) {

            Assert.assertThat(e, instanceOf(RuntimeException.class));

            final Throwable cause = Exceptions.getCause(e);

            Assert.assertThat(cause, instanceOf(IOException.class));

            Assert.assertNotEquals("FooBar", e.getMessage());
            Assert.assertEquals("FooBar", Exceptions.getCauseMessage(e));
        }
    }

    /**
     * Test with nulls
     */
    @Test
    public void testCauseNull1() {
        Exceptions.getCause(null);
        Exceptions.getCauseMessage(null);
    }

    /**
     * Test a NPE
     */
    @Test
    public void testCauseNull2() {
        try {
            Exceptions.wrap(() -> {
                Objects.requireNonNull(null); // expect NPE
            });
        } catch (final Throwable e) {

            // assert wrapped exception
            Assert.assertThat(e, instanceOf(RuntimeException.class));

            // get the cause
            final Throwable cause = Exceptions.getCause(e);

            // assert it is a NPE
            Assert.assertThat(cause, instanceOf(NullPointerException.class));

            // the message of the wrapper is null
            Assert.assertNull(e.getMessage());

            // the message of the cause is null
            Assertions.assertThat(Exceptions.getCause(e))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage(null);

            // but getCauseMessage never returns null
            Assert.assertEquals("java.lang.NullPointerException", Exceptions.getCauseMessage(e));
        }
    }

    /**
     * Test a cause cycle
     */
    @Test
    public void testNestedCycle() {

        final Throwable thrown = Assertions.catchThrowable(() -> {
            final AtomicReference<Exception> ref = new AtomicReference<>();
            final Exception e1 = new Exception("1") {

                private static final long serialVersionUID = 1L;

                @Override
                public synchronized Throwable getCause() {
                    return ref.get();
                }
            };
            final Exception e2 = new Exception("2", e1);
            final Exception e3 = new Exception("3", e2);
            ref.set(e3);
            throw e3;
        });

        Assertions.assertThat(thrown)
                .isNotNull()
                .isInstanceOf(Exception.class);

        // get the cause
        final Throwable cause = Exceptions.getCause(thrown);

        Assertions.assertThat(cause)
                .hasMessage("1");

    }
}
