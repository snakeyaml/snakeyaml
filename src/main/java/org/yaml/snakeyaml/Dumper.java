/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml;

import java.io.Writer;
import java.util.Iterator;

import org.yaml.snakeyaml.emitter.Emitter;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.resolver.Resolver;
import org.yaml.snakeyaml.serializer.Serializer;

/**
 * @see <a href="http://pyyaml.org/wiki/PyYAML">PyYAML</a> for more information
 */
public class Dumper {
    private final Representer representer;
    private final DumperOptions options;
    private boolean attached = false;

    public Dumper(Representer representer, DumperOptions options) {
        this.representer = representer;
        this.options = options;
    }

    public Dumper(DumperOptions options) {
        this(new Representer(options.getDefaultStyle().getChar(), options.getDefaultFlowStyle()
                .getStyleBoolean()), options);
    }

    public void dump(Iterator<? extends Object> iter, Writer output, Resolver resolver) {
        Serializer s = new Serializer(new Emitter(output, options), resolver, options);
        try {
            s.open();
            while (iter.hasNext()) {
                representer.represent(s, iter.next());
            }
            s.close();
        } catch (java.io.IOException e) {
            throw new YAMLException(e);
        }
    }

    /**
     * Because Dumper is stateful it cannot be shared
     */
    void setAttached() {
        if (!attached) {
            attached = true;
        } else {
            throw new YAMLException("Dumper cannot be shared.");
        }
    }
}
