/**
 * Copyright (c) 2008, SnakeYAML
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
package org.yaml.snakeyaml.issues.issue440;

import junit.framework.TestCase;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class LiteralWithTrailingSpacesTest extends TestCase {

    public void testLiteral() {
        DumperOptions options = new DumperOptions();
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.LITERAL);
        Yaml yaml = new Yaml(options);
        String correct = "this is some text with \"quotes\" and\nline breaks";
        String correctYaml = yaml.dump(correct);
        assertEquals("|-\n" +
                "  this is some text with \"quotes\" and\n" +
                "  line breaks\n", correctYaml);
    }

    public void testTrimTrailingWhiteSpace() {
        Yaml yaml = new Yaml();
        assertEquals("trailing", yaml.load("trailing "));
        assertEquals("trailing", yaml.load("trailing\r"));
        assertEquals("trailing", yaml.load("trailing\n"));
        assertEquals("trailing", yaml.load("trailing\r\n"));
        assertEquals("trailing", yaml.load("trailing\t\n"));
        assertEquals("trailing", yaml.load("trailing\n\n"));
        assertEquals("trailing", yaml.load("trailing\n \r \n"));
    }

    public void testLiteralWithNewLine() {
        DumperOptions options = new DumperOptions();
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.LITERAL);
        Yaml yaml = new Yaml(options);
        String inCorrect = "this is some text with \"quotes\" and \nline breaks";
        String inCorrectYaml = yaml.dump(inCorrect);
        //System.out.println(inCorrectYaml);
        assertEquals("\"this is some text with \\\"quotes\\\" and \\nline breaks\"\n", inCorrectYaml);
        //TODO assertTrue(inCorrectYaml.contains("|"));
    }
}
