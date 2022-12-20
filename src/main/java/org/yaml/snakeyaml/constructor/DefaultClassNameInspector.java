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
package org.yaml.snakeyaml.constructor;

import java.util.ArrayList;
import java.util.List;

/**
 * The default implementation of ClassNameInspector which does not allow to create a class if it
 * begins with the deny pattern. By default, "javax.script" and "java.lang.ClassLoader" are denied.
 */
public class DefaultClassNameInspector implements ClassNameInspector {

  private final List<String> denyList;

  /**
   * Create with default classes
   */
  public DefaultClassNameInspector() {
    this(defaultList());
  }

  /**
   * Create with provided classes to reject
   *
   * @param denyList - the list of classes to reject
   */
  public DefaultClassNameInspector(List<String> denyList) {
    this.denyList = denyList;
  }

  /**
   * Add javax.script.* and java.lang.ClassLoader
   *
   * @return default name patterns to reject
   */
  public static List<String> defaultList() {
    List<String> denyList = new ArrayList<String>();
    denyList.add("javax.script");
    denyList.add("java.lang.ClassLoader");
    return denyList;
  }

  /**
   * Check if the class to be created is denied (it is prohibited because of security or other
   * reasons)
   *
   * @param fullClassName - class to create
   * @return true when the class should not be created
   */
  @Override
  public boolean isAllowed(String fullClassName) {
    for (String denied : denyList) {
      if (fullClassName.startsWith(denied)) {
        return false;
      }
    }
    return true;
  }
}
