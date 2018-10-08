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
package io.glutamate.str;

import static com.google.common.io.Resources.getResource;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.io.Resources;

public class TablesTest {

    @Test
    public void test1() throws IOException {
        assertTable("table1");
    }

    private void assertTable(final String string) throws IOException {

        List<String> header = null;
        final List<List<String>> data = new LinkedList<>();

        for (final String line : Resources.readLines(getResource(TablesTest.class, string + ".in"), UTF_8)) {
            final String[] toks = line.split("\\s+");
            if (header == null) {
                header = Arrays.asList(toks);
            } else {
                data.add(Arrays.asList(toks));
            }
        }

        if (header == null) {
            header = new LinkedList<>();
        }

        final String out = Resources.toString(getResource(TablesTest.class, string + ".out"), UTF_8);

        final String result = Tables.showTable(new StringBuilder(), header, data, 2).toString();

        System.out.println("----");
        System.out.println(out);
        System.out.println("----");
        System.out.println(result);
        System.out.println("----");

        Assert.assertEquals(out, result);
    }

}
