package io.glutamate.util;

import static io.glutamate.util.Optionals.presentAndEqual;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit tests for {@link Optionals}
 */
public class OptionalsTest {

    /**
     * Test for equals
     */
    @Test
    public void test1() {
        assertTrue(presentAndEqual(of(1), 1));
        assertTrue(presentAndEqual(of(1L), 1L));
    }

    /**
     * Test for not equals
     */
    @Test
    public void test2() {
        assertFalse(presentAndEqual(of(0), 1));
        assertFalse(presentAndEqual(of(0L), 1L));
    }

    /**
     * Test for equals with non-present
     */
    @Test
    public void test3() {
        assertFalse(presentAndEqual(empty(), 1));
        assertFalse(presentAndEqual(empty(), 1L));
    }

    /**
     * Test for equals with both null
     */
    @Test
    public void test4() {
        assertTrue(presentAndEqual(empty(), null));
        assertTrue(presentAndEqual(empty(), null));
    }

    /**
     * Test for equals with present and null
     */
    @Test
    public void test5() {
        assertFalse(presentAndEqual(of(1), null));
        assertFalse(presentAndEqual(of(1L), null));
    }
}
