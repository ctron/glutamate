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
package io.glutamate.util.function;

import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@link BiFunctions}
 */
public class BiFunctionsTest {
    /**
     * Basic tests
     */
    @Test
    public void test1() {
        Assert.assertEquals("Foo42BarTest", BiFunctions.formatter("Foo%sBar%s").apply(42, "Test"));
        Assert.assertEquals("Foo42BarTest", BiFunctions.formatter(null, "Foo%sBar%s").apply(42, "Test"));
        Assert.assertEquals("Foo42BarTest", BiFunctions.formatter(Locale.getDefault(), "Foo%sBar%s").apply(42, "Test"));
    }
}
