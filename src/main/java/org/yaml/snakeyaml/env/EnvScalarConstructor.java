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
package org.yaml.snakeyaml.env;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.MissingEnvironmentVariableException;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.Tag;

/**
 * Construct scalar for format ${VARIABLE} replacing the template with the value from environment.
 * It can also be used to create JavaBeans when the all the arguments are provided.
 *
 * @see <a href="https://bitbucket.org/snakeyaml/snakeyaml/wiki/Variable%20substitution">Variable
 *      substitution</a>
 * @see <a href="https://docs.docker.com/compose/compose-file/#variable-substitution">Variable
 *      substitution</a>
 */
public class EnvScalarConstructor extends Constructor {

  /**
   * Tag to indicate ENV
   */
  public static final Tag ENV_TAG = new Tag("!ENV");

  /**
   * name must be a word ⇒ \w+ value can be any non-space ⇒ \S+
   */
  public static final Pattern ENV_FORMAT = Pattern
      .compile("^\\$\\{\\s*((?<name>\\w+)((?<separator>:?(-|\\?))(?<value>\\S+)?)?)\\s*\\}$");

  /**
   * For simple cases when no JavaBeans are needed
   */
  public EnvScalarConstructor() {
    super(new LoaderOptions());
    this.yamlConstructors.put(ENV_TAG, new ConstructEnv());
  }

  /**
   * Create EnvScalarConstructor which can create JavaBeans with variable substitution
   *
   * @param theRoot - the class (usually JavaBean) to be constructed
   * @param moreTDs - collection of classes used by the root class
   * @param loadingConfig - configuration
   */
  public EnvScalarConstructor(TypeDescription theRoot, Collection<TypeDescription> moreTDs,
      LoaderOptions loadingConfig) {
    super(theRoot, moreTDs, loadingConfig);
    this.yamlConstructors.put(ENV_TAG, new ConstructEnv());
  }

  private class ConstructEnv extends AbstractConstruct {

    public Object construct(Node node) {
      String val = constructScalar((ScalarNode) node);
      Matcher matcher = ENV_FORMAT.matcher(val);
      matcher.matches();
      String name = matcher.group("name");
      String value = matcher.group("value");
      String separator = matcher.group("separator");
      return apply(name, separator, value != null ? value : "", getEnv(name));
    }
  }

  /**
   * Implement the logic for missing and unset variables
   *
   * @param name - variable name in the template
   * @param separator - separator in the template, can be :-, -, :?, ?
   * @param value - default value or the error in the template
   * @param environment - the value from environment for the provided variable
   * @return the value to apply in the template
   */
  public String apply(String name, String separator, String value, String environment) {
    if (environment != null && !environment.isEmpty()) {
      return environment;
    }
    // variable is either unset or empty
    if (separator != null) {
      // there is a default value or error
      if (separator.equals("?") && (environment == null)) {
        throw new MissingEnvironmentVariableException(
            "Missing mandatory variable " + name + ": " + value);

      }
      if (separator.equals(":?")) {
        if (environment == null) {
          throw new MissingEnvironmentVariableException(
              "Missing mandatory variable " + name + ": " + value);
        }
        if (environment.isEmpty()) {
          throw new MissingEnvironmentVariableException(
              "Empty mandatory variable " + name + ": " + value);
        }
      }
      if (separator.startsWith(":")) {
        if (environment == null || environment.isEmpty()) {
          return value;
        }
      } else {
        if (environment == null) {
          return value;
        }
      }
    }
    return "";
  }

  /**
   * Get value of the environment variable
   *
   * @param key - the name of the variable
   * @return value or null if not set
   */
  public String getEnv(String key) {
    return System.getenv(key);
  }
}
