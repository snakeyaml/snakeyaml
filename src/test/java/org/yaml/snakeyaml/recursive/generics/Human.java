/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.recursive.generics;

import java.util.LinkedHashSet;
import java.util.Set;

public class Human extends AbstractHuman<Set<Human>, Human>{
    public Human() {
        children = new LinkedHashSet<Human>();
    }
}
