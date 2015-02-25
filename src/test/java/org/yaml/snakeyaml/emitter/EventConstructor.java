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
package org.yaml.snakeyaml.emitter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.DumperOptions.Version;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.events.AliasEvent;
import org.yaml.snakeyaml.events.DocumentEndEvent;
import org.yaml.snakeyaml.events.DocumentStartEvent;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.ImplicitTuple;
import org.yaml.snakeyaml.events.MappingEndEvent;
import org.yaml.snakeyaml.events.MappingStartEvent;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.SequenceEndEvent;
import org.yaml.snakeyaml.events.SequenceStartEvent;
import org.yaml.snakeyaml.events.StreamEndEvent;
import org.yaml.snakeyaml.events.StreamStartEvent;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;

public class EventConstructor extends Constructor {

    public EventConstructor() {
        this.yamlConstructors.put(null, new ConstructEvent());
    }

    private class ConstructEvent extends AbstractConstruct {

        @SuppressWarnings("unchecked")
        public Object construct(Node node) {
            Map<Object, Object> mapping;
            if (node instanceof ScalarNode) {
                mapping = new HashMap<Object, Object>();
            } else {
                mapping = constructMapping((MappingNode) node);
            }
            String className = node.getTag().getValue().substring(1) + "Event";
            Event value;
            if (className.equals("AliasEvent")) {
                value = new AliasEvent((String) mapping.get("anchor"), null, null);
            } else if (className.equals("ScalarEvent")) {
                String tag = (String) mapping.get("tag");
                String v = (String) mapping.get("value");
                if (v == null) {
                    v = "";
                }
                List<Boolean> implicitList = (List<Boolean>) mapping.get("implicit");
                ImplicitTuple implicit;
                if (implicitList == null) {
                    implicit = new ImplicitTuple(false, true);
                } else {
                    implicit = new ImplicitTuple((Boolean) implicitList.get(0),
                            (Boolean) implicitList.get(1));
                }
                value = new ScalarEvent((String) mapping.get("anchor"), tag, implicit, v, null,
                        null, null);
            } else if (className.equals("SequenceStartEvent")) {
                String tag = (String) mapping.get("tag");
                Boolean implicit = (Boolean) mapping.get("implicit");
                if (implicit == null) {
                    implicit = true;
                }
                value = new SequenceStartEvent((String) mapping.get("anchor"), tag, implicit, null,
                        null, false);
            } else if (className.equals("MappingStartEvent")) {
                String tag = (String) mapping.get("tag");
                Boolean implicit = (Boolean) mapping.get("implicit");
                if (implicit == null) {
                    implicit = true;
                }
                value = new MappingStartEvent((String) mapping.get("anchor"), tag, implicit, null,
                        null, false);
            } else if (className.equals("DocumentEndEvent")) {
                value = new DocumentEndEvent(null, null, false);
            } else if (className.equals("DocumentStartEvent")) {
                Map<String, String> tags = (Map<String, String>) mapping.get("tags");
                List<Integer> versionList = (List<Integer>) mapping.get("version");
                Version version = null;
                // TODO ???
                if (versionList != null) {
                    Integer major = versionList.get(0).intValue();
                    if (major != 1) {
                        throw new YAMLException("Unsupported version.");
                    }
                    Integer minor = versionList.get(1).intValue();
                    if (minor == 0) {
                        version = Version.V1_0;
                    } else {
                        version = Version.V1_1;
                    }
                }
                value = new DocumentStartEvent(null, null, false, version, tags);
            } else if (className.equals("MappingEndEvent")) {
                value = new MappingEndEvent(null, null);
            } else if (className.equals("SequenceEndEvent")) {
                value = new SequenceEndEvent(null, null);
            } else if (className.equals("StreamEndEvent")) {
                value = new StreamEndEvent(null, null);
            } else if (className.equals("StreamStartEvent")) {
                value = new StreamStartEvent(null, null);
            } else {
                throw new UnsupportedOperationException();
            }
            return value;
        }
    }
}
