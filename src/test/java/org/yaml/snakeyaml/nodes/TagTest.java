/**
 * Copyright (c) 2008, http://www.snakeyaml.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.yaml.snakeyaml.nodes;

import java.net.URI;
import java.net.URISyntaxException;

import junit.framework.TestCase;

public class TagTest extends TestCase {

    public void testCreate() {
        try {
            new Tag((String) null);
            fail();
        } catch (Exception e) {
            assertEquals("Tag must be provided.", e.getMessage());
        }
        try {
            new Tag("");
            fail();
        } catch (Exception e) {
            assertEquals("Tag must not be empty.", e.getMessage());
        }
        try {
            new Tag("!Dice ");
            fail();
        } catch (Exception e) {
            assertEquals("Tag must not contain leading or trailing spaces.", e.getMessage());
        }
        Tag tag = new Tag(TagTest.class);
        assertEquals(Tag.PREFIX + "org.yaml.snakeyaml.nodes.TagTest", tag.getValue());
    }

    public void testCreate2() {
        try {
            new Tag((Class<?>) null);
            fail();
        } catch (Exception e) {
            assertEquals("Class for tag must be provided.", e.getMessage());
        }
    }

    public void testGetClassName() {
        Tag tag = new Tag(Tag.PREFIX + "org.yaml.snakeyaml.nodes.TagTest");
        assertEquals("org.yaml.snakeyaml.nodes.TagTest", tag.getClassName());
    }

    public void testGetClassNameError() {
        try {
            Tag tag = new Tag("!TagTest");
            tag.getClassName();
            fail("Class name is only available for global tag");
        } catch (Exception e) {
            assertEquals("Invalid tag: !TagTest", e.getMessage());
        }
    }

    public void testLength() {
        String t = Tag.PREFIX + "org.yaml.snakeyaml.nodes.TagTest";
        Tag tag = new Tag(t);
        assertEquals(t.length(), tag.getLength());
    }

    public void testToString() {
        Tag tag = new Tag("!car");
        assertEquals("!car", tag.toString());
    }

    public void testUri1() {
        Tag tag = new Tag("!Académico");
        assertEquals("!Acad%C3%A9mico", tag.toString());
    }

    public void testUri2() {
        Tag tag = new Tag("!ruby/object:Test::Module::Sub2");
        assertEquals("!ruby/object:Test::Module::Sub2", tag.getValue());
    }

    public void testCompare() {
        Tag tag = new Tag("!car");
        assertEquals(0, tag.compareTo(new Tag("!car")));
    }

    public void testEqualsObject() {
        Tag tag = new Tag("!car");
        assertEquals(tag, tag);
        assertEquals(tag, new Tag("!car"));
        assertFalse(tag.equals(new Tag("!!str")));
        assertFalse(tag.equals(null));
        assertFalse(tag.equals(25));
    }
}
