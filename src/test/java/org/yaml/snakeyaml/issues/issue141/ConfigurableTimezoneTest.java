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
package org.yaml.snakeyaml.issues.issue141;

import java.util.Date;
import java.util.TimeZone;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class ConfigurableTimezoneTest extends TestCase {

    public void testNoTimezone() {
        Yaml yaml = new Yaml();
        String output = yaml.dump(new Date());
        assertTrue(output, output.endsWith("Z\n"));
    }

    public void testTimezone() {
        DumperOptions options = new DumperOptions();
        options.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));
        Yaml yaml = new Yaml(options);
        Date date = new Date();
        String output = yaml.dump(date);
        // System.out.println(output);
        assertTrue(output, output.trim().endsWith("+1:00"));
        Date parsed = (Date) yaml.load(output);
        assertEquals(date, parsed);
    }
}
