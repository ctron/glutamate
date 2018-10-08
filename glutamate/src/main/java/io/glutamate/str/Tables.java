/*******************************************************************************
 * Copyright (c) 2013, 2018 Jens Reimann and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Jens Reimann - initial API and implementation
 *     IBH SYSTEMS GmbH - add variant with PrintWriter
 *     Jens Reimann - migration to glutamate.io
 *******************************************************************************/
package io.glutamate.str;

import java.io.IOException;
import java.util.Formatter;
import java.util.List;
import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;

/**
 * Render simple tables.
 */
public final class Tables {
    private Tables() {
    }

    /**
     * Print out a table
     *
     * @param appendable
     *            the {@link Appendable} to append to
     * @param header
     *            the column headers
     * @param data
     *            the data, in rows and cells
     * @param gap
     *            gap between columns
     * @return returns the provided appendable
     */
    public static <T extends Appendable> T showTable(@NonNull final T appendable, final @NonNull List<String> header,
            final List<List<String>> data, int gap) throws IOException {

        Objects.requireNonNull(appendable);

        final String newline = System.lineSeparator();
        @SuppressWarnings("resource")
        final Formatter formatter = new Formatter(appendable);

        if (gap < 0) {
            gap = 0;
        }

        int max = header.size();
        for (final List<String> row : data) {
            max = Math.max(row.size(), max);
        }

        final int[] lens = new int[max];

        {
            int i = 0;
            for (final String cell : header) {
                if (cell != null) {
                    lens[i] = Math.max(lens[i], cell.length());
                }
                i++;
            }
        }

        if (data != null) {
            for (final List<String> row : data) {
                int i = 0;
                for (final String cell : row) {
                    if (cell != null) {
                        lens[i] = Math.max(lens[i], cell.length());
                    }
                    i++;
                }
            }
        }

        final String[] formats = new String[max];

        int totalLen = 0;
        for (int i = 0; i < formats.length; i++) {
            totalLen += lens[i] + gap;
            formats[i] = String.format("%%-%ds", lens[i] + gap);
        }

        {
            int i = 0;
            for (final String cell : header) {
                formatter.format(formats[i], cell);
                i++;
            }
            appendable.append(newline);
        }

        // header line
        for (int i = 0; i < totalLen; i++) {
            appendable.append('=');
        }
        appendable.append(newline);

        // data
        if (data != null) {
            for (final List<String> row : data) {
                int i = 0;
                for (final String cell : row) {
                    formatter.format(formats[i], cell == null ? "" : cell);
                    i++;
                }
                appendable.append(newline);
            }
        }

        return appendable;
    }
}
