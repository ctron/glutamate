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

import static io.glutamate.lang.Iterables.from;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@link Iterables}.
 */
public class IterablesTest {

    @Test
    public void testNull() {
        Assert.assertNull(Iterables.from(null));
    }

    @Test
    public void testSimple1() {
        final Hashtable<String, String> hashtable = new Hashtable<>();
        hashtable.put("foo", "1");
        hashtable.put("bar", "1");

        final List<String> result = new ArrayList<>();
        for (final String key : from(hashtable.keys())) {
            result.add(key);
        }

        result.sort(Comparator.naturalOrder());

        Assert.assertArrayEquals(new String[] { "bar", "foo" }, result.toArray(new String[0]));
    }

}
