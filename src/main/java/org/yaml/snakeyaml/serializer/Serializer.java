/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.serializer;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.emitter.Emitter;
import org.yaml.snakeyaml.events.AliasEvent;
import org.yaml.snakeyaml.events.DocumentEndEvent;
import org.yaml.snakeyaml.events.DocumentStartEvent;
import org.yaml.snakeyaml.events.MappingEndEvent;
import org.yaml.snakeyaml.events.MappingStartEvent;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.SequenceEndEvent;
import org.yaml.snakeyaml.events.SequenceStartEvent;
import org.yaml.snakeyaml.events.StreamEndEvent;
import org.yaml.snakeyaml.events.StreamStartEvent;
import org.yaml.snakeyaml.nodes.CollectionNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.resolver.Resolver;

/**
 * @see <a href="http://pyyaml.org/wiki/PyYAML">PyYAML</a> for more information
 */
public final class Serializer {
    private final Emitter emitter;
    private final Resolver resolver;
    private boolean explicitStart;
    private boolean explicitEnd;
    private Integer[] useVersion;
    private Map<String, String> useTags;
    private Set<Node> serializedNodes;
    private Map<Node, String> anchors;
    private int lastAnchorId;
    private Boolean closed;
    private String explicitRoot;

    public Serializer(Emitter emitter, Resolver resolver, DumperOptions opts) {
        this.emitter = emitter;
        this.resolver = resolver;
        this.explicitStart = opts.isExplicitStart();
        this.explicitEnd = opts.isExplicitEnd();
        if (opts.getVersion() != null) {
            this.useVersion = opts.getVersion().getArray();
        }
        this.useTags = opts.getTags();
        this.serializedNodes = new HashSet<Node>();
        this.anchors = new HashMap<Node, String>();
        this.lastAnchorId = 0;
        this.closed = null;
        this.explicitRoot = opts.getExplicitRoot();
    }

    public void open() throws IOException {
        if (closed == null) {
            this.emitter.emit(new StreamStartEvent(null, null));
            this.closed = Boolean.FALSE;
        } else if (Boolean.TRUE.equals(closed)) {
            throw new SerializerException("serializer is closed");
        } else {
            throw new SerializerException("serializer is already opened");
        }
    }

    public void close() throws IOException {
        if (closed == null) {
            throw new SerializerException("serializer is not opened");
        } else if (!Boolean.TRUE.equals(closed)) {
            this.emitter.emit(new StreamEndEvent(null, null));
            this.closed = Boolean.TRUE;
        }
    }

    public void serialize(Node node) throws IOException {
        if (closed == null) {
            throw new SerializerException("serializer is not opened");
        } else if (closed) {
            throw new SerializerException("serializer is closed");
        }
        this.emitter.emit(new DocumentStartEvent(null, null, this.explicitStart, this.useVersion,
                useTags));
        anchorNode(node);
        if (explicitRoot != null) {
            node.setTag(explicitRoot);
        }
        serializeNode(node, null, null);
        this.emitter.emit(new DocumentEndEvent(null, null, this.explicitEnd));
        this.serializedNodes.clear();
        this.anchors.clear();
        this.lastAnchorId = 0;
    }

    @SuppressWarnings("unchecked")
    private void anchorNode(Node node) {
        if (this.anchors.containsKey(node)) {
            String anchor = this.anchors.get(node);
            if (null == anchor) {
                anchor = generateAnchor();
                this.anchors.put(node, anchor);
            }
        } else {
            this.anchors.put(node, null);
            switch (node.getNodeId()) {
            case sequence:
                List<Node> list = (List<Node>) node.getValue();
                for (Node item : list) {
                    anchorNode(item);
                }
                break;
            case mapping:
                List<Object[]> map = (List<Object[]>) node.getValue();
                for (Object[] object : map) {
                    Node key = (Node) object[0];
                    Node value = (Node) object[1];
                    anchorNode(key);
                    anchorNode(value);
                }
                break;
            }
        }
    }

    private String generateAnchor() {
        this.lastAnchorId++;
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMinimumIntegerDigits(3);
        String anchorId = format.format(this.lastAnchorId);
        return "id" + anchorId;
    }

    @SuppressWarnings("unchecked")
    private void serializeNode(Node node, Node parent, Object index) throws IOException {
        String tAlias = this.anchors.get(node);
        if (this.serializedNodes.contains(node)) {
            this.emitter.emit(new AliasEvent(tAlias, null, null));
        } else {
            this.serializedNodes.add(node);
            // this.resolver.descendResolver(parent, index);
            switch (node.getNodeId()) {
            case scalar:
                String detectedTag = this.resolver.resolve(NodeId.scalar, (String) node.getValue(),
                        true);
                String defaultTag = this.resolver.resolve(NodeId.scalar, (String) node.getValue(),
                        false);
                boolean[] implicit = new boolean[] { false, false };
                implicit[0] = node.getTag().equals(detectedTag);
                implicit[1] = node.getTag().equals(defaultTag);
                ScalarEvent event = new ScalarEvent(tAlias, node.getTag(), implicit, (String) node
                        .getValue(), null, null, ((ScalarNode) node).getStyle());
                this.emitter.emit(event);
                break;
            case sequence:
                boolean implicitS = (node.getTag().equals(this.resolver.resolve(NodeId.sequence,
                        null, true)));
                this.emitter.emit(new SequenceStartEvent(tAlias, node.getTag(), implicitS, null,
                        null, ((CollectionNode) node).getFlowStyle()));
                int indexCounter = 0;
                List<Node> list = (List<Node>) node.getValue();
                for (Node item : list) {
                    serializeNode(item, node, new Integer(indexCounter));
                    indexCounter++;
                }
                this.emitter.emit(new SequenceEndEvent(null, null));
                break;
            default:// instance of MappingNode
                String implicitTag = this.resolver.resolve(NodeId.mapping, null, true);
                boolean implicitM = (node.getTag().equals(implicitTag));
                this.emitter.emit(new MappingStartEvent(tAlias, node.getTag(), implicitM, null,
                        null, ((CollectionNode) node).getFlowStyle()));
                List<Object[]> map = (List<Object[]>) node.getValue();
                for (Object[] row : map) {
                    Node key = (Node) row[0];
                    Node value = (Node) row[1];
                    serializeNode(key, node, null);
                    serializeNode(value, node, key);
                }
                this.emitter.emit(new MappingEndEvent(null, null));
            }
        }
    }
}
