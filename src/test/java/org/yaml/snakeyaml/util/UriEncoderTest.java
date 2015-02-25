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
package org.yaml.snakeyaml.util;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;

import junit.framework.TestCase;

public class UriEncoderTest extends TestCase {

    public void testEncode() {
        assertEquals("Acad%C3%A9mico", UriEncoder.encode("Académico"));
        assertEquals("Check http://yaml.org/spec/1.1/#escaping%20in%20URI/", "[]",
                UriEncoder.encode("[]"));
    }

    public void testDecode() throws CharacterCodingException {
        ByteBuffer buff = ByteBuffer.allocate(10);
        buff.put((byte) 0x34);
        buff.put((byte) 0x35);
        buff.flip();
        assertEquals("45", UriEncoder.decode(buff));
    }

    public void testFailDecode() throws CharacterCodingException {
        ByteBuffer buff = ByteBuffer.allocate(10);
        buff.put((byte) 0x34);
        buff.put((byte) 0xC1);
        buff.flip();
        try {
            UriEncoder.decode(buff);
            fail("Invalid UTF-8 must not be accepted.");
        } catch (Exception e) {
            assertEquals("Input length = 1", e.getMessage());
        }
    }
}
