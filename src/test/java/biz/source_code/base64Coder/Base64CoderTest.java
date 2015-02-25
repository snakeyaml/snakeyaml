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
package biz.source_code.base64Coder;

import java.io.UnsupportedEncodingException;

import junit.framework.TestCase;

import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

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
            Base64Coder.decode("YQ=".toCharArray());
            fail();
        } catch (Exception e) {
            assertEquals("Length of Base64 encoded input string is not a multiple of 4.",
                    e.getMessage());
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
            Base64Coder.decode(encoded.toCharArray());
            fail("Illegal chanracter.");
        } catch (Exception e) {
            assertEquals("Illegal character in Base64 encoded data.", e.getMessage());
        }
    }

    private void check(String text, String encoded) throws UnsupportedEncodingException {
        char[] s1 = Base64Coder.encode(text.getBytes("UTF-8"));
        String t1 = new String(s1);
        assertEquals(encoded, t1);
        byte[] s2 = Base64Coder.decode(encoded.toCharArray());
        String t2 = new String(s2, "UTF-8");
        assertEquals(text, t2);
    }
}
