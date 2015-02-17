/**
 * Copyright (c) 2008, http://www.snakeyaml.org
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
package examples;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

public class DiceExampleTest extends TestCase {
    public void testRepresenter() {
        Dice dice = new Dice(3, 6);
        DumperOptions options = new DumperOptions();
        options.setAllowReadOnlyProperties(true);
        Yaml yaml = new Yaml(options);
        String output = yaml.dump(dice);
        assertEquals("!!examples.Dice {a: 3, b: 6}\n", output);
    }

    public void testDiceRepresenter() {
        Dice dice = new Dice(3, 6);
        Map<String, Dice> data = new HashMap<String, Dice>();
        data.put("gold", dice);
        Yaml yaml = new Yaml(new DiceRepresenter(), new DumperOptions());
        String output = yaml.dump(data);
        assertEquals("{gold: !dice '3d6'}\n", output);
    }

    class DiceRepresenter extends Representer {
        public DiceRepresenter() {
            this.representers.put(Dice.class, new RepresentDice());
        }

        private class RepresentDice implements Represent {
            public Node representData(Object data) {
                Dice dice = (Dice) data;
                String value = dice.getA() + "d" + dice.getB();
                return representScalar(new Tag("!dice"), value);
            }
        }
    }

    class DiceConstructor extends Constructor {
        public DiceConstructor() {
            this.yamlConstructors.put(new Tag("!dice"), new ConstructDice());
        }

        private class ConstructDice extends AbstractConstruct {
            public Object construct(Node node) {
                String val = (String) constructScalar((ScalarNode) node);
                int position = val.indexOf('d');
                Integer a = new Integer(val.substring(0, position));
                Integer b = new Integer(val.substring(position + 1));
                return new Dice(a, b);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void testConstructor() {
        Yaml yaml = new Yaml(new DiceConstructor());
        Object data = yaml.load("{initial hit points: !dice '8d4'}");
        Map<String, Dice> map = (Map<String, Dice>) data;
        assertEquals(new Dice(8, 4), map.get("initial hit points"));
    }

    // the tag must start with a digit
    @SuppressWarnings("unchecked")
    public void testImplicitResolver() {
        Yaml yaml = new Yaml(new DiceConstructor(), new DiceRepresenter());
        // the tag must start with a digit
        yaml.addImplicitResolver(new Tag("!dice"), Pattern.compile("\\d+d\\d+"), "123456789");
        // dump
        Map<String, Dice> treasure = new HashMap<String, Dice>();
        treasure.put("treasure", new Dice(10, 20));
        String output = yaml.dump(treasure);
        assertEquals("{treasure: 10d20}\n", output);
        // load
        Object data = yaml.load("{damage: 5d10}");
        Map<String, Dice> map = (Map<String, Dice>) data;
        assertEquals(new Dice(5, 10), map.get("damage"));
    }

    // the tag may start with anything
    @SuppressWarnings("unchecked")
    public void testImplicitResolverWithNull() {
        Yaml yaml = new Yaml(new DiceConstructor(), new DiceRepresenter());
        // the tag may start with anything
        yaml.addImplicitResolver(new Tag("!dice"), Pattern.compile("\\d+d\\d+"), null);
        // dump
        Map<String, Dice> treasure = new HashMap<String, Dice>();
        treasure.put("treasure", new Dice(10, 20));
        String output = yaml.dump(treasure);
        assertEquals("{treasure: 10d20}\n", output);
        // load
        Object data = yaml.load("{damage: 5d10}");
        Map<String, Dice> map = (Map<String, Dice>) data;
        assertEquals(new Dice(5, 10), map.get("damage"));
    }

    static class DiceBean {
        public Dice treasure;

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof DiceBean))
                return false;

            DiceBean diceBean = (DiceBean) o;
            if (treasure != null ? !treasure.equals(diceBean.treasure) : diceBean.treasure != null)
                return false;
            return true;
        }

        @Override
        public int hashCode() {
            return treasure != null ? treasure.hashCode() : 0;
        }
    }

    public void testImplicitResolverJavaBean() {
        Yaml yaml = new Yaml(new DiceConstructor(), new DiceRepresenter());
        // the tag must start with a digit
        yaml.addImplicitResolver(new Tag("!dice"), Pattern.compile("\\d+d\\d+"), "123456789");
        // dump
        DiceBean bean = new DiceBean();
        bean.treasure = new Dice(10, 20);
        String output = yaml.dump(bean);
        assertEquals("!!examples.DiceExampleTest$DiceBean {treasure: 10d20}\n", output);
        // load
        Object loaded = yaml.load(output);
        assertEquals(loaded, bean);
    }
}
