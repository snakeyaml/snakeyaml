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
package org.yaml.snakeyaml.env;

import junit.framework.TestCase;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;

import static org.yaml.snakeyaml.env.EnvScalarConstructor.ENV_FORMAT;
import static org.yaml.snakeyaml.env.EnvScalarConstructor.ENV_TAG;

public class EnvLombokTest extends TestCase {
    public void testEnvConstructor() {
        Yaml yaml = new Yaml(new EnvScalarConstructor(new TypeDescription(ApplicationProperties.class), new ArrayList<TypeDescription>(), new LoaderOptions()));
        yaml.addImplicitResolver(ENV_TAG, ENV_FORMAT, "$");
        String yamlData = "kafkaBrokers: ${KAFKA_URL:-kafka:9092}\n" +
                "kafkaGroupIdConfig: keycloak_group_id\n" +
                "workers: 17\n" +
                "kafkaTopicName: ${TOPIC_NAME:-keycloakTestTopic}\n";
        ApplicationProperties props = yaml.load(yamlData);
        assertEquals("kafka:9092", props.getKafkaBrokers());
        assertEquals("keycloakTestTopic", props.getKafkaTopicName());
        assertEquals("keycloak_group_id", props.getKafkaGroupIdConfig());
        assertEquals(Integer.valueOf(17), props.getWorkers());
    }
}
