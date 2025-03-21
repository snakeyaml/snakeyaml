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
package org.yaml.snakeyaml.issues.issue1101;

import static org.junit.Assert.assertEquals;
import java.io.InputStream;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.util.LogCollector;

/**
 * https://bitbucket.org/snakeyaml/snakeyaml/issues/1101
 */
public class OptionToLogDuplicateKeysTest {

  private final static LogCollector logs = new LogCollector();

  /*
   * Store the log level to restore at the end
   */
  private static Level previousLevel;

  /*
   * The logger to configure in order to intercept the log message
   */
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
  public void loadDuplicateKeys() {
    InputStream str = Util.getInputstream("issues/issue1101-option-to-log-duplicate-keys.yaml");
    LoaderOptions options = new LoaderOptions();
    options.setWarnOnDuplicateKeys(true);
    Yaml yaml = new Yaml(options);
    Map<String, Object> sourceTree = yaml.load(str);
    assertEquals(1, sourceTree.size());
    // actual log message checking
    Assert.assertTrue(logs.containsLogMessage(Level.WARNING, "duplicate keys found : banner"));
  }

}
