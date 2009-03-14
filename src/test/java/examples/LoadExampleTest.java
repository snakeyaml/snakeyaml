/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package examples;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;

public class LoadExampleTest extends TestCase {
    @SuppressWarnings("unchecked")
    public void testLoad() {
        Yaml yaml = new Yaml();
        String document = "\n- Hesperiidae\n- Papilionidae\n- Apatelodidae\n- Epiplemidae";
        System.out.println(document);
        List<String> list = (List<String>) yaml.load(document);
        System.out.println(list);
        assertEquals("[Hesperiidae, Papilionidae, Apatelodidae, Epiplemidae]", list.toString());
    }

    @SuppressWarnings("unchecked")
    public void testLoadFromString() {
        Yaml yaml = new Yaml();
        String document = "hello: 25";
        Map map = (Map) yaml.load(document);
        assertEquals("{hello=25}", map.toString());
        assertEquals(new Integer(25), map.get("hello"));
    }

    public void testLoadFromStream() throws FileNotFoundException {
        InputStream input = new FileInputStream(new File("src/test/resources/reader/utf-8.txt"));
        Yaml yaml = new Yaml();
        Object data = yaml.load(input);
        assertEquals("test", data);
        //
        data = yaml.load(new ByteArrayInputStream("test2".getBytes()));
        assertEquals("test2", data);
    }

    public void testLoadManyDocuments() throws FileNotFoundException {
        InputStream input = new FileInputStream(new File(
                "src/test/resources/specification/example2_28.yaml"));
        Yaml yaml = new Yaml();
        int counter = 0;
        for (Object data : yaml.loadAll(input)) {
            System.out.println(data);
            counter++;
        }
        assertEquals(3, counter);
    }
}
