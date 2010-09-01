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

package org.yaml.snakeyaml.javabeans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.yaml.snakeyaml.Yaml;

import junit.framework.TestCase;

public class PropOrderInfluenceWhenAliasedInGenericCollectionTest extends TestCase {

    public static interface Account {
    }

    public static class GeneralAccount implements Account {
        public String name = "General";
    }

    public static class SuperSaverAccount extends GeneralAccount {        
        public String name = "SuperSaver";
    }

    public static class SecretAccount implements Account {        
        public String name = "Secret";
    }

    public static class CustomerAB {
        public Collection<Account> aAll;
        public Collection<GeneralAccount> bGeneral;
    }

    public static class CustomerBA {
        public Collection<GeneralAccount> aGeneral;
        public Collection<Account> bAll;
    }

    public void testAB() {
        SuperSaverAccount supersaver = new SuperSaverAccount();
        SecretAccount secret = new SecretAccount();
        GeneralAccount generalAccount = new GeneralAccount();
        
        CustomerAB customerAB = new CustomerAB();
        ArrayList<Account> all = new ArrayList<Account>();
        all.add(supersaver);
        all.add(secret);
        all.add(generalAccount);
        ArrayList<GeneralAccount> general = new ArrayList<GeneralAccount>();
        general.add(generalAccount);
        general.add(supersaver);
        
        customerAB.aAll = all;
        customerAB.bGeneral = general;
        
        Yaml yaml = new Yaml();
        String dump = yaml.dump(customerAB);
        //System.out.println(dump);
        
        yaml.load(dump);
        
    }

    public void testBA() {
        SuperSaverAccount supersaver = new SuperSaverAccount();
        SecretAccount secret = new SecretAccount();
        GeneralAccount generalAccount = new GeneralAccount();
        
        CustomerBA customerBA = new CustomerBA();
        ArrayList<Account> all = new ArrayList<Account>();
        all.add(supersaver);
        all.add(secret);
        all.add(generalAccount);
        ArrayList<GeneralAccount> general = new ArrayList<GeneralAccount>();
        general.add(generalAccount);
        general.add(supersaver);
        
        customerBA.aGeneral = general;
        customerBA.bAll = all;
        
        Yaml yaml = new Yaml();
        String dump = yaml.dump(customerBA);
        //System.out.println(dump);
        yaml.load(dump);
    }
}
