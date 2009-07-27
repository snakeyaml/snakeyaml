/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.yaml.snakeyaml.constructor.Constructor;

public class YamlDocument {
    public static final String ROOT = "specification/";
    private String source;
    private String presentation;
    private Object nativeData;

    public YamlDocument(String sourceName, boolean check, Constructor constructor) {
        InputStream input = YamlDocument.class.getClassLoader().getResourceAsStream(
                ROOT + sourceName);
        if (constructor == null) {
            constructor = new Constructor();
        }
        Loader loader = new Loader(constructor);
        Yaml yaml = new Yaml(loader);
        nativeData = yaml.load(input);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Charset charset = Charset.forName("UTF-8");
        yaml.dump(nativeData, new OutputStreamWriter(output, charset));
        try {
            presentation = output.toString(charset.name());
            source = Util.getLocalResource(ROOT + sourceName);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        // try to read generated presentation to prove that the presentation
        // is identical to the source
        Object result = yaml.load(presentation);
        if (check && !nativeData.equals(result)) {
            throw new RuntimeException("Generated presentation is not valid: " + presentation);
        }
    }

    public YamlDocument(String sourceName, boolean check) {
        this(sourceName, check, null);
    }

    public YamlDocument(String sourceName) {
        this(sourceName, true);
    }

    public String getSource() {
        return source;
    }

    public String getPresentation() {
        return presentation;
    }

    public Object getNativeData() {
        if (nativeData == null) {
            throw new NullPointerException("No object is parsed.");
        }
        return nativeData;
    }
}
