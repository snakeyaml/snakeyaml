/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.pyyaml;

public class AnInstance {
    private Object foo;
    private Object bar;

    public AnInstance() {
    }

    public AnInstance(Object foo, Object bar) {
        this.foo = foo;
        this.bar = bar;
    }

    public Object getFoo() {
        return foo;
    }

    public void setFoo(Object foo) {
        this.foo = foo;
    }

    public Object getBar() {
        return bar;
    }

    public void setBar(Object bar) {
        this.bar = bar;
    }

}