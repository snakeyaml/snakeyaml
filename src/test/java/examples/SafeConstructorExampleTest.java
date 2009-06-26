/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package examples;

import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Loader;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

public class SafeConstructorExampleTest extends TestCase {
    @SuppressWarnings("unchecked")
    public void testConstruct() throws IOException {
        String doc = "- 5\n- Person\n- true";
        Loader loader = new Loader(new SafeConstructor());
        Yaml yaml = new Yaml(loader);
        List<Object> list = (List<Object>) yaml.load(doc);
        assertEquals(3, list.size());
        assertEquals(new Integer(5), list.get(0));
        assertEquals("Person", list.get(1));
        assertEquals(Boolean.TRUE, list.get(2));
    }

    public void testSafeConstruct() throws IOException {
        String doc = "- 5\n- !org.yaml.snakeyaml.constructor.Person\n  firstName: Andrey\n  age: 99\n- true";
        Loader loader = new Loader(new SafeConstructor());
        Yaml yaml = new Yaml(loader);
        try {
            yaml.load(doc);
            fail("Custom Java classes should not be created.");
        } catch (Exception e) {
            assertEquals(
                    "null; could not determine a constructor for the tag !org.yaml.snakeyaml.constructor.Person",
                    e.getMessage());
        }
    }
}
