/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.yaml.snakeyaml.reader.UnicodeReader;
import org.yaml.snakeyaml.resolver.Resolver;

/**
 * Public YAML interface. Each Thread must have its own instance.
 */
public class Yaml {
    private final Dumper dumper;
    private final Loader loader;
    private final Resolver resolver;
    private String name;

    public Yaml(DumperOptions options) {
        this(new Loader(), new Dumper(options));
    }

    public Yaml(Dumper dumper) {
        this(new Loader(), dumper);
    }

    public Yaml(Loader loader) {
        this(loader, new Dumper(new DumperOptions()));
    }

    /**
     * Create Yaml instance. It is safe to create a few instances and use them
     * in different Threads.
     * 
     * @param loader
     *            - Loader to parse incoming documents
     * @param dumper
     *            - Dumper to emit outgoing objects
     */
    public Yaml(Loader loader, Dumper dumper) {
        this.loader = loader;
        loader.setAttached();
        this.dumper = dumper;
        dumper.setAttached();
        this.resolver = new Resolver();
        this.loader.setResolver(resolver);
        this.name = "Yaml:" + System.identityHashCode(this);
    }

    public Yaml() {
        this(new Loader(), new Dumper(new DumperOptions()));
    }

    /**
     * Serialize a Java object into a YAML String.
     * 
     * @param data
     *            - Java object to be Serialized to YAML
     * @return YAML String
     */
    public String dump(Object data) {
        List<Object> lst = new ArrayList<Object>(1);
        lst.add(data);
        return dumpAll(lst.iterator());
    }

    /**
     * Serialize a sequence of Java objects into a YAML String.
     * 
     * @param data
     *            - Iterator with Objects
     * @return - YAML String with all the objects in proper sequence
     */
    public String dumpAll(Iterator<? extends Object> data) {
        StringWriter buffer = new StringWriter();
        dumpAll(data, buffer);
        return buffer.toString();
    }

    /**
     * Serialize a Java object into a YAML stream.
     * 
     * @param data
     *            - Java object to be Serialized to YAML
     * @param output
     *            - stream to write to
     */
    public void dump(Object data, Writer output) {
        List<Object> lst = new ArrayList<Object>(1);
        lst.add(data);
        dumpAll(lst.iterator(), output);
    }

    /**
     * Serialize a sequence of Java objects into a YAML stream.
     * 
     * @param data
     *            - Iterator with Objects
     * @param output
     *            - stream to write to
     */
    public void dumpAll(Iterator<? extends Object> data, Writer output) {
        dumper.dump(data, output, resolver);
    }

    /**
     * Parse the first YAML document in a String and produce the corresponding
     * Java object. (Because the encoding in known BOM is not respected.)
     * 
     * @param yaml
     *            - YAML data to load from (BOM must not be present)
     * @return parsed object
     */
    public Object load(String yaml) {
        return loader.load(new StringReader(yaml));
    }

    /**
     * Parse the first YAML document in a stream and produce the corresponding
     * Java object.
     * 
     * @param io
     *            - data to load from (BOM is respected and removed)
     * @return parsed object
     */
    public Object load(InputStream io) {
        return loader.load(new UnicodeReader(io));
    }

    /**
     * Parse the first YAML document in a stream and produce the corresponding
     * Java object.
     * 
     * @param io
     *            - data to load from (BOM must not be present)
     * @return parsed object
     */
    public Object load(java.io.Reader io) {
        return loader.load(io);
    }

    /**
     * Parse all YAML documents in a String and produce corresponding Java
     * objects.
     * 
     * @param yaml
     *            - YAML data to load from (BOM must not be present)
     * @return an iterator over the parsed Java objects in this String in proper
     *         sequence
     */
    public Iterable<Object> loadAll(java.io.Reader yaml) {
        return loader.loadAll(yaml);
    }

    /**
     * Parse all YAML documents in a String and produce corresponding Java
     * objects. (Because the encoding in known BOM is not respected.)
     * 
     * @param yaml
     *            - YAML data to load from (BOM must not be present)
     * @return an iterator over the parsed Java objects in this String in proper
     *         sequence
     */
    public Iterable<Object> loadAll(String yaml) {
        return loadAll(new StringReader(yaml));
    }

    /**
     * Parse all YAML documents in a stream and produce corresponding Java
     * objects.
     * 
     * @param yaml
     *            - YAML data to load from (BOM is respected and ignored)
     * @return an iterator over the parsed Java objects in this stream in proper
     *         sequence
     */
    public Iterable<Object> loadAll(InputStream yaml) {
        return loadAll(new UnicodeReader(yaml));
    }

    /**
     * Add an implicit scalar detector. If an implicit scalar value matches the
     * given regexp, the corresponding tag is assigned to the scalar. first is a
     * sequence of possible initial characters or null (which means any).
     * 
     * @param tag
     *            - tag to assign to the node
     * @param regexp
     *            - regular expression to match against
     * @param first
     *            - a sequence of possible initial characters or None
     */
    public void addImplicitResolver(String tag, Pattern regexp, String first) {
        resolver.addImplicitResolver(tag, regexp, first);
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    /**
     * Set a meaningful name to be shown in toString()
     * 
     * @param name
     *            - human readable name
     */
    public void setName(String name) {
        this.name = name;
    }
}
