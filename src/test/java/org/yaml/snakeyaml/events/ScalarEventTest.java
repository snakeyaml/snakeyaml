/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.events;

import junit.framework.TestCase;

public class ScalarEventTest extends TestCase {

    public void testToString() {
        ScalarEvent event = new ScalarEvent("a2", "str", new ImplicitTuple(true, true), "text",
                null, null, '"');
        assertEquals(
                "<org.yaml.snakeyaml.events.ScalarEvent(anchor=a2, tag=str, implicit=[true, true], value=text)>",
                event.toString());
    }

    public void testNotEqual() {
        ScalarEvent event = new ScalarEvent("a2", "str", new ImplicitTuple(true, true), "text",
                null, null, '"');
        assertFalse(event.equals(event.toString()));
    }
}
