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
package org.yaml.snakeyaml.issues.issue114;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;

public class PreserveTypeTest extends TestCase {

    public static class MyBean {

        private int dummy;

        public int getDummy() {
            return dummy;
        }

        public void setDummy(int dummy) {
            this.dummy = dummy;
        }
    }

    public static class ReferencingBean {

        private List<MyBean> myBeans = new LinkedList<PreserveTypeTest.MyBean>();

        public List<MyBean> getMyBeans() {
            return myBeans;
        }

        public void setMyBeans(List<MyBean> myBeans) {
            this.myBeans = myBeans;
        }
    }

    private Map<String, Object> createData(boolean collectionFirst) {
        MyBean myBean = new MyBean();
        ReferencingBean referencingBean = new ReferencingBean();
        referencingBean.getMyBeans().add(myBean);

        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        if (collectionFirst) {
            map.put("referencingBean", referencingBean);
            map.put("myBean", myBean);
        } else {
            map.put("myBean", myBean);
            map.put("referencingBean", referencingBean);
        }
        return map;
    }

    private void check(String doc) {
        Yaml yaml = new Yaml();
        @SuppressWarnings("unchecked")
        Map<String, Object> loaded = (Map<String, Object>) yaml.load(doc);
        Object myBean2 = loaded.get("myBean");
        assertTrue(myBean2.getClass().toString(), myBean2 instanceof MyBean);
    }

    public void testPreserveType1() {
        Yaml yaml = new Yaml();
        String s = yaml.dump(createData(true));
        check(s);
    }

    public void testPreserveType2() {
        Yaml yaml = new Yaml();
        String s = yaml.dump(createData(false));
        check(s);
    }
}
