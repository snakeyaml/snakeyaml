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

import static org.yaml.snakeyaml.env.EnvScalarConstructor.ENV_FORMAT;

import java.util.regex.Matcher;
import junit.framework.TestCase;

/*
 * ${VARIABLE:-default} evaluates to default if VARIABLE is unset or empty in the environment.
 * ${VARIABLE-default} evaluates to default only if VARIABLE is unset in the environment.
 *
 * Similarly, the following syntax allows you to specify mandatory variables:
 *
 * ${VARIABLE:?err} exits with an error message containing err if VARIABLE is unset or empty in the
 * environment. ${VARIABLE?err} exits with an error message containing err if VARIABLE is unset in
 * the environment.
 */
public class EnvFormatTest extends TestCase {

  public void testMatchBasic() {
    assertTrue(ENV_FORMAT.matcher("${V}").matches());
    assertTrue(ENV_FORMAT.matcher("${PATH}").matches());
    assertTrue(ENV_FORMAT.matcher("${VARIABLE}").matches());
    assertTrue(ENV_FORMAT.matcher("${ VARIABLE}").matches());
    assertTrue(ENV_FORMAT.matcher("${ VARIABLE}").matches());
    assertTrue(ENV_FORMAT.matcher("${ VARIABLE }").matches());
    assertTrue(ENV_FORMAT.matcher("${ VARIABLE}").matches());
    assertTrue(ENV_FORMAT.matcher("${\tVARIABLE  }").matches());

    Matcher matcher = ENV_FORMAT.matcher("${VARIABLE}");
    matcher.matches();
    assertEquals("VARIABLE", matcher.group("name"));
    assertNull(matcher.group("value"));
    assertNull(matcher.group("separator"));

    assertFalse(ENV_FORMAT.matcher("${VARI ABLE}").matches());
  }

  public void testMatchDefault() {
    assertTrue(ENV_FORMAT.matcher("${VARIABLE-default}").matches());
    assertTrue(ENV_FORMAT.matcher("${ VARIABLE-default}").matches());
    assertTrue(ENV_FORMAT.matcher("${ VARIABLE-default }").matches());
    assertTrue(ENV_FORMAT.matcher("${ VARIABLE-default}").matches());
    assertTrue(ENV_FORMAT.matcher("${ VARIABLE-}").matches());

    Matcher matcher = ENV_FORMAT.matcher("${VARIABLE-default}");
    matcher.matches();
    assertEquals("VARIABLE", matcher.group("name"));
    assertEquals("default", matcher.group("value"));
    assertEquals("-", matcher.group("separator"));

    assertFalse(ENV_FORMAT.matcher("${VARIABLE -default}").matches());
    assertFalse(ENV_FORMAT.matcher("${VARIABLE - default}").matches());
    assertFalse(ENV_FORMAT.matcher("${VARIABLE -default}").matches());
  }

  public void testMatchDefaultOrEmpty() {
    assertTrue(ENV_FORMAT.matcher("${VARIABLE:-default}").matches());
    assertTrue(ENV_FORMAT.matcher("${ VARIABLE:-default }").matches());
    assertTrue(ENV_FORMAT.matcher("${ VARIABLE:-}").matches());

    Matcher matcher = ENV_FORMAT.matcher("${VARIABLE:-default}");
    matcher.matches();
    assertEquals("VARIABLE", matcher.group("name"));
    assertEquals("default", matcher.group("value"));
    assertEquals(":-", matcher.group("separator"));

    assertFalse(ENV_FORMAT.matcher("${VARIABLE :-default}").matches());
    assertFalse(ENV_FORMAT.matcher("${VARIABLE : -default}").matches());
    assertFalse(ENV_FORMAT.matcher("${VARIABLE : - default}").matches());
  }

  public void testMatchErrorDefaultOrEmpty() {
    assertTrue(ENV_FORMAT.matcher("${VARIABLE:?err}").matches());
    assertTrue(ENV_FORMAT.matcher("${ VARIABLE:?err }").matches());
    assertTrue(ENV_FORMAT.matcher("${ VARIABLE:? }").matches());

    Matcher matcher = ENV_FORMAT.matcher("${VARIABLE:?err}");
    matcher.matches();
    assertEquals("VARIABLE", matcher.group("name"));
    assertEquals("err", matcher.group("value"));
    assertEquals(":?", matcher.group("separator"));

    assertFalse(ENV_FORMAT.matcher("${ VARIABLE :?err }").matches());
    assertFalse(ENV_FORMAT.matcher("${ VARIABLE : ?err }").matches());
    assertFalse(ENV_FORMAT.matcher("${ VARIABLE : ? err }").matches());
  }

  public void testMatchErrorDefault() {
    assertTrue(ENV_FORMAT.matcher("${VARIABLE?err}").matches());
    assertTrue(ENV_FORMAT.matcher("${ VARIABLE:?err }").matches());
    assertTrue(ENV_FORMAT.matcher("${ VARIABLE:?}").matches());

    Matcher matcher = ENV_FORMAT.matcher("${ VARIABLE?err }");
    matcher.matches();
    assertEquals("VARIABLE", matcher.group("name"));
    assertEquals("err", matcher.group("value"));
    assertEquals("?", matcher.group("separator"));

    assertFalse(ENV_FORMAT.matcher("${ VARIABLE ?err }").matches());
    assertFalse(ENV_FORMAT.matcher("${ VARIABLE ?err }").matches());
    assertFalse(ENV_FORMAT.matcher("${ VARIABLE ? err }").matches());
  }
}
