/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml;

import java.util.Map;

import org.yaml.snakeyaml.emitter.Emitter;
import org.yaml.snakeyaml.error.YAMLException;

/**
 * @see <a href="http://pyyaml.org/wiki/PyYAML">PyYAML</a> for more information
 */
public class DumperOptions {
    public enum DefaultScalarStyle {
        DOUBLE_QUOTED(new Character('"')), SINGLE_QUOTED(new Character('\'')), LITERAL(
                new Character('|')), FOLDED(new Character('>')), PLAIN(null);
        private Character styleChar;

        private DefaultScalarStyle(Character defaultStyle) {
            this.styleChar = defaultStyle;
        }

        public Character getChar() {
            return styleChar;
        }

        @Override
        public String toString() {
            return "Scalar style: '" + styleChar + "'";
        }
    }

    public enum DefaultFlowStyle {
        FLOW(Boolean.TRUE), BLOCK(Boolean.FALSE), AUTO(null);

        private Boolean styleBoolean;

        private DefaultFlowStyle(Boolean defaultFlowStyle) {
            styleBoolean = defaultFlowStyle;
        }

        public Boolean getStyleBoolean() {
            return styleBoolean;
        }

        @Override
        public String toString() {
            return "Flow style: '" + styleBoolean + "'";
        }
    }

    public enum LineBreak {
        WIN("\r\n"), MAC("\r"), LINUX("\n");

        private String lineBreak;

        private LineBreak(String lineBreak) {
            this.lineBreak = lineBreak;
        }

        public String getString() {
            return lineBreak;
        }

        @Override
        public String toString() {
            return "Line break: " + name();
        }
    }

    public enum Version {
        V1_0(new Integer[] { 1, 0 }), V1_1(new Integer[] { 1, 1 });

        private Integer[] version;

        private Version(Integer[] version) {
            this.version = version;
        }

        public Integer[] getArray() {
            return version;
        }

        @Override
        public String toString() {
            return "Version: " + version[0] + "." + version[1];
        }
    }

    private DefaultScalarStyle defaultStyle = DefaultScalarStyle.PLAIN;
    private DefaultFlowStyle defaultFlowStyle = DefaultFlowStyle.AUTO;
    private boolean canonical = false;
    private boolean allowUnicode = true;
    private int indent = 2;
    private int bestWidth = 80;
    private LineBreak lineBreak = LineBreak.LINUX;
    private boolean explicitStart = false;
    private boolean explicitEnd = false;
    private String explicitRoot = null;
    private Version version = null;
    private Map<String, String> tags = null;

    public boolean isAllowUnicode() {
        return allowUnicode;
    }

    /**
     * Specify whether to emit non-ASCII printable Unicode characters (to
     * support ASCII terminals). The default value is true.
     * 
     * @param allowUnicode
     *            - if allowUnicode is false then all non-ASCII characters are
     *            escaped
     */
    public void setAllowUnicode(boolean allowUnicode) {
        this.allowUnicode = allowUnicode;
    }

    public DefaultScalarStyle getDefaultStyle() {
        return defaultStyle;
    }

    /**
     * Set default style for scalars. See YAML 1.1 specification, 2.3 Scalars
     * (http://yaml.org/spec/1.1/#id858081)
     * 
     * @param defaultStyle
     */
    public void setDefaultStyle(DefaultScalarStyle defaultStyle) {
        if (defaultStyle == null) {
            throw new NullPointerException("Use DefaultScalarStyle enum.");
        }
        this.defaultStyle = defaultStyle;
    }

    public void setIndent(int indent) {
        if (indent < Emitter.MIN_INDENT) {
            throw new YAMLException("Indent must be at least " + Emitter.MIN_INDENT);
        }
        if (indent > Emitter.MAX_INDENT) {
            throw new YAMLException("Indent must be at most " + Emitter.MAX_INDENT);
        }
        this.indent = indent;
    }

    public int getIndent() {
        return this.indent;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public Version getVersion() {
        return this.version;
    }

    public DumperOptions setCanonical(boolean canonical) {
        this.canonical = canonical;
        return this;
    }

    public boolean isCanonical() {
        return this.canonical;
    }

    /**
     * Specify the preferred width to emit scalars. When the scalar
     * representation takes more then the preferred with the scalar will be
     * split into a few lines. The default is 80.
     * 
     * @param bestWidth
     *            - the preferred with for scalars.
     */
    public void setWidth(int bestWidth) {
        this.bestWidth = bestWidth;
    }

    public int getWidth() {
        return this.bestWidth;
    }

    public LineBreak getLineBreak() {
        return lineBreak;
    }

    public void setDefaultFlowStyle(DefaultFlowStyle defaultFlowStyle) {
        if (defaultFlowStyle == null) {
            throw new NullPointerException("Use DefaultFlowStyle enum.");
        }
        this.defaultFlowStyle = defaultFlowStyle;
    }

    public DefaultFlowStyle getDefaultFlowStyle() {
        return defaultFlowStyle;
    }

    public String getExplicitRoot() {
        return explicitRoot;
    }

    /**
     * @param expRoot
     *            - tag to be used for the root node or <code>null</code> to get
     *            the default
     */
    public void setExplicitRoot(String expRoot) {
        if (expRoot == null) {
            throw new NullPointerException("Root tag must be specified.");
        }
        this.explicitRoot = expRoot;
    }

    /**
     * Specify the line break to separate the lines. It is platform specific:
     * Windows - "\r\n", MacOS - "\r", Linux - "\n". The default value is the
     * one for Linux.
     */
    public void setLineBreak(LineBreak lineBreak) {
        if (lineBreak == null) {
            throw new NullPointerException("Specify line break.");
        }
        this.lineBreak = lineBreak;
    }

    public boolean isExplicitStart() {
        return explicitStart;
    }

    public void setExplicitStart(boolean explicitStart) {
        this.explicitStart = explicitStart;
    }

    public boolean isExplicitEnd() {
        return explicitEnd;
    }

    public void setExplicitEnd(boolean explicitEnd) {
        this.explicitEnd = explicitEnd;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

}
