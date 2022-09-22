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
package org.yaml.snakeyaml.extensions.compactnotation;

import org.yaml.snakeyaml.LoaderOptions;

public class PackageCompactConstructor extends CompactConstructor {

  private final String packageName;

  public PackageCompactConstructor(String packageName) {
    super(new LoaderOptions());
    this.packageName = packageName;
  }

  @Override
  protected Class<?> getClassForName(String name) throws ClassNotFoundException {
    if (name.indexOf('.') < 0) {
      try {
        Class<?> clazz = Class.forName(packageName + "." + name);
        return clazz;
      } catch (ClassNotFoundException e) {
        // use super implementation
      }
    }
    return super.getClassForName(name);
  }
}
