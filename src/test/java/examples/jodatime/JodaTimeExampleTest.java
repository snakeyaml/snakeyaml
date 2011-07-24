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
import java.util.List;

import junit.framework.TestCase;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.nodes.Node;

public class JodaTimeExampleTest extends TestCase {
    private static final long timestamp = 1000000000000L;

    public void testDump() throws IOException {
        DateTime time = new DateTime(timestamp, DateTimeZone.UTC);
        Yaml yaml = new Yaml(new JodaTimeRepresenter());
        String joda = yaml.dump(time);
        String date = new Yaml().dump(new Date(timestamp));
        assertEquals(date, joda);
        assertEquals("2001-09-09T01:46:40Z\n", joda);
    }

    public void testLoad() throws IOException {
        Yaml yaml = new Yaml(new JodaTimeContructor());
        DateTime time = (DateTime) yaml.load("2001-09-09T01:46:40Z");
        assertEquals(new DateTime(timestamp, DateTimeZone.UTC), time);
    }

    /**
     * @see http://code.google.com/p/snakeyaml/issues/detail?id=128
     */
 // FIXME issue 128
    public void qqqtestLoadBeanWithBlockFlow() {
        MyBean bean = new MyBean();
        bean.setId("id123");
        DateTime etalon = new DateTime(timestamp, DateTimeZone.UTC);
        bean.setDate(etalon);
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(FlowStyle.BLOCK);
        Yaml dumper = new Yaml(new JodaTimeRepresenter(), options);
        // compare Nodes with flow style AUTO and flow style BLOCK
        Node node1 = dumper.represent(bean);
        DumperOptions options2 = new DumperOptions();
        options2.setDefaultFlowStyle(FlowStyle.AUTO);
        Yaml dumper2 = new Yaml(new JodaTimeRepresenter(), options2);
        Node node2 = dumper2.represent(bean);
        assertEquals(node2.toString(), node1.toString());
        // compare Events with flow style AUTO and flow style BLOCK
        List<Event> events1 = dumper.serialize(node1);
        List<Event> events2 = dumper2.serialize(node2);
        assertEquals(events2.size(), events1.size());
        int i = 0;
        for (Event etalonEvent : events2) {
            assertEquals(etalonEvent, events1.get(i++));
            if (etalonEvent instanceof ScalarEvent) {
                ScalarEvent scalar = (ScalarEvent) etalonEvent;
                if (scalar.getValue().equals("2001-09-09T01:46:40Z")) {
                    assertFalse("The tag cannot be omitted even in the plain scalar style.", scalar
                            .getImplicit().canOmitTagInPlainScalar());
                    assertFalse(scalar.getImplicit().canOmitTagInNonPlainScalar());
                }
            }
            System.out.println(etalonEvent);
        }
        // Nodes and Events are the same. Only emitter may influence the output.
        String doc1 = dumper.dump(bean);
        // System.out.println(doc1);
        /*
         * 'date' must be used only with the explicit '!!timestamp' tag.
         * Implicit tag will not work because 'date' is the JavaBean property
         * and in this case the empty constructor of the class will be used.
         * Since this constructor does not exist for JodaTime an exception will
         * be thrown.
         */
        assertEquals(
                "!!examples.jodatime.MyBean\ndate: !!timestamp 2001-09-09T01:46:40Z\nid: id123\n",
                doc1);
        Yaml loader = new Yaml(new JodaTimeContructor());
        MyBean parsed = (MyBean) loader.load(doc1);
        assertEquals(etalon, parsed.getDate());
    }

    /**
     * !!timestamp must be used, without it the implicit tag will be ignored
     * because 'date' is the JavaBean property.
     * 
     * Since the timestamp contains ':' character it cannot use plain scalar
     * style in the FLOW mapping style. Emitter suggests single quoted scalar
     * style and that is why the explicit '!!timestamp' is present in the YAML
     * document.
     * 
     * @see http://code.google.com/p/snakeyaml/issues/detail?id=128
     * 
     */
    public void testLoadBeanWithAutoFlow() throws IOException {
        MyBean bean = new MyBean();
        bean.setId("id123");
        DateTime etalon = new DateTime(timestamp, DateTimeZone.UTC);
        bean.setDate(etalon);
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(FlowStyle.AUTO);
        Yaml dumper = new Yaml(new JodaTimeRepresenter(), options);
        String doc = dumper.dump(bean);
        System.out.println(doc);
        assertEquals(
                "!!examples.jodatime.MyBean {date: !!timestamp '2001-09-09T01:46:40Z', id: id123}\n",
                doc);
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
