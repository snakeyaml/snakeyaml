/**
 * Copyright (c) 2008-2010, http://code.google.com/p/snakeyaml/
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

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.yaml.snakeyaml.Dumper;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class JodaTimeExampleTest extends TestCase {
    private static final long timestamp = 1000000000000L;

    public void testDump() throws IOException {
        DateTime time = new DateTime(timestamp, DateTimeZone.UTC);
        Yaml yaml = new Yaml(new Dumper(new JodaTimeRepresenter(), new DumperOptions()));
        String joda = yaml.dump(time);
        String date = new Yaml().dump(new Date(timestamp));
        assertEquals(date, joda);
    }

    public void testLoad() throws IOException {
        Yaml yaml = new Yaml(new JodaTimeContructor());
        DateTime time = (DateTime) yaml.load("2001-09-09T01:46:40Z");
        assertEquals(new DateTime(timestamp, DateTimeZone.UTC), time);
    }
}
