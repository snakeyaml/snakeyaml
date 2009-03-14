/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml;

import java.io.InputStream;
import java.io.StringReader;

import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.reader.UnicodeReader;
import org.yaml.snakeyaml.resolver.Resolver;

/**
 * Convenience utility to parse JavaBeans. The returned instance is enforced to
 * be of the same class as the provided argument. All the methods are Thread
 * safe. When the YAML document contains a global tag with the class definition
 * like '!!com.package.MyBean' it is ignored in favour of the runtime class.
 */
public class JavaBeanParser {

    private JavaBeanParser() {
    }

    /**
     * @param yaml
     *            - YAML document
     * @param javabean
     *            - JavaBean class to be parsed
     * @return parsed JavaBean
     */
    @SuppressWarnings("unchecked")
    public static <T> T load(String yaml, Class<T> javabean) {
        Loader loader = createLoader(javabean);
        return (T) loader.load(new StringReader(yaml));
    }

    /**
     * Parse the first YAML document in a stream and produce the corresponding
     * JavaBean.
     * 
     * @param io
     *            - data to load from (BOM is respected and removed)
     * @param javabean
     *            - JavaBean class to be parsed
     * @return parsed JavaBean
     */
    @SuppressWarnings("unchecked")
    public static <T> T load(InputStream io, Class<T> javabean) {
        Loader loader = createLoader(javabean);
        return (T) loader.load(new UnicodeReader(io));
    }

    /**
     * Parse the first YAML document in a stream and produce the corresponding
     * Java object.
     * 
     * @param io
     *            - data to load from (BOM must not be present)
     * @param javabean
     *            - JavaBean class to be parsed
     * @return parsed JavaBean
     */
    @SuppressWarnings("unchecked")
    public static <T> T load(java.io.Reader io, Class<T> javabean) {
        Loader loader = createLoader(javabean);
        return (T) loader.load(io);
    }

    private static Loader createLoader(Class<? extends Object> clazz) {
        Loader loader = new Loader(new Constructor(clazz));
        Resolver resolver = new Resolver();
        loader.setResolver(resolver);
        return loader;
    }
}
