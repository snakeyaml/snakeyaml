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
package org.yaml.snakeyaml.types;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @see http://yaml.org/type/binary.html
 */
public class BinaryTagTest extends AbstractTest {
    String line1 = "R0lGODlhDAAMAIQAAP//9/X17unp5WZmZgAAAOfn515eXvPz7Y6OjuDg4J+fn5";
    String line2 = "OTk6enp56enmlpaWNjY6Ojo4SEhP/++f/++f/++f/++f/++f/++f/++f/++f/+";
    String line3 = "+f/++f/++f/++f/++f/++SH+Dk1hZGUgd2l0aCBHSU1QACwAAAAADAAMAAAFLC";
    String line4 = "AgjoEwnuNAFOhpEMTRiggcz4BNJHrv/zCFcLiwMWYNG84BwwEeECcgggoBADs=";
    String content = line1 + line2 + line3 + line4;

    public void testBinary() {
        byte[] binary = (byte[]) getMapValue("canonical: !!binary " + content, "canonical");
        assertEquals((byte) 'G', binary[0]);
        assertEquals((byte) 'I', binary[1]);
        assertEquals((byte) 'F', binary[2]);
        assertEquals((byte) '8', binary[3]);
        assertEquals((byte) '9', binary[4]);
    }

    public void testBinary2() {
        byte[] binary = (byte[]) load("!!binary \"MQ==\"");
        assertEquals(1, binary.length);
        assertEquals((byte) '1', binary[0]);
    }

    public void testBinaryTag() {
        byte[] binary = (byte[]) getMapValue("canonical: !<tag:yaml.org,2002:binary> " + content,
                "canonical");
        assertEquals((byte) 'G', binary[0]);
        assertEquals((byte) 'I', binary[1]);
        assertEquals((byte) 'F', binary[2]);
        assertEquals((byte) '8', binary[3]);
        assertEquals((byte) '9', binary[4]);
    }

    public void testBinaryOut() throws IOException {
        byte[] data = "GIF89\tbi\u0003\u0000nary\n\u001Fimage\n".getBytes("ISO-8859-1");
        Map<String, String> map = new HashMap<String, String>();
        String value = new String(data, "ISO-8859-1");
        map.put("canonical", value);
        String output = dump(map);
        assertEquals("canonical: !!binary |-\n  R0lGODkJYmkDAG5hcnkKH2ltYWdlCg==\n", output);
    }

    public void testByteArray() {
        byte[] data = { 8, 14, 15, 10, 126, 32, 65, 65, 65 };
        String output = dump(data);
        assertEquals("!!binary |-\n  CA4PCn4gQUFB\n", output);
        byte[] parsed = (byte[]) load(output);
        assertEquals(data.length, parsed.length);
        for (int i = 0; i < data.length; i++) {
            assertEquals(data[i], parsed[i]);
        }
    }
}
