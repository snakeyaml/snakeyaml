/**
 * Copyright (c) 2008-2011, http://www.snakeyaml.org
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

package examples.jodatime;

import java.io.IOException;
import java.util.Date;

import junit.framework.TestCase;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.yaml.snakeyaml.Yaml;

public class JodaTimeExampleTest extends TestCase {
    private static final long timestamp = 1000000000000L;

    public void testDump() throws IOException {
        DateTime time = new DateTime(timestamp, DateTimeZone.UTC);
        Yaml yaml = new Yaml(new JodaTimeRepresenter());
        String joda = yaml.dump(time);
        String date = new Yaml().dump(new Date(timestamp));
        assertEquals(date, joda);
    }

    public void testLoad() throws IOException {
        Yaml yaml = new Yaml(new JodaTimeContructor());
        DateTime time = (DateTime) yaml.load("2001-09-09T01:46:40Z");
        assertEquals(new DateTime(timestamp, DateTimeZone.UTC), time);
    }

    public void testLoadBean() throws IOException {
        MyBean bean = new MyBean();
        bean.setId("id123");
        DateTime etalon = new DateTime(timestamp, DateTimeZone.UTC);
        bean.setDate(etalon);
        Yaml dumper = new Yaml(new JodaTimeRepresenter());
        String doc = dumper.dump(bean);
        // System.out.println(doc);
        Yaml loader = new Yaml(new JodaTimeContructor());
        MyBean parsed = (MyBean) loader.load(doc);
        assertEquals(etalon, parsed.getDate());
    }

    /**
     * test issue 109
     */
    public void test109() throws IOException {
        Date someDate = new DateMidnight(9, 2, 21, DateTimeZone.forID("Europe/Amsterdam")).toDate();
        Yaml yaml = new Yaml();
        String timestamp = yaml.dump(someDate);
        assertEquals("0009-02-22T23:40:28Z\n", timestamp);
        // System.out.println(timestamp);
        Object o = yaml.load(timestamp);
        assertEquals(someDate, o);
    }
}
