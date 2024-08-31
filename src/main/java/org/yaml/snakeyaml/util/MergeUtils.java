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

/**
 * Utility to process merge keys (https://yaml.org/type/merge.html) of the MappingNode
 */
public abstract class MergeUtils {

  /**
   * Converts the specified {@code node} into a {@link MappingNode}.
   * <p>
   * This method is designed to transform various types of {@link Node} into a {@link MappingNode},
   * enabling further processing such as merging of keys.
   * </p>
   *
   * @param node The node to be transformed.
   * @return A {@link MappingNode} representation of the input {@code node}.
   */
  abstract public MappingNode asMappingNode(Node node);

  /**
   * Processes and resolves merge keys in a {@link MappingNode}, merging resolved key/values into
   * the node.
   * <p>
   * Implements the YAML merge key feature by examining the nodes within the provided {@code node}
   * and merging keys from referenced by "merge key" map(s) into the current mapping as per the YAML
   * specification. Handling of duplicate keys is defined by the order of appearance in the mapping
   * node, with priority given to the keys defined in {@code node} and the the earliest occurrences
   * in the merging ones.
   * </p>
   *
   * @param node The MappingNode to process for merge keys.
   * @return A list of {@link NodeTuple} containing the merged keys and values.
   * @see <a href="https://yaml.org/type/merge.html">YAML Merge Key Specification</a>
   */
  public List<NodeTuple> flatten(MappingNode node) {
    List<NodeTuple> toProcess = node.getValue();
    List<NodeTuple> result = toProcess;
    boolean process = true;
    while (process) {
      process = false;
      List<NodeTuple> updated = new ArrayList<>(toProcess.size());
      Set<String> keys = new HashSet<>(toProcess.size());
      List<NodeTuple> merges = new ArrayList<>(2);
      for (NodeTuple tuple : toProcess) {
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
            process = process || mergable.isMerged();
            Tuple<List<NodeTuple>, Set<String>> filtered = filter(mergable.getValue(), keys);
            updated.addAll(filtered._1());
            keys.addAll(filtered._2());
          }
        } else {
          MappingNode mergable = asMappingNode(valueNode);
          process = process || mergable.isMerged();
          Tuple<List<NodeTuple>, Set<String>> filtered = filter(mergable.getValue(), keys);
          updated.addAll(filtered._1());
          keys.addAll(filtered._2());
        }
      }
      result = updated;
      if (process) {
        toProcess = updated;
      }
    }
    return result;
  }

  /**
   * Filters out {@link NodeTuple}s with {@link ScalarNode} keys that are present in the provided
   * filter set.
   * <p>
   * This utility method supports the {@link #flatten(MappingNode)} method by filtering out node
   * tuples based on their key's presence in a set of strings. This ensures that the returned list
   * of NodeTuples does not contain any keys that are present in the filter set. The set of strings
   * returned alongside the list represents the keys of the NodeTuples in the returned list,
   * facilitating the identification of newly added keys as part of the merge process.
   * </p>
   *
   * @param mergables The list of NodeTuples to process.
   * @param filter A set of string values used as a filter. NodeTuples with keys in this set are
   *        omitted.
   * @return A tuple of a list of filtered NodeTuples and a set containing the keys of the
   *         NodeTuples in the returned list.
   */
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
