package org.yaml.snakeyaml.constructor;

/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import junit.framework.TestCase;

import org.yaml.snakeyaml.composer.Composer;
import org.yaml.snakeyaml.parser.Parser;
import org.yaml.snakeyaml.parser.ParserImpl;
import org.yaml.snakeyaml.reader.Reader;
import org.yaml.snakeyaml.resolver.Resolver;

public class ConstructorMappingTest extends TestCase {

    @SuppressWarnings("unchecked")
    public void testGetDefaultMap() {
        String data = "{ one: 1, two: 2, three: 3 }";
        Map<Object, Object> map = (Map<Object, Object>) construct(new CustomConstructor(), data);
        assertNotNull(map);
        assertTrue(map.getClass().toString(), map instanceof TreeMap);
    }

    @SuppressWarnings("unchecked")
    public void testGetLinkedList() {
        String data = "{ one: 1, two: 2, three: 3 }";
        Map<Object, Object> map = (Map<Object, Object>) construct(data);
        assertNotNull(map);
        assertTrue(map.getClass().toString(), map instanceof LinkedHashMap);
    }

    private Object construct(String data) {
        return construct(new Constructor(), data);
    }

    private Object construct(Constructor constructor, String data) {
        Reader reader = new Reader(data);
        Parser parser = new ParserImpl(reader);
        Resolver resolver = new Resolver();
        Composer composer = new Composer(parser, resolver);
        constructor.setComposer(composer);
        return constructor.getSingleData();
    }

    class CustomConstructor extends Constructor {
        @Override
        protected Map<Object, Object> createDefaultMap() {
            return new TreeMap<Object, Object>();
        }
    }
}
