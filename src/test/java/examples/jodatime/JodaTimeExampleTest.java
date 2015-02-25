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

import junit.framework.TestCase;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Construct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.Tag;

public class JodaTimeExampleTest extends TestCase {
    private static final long timestamp = 1000000000000L;

    public void testDump() {
        DateTime time = new DateTime(timestamp, DateTimeZone.UTC);
        Yaml yaml = new Yaml(new JodaTimeRepresenter());
        String joda = yaml.dump(time);
        String date = new Yaml().dump(new Date(timestamp));
        assertEquals(date, joda);
        assertEquals("2001-09-09T01:46:40Z\n", joda);
    }

    public void testLoad() {
        Yaml yaml = new Yaml(new JodaTimeImplicitContructor());
        DateTime time = (DateTime) yaml.load("2001-09-09T01:46:40Z");
        assertEquals(new DateTime(timestamp, DateTimeZone.UTC), time);
    }

    /**
     * test issue 109
     */
    public void test109() {
        Date someDate = new DateMidnight(9, 2, 21, DateTimeZone.forID("Europe/Amsterdam")).toDate();
        Yaml yaml = new Yaml();
        String timestamp = yaml.dump(someDate);
        assertEquals("0009-02-22T23:40:28Z\n", timestamp);
        // System.out.println(timestamp);
        Object o = yaml.load(timestamp);
        assertEquals(someDate, o);
    }

    class JodaPropertyConstructor extends Constructor {
        public JodaPropertyConstructor() {
            yamlClassConstructors.put(NodeId.scalar, new TimeStampConstruct());
        }

        class TimeStampConstruct extends Constructor.ConstructScalar {
            @Override
            public Object construct(Node nnode) {
                if (nnode.getTag().equals("tag:yaml.org,2002:timestamp")) {
                    Construct dateConstructor = yamlConstructors.get(Tag.TIMESTAMP);
                    Date date = (Date) dateConstructor.construct(nnode);
                    return new DateTime(date, DateTimeZone.UTC);
                } else {
                    return super.construct(nnode);
                }
            }
        }
    }

    /**
     * This class should be used if JodaTime may appear with a tag or as a
     * JavaBean property
     */
    public class JodaTimeConstructor extends Constructor {
        private final Construct javaDateConstruct;
        private final Construct jodaDateConstruct;

        public JodaTimeConstructor() {
            javaDateConstruct = new ConstructYamlTimestamp();
            jodaDateConstruct = new ConstructJodaTimestamp();
            // Whenever we see an explicit timestamp tag, make a Joda Date
            // instead
            yamlConstructors.put(Tag.TIMESTAMP, jodaDateConstruct);
            // See
            // We need this to work around implicit construction.
            yamlClassConstructors.put(NodeId.scalar, new TimeStampConstruct());
        }

        public class ConstructJodaTimestamp extends AbstractConstruct {
            public Object construct(Node node) {
                Date date = (Date) javaDateConstruct.construct(node);
                return new DateTime(date, DateTimeZone.UTC);
            }
        }

        class TimeStampConstruct extends Constructor.ConstructScalar {
            @Override
            public Object construct(Node nnode) {
                if (nnode.getTag().equals(Tag.TIMESTAMP)) {
                    return jodaDateConstruct.construct(nnode);
                } else {
                    return super.construct(nnode);
                }
            }
        }
    }
}
