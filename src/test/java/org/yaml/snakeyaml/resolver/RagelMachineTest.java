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
package org.yaml.snakeyaml.resolver;

import junit.framework.TestCase;

public class RagelMachineTest extends TestCase {
    private RagelMachine machine = new RagelMachine();

    public void testScan() {
        assertNull(machine.scan("abc"));
    }

    public void testNullPointerException() {
        try {
            machine.scan(null);
            fail("null must not be accepted.");
        } catch (NullPointerException e) {
            assertEquals("Scalar must be provided.", e.getMessage());
        }
    }

    public void testScanBoolean() {
        assertEquals("tag:yaml.org,2002:bool", machine.scan("true"));
        assertEquals("tag:yaml.org,2002:bool", machine.scan("True"));
        assertEquals("tag:yaml.org,2002:bool", machine.scan("TRUE"));
        assertEquals("tag:yaml.org,2002:bool", machine.scan("false"));
        assertEquals("tag:yaml.org,2002:bool", machine.scan("False"));
        assertEquals("tag:yaml.org,2002:bool", machine.scan("FALSE"));
        assertEquals("tag:yaml.org,2002:bool", machine.scan("on"));
        assertEquals("tag:yaml.org,2002:bool", machine.scan("ON"));
        assertEquals("tag:yaml.org,2002:bool", machine.scan("On"));
        assertEquals("tag:yaml.org,2002:bool", machine.scan("off"));
        assertEquals("tag:yaml.org,2002:bool", machine.scan("Off"));
        assertEquals("tag:yaml.org,2002:bool", machine.scan("OFF"));
        assertEquals("tag:yaml.org,2002:bool", machine.scan("on"));
        assertEquals("tag:yaml.org,2002:bool", machine.scan("ON"));
        assertEquals("tag:yaml.org,2002:bool", machine.scan("On"));
        assertEquals("tag:yaml.org,2002:bool", machine.scan("off"));
        assertEquals("tag:yaml.org,2002:bool", machine.scan("Off"));
        assertEquals("tag:yaml.org,2002:bool", machine.scan("OFF"));
    }

    public void testScanNull() {
        assertEquals("tag:yaml.org,2002:null", machine.scan("null"));
        assertEquals("tag:yaml.org,2002:null", machine.scan("Null"));
        assertEquals("tag:yaml.org,2002:null", machine.scan("NULL"));
        assertEquals("tag:yaml.org,2002:null", machine.scan("~"));
        assertEquals("tag:yaml.org,2002:null", machine.scan(" "));
        assertEquals("tag:yaml.org,2002:null", machine.scan(""));
    }

    public void testScanMerge() {
        assertEquals("tag:yaml.org,2002:merge", machine.scan("<<"));
    }

    public void testScanValue() {
        assertEquals("tag:yaml.org,2002:value", machine.scan("="));
    }

    public void testScanInt() {
        assertEquals("tag:yaml.org,2002:int", machine.scan("0"));
        assertEquals("tag:yaml.org,2002:int", machine.scan("1"));
        assertEquals("tag:yaml.org,2002:int", machine.scan("-0"));
        assertEquals("tag:yaml.org,2002:int", machine.scan("-9"));
        assertEquals("tag:yaml.org,2002:int", machine.scan("0b0011"));
        assertEquals("tag:yaml.org,2002:int", machine.scan("0x12ef"));
        assertEquals("tag:yaml.org,2002:int", machine.scan("0123"));
        assertEquals("tag:yaml.org,2002:int", machine.scan("1_000"));
        assertEquals("tag:yaml.org,2002:int", machine.scan("1_000_000"));
        assertEquals("tag:yaml.org,2002:int", machine.scan("+0"));
        assertEquals("tag:yaml.org,2002:int", machine.scan("+10"));
        assertEquals("tag:yaml.org,2002:int", machine.scan("1__000"));
        assertEquals("tag:yaml.org,2002:int", machine.scan("24:12:34"));
        assertEquals("tag:yaml.org,2002:int", machine.scan("240:12:34"));
    }

    public void testScanFloat() {
        assertEquals("tag:yaml.org,2002:float", machine.scan("1.0"));
        assertEquals("tag:yaml.org,2002:float", machine.scan("-0.0"));
        assertEquals("tag:yaml.org,2002:float", machine.scan("+2.2310"));
        assertEquals("tag:yaml.org,2002:float", machine.scan("1.0e+12"));
        assertEquals("tag:yaml.org,2002:float", machine.scan("1.345e-3"));
        assertEquals("tag:yaml.org,2002:float", machine.scan("190:20:30.15"));
        assertEquals("tag:yaml.org,2002:float", machine.scan("-.inf"));
        assertEquals("tag:yaml.org,2002:float", machine.scan("+.INF"));
        assertEquals("tag:yaml.org,2002:float", machine.scan(".Inf"));
        assertEquals("tag:yaml.org,2002:float", machine.scan(".nan"));
        assertEquals("tag:yaml.org,2002:float", machine.scan(".NaN"));
        assertEquals("tag:yaml.org,2002:float", machine.scan(".NAN"));
        assertEquals("tag:yaml.org,2002:float", machine.scan("1_000.5"));
        assertEquals("tag:yaml.org,2002:float", machine.scan("1.023_456"));
        assertEquals("tag:yaml.org,2002:float", machine.scan("-1_123.45"));
        assertEquals("tag:yaml.org,2002:float", machine.scan(".5"));
        assertEquals("tag:yaml.org,2002:float", machine.scan("1.E+1"));
        assertNull(machine.scan("0x1,1"), machine.scan("0x1,1"));
    }

    public void testScanTimestamp() {
        assertEquals("tag:yaml.org,2002:timestamp", machine.scan("2009-02-28"));
        assertEquals("tag:yaml.org,2002:timestamp", machine.scan("2001-12-15T02:59:43.1Z"));
        assertEquals("tag:yaml.org,2002:timestamp", machine.scan("2001-12-14t21:59:43.10-05:00"));
        assertEquals("tag:yaml.org,2002:timestamp", machine.scan("2001-12-14 21:59:43.10 -5"));
        assertEquals("tag:yaml.org,2002:timestamp", machine.scan("2001-12-15 2:59:43.10"));
    }
}
