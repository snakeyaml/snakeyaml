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
package org.yaml.snakeyaml.tokens;

import junit.framework.TestCase;

import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.tokens.Token.ID;

public class TagTokenTest extends TestCase {

    public void testGetArguments() {
        Mark mark = new Mark("test1", 0, 0, 0, "*The first line.\nThe last line.", 0);
        TagToken token = new TagToken(new TagTuple("!foo", "!bar"), mark, mark);
        assertEquals("value=[!foo, !bar]", token.getArguments());
    }

    public void testNoMarks() {
        Mark mark = new Mark("test1", 0, 0, 0, "*The first line.\nThe last line.", 0);
        try {
            new TagToken(new TagTuple("!foo", "!bar"), null, mark);
            fail("Token without start mark should not be accepted.");
        } catch (YAMLException e) {
            assertEquals("Token requires marks.", e.getMessage());
        }
        try {
            new TagToken(new TagTuple("!foo", "!bar"), mark, null);
            fail("Token without end mark should not be accepted.");
        } catch (YAMLException e) {
            assertEquals("Token requires marks.", e.getMessage());
        }
    }

    public void testNoTag() {
        try {
            Mark mark = new Mark("test1", 0, 0, 0, "*The first line.\nThe last line.", 0);
            new TagToken(new TagTuple("!foo", null), mark, mark);
            fail("Marks must be provided.");
        } catch (NullPointerException e) {
            assertEquals("Suffix must be provided.", e.getMessage());
        }
    }

    public void testGetTokenId() {
        Mark mark = new Mark("test1", 0, 0, 0, "*The first line.\nThe last line.", 0);
        TagToken token = new TagToken(new TagTuple("!foo", "!bar"), mark, mark);
        assertEquals(ID.Tag, token.getTokenId());
    }
}
