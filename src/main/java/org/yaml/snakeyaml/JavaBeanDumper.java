/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.resolver.Resolver;

/**
 * Convenience utility to serialize JavaBeans.
 */
public class JavaBeanDumper {
    private boolean useGlobalTag;
    private FlowStyle flowStyle;

    /**
     * Create Dumper for JavaBeans
     * 
     * @param useGlobalTag
     *            true to emit the global tag with the class name
     */
    public JavaBeanDumper(boolean useGlobalTag) {
        this.useGlobalTag = useGlobalTag;
        this.flowStyle = FlowStyle.BLOCK;
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
            options.setExplicitRoot("tag:yaml.org,2002:map");
        }
        options.setDefaultFlowStyle(flowStyle);
        Dumper dumper = new Dumper(options);
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

}
