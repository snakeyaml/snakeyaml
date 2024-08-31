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
package org.yaml.snakeyaml;

import java.util.Map;
import java.util.TimeZone;
import org.yaml.snakeyaml.emitter.Emitter;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.serializer.AnchorGenerator;
import org.yaml.snakeyaml.serializer.NumberAnchorGenerator;


/**
 * Configuration for serialisation
 */
public class DumperOptions {

  /**
   * YAML provides a rich set of scalar styles. Block scalar styles include the literal style and
   * the folded style; flow scalar styles include the plain style and two quoted styles, the
   * single-quoted style and the double-quoted style. These styles offer a range of trade-offs
   * between expressive power and readability.
   *
   * @see <a href="http://yaml.org/spec/1.1/#id903915">Chapter 9. Scalar Styles</a>
   * @see <a href="http://yaml.org/spec/1.1/#id858081">2.3. Scalars</a>
   */
  public enum ScalarStyle {
    /**
     * Double quoted scalar
     */
    DOUBLE_QUOTED('"'),
    /**
     * Single quoted scalar
     */
    SINGLE_QUOTED('\''),
    /**
     * Literal scalar
     */
    LITERAL('|'),
    /**
     * Folded scalar
     */
    FOLDED('>'),
    /**
     * Mixture of scalar styles to dump JSON format. Double-quoted style for !!str, !!binary,
     * !!timestamp. Plain style - for !!bool, !!float, !!int, !!null
     *
     * These are never dumped - !!merge, !!value, !!yaml
     */
    JSON_SCALAR_STYLE('J'),
    /**
     * Plain scalar
     */
    PLAIN(null);

    private final Character styleChar;

    ScalarStyle(Character style) {
      this.styleChar = style;
    }

    /**
     * getter
     *
     * @return the char behind the style
     */
    public Character getChar() {
      return styleChar;
    }

    /**
     * Readable style
     *
     * @return for humans
     */
    @Override
    public String toString() {
      return "Scalar style: '" + styleChar + "'";
    }

    /**
     * Create
     *
     * @param style - source char
     * @return parsed style
     */
    public static ScalarStyle createStyle(Character style) {
      if (style == null) {
        return PLAIN;
      } else {
        switch (style) {
          case '"':
            return DOUBLE_QUOTED;
          case '\'':
            return SINGLE_QUOTED;
          case '|':
            return LITERAL;
          case '>':
            return FOLDED;
          default:
            throw new YAMLException("Unknown scalar style character: " + style);
        }
      }
    }
  }

  /**
   * Block styles use indentation to denote nesting and scope within the document. In contrast, flow
   * styles rely on explicit indicators to denote nesting and scope.
   *
   * @see <a href="http://www.yaml.org/spec/current.html#id2509255">3.2.3.1. Node Styles
   *      (http://yaml.org/spec/1.1)</a>
   */
  public enum FlowStyle {
    /**
     * Flow style
     */
    FLOW(Boolean.TRUE),
    /**
     * Block style
     */
    BLOCK(Boolean.FALSE),
    /**
     * Auto (first block, then flow)
     */
    AUTO(null);

    private final Boolean styleBoolean;

    FlowStyle(Boolean flowStyle) {
      styleBoolean = flowStyle;
    }

    @Override
    public String toString() {
      return "Flow style: '" + styleBoolean + "'";
    }
  }

  /**
   * Platform dependent line break.
   */
  public enum LineBreak {
    /**
     * Windows
     */
    WIN("\r\n"),
    /**
     * Old Mac (should not be used !)
     */
    MAC("\r"),
    /**
     * Linux and Mac
     */
    UNIX("\n");

    private final String lineBreak;

    /**
     * Create
     *
     * @param lineBreak - break
     */
    LineBreak(String lineBreak) {
      this.lineBreak = lineBreak;
    }

    /**
     * getter
     *
     * @return the break
     */
    public String getString() {
      return lineBreak;
    }

    /**
     * for humans
     *
     * @return representation
     */
    @Override
    public String toString() {
      return "Line break: " + name();
    }

    /**
     * Get the line break used by the current Operating System
     *
     * @return detected line break
     */
    public static LineBreak getPlatformLineBreak() {
      String platformLineBreak = System.getProperty("line.separator");
      for (LineBreak lb : values()) {
        if (lb.lineBreak.equals(platformLineBreak)) {
          return lb;
        }
      }
      return LineBreak.UNIX;
    }
  }

