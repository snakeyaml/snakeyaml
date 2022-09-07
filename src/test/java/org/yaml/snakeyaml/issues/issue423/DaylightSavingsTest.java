/**
 * Copyright (c) 2008, SnakeYAML
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.yaml.snakeyaml.issues.issue423;

import java.util.Calendar;
import java.util.TimeZone;
import junit.framework.TestCase;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class DaylightSavingsTest extends TestCase {

  public void testDaylightSavings20181104() {

    // Daylight savings ends on 2018-11-04 and 2 am America/Denver Timezone falls back from -06:00
    // to -07:00

    TimeZone denverTimeZone = TimeZone.getTimeZone("America/Denver");

    DumperOptions dumperOptions = new DumperOptions();
    dumperOptions.setTimeZone(TimeZone.getTimeZone("America/Denver"));

    Yaml yaml = new Yaml(dumperOptions);

    Calendar midnightBeforeFallback = Calendar.getInstance(denverTimeZone);
    midnightBeforeFallback.set(2018, Calendar.NOVEMBER, 4, 0, 0);

    String dateString = yaml.dump(midnightBeforeFallback).trim(); // Trim to remove the new line
    // character

    assertTrue("Timezone value should be -06:00", dateString.endsWith("-06:00"));

    Calendar oneHourAfterFallback = Calendar.getInstance(denverTimeZone);
    oneHourAfterFallback.set(2018, Calendar.NOVEMBER, 4, 3, 0);

    dateString = yaml.dump(oneHourAfterFallback).trim();

    assertTrue("Timezone value should be -07:00", dateString.endsWith("-07:00"));

  }

}
