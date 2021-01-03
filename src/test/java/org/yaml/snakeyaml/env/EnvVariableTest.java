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
package org.yaml.snakeyaml.env;

import junit.framework.TestCase;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.MissingEnvironmentVariableException;

import java.util.Map;

public class EnvVariableTest extends TestCase {
    // the variables EnvironmentKey1 and EnvironmentEmpty are set by Maven
    private static final String KEY1 = "EnvironmentKey1";
    private static final String EMPTY = "EnvironmentEmpty";
    private static final String VALUE1 = "EnvironmentValue1";

    private String load(String template) {
        Yaml yaml = new Yaml(new EnvScalarConstructor());
        yaml.addImplicitResolver(EnvScalarConstructor.ENV_TAG, EnvFormatTest.ENV_FORMAT, "$");
        String loaded = yaml.load(template);
        return loaded;
    }

    public void testEnvironmentSet() {
        assertEquals("Surefire plugin must set the variable.", VALUE1, System.getenv(KEY1));
        assertEquals("Surefire plugin must set the variable.", "", System.getenv(EMPTY));
    }

    public void testEnvConstructor() {
        assertEquals(VALUE1, load("${EnvironmentKey1}"));
        assertEquals(VALUE1, load("${EnvironmentKey1-any}"));
        assertEquals(VALUE1, load("${EnvironmentKey1:-any}"));
        assertEquals(VALUE1, load("${EnvironmentKey1:?any}"));
        assertEquals(VALUE1, load("${EnvironmentKey1?any}"));
    }

    public void testEnvConstructorForEmpty() {
        assertEquals("", load("${EnvironmentEmpty}"));
        assertEquals("", load("${EnvironmentEmpty?}"));
        assertEquals("detected", load("${EnvironmentEmpty:-detected}"));
        assertEquals("", load("${EnvironmentEmpty-detected}"));
        assertEquals("", load("${EnvironmentEmpty?detectedError}"));
        try {
            load("${EnvironmentEmpty:?detectedError}");
        } catch (MissingEnvironmentVariableException e) {
            assertEquals("Empty mandatory variable EnvironmentEmpty: detectedError", e.getMessage());
        }
    }

    public void testEnvConstructorForUnset() {
        assertEquals("", load("${EnvironmentUnset}"));
        assertEquals("", load("${EnvironmentUnset:- }"));
        assertEquals("detected", load("${EnvironmentUnset:-detected}"));
        assertEquals("detected", load("${EnvironmentUnset-detected}"));
        try {
            load("${EnvironmentUnset:?detectedError}");
        } catch (MissingEnvironmentVariableException e) {
            assertEquals("Missing mandatory variable EnvironmentUnset: detectedError", e.getMessage());
        }
        try {
            load("${EnvironmentUnset?detectedError}");
        } catch (MissingEnvironmentVariableException e) {
            assertEquals("Missing mandatory variable EnvironmentUnset: detectedError", e.getMessage());
        }
    }

    public void testDockerCompose() {
        Yaml yaml = new Yaml(new EnvScalarConstructor());
        yaml.addImplicitResolver(EnvScalarConstructor.ENV_TAG, EnvFormatTest.ENV_FORMAT, "$");
        String resource = Util.getLocalResource("env/docker-compose.yaml");
        Map<String, Object> compose = yaml.load(resource);
        String output = compose.toString();
        assertTrue(output, output.endsWith("environment={URL1=EnvironmentValue1, URL2=, URL3=server3, URL4=, URL5=server5, URL6=server6}}}}"));
    }

    public void testIssue493() {
        Yaml yaml = new Yaml(new EnvScalarConstructor());
        yaml.addImplicitResolver(EnvScalarConstructor.ENV_TAG, EnvFormatTest.ENV_FORMAT, "$");
        String resource = Util.getLocalResource("env/env-493.yaml");
        Map<String, Object> compose = yaml.load(resource);
        String output = compose.toString();
        assertEquals("{database={url=jdbc:postgresql://localhost:5432/server493, user=user493, password=password493}}", output);
    }
}