  /**
   * Specification version. Currently supported 1.0 and 1.1
   */
  public enum Version {
    /**
     * 1.0
     */
    V1_0(new Integer[] {1, 0}),
    /**
     * 1.1
     */
    V1_1(new Integer[] {1, 1});

    private final Integer[] version;

    /**
     * Create
     *
     * @param version - definition
     */
    Version(Integer[] version) {
      this.version = version;
    }

    /**
     * getter
     *
     * @return major part (always 1)
     */
    public int major() {
      return version[0];
    }

    /**
     * Minor part (0 or 1)
     *
     * @return 0 or 1
     */
    public int minor() {
      return version[1];
    }

    /**
     * getter
     *
     * @return representation for serialisation
     */
    public String getRepresentation() {
      return version[0] + "." + version[1];
    }

    /**
     * Readable string
     *
     * @return for humans
     */
    @Override
    public String toString() {
      return "Version: " + getRepresentation();
    }
  }

  /**
   * the way to serialize non-printable
   */
  public enum NonPrintableStyle {
    /**
     * Transform String to binary if it contains non-printable characters
     */
    BINARY,
    /**
     * Escape non-printable characters
     */
    ESCAPE
  }

  private ScalarStyle defaultStyle = ScalarStyle.PLAIN;
  private FlowStyle defaultFlowStyle = FlowStyle.AUTO;
  private boolean canonical = false;
  private boolean allowUnicode = true;
  private boolean allowReadOnlyProperties = false;
  private int indent = 2;
  private int indicatorIndent = 0;
  private boolean indentWithIndicator = false;
  private int bestWidth = 80;
  private boolean splitLines = true;
  private LineBreak lineBreak = LineBreak.UNIX;
  private boolean explicitStart = false;
  private boolean explicitEnd = false;
  private TimeZone timeZone = null;
  private int maxSimpleKeyLength = 128;
  private boolean processComments = false;
  private NonPrintableStyle nonPrintableStyle = NonPrintableStyle.BINARY;

  private Version version = null;
  private Map<String, String> tags = null;
  private Boolean prettyFlow = false;
  private AnchorGenerator anchorGenerator = new NumberAnchorGenerator(0);

  private boolean dereferenceAliases = false;

  /**
   * getter
   *
   * @return false when non-ASCII is escaped
   */
  public boolean isAllowUnicode() {
    return allowUnicode;
  }

  /**
   * Specify whether to emit non-ASCII printable Unicode characters. The default value is true. When
   * set to false then printable non-ASCII characters (Cyrillic, Chinese etc) will be not printed
   * but escaped (to support ASCII terminals)
   *
   * @param allowUnicode if allowUnicode is false then all non-ASCII characters are escaped
   */
  public void setAllowUnicode(boolean allowUnicode) {
    this.allowUnicode = allowUnicode;
  }

  /**
   * getter
   *
   * @return scalar style
   */
  public ScalarStyle getDefaultScalarStyle() {
    return defaultStyle;
  }

  /**
   * Set default style for scalars. See YAML 1.1 specification, 2.3 Scalars
   * (http://yaml.org/spec/1.1/#id858081)
   *
   * @param defaultStyle set the style for all scalars
   */
  public void setDefaultScalarStyle(ScalarStyle defaultStyle) {
    if (defaultStyle == null) {
      throw new NullPointerException("Use ScalarStyle enum.");
    }
    this.defaultStyle = defaultStyle;
  }

  /**
   * Define indentation. Must be within the limits (1-10)
   *
   * @param indent number of spaces to serve as indentation
   */
  public void setIndent(int indent) {
    if (indent < Emitter.MIN_INDENT) {
      throw new YAMLException("Indent must be at least " + Emitter.MIN_INDENT);
    }
    if (indent > Emitter.MAX_INDENT) {
      throw new YAMLException("Indent must be at most " + Emitter.MAX_INDENT);
    }
    this.indent = indent;
  }

  /**
   * getter
   *
   * @return indent
   */
  public int getIndent() {
    return this.indent;
  }

