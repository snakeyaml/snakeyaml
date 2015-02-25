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
package org.pyyaml;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.composer.Composer;
import org.yaml.snakeyaml.error.YAMLException;

public class CanonicalLoader extends Yaml {
    @Override
    public Object load(Reader yaml) {
        try {
            int ch = yaml.read();
            StringBuilder buffer = new StringBuilder();
            while (ch != -1) {
                buffer.append((char) ch);
                ch = yaml.read();
            }
            Composer composer = new Composer(new CanonicalParser(buffer.toString()), resolver);
            constructor.setComposer(composer);
            return constructor.getSingleData(Object.class);
        } catch (IOException e) {
            throw new YAMLException(e);
        }
    }

    public Iterable<Object> loadAll(Reader yaml) {
        try {
            int ch = yaml.read();
            StringBuilder buffer = new StringBuilder();
            while (ch != -1) {
                buffer.append((char) ch);
                ch = yaml.read();
            }
            Composer composer = new Composer(new CanonicalParser(buffer.toString()), resolver);
            this.constructor.setComposer(composer);
            Iterator<Object> result = new Iterator<Object>() {
                public boolean hasNext() {
                    return constructor.checkData();
                }

                public Object next() {
                    return constructor.getData();
                }

                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
            return new YamlIterable(result);
        } catch (IOException e) {
            throw new YAMLException(e);
        }
    }

    private class YamlIterable implements Iterable<Object> {
        private Iterator<Object> iterator;

        public YamlIterable(Iterator<Object> iterator) {
            this.iterator = iterator;
        }

        public Iterator<Object> iterator() {
            return iterator;
        }

    }
}
