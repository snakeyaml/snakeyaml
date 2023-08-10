/**
 * Copyright (c) 2008, SnakeYAML
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
module org.yaml.snakeyaml {
    /**
     * Required when using classes in the org.yaml.snakeyaml.introspector package.
     */
    requires static java.desktop;
    /**
     * Required when serializing or deserializing instances of {@link java.sql.Date}
     */
    requires static java.sql;

    exports org.yaml.snakeyaml;
    exports org.yaml.snakeyaml.comments;
    exports org.yaml.snakeyaml.composer;
    exports org.yaml.snakeyaml.constructor;
    exports org.yaml.snakeyaml.emitter;
    exports org.yaml.snakeyaml.env;
    exports org.yaml.snakeyaml.error;
    exports org.yaml.snakeyaml.events;
    exports org.yaml.snakeyaml.extensions.compactnotation;
    exports org.yaml.snakeyaml.inspector;
    exports org.yaml.snakeyaml.introspector;
    exports org.yaml.snakeyaml.nodes;
    exports org.yaml.snakeyaml.parser;
    exports org.yaml.snakeyaml.reader;
    exports org.yaml.snakeyaml.representer;
    exports org.yaml.snakeyaml.resolver;
    exports org.yaml.snakeyaml.scanner;
    exports org.yaml.snakeyaml.serializer;
    exports org.yaml.snakeyaml.tokens;
    exports org.yaml.snakeyaml.util;
}
