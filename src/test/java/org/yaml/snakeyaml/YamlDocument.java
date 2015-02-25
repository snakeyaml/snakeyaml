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
        Yaml yaml = new Yaml(constructor);
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
