package org.yaml.snakeyaml.issues.issue479;

import junit.framework.TestCase;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.InputStream;

public class MergeKeyDeepMergeTest extends TestCase {

    public void testMergeKeyDeepMerge() {
        Constructor constructor = new Constructor(DemoProperty.class);
        Representer representer = new Representer();
        representer.getPropertyUtils().setSkipMissingProperties(true);
        Yaml yaml = new Yaml(constructor, representer);

        InputStream inputStream = MergeKeyDeepMergeTest.class.getResourceAsStream("/issues/issue479.yaml");
        DemoProperty property = yaml.load(inputStream);
        String output = yaml.dump(property);
//        System.out.println(output);

        String v = "!!org.yaml.snakeyaml.issues.issue479.DemoProperty\n" +
                "system:\n" +
                "  erp:\n" +
                "    mysql: {db_host: mysql.avatar2.test, db_name: erp, db_password: default, db_port: 3306, db_user: secret}\n" +
                "    test1: '2'\n";
        assertEquals("mysql.avatar2.test", property.getSystem().get("erp").getMysql().getDb_host());
        assertEquals(v, output);

    }
}
