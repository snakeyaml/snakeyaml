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
package org.yaml.snakeyaml.issues.issue82;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

/**
 * @see issue 82: property order influence when aliased in generic collection
 */
public class PropOrderInfluenceWhenAliasedInGenericCollectionTest extends TestCase {

    public static interface Account {
    }

    public static class GeneralAccount implements Account {
        public String name = "General";
    }

    public static class SuperSaverAccount extends GeneralAccount {

        public SuperSaverAccount() {
            name = "SuperSaver";
        }
    }

    public static class CustomerAB {
        public Collection<Account> aAll;
        public Collection<GeneralAccount> bGeneral;

        @Override
        public String toString() {
            return "CustomerAB";
        }
    }

    public static class CustomerBA {
        public Collection<GeneralAccount> aGeneral;
        public Collection<Account> bAll;
    }

    public static class CustomerAB_MapValue {
        public Collection<Account> aAll;
        public Map<String, GeneralAccount> bGeneralMap;

        @Override
        public String toString() {
            return "CustomerAB_MapValue";
        }
    }

    public static class CustomerAB_MapKey {
        public Collection<Account> aAll;
        public Map<GeneralAccount, String> bGeneralMap;

        @Override
        public String toString() {
            return "CustomerAB_MapKey";
        }
    }

    public static class CustomerAB_Property {
        public Account acc;
        public Collection<GeneralAccount> bGeneral;

        @Override
        public String toString() {
            return "CustomerAB_Property";
        }
    }

    public void testAB() {
        SuperSaverAccount supersaver = new SuperSaverAccount();
        GeneralAccount generalAccount = new GeneralAccount();

        CustomerAB customerAB = new CustomerAB();
        ArrayList<Account> all = new ArrayList<Account>();
        all.add(supersaver);
        all.add(generalAccount);
        ArrayList<GeneralAccount> general = new ArrayList<GeneralAccount>();
        general.add(generalAccount);
        general.add(supersaver);

        customerAB.aAll = all;
        customerAB.bGeneral = general;

        Yaml yaml = new Yaml();
        String dump = yaml.dump(customerAB);
        // System.out.println(dump);
        CustomerAB parsed = (CustomerAB) yaml.load(dump);
        assertNotNull(parsed);
    }

    public void testAB_Set() {
        SuperSaverAccount supersaver = new SuperSaverAccount();
        GeneralAccount generalAccount = new GeneralAccount();

        CustomerAB customerAB = new CustomerAB();
        ArrayList<Account> all = new ArrayList<Account>();
        all.add(supersaver);
        all.add(generalAccount);
        Set<GeneralAccount> general = new HashSet<GeneralAccount>();
        general.add(generalAccount);
        general.add(supersaver);

        customerAB.aAll = all;
        customerAB.bGeneral = general;

        Yaml yaml = new Yaml();
        String dump = yaml.dump(customerAB);
        // System.out.println(dump);
        CustomerAB parsed = (CustomerAB) yaml.load(dump);
        assertNotNull(parsed);
    }

    public void testABWithCustomTag() {
        SuperSaverAccount supersaver = new SuperSaverAccount();
        GeneralAccount generalAccount = new GeneralAccount();

        CustomerAB customerAB = new CustomerAB();
        ArrayList<Account> all = new ArrayList<Account>();
        all.add(supersaver);
        all.add(generalAccount);
        ArrayList<GeneralAccount> general = new ArrayList<GeneralAccount>();
        general.add(generalAccount);
        general.add(supersaver);

        customerAB.aAll = all;
        customerAB.bGeneral = general;

        Constructor constructor = new Constructor();
        Representer representer = new Representer();
        Tag generalAccountTag = new Tag("!GA");
        constructor
                .addTypeDescription(new TypeDescription(GeneralAccount.class, generalAccountTag));
        representer.addClassTag(GeneralAccount.class, generalAccountTag);

        Yaml yaml = new Yaml(constructor, representer);
        String dump = yaml.dump(customerAB);
        // System.out.println(dump);
        CustomerAB parsed = (CustomerAB) yaml.load(dump);
        assertNotNull(parsed);
    }

    public void testABProperty() {
        SuperSaverAccount supersaver = new SuperSaverAccount();
        GeneralAccount generalAccount = new GeneralAccount();

        CustomerAB_Property customerAB_property = new CustomerAB_Property();
        ArrayList<Account> all = new ArrayList<Account>();
        all.add(supersaver);
        all.add(generalAccount);
        ArrayList<GeneralAccount> general = new ArrayList<GeneralAccount>();
        general.add(generalAccount);
        general.add(supersaver);

        customerAB_property.acc = generalAccount;
        customerAB_property.bGeneral = general;

        Constructor constructor = new Constructor();
        Representer representer = new Representer();

        Yaml yaml = new Yaml(constructor, representer);
        String dump = yaml.dump(customerAB_property);
        // System.out.println(dump);
        CustomerAB_Property parsed = (CustomerAB_Property) yaml.load(dump);
        assertNotNull(parsed);
    }

