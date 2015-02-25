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

import java.util.ArrayList;
import java.util.List;

public class Human3 extends AbstractHuman {

    private Human3 father;
    private Human3 mother;
    private Human3 partner;
    private Human3 bankAccountOwner;
    protected List<Human3> children;

    public Human3() {
        children = new ArrayList<Human3>();
    }

    public Human3 getFather() {
        return father;
    }

    public void setFather(Human3 father) {
        this.father = father;
    }

    public Human3 getMother() {
        return mother;
    }

    public void setMother(Human3 mother) {
        this.mother = mother;
    }

    public Human3 getPartner() {
        return partner;
    }

    public void setPartner(Human3 partner) {
        this.partner = partner;
    }

    public Human3 getBankAccountOwner() {
        return bankAccountOwner;
    }

    public void setBankAccountOwner(Human3 bankAccountOwner) {
        this.bankAccountOwner = bankAccountOwner;
    }

    public List<Human3> getChildren() {
        return children;
    }

    public void setChildren(List<Human3> children) {
        this.children = children;
    }

}
