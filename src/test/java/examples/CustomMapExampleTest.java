/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package examples;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Loader;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class CustomMapExampleTest extends TestCase {
    @SuppressWarnings("unchecked")
    public void testMap() throws IOException {
        Loader loader = new Loader(new CustomConstructor());
        Yaml yaml = new Yaml(loader);
        Map data = (Map) yaml.load("{2: '222', 1: '111', 3: '333'}");
        assertTrue(data instanceof TreeMap);
        Object[] keys = data.keySet().toArray();
        // must be sorted
        assertEquals(new Integer(1), keys[0]);
        assertEquals(new Integer(2), keys[1]);
        assertEquals(new Integer(3), keys[2]);
    }

    class CustomConstructor extends Constructor {
        @Override
        protected Map<Object, Object> createDefaultMap() {
            return new TreeMap<Object, Object>();
        }
    }
}
