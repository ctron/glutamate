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

import static io.glutamate.lang.Exceptions.ignore;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;

public class ExceptionsTest {

    public static void doStuff() throws Exception {
        throw new Exception();
    }

    public static void doStringStuff(final String value) throws Exception {
        throw new Exception();
    }

    @Test(expected = RuntimeException.class)
    public void test1() {
        Exceptions.ignore(() -> {
            throw new Exception();
        });
    }

    @SuppressWarnings("unused")
    @Test(expected = RuntimeException.class)
    public void test2() {
        Exceptions.ignore(() -> {
            if (true) {
                throw new Exception();
            }
            return null;
        });
    }

    @Test(expected = RuntimeException.class)
    public void test3() {
        Exceptions.ignore(ExceptionsTest::doStuff);
    }

    @Test(expected = RuntimeException.class)
    public void test4() {
        Optional.of("Foo").ifPresent(ignore(ExceptionsTest::doStringStuff));
    }

    @Test
    public void testCause1() {
        try {
            Exceptions.ignore(() -> {
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

    @Test
    public void testCauseNull1() {
        Exceptions.getCause(null);
        Exceptions.getCauseMessage(null);
    }

    @Test
    public void testCauseNull2() {
        try {
            Exceptions.ignore(() -> {
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
            Assert.assertNull(Exceptions.getCause(e).getMessage());

            // but getCauseMessage never returns null
            Assert.assertEquals("java.lang.NullPointerException", Exceptions.getCauseMessage(e));
        }
    }
}
