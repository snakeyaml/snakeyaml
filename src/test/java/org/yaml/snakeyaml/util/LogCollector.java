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
package org.yaml.snakeyaml.util;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.stream.Stream;


/**
 * Handler to use in tests which need to check logs.
 *
 * Usage example:
 *
 * <pre>
 * {@code
 * class ClassOfTest {
 *   private final static LogCollector logs = new LogCollector();
 *
 *   // Store the log level to restore at the end
 *   private static Level previousLevel;
 *
 *   // The logger to configure in order to intercept the log message
 *   private static Logger logger = Logger.getLogger("org.yaml.snakeyaml");
 *
 *   &#64;BeforeClass
 *   public static void start() {
 *     // add log message intercepting handler
 *     logger.addHandler(logs);
 *     previousLevel = logger.getLevel();
 *     logger.setLevel(Level.ALL);
 *   }
 *
 *   &#64;AfterClass
 *   public static void end() {
 *     // restore the logger status
 *     logger.removeHandler(logs);
 *     logger.setLevel(previousLevel);
 *   }
 *   // test methods
 * }
 * }
 * </pre>
 *
 */
public class LogCollector extends Handler {

  private Queue<LogRecord> logs = new ConcurrentLinkedQueue<LogRecord>();

  public void clear() {
    logs.clear();
  }

  public boolean containsLogMessage(Level level, String messagePattern) {
    return currentThreadLogs().anyMatch(
        record -> record.getLevel().equals(level) && record.getMessage().matches(messagePattern));
  }

  public boolean containsLogMessage(String loggerNamePattern, Level level, String messagePattern) {
    return currentThreadLogsFor(loggerNamePattern).filter(record -> record.getLevel() == level)
        .anyMatch(record -> record.getMessage().matches(messagePattern));
  }

  public long numberOfLogEntried() {
    return currentThreadLogs().count();
  }

  public long numberOfLogEntriesFor(String loggerNamePattern, Level level) {
    return currentThreadLogsFor(loggerNamePattern).filter(record -> record.getLevel() == level)
        .count();
  }

  public Stream<LogRecord> currentThreadLogsFor(String loggerNamePattern) {
    return currentThreadLogs().filter(record -> record.getLoggerName().matches(loggerNamePattern));
  }

  /*
   * Filter records for current thread. As we are not multythreaded at the moment this should work
   * for us, even if we make test suites run in parallel
   */
  public Stream<LogRecord> currentThreadLogs() {
    final long threadId = Thread.currentThread().getId();
    // in Java8 record.getThreadID() is `int` and it is deprecated in Java16 where there is
    // `.getLongThreadID()`
    return logs.stream().filter(record -> record.getThreadID() == threadId);
  }

  @Override
  public void publish(LogRecord record) {
    logs.add(record);
  }

  @Override
  public void flush() {}

  @Override
  public void close() throws SecurityException {
    clear();
  }
}
