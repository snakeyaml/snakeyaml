/**
 * Copyright (c) 2008, http://www.snakeyaml.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.yaml.snakeyaml;

import java.sql.Date;
import java.sql.Timestamp;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions.FlowStyle;

public class JavaBeanTimeStampTest extends TestCase {
    public void testLoadDefaultJavaSqlTimestamp() {
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
        Yaml loader = new Yaml();
        JavaBeanWithSqlTimestamp javaBeanToLoad = loader.loadAs(dumpStr,
                JavaBeanWithSqlTimestamp.class);
        assertEquals(stamp, javaBeanToLoad.getTimestamp());
        assertEquals(date, javaBeanToLoad.getDate());
    }

    public void testLoadDefaultJavaSqlTimestampNoGlobalTag() {
        JavaBeanWithSqlTimestamp javaBeanToDump = new JavaBeanWithSqlTimestamp();
        Timestamp stamp = new Timestamp(1000000000000L);
        javaBeanToDump.setTimestamp(stamp);
        Date date = new Date(1001376000000L);
        javaBeanToDump.setDate(date);
        Yaml yaml = new Yaml();
        String dumpStr = yaml.dumpAsMap(javaBeanToDump);
        assertEquals("date: 2001-09-25T00:00:00Z\ntimestamp: 2001-09-09T01:46:40Z\n", dumpStr);
        Yaml loader = new Yaml();
        JavaBeanWithSqlTimestamp javaBeanToLoad = loader.loadAs(dumpStr,
                JavaBeanWithSqlTimestamp.class);
        assertEquals(stamp, javaBeanToLoad.getTimestamp());
        assertEquals(date, javaBeanToLoad.getDate());
    }
}
