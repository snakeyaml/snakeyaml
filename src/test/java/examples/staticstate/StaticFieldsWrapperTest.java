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
package examples.staticstate;

import junit.framework.TestCase;

import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

/**
 * Example: using wrapper object for static fields
 */
public class StaticFieldsWrapperTest extends TestCase {

    /**
     * use wrapper with global tag
     */
    public void testWrapper() {
        JavaBeanWithStaticState bean = new JavaBeanWithStaticState();
        bean.setName("Bahrack");
        bean.setAge(-47);
        JavaBeanWithStaticState.setType("Type3");
        JavaBeanWithStaticState.color = "Violet";
        Yaml yaml = new Yaml();
        String output = yaml.dump(new Wrapper(bean));
        // System.out.println(output);
        assertEquals(
                "!!examples.staticstate.Wrapper {age: -47, color: Violet, name: Bahrack, type: Type3}\n",
                output);
        // parse back to instance
        Wrapper wrapper = (Wrapper) yaml.load(output);
        JavaBeanWithStaticState bean2 = wrapper.createBean();
        assertEquals(-47, bean2.getAge());
        assertEquals("Bahrack", bean2.getName());
    }

    /**
     * use wrapper with local tag
     */
    public void testLocalTag() {
        JavaBeanWithStaticState bean = new JavaBeanWithStaticState();
        bean.setName("Bahrack");
        bean.setAge(-47);
        JavaBeanWithStaticState.setType("Type3");
        JavaBeanWithStaticState.color = "Violet";
        Representer repr = new Representer();
        repr.addClassTag(Wrapper.class, new Tag("!mybean"));
        Yaml yaml = new Yaml(repr);
        String output = yaml.dump(new Wrapper(bean));
        // System.out.println(output);
        assertEquals("!mybean {age: -47, color: Violet, name: Bahrack, type: Type3}\n", output);
        // parse back to instance
        Constructor constr = new Constructor();
        TypeDescription description = new TypeDescription(Wrapper.class, new Tag("!mybean"));
        constr.addTypeDescription(description);
        yaml = new Yaml(constr);
        Wrapper wrapper = (Wrapper) yaml.load(output);
        JavaBeanWithStaticState bean2 = wrapper.createBean();
        assertEquals(-47, bean2.getAge());
        assertEquals("Bahrack", bean2.getName());
    }

    /**
     * use wrapper with no tag
     */
    public void testRootBean() {
        JavaBeanWithStaticState bean = new JavaBeanWithStaticState();
        bean.setName("Bahrack");
        bean.setAge(-47);
        JavaBeanWithStaticState.setType("Type3");
        JavaBeanWithStaticState.color = "Violet";
        Yaml yaml = new Yaml();
        String output = yaml.dumpAsMap(new Wrapper(bean));
        // System.out.println(output);
        assertEquals("age: -47\ncolor: Violet\nname: Bahrack\ntype: Type3\n", output);
        // parse back to instance
        Yaml loader = new Yaml();
        Wrapper wrapper = loader.loadAs(output, Wrapper.class);
        JavaBeanWithStaticState bean2 = wrapper.createBean();
        assertEquals(-47, bean2.getAge());
        assertEquals("Bahrack", bean2.getName());
    }

}
