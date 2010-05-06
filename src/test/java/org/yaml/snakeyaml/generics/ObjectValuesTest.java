package org.yaml.snakeyaml.generics;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

import junit.framework.TestCase;

import org.yaml.snakeyaml.JavaBeanDumper;
import org.yaml.snakeyaml.JavaBeanLoader;

public class ObjectValuesTest extends TestCase {

    public void testObjectValues() {
        ObjectValues ov = new ObjectValues();
        Integer obj = new Integer(131313);
        ov.setObject(obj);
        final Map<String, Map<Integer, Object>> prop2values = new HashMap<String, Map<Integer, Object>>();

        final String[] props = { "prop1", "prop2", "prop3" };
        for (String name : props) {
            Map<Integer, Object> values = new HashMap<Integer, Object>();
            prop2values.put(name, values);
            for (int i = 0; i < 3; i++) {
                values.put(i, name + i);
            }
        }
        
        ov.setValues(prop2values);
        ov.setPossible(props);
        
        JavaBeanDumper dumper = new JavaBeanDumper();
        String dumpedStr = dumper.dump(ov);

        JavaBeanLoader<ObjectValues> loader = new JavaBeanLoader<ObjectValues>(ObjectValues.class);
        ObjectValues ov2 = loader.load(dumpedStr);

        assertEquals(ov.getObject(), ov2.getObject());
        assertEquals(ov.getValues(), ov2.getValues());
        assertArrayEquals(ov.getPossible(), ov2.getPossible());
        ov.getPossible()[0] = ov2.getPossible()[0];
    }

    public void testObjectValuesWithParam() {
        ObjectValuesWithParam<String, Integer> ov = new ObjectValuesWithParam<String,Integer>();
        Integer obj = new Integer(131313);
        ov.setObject(obj);
        final Map<String, Map<Integer, Object>> prop2values = new HashMap<String, Map<Integer, Object>>();
        
        final String[] props = { "prop1", "prop2", "prop3" };
        for (String name : props) {
            Map<Integer, Object> values = new HashMap<Integer, Object>();
            prop2values.put(name, values);
            for (int i = 0; i < 3; i++) {
                values.put(i, name + i);
            }
        }
        
        ov.setValues(prop2values);
        ov.setPossible(props);
        
        JavaBeanDumper dumper = new JavaBeanDumper();
        String dumpedStr = dumper.dump(ov);

        JavaBeanLoader<ObjectValuesWithParam<String,Integer>> loader = new JavaBeanLoader<ObjectValuesWithParam<String, Integer>>(new ObjectValuesWithParam<String, Integer>().getClass());
        ObjectValuesWithParam<String, Integer> ov2 = loader.load(dumpedStr);
        
        assertEquals(ov.getObject(), ov2.getObject());
        assertEquals(ov.getValues(), ov2.getValues());
        assertArrayEquals(ov.getPossible(), ov2.getPossible());
//        ov.getPossible()[0] = ov2.getPossible()[0]; //TODO: This actually FAILS. Use of GenericArrays is ..... no words.
    }

}
