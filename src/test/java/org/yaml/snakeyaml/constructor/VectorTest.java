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
package org.yaml.snakeyaml.constructor;

import java.util.Vector;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;

public class VectorTest extends TestCase {

    @SuppressWarnings("unchecked")
    public void testVector() throws ClassNotFoundException {
        // Data to serialise
        Vector<String> srcVector = new Vector<String>();
        srcVector.add("this");
        srcVector.add("is");
        srcVector.add("a");
        srcVector.add("test");
        // System.out.println("Source Vector: " + srcVector);
        Yaml yaml = new Yaml();
        String instance = yaml.dump(srcVector);
        //System.out.println("YAML String: " + instance);
        yaml = new Yaml(new Constructor("java.util.Vector"));
        // If I try to get a Vector I receive a class cast exception.
        Vector<String> vector = (Vector<String>) yaml.load(instance);
        // System.out.println("Vector: " + vector);
        assertEquals(4, vector.size());
        assertEquals("this", vector.firstElement());
        assertEquals("test", vector.lastElement());
    }
}
