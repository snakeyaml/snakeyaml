/**
 * Copyright (c) 2008-2010, http://code.google.com/p/snakeyaml/
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

import junit.framework.TestCase;

import org.yaml.snakeyaml.JavaBeanDumper;
import org.yaml.snakeyaml.JavaBeanLoader;
import org.yaml.snakeyaml.Yaml;

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
        try {
            CustomerAB parsed = (CustomerAB) yaml.load(dump);
        } catch (Exception e) {
            // TODO fix issue 82
            e.printStackTrace();
        }
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

        JavaBeanDumper dumper = new JavaBeanDumper();
        String dump2 = dumper.dump(customerAB);
        // System.out.println(dump2);
        JavaBeanLoader<CustomerAB> loader = new JavaBeanLoader<CustomerAB>(CustomerAB.class);
        try {
            CustomerAB parsed = loader.load(dump2);
        } catch (Exception e) {
            // TODO fix issue 82
            e.printStackTrace();
        }

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
