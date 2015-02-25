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
package org.yaml.snakeyaml.recursive;

import java.util.Date;

import junit.framework.TestCase;

import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class Human_WithArrayOfChildrenTest extends TestCase {

    public static class Human_WithArrayOfChildren extends AbstractHuman {

        private Human_WithArrayOfChildren father;
        private Human_WithArrayOfChildren mother;
        private Human_WithArrayOfChildren partner;
        private Human_WithArrayOfChildren bankAccountOwner;
        protected Human_WithArrayOfChildren[] children;

        public Human_WithArrayOfChildren() {
            children = new Human_WithArrayOfChildren[0];
        }

        public Human_WithArrayOfChildren getFather() {
            return father;
        }

        public void setFather(Human_WithArrayOfChildren father) {
            this.father = father;
        }

        public Human_WithArrayOfChildren getMother() {
            return mother;
        }

        public void setMother(Human_WithArrayOfChildren mother) {
            this.mother = mother;
        }

        public Human_WithArrayOfChildren getPartner() {
            return partner;
        }

        public void setPartner(Human_WithArrayOfChildren partner) {
            this.partner = partner;
        }

        public Human_WithArrayOfChildren getBankAccountOwner() {
            return bankAccountOwner;
        }

        public void setBankAccountOwner(Human_WithArrayOfChildren bankAccountOwner) {
            this.bankAccountOwner = bankAccountOwner;
        }

        public Human_WithArrayOfChildren[] getChildren() {
            return children;
        }

        public void setChildren(Human_WithArrayOfChildren[] children) {
            this.children = children;
        }

    }

    private Human_WithArrayOfChildren createSon() {
        Human_WithArrayOfChildren father = new Human_WithArrayOfChildren();
        father.setName("Father");
        father.setBirthday(new Date(1000000000));
        father.setBirthPlace("Leningrad");
        father.setBankAccountOwner(father);
        //
        Human_WithArrayOfChildren mother = new Human_WithArrayOfChildren();
        mother.setName("Mother");
        mother.setBirthday(new Date(100000000000L));
        mother.setBirthPlace("Saint-Petersburg");
        father.setPartner(mother);
        mother.setPartner(father);
        mother.setBankAccountOwner(father);
        //
        Human_WithArrayOfChildren son = new Human_WithArrayOfChildren();
        son.setName("Son");
        son.setBirthday(new Date(310000000000L));
        son.setBirthPlace("Munich");
        son.setBankAccountOwner(father);
        son.setFather(father);
        son.setMother(mother);
        //
        Human_WithArrayOfChildren daughter = new Human_WithArrayOfChildren();
        daughter.setName("Daughter");
        daughter.setBirthday(new Date(420000000000L));
        daughter.setBirthPlace("New York");
        daughter.setBankAccountOwner(father);
        daughter.setFather(father);
        daughter.setMother(mother);
        //
        Human_WithArrayOfChildren[] children = new Human_WithArrayOfChildren[] { son, daughter };
        father.setChildren(children);
        mother.setChildren(children);
        //
        return son;
    }

    private void checkSon(Human_WithArrayOfChildren son) {
        assertNotNull(son);
        assertEquals("Son", son.getName());

        Human_WithArrayOfChildren father2 = son.getFather();
        assertEquals("Father", father2.getName());
        assertEquals("Mother", son.getMother().getName());
        assertSame(father2, father2.getBankAccountOwner());
        assertSame(father2.getPartner(), son.getMother());
        assertSame(father2, son.getMother().getPartner());

        Human_WithArrayOfChildren[] fathersChildren = father2.getChildren();
        assertEquals(2, fathersChildren.length);
        Human_WithArrayOfChildren[] mothersChildren = father2.getPartner().getChildren();
        assertEquals(2, mothersChildren.length);
        assertSame(mothersChildren, fathersChildren);

        for (Object child : fathersChildren) {
            // check if type descriptor was correct
            assertSame(Human_WithArrayOfChildren.class, child.getClass());
        }
    }

    public void testChildrenArray() {
        Constructor constructor = new Constructor(Human_WithArrayOfChildren.class);
        TypeDescription HumanWithChildrenArrayDescription = new TypeDescription(
                Human_WithArrayOfChildren.class);
        HumanWithChildrenArrayDescription.putListPropertyType("children",
                Human_WithArrayOfChildren.class);
        constructor.addTypeDescription(HumanWithChildrenArrayDescription);
        Human_WithArrayOfChildren son = createSon();
        Yaml yaml = new Yaml(constructor);
        String output = yaml.dump(son);
        // System.out.println(output);
        String etalon = Util.getLocalResource("recursive/with-childrenArray.yaml");
        assertEquals(etalon, output);
        //
        Human_WithArrayOfChildren son2 = (Human_WithArrayOfChildren) yaml.load(output);
        checkSon(son2);
    }

    public void testDumpChildrenArrayWithoutRootTag() {
        Yaml yaml = new Yaml();
        Human_WithArrayOfChildren son = createSon();
        String output = yaml.dumpAsMap(son);
        // System.out.println(output);
        String etalon = Util.getLocalResource("recursive/with-childrenArray-no-root-tag.yaml");
        assertEquals(etalon, output);
    }

    public void testParseChildrenArrayWithoutRootTag() {
        Constructor constructor = new Constructor(Human_WithArrayOfChildren.class);
        TypeDescription HumanWithChildrenArrayDescription = new TypeDescription(
                Human_WithArrayOfChildren.class);
        HumanWithChildrenArrayDescription.putListPropertyType("children",
                Human_WithArrayOfChildren.class);
        constructor.addTypeDescription(HumanWithChildrenArrayDescription);
        Yaml yaml = new Yaml(constructor);
        String doc = Util.getLocalResource("recursive/with-childrenArray-no-root-tag.yaml");
        Human_WithArrayOfChildren son2 = (Human_WithArrayOfChildren) yaml.load(doc);
        checkSon(son2);
    }
}
