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
package examples.jodatime;

import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Construct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.Tag;

public class JodaTimeFlowStylesTest extends TestCase {
    private static final long timestamp = 1000000000000L;

    /**
     * @see http://code.google.com/p/snakeyaml/issues/detail?id=128
     */
    public void testLoadBeanWithBlockFlow() {
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
                    assertTrue(scalar.getImplicit().canOmitTagInPlainScalar());
                    assertFalse(scalar.getImplicit().canOmitTagInNonPlainScalar());
                }
            }
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
        assertEquals("!!examples.jodatime.MyBean\ndate: 2001-09-09T01:46:40Z\nid: id123\n", doc1);
        /*
         * provided JodaTimeContructor will be ignored because 'date' is a
         * JavaBean property and its class gets more priority then the implicit
         * '!!timestamp' tag.
         */
        Yaml loader = new Yaml(new JodaTimeImplicitContructor());
        try {
            loader.load(doc1);
        } catch (Exception e) {
            assertTrue(
                    "The error must indicate that JodaTime cannot be created from the scalar value.",
                    e.getMessage()
                            .contains(
                                    "No String constructor found. Exception=org.joda.time.DateTime.<init>(java.lang.String)"));
        }
        // we have to provide a special way to create JodaTime instances from
        // scalars
        Yaml loader2 = new Yaml(new JodaPropertyConstructor());
        MyBean parsed = (MyBean) loader2.load(doc1);
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
    public void testLoadBeanWithAutoFlow() {
        MyBean bean = new MyBean();
        bean.setId("id123");
        DateTime etalon = new DateTime(timestamp, DateTimeZone.UTC);
        bean.setDate(etalon);
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(FlowStyle.AUTO);
        Yaml dumper = new Yaml(new JodaTimeRepresenter(), options);
        String doc = dumper.dump(bean);
        // System.out.println(doc);
        assertEquals(
                "!!examples.jodatime.MyBean {date: !!timestamp '2001-09-09T01:46:40Z', id: id123}\n",
                doc);
        Yaml loader = new Yaml(new JodaTimeImplicitContructor());
        MyBean parsed = (MyBean) loader.load(doc);
        assertEquals(etalon, parsed.getDate());
    }

    private class JodaPropertyConstructor extends Constructor {
        public JodaPropertyConstructor() {
            yamlClassConstructors.put(NodeId.scalar, new TimeStampConstruct());
        }

        class TimeStampConstruct extends Constructor.ConstructScalar {
            @Override
            public Object construct(Node nnode) {
                if (nnode.getTag().equals(new Tag("tag:yaml.org,2002:timestamp"))) {
                    Construct dateConstructor = yamlConstructors.get(Tag.TIMESTAMP);
                    Date date = (Date) dateConstructor.construct(nnode);
                    return new DateTime(date, DateTimeZone.UTC);
                } else {
                    return super.construct(nnode);
                }
            }

        }
    }
}
