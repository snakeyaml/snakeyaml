/**
 * See LICENSE file in distribution for copyright and licensing information.
 */
package examples;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.yaml.snakeyaml.Yaml;

public class SpringTest extends TestCase {
    public void testSimple() {
        ApplicationContext context = new ClassPathXmlApplicationContext("examples/spring.xml");
        Yaml yaml = (Yaml) context.getBean("standardYaml");
        assertNotNull(yaml);
        //
        yaml = (Yaml) context.getBean("javabeanYaml");
        assertNotNull(yaml);
        //
        yaml = (Yaml) context.getBean("snakeYaml");
        assertNotNull(yaml);
    }
}