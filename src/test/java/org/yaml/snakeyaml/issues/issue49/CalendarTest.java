/**
 * Copyright (c) 2008-2010 Andrey Somov
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

import org.yaml.snakeyaml.JavaBeanDumper;
import org.yaml.snakeyaml.JavaBeanLoader;
import org.yaml.snakeyaml.Yaml;

public class CalendarTest extends TestCase {
    public void testDump() {
        CalendarBean bean = new CalendarBean();
        bean.setName("lunch");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(1000000000000L));
        cal.setTimeZone(TimeZone.getTimeZone("GMT-8:00"));
        bean.setCalendar(cal);
        JavaBeanDumper yaml = new JavaBeanDumper();
        String output = yaml.dump(bean);
        // System.out.println(output);
        // TODO assertEquals("???", output);
        assertTrue(output, output.startsWith("calendar: !!java.util.GregorianCalendar"));
    }

    public void testLoadBean() {
        JavaBeanLoader<CalendarBean> beanLoader = new JavaBeanLoader<CalendarBean>(
                CalendarBean.class);
        CalendarBean bean = beanLoader
                .load("calendar:  2001-12-14t21:59:43.10-05:00\nname: dinner");
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
