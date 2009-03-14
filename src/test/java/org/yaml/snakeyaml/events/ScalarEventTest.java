/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.events;

import junit.framework.TestCase;

public class ScalarEventTest extends TestCase {

    public void testToString() {
        boolean[] implicit = new boolean[2];
        implicit[0] = true;
        implicit[1] = true;
        ScalarEvent event = new ScalarEvent("a2", "str", implicit, "text", null, null, '"');
        assertEquals(
                "<org.yaml.snakeyaml.events.ScalarEvent(anchor=a2, tag=str, implicit=[true, true], value=text)>",
                event.toString());
    }

    public void testNotEqual() {
        boolean[] implicit = new boolean[2];
        implicit[0] = true;
        implicit[1] = true;
        ScalarEvent event = new ScalarEvent("a2", "str", implicit, "text", null, null, '"');
        assertFalse(event.equals(event.toString()));
    }
}
