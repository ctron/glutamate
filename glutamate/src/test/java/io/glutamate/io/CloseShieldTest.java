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
package io.glutamate.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Test;

public class CloseShieldTest {

    private static class MockInputStream extends InputStream {

        private boolean closed;

        @Override
        public void close() throws IOException {
            this.closed = true;
        }

        @Override
        public int read() throws IOException {
            return 0;
        }

        public boolean isClosed() {
            return this.closed;
        }

        @Override
        public synchronized void reset() throws IOException {
        }

    }

    private static class MockOutputStream extends OutputStream {
        private boolean closed;

        @Override
        public void close() throws IOException {
            this.closed = true;
        }

        @Override
        public void write(final int b) throws IOException {
        }

        public boolean isClosed() {
            return this.closed;
        }
    }

    @Test
    public void testClose1() throws IOException {
        final MockInputStream mockedStream = new MockInputStream();

        final InputStream stream = Close.shield(mockedStream);

        stream.read();
        stream.read(new byte[1]);
        stream.read(new byte[1], 0, 1);
        stream.mark(0);
        stream.available();
        stream.reset();
        stream.skip(0);
        stream.markSupported();
        stream.toString();

        stream.close();

        Assert.assertFalse(mockedStream.isClosed());

        mockedStream.close();
        Assert.assertTrue(mockedStream.isClosed());
    }

    @Test
    public void testClose2() throws IOException {
        final MockOutputStream mockedStream = new MockOutputStream();

        final OutputStream stream = Close.shield(mockedStream);

        stream.write(0);
        stream.write(new byte[] { 0 });
        stream.write(new byte[] { 0 }, 0, 1);
        stream.flush();
        stream.toString();

        stream.close();

        Assert.assertFalse(mockedStream.isClosed());

        mockedStream.close();
        Assert.assertTrue(mockedStream.isClosed());
    }
}
