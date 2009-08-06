/**
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.nodes;

import junit.framework.TestCase;

public class TagsTest extends TestCase {

    /**
     * Dummy test for Cobertura report
     */
    public void testToString() {
        assertNotNull(new Tags());
        assertEquals("tag:yaml.org,2002:map", Tags.MAP);
    }

    public void testGetGlobalTagForClass() {
        assertEquals("tag:yaml.org,2002:java.lang.String", Tags.getGlobalTagForClass(String.class));
        assertEquals("tag:yaml.org,2002:org.yaml.snakeyaml.nodes.TagsTest", Tags
                .getGlobalTagForClass(TagsTest.class));
    }
}
