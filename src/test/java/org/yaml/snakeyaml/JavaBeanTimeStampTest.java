/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml;

import java.sql.Date;
import java.sql.Timestamp;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions.FlowStyle;

public class JavaBeanTimeStampTest extends TestCase {
    public void testLoadDefaultJavaSqlTimestamp() throws Exception {
        JavaBeanWithSqlTimestamp javaBeanToDump = new JavaBeanWithSqlTimestamp();
        Timestamp stamp = new Timestamp(1000000000000L);
        javaBeanToDump.setTimestamp(stamp);
        Date date = new Date(1001376000000L);
        javaBeanToDump.setDate(date);
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
        String dumpStr = yaml.dump(javaBeanToDump);
        assertEquals(
                "!!org.yaml.snakeyaml.JavaBeanWithSqlTimestamp\ndate: 2001-09-25T00:00:00Z\ntimestamp: 2001-09-09T01:46:40Z\n",
                dumpStr);
        JavaBeanWithSqlTimestamp javaBeanToLoad = JavaBeanParser.load(dumpStr,
                JavaBeanWithSqlTimestamp.class);
        assertEquals(stamp, javaBeanToLoad.getTimestamp());
        assertEquals(date, javaBeanToLoad.getDate());
    }
}
