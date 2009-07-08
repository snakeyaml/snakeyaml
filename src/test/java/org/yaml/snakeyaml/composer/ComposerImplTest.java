/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.composer;

import java.io.StringReader;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;

public class ComposerImplTest extends TestCase {

    public void testGetNode() {
        String data = "american:\n  - Boston Red Sox";
        Yaml yaml = new Yaml();
        Node node = yaml.compose(new StringReader(data));
        assertNotNull(node);
        assertTrue(node instanceof MappingNode);
        String data2 = "---\namerican:\n- Boston Red Sox";
        Node node2 = yaml.compose(new StringReader(data2));
        assertNotNull(node2);
        assertFalse(node.equals(node2));
    }

    public void testComposeBean() {
        String data = "!!org.yaml.snakeyaml.composer.ComposerImplTest$BeanToCompose {name: Bill, age: 18}";
        Yaml yaml = new Yaml();
        Node node = yaml.compose(new StringReader(data));
        assertNotNull(node);
        assertTrue(node instanceof MappingNode);
        assertEquals(
                "tag:yaml.org,2002:org.yaml.snakeyaml.composer.ComposerImplTest$BeanToCompose",
                node.getTag());
        assertEquals(NodeId.mapping, node.getNodeId());
        assertEquals(Object.class, node.getType());
    }

    public static class BeanToCompose {
        private String name;
        private int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
