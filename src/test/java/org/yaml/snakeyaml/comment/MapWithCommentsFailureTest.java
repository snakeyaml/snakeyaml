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
package org.yaml.snakeyaml.comment;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.composer.Composer;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.parser.ParserImpl;
import org.yaml.snakeyaml.reader.StreamReader;
import org.yaml.snakeyaml.resolver.Resolver;

/**
 * https://bitbucket.org/snakeyaml/snakeyaml/issues/1062/bug-when-parsing-flow-style-map-with
 */
public class MapWithCommentsFailureTest {

  @Test
  @Ignore
  public void testParseComment() {
    String yaml = "flowWithComments: { # this is valid place for comment \n" + "  fizz: \"buzz\"}";
    LoaderOptions loadOpts = new LoaderOptions();
    loadOpts.setProcessComments(true);
    MappingNode parsed =
        (MappingNode) new Composer(new ParserImpl(new StreamReader(yaml), loadOpts), new Resolver(),
            loadOpts).getSingleNode();

    assertEquals(1, parsed.getValue());
    ScalarNode flowWithComments = (ScalarNode) parsed.getValue().get(0).getKeyNode();
    assertEquals("flowWithComments", flowWithComments.getValue());
    MappingNode fizzBuzz = (MappingNode) parsed.getValue().get(0).getValueNode();
    assertEquals(1, parsed.getValue());
    ScalarNode fizz = (ScalarNode) fizzBuzz.getValue().get(0).getKeyNode();
    ScalarNode buzz = (ScalarNode) fizzBuzz.getValue().get(0).getValueNode();
    assertEquals("fizz", fizz.getValue());
    assertEquals("buzz", buzz.getValue());
  }
}
