/**
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml;

import junit.framework.TestCase;

public class BinaryJavaBeanTest extends TestCase {
    public void testBeanTest() {
        BinaryBean bean = new BinaryBean();
        bean.setId(1);
        byte[] bytes = new byte[] { 1, 7, 9, 31, 65 };
        bean.setData(bytes);
        Yaml yaml = new Yaml();
        String output = yaml.dump(bean);
        String etalon = "!!org.yaml.snakeyaml.BinaryBean\ndata: !!binary |-\n  AQcJH0E=\nid: 1\n";
        assertEquals(etalon, output);
        // load
        BinaryBean bean2 = (BinaryBean) yaml.load(output);
        assertEquals(1, bean2.getId());
        assertEquals(new String(bytes), new String(bean2.getData()));
    }
}
