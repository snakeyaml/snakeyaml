/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.recursive.generics;

import java.util.LinkedHashSet;
import java.util.Set;

public class HumanGen extends AbstractHumanGen<Set<HumanGen>, HumanGen>{
    public HumanGen() {
        children = new LinkedHashSet<HumanGen>();
    }
}
