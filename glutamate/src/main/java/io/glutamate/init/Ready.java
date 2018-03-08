/*******************************************************************************
 * Copyright (c) 2017, 2018 Red Hat Inc and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Jens Reimann - initial API and implementation
 *******************************************************************************/
package io.glutamate.init;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.glutamate.annotations.Experimental;
import io.glutamate.lang.Exceptions;
import io.glutamate.lang.ThrowingRunnable;

/**
 * Help ensuring an instance is ready/started.
 * <p>
 * This class helps if sub-classes are expected to call an initial start method
 * when they are done initializing but don't want to add an explicit start
 * method.
 * <p>
 * Assume you have a class hierarchy like this, where the init method must be
 * called, but cannot be called in the constructor of A:
 *
 * <pre>
 * <code>
 * class A {
 *   public A () {
 *   }
 *   private void init () {
 *   }
 * }
 *
 * class B extends A {
 *   public B () {
 *     super();
 *   }
 * }
 * </code>
 * </pre>
 *
 * Ideally "init" would be a public method called "start" or "open" and be
 * called explicitly from outside the class instance. Alternatively it would be
 * possible to make "init" protected and call it in the constructor of B.
 * However that requires that B has the knowledge about this call flow and also
 * if a new layer C would be introduced that approach might fail.
 * <p>
 * This class helps solving this by introducing a kind of handle:
 *
 * <pre>
 * <code>
 * class A {
 *   protected A ( Ready ready ) {
 *     ready.whenReady(this::init);
 *     ready.ready();
 *   }
 *   private void init () {}
 * }
 *
 * class B extends A {
 *   public B ()  {
 *     this(Ready.first());
 *   }
 *   public B(Ready ready) {
 *     super(ready.forSuper());
 *     ready.whenReady(this::init2);
 *     ready.ready();
 *   }
 *   private void init2() {}
 * }
 * </code>
 * </pre>
 * <p>
 * The contract of Ready is:
 * <ul>
 * <li>When a constructor accepts a ready instance it has to call
 * {@link #ready()} when it is done initializing</li>
 * <li>When a constructor wants to pass a ready instance to its super class, it
 * must pass the result created by {@link #forSuper()}</li>
 * <li>{@link #whenReady(ThrowingRunnable)} must be called before
 * {@link #ready()}
 * </ul>
 *
 */
@NonNullByDefault
@Experimental
public final class Ready {

    private final List<ThrowingRunnable<? extends Exception>> actions = new LinkedList<>();
    private int counter;

    private Ready(final int counter) {
        this.counter = counter;
    }

    /**
     * Must be called once for each accepted runnable.
     */
    public void ready() {
        this.counter--;
        if (this.counter == 0) {
            run();
        }
    }

    private void run() {
        for (final ThrowingRunnable<? extends Exception> runnable : this.actions) {
            Exceptions.wrap(runnable);
        }
    }

    /**
     * Add a runnable which needs to be execute once the initialization is complete.
     *
     * @param runnable
     *            the runnable to execute
     */
    public void whenReady(final ThrowingRunnable<? extends Exception> runnable) {
        if (this.counter == 0) {
            throw new IllegalStateException();
        }

        if (runnable != null) {
            this.actions.add(runnable);
        }
    }

    /**
     * Create the first level instance.
     *
     * @return the first level instance, never returns {@code null}
     */
    public static Ready first() {
        return new Ready(1);
    }

    /**
     * Get the instance for the super class.
     *
     * @return the instance for the super class, never returns {@code null}
     */
    public Ready forSuper() {
        this.counter++;
        return this;
    }

}
