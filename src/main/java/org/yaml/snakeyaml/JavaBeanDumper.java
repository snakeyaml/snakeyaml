/**
 * Copyright (c) 2008-2009 Andrey Somov
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
package org.yaml.snakeyaml;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.nodes.Tags;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.resolver.Resolver;

/**
 * Convenience utility to serialize JavaBeans.
 */
public class JavaBeanDumper {
    private boolean useGlobalTag;
    private FlowStyle flowStyle;
    private Set<Class<? extends Object>> classTags;

    /**
     * Create Dumper for JavaBeans
     * 
     * @param useGlobalTag
     *            true to emit the global tag with the class name
     */
    public JavaBeanDumper(boolean useGlobalTag) {
        this.useGlobalTag = useGlobalTag;
        this.flowStyle = FlowStyle.BLOCK;
        classTags = new HashSet<Class<? extends Object>>();
    }

    /**
     * Create Dumper for JavaBeans. Use "tag:yaml.org,2002:map" as the root tag.
     */
    public JavaBeanDumper() {
        this(false);
    }

    /**
     * Serialize JavaBean
     * 
     * @param data
     *            JavaBean instance to serialize
     * @param output
     *            destination
     */
    public void dump(Object data, Writer output) {
        DumperOptions options = new DumperOptions();
        if (!useGlobalTag) {
            options.setExplicitRoot(Tags.MAP);
        }
        options.setDefaultFlowStyle(flowStyle);
        Representer representer = new Representer();
        for (Class<? extends Object> clazz : classTags) {
            representer.addClassTag(clazz, Tags.MAP);
        }
        Dumper dumper = new Dumper(representer, options);
        List<Object> list = new ArrayList<Object>(1);
        list.add(data);
        dumper.dump(list.iterator(), output, new Resolver());
    }

    /**
     * Serialize JavaBean
     * 
     * @param data
     *            JavaBean instance to serialize
     * @return serialized YAML document
     */
    public String dump(Object data) {
        StringWriter buffer = new StringWriter();
        dump(data, buffer);
        return buffer.toString();
    }

    public boolean isUseGlobalTag() {
        return useGlobalTag;
    }

    public void setUseGlobalTag(boolean useGlobalTag) {
        this.useGlobalTag = useGlobalTag;
    }

    public FlowStyle getFlowStyle() {
        return flowStyle;
    }

    public void setFlowStyle(FlowStyle flowStyle) {
        this.flowStyle = flowStyle;
    }

    /**
     * Skip global tag with the specified class in a type-safe collection
     * 
     * @param clazz
     *            JavaBean <code>Class</code> to represent as Map
     */
    public void setMapTagForBean(Class<? extends Object> clazz) {
        classTags.add(clazz);
    }
}
