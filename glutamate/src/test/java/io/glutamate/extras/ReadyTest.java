package io.glutamate.extras;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import io.glutamate.init.Ready;

public class ReadyTest {

    public static class A {

        protected List<String> started = new LinkedList<>();

        public A(final Ready ready) {
            ready.whenReady(this::start);

            ready.ready();
        }

        public void start() {
            this.started.add("A");
        }
    }

    public static class B1 extends A {
        public B1() {
            this(Ready.first());
        }

        public B1(final Ready ready) {
            super(ready.forSuper());
            ready.ready();
        }
    }

    public static class B2 extends A {
        public B2() {
            super(Ready.first());
        }
    }

    public static class B3 extends A {
        public B3(final Ready ready) {
            super(ready.forSuper());

            ready.whenReady(this::start2);

            ready.ready();
        }

        public void start2() {
            this.started.add("B3");
        }
    }

    public static class C extends B3 {
        public C() {
            super(Ready.first());
        }
    }

    @Test
    public void test1() {
        final B1 b = new B1();
        Assert.assertArrayEquals(new String[] { "A" }, b.started.toArray());
    }

    @Test
    public void test2() {
        final C c = new C();
        Assert.assertArrayEquals(new String[] { "A", "B3" }, c.started.toArray());
    }
}
