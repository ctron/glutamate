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

import static org.junit.Assert.assertEquals;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.junit.Test;

@NonNullByDefault
public class SuppliersTest {

    @Test
    public void testConst1() {
        assertEquals(true, Suppliers.constant(true).getAsBoolean());
        assertEquals(false, Suppliers.constant(false).getAsBoolean());

        assertEquals(true, Suppliers.ofTrue().getAsBoolean());
        assertEquals(false, Suppliers.ofFalse().getAsBoolean());
    }

    @Test
    public void testConst2() {
        assertEquals(0, Suppliers.constant(0).getAsInt());
        assertEquals(1, Suppliers.constant(1).getAsInt());
        assertEquals(Integer.MAX_VALUE, Suppliers.constant(Integer.MAX_VALUE).getAsInt());
    }

    @Test
    public void testConst3() {
        assertEquals("Foo", Suppliers.constant("Foo").get());
        assertEquals("Bar", Suppliers.constant("Bar").get());
    }

    @Test
    public void testConst4() {
        assertEquals(null, Suppliers.constant(null).get());
    }

    @Test
    public void testConst5() {
        assertEquals(0L, Suppliers.constant(0L).getAsLong());
        assertEquals(1L, Suppliers.constant(1L).getAsLong());
        assertEquals(Long.MAX_VALUE, Suppliers.constant(Long.MAX_VALUE).getAsLong());
    }

    @Test
    public void testConst6() {
        assertEquals(0.0, Suppliers.constant(0.0).getAsDouble(), 0.01);
        assertEquals(0.1, Suppliers.constant(0.1).getAsDouble(), 0.01);
        assertEquals(Double.MAX_VALUE, Suppliers.constant(Double.MAX_VALUE).getAsDouble(), 1.0);
    }
}
