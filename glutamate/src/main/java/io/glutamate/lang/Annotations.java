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

import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.jdt.annotation.NonNull;

/**
 * Helper for working with {@link Annotation}s. *
 */
public final class Annotations {
    private Annotations() {
    }

    /**
     * The scan mode.
     */
    public static enum ScanMode {
        /**
         * Scan super-classes before interfaces.
         */
        DEPTH,
        /**
         * Scan interfaces before super-classes.
         */
        BREADTH;
    }

    /**
     * Scan for an annotation in the full inheritance of a class.
     *
     * @param annotationClazz
     *            The annotation to scan for
     * @param clazz
     *            The class to scan on
     * @param mode
     *            The scan mode
     * @param <A>
     *            The type of the annotation
     * @return The annotation, if found, never returns {@code null}, but may return
     *         {@link Optional#empty()}.
     */
    public static <A extends Annotation> @NonNull Optional<A> scanFor(
            @NonNull final Class<A> annotationClazz,
            @NonNull final Class<?> clazz,
            @NonNull final ScanMode mode) {

        Objects.requireNonNull(mode);

        if (mode == ScanMode.DEPTH) {
            return scanForDepthFirst(annotationClazz, clazz);
        } else {
            return scanForBreadthFirst(annotationClazz, clazz);
        }
    }

    /**
     * Scan for an annotation with {@link ScanMode#DEPTH}.
     *
     * @param annotationClazz
     *            The annotation to scan for
     * @param clazz
     *            The class to scan on
     * @param <A>
     *            The type of the annotation
     * @return The annotation, if found, never returns {@code null}, but may return
     *         {@link Optional#empty()}.
     *
     */
    public static <A extends Annotation> @NonNull Optional<A> scanForDepthFirst(
            @NonNull final Class<A> annotationClazz,
            @NonNull final Class<?> clazz) {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(annotationClazz);

        final A annotation = clazz.getAnnotation(annotationClazz);
        if (annotation != null) {
            return Optional.of(annotation);
        } else {

            final Class<?> superclazz = clazz.getSuperclass();
            if (superclazz != null) {
                final Optional<A> result = scanForDepthFirst(annotationClazz, superclazz);
                if (result.isPresent()) {
                    return result;
                }
            }

            for (final Class<?> iface : clazz.getInterfaces()) {
                if (iface == null) {
                    continue;
                }

                final Optional<A> result = scanForDepthFirst(annotationClazz, iface);
                if (result.isPresent()) {
                    return result;
                }
            }

        }

        return Optional.empty();
    }

    /**
     * Scan for an annotation with {@link ScanMode#BREADTH}.
     *
     * @param annotationClazz
     *            The annotation to scan for
     * @param clazz
     *            The class to scan on
     * @param <A>
     *            The type of the annotation
     * @return The annotation, if found, never returns {@code null}, but may return
     *         {@link Optional#empty()}.
     *
     */
    public static <A extends Annotation> @NonNull Optional<A> scanForBreadthFirst(
            @NonNull final Class<A> annotationClazz,
            @NonNull final Class<?> clazz) {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(annotationClazz);

        final A annotation = clazz.getAnnotation(annotationClazz);
        if (annotation != null) {
            return Optional.of(annotation);
        } else {
            for (final Class<?> iface : clazz.getInterfaces()) {
                if (iface == null) {
                    continue;
                }

                final Optional<A> result = scanForBreadthFirst(annotationClazz, iface);
                if (result.isPresent()) {
                    return result;
                }
            }

            final Class<?> superclazz = clazz.getSuperclass();
            if (superclazz != null) {
                return scanForBreadthFirst(annotationClazz, superclazz);
            }
        }

        return Optional.empty();
    }
}