  /**
   * Set number of white spaces to use for the sequence indicator '-'
   *
   * @param indicatorIndent value to be used as indent
   */
  public void setIndicatorIndent(int indicatorIndent) {
    if (indicatorIndent < 0) {
      throw new YAMLException("Indicator indent must be non-negative.");
    }
    if (indicatorIndent > Emitter.MAX_INDENT - 1) {
      throw new YAMLException(
          "Indicator indent must be at most Emitter.MAX_INDENT-1: " + (Emitter.MAX_INDENT - 1));
    }
    this.indicatorIndent = indicatorIndent;
  }

  public int getIndicatorIndent() {
    return this.indicatorIndent;
  }

  public boolean getIndentWithIndicator() {
    return indentWithIndicator;
  }

  /**
   * Set to true to add the indent for sequences to the general indent
   *
   * @param indentWithIndicator - true when indent for sequences is added to general
   */
  public void setIndentWithIndicator(boolean indentWithIndicator) {
    this.indentWithIndicator = indentWithIndicator;
  }

  /**
   * Of no use - it is better not to include YAML version as the directive
   *
   * @param version 1.0 or 1.1
   */
  public void setVersion(Version version) {
    this.version = version;
  }

  /**
   * getter
   *
   * @return the expected version
   */
  public Version getVersion() {
    return this.version;
  }

  /**
   * Force the emitter to produce a canonical YAML document.
   *
   * @param canonical true produce canonical YAML document
   */
  public void setCanonical(boolean canonical) {
    this.canonical = canonical;
  }

  /**
   * getter
   *
   * @return true when well established format should be dumped
   */
  public boolean isCanonical() {
    return this.canonical;
  }

  /**
   * Force the emitter to produce a pretty YAML document when using the flow style.
   *
   * @param prettyFlow true produce pretty flow YAML document
   */
  public void setPrettyFlow(boolean prettyFlow) {
    this.prettyFlow = prettyFlow;
  }

  /**
   * getter
   *
   * @return true for pretty style
   */
  public boolean isPrettyFlow() {
    return this.prettyFlow;
  }

  /**
   * Specify the preferred width to emit scalars. When the scalar representation takes more then the
   * preferred with the scalar will be split into a few lines. The default is 80.
   *
   * @param bestWidth the preferred width for scalars.
   */
  public void setWidth(int bestWidth) {
    this.bestWidth = bestWidth;
  }

  /**
   * getter
   *
   * @return the preferred width for scalars
   */
  public int getWidth() {
    return this.bestWidth;
  }

  /**
   * Specify whether to split lines exceeding preferred width for scalars. The default is true.
   *
   * @param splitLines whether to split lines exceeding preferred width for scalars.
   */
  public void setSplitLines(boolean splitLines) {
    this.splitLines = splitLines;
  }

  /**
   * getter
   *
   * @return true when to split lines exceeding preferred width for scalars
   */
  public boolean getSplitLines() {
    return this.splitLines;
  }

  /**
   * getter
   *
   * @return line break to separate lines
   */
  public LineBreak getLineBreak() {
    return lineBreak;
  }

  /**
   * setter
   *
   * @param defaultFlowStyle - enum for the flow style
   */
  public void setDefaultFlowStyle(FlowStyle defaultFlowStyle) {
    if (defaultFlowStyle == null) {
      throw new NullPointerException("Use FlowStyle enum.");
    }
    this.defaultFlowStyle = defaultFlowStyle;
  }

  /**
   * getter
   *
   * @return flow style for collections
   */
  public FlowStyle getDefaultFlowStyle() {
    return defaultFlowStyle;
  }

  /**
   * Specify the line break to separate the lines. It is platform specific: Windows - "\r\n", old
   * MacOS - "\r", Unix - "\n". The default value is the one for Unix.
   *
   * @param lineBreak to be used for the input
   */
  public void setLineBreak(LineBreak lineBreak) {
    if (lineBreak == null) {
      throw new NullPointerException("Specify line break.");
    }
    this.lineBreak = lineBreak;
  }

  /**
   * getter
   *
   * @return true when '---' must be printed
   */
  public boolean isExplicitStart() {
    return explicitStart;
  }

  /**
   * setter - require explicit '...'
   *
   * @param explicitStart - true to emit '---'
   */
  public void setExplicitStart(boolean explicitStart) {
    this.explicitStart = explicitStart;
  }

