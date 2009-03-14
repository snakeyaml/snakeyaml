/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.pyyaml;

import java.io.IOException;
import java.util.Iterator;

import org.yaml.snakeyaml.Loader;
import org.yaml.snakeyaml.composer.Composer;
import org.yaml.snakeyaml.error.YAMLException;

public class CanonicalLoader extends Loader {
    @Override
    public Object load(java.io.Reader yaml) {
        try {
            int ch = yaml.read();
            StringBuffer buffer = new StringBuffer();
            while (ch != -1) {
                buffer.append((char) ch);
                ch = yaml.read();
            }
            Composer composer = new Composer(new CanonicalParser(buffer.toString()), resolver);
            constructor.setComposer(composer);
            return constructor.getSingleData();
        } catch (IOException e) {
            throw new YAMLException(e);
        }
    }

    public Iterable<Object> loadAll(java.io.Reader yaml) {
        try {
            int ch = yaml.read();
            StringBuffer buffer = new StringBuffer();
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
