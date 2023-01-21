package org.yaml.snakeyaml.internal;

public class Logger {
  public enum Level {
    WARNING(System.Logger.Level.WARNING);

    private final System.Logger.Level level;
    Level(System.Logger.Level level) {
      this.level = level;
    }
  }
  private final System.Logger logger;
  private Logger(String name) {
    this.logger = System.getLogger(name);
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
