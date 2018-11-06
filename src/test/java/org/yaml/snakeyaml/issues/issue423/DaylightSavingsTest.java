package org.yaml.snakeyaml.issues.issue423;

import junit.framework.TestCase;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.util.Calendar;
import java.util.TimeZone;

public class DaylightSavingsTest extends TestCase {

    public void testDaylightSavings20181104() {

        // Daylight savings ends on 2018-11-04 and 2 am America/Denver Timezone falls back from -06:00 to -07:00

        TimeZone denverTimeZone = TimeZone.getTimeZone("America/Denver");

        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setTimeZone(TimeZone.getTimeZone("America/Denver"));

        Yaml yaml = new Yaml(dumperOptions);

        Calendar midnightBeforeFallback = Calendar.getInstance(denverTimeZone);
        midnightBeforeFallback.set(2018, Calendar.NOVEMBER, 4, 0, 0);

        String dateString = yaml.dump(midnightBeforeFallback).trim();  // Trim to remove the new line character

        assertTrue("Timezone value should be -06:00", dateString.endsWith("-06:00"));

        Calendar oneHourAfterFallback = Calendar.getInstance(denverTimeZone);
        oneHourAfterFallback.set(2018, Calendar.NOVEMBER, 4, 3, 0);

        dateString = yaml.dump(oneHourAfterFallback).trim();

        assertTrue("Timezone value should be -07:00", dateString.endsWith("-07:00"));

    }

}
