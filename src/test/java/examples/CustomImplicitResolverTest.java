/**
 * Copyright (c) 2008, http://www.snakeyaml.org
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
package examples;

import java.math.BigDecimal;
import java.util.Map;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.Tag;

/**
 * Use custom implicit resolver when the runtime class is not defined.
 * http://code.google.com/p/snakeyaml/issues/detail?id=75
 */
public class CustomImplicitResolverTest extends TestCase {
    private final Tag CUSTOM_TAG = new Tag("!BigDecimalDividedBy100");
    private final Pattern CUSTOM_PATTERN = Pattern.compile("\\d+%");

    @SuppressWarnings("unchecked")
    public void testImplicit() {
        Yaml yaml = new Yaml(new BigConstructor());
        yaml.addImplicitResolver(CUSTOM_TAG, CUSTOM_PATTERN, "-0123456789");
        Map<String, Object> obj = (Map<String, Object>) yaml.load("bar: 50%");
        assertEquals("0.5", obj.get("bar").toString());
        assertEquals(BigDecimal.class, obj.get("bar").getClass());
    }

    public void testImplicitFailure() {
        Yaml yaml = new Yaml(new BigConstructor());
        yaml.addImplicitResolver(CUSTOM_TAG, Pattern.compile("\\d+%"), "-0123456789");
        try {
            yaml.load("bar: !!float 50%");
            fail("Both implicit and explicit are present.");
        } catch (NumberFormatException e) {
            assertEquals("For input string: \"50%\"", e.getMessage());
        }
    }

    class BigConstructor extends SafeConstructor {
        public BigConstructor() {
            this.yamlConstructors.put(CUSTOM_TAG, new ConstructBig());
        }

        private class ConstructBig extends AbstractConstruct {
            public Object construct(Node node) {
                String val = (String) constructScalar((ScalarNode) node);
                return new BigDecimal(val.substring(0, val.length() - 1))
                        .divide(new BigDecimal(100));
            }
        }
    }
}
