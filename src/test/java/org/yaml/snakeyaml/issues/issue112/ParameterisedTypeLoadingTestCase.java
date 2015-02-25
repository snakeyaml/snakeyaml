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
package org.yaml.snakeyaml.issues.issue112;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class ParameterisedTypeLoadingTestCase {

    @Test
    public void testParameterisedTypeLoading() throws IOException {
        Yaml yamlParser = new Yaml(new Constructor(MyCompositeObject.class));
        MyCompositeObject obj = (MyCompositeObject) yamlParser.load(getInput());
        check(obj);

        // dump the object
        Yaml yaml = new Yaml();
        String output = yaml.dumpAsMap(obj);
        assertEquals(Util.getLocalResource("issues/issue112-2.yaml"), output);
    }

    @Test
    public void testJavaBeanLoader() throws IOException {
        Yaml yamlParser = new Yaml();
        MyCompositeObject obj = yamlParser.loadAs(getInput(), MyCompositeObject.class);
        check(obj);
    }

    private void check(MyCompositeObject obj) {
        Object[] values = { 1, "two", 3, "four", "!!!" };
        assertNotNull(obj);
        assertEquals(5, obj.getThings().size());
        int i = 0;
        for (MyClass<? extends Object> thing : obj.getThings()) {
            assertEquals(MyClass.class, thing.getClass());
            assertNotNull("The 'name' property must be set.", thing.getName());
            assertEquals(values[i++], thing.getName());
        }
    }

    private InputStream getInput() throws IOException {
        return this.getClass().getClassLoader().getResource("issues/issue112-1.yaml").openStream();
    }
}
