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

import java.util.HashSet;
import java.util.Set;

public class Human extends AbstractHuman {

    private Human father;
    private Human mother;
    private Human partner;
    private Human bankAccountOwner;
    protected Set<Human> children;

    public Human() {
        children = new HashSet<Human>();
    }

    public Human getFather() {
        return father;
    }

    public void setFather(Human father) {
        this.father = father;
    }

    public Human getMother() {
        return mother;
    }

    public void setMother(Human mother) {
        this.mother = mother;
    }

    public Human getPartner() {
        return partner;
    }

    public void setPartner(Human partner) {
        this.partner = partner;
    }

    public Human getBankAccountOwner() {
        return bankAccountOwner;
    }

    public void setBankAccountOwner(Human bankAccountOwner) {
        this.bankAccountOwner = bankAccountOwner;
    }

    public Set<Human> getChildren() {
        return children;
    }

    public void setChildren(Set<Human> children) {
        this.children = children;
    }

}
