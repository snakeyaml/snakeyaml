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
package org.yaml.snakeyaml.types;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * @see http://yaml.org/type/timestamp.html
 */
public class TimestampTagTest extends AbstractTest {

    public void testTimestamp() {
        assertEquals("2001-12-15 at 2:59:43 (100)",
                getText("canonical: 2001-12-15T02:59:43.1Z", "canonical"));
        // zero miliseconds
        assertEquals("2001-12-15 at 2:59:43 (0)",
                getText("canonical: 2001-12-15T02:59:43.000Z", "canonical"));
        assertEquals("2001-12-15 at 2:59:43 (100)",
                getText("valid iso8601:    2001-12-14t21:59:43.10-05:00", "valid iso8601"));
        // half hour time zone
        assertEquals("2001-12-14 at 22:29:43 (100)",
                getText("valid iso8601:    2001-12-14t21:59:43.10-0:30", "valid iso8601"));
        // + time zone
        assertEquals("2001-12-14 at 19:59:43 (100)",
                getText("valid iso8601:    2001-12-14t21:59:43.10+2:00", "valid iso8601"));
        assertEquals("2001-12-15 at 2:59:43 (100)",
                getText("space separated:  2001-12-14 21:59:43.10 -5", "space separated"));
        assertEquals("2001-12-15 at 2:59:43 (100)",
                getText("no time zone (Z): 2001-12-15 2:59:43.10", "no time zone (Z)"));
        assertEquals("2002-12-14 at 0:0:0 (0)",
                getText("date (00:00:00Z): 2002-12-14", "date (00:00:00Z)"));
        assertEquals("2010-5-16 at 3:6:11 (3)",
                getText("milliseconds: 2010-05-16 03:06:11.003", "milliseconds"));
        assertEquals("2010-5-16 at 3:6:11 (7)",
                getText("milliseconds: 2010-05-16 03:06:11.0068", "milliseconds"));
        assertEquals("2010-5-16 at 3:6:11 (0)",
                getText("milliseconds: 2010-05-16 03:06:11.0000", "milliseconds"));
        assertEquals("2010-5-16 at 3:6:11 (0)",
                getText("milliseconds: 2010-05-16 03:06:11.0004", "milliseconds"));
        assertEquals("2010-5-16 at 3:6:11 (25)",
                getText("milliseconds: 2010-05-16 03:06:11.0250", "milliseconds"));
    }

    public void testTimestampShorthand() {
        assertTrue(getMapValue("canonical: !!timestamp 2001-12-15T02:59:43.1Z", "canonical") instanceof Date);
    }

    public void testTimestampTag() {
        assertTrue(getMapValue("canonical: !<tag:yaml.org,2002:timestamp> 2001-12-15T02:59:43.1Z",
                "canonical") instanceof Date);
    }

    public void testTimestampOut() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Moscow"));
        cal.clear();
        cal.set(2008, 8, 23, 14, 35, 4);
        Date date = cal.getTime();
        String output = dump(date);
        assertEquals("2008-09-23T10:35:04Z\n", output);
    }

    public void testTimestampOutMap() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Moscow"));
        cal.clear();
        cal.set(2008, 8, 23, 14, 35, 4);
        Date date = cal.getTime();
        Map<String, Date> map = new HashMap<String, Date>();
        map.put("canonical", date);
        String output = dump(map);
        assertEquals("{canonical: !!timestamp '2008-09-23T10:35:04Z'}\n", output);
    }

    private String getText(String yaml, String key) {
        Date date = (Date) getMapValue(yaml, key);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(date);
        int years = cal.get(Calendar.YEAR);
        int months = cal.get(Calendar.MONTH) + 1; // 0..12
        int days = cal.get(Calendar.DAY_OF_MONTH); // 1..31
        int hour24 = cal.get(Calendar.HOUR_OF_DAY); // 0..24
        int minutes = cal.get(Calendar.MINUTE); // 0..59
        int seconds = cal.get(Calendar.SECOND); // 0..59
        int millis = cal.get(Calendar.MILLISECOND);
        String result = String.valueOf(years) + "-" + String.valueOf(months) + "-"
                + String.valueOf(days) + " at " + String.valueOf(hour24) + ":"
                + String.valueOf(minutes) + ":" + String.valueOf(seconds) + " ("
                + String.valueOf(millis) + ")";
        return result;
    }

    public void testTimestampReadWrite() {
        Date date = (Date) getMapValue("Time: 2001-11-23 15:01:42 -5", "Time");
        Map<String, Date> map = new HashMap<String, Date>();
        map.put("canonical", date);
        String output = dump(map);
        assertEquals("{canonical: !!timestamp '2001-11-23T20:01:42Z'}\n", output);
    }

    public void testSqlDate() {
        java.sql.Date date = new java.sql.Date(1000000000000L);
        Map<String, java.sql.Date> map = new HashMap<String, java.sql.Date>();
        map.put("canonical", date);
        String output = dump(map);
        assertEquals("{canonical: !!timestamp '2001-09-09T01:46:40Z'}\n", output);
    }
}
