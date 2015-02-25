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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.AssertionFailedError;

public class YamlStream {
    private List<Object> nativeData = new ArrayList<Object>();

    public YamlStream(String sourceName) {
        InputStream input = YamlDocument.class.getClassLoader().getResourceAsStream(
                YamlDocument.ROOT + sourceName);
        Yaml yaml = new Yaml();
        for (Object document : yaml.loadAll(input)) {
            nativeData.add(document);
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        yaml.dumpAll(nativeData.iterator(), new OutputStreamWriter(output));
        String presentation;
        try {
            presentation = output.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        // try to read generated presentation to prove that the presentation
        // is identical to the source
        List<Object> parsedNativeData = new ArrayList<Object>();
        for (Object document : yaml.loadAll(presentation)) {
            parsedNativeData.add(document);
        }
        if (nativeData.getClass() != parsedNativeData.getClass()) {
            throw new AssertionFailedError("Different class: " + parsedNativeData.getClass());
        }
        if (nativeData.size() != parsedNativeData.size()) {
            throw new AssertionFailedError("Different size.");
        }
        Iterator<Object> piterator = parsedNativeData.iterator();
        Iterator<Object> niterator = nativeData.iterator();
        while (piterator.hasNext()) {
            Object obj1 = niterator.next();
            Object obj2 = piterator.next();
            if (obj1 instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<Object, Object> map1 = (Map<Object, Object>) obj1;
                @SuppressWarnings("unchecked")
                Map<Object, Object> map2 = (Map<Object, Object>) obj2;
                if (!map1.keySet().equals(map2.keySet())) {
                    throw new AssertionFailedError("Keyset: " + map1.keySet() + "; but was: "
                            + map2.keySet());
                }
                for (Iterator<Object> iterator = map1.keySet().iterator(); iterator.hasNext();) {
                    Object key = iterator.next();
                    Object o1 = map1.get(key);
                    Object o2 = map2.get(key);
                    if (!o1.equals(o2)) {
                        throw new AssertionFailedError("Values: " + o1 + "; but was: " + o2);
                    }
                }
            }
            if (!obj1.equals(obj2)) {
                throw new AssertionFailedError("Expected: " + obj1 + "; but was: " + obj2);
            }
        }
        if (!parsedNativeData.equals(nativeData)) {
            throw new AssertionFailedError("Generated presentation is not the same: "
                    + presentation);
        }
    }

    public List<Object> getNativeData() {
        return nativeData;
    }
}
