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
package org.yaml.snakeyaml.issues.issue10;

import java.util.ArrayList;
import java.util.Iterator;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;

public class BasicDumpTest extends TestCase {

    public void testTag() {
        DataSource base = new DataSource();
        JDBCDataSource baseJDBC = new JDBCDataSource();
        baseJDBC.setParent(base);

        ArrayList<DataSource> dataSources = new ArrayList<DataSource>();
        // trying expected order first
        dataSources.add(base);
        dataSources.add(baseJDBC);

        DataSources ds = new DataSources();
        ds.setDataSources(dataSources);

        Yaml yaml = new Yaml();
        String output = yaml.dump(ds);

        String etalon = Util.getLocalResource("javabeans/issue10-1.yaml");
        assertEquals(etalon.trim(), output.trim());
        Object obj = yaml.load(output);
        DataSources dsOut = (DataSources) obj;
        Iterator<DataSource> iter = dsOut.getDataSources().iterator();
        assertFalse("Must be DataSource.", iter.next() instanceof JDBCDataSource);
        assertTrue(iter.next() instanceof JDBCDataSource);
    }

    public void testTag2() {
        DataSource base = new DataSource();
        JDBCDataSource baseJDBC = new JDBCDataSource();
        baseJDBC.setParent(base);

        ArrayList<DataSource> dataSources = new ArrayList<DataSource>();
        dataSources.add(base);
        dataSources.add(baseJDBC);

        DataSources ds = new DataSources();
        ds.setDataSources(dataSources);

        Yaml yaml = new Yaml();
        String output = yaml.dumpAsMap(ds);

        String etalon = Util.getLocalResource("javabeans/issue10-2.yaml");
        assertEquals(etalon.trim(), output.trim());
    }

    /**
     * different order does not require the global tag
     */
    public void testTag3() {
        DataSource base = new DataSource();
        JDBCDataSource baseJDBC = new JDBCDataSource();
        baseJDBC.setParent(base);

        ArrayList<DataSource> dataSources = new ArrayList<DataSource>();
        dataSources.add(baseJDBC);
        dataSources.add(base);

        DataSources ds = new DataSources();
        ds.setDataSources(dataSources);

        Yaml yaml = new Yaml();
        String output = yaml.dumpAsMap(ds);

        String etalon = Util.getLocalResource("javabeans/issue10-3.yaml");
        assertEquals(etalon.trim(), output.trim());
        // load
        Yaml beanLoader = new Yaml();
        DataSources bean = beanLoader.loadAs(output, DataSources.class);
        Iterator<DataSource> iter = bean.getDataSources().iterator();
        assertTrue(iter.next() instanceof JDBCDataSource);
        assertFalse("Must be DataSource.", iter.next() instanceof JDBCDataSource);
    }
}
