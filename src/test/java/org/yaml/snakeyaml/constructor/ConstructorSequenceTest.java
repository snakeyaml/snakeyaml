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

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.composer.Composer;
import org.yaml.snakeyaml.parser.Parser;
import org.yaml.snakeyaml.parser.ParserImpl;
import org.yaml.snakeyaml.reader.StreamReader;
import org.yaml.snakeyaml.resolver.Resolver;

public class ConstructorSequenceTest extends TestCase {

    public void testGetList() {
        String data = "[ 1, 2, 3 ]";
        List<Object> list = construct(new CustomConstructor(), data);
        assertNotNull(list);
        assertTrue(list.getClass().toString(), list instanceof ArrayList<?>);
    }

    public void testGetArrayList() {
        String data = "[ 1, 2, 3 ]";
        List<Object> list = construct(data);
        assertNotNull(list);
        assertTrue(list.getClass().toString(), list instanceof ArrayList<?>);
    }

    public void testDumpList() {
        List<Integer> l = new ArrayList<Integer>(2);
        l.add(1);
        l.add(2);
        Yaml yaml = new Yaml();
        String result = yaml.dump(l);
        assertEquals("[1, 2]\n", result);
    }

    public void testDumpListSameIntegers() {
        List<Integer> l = new ArrayList<Integer>(2);
        l.add(1);
        l.add(1);
        Yaml yaml = new Yaml();
        String result = yaml.dump(l);
        assertEquals("[1, 1]\n", result);
    }

    private List<Object> construct(String data) {
        return construct(new Constructor(), data);
    }

    @SuppressWarnings("unchecked")
    private List<Object> construct(Constructor constructor, String data) {
        StreamReader reader = new StreamReader(data);
        Parser parser = new ParserImpl(reader);
        Resolver resolver = new Resolver();
        Composer composer = new Composer(parser, resolver);
        constructor.setComposer(composer);
        List<Object> result = (List<Object>) constructor.getSingleData(Object.class);
        return result;
    }

    class CustomConstructor extends Constructor {
        @Override
        protected List<Object> createDefaultList(int initSize) {
            return new ArrayList<Object>(initSize);
        }
    }
}
