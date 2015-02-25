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
package org.yaml.snakeyaml.issues.issue102;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;

public class BigDataLoadTest extends TestCase {
    private static final int SIZE = 5000;

    public void testBigStringData() {
        Yaml yaml = new Yaml();
        List<?> loaded = (List<?>) yaml.load(getLongYamlDocument(SIZE));
        assertEquals(SIZE, loaded.size());
    }

    public void testBigStreamData() {
        Yaml yaml = new Yaml();
        StringReader buffer = new StringReader(getLongYamlDocument(SIZE));
        List<?> loaded = (List<?>) yaml.load(buffer);
        assertEquals(SIZE, loaded.size());
    }

    private String getLongYamlDocument(int size) {
        List<DataBean> beans = new ArrayList<DataBean>();
        Yaml yaml = new Yaml();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            List<String> list = new ArrayList<String>();
            for (int j = 0; j < 10; j++) {
                list.add(String.valueOf(i + j));
            }
            Map<String, Integer> map = new HashMap<String, Integer>();
            for (int j = 0; j < 10; j++) {
                map.put(String.valueOf(j), i + j);
            }
            DataBean bean = new DataBean();
            bean.setId("id" + i);
            bean.setList(list);
            bean.setMap(map);
            beans.add(bean);
            String ooo = yaml.dumpAsMap(bean);
            String[] lines = ooo.split("\n");
            builder.append("-\n");
            for (int j = 0; j < lines.length; j++) {
                builder.append("  ");
                builder.append(lines[j]);
                builder.append("\n");
            }
        }
        String data = builder.toString();
        System.out.println("Long data size: " + data.length() / 1024 + " kBytes.");
        return data;
    }
}
