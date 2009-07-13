/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.recursive.generics;

import java.util.HashMap;
import java.util.Map;

public class HumanGen2 extends AbstractHumanGen<Map<HumanGen2, String>, HumanGen2>{

    public HumanGen2() {
        children = new HashMap<HumanGen2, String>();
    }
}
