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
package org.yaml.snakeyaml.serialization;

import static org.junit.Assert.*;

import java.beans.Transient;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class TransientValuesTest {

    //This test expects that transient fields and properties do not get (de)serialized,
    //while non-transient fields and properties do get (de)serialized.
    @Test
    public void testDumpTransientFieldsAndProperties() throws Exception {
        ClassWithTransientFields entity = new ClassWithTransientFields();
        entity.setNonTransientProperty("alpha");
        entity.setTransientProperty("beta");
        entity.transientField = "gamma";
        entity.nonTransientField = "delta";

        Yaml yaml = new Yaml();
        String dumpedInstance = yaml.dump(entity);
        yaml = new Yaml(new Constructor(ClassWithTransientFields.class.getName()));
        ClassWithTransientFields deserializedEntity = yaml.load(dumpedInstance);

        assertTrue(dumpedInstance.contains("alpha"));
        assertTrue(dumpedInstance.contains("delta"));
        assertFalse(dumpedInstance.contains("gamma"));
        assertFalse(dumpedInstance.contains("beta"));

        assertEquals("delta", deserializedEntity.nonTransientField);
        assertEquals("alpha", deserializedEntity.getNonTransientProperty());
        assertNull(deserializedEntity.transientField);
        assertNull(deserializedEntity.getTransientProperty());

    }

    public static class ClassWithTransientFields {

        public String nonTransientField;
        public transient String transientField;

        private String nonTransientProperty;
        private String transientProperty;

        public String getNonTransientProperty() {
            return nonTransientProperty;
        }

        @Transient
        public String getTransientProperty() {
            return transientProperty;
        }

        public void setNonTransientProperty(String nonTransientProperty) {
            this.nonTransientProperty = nonTransientProperty;
        }

        @Transient
        public void setTransientProperty(String transientProperty) {
            this.transientProperty = transientProperty;
        }

    }

}
