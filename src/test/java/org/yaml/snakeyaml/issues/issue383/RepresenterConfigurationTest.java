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
package org.yaml.snakeyaml.issues.issue383;

import static org.yaml.snakeyaml.DumperOptions.FlowStyle.BLOCK;
import static org.yaml.snakeyaml.DumperOptions.FlowStyle.FLOW;
import static org.yaml.snakeyaml.DumperOptions.ScalarStyle.FOLDED;
import static org.yaml.snakeyaml.DumperOptions.ScalarStyle.PLAIN;

import java.util.Calendar;
import java.util.TimeZone;
import org.junit.Assert;
import org.junit.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.representer.Representer;

public class RepresenterConfigurationTest {

  @Test
  public void testDefaultFlowStyleNotOverridden() {
    Representer representer = new Representer(new DumperOptions());
    representer.setDefaultFlowStyle(BLOCK);
    Yaml yaml = new Yaml(representer);

    MappingNode node = (MappingNode) yaml.represent(new TestObject(27, "test"));
    Assert.assertEquals(DumperOptions.FlowStyle.BLOCK, node.getFlowStyle());
  }

  @Test
  public void testDefaultFlowStyleIsOverridden() {
    DumperOptions dumperOptions = new DumperOptions();
    dumperOptions.setDefaultFlowStyle(FLOW);
    Representer representer = new Representer(dumperOptions);
    // ??? representer.setDefaultFlowStyle(BLOCK);

    Yaml yaml = new Yaml(representer, dumperOptions);

    MappingNode node = (MappingNode) yaml.represent(new TestObject(27, "test"));
    Assert.assertEquals(DumperOptions.FlowStyle.FLOW, node.getFlowStyle());
  }

  @Test
  public void testDefaultScalarStyleNotOverridden() {
    Representer representer = new Representer(new DumperOptions());
    representer.setDefaultScalarStyle(FOLDED);

    Yaml yaml = new Yaml(representer);

    ScalarNode node = (ScalarNode) yaml.represent("test");
    Assert.assertEquals(FOLDED, node.getScalarStyle());
  }

  @Test
  public void testDefaultScalarStyleOverridden() {
    DumperOptions dumperOptions = new DumperOptions();
    dumperOptions.setDefaultScalarStyle(PLAIN);
    Representer representer = new Representer(dumperOptions);
    // ???representer.setDefaultScalarStyle(FOLDED);

    Yaml yaml = new Yaml(representer, dumperOptions);

    ScalarNode node = (ScalarNode) yaml.represent("test");
    Assert.assertEquals(node.getScalarStyle(), PLAIN);
  }

  @Test
  public void testPlainStyleByDefault() {
    Yaml yaml = new Yaml();
    ScalarNode node = (ScalarNode) yaml.represent("test");
    Assert.assertEquals(PLAIN, node.getScalarStyle());
  }

  @Test
  public void testTimeZoneNotOverridden() {
    Representer representer = new Representer(new DumperOptions());
    representer.setTimeZone(TimeZone.getTimeZone("Europe/Kiev"));

    Yaml yaml = new Yaml(representer);

    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(0);

    ScalarNode node = (ScalarNode) yaml.represent(calendar.getTime());

    Assert.assertEquals("1970-01-01T03:00:00+03:00", node.getValue());
  }

  @Test
  public void testTimeZoneOverridden() {
    Representer representer = new Representer(new DumperOptions());
    representer.setTimeZone(TimeZone.getTimeZone("Europe/Kiev"));

    DumperOptions dumperOptions = new DumperOptions();
    dumperOptions.setTimeZone(TimeZone.getTimeZone("UTC"));

    Yaml yaml = new Yaml(representer, dumperOptions);

    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(0);

    ScalarNode node = (ScalarNode) yaml.represent(calendar.getTime());

    Assert.assertEquals("1970-01-01T00:00:00Z", node.getValue());
  }

  @Test
  public void testDefaultTimeZone() {
    Yaml yaml = new Yaml();

    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(0);

    ScalarNode node = (ScalarNode) yaml.represent(calendar.getTime());

    Assert.assertEquals("1970-01-01T00:00:00Z", node.getValue());
  }

  @Test
  public void testAllowReadOnlyPropertiesNotOverridden() {
    PropertyUtils propertyUtils = new PropertyUtils();
    propertyUtils.setAllowReadOnlyProperties(true);
    Representer representer = new Representer(new DumperOptions());
    representer.setPropertyUtils(propertyUtils);

    Yaml yaml = new Yaml(representer);
    MappingNode mappingNode = (MappingNode) yaml.represent(new TestObject(27, "test"));
    Assert.assertEquals(2, mappingNode.getValue().size());
  }

  @Test
  public void testAllowReadOnlyPropertiesOverridden() {
    PropertyUtils propertyUtils = new PropertyUtils();
    propertyUtils.setAllowReadOnlyProperties(true);
    DumperOptions dumperOptions = new DumperOptions();
    dumperOptions.setAllowReadOnlyProperties(false);
    Representer representer = new Representer(dumperOptions);
    representer.setPropertyUtils(propertyUtils);
    Yaml yaml = new Yaml(representer, dumperOptions);
    MappingNode mappingNode = (MappingNode) yaml.represent(new TestObject(27, "test"));
    Assert.assertEquals(1, mappingNode.getValue().size());
  }

  @Test
  public void testReadOnlyPropertiesNotAllowedByDefault() {
    Yaml yaml = new Yaml();
    MappingNode mappingNode = (MappingNode) yaml.represent(new TestObject(27, "test"));
    Assert.assertEquals(1, mappingNode.getValue().size());
  }


  public static class TestObject {

    private int id;

    private final String value;

    public TestObject(int id, String value) {
      this.id = id;
      this.value = value;
    }

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public String getValue() {
      return value;
    }
  }

}
