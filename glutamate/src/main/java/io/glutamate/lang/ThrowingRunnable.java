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

/**
 * A runnable throwing an exception.
 */
@FunctionalInterface
public interface ThrowingRunnable {
    /**
     * Run boy run
     * 
     * @throws Exception
     *             if anything goes wrong
     */
    public void run() throws Exception;
}
