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
package org.yaml.snakeyaml.resolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.Tag;

/**
 * Resolver tries to detect a type by content (when the tag is implicit)
 */
public class Resolver {

  public static final Pattern BOOL = Pattern
      .compile("^(?:yes|Yes|YES|no|No|NO|true|True|TRUE|false|False|FALSE|on|On|ON|off|Off|OFF)$");

  /**
   * The regular expression is taken from the 1.2 specification but '_'s are added to keep backwards
   * compatibility
   */
  public static final Pattern FLOAT =
      Pattern.compile("^(" + "[-+]?(?:[0-9][0-9_]*)\\.[0-9_]*(?:[eE][-+]?[0-9]+)?" + // (base 10)
          "|[-+]?(?:[0-9][0-9_]*)(?:[eE][-+]?[0-9]+)" + // (base 10, scientific notation without .)
          "|[-+]?\\.[0-9_]+(?:[eE][-+]?[0-9]+)?" + // (base 10, starting with .)
          "|[-+]?[0-9][0-9_]*(?::[0-5]?[0-9])+\\.[0-9_]*" + // (base 60)
          "|[-+]?\\.(?:inf|Inf|INF)" + "|\\.(?:nan|NaN|NAN)" + ")$");
  public static final Pattern INT = Pattern.compile("^(?:" + "[-+]?0b_*[0-1][0-1_]*" + // (base 2)
      "|[-+]?0_*[0-7][0-7_]*" + // (base 8)
      "|[-+]?(?:0|[1-9][0-9_]*)" + // (base 10)
      "|[-+]?0x_*[0-9a-fA-F][0-9a-fA-F_]*" + // (base 16)
      "|[-+]?[1-9][0-9_]*(?::[0-5]?[0-9])+" + // (base 60)
      ")$");
  public static final Pattern MERGE = Pattern.compile("^(?:<<)$");
  public static final Pattern NULL = Pattern.compile("^(?:~|null|Null|NULL| )$");
  public static final Pattern EMPTY = Pattern.compile("^$");
  public static final Pattern TIMESTAMP = Pattern.compile(
      "^(?:[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]|[0-9][0-9][0-9][0-9]-[0-9][0-9]?-[0-9][0-9]?(?:[Tt]|[ \t]+)[0-9][0-9]?:[0-9][0-9]:[0-9][0-9](?:\\.[0-9]*)?(?:[ \t]*(?:Z|[-+][0-9][0-9]?(?::[0-9][0-9])?))?)$");
  public static final Pattern VALUE = Pattern.compile("^(?:=)$");
  public static final Pattern YAML = Pattern.compile("^(?:!|&|\\*)$");

  protected Map<Character, List<ResolverTuple>> yamlImplicitResolvers =
      new HashMap<Character, List<ResolverTuple>>();

  protected void addImplicitResolvers() {
    addImplicitResolver(Tag.BOOL, BOOL, "yYnNtTfFoO", 10);
    /*
     * INT must be before FLOAT because the regular expression for FLOAT matches INT (see issue 130)
     * http://code.google.com/p/snakeyaml/issues/detail?id=130
     */
    addImplicitResolver(Tag.INT, INT, "-+0123456789");
    addImplicitResolver(Tag.FLOAT, FLOAT, "-+0123456789.");
    addImplicitResolver(Tag.MERGE, MERGE, "<", 10);
    addImplicitResolver(Tag.NULL, NULL, "~nN\0", 10);
    addImplicitResolver(Tag.NULL, EMPTY, null, 10);
    addImplicitResolver(Tag.TIMESTAMP, TIMESTAMP, "0123456789", 50);
    // The following implicit resolver is only for documentation purposes.
    // It cannot work because plain scalars cannot start with '!', '&', or '*'.
    addImplicitResolver(Tag.YAML, YAML, "!&*", 10);
  }

  public Resolver() {
    addImplicitResolvers();
  }

  public void addImplicitResolver(Tag tag, Pattern regexp, String first) {
    addImplicitResolver(tag, regexp, first, 1024);
  }

  /**
   * Add a resolver to resolve a value that matches the provided regular expression to the provided
   * tag
   *
   * @param tag - the Tag to assign when the value matches
   * @param regexp - the RE which is applied for every value
   * @param first - the possible first characters (this is merely for performance improvement) to
   *        skip RE evaluation to gain time
   * @param limit - the limit of the value to analyze. The limit is here only to fight the DoS
   *        attack when huge values are provided, and it may lead to slow pattern evaluation
   */
  public void addImplicitResolver(Tag tag, Pattern regexp, String first, int limit) {
    if (regexp == null) {
      throw new IllegalStateException("No pattern provided for Tag=" + tag);
    }
    if (first == null) {
      List<ResolverTuple> curr = yamlImplicitResolvers.get(null);
      if (curr == null) {
        curr = new ArrayList<ResolverTuple>();
        yamlImplicitResolvers.put(null, curr);
      }
      curr.add(new ResolverTuple(tag, regexp, limit));
    } else {
      char[] chrs = first.toCharArray();
      for (int i = 0, j = chrs.length; i < j; i++) {
        Character theC = Character.valueOf(chrs[i]);
        if (theC == 0) {
          // special case: for null
          theC = null;
        }
        List<ResolverTuple> curr = yamlImplicitResolvers.get(theC);
        if (curr == null) {
          curr = new ArrayList<ResolverTuple>();
          yamlImplicitResolvers.put(theC, curr);
        }
        curr.add(new ResolverTuple(tag, regexp, limit));
      }
    }
  }

  public Tag resolve(NodeId kind, String value, boolean implicit) {
    if (kind == NodeId.scalar && implicit) {
      final List<ResolverTuple> resolvers;
      if (value.isEmpty()) {
        resolvers = yamlImplicitResolvers.get('\0');
      } else {
        resolvers = yamlImplicitResolvers.get(value.charAt(0));
      }
      if (resolvers != null) {
        for (ResolverTuple v : resolvers) {
          Tag tag = v.getTag();
          Pattern regexp = v.getRegexp();
          if (value.length() <= v.getLimit() && regexp.matcher(value).matches()) {
            return tag;
          }
        }
      }
      if (yamlImplicitResolvers.containsKey(null)) {
        // check null resolver
        for (ResolverTuple v : yamlImplicitResolvers.get(null)) {
          Tag tag = v.getTag();
          Pattern regexp = v.getRegexp();
          if (value.length() <= v.getLimit() && regexp.matcher(value).matches()) {
            return tag;
          }
        }
      }
    }
    switch (kind) {
      case scalar:
        return Tag.STR;
      case sequence:
        return Tag.SEQ;
      default:
        return Tag.MAP;
    }
  }
}
