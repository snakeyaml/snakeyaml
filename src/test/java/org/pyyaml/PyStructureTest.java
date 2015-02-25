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
package org.pyyaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.composer.Composer;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.events.AliasEvent;
import org.yaml.snakeyaml.events.CollectionStartEvent;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.parser.ParserImpl;
import org.yaml.snakeyaml.reader.StreamReader;
import org.yaml.snakeyaml.reader.UnicodeReader;
import org.yaml.snakeyaml.resolver.Resolver;

/**
 * @see imported from PyYAML
 */
public class PyStructureTest extends PyImportTest {

    private void compareEvents(List<Event> events1, List<Event> events2, boolean full) {
        assertEquals(events1.size(), events2.size());
        Iterator<Event> iter1 = events1.iterator();
        Iterator<Event> iter2 = events2.iterator();
        while (iter1.hasNext()) {
            Event event1 = iter1.next();
            Event event2 = iter2.next();
            assertEquals(event1.getClass(), event2.getClass());
            if (event1 instanceof AliasEvent && full) {
                assertEquals(((AliasEvent) event1).getAnchor(), ((AliasEvent) event2).getAnchor());
            }
            if (event1 instanceof CollectionStartEvent) {
                String tag1 = ((CollectionStartEvent) event1).getTag();
                String tag2 = ((CollectionStartEvent) event1).getTag();
                if (tag1 != null && !"!".equals(tag1) && tag2 != null && !"!".equals(tag1)) {
                    assertEquals(tag1, tag2);
                }
            }
            if (event1 instanceof ScalarEvent) {
                ScalarEvent scalar1 = (ScalarEvent) event1;
                ScalarEvent scalar2 = (ScalarEvent) event2;
                if (scalar1.getImplicit().bothFalse() && scalar2.getImplicit().bothFalse()) {
                    assertEquals(scalar1.getTag(), scalar2.getTag());
                }
                assertEquals(scalar1.getValue(), scalar2.getValue());
            }
        }
    }

