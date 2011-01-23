/**
 * Copyright (c) 2008-2011, http://www.snakeyaml.org
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.JavaBeanDumper;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

public class BigDataLoadTest extends TestCase {

    public void testBigData() {
        int size = 5000;
        Yaml yaml = new Yaml();
        List<?> loaded = (List<?>) yaml.load(getLongYamlDocument(size));
        assertEquals(size, loaded.size());
    }

    private String getLongYamlDocument(int size) {
        List<DataBean> beans = new ArrayList<DataBean>();
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(FlowStyle.AUTO);
        options.setExplicitRoot(Tag.MAP);
        JavaBeanDumper dumper = new JavaBeanDumper(new Representer(), options);
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
            String ooo = dumper.dump(bean);
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
