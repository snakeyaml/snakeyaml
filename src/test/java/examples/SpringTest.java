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
package examples;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.helpers.FileTestHelper;

import examples.spring.TestEntity;
import junit.framework.TestCase;

public class SpringTest extends TestCase {

    public void testSimple() {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("examples/spring.xml");
        try {
            Yaml yaml = (Yaml) context.getBean("standardYaml");
            assertNotNull(yaml);
            //
            yaml = (Yaml) context.getBean("javabeanYaml");
            assertNotNull(yaml);
            //
            yaml = (Yaml) context.getBean("snakeYaml");
            assertNotNull(yaml);

        } finally {
            IOUtils.closeQuietly(context);
        }
    }

    public void testTypeDescriptionWithBean() {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("examples/spring.xml");
        InputStream is = null;
        try {
            Yaml yaml = context.getBean("javabeanYamlWithCustomTypeDescriptions", Yaml.class);
            assertNotNull(yaml);
            is = FileTestHelper.getInputStreamFromClasspath("examples/spring/test-entity.yaml");

            TestEntity entity = yaml.loadAs(is, TestEntity.class);
            assertNotNull(entity);
            assertEquals(1, entity.getCounter()); //retrieved from DataRegistry
            assertEquals("1:A", entity.getId()); //loaded from yaml
            assertEquals("1:A-1:A", entity.getData()); //retrieved from DataRegistry based on ID and counter.
        } finally {
            IOUtils.closeQuietly(context);
            IOUtils.closeQuietly(is);
        }
    }

}