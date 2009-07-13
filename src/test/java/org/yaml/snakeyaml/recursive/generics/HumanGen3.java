/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.recursive.generics;

import java.util.LinkedList;
import java.util.List;

public class HumanGen3 extends AbstractHumanGen<List<HumanGen3>, HumanGen3>{

    public HumanGen3() {
        children = new LinkedList<HumanGen3>();
    }
}
