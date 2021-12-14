/**
 * Copyright (c) 2008, SnakeYAML
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
package org.yaml.snakeyaml.issues.issue337;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

public class DuplicateKeyTest {

    public static class MapProvider<K, V> {
        private Map<K, V> map = new LinkedHashMap<K, V>();

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Map<K, V> getMap() {
            return map;
        }

        public void setMap(Map<K, V> map) {
            this.map = map;
        }

        @SuppressWarnings("rawtypes")
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof MapProvider) {
                return map.equals(((MapProvider) obj).getMap());
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return map.hashCode();
        }
    }

    // test guff
    public static class FooEntry {
        private String id;
        private String url;

        public FooEntry() {
        }

        public FooEntry(String id, String url) {
            this.id = id;
            this.url = url;
        }

        public String getUrl() {
            return this.url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getId() {
            return this.id;
        }

        public void setId(String id) {
            this.id = id;
        }

    }

    @Test
    public void defaultConfigurationNoErrorsWithDuplicates() {
        String input = Util.getLocalResource("issues/issue337-duplicate-keys.yaml");
        Yaml yaml = new Yaml();
        MapProvider<String, FooEntry> testdata = yaml.loadAs(input, MapProvider.class);
        assertEquals("has-dup-keys", testdata.getName().toString());
        assertEquals(1, testdata.getMap().size());
        assertEquals("daaf8911-36e4-4e92-86ea-eb77ac2c1e91", testdata.getMap().get("someitem").getId().toString());
    }

    @Test
    public void errorOnDuplicateKeys() {
        String input = Util.getLocalResource("issues/issue337-duplicate-keys.yaml");
        LoaderOptions lc = new LoaderOptions();
        lc.setAllowDuplicateKeys(false);
        Yaml yaml = new Yaml(lc);
        try {
            yaml.loadAs(input, MapProvider.class);
        } catch (DuplicateKeyException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("found duplicate key someitem"));
            assertTrue(e.getMessage(), e.getMessage().contains("line 3, column 3"));
        }
    }

    @Test
    public void errorOnDuplicateKeysInJavaBeanProperty() {
        String input = Util
                .getLocalResource("issues/issue337-duplicate-keys-javabean-property.yaml");
        LoaderOptions lc = new LoaderOptions();
        lc.setAllowDuplicateKeys(false);
        Yaml yaml = new Yaml(lc);
        try {
            MapProvider<String, FooEntry> testdata = yaml.loadAs(input, MapProvider.class);
            assertEquals("has-dup-keys", testdata.getName());
        } catch (DuplicateKeyException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("found duplicate key name"));
            assertTrue(e.getMessage(), e.getMessage().contains("line 9, column 1"));
        }
    }

    @Test
    public void acceptDuplicateKeysInJavaBeanProperty() {
        String input = Util
                .getLocalResource("issues/issue337-duplicate-keys-javabean-property.yaml");
        LoaderOptions lc = new LoaderOptions();
        lc.setAllowDuplicateKeys(true);
        Yaml yaml = new Yaml(lc);
        MapProvider<String, FooEntry> testdata = yaml.loadAs(input, MapProvider.class);
        assertEquals("has-dup-keys", testdata.getName());
    }

    @Test
    public void defaultConfigUniqueKeysWorks() {
        String input = Util.getLocalResource("issues/issue337-duplicate-keys-no-dups.yaml");
        Yaml yaml = new Yaml();
        MapProvider<String, FooEntry> testdata = yaml.loadAs(input, MapProvider.class);
        assertEquals("no-dups-test", testdata.getName().toString());
        assertEquals(3, testdata.getMap().size());
        assertEquals("aead4b16-4b61-4eff-b241-6eff26eaa778", testdata.getMap().get("someitem1").getId().toString());
        assertEquals("daaf8911-36e4-4e92-86ea-eb77ac2c1e91", testdata.getMap().get("someitem3").getId().toString());
    }

    @Test
    public void noDuplicatesConfigMutablePostChange() {
        String input = Util.getLocalResource("issues/issue337-duplicate-keys-no-dups.yaml");
        LoaderOptions lc = new LoaderOptions();
        lc.setAllowDuplicateKeys(false);
        Yaml yaml = new Yaml(lc);
        MapProvider<String, FooEntry> testdata = yaml.loadAs(input, MapProvider.class);
        assertEquals("no-dups-test", testdata.getName().toString());
        assertEquals(3, testdata.getMap().size());
        assertEquals("aead4b16-4b61-4eff-b241-6eff26eaa778", testdata.getMap().get("someitem1").getId().toString());
        assertEquals("daaf8911-36e4-4e92-86ea-eb77ac2c1e91", testdata.getMap().get("someitem3").getId().toString());
        testdata.getMap().put("someitem1", new FooEntry("AnotherEntry", "AnotherURL"));
    }

}
