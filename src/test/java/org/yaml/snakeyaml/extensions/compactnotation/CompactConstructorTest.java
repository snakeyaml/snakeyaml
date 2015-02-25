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
package org.yaml.snakeyaml.extensions.compactnotation;

import junit.framework.TestCase;

public class CompactConstructorTest extends TestCase {

    public void testNoCompactData() {
        CompactConstructor flow = new CompactConstructor();
        assertNull(flow.getCompactData("scalar"));
        assertNull(flow.getCompactData("123"));
        assertNull(flow.getCompactData("(name=frame,title=My Frame)"));
        assertNull(flow.getCompactData("JFrame name=frame,title=My Frame)"));
        assertNull(flow.getCompactData("JFrame name=frame,title=My Frame"));
        assertNull(flow.getCompactData("JFrame(name=frame,title=My Frame"));
        assertNull(flow.getCompactData("JFrame(name=frame,title=My Frame)b"));
        assertNull(flow.getCompactData("JFrame(name=frame,title=My Frame) "));
        assertNull(flow.getCompactData("JFrame(name=)"));
        assertNull(flow.getCompactData("JFrame(=name)"));
    }

    public void testGetCompactData1() {
        CompactConstructor flow = new CompactConstructor();
        CompactData data = flow.getCompactData("JFrame(name=frame)");
        assertNotNull(data);
        assertEquals("JFrame", data.getPrefix());
        assertEquals(1, data.getProperties().size());
        assertEquals("frame", data.getProperties().get("name"));
    }

    public void testGetCompactData2() {
        CompactConstructor flow = new CompactConstructor();
        CompactData data = flow.getCompactData("Frame(name=frame,title=My Frame)");
        assertNotNull(data);
        assertEquals("Frame", data.getPrefix());
        assertEquals(2, data.getProperties().size());
        assertEquals("frame", data.getProperties().get("name"));
        assertEquals("My Frame", data.getProperties().get("title"));

        assertNotNull(flow.getCompactData("JFrame ( name = frame , title = My Frame )"));
    }

    public void testGetCompactData3() {
        CompactConstructor flow = new CompactConstructor();
        CompactData data = flow
                .getCompactData("JFrame ( name = frame , title = My Frame, number= 123 )");
        assertNotNull(data);
        assertEquals("JFrame", data.getPrefix());
        assertEquals(3, data.getProperties().size());
        assertEquals("frame", data.getProperties().get("name"));
        assertEquals("My Frame", data.getProperties().get("title"));
        assertEquals("123", data.getProperties().get("number"));
    }

    public void testGetCompactData4() {
        CompactConstructor flow = new CompactConstructor();
        CompactData data = flow.getCompactData("JFrame(title)");
        assertNotNull(data);
        assertEquals("JFrame", data.getPrefix());
        assertEquals(0, data.getProperties().size());
        assertEquals(1, data.getArguments().size());
        assertEquals("title", data.getArguments().get(0));
    }

    public void testGetCompactData5() {
        CompactConstructor flow = new CompactConstructor();
        CompactData data = flow.getCompactData("JFrame(id123, title, name=foo, alignment=center)");
        assertNotNull(data);
        assertEquals("JFrame", data.getPrefix());
        assertEquals(2, data.getProperties().size());
        assertEquals(2, data.getArguments().size());
        assertEquals("id123", data.getArguments().get(0));
        assertEquals("title", data.getArguments().get(1));
    }

    public void testGetCompactData6() {
        CompactConstructor flow = new CompactConstructor();
        CompactData data = flow.getCompactData("JFrame()");
        assertNotNull(data);
        assertEquals("JFrame", data.getPrefix());
        assertEquals(0, data.getProperties().size());
        assertEquals(0, data.getArguments().size());
    }

    public void testGetCompactData7() {
        CompactConstructor flow = new CompactConstructor();
        CompactData data = flow.getCompactData("package.Container(name=parent, id=123)");
        assertNotNull(data);
        assertEquals("package.Container", data.getPrefix());
        assertEquals(2, data.getProperties().size());
        assertEquals(0, data.getArguments().size());
    }

    public void testCompactDataToString() {
        CompactData data = new CompactData("foo");
        assertEquals("CompactData: foo {}", data.toString());
    }
}
