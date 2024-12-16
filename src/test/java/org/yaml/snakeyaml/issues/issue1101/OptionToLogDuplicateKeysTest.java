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

import org.junit.*;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.*;
import java.util.logging.*;

import static org.junit.Assert.assertEquals;

/**
 * https://bitbucket.org/snakeyaml/snakeyaml/issues/1101
 */
public class OptionToLogDuplicateKeysTest {

  private static final LogMessageInterceptorHandler LOG_MESSAGE_INTERCEPTOR_HANDLER =
      new LogMessageInterceptorHandler();

  /*
   * Store the log level to restore at the end
   */
  private static Level PREVIOUS_LEVEL;

  /*
   * The logger to configure in order to intercept the log message
   */
  private static Logger logger = Logger.getLogger("org.yaml.snakeyaml.constructor");

  /*
   * Logging handler used to test if a message has been really logged.
   */
  public static class LogMessageInterceptorHandler extends Handler {

    private Set<String> messages = new HashSet<>();

    @Override
    public void publish(LogRecord record) {
      // add log messages to a set
      this.messages.add(record.getMessage());
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }

    @Override
    public boolean isLoggable(LogRecord record) {
      return super.isLoggable(record);
    }

    public boolean containsLogMessage(String message) {
      // check if a message has been logged
      return this.messages.contains(message);
    }

  }

  @BeforeClass
  public static void start() {
    // add log message intercepting handler
    logger.addHandler(LOG_MESSAGE_INTERCEPTOR_HANDLER);
    PREVIOUS_LEVEL = logger.getLevel();
    logger.setLevel(Level.FINEST);
  }

  @AfterClass
  public static void end() {
    // restore the logger status
    logger.removeHandler(LOG_MESSAGE_INTERCEPTOR_HANDLER);
    logger.setLevel(PREVIOUS_LEVEL);
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
    Assert.assertTrue(
        LOG_MESSAGE_INTERCEPTOR_HANDLER.containsLogMessage("duplicate keys found : banner"));
  }

}
