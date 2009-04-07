/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package examples;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class CollectionStyleTest extends TestCase {
    public void testNestedStyle() {
        Yaml yaml = new Yaml();
        String document = "  a: 1\n  b:\n    c: 3\n    d: 4\n";
        System.out.println(document);
        System.out.println(yaml.dump(yaml.load(document)));
        assertEquals("a: 1\nb: {c: 3, d: 4}\n", yaml.dump(yaml.load(document)));
    }

    public void testNestedStyle2() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
        String document = "  a: 1\n  b:\n    c: 3\n    d: 4\n";
        System.out.println(yaml.dump(yaml.load(document)));
        assertEquals("a: 1\nb:\n  c: 3\n  d: 4\n", yaml.dump(yaml.load(document)));
    }
}
