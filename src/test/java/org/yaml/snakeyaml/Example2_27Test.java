/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml;

import java.io.IOException;

import junit.framework.TestCase;

import org.yaml.snakeyaml.constructor.Constructor;

/**
 * Test Example 2.27 from the YAML specification
 * 
 * @author py4fun
 * @see http://yaml.org/spec/1.1/
 */
public class Example2_27Test extends TestCase {

    public void testExample_2_27() throws IOException {
        Loader loader = new Loader(new Constructor(Invoice.class));
        Yaml yaml = new Yaml(loader);
        Invoice invoice = (Invoice) yaml.load(Util
                .getLocalResource("specification/example2_27.yaml"));
        assertNotNull(invoice);
        Person billTo = invoice.billTo;
        assertEquals("Dumars", billTo.family);
        Dumper dumper = new Dumper(new DumperOptions());
        yaml = new Yaml(dumper);
        String output = yaml.dump(invoice);
        String etalon = Util.getLocalResource("specification/example2_27_dumped.yaml");
        assertEquals(etalon, output);
    }
}
