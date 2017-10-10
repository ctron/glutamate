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
import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;

/**
 * Help with closing streams.
 */
public final class Close {

    private static final class ShieldedOutputStream extends OutputStream {

        private final OutputStream stream;

        private ShieldedOutputStream(@NonNull final OutputStream stream) {
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

        private ShieldedInputStream(@NonNull final InputStream stream) {
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

    private static final class ShieldedWriter extends Writer {

        private final Writer writer;

        public ShieldedWriter(final Writer writer) {
            this.writer = writer;
        }

        @Override
        public void write(final int c) throws IOException {
            this.writer.write(c);
        }

        @Override
        public void write(final char[] cbuf) throws IOException {
            this.writer.write(cbuf);
        }

        @Override
        public void write(final char[] cbuf, final int off, final int len) throws IOException {
            this.writer.write(cbuf, off, len);
        }

        @Override
        public void write(final String str) throws IOException {
            this.writer.write(str);
        }

        @Override
        public void write(final String str, final int off, final int len) throws IOException {
            this.writer.write(str, off, len);
        }

        @Override
        public Writer append(final CharSequence csq) throws IOException {
            return this.writer.append(csq);
        }

        @Override
        public Writer append(final CharSequence csq, final int start, final int end) throws IOException {
            return this.writer.append(csq, start, end);
        }

        @Override
        public Writer append(final char c) throws IOException {
            return this.writer.append(c);
        }

        @Override
        public void flush() throws IOException {
            this.writer.flush();
        }

        @Override
        public void close() throws IOException {
        }

        @Override
        public String toString() {
            return String.format("%s[shielding: %s]", super.toString(), this.writer);
        }

    }

    private static final class ShieldedReader extends Reader {

        private final Reader reader;

        public ShieldedReader(final Reader reader) {
            this.reader = reader;
        }

        @Override
        public int read(final CharBuffer target) throws IOException {
            return this.reader.read(target);
        }

        @Override
        public int read() throws IOException {
            return this.reader.read();
        }

        @Override
        public int read(final char[] cbuf) throws IOException {
            return this.reader.read(cbuf);
        }

        @Override
        public int read(final char[] cbuf, final int off, final int len) throws IOException {
            return this.reader.read(cbuf, off, len);
        }

        @Override
        public long skip(final long n) throws IOException {
            return this.reader.skip(n);
        }

        @Override
        public boolean ready() throws IOException {
            return this.reader.ready();
        }

        @Override
        public boolean markSupported() {
            return this.reader.markSupported();
        }

        @Override
        public void mark(final int readAheadLimit) throws IOException {
            this.reader.mark(readAheadLimit);
        }

        @Override
        public void reset() throws IOException {
            this.reader.reset();
        }

        @Override
        public void close() throws IOException {
        }

        @Override
        public String toString() {
            return String.format("%s[shielding: %s]", super.toString(), this.reader);
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
    public static OutputStream shield(@NonNull final OutputStream stream) {
        Objects.requireNonNull(stream);
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
    public static InputStream shield(@NonNull final InputStream stream) {
        Objects.requireNonNull(stream);
        return new ShieldedInputStream(stream);
    }

    /**
     * Shields the writer from being closed.
     *
     * @param writer
     *            to shield
     * @return A new writer which forwards calls to the input writer, except for the
     *         {@link AutoCloseable#close()} call
     */
    public static Writer shield(@NonNull final Writer writer) {
        Objects.requireNonNull(writer);
        return new ShieldedWriter(writer);
    }

    /**
     * Shields the reader from being closed.
     *
     * @param reader
     *            to shield
     * @return A new reader which forwards calls to the input reader, except for the
     *         {@link AutoCloseable#close()} call
     */
    public static Reader shield(@NonNull final Reader reader) {
        Objects.requireNonNull(reader);
        return new ShieldedReader(reader);
    }
}
