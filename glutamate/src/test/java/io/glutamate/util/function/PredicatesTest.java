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
package io.glutamate.util.function;

import static org.junit.Assert.assertEquals;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.junit.Test;

/**
 * Unit test for {@link Predicates}.
 */
@NonNullByDefault
public class PredicatesTest {

    @Test
    public void testFail() {
        assertEquals(false, Predicates.fail().test(null));
    }

    @Test
    public void testPass() {
        assertEquals(true, Predicates.pass().test(null));
    }

}
