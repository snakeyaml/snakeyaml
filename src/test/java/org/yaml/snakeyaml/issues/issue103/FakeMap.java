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
package org.yaml.snakeyaml.issues.issue103;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Implements map interface, but behaves like collection. It just collects
 * whatever you put(...) in here. Needed to track duplications in merge
 * procedure.
 * 
 * @see issue100 & issue103
 */
public class FakeMap<K, V> implements Map<K, V> {

    class FakeEntry implements java.util.Map.Entry<K, V> {

        private final K key;
        private V val;

        public FakeEntry(K key, V val) {
            this.key = key;
            this.val = val;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return val;
        }

        public V setValue(V newV) {
            V old = val;
            val = newV;
            return old;
        }

    }

    ArrayList<java.util.Map.Entry<K, V>> entries = new ArrayList<Map.Entry<K, V>>();

    public void clear() {
        entries.clear();
    }

    public boolean containsKey(Object arg0) {
        for (java.util.Map.Entry<K, V> entry : entries) {
            if (entry.getKey().equals(arg0))
                return true;
        }
        return false;
    }

    public boolean containsValue(Object arg0) {
        for (java.util.Map.Entry<K, V> entry : entries) {
            if (entry.getValue().equals(arg0))
                return true;
        }
        return false;
    }

    public Set<java.util.Map.Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException();
    }

    public V get(Object arg0) {
        for (java.util.Map.Entry<K, V> entry : entries) {
            if (entry.getKey().equals(arg0))
                return entry.getValue();
        }
        return null;
    }

    public boolean isEmpty() {
        return values().isEmpty();
    }

    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    public V put(K key, V val) {
        entries.add(new FakeEntry(key, val));
        return null;
    }

    public void putAll(Map<? extends K, ? extends V> arg0) {
        throw new UnsupportedOperationException();
    }

    public V remove(Object arg0) {
        for (Iterator<java.util.Map.Entry<K, V>> iter = entries.iterator(); iter.hasNext();) {
            java.util.Map.Entry<K, V> entry = iter.next();
            if (entry.getKey().equals(arg0)) {
                iter.remove();
                return entry.getValue();
            }
        }
        return null;
    }

    public int size() {
        return entries.size();
    }

    public Collection<V> values() {
        throw new UnsupportedOperationException();
    }
}
