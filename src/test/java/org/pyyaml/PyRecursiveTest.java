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
import org.yaml.snakeyaml.constructor.ConstructorException;

public class PyRecursiveTest extends TestCase {

    @SuppressWarnings("unchecked")
    public void testDict() {
        Map<AnInstance, AnInstance> value = new HashMap<AnInstance, AnInstance>();
        AnInstance instance = new AnInstance(value, value);
        value.put(instance, instance);
        Yaml yaml = new Yaml();
        try {
            String output1 = yaml.dump(value);
            Map<AnInstance, AnInstance> value2 = (Map<AnInstance, AnInstance>) yaml.load(output1);
            assertEquals(value, value2);
        } catch (ConstructorException e) {
            // TODO recursive objects are not allowed
        }
    }

    @SuppressWarnings("unchecked")
    public void testList() {
        List value = new ArrayList();
        value.add(value);
        Yaml yaml = new Yaml();
        try {
            String output1 = yaml.dump(value);
            List value2 = (List) yaml.load(output1);
            assertEquals(value, value2);
        } catch (ConstructorException e) {
            // TODO recursive objects are not allowed
        }
    }

    @SuppressWarnings("unchecked")
    public void testSet() {
        Set value = new HashSet();
        value.add(new AnInstance(value, value));
        Yaml yaml = new Yaml();
        try {
            String output1 = yaml.dump(value);
            List value2 = (List) yaml.load(output1);
            assertEquals(value, value2);
        } catch (ConstructorException e) {
            // TODO recursive objects are not allowed
        }
    }
}
