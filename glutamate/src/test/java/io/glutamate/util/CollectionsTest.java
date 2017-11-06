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
package io.glutamate.util;

import static org.assertj.core.api.Assertions.entry;

import java.util.Map;
import java.util.function.Consumer;

import org.assertj.core.api.Assertions;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.Test;

/**
 * Unit test for {@link Collections}
 */
public class CollectionsTest {

    /**
     * Test default operation
     */
    public void testMap1() {
        final Map<String, String> result = Collections.map(map -> {
            map.put("foo", "bar");
        });

        Assertions
                .assertThat(result)
                .containsExactly(entry("foo", "bar"));
    }

    /**
     * Test for providing a null value
     */
    @SuppressWarnings("null")
    @Test(expected = NullPointerException.class)
    public void testMap2() {
        Collections.<String, String>map((@NonNull Consumer<Map<String, String>>) null);
    }
}
