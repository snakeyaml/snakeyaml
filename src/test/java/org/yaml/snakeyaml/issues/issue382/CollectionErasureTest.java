/**
 * Copyright (c) 2008, http://www.snakeyaml.org
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.yaml.snakeyaml.issues.issue382;

import org.junit.Test;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CollectionErasureTest {

    @Test
    public void testPublicFooWithoutGetter() {
        Constructor constructor = new Constructor();
        constructor.addTypeDescription(new TypeDescription(PublicFooWithoutGetter.class, "!foo"));
        Yaml yaml = new Yaml(constructor);

        PublicFooWithoutGetter foo = (PublicFooWithoutGetter) yaml.load("!foo\ncountryCodes: [NZ,AU,NO]\nsome: YES");

        assertEquals(3, foo.countryCodes.size());
        assertEquals("NZ", foo.countryCodes.get(0));
        assertEquals("AU", foo.countryCodes.get(1));
        assertEquals("NO", foo.countryCodes.get(2));
        assertEquals("YES", foo.some);
    }

    @Test
    public void testStaticFooWithoutGetter() {
        Constructor constructor = new Constructor();
        constructor.addTypeDescription(new TypeDescription(StaticFooWithoutGetter.class, "!foo"));
        Yaml yaml = new Yaml(constructor);

        StaticFooWithoutGetter foo = (StaticFooWithoutGetter) yaml.load("!foo\ncountryCodes: [NZ,AU,NO]\nsome: YES");

        assertEquals(3, foo.countryCodes.size());
        assertEquals("NZ", foo.countryCodes.get(0));
        assertEquals("AU", foo.countryCodes.get(1));
        assertEquals(false, foo.countryCodes.get(2)); //Wow !!! Dynamic typing in Java ?
        assertEquals("YES", foo.some);
    }

    @Test
    public void testStaticFooWithGetter() {
        Constructor constructor = new Constructor();
        constructor.addTypeDescription(new TypeDescription(StaticFooWithGetter.class, "!foo"));
        Yaml yaml = new Yaml(constructor);

        StaticFooWithGetter foo = (StaticFooWithGetter) yaml.load("!foo\ncountryCodes: [NZ,AU, NO]\nsome: YES");

        assertEquals(3, foo.countryCodes.size());
        assertEquals("NZ", foo.countryCodes.get(0));
        assertEquals("AU", foo.countryCodes.get(1));
        assertEquals("NO", foo.countryCodes.get(2));
        assertEquals("YES", foo.some);
    }

    public static class StaticFooWithoutGetter {
        private List<String> countryCodes = new ArrayList<String>();
        private String some;

        public void setCountryCodes(List<String> countryCodes) {
            for (Object countryCode : countryCodes) {
                System.out.println(countryCode.getClass().getName());
            }
            this.countryCodes = countryCodes;
        }

        public void setSome(String sime) {
            this.some = sime;
        }
    }

    public static class StaticFooWithGetter {
        private List<String> countryCodes = new ArrayList<String>();
        private String some;

        // If this method is not here then "NO" is a Boolean,
        // but when this method is here, then "NO" is a String.
        // *I* think it should always be considered a String, given the 'countryCodes' datatype :-)
        public List<String> getCountryCodes() {
            return countryCodes;
        }

        public void setCountryCodes(List<String> countryCodes) {
            for (Object countryCode : countryCodes) {
                System.out.println(countryCode.getClass().getName());
            }
            this.countryCodes = countryCodes;
        }

        public void setSome(String sime) {
            this.some = sime;
        }
    }
}

