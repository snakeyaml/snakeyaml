package org.yaml.snakeyaml.constructor;

/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.composer.Composer;
import org.yaml.snakeyaml.parser.Parser;
import org.yaml.snakeyaml.parser.ParserImpl;
import org.yaml.snakeyaml.reader.Reader;
import org.yaml.snakeyaml.resolver.Resolver;

public class ConstructorSequenceTest extends TestCase {

    public void testGetList() {
        String data = "[ 1, 2, 3 ]";
        List<Object> list = construct(new CustomConstructor(), data);
        assertNotNull(list);
        assertTrue(list.getClass().toString(), list instanceof ArrayList);
    }

    public void testGetLinkedList() {
        String data = "[ 1, 2, 3 ]";
        List<Object> list = construct(data);
        assertNotNull(list);
        assertTrue(list.getClass().toString(), list instanceof LinkedList);
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
        Reader reader = new Reader(data);
        Parser parser = new ParserImpl(reader);
        Resolver resolver = new Resolver();
        Composer composer = new Composer(parser, resolver);
        constructor.setComposer(composer);
        List result = (List) constructor.getSingleData();
        return result;
    }

    class CustomConstructor extends Constructor {
        @Override
        protected List<Object> createDefaultList(int initSize) {
            return new ArrayList<Object>(initSize);
        }
    }
}
