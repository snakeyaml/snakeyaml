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
package org.yaml.snakeyaml.issues.issue318;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

public class ContextClassLoaderTest {

    static public class DomainBean {

        private int value = 0;

        public void setValue(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + value;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            DomainBean other = (DomainBean) obj;
            if (value != other.value)
                return false;
            return true;
        }

    }

    private URLClassLoader yamlCL;

    @Before
    public void before() throws MalformedURLException {
        Properties classpath = new Properties();
        InputStream cpProperties = getClass().getResourceAsStream("classpath.properties");
        try {
            classpath.load(cpProperties);
        } catch (IOException e2) {
            fail(e2.getLocalizedMessage());
        }

        File runtimeClassesDir = new File(classpath.getProperty("runtime_classes_dir"));

        yamlCL = new URLClassLoader(new URL[] { runtimeClassesDir.toURI().toURL() }, null);
    }

    @After
    public void after() {
        // URLClassLoader.close is @since 1.7
        // if (yamlCL != null) {
        //   try {
        //     yamlCL.close();
        //   } catch (IOException e) {
        //     e.printStackTrace();
        //   } finally {
        yamlCL = null;
        //   }
        // }
    }

    @Test(expected = ClassNotFoundException.class)
    public void expectNoDomainClassInYamlCL() throws ClassNotFoundException {
        yamlCL.loadClass(DomainBean.class.getName());
    }

    @Test
    public void yamlClassInYAMLCL() throws ClassNotFoundException {
        yamlCL.loadClass(Yaml.class.getName());
    }

    @Test
    public void domainInDifferentConstructor() throws ClassNotFoundException,
            InstantiationException, IllegalAccessException, NoSuchMethodException,
            SecurityException, IllegalArgumentException, InvocationTargetException {

        Class<?> yamlClass = yamlCL.loadClass(Yaml.class.getName());

        DomainBean bean = new DomainBean();
        bean.setValue(13);

        Object yaml = yamlClass.newInstance();

        Method dumpMethod = yaml.getClass().getMethod("dump", new Class<?>[] { Object.class });
        String dump = dumpMethod.invoke(yaml, bean).toString();

        Method loadMethod = yaml.getClass().getMethod("load", new Class<?>[] { String.class });
        DomainBean object = (DomainBean) loadMethod.invoke(yaml, dump);

        assertEquals(bean, object);
    }

}
