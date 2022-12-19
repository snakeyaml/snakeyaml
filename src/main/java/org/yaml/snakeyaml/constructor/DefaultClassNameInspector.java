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
 * The default implementation of ClassNameInspector which does not allow to create a class if it is
 * a descendant of any class in the provided list
 */
public class DefaultClassNameInspector implements ClassNameInspector {
  private final List<Class> denyList;

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
  public DefaultClassNameInspector(List<Class> denyList) {
    this.denyList = denyList;
  }

  /**
   * Add javax.script.* and java.lang.ClassLoader
   *
   * @return default classes to reject
   */
  public static List<Class> defaultList() {
    List<Class> denyList = new ArrayList<Class>();
    denyList.add(javax.script.ScriptEngine.class);
    denyList.add(javax.script.ScriptEngineFactory.class);
    denyList.add(javax.script.ScriptEngineManager.class);
    denyList.add(java.lang.ClassLoader.class);
    return denyList;
  }

  @Override
  public boolean isAllowed(Class fullClassName) {
    return !isClassDenied(fullClassName);
  }

  /**
   * Check if the class to be created is denied (it is prohibited because of security or other
   * reasons)
   *
   * @param clazz - class to create
   * @return true when the class should not be created
   */
  private boolean isClassDenied(Class clazz) {
    for (Class denied : denyList) {
      if (denied.isAssignableFrom(clazz))
        return true;
    }
    return false;
  }
}
