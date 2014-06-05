package org.yaml.snakeyaml.issues.issue193;

import org.yaml.snakeyaml.Yaml;

public class TestYaml {

    public static abstract class BeanA {

        public abstract Object getId();

    }

    public static class BeanA1 extends BeanA {

        private Long id;

        // @Override
        public Long getId() {
            return id;
        }


        public void setId(Long id) {
            this.id = id;
        }

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    public static void main(String[] args) {

        System.out.println("test...");
        BeanA1 b = new BeanA1();
        b.setId(2l);
        b.setName("name1");
        Yaml yaml = new Yaml();
        String dump = yaml.dump(b);

        System.out.println("dump:" + dump);

        dump = "!!org.yaml.snakeyaml.issues.issue193.TestYaml$BeanA1 {id: 2, name: name1}";

        yaml.load(dump);
    }
}
