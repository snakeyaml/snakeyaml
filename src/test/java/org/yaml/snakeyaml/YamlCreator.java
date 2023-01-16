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
package org.yaml.snakeyaml;

import java.util.Collections;
import org.yaml.snakeyaml.inspector.TrustedPrefixesTagInspector;
import org.yaml.snakeyaml.inspector.TrustedTagInspector;

public class YamlCreator {

  public static Yaml allowClassPrefix(String start) {
    LoaderOptions options = new LoaderOptions();
    options.setTagInspector(new TrustedPrefixesTagInspector(Collections.singletonList(start)));
    Yaml yaml = new Yaml(options);
    return yaml;
  }

  public static Yaml allowAnyClass() {
    return new Yaml(trustedLoaderOptions());
  }

  public static LoaderOptions trustedLoaderOptions() {
    LoaderOptions options = new LoaderOptions();
    options.setTagInspector(new TrustedTagInspector());
    return options;
  }

  public static LoaderOptions trustPrefixLoaderOptions(String packageName) {
    LoaderOptions options = new LoaderOptions();
    options
        .setTagInspector(new TrustedPrefixesTagInspector(Collections.singletonList(packageName)));
    return options;
  }
}
