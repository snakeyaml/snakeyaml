/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package examples;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Loader;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class CustomListExampleTest extends TestCase {
    @SuppressWarnings("unchecked")
    public void testList() throws IOException {
        Loader loader = new Loader(new CustomConstructor());
        Yaml yaml = new Yaml(loader);
        List<Integer> data = (List<Integer>) yaml.load("[1, 2, 3]");
        assertTrue(data instanceof ArrayList);
    }

    class CustomConstructor extends Constructor {
        @Override
        protected List<Object> createDefaultList(int initSize) {
            return new ArrayList<Object>(initSize);
        }
    }
}
