/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.generics;

public class Bird extends AbstractAnimal<Nest> {
    private Nest home;

    public Nest getHome() {
        return home;
    }

    public void setHome(Nest home) {
        this.home = home;
    }
}
