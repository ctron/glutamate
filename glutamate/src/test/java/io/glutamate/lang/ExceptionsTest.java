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

import java.util.Optional;

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
}
