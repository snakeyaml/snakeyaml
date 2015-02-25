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
package org.yaml.snakeyaml.issues.issue49;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;

public class CalendarTest extends TestCase {
    /**
     * Daylight Saving Time is not taken into account
     */
    public void testDumpDstIgnored() {
        CalendarBean bean = new CalendarBean();
        bean.setName("lunch");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(1000000000000L));
        cal.setTimeZone(TimeZone.getTimeZone("GMT-8:00"));
        bean.setCalendar(cal);
        Yaml yaml = new Yaml();
        String output = yaml.dumpAsMap(bean);
        // System.out.println(output);
        assertEquals("calendar: 2001-09-08T17:46:40-8:00\nname: lunch\n", output);
        //
        Yaml loader = new Yaml();
        CalendarBean parsed = loader.loadAs(output, CalendarBean.class);
        assertEquals(bean.getCalendar(), parsed.getCalendar());
    }

    /**
     * Daylight Saving Time is in effect on this date/time in
     * America/Los_Angeles Daylight<br/>
     * Saving Time is not in effect on this date/time in GMT
     */
    public void testDumpDstIsNotTheSame() {
        check(1000000000000L, "America/Los_Angeles", "Must be 7 hours difference.",
                "2001-09-08T18:46:40-7:00");
    }

    /**
     * Daylight Saving Time is not in effect on this date/time in
     * America/Los_Angeles Daylight<br/>
     * Saving Time is not in effect on this date/time in GMT
     */
    public void testDumpDstIsTheSame() {
        check(1266833741374L, "America/Los_Angeles", "Must be 8 hours difference.",
                "2010-02-22T02:15:41.374-8:00");
    }

    /**
     * Test odd time zone
     */
    public void testNepal() {
        check(1266833741374L, "Asia/Katmandu", "Must be 5:45 hours difference.",
                "2010-02-22T16:00:41.374+5:45");
    }

    public void testMoreThen10hours() {
        check(1266833741374L, "Asia/Kamchatka", "Must be 12 hours difference.",
                "2010-02-22T22:15:41.374+12:00");
    }

    private void check(long time, String timeZone, String warning, String etalon) {
        CalendarBean bean = new CalendarBean();
        bean.setName("lunch");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(time));
        cal.setTimeZone(TimeZone.getTimeZone(timeZone));
        bean.setCalendar(cal);
        Yaml yaml = new Yaml();
        String output = yaml.dumpAsMap(bean);
        // System.out.println(output);
        assertEquals(warning, "calendar: " + etalon + "\nname: lunch\n", output);
        //
        Yaml loader = new Yaml();
        CalendarBean parsed = loader.loadAs(output, CalendarBean.class);
        assertFalse("TimeZone must deviate.", bean.getCalendar().equals(parsed.getCalendar()));
        assertEquals(bean.getCalendar().getTimeInMillis(), parsed.getCalendar().getTimeInMillis());
    }

    public void testLoadBean() {
        Yaml beanLoader = new Yaml();
        CalendarBean bean = beanLoader.loadAs(
                "calendar:  2001-12-14t21:59:43.10-05:00\nname: dinner", CalendarBean.class);
        assertEquals("dinner", bean.getName());
        Calendar calendar = bean.getCalendar();
        assertEquals(TimeZone.getTimeZone("GMT-5:00").getOffset(calendar.getTime().getTime()),
                calendar.getTimeZone().getOffset(calendar.getTime().getTime()));
        //
        Yaml yaml = new Yaml();
        Date date = (Date) yaml.load("2001-12-14t21:59:43.10-05:00");
        assertEquals(date, calendar.getTime());
    }

    public void testLoadWithTag() {
        Yaml yaml = new Yaml();
        GregorianCalendar calendar = (GregorianCalendar) yaml
                .load("!!java.util.GregorianCalendar 2001-12-14t21:59:43.10-05:00");
        assertEquals(TimeZone.getTimeZone("GMT-5:00").getOffset(calendar.getTime().getTime()),
                calendar.getTimeZone().getOffset(calendar.getTime().getTime()));
        //
        Date date = (Date) yaml.load("2001-12-14t21:59:43.10-05:00");
        assertEquals(date, calendar.getTime());
    }
}
