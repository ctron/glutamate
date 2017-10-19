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
import java.io.Reader;
import java.io.Writer;
import java.nio.CharBuffer;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link Close}.
 */
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

    private static class MockReader extends Reader {

        private boolean closed;

        @Override
        public void close() throws IOException {
            this.closed = true;
        }

        public boolean isClosed() {
            return this.closed;
        }

        @Override
        public void reset() throws IOException {
        }

        @Override
        public void mark(final int readAheadLimit) throws IOException {
        }

        @Override
        public int read(final char[] cbuf, final int off, final int len) throws IOException {
            return 0;
        }

    }

    private static class MockWriter extends Writer {
        private boolean closed;

        @Override
        public void close() throws IOException {
            this.closed = true;
        }

        public boolean isClosed() {
            return this.closed;
        }

        @Override
        public void write(final char[] cbuf, final int off, final int len) throws IOException {
        }

        @Override
        public void flush() throws IOException {
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

    @Test
    public void testClose3() throws IOException {
        final MockReader mockedReader = new MockReader();

        final Reader reader = Close.shield(mockedReader);

        reader.read();
        reader.read(new char[1]);
        reader.read(new char[1], 0, 1);
        reader.read(CharBuffer.allocate(1));
        reader.ready();
        reader.mark(0);
        reader.reset();
        reader.skip(0);
        reader.markSupported();

        reader.toString();

        reader.close();

        Assert.assertFalse(mockedReader.isClosed());

        mockedReader.close();
        Assert.assertTrue(mockedReader.isClosed());
    }

    @Test
    public void testClose4() throws IOException {
        final MockWriter mockedWriter = new MockWriter();

        final Writer writer = Close.shield(mockedWriter);

        writer.append("");
        writer.append('A');
        writer.append("A", 0, 1);

        writer.write(0);
        writer.write(new char[] { 0 });
        writer.write(new char[] { 0 }, 0, 1);
        writer.write("");
        writer.write("A", 0, 1);
        writer.flush();

        writer.toString();

        writer.close();

        Assert.assertFalse(mockedWriter.isClosed());

        mockedWriter.close();
        Assert.assertTrue(mockedWriter.isClosed());
    }
};