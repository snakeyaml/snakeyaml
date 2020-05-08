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
package org.yaml.snakeyaml.issues.issue478;

import junit.framework.TestCase;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.Property;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class PropertyOrderTest extends TestCase {

    public void testParseBytes() {
        Yaml y = new Yaml(new org.yaml.snakeyaml.representer.Representer() {
            @Override
            protected Set<Property> getProperties(Class<? extends Object> type) {
                System.out.println("getProperties: reverse order");
                Set<Property> reverse = new TreeSet<>();
                Iterator<Property> itr = ((TreeSet) super.getProperties(type)).descendingIterator();
                while (itr.hasNext()) {
                    Property a = itr.next();
                    //System.out.println("> " + a.getName());
                    reverse.add(a);
                }
                return reverse;
            }
        });
        String v = "x: 1\ny: 2\nz: 3\n";
        Location location = y.loadAs(v, Location.class);

        String output = y.dumpAsMap(location);//FIXME the order must be reversed
        assertEquals(v, output);
    }
}
