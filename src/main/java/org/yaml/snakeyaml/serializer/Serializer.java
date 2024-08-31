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
package org.yaml.snakeyaml.serializer;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.Version;
import org.yaml.snakeyaml.comments.CommentLine;
import org.yaml.snakeyaml.emitter.Emitable;
import org.yaml.snakeyaml.events.AliasEvent;
import org.yaml.snakeyaml.events.CommentEvent;
import org.yaml.snakeyaml.events.DocumentEndEvent;
import org.yaml.snakeyaml.events.DocumentStartEvent;
import org.yaml.snakeyaml.events.ImplicitTuple;
import org.yaml.snakeyaml.events.MappingEndEvent;
import org.yaml.snakeyaml.events.MappingStartEvent;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.SequenceEndEvent;
import org.yaml.snakeyaml.events.SequenceStartEvent;
import org.yaml.snakeyaml.events.StreamEndEvent;
import org.yaml.snakeyaml.events.StreamStartEvent;
import org.yaml.snakeyaml.nodes.AnchorNode;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.resolver.Resolver;
import org.yaml.snakeyaml.util.MergeUtils;

public final class Serializer {

  private final Emitable emitter;
  private final Resolver resolver;
  private final boolean explicitStart;
  private final boolean explicitEnd;
  private Version useVersion;
  private final Map<String, String> useTags;
  private final Set<Node> serializedNodes;
  private final Map<Node, String> anchors;
  private final AnchorGenerator anchorGenerator;
  private Boolean closed;
  private final Tag explicitRoot;
  private final boolean dereferenceAliases;
  private final Set<Node> recursive;

  private final MergeUtils mergeUtils;

