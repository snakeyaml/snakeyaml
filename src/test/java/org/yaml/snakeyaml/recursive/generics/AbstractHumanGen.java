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
package org.yaml.snakeyaml.recursive.generics;

import java.util.Date;

public abstract class AbstractHumanGen<T, K extends AbstractHumanGen<T, ?>> {
    private String name;
    private Date birthday;
    private String birthPlace;
    private K father;
    private K mother;
    private K partner;
    private K bankAccountOwner;
    protected T children;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public K getFather() {
        return father;
    }

    public void setFather(K father) {
        this.father = father;
    }

    public K getMother() {
        return mother;
    }

    public void setMother(K mother) {
        this.mother = mother;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public T getChildren() {
        return children;
    }

    public void setChildren(T children) {
        this.children = children;
    }

    public K getPartner() {
        return partner;
    }

    public void setPartner(K partner) {
        this.partner = partner;
    }

    public K getBankAccountOwner() {
        return bankAccountOwner;
    }

    public void setBankAccountOwner(K bankAccountOwner) {
        this.bankAccountOwner = bankAccountOwner;
    }

}
