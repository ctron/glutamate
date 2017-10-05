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

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Test;

import io.glutamate.lang.Annotations.ScanMode;

public class AnnotationsTest {

    @MockAnnotation("A")
    public static class A {
    }

    public static class B extends A {
    }

    public static class C {
    }

    @MockAnnotation("D")
    public interface D {
    }

    public static class E implements D {
    }

    public static class F extends E {
    }

    @MockAnnotation("G")
    public static class G extends E {
    }

    public static class H extends G {
    }

    @MockAnnotation("I")
    public interface I {

    }

    public static class J extends H implements I, D {
    }

    @Test
    public void test1() {
        assertAnnotation(B.class, "A", "A");
    }

    @Test
    public void test2() {
        assertAnnotation(D.class, "D", "D");
        assertAnnotation(E.class, "D", "D");
        assertAnnotation(F.class, "D", "D");
    }

    @Test
    public void test3() {
        assertAnnotation(G.class, "G", "G");
        assertAnnotation(H.class, "G", "G");
    }

    @Test
    public void test4() {
        assertAnnotation(J.class, "G", "I");
    }

    @Test
    public void testNotPresent1() {
        assertAnnotation(C.class, null, null);
    }

    private void assertAnnotation(final @NonNull Class<?> clazz, final String expectedValue1,
            final String expectedValue2) {
        final String result1 = Annotations.scanFor(MockAnnotation.class, clazz, ScanMode.DEPTH)
                .map(MockAnnotation::value).orElse(null);
        final String result2 = Annotations.scanFor(MockAnnotation.class, clazz, ScanMode.BREADTH)
                .map(MockAnnotation::value).orElse(null);

        Assert.assertEquals(expectedValue1, result1);
        Assert.assertEquals(expectedValue2, result2);
    }
}