  /**
   * getter
   *
   * @return true when '...' must be printed
   */
  public boolean isExplicitEnd() {
    return explicitEnd;
  }

  /**
   * setter - require explicit '...'
   *
   * @param explicitEnd - true to emit '...'
   */
  public void setExplicitEnd(boolean explicitEnd) {
    this.explicitEnd = explicitEnd;
  }

  /**
   * getter
   *
   * @return previously defined tag directives
   */
  public Map<String, String> getTags() {
    return tags;
  }

  /**
   * setter
   *
   * @param tags - tag directives for the YAML document
   */
  public void setTags(Map<String, String> tags) {
    this.tags = tags;
  }

  /**
   * Report whether read-only JavaBean properties (the ones without setters) should be included in
   * the YAML document
   *
   * @return false when read-only JavaBean properties are not emitted
   */
  public boolean isAllowReadOnlyProperties() {
    return allowReadOnlyProperties;
  }

  /**
   * Set to true to include read-only JavaBean properties (the ones without setters) in the YAML
   * document. By default these properties are not included to be able to parse later the same
   * JavaBean.
   *
   * @param allowReadOnlyProperties - true to dump read-only JavaBean properties
   */
  public void setAllowReadOnlyProperties(boolean allowReadOnlyProperties) {
    this.allowReadOnlyProperties = allowReadOnlyProperties;
  }

  /**
   * getter
   *
   * @return timezone to be used to emit Date
   */
  public TimeZone getTimeZone() {
    return timeZone;
  }

  /**
   * Set the timezone to be used for Date. If set to <code>null</code> UTC is used.
   *
   * @param timeZone for created Dates or null to use UTC
   */
  public void setTimeZone(TimeZone timeZone) {
    this.timeZone = timeZone;
  }


  /**
   * getter
   *
   * @return generator to create anchor names
   */
  public AnchorGenerator getAnchorGenerator() {
    return anchorGenerator;
  }

  /**
   * Provide a custom generator
   *
   * @param anchorGenerator - the way to create custom anchors
   */
  public void setAnchorGenerator(AnchorGenerator anchorGenerator) {
    this.anchorGenerator = anchorGenerator;
  }

  public int getMaxSimpleKeyLength() {
    return maxSimpleKeyLength;
  }

  /**
   * Define max key length to use simple key (without '?') More info
   * https://yaml.org/spec/1.1/#id934537
   *
   * @param maxSimpleKeyLength - the limit after which the key gets explicit key indicator '?'
   */
  public void setMaxSimpleKeyLength(int maxSimpleKeyLength) {
    if (maxSimpleKeyLength > 1024) {
      throw new YAMLException(
          "The simple key must not span more than 1024 stream characters. See https://yaml.org/spec/1.1/#id934537");
    }
    this.maxSimpleKeyLength = maxSimpleKeyLength;
  }

  /**
   * Set the comment processing. By default, comments are ignored.
   *
   * @param processComments <code>true</code> to process; <code>false</code> to ignore
   */
  public void setProcessComments(boolean processComments) {
    this.processComments = processComments;
  }

  /**
   * getter
   *
   * @return true when comments are not ignored and can be used after composing a Node
   */
  public boolean isProcessComments() {
    return processComments;
  }

  public NonPrintableStyle getNonPrintableStyle() {
    return this.nonPrintableStyle;
  }

  /**
   * When String contains non-printable characters SnakeYAML convert it to binary data with the
   * !!binary tag. Set this to ESCAPE to keep the !!str tag and escape the non-printable chars with
   * \\x or \\u
   *
   * @param style ESCAPE to force SnakeYAML to keep !!str tag for non-printable data
   */
  public void setNonPrintableStyle(NonPrintableStyle style) {
    this.nonPrintableStyle = style;
  }

  public boolean isDereferenceAliases() {
    return dereferenceAliases;
  }

  /**
   * Forces Serializer to skip emitting Anchors names, emit Node content instead of Alias, fail with
   * SerializationException if serialized structure is recursive.
   *
   * Default value is <code>false</code> - emit Aliases.
   *
   * @param dereferenceAliases emit node referenced by the alias or alias itself
   */
  public void setDereferenceAliases(boolean dereferenceAliases) {
    this.dereferenceAliases = dereferenceAliases;
  }

}
