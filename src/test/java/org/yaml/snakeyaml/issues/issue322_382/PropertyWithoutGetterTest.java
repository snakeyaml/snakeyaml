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
package org.yaml.snakeyaml.issues.issue322_382;

import org.junit.Test;

import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PropertyWithoutGetterTest {

    @Test
    public void testPublicFooWithPublicFields() {
        Constructor constructor = new Constructor();
        constructor
                .addTypeDescription(new TypeDescription(PublicFooWithPublicFields.class, "!foo"));
        Yaml yaml = new Yaml(constructor);

        PublicFooWithPublicFields foo = yaml.loadAs("!foo\ncountryCodes: [NZ, NO]\nsome: NO",
                PublicFooWithPublicFields.class);

        assertEquals(2, foo.countryCodes.size());
        assertEquals("NZ", foo.countryCodes.get(0));
        assertEquals("The type (String) must be taken from the field.", "NO",
                foo.countryCodes.get(1));
        assertEquals("NO", foo.some);
    }

    @Test
    public void testStaticFooWithoutGetter() {
        Constructor constructor = new Constructor();
        constructor.addTypeDescription(new TypeDescription(StaticFooWithoutGetter.class, "!foo"));
        Yaml yaml = new Yaml(constructor);

        StaticFooWithoutGetter foo = yaml.loadAs("!foo\ncountryCodes: [NZ, NO]\nsome: NO",
                StaticFooWithoutGetter.class);

        assertEquals(2, foo.countryCodes.size());
        assertEquals("NZ", foo.countryCodes.get(0));
        assertEquals("The type List(String) must be taken from the setter.", "NO",
                foo.countryCodes.get(1));
        assertEquals("NO", foo.some);
    }

    @Test
    public void testStaticFooWithGetter() {
        Constructor constructor = new Constructor();
        constructor.addTypeDescription(new TypeDescription(StaticFooWithGetter.class, "!foo"));
        Yaml yaml = new Yaml(constructor);

        StaticFooWithGetter foo = yaml.loadAs("!foo\ncountryCodes: [NZ, NO]\nsome: NO",
                StaticFooWithGetter.class);

        assertEquals(2, foo.countryCodes.size());
        assertEquals("NZ", foo.countryCodes.get(0));
        assertEquals("The type List(String) must be taken from the getter.", "NO",
                foo.countryCodes.get(1));
        assertEquals("NO", foo.some);
    }

    public static class StaticFooWithoutGetter {
        private List<String> countryCodes = new ArrayList<String>();
        private String some;

        public void setCountryCodes(List<String> countryCodes) {
            for (Object countryCode : countryCodes) {
                //System.out.println(countryCode.getClass().getName());
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

        public List<String> getCountryCodes() {
            return countryCodes;
        }

        public void setCountryCodes(List<String> countryCodes) {
            for (Object countryCode : countryCodes) {
                //System.out.println(countryCode.getClass().getName());
            }
            this.countryCodes = countryCodes;
        }

        public void setSome(String sime) {
            this.some = sime;
        }
    }
}
