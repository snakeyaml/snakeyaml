/**
 * Copyright (c) 2008, SnakeYAML
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

public class TokenTest extends TestCase {
    public void testTokenID() {
        assertEquals("<alias>", Token.ID.Alias.toString());
        assertEquals("<anchor>", Token.ID.Anchor.toString());
        assertEquals("<block end>", Token.ID.BlockEnd.toString());
        assertEquals("-", Token.ID.BlockEntry.toString());
        assertEquals("<block mapping start>", Token.ID.BlockMappingStart.toString());
        assertEquals("<block sequence start>", Token.ID.BlockSequenceStart.toString());
        assertEquals("<directive>", Token.ID.Directive.toString());
        assertEquals("<document end>", Token.ID.DocumentEnd.toString());
        assertEquals("<document start>", Token.ID.DocumentStart.toString());
        assertEquals(",", Token.ID.FlowEntry.toString());
        assertEquals("}", Token.ID.FlowMappingEnd.toString());
        assertEquals("{", Token.ID.FlowMappingStart.toString());
        assertEquals("]", Token.ID.FlowSequenceEnd.toString());
        assertEquals("[", Token.ID.FlowSequenceStart.toString());
        assertEquals("?", Token.ID.Key.toString());
        assertEquals("<scalar>", Token.ID.Scalar.toString());
        assertEquals("<stream end>", Token.ID.StreamEnd.toString());
        assertEquals("<stream start>", Token.ID.StreamStart.toString());
        assertEquals("<tag>", Token.ID.Tag.toString());
        assertEquals(":", Token.ID.Value.toString());
        assertEquals("<whitespace>", Token.ID.Whitespace.toString());
        assertEquals("#", Token.ID.Comment.toString());
        assertEquals("<error>", Token.ID.Error.toString());
    }
}

