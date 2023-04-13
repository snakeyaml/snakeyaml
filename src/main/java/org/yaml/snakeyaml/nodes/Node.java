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
package org.yaml.snakeyaml.nodes;

import java.util.List;
import org.yaml.snakeyaml.comments.CommentLine;
import org.yaml.snakeyaml.error.Mark;

/**
 * Base class for all nodes.
 * <p>
 * The nodes form the node-graph described in the <a href="http://yaml.org/spec/1.1/">YAML
 * Specification</a>.
 * </p>
 * <p>
 * While loading, the node graph is usually created by the
 * {@link org.yaml.snakeyaml.composer.Composer}, and later transformed into application specific
 * Java classes by the classes from the {@link org.yaml.snakeyaml.constructor} package.
 * </p>
 */
public abstract class Node {

  private Tag tag;
  private final Mark startMark;
  protected Mark endMark;
  private Class<? extends Object> type;
  private boolean twoStepsConstruction;
  private String anchor;
  private List<CommentLine> inLineComments;
  private List<CommentLine> blockComments;
  // End Comments are only on the last node in a document
  private List<CommentLine> endComments;

  /**
   * true when the tag is assigned by the resolver
   */
  protected boolean resolved;
  protected Boolean useClassConstructor;

  public Node(Tag tag, Mark startMark, Mark endMark) {
    setTag(tag);
    this.startMark = startMark;
    this.endMark = endMark;
    this.type = Object.class;
    this.twoStepsConstruction = false;
    this.resolved = true;
    this.useClassConstructor = null;
    this.inLineComments = null;
    this.blockComments = null;
    this.endComments = null;
  }

  /**
   * Tag of this node.
   * <p>
   * Every node has a tag assigned. The tag is either local or global.
   *
   * @return Tag of this node.
   */
  public Tag getTag() {
    return this.tag;
  }

  public Mark getEndMark() {
    return endMark;
  }

  /**
   * For error reporting.
   *
   * @see "class variable 'id' in PyYAML"
   * @return scalar, sequence, mapping
   */
  public abstract NodeId getNodeId();

  public Mark getStartMark() {
    return startMark;
  }

  public void setTag(Tag tag) {
    if (tag == null) {
      throw new NullPointerException("tag in a Node is required.");
    }
    this.tag = tag;
  }

  /**
   * Node is only equal to itself
   */
  @Override
  public final boolean equals(Object obj) {
    return super.equals(obj);
  }

  public Class<? extends Object> getType() {
    return type;
  }

  public void setType(Class<? extends Object> type) {
    if (!type.isAssignableFrom(this.type)) {
      this.type = type;
    }
  }

  public void setTwoStepsConstruction(boolean twoStepsConstruction) {
    this.twoStepsConstruction = twoStepsConstruction;
  }

  /**
   * Indicates if this node must be constructed in two steps.
   * <p>
   * Two-step construction is required whenever a node is a child (direct or indirect) of it self.
   * That is, if a recursive structure is build using anchors and aliases.
   * </p>
   * <p>
   * Set by {@link org.yaml.snakeyaml.composer.Composer}, used during the construction process.
   * </p>
   * <p>
   * Only relevant during loading.
   * </p>
   *
   * @return <code>true</code> if the node is self referenced.
   */
  public boolean isTwoStepsConstruction() {
    return twoStepsConstruction;
  }

  @Override
  public final int hashCode() {
    return super.hashCode();
  }

  public boolean useClassConstructor() {
    if (useClassConstructor == null) {
      // the tag is compatible with the runtime class
      // the tag will be ignored
      if (!tag.isSecondary() && resolved && !Object.class.equals(type) && !tag.equals(Tag.NULL)) {
        return true;
      } else {
        return tag.isCompatible(getType());
      }
    }
    return useClassConstructor.booleanValue();
  }

  public void setUseClassConstructor(Boolean useClassConstructor) {
    this.useClassConstructor = useClassConstructor;
  }

  public String getAnchor() {
    return anchor;
  }

  public void setAnchor(String anchor) {
    this.anchor = anchor;
  }

  /**
   * The ordered list of in-line comments. The first of which appears at the end of the line
   * respresent by this node. The rest are in the following lines, indented per the Spec to indicate
   * they are continuation of the inline comment.
   *
   * @return the comment line list.
   */
  public List<CommentLine> getInLineComments() {
    return inLineComments;
  }

  public void setInLineComments(List<CommentLine> inLineComments) {
    this.inLineComments = inLineComments;
  }

  /**
   * The ordered list of blank lines and block comments (full line) that appear before this node.
   *
   * @return the comment line list.
   */
  public List<CommentLine> getBlockComments() {
    return blockComments;
  }

  public void setBlockComments(List<CommentLine> blockComments) {
    this.blockComments = blockComments;
  }

  /**
   * The ordered list of blank lines and block comments (full line) that appear AFTER this node.
   * <p>
   * NOTE: these comment should occur only in the last node in a document, when walking the node
   * tree "in order"
   *
   * @return the comment line list.
   */
  public List<CommentLine> getEndComments() {
    return endComments;
  }

  public void setEndComments(List<CommentLine> endComments) {
    this.endComments = endComments;
  }
}
