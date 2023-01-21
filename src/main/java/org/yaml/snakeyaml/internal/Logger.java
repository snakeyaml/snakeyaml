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
