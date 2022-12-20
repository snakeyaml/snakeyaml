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

/**
 * Check the class either to allow or to reject to create an instance. It will control the classes
 * to be created at runtime to avoid remote code invocation from untrusted sources.
 *
 * The implementation may decide to load the class to check if it is a descendant of a prohibited
 * class.
 */
public interface ClassNameInspector {

  /**
   * Check the class
   *
   * @param className - the class to check
   * @return true when it is safe to create an instance of this class
   */
  boolean isAllowed(String className);
}
