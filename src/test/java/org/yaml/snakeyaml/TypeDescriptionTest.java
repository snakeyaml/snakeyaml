package org.yaml.snakeyaml;

import junit.framework.TestCase;

import org.yaml.snakeyaml.constructor.ArrayTagsTest.CarWithArray;

public class TypeDescriptionTest extends TestCase {

    public void testSetTag() {
        TypeDescription descr = new TypeDescription(TypeDescriptionTest.class);
        descr.setTag("!bla");
        assertEquals("!bla", descr.getTag());
    }

    public void testToString() {
        TypeDescription carDescription = new TypeDescription(CarWithArray.class, "!car");
        assertEquals(
                "TypeDescription for class org.yaml.snakeyaml.constructor.ArrayTagsTest$CarWithArray (tag='!car')",
                carDescription.toString());
    }
}
