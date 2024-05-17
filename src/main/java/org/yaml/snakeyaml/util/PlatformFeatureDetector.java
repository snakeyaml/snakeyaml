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

public class PlatformFeatureDetector {

  private Boolean isRunningOnAndroid = null;

  public boolean isIntrospectionAvailable() {
    /*
     * Android lacks much of java.beans (including the Introspector class, used here), because
     * java.beans classes tend to rely on java.awt, which isn't supported in the Android SDK. That
     * means we have to fall back on FIELD access only when SnakeYAML is running on the Android
     * Runtime.
     */
    if (isRunningOnAndroid()) {
      return false;
    }

    try {
      Class.forName("java.beans.Introspector");
      /* java.desktop module and its java.beans package is available */
      return true;
    } catch (ClassNotFoundException ex) {
      /* running with jlink assembled JDK without java.desktop module */
      return false;
    }
  }

  public boolean isRunningOnAndroid() {
    if (isRunningOnAndroid == null) {
      String name = System.getProperty("java.runtime.name");
      isRunningOnAndroid = (name != null && name.startsWith("Android Runtime"));
    }
    return isRunningOnAndroid;
  }
}