  public Serializer(Emitable emitter, Resolver resolver, DumperOptions opts, Tag rootTag) {
    if (emitter == null) {
      throw new NullPointerException("Emitter must  be provided");
    }
    if (resolver == null) {
      throw new NullPointerException("Resolver must  be provided");
    }
    if (opts == null) {
      throw new NullPointerException("DumperOptions must  be provided");
    }
    this.emitter = emitter;
    this.resolver = resolver;
    this.explicitStart = opts.isExplicitStart();
    this.explicitEnd = opts.isExplicitEnd();
    if (opts.getVersion() != null) {
      this.useVersion = opts.getVersion();
    }
    this.useTags = opts.getTags();
    this.serializedNodes = new HashSet<Node>();
    this.anchors = new HashMap<Node, String>();
    this.anchorGenerator = opts.getAnchorGenerator();
    this.dereferenceAliases = opts.isDereferenceAliases();
    this.recursive = Collections.newSetFromMap(new IdentityHashMap<Node, Boolean>());
    this.closed = null;
    this.explicitRoot = rootTag;

    mergeUtils = new MergeUtils() {
      public MappingNode asMappingNode(Node node) {
        if (node instanceof MappingNode) {
          return (MappingNode) node;
        }
        // TODO: This need to be explored more to understand if only MappingNode possible.
        // Or at least the error message needs to be improved.
        throw new SerializerException("expecting MappingNode while processing merge.");
      }
    };
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
      // release unused resources
      this.serializedNodes.clear();
      this.anchors.clear();
      this.recursive.clear();
    }
  }

  public void serialize(Node node) throws IOException {
    if (closed == null) {
      throw new SerializerException("serializer is not opened");
    } else if (closed) {
      throw new SerializerException("serializer is closed");
    }
    this.emitter
        .emit(new DocumentStartEvent(null, null, this.explicitStart, this.useVersion, useTags));
    anchorNode(node);
    if (explicitRoot != null) {
      node.setTag(explicitRoot);
    }
    serializeNode(node, null);
    this.emitter.emit(new DocumentEndEvent(null, null, this.explicitEnd));
    this.serializedNodes.clear();
    this.anchors.clear();
    this.recursive.clear();
  }

  private void anchorNode(Node node) {
    if (node.getNodeId() == NodeId.anchor) {
      node = ((AnchorNode) node).getRealNode();
    }
    if (this.anchors.containsKey(node)) {
      String anchor = this.anchors.get(node);
      if (null == anchor) {
        anchor = this.anchorGenerator.nextAnchor(node);
        this.anchors.put(node, anchor);
      }
    } else {
      this.anchors.put(node,
          node.getAnchor() != null ? this.anchorGenerator.nextAnchor(node) : null);
      switch (node.getNodeId()) {
        case sequence:
          SequenceNode seqNode = (SequenceNode) node;
          List<Node> list = seqNode.getValue();
          for (Node item : list) {
            anchorNode(item);
          }
          break;
        case mapping:
          MappingNode mnode = (MappingNode) node;
          List<NodeTuple> map = mnode.getValue();
          for (NodeTuple object : map) {
            Node key = object.getKeyNode();
            Node value = object.getValueNode();
            anchorNode(key);
            anchorNode(value);
          }
          break;
      }
    }
  }

  // parent Node is not used but might be used in the future
  private void serializeNode(Node node, Node parent) throws IOException {
    if (node.getNodeId() == NodeId.anchor) {
      node = ((AnchorNode) node).getRealNode();
    }
    if (dereferenceAliases && recursive.contains(node)) {
      throw new SerializerException("Cannot dereferenceAliases for recursive structures.");
    }
    recursive.add(node);
    String tAlias = !dereferenceAliases ? this.anchors.get(node) : null;
    if (!dereferenceAliases && this.serializedNodes.contains(node)) {
      this.emitter.emit(new AliasEvent(tAlias, null, null));
    } else {
      this.serializedNodes.add(node);
      switch (node.getNodeId()) {
        case scalar:
          ScalarNode scalarNode = (ScalarNode) node;
          serializeComments(node.getBlockComments());
          Tag detectedTag = this.resolver.resolve(NodeId.scalar, scalarNode.getValue(), true);
          Tag defaultTag = this.resolver.resolve(NodeId.scalar, scalarNode.getValue(), false);
          ImplicitTuple tuple = new ImplicitTuple(node.getTag().equals(detectedTag),
              node.getTag().equals(defaultTag));
          ScalarEvent event = new ScalarEvent(tAlias, node.getTag().getValue(), tuple,
              scalarNode.getValue(), null, null, scalarNode.getScalarStyle());
          this.emitter.emit(event);
          serializeComments(node.getInLineComments());
          serializeComments(node.getEndComments());
          break;
        case sequence:
          SequenceNode seqNode = (SequenceNode) node;
          serializeComments(node.getBlockComments());
          boolean implicitS =
              node.getTag().equals(this.resolver.resolve(NodeId.sequence, null, true));
          this.emitter.emit(new SequenceStartEvent(tAlias, node.getTag().getValue(), implicitS,
              null, null, seqNode.getFlowStyle()));
          List<Node> list = seqNode.getValue();
          for (Node item : list) {
            serializeNode(item, node);
          }
          this.emitter.emit(new SequenceEndEvent(null, null));
          serializeComments(node.getInLineComments());
          serializeComments(node.getEndComments());
          break;
        default:// instance of MappingNode
          serializeComments(node.getBlockComments());
          if (node.getTag() != Tag.COMMENT) {
            Tag implicitTag = this.resolver.resolve(NodeId.mapping, null, true);
            boolean implicitM = node.getTag().equals(implicitTag);
            MappingNode mnode = (MappingNode) node;
            List<NodeTuple> map = mnode.getValue();

            if (this.dereferenceAliases && mnode.isMerged()) {
              map = mergeUtils.flatten(mnode);
            }

            this.emitter.emit(new MappingStartEvent(tAlias, mnode.getTag().getValue(), implicitM,
                null, null, mnode.getFlowStyle()));
            for (NodeTuple row : map) {
              Node key = row.getKeyNode();
              Node value = row.getValueNode();
              serializeNode(key, mnode);
              serializeNode(value, mnode);
            }
            this.emitter.emit(new MappingEndEvent(null, null));
            serializeComments(node.getInLineComments());
            serializeComments(node.getEndComments());
          }
      }
    }
    recursive.remove(node);
  }

  private void serializeComments(List<CommentLine> comments) throws IOException {
    if (comments == null) {
      return;
    }
    for (CommentLine line : comments) {
      CommentEvent commentEvent = new CommentEvent(line.getCommentType(), line.getValue(),
          line.getStartMark(), line.getEndMark());
      this.emitter.emit(commentEvent);
    }
  }
}
