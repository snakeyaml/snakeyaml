/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.pyyaml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;

public class PyRecursiveTest extends TestCase {

    @SuppressWarnings("unchecked")
    public void testDict() {
        Map<AnInstance, AnInstance> value = new HashMap<AnInstance, AnInstance>();
        AnInstance instance = new AnInstance(value, value);
        value.put(instance, instance);
        Yaml yaml = new Yaml();
        String output1 = yaml.dump(value);
        Map<AnInstance, AnInstance> value2 = (Map<AnInstance, AnInstance>) yaml.load(output1);
        assertEquals(value.size(), value2.size());
        for (AnInstance tmpInstance : value2.values()) {
            assertSame(tmpInstance.getBar(), tmpInstance.getFoo());
            assertSame(tmpInstance.getBar(), value2);
            assertSame(tmpInstance, value2.get(tmpInstance));
        }
    }

    @SuppressWarnings("unchecked")
    public void testList() {
        List value = new ArrayList();
        value.add(value);
        value.add("test");
        value.add(new Integer(1));

        Yaml yaml = new Yaml();
        String output1 = yaml.dump(value);
        List value2 = (List) yaml.load(output1);
        assertEquals(3, value2.size());
        assertEquals(value.size(), value2.size());
        assertSame(value2, value2.get(0));
        // we expect self-reference as 1st element of the list
        // let's remove self-reference and check other "simple" members of the
        // list. otherwise assertEquals will lead us to StackOverflow
        value.remove(0);
        value2.remove(0);
        assertEquals(value, value2);
    }

    @SuppressWarnings("unchecked")
    public void testSet() {
        Set value = new HashSet();
        value.add(new AnInstance(value, value));
        Yaml yaml = new Yaml();
        String output1 = yaml.dump(value);
        Set<AnInstance> value2 = (Set<AnInstance>) yaml.load(output1);

        assertEquals(value.size(), value2.size());
        for (AnInstance tmpInstance : value2) {
            assertSame(tmpInstance.getBar(), tmpInstance.getFoo());
            assertSame(tmpInstance.getBar(), value2);
        }
    }

    // TODO write same more complex tests for recursions. maybe recursion in
    // Arrays, bean properties...
}
