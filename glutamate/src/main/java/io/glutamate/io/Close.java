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

/**
 * Help with closing streams.
 */
public final class Close {

    private static final class ShieldedOutputStream extends OutputStream {

        private final OutputStream stream;

        private ShieldedOutputStream(final OutputStream stream) {
            this.stream = stream;
        }

        @Override
        public void write(final int b) throws IOException {
            this.stream.write(b);
        }

        @Override
        public void write(final byte[] b) throws IOException {
            this.stream.write(b);
        }

        @Override
        public void write(final byte[] b, final int off, final int len) throws IOException {
            this.stream.write(b, off, len);
        }

        @Override
        public void flush() throws IOException {
            this.stream.flush();
        }

        @Override
        public void close() throws IOException {
        }

        @Override
        public String toString() {
            return String.format("%s[shielding: %s]", super.toString(), this.stream);
        }
    }

    private static final class ShieldedInputStream extends InputStream {

        private final InputStream stream;

        private ShieldedInputStream(final InputStream stream) {
            this.stream = stream;
        }

        @Override
        public int read() throws IOException {
            return this.stream.read();
        }

        @Override
        public int read(final byte[] b) throws IOException {
            return this.stream.read(b);
        }

        @Override
        public int read(final byte[] b, final int off, final int len) throws IOException {
            return this.stream.read(b, off, len);
        }

        @Override
        public long skip(final long n) throws IOException {
            return this.stream.skip(n);
        }

        @Override
        public int available() throws IOException {
            return this.stream.available();
        }

        @Override
        public void mark(final int readlimit) {
            this.stream.mark(readlimit);
        }

        @Override
        public void reset() throws IOException {
            this.stream.reset();
        }

        @Override
        public boolean markSupported() {
            return this.stream.markSupported();
        }

        @Override
        public void close() throws IOException {
        }

        @Override
        public String toString() {
            return String.format("%s[shielding: %s]", super.toString(), this.stream);
        }
    }

    private Close() {
    }

    /**
     * Shields the stream from being closed.
     *
     * @param stream
     *            to shield
     * @return A new stream which forwards calls to the input stream, except for the
     *         {@link AutoCloseable#close()} call
     */
    public static OutputStream shield(final OutputStream stream) {
        return new ShieldedOutputStream(stream);
    }

    /**
     * Shields the stream from being closed.
     *
     * @param stream
     *            to shield
     * @return A new stream which forwards calls to the input stream, except for the
     *         {@link AutoCloseable#close()} call
     */
    public static InputStream shield(final InputStream stream) {
        return new ShieldedInputStream(stream);
    }
}