    public void testParser() {
        File[] files = getStreamsByExtension(".data", true);
        assertTrue("No test files found.", files.length > 0);
        for (File file : files) {
            if (!file.getName().contains("scan-line-b")) {
                continue;
            }
            try {
                InputStream input = new FileInputStream(file);
                List<Event> events1 = parse(input);
                input.close();
                assertFalse(events1.isEmpty());
                int index = file.getAbsolutePath().lastIndexOf('.');
                String canonicalName = file.getAbsolutePath().substring(0, index) + ".canonical";
                File canonical = new File(canonicalName);
                List<Event> events2 = canonicalParse(new FileInputStream(canonical));
                assertFalse(events2.isEmpty());
                compareEvents(events1, events2, false);
            } catch (Exception e) {
                System.out.println("Failed File: " + file);
                // fail("Failed File: " + file + "; " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    public void testParserOnCanonical() {
        File[] canonicalFiles = getStreamsByExtension(".canonical", false);
        assertTrue("No test files found.", canonicalFiles.length > 0);
        for (File file : canonicalFiles) {
            try {
                InputStream input = new FileInputStream(file);
                List<Event> events1 = parse(input);
                input.close();
                assertFalse(events1.isEmpty());
                List<Event> events2 = canonicalParse(new FileInputStream(file));
                assertFalse(events2.isEmpty());
                compareEvents(events1, events2, true);
            } catch (Exception e) {
                System.out.println("Failed File: " + file);
                // fail("Failed File: " + file + "; " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    private void compareNodes(Node node1, Node node2) {
        assertEquals(node1.getClass(), node2.getClass());
        if (node1 instanceof ScalarNode) {
            ScalarNode scalar1 = (ScalarNode) node1;
            ScalarNode scalar2 = (ScalarNode) node2;
            assertEquals(scalar1.getTag(), scalar2.getTag());
            assertEquals(scalar1.getValue(), scalar2.getValue());
        } else {
            if (node1 instanceof SequenceNode) {
                SequenceNode seq1 = (SequenceNode) node1;
                SequenceNode seq2 = (SequenceNode) node2;
                assertEquals(seq1.getTag(), seq2.getTag());
                assertEquals(seq1.getValue().size(), seq2.getValue().size());
                Iterator<Node> iter2 = seq2.getValue().iterator();
                for (Node child1 : seq1.getValue()) {
                    Node child2 = iter2.next();
                    compareNodes(child1, child2);
                }
            } else {
                MappingNode seq1 = (MappingNode) node1;
                MappingNode seq2 = (MappingNode) node2;
                assertEquals(seq1.getTag(), seq2.getTag());
                assertEquals(seq1.getValue().size(), seq2.getValue().size());
                Iterator<NodeTuple> iter2 = seq2.getValue().iterator();
                for (NodeTuple child1 : seq1.getValue()) {
                    NodeTuple child2 = iter2.next();
                    compareNodes(child1.getKeyNode(), child2.getKeyNode());
                    compareNodes(child1.getValueNode(), child2.getValueNode());
                }
            }
        }
    }

    public void testComposer() {
        File[] files = getStreamsByExtension(".data", true);
        assertTrue("No test files found.", files.length > 0);
        for (File file : files) {
            try {
                InputStream input = new FileInputStream(file);
                List<Node> events1 = compose_all(input);
                input.close();
                int index = file.getAbsolutePath().lastIndexOf('.');
                String canonicalName = file.getAbsolutePath().substring(0, index) + ".canonical";
                File canonical = new File(canonicalName);
                InputStream input2 = new FileInputStream(canonical);
                List<Node> events2 = canonical_compose_all(input2);
                input2.close();
                assertEquals(events1.size(), events2.size());
                Iterator<Node> iter1 = events1.iterator();
                Iterator<Node> iter2 = events2.iterator();
                while (iter1.hasNext()) {
                    compareNodes(iter1.next(), iter2.next());
                }
            } catch (Exception e) {
                System.out.println("Failed File: " + file);
                // fail("Failed File: " + file + "; " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    private List<Node> compose_all(InputStream file) {
        Composer composer = new Composer(new ParserImpl(new StreamReader(new UnicodeReader(file))),
                new Resolver());
        List<Node> documents = new ArrayList<Node>();
        while (composer.checkNode()) {
            documents.add(composer.getNode());
        }
        return documents;
    }

    private List<Node> canonical_compose_all(InputStream file) {
        StreamReader reader = new StreamReader(new UnicodeReader(file));
        StringBuilder buffer = new StringBuilder();
        while (reader.peek() != '\0') {
            buffer.append(reader.peek());
            reader.forward();
        }
        CanonicalParser parser = new CanonicalParser(buffer.toString());
        Composer composer = new Composer(parser, new Resolver());
        List<Node> documents = new ArrayList<Node>();
        while (composer.checkNode()) {
            documents.add(composer.getNode());
        }
        return documents;
    }

    class CanonicalLoader extends Yaml {
        public CanonicalLoader() {
            super(new MyConstructor());
        }

        @Override
        public Iterable<Object> loadAll(Reader yaml) {
            StreamReader reader = new StreamReader(yaml);
            StringBuilder buffer = new StringBuilder();
            while (reader.peek() != '\0') {
                buffer.append(reader.peek());
                reader.forward();
            }
            CanonicalParser parser = new CanonicalParser(buffer.toString());
            Composer composer = new Composer(parser, resolver);
            this.constructor.setComposer(composer);
            Iterator<Object> result = new Iterator<Object>() {
                public boolean hasNext() {
                    return constructor.checkData();
                }

                public Object next() {
                    return constructor.getData();
                }

                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
            return new YamlIterable(result);
        }

        private class YamlIterable implements Iterable<Object> {
            private Iterator<Object> iterator;

            public YamlIterable(Iterator<Object> iterator) {
                this.iterator = iterator;
            }

            public Iterator<Object> iterator() {
                return iterator;
            }

        }

    }

    private class MyConstructor extends Constructor {
        public MyConstructor() {
            this.yamlConstructors.put(null, new ConstructUndefined());
        }

        private class ConstructUndefined extends AbstractConstruct {
            public Object construct(Node node) {
                return constructScalar((ScalarNode) node);
            }
        }
    }

    public void testConstructor() {
        File[] files = getStreamsByExtension(".data", true);
        assertTrue("No test files found.", files.length > 0);
        Yaml myYaml = new Yaml(new MyConstructor());
        Yaml canonicalYaml = new CanonicalLoader();
        for (File file : files) {
            try {
                InputStream input = new FileInputStream(file);
                Iterable<Object> documents1 = myYaml.loadAll(input);
                int index = file.getAbsolutePath().lastIndexOf('.');
                String canonicalName = file.getAbsolutePath().substring(0, index) + ".canonical";
                File canonical = new File(canonicalName);
                InputStream input2 = new FileInputStream(canonical);
                Iterable<Object> documents2 = canonicalYaml.loadAll(input2);
                input2.close();
                Iterator<Object> iter2 = documents2.iterator();
                for (Object object1 : documents1) {
                    Object object2 = iter2.next();
                    if (object2 != null) {
                        assertFalse(System.identityHashCode(object1) == System
                                .identityHashCode(object2));
                    }
                    assertEquals("" + object1, object1, object2);
                }
                input.close();
            } catch (Exception e) {
                System.out.println("Failed File: " + file);
                // fail("Failed File: " + file + "; " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }
}