    public void testABPropertyWithCustomTag() {
        SuperSaverAccount supersaver = new SuperSaverAccount();
        GeneralAccount generalAccount = new GeneralAccount();

        CustomerAB_Property customerAB_property = new CustomerAB_Property();
        ArrayList<Account> all = new ArrayList<Account>();
        all.add(supersaver);
        all.add(generalAccount);
        ArrayList<GeneralAccount> general = new ArrayList<GeneralAccount>();
        general.add(generalAccount);
        general.add(supersaver);

        customerAB_property.acc = generalAccount;
        customerAB_property.bGeneral = general;

        Constructor constructor = new Constructor();
        Representer representer = new Representer();

        Tag generalAccountTag = new Tag("!GA");
        constructor
                .addTypeDescription(new TypeDescription(GeneralAccount.class, generalAccountTag));
        representer.addClassTag(GeneralAccount.class, generalAccountTag);

        Yaml yaml = new Yaml(constructor, representer);
        String dump = yaml.dump(customerAB_property);
        // System.out.println(dump);
        CustomerAB_Property parsed = (CustomerAB_Property) yaml.load(dump);
        assertNotNull(parsed);
    }

    public void testABwithJavaBeanHelpers() {
        SuperSaverAccount supersaver = new SuperSaverAccount();
        GeneralAccount generalAccount = new GeneralAccount();

        CustomerAB customerAB = new CustomerAB();
        ArrayList<Account> all = new ArrayList<Account>();
        all.add(supersaver);
        all.add(generalAccount);
        ArrayList<GeneralAccount> general = new ArrayList<GeneralAccount>();
        general.add(generalAccount);
        general.add(supersaver);

        customerAB.aAll = all;
        customerAB.bGeneral = general;

        Yaml yaml = new Yaml();
        String dump2 = yaml.dumpAsMap(customerAB);
        // System.out.println(dump2);
        Yaml loader = new Yaml();
        CustomerAB parsed = loader.loadAs(dump2, CustomerAB.class);
        assertNotNull(parsed);
    }

    public void testAB_asMapValue() {
        SuperSaverAccount supersaver = new SuperSaverAccount();
        GeneralAccount generalAccount = new GeneralAccount();

        CustomerAB_MapValue customerAB_mapValue = new CustomerAB_MapValue();
        ArrayList<Account> all = new ArrayList<Account>();
        all.add(supersaver);
        all.add(generalAccount);
        Map<String, GeneralAccount> generalMap = new HashMap<String, GeneralAccount>();
        generalMap.put(generalAccount.name, generalAccount);
        generalMap.put(supersaver.name, supersaver);

        customerAB_mapValue.aAll = all;
        customerAB_mapValue.bGeneralMap = generalMap;

        Yaml yaml = new Yaml();
        String dump = yaml.dump(customerAB_mapValue);
        // System.out.println(dump);
        CustomerAB_MapValue parsed = (CustomerAB_MapValue) yaml.load(dump);
        assertNotNull(parsed);
    }

    public void testAB_asMapKey() {
        SuperSaverAccount supersaver = new SuperSaverAccount();
        GeneralAccount generalAccount = new GeneralAccount();

        CustomerAB_MapKey customerAB_mapKey = new CustomerAB_MapKey();
        ArrayList<Account> all = new ArrayList<Account>();
        all.add(supersaver);
        all.add(generalAccount);
        Map<GeneralAccount, String> generalMap = new HashMap<GeneralAccount, String>();
        generalMap.put(generalAccount, generalAccount.name);
        generalMap.put(supersaver, supersaver.name);

        customerAB_mapKey.aAll = all;
        customerAB_mapKey.bGeneralMap = generalMap;

        Yaml yaml = new Yaml();
        String dump = yaml.dump(customerAB_mapKey);
        // System.out.println(dump);
        CustomerAB_MapKey parsed = (CustomerAB_MapKey) yaml.load(dump);
        assertNotNull(parsed);
    }

    public void testBA() {
        SuperSaverAccount supersaver = new SuperSaverAccount();
        GeneralAccount generalAccount = new GeneralAccount();

        CustomerBA customerBA = new CustomerBA();
        ArrayList<Account> all = new ArrayList<Account>();
        all.add(supersaver);
        all.add(generalAccount);
        ArrayList<GeneralAccount> general = new ArrayList<GeneralAccount>();
        general.add(generalAccount);
        general.add(supersaver);

        customerBA.aGeneral = general;
        customerBA.bAll = all;

        Yaml yaml = new Yaml();
        String dump = yaml.dump(customerBA);
        // System.out.println(dump);
        //
        CustomerBA parsed = (CustomerBA) yaml.load(dump);
        assertEquals(2, parsed.bAll.size());
        assertEquals(2, parsed.aGeneral.size());
        assertFalse(parsed.bAll.equals(parsed.aGeneral));
        GeneralAccount[] array = parsed.aGeneral.toArray(new GeneralAccount[2]);
        assertEquals(GeneralAccount.class, array[0].getClass());
        assertEquals(SuperSaverAccount.class, array[1].getClass());
        assertEquals("SuperSaver", array[1].name);
    }
}
