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

import java.io.IOException;

import org.junit.Test;

/**
 * Tests for {@link ClassLoaders}
 */
public class ClassLoadersTest {

    /**
     * Test if all lambda types match
     * 
     * @throws IOException
     *             should never happen
     */
    @Test
    public void test1() throws IOException {
        ClassLoaders.callWithClassLoader(getClass(), this::method1);
        ClassLoaders.runWithClassLoader(getClass(), this::method2);
        ClassLoaders.callWithClassLoader(getClass(), this::method3);
        ClassLoaders.runWithClassLoader(getClass(), this::method4);
    }

    private String method1() {
        return "";
    }

    private void method2() {
    }

    private String method3() throws IOException {
        return "";
    }

    private void method4() throws IOException {
    }
}
