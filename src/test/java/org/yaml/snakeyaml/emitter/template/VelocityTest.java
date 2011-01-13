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

package org.yaml.snakeyaml.emitter.template;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.JavaBeanDumper;
import org.yaml.snakeyaml.JavaBeanLoader;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.immutable.Point;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

public class VelocityTest extends TestCase {
    public void testNoTemplate() {
        DumperOptions options = new DumperOptions();
        options.setAllowReadOnlyProperties(true);
        options.setExplicitRoot(Tag.MAP);
        options.setDefaultFlowStyle(FlowStyle.BLOCK);
        JavaBeanDumper dumper = new JavaBeanDumper(new Representer(), options);
        String output = dumper.dump(createBean());
        // System.out.println(output);
        assertEquals(Util.getLocalResource("template/etalon1.yaml"), output);
    }

    public void testTemplate1() throws Exception {
        VelocityContext context = new VelocityContext();
        MyBean bean = createBean();
        context.put("bean", bean);
        Yaml yaml = new Yaml();
        context.put("list", yaml.dump(bean.getList()));
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty("file.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();
        Template t = ve.getTemplate("template/mybean1.vm");
        StringWriter writer = new StringWriter();
        t.merge(context, writer);
        String output = writer.toString().trim().replaceAll("\\r\\n", "\n");
        // System.out.println(output);
        String etalon = Util.getLocalResource("template/etalon2-template.yaml").trim();
        assertEquals(etalon.length(), output.length());
        assertEquals(etalon, output);
        // parse the YAML document
        JavaBeanLoader<MyBean> loader = new JavaBeanLoader<MyBean>(MyBean.class);
        MyBean parsedBean = loader.load(output);
        assertEquals(bean, parsedBean);
    }

    private MyBean createBean() {
        MyBean bean = new MyBean();
        bean.setId("id123");
        List<String> list = new ArrayList<String>();
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");
        bean.setList(list);
        Point p = new Point(1.0, 2.0);
        bean.setPoint(p);
        return bean;
    }
}
