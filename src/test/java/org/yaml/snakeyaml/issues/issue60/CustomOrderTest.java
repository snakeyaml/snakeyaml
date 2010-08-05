/**
 * Copyright (c) 2008-2010, http://code.google.com/p/snakeyaml/
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

package org.yaml.snakeyaml.issues.issue60;

import java.beans.IntrospectionException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Dumper;
import org.yaml.snakeyaml.SnakeYaml;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.representer.Representer;

public class CustomOrderTest extends TestCase {

    public void testReversedOrder() {
        SnakeYaml yaml = new SnakeYaml(new Dumper(new ReversedRepresenter()));
        String output = yaml.dump(getBean());
        // System.out.println(output);
        assertEquals(Util.getLocalResource("issues/issue59-1.yaml"), output);
    }

    private class ReversedRepresenter extends Representer {
        @Override
        protected Set<Property> getProperties(Class<? extends Object> type)
                throws IntrospectionException {
            Set<Property> result = new TreeSet<Property>(Collections.reverseOrder());
            result.addAll(super.getProperties(type));
            return result;
        }
    }

    private SkipBean getBean() {
        SkipBean bean = new SkipBean();
        bean.setText("foo");
        bean.setListDate(null);
        bean.setListInt(Arrays.asList(new Integer[] { null, 1, 2, 3 }));
        bean.setListStr(Arrays.asList(new String[] { "bar", null, "foo", null }));
        return bean;
    }
}
