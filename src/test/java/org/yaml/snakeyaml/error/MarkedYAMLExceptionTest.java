/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.error;

import junit.framework.TestCase;

public class MarkedYAMLExceptionTest extends TestCase {

    public void testToString1() {
        Mark mark = new Mark("test1", 0, 0, 0, "*The first line.\nThe last line.", 0);
        MarkedYAMLException exception = new MarkedYAMLException(null, null, "Error happened", mark);
        assertTrue(exception.toString().contains("Error happened"));
        assertTrue(exception.toString().contains("The first line"));
        assertTrue(exception.toString().contains("\"test1\""));
    }

    public void testToString2() {
        Mark mark = new Mark("search", 0, 0, 0, "*The first line.\nThe last line.", 0);
        MarkedYAMLException exception = new MarkedYAMLException("See http://www.google.com", mark,
                "Error2 happened", mark);
        assertTrue(exception.toString().contains("Error2 happened"));
        assertTrue(exception.toString().contains("The first line"));
        assertTrue(exception.toString().contains("\"search\""));
    }

}
