/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.constructor;

public class Parent1 {
    private String id;
    private Child1 child;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Child1 getChild() {
        return child;
    }

    public void setChild(Child1 child) {
        this.child = child;
    }
}