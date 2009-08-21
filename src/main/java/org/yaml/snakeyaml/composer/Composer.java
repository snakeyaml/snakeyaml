/**
 * Copyright (c) 2008-2009 Andrey Somov
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
package org.yaml.snakeyaml.composer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.yaml.snakeyaml.events.AliasEvent;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.MappingEndEvent;
import org.yaml.snakeyaml.events.MappingStartEvent;
import org.yaml.snakeyaml.events.NodeEvent;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.SequenceEndEvent;
import org.yaml.snakeyaml.events.SequenceStartEvent;
import org.yaml.snakeyaml.events.StreamEndEvent;
import org.yaml.snakeyaml.events.StreamStartEvent;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.parser.Parser;
import org.yaml.snakeyaml.resolver.Resolver;

/**
 * @see <a href="http://pyyaml.org/wiki/PyYAML">PyYAML</a> for more information
 */
public class Composer {
    private final Parser parser;
    private final Resolver resolver;
    private final Map<String, Node> anchors;
    private final Set<Node> recursiveNodes;

    public Composer(Parser parser, Resolver resolver) {
        this.parser = parser;
        this.resolver = resolver;
        this.anchors = new HashMap<String, Node>();
        this.recursiveNodes = new HashSet<Node>();
    }

    public boolean checkNode() {
        // Drop the STREAM-START event.
        if (parser.checkEvent(StreamStartEvent.class)) {
            parser.getEvent();
        }
        // If there are more documents available?
        return !parser.checkEvent(StreamEndEvent.class);
    }

    public Node getNode() {
        // Get the root node of the next document.
        if (!parser.checkEvent(StreamEndEvent.class)) {
            return composeDocument();
        } else {
            return (Node) null;
        }
    }

    public Node getSingleNode() {
        // Drop the STREAM-START event.
        parser.getEvent();
        // Compose a document if the stream is not empty.
        Node document = null;
        if (!parser.checkEvent(StreamEndEvent.class)) {
            document = composeDocument();
        }
        // Ensure that the stream contains no more documents.
        if (!parser.checkEvent(StreamEndEvent.class)) {
            Event event = parser.getEvent();
            throw new ComposerException("expected a single document in the stream", document
                    .getStartMark(), "but found another document", event.getStartMark());
        }
        // Drop the STREAM-END event.
        parser.getEvent();
        return document;
    }

    private Node composeDocument() {
        // Drop the DOCUMENT-START event.
        parser.getEvent();
        // Compose the root node.
        Node node = composeNode(null, null);
        // Drop the DOCUMENT-END event.
        parser.getEvent();
        this.anchors.clear();
        recursiveNodes.clear();
        return node;
    }

    private Node composeNode(Node parent, Object index) {
        recursiveNodes.add(parent);
        if (parser.checkEvent(AliasEvent.class)) {
            AliasEvent event = (AliasEvent) parser.getEvent();
            String anchor = event.getAnchor();
            if (!anchors.containsKey(anchor)) {
                throw new ComposerException(null, null, "found undefined alias " + anchor, event
                        .getStartMark());
            }
            Node result = (Node) anchors.get(anchor);
            if (recursiveNodes.remove(result)) {
                result.setTwoStepsConstruction(true);
            }
            return result;
        }
        NodeEvent event = (NodeEvent) parser.peekEvent();
        String anchor = null;
        anchor = event.getAnchor();
        if (anchor != null && anchors.containsKey(anchor)) {
            throw new ComposerException("found duplicate anchor " + anchor + "; first occurence",
                    this.anchors.get(anchor).getStartMark(), "second occurence", event
                            .getStartMark());
        }
        // resolver.descendResolver(parent, index);
        Node node = null;
        if (parser.checkEvent(ScalarEvent.class)) {
            node = composeScalarNode(anchor);
        } else if (parser.checkEvent(SequenceStartEvent.class)) {
            node = composeSequenceNode(anchor);
        } else {
            node = composeMappingNode(anchor);
        }
        // resolver.ascendResolver();
        recursiveNodes.remove(parent);
        return node;
    }

    private Node composeScalarNode(String anchor) {
        ScalarEvent ev = (ScalarEvent) parser.getEvent();
        String tag = ev.getTag();
        boolean explicitTag = true;
        if (tag == null || tag.equals("!")) {
            tag = resolver.resolve(NodeId.scalar, ev.getValue(), ev.getImplicit().isFirst());
            explicitTag = false;
        }
        Node node = new ScalarNode(tag, explicitTag, ev.getValue(), ev.getStartMark(), ev
                .getEndMark(), ev.getStyle());
        if (anchor != null) {
            anchors.put(anchor, node);
        }
        return node;
    }

    private Node composeSequenceNode(String anchor) {
        SequenceStartEvent startEvent = (SequenceStartEvent) parser.getEvent();
        String tag = startEvent.getTag();
        if (tag == null || tag.equals("!")) {
            tag = resolver.resolve(NodeId.sequence, null, startEvent.getImplicit());
        }
        SequenceNode node = new SequenceNode(tag, new ArrayList<Node>(), startEvent.getStartMark(),
                null, startEvent.getFlowStyle());
        if (anchor != null) {
            anchors.put(anchor, node);
        }
        int index = 0;
        while (!parser.checkEvent(SequenceEndEvent.class)) {
            (node.getValue()).add(composeNode(node, new Integer(index)));
            index++;
        }
        Event endEvent = parser.getEvent();
        node.setEndMark(endEvent.getEndMark());
        return node;
    }

    private Node composeMappingNode(String anchor) {
        MappingStartEvent startEvent = (MappingStartEvent) parser.getEvent();
        String tag = startEvent.getTag();
        if (tag == null || tag.equals("!")) {
            tag = resolver.resolve(NodeId.mapping, null, startEvent.getImplicit());
        }
        MappingNode node = new MappingNode(tag, new ArrayList<NodeTuple>(), startEvent
                .getStartMark(), null, startEvent.getFlowStyle());
        if (anchor != null) {
            anchors.put(anchor, node);
        }
        while (!parser.checkEvent(MappingEndEvent.class)) {
            Node itemKey = composeNode(node, null);
            Node itemValue = composeNode(node, itemKey);
            node.getValue().add(new NodeTuple(itemKey, itemValue));
        }
        Event endEvent = parser.getEvent();
        node.setEndMark(endEvent.getEndMark());
        return node;
    }
}
