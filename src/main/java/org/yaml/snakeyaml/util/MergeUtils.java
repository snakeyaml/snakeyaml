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
package org.yaml.snakeyaml.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.Tag;

public abstract class MergeUtils {

  abstract public MappingNode asMappingNode(Node node);

  public List<NodeTuple> flatten(MappingNode node) {
    List<NodeTuple> original = node.getValue();
    Set<String> keys = new HashSet<>(original.size());
    List<NodeTuple> updated = new ArrayList<>(original.size());
    List<NodeTuple> merges = new ArrayList<>(2);

    for (NodeTuple tuple : original) {
      Node keyNode = tuple.getKeyNode();
      if (keyNode.getTag().equals(Tag.MERGE)) {
        merges.add(tuple);
      } else {
        updated.add(tuple);
        if (keyNode instanceof ScalarNode) {
          ScalarNode sNode = (ScalarNode) keyNode;
          keys.add(sNode.getValue());
        }
      }
    }

    for (NodeTuple tuple : merges) {
      Node valueNode = tuple.getValueNode();
      if (valueNode instanceof SequenceNode) {
        SequenceNode seqNode = (SequenceNode) valueNode;
        for (Node ref : seqNode.getValue()) {
          MappingNode mergable = asMappingNode(ref);
          Tuple<List<NodeTuple>, Set<String>> filtered = filter(mergable.getValue(), keys);
          updated.addAll(filtered._1());
          keys.addAll(filtered._2());
        }
      } else {
        MappingNode mergable = asMappingNode(valueNode);
        Tuple<List<NodeTuple>, Set<String>> filtered = filter(mergable.getValue(), keys);
        updated.addAll(filtered._1());
        keys.addAll(filtered._2());
      }
    }
    return updated;
  }

  private Tuple<List<NodeTuple>, Set<String>> filter(List<NodeTuple> mergables,
      Set<String> filter) {
    int size = mergables.size();
    Set<String> keys = new HashSet<>(size);
    List<NodeTuple> result = new ArrayList<>(size);
    for (NodeTuple tuple : mergables) {
      Node key = tuple.getKeyNode();
      if (key instanceof ScalarNode) {
        ScalarNode sNode = (ScalarNode) key;
        String nodeValue = sNode.getValue();
        if (!filter.contains(nodeValue)) {
          result.add(tuple);
          keys.add(nodeValue);
        }
      } else {
        result.add(tuple);
      }
    }
    return new Tuple<>(result, keys);
  }

}
