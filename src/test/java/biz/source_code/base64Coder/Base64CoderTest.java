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
package biz.source_code.base64Coder;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import junit.framework.TestCase;

public class Base64CoderTest extends TestCase {

  public void testDecode() throws UnsupportedEncodingException {
    check("Aladdin:open sesame", "QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
    check("a", "YQ==");
    check("aa", "YWE=");
    check("a=", "YT0=");
    check("", "");
  }

  public void testFailure1() throws UnsupportedEncodingException {
    try {
      Base64.getDecoder().decode("YQ=");
      fail();
    } catch (Exception e) {
      assertEquals("Input byte array has wrong 4-byte ending unit", e.getMessage());
    }
  }

  public void testFailure2() throws UnsupportedEncodingException {
    checkInvalid("\tWE=");
    checkInvalid("Y\tE=");
    checkInvalid("YW\t=");
    checkInvalid("YWE\t");
    //
    checkInvalid("©WE=");
    checkInvalid("Y©E=");
    checkInvalid("YW©=");
    checkInvalid("YWE©");
  }

  private void checkInvalid(String encoded) {
    try {
      Base64.getDecoder().decode(encoded);
      fail("Illegal character.");
    } catch (Exception e) {
      assertTrue(e.getMessage().startsWith("Illegal base64 character "));
    }
  }

  private void check(String text, String encoded) throws UnsupportedEncodingException {
    byte[] s1 = Base64.getEncoder().encode(text.getBytes());
    String t1 = new String(s1);
    assertEquals(encoded, t1);
    byte[] s2 = Base64.getDecoder().decode(encoded);
    String t2 = new String(s2, StandardCharsets.UTF_8);
    assertEquals(text, t2);
  }
}
