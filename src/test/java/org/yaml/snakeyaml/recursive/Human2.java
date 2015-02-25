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

import java.util.HashMap;
import java.util.Map;

public class Human2 extends AbstractHuman {

    private Human2 father;
    private Human2 mother;
    private Human2 partner;
    private Human2 bankAccountOwner;
    protected Map<Human2, String> children;

    public Human2() {
        children = new HashMap<Human2, String>();
    }

    public Human2 getFather() {
        return father;
    }

    public void setFather(Human2 father) {
        this.father = father;
    }

    public Human2 getMother() {
        return mother;
    }

    public void setMother(Human2 mother) {
        this.mother = mother;
    }

    public Human2 getPartner() {
        return partner;
    }

    public void setPartner(Human2 partner) {
        this.partner = partner;
    }

    public Human2 getBankAccountOwner() {
        return bankAccountOwner;
    }

    public void setBankAccountOwner(Human2 bankAccountOwner) {
        this.bankAccountOwner = bankAccountOwner;
    }

    public Map<Human2, String> getChildren() {
        return children;
    }

    public void setChildren(Map<Human2, String> children) {
        this.children = children;
    }

}
