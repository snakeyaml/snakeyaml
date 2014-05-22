package org.yaml.snakeyaml.issues.issue192;

import junit.framework.TestCase;
import org.yaml.snakeyaml.Yaml;

public class EqualsSignTest extends TestCase {

    public void testMappingNode() {
        new Yaml().load("part1: =");
    }

    public void testScalarNode() {
        new Yaml().load("=");
    }
}



