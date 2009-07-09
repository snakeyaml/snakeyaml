/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.recursive.generics;

import java.util.LinkedList;
import java.util.List;

public class Human3 extends AbstractHuman<List<Human3>, Human3>{

    public Human3() {
        children = new LinkedList<Human3>();
    }
}
