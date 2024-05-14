package org.yaml.snakeyaml.json;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

import java.util.Date;

/**
 * Represent data structures into Nodes in the way suitable for serialisation as JSON
 */
public class JsonRepresenter extends Representer {
  public JsonRepresenter(DumperOptions options) {
    super(options);
    this.representers.put(byte[].class, new RepresentByteArray());
    this.multiRepresenters.put(Date.class, new RepresentDate());
    if (options.getDefaultScalarStyle() != DumperOptions.ScalarStyle.JSON_SCALAR_STYLE) {
      throw new IllegalStateException("JSON requires ScalarStyle.JSON_SCALAR_STYLE");
    }
    if (options.getNonPrintableStyle() != DumperOptions.NonPrintableStyle.ESCAPE) {
      throw new IllegalStateException("JSON requires NonPrintableStyle.ESCAPE");
    }
  }

  /**
   * Use the provided serialisation but emit as string
   */
  protected class RepresentDate extends Representer.RepresentDate {
    @Override
    public Tag getDefaultTag() {
      return Tag.STR;
    }
  }

  /**
   * JSON does not have support for binary data. This method should be overridden to emit the
   * expected string for binary data
   */
  protected class RepresentByteArray implements Represent {
    public Node representData(Object data) {
      char[] binary = Base64Coder.encode((byte[]) data);
      return representScalar(Tag.STR, String.valueOf(binary));
    }
  }
}
