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
package org.yaml.snakeyaml.issues.issue1106;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.yaml.snakeyaml.EnumBeanGen;
import org.yaml.snakeyaml.Suit;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.YamlCreator;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.PropertySubstitute;
import org.yaml.snakeyaml.util.LogCollector;

public class LogMessagesAtCorrectLevelTest {

  private final static LogCollector logs = new LogCollector();

  // Store the log level to restore at the end
  private static Level previousLevel;

  // The logger to configure in order to intercept the log message
  private static Logger logger = Logger.getLogger("org.yaml.snakeyaml");

  @BeforeClass
  public static void start() {
    // add log message intercepting handler
    logger.addHandler(logs);
    previousLevel = logger.getLevel();
    logger.setLevel(Level.ALL);
  }

  @AfterClass
  public static void end() {
    // restore the logger status
    logger.removeHandler(logs);
    logger.setLevel(previousLevel);
  }

  @Test
  public void kebabCaseFieldNotFoundLogging() {
    // This is taken from org.snakeyaml.EnumBeanGenTest.testLoadNoTags
    Constructor c = new Constructor(YamlCreator.trustedLoaderOptions());

    TypeDescription td = new TypeDescription(EnumBeanGen.class);
    td.substituteProperty("s-u-i-t", Suit.class, "getSuit", "setSuit");
    td.substituteProperty(new PropertySubstitute("m-a-p", LinkedHashMap.class, "getMap", "setMap",
        Suit.class, Object.class));
    c.addTypeDescription(td);

    Yaml yaml = new Yaml(c);

    EnumBeanGen<Suit> bean = yaml.load(
        "!!org.yaml.snakeyaml.EnumBeanGen\nid: 174\nm-a-p:\n  CLUBS: 1\n  DIAMONDS: 2\ns-u-i-t: CLUBS");

    LinkedHashMap<Suit, Integer> map = new LinkedHashMap<Suit, Integer>();
    map.put(Suit.CLUBS, 1);
    map.put(Suit.DIAMONDS, 2);

    assertEquals(Suit.CLUBS, bean.getSuit());
    assertEquals(174, bean.getId());
    assertEquals(map, bean.getMap());

    String introspectorLoggerName = PropertySubstitute.class.getPackage().getName();

    assertEquals(2, logs.numberOfLogEntriesFor(introspectorLoggerName, Level.FINE));
    assertEquals(0, logs.numberOfLogEntriesFor(introspectorLoggerName, Level.WARNING));

    assertTrue(logs.containsLogMessage(Level.FINE,
        "Failed to find field for org.yaml.snakeyaml.EnumBeanGen.m-a-p"));
    assertTrue(logs.containsLogMessage(Level.FINE,
        "Failed to find field for org.yaml.snakeyaml.EnumBeanGen.s-u-i-t"));


  }


}
