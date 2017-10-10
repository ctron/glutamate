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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link Resource}.
 */
public class ResourceTest {

    /**
     * Mock resource.
     */
    public static class MyResource {
        /**
         * Number of times {@link #dispose()} got called.
         */
        public int closed;

        /**
         * Simulate a resource disposal, don't use close here.
         */
        public void dispose() {
            this.closed++;
        }
    }

    /**
     * Mock resource which implements {@link AutoCloseable}.
     */
    public static class MyCloseable extends MyResource implements AutoCloseable {
        @Override
        public void close() throws Exception {
            dispose();
        }
    }

    /**
     * Test using a resource.
     *
     * @throws Exception
     *             if anything goes wrong
     */
    @Test
    public void test1() throws Exception {

        final MyResource r1 = new MyResource();

        try (final Resource<MyResource> r = Resource.use(r1)) {
            Assert.assertEquals(0, r1.closed);
            Assert.assertTrue(r.get() == r1);
        }
        Assert.assertEquals(0, r1.closed);
    }

    /**
     * Test managing a resource.
     *
     * @throws Exception
     *             if anything goes wrong
     */
    @Test
    public void test2() throws Exception {

        final MyResource r1 = new MyResource();

        try (final Resource<MyResource> r = Resource.manage(r1, mr -> mr.dispose())) {
            Assert.assertEquals(0, r1.closed);
            Assert.assertTrue(r.get() == r1);
        }

        Assert.assertEquals(1, r1.closed);
    }

    /**
     * Test managing a closable.
     *
     * @throws Exception
     *             if anything goes wrong
     */
    @Test
    public void test3() throws Exception {

        final MyCloseable c1 = new MyCloseable();

        try (final Resource<MyCloseable> r = Resource.manage(c1)) {
            Assert.assertEquals(0, c1.closed);
            Assert.assertTrue(r.get() == c1);
        }

        Assert.assertEquals(1, c1.closed);
    }

    /**
     * Test managing an executor service.
     *
     * @throws Exception
     *             if anything goes wrong
     */
    @Test
    public void test4() throws Exception {

        final ExecutorService e1 = Executors.newCachedThreadPool();

        Assert.assertEquals(false, e1.isShutdown());

        try (final Resource<ExecutorService> r = Resource.use(e1)) {
            Assert.assertEquals(false, e1.isShutdown());
            Assert.assertTrue(r.get() == e1);
        }

        Assert.assertEquals(false, e1.isShutdown());

        try (final Resource<ExecutorService> r = Resource.manage(e1)) {
            Assert.assertEquals(false, e1.isShutdown());
            Assert.assertTrue(r.get() == e1);
        }

        Assert.assertEquals(true, e1.isShutdown());
    }

}
