/**
 * Copyright (c) 2008, SnakeYAML
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
package org.yaml.snakeyaml.issues.issue374;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import static org.junit.Assert.assertEquals;


public class NumberAsJavaBeanPropertyTest {

    @Test
    public void testNumberAsDouble() {
        Locale originalLocale = Locale.getDefault();

        Locale.setDefault(Locale.CANADA_FRENCH);

        AmbiguousNumberType original = new AmbiguousNumberType();
        original.number = 1.1;
        assertEquals(Double.class, original.number.getClass());

        Yaml yaml = new Yaml();
        String str = yaml.dump(original);

        AmbiguousNumberType interpreted = (AmbiguousNumberType) yaml.load(str);

        Locale.setDefault(originalLocale);
        assertEquals(original.number, interpreted.number);
    }

    public static class AmbiguousNumberType {
        public Number number;
    }

    @Test
    public void testNumberAsInteger() {
        AmbiguousNumberType original = new AmbiguousNumberType();
        original.number = 1;
        assertEquals(Integer.class, original.number.getClass());


        Yaml yaml = new Yaml();
        String str = yaml.dump(original);

        AmbiguousNumberType interpreted = (AmbiguousNumberType) yaml.load(str);

        assertEquals(Double.valueOf(original.number.intValue()), interpreted.number);
    }

    @Test
    public void testNumberAsLong() {
        AmbiguousNumberType original = new AmbiguousNumberType();
        original.number = 1L;
        assertEquals(Long.class, original.number.getClass());


        Yaml yaml = new Yaml();
        String str = yaml.dump(original);

        AmbiguousNumberType interpreted = (AmbiguousNumberType) yaml.load(str);

        assertEquals(Double.valueOf(original.number.intValue()), interpreted.number);
    }

    @Test
    public void testNumberFormatParse() throws ParseException {
        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        assertEquals(Long.valueOf(1), nf.parse("1"));
        assertEquals("NumberFormat converts 1.0 to 1 - this is against the specification.",
                Long.valueOf(1), nf.parse("1.0"));
    }
}
