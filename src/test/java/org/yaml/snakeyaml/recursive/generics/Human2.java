/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.recursive.generics;

import java.util.HashMap;
import java.util.Map;

public class Human2 extends AbstractHuman<Map<Human2, String>, Human2>{

    public Human2() {
        children = new HashMap<Human2, String>();
    }
}
