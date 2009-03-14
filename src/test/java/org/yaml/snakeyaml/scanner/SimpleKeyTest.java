/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.scanner;

import junit.framework.TestCase;

public class SimpleKeyTest extends TestCase {

    public void testToString() {
        SimpleKey key = new SimpleKey(1, false, 5, 3, 2, null);
        assertTrue(key.toString().contains("SimpleKey"));
    }
}
