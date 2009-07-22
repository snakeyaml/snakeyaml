/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.javabeans;

public class FrontDoor extends Door {
    private String keytype;

    public FrontDoor() {
        super();
    }

    public FrontDoor(String id, int height) {
        super(id, height);
    }

    public String getKeytype() {
        return keytype;
    }

    public void setKeytype(String keytype) {
        this.keytype = keytype;
    }

}
