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
package org.yaml.snakeyaml.resolver;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Construct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.Tag;

/**
 * Custom implicit resolver does not apply inside JavaBean declaration <a href=
 * "http://groups.google.com/group/snakeyaml-core/browse_frm/thread/c75c35a3d9cfcaba"
 * >mailing list</a> for more information
 */
public class ImplicitResolverTest extends TestCase {
    private static final Tag CFG = new Tag("!cfg");

    public static class ConfigurationConstructor extends Constructor {
        protected Map<String, String> config = null;

        public ConfigurationConstructor(Map<String, String> config) {
            this.config = config;
            this.yamlConstructors.put(CFG, new ConfigObjectConstruct());
        }

        private class ConfigObjectConstruct extends AbstractConstruct {
            public Object construct(Node node) {
                String val = (String) constructScalar((ScalarNode) node);
                val = val.substring(2, val.length() - 1);
                return config.get(val);
            }
        }

        protected Construct getConstructor(Node node) {
            if (CFG.equals(node.getTag())) {
                node.setUseClassConstructor(false);
            }
            return super.getConstructor(node);
        }
    }

    public static class TestBean {
        String myval;

        public String getMyval() {
            return myval;
        }

        public void setMyval(String myval) {
            this.myval = myval;
        }

        public String toString() {
            return "MyVal: " + myval;
        }
    }

    public void testMain() {
        Map<String, String> config = new HashMap<String, String>();
        config.put("user.home", "HOME");
        Constructor constructor = new ConfigurationConstructor(config);
        constructor.addTypeDescription(new TypeDescription(TestBean.class, "!testbean"));
        Yaml yaml = new Yaml(constructor);
        yaml.addImplicitResolver(CFG, Pattern.compile("\\$\\([a-zA-Z\\d\\u002E\\u005F]+\\)"), "$");
        TestBean bean = (TestBean) yaml.load("!testbean {myval: !cfg $(user.home)}");
        // System.out.println(bean.toString());
        assertEquals("Explicit tag must be respected", "HOME", bean.getMyval());
        bean = (TestBean) yaml.load("!testbean {myval: $(user.home)}");
        // System.out.println(bean.toString());
        assertEquals("Implicit tag must be respected", "HOME", bean.getMyval());
    }
}
