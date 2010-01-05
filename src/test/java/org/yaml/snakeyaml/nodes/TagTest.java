/**
 * Copyright (c) 2008-2010 Andrey Somov
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

import junit.framework.TestCase;

public class TagTest extends TestCase {

    public void testCreate() {
        assertNull(Tag.createTag(null));
        Tag tag = new Tag(TagTest.class);
        assertEquals(Tag.PREFIX + "org.yaml.snakeyaml.nodes.TagTest", tag.getValue());
    }

    public void testGetClassName() {
        Tag tag = Tag.createTag(Tag.PREFIX + "org.yaml.snakeyaml.nodes.TagTest");
        assertEquals("org.yaml.snakeyaml.nodes.TagTest", tag.getClassName());
    }

    public void testLength() {
        String t = Tag.PREFIX + "org.yaml.snakeyaml.nodes.TagTest";
        Tag tag = Tag.createTag(t);
        assertEquals(t.length(), tag.getLength());
    }

    public void testToString() {
        Tag tag = Tag.createTag("!car");
        assertEquals("!car", tag.toString());
    }

    public void testEqualsObject() {
        Tag tag = Tag.createTag("!car");
        assertEquals(tag, tag);
        assertEquals(tag, "!car");
        assertEquals(tag, Tag.createTag("!car"));
        assertFalse(tag.equals(Tag.createTag("!!str")));
        assertFalse(tag.equals(null));
        assertFalse(tag.equals(25));
    }

}
