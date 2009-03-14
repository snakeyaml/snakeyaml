/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.types;

import java.util.Map;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;

public abstract class AbstractTest extends TestCase {
    @SuppressWarnings("unchecked")
    protected Map<String, Object> getMap(String data) {
        Yaml yaml = new Yaml();
        Map<String, Object> nativeData = (Map<String, Object>) yaml.load(data);
        return nativeData;
    }

    protected Object load(String data) {
        Yaml yaml = new Yaml();
        Object obj = yaml.load(data);
        return obj;
    }

    protected String dump(Object data) {
        Yaml yaml = new Yaml();
        return yaml.dump(data);
    }

    @SuppressWarnings("unchecked")
    protected Object getMapValue(String data, String key) {
        Map nativeData = getMap(data);
        return nativeData.get(key);
    }
}
