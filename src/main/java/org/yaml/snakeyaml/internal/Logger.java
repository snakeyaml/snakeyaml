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
package org.yaml.snakeyaml.internal;

public class Logger {
  public enum Level {
    WARNING(java.util.logging.Level.FINE);

    private final java.util.logging.Level level;

    Level(java.util.logging.Level level) {
      this.level = level;
    }
  }

  private final java.util.logging.Logger logger;

  private Logger(String name) {
    this.logger = java.util.logging.Logger.getLogger(name);
  }

  public static Logger getLogger(String name) {
    return new Logger(name);
  }

  public boolean isLoggable(Level level) {
    return logger.isLoggable(level.level);
  }

  public void warn(String msg) {
    logger.log(Level.WARNING.level, msg);
  }
}
