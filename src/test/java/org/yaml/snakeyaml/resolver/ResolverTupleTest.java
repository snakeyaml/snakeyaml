/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.resolver;

import java.util.regex.Pattern;

import junit.framework.TestCase;

public class ResolverTupleTest extends TestCase {

    public void testToString() {
        ResolverTuple tuple = new ResolverTuple("dice", Pattern.compile("\\d+"));
        assertEquals("Tuple tag=dice regexp=\\d+", tuple.toString());
    }
}
