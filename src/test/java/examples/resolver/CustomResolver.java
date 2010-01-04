/**
 * Copyright (c) 2008-2010 Andrey Somov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package examples.resolver;

import org.yaml.snakeyaml.nodes.Tags;
import org.yaml.snakeyaml.resolver.Resolver;

public class CustomResolver extends Resolver {

    /*
     * do not resolve float and timestamp
     */
    protected void addImplicitResolvers() {
        addImplicitResolver(Tags.BOOL, BOOL, "yYnNtTfFoO");
        // addImplicitResolver(Tags.FLOAT, FLOAT, "-+0123456789.");
        addImplicitResolver(Tags.INT, INT, "-+0123456789");
        addImplicitResolver(Tags.MERGE, MERGE, "<");
        addImplicitResolver(Tags.NULL, NULL, "~nN\0");
        addImplicitResolver(Tags.NULL, EMPTY, null);
        // addImplicitResolver(Tags.TIMESTAMP, TIMESTAMP, "0123456789");
        addImplicitResolver(Tags.VALUE, VALUE, "=");
    }
}
