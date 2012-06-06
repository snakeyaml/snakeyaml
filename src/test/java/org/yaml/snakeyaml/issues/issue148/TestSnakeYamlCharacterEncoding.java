/**
 * Copyright (c) 2008-2012, http://www.snakeyaml.org
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
package org.yaml.snakeyaml.issues.issue148;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.emitter.YamlCharacterEncoding;
import org.yaml.snakeyaml.reader.ReaderException;

public class TestSnakeYamlCharacterEncoding {

    /**
     * Test serialization and deserialization of Unicode codepoints in plane 0.
     */
    public static void main(String[] args) {
        Yaml yaml = new Yaml();

        // test serialization of all Unicode codepoints
        for (int c = Character.MIN_VALUE; c <= Character.MAX_VALUE; c++) {
            String original = Character.toString((char) c);
            String serialized = yaml.dump(original);

            // "On output, a YAML processor must only produce these acceptable
            // characters,
            // and should also escape all non-printable Unicode characters."
            for (int i = 0; i < serialized.length(); i++) {
                int cp = (int) serialized.charAt(i);
                if (!YamlCharacterEncoding.isAcceptable(cp))
                    System.err.printf(
                            "U+%04x: Serialization produced result with unacceptable U+%04x\n", c,
                            cp);
                if (!YamlCharacterEncoding.isPrintable(cp))
                    System.err.printf(
                            "U+%04x: Serialization produced result with nonprintable U+%04x\n", c,
                            cp);
            }
        }

        // test deserialization of non-escaped codepoints
        for (int c = Character.MIN_VALUE; c <= Character.MAX_VALUE; c++) {
            // ignore breaks, which have special meaning
            if (c == 0x0A || c == 0x0D || c == 0x85 || c == 0x2028 || c == 0x2029)
                continue;
            if (!YamlCharacterEncoding.isAcceptable(c) || c == 0x27)
                continue;
            String expected = Character.toString((char) c);
            String serialized = "'" + expected + "'";

            String result;
            try {
                result = yaml.load(serialized).toString();
            } catch (ReaderException e) {
                System.err
                        .printf("U+%04x: Deserialization threw ReaderException for an acceptable character\n",
                                c);
                continue;
            }
            if (!result.equals(expected))
                System.err.printf("U+%04x: Deserialization incorrect: %s\n", c,
                        YamlCharacterEncoding.hexdump(result));
        }

        // test deserialization of escaped codepoints
        // "Any such characters must be presented using escape sequences."
        for (int c = Character.MIN_VALUE; c <= Character.MAX_VALUE; c++) {
            String expected = Character.toString((char) c);
            String serialized = String.format("\"\\u%04x\"", c);

            String result;
            try {
                result = yaml.load(serialized).toString();
            } catch (ReaderException e) {
                System.err
                        .printf("U+%04x: Deserialization threw ReaderException for an acceptable escaped character\n",
                                c);
                continue;
            }
            if (!result.equals(expected))
                System.err.printf("U+%04x: Deserialization of escaped character incorrect: %s\n",
                        c, YamlCharacterEncoding.hexdump(result));
        }
    }

}
